package com.example.sistemaidentidaddigital.flyweight;

import java.util.List;

// 1. Interfaz Flyweight
public interface RolPermiso {
    String getNombreRol();
    List<String> getPermisos();
    void mostrarPermisos();
}