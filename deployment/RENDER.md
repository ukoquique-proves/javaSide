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

**Important**: Render's free tier cannot connect to external databases like Supabase due to network restrictions. You have two options:

#### Option A: Use Render Managed PostgreSQL (Recommended for Free Tier)

1. **Create PostgreSQL Database**:
   - In Render Dashboard → **New +** → **PostgreSQL**
   - **Name**: `javaside-db`
   - **Database**: `javaside`
   - **User**: `javaside`
   - **Region**: Same as your web service
   - **PostgreSQL Version**: 16
   - **Plan**: **Free**
   - Click **Create Database**

2. **Get Connection Details**:
   - Once created, copy the **Internal Database URL**
   - Format: `postgresql://user:password@host/database`
   - Example: `postgresql://javaside_user:abc123@dpg-xxxxx-a/javaside_db`

3. **Convert to JDBC Format**:
   - From: `postgresql://user:password@host/database`
   - To: `jdbc:postgresql://host/database`
   - Example: `jdbc:postgresql://dpg-xxxxx-a/javaside_db`

4. **Add Environment Variables**:
   Click **Advanced** in your web service and add:

   | Key | Value |
   | :--- | :--- |
   | `SUPABASE_DB_URL` | `jdbc:postgresql://[internal-host]/[database-name]` |
   | `SUPABASE_DB_USER` | `[database-user]` |
   | `SUPABASE_DB_PASSWORD` | `[database-password]` |

5. **Database Table Creation**:
   - Hibernate will auto-create tables with `spring.jpa.hibernate.ddl-auto=update`
   - Or manually run SQL from `database/step2_connection_test.sql`

#### Option B: Use Supabase Connection Pooler (May Work)

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
   | `SUPABASE_DB_URL` | `jdbc:postgresql://[pooler-host]:6543/postgres` |
   | `SUPABASE_DB_USER` | `postgres` |
   | `SUPABASE_DB_PASSWORD` | `[your-password]` |

**Note**: Connection pooler success rate is ~60-70% on Render free tier.

#### Option C: Use External Supabase (Paid Render Plans Only)

Paid Render plans have full network access and can connect directly to Supabase:

| Key | Value |
| :--- | :--- |
| `SUPABASE_DB_URL` | `jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres` |
| `SUPABASE_DB_USER` | `postgres` |
| `SUPABASE_DB_PASSWORD` | `[your-password]` |

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
