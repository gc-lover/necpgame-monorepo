---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:59
**api-readiness-notes:** Achievement System Examples & API. Примеры достижений, API endpoints, rewards. ~350 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 03:05
---

# Achievement System - Examples & API

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:59  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Achievement examples & API endpoints  
**Размер:** ~350 строк ✅

**Связанные микрофичи:**
- [Achievement Core](./achievement-core.md)
- [Achievement Tracking](./achievement-tracking.md)

---

## Achievement Examples

### Combat Category

```json
{
  "code": "ACH_KILLER_TIER1",
  "name": "Killer I",
  "description": "Kill 100 enemies",
  "category": "COMBAT",
  "type": "PROGRESSIVE",
  "rarity": "COMMON",
  "conditions": {
    "type": "kill_count",
    "target": {"count": 100}
  },
  "max_progress": 100,
  "rewards": {
    "title": "Killer",
    "points": 10
  }
}

{
  "code": "ACH_HEADHUNTER",
  "name": "Headhunter",
  "description": "Get 500 headshot kills",
  "category": "COMBAT",
  "type": "PROGRESSIVE",
  "rarity": "RARE",
  "conditions": {
    "type": "kill_count",
    "target": {
      "count": 500,
      "kill_type": "HEADSHOT"
    }
  },
  "max_progress": 500,
  "rewards": {
    "title": "Headhunter",
    "perk": "PERK_HEADSHOT_DAMAGE_5",
    "points": 50
  }
}
```

### Quest Category

```json
{
  "code": "ACH_STORY_COMPLETE",
  "name": "The End of the Road",
  "description": "Complete the main story (2020-2093)",
  "category": "QUEST",
  "type": "ONE_TIME",
  "rarity": "EPIC",
  "conditions": {
    "type": "quest_complete",
    "target": {"quest_id": "MQ_FINALE"}
  },
  "rewards": {
    "title": "Legend of Night City",
    "cosmetic": "PLAYER_ICON_LEGEND",
    "points": 100
  }
}
```

### Social Category

```json
{
  "code": "ACH_ROMANCE_MASTER",
  "name": "Casanova",
  "description": "Complete romance with 5 different NPCs",
  "category": "SOCIAL",
  "type": "PROGRESSIVE",
  "rarity": "LEGENDARY",
  "conditions": {
    "type": "romance_complete",
    "target": {"count": 5, "unique": true}
  },
  "max_progress": 5,
  "rewards": {
    "title": "Casanova of Night City",
    "perk": "PERK_ROMANCE_BONUS",
    "points": 250
  }
}
```

### Economy Category

```json
{
  "code": "ACH_MILLIONAIRE",
  "name": "Millionaire",
  "description": "Accumulate 1,000,000 eddies",
  "category": "ECONOMY",
  "type": "PROGRESSIVE",
  "rarity": "EPIC",
  "conditions": {
    "type": "currency_total",
    "target": {"currency": "EDDIES", "amount": 1000000}
  },
  "max_progress": 1000000,
  "rewards": {
    "title": "Millionaire",
    "cosmetic": "NAME_PLATE_GOLD",
    "points": 100
  }
}
```

### Exploration Category

```json
{
  "code": "ACH_NIGHT_CITY_EXPLORER",
  "name": "Night City Explorer",
  "description": "Visit all 100 POIs in Night City",
  "category": "EXPLORATION",
  "type": "PROGRESSIVE",
  "rarity": "RARE",
  "conditions": {
    "type": "locations_visited",
    "target": {
      "region": "night_city",
      "poi_count": 100
    }
  },
  "max_progress": 100,
  "rewards": {
    "title": "Explorer of Night City",
    "points": 50
  }
}
```

### Skills Category

