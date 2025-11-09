---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:35
**api-readiness-notes:** Character Select UI. Выбор персонажа, создание нового. ~120 строк.
---

# Character Select - Выбор персонажа

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:35  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Character selection  
**Размер:** ~120 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## UI

```
┌──────────────────────────────────────────────────────┐
│ SELECT CHARACTER                    [CREATE NEW +]   │
├──────────────────────────────────────────────────────┤
│ ┌────────────────────────────────────────────────┐  │
│ │ Viktor "V" Chen                                │  │
│ │ Level 15 Solo - Night City                     │  │
│ │ Last played: 2 hours ago                       │  │
│ │ [SELECT] [DELETE]                              │  │
│ └────────────────────────────────────────────────┘  │
│                                                       │
│ ┌────────────────────────────────────────────────┐  │
│ │ Empty Slot 2/5                                 │  │
│ │ [CREATE CHARACTER]                             │  │
│ └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

**API:** GET /api/v1/characters

---

## Связанные документы

- `.BRAIN/05-technical/ui/game-start/login-screen.md` - Login (микрофича 1/3)
- `.BRAIN/05-technical/ui/game-start/server-selection.md` - Server (микрофича 2/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:35)** - Микрофича 3/3 (split from ui-game-start.md)
