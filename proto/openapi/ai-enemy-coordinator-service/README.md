# AI Enemy Coordinator Service

## Обзор

**AI Enemy Coordinator Service** - центральный микросервис оркестрации систем ИИ врагов в NECPGAME MMOFPS RPG.

## Назначение домена

Обеспечивает централизованную координацию и управление состоянием всех сущностей ИИ врагов в игровом мире. Обрабатывает жизненный цикл ИИ, оптимизацию производительности и поддержку до 500+ сущностей ИИ на зону.

## Целевые показатели производительности

- **P99 Latency**: <50ms для всех endpoints
- **Память на инстанс**: <50KB baseline
- **Одновременные пользователи**: 10,000+ поддерживается
- **Сущности ИИ на зону**: 500+ поддерживается
- **Uptime**: 99.9% SLA

## Архитектура

### SOLID/DRY Наследование домена

Сервис наследует из `game-entities.yaml`, предоставляя:
- Консистентную базовую структуру сущностей (id, timestamps, version) - **НЕ ДУБЛИРУЕМ!**
- Игровые доменные сущности через паттерн `allOf`
- Оптимистическую блокировку для конкурентных операций (поле version)
- Строгую типизацию с правилами валидации (min/max, patterns, enums)

### Ключевые компоненты

1. **AI Enemy Lifecycle Management** - управление жизненным циклом ИИ сущностей
2. **Zone Coordination** - зональная координация и синхронизация
3. **Performance Monitoring** - метрики производительности ИИ
4. **Load Balancing** - динамическая балансировка нагрузки

## API Endpoints

### Основные операции

- `POST /ai-enemies/{enemy_id}/spawn` - спавн сущности ИИ врага
- `POST /ai-enemies/{enemy_id}/despawn` - деспавн сущности ИИ врага
- `GET /ai-enemies/{enemy_id}/state` - получение состояния ИИ врага
- `PUT /ai-enemies/{enemy_id}/state` - обновление состояния ИИ врага

### Зональная координация

- `GET /zones/{zone_id}/ai-coordination` - статус координации ИИ зоны
- `POST /zones/{zone_id}/ai-coordination/balance` - балансировка нагрузки ИИ зоны

### Мониторинг производительности

- `GET /metrics/ai-performance` - метрики производительности ИИ

## Оптимизации производительности

### Struct Alignment (экономия памяти 30-50%)

```go
// Пример оптимизированной структуры Go
type AiEnemyEntity struct {
    // Large fields first (16-24B)
    Position      Vector3    `json:"position"`
    BehaviorState AiBehaviorState `json:"behavior_state"`

    // Medium fields (8B)
    ID        uuid.UUID `json:"id"`
    ZoneID    uuid.UUID `json:"zone_id"`
    CreatedAt time.Time `json:"created_at"`
    UpdatedAt time.Time `json:"updated_at"`

    // Small fields (4B)
    Level       int32 `json:"level"`
    Health      int32 `json:"health"`
    MaxHealth   int32 `json:"max_health"`
    EnemyType   AiEnemyType `json:"enemy_type"`
    Faction     Faction `json:"faction"`

    // Byte fields (1B)
    Version     int8 `json:"version"`
}
```

### Memory Pooling

- **Object Pooling** для сущностей ИИ врагов
- **Zero-allocations** в hot paths принятия решений
- **Connection pooling** для подключений к БД

### Concurrency Optimizations

- **Optimistic Locking** для конкурентных обновлений
- **Event-driven architecture** для синхронизации состояния
- **Horizontal scaling** через zone-based sharding

## Типы ИИ врагов

### Elite Mercenary Bosses
- **Архитектурный паттерн**: Singleton Boss Controller
- **Масштабирование**: Zone-based sharding
- **Компоненты**: Boss State Machine, Ability Cooldown System, Environmental Interaction

### Cyberpsychic Elites
- **Архитектурный паттерн**: Mental State Controller
- **Масштабирование**: Player-centric replication
- **Компоненты**: Psychic Ability Engine, Perception Manipulation, Mind Reading System

### Corporate Elite Squads
- **Архитектурный паттерн**: Squad Coordination Framework
- **Масштабирование**: Squad-based clustering
- **Компоненты**: Formation Manager, Role Assignment, Communication Network

## Мониторинг и Observability

### Метрики производительности
- P99 latency по всем сервисам
- Использование памяти/CPU по зонам
- Время принятия решений ИИ
- Количество активных зон

### Метрики бизнеса
- Количество активных guild wars
- Завершение миссий cyberspace
- Использование интерактивных объектов
- Вовлеченность игроков по типам квестов

### Critical Alerts
- Latency >50ms P99
- Сбои спавна ИИ врагов
- Коррупция состояния квестов
- Сбои синхронизации интерактивных объектов

## Безопасность

### API Security
- JWT authentication для всех сервисов
- Rate limiting по игроку/сервису
- Input validation и sanitization
- OWASP Top 10 compliance

### Data Security
- Зашифрованные подключения к БД
- PII data protection
- Audit logging для sensitive operations
- Secure key management

## Развертывание

### Kubernetes Manifests
- ai-enemy-coordinator-deployment.yaml
- ai-enemy-coordinator-service.yaml
- ai-enemy-coordinator-configmap.yaml

### CI/CD Pipeline
1. **Build**: Go compilation с оптимизациями
2. **Test**: Unit, integration, performance tests
3. **Security**: Vulnerability scanning, secrets check
4. **Deploy**: Blue-green deployment с rollback

## Связанные компоненты

- **AI Behavior Engine Service** - движок поведений ИИ
- **AI Combat Calculator Service** - расчеты урона/исцеления
- **AI Position Sync Service** - синхронизация движения
- **Quest Engine Service** - система квестов
- **Interactive Objects Service** - интерактивные объекты

## Issue
#2300 - [API] Design OpenAPI specifications for AI Enemy Services