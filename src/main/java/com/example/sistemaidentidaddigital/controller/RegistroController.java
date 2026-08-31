package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.service.CiudadanoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    private final CiudadanoService ciudadanoService;

    public RegistroController(CiudadanoService ciudadanoService) {
        this.ciudadanoService = ciudadanoService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            @RequestParam String fechaNacimiento,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmarPassword,
            RedirectAttributes redirectAttributes) {

        String error = ciudadanoService.registrarCiudadano(
                nombre,
                apellido,
                documento,
                fechaNacimiento,
                telefono,
                email,
                password,
                confirmarPassword
        );

        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
            return "redirect:/registro";
        }

        return "redirect:/login";
    }
}