package com.example.sistemaidentidaddigital.proxy;

// 1. Interfaz que define la acción de validar el acceso
public interface ValidacionAcceso {
    boolean tieneAcceso(String rol, String accionSolicitada);
}