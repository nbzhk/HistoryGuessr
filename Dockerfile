# Start with a base OpenJDK image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/HistoryGuessr-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app listens on (default 8080)
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
