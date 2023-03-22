# Use a base image with the appropriate Java version and operating system
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Expose the port that the microservice will listen on
EXPOSE 8080

# Start the Spring Boot application
CMD ["java", "-jar", "app.jar"]
