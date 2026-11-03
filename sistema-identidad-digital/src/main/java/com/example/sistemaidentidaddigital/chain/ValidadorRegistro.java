package com.example.sistemaidentidaddigital.chain;

import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.ui.Model;

public abstract class ValidadorRegistro {
    
    // Referencia al siguiente eslabón en la cadena
    protected ValidadorRegistro siguienteValidador;

    // Método para conectar los eslabones
    public ValidadorRegistro setSiguiente(ValidadorRegistro siguienteValidador) {
        this.siguienteValidador = siguienteValidador;
        return siguienteValidador; // Retornamos el siguiente para poder encadenarlos
    }

    // El método que cada validador concreto deberá implementar
    public abstract boolean validar(String password, String confirmarPassword, String email, String documento, Model model, CiudadanoRepository repo);

    // Método auxiliar para pasar al siguiente eslabón si todo va bien
    protected boolean verificarSiguiente(String password, String confirmarPassword, String email, String documento, Model model, CiudadanoRepository repo) {
        if (siguienteValidador == null) {
            return true; // Fin de la cadena: todas las validaciones pasaron
        }
        return siguienteValidador.validar(password, confirmarPassword, email, documento, model, repo);
    }
}