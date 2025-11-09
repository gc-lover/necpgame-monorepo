---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:30
**api-readiness-notes:** Chat Channels микрофича. Типы каналов (global, local, party, guild, whisper), permissions, cooldowns, message limits. ~380 строк.
---

# Chat Channels - Типы чат-каналов

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 05:30  
**Приоритет:** критический  
**Автор:** AI Brain Manager

**Микрофича:** Chat channels  
**Размер:** ~380 строк ✅

---

- **Status:** completed
- **Last Updated:** 2025-11-08 22:40
---

## Краткое описание

**Chat Channels** - типы и настройки чат-каналов для внутриигровой коммуникации.

**Ключевые возможности:**
- ✅ 10+ типов каналов (global, local, party, guild, whisper, trade, combat)
- ✅ Permissions (кто может читать/писать)
- ✅ Cooldowns (anti-spam)
- ✅ Message length limits
- ✅ Scope (server/zone/party/personal)

---

## Database Schema

### Таблица `chat_channels`

```sql
CREATE TABLE chat_channels (
    id VARCHAR(100) PRIMARY KEY,
    
    -- Тип канала
    channel_type VARCHAR(50) NOT NULL,
    -- GLOBAL, LOCAL, ZONE, PARTY, RAID, GUILD, WHISPER, TRADE, etc
    
    -- Название (для custom channels)
    channel_name VARCHAR(100),
    
    -- Владелец (для custom/private channels)
    owner_id UUID,
    
    -- Члены (для private group chats)
    members JSONB, -- Array of player IDs
    
    -- Настройки
    max_members INTEGER,
    is_public BOOLEAN DEFAULT TRUE,
    is_moderated BOOLEAN DEFAULT FALSE,
    
    -- Permissions
    can_read JSONB, -- Array of roles/player IDs who can read
    can_write JSONB, -- Array of roles/player IDs who can write
    can_moderate JSONB, -- Array of moderator IDs
    
    -- Cooldowns
    message_cooldown INTEGER DEFAULT 0, -- Seconds
    max_message_length INTEGER DEFAULT 500,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_channel_owner FOREIGN KEY (owner_id) 
        REFERENCES players(id) ON DELETE SET NULL
);

CREATE INDEX idx_channels_type ON chat_channels(channel_type);
CREATE INDEX idx_channels_owner ON chat_channels(owner_id);
```

---

## Типы каналов

### 1. Global Channels (серверные)

**GLOBAL:**
- Scope: Весь сервер
- Cooldown: 5 секунд
- Max length: 200 символов
- Moderation: Строгая

**TRADE:**
- Scope: Весь сервер
- Cooldown: 30 секунд (anti-spam)
- Max length: 300 символов
- Only: Торговые объявления

**NEWBIE:**
- Scope: Игроки level 1-20
- Cooldown: 2 секунды
- Max length: 200 символов
- Helpers: Опытные могут помогать

### 2. Local Channels (локальные)

**LOCAL:**
- Scope: Radius 100m
- Cooldown: Нет
- Max length: 300 символов
- Real-time (только рядом)

**ZONE:**
- Scope: Текущая зона (Watson, Westbrook)
- Cooldown: 3 секунды
- Max length: 250 символов

**INSTANCE:**
- Scope: Текущий инстанс (dungeon, raid)
- Cooldown: Нет
- Max length: 500 символов
- Auto-active в dungeons

### 3. Group Channels (групповые)

**PARTY:**
- Scope: Party members (max 5)
- Cooldown: Нет
- Max length: 500 символов
- Tactical commands support

**RAID:**
- Scope: Raid members (max 15)
- Cooldown: Нет
- Max length: 500 символов
- Raid leader commands

**GUILD:**
- Scope: Guild members
- Cooldown: 1 секунда
- Max length: 500 символов
- Guild events notifications

**GUILD_OFFICER:**
- Scope: Guild officers
- Cooldown: Нет
- Max length: 1000 символов
- Private strategy discussions

### 4. Private Channels (личные)

**WHISPER:**
- Scope: 1-on-1 messages
- Cooldown: Нет
- Max length: 1000 символов
- History: 30 дней

**SYSTEM:**
- Scope: System notifications
- Read-only для игроков
- Quests, rewards, achievements

### 5. Combat Channels (боевые)

**COMBAT_LOG:**
- Scope: Current combat
- Read-only
- Auto-generated: damage, heals, deaths

**EMOTE:**
- Scope: Local (radius 100m)
- Cooldown: Нет
- Max length: 100 символов
- RP messages: "/wave", "/dance"

---

## Channel Recipients

### Определение получателей

```java
private List<UUID> getRecipients(ChannelType channelType, String channelId) {
    switch (channelType) {
        case GLOBAL:
        case TRADE:
            return sessionService.getAllActivePlayers(getServerId());
            
        case LOCAL:
            return locationService.getPlayersNearby(getSenderId(), 100);
            
        case ZONE:
            return locationService.getPlayersInZone(channelId);
            
        case PARTY:
            return partyService.getMembers(UUID.fromString(channelId));
            
        case RAID:
            return raidService.getMembers(UUID.fromString(channelId));
            
        case GUILD:
            return guildService.getOnlineMembers(UUID.fromString(channelId));
            
        case WHISPER:
            return List.of(getRecipientId());
            
        default:
            return List.of();
    }
}
```

---

## API Endpoints

**GET `/api/v1/chat/channels`** - список доступных каналов
**POST `/api/v1/chat/channels/join`** - присоединиться
**POST `/api/v1/chat/channels/leave`** - покинуть
**GET `/api/v1/chat/channels/{type}/members`** - участники канала

---

## Связанные документы

- `.BRAIN/05-technical/backend/chat/chat-moderation.md` - Модерация (микрофича 2/3)
- `.BRAIN/05-technical/backend/chat/chat-features.md` - Features (микрофича 3/3)
- `.BRAIN/05-technical/backend/session-management-system.md` - Sessions

---

## История изменений

- **v1.0.0 (2025-11-07 05:30)** - Микрофича 1/3: Chat Channels (split from chat-system.md)



