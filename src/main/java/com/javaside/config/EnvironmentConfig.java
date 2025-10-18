package com.javaside.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de variables de entorno.
 * Carga las variables desde el archivo .env (si existe) y las establece como propiedades del sistema.
 * 
 * Esta clase separa la lógica de configuración de la clase principal de la aplicación,
 * mejorando la testabilidad y siguiendo el principio de responsabilidad única.
 */
@Configuration
@Slf4j
public class EnvironmentConfig {

    /**
     * Carga las variables de entorno desde el archivo .env después de la construcción del bean.
     * Se ejecuta automáticamente por Spring después de la inicialización del contexto.
     */
    @PostConstruct
    public void loadEnvironmentVariables() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()  // No falla si el archivo .env no existe (útil en producción)
                    .load();
            
            // Establecer las variables como propiedades del sistema
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
                log.debug("Variable de entorno cargada: {}", entry.getKey());
            });
            
            log.info("Variables de entorno cargadas exitosamente desde .env");
            
        } catch (Exception e) {
            log.warn("No se pudo cargar el archivo .env: {}. Usando variables de entorno del sistema.", e.getMessage());
        }
    }
}
