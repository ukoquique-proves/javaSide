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

For its combination of an integrated free database, ease of use, and excellent developer experience, **Render is the clear winner** and the recommended platform for deploying the Javaside application.

## Deployment Instructions

Choose a platform from the list below and follow the corresponding guide for step-by-step deployment instructions.

- **[Koyeb](./KOYEB.md)**: Our current recommended platform. This guide explains how to deploy the application using Docker and connect it to a Supabase database.

- **Render**: (Guide to be created if needed). Render was our initial choice, and a `render.yaml` file can be quickly generated if we decide to use it in the future.
