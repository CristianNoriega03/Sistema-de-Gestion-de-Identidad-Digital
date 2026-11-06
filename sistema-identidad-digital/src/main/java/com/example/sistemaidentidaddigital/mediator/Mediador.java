package com.example.sistemaidentidaddigital.mediator;

import jakarta.servlet.http.HttpSession;

public interface Mediador {
    // La torre de control recibe el evento y decide a quién llamar
    void notificarEvento(String evento, String email, HttpSession session);
}