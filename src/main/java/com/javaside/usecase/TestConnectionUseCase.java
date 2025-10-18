package com.javaside.usecase;

import com.javaside.dto.ConnectionResultDTO;
import com.javaside.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Caso de uso para probar la conexión a la base de datos.
 * Actúa como intermediario entre la capa de presentación y la capa de servicio.
 * Implementa el principio de inversión de dependencias de Clean Architecture.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TestConnectionUseCase {
    
    private final DatabaseService databaseService;
    
    /**
     * Ejecuta la prueba de conexión a la base de datos.
     * 
     * @return ConnectionResultDTO con el resultado de la prueba
     */
    public ConnectionResultDTO execute() {
        log.info("Ejecutando caso de uso: Test de conexión a base de datos");
        
        try {
            ConnectionResultDTO result = databaseService.testConnection();
            
            if (result.isSuccess()) {
                log.info("Caso de uso completado exitosamente. ID: {}", result.getRecordId());
            } else {
                log.warn("Caso de uso completado con error: {}", result.getErrorMessage());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("Error inesperado en caso de uso de test de conexión", e);
            return ConnectionResultDTO.error("Error inesperado: " + e.getMessage());
        }
    }
}
