package com.example.sistemaidentidaddigital.decorator;

public class CarnetBase implements CarnetSeguridad {
    @Override
    public String obtenerSeguridad() {
        return "✅ Documento Base Estándar";
    }
}