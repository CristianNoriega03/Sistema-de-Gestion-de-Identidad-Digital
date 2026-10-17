package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import com.example.sistemaidentidaddigital.repository.CitaRepository;
import com.example.sistemaidentidaddigital.proxy.ValidacionAcceso;
import com.example.sistemaidentidaddigital.builder.CiudadanoBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class InicioController {

    private final CiudadanoRepository ciudadanoRepository;
    private final CitaRepository citaRepository;
    private final ValidacionAcceso proxySeguridad;
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias
    public InicioController(CiudadanoRepository ciudadanoRepository, CitaRepository citaRepository, ValidacionAcceso proxySeguridad, PasswordEncoder passwordEncoder) {
        this.ciudadanoRepository = ciudadanoRepository;
        this.citaRepository = citaRepository;
        this.proxySeguridad = proxySeguridad;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String inicio(Authentication authentication, Model model) {
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
            String rol = ciudadano.getRol();
            model.addAttribute("ciudadano", ciudadano);

            // 1. EL PROXY EN ACCIÓN: Decide a qué pantalla mandar a cada quien
            if (proxySeguridad.tieneAcceso(rol, "VER_TODO_SISTEMA")) {
                return "inicio_superadmin"; // El Jefe va a su panel
            } else if (proxySeguridad.tieneAcceso(rol, "VER_CITAS_GLOBALES")) {
                
                // --- NUEVO FILTRO PARA LIMPIAR LA TABLA ---
                // Obtenemos todas las citas de la base de datos pero filtramos las canceladas
                List<Cita> citasActivas = ((List<Cita>) citaRepository.findAll()).stream()
                        .filter(cita -> !cita.getEstado().equals("CANCELADA"))
                        .collect(Collectors.toList());
                
                model.addAttribute("todasLasCitas", citasActivas);
                return "inicio_admin"; // El Asesor va a su panel
            } else {
                model.addAttribute("ciudadano", ciudadano);
                return "inicio"; // El Ciudadano normal va a la vista que ya tenías
            }
        }
        return "redirect:/login";
    }

    // Método exclusivo para que el SuperAdmin cree nuevos Asesores (Admins)
    @PostMapping("/crear-asesor")
    public String crearAsesor(@RequestParam String nombre, 
                              @RequestParam String email, 
                              @RequestParam String password, 
                              Authentication authentication, 
                              RedirectAttributes redirectAttributes) {
        
        String rolActual = ciudadanoRepository.findByEmail(authentication.getName()).get().getRol();
        
        // 2. EL PROXY PROTEGIENDO LA RUTA
        if (!proxySeguridad.tieneAcceso(rolActual, "CREAR_ADMIN")) {
            System.out.println("🚨 ALERTA PROXY: Intento de acceso no autorizado bloqueado.");
            return "redirect:/?error=AccesoDenegado";
        }

        if (ciudadanoRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("mensajeError", "❌ Error: Este correo ya está registrado en el sistema.");
            return "redirect:/";
        }

        // 3. Usamos tu patrón Builder para crear al asesor
        Ciudadano nuevoAsesor = new CiudadanoBuilder()
                .conNombre(nombre)
                .conApellido("Asesor")
                .conDocumento("ADMIN-" + System.currentTimeMillis()) // Cédula interna automática
                .conEmail(email)
                .conPassword(passwordEncoder.encode(password))
                .conRol("ADMIN")
                .build();

        ciudadanoRepository.save(nuevoAsesor);
        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Asesor corporativo creado correctamente.");
        
        return "redirect:/";
    }
}