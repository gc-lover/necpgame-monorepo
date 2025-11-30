# Алгоритм определения агента для Issue

## Ключевые слова и паттерны

### Idea Writer (Status: Idea Writer - Todo/In Progress)
- Лор, lore, narrative, квесты, quests (концепции)
- NPC, персонажи, characters (концепции)
- Контент, content, game-design (концепции)
- Видение, vision, philosophy, философия
- Worldbuilding, world building
- Canon (если про концепции/документы)
- Сюжет, story, storyline (концепции)
- Диалоги, dialogues (концепции)
- Концепция, concept

### Content Writer (Status: Content Writer - Todo/In Progress)
- [Canon/Lore] Квест: ... (контентные квесты)
- Реализация контентных квестов
- Создание YAML файлов квестов
- Детальный лор для квестов
- Диалоги и ветвления (реализация)
- NPC взаимодействия (контент)
- Контентные квесты после Idea Writer
- Метки: `canon`, `lore`, `quest` (вместе с `content`)

### Architect (Status: Architect - Todo/In Progress)
- Система, system, architecture, архитектура
- Структурирование, структура
- Проектирование, design (техническое)
- Database, база данных, БД
- Coherence, целостность
- Микросервисы, microservices
- Компоненты, components
- Техническое задание

### API Designer (Status: API Designer - Todo/In Progress)
- API, endpoints, спецификация
- OpenAPI, протокол, protocol
- Request/Response схемы
- REST, gRPC (если про спецификацию)

### Backend Developer (Status: Backend - Todo/In Progress)
- [Backend] в заголовке (явное указание)
- Реализация, implementation (код)
- Go, сервисы, services
- Handlers, бизнес-логика
- Миграции БД, migrations
- Код, code (если явно про код)
- Chat System, Achievement System, Session Management (системы бекенда)
- Этап разработки: implementation (если про код)

### Network Engineer (Status: Network - Todo/In Progress)
- Сеть, network, Envoy
- gRPC, протокол (реализация)
- Realtime, синхронизация
- Protocol Buffers (реализация)

### DevOps (Status: DevOps - Todo/In Progress)
- Инфраструктура, infrastructure
- Автоматизация, automation
- Деплой, deployment
- CI/CD, GitHub Actions
- Docker, Kubernetes, K8s
- Мониторинг, monitoring

### Performance Engineer (Status: Performance - Todo/In Progress)
- Оптимизация, optimization
- Производительность, performance
- Бенчмарки, benchmarks
- Профилирование, profiling

### UE5 Developer (Status: UE5 - Todo/In Progress)
- Клиент, client, Unreal Engine
- C++, UE5
- UI, UX, интерфейс
- Визуальные ассеты (если про реализацию)
- Игровая механика (реализация)

### QA/Testing (Status: QA - Todo/In Progress)
- Тестирование, testing, тесты
- Баги, bugs
- Валидация, validation

### Release (Status: Release - Todo/In Progress)
- Релиз, release
- Release notes
- Деплой в продакшен

## Приоритет правил

1. Если есть явное указание на код/реализацию → Backend или UE5
2. Если про систему/архитектуру → Architect
3. Если про контентные квесты (Canon/Lore) с функциональными метками `canon`, `lore`, `quest` → Content Writer
4. Если про лор/контент/квесты (концепции) → Idea Writer
5. Если про инфраструктуру/деплой → DevOps
6. Если про сеть/протокол → Network
7. Если про оптимизацию → Performance
8. Если про тестирование → QA
9. Если про релиз → Release

## Workflow для контентных задач

**Контентные квесты (Canon/Lore) НЕ проходят через архитектурный этап:**

**Контентные квесты ОБЯЗАТЕЛЬНО проходят через импорт в БД:**

```
Idea Writer → Content Writer (создает + валидирует YAML) → Backend (импорт в БД) → QA (тестирование) → Release
```

**Важно:** 
- Content Writer сам валидирует YAML файлы
- **ВСЕГДА передает Backend Developer для импорта в БД** - без импорта контент не попадет в игру
- Backend Developer импортирует контент в БД через API endpoint `POST /api/v1/gameplay/quests/content/reload`
- QA тестирует функционал после импорта в БД

**Системные задачи проходят через архитектурный этап:**

```
Idea Writer → Architect → API Designer → Backend → Network → UE5 → QA → Release
```

## Примеры

- "[Canon] NPC Lore" → Idea Writer (концепция лора, NPC)
- "[Canon/Lore] Квест: Знак Голливуда" → Content Writer (контентный квест)
- "[Canon] Narrative Coherence System" → Architect (система, архитектура)
- "[Implementation] Укрепление автоматизации" → DevOps (автоматизация, инфраструктура)
- "[Quests] Las Vegas" → Idea Writer (концепция квестов)
- "Настройка GitHub App" → DevOps (инфраструктура, настройка)

