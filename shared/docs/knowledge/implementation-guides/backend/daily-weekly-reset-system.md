---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Daily/Weekly Reset System - Система сбросов

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** средний (Engagement)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Система автоматических сбросов. Daily/weekly resets для квестов, rewards, limits, bonuses. Scheduled jobs (cron), reset logic. Готов к API!

---

## Краткое описание

Система автоматических сбросов для ежедневных и еженедельных активностей.

**Микрофича:** Daily/Weekly resets (quests, rewards, limits, bonuses)

---

## Микросервисная архитектура

**Ответственный микросервис:** world-service  
**Порт:** 8086  
**API Gateway маршрут:** `/api/v1/world/system/reset`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- gameplay-service: reset daily quests
- economy-service: reset trading limits
- social-service: reset daily friend invites

**Event Bus события:**
- Публикует: `system:daily-reset`, `system:weekly-reset`, `system:monthly-reset`
- Все сервисы подписываются на эти события для своих reset логик

**Паттерн:** Scheduled job в world-service, который публикует события для всех сервисов

---

## 🔄 Концепция

**Reset** — автоматический сброс прогресса/лимитов для повторяющихся активностей.

**Цели:**
1. **Retention** - игроки возвращаются каждый день
2. **Routine** - создать игровую рутину
3. **Limits** - ограничить фарм
4. **Rewards** - регулярные награды

---

## 📅 Типы сбросов

### Daily Reset (Ежедневный)

**Время:** 00:00 server time (каждый день)

**Что сбрасывается:**
```
✅ Daily quests (новые квесты)
✅ Daily quest slots (5 слотов обновляются)
✅ Daily login rewards
✅ Daily limits (auction posts, etc.)
✅ Daily bonuses (first win of the day)
✅ Vendor inventory (NPC магазины)
✅ Daily instance resets (dungeons)
```

**Примеры:**
```
Daily Quest: "Kill 50 enemies"
Reward: 1,000 eddies + 500 XP
Reset: Tomorrow at 00:00

Daily Login Bonus: Day 5/7
Reward today: 500 eddies
Tomorrow: 1,000 eddies (Day 6)
```

### Weekly Reset (Еженедельный)

**Время:** Monday 00:00 server time

**Что сбрасывается:**
```
✅ Weekly quests (новые квесты)
✅ Raid lockouts (можно снова пройти рейды)
✅ Weekly rewards (bonus chests)
✅ Guild quest progress
✅ Seasonal points reset (если конец сезона)
```

**Примеры:**
```
Weekly Quest: "Complete 10 raids"
Reward: 10,000 eddies + Epic item
Reset: Every Monday

Raid Lockout:
"Blackwall Expedition" - completed this week
Cannot enter again until Monday
```

---

## 📊 Daily Quest System

### Quest Pool

**Категории:**
```
Combat: "Kill 50 enemies"
Economic: "Sell 10 items on auction"
Social: "Party with 3 friends"
Exploration: "Discover 5 new locations"
Crafting: "Craft 3 items"
```

**Rotation:**
```
Player gets 5 random daily quests from pool
Each category: 1-2 quests max
Pool size: 50+ different quests
Rotation: Never same 5 quests twice in a row
```

### Rewards

```
Daily quest reward:
- Base: 500-1,500 eddies
- XP: 250-750
- Items: Common-Rare (random)

Complete all 5 daily quests:
Bonus: +2,000 eddies + Rare item guaranteed
```

---

## 📅 Weekly Quest System

### Quest Types

**Solo weekly:**
```
"Raid Runner" - Complete 5 raids
Reward: 5,000 eddies + Epic item

"PvP Warrior" - Win 20 PvP matches
Reward: 3,000 eddies + Ranked points boost
```

**Guild weekly:**
```
"Guild Territory" - Capture 3 territories
Reward: 10,000 guild points

"Guild Economy" - Guild earns 1M eddies
Reward: Guild bank +100k eddies
```

---

## 🎁 Login Rewards

### Daily Login Calendar

```
Day 1: 100 eddies
Day 2: 200 eddies
Day 3: 300 eddies + Common item
Day 4: 400 eddies
Day 5: 500 eddies + Uncommon item
Day 6: 1,000 eddies
Day 7: 2,000 eddies + Rare item + 50 premium currency

Reset: После пропуска дня → Start from Day 1
```

### Monthly Login

