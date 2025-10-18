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

### 3. Google Cloud Platform (GCP) - Cloud Run

**GCP is a powerful option but is more complex than developer-focused platforms.**

- **Why it's #3**: GCP's "Always Free" tier is generous, and Cloud Run is an excellent serverless platform for running containers. However, the setup is significantly more involved. It requires managing IAM permissions, project configurations, and potentially a separate Cloud SQL database instance. While powerful, this complexity makes it less attractive for quick, simple deployments.

### 4. Azure / Oracle Cloud (Least Attractive)

**These platforms are powerful but are overkill and too complex for this project's needs.**

- **Why they are last**: Azure and Oracle Cloud are enterprise-grade platforms. While their free tiers can be generous (especially Oracle's "Always Free" VMs), they come with the steepest learning curve and the most complex setup process. Navigating their dashboards, networking, and security rules is far more work than is necessary for a project of this scale, making them the least attractive options for rapid and easy deployment.

---

## Final Decision

While Render offers an integrated database solution, **Koyeb was chosen for this deployment** due to its excellent Docker support and seamless GitHub integration. The application is successfully deployed and running at:

**Live URL**: https://mixed-trixi-teledigitos-565be96c.koyeb.app

## Deployment Instructions

Choose a platform from the list below and follow the corresponding guide for step-by-step deployment instructions.

- **[Koyeb](./KOYEB.md)**: ✅ **Currently deployed**. This guide explains how to deploy the application using Docker and connect it to a Supabase database.

- **Render**: Alternative option with integrated database. A `render.yaml` file can be quickly generated if needed in the future.

## Current Deployment Status

- **Platform**: Koyeb
- **Status**: ✅ Healthy and Running
- **URL**: https://mixed-trixi-teledigitos-565be96c.koyeb.app
- **Database**: Supabase PostgreSQL (external)
- **Port**: 8000
- **Container**: Docker with multi-stage build
- **Frontend**: Vaadin production mode with pre-built bundle

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

**Known Issues with Render**:

1. **Automatic Port Detection**: Render sets a `PORT` environment variable. Your application must read from it:
   ```properties
   server.port=${PORT:10000}
   ```

2. **Build Command**: Render may require explicit build commands in `render.yaml`:
   ```yaml
   buildCommand: mvn clean install -DskipTests
   ```

3. **Vaadin Production Mode**: Same issue as Koyeb. Ensure `vaadin.productionMode=true` and the Maven plugin is configured.

4. **Database Connection**: If using Render's managed PostgreSQL, the connection string format may differ slightly. Verify the JDBC URL format.

**Render Deployment Checklist**:
- [ ] `vaadin.productionMode=true` in `application.properties`
- [ ] `vaadin-maven-plugin` configured in `pom.xml`
- [ ] `server.port=${PORT:10000}` for dynamic port binding
- [ ] `render.yaml` with correct build and start commands
- [ ] Environment variables set for database connection

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
