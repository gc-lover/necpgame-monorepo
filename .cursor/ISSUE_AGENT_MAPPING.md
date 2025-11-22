# Алгоритм определения агента для Issue

## Ключевые слова и паттерны

### Idea Writer (agent:idea-writer, stage:idea)
- Лор, lore, narrative, квесты, quests
- NPC, персонажи, characters
- Контент, content, game-design
- Видение, vision, philosophy, философия
- Worldbuilding, world building
- Canon (если про контент/документы)
- Сюжет, story, storyline
- Диалоги, dialogues
- Концепция, concept

### Architect (agent:architect, stage:design)
- Система, system, architecture, архитектура
- Структурирование, структура
- Проектирование, design (техническое)
- Database, база данных, БД
- Coherence, целостность
- Микросервисы, microservices
- Компоненты, components
- Техническое задание

### API Designer (agent:api-designer, stage:api-design)
- API, endpoints, спецификация
- OpenAPI, протокол, protocol
- Request/Response схемы
- REST, gRPC (если про спецификацию)

### Backend Developer (agent:backend, stage:backend-dev)
- Реализация, implementation (код)
- Go, сервисы, services
- Handlers, бизнес-логика
- Миграции БД, migrations
- Код, code (если явно про код)

### Network Engineer (agent:network, stage:network)
- Сеть, network, Envoy
- gRPC, протокол (реализация)
- Realtime, синхронизация
- Protocol Buffers (реализация)

### DevOps (agent:devops, stage:infrastructure)
- Инфраструктура, infrastructure
- Автоматизация, automation
- Деплой, deployment
- CI/CD, GitHub Actions
- Docker, Kubernetes, K8s
- Мониторинг, monitoring

### Performance Engineer (agent:performance, stage:performance)
- Оптимизация, optimization
- Производительность, performance
- Бенчмарки, benchmarks
- Профилирование, profiling

### UE5 Developer (agent:ue5, stage:client-dev)
- Клиент, client, Unreal Engine
- C++, UE5
- UI, UX, интерфейс
- Визуальные ассеты (если про реализацию)
- Игровая механика (реализация)

### QA/Testing (agent:qa, stage:testing)
- Тестирование, testing, тесты
- Баги, bugs
- Валидация, validation

### Release (agent:release, stage:release)
- Релиз, release
- Release notes
- Деплой в продакшен

## Приоритет правил

1. Если есть явное указание на код/реализацию → Backend или UE5
2. Если про систему/архитектуру → Architect
3. Если про лор/контент/квесты → Idea Writer
4. Если про инфраструктуру/деплой → DevOps
5. Если про сеть/протокол → Network
6. Если про оптимизацию → Performance
7. Если про тестирование → QA
8. Если про релиз → Release

## Примеры

- "[Canon] NPC Lore" → Idea Writer (лор, NPC)
- "[Canon] Narrative Coherence System" → Architect (система, архитектура)
- "[Implementation] Укрепление автоматизации" → DevOps (автоматизация, инфраструктура)
- "[Quests] Las Vegas" → Idea Writer (квесты, лор)
- "Настройка GitHub App" → DevOps (инфраструктура, настройка)