```
Login 7 days/month: Uncommon item
Login 15 days/month: Rare item
Login 25 days/month: Epic item + 100 premium
Login 30 days/month: Legendary item! + 500 premium
```

---

## ⏰ Timing и Time Zones

### Server Time

**Base:** UTC (server time)

**Player perspective:**
```
Server time: 00:00 UTC = Reset

Player in PST (UTC-8):
Reset: 16:00 (4 PM) previous day

Player in JST (UTC+9):
Reset: 09:00 (9 AM) same day

All players: Same reset time in different local times
```

### Countdown Timer

```
UI показывает:
"Daily reset in: 5h 23m 45s"
"Weekly reset in: 2d 5h 23m"

Visual: Progress bar до reset
```

---

## 🗄️ Структура БД

### Daily Quests

```sql
CREATE TABLE daily_quests_pool (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    
    requirements JSONB NOT NULL,
    rewards JSONB NOT NULL,
    
    weight INTEGER DEFAULT 1, -- Для rotation
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Player Daily Quests

```sql
CREATE TABLE player_daily_quests (
    player_id UUID NOT NULL,
    quest_id VARCHAR(100) NOT NULL,
    
    assigned_date DATE NOT NULL,
    
    progress INTEGER DEFAULT 0,
    required INTEGER NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    
    reward_claimed BOOLEAN DEFAULT FALSE,
    
    PRIMARY KEY (player_id, assigned_date, quest_id)
);

CREATE INDEX idx_daily_quests_player ON player_daily_quests(player_id, assigned_date);
```

### Reset Tracking

```sql
CREATE TABLE reset_tracking (
    player_id UUID NOT NULL,
    reset_type VARCHAR(20) NOT NULL, -- "DAILY", "WEEKLY"
    
    last_reset_at TIMESTAMP NOT NULL,
    next_reset_at TIMESTAMP NOT NULL,
    
    PRIMARY KEY (player_id, reset_type)
);
```

### Login Rewards

```sql
CREATE TABLE login_rewards_tracking (
    player_id UUID PRIMARY KEY,
    
    consecutive_days INTEGER DEFAULT 0,
    last_login_date DATE,
    
    monthly_logins INTEGER DEFAULT 0,
    current_month VARCHAR(7), -- "2025-11"
    
    total_logins INTEGER DEFAULT 0,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🔧 Reset Process

### Daily Reset Job

```
Cron: 0 0 * * * (every day at 00:00 UTC)

Actions:
1. Clear player_daily_quests (yesterday's quests)
2. Assign new daily quests (5 random per player)
3. Reset daily limits (auction posts, etc.)
4. Refresh vendor inventory
5. Reset daily instance lockouts
6. Process login rewards (check consecutive days)
7. Send notification: "Daily reset complete! New quests available"
```

### Weekly Reset Job

```
Cron: 0 0 * * 1 (every Monday at 00:00 UTC)

Actions:
1. Clear player_weekly_quests
2. Assign new weekly quests
3. Reset raid lockouts
4. Calculate weekly leaderboard rewards
5. Reset guild weekly progress
6. Check season end (if applicable)
7. Send notification: "Weekly reset! Raids unlocked!"
```

---

## 🔔 Notifications

**Daily reset:**
```
"🌅 Daily Reset Complete!"
- 5 new daily quests available
- Daily login bonus: Day 3
- Vendor inventory refreshed
```

**Weekly reset:**
```
"📅 Weekly Reset!"
- Raid lockouts cleared
- New weekly quests
- Guild quests reset
```

---

## 🔗 API Endpoints

```
GET /daily-quests/available        - Today's daily quests
POST /daily-quests/{id}/complete   - Complete daily quest
GET /daily-quests/progress         - Daily progress

GET /weekly-quests/available       - This week's quests
GET /login-rewards/status          - Login streak status

GET /reset/next                    - Time until next reset
```

---

## 🎯 Примеры

### Daily Quest Flow

```
00:00 - Reset происходит
Player logs in at 08:00:
→ 5 new daily quests assigned
→ Notification shown

Player completes 3/5 quests:
→ Rewards claimed: 3,000 eddies
→ Bonus progress: 3/5

23:59 - Player completes 4th quest
→ Reward claimed
→ Progress: 4/5

00:00 - Next day reset:
→ Old quest (5th) removed (uncompleted)
→ 5 NEW quests assigned
```

---

## 🔗 Связанные документы

- `achievement-system.md`
- `leaderboard-system.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание системы сбросов
