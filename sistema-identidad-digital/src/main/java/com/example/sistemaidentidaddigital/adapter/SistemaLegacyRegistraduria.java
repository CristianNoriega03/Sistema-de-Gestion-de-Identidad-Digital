package com.example.sistemaidentidaddigital.adapter;

import java.util.Arrays;
import java.util.List;

public class SistemaLegacyRegistraduria {
    
    // Lista negra: Cédulas de prueba que saldrán como INACTIVAS SIMULADAS
    private final List<String> cedulasCanceladas = Arrays.asList("0000000000", "1111111111", "22222222", "1005290605");

    public String consultarEstado(String doc) {
        // Simulamos la respuesta de un sistema viejo en texto plano
        if (cedulasCanceladas.contains(doc)) {
            return doc + "|0|INACTIVA"; 
        } else {
            return doc + "|1|ACTIVA";
        }
    }
}