package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.adapter.ValidadorDocumento;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import com.example.sistemaidentidaddigital.proxy.ValidacionAcceso; // 1. Importamos el proxy
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class ValidacionController {

    private final ValidadorDocumento validadorAdapter;
    private final CiudadanoRepository ciudadanoRepository;
    private final ValidacionAcceso proxySeguridad; // 2. Inyectamos nuestro guardia proxy

    // Añadimos el proxy al constructor
    public ValidacionController(ValidadorDocumento validadorAdapter, CiudadanoRepository ciudadanoRepository, ValidacionAcceso proxySeguridad) {
        this.validadorAdapter = validadorAdapter;
        this.ciudadanoRepository = ciudadanoRepository;
        this.proxySeguridad = proxySeguridad;
    }

    // Clase interna para asegurar que el HTML pueda leer los datos
    public static class NotificacionDto {
        private String tipo;
        private String mensaje;

        public NotificacionDto(String tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }
        public String getTipo() { return tipo; }
        public String getMensaje() { return mensaje; }
    }

    @GetMapping("/validar-estado")
    public String validar(Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();
            
            // 🚨 BARRERA DEL PROXY: Verificamos si tiene el permiso antes de validar
            if (!proxySeguridad.tieneAcceso(ciudadano.getRol(), "VER_CARNE")) {
                System.out.println("🚨 PROXY: Bloqueando al rol " + ciudadano.getRol() + " intentando validar el estado.");
                return "redirect:/?error=AccesoDenegado";
            }

            // Usamos el Adaptador
            boolean estaActiva = validadorAdapter.validarEstadoActivo(ciudadano.getDocumento());

            if (estaActiva) {
                redirectAttributes.addFlashAttribute("notificacionFlotante", 
                    new NotificacionDto("success", "✅ Documento ACTIVO y vigente en la Registraduría Nacional."));
            } else {
                redirectAttributes.addFlashAttribute("notificacionFlotante", 
                    new NotificacionDto("error", "⚠️ Atención: El documento registra como INACTIVO en la base nacional."));
            }
        }
        
        return "redirect:/";
    }
}