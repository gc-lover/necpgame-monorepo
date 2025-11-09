---

- **Status:** queued
- **Last Updated:** 2025-11-08 18:10
---


# Leaderboard System - Система рейтингов

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** средний (Engagement)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Система глобальных рейтингов. Global/seasonal/friend/guild leaderboards, Redis sorted sets, real-time updates, multiple categories. Готов к API!

---

## Краткое описание

Система глобальных рейтингов и leaderboards для NECPGAME.

**Микрофича:** Leaderboards (global, seasonal, friend, guild)

---

## Микросервисная архитектура

**Ответственный микросервис:** world-service  
**Порт:** 8086  
**API Gateway маршрут:** `/api/v1/world/leaderboards/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- character-service: получение данных для rankings
- social-service: friend/guild leaderboards

**Event Bus события:**
- Подписывается: `character:level-up`, `pvp:match-won`, `raid:boss-killed`, `achievement:unlocked`
- Публикует: `leaderboard:updated`, `leaderboard:rank-changed`

**Кэширование:** Redis Sorted Sets для быстрого доступа к rankings

---

## 🏆 Типы Leaderboards

### 1. Global Leaderboards

**Level Leaderboard:**
```
Top Players by Level:

#1. ProGamer_2077 - Level 50 (Max!)
#2. NightCityLegend - Level 49
#3. CyberPunk_Elite - Level 48
...
#100. Player_Name - Level 42

Your rank: #523 (Level 38)
```

**Wealth Leaderboard:**
```
Top Players by Wealth:

#1. TradingKing - 10,523,456 eddies
#2. InvestorPro - 8,234,567 eddies
#3. MerchantLord - 6,789,012 eddies
...

Your rank: #1,234 (450,000 eddies)
```

**PvP Rating Leaderboard:**
```
Top PvP Players:

#1. SharpShooter - 2,456 MMR (Immortal)
#2. HeadHunter - 2,389 MMR (Immortal)
#3. QuickScope - 2,301 MMR (Radiant)
...

Your rank: #5,678 (1,234 MMR - Diamond)
```

**Achievement Points:**
```
Top Achievement Hunters:

#1. Completionist - 9,850 points (98% complete)
#2. ChievoHunter - 9,234 points (92%)
#3. Collector - 8,567 points (86%)
...

Your rank: #2,345 (3,456 points - 35%)
```

### 2. Class Leaderboards

```
Top Solos:
#1. Solo_King - Level 50, 2,345 MMR
...

Top Netrunners:
#1. HackMaster - Level 50, 2,198 MMR
...

По каждому классу отдельно
```

### 3. Seasonal Leaderboards

```
Season 3 - "Corporate Wars" (Nov 1 - Dec 31)

Top Players this season:

#1. SeasonKing - 15,234 season points
#2. LeagueLegend - 14,567 season points
...

Your rank: #892 (5,678 season points)

Season rewards:
Top 10: Legendary skin + 10k premium currency
Top 100: Epic skin + 5k premium currency
Top 1000: Rare skin + 1k premium currency
```

### 4. Friend Leaderboards

```
Compare with Friends:

Your Level: 38
Friends:
#1. BestFriend - Level 42 (you're #4 behind)
#2. OldPal - Level 40
#3. Choom - Level 39
#4. YOU - Level 38
#5. NewFriend - Level 35

Friendly competition!
```

### 5. Guild Leaderboards

```
Top Guilds:

#1. NightCityElite - 125,456 guild points
   Members: 50 | Avg Level: 47
   
#2. CyberLegends - 98,234 guild points
   Members: 48 | Avg Level: 45
...

Your guild: #45 (12,345 points)
```

---

## 📊 Ranking Metrics

### What to rank

**Player metrics:**
- Level
- Wealth (total eddies)
- PvP rating (MMR)
- Achievement points
- Quest completion count
- Kill count
- Death count (lowest = best)
- K/D ratio
- Win rate (PvP)

**Guild metrics:**
- Total guild points
- Average member level
- Territory controlled
- Guild wars won
- Total wealth

---

## 🔄 Update Frequency

**Real-time:**
- PvP rating (after each match)
- Kill count (after each kill)

**Hourly:**
- Level leaderboard
- Wealth leaderboard

**Daily:**
- Achievement points
- Quest completion

**Weekly:**
- Seasonal leaderboards
- Guild leaderboards

---

## 🗄️ Структура БД

### Leaderboards

```sql
CREATE TABLE leaderboards (
    id SERIAL PRIMARY KEY,
    leaderboard_type VARCHAR(50) NOT NULL, -- "LEVEL", "WEALTH", "PVP", "ACHIEVEMENTS"
    
    scope VARCHAR(20) NOT NULL, -- "GLOBAL", "REGION", "GUILD", "FRIENDS"
    
    season_id INTEGER, -- Для seasonal leaderboards
    
    -- Caching
    last_calculated_at TIMESTAMP NOT NULL,
    cache_ttl INTEGER DEFAULT 3600, -- seconds
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Leaderboard Entries (Materialized View)

```sql
CREATE MATERIALIZED VIEW leaderboard_global_level AS
SELECT 
    player_id,
    character_name,
    level,
    experience,
    class,
    ROW_NUMBER() OVER (ORDER BY level DESC, experience DESC) as rank
FROM characters
WHERE is_deleted = FALSE
ORDER BY level DESC, experience DESC
LIMIT 10000;

-- Refresh каждый час
REFRESH MATERIALIZED VIEW CONCURRENTLY leaderboard_global_level;
```

### Seasonal Leaderboards

```sql
CREATE TABLE seasonal_leaderboards (
    season_id INTEGER NOT NULL,
    player_id UUID NOT NULL,
    
    season_points INTEGER DEFAULT 0,
    matches_played INTEGER DEFAULT 0,
    wins INTEGER DEFAULT 0,
    
    rank INTEGER,
    
    PRIMARY KEY (season_id, player_id)
);

CREATE INDEX idx_seasonal_rank ON seasonal_leaderboards(season_id, season_points DESC);
```

---

## 🔗 API Endpoints

```
GET /leaderboards/global/{type}      - Global leaderboard
GET /leaderboards/seasonal/{seasonId} - Seasonal leaderboard
GET /leaderboards/guild/{guildId}     - Guild members ranking
GET /leaderboards/friends/{playerId}  - Friends comparison

GET /leaderboards/player/{playerId}/rank - Player's rank
GET /leaderboards/nearby/{playerId}      - Players near your rank
```

---

## 🎯 Примеры

### Пример 1: Global Level Leaderboard

```
Player reaches level 50:
→ Rank recalculated: #42 globally
→ Notification: "You're #42 in global level leaderboard!"
```

### Пример 2: Seasonal Points

```
Season 3:
Player plays 100 matches, wins 60
Season points: 60 × 100 = 6,000
Rank: #234

Season ends:
Top 1000 reward: Rare skin ✅
```

---

## 🔗 Связанные документы

- `achievement-system.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание системы рейтингов
