FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:17-jre

WORKDIR /app

RUN groupadd -r appgroup && useradd -r -g appgroup appuser

COPY --from=builder /app/target/remember-me-1.0.0.jar app.jar

RUN mkdir -p /app/data && chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker
ENV HIBERNATE_H2_FILE_PATH=/app/data/wordlearner

ENTRYPOINT ["java", "-jar", "app.jar"]