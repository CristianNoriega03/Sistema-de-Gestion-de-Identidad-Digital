package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.command.Command;
import com.example.sistemaidentidaddigital.command.CancelarCitaCommand;
import com.example.sistemaidentidaddigital.command.GestorComandos;
import com.example.sistemaidentidaddigital.facade.RespuestaFacade;
import com.example.sistemaidentidaddigital.facade.TramiteCitasFacade;
import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CitaRepository;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import com.example.sistemaidentidaddigital.proxy.ValidacionAcceso;
import com.example.sistemaidentidaddigital.observer.GestorNotificaciones; // Importamos el Observer

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
public class CitaController {

    private final CitaRepository citaRepository;
    private final CiudadanoRepository ciudadanoRepository;
    private final TramiteCitasFacade tramiteCitasFacade;
    private final ValidacionAcceso proxySeguridad;
    private final GestorComandos gestorComandos;
    private final GestorNotificaciones notificador; // Inyectamos el Observer

    public CitaController(CitaRepository citaRepository, CiudadanoRepository ciudadanoRepository, 
                          TramiteCitasFacade tramiteCitasFacade, ValidacionAcceso proxySeguridad,
                          GestorComandos gestorComandos, GestorNotificaciones notificador) {
        this.citaRepository = citaRepository;
        this.ciudadanoRepository = ciudadanoRepository;
        this.tramiteCitasFacade = tramiteCitasFacade;
        this.proxySeguridad = proxySeguridad;
        this.gestorComandos = gestorComandos;
        this.notificador = notificador; // Lo guardamos
    }

    @GetMapping("/citas")
    public String gestionarCitas(Authentication authentication, Model model, HttpSession session) {
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();

            if (!proxySeguridad.tieneAcceso(ciudadano.getRol(), "VER_MIS_CITAS")) {
                return "redirect:/?error=AccesoDenegado";
            }

            List<Cita> misCitas = citaRepository.findByCiudadano(ciudadano);
            model.addAttribute("citas", misCitas);

            boolean tieneCitaActiva = misCitas.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
            model.addAttribute("tieneCitaActiva", tieneCitaActiva);

            Boolean pago = (Boolean) session.getAttribute("pagoAprobado");
            model.addAttribute("pagoAprobado", pago != null && pago);

            return "citas";
        }
        return "redirect:/";
    }

    @PostMapping("/simular-pago")
    public String simularPago(HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();

        if (!proxySeguridad.tieneAcceso(ciudadano.getRol(), "CREAR_CITA")) {
            return "redirect:/?error=AccesoDenegado";
        }

        List<Cita> citasExistentes = citaRepository.findByCiudadano(ciudadano);
        boolean yaTieneCita = citasExistentes.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
        
        if (yaTieneCita) {
            redirectAttributes.addFlashAttribute("error", "❌ Transacción rechazada. Ya tienes un trámite en curso.");
            return "redirect:/citas";
        }

        session.setAttribute("pagoAprobado", true);
        redirectAttributes.addFlashAttribute("mensaje", "✅ Pago exitoso en PSE.");
        return "redirect:/citas";
    }

    @PostMapping("/citas/crear")
    public String crearCita(@RequestParam String fecha, @RequestParam String hora,
                            @RequestParam(required = false) boolean confirmarDenuncio,
                            HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();

        if (!proxySeguridad.tieneAcceso(ciudadano.getRol(), "CREAR_CITA")) {
            return "redirect:/?error=AccesoDenegado";
        }

        Boolean pago = (Boolean) session.getAttribute("pagoAprobado");
        boolean pagoAprobado = (pago != null && pago);

        RespuestaFacade respuesta = tramiteCitasFacade.agendarNuevaCita(email, fecha, hora, confirmarDenuncio, pagoAprobado);

        if (respuesta.isExito()) {
            session.removeAttribute("pagoAprobado");
            redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
        } else {
            redirectAttributes.addFlashAttribute("error", respuesta.getMensaje());
        }
        return "redirect:/citas";
    }
    
    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();
        
        if (!proxySeguridad.tieneAcceso(ciudadano.getRol(), "VER_MIS_CITAS")) {
            return "redirect:/?error=AccesoDenegado";
        }

        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            try {
                Command cancelarCmd = new CancelarCitaCommand(cita, citaRepository);
                gestorComandos.ejecutarComando(cancelarCmd);
                
                // --- PATRÓN OBSERVER EN ACCIÓN ---
                notificador.notificarCambio(cita, "CANCELADA");

                redirectAttributes.addFlashAttribute("mensaje", "⚠️ Cita cancelada. Tu pago será devuelto.");
            } catch (IllegalStateException e) {
                redirectAttributes.addFlashAttribute("error", "❌ Acción denegada: " + e.getMessage());
            }
        }
        return "redirect:/citas";
    }

    @PostMapping("/admin/citas/cancelar/{id}")
    public String cancelarCitaAdmin(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano administrador = ciudadanoRepository.findByEmail(email).get();

        if (!proxySeguridad.tieneAcceso(administrador.getRol(), "CANCELAR_CUALQUIER_CITA")) {
            return "redirect:/?error=AccesoDenegado";
        }

        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            try {
                Command cancelarCmd = new CancelarCitaCommand(cita, citaRepository);
                gestorComandos.ejecutarComando(cancelarCmd);
                
                // --- PATRÓN OBSERVER EN ACCIÓN ---
                notificador.notificarCambio(cita, "CANCELADA por el Administrador");

                redirectAttributes.addFlashAttribute("mensajeExito", "✅ Cita cancelada exitosamente.");
            } catch (IllegalStateException e) {
                redirectAttributes.addFlashAttribute("mensajeError", "❌ Acción denegada: " + e.getMessage());
            }
        }
        return "redirect:/";
    }

    @PostMapping("/admin/citas/deshacer")
    public String deshacerCancelacionAdmin(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano administrador = ciudadanoRepository.findByEmail(email).get();

        if (!proxySeguridad.tieneAcceso(administrador.getRol(), "CANCELAR_CUALQUIER_CITA")) {
            return "redirect:/?error=AccesoDenegado";
        }

        if (gestorComandos.deshacerUltimoComando()) {
            redirectAttributes.addFlashAttribute("mensajeExito", "🔄 Acción deshecha: La cita ha sido restaurada.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "❌ No hay acciones para deshacer.");
        }
        return "redirect:/";
    }

    @GetMapping("/admin/citas/finalizar/{id}")
    public String finalizarCitaAdmin(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano administrador = ciudadanoRepository.findByEmail(email).get();

        if (!proxySeguridad.tieneAcceso(administrador.getRol(), "CANCELAR_CUALQUIER_CITA")) {
            return "redirect:/?error=AccesoDenegado";
        }

        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            try {
                // 1. Patrón State (Valida y finaliza)
                cita.procesarFinalizacion(); 
                citaRepository.save(cita);
                
                // 2. Patrón Observer (Notifica)
                notificador.notificarCambio(cita, "FINALIZADA");

                // 3. Patrón Visitor (Genera el reporte HTML de la cita)
                com.example.sistemaidentidaddigital.visitor.VisitorCita visitanteHtml = 
                    new com.example.sistemaidentidaddigital.visitor.ReporteAuditoriaHtmlVisitor();
                cita.aceptar(visitanteHtml);

                redirectAttributes.addFlashAttribute("mensajeExito", "✅ Cita atendida con éxito. Reporte generado en el servidor.");
            } catch (IllegalStateException e) {
                redirectAttributes.addFlashAttribute("mensajeError", "❌ Acción denegada: " + e.getMessage());
            }
        }
        return "redirect:/";
    }

    
}