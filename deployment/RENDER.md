# Deploying to Render

This guide provides step-by-step instructions for deploying the Javaside application to Render using Docker and connecting it to a Supabase database.

## Prerequisites

1.  **GitHub Repository**: The project code must be pushed to a GitHub repository.
2.  **Render Account**: A free account on [Render](https://render.com/).
3.  **Supabase Account**: A Supabase project with a PostgreSQL database. You will need your database connection details (URL, user, and password).

---

## Deployment Steps

### Step 1: Create a New Web Service on Render

1.  Log in to your Render account.
2.  Click **New +** and select **Web Service**.

### Step 2: Connect Your Repository

1.  Select **Build and deploy from a Git repository**.
2.  Connect your GitHub account if you haven't already.
3.  Choose your project repository: `ukoquique-proves/javaAlexiaTestChico`.

### Step 3: Configure the Service

Render will automatically detect the `Dockerfile` and `render.yaml` in your repository.

1.  **Name**: Choose a name for your service (e.g., `javaside-app`).
2.  **Region**: Select the region closest to you or your users.
3.  **Branch**: Select `main` (or your default branch).
4.  **Root Directory**: Leave blank (uses repository root).
5.  **Environment**: Select **Docker**.
6.  **Dockerfile Path**: Should auto-detect `./Dockerfile`.

### Step 4: Configure Instance Type

1.  **Instance Type**: Select **Free** for testing, or a paid tier for production.
2.  **Auto-Deploy**: Enable this to automatically deploy when you push to GitHub.

### Step 5: Set Up Database

**Important**: Render's free tier:
- ✅ **CAN** connect to Render's own PostgreSQL databases (internal network)
- ❌ **CANNOT** connect to external databases like Supabase (network restrictions)
- ⚠️ **Free tier allows only ONE free PostgreSQL database per account**

---

#### Option A: Use Render Managed PostgreSQL (Recommended) ⭐

**Step 5.1: Create PostgreSQL Database**

1. In Render Dashboard → **New +** → **PostgreSQL**
2. Configure:
   - **Name**: `javaside-db`
   - **Database**: `javaside`
   - **User**: `javaside` (auto-generated)
   - **Region**: **Oregon (US West)** (same as web service for low latency)
   - **PostgreSQL Version**: **16**
   - **Plan**: **Free** (90 days free, then $7/month)
3. Click **Create Database**
4. Wait 2-3 minutes for provisioning

**Step 5.2: Get Connection Details**

Once created, you'll see the **Internal Database URL**:

```
postgresql://javaside:Ir7hgbg4hTPiksD69TIRd2GRwhnApSDZ@dpg-d3qemls9c44c73cn3760-a/javaside
```

**Breakdown:**
- **Hostname**: `dpg-d3qemls9c44c73cn3760-a`
- **Database**: `javaside`
- **Username**: `javaside`
- **Password**: `Ir7hgbg4hTPiksD69TIRd2GRwhnApSDZ`

**Step 5.3: Convert to JDBC Format**

Spring Boot requires JDBC format:

```
From: postgresql://javaside:password@dpg-xxxxx-a/javaside
To:   jdbc:postgresql://dpg-xxxxx-a/javaside
```

**Example for this project:**
```
jdbc:postgresql://dpg-d3qemls9c44c73cn3760-a/javaside
```

**Step 5.4: Configure Environment Variables**

The project uses **flexible variable names** that support multiple database providers:

**Primary Variables (Recommended for Render):**
```bash
DATABASE_URL = jdbc:postgresql://dpg-d3qemls9c44c73cn3760-a/javaside
DATABASE_USER = javaside
DATABASE_PASSWORD = Ir7hgbg4hTPiksD69TIRd2GRwhnApSDZ
```

**Fallback Variables (Legacy Supabase support):**
```bash
SUPABASE_DB_URL = jdbc:postgresql://dpg-d3qemls9c44c73cn3760-a/javaside
SUPABASE_DB_USER = javaside
SUPABASE_DB_PASSWORD = Ir7hgbg4hTPiksD69TIRd2GRwhnApSDZ
```

**How it works:**
The `application.properties` file uses this configuration:
```properties
spring.datasource.url=${DATABASE_URL:${SUPABASE_DB_URL}}
spring.datasource.username=${DATABASE_USER:${SUPABASE_DB_USER}}
spring.datasource.password=${DATABASE_PASSWORD:${SUPABASE_DB_PASSWORD}}
```

This means:
- ✅ Try `DATABASE_URL` first (generic)
- ✅ If not found, fallback to `SUPABASE_DB_URL`
- ✅ Works with Render, Railway, Fly.io, or any PostgreSQL provider

**Step 5.5: Add Variables to Render Web Service**

When creating your Web Service, in the **Environment Variables** section, add:

| Key | Value |
| :--- | :--- |
| `DATABASE_URL` | `jdbc:postgresql://dpg-d3qemls9c44c73cn3760-a/javaside` |
| `DATABASE_USER` | `javaside` |
| `DATABASE_PASSWORD` | `Ir7hgbg4hTPiksD69TIRd2GRwhnApSDZ` |
| `TELEGRAM_BOT_TOKEN` | `<your-telegram-bot-token>` |
| `GROK_API_KEY` | `<your-grok-api-key>` |

**Step 5.6: Database Table Creation**

Tables are created automatically:
- ✅ Hibernate auto-creates tables with `spring.jpa.hibernate.ddl-auto=update`
- ✅ On first connection, `connection_test` table will be created
- ✅ No manual SQL execution needed

**Optional:** To manually create tables, connect via `psql` and run:
```sql
-- From database/step2_connection_test.sql
CREATE TABLE connection_test (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

#### Option B: If You Already Have a Render PostgreSQL Database

**Error:** `cannot have more than one active free tier database`

**Solutions:**

1. **Delete unused database:**
   - Dashboard → Select old database → Settings → Delete Database
   - Then create new one for Javaside

2. **Reuse existing database:**
   - Use the existing database credentials
   - Update environment variables with those credentials

3. **Upgrade to paid plan:**
   - $7/month allows multiple databases
   - No free tier restrictions

---

#### Option C: Use Supabase Connection Pooler (Not Recommended)

⚠️ **Success rate: ~60-70% on Render free tier** - Network restrictions may block connection

1. **Get Pooler URL from Supabase**:
   - Go to Supabase → **Settings** → **Database** → **Connection Pooling**
   - Copy the **Transaction mode** connection string
   - Format: `postgresql://postgres.[ref]:[password]@aws-0-[region].pooler.supabase.com:6543/postgres`

2. **Convert to JDBC Format**:
   - From: `postgresql://user:password@pooler-host:6543/postgres`
   - To: `jdbc:postgresql://pooler-host:6543/postgres`

3. **Add Environment Variables**:
   | Key | Value |
   | :--- | :--- |
   | `DATABASE_URL` | `jdbc:postgresql://[pooler-host]:6543/postgres` |
   | `DATABASE_USER` | `postgres` |
   | `DATABASE_PASSWORD` | `[your-password]` |

---

#### Option D: Use External Supabase (Paid Render Plans Only)

Paid Render plans ($7+/month) have full network access and can connect directly to Supabase:

| Key | Value |
| :--- | :--- |
| `DATABASE_URL` | `jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres` |
| `DATABASE_USER` | `postgres` |
| `DATABASE_PASSWORD` | `[your-password]` |

**Important Notes**:
- Render automatically sets the `PORT` environment variable
- The application is configured to read from `${PORT:8000}`, so it will use Render's port automatically
- No need to manually configure the port

### Step 6: Deploy

1.  Click **Create Web Service**.

Render will now:
- Clone your repository
- Build the Docker image (this takes 5-10 minutes on first build due to Vaadin frontend compilation)
- Deploy the container
- Assign a public URL

Once deployment is complete, Render will provide a public URL like:
`https://javaside-app.onrender.com`

---

## Monitoring and Logs

### View Logs
1. Go to your service dashboard
2. Click **Logs** tab
3. You should see:
   ```
   ✓ Javaside Application Started Successfully!
   ✓ Dashboard available at: http://localhost:10000
   ```
   (Port 10000 is Render's default, but may vary)

### Check Health
- Render automatically performs health checks
- Your service status should show as **Live** with a green indicator

---

## ⚠️ Lecciones Aprendidas: Por Qué Fallan los Despliegues en Render

Esta sección documenta errores comunes que causan fallos en el despliegue, basados en experiencias reales con este proyecto.

### 🚫 Error #1: Intentar Usar Supabase en Free Tier

**Problema Común:**
```
Error: Connection refused
Network is unreachable
Could not connect to database
```

**Causa Raíz:**
- El **free tier de Render NO puede conectarse a bases de datos externas** como Supabase
- Render free tier tiene restricciones de red que bloquean conexiones salientes a servicios externos
- Esto incluye Supabase, AWS RDS, Google Cloud SQL, etc.

**Síntomas:**
- ✅ Build exitoso
- ✅ Contenedor inicia correctamente
- ❌ Aplicación falla al conectarse a la base de datos
- ❌ Logs muestran "Connection refused" o "Network unreachable"
- ❌ Health checks fallan

**Solución:**
1. **Usar Render PostgreSQL** (opción recomendada)
2. **Upgrade a plan pago** ($7/mes) para acceso a bases de datos externas
3. **Intentar Connection Pooler** de Supabase (60-70% éxito)

**Prevención:**
```bash
# ❌ NO USAR en Render Free Tier:
DATABASE_URL=jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres

# ✅ USAR en Render Free Tier:
DATABASE_URL=jdbc:postgresql://dpg-xxxxx-a/javaside  # Render PostgreSQL
```

---

### 🚫 Error #2: Variables de Entorno Mal Configuradas

**Problema Común:**
```
Error: Could not resolve placeholder 'DATABASE_URL'
Application failed to start
```

**Causa Raíz:**
- Variables de entorno no configuradas en el dashboard de Render
- Nombres de variables incorrectos (mayúsculas/minúsculas)
- Variables configuradas pero no guardadas

**Síntomas:**
- ✅ Build exitoso
- ❌ Aplicación falla al iniciar
- ❌ Logs muestran "placeholder" errors
- ❌ Spring Boot no puede resolver propiedades

**Solución:**
1. Ir a Render Dashboard → Tu servicio → **Environment**
2. Verificar que TODAS las variables estén configuradas:
   ```
   DATABASE_URL
   DATABASE_USER
   DATABASE_PASSWORD
   ```
3. Click **Save Changes** (importante!)
4. Redeploy manual si es necesario

**Prevención:**
- Usar el archivo `.env.render` como referencia
- Copiar y pegar valores exactos (sin espacios extra)
- Verificar que `render.yaml` liste todas las variables necesarias

---

### 🚫 Error #3: Usar SUPABASE_DB_* en Lugar de DATABASE_*

**Problema Común:**
```
Application starts but cannot connect to Render database
```

**Causa Raíz:**
- El proyecto está configurado con fallback: `${DATABASE_URL:${SUPABASE_DB_URL}}`
- Si configuras `SUPABASE_DB_URL` con credenciales de Supabase, la app intentará conectarse a Supabase (que falla en free tier)
- Render PostgreSQL requiere usar variables `DATABASE_*`

**Síntomas:**
- ✅ Build exitoso
- ✅ Aplicación inicia
- ❌ Conexión a base de datos falla
- ❌ Logs muestran intento de conexión a Supabase

**Solución:**
```bash
# ❌ INCORRECTO (intentará conectarse a Supabase):
SUPABASE_DB_URL=jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres
SUPABASE_DB_USER=postgres
SUPABASE_DB_PASSWORD=tu_password

# ✅ CORRECTO (usa Render PostgreSQL):
DATABASE_URL=jdbc:postgresql://dpg-xxxxx-a/javaside
DATABASE_USER=javaside
DATABASE_PASSWORD=render_password
```

**Prevención:**
- **Siempre usar `DATABASE_*` en producción**
- Reservar `SUPABASE_DB_*` solo para desarrollo local
- Verificar logs para confirmar qué base de datos se está usando

---

### 🚫 Error #4: Formato JDBC Incorrecto

**Problema Común:**
```
Invalid database URL format
Could not load driver class
```

**Causa Raíz:**
- Usar formato PostgreSQL nativo en lugar de JDBC
- Incluir usuario/password en la URL (no compatible con Spring Boot)

**Síntomas:**
- ❌ Aplicación falla al iniciar
- ❌ Logs muestran "Invalid URL" o "Driver not found"

**Solución:**
```bash
# ❌ INCORRECTO (formato PostgreSQL nativo):
DATABASE_URL=postgresql://javaside:password@dpg-xxxxx-a/javaside

# ✅ CORRECTO (formato JDBC):
DATABASE_URL=jdbc:postgresql://dpg-xxxxx-a/javaside
DATABASE_USER=javaside
DATABASE_PASSWORD=password
```

**Conversión:**
```
De:  postgresql://user:pass@host/database
A:   jdbc:postgresql://host/database
     + DATABASE_USER=user
     + DATABASE_PASSWORD=pass
```

---

### 🚫 Error #5: Múltiples Bases de Datos Free Tier

**Problema Común:**
```
Error: cannot have more than one active free tier database
```

**Causa Raíz:**
- Render free tier permite **solo UNA base de datos PostgreSQL gratuita** por cuenta
- Intentar crear una segunda base de datos falla

**Síntomas:**
- ❌ No puedes crear nueva base de datos
- ❌ Error al intentar provisionar PostgreSQL

**Solución:**
1. **Eliminar base de datos no usada:**
   - Dashboard → Base de datos antigua → Settings → Delete Database
2. **Reutilizar base de datos existente:**
   - Usar credenciales de la base de datos que ya tienes
3. **Upgrade a plan pago:**
   - $7/mes permite múltiples bases de datos

---

## ✅ Checklist de Despliegue Exitoso

Usa esta lista para evitar errores comunes:

### Antes de Desplegar
- [ ] Decidir: ¿Usar Render PostgreSQL o Supabase?
  - Free tier → **Render PostgreSQL obligatorio**
  - Plan pago → Puedes usar Supabase
- [ ] Verificar que solo tienes UNA base de datos free tier activa
- [ ] Confirmar formato JDBC correcto en variables

### Durante la Configuración
- [ ] Crear base de datos PostgreSQL en Render PRIMERO
- [ ] Copiar credenciales exactas (hostname, database, user, password)
- [ ] Convertir URL a formato JDBC
- [ ] Configurar variables `DATABASE_*` (NO `SUPABASE_DB_*`)
- [ ] Guardar cambios en Render dashboard

### Verificación Post-Despliegue
- [ ] Build completado sin errores
- [ ] Logs muestran: "✓ Usando variables de entorno del sistema (producción)"
- [ ] Logs muestran: "✓ Conexión exitosa a PostgreSQL"
- [ ] Health checks pasan (indicador verde)
- [ ] URL pública accesible
- [ ] Dashboard carga correctamente
- [ ] Botón "Probar Conexión a BD" funciona

---

## 🎯 Recomendaciones Finales

### Para Free Tier
1. **SIEMPRE usa Render PostgreSQL** - No intentes conectar a Supabase
2. **Usa variables `DATABASE_*`** - No uses `SUPABASE_DB_*`
3. **Verifica formato JDBC** - Debe empezar con `jdbc:postgresql://`
4. **Una sola base de datos** - Elimina las que no uses

### Para Plan Pago ($7/mes)
1. Puedes usar Supabase directamente
2. Múltiples bases de datos permitidas
3. Sin restricciones de red
4. Sin sleep después de inactividad

### Migración de Supabase a Render
Si ya tienes datos en Supabase:
1. Exportar datos: `pg_dump` desde Supabase
2. Crear base de datos en Render
3. Importar datos: `psql` a Render PostgreSQL
4. Actualizar variables de entorno
5. Redeploy

---

## Troubleshooting

### Build Fails or Takes Too Long

**Issue**: Build times out or fails during Maven build.

**Solutions**:
1. The first build takes 5-10 minutes due to Vaadin frontend compilation - this is normal
2. Check the build logs for specific errors
3. Ensure `vaadin-maven-plugin` is properly configured in `pom.xml`

### Application Shows 500 Error

**Issue**: Service is running but returns HTTP 500 when accessed.

**Solutions**:
1. Check logs for Vaadin-related errors
2. Verify `vaadin.productionMode=true` is set in `application.properties`
3. Ensure all environment variables are correctly set
4. Check database connection string format

### Database Connection Fails

**Issue**: Application starts but can't connect to database.

**Solutions**:

**For Render Managed PostgreSQL**:
1. Verify you're using the **Internal Database URL** (not External)
2. Ensure JDBC format is correct: `jdbc:postgresql://host/database`
3. Check that web service and database are in the same region
4. Verify username and password match database credentials

**For External Supabase (Free Tier)**:
1. **Network is unreachable** error is common on Render free tier
2. Try Option B (Connection Pooler) from Step 5
3. Or upgrade to paid Render plan for external database access

**For Supabase Connection Pooler**:
1. Ensure you're using port 6543 (not 5432)
2. Verify pooler URL format: `jdbc:postgresql://pooler-host:6543/postgres`
3. Check Supabase pooler is enabled in your project settings

### Port Binding Issues

**Issue**: Application fails to start with port-related errors.

**Solution**:
- The application is configured to use `${PORT:8000}`
- Render automatically sets the `PORT` environment variable
- No manual configuration needed
- If issues persist, check that `server.port=${PORT:8000}` is in `application.properties`

---

## Updating Your Deployment

### Automatic Updates
If you enabled **Auto-Deploy**:
1. Push changes to your `main` branch
2. Render automatically rebuilds and redeploys
3. Zero-downtime deployment

### Manual Updates
1. Go to your service dashboard
2. Click **Manual Deploy** → **Deploy latest commit**
3. Render will rebuild and redeploy

---

## Render vs Koyeb Comparison

| Feature | Render | Koyeb |
| :--- | :--- | :--- |
| **Port Configuration** | Automatic via `PORT` env var | Fixed at 8000 |
| **Free Tier** | 750 hours/month | Limited free tier |
| **Build Time** | 5-10 minutes (first build) | 5-10 minutes (first build) |
| **Auto-Deploy** | Yes (configurable) | Yes (via GitHub) |
| **Database** | Managed PostgreSQL available | External only |
| **Custom Domains** | Yes (paid plans) | Yes |

---

## Success Checklist

After deployment, verify:
- [ ] Service status shows **Live**
- [ ] Public URL is accessible
- [ ] Dashboard loads without errors
- [ ] "Test Connection" button works
- [ ] Database connection successful
- [ ] Logs show correct port
- [ ] No Vaadin development mode errors

---

## Next Steps

1. **Custom Domain**: Add a custom domain in Render dashboard (paid plans)
2. **Environment-Specific Configs**: Create separate services for staging/production
3. **Monitoring**: Set up alerts for service health
4. **Scaling**: Upgrade to paid tier for better performance and multiple instances

---

Your application is now deployed on Render and connected to your Supabase database!
