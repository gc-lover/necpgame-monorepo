---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:35
**api-readiness-notes:** Server Selection UI. Выбор сервера, статус серверов. ~100 строк.
---

# Server Selection - Выбор сервера

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:35  
**Приоритет:** средний  
**Автор:** AI Brain Manager

**Микрофича:** Server selection  
**Размер:** ~100 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## UI

```
┌──────────────────────────────────────────────────────┐
│ SELECT SERVER                                        │
├──────────────────────────────────────────────────────┤
│ 🟢 Server 01 (US-East) - Online: 2,543 - [SELECT]   │
│ 🟢 Server 02 (EU-West) - Online: 1,892 - [SELECT]   │
│ 🟡 Server 03 (Asia) - Online: 4,123 - [SELECT]      │
└──────────────────────────────────────────────────────┘
```

**API:** GET /api/v1/servers

---

## Связанные документы

- `.BRAIN/05-technical/ui/game-start/login-screen.md` - Login (микрофича 1/3)
- `.BRAIN/05-technical/ui/game-start/character-select.md` - Characters (микрофича 3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:35)** - Микрофича 2/3 (split from ui-game-start.md)
