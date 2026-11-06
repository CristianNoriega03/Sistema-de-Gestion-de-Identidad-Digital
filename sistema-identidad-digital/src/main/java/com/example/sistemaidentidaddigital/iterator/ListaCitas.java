package com.example.sistemaidentidaddigital.iterator;

import com.example.sistemaidentidaddigital.model.Cita;
import java.util.List;

public class ListaCitas implements ColeccionCitas {
    
    private List<Cita> citas;

    public ListaCitas(List<Cita> citas) {
        this.citas = citas;
    }

    @Override
    public IteradorCitas crearIteradorActivas() {
        return new IteradorCitasActivas(this.citas);
    }
}