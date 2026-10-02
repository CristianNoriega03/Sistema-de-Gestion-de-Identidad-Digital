package com.example.sistemaidentidaddigital.composite;

public class RequisitoDenuncio implements RequisitoTramite {
    private boolean denuncioAceptado;

    public RequisitoDenuncio(boolean denuncioAceptado) {
        this.denuncioAceptado = denuncioAceptado;
    }

    @Override
    public boolean validar() {
        // Retorna true si el usuario marcó la casilla
        return this.denuncioAceptado;
    }
}