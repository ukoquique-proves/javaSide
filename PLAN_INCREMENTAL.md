# Alexia - Plan de Desarrollo Incremental

## 🎯 Objetivo
Crear la aplicación Alexia paso a paso, probando cada funcionalidad antes de continuar. Cada paso será pequeño, funcional y verificable.

---

## 📋 Lecciones del Primer Intento

### ❌ Problemas del primer intento:
- Se intentó crear toda la aplicación de una vez
- Código sobrecargado sin pruebas intermedias
- No se verificó que cada componente funcionara antes de continuar
- Demasiadas dependencias y configuraciones simultáneas

### ✅ Nuevo enfoque:
- **Desarrollo incremental**: Un paso a la vez
- **Pruebas continuas**: Ejecutar y verificar después de cada paso
- **Funcionalidad mínima**: Solo lo necesario en cada etapa
- **Sin código innecesario**: Solo agregar lo que se va a usar

---

## 🏗️ Arquitectura Simplificada (Fase Inicial)

```
┌─────────────────────────────────────────┐
│         USUARIO (Telegram)              │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│    ALEXIA BACKEND (Spring Boot)         │
│                                          │
│  ┌────────────┐      ┌────────────┐    │
│  │  Telegram  │      │  Dashboard │    │
│  │   Service  │      │  (Vaadin)  │    │
│  └────────────┘      └────────────┘    │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│      SUPABASE (PostgreSQL)              │
│      (Solo tabla de logs inicial)       │
└─────────────────────────────────────────┘
```

---

## 📦 Pasos de Desarrollo

### **PASO 1: Proyecto Base y Dashboard Básico** ✅ COMPLETADO
**Objetivo**: Crear un proyecto Spring Boot + Vaadin que compile y muestre un dashboard vacío.

**Tareas**:
- [x] Crear estructura Maven básica
- [x] Configurar `pom.xml` con dependencias mínimas:
  - Spring Boot Starter Web
  - Spring Boot Starter Data JPA
  - PostgreSQL Driver
  - Vaadin (versión compatible)
  - Lombok
- [x] Crear `application.properties` básico
- [x] Crear clase principal `AlexiaApplication.java`
- [x] Crear `DashboardView.java` con mensaje "Bienvenido a Alexia"
- [x] Crear archivo `.env` con credenciales de Supabase

**Verificación**:
```bash
mvn clean install  # ✅ BUILD SUCCESS
mvn spring-boot:run  # ✅ Application Started
# Abrir http://localhost:8080
# ✅ Muestra: "Bienvenido a Alexia"
```

**Archivos creados**:
- `pom.xml`
- `src/main/java/com/alexia/AlexiaApplication.java`
- `src/main/java/com/alexia/views/DashboardView.java`
- `src/main/resources/application.properties`
- `.env`
- `.env.example`
- `README.md`
- `.gitignore`

**Fecha de completado**: 2025-10-14

---

### **PASO 2: Conexión a Supabase** ✅ COMPLETADO
**Objetivo**: Conectar la aplicación a la base de datos Supabase y verificar la conexión.

**Tareas**:
- [x] Configurar conexión a PostgreSQL en `application.properties`
- [x] Crear entidad simple `ConnectionTest.java`
- [x] Crear repositorio `ConnectionTestRepository.java`
- [x] Crear tabla `connection_test` en Supabase
- [x] Agregar botón en Dashboard para probar conexión
- [x] Mostrar resultado de la prueba en pantalla
- [x] Agregar dependencia `dotenv-java` para cargar `.env`
- [x] Configurar carga automática de variables de entorno

**Verificación**:
```bash
mvn spring-boot:run
# Abrir http://localhost:8080
# Hacer clic en "Probar Conexión"
# ✅ Muestra: "✓ Conexión exitosa a Supabase!"
# ✅ Registro guardado con ID: 1
```