```json
{
  "code": "ACH_MASTER_HACKER",
  "name": "Master Hacker",
  "description": "Reach Hacking skill level 20",
  "category": "SKILLS",
  "type": "ONE_TIME",
  "rarity": "EPIC",
  "conditions": {
    "type": "skill_level",
    "target": {
      "skill": "HACKING",
      "level": 20
    }
  },
  "rewards": {
    "title": "Master Hacker",
    "perk": "PERK_HACKING_MASTERY",
    "points": 100
  }
}
```

### Hidden Achievements

```json
{
  "code": "ACH_EASTER_EGG_001",
  "name": "???",
  "description": "???",
  "category": "SPECIAL",
  "type": "ONE_TIME",
  "rarity": "LEGENDARY",
  "is_hidden": true,
  "conditions": {
    "type": "location_visited",
    "target": {
      "location_id": "SECRET_ROOM_001"
    }
  },
  "rewards": {
    "title": "Easter Egg Hunter",
    "cosmetic": "ICON_SECRET_HUNTER",
    "points": 250
  }
}
```

---

## API Endpoints

### GET `/api/v1/achievements`

Список всех достижений (для UI)

**Response:**
```json
{
  "achievements": [
    {
      "id": "uuid",
      "code": "ACH_KILLER_TIER1",
      "name": "Killer I",
      "description": "Kill 100 enemies",
      "category": "COMBAT",
      "subcategory": "kills",
      "rarity": "COMMON",
      "points": 10,
      "isHidden": false,
      "isSeasonal": false
    },
    {
      "id": "uuid",
      "code": "ACH_ROMANCE_MASTER",
      "name": "Casanova",
      "description": "Complete romance with 5 different NPCs",
      "category": "SOCIAL",
      "rarity": "LEGENDARY",
      "points": 250,
      "isHidden": false
    }
  ],
  "categories": [
    {"code": "COMBAT", "name": "Combat", "count": 50},
    {"code": "QUEST", "name": "Quests", "count": 100},
    {"code": "SOCIAL", "name": "Social", "count": 40},
    {"code": "ECONOMY", "name": "Economy", "count": 50},
    {"code": "EXPLORATION", "name": "Exploration", "count": 60},
    {"code": "SKILLS", "name": "Skills", "count": 40},
    {"code": "COLLECTIONS", "name": "Collections", "count": 30},
    {"code": "SPECIAL", "name": "Special", "count": 30}
  ],
  "totalCount": 400
}
```

---

### GET `/api/v1/players/{playerId}/achievements`

Прогресс игрока по всем достижениям

**Response:**
```json
{
  "playerId": "uuid",
  "totalAchievements": 500,
  "unlockedCount": 125,
  "totalPoints": 2450,
  "completionPercentage": 25.0,
  
  "achievements": [
    {
      "achievementId": "uuid",
      "code": "ACH_KILLER_TIER1",
      "name": "Killer I",
      "description": "Kill 100 enemies",
      "category": "COMBAT",
      "rarity": "COMMON",
      "status": "UNLOCKED",
      "progress": 100,
      "maxProgress": 100,
      "progressPercentage": 100,
      "unlockedAt": "2025-11-06T20:00:00Z"
    },
    {
      "achievementId": "uuid",
      "code": "ACH_KILLER_TIER2",
      "name": "Killer II",
      "description": "Kill 500 enemies",
      "category": "COMBAT",
      "rarity": "UNCOMMON",
      "status": "IN_PROGRESS",
      "progress": 245,
      "maxProgress": 500,
      "progressPercentage": 49.0,
      "firstProgressAt": "2025-11-05T10:00:00Z",
      "lastProgressAt": "2025-11-07T01:30:00Z"
    }
  ],
  
  "recentUnlocks": [
    {
      "achievementId": "uuid",
      "code": "ACH_QUEST_100",
      "name": "Quest Master",
      "unlockedAt": "2025-11-07T01:00:00Z",
      "rarity": "RARE"
    }
  ],
  
  "nearCompletion": [
    {
      "achievementId": "uuid",
      "code": "ACH_MILLIONAIRE",
      "name": "Millionaire",
      "progress": 950000,
      "maxProgress": 1000000,
      "progressPercentage": 95.0
    }
  ]
}
```

