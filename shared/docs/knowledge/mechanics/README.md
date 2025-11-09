# Игровая механика

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-07
**api-readiness-notes:** Служебный файл-индекс раздела игровых механик. Сам не содержит механик для API. Дочерние документы с механиками имеют свои статусы готовности (большинство ready после расширений).

Этот раздел содержит описания всех игровых механик NECPGAME.

---

## Распределение по микросервисам

### 🎮 gameplay-service (Port 8083)
**Combat, Progression, PvP:**
- Combat system (shooter, extraction, abilities)
- Progression (leveling, skills, attributes)
- Quest engine
- Matchmaking

### 💰 economy-service (Port 8085)
**Economy, Trading, Crafting:**
- Inventory и equipment
- Trading (P2P, auction, market)
- Crafting system
- Currencies и resources
- Loot generation

### 👥 social-service (Port 8084)
**Social, NPC, Guilds:**
- Guilds/Clans
- NPC relationships
- Romances
- Chat, Friends, Party
- Mail, Notifications

### 🌍 world-service (Port 8086)
**World, Events, Raids:**
- World events
- Raids
- Territory/Building
- Global state
- Real-time sync

**Production-доступ:** все игровые сервисы публикуются через `https://api.necp.game/v1` (HTTP) и `wss://api.necp.game/v1`; OpenAPI спецификации содержат `info.x-microservice` с целевым сервисом.

---

## Структура

### Combat (Боевая система) → gameplay-service
- `combat/` - Боевые механики
  - shooter mechanics, extraction, abilities, implants
  - Combat session backend (05-technical)

### Progression (Прокачка и развитие) → gameplay-service
- `progression/` - Системы прогрессии
  - Skills, leveling, attributes, equipment
  - Progression backend (05-technical)

### Economy (Экономика) → economy-service
- `economy/` - Экономические системы
  - Trading, crafting, currencies
  - Inventory, loot systems (05-technical)

### Social (Социальные механики) → social-service
- `social/` - Социальные системы
  - Guilds, relationships, romances
  - Chat, friends, party (05-technical)

### World (Игровой мир) → world-service
- `world/` - Мировые системы
  - Events, raids, building
  - Global state (05-technical)

