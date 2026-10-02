package com.example.sistemaidentidaddigital.composite;

import java.util.ArrayList;
import java.util.List;

public class PaqueteRequisitos implements RequisitoTramite {
    
    // Lista para guardar cualquier cantidad de requisitos
    private List<RequisitoTramite> listaRequisitos = new ArrayList<>();

    // Método para agregar un nuevo requisito al paquete
    public void agregarRequisito(RequisitoTramite requisito) {
        listaRequisitos.add(requisito);
    }

    @Override
    public boolean validar() {
        // Recorre todos los requisitos guardados. 
        // Si UNO solo falla, todo el paquete es inválido.
        for (RequisitoTramite req : listaRequisitos) {
            if (!req.validar()) {
                return false; 
            }
        }
        // Si termina el ciclo, es porque todos son true
        return true; 
    }
}