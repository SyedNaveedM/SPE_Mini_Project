# Stage 1: Build the Spring Boot application
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration first to improve dependency caching
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy application source and package the application
COPY src ./src
RUN mvn -B clean package -DskipTests

# Stage 2: Run the application using a smaller JRE image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Run the application as a non-root user
RUN useradd --system --uid 10001 appuser

COPY --from=build /app/target/*.jar app.jar

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

USER 10001

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]