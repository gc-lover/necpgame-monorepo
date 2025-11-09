# Multi-stage build для оптимизации размера образа
# Stage 1: Сборка приложения с Maven
FROM eclipse-temurin:21-jdk-alpine AS builder

# Установка Maven и Node.js (нужен для генерации OpenAPI кода)
RUN apk add --no-cache maven nodejs npm

# Создание рабочей директории
WORKDIR /build

# Копирование pom.xml и зависимостей для кэширования слоев Docker
COPY pom.xml .
COPY openapitools.json .

# Загрузка зависимостей Maven (кэшируется если pom.xml не изменился)
RUN mvn dependency:go-offline -B

# Копирование исходного кода
COPY src ./src
COPY templates ./templates
COPY templates-standard ./templates-standard

# Копирование OpenAPI спецификаций из API-SWAGGER (нужны для генерации)
COPY ../API-SWAGGER/api ./api-swagger

# Сборка приложения (skip tests для ускорения, тесты запускаем отдельно)
# -Dskip.openapi.generation=true т.к. генерация будет через скрипт или вручную
RUN mvn clean package -DskipTests -Dskip.openapi.generation=true

# Stage 2: Runtime образ
FROM eclipse-temurin:21-jre-alpine

# Создание пользователя для безопасности (не root)
RUN addgroup -g 1000 necpgame && \
    adduser -D -u 1000 -G necpgame necpgame

# Рабочая директория
WORKDIR /app

# Копирование собранного jar из builder stage
COPY --from=builder /build/target/*.jar app.jar

# Создание директорий для логов
RUN mkdir -p /app/logs && chown -R necpgame:necpgame /app

# Переключение на непривилегированного пользователя
USER necpgame

# Expose порт приложения
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Запуск приложения с оптимизированными JVM параметрами
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]










