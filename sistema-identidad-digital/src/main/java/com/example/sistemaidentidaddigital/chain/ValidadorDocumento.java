package com.example.sistemaidentidaddigital.chain;

import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.ui.Model;

public class ValidadorDocumento extends ValidadorRegistro {
    @Override
    public boolean validar(String password, String confirmarPassword, String email, String documento, Model model, CiudadanoRepository repo) {
        if (repo.existsByDocumento(documento)) {
            model.addAttribute("error", "El documento de identidad ya se encuentra registrado");
            return false; // Rompe la cadena
        }
        return verificarSiguiente(password, confirmarPassword, email, documento, model, repo);
    }
}