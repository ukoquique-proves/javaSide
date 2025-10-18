package com.javaside.service;

import com.javaside.constants.Messages;
import com.javaside.dto.ConnectionResultDTO;
import com.javaside.entity.ConnectionTest;
import com.javaside.repository.ConnectionTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para operaciones de base de datos.
 * Maneja la lógica de negocio relacionada con la persistencia.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseService {

    private final ConnectionTestRepository connectionTestRepository;

    /**
     * Prueba la conexión a la base de datos creando un registro de prueba.
     * 
     * @return ConnectionResultDTO con el resultado de la operación
     */
    public ConnectionResultDTO testConnection() {
        try {
            // Crear un registro de prueba
            String testMessage = "Prueba de conexión - " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            ConnectionTest test = new ConnectionTest(testMessage);
            ConnectionTest saved = connectionTestRepository.save(test);
            
            log.info("Conexión exitosa a Supabase. Registro guardado con ID: {}", saved.getId());
            
            // Contar registros totales
            long count = connectionTestRepository.count();
            
            return ConnectionResultDTO.success(
                saved.getId(),
                count,
                saved.getMessage()
            );
            
        } catch (Exception e) {
            log.error("Error al conectar con Supabase: {}", e.getMessage());
            return ConnectionResultDTO.error(e.getMessage());
        }
    }
}
