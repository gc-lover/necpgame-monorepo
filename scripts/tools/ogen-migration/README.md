# Ogen Migration Core Components

Комплексная система для автоматизированной миграции сервисов с `oapi-codegen` на `ogen` с фокусом на производительность, безопасность и совместимость.

## 🎯 Обзор

Система состоит из трех основных компонентов:

1. **Migration Orchestrator** (`orchestrator.py`) - Оркестрация процесса миграции
2. **Performance Benchmark Suite** (`benchmark_suite.py`) - Измерение производительности
3. **Compatibility Validator** (`compatibility_validator.py`) - Валидация совместимости

## 📋 Предварительные требования

- Python 3.8+
- Go 1.24+
- Docker
- kubectl (для Kubernetes развертывания)
- Prometheus (для метрик)

## 🚀 Быстрый старт

### 1. Установка зависимостей

```bash
cd scripts/ogen-migration
pip install -r requirements.txt
```

### 2. Настройка конфигурации

Отредактируйте `config.yaml` для вашей среды:

```yaml
execution:
  parallel_migrations: 2  # Количество одновременных миграций

ogen:
  version: "latest"  # Версия ogen

monitoring:
  enable_metrics: true
```

### 3. Запуск dry-run миграции

```bash
python orchestrator.py --dry-run
```

### 4. Запуск полной миграции

```bash
python orchestrator.py
```

## 🏗️ Компоненты

### Migration Orchestrator

**Функции:**
- Автоматическое обнаружение сервисов
- Оценка рисков и сложности миграции
- Планирование миграции с учетом зависимостей
- Параллельное выполнение миграций
- Автоматический откат при ошибках

**Использование:**

```bash
# Миграция всех сервисов
python orchestrator.py

# Миграция конкретных сервисов
python orchestrator.py --services user-service auth-service

# Dry-run режим
python orchestrator.py --dry-run

# Пользовательская конфигурация
python orchestrator.py --config /path/to/config.yaml
```

**Алгоритм миграции:**

1. **Обнаружение сервисов** - Сканирование директории `services/`
2. **Анализ зависимостей** - Построение графа зависимостей
3. **Оценка рисков** - Автоматическая классификация по сложности
4. **Создание плана** - Топологическая сортировка и планирование
5. **Выполнение** - Параллельная миграция с мониторингом
6. **Валидация** - Проверка совместимости после миграции

### Performance Benchmark Suite

**Метрики производительности:**
- HTTP latency (запрос-ответ)
- CPU utilization
- Memory usage
- Concurrent request handling

**Использование:**

```bash
# Бенчмаркинг всех сервисов
python benchmark_suite.py

# Бенчмаркинг конкретных сервисов
python benchmark_suite.py --services api-gateway user-service

# Сохранение результатов
python benchmark_suite.py --output benchmark_results.json
```

**Пример вывода:**

```
=== Ogen Migration Benchmark Summary ===
Services benchmarked: 5
Average improvement: +12.3%
Memory savings: -8.7%
HTTP latency improvement: +15.2%

Per-operation improvements:
  http_latency: +15.2%
  memory_usage: -8.7%
  cpu_usage: -3.1%
```

### Compatibility Validator

**Проверки совместимости:**
- API контракты (OpenAPI спецификации)
- Типы данных (Go structs)
- Импорты и зависимости
- Обработка ошибок
- Middleware совместимость

**Использование:**

```bash
# Валидация всех сервисов
python compatibility_validator.py

# Валидация конкретных сервисов
python compatibility_validator.py --services payment-service

# Сохранение отчета
python compatibility_validator.py --output compatibility_report.json
```

**Пример отчета:**

```json
{
  "summary": {
    "total_services": 10,
    "compatible_services": 8,
    "incompatible_services": 2,
    "total_issues": 15,
    "error_count": 3,
    "warning_count": 12
  },
  "services": [
    {
      "name": "user-service",
      "compatible": true,
      "coverage_percentage": 95.2,
      "issues": [
        {
          "severity": "warning",
          "category": "api_contract",
          "message": "Missing operationId for GET /users"
        }
      ]
    }
  ]
}
```

## 📊 Мониторинг и метрики

### Prometheus метрики

Система экспортирует метрики в Prometheus:

```prometheus
# Migration progress
ogen_migration_services_total{status="completed"} 8
ogen_migration_services_total{status="failed"} 1
ogen_migration_services_total{status="in_progress"} 1

# Performance improvements
ogen_migration_performance_improvement_percent{operation="http_latency"} 15.2
ogen_migration_performance_improvement_percent{operation="memory_usage"} -8.7

# Compatibility issues
ogen_migration_compatibility_issues_total{severity="error"} 3
ogen_migration_compatibility_issues_total{severity="warning"} 12
```

### Grafana Dashboard

Автоматически создается дашборд для мониторинга миграции:

- **Migration Progress** - Прогресс миграции по сервисам
- **Performance Metrics** - Сравнение производительности
- **Compatibility Issues** - Анализ проблем совместимости
- **Risk Assessment** - Оценка рисков по сервисам

## 🔧 Конфигурация

### Основные настройки