**Archivos creados**:
- `src/main/java/com/alexia/entity/ConnectionTest.java`
- `src/main/java/com/alexia/repository/ConnectionTestRepository.java`
- `src/main/java/com/alexia/service/DatabaseService.java`
- `database/step2_connection_test.sql`

**Archivos modificados**:
- `pom.xml` (agregada dependencia dotenv-java)
- `src/main/java/com/alexia/AlexiaApplication.java` (carga de .env)
- `src/main/java/com/alexia/views/DashboardView.java` (botón de prueba)

**SQL ejecutado en Supabase**:
```sql
CREATE TABLE connection_test (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);
```

**Fecha de completado**: 2025-10-14

---

### **MEJORA UI: Dashboard Profesional Completo** ✅ COMPLETADO
**Objetivo**: Crear un dashboard profesional con todas las secciones del proyecto visibles.

**Tareas**:
- [x] Crear `MainLayout.java` con menú lateral
- [x] Organizar menú en 4 secciones temáticas
- [x] Rediseñar `DashboardView.java` con métricas visuales
- [x] Crear 8 cards de métricas con iconos y colores
- [x] Agregar sección de estado del sistema con badges
- [x] Crear 13 vistas placeholder para todas las secciones
- [x] Implementar navegación entre vistas

**Vistas creadas**:
- `BusinessesView.java` - Gestión de negocios
- `ProductsView.java` - Catálogo de productos
- `CampaignsView.java` - Campañas CPC/CPA
- `LeadsView.java` - Gestión de leads
- `TelegramView.java` - Bot de Telegram
- `WhatsAppView.java` - Bot de WhatsApp
- `ConversationsView.java` - Historial de conversaciones
- `MetricsView.java` - Análisis de métricas
- `BillingView.java` - Facturación automática
- `TrackingView.java` - Sistema de tracking
- `ConfigurationView.java` - Configuración general
- `DatabaseView.java` - Gestión de BD
- `LogsView.java` - Registro de actividad

**Características del Dashboard**:
- 8 cards de métricas con colores distintivos
- Badges de estado para servicios (Supabase ✅, otros ⏳)
- Menú lateral con iconos y secciones organizadas
- Navegación fluida entre todas las vistas
- Diseño profesional con sombras y bordes de color

**Fecha de completado**: 2025-10-14

---

### **PASO 3: Integración Básica con Telegram**
**Objetivo**: Conectar el bot de Telegram y recibir/responder mensajes simples.

**Tareas**:
- [ ] Agregar dependencia `telegrambots` al `pom.xml`
- [ ] Crear clase `TelegramBotConfig.java` con token del `.env`
- [ ] Crear clase `AlexiaTelegramBot.java` que extienda `TelegramLongPollingBot`
- [ ] Implementar método `onUpdateReceived()` para recibir mensajes
- [ ] Responder con eco: "Recibí tu mensaje: [texto]"
- [ ] Crear tabla `telegram_messages` en Supabase para guardar logs
- [ ] Guardar cada mensaje recibido en la base de datos

**Verificación**:
```bash
mvn spring-boot:run
# Abrir Telegram
# Buscar el bot: @[tu_bot_username]
# Enviar: "Hola"
# Debe responder: "Recibí tu mensaje: Hola"
# Verificar en Supabase que se guardó el mensaje
```

**Archivos a crear/modificar**:
- `src/main/java/com/alexia/config/TelegramBotConfig.java`
- `src/main/java/com/alexia/telegram/AlexiaTelegramBot.java`
- `src/main/java/com/alexia/entity/TelegramMessage.java`
- `src/main/java/com/alexia/repository/TelegramMessageRepository.java`
- `src/main/java/com/alexia/service/TelegramService.java`
- Modificar `pom.xml`

