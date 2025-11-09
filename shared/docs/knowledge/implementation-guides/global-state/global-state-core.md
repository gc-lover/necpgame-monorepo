---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:00
**api-readiness-notes:** Global State Core микрофича. Архитектура системы, Event Sourcing паттерн, Event Store schema. ~400 строк.
---

# Global State Core - Архитектура системы

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 06:00  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Global State архитектура и Event Sourcing  
**Размер:** ~400 строк ✅  

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

**Global State Core** - централизованная система управления состоянием мира через Event Sourcing.

**Ключевые возможности:**
- ✅ Регистрация ВСЕХ событий в игре
- ✅ Event Store (PostgreSQL)
- ✅ State Reconstruction
- ✅ Time Travel (откат состояния)

---

## Архитектура системы

```
CLIENT (Frontend)
    ↓ WebSocket
API GATEWAY
    ↓
BACKEND SERVICES (Quest, Combat, Economy, Social)
    ↓
EVENT BUS (Kafka/RabbitMQ)
    ↓
Event Store Writer | Global State Manager | Analytics
    ↓
PERSISTENCE (PostgreSQL + Redis)
```

---

## Event Sourcing Pattern

### Концепция

**Традиционный CRUD:**
```
Player Level: 50 (только текущее)
❌ Потеря истории изменений
```

**Event Sourcing:**
```
Event 1: PlayerLeveledUp (1→2)
Event 2: PlayerLeveledUp (2→3)
...
Event 50: PlayerLeveledUp (49→50)
✅ Полная история + replay + аудит
```

---

## Event Store Schema

```sql
CREATE TABLE game_events (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    event_type VARCHAR(100) NOT NULL,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(200) NOT NULL,
    
    -- Метаданные
    event_version INTEGER NOT NULL DEFAULT 1,
    correlation_id UUID,
    causation_id UUID,
    
    -- Данные
    event_data JSONB NOT NULL,
    metadata JSONB,
    
    -- Контекст
    server_id VARCHAR(100) NOT NULL,
    player_id UUID,
    session_id UUID,
    
    -- Время
    event_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    
    -- Влияние
    state_changes JSONB,
    affected_players JSONB,
    
    -- Обработка
    is_processed BOOLEAN DEFAULT FALSE,
    processing_error TEXT,
    retry_count INTEGER DEFAULT 0,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_events_aggregate ON game_events(aggregate_type, aggregate_id);
CREATE INDEX idx_events_type ON game_events(event_type);
CREATE INDEX idx_events_timestamp ON game_events(event_timestamp DESC);
CREATE INDEX idx_events_player ON game_events(player_id);
CREATE INDEX idx_events_unprocessed ON game_events(is_processed) WHERE is_processed = FALSE;
```

---

## State Store Schema

```sql
CREATE TABLE global_state (
    id SERIAL PRIMARY KEY,
    server_id VARCHAR(100) NOT NULL,
    
    state_key VARCHAR(300) NOT NULL,
    state_category VARCHAR(50) NOT NULL,
    
    state_value TEXT NOT NULL,
    state_type VARCHAR(20) NOT NULL,
    
    description TEXT,
    owner_id VARCHAR(200),
    region VARCHAR(100),
    
    version INTEGER NOT NULL DEFAULT 1,
    previous_value TEXT,
    changed_by_event_id UUID,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    expires_at TIMESTAMP,
    is_permanent BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_changed_by_event FOREIGN KEY (changed_by_event_id) 
        REFERENCES game_events(event_id),
    UNIQUE(server_id, state_key)
);

CREATE INDEX idx_global_state_server_category ON global_state(server_id, state_category);
CREATE INDEX idx_global_state_key ON global_state(state_key);
CREATE INDEX idx_global_state_owner ON global_state(owner_id);
```

---

## Snapshots Schema

```sql
CREATE TABLE state_snapshots (
    id BIGSERIAL PRIMARY KEY,
    server_id VARCHAR(100) NOT NULL,
    state_category VARCHAR(50) NOT NULL,
    
    snapshot_data JSONB NOT NULL,
    snapshot_version BIGINT NOT NULL,
    
    snapshot_timestamp TIMESTAMP NOT NULL,
    last_event_id UUID NOT NULL,
    event_count BIGINT NOT NULL,
    
    snapshot_size_bytes INTEGER,
    creation_duration_ms INTEGER,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_snapshot_event FOREIGN KEY (last_event_id) 
        REFERENCES game_events(event_id),
    UNIQUE(server_id, state_category, snapshot_version)
);

CREATE INDEX idx_snapshots_server_category ON state_snapshots(server_id, state_category);
CREATE INDEX idx_snapshots_version ON state_snapshots(snapshot_version DESC);
```

**Стратегия:**
- Каждые 1000 событий → snapshot
- Каждый час → snapshot
- При смене лиги → snapshot

---

## Иерархия State Keys

**Формат:** `category.entity.attribute[.subAttribute]`

**Примеры:**

**Player State:**
```
player.{playerId}.level = 50
player.{playerId}.attributes.STR = 18
player.{playerId}.reputation.arasaka = 45
player.{playerId}.quest.{questId}.status = "ACTIVE"
```

**World State:**
```
world.territory.watson.controller = "Arasaka"
world.npc.morgana.fate = "hero"
world.faction.arasaka.power = 75
```

**Economy State:**
```
economy.item.mantisBlades.price = 6500
economy.currency.eurodollar.exchangeRate.yen = 0.85
```

---

## Связанные документы

- `.BRAIN/05-technical/global-state/global-state-events.md` - Типы событий (микрофича 2/5)
- `.BRAIN/05-technical/global-state/global-state-management.md` - State Management (микрофича 3/5)
- `.BRAIN/05-technical/global-state/global-state-sync.md` - Синхронизация (микрофича 4/5)
- `.BRAIN/05-technical/global-state/global-state-operations.md` - Operations (микрофича 5/5)

---

## История изменений

- **v1.0.0 (2025-11-07 06:00)** - Микрофича 1/5: Global State Core (split from global-state-system.md)
