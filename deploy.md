# Deployment Guide (Render + Aiven PostgreSQL)

This guide walks you through deploying **bb-erp-be** using Docker on Render and connecting it to a managed Aiven PostgreSQL database.

## 1. Aiven PostgreSQL Setup

You already have an Aiven PostgreSQL service running. Based on your provided credentials, here is the exact database URL format you need for Spring Boot:

**Spring Boot JDBC URL (`DB_URL`)**:
```
jdbc:postgresql://pg-379b0e0c-aharsh236-afc6.l.aivencloud.com:27791/defaultdb?sslmode=require
```

*Note: The `?sslmode=require` parameter is strictly required by Aiven to accept the connection.*

## 2. Deploying to Render via Docker

Since the repository contains a `Dockerfile`, Render can natively build and deploy it.

1. Log in to [Render](https://render.com/).
2. Click **New +** and select **Web Service**.
3. Connect your GitHub account and select the repository `CandidateMaster2002/bb-erp-be`.
4. In the service settings:
   - **Name**: `bb-erp-be` (or your preference)
   - **Runtime**: `Docker` (Render should automatically detect this because of the `Dockerfile`).
   - **Branch**: `main` (or `master`)
   - **Region**: Select the region closest to your Aiven database to minimize latency.
   - **Plan**: Free or Starter (Starter recommended for production).

## 3. Environment Variables (Render)

Scroll down to the **Environment Variables** section on Render and add the following keys. These are critical for the app to start:

| Key | Value | Description |
| :--- | :--- | :--- |
| `SPRING_PROFILES_ACTIVE` | `prod` | Activates production optimizations (disables SQL logging). |
| `DB_URL` | `jdbc:postgresql://pg-379b0e0c-aharsh236-afc6.l.aivencloud.com:27791/defaultdb?sslmode=require` | Your Aiven JDBC URL. |
| `DB_USER` | `avnadmin` | Your Aiven DB user. |
| `DB_PASSWORD` | `[YOUR_AIVEN_PASSWORD]` | Your Aiven DB password. |
| `JWT_SECRET` | *(Generate a long random string)* | Used for tokens (even if basic auth is currently used, it satisfies config). |
| `CORS_ALLOWED_ORIGINS` | `https://your-frontend-netlify-url.netlify.app` | Comma-separated list of allowed frontend domains. |
| `ADMIN_EMAIL` | `admin@boltblazers.com` | Email for the initial admin seed. |
| `ADMIN_PASSWORD` | `SecurePassword123!` | Password for the initial admin seed. |

*(Note: Render automatically injects a `PORT` environment variable, which our Dockerfile and `application.yml` are configured to listen on).*

## 4. Health Check Path Configuration

Render uses health checks to know when your app has successfully started and is ready to receive traffic (and for zero-downtime deployments).

1. In your Render Web Service settings, scroll down to **Advanced**.
2. Find the **Health Check Path** field.
3. Enter `/api/health`
4. Save Changes. Render will now ping this endpoint (which returns `{"status":"UP"}`) to verify deployment success.

## 5. Continuous Deployment

Once deployed, Render will automatically listen to pushes on your GitHub `main` branch. 
Additionally, we have added a GitHub Actions workflow (`.github/workflows/build.yml`) that runs tests on every push/PR to ensure broken code doesn't make it to `main`.
