# Tracking Service - OpenAPI Specification

## Назначение

**Tracking Service** предоставляет enterprise-grade систему отслеживания пользовательского поведения в NECPGAME. Сервис собирает, обрабатывает и анализирует события пользователей, обеспечивая полное соответствие GDPR и CCPA, с фокусом на приватность и производительность.

## Ключевые Функциональности

### 📊 Сбор Событий
- **Высокопроизводительный сбор** событий с throughput 100k+ events/second
- **Batch обработка** для оптимизации сетевых взаимодействий
- **Real-time обработка** с sub-millisecond latency

### 🔒 Приватность и Соответствие
- **GDPR/CCPA compliance** с полным контролем согласий
- **Data minimization** и автоматическая анонимизация
- **Right to erasure** с аудиторскими следами

### 📈 Аналитика и Метрики
- **Real-time aggregation** метрик для dashboard
- **Behavioral segmentation** на основе ML алгоритмов
- **Privacy-preserving analytics** без нарушения приватности

### 🎯 Персонализация
- **Event-driven personalization** для адаптивных систем
- **User profiling** на основе поведенческих паттернов
- **A/B testing support** для оптимизации опыта

## Структура API

```
tracking-service/
├── main.yaml              # Основная спецификация OpenAPI
├── README.md             # Эта документация
├── docs/                 # Сгенерированная документация (опционально)
└── tests/               # Тесты API (опционально)
```

## Основные Эндпоинты

### Сбор Событий
- `POST /events` - Отслеживание одиночного события
- `POST /events/batch` - Batch обработка событий

### Аналитика Пользователей
- `GET /users/{userId}/events` - История событий пользователя
- `GET /analytics/behavior/segments` - Сегментация пользователей

### Приватность и Consent
- `GET /users/{userId}/consent` - Статус согласий пользователя
- `PUT /users/{userId}/consent` - Обновление согласий
- `POST /users/{userId}/data/delete` - GDPR data deletion

### Метрики и Dashboard
- `GET /analytics/metrics/{metricType}` - Агрегированные метрики

### Мониторинг Здоровья
- `GET /health` - Проверка здоровья сервиса
- `POST /health/batch` - Массовый health check
- `GET /health/ws` - WebSocket мониторинг

## Зависимости от Других Сервисов

### Требуемые Сервисы
- **user-profile-service** - Профили пользователей для анонимизации
- **adaptive-system-service** - Адаптивные рекомендации на основе событий
- **player-analytics-service** - Продвинутая аналитика поведения

### Общие Компоненты
- `../common/schemas/health.yaml` - Схемы здоровья сервиса
- `../common/schemas/error.yaml` - Стандартные ошибки
- `../common/responses/success.yaml` - Успешные ответы

## Производительность

### Целевые Показатели
- **P99 Latency**: <10ms для ingestion событий
- **Память**: <50KB на активную сессию отслеживания
- **Одновременные пользователи**: 500,000+ simultaneous tracking
- **Throughput**: 100,000+ events/second
- **Data retention**: Настраиваемый с автоматической очисткой

### Оптимизации
- **Memory-mapped files** для высокоскоростного ingestion
- **Time-series databases** для эффективного хранения
- **Bloom filters** для быстрого поиска событий
- **Event-driven architecture** для real-time processing

## Использование

### Валидация Спецификации
```bash
# Линтинг с Redocly
npx @redocly/cli lint main.yaml

# Бандлинг для проверки $ref
npx @redocly/cli bundle main.yaml -o bundled.yaml
```

### Генерация Go Кода
```bash
# Генерация с ogen
ogen --target ../../services/tracking-service-go/pkg/api \
     --package api --clean main.yaml
```

### Документация
```bash
# Генерация HTML документации
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

## Примеры Использования

### Отслеживание События
```bash
curl -X POST \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: application/json" \
     -d '{
       "user_id": "123e4567-e89b-12d3-a456-426614174000",
       "event_type": "game_action",
       "event_name": "player_level_up",
       "timestamp": "2025-12-21T10:00:00Z",
       "properties": {
         "level": 15,
         "duration": 45
       }
     }' \
     https://api.necpgame.com/v1/tracking/events
