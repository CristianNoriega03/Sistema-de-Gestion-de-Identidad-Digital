package com.example.sistemaidentidaddigital.composite;

public class RequisitoPago implements RequisitoTramite {
    private boolean pagoAprobado;

    public RequisitoPago(boolean pagoAprobado) {
        this.pagoAprobado = pagoAprobado;
    }

    @Override
    public boolean validar() {
        // Retorna true si el usuario simuló el pago exitosamente
        return this.pagoAprobado;
    }
}