# Deploying to Koyeb

This guide provides step-by-step instructions for deploying the Javaside application to Koyeb using Docker and connecting it to a Supabase database.

## Prerequisites

1.  **GitHub Repository**: The project code must be pushed to a GitHub repository.
2.  **Koyeb Account**: A free account on [Koyeb](https://www.koyeb.com/).
3.  **Supabase Account**: A Supabase project with a PostgreSQL database. You will need your database connection details (URL, user, and password).

---

## Deployment Steps

### Step 1: Create a New App on Koyeb

1.  Log in to your Koyeb account.
2.  On the **Overview** page, click **Create App**.

### Step 2: Choose the Deployment Method

1.  Select **GitHub** as the deployment method.
2.  If you haven't already, install the Koyeb GitHub App and grant it access to your repository.
3.  Choose your project repository from the list.

### Step 3: Configure the Service

Koyeb will automatically detect the `Dockerfile` in your repository.

1.  **Deployment Method**: Ensure **Dockerfile** is selected.
2.  **App and Service Names**: You can keep the default names or change them (e.g., `javaside-app`).
3.  **Port**: Koyeb automatically detects the `EXPOSE` instruction from the `Dockerfile`. It should be set to `8080`.

### Step 4: Add Environment Variables

This is the most important step for connecting to your Supabase database.

1.  Scroll down to the **Environment variables** section.
2.  Click **Add Variable** and add the following three secrets. Make sure to select the **Secret** type for the password to keep it secure.

    | Name | Type | Value |
    | :--- | :--- | :--- |
    | `SUPABASE_DB_URL` | `Value` | `jdbc:postgresql://<YOUR_SUPABASE_HOST>:5432/<YOUR_DB_NAME>` |
    | `SUPABASE_DB_USER` | `Value` | `<YOUR_SUPABASE_DB_USER>` |
    | `SUPABASE_DB_PASSWORD` | `Secret` | `<YOUR_SUPABASE_DB_PASSWORD>` |

    **Where to find these values?**
    - Go to your Supabase project -> **Project Settings** -> **Database**.
    - Under **Connection string**, you will find the Host, Database name, and User.

4.  Add one more environment variable to ensure Spring Boot uses the correct dialect for PostgreSQL:

    | Name | Type | Value |
    | :--- | :--- | :--- |
    | `spring.jpa.properties.hibernate.dialect` | `Value` | `org.hibernate.dialect.PostgreSQLDialect` |

### Step 5: Deploy

1.  Click the **Deploy** button.

Koyeb will now start building your application from the `Dockerfile` and deploy it. The process may take a few minutes.

Once the deployment is complete, Koyeb will provide a public URL (e.g., `https://javaside-app-your-org.koyeb.app`) where you can access your live application.

---

That's it! Your application is now deployed on Koyeb and connected to your Supabase database.
