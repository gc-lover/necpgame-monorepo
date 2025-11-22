# NECPGAME

Cyberpunk 2077 style competitive shooter with MMOFPS RPG elements and looting mechanics.

## Описание

NECPGAME - это киберспортивный шутер в стиле Cyberpunk 2077 с элементами MMOFPS RPG и лутинг шутера.

## Технологии

- **Backend:** Go микросервисы
- **Client:** Unreal Engine 5.7 и C++
- **Infrastructure:** Docker, Kubernetes, Envoy
- **Protocol:** Protocol Buffers для realtime коммуникации

## Структура проекта

```
necpgame/
├── services/          # Go микросервисы
│   ├── character-service-go/
│   ├── inventory-service-go/
│   ├── movement-service-go/
│   ├── matchmaking-go/
│   ├── realtime-gateway-go/
│   └── ws-lobby-go/
├── client/UE5/        # Unreal Engine 5.7 клиент
├── infrastructure/    # Docker, K8s, Envoy, Liquibase
├── proto/realtime/    # Protocol Buffers определения
├── scripts/           # Утилиты и скрипты
└── k8s/               # Kubernetes манифесты
```

## Быстрый старт

### Требования

- Go 1.21+
- Unreal Engine 5.7
- Docker и Docker Compose
- Kubernetes (для деплоя)

### Запуск локально

```bash
# Запуск сервисов через Docker Compose
docker-compose up -d

# Или запуск отдельных сервисов
cd services/character-service-go
go run main.go
```

## Разработка

### Workflow

1. Создайте Issue для задачи
2. Создайте ветку: `git checkout -b feature/issue-number-description`
3. Реализуйте изменения
4. Создайте Pull Request
5. После ревью и CI - мерж в `main`

### Стандарты кода

- Следуйте SOLID принципам
- Go: следуйте Go best practices
- UE5: следуйте Unreal Engine coding standards
- Классы не более 300-400 строк
- Без комментариев и TODO в коде

## CI/CD

GitHub Actions настроены для:
- Тестирования Go сервисов
- Валидации UE5 проекта
- Автоматического ревью зависимостей
- Автоматического закрытия неактивных issues

## Деплой

### Docker

```bash
docker build -t necpgame/character-service:latest services/character-service-go/
```

### Kubernetes

```bash
kubectl apply -f k8s/
```

## Лицензия

[Указать лицензию]

## Ссылки

- [Issues](https://github.com/gc-lover/necpgame-monorepo/issues)
- [Projects](https://github.com/gc-lover/necpgame-monorepo/projects)
