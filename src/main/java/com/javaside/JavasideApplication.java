package com.javaside;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Clase principal de la aplicación Javaside.
 * Responsable de cargar variables de entorno y iniciar el contexto de Spring Boot.
 */
@SpringBootApplication
public class JavasideApplication {

    public static void main(String[] args) {
        // Cargar variables de entorno ANTES de iniciar Spring
        loadEnvironmentVariables();
        
        ConfigurableApplicationContext context = SpringApplication.run(JavasideApplication.class, args);
        Environment env = context.getEnvironment();
        String port = env.getProperty("server.port", "8080");
        
        System.out.println("✓ Javaside Application Started Successfully!");
        System.out.println("✓ Dashboard available at: http://localhost:" + port);
    }
    
    /**
     * Carga las variables de entorno desde el archivo .env antes de que Spring inicie.
     * Esto es crítico para que las propiedades de conexión a la base de datos se resuelvan correctamente.
     */
    private static void loadEnvironmentVariables() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });
            
            System.out.println("✓ Variables de entorno cargadas desde .env");
        } catch (Exception e) {
            System.out.println("⚠ No se pudo cargar .env: " + e.getMessage());
            System.out.println("  Usando variables de entorno del sistema");
        }
    }
}
