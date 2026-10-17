package com.example.sistemaidentidaddigital.flyweight;

import java.util.List;

// 2. Estado intrínseco (compartido y de solo lectura)
public class RolConcreto implements RolPermiso {
    private final String nombreRol;
    private final List<String> permisos;

    // Se asignan una sola vez al crearlo
    public RolConcreto(String nombreRol, List<String> permisos) {
        this.nombreRol = nombreRol;
        this.permisos = permisos;
    }

    @Override
    public String getNombreRol() {
        return nombreRol;
    }

    @Override
    public List<String> getPermisos() {
        return permisos;
    }

    @Override
    public void mostrarPermisos() {
        System.out.println("Rol: " + nombreRol + " | Permisos: " + permisos);
    }
}