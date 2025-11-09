---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Техническая архитектура гильдий. Guild creation, membership, ranks/roles/permissions, guild bank, events, progression, wars.
---
---

- **Status:** queued
- **Last Updated:** 2025-11-08 17:55
---



# Guild System Backend - Техническая архитектура гильдий

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** высокий  
**Автор:** AI Brain Manager

---

## Краткое описание

**Guild System Backend** - техническая реализация системы гильдий/кланов для MMORPG.

**Ключевые возможности:**
- ✅ Guild creation/deletion
- ✅ Membership management (invite/join/leave/kick)
- ✅ Ranks & Permissions
- ✅ Guild bank (общий склад)
- ✅ Guild events (schedule, attendance)
- ✅ Guild progression (levels, perks)
- ✅ Guild wars support

---

## Микросервисная архитектура

**Ответственный микросервис:** social-service  
**Порт:** 8084  
**API Gateway маршрут:** `/api/v1/social/guilds/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- character-service: получение данных членов гильдии
- economy-service: guild bank операции
- world-service: guild territory management

**Event Bus события:**
- Публикует: `guild:created`, `guild:member-joined`, `guild:leveled-up`, `guild:war-started`
- Подписывается: `character:level-up` (guild experience), `world:territory-captured`

---

## Database Schema

```sql
CREATE TABLE guilds (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_name VARCHAR(100) UNIQUE NOT NULL,
    guild_tag VARCHAR(10) UNIQUE NOT NULL, -- [TAG]
    
    -- Leader
    leader_id UUID NOT NULL,
    
    -- Members
    member_count INTEGER DEFAULT 1,
    max_members INTEGER DEFAULT 50, -- Increases with guild level
    
    -- Level
    guild_level INTEGER DEFAULT 1,
    guild_experience BIGINT DEFAULT 0,
    
    -- Treasury
    guild_gold BIGINT DEFAULT 0,
    
    -- Description
    description TEXT,
    motd TEXT, -- Message of the day
    
    -- Status
    status VARCHAR(20) DEFAULT 'ACTIVE',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_guild_leader FOREIGN KEY (leader_id) 
        REFERENCES characters(id)
);

CREATE INDEX idx_guilds_name ON guilds(guild_name);
CREATE INDEX idx_guilds_tag ON guilds(guild_tag);

CREATE TABLE guild_members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    character_id UUID NOT NULL,
    
    rank VARCHAR(50) DEFAULT 'MEMBER',
    -- LEADER, OFFICER, VETERAN, MEMBER, RECRUIT
    
    permissions JSONB DEFAULT '[]',
    
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_member_guild FOREIGN KEY (guild_id) 
        REFERENCES guilds(id) ON DELETE CASCADE,
    CONSTRAINT fk_member_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE CASCADE,
    UNIQUE(character_id)
);

CREATE INDEX idx_guild_members_guild ON guild_members(guild_id);
CREATE INDEX idx_guild_members_character ON guild_members(character_id);
```

---

## Связанные документы

- `.BRAIN/02-gameplay/social/guilds-overview.md`

---

## История изменений

- **v1.0.0 (2025-11-07 05:20)** - Создан документ Guild System Backend
