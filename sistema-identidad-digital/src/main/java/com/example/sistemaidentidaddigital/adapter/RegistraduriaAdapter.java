package com.example.sistemaidentidaddigital.adapter;

import org.springframework.stereotype.Component;

@Component
public class RegistraduriaAdapter implements ValidadorDocumento {
    
    private final SistemaLegacyRegistraduria sistemaViejo;

    public RegistraduriaAdapter() {
        this.sistemaViejo = new SistemaLegacyRegistraduria();
    }

    @Override
    public boolean validarEstadoActivo(String cedula) {
        // 1. Obtiene el texto feo del sistema viejo
        String respuestaCruda = sistemaViejo.consultarEstado(cedula);
        
        // 2. Corta el texto separándolo por las barras "|"
        String[] partes = respuestaCruda.split("\\|");
        
        // 3. Devuelve true si el número en el medio es "1" (Activa)
        return partes[1].equals("1");
    }
}