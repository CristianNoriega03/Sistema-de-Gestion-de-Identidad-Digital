package com.example.sistemaidentidaddigital.chain;

import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.ui.Model;

public class ValidadorEmail extends ValidadorRegistro {
    @Override
    public boolean validar(String password, String confirmarPassword, String email, String documento, Model model, CiudadanoRepository repo) {
        if (repo.existsByEmail(email)) {
            model.addAttribute("error", "El correo ya está registrado");
            return false; // Rompe la cadena
        }
        return verificarSiguiente(password, confirmarPassword, email, documento, model, repo);
    }
}