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
    PlayerID     string
    NPCID        string
    Relationship RelationshipLevel
    Trust        int
    Loyalty      int
    LastInteraction time.Time
    Events       []RelationshipEvent
}
```

### 2. Quest/Order System (Система заказов)

```go
type OrderSystem struct {
    activeOrders map[string]*PlayerOrder
    orderQueue   chan *OrderRequest
    workers      []*OrderWorker
}

type PlayerOrder struct {
    ID          string
    PlayerID    string
    Type        OrderType // escort, delivery, assassination, etc.
    Target      string
    Reward      OrderReward
    Deadline    time.Time
    Status      OrderStatus
    AssignedNPC *NPC
}
```

### 3. NPC Hiring System (Система найма NPC)

```go
type NPCHiringSystem struct {
    availableNPCs map[string]*HireableNPC
    contracts     map[string]*NPCContract
    scheduler     *NPCScheduler
}

type HireableNPC struct {
    ID          string
    Name        string
    Class       NPCClass
    Skills      []NPCSkill
    Price       int
    Availability NPCAvailability
    Reputation  int
}
```

## Система отношений

### Уровни отношений
- **Stranger** (0-10): Незнакомец
- **Acquaintance** (11-30): Знакомый
- **Friend** (31-60): Друг
- **Close Friend** (61-90): Близкий друг
- **Trusted Ally** (91+): Доверенный союзник

### Факторы влияния
- **Trust**: Доверие (влияет на торговлю, информацию)
- **Loyalty**: Лояльность (влияет на помощь в бою)
- **Reputation**: Репутация (влияет на социальный статус)

## Типы заказов

### 1. Escort Missions (Сопровождение)
- Защита NPC во время путешествия
- Безопасная доставка ценностей
- Сопровождение караванов

### 2. Delivery Quests (Доставка)
- Передача сообщений
- Доставка предметов
- Срочные посылки

### 3. Assassination Contracts (Контракты)
- Устранение целей
- Сбор информации
- Саботаж

### 4. Investigation Tasks (Расследования)
- Поиск пропавших людей
- Расследование преступлений
- Сбор улик

## Система найма NPC

### Классы NPC
- **Mercenary** (Наемник): Бойцы для сопровождения
- **Courier** (Курьер): Специалисты по доставке
- **Informant** (Информатор): Источники информации
- **Specialist** (Специалист): Уникальные навыки

### Навыки NPC
- Combat Skills (Боевые навыки)
- Stealth Skills (Скрытность)
- Social Skills (Социальные навыки)
- Technical Skills (Технические навыки)

## API Интерфейсы

### Relationship Management

```go
// Get player relationships
GET /api/v1/social/relationships/{playerId}

// Update relationship
POST /api/v1/social/relationships/{playerId}/{npcId}

// Get reputation with factions
GET /api/v1/social/reputation/{playerId}
```

### Order System

```go
// Get available orders
GET /api/v1/social/orders/available

// Accept order
POST /api/v1/social/orders/{orderId}/accept

// Complete order
POST /api/v1/social/orders/{orderId}/complete
```

### NPC Hiring

```go
// Get available NPCs for hire
GET /api/v1/social/npcs/available

// Hire NPC
POST /api/v1/social/npcs/{npcId}/hire

// Manage contract
PUT /api/v1/social/contracts/{contractId}
```

## База данных

### Таблицы отношений

```sql
CREATE TABLE player_relationships (
    player_id VARCHAR(36) NOT NULL,
    npc_id VARCHAR(36) NOT NULL,
    relationship_level INT DEFAULT 0,
    trust INT DEFAULT 0,
    loyalty INT DEFAULT 0,
    last_interaction TIMESTAMP,
    PRIMARY KEY (player_id, npc_id)
);

CREATE TABLE relationship_events (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    npc_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    impact INT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Таблицы заказов

```sql
CREATE TABLE player_orders (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    order_type VARCHAR(50) NOT NULL,
    target VARCHAR(255),
    reward_gold INT DEFAULT 0,
    reward_items JSON,
    deadline TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active',
    assigned_npc_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Таблицы найма NPC

```sql
CREATE TABLE hireable_npcs (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    class VARCHAR(50) NOT NULL,
    skills JSON,
    base_price INT NOT NULL,
    availability VARCHAR(20) DEFAULT 'available',
    reputation INT DEFAULT 0
);

CREATE TABLE npc_contracts (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    npc_id VARCHAR(36) NOT NULL,
    contract_type VARCHAR(50) NOT NULL,
    duration_hours INT NOT NULL,
    payment INT NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active'
);
```

## Геймплейные механики

### Динамические отношения
- Отношения меняются на основе действий игрока
- NPC помнят прошлые взаимодействия
- Репутация влияет на доступ к контенту

### Экономика заказов
- Цены зависят от сложности и риска
- Бонусы за своевременное выполнение
- Штрафы за провал

### NPC развитие
- NPC могут улучшать навыки через контракты
- Репутация влияет на доступность NPC
- Специальные NPC с уникальными способностями

## Мониторинг и аналитика

### Метрики системы
- Количество активных заказов
- Уровень удовлетворенности NPC
- Популярность различных типов заказов
- Эффективность найма NPC

### Логирование событий
- Все изменения отношений
- Выполнение заказов
- Контракты найма
- Социальные взаимодействия

## Интеграция с другими системами

### Quest System
- Социальные квесты зависят от отношений
- Заказы могут быть частью квестовых линий

### Economy System
- Торговля зависит от отношений
- Награды за заказы влияют на экономику

### Combat System
- Найм NPC влияет на боевые возможности
- Репутация влияет на союзников в бою
