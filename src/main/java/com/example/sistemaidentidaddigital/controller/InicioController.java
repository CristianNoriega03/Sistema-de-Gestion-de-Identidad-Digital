package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    private final CiudadanoRepository ciudadanoRepository;

    public InicioController(CiudadanoRepository ciudadanoRepository) {
        this.ciudadanoRepository = ciudadanoRepository;
    }

    @GetMapping("/")
    public String inicio(
            Authentication authentication,
            Model model,
            HttpSession session) {

        //Extraer y limpiar la notificación de la sesión
        Object notificacion = session.getAttribute("notificacionFlotante");

        if (notificacion != null) {
            model.addAttribute("notificacionFlotante", notificacion);
            session.removeAttribute("notificacionFlotante");
        }

        //Cargar datos del usuario
        String email = authentication.getName();

        Ciudadano ciudadano = ciudadanoRepository
                .findByEmail(email)
                .orElseThrow();

        model.addAttribute("nombre", ciudadano.getNombre());

        return "inicio";
    }
}