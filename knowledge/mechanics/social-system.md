# [Mechanics] Social система - отношения, заказы, найм NPC

## Issue: #140875791
**Тип:** Mechanics
**Статус:** In Progress
**Ответственный:** Backend Agent

## Обзор системы

Social система предоставляет комплексные механизмы взаимодействия между игроками и NPC, включая систему отношений, заказов, найма NPC и социальные квесты. Система поддерживает динамические отношения, влияющие на доступ к контенту и возможности в игре.

## Архитектурные компоненты

### 1. Relationship Manager (Менеджер отношений)

```go
type RelationshipManager struct {
    relationships map[string]*PlayerRelationship // playerID -> relationships
    reputation    map[string]int                 // factionID -> reputation
    influence     map[string]int                 // playerID -> influence score
    logger        *zap.Logger
}

type PlayerRelationship struct {
    PlayerID      string
    NPCID         string
    RelationshipLevel int    // -100 to +100 scale
    TrustLevel    int       // 0-100 scale
    LastInteraction time.Time
    Interactions  []InteractionRecord
}

type InteractionRecord struct {
    Timestamp time.Time
    Type      string // "dialogue", "trade", "quest", "combat"
    Outcome   string // "positive", "negative", "neutral"
    Impact    int    // relationship impact value
}
```

- **Ответственность:** Управление уровнем отношений между игроками и NPC, расчет репутации и влияния.
- **Взаимодействие:** Получает события от других систем (диалоги, квесты, торговля) и обновляет отношения.

### 2. Order System (Система заказов)

```go
type OrderSystem struct {
    activeOrders map[string]*PlayerOrder // orderID -> order
    orderTemplates map[string]*OrderTemplate
    executorPool *ExecutorPool
    logger      *zap.Logger
}

type PlayerOrder struct {
    ID          string
    PlayerID    string
    Type        string // "delivery", "assassination", "collection", "escort"
    Target      string
    Reward      OrderReward
    Deadline    time.Time
    Status      string // "open", "claimed", "completed", "failed", "expired"
    ClaimedBy   string // NPC ID who claimed the order
}

type OrderReward struct {
    Currency int
    Items    []string
    Reputation int
    Experience int
}

type ExecutorPool struct {
    availableNPCs map[string]*NPCExecutor
    busyNPCs      map[string]*NPCExecutor
    skillIndex    map[string][]string // skill -> npcIDs
}
```

- **Ответственность:** Управление заказами от игроков NPC-исполнителям, обработка выполнения заказов.
- **Взаимодействие:** Интегрируется с квестовой системой и экономикой.

### 3. NPC Hiring System (Система найма NPC)

```go
type NPCHiringSystem struct {
    availableNPCs map[string]*HirableNPC
    hiredNPCs     map[string]*HiredNPC      // playerID -> hired NPCs
    contractManager *ContractManager
    logger        *zap.Logger
}

type HirableNPC struct {
    NPCID       string
    Name        string
    Skills      []string
    BaseCost    int
    Availability string // "always", "limited", "quest_unlocked"
    Requirements HirableRequirements
}

type HiredNPC struct {
    NPCID       string
    PlayerID    string
    Contract    *NPCContract
    Status      string // "active", "on_mission", "resting", "dismissed"
    Loyalty     int    // 0-100 scale
    Experience  int    // earned experience
}

type NPCContract struct {
    Duration    time.Duration
    Salary      int
    Bonuses     []string
    TerminationTerms string
    SignedAt    time.Time
    ExpiresAt   time.Time
}
```

- **Ответственность:** Управление наймом NPC игроками, обработка контрактов и лояльности.
- **Взаимодействие:** Работает с Relationship Manager для расчета лояльности.

### 4. Social Quest Engine (Движок социальных квестов)

