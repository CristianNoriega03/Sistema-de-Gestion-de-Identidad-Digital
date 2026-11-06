package com.example.sistemaidentidaddigital.iterator;

import com.example.sistemaidentidaddigital.model.Cita;

public interface IteradorCitas {
    boolean tieneSiguiente();
    Cita siguiente();
}