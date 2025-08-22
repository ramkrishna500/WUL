# Use OpenJDK 21 image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy JAR into container
COPY target/*.jar app.jar

# Expose the port this service runs on
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=docker"]