**SQL en Supabase**:
```sql
CREATE TABLE telegram_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    chat_id BIGINT NOT NULL,
    user_name VARCHAR(255),
    message_text TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

### **PASO 4: Dashboard con Logs de Telegram**
**Objetivo**: Mostrar en el dashboard los mensajes recibidos de Telegram.

**Tareas**:
- [ ] Crear vista `TelegramLogsView.java` en Vaadin
- [ ] Crear Grid para mostrar mensajes (fecha, usuario, mensaje)
- [ ] Agregar menú lateral con navegación Dashboard/Logs
- [ ] Implementar auto-refresh cada 5 segundos
- [ ] Agregar filtro por fecha

**Verificación**:
```bash
mvn spring-boot:run
# Abrir http://localhost:8080
# Navegar a "Logs de Telegram"
# Enviar mensajes desde Telegram
# Verificar que aparecen en la tabla del dashboard
```

**Archivos a crear/modificar**:
- `src/main/java/com/alexia/views/TelegramLogsView.java`
- `src/main/java/com/alexia/views/MainLayout.java`
- Modificar `DashboardView.java`

---

### **PASO 5: Comandos Básicos del Bot**
**Objetivo**: Implementar comandos simples en el bot de Telegram.

**Tareas**:
- [ ] Implementar comando `/start` - Mensaje de bienvenida
- [ ] Implementar comando `/help` - Lista de comandos disponibles
- [ ] Implementar comando `/status` - Estado del bot
- [ ] Crear tabla `bot_commands` para registrar uso de comandos
- [ ] Mostrar estadísticas de comandos en el dashboard

**Verificación**:
```bash
# En Telegram:
/start → "¡Bienvenido a Alexia! Soy tu asistente..."
/help → "Comandos disponibles: /start, /help, /status"
/status → "Bot activo ✓ | Mensajes procesados: X"
```

**Archivos a crear/modificar**:
- Modificar `AlexiaTelegramBot.java`
- `src/main/java/com/alexia/entity/BotCommand.java`
- `src/main/java/com/alexia/repository/BotCommandRepository.java`
- Modificar `DashboardView.java`

**SQL en Supabase**:
```sql
CREATE TABLE bot_commands (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    chat_id BIGINT NOT NULL,
    command VARCHAR(50),
    user_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

### **PASO 6: Búsqueda Simple en Base de Datos**
**Objetivo**: Crear tabla de negocios y permitir búsqueda básica por categoría.

**Tareas**:
- [ ] Crear tabla `businesses` en Supabase (versión simplificada)
- [ ] Crear entidad `Business.java`
- [ ] Crear repositorio `BusinessRepository.java`
- [ ] Agregar algunos negocios de prueba manualmente en Supabase
- [ ] Implementar búsqueda: "buscar [categoría]" en Telegram
- [ ] Responder con lista de negocios encontrados

**Verificación**:
```bash
# En Telegram:
"buscar panadería" → "Encontré 2 panaderías:
1. Panadería El Sol - Calle 123
2. Pan Caliente - Av. Principal"
```

**Archivos a crear/modificar**:
- `src/main/java/com/alexia/entity/Business.java`
- `src/main/java/com/alexia/repository/BusinessRepository.java`
- `src/main/java/com/alexia/service/BusinessService.java`
- Modificar `AlexiaTelegramBot.java`

**SQL en Supabase**:
```sql
CREATE TABLE businesses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    address VARCHAR(500),
    phone VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Insertar datos de prueba
INSERT INTO businesses (name, category, address, phone) VALUES
('Panadería El Sol', 'panadería', 'Calle 123, Costa Azul', '555-0001'),
('Pan Caliente', 'panadería', 'Av. Principal 456', '555-0002'),
('Restaurante La Costa', 'restaurante', 'Playa Norte 789', '555-0003');
```

---

### **PASO 7: CRUD de Negocios en Dashboard**
**Objetivo**: Permitir crear, editar y eliminar negocios desde el panel web.

**Tareas**:
- [ ] Crear vista `BusinessView.java` con Grid de negocios
- [ ] Crear formulario para agregar/editar negocios
- [ ] Implementar botones: Nuevo, Editar, Eliminar
- [ ] Agregar validaciones básicas
- [ ] Actualizar menú lateral con opción "Negocios"

**Verificación**:
```bash
# En el dashboard:
# Ir a "Negocios"
# Hacer clic en "Nuevo"
# Llenar formulario y guardar
# Verificar que aparece en la lista
# Desde Telegram buscar el negocio creado
```

**Archivos a crear/modificar**:
- `src/main/java/com/alexia/views/BusinessView.java`
- `src/main/java/com/alexia/views/BusinessForm.java`
- Modificar `MainLayout.java`

---

### **PASO 8: Integración con IA (Grok)**
**Objetivo**: Usar Grok para analizar la intención del usuario.

**Tareas**:
- [ ] Agregar dependencia HTTP client al `pom.xml`
- [ ] Crear clase `GrokService.java`
- [ ] Implementar llamada a API de Grok
- [ ] Extraer categoría del mensaje del usuario usando IA
- [ ] Usar la categoría extraída para buscar negocios
- [ ] Registrar consultas IA en tabla `ai_queries`

**Verificación**:
```bash
# En Telegram:
"Necesito comprar pan" → Grok extrae "panadería" → Muestra panaderías
"Dónde puedo comer?" → Grok extrae "restaurante" → Muestra restaurantes
```

**Archivos a crear/modificar**:
- `src/main/java/com/alexia/service/GrokService.java`
- `src/main/java/com/alexia/entity/AIQuery.java`
- `src/main/java/com/alexia/repository/AIQueryRepository.java`
- Modificar `AlexiaTelegramBot.java`

**SQL en Supabase**:
```sql
CREATE TABLE ai_queries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_message TEXT,
    extracted_category VARCHAR(100),
    ai_response TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

### **PASO 9: Búsqueda por Ubicación**
**Objetivo**: Permitir que el usuario comparta su ubicación y buscar negocios cercanos.

**Tareas**:
- [ ] Agregar columnas de ubicación a tabla `businesses`
- [ ] Modificar entidad `Business.java` con latitud/longitud
- [ ] Capturar ubicación compartida en Telegram
- [ ] Implementar búsqueda por distancia (PostGIS)
- [ ] Responder con negocios ordenados por cercanía

**Verificación**:
```bash
# En Telegram:
# Compartir ubicación
# Escribir: "panaderías cerca"
# Debe responder con negocios ordenados por distancia
```

**Archivos a crear/modificar**:
- Modificar `Business.java`
- Modificar `BusinessRepository.java`
- Modificar `AlexiaTelegramBot.java`
- Modificar `BusinessService.java`

**SQL en Supabase**:
```sql
ALTER TABLE businesses 
ADD COLUMN latitude DECIMAL(10, 8),
ADD COLUMN longitude DECIMAL(11, 8);

-- Actualizar datos de prueba con coordenadas
UPDATE businesses SET latitude = 10.3910, longitude = -75.4794 WHERE name = 'Panadería El Sol';
```

---

### **PASO 10: Dashboard con Métricas**
**Objetivo**: Mostrar estadísticas básicas en el dashboard.

**Tareas**:
- [ ] Crear cards con métricas:
  - Total de mensajes recibidos
  - Total de búsquedas realizadas
  - Total de negocios registrados
  - Comandos más usados
- [ ] Agregar gráfico simple de mensajes por día
- [ ] Implementar filtro por rango de fechas

**Verificación**:
```bash
# Abrir dashboard
# Verificar que muestra:
# - "Mensajes: 45"
# - "Búsquedas: 23"
# - "Negocios: 8"
# - Gráfico de barras con actividad
```

**Archivos a crear/modificar**:
- Modificar `DashboardView.java`
- `src/main/java/com/alexia/service/MetricsService.java`

---

## 🔑 Credenciales Disponibles (del primer intento)

```env
# Supabase
SUPABASE_URL=https://hgcesbylhkjoxtymxysy.supabase.co
SUPABASE_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhnY2VzYnlsaGtqb3h0eW14eXN5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjA0NjUzMzAsImV4cCI6MjA3NjA0MTMzMH0.F9lGCkzcAf2TdtmiG2Piqa17pxChMwF0QSf2o1AZULI
SUPABASE_DB_URL=jdbc:postgresql://your-supabase-host.supabase.co:5432/postgres
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=your_password_here

# Grok
GROK_API_KEY=your_grok_api_key_here

# Telegram
TELEGRAM_BOT_TOKEN=your_telegram_bot_token_here
```

---

## 📝 Reglas de Desarrollo

### ✅ HACER:
1. **Un paso a la vez**: Completar y probar antes de continuar
2. **Código mínimo**: Solo lo necesario para la funcionalidad actual
3. **Probar siempre**: Ejecutar `mvn spring-boot:run` después de cada paso
4. **Commits frecuentes**: Guardar progreso después de cada paso exitoso
5. **Logs claros**: Agregar logs para debugging

### ❌ NO HACER:
1. **No adelantarse**: No agregar código para pasos futuros
2. **No sobrecargar**: No agregar dependencias innecesarias
3. **No asumir**: Verificar que todo funciona antes de continuar
4. **No código muerto**: Eliminar código que no se usa
5. **No complejidad prematura**: Mantener simple hasta que sea necesario

---

## 🎯 Criterios de Éxito por Paso

Cada paso se considera **COMPLETADO** solo si:
- ✅ El código compila sin errores
- ✅ La aplicación inicia correctamente
- ✅ La funcionalidad específica funciona como se espera
- ✅ Se puede demostrar visualmente (dashboard o Telegram)
- ✅ Los datos se guardan correctamente en Supabase

---

## 📊 Progreso Actual

| Paso | Estado | Fecha | Descripción |
|------|--------|-------|-------------|
| 1. Proyecto Base y Dashboard | ✅ Completado | 2025-10-14 | Maven, Spring Boot, Vaadin básico |
| 2. Conexión a Supabase | ✅ Completado | 2025-10-14 | Conexión verificada, dotenv configurado |
| UI. Dashboard Profesional | ✅ Completado | 2025-10-14 | 13 vistas, menú lateral, métricas |
| 3. Integración con Telegram | ⏳ Pendiente | - | Bot con respuestas eco |
| 4. Dashboard con Logs | ⏳ Pendiente | - | Visualización de mensajes |
| 5. Comandos Básicos | ⏳ Pendiente | - | /start, /help, /status |
| 6. Búsqueda Simple | ⏳ Pendiente | - | Búsqueda por categoría |
| 7. CRUD de Negocios | ⏳ Pendiente | - | Gestión completa de negocios |
| 8. Integración con IA | ⏳ Pendiente | - | Grok para análisis de intención |
| 9. Búsqueda por Ubicación | ⏳ Pendiente | - | PostGIS, búsqueda geográfica |
| 10. Dashboard con Métricas | ⏳ Pendiente | - | Gráficos y estadísticas |

**Progreso**: 2 pasos + UI completo de 10 pasos = **30% completado**

---

## 🚀 Pasos Futuros (Después del Paso 10)

Una vez completados los 10 pasos básicos, se pueden agregar:
- Integración con Google Places (fallback)
- Sistema de campañas y tracking
- Facturación automática
- Integración con WhatsApp
- Autenticación y roles en dashboard
- Sistema de productos
- Notificaciones push
- Reportes avanzados

---

**Versión**: 1.0  
**Fecha de creación**: 2025-10-14  
**Última actualización**: 2025-10-14  
**Estado**: Paso 2 completado + UI profesional implementado  
**Próximo paso**: Paso 3 - Integración con Telegram
