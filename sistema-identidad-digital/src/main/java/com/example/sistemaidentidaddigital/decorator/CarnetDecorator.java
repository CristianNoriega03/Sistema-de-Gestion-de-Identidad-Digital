package com.example.sistemaidentidaddigital.decorator;

public abstract class CarnetDecorator implements CarnetSeguridad {
    
    // Aquí guardamos el carné que estamos envolviendo
    protected CarnetSeguridad carnetWrappeado;

    public CarnetDecorator(CarnetSeguridad carnet) {
        this.carnetWrappeado = carnet;
    }

    @Override
    public String obtenerSeguridad() {
        return carnetWrappeado.obtenerSeguridad();
    }
}