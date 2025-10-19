# Deployment Guides

This directory contains all the necessary files and documentation for deploying the Javaside application to various cloud platforms.

## Platform Information

This section provides a detailed comparison of different cloud platforms and explains the reasoning behind our recommended choices.

### Key Requirements

- **Free Tier**: Must offer a reliable free tier for both the application and a PostgreSQL database.
- **Ease of Use**: Should be simple to configure and manage.
- **Automation**: Should support automated builds and deployments from a Git repository.

---

## The Ranking

### 1. Render (Most Attractive)

**Render is the top choice because it perfectly balances simplicity, features, and a generous free tier.**

- **Why it's #1**: Render's main advantage is its **all-in-one free tier**, which includes both a web service and a PostgreSQL database. This eliminates the need to manage a separate database provider. The developer experience is excellent, with deployments automated via a simple `render.yaml` file. It is the fastest and most efficient option for this project.

### 2. Koyeb

**Koyeb is a strong runner-up, offering a similar developer-friendly experience to Render.**

- **Why it's #2**: Koyeb is also extremely easy to use and offers a great free tier for web services. However, it loses the top spot because its free tier **does not include a managed database**. We would need to connect to an external database (like Supabase), which adds a small amount of complexity compared to Render's integrated solution.

### 3. Linode (Akamai) - Recommended Alternative

**Linode is an IaaS (Infrastructure as a Service) provider that offers a free trial without requiring a credit card.**

- **Why it's a good alternative**: While platforms like Render or Koyeb (PaaS) are simpler, Linode provides a full virtual server (IaaS). This gives us complete control to install Docker and configure the environment manually. Most importantly, their free trial signup process does not mandate a credit card, making it the most accessible option.
- **Complexity**: It's more hands-on than a PaaS. We are responsible for setting up the server, installing dependencies (like Docker), and managing the firewall.

### 4. alwaysdata

**alwaysdata is a hosting platform that supports Java and does not require a credit card, but has significant limitations.**

- **Pros**: It offers a free tier without requiring a credit card for signup and supports multiple Java versions.
- **Cons**: The free plan **does not support Docker**. This means we would need to deploy the application as a standard Java process, not as a container, which would require a different deployment strategy.

### 5. domcloud.co

**domcloud.co is another platform that does not require a credit card but also does not support Docker on its free plan.**

- **Pros**: No credit card required for signup.
- **Cons**: Docker support is only available on paid plans. The free plan also has a restrictive firewall that would likely block the connection to our external Supabase database.

### 6. Google Cloud Platform (GCP) - Cloud Run

**GCP is powerful but requires a credit card for its free tier, making it unsuitable for this project's constraints.**

- **Why it's #4**: Although Cloud Run has a generous "Always Free" tier, it requires a billing account to be set up, which involves providing a credit card. This was a deal-breaker.

### 5. Azure / Oracle Cloud (Least Attractive)

**These platforms are enterprise-grade and far too complex for this project's needs.**

- **Why they are last**: Similar to GCP, they often require billing information. Their complexity, steep learning curve, and enterprise focus make them the least suitable options for a quick and simple deployment.

---

## Final Decision

**Render was chosen for this deployment** due to its integrated PostgreSQL database solution and excellent developer experience. The application is successfully deployed and running at:

**Live URL**: https://javaside.onrender.com

## Deployment Instructions

Choose a platform from the list below and follow the corresponding guide for step-by-step deployment instructions.

- **[Render](./RENDER.md)**: ✅ **Currently deployed**. Complete guide with `render.yaml` configuration included. Supports automatic port detection and integrated PostgreSQL.

- **[Koyeb](./KOYEB.md)**: ✅ **Previously deployed**. Alternative platform with excellent Docker support.

## Current Deployment Status

- **Platform**: Render
- **Status**: ✅ Live and Running
- **URL**: https://javaside.onrender.com
- **Database**: Render PostgreSQL (internal network)
- **Port**: Dynamic (via PORT env var)
- **Container**: Docker with multi-stage build
- **Frontend**: Vaadin production mode with pre-built bundle
- **Auto-deploy**: Enabled from GitHub

---

## Critical Issues Encountered During Deployment

This section documents all issues encountered during the Koyeb deployment. **These issues are likely to occur on ANY platform** (Render, GCP, Azure, etc.) when deploying a Vaadin Spring Boot application.

### 1. Vaadin Development Mode in Production ❌

**Problem**: Application crashed with error:
```
Failed to determine project directory for dev mode
IllegalStateException: Directory '/app' does not look like a Maven or Gradle project
```

