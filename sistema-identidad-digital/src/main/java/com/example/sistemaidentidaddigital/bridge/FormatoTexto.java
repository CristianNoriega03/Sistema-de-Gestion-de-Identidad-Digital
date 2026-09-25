package com.example.sistemaidentidaddigital.bridge;

import com.example.sistemaidentidaddigital.prototype.CarneDigital;
import org.springframework.ui.Model;

public class FormatoTexto implements FormatoCarne {
    @Override
    public String mostrarVista(Model model, CarneDigital carne) {
        model.addAttribute("carne", carne);
        // Retorna una nueva vista básica sin colores
        return "carne_texto"; 
    }
}