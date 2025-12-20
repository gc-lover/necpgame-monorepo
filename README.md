# NECP Game - MMOFPS RPG Backend Services

[![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)](https://docker.com)
[![Go](https://img.shields.io/badge/go-%2300ADD8.svg?style=for-the-badge&logo=go&logoColor=white)](https://golang.org)
[![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)](https://postgresql.org)
[![Redis](https://img.shields.io/badge/redis-%23DC382D.svg?style=for-the-badge&logo=redis&logoColor=white)](https://redis.io)
[![Prometheus](https://img.shields.io/badge/prometheus-%23E6522C.svg?style=for-the-badge&logo=prometheus&logoColor=white)](https://prometheus.io)

Backend сервисы для MMOFPS RPG игры с микросервисной архитектурой.

## 🏗️ Архитектура

Проект использует **микросервисную архитектуру** с 27 специализированными сервисами:

### 🎮 Игровые сервисы
- **Achievement Service** - система достижений и прогресса
- **Battle Pass Service** - сезонные боевые пропуска
- **Character Services** - управление персонажами и их развитием
- **Combat Services** - боевая система (урон, хакинг, сессии)
- **Economic Services** - экономика (валюты, магазины, торговля)
- **Social Services** - социальные функции (рейтинги, рефералы)

### 🔧 Инфраструктура
- **PostgreSQL** - основная база данных
- **Redis** - кеширование и сессии
- **Keycloak** - аутентификация и авторизация

### 📊 Мониторинг (опционально)
- **Prometheus** - сбор метрик
- **Grafana** - dashboards и визуализация
- **Loki** - агрегация логов
- **AlertManager** - алерты и уведомления

## 🚀 Быстрый старт

### Предварительные требования
- Docker и Docker Compose
- 8GB+ RAM
- 20GB+ свободного места

### 1. Клонировать репозиторий
```bash
git clone <repository-url>
cd necpgame-monorepo
```

### 2. Запустить сервисы
```bash
# Запустить все сервисы
docker-compose up -d

# Проверить статус
docker-compose ps
```

### 3. Проверка здоровья
```bash
# Использовать скрипт проверки
./scripts/system-check.sh
# или для Windows
./scripts/system-check.ps1
```

### 4. Доступ к сервисам
- **API Documentation**: Каждый сервис имеет health endpoint `/health`
- **Metrics**: Доступны на `/metrics` для каждого сервиса
- **pprof**: Profiling endpoints на `localhost:{port}` для каждого сервиса

## 📁 Структура проекта

```
necp-game-monorepo/
├── services/                    # Go микросервисы
│   ├── *-service-go/           # Каждый сервис в отдельной папке
│   │   ├── main.go            # Entry point
│   │   ├── server/            # HTTP сервер и handlers
│   │   ├── pkg/api/           # OGEN-generated API код
│   │   └── Dockerfile         # Docker конфигурация
├── proto/openapi/              # OpenAPI спецификации
├── infrastructure/             # Инфраструктура
│   ├── monitoring/            # Prometheus, Grafana, Loki
│   └── liquibase/             # Database migrations
├── scripts/                    # Автоматизация и утилиты
├── docker-compose.yml          # Основная конфигурация
└── docker-compose.monitoring.yml # Мониторинг стек
```

## 🔧 Разработка

### Добавление нового сервиса

1. **Создать OpenAPI спецификацию**
   ```yaml
   # proto/openapi/new-service.yaml
   openapi: 3.0.3
   info:
     title: New Service API
     version: 1.0.0
   paths:
     /health:
       get:
         responses:
           200:
             description: OK
   ```

2. **Создать сервисную папку**
   ```bash
   mkdir services/new-service-go
   cd services/new-service-go
   go mod init github.com/necpgame/new-service-go
   ```

3. **Реализовать handlers**
   ```go
   // server/handlers.go
   func (h *Handlers) Health(ctx context.Context) error {
       return nil
   }
   ```

4. **Добавить в docker-compose.yml**
   ```yaml
   new-service:
     build:
       context: ./services/new-service-go
     ports:
       - "8123:8123"
   ```

### API Development Workflow

1. **Обновить OpenAPI спецификацию**
2. **Сгенерировать код**: `make generate-api`
3. **Реализовать handlers**
4. **Протестировать**: `curl http://localhost:{port}/health`
5. **Добавить в docker-compose**

## 📊 Мониторинг

### Запуск мониторинга
```bash
docker-compose -f docker-compose.monitoring.yml up -d
```

### Доступ
- **Grafana**: http://localhost:3000 (admin/admin123)
- **Prometheus**: http://localhost:9090
- **Loki**: http://localhost:3100

### Dashboards
- **NECP Game Services Overview** - общая статистика
- Service health, request rates, response times, resource usage

## 🧪 Тестирование

### Health checks
```bash
# Все сервисы
./scripts/system-check.sh

# Конкретный сервис
curl http://localhost:8100/health
```

### API тестирование
```bash
# Требуется JWT токен для большинства endpoints
curl -H "Authorization: Bearer <token>" \
     http://localhost:8100/api/v1/achievements
```

## 🚢 Развертывание

### Production setup
1. Настроить environment variables
2. Использовать production-grade базы данных
3. Настроить secrets management
4. Включить TLS/HTTPS
5. Настроить load balancing

### Environment Variables
```bash
# Database
DATABASE_URL=postgres://user:pass@host:5432/db

# Redis
REDIS_ADDR=redis:6379

# JWT
JWT_SECRET=your-production-secret

# Services
ADDR=0.0.0.0:8100
```

## 🤝 Contributing

### Code Style
- Go: стандартный formatter (`gofmt`)
- Commits: conventional commits
- PR: требуется code review

### Development Setup
```bash
# Установить dependencies
go mod download

# Запустить локально
go run main.go

# Сгенерировать API
make generate-api

# Запустить тесты
go test ./...
```

## 📚 Документация

- [API Specifications](./proto/openapi/) - OpenAPI 3.0 specs
- [Monitoring Setup](./MONITORING_SETUP.md) - Настройка мониторинга
- [Service Validation](./knowledge/implementation/api-requirements/SERVICE_VALIDATION_REPORT.md) - Отчеты о проверках
- [Architecture](./knowledge/implementation/architecture/) - Архитектурная документация

## 🔒 Безопасность

- JWT-based authentication
- Input validation через OpenAPI schemas
- Rate limiting (запланировано)
- Audit logging (запланировано)

## 📈 Производительность

### Текущие метрики
- **27 сервисов** запущено одновременно
- **100% health checks** passing
- **Sub-100ms** response times
- **Low memory footprint** per service

### Оптимизации
- OGEN для высокопроизводительного routing
- Context timeouts для всех операций
- Goroutine monitoring и limits
- Connection pooling для БД

## 🐛 Troubleshooting

### Сервис не запускается
```bash
# Проверить логи
docker logs necpgame-service-name-1

# Проверить health
curl http://localhost:{port}/health
```

### Метрики не собираются
```bash
# Проверить endpoint
curl http://localhost:{port}/metrics

# Проверить конфигурацию Prometheus
docker logs necpgame-prometheus
```

### Высокое потребление ресурсов
```bash
# Проверить goroutines
curl http://localhost:{port}/metrics | grep go_goroutines

# Проверить память
docker stats necpgame-service-name-1
```

## 📄 Лицензия

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Команда

- **Backend Team** - Go микросервисы
- **API Designer** - OpenAPI спецификации
- **DevOps** - Docker и инфраструктура
- **QA** - Тестирование и валидация

## 🎯 Roadmap

### Phase 1 OK (Complete)
- [x] Basic service architecture
- [x] Docker containerization
- [x] Health checks implementation
- [x] Monitoring stack setup

### Phase 2 🚧 (In Progress)
- [ ] Full API implementation
- [ ] Authentication integration
- [ ] Database schema completion
- [ ] End-to-end testing

### Phase 3 📋 (Planned)
- [ ] Performance optimization
- [ ] Security hardening
- [ ] Production deployment
- [ ] Scaling and load balancing

---

**Status**: 🟢 **Infrastructure Ready** - All 27 services healthy and monitored
