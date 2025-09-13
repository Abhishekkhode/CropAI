# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Copy jar from target folder
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