```

### Batch Отправка Событий
```bash
curl -X POST \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: application/json" \
     -d '{
       "events": [
         {
           "user_id": "123e4567-e89b-12d3-a456-426614174000",
           "event_type": "ui_interaction",
           "event_name": "button_click",
           "timestamp": "2025-12-21T10:00:00Z"
         },
         {
           "user_id": "123e4567-e89b-12d3-a456-426614174000",
           "event_type": "achievement",
           "event_name": "achievement_unlocked",
           "timestamp": "2025-12-21T10:00:05Z",
           "properties": {
             "achievement_id": "first_kill"
           }
         }
       ]
     }' \
     https://api.necpgame.com/v1/tracking/events/batch
```

### Получение Событий Пользователя
```bash
curl -H "Authorization: Bearer {token}" \
     "https://api.necpgame.com/v1/tracking/users/123e4567-e89b-12d3-a456-426614174000/events?event_type=game_action&limit=20"
```

### Проверка Согласий
```bash
curl -H "Authorization: Bearer {token}" \
     https://api.necpgame.com/v1/tracking/users/123e4567-e89b-12d3-a456-426614174000/consent
```

### GDPR Data Deletion
```bash
curl -X POST \
     -H "Authorization: Bearer {token}" \
     -H "Content-Type: application/json" \
     -d '{
       "reason": "user_request",
       "confirmation": true,
       "delete_related_data": true
     }' \
     https://api.necpgame.com/v1/tracking/users/123e4567-e89b-12d3-a456-426614174000/data/delete
```

### Получение Метрик
```bash
curl -H "Authorization: Bearer {token}" \
     "https://api.necpgame.com/v1/tracking/analytics/metrics/user_engagement?period=day"
```

### WebSocket Мониторинг
```javascript
const ws = new WebSocket('ws://localhost:8080/api/v1/tracking/health/ws');
ws.onmessage = (event) => {
    const health = JSON.parse(event.data);
    console.log('Tracking service health:', health);
};
```

## Архитектурные Решения

### Domain-Driven Design
- **Ограниченный контекст**: Отслеживание и аналитика поведения
- **Агрегаты**: Event, UserConsent, AnalyticsMetric, BehaviorSegment
- **Value Objects**: EventType, ConsentStatus, MetricPeriod

### Data Architecture
- **Time-series storage** для событий с автоматической партиционированием
- **Document database** для гибких свойств событий
- **In-memory caching** для горячих метрик

### Privacy by Design
- **Data minimization** - сбор только необходимых данных
- **Purpose limitation** - использование данных только по назначению
- **Storage limitation** - автоматическое удаление устаревших данных

### Масштабируемость
- **Horizontal scaling** для ingestion и processing
- **Event streaming** с Kafka для decoupling
- **CQRS pattern** для reads/writes separation

## Безопасность

### Аутентификация
- **JWT Bearer tokens** для API доступа
- **Service-to-service** аутентификация
- **API key authentication** для SDK integration

### Авторизация
- **Resource ownership** - пользователи видят только свои события
- **Role-based access** для аналитики и метрик
- **Data classification** с соответствующими уровнями доступа

### Приватность
- **End-to-end encryption** для чувствительных данных
- **Pseudonymization** для долгосрочного хранения
- **Audit logging** всех доступов к данным

## Мониторинг и Observability

### Метрики
- **Ingestion rates** - скорость приема событий
- **Processing latency** - время обработки
- **Data retention** - объем хранимых данных
- **Privacy compliance** - статус согласий

### Логирование
```json
{
  "timestamp": "2025-12-21T10:00:00Z",
  "level": "INFO",
  "service": "tracking-service",
  "operation": "event_ingestion",
  "user_id": "123e4567-e89b-12d3-a456-426614174000",
  "event": {
    "type": "game_action",
    "name": "player_level_up",
    "processing_time_ms": 5
  }
}
```

### Распределенная Трассировка
- **OpenTelemetry** для end-to-end tracing ingestion
- **Jaeger** для визуализации processing pipelines
- **Service mesh** интеграция

## Тестирование

### Unit Tests
```bash
go test ./pkg/event/
go test ./pkg/consent/
go test ./pkg/analytics/
```

### Integration Tests
```bash
# Тестирование с другими сервисами
./scripts/run-integration-tests.sh tracking-service
```

### Performance Tests
```bash
# Нагрузочное тестирование ingestion
./scripts/run-performance-tests.sh --service tracking-service --events-per-second 100000

