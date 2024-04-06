# Use Maven image to build the application
FROM maven:3.8.4-openjdk-17 as builder

# Create app directory in container
WORKDIR /app

# Copy the parent project files to the container
# Note: This assumes the pom.xml has configurations that won't interfere with building just the log-consumer module.
COPY pom.xml .

# Copy log-consumer module
COPY log-consumer/pom.xml log-consumer/pom.xml
COPY log-consumer/src log-consumer/src

# Navigate to the log-consumer module directory
WORKDIR /app/log-consumer

# Build the log-consumer module specifically
RUN mvn clean package -DskipTests

# Start with a clean slate for the runtime image
FROM openjdk:17-jdk

# Copy the built jar file from the builder stage
COPY --from=builder /app/log-consumer/target/log-consumer-*.jar /log-consumer.jar

# Command to run the application
CMD ["java", "-jar", "/log-consumer.jar"]