```yaml
# Параллельные миграции
execution:
  parallel_migrations: 2

# Настройки ogen
ogen:
  version: "latest"
  generator_flags:
    - "--target"
    - "--clean"
    - "--validate"

# Мониторинг
monitoring:
  enable_metrics: true
  metrics_interval_seconds: 60
```

### Настройки рисков

```yaml
risk_levels:
  low:
    max_concurrent: 5
    requires_review: false
  medium:
    max_concurrent: 3
    requires_review: true
  high:
    max_concurrent: 1
    requires_review: true
```

## 🚨 Обработка ошибок

### Автоматический откат

При обнаружении ошибок система автоматически:

1. **Останавливает миграцию** текущего сервиса
2. **Создает бэкап** текущего состояния
3. **Восстанавливает** предыдущую версию
4. **Запускает тесты** для подтверждения восстановления
5. **Генерирует отчет** об инциденте

### Ручной откат

```bash
# Ручной откат сервиса
python orchestrator.py --rollback user-service

# Принудительный откат
python orchestrator.py --force-rollback user-service
```

## 📈 Производительность

### Оптимизации

- **Параллельная миграция** - до 5 сервисов одновременно
- **Инкрементная валидация** - проверка только измененных частей
- **Кеширование** - повторное использование результатов
- **Ленивая загрузка** - загрузка данных по требованию

### Целевые показатели

```
HTTP Latency:    +10% improvement
Memory Usage:    -5% reduction
CPU Usage:       -5% reduction
Build Time:      <60 seconds
```

## 🔒 Безопасность

### Меры безопасности

- **Валидация вводимых данных** - проверка всех конфигураций
- **Санитизация путей** - предотвращение path traversal
- **Ограничение ресурсов** - лимиты на CPU/память/диск
- **Аудит логов** - полное логирование всех операций

### Аутентификация

```yaml
security:
  require_code_review: true
  require_security_audit: true
  vulnerability_scanning: true
```

## 📝 Логирование

### Уровни логирования

- **DEBUG** - Детальная отладочная информация
- **INFO** - Общая информация о прогрессе
- **WARNING** - Предупреждения о потенциальных проблемах
- **ERROR** - Ошибки выполнения
- **CRITICAL** - Критические ошибки требующие внимания

### Формат логов

```json
{
  "timestamp": "2025-01-05T12:30:45Z",
  "level": "INFO",
  "component": "orchestrator",
  "service": "user-service",
  "message": "Migration completed successfully",
  "duration_ms": 15432,
  "issues_found": 0
}
```

## 🔄 CI/CD Интеграция

### GitHub Actions

```yaml
name: Ogen Migration
on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run compatibility validator
        run: |
          cd scripts/ogen-migration
          python compatibility_validator.py

  benchmark:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Run benchmark suite
        run: |
          cd scripts/ogen-migration
          python benchmark_suite.py
```

### Docker интеграция

```dockerfile
FROM python:3.11-slim

WORKDIR /app
COPY requirements.txt .
RUN pip install -r requirements.txt

COPY . .
CMD ["python", "orchestrator.py"]
```

## 📚 API Reference

### Orchestrator API

```python
from scripts.ogen_migration.orchestrator import MigrationOrchestrator

# Initialize
orchestrator = MigrationOrchestrator(base_path="/path/to/project")

# Discover services
orchestrator.discover_services()

# Create migration plan
orchestrator.create_migration_plan()

# Execute migration
await orchestrator.execute_migration()

# Generate report
orchestrator.generate_report()
```

### Benchmark API

```python
from scripts.ogen_migration.benchmark_suite import BenchmarkSuite

# Initialize
suite = BenchmarkSuite(base_path="/path/to/project")

# Run benchmarks
await suite.run_full_benchmark_suite()

# Get results
results = suite.results
comparisons = suite.comparisons
```

### Validator API

```python
from scripts.ogen_migration.compatibility_validator import CompatibilityValidator

# Initialize
validator = CompatibilityValidator(base_path="/path/to/project")

# Validate service
result = validator.validate_service("user-service")

# Validate all services
results = validator.validate_all_services()
```

## 🤝 Contributing

### Добавление новых проверок

1. **Расширьте валидатор:**

```python
def _validate_custom_rule(self, service_path, oapi_path, ogen_path):
    """Custom validation rule."""
    issues = []
    # Your validation logic here
    return issues

# Add to validation_rules
self.validation_rules["custom"] = self._validate_custom_rule
```

2. **Добавьте метрики:**

```python
# Add to Prometheus
self.custom_metric = prometheus.NewGaugeVec(...)
```

3. **Обновите конфигурацию:**

```yaml
validation:
  custom_rule_enabled: true
  custom_rule_threshold: 0.8
```

### Тестирование

```bash
# Run unit tests
python -m pytest tests/

# Run integration tests
python -m pytest tests/integration/

# Run performance tests
python benchmark_suite.py --performance-test
```

## 📄 Лицензия

Copyright (c) 2025 NECPGAME. All rights reserved.

## 📞 Поддержка

- **Email:** backend@necp.game
- **Slack:** #backend-migration
- **Docs:** [Migration Guide](../../docs/migration/ogen-migration.md)
