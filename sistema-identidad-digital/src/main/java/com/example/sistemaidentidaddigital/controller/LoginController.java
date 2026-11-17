package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import com.example.sistemaidentidaddigital.strategy.ContextoAutenticacion2FA;
import com.example.sistemaidentidaddigital.strategy.AutenticacionSMS;
import com.example.sistemaidentidaddigital.strategy.AutenticacionEmail;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Random;

@Controller
public class LoginController {

    @Autowired
    private CiudadanoRepository ciudadanoRepository;

    @GetMapping("/login")
    public String login(HttpSession session, Model model) {
        Object notificacion = session.getAttribute("notificacionFlotante");
        if (notificacion != null) {
            model.addAttribute("notificacionFlotante", notificacion);
            session.removeAttribute("notificacionFlotante");
        }
        return "login";
    }

    // --- 1. PANTALLA INTERMEDIA PARA ELEGIR MÉTODO (STRATEGY) ---
    @GetMapping("/dos-pasos")
    public String mostrarDosPasos(Authentication authentication, Model model) {
        String emailAutenticado = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(emailAutenticado);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
            
            // Censurar Teléfono
            String tel = ciudadano.getTelefono();
            String telCensurado = (tel != null && tel.length() >= 4) ? "******" + tel.substring(tel.length() - 4) : "******";

            // Censurar Email
            String email = ciudadano.getEmail();
            String emailCensurado = email;
            if (email != null && email.contains("@")) {
                String[] partes = email.split("@");
                if (partes[0].length() > 2) {
                    emailCensurado = partes[0].charAt(0) + "***" + partes[0].charAt(partes[0].length()-1) + "@" + partes[1];
                } else {
                    emailCensurado = "***@" + partes[1];
                }
            }

            model.addAttribute("telefonoCensurado", telCensurado);
            model.addAttribute("emailCensurado", emailCensurado);
            model.addAttribute("telefonoReal", tel);
            model.addAttribute("emailReal", email);
        }

        return "dos_pasos"; 
    }

    // --- 2. EJECUCIÓN DEL PATRÓN STRATEGY Y ENVÍO DEL CÓDIGO ---
    @PostMapping("/login/enviar-codigo")
    public String enviarCodigo2FA(@RequestParam String metodoElegido, 
                                  @RequestParam String destinoReal, 
                                  HttpSession session) {
        
        String codigoGenerado = String.format("%04d", new Random().nextInt(10000));
        session.setAttribute("codigo2FA", codigoGenerado); 

        ContextoAutenticacion2FA contexto2FA = new ContextoAutenticacion2FA();

        if (metodoElegido.equalsIgnoreCase("SMS")) {
            contexto2FA.setEstrategia(new AutenticacionSMS());
        } else {
            contexto2FA.setEstrategia(new AutenticacionEmail());
        }

        contexto2FA.ejecutarEnvio(destinoReal, codigoGenerado);

        // ¡Corrección! Ahora lo mandamos a escribir el código, no al inicio
        return "redirect:/login/verificar-codigo"; 
    }

    // --- 3. MOSTRAR PANTALLA PARA DIGITAR EL CÓDIGO ---
    @GetMapping("/login/verificar-codigo")
    public String mostrarVerificacionCodigo() {
        return "verificar_codigo";
    }

    // --- 4. VALIDAR EL CÓDIGO Y DEJAR ENTRAR AL SISTEMA ---
    @PostMapping("/login/validar-codigo")
    public String validarCodigo(@RequestParam String codigoIngresado, HttpSession session) {
        String codigoGuardado = (String) session.getAttribute("codigo2FA");

        if (codigoGuardado != null && codigoGuardado.equals(codigoIngresado)) {
            session.removeAttribute("codigo2FA"); // Limpiamos seguridad
            return "redirect:/"; // ¡Código correcto! Entra al sistema
        }
        
        // Código incorrecto, lo devolvemos con error
        return "redirect:/login/verificar-codigo?error";
    }


// --- 5. CANCELAR EL 2FA Y DESTRUIR SESIÓN ---
    @GetMapping("/login/cancelar-2fa")
    public String cancelar2FA(HttpSession session) {
        // 1. Borramos la autenticación en Spring Security (Deslogueo real)
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        
        // 2. Destruimos la sesión (Esto borra el código 2FA y la notificación flotante de éxito)
        session.invalidate();
        
        // 3. Devolvemos al login limpio
        return "redirect:/login";
    }

}