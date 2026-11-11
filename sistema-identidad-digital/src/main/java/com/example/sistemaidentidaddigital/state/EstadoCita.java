package com.example.sistemaidentidaddigital.state;

import com.example.sistemaidentidaddigital.model.Cita;

// Interfaz base: Define las acciones que TODO estado debe saber procesar.
public interface EstadoCita {
    void cancelar(Cita cita);
    void finalizar(Cita cita);
}