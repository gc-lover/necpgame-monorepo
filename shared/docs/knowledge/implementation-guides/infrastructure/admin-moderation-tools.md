---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Admin & Moderation Tools - Инструменты администрирования

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 05:20  
**Приоритет:** критический (Production)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Admin панель и moderation tools. Player management (ban/mute/kick), content moderation, world management, analytics dashboard, audit logs. Production critical!

---

## Краткое описание

Инструменты для администраторов и модераторов.

**Микрофича:** Admin tools (player management, moderation, analytics)

---

## 👑 Роли

### Super Admin
- Полный доступ ко всему
- Database access
- Server management

### Admin
- Player management (ban, unban, edit)
- Economy management (add/remove items, eddies)
- Quest management (reset, complete)

### Moderator
- Chat moderation
- Player reports review
- Temporary bans (up to 7 days)

---

## 🛠️ Admin Panel Features

### Player Management

```
PLAYER TOOLS:
- View player profile (all data)
- Edit player data (level, eddies, items)
- Ban/Unban player
- Delete account
- View login history
- View trade history
- View transaction history
```

### Economy Management

```
ECONOMY TOOLS:
- Add/Remove eddies
- Add/Remove items
- View market activity
- Suspicious transaction alerts
- RMT detection reports
```

### Content Management

```
CONTENT TOOLS:
- Create/Edit achievements
- Create/Edit daily quests
- Manage global events
- Edit NPC dialogues (hot-fix)
```

---

## 📊 Analytics Dashboard

```
REAL-TIME STATS:
- Players online: 12,458
- Active matches: 2,345
- Trades/hour: 567
- Chat messages/min: 8,234
- Server load: 67%

ALERTS:
⚠️ Unusual trading activity detected (Player X)
⚠️ Chat spam in Global channel
⚠️ Server lag spike (Zone: Watson)
```

---

## 🗄️ Структура БД

```sql
CREATE TABLE admin_actions_log (
    id BIGSERIAL PRIMARY KEY,
    admin_id UUID NOT NULL,
    
    action_type VARCHAR(50) NOT NULL,
    target_player_id UUID,
    
    action_details JSONB NOT NULL,
    reason TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🔗 Связанные документы

- `anti-cheat-system.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание admin tools
