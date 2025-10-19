package com.javaside.constants;

/**
 * Constantes de mensajes del sistema.
 * Centraliza todos los textos para facilitar mantenimiento y i18n futuro.
 */
public final class Messages {
    
    private Messages() {
        throw new UnsupportedOperationException("Esta es una clase de constantes");
    }
    
    // Mensajes de conexión
    public static final String CONNECTION_SUCCESS = "✓ Conexión exitosa a la base de datos!";
    public static final String CONNECTION_ERROR = "✗ Error de conexión: ";
    public static final String CONNECTION_TESTING = "Probando conexión...";
    public static final String DB_PROVIDER_NAME = "PostgreSQL"; // Supabase, Render, Railway, etc.
    
    // Mensajes del sistema
    public static final String SYSTEM_STARTED = "✓ Javaside Application Started Successfully!";
    public static final String DASHBOARD_AVAILABLE = "✓ Dashboard available at: http://localhost:{port}";
    
    // Mensajes de estado
    public static final String STATUS_ACTIVE = "Activo";
    public static final String STATUS_INACTIVE = "Inactivo";
    public static final String STATUS_PENDING = "Pendiente";
    
    // Mensajes de placeholder
    public static final String NO_RECENT_ACTIVITY = "No hay actividad reciente. Los mensajes y eventos aparecerán aquí una vez que se conecten los servicios.";
    public static final String FEATURE_IN_DEVELOPMENT = "⏳ Funcionalidad en desarrollo";
}
