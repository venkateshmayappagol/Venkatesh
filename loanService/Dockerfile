# Use official OpenJDK image as the base image for building
FROM maven:2.5.5-jdk-17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file into the container
COPY pom.xml .

# Download the dependencies needed for building the application
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src ./src

# Build the application
RUN mvn package

# Use a lightweight base image for the application runtime
FROM openjdk:17-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged Spring Boot application from the builder stage into the container
COPY --from=builder /app/target/loanservice .

# Expose the port the application runs on
EXPOSE 8080

# Specify the command to run the application
CMD ["java", "-jar", "loanservice.jar"]
