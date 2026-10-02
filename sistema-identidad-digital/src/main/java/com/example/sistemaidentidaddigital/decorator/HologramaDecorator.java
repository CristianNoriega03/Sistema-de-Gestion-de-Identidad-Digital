package com.example.sistemaidentidaddigital.decorator;

public class HologramaDecorator extends CarnetDecorator {

    public HologramaDecorator(CarnetSeguridad carnet) {
        super(carnet);
    }

    @Override
    public String obtenerSeguridad() {
        // Llama a lo que ya tenía y le suma su propio sello
        return super.obtenerSeguridad() + " | 🌟 Holograma Biométrico 3D";
    }
}