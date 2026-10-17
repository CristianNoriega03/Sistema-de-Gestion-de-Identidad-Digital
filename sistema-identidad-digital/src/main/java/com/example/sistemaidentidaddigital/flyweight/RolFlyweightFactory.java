package com.example.sistemaidentidaddigital.flyweight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 3. La Fábrica Flyweight (El caché)
public class RolFlyweightFactory {
    
    // Este Map actuará como nuestra memoria RAM para guardar las únicas instancias
    private static final Map<String, RolPermiso> rolesCache = new HashMap<>();

    public static RolPermiso getRol(String tipoRol) {
        String clave = tipoRol.toUpperCase();
        
        if (!rolesCache.containsKey(clave)) {
            // Si el rol NO existe en memoria, lo creamos por primera y única vez
            switch (clave) {
                case "SUPERADMIN":
                    rolesCache.put(clave, new RolConcreto("SUPERADMIN", List.of("CREAR_ADMIN", "VER_TODO_SISTEMA")));
                    break;
                case "ADMIN":
                    rolesCache.put(clave, new RolConcreto("ADMIN", List.of("VER_CITAS_GLOBALES","CANCELAR_CUALQUIER_CITA")));
                    break;
                case "CIUDADANO":
                    rolesCache.put(clave, new RolConcreto("CIUDADANO", List.of("CREAR_CITA", "VER_MIS_CITAS", "VER_CARNE")));
                    break;
                default:
                    throw new IllegalArgumentException("Rol no soportado: " + clave);
            }
            System.out.println("🪶 Fábrica Flyweight: Creando NUEVA instancia en memoria para el rol -> " + clave);
        } else {
            // Si ya existe, NO usamos "new", simplemente reutilizamos el de la memoria
            System.out.println("🪶 Fábrica Flyweight: Reutilizando instancia COMPARTIDA para el rol -> " + clave);
        }
        
        return rolesCache.get(clave);
    }
}