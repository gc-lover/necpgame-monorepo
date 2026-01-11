# AI Behavior Engine Service

## Обзор

**AI Behavior Engine Service** - передовой движок искусственного интеллекта для систем поведения в NECPGAME MMOFPS RPG.

## Назначение домена

Предоставляет комплексное управление поведением ИИ включая адаптивное масштабирование сложности, деревья поведений NPC, процедурную генерацию, аналитику поведения игроков, динамическую генерацию квестов и оптимизацию боевого ИИ.

## Целевые показатели производительности

- **P99 Latency**: <15ms для операций ИИ
- **Память**: <50MB на зону
- **Задержка решения**: <10ms на сущность ИИ
- **Одновременные сущности ИИ**: 500+ на зону поддерживается

## Архитектура

### SOLID/DRY Наследование домена

Сервис наследует из `game-entities.yaml`, предоставляя:
- Консистентную базовую структуру сущностей (id, timestamps, version) - **НЕ ДУБЛИРУЕМ!**
- Игровые доменные сущности через паттерн `allOf`
- Оптимистическую блокировку для конкурентных операций (поле version)
- Строгую типизацию с правилами валидации (min/max, patterns, enums)

### Ключевые компоненты

1. **Behavior Trees** - иерархическое выполнение задач и управление поведением
2. **Utility AI** - динамическое принятие решений
3. **Learning Systems** - распознавание паттернов и адаптивное обучение
4. **Coordination Logic** - координация поведения групп/стаи

### Типы ИИ врагов

#### Elite Mercenary Bosses
- **Поведенческие паттерны**: Сложные деревья поведения с фазовыми переходами
- **Масштабирование**: Zone-based sharding
- **Особенности**: Environmental Interaction, Ability Cooldown System

#### Cyberpsychic Elites
- **Поведенческие паттерны**: Mental State Controller
- **Масштабирование**: Player-centric replication
- **Особенности**: Psychic Ability Engine, Perception Manipulation

#### Corporate Elite Squads
- **Поведенческие паттерны**: Squad Coordination Framework
- **Масштабирование**: Squad-based clustering
- **Особенности**: Formation Manager, Communication Network

## API Endpoints

### Управление деревьями поведений

- `GET /behavior-trees` - список доступных деревьев поведений
- `POST /behavior-trees` - создание нового дерева поведений
- `GET /behavior-trees/{tree_id}` - получение дерева поведений
- `PUT /behavior-trees/{tree_id}` - обновление дерева поведений

### Принятие решений ИИ

- `POST /ai-decisions/{enemy_id}/evaluate` - оценка решения для ИИ врага
- `POST /ai-decisions/batch` - пакетная оценка решений для нескольких врагов

### Системы обучения

- `POST /learning/patterns/{enemy_id}/update` - обновление паттерна обучения
- `POST /learning/patterns/{enemy_id}/predict` - предсказание поведения игрока

### Логика координации

- `POST /coordination/squads/{squad_id}/behavior` - координация поведения отряда
- `POST /coordination/swarm/{swarm_id}/optimize` - оптимизация поведения стаи

### Адаптивная сложность

- `POST /adaptive-difficulty/{player_id}/adjust` - настройка адаптивной сложности
- `POST /adaptive-difficulty/global/balance` - балансировка глобальной сложности

### Мониторинг производительности

- `GET /metrics/behavior-performance` - метрики производительности поведения ИИ

## Оптимизации производительности

### Memory Pooling для сущностей ИИ

```go
// Pool для контекстов решений
var decisionContextPool = sync.Pool{
    New: func() interface{} {
        return &DecisionContext{
            CurrentState: &AiEntityState{},
            Environment:  &EnvironmentState{},
            Actions:      make([]*AiAction, 0, 10),
        }
    },
}
```

### Utility AI с кешированием

```go
// Кеширование оценок полезности действий
type UtilityCache struct {
    mu     sync.RWMutex
    cache  map[string]float64
    ttl    time.Duration
}

func (uc *UtilityCache) Get(actionKey string) (float64, bool) {
    uc.mu.RLock()
    defer uc.mu.RUnlock()

    if score, exists := uc.cache[actionKey]; exists {
        return score, true
    }
    return 0, false
}
```

### Оптимизированные деревья поведений

- **Compiled Trees**: Предварительная компиляция деревьев в оптимизированные структуры
- **Node Pooling**: Переиспользование узлов деревьев
- **Parallel Evaluation**: Параллельная оценка независимых ветвей

## Системы обучения

### Pattern Recognition

```go
type PatternRecognizer struct {
    patterns   map[string]*LearnedPattern
    confidence map[string]float64
    history    []*Observation
}

func (pr *PatternRecognizer) Learn(observation *Observation) {
    // Инкрементальное обучение без полной перестройки
    pattern := pr.identifyPattern(observation)
    pr.updateConfidence(pattern, observation.Weight)
    pr.adaptBehavior(pattern)
}
```

### Adaptive Difficulty Scaling

```go
type DifficultyScaler struct {
    playerMetrics *PlayerPerformanceMetrics
    currentLevel  int
    adjustmentRate float64
}

func (ds *DifficultyScaler) Scale() DifficultyAdjustment {
    effectiveness := ds.playerMetrics.CombatEffectiveness
    survival := ds.calculateSurvivalScore()
    completion := ds.playerMetrics.ObjectiveCompletion

    newLevel := ds.calculateOptimalLevel(effectiveness, survival, completion)
    return ds.createAdjustment(newLevel)
}
```

## Координация поведения

### Squad Coordination

- **Formation Manager**: Управление тактическим позиционированием
- **Role Assignment**: Динамическая специализация
- **Communication Network**: Координация отряда

### Swarm Intelligence

- **Emergent Behaviors**: Самовозникающие паттерны поведения
- **Distributed Decision Making**: Распределенное принятие решений
- **Adaptive Clustering**: Адаптивное формирование кластеров

## Мониторинг и Observability

### Метрики производительности

- **Decision Metrics**: Задержка решений, рейтинг успеха, распределение типов решений
- **Learning Metrics**: Точность предсказаний, скорость адаптации, количество паттернов
- **Coordination Metrics**: Эффективность координации отряда, эффективность стаи

### Critical Alerts

- Latency >15ms P99 для решений ИИ
- Снижение точности предсказаний <70%
- Сбои координации отряда
- Переполнение памяти >50MB на зону

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
- ai-behavior-engine-deployment.yaml
- ai-behavior-engine-service.yaml
- ai-behavior-engine-configmap.yaml

### CI/CD Pipeline
1. **Build**: Go compilation с оптимизациями
2. **Test**: Unit, integration, performance tests
3. **Security**: Vulnerability scanning, secrets check
4. **Deploy**: Blue-green deployment с rollback

## Связанные компоненты

- **AI Enemy Coordinator Service** - централизованная оркестрация ИИ
- **AI Combat Calculator Service** - расчеты урона/исцеления
- **AI Position Sync Service** - синхронизация движения
- **Quest Engine Service** - система квестов
- **Gameplay Service** - игровая логика

## Issue
#2300 - [API] Design OpenAPI specifications for AI Enemy Services