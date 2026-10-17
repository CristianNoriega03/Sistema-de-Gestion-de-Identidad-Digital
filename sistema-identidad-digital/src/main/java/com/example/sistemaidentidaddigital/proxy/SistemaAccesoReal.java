package com.example.sistemaidentidaddigital.proxy;

import org.springframework.stereotype.Service;

// 2. El Objeto Real (El sistema sin protección)
@Service
public class SistemaAccesoReal implements ValidacionAcceso {

    @Override
    public boolean tieneAcceso(String rol, String accionSolicitada) {
        // El sistema real simplemente ejecuta la acción asumiendo que es seguro
        return true; 
    }
}