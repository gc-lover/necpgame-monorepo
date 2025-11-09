---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:59
**api-readiness-notes:** Achievement System Core. Система достижений - типы, категории, прогресс, rewards. ~400 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 02:38
---

# Achievement System - Core System

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:59  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Achievement core mechanics  
**Размер:** ~380 строк ✅

**Связанные микрофичи:**
- [Achievement Tracking](./achievement-tracking.md)
- [Achievement Examples & API](./achievement-examples-api.md)

---

## Краткое описание

**Achievement System** - система достижений для мотивации и retention игроков в MMORPG NECPGAME.

**Ключевые возможности:**
- ✅ 500+ достижений (различные категории)
- ✅ Прогресс-трекинг (quest/kill/craft/social)
- ✅ Rewards (titles, cosmetics, perks)
- ✅ Rarity tiers (Common → Legendary)
- ✅ Hidden achievements (секретные)
- ✅ Meta achievements (коллекции)

---

## Архитектура системы

```
Player Action (kill/quest/craft/etc)
    ↓
Event Bus → Achievement Listener
    ↓
Check Achievement Conditions
    ↓
Update Progress (0-100%)
    ↓
Achievement Unlocked? → Grant Rewards
    ↓
Notification + UI Update
```

---

## Типы достижений

### 1. One-Time Achievements (Одноразовые)

**Пример:**
```
"First Blood" - Убить первого врага
"Welcome to Night City" - Посетить Night City
"Netrunner Initiate" - Совершить первый hack
```

**Механика:** Событие → сразу unlock

---

### 2. Progressive Achievements (Прогрессивные)

**Пример:**
```
"Killer" - Убить 100/500/1000/5000 врагов
"Wealthy" - Накопить 10k/100k/1M/10M eddies
"Social Butterfly" - Встретить 10/50/100/500 NPC
```

**Механика:** Счетчик → unlock при достижении порога

**Тиры:**
- Tier 1: 100 (Bronze)
- Tier 2: 500 (Silver)
- Tier 3: 1000 (Gold)
- Tier 4: 5000 (Platinum)
- Tier 5: 10000 (Diamond)

---

### 3. Hidden Achievements (Секретные)

**Пример:**
```
"Easter Egg Hunter" - Найти 10 секретных пасхалок
"Ghost Walker" - Пройти всю локацию незамеченным
"Pacifist Run" - Пройти квест без убийств
```

**Механика:** Не показываются до unlock

---

### 4. Seasonal Achievements (Сезонные)

**Пример:**
```
"League Champion" - Топ 100 в лиге 2093
"Event Participant" - Участие в мировом событии
"Holiday Spirit" - Собрать все новогодние предметы
```

**Механика:** Доступны только в определенный период

---

### 5. Meta Achievements (Коллекции)

**Пример:**
```
"Master of Combat" - Unlock все боевые достижения (50/50)
"Romance Expert" - Unlock все романтические (25/25)
"Economy Tycoon" - Unlock все экономические (30/30)
```

**Механика:** Unlock при получении всех достижений категории

---

## Категории достижений

### Combat (50+ achievements)
- Kill counts (по типам врагов)
- Weapon masteries (каждое оружие)
- Combat challenges (headshots, combos)
- Boss kills (каждый босс)

### Quests (100+ achievements)
- Main quests (каждый основной)
- Side quests (каждый побочный)
- Romance quests (каждая романтика)
- Faction quests (каждая фракция)

### Social (40+ achievements)
- Friend milestones (10/50/100 friends)
- Romance milestones (first kiss, proposal)
- Guild achievements (create, lead, win war)
- Reputation (exalted with each faction)

### Economy (50+ achievements)
- Wealth milestones (10k/100k/1M/10M)
- Trading (100/1000 trades)
- Crafting (100/1000 crafts)
- Market mastery (profit thresholds)

### Exploration (60+ achievements)
- Locations (visit all POIs)
- Regions (visit all cities)
- Hidden spots (secret locations)
- Fast travel unlocks

### Skills (40+ achievements)
- Skill masteries (max level each skill)
- Attribute milestones (STR/DEX/INT 20)
- Perk unlocks (all perks in tree)
- Class masteries (max level class)

### Collections (30+ achievements)
- Weapon collector (all weapons)
- Armor collector (all sets)
- Implant collector (all implants)
- Vehicle collector (all vehicles)

