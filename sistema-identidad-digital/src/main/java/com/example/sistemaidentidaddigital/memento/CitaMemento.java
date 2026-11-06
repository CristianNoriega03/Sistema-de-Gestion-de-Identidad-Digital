package com.example.sistemaidentidaddigital.memento;

public class CitaMemento {
    
    // Los datos que queremos proteger/recordar
    private final String fecha;
    private final String hora;
    private final String estado;

    public CitaMemento(String fecha, String hora, String estado) {
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Solo Getters, no hay Setters porque una fotografía del pasado no se puede alterar
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstado() { return estado; }
}