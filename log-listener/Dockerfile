# Use Maven image to build the application
FROM maven:3.8.4-openjdk-17 as builder

# Create app directory in container
WORKDIR /app

# Assuming the parent pom.xml is needed, copy it into the container
COPY pom.xml .

# Copy the log-listener module specifics
COPY log-listener/pom.xml log-listener/pom.xml
COPY log-listener/src log-listener/src

# Navigate to the log-listener module directory
WORKDIR /app/log-listener

# Build the log-listener module specifically
RUN mvn clean package -DskipTests

# Start with a clean slate for the runtime image
FROM openjdk:17-jdk

# Copy the built jar file from the builder stage
COPY --from=builder /app/log-listener/target/log-listener-*.jar /log-listener.jar

# Command to run the application
CMD ["java", "-jar", "/log-listener.jar"]