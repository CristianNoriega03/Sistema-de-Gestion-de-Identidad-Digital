package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.builder.CiudadanoBuilder;
import com.example.sistemaidentidaddigital.chain.ValidadorDocumento;
import com.example.sistemaidentidaddigital.chain.ValidadorEmail;
import com.example.sistemaidentidaddigital.chain.ValidadorPassword;
import com.example.sistemaidentidaddigital.chain.ValidadorRegistro;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    // 1. Variables declaradas como final 
    private final CiudadanoRepository ciudadanoRepository;
    private final PasswordEncoder passwordEncoder;

    // 2. Inyección de dependencias mediante el constructor
    public RegistroController(CiudadanoRepository ciudadanoRepository, PasswordEncoder passwordEncoder) {
        this.ciudadanoRepository = ciudadanoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro"; // Retorna la vista HTML
    }

@PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String documento,
            @RequestParam String fechaExpedicion,
            @RequestParam String fechaNacimiento,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmarPassword,
            Model model) {

        // 1. APLICACIÓN DEL PATRÓN CHAIN OF RESPONSIBILITY
        // Ensamblamos la cadena de validaciones
        ValidadorRegistro cadenaValidacion = new ValidadorPassword();
        cadenaValidacion.setSiguiente(new ValidadorEmail())
                        .setSiguiente(new ValidadorDocumento());

        // Ejecutamos la cadena
        boolean esValido = cadenaValidacion.validar(password, confirmarPassword, email, documento, model, ciudadanoRepository);
        
        if (!esValido) {
            return "registro"; // Devuelve la vista con el error correspondiente si la cadena se rompe
        }

        // 2. APLICACIÓN DEL PATRÓN BUILDER 
        Ciudadano nuevoCiudadano = new CiudadanoBuilder()
                .conNombre(nombre)
                .conApellido(apellido)
                .conDocumento(documento)
                .conFechaExpedicion(fechaExpedicion)
                .conFechaNacimiento(fechaNacimiento)
                .conTelefono(telefono)
                .conEmail(email)
                .conPassword(passwordEncoder.encode(password))
                .build();

        // 3. Guardado en base de datos
        ciudadanoRepository.save(nuevoCiudadano);

        return "redirect:/login?registroExitoso";
    }
}