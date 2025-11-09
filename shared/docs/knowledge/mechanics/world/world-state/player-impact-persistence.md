---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:25
**api-readiness-notes:** Player Impact Persistence. БД schema, хранение влияния, API. ~200 строк.
---

# Player Impact Persistence - Хранение влияния

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:25  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Impact persistence  
**Размер:** ~200 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Database Schema

### player_world_impact

```sql
CREATE TABLE player_world_impact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    
    impact_type VARCHAR(50) NOT NULL,
    -- QUEST, ECONOMIC, POLITICAL, COMBAT, SOCIAL
    
    impact_target VARCHAR(200) NOT NULL,
    -- territory.watson, npc.morgana, item.cyberdeck.price
    
    impact_value INTEGER NOT NULL,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_impact_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE
);

CREATE INDEX idx_impact_player ON player_world_impact(player_id);
CREATE INDEX idx_impact_target ON player_world_impact(impact_target);
CREATE INDEX idx_impact_type ON player_world_impact(impact_type);
```

---

## API Endpoints

**GET /api/v1/world/state** - текущее состояние мира  
**GET /api/v1/world/impact/player/{playerId}** - влияние игрока  
**POST /api/v1/world/vote** - голосование  
**GET /api/v1/world/territories** - контроль территорий

---

## Связанные документы

- `.BRAIN/02-gameplay/world/world-state/player-impact-mechanics.md` - Mechanics (микрофича 1/3)
- `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md` - Systems (микрофича 2/3)
- `.BRAIN/05-technical/global-state/` - Global State System

---

## История изменений

- **v1.0.0 (2025-11-07 06:25)** - Микрофича 3/3 (split from world-state-player-impact.md)
