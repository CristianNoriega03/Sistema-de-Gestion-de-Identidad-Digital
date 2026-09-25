package com.example.sistemaidentidaddigital.bridge;

import com.example.sistemaidentidaddigital.prototype.CarneDigital;
import org.springframework.ui.Model;

public class CarneBridge {
    // Aquí está el puente: La clase contiene la interfaz de los formatos
    protected FormatoCarne formato;

    public CarneBridge(FormatoCarne formato) {
        this.formato = formato;
    }

    public String generar(Model model, CarneDigital carne) {
        return formato.mostrarVista(model, carne);
    }
}