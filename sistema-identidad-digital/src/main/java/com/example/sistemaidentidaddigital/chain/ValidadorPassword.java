package com.example.sistemaidentidaddigital.chain;

import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.ui.Model;

public class ValidadorPassword extends ValidadorRegistro {
    @Override
    public boolean validar(String password, String confirmarPassword, String email, String documento, Model model, CiudadanoRepository repo) {
        if (!password.equals(confirmarPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return false; // Rompe la cadena
        }
        // Si pasa, llama al siguiente eslabón
        return verificarSiguiente(password, confirmarPassword, email, documento, model, repo);
    }
}