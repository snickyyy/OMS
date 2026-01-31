FROM gradle:8.14.3-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
COPY gradlew .

RUN ./gradlew dependencies --no-daemon || true

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 \
               -XX:+UseG1GC \
               -XX:+ExitOnOutOfMemoryError \
               -XX:+HeapDumpOnOutOfMemoryError"


COPY --from=build /app/build/libs/*.jar app.jar
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
RUN apk add --no-cache curl && addgroup -S appuser && adduser -S appuser -G appuser

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=60s --retries=5 --start-period=5s --timeout=10s CMD curl -f -sS --max-time 5 http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["/app/entrypoint.sh"]
