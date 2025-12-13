# Issue: #1

# Multi-stage build для оптимизации размера образа
FROM golang:1.24-alpine AS builder

# Устанавливаем зависимости для сборки
RUN apk add --no-cache git ca-certificates tzdata

# Создаем пользователя для безопасности
RUN adduser -D -s /bin/sh -u 1001 appuser

# Устанавливаем рабочую директорию
WORKDIR /build

# Копируем go mod файлы
COPY go.mod go.sum ./

# Скачиваем зависимости
RUN go mod download

# Копируем исходный код
COPY . .

# Собираем бинарный файл с оптимизациями
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o auth-login-service .

# Финальный образ
FROM scratch

# Копируем CA сертификаты для HTTPS
COPY --from=builder /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/

# Копируем timezone data
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

# Копируем пользователя
COPY --from=builder /etc/passwd /etc/passwd

# Создаем пользователя
USER appuser

# Копируем бинарный файл
COPY --from=builder /build/auth-login-service /auth-login-service

# Устанавливаем timezone
ENV TZ=UTC

# Экспортируем порт
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD ["/auth-login-service", "--health-check"] || exit 1

# Запускаем сервис
ENTRYPOINT ["/auth-login-service"]