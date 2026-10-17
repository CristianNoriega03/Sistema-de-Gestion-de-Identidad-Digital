package com.example.sistemaidentidaddigital.proxy;

import com.example.sistemaidentidaddigital.flyweight.RolPermiso;
import com.example.sistemaidentidaddigital.flyweight.RolFlyweightFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

// 3. El Proxy de Protección
@Service
@Primary // Le dice a Spring Boot que use este Proxy por defecto en lugar del sistema real
public class SeguridadProxy implements ValidacionAcceso {

    private final SistemaAccesoReal sistemaReal;

    public SeguridadProxy(SistemaAccesoReal sistemaReal) {
        this.sistemaReal = sistemaReal;
    }

    @Override
    public boolean tieneAcceso(String rol, String accionSolicitada) {
        System.out.println("🛡️ PROXY: Interceptando petición para la acción -> " + accionSolicitada);
        
        // 1. CONEXIÓN DE PATRONES: Usamos el Flyweight para obtener los permisos sin gastar memoria
        RolPermiso rolUsuario = RolFlyweightFactory.getRol(rol);
        
        // 2. Verificamos si el usuario tiene la "manilla" correcta para entrar
        if (rolUsuario.getPermisos().contains(accionSolicitada)) {
            System.out.println("✅ PROXY: Acceso CONCEDIDO al rol " + rol);
            
            // 3. Como todo está en orden, delegamos el paso al sistema real
            return sistemaReal.tieneAcceso(rol, accionSolicitada);
        } else {
            System.out.println("❌ PROXY: Acceso DENEGADO. El rol " + rol + " no tiene permiso para " + accionSolicitada);
            return false;
        }
    }
}