**Root Cause**: Vaadin was attempting to run in development mode in the production container. Development mode requires Maven/Gradle project files and Node.js, which don't exist in the final Docker image.

**Solution**:
- Add `vaadin.productionMode=true` to `application.properties`
- Configure `vaadin-maven-plugin` in `pom.xml` with goals:
  - `prepare-frontend`
  - `build-frontend`
- These goals must run during the `compile` phase

**Impact**: This is a **critical issue** that will affect **all platforms** (Render, GCP, Azure, Oracle Cloud). Without this configuration, the application will fail to start.

### 2. Missing Vaadin Production Bundle ❌

**Problem**: Application returned HTTP 500 errors when accessed, even though health checks passed.

**Root Cause**: The Vaadin frontend resources were not compiled into a production bundle during the Maven build.

**Solution**:
```xml
<plugin>
    <groupId>com.vaadin</groupId>
    <artifactId>vaadin-maven-plugin</artifactId>
    <version>${vaadin.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-frontend</goal>
                <goal>build-frontend</goal>
            </goals>
            <phase>compile</phase>
        </execution>
    </executions>
</plugin>
```

**Build Time Impact**: The first build will take 5-10 minutes due to:
- Node.js and npm installation
- Frontend dependency downloads
- Webpack compilation of Vaadin components

**Impact**: This issue will occur on **all platforms** that use Docker or JAR deployment.

### 3. Port Configuration Mismatch ❌

**Problem**: Application health checks failed. Logs showed the app was running, but the platform couldn't connect.

**Root Cause**: The application was configured to run on port 8080, but the platform (Koyeb) expected port 8000.

**Solution**:
- Update `server.port=8000` in `application.properties`
- Update `EXPOSE 8000` in `Dockerfile`
- Ensure platform configuration matches

**Platform-Specific Notes**:
- **Koyeb**: Defaults to port 8000
- **Render**: Typically uses port 10000 (configurable)
- **GCP Cloud Run**: Uses port 8080 by default
- **Azure/Oracle**: Configurable, typically 8080

**Recommendation**: Make the port configurable via environment variable:
```properties
server.port=${PORT:8080}
```

**✅ This project now uses dynamic port configuration**: `server.port=${PORT:8000}` - automatically adapts to any platform.

### 4. Dynamic Port Message Issue ⚠️

**Problem**: Startup logs showed incorrect port (hardcoded to 8080).

**Root Cause**: The startup message was hardcoded instead of reading from configuration.

**Solution**: Read the port dynamically from Spring Environment:
```java
ConfigurableApplicationContext context = SpringApplication.run(JavasideApplication.class, args);
Environment env = context.getEnvironment();
String port = env.getProperty("server.port", "8080");
System.out.println("✓ Dashboard available at: http://localhost:" + port);
```

**Impact**: Cosmetic issue, but can cause confusion during debugging.

---

## Platform-Specific Deployment Guides

### Deploying to Render

**✅ Currently Deployed Platform** - See [RENDER.md](./RENDER.md) for complete guide.

**Critical Requirements**:

1. **Database Format Conversion (REQUIRED)**: Render provides PostgreSQL URL in native format, but Spring Boot requires JDBC format:
   ```bash
   # ❌ Render gives you this (PostgreSQL native):
   postgresql://user:password@dpg-xxxxx-a/database
   
   # ✅ You MUST convert to JDBC format:
   jdbc:postgresql://dpg-xxxxx-a/database
   # Then set user and password separately:
   DATABASE_USER=user
   DATABASE_PASSWORD=password
   ```

