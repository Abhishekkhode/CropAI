# Use official OpenJDK 17 image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy the built jar file into the container
COPY target/CropAI-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Pass environment variables from Docker or Render
ENV TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID}
ENV TWILIO_AUTH_TOKEN=${TWILIO_AUTH_TOKEN}
ENV TWILIO_PHONE_NUMBER=${TWILIO_PHONE_NUMBER}

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