### Special (30+ achievements)
- Easter eggs
- Speedruns
- Challenge runs (permadeath, no deaths)
- Unique combinations

---

## Database Schema

### Таблица `achievements`

```sql
CREATE TABLE achievements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Identification
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    
    -- Category
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(50),
    
    -- Type
    type VARCHAR(20) NOT NULL,
    
    -- Conditions
    conditions JSONB NOT NULL,
    
    -- Progress
    max_progress INTEGER DEFAULT 1,
    
    -- Rarity
    rarity VARCHAR(20) DEFAULT 'COMMON',
    points INTEGER DEFAULT 10,
    
    -- Visibility
    is_hidden BOOLEAN DEFAULT FALSE,
    is_seasonal BOOLEAN DEFAULT FALSE,
    season_id UUID,
    
    -- Rewards
    rewards JSONB,
    
    -- Meta
    is_meta BOOLEAN DEFAULT FALSE,
    required_achievements UUID[],
    
    -- Order
    display_order INTEGER DEFAULT 0,
    
    -- Timestamps
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_achievements_category ON achievements(category);
CREATE INDEX idx_achievements_type ON achievements(type);
CREATE INDEX idx_achievements_rarity ON achievements(rarity);
CREATE INDEX idx_achievements_seasonal ON achievements(season_id) 
    WHERE is_seasonal = TRUE;
```

### Таблица `player_achievements`

```sql
CREATE TABLE player_achievements (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    achievement_id UUID NOT NULL,
    
    -- Progress
    current_progress INTEGER DEFAULT 0,
    max_progress INTEGER NOT NULL,
    
    -- Status
    status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    
    -- Unlock
    unlocked_at TIMESTAMP,
    
    -- Tracking
    first_progress_at TIMESTAMP,
    last_progress_at TIMESTAMP,
    
    -- Metadata
    unlock_context JSONB,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player_achievement_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_player_achievement_achievement FOREIGN KEY (achievement_id) 
        REFERENCES achievements(id) ON DELETE CASCADE,
    
    UNIQUE(player_id, achievement_id)
);

CREATE INDEX idx_player_achievements_player ON player_achievements(player_id);
CREATE INDEX idx_player_achievements_status ON player_achievements(status);
CREATE INDEX idx_player_achievements_unlocked 
    ON player_achievements(unlocked_at DESC) 
    WHERE status = 'UNLOCKED';
```

### Таблица `achievement_categories`

```sql
CREATE TABLE achievement_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(100),
    display_order INTEGER DEFAULT 0
);
```

---

## Rarity Tiers

```java
public enum AchievementRarity {
    COMMON(10, "#CCCCCC"),      // 60% of achievements
    UNCOMMON(25, "#00FF00"),    // 25%
    RARE(50, "#0070DD"),        // 10%
    EPIC(100, "#A335EE"),       // 4%
    LEGENDARY(250, "#FF8000");  // 1%
    
    private final int points;
    private final String color;
}
```

---

## Conditions Format

```json
{
  "type": "kill_count",
  "target": {
    "enemy_type": "CORPO_SOLDIER",
    "count": 100
  }
}

{
  "type": "quest_complete",
  "target": {
    "quest_id": "MQ-001",
    "branch": "romance_path"
  }
}

{
  "type": "skill_level",
  "target": {
    "skill": "HACKING",
    "level": 20
  }
}

{
  "type": "collection",
  "target": {
    "item_category": "LEGENDARY_WEAPONS",
    "count": 10
  }
}

{
  "type": "composite",
  "operator": "AND",
  "conditions": [
    {"type": "level", "value": 50},
    {"type": "reputation", "faction": "ARASAKA", "value": "EXALTED"}
  ]
}
```

---

## Rewards

```json
{
  "title": "Netrunner Supreme",
  "cosmetics": [
    {"type": "PLAYER_ICON", "id": "icon_netrunner_master"},
    {"type": "NAME_PLATE", "id": "plate_legendary_hacker"}
  ],
  "perks": [
    {"perk_id": "PERK_ACHIEVEMENT_HACKER", "permanent": true}
  ],
  "currency": {
    "eddies": 10000,
    "premium_currency": 100
  },
  "items": [
    {"item_id": "ITEM_LEGENDARY_CYBERDECK", "quantity": 1}
  ]
}
```

---

## Achievement Unlock Flow