---

### GET `/api/v1/achievements/leaderboard`

Таблица лидеров по очкам достижений

**Query Parameters:**
- `period` - all_time | monthly | weekly (default: all_time)
- `limit` - количество игроков (default: 100)

**Response:**
```json
{
  "period": "all_time",
  "lastUpdated": "2025-11-07T01:59:00Z",
  
  "players": [
    {
      "rank": 1,
      "playerId": "uuid",
      "playerName": "V",
      "playerLevel": 50,
      "totalPoints": 15000,
      "achievementsUnlocked": 487,
      "completionPercentage": 97.4
    },
    {
      "rank": 2,
      "playerId": "uuid",
      "playerName": "Johnny",
      "playerLevel": 48,
      "totalPoints": 14200,
      "achievementsUnlocked": 465,
      "completionPercentage": 93.0
    }
  ],
  
  "totalPlayers": 50000
}
```

---

### GET `/api/v1/achievements/{achievementId}/stats`

Статистика по достижению (сколько игроков получили)

**Response:**
```json
{
  "achievementId": "uuid",
  "code": "ACH_KILLER_TIER1",
  "name": "Killer I",
  "rarity": "COMMON",
  
  "stats": {
    "totalPlayers": 50000,
    "playersUnlocked": 35000,
    "unlockPercentage": 70.0,
    "averageTimeToUnlock": "2h 30m",
    "firstUnlockedBy": {
      "playerId": "uuid",
      "playerName": "SpeedRunner",
      "unlockedAt": "2025-10-01T12:00:00Z"
    }
  }
}
```

---

## Rewards System

### Title Rewards

```java
@Service
public class TitleRewardService {
    
    public void grantTitle(UUID playerId, String titleCode) {
        PlayerTitle title = new PlayerTitle();
        title.setPlayerId(playerId);
        title.setTitleCode(titleCode);
        title.setUnlockedAt(Instant.now());
        title.setSource("ACHIEVEMENT");
        
        titleRepository.save(title);
        
        // Notify player
        notificationService.send(playerId, 
            new TitleUnlockedNotification(titleCode));
        
        log.info("Title granted: playerId={}, title={}", playerId, titleCode);
    }
}
```

### Perk Rewards

```java
@Service
public class PerkRewardService {
    
    public void grantPerk(UUID playerId, String perkId) {
        // Permanent passive bonus
        PlayerPerk perk = new PlayerPerk();
        perk.setPlayerId(playerId);
        perk.setPerkId(perkId);
        perk.setSource("ACHIEVEMENT");
        perk.setPermanent(true);
        
        perkRepository.save(perk);
        
        // Recalculate stats
        playerStatsService.recalculate(playerId);
        
        log.info("Perk granted: playerId={}, perk={}", playerId, perkId);
    }
}
```

### Currency Rewards

```java
public void grantCurrency(UUID playerId, Map<String, Long> currency) {
    Player player = playerRepository.findById(playerId);
    
    for (Map.Entry<String, Long> entry : currency.entrySet()) {
        String currencyType = entry.getKey();
        Long amount = entry.getValue();
        
        if (currencyType.equals("eddies")) {
            player.setEddies(player.getEddies() + amount);
        } else if (currencyType.equals("premium_currency")) {
            player.setPremiumCurrency(player.getPremiumCurrency() + amount);
        }
    }
    
    playerRepository.save(player);
}
```

### Item Rewards

```java
public void grantItems(UUID playerId, List<ItemReward> items) {
    for (ItemReward reward : items) {
        inventoryService.addItem(
            playerId,
            reward.getItemId(),
            reward.getQuantity()
        );
    }
}
```

---

## Связанные документы

- [Achievement Core](./achievement-core.md)
- [Achievement Tracking](./achievement-tracking.md)
- [Leaderboard System](../leaderboard/leaderboard-core.md)
