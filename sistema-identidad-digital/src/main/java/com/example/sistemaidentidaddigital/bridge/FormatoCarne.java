package com.example.sistemaidentidaddigital.bridge;

import com.example.sistemaidentidaddigital.prototype.CarneDigital;
import org.springframework.ui.Model;

public interface FormatoCarne {
    // Define cómo se renderizará el carné
    String mostrarVista(Model model, CarneDigital carne);
}



