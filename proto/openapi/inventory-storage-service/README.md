# Inventory Storage Service - OpenAPI Specification

## Назначение

**Inventory Storage Service** управляет хранением предметов игроков, организацией инвентаря, управлением вместимостью и оптимизацией хранения в NECPGAME.

Этот сервис предоставляет enterprise-grade API для всех операций с инвентарем персонажей в cyberpunk RPG.

## Ключевые Особенности

### Inventory Management

- **Многоуровневая организация**: Предметы по категориям (оружие, броня, импланты, расходники)
- **Управление вместимостью**: Динамическое расширение инвентаря
- **Оптимизация хранения**: Автоматическая сортировка и фильтрация
- **Безопасность предметов**: Защита от потери и кражи

### Performance Optimized

- **MMOFPS-grade performance**: <50ms P99 latency для всех операций
- **Memory efficient**: <30KB per active player inventory
- **Concurrent access**: 100,000+ simultaneous inventory operations
- **Struct alignment**: 30-50% memory savings через оптимизацию полей

### Enterprise-Grade Features

- **Bulk operations**: Добавление/удаление множественных предметов
- **Transaction safety**: Атомарные операции с rollback
- **Audit logging**: Полное логирование всех изменений инвентаря
- **Data validation**: Строгая валидация всех входных данных

## Структура Сервиса

```
proto/openapi/inventory-storage-service/
├── main.yaml           # Основная спецификация API
└── README.md          # Эта документация

proto/openapi/common/                   # Общие компоненты (используются по умолчанию)
├── responses/
│   ├── error.yaml      # Общие ответы ошибок
│   └── success.yaml    # Успешные ответы и health checks
├── schemas/
│   ├── error.yaml      # Схема ошибки
│   ├── health.yaml     # Health response
│   └── pagination.yaml # Пагинация
└── security/
    └── security.yaml   # JWT Bearer authentication
```

## Функциональность

### Основные Операции

- **Получение инвентаря**: `/inventory/{userId}` (GET)
- **Обновление настроек**: `/inventory/{userId}` (PUT)
- **Добавление предметов**: `/inventory/{userId}/items` (POST)
- **Удаление предметов**: `/inventory/{userId}/items` (DELETE)
- **Информация о вместимости**: `/inventory/{userId}/capacity` (GET)
- **Расширение вместимости**: `/inventory/{userId}/capacity` (POST)

### Категории Предметов

- `weapons` - Оружие
- `armor` - Броня и защита
- `implants` - Кибернетические импланты
- `consumables` - Расходные предметы
- `quest_items` - Квестовые предметы
- `miscellaneous` - Разное
```

## Зависимости

### Внешние Сервисы

- **item-service**: Получение информации о предметах
- **equipment-service**: Синхронизация экипированного снаряжения
- **currency-service**: Валидация платежей за расширение инвентаря
- **transaction-service**: Обработка транзакций расширения

### Common Components

- **common/responses/**: Стандартные ответы (success, error)
- **common/schemas/**: Общие схемы (error, health, pagination)
- **common/security/**: JWT Bearer authentication

## Performance

### Цели Производительности

- **P99 Latency**: <50ms для всех операций с инвентарем
- **Memory per Instance**: <30KB на активный инвентарь игрока
- **Concurrent Users**: 100,000+ одновременных операций
- **Storage Operations**: <15ms среднее время ответа

### Оптимизации

- **Struct Alignment**: Поля упорядочены для экономии памяти (30-50%)
- **Bulk Operations**: Пакетная обработка до 100 предметов за раз
- **Caching**: Redis кеширование для часто запрашиваемых инвентарей
- **Database Indexing**: Оптимизированные индексы для быстрого поиска

## Безопасность

### Аутентификация

- **JWT Bearer Tokens**: Обязательная аутентификация для всех операций
- **User Context**: Все операции выполняются в контексте пользователя
- **Permission Checks**: Валидация прав на модификацию инвентаря

### Валидация

- **Input Sanitization**: Все входные данные проходят санитизацию
- **Business Rules**: Строгая валидация бизнес-логики
- **Capacity Limits**: Защита от переполнения инвентаря
- **Item Validation**: Проверка существования и прав на предметы

## Мониторинг

### Health Endpoints

- `/health`: Базовая проверка здоровья
- `/health/batch`: Пакетная проверка зависимых сервисов
- `/health/ws`: Real-time мониторинг через WebSocket

### Метрики

- **Inventory Operations**: Количество операций в секунду
- **Capacity Usage**: Процент использования вместимости
- **Error Rates**: Процент ошибочных операций
- **Response Times**: P50, P95, P99 latency

## Использование

### Валидация Спецификации

```bash
# Линтинг OpenAPI спецификации
npx @redocly/cli lint main.yaml

