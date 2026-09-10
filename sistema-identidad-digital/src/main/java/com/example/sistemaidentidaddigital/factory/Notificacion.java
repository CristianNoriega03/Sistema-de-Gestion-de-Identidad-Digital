package com.example.sistemaidentidaddigital.factory;

public interface Notificacion {
    String getTipo();    // Tipos "success", "warning", "info"
    String getMensaje(); // Texto que se mostrará
}