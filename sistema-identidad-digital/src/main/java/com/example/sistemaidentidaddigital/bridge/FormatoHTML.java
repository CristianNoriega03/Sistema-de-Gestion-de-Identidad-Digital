package com.example.sistemaidentidaddigital.bridge;

import com.example.sistemaidentidaddigital.prototype.CarneDigital;
import org.springframework.ui.Model;

public class FormatoHTML implements FormatoCarne {
    @Override
    public String mostrarVista(Model model, CarneDigital carne) {
        model.addAttribute("carne", carne);
        // Retorna la vista que ya tenías diseñada
        return "carne_digital"; 
    }
}