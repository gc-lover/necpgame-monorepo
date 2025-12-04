# Services Dashboard - Comprehensive Guide

**Расширенный дашборд для мониторинга микросервисов с метриками, здоровьем и историческими данными**

---

## Возможности

### 1. Обзор сервисов
- **Статус здоровья** (healthy/unhealthy/unknown)
- **Метрики производительности** (request rate, latency, error rate)
- **Использование ресурсов** (CPU, memory)
- **Тренды бенчмарков** (improving/stable/degrading)

### 2. Исторические данные
- **Графики бенчмарков** по времени
- **Сравнение версий/коммитов**
- **Отслеживание изменений** производительности
- **Идентификация регрессий**

### 3. Тренды и анализ
- **Улучшающиеся сервисы** (performance improving)
- **Деградирующие сервисы** (performance degrading)
- **Стабильные сервисы**
- **Процентные изменения** производительности

### 4. Интеграция с Prometheus
- **Реальные метрики** из Prometheus
- **Request rate, latency, error rate**
- **CPU и memory usage**
- **Автоматическое обновление**

---

## Запуск

```bash
cd infrastructure/services-dashboard
go run main.go
```

Откройте: http://localhost:8080

---

## API Endpoints

### `GET /api/services`
Список всех сервисов с их статусом и метриками.

**Response:**
```json
[
  {
    "name": "loot-service-go",
    "health": "healthy",
    "request_rate": 150.5,
    "error_rate": 0.1,
    "latency_p95": 12.5,
    "cpu_usage": 45.2,
    "memory_usage": 256.0,
    "benchmark_trend": "improving",
    "metrics": {
      "request_rate": 150.5,
      "latency_p95": 12.5
    }
  }
]
```

### `GET /api/benchmarks/history?service=X&benchmark=Y`
История бенчмарков для сервиса.

**Response:**
```json
[
  {
    "service": "loot-service-go",
    "benchmark": "BenchmarkDistributeLoot",
    "points": [
      {
        "timestamp": "20251204_230210",
        "value": 200.2
      }
    ]
  }
]
```

### `GET /api/trends?service=X`
Тренды производительности.

**Response:**
```json
{
  "loot-service-go:BenchmarkDistributeLoot": {
    "service": "loot-service-go",
    "metric": "BenchmarkDistributeLoot",
    "direction": "improving",
    "change": -15.5,
    "points": [...]
  }
}
```

### `GET /api/summary`
Общая статистика.

**Response:**
```json
{
  "total_services": 86,
  "healthy_services": 80,
  "unhealthy_services": 2,
  "avg_latency": 15.5,
  "total_request_rate": 5000.0,
  "services_improving": 12,
  "services_degrading": 3
}
```

### `GET /api/service/{name}`
Детальная информация о сервисе.

### `GET /api/prometheus/?query=...`
Прокси для запросов к Prometheus.

---

## Конфигурация

### Prometheus Integration

Установите переменную окружения:
```bash
export PROMETHEUS_URL=http://localhost:9090
```

Или в Docker:
```yaml
environment:
  - PROMETHEUS_URL=http://prometheus:9090
```

### Health Checks

Дашборд автоматически проверяет:
- Наличие недавних бенчмарков (в течение 24 часов)
- Health endpoints сервисов (если доступны)
- Метрики из Prometheus

---

## Графики и визуализация

### 1. Overview Tab
- Карточки сервисов с метриками
- Фильтры по сервисам и статусу здоровья
- Индикаторы трендов

### 2. Benchmarks Tab
- Графики истории бенчмарков
- Сравнение между запусками
- Фильтры по сервисам и бенчмаркам

### 3. Trends Tab
- Графики трендов производительности
- Цветовая индикация (зеленый = улучшение, красный = деградация)
- Процентные изменения

### 4. Metrics Tab
- Детальные метрики сервиса
- Интеграция с Prometheus
- Real-time данные

---

## Отслеживание изменений

### Как определить что улучшает/ухудшает

1. **Смотри Trends Tab:**
   - Зеленый тренд = улучшение (lower ns/op)
   - Красный тренд = деградация (higher ns/op)
   - Процент изменения показывает масштаб

2. **Сравни коммиты:**
   - Каждый бенчмарк связан с timestamp
   - Сравни значения до/после изменений
   - Ищи паттерны в графиках

3. **Мониторь метрики:**
   - Request rate изменения
   - Latency изменения
   - Error rate всплески

---

## Автоматизация

### При билде:
- Бенчмарки запускаются автоматически
- Результаты сохраняются в `.benchmarks/results/`
- Дашборд автоматически обновляется

### В CI/CD:
- GitHub Actions запускает полные бенчмарки
- Результаты коммитятся в репозиторий
- Дашборд показывает историю

### Pre-commit:
- Быстрые бенчмарки при коммите
- Результаты сохраняются
- Можно отследить локальные изменения

---

## Примеры использования

### Найти деградирующие сервисы:
1. Открой Trends Tab
2. Фильтруй по "degrading"
3. Смотри процент изменения

### Сравнить производительность:
1. Открой Benchmarks Tab
2. Выбери сервис и бенчмарк
3. Смотри график - видно улучшения/ухудшения

### Проверить здоровье:
1. Открой Overview Tab
2. Фильтруй по "unhealthy"
3. Проверь метрики проблемных сервисов

---

**Статус:** OK Готов к использованию
**Порт:** 8080
**Данные:** `.benchmarks/results/*.json`

