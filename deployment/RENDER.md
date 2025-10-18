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

### Step 5: Add Environment Variables

This is critical for connecting to your Supabase database.

Click **Advanced** and add the following environment variables:

| Key | Value | Notes |
| :--- | :--- | :--- |
| `SUPABASE_DB_URL` | `jdbc:postgresql://db.hgcesbylhkjoxtymxysy.supabase.co:5432/postgres` | Replace with your Supabase host and database name |
| `SUPABASE_DB_USER` | `postgres` | Your Supabase database user |
| `SUPABASE_DB_PASSWORD` | `your_password_here` | Your Supabase database password |

**Where to find these values?**
- Go to your Supabase project → **Project Settings** → **Database**
- Under **Connection string**, you will find the Host, Database name, and User

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

**Issue**: Application starts but can't connect to Supabase.

**Solutions**:
1. Verify environment variables are correctly set
2. Check Supabase connection string format: `jdbc:postgresql://HOST:5432/DATABASE`
3. Ensure Supabase allows connections from Render's IP ranges
4. Test connection string locally first

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
