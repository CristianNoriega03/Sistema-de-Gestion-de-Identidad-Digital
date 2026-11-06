package com.example.sistemaidentidaddigital.iterator;

import com.example.sistemaidentidaddigital.model.Cita;
import java.util.List;

public class IteradorCitasActivas implements IteradorCitas {
    
    private List<Cita> citas;
    private int posicion = 0;

    public IteradorCitasActivas(List<Cita> citas) {
        this.citas = citas;
    }

    @Override
    public boolean tieneSiguiente() {
        // Magia del patrón: Avanza silenciosamente si encuentra citas canceladas
        while (posicion < citas.size() && citas.get(posicion).getEstado().equals("CANCELADA")) {
            System.out.println("Iterator actuando: Saltando cita cancelada.....");
            posicion++;
        }
        return posicion < citas.size();
    }

    @Override
    public Cita siguiente() {
        if (tieneSiguiente()) {
            Cita cita = citas.get(posicion);
            posicion++;
            return cita;
        }
        return null;
    }
}