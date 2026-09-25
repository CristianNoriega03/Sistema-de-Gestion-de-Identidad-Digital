package com.example.sistemaidentidaddigital.controller;

import com.example.sistemaidentidaddigital.bridge.CarneBridge;
import com.example.sistemaidentidaddigital.bridge.FormatoCarne;
import com.example.sistemaidentidaddigital.bridge.FormatoHTML;
import com.example.sistemaidentidaddigital.bridge.FormatoTexto;
import com.example.sistemaidentidaddigital.model.Ciudadano;
import com.example.sistemaidentidaddigital.prototype.CarneDigital;
import com.example.sistemaidentidaddigital.repository.CiudadanoRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CarneController {

    private final CiudadanoRepository ciudadanoRepository;
    private final CarneDigital carneBase; // plantilla en memoria

    public CarneController(CiudadanoRepository ciudadanoRepository) {
        this.ciudadanoRepository = ciudadanoRepository;
        // Instanciamos la plantilla pesada UNA SOLA VEZ cuando arranca el controlador
        this.carneBase = new CarneDigital(); 
    }

    @GetMapping("/perfil")
    public String verCarne(Authentication authentication, Model model,
                           @RequestParam(value = "diseno", defaultValue = "html") String diseno) {
        
        // 1. Obtenemos el correo del usuario logueado
        String email = authentication.getName();
        Optional<Ciudadano> ciudadanoOpt = ciudadanoRepository.findByEmail(email);

        if (ciudadanoOpt.isPresent()) {
            Ciudadano ciudadano = ciudadanoOpt.get();

            // 2. APLICACIÓN DEL PATRÓN PROTOTYPE: Clonamos la base en lugar de usar 'new'
            CarneDigital miCarne = carneBase.clone();
            
            // 3. Le inyectamos los datos específicos del ciudadano
            miCarne.setNombreCompleto(ciudadano.getNombre() + " " + ciudadano.getApellido());
            miCarne.setCedula(ciudadano.getDocumento());
            miCarne.setFechaExpedicion(ciudadano.getFechaExpedicion());

            // 4. APLICACIÓN DEL PATRÓN BRIDGE: Seleccionamos la forma visual
            FormatoCarne formatoElegido;
            if (diseno.equalsIgnoreCase("texto")) {
                formatoElegido = new FormatoTexto();
            } else {
                formatoElegido = new FormatoHTML();
            }

            // 5. Usamos el puente para generar y retornar la vista final
            CarneBridge puente = new CarneBridge(formatoElegido);
            return puente.generar(model, miCarne);
        }

        return "redirect:/";
    }
}