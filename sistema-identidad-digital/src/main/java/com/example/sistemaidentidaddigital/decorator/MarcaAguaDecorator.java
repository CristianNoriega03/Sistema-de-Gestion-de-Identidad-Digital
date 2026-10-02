package com.example.sistemaidentidaddigital.decorator;

public class MarcaAguaDecorator extends CarnetDecorator {

    public MarcaAguaDecorator(CarnetSeguridad carnet) {
        super(carnet);
    }

    @Override
    public String obtenerSeguridad() {
        // Llama a lo que ya tenía y le suma su propio sello
        return super.obtenerSeguridad() + " | 💧 Marca de Agua Digital";
    }
}