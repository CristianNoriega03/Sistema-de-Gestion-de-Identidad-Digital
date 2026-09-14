package com.example.sistemaidentidaddigital.builder;

import com.example.sistemaidentidaddigital.model.Ciudadano;

public class CiudadanoBuilder {
    private String nombre;
    private String apellido;
    private String documento;
    private String fechaExpedicion; // Nuevo campo
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String password;

    public CiudadanoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CiudadanoBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public CiudadanoBuilder conDocumento(String documento) {
        this.documento = documento;
        return this;
    }

    public CiudadanoBuilder conFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
        return this;
    }

    public CiudadanoBuilder conFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public CiudadanoBuilder conTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public CiudadanoBuilder conEmail(String email) {
        this.email = email;
        return this;
    }

    public CiudadanoBuilder conPassword(String password) {
        this.password = password;
        return this;
    }

    public Ciudadano build() {
        Ciudadano ciudadano = new Ciudadano();
        ciudadano.setNombre(this.nombre);
        ciudadano.setApellido(this.apellido);
        ciudadano.setDocumento(this.documento);
        ciudadano.setFechaExpedicion(this.fechaExpedicion); 
        ciudadano.setFechaNacimiento(this.fechaNacimiento);
        ciudadano.setTelefono(this.telefono);
        ciudadano.setEmail(this.email);
        ciudadano.setPassword(this.password);
        return ciudadano;
    }
}