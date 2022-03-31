# syntax=docker/dockerfile:1
FROM bellsoft/liberica-openjdk-alpine-musl:17 as base
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run", "-Dspring.profiles.active=dev"]