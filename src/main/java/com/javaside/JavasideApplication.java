package com.javaside;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Clase principal de la aplicación Javaside.
 * Responsable únicamente de iniciar el contexto de Spring Boot.
 * 
 * La configuración de variables de entorno se maneja en EnvironmentConfig.
 */
@SpringBootApplication
public class JavasideApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JavasideApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        
        System.out.println("✓ Javaside Application Started Successfully!");
        System.out.println("✓ Dashboard available at: http://localhost:" + port);
    }
}
