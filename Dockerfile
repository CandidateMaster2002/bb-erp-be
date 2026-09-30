# Use a Maven image with Java 21 to build the app
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Use a lightweight JRE image for the runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Expose the port (Render provides $PORT environment variable)
EXPOSE ${PORT:-8080}
# Run the application
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