# Тестирование data deletion
./scripts/test-gdpr-compliance.sh
```

### Privacy Tests
```bash
# Тестирование приватности
./scripts/test-privacy-compliance.sh --regulation gdpr

# Тестирование data anonymization
./scripts/test-data-anonymization.sh
```

## Развертывание

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tracking-service
spec:
  replicas: 10
  template:
    spec:
      containers:
      - name: tracking
        image: necpgame/tracking-service:v1.0.0
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "2Gi"
            cpu: "2000m"
        env:
        - name: EVENT_INGESTION_RATE_LIMIT
          value: "100000"
        - name: DATA_RETENTION_DAYS
          value: "365"
        - name: PRIVACY_MODE
          value: "gdpr"
        ports:
        - containerPort: 8080
          name: http
        - containerPort: 9090
          name: websocket
```

### Helm Chart
```bash
helm install tracking-service ./charts/tracking-service \
  --set ingestion.rateLimit=100000 \
  --set privacy.regulation=gdpr \
  --set retention.days=365
```

## SDK Integration

### JavaScript SDK
```javascript
import { TrackingSDK } from '@necpgame/tracking-sdk';

const tracker = new TrackingSDK({
  apiKey: 'your-api-key',
  endpoint: 'https://api.necpgame.com/v1/tracking'
});

// Track event
tracker.track('game_action', 'player_level_up', {
  level: 15,
  duration: 45
});

// Batch track
tracker.trackBatch([
  { type: 'ui_interaction', name: 'button_click' },
  { type: 'achievement', name: 'first_kill' }
]);
```

### Mobile SDKs
```swift
// iOS
let tracker = TrackingSDK(apiKey: "your-api-key")
tracker.track(event: "purchase", properties: ["item": "premium_skin", "amount": 9.99])
```

```kotlin
// Android
val tracker = TrackingSDK(apiKey = "your-api-key")
tracker.track("game_start", mapOf("mode" to "pvp", "region" to "eu"))
```

## Бизнес-Правила

### Data Retention
- **Standard retention**: 365 дней для аналитики
- **Extended retention**: до 2555 дней (GDPR max) для premium пользователей
- **Immediate deletion**: по запросу пользователя

### Consent Management
- **Granular consents**: отдельные разрешения для разных типов данных
- **Consent versioning**: отслеживание версий условий
- **Consent withdrawal**: возможность отзыва в любое время

### Event Processing
- **Real-time validation** всех входящих событий
- **Deduplication** для предотвращения дублированных событий
- **Filtering** на основе consent settings

## Troubleshooting

### Распространенные Проблемы

#### Высокая латентность ingestion
**Симптомы**: >10ms P99 latency на event ingestion
**Решение**:
- Проверить rate limiting
- Оптимизировать database индексы
- Масштабировать ingestion workers

#### Data retention violations
**Симптомы**: Данные хранятся дольше разрешенного периода
**Решение**:
- Проверить cleanup jobs
- Валидировать consent settings
- Обновить retention policies

#### Privacy compliance issues
**Симптомы**: Нарушения GDPR/CCPA требований
**Решение**:
- Аудит consent management
- Внедрить data anonymization
- Обновить privacy policies

## Контакты

- **Владелец сервиса**: @tracking-team
- **DevOps**: @platform-team
- **Security**: @security-team
- **Privacy**: @legal-privacy-team
- **Документация**: docs@necpgame.com

## Следующие Шаги

1. **SDK development** для различных платформ
2. **Advanced ML analytics** для behavioral insights
3. **Real-time dashboards** для game monitoring
4. **Privacy-preserving federated learning** для cross-game analytics

---

*Tracking Service является фундаментом data-driven подхода в NECPGAME, обеспечивая глубокое понимание пользовательского поведения при строгом соблюдении приватности.*