2. **Free Tier Database Restrictions**:
   - ❌ **CANNOT connect to external databases** (Supabase, AWS RDS, etc.)
   - ✅ **MUST use Render PostgreSQL** for free tier
   - ⚠️ Only **ONE free database** allowed per account
   - See [RENDER.md - Error #1](./RENDER.md#-error-1-intentar-usar-supabase-en-free-tier) for details

3. **Variable Naming Convention**:
   - ✅ Use `DATABASE_*` variables in production (Render)
   - ❌ Do NOT use `SUPABASE_DB_*` variables (will try to connect to Supabase)
   - See [RENDER.md - Error #3](./RENDER.md#-error-3-usar-supabase_db_-en-lugar-de-database_) for details

4. **Automatic Port Detection**: Render sets a `PORT` environment variable. Your application must read from it:
   ```properties
   server.port=${PORT:8000}
   ```

5. **Vaadin Production Mode**: Ensure `vaadin.productionMode=true` and the Maven plugin is configured.

**Common Deployment Failures**:
- **Error #1**: Attempting to use Supabase on free tier → Network restrictions block connection
- **Error #2**: Misconfigured environment variables → App fails to start
- **Error #3**: Using `SUPABASE_DB_*` instead of `DATABASE_*` → Connects to wrong database
- **Error #4**: Incorrect JDBC URL format → Driver not found
- **Error #5**: Multiple free tier databases → Cannot create new database

**Full troubleshooting guide**: See [RENDER.md - Lecciones Aprendidas](./RENDER.md#️-lecciones-aprendidas-por-qué-fallan-los-despliegues-en-render)

**Render Deployment Checklist**:
- [ ] `vaadin.productionMode=true` in `application.properties`
- [ ] `vaadin-maven-plugin` configured in `pom.xml`
- [ ] `server.port=${PORT:8000}` for dynamic port binding
- [ ] Database URL converted to JDBC format (`jdbc:postgresql://...`)
- [ ] Using `DATABASE_*` variables (NOT `SUPABASE_DB_*`)
- [ ] Only one free tier database in account
- [ ] `render.yaml` with correct build and start commands
- [ ] Environment variables set and SAVED in Render dashboard

### Deploying to GCP Cloud Run

**Known Issues with GCP**:

1. **Port 8080 Required**: Cloud Run expects applications to listen on port 8080. Set:
   ```properties
   server.port=${PORT:8080}
   ```

2. **Container Registry**: You must push your Docker image to Google Container Registry (GCR) or Artifact Registry.

3. **IAM Permissions**: Ensure the Cloud Run service account has permissions to access Cloud SQL if using a managed database.

4. **Cold Starts**: Vaadin applications can have slow cold starts. Consider using minimum instances to keep the service warm.

**GCP Deployment Checklist**:
- [ ] `vaadin.productionMode=true` in `application.properties`
- [ ] `vaadin-maven-plugin` configured in `pom.xml`
- [ ] Docker image pushed to GCR/Artifact Registry
- [ ] `PORT` environment variable set to 8080
- [ ] Cloud SQL connection configured (if applicable)
- [ ] IAM roles properly configured

### Deploying to Azure App Service

**Known Issues with Azure**:

1. **Java Version**: Ensure the Azure runtime matches your Java version (17 in this project).

2. **Startup Time**: Azure may timeout if the application takes too long to start. Vaadin's frontend build can cause this. Consider:
   - Pre-building the frontend locally
   - Increasing the startup timeout in Azure settings

3. **Application Settings**: Database credentials must be set as Application Settings (environment variables).

**Azure Deployment Checklist**:
- [ ] `vaadin.productionMode=true` in `application.properties`
- [ ] `vaadin-maven-plugin` configured in `pom.xml`
- [ ] Java 17 runtime selected in Azure
- [ ] Application Settings configured for database
- [ ] Startup timeout increased if needed

---

## Universal Deployment Checklist

Use this checklist for **any platform**:

### Pre-Deployment
- [ ] `vaadin.productionMode=true` set in `application.properties`
- [ ] `vaadin-maven-plugin` configured in `pom.xml` with `prepare-frontend` and `build-frontend` goals
- [ ] Port configuration matches platform requirements
- [ ] Database connection string format verified for platform
- [ ] All environment variables documented

### Docker-Specific
- [ ] Multi-stage build to reduce image size
- [ ] `EXPOSE` instruction matches application port
- [ ] `.dockerignore` file excludes unnecessary files
- [ ] Base image uses appropriate Java version (17)

### Testing
- [ ] Application builds successfully locally: `mvn clean install`
- [ ] Docker image builds successfully: `docker build -t test .`
- [ ] Application runs in Docker locally: `docker run -p 8000:8000 test`
- [ ] Database connection works with environment variables
- [ ] Frontend loads correctly (no 500 errors)

### Post-Deployment
- [ ] Health checks pass
- [ ] Application accessible via public URL
- [ ] Database connection successful
- [ ] Logs show correct port
- [ ] No Vaadin development mode errors

---

## Lessons Learned

1. **Vaadin Production Mode is Critical**: This is the #1 issue that will break deployments across all platforms. Always configure it before deploying.

2. **Port Configuration Varies**: Each platform has different port expectations. Use environment variables for flexibility.

3. **Build Time Matters**: The first Vaadin production build is slow (5-10 minutes). Plan accordingly and don't assume the build failed.

4. **Test Locally First**: Always test the Docker container locally before pushing to any platform.

5. **Document Everything**: Keep track of environment variables, port configurations, and platform-specific quirks.
