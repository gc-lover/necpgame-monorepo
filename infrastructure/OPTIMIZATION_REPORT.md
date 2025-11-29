# Отчет об оптимизации инфраструктуры и сервисов

## Найденные проблемы

### 1. Dockerfile - Несогласованность паттернов

**Проблема:** Используются 3 разных паттерна Dockerfile:

1. **Паттерн 1 (новые сервисы):** Контекст из директории сервиса
   - `social-service-go`, `economy-service-go`, `gameplay-service-go`
   - OK Правильный подход

2. **Паттерн 2 (старые сервисы):** Контекст из корня монорепо
   - `character-service-go`, `clan-war-service-go`, `achievement-service-go`
   - ❌ Не соответствует docker-compose.yml

3. **Паттерн 3 (минималистичный):** Без генерации кода
   - `matchmaking-go`, `ws-lobby-go`
   - OK Правильный, но можно улучшить

**Влияние:**
- Несовместимость с docker-compose.yml
- Дублирование кода в Dockerfile
- Сложность поддержки

### 2. Dockerfile - Отсутствие оптимизаций

**Проблемы:**
- ❌ Нет health checks
- ❌ Нет tzdata для корректной работы с временными зонами
- ❌ Неоптимальное кэширование слоев
- ❌ Отсутствие статической линковки
- ❌ Разные версии Go (1.23 vs 1.24)

### 3. Kubernetes - Недостающие оптимизации

**Проблемы:**
- ❌ Не все сервисы имеют HPA (только 3 из 24)
- ❌ Не все сервисы имеют PDB (только 3 из 24)
- ❌ Одинаковые ресурсы для всех сервисов
- ❌ Отсутствие стратегии обновлений (rollingUpdate)
- ❌ Отсутствие security contexts
- ❌ Нет topology spread constraints

### 4. Код - Дублирование методов

**Найдено:**
- OK Исправлено в `achievement-service-go` (дублирование методов)

**Требует проверки:**
- Другие сервисы на наличие дублирования

## Рекомендации по оптимизации

### 1. Унифицировать Dockerfile

**Действие:** Создать единый оптимальный шаблон для всех сервисов

**Шаблон:**
```dockerfile
FROM golang:1.24-alpine AS builder

WORKDIR /app

RUN apk add --no-cache make nodejs npm git ca-certificates tzdata

COPY go.mod go.sum ./
RUN go mod download

RUN go install github.com/oapi-codegen/oapi-codegen/v2/cmd/oapi-codegen@latest

COPY . .
COPY ../../proto ./proto 2>/dev/null || true

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

### 2. Оптимизировать docker-compose.yml

**Действие:** Унифицировать контекст сборки

**Для сервисов с proto/:** Использовать контекст из корня
```yaml
build:
  context: .
  dockerfile: services/service-name-go/Dockerfile
```

**Для сервисов без proto/:** Использовать контекст из директории сервиса
```yaml
build:
  context: ./services/service-name-go
  dockerfile: Dockerfile
```

### 3. Оптимизировать Kubernetes манифесты

#### 3.1. Добавить HPA для всех сервисов

**Критерии:**
- Все сервисы с нагрузкой → HPA
- Сервисы без нагрузки → фиксированные replicas

**Оптимальные настройки:**
```yaml
minReplicas: 1-2
maxReplicas: 5-10
cpuUtilization: 70%
memoryUtilization: 80%
```

#### 3.2. Добавить PDB для всех сервисов

**Критерии:**
- minAvailable: 1 для single-replica сервисов
- minAvailable: 50% для multi-replica сервисов

#### 3.3. Добавить стратегию обновлений

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxSurge: 1
    maxUnavailable: 0
```

#### 3.4. Добавить security contexts

```yaml
securityContext:
  runAsNonRoot: true
  runAsUser: 1000
  allowPrivilegeEscalation: false
  readOnlyRootFilesystem: true
```

#### 3.5. Оптимизировать ресурсы по типу сервиса

**Легкие сервисы (REST API):**
- requests: cpu 50m, memory 64Mi
- limits: cpu 200m, memory 256Mi

**Средние сервисы (с БД):**
- requests: cpu 100m, memory 128Mi
- limits: cpu 500m, memory 512Mi

**Тяжелые сервисы (realtime, обработка):**
- requests: cpu 200m, memory 256Mi
- limits: cpu 1000m, memory 1Gi

#### 3.6. Добавить topology spread constraints

```yaml
topologySpreadConstraints:
  - maxSkew: 1
    topologyKey: kubernetes.io/hostname
    whenUnsatisfiable: DoNotSchedule
    labelSelector:
      matchLabels:
        app: service-name
```

### 4. Инфраструктура

#### 4.1. Observability - все настроено OK
- Prometheus OK
- Grafana OK
- Loki OK
- Tempo OK

#### 4.2. Liquibase миграции - требуют проверки
- Проверить порядок миграций
- Проверить версионирование

## Приоритеты оптимизации

### Высокий приоритет
1. OK Унифицировать Dockerfile
2. OK Исправить дублирование кода
3. WARNING Добавить HPA для критичных сервисов
4. WARNING Добавить PDB для всех сервисов

### Средний приоритет
5. Оптимизировать ресурсы по типу сервиса
6. Добавить security contexts
7. Добавить стратегию обновлений

### Низкий приоритет
8. Добавить topology spread constraints
9. Оптимизировать docker-compose.yml контексты

## План действий

1. Создать оптимальный Dockerfile шаблон OK
2. Применить шаблон ко всем сервисам
3. Обновить docker-compose.yml
4. Добавить HPA/PDB для всех сервисов
5. Оптимизировать ресурсы
6. Добавить security contexts
7. Тестирование и валидация

