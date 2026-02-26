FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon > /dev/null 2>&1 || true

COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]