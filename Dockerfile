FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
RUN gradle build -x test --no-daemon > /dev/null 2>&1 || true

COPY . .
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

#강사님 요청 설정(환경변수 및 포트)
ENV DB_DDL_AUTO=none
EXPOSE 8080

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]