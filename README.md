# Javaside - Asistente Automatizado

Asistente automatizado que ayuda a usuarios de WhatsApp y Telegram a encontrar negocios, productos y servicios locales usando IA y fuentes verificadas.

## 🚀 Estado Actual: DESPLEGADO EN PRODUCCIÓN ✅

**Aplicación en vivo**: https://javaside.onrender.com

Este proyecto se está desarrollando de forma **incremental**, paso por paso, probando cada funcionalidad antes de continuar.

Ver el plan completo en: [PLAN_INCREMENTAL.md](PLAN_INCREMENTAL.md)

## 📋 Requisitos

### Desarrollo Local
- Java 17 o superior
- Maven 3.6+
- PostgreSQL (Supabase o Render)
- Token de Telegram Bot (opcional)

### Producción
- Cuenta de Render (o plataforma compatible)
- PostgreSQL database
- GitHub repository

## ⚙️ Configuración

1. **Clonar el repositorio**
```bash
cd /home/uko/COLOMBIA/JAVA/2do-Intento-JAVA/javaDos-
```

2. **Configurar variables de entorno**
```bash
cp .env.example .env
# Editar .env con tus credenciales (ya está configurado)
```

3. **Crear tabla en Supabase**
```sql
-- Ejecutar en Supabase SQL Editor
CREATE TABLE IF NOT EXISTS connection_test (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);
```

4. **Compilar el proyecto**
```bash
mvn clean install
```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

6. **Abrir el dashboard**
```
http://localhost:8080
```

7. **Probar la conexión**
- Haz clic en el botón "Probar Conexión"
- Deberías ver: ✓ Conexión exitosa a Supabase!

## 📦 Tecnologías

- **Backend**: Spring Boot 3.1.5
- **Frontend**: Vaadin 24.2.5
- **Base de datos**: Supabase (PostgreSQL)
- **Java**: 17
- **Build**: Maven
- **Env Management**: Dotenv Java

## 🎯 Funcionalidades Actuales

### ✅ Paso 1: Dashboard Básico
- [x] Proyecto Maven configurado
- [x] Dashboard web con Vaadin
- [x] Mensaje de bienvenida

### ✅ Paso 2: Conexión a Supabase
- [x] Entidad JPA `ConnectionTest`
- [x] Repositorio Spring Data JPA
- [x] Servicio de base de datos
- [x] Botón de prueba en dashboard
- [x] Carga automática de variables `.env`
- [x] Conexión verificada y funcionando

### ⏳ Próximos Pasos
- [ ] Paso 3: Integración con Telegram
- [ ] Paso 4: Dashboard con logs
- [ ] Paso 5: Comandos básicos del bot

## 📝 Estructura del Proyecto

```
javaside/
├── deployment/              # Guías y configuración de despliegue
│   ├── KOYEB.md
│   ├── README_DEPLOY.md
│   └── README.md
├── src/
│   └── main/
│       ├── java/com/javaside/  # Código fuente de la aplicación
│       │   ├── JavasideApplication.java
│       │   ├── constants/
│       │   ├── dto/
│       │   ├── entity/
│       │   ├── repository/
│       │   ├── service/
│       │   ├── usecase/
│       │   └── views/
│       └── resources/
│           ├── application.properties
│           └── application-mock.properties
├── Dockerfile                 # Define el contenedor de la aplicación
├── pom.xml                    # Dependencias y configuración de Maven
├── .env.example               # Plantilla de variables de entorno
└── README.md                  # Este archivo
```

## 🔧 Comandos Útiles

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run

# Ejecutar tests
mvn test

# Limpiar
mvn clean
```

## 🚀 Despliegue (Deployment)

### Estado Actual
- ✅ **Desplegado en Render**: https://javaside.onrender.com
- ✅ **Base de datos**: Render PostgreSQL
- ✅ **Auto-deploy**: Activado desde GitHub
- ✅ **Estado**: Live y funcionando

### Guías de Despliegue
Para instrucciones detalladas sobre cómo desplegar esta aplicación:

- **[Guía de Render](./deployment/RENDER.md)** - ⭐ Recomendado (actualmente en uso)
- **[Guía de Koyeb](./deployment/KOYEB.md)** - Alternativa
- **[Comparación de Plataformas](./deployment/README_DEPLOY.md)**

## 📚 Documentación

- [Plan de Desarrollo Incremental](PLAN_INCREMENTAL.md)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Vaadin Docs](https://vaadin.com/docs)

## 🐛 Troubleshooting

### Error de conexión a base de datos
Verificar que las credenciales en `.env` sean correctas.

### Puerto 8080 en uso
Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

## 📊 Progreso del Desarrollo

| Paso | Estado | Descripción |
|------|--------|-------------|
| 1 | ✅ | Dashboard Básico |
| 2 | ✅ | Conexión a Supabase |
| 3 | ⏳ | Integración con Telegram |
| 4 | ⏳ | Dashboard con Logs |
| 5 | ⏳ | Comandos Básicos del Bot |
| 6 | ⏳ | Búsqueda Simple |
| 7 | ⏳ | CRUD de Negocios |
| 8 | ⏳ | Integración con IA |
| 9 | ⏳ | Búsqueda por Ubicación |
| 10 | ⏳ | Dashboard con Métricas |

## 📄 Licencia

Este proyecto es privado y está en desarrollo.

---

**Versión**: 1.0.0  
**Última actualización**: 2025-10-19  
**Pasos completados**: 2/10  
**Estado**: ✅ Desplegado en producción (Render)