```go
type SocialQuestEngine struct {
    activeQuests map[string]*SocialQuest
    questGenerator *QuestGenerator
    relationshipTracker *RelationshipTracker
    logger       *zap.Logger
}

type SocialQuest struct {
    ID          string
    PlayerID    string
    NPCID       string
    Type        string // "relationship", "influence", "networking", "alliance"
    Objectives  []QuestObjective
    Rewards     SocialRewards
    Status      string // "active", "completed", "failed"
    Progress    map[string]int // objectiveID -> progress
}

type QuestObjective struct {
    ID          string
    Type        string // "improve_relationship", "gain_influence", "network_contacts"
    Target      string
    RequiredValue int
    CurrentValue int
}

type SocialRewards struct {
    RelationshipBonus int
    InfluenceBonus    int
    NewContacts      []string // NPC IDs
    SpecialAccess    []string // unlocked locations/quests
}
```

- **Ответственность:** Генерация и управление социальными квестами на основе отношений игрока.
- **Взаимодействие:** Интегрируется с основной квестовой системой.

### 5. Faction Dynamics (Динамика фракций)

```go
type FactionDynamics struct {
    factions     map[string]*Faction
    alliances    map[string][]string // factionID -> allied factionIDs
    conflicts    map[string][]string // factionID -> conflicting factionIDs
    influenceMap map[string]map[string]int // region -> faction -> influence
    logger       *zap.Logger
}

type Faction struct {
    ID          string
    Name        string
    Leader      string // NPC ID
    Members     []string // NPC IDs
    Reputation  int      // global reputation score
    Resources   int      // economic power
    MilitaryPower int    // combat strength
    Influence   map[string]int // region -> influence level
}
```

- **Ответственность:** Моделирование динамики между фракциями NPC, влияния на мир игры.
- **Взаимодействие:** Влияет на доступ к контенту и NPC поведению.

## Интеграция с другими системами

### Economy System
- Заказы влияют на рынок (цены, доступность товаров)
- Репутация фракций влияет на торговые скидки
- Нанятые NPC могут выполнять торговые миссии

### Quest System
- Социальные квесты генерируются на основе отношений
- Заказы могут превращаться в полноценные квесты
- Репутация влияет на доступ к эксклюзивным квестам

### Combat System
- Отношения влияют на поведение NPC в бою (союзники/враги)
- Нанятые NPC могут участвовать в боях
- Фракционная принадлежность влияет на исходы конфликтов

### World Events
- Социальные события могут влиять на отношения
- Заказы могут быть частью мировых событий
- Репутация фракций влияет на исход событий

## Примеры использования

### Сценарий 1: Построение сети контактов
Игрок начинает с низким уровнем влияния. Через диалоги, квесты и заказы он улучшает отношения с ключевыми NPC. Это открывает доступ к эксклюзивным заказам, информации и альянсам.

### Сценарий 2: Управление фракциями
Игрок может манипулировать отношениями между фракциями через заказы и социальные квесты. Это влияет на экономику регионов, доступность ресурсов и поведение NPC.

### Сценарий 3: Постоянный компаньон
Игрок нанимает NPC с уникальными навыками. Со временем NPC развивается, повышает лояльность и становится ценным союзником в различных активностях.

## Технические требования

### Производительность
- Поддержка 1000+ одновременных игроков с социальными взаимодействиями
- Эффективное хранение и кеширование отношений
- Оптимизированные запросы к базе данных для расчета влияния

### Масштабируемость
- Горизонтальное масштабирование компонентов
- Распределенное хранение социальных данных
- Кеширование горячих данных (Redis)

### Надежность
- Транзакционная целостность операций с отношениями
- Graceful handling сетевых сбоев
- Backup и recovery социальных данных

## Метрики и мониторинг

- **Social Health Metrics:** Уровни отношений, репутация фракций, влияние игроков
- **Order Fulfillment Rate:** Процент выполненных заказов
- **NPC Utilization:** Процент занятых нанятых NPC
- **Quest Completion:** Успешность социальных квестов
- **Performance:** Время обработки социальных событий, latency запросов

## Roadmap

### Фаза 1: Core Relationships
- Базовая система отношений игрок-NPC
- Простые социальные квесты
- Основные фракции

### Фаза 2: Advanced Social Mechanics
- Система заказов и найма NPC
- Динамика фракций
- Комплексные социальные цепочки

### Фаза 3: World Integration
- Интеграция с мировыми событиями
- Глобальное влияние игроков
- Эмерджентное повествование