FROM maven:3.9.16-eclipse-temurin-25 AS builder

WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp clean package

FROM eclipse-temurin:25.0.4.1_1-jre

WORKDIR /app
COPY --from=builder /workspace/target/monolith-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
