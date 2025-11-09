---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Anti-Cheat System - Система защиты от читов

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 05:20  
**Приоритет:** критический (Production)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Многоуровневая защита от читов. Client validation, server reconciliation, pattern detection (speedhack, teleport, damage hack), auto-ban, manual review queue. Готов к API!

---

## Краткое описание

Многоуровневая система защиты от читов и эксплойтов.

**Микрофича:** Anti-cheat (detection, prevention, ban system)

---

## 🛡️ Уровни защиты

### Level 1: Client-Side Validation

**Что проверяется:**
- Input validation (движение, действия)
- Rate limiting (actions per second)
- Boundary checks (не выходить за карту)

**Защита от:**
- Speed hacks (слишком быстрое движение)
- Teleport hacks
- Rapid fire exploits

### Level 2: Server-Side Validation

**Что проверяется:**
- All client actions
- Physics validation (возможно ли действие?)
- Cooldown enforcement (нельзя использовать ability 2 раза в секунду)
- Resource checks (есть ли ammo, health)

**Защита от:**
- Infinite ammo
- No cooldown hacks
- Impossible actions

### Level 3: Behavioral Analysis

**Что отслеживается:**
- Kill patterns (слишком много headshots = aim bot?)
- Movement patterns (нечеловеческая точность)
- Reaction time (< 50ms = подозрительно)
- Win rate (99% = подозрительно)

**Machine Learning:**
```
Train model on:
- Legitimate players (baseline)
- Confirmed cheaters (known patterns)

Detect anomalies:
- Player behavior deviates significantly → Flag for review
```

### Level 4: System Integrity

**Что проверяется:**
- Memory scanning (client-side check)
- Process list (cheat programs running?)
- DLL injection detection
- File integrity (game files modified?)

---

## 🚨 Detection Methods

### 1. Impossible Actions

```
Player kills enemy at 500m with pistol:
→ Pistol max range: 50m
→ IMPOSSIBLE → Auto-flag

Player deals 10,000 damage (one-shot boss):
→ Max player damage: 500
→ IMPOSSIBLE → Auto-ban
```

### 2. Statistical Anomalies

```
Player headshot rate: 95%
Average player: 15%
Pro player: 35%

→ 95% is anomaly → Flag for review
```

### 3. Pattern Recognition

```
Player movement pattern:
Frame 1: (0, 0)
Frame 2: (100, 100) ← 100m in 16ms?
Frame 3: (0, 0) ← Teleported back?

→ Movement hack detected → Auto-ban
```

---

## 🔨 Ban System

### Ban Levels

**Warning (1st offense):**
```
Minor offense (e.g., macro use)
→ Warning message
→ No ban, but tracked
→ Points: +1 violation
```

**Temporary Ban:**
```
Medium offense (e.g., speed hack)
→ Ban: 7 days
→ Points: +3 violations
→ Message: "You violated ToS. Banned 7 days."
```

**Permanent Ban:**
```
Major offense (e.g., aimbot, damage hack)
→ Ban: Permanent
→ Points: +10 violations (auto-permanent)
→ Appeal: Possible (manual review)
```

### Hardware Ban

```
Extreme cases:
→ Ban hardware ID (motherboard, MAC address)
→ Cannot create new account on same PC
→ For repeat offenders
```

---

## 🗄️ Структура БД

### Anti-Cheat Logs

```sql
CREATE TABLE anticheat_logs (
    id BIGSERIAL PRIMARY KEY,
    player_id UUID NOT NULL,
    
    violation_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL, -- "LOW", "MEDIUM", "HIGH", "CRITICAL"
    
    details JSONB NOT NULL,
    evidence JSONB, -- Game state, replay data
    
    auto_action VARCHAR(20), -- "NONE", "FLAG", "KICK", "BAN"
    
    reviewed BOOLEAN DEFAULT FALSE,
    reviewer_id UUID,
    review_decision VARCHAR(20),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_anticheat_player ON anticheat_logs(player_id, created_at DESC);
CREATE INDEX idx_anticheat_unreviewed ON anticheat_logs(reviewed) WHERE reviewed = FALSE;
```

### Player Ban Records

```sql
CREATE TABLE player_bans (
    id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    
    ban_type VARCHAR(20) NOT NULL, -- "TEMP", "PERMANENT", "HARDWARE"
    reason TEXT NOT NULL,
    evidence JSONB,
    
    banned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    banned_until TIMESTAMP, -- NULL for permanent
    banned_by VARCHAR(20) DEFAULT 'SYSTEM', -- "SYSTEM" or admin ID
    
    is_active BOOLEAN DEFAULT TRUE,
    
    appeal_submitted BOOLEAN DEFAULT FALSE,
    appeal_text TEXT,
    appeal_decision VARCHAR(20),
    
    CONSTRAINT fk_ban_player FOREIGN KEY (player_id) REFERENCES players(id)
);

CREATE INDEX idx_bans_active ON player_bans(player_id, is_active) WHERE is_active = TRUE;
```

---

## 📊 Detection Examples

**Aimbot:**
```
Tracking:
- Headshot rate: 95% (avg: 15%)
- Flick speed: Inhuman
- Tracking smoothness: Too perfect

Score: 98/100 (CRITICAL)
Action: Auto-ban + manual review
```

**Speed Hack:**
```
Movement log:
Position change: 100m in 0.1s
Max speed: 10m/s
Expected time: 10s

Violation: 100x too fast
Action: Auto-kick + flag
```

**Damage Hack:**
```
Damage dealt: 10,000
Weapon: Pistol (max damage 200)

Violation: 50x too high
Action: Auto-ban (no review needed - obvious)
```

---

## 🔗 API Endpoints

```
POST /anticheat/report              - Report player
GET  /anticheat/status/{playerId}   - Ban status
POST /anticheat/appeal              - Submit appeal
```

---

## 🔗 Связанные документы

- `admin-moderation-tools.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание anti-cheat системы
