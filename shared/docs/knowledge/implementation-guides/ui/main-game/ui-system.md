---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:20
**api-readiness-notes:** UI Main Game System. Торговля, настройки, технические требования, адаптивность. ~200 строк.
---

# UI Main Game System - Системные компоненты

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:20  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Trading, Settings, Technical  
**Размер:** ~200 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Торговля UI

```
┌──────────────────────────────────────────────────────┐
│ MARKET                  [Player Market] [Auction]    │
├──────────────────────────────────────────────────────┤
│ Search: [cyberdeck]  [🔍]     Sort: [Price ▼]       │
│                                                       │
│ ┌────────────────────────────────────────────────┐  │
│ │ [Icon] Legendary Cyberdeck                     │  │
│ │        Seller: NetMaster ⭐⭐⭐⭐⭐                │  │
│ │        Price: 25,000 ed                         │  │
│ │        [Buy Now] [Contact]                      │  │
│ └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

---

## Настройки

```
┌──────────────────────────────────────────────────────┐
│ SETTINGS                                             │
├──────────────────────────────────────────────────────┤
│ [Graphics] [Sound] [Gameplay] [Controls] [Account]  │
│                                                       │
│ GAMEPLAY SETTINGS:                                   │
│ ☑️ Auto-loot (quality: Rare+)                        │
│ ☑️ Combat text log                                   │
│ ☐ Auto-accept party invites                         │
│ ☑️ Show damage numbers                               │
│                                                       │
│ TEXT SPEED: [━━━━━━━━━━░░] 80%                      │
│ INTERFACE SCALE: [━━━━━━░░░░░░] 100%                │
│                                                       │
│ [Save] [Reset to Default] [Cancel]                  │
└──────────────────────────────────────────────────────┘
```

---

## Технические требования

**Framework:** React 18+  
**State:** Redux Toolkit  
**Routing:** React Router v6  
**UI Library:** Material-UI или Chakra UI  
**API Client:** Axios или Fetch  
**WebSocket:** Socket.io-client

**Responsive:**
- Desktop: 1920x1080, 1366x768
- Tablet: 1024x768, 768x1024
- Mobile: 375x667, 414x896

**Performance:**
- Lazy loading для маршрутов
- Virtual scrolling для списков
- Мемоизация компонентов
- Code splitting

---

## API Integration

**GET /api/v1/market/listings** - market  
**GET /api/v1/settings/{playerId}** - настройки  
**PUT /api/v1/settings** - сохранить

---

## Связанные документы

- `.BRAIN/05-technical/ui/main-game/ui-hud-core.md` - HUD (микрофича 1/3)
- `.BRAIN/05-technical/ui/main-game/ui-features.md` - Features (микрофича 2/3)
- `.BRAIN/05-technical/ui-game-start.md` - Start screens

---

## История изменений

- **v1.0.0 (2025-11-07 06:20)** - Микрофича 3/3 (split from ui-main-game.md)
