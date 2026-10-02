package com.example.sistemaidentidaddigital.model;

import jakarta.persistence.*;

@Entity
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fecha;
    private String hora;
    private String estado; // Aquí guardaremos "ASIGNADA" o "CANCELADA"

    // Relación: Muchos trámites de cita pueden pertenecer a un solo ciudadano
    @ManyToOne
    @JoinColumn(name = "ciudadano_id")
    private Ciudadano ciudadano;

    // --- Getters y Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Ciudadano getCiudadano() { return ciudadano; }
    public void setCiudadano(Ciudadano ciudadano) { this.ciudadano = ciudadano; }
}