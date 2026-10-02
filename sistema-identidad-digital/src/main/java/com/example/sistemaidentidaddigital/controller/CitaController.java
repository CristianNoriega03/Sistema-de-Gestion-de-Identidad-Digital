package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.composite.PaqueteRequisitos;
import com.example.sistemaidentidaddigital.composite.RequisitoDenuncio;
import com.example.sistemaidentidaddigital.composite.RequisitoPago;
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

    public CitaController(CitaRepository citaRepository, CiudadanoRepository ciudadanoRepository) {
        this.citaRepository = citaRepository;
        this.ciudadanoRepository = ciudadanoRepository;
    }

    // 1. Mostrar la pantalla de citas
    @GetMapping("/citas")
    public String gestionarCitas(Authentication authentication, Model model, HttpSession session) {
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
            
            // Traemos las citas de este ciudadano y las mandamos a la vista
            List<Cita> misCitas = citaRepository.findByCiudadano(ciudadano);
            model.addAttribute("citas", misCitas);

            // Buscamos si alguna cita tiene estado "ASIGNADA"
            boolean tieneCitaActiva = misCitas.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
            model.addAttribute("tieneCitaActiva", tieneCitaActiva);

            // Revisamos si el usuario ya hizo el pago simulado en esta sesión
            Boolean pago = (Boolean) session.getAttribute("pagoAprobado");
            model.addAttribute("pagoAprobado", pago != null && pago);

            return "citas"; // Cargará el archivo citas.html
        }
        return "redirect:/";
    }

    // 2. Simular el pago de PSE (AHORA CON BARRERA DE SEGURIDAD)
    @PostMapping("/simular-pago")
    public String simularPago(HttpSession session, Authentication authentication, RedirectAttributes redirectAttributes) {
        
        // --- 1. Identificamos al usuario ---
        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();

        // --- 2. Verificamos si ya tiene una cita activa ---
        List<Cita> citasExistentes = citaRepository.findByCiudadano(ciudadano);
        boolean yaTieneCita = citasExistentes.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
        
        if (yaTieneCita) {
            // Si ya tiene cita, bloqueamos el pago
            redirectAttributes.addFlashAttribute("error", "❌ Transacción rechazada. No puedes realizar pagos porque ya tienes un trámite en curso.");
            return "redirect:/citas";
        }

        // --- 3. Si todo está limpio, aprobamos el pago ---
        session.setAttribute("pagoAprobado", true);
        redirectAttributes.addFlashAttribute("mensaje", "✅ Pago exitoso en PSE.");
        return "redirect:/citas";
    }

    // 3. Crear la cita (COMPOSITE + BARRERA DE SEGURIDAD)
    @PostMapping("/citas/crear")
    public String crearCita(@RequestParam String fecha,
                            @RequestParam String hora,
                            @RequestParam(required = false) boolean confirmarDenuncio,
                            HttpSession session,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {

        // --- 1. BUSCAR AL CIUDADANO ACTUAL ---
        String email = authentication.getName();
        Ciudadano ciudadano = ciudadanoRepository.findByEmail(email).get();

        // --- 2. BARRERA DE SEGURIDAD: EVITAR DOBLE CITA ---
        List<Cita> citasExistentes = citaRepository.findByCiudadano(ciudadano);
        boolean yaTieneCita = citasExistentes.stream().anyMatch(c -> c.getEstado().equals("ASIGNADA"));
        if (yaTieneCita) {
            redirectAttributes.addFlashAttribute("error", "❌ No puedes sacar otra cita porque ya tienes un trámite activo.");
            return "redirect:/citas";
        }

        // --- 3. REVISAR EL ESTADO DEL PAGO ---
        Boolean pago = (Boolean) session.getAttribute("pagoAprobado");
        boolean pagoAprobado = (pago != null && pago);

        // --- 4. APLICACIÓN DEL PATRÓN COMPOSITE ---
        PaqueteRequisitos paqueteRenovacion = new PaqueteRequisitos();
        paqueteRenovacion.agregarRequisito(new RequisitoDenuncio(confirmarDenuncio));
        paqueteRenovacion.agregarRequisito(new RequisitoPago(pagoAprobado));

        // Validamos TODOS los requisitos con una sola línea
        if (!paqueteRenovacion.validar()) {
            redirectAttributes.addFlashAttribute("error", "❌ Faltan requisitos. Asegúrese de marcar el denuncio y realizar el pago.");
            return "redirect:/citas";
        }
        // ---------------------------------------

        // --- 5. SI PASA LA VALIDACIÓN, GUARDAMOS LA CITA ---
        Cita nuevaCita = new Cita();
        nuevaCita.setFecha(fecha);
        nuevaCita.setHora(hora);
        nuevaCita.setEstado("ASIGNADA"); // Estado inicial
        nuevaCita.setCiudadano(ciudadano);

        citaRepository.save(nuevaCita);

        // Limpiamos la variable del pago para que no pueda pedir otra cita gratis
        session.removeAttribute("pagoAprobado");
        redirectAttributes.addFlashAttribute("mensaje", "📅 Cita asignada correctamente.");

        return "redirect:/citas";
    }
    
    // 4. Cancelar la cita
    @PostMapping("/citas/cancelar/{id}")
    public String cancelarCita(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Cita> citaOpt = citaRepository.findById(id);
        if (citaOpt.isPresent()) {
            Cita cita = citaOpt.get();
            cita.setEstado("CANCELADA"); // Cambiamos el estado, no borramos el registro
            citaRepository.save(cita);
            redirectAttributes.addFlashAttribute("mensaje", "⚠️ Cita cancelada con éxito, Tu pago sera devuelto al medio de pago seleccionado.");
        }
        return "redirect:/citas";
    }
}