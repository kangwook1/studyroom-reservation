FROM bellsoft/liberica-openjdk-alpine:17 AS builder
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src
RUN ./gradlew clean build -x test

FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