```java
@Service
public class AchievementService {
    
    public void trackProgress(UUID playerId, String eventType, Map<String, Object> data) {
        // 1. Найти все достижения для этого события
        List<Achievement> relevantAchievements = achievementRepository
            .findByEventType(eventType);
        
        // 2. Проверить каждое
        for (Achievement achievement : relevantAchievements) {
            checkAndUpdateProgress(playerId, achievement, data);
        }
    }
    
    private void checkAndUpdateProgress(UUID playerId, Achievement achievement, Map<String, Object> data) {
        // 1. Получить прогресс игрока
        PlayerAchievement progress = playerAchievementRepository
            .findByPlayerAndAchievement(playerId, achievement.getId())
            .orElseGet(() -> createNewProgress(playerId, achievement));
        
        // 2. Если уже unlock - skip
        if (progress.getStatus() == AchievementStatus.UNLOCKED) {
            return;
        }
        
        // 3. Проверить условия
        boolean conditionsMet = evaluateConditions(achievement.getConditions(), data);
        
        if (!conditionsMet) {
            return;
        }
        
        // 4. Обновить прогресс
        int newProgress = calculateNewProgress(progress, achievement, data);
        progress.setCurrentProgress(newProgress);
        progress.setLastProgressAt(Instant.now());
        
        // 5. Проверить unlock
        if (newProgress >= achievement.getMaxProgress()) {
            unlockAchievement(playerId, achievement, progress);
        } else {
            playerAchievementRepository.save(progress);
        }
    }
    
    private void unlockAchievement(UUID playerId, Achievement achievement, PlayerAchievement progress) {
        progress.setStatus(AchievementStatus.UNLOCKED);
        progress.setUnlockedAt(Instant.now());
        progress.setUnlockContext(captureUnlockContext());
        
        playerAchievementRepository.save(progress);
        
        // Grant rewards
        grantRewards(playerId, achievement.getRewards());
        
        // Notification
        notificationService.send(playerId, new AchievementUnlockedNotification(
            achievement.getName(),
            achievement.getDescription(),
            achievement.getRarity()
        ));
        
        // Event
        eventBus.publish(new AchievementUnlockedEvent(
            playerId,
            achievement.getId(),
            achievement.getCode()
        ));
        
        // Check meta achievements
        checkMetaAchievements(playerId, achievement.getCategory());
        
        // Analytics
        analyticsService.trackAchievementUnlock(playerId, achievement.getId());
        
        log.info("Achievement unlocked: player={}, achievement={}", 
            playerId, achievement.getCode());
    }
}
```

---

## Progress Tracking

### Event Listeners

```java
@Component
public class AchievementEventListener {
    
    @EventListener
    public void onEnemyKilled(EnemyKilledEvent event) {
        achievementService.trackProgress(
            event.getPlayerId(),
            "enemy_killed",
            Map.of(
                "enemy_type", event.getEnemyType(),
                "enemy_level", event.getEnemyLevel(),
                "weapon_used", event.getWeaponId()
            )
        );
    }
    
    @EventListener
    public void onQuestCompleted(QuestCompletedEvent event) {
        achievementService.trackProgress(
            event.getPlayerId(),
            "quest_completed",
            Map.of(
                "quest_id", event.getQuestId(),
                "quest_type", event.getQuestType(),
                "branch_taken", event.getBranchName()
            )
        );
    }
    
    @EventListener
    public void onItemCrafted(ItemCraftedEvent event) {
        achievementService.trackProgress(
            event.getPlayerId(),
            "item_crafted",
            Map.of(
                "item_id", event.getItemId(),
                "item_rarity", event.getItemRarity(),
                "craft_quality", event.getQuality()
            )
        );
    }
    
    @EventListener
    public void onSkillLevelUp(SkillLevelUpEvent event) {
        achievementService.trackProgress(
            event.getPlayerId(),
            "skill_level",
            Map.of(
                "skill", event.getSkillName(),
                "new_level", event.getNewLevel()
            )
        );
    }
}
```

**Детальные примеры, API endpoints и rewards:** См. [Achievement Examples & API](./achievement-examples-api.md)

---

## Связанные документы

- [Achievement Tracking](./achievement-tracking.md)
- [Achievement Examples & API](./achievement-examples-api.md)
- [Leaderboard System](../leaderboard/leaderboard-core.md)
- [Notification System](../notification-system.md)
