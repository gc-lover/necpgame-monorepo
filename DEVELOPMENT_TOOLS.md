# NECP Game Development Tools & Automation

## Обзор

Полный набор инструментов для разработки, тестирования, мониторинга и развертывания MMOFPS RPG сервисов.

## 🛠️ Инструменты Разработки

### 1. Генерация JWT Токенов (`scripts/generate-jwt-token.py`)

**Назначение:** Генерация JWT токенов для тестирования API endpoints

**Использование:**
```bash
# Простой токен
python3 scripts/generate-jwt-token.py

# С кастомными параметрами
python3 scripts/generate-jwt-token.py --user-id "player123" --roles "player,premium"

# Для curl команды
python3 scripts/generate-jwt-token.py --output curl
```

**Особенности:**
- Поддержка ролей и пользовательских ID
- Настраиваемый срок действия
- Интеграция с curl командами

### 2. API Тестирование (`scripts/api-test.sh`, `scripts/api-test.ps1`)

**Назначение:** Комплексное тестирование всех API endpoints

**Использование:**
```bash
# Полное тестирование
./scripts/api-test.sh

# Детальное тестирование (PowerShell)
./scripts/api-test.ps1 -Detailed
```

**Тестирует:**
- OK Health endpoints (все сервисы)
- 📊 Metrics endpoints (доступные сервисы)
- 🔗 API endpoints (если реализованы)

### 3. Нагрузочное Тестирование (`scripts/load-test.sh`)

**Назначение:** Проверка производительности сервисов под нагрузкой

**Использование:**
```bash
# Тестирование с параметрами по умолчанию
./scripts/load-test.sh

# Кастомные параметры
CONCURRENT_REQUESTS=20 TOTAL_REQUESTS=500 DURATION=120 ./scripts/load-test.sh
```

**Метрики:**
- RPS (запросов в секунду)
- Время отклика (среднее, 95-й перцентиль)
- Успешность ответов (%)

### 4. Системная Проверка (`scripts/system-check.sh`, `scripts/system-check.ps1`)

**Назначение:** Быстрая проверка здоровья всех сервисов

**Использование:**
```bash
# Автоматическая проверка
./scripts/system-check.sh

# Детальная информация
./scripts/system-check.ps1 -Verbose
```

**Результат:** Статус всех 27 сервисов + инфраструктуры

## 🚀 Инструменты Развертывания

### 5. Резервное Копирование (`scripts/backup-databases.sh`)

**Назначение:** Автоматическое создание резервных копий PostgreSQL и Redis

**Использование:**
```bash
# Стандартное копирование
./scripts/backup-databases.sh

# Кастомные настройки
BACKUP_DIR="/mnt/backups" KEEP_BACKUPS=14 ./scripts/backup-databases.sh
```

**Создает:**
- 📦 PostgreSQL дамп (gzip сжатый)
- 🔴 Redis RDB файл (gzip сжатый)
- 📋 Манифест с метаданными
- Автоматическая ротация старых копий

### 6. Развертывание Обновлений (`scripts/deploy-update.sh`)

**Назначение:** Безопасное обновление сервисов с откатом

**Использование:**
```bash
# Обновить конкретный сервис
./scripts/deploy-update.sh achievement-service

# Обновить несколько сервисов
./scripts/deploy-update.sh achievement-service cosmetic-service

# Обновить все сервисы
./scripts/deploy-update.sh
```

**Особенности:**
- 💾 Автоматическое резервное копирование
- 🔄 Откат при неудаче
- OK Валидация после обновления
- 🏥 Health checks после развертывания

### 7. Создание Новых Сервисов (`scripts/create-service.sh`)

**Назначение:** Автоматическая генерация шаблона нового микросервиса

**Использование:**
```bash
# Создать новый сервис
./scripts/create-service.sh my-service "My awesome service" 8123
```

**Создает:**
- 🏗️ Полную структуру директорий
- 📄 Все необходимые файлы (main.go, handlers, service, repository)
- 🐳 Dockerfile с health checks
- 🔧 Makefile для сборки и генерации API
- 🐙 docker-compose.yml обновление

## 📊 Мониторинг и Наблюдение

### Prometheus + Grafana + Loki

**Быстрый старт:**
```bash
# Запустить мониторинг
docker-compose -f docker-compose.monitoring.yml up -d

# Доступ
# Grafana: http://localhost:3000 (admin/admin123)
# Prometheus: http://localhost:9090
# Loki: http://localhost:3100
```

