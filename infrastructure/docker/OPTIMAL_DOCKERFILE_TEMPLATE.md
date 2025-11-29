# Оптимальный Dockerfile шаблон для Go сервисов

## Принципы (SOLID + Best Practices)

### Single Responsibility Principle
- Dockerfile отвечает только за сборку и упаковку
- Генерация кода вынесена в отдельные этапы
- Минимальный финальный образ (multi-stage build)

### Open/Closed Principle
- Поддержка генерации кода через Makefile (опционально)
- Легко расширяется для новых зависимостей

### Dependency Inversion
- Зависимости устанавливаются через go mod
- Не зависит от порядка установки пакетов

## Оптимальный шаблон

### Вариант 1: Сервис БЕЗ генерации кода (proto/openapi)

```dockerfile
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache git ca-certificates tzdata

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o service-name -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

COPY --from=builder /app/service-name /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

EXPOSE 8080/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/metrics || exit 1

ENTRYPOINT ["/app/service-name"]
```

### Вариант 2: Сервис С генерацией кода (proto/openapi)

```dockerfile
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata

COPY go.mod go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY . .
COPY ../../proto ./proto

RUN make generate-api || true

RUN go mod tidy
RUN CGO_ENABLED=0 GOOS=linux go build -o service-name -ldflags="-w -s -extldflags '-static'" .

FROM alpine:latest

RUN apk --no-cache add ca-certificates tzdata

WORKDIR /app

COPY --from=builder /app/service-name /app/
COPY --from=builder /usr/share/zoneinfo /usr/share/zoneinfo

EXPOSE 8080/tcp 9090

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/metrics || exit 1

ENTRYPOINT ["/app/service-name"]
```

## Оптимизации

### 1. Multi-stage build
- Минимальный финальный образ (только alpine + бинарник)
- Отдельный stage для сборки

### 2. Кэширование слоев
- go.mod/go.sum копируются отдельно для кэширования
- Зависимости устанавливаются до копирования кода

### 3. Безопасность
- Не root пользователь (опционально)
- Минимальные образы (alpine)
- Статическая сборка (CGO_ENABLED=0)

### 4. Производительность
- Статическая линковка (-extldflags '-static')
- Удаление debug информации (-ldflags="-w -s")
- Health checks для Kubernetes

### 5. Контекст сборки
- Всегда использовать контекст из директории сервиса
- Для proto использовать копирование из родительских директорий

## Контекст сборки

### Правильный подход:
```yaml
# docker-compose.yml
build:
  context: ./services/service-name-go
  dockerfile: Dockerfile
```

### Dockerfile должен работать с контекстом из директории сервиса:
```dockerfile
# Все пути относительно ./services/service-name-go
COPY go.mod go.sum ./
COPY . .
```

### Если нужен proto/:
```dockerfile
# В docker-compose.yml используем контекст из корня:
context: .

# В Dockerfile:
COPY services/service-name-go/go.mod services/service-name-go/go.sum ./
COPY proto/ ./proto/
COPY services/service-name-go/ ./
```

