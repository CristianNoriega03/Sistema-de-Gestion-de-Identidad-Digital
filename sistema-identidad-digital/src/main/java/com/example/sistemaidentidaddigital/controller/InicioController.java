package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.model.Cita;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import com.example.sistemaidentidaddigital.repository.CitaRepository;
import com.example.sistemaidentidaddigital.proxy.ValidacionAcceso;
import com.example.sistemaidentidaddigital.builder.CiudadanoBuilder;
import com.example.sistemaidentidaddigital.command.GestorComandos;

import com.example.sistemaidentidaddigital.iterator.ColeccionCitas;
import com.example.sistemaidentidaddigital.iterator.IteradorCitas;
import com.example.sistemaidentidaddigital.iterator.ListaCitas;

import jakarta.servlet.http.HttpSession; 
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class InicioController {

    private final CiudadanoRepository ciudadanoRepository;
    private final CitaRepository citaRepository;
    private final ValidacionAcceso proxySeguridad;
    private final PasswordEncoder passwordEncoder;
    private final GestorComandos gestorComandos; 
    
    // Inyección de dependencias con el GestorComandos
    public InicioController(CiudadanoRepository ciudadanoRepository, CitaRepository citaRepository, ValidacionAcceso proxySeguridad, PasswordEncoder passwordEncoder, GestorComandos gestorComandos) {
        this.ciudadanoRepository = ciudadanoRepository;
        this.citaRepository = citaRepository;
        this.proxySeguridad = proxySeguridad;
        this.passwordEncoder = passwordEncoder;
        this.gestorComandos = gestorComandos; 
    }

    @GetMapping("/")
    public String inicio(Authentication authentication, Model model, HttpSession session) {
        
        // --- NUEVO: Rescatar la notificación del Factory Method desde la sesión ---
        Object alerta = session.getAttribute("notificacionFlotante");
        if (alerta != null) {
            model.addAttribute("notificacionFlotante", alerta); 
            session.removeAttribute("notificacionFlotante"); 
        }
        // -------------------------------------------------------------------------

        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
            String rol = ciudadano.getRol();
            model.addAttribute("ciudadano", ciudadano);

            // 1. EL PROXY EN ACCIÓN
            if (proxySeguridad.tieneAcceso(rol, "VER_TODO_SISTEMA")) {
                return "inicio_superadmin"; 
            } else if (proxySeguridad.tieneAcceso(rol, "VER_CITAS_GLOBALES")) {
                
                // --- 2. PATRÓN ITERATOR EN ACCIÓN ---
                // Obtenemos todas las citas crudas de la BD
                List<Cita> todasLasCitas = (List<Cita>) citaRepository.findAll();
                
                // Las metemos en nuestra colección
                ColeccionCitas coleccion = new ListaCitas(todasLasCitas);
                IteradorCitas iterador = coleccion.crearIteradorActivas();
                
                // El iterador filtra las canceladas automáticamente al recorrerlas
                List<Cita> citasActivas = new ArrayList<>();
                while (iterador.tieneSiguiente()) {
                    citasActivas.add(iterador.siguiente());
                }
                
                model.addAttribute("todasLasCitas", citasActivas);
                
                // LÍNEA CLAVE PARA EL COMMAND Y EL BOTÓN DESHACER
                model.addAttribute("hayComandosParaDeshacer", gestorComandos.hayComandosParaDeshacer());
                
                return "inicio_admin"; 
            } else {
                model.addAttribute("ciudadano", ciudadano);
                return "inicio"; 
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/crear-asesor")
    public String crearAsesor(@RequestParam String nombre, 
                              @RequestParam String email, 
                              @RequestParam String password, 
                              Authentication authentication, 
                              RedirectAttributes redirectAttributes) {
        
        String rolActual = ciudadanoRepository.findByEmail(authentication.getName()).get().getRol();
        
        if (!proxySeguridad.tieneAcceso(rolActual, "CREAR_ADMIN")) {
            return "redirect:/?error=AccesoDenegado";
        }

        if (ciudadanoRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("mensajeError", "❌ Error: Este correo ya está registrado en el sistema.");
            return "redirect:/";
        }

        Ciudadano nuevoAsesor = new CiudadanoBuilder()
                .conNombre(nombre)
                .conApellido("Asesor")
                .conDocumento("ADMIN-" + System.currentTimeMillis()) 
                .conEmail(email)
                .conPassword(passwordEncoder.encode(password))
                .conRol("ADMIN")
                .build();

        ciudadanoRepository.save(nuevoAsesor);
        redirectAttributes.addFlashAttribute("mensajeExito", "✅ Asesor corporativo creado correctamente.");
        
        return "redirect:/";
    }
}