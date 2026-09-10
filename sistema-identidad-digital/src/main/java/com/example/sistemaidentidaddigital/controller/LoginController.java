package com.example.sistemaidentidaddigital.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        //Verificar si hay una notificación en la sesión
        Object notificacion = session.getAttribute("notificacionFlotante");

        if (notificacion != null) {
            // Pasarla al Modelo de la vista
            model.addAttribute("notificacionFlotante", notificacion);
            // Eliminarla de la sesión para que no vuelva a salir al recargar
            session.removeAttribute("notificacionFlotante");
        }

        return "login";
    }
}