# Бандлинг для проверки $ref
npx @redocly/cli bundle main.yaml -o bundled.yaml
```

### Генерация Go Кода

```bash
# Генерация Go API с ogen
ogen --target ../../services/inventory-storage-go/pkg/api \
     --package api --clean main.yaml
```

### Компиляция и Тестирование

```bash
# Компиляция сгенерированного кода
cd ../../services/inventory-storage-go
go mod init inventory-storage-go
go mod tidy
go build .
```

### Документация

```bash
# Генерация интерактивной документации
npx @redocly/cli build-docs main.yaml \
     -o docs/index.html \
     --title "Inventory Storage Service API"

# Публикация документации
npx @redocly/cli build-docs main.yaml \
     --template swagger-ui \
     -o docs/playground.html
```

### Примеры Использования API

#### Получение Инвентаря

```bash
curl -X GET \
  https://api.necpgame.com/v1/inventory-storage/inventory/123e4567-e89b-12d3-a456-426614174000 \
  -H "Authorization: Bearer <jwt-token>" \
  -H "Accept: application/json"
```

#### Добавление Предметов

```bash
curl -X POST \
  https://api.necpgame.com/v1/inventory-storage/inventory/123e4567-e89b-12d3-a456-426614174000/items \
  -H "Authorization: Bearer <jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {
        "item_id": "456e7890-e89b-12d3-a456-426614174001",
        "quantity": 5
      }
    ],
    "source": "loot"
  }'
```

### Health Checks

```bash
# Базовая проверка здоровья
curl https://api.necpgame.com/v1/inventory-storage/health

# Пакетная проверка зависимых сервисов
curl -X POST https://api.necpgame.com/v1/inventory-storage/health/batch \
  -H "Content-Type: application/json" \
  -d '{"services": ["item-service", "equipment-service"]}'
```

### Мониторинг

```bash
# WebSocket подключение для real-time мониторинга
ws://api.necpgame.com/v1/inventory-storage/health/ws?services=inventory-storage
```

## Разработка

### Локальный Запуск

```bash
# Клонирование репозитория
git clone https://github.com/necpgame/inventory-storage-service.git
cd inventory-storage-service

# Установка зависимостей
go mod download

# Запуск сервиса
go run cmd/main.go
```

### Docker

```bash
# Сборка образа
docker build -t necpgame/inventory-storage-service .

# Запуск контейнера
docker run -p 8080:8080 necpgame/inventory-storage-service
```

### Kubernetes

```bash
# Деплой в Kubernetes
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Проверка статуса
kubectl get pods -l app=inventory-storage-service
```

## Тестирование

### Unit Tests

```bash
go test ./pkg/... -v
```

### Integration Tests

```bash
go test ./tests/integration/... -v
```

### Performance Tests

```bash
# Load testing с 1000 одновременных пользователей
./scripts/load-test.sh --users 1000 --duration 300s
```

### E2E Tests

```bash
# Полный end-to-end цикл тестирования
./scripts/e2e-test.sh --environment staging
```

---

*Inventory Storage Service обеспечивает надежное и производительное управление инвентарем игроков в NECPGAME*