**Dashboards:**
- 📈 **NECP Game Services Overview** - общая статистика
- 🔍 Метрики: health status, request rate, response time, goroutines, memory

## 🔧 Скрипты Обслуживания

### Проверка Системы
```bash
# Полная диагностика
./scripts/system-check.sh

# API тестирование
./scripts/api-test.sh

# Нагрузочное тестирование
./scripts/load-test.sh
```

### Резервное Копирование
```bash
# Еженедельное копирование
0 2 * * 0 ./scripts/backup-databases.sh

# Кастомный путь
BACKUP_DIR="/secure/backups" ./scripts/backup-databases.sh
```

### Развертывание
```bash
# Blue-green deployment
./scripts/deploy-update.sh --blue-green achievement-service

# Rolling update
./scripts/deploy-update.sh --rolling
```

## 📈 Производительность и Масштабирование

### Метрики Мониторинга

**Application Metrics:**
- HTTP request rate, duration, status codes
- Database connection pools
- Goroutine counts per service
- Memory usage patterns

**Infrastructure Metrics:**
- Container resource usage (CPU, memory, network)
- Database performance (queries/sec, slow queries)
- Redis hit rates and memory usage
- Network latency between services

### Оптимизация

**Performance Tuning:**
```bash
# Проверить goroutines
curl http://localhost:9200/metrics | grep go_goroutines

# Проверить память
docker stats necpgame-achievement-service-1
```

**Scaling Strategies:**
- Horizontal scaling с load balancer
- Database read replicas
- Redis clustering
- Service mesh (Istio/Linkerd)

## 🛡️ Безопасность и Compliance

### JWT Authentication
```python
# Генерация токенов для тестирования
python3 scripts/generate-jwt-token.py --user-id "admin" --roles "admin,moderator"
```

### Access Control
- Role-based permissions
- Service-to-service authentication
- API rate limiting
- Audit logging

## 🔄 CI/CD Интеграция

### GitHub Actions Примеры

**Build & Test:**
```yaml
- name: Run System Checks
  run: ./scripts/system-check.sh

- name: API Testing
  run: ./scripts/api-test.sh

- name: Load Testing
  run: ./scripts/load-test.sh
```

**Deploy:**
```yaml
- name: Deploy Services
  run: ./scripts/deploy-update.sh ${{ github.event.inputs.services }}
```

## 📚 Использование в Разработке

### Ежедневный Workflow

1. **Проверка системы:**
   ```bash
   ./scripts/system-check.sh
   ```

2. **Разработка нового сервиса:**
   ```bash
   ./scripts/create-service.sh inventory-service "Inventory Management" 8131
   ```

3. **Тестирование API:**
   ```bash
   ./scripts/api-test.sh
   ```

4. **Резервное копирование:**
   ```bash
   ./scripts/backup-databases.sh
   ```

5. **Развертывание:**
   ```bash
   ./scripts/deploy-update.sh inventory-service
   ```

### Мониторинг Разработки

- **Grafana Dashboards** для реального времени метрик
- **Loki** для поиска по логам
- **Prometheus Alerts** для автоматических уведомлений

## 🚨 Troubleshooting

### Сервис не запускается
```bash
# Проверить логи
docker logs necpgame-service-name-1

# Проверить health
curl http://localhost:PORT/health

# Перезапустить с логами
docker-compose restart service-name
```

### API возвращает 404
```bash
# Проверить OpenAPI спецификацию
cat proto/openapi/service-name.yaml

# Перегенерировать API
cd services/service-name-go && make generate-api
```

### Высокое потребление ресурсов
```bash
# Проверить метрики
curl http://localhost:PORT/metrics

# Проверить goroutines
docker exec necpgame-service-name-1 ps aux
```

## 🎯 Следующие Шаги

### Планируемые Улучшения

1. **Kubernetes Support**
   - Helm charts для K8s развертывания
   - Ingress контроллеры
   - Service mesh интеграция

2. **Advanced Monitoring**
   - Distributed tracing (Jaeger)
   - Custom business metrics
   - Anomaly detection

3. **Security Enhancements**
   - OAuth2 integration
   - API gateway
   - Secret management

4. **Performance Tools**
   - Chaos engineering
   - A/B testing framework
   - Feature flags

## 📞 Поддержка

- **Документация:** `README.md`, `MONITORING_SETUP.md`
- **Примеры:** Смотрите существующие сервисы
- **Issues:** Создавайте в GitHub repository

---

**🎉 NECP Game теперь имеет полный DevOps toolkit для масштабируемой разработки микросервисов!**
