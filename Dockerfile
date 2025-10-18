# Use a multi-stage build to create a lean final image

# --- Build Stage ---
# Use a Maven image to build the application .jar file
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application, skipping tests for faster builds
RUN mvn clean install -DskipTests

# --- Run Stage ---
# Use a slim Java runtime image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the executable .jar file from the build stage
COPY --from=build /app/target/javaside-1.0.0.jar ./app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the command to run the application
# The database credentials will be passed as environment variables by Render
ENTRYPOINT ["java", "-jar", "app.jar"]
