# Multi-stage build for Member Service
# Stage 1: Build with Maven
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory
WORKDIR /build

# Copy the entire project to satisfy parent POM reactor requirements
COPY . .

# Build the application
# Skip tests during Docker build for faster builds
RUN mvn clean package -pl member -am -DskipTests

# Stage 2: Runtime with JRE (smaller image)
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /build/member/target/*.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose the service port
EXPOSE 8081

# Set default profile to docker
ENV SPRING_PROFILES_ACTIVE=docker

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
