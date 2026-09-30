# BoltBlazers ERP Backend

## Setup and Local Run

1. Ensure Java 21 and Maven are installed.
2. Run PostgreSQL locally and create a database named `bb_erp`.
3. Copy `.env.example` to `.env` and adjust the values if needed.
4. Load environment variables. In PowerShell, you can use:
   ```powershell
   Get-Content .env | ForEach-Object {
       if ($_ -match '^(.*?)=(.*)$') {
           [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
       }
   }
   ```
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Documentation

Swagger UI is available at:
`http://localhost:8080/swagger-ui/index.html`

Health Check:
`http://localhost:8080/api/health`
