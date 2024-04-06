# Use Maven image to build the application
FROM maven:3.8.4-openjdk-17 as builder

# Create app directory in container
WORKDIR /app

# Copy the parent project files to the container
# Note: This assumes the pom.xml has configurations that won't interfere with building just the log-creator module.
COPY pom.xml .

# Copy log-creator module
COPY log-creator/pom.xml log-creator/pom.xml
COPY log-creator/src log-creator/src

# Navigate to the log-creator module directory
WORKDIR /app/log-creator

# Build the log-creator module specifically
RUN mvn clean package -DskipTests

# Start with a clean slate for the runtime image
FROM openjdk:17-jdk

# Copy the built jar file from the builder stage
COPY --from=builder /app/log-creator/target/log-creator-*.jar /log-creator.jar

# Command to run the application
CMD ["java", "-jar", "/log-creator.jar"]