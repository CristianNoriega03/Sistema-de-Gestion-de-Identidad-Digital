package com.example.sistemaidentidaddigital.prototype;

public class CarneDigital implements Cloneable {

    // 1. Datos estáticos (La plantilla del gobierno que no cambia y se clona)
    private String escudo;
    private String republica;
    private String entidad;
    private String firmasLegales;

    // 2. Datos dinámicos (Los que inyectaremos después de clonar)
    private String nombreCompleto;
    private String cedula;
    private String fechaExpedicion;

    // El constructor configura la información "pesada" una sola vez
public CarneDigital() {
        this.escudo = "/images/logo.png"; 
        this.republica = "REPÚBLICA DE COLOMBIA";
        this.entidad = "REGISTRADURÍA NACIONAL DEL ESTADO CIVIL";
        this.firmasLegales = "Este documento digital es personal, intransferible y tiene validez legal para trámites web. Firmado digitalmente por la Dirección de Identificación.";
    }

    // --- Getters para leer los datos oficiales ---
    public String getEscudo() { return escudo; }
    public String getRepublica() { return republica; }
    public String getEntidad() { return entidad; }
    public String getFirmasLegales() { return firmasLegales; }

    // --- Getters y Setters para los datos del ciudadano ---
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getFechaExpedicion() { return fechaExpedicion; }
    public void setFechaExpedicion(String fechaExpedicion) { this.fechaExpedicion = fechaExpedicion; }

    // 3. El método clave del patrón
    @Override
    public CarneDigital clone() {
        try {
            // Hace una copia exacta y rápida a nivel de memoria
            return (CarneDigital) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Error al clonar el carné base", e);
        }
    }
}