package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.facade.RespuestaFacade;
import com.example.sistemaidentidaddigital.facade.TramiteCitasFacade;
import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CitaRepository;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
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
    private final TramiteCitasFacade tramiteCitasFacade; // Inyectamos la fachada

    public CitaController(CitaRepository citaRepository, CiudadanoRepository ciudadanoRepository, TramiteCitasFacade tramiteCitasFacade) {
        this.citaRepository = citaRepository;
        this.ciudadanoRepository = ciudadanoRepository;
        this.tramiteCitasFacade = tramiteCitasFacade;
    }

    // 1. Mostrar la pantalla de citas 
    @GetMapping("/citas")
    public String gestionarCitas(Authentication authentication, Model model, HttpSession session) {
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
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

    // 2. Simular el pago de PSE 
    @PostMapping("/simular-pago")
    public String simularPago(HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();

        List<Cita> citasExistentes = citaRepository.findByCiudadano(ciudadano);
        boolean yaTieneCita = citasExistentes.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
        
        if (yaTieneCita) {
            redirectAttributes.addFlashAttribute("error", "❌ Transacción rechazada. No puedes realizar pagos porque ya tienes un trámite en curso.");
            return "redirect:/citas";
        }

        session.setAttribute("pagoAprobado", true);
        redirectAttributes.addFlashAttribute("mensaje", "✅ Pago exitoso en PSE.");
        return "redirect:/citas";
    }

    // 3. Crear la cita (QUEDA MAS LIMPIO GRACIAS AL FACADE)
    @PostMapping("/citas/crear")
    public String crearCita(@RequestParam String fecha,
                            @RequestParam String hora,
                            @RequestParam(required = false) boolean confirmarDenuncio,
                            HttpSession session,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        Boolean pago = (Boolean) session.getAttribute("pagoAprobado");
        boolean pagoAprobado = (pago != null && pago);

        // APLICACIÓN DEL PATRÓN FACADE: El controlador solo recoge datos y se los lanza a la fachada
        RespuestaFacade respuesta = tramiteCitasFacade.agendarNuevaCita(email, fecha, hora, confirmarDenuncio, pagoAprobado);

        // Evaluamos la respuesta de la fachada
        if (respuesta.isExito()) {
            session.removeAttribute("pagoAprobado"); // Limpiamos el pago si fue exitoso
            redirectAttributes.addFlashAttribute("mensaje", respuesta.getMensaje());
        } else {
            redirectAttributes.addFlashAttribute("error", respuesta.getMensaje());
        }

        return "redirect:/citas";
    }
    
    // 4. Cancelar la cita 
    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado("CANCELADA");
            citaRepository.save(cita);
            redirectAttributes.addFlashAttribute("mensaje", "⚠️ Cita cancelada con éxito, Tu pago sera devuelto al medio de pago seleccionado.");
        }
        return "redirect:/citas";
    }
}