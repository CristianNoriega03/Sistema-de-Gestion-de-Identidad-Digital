package com.example.sistemaidentidaddigital.command;

public interface Command {
    void ejecutar();
    // Preparando el terreno para futuras funcionalidades de la Registraduría
    void deshacer(); 
}