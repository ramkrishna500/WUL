# Use OpenJDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR into container
COPY target/apigateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port this service runs on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]