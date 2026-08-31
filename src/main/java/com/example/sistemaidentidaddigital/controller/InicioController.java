package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
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
            Model model) {

        String email = authentication.getName();

        Ciudadano ciudadano = ciudadanoRepository
                .findByEmail(email)
                .orElseThrow();

        model.addAttribute("nombre", ciudadano.getNombre());

        return "inicio";
    }
}