package com.example.sistemaidentidaddigital.factory;

import com.example.sistemaidentidaddigital.abstractfactory.ExitoFactory;
import com.example.sistemaidentidaddigital.abstractfactory.FalloFactory;
import com.example.sistemaidentidaddigital.abstractfactory.LogoutFactory;
import com.example.sistemaidentidaddigital.abstractfactory.LoginAbstractFactory;

public class NotificacionFactory {

    public static LoginAbstractFactory obtenerFamilia(String tipo) {
        if (tipo == null) return null;

        switch (tipo.toUpperCase()) {
            case "EXITO":
                return new ExitoFactory();
            case "FALLO":
                return new FalloFactory();
            case "LOGOUT":
                return new LogoutFactory(); 
            default:
                return null;
        }
    }
}