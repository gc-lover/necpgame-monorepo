---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:20
**api-readiness-notes:** UI Main Game HUD. Главный экран, меню действий, инвентарь, экипировка. ~350 строк.
---

# UI Main Game HUD - Основной интерфейс

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:20  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** HUD & Inventory UI  
**Размер:** ~350 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Главный экран

```
┌──────────────────────────────────────────────────────┐
│ NECPGAME                            [⚙️ Settings]    │
├─────────────────────┬────────────────────────────────┤
│ CHARACTER INFO      │ LOCATION                       │
│                     │                                │
│ Viktor "V" Chen    │ 📍 Night City                  │
│ Level 15 Solo      │    Watson - Kabuki Market      │
│ ❤️  180/200 HP      │                                │
│ ⚡ 45/50 SP         │ 🌡️  Danger: Low                │
│                     │                                │
│ 💰 12,500 ed        │ [📍 View Map] [🚗 Travel]      │
│ ⭐ Arasaka: +45     │                                │
│                     │                                │
├─────────────────────┴────────────────────────────────┤
│ QUICK ACTIONS                                        │
│ [📋 Quests] [🎒 Inventory] [🗺️ Map] [👥 Social]     │
│ [⚔️ Combat] [🛒 Market] [📞 Contacts] [📊 Stats]    │
└──────────────────────────────────────────────────────┘
```

**Компоненты:**
- Character info panel (HP, level, money)
- Location panel (текущая локация, danger)
- Quick actions (8 кнопок)
- Notifications area (уведомления вверху)

---

## Меню действий

**Primary Actions (всегда доступны):**
```
📋 Quests - активные квесты
🎒 Inventory - инвентарь
🗺️ Map - карта мира
👥 Social - друзья, гильдия
⚔️ Combat - режим боя
🛒 Market - торговая площадка
📞 Contacts - NPC контакты
📊 Stats - статистика персонажа
```

**Contextual Actions (зависят от локации):**
```
🏪 Shop - если рядом магазин
💼 Job Board - если на точке заказов
🏥 Med Center - если в клинике
🔧 Cyberware Clinic - если в клинике имплантов
```

---

## Инвентарь UI

```
┌──────────────────────────────────────────────────────┐
│ INVENTORY              Weight: 45.2 / 100 kg   [Sort]│
├──────────────────────────────────────────────────────┤
│ [All] [Weapons] [Armor] [Consumables] [Materials]   │
│                                                       │
│ ┌──────┬──────┬──────┬──────┬──────┬──────┐        │
│ │[Icon]│[Icon]│[Icon]│[Icon]│      │      │  ←Row 1│
│ ├──────┼──────┼──────┼──────┼──────┼──────┤        │
│ │[Icon]│[Icon]│      │      │      │      │  ←Row 2│
│ ├──────┼──────┼──────┼──────┼──────┼──────┤        │
│ │      │      │      │      │      │      │  ←Row 3│
│ └──────┴──────┴──────┴──────┴──────┴──────┘        │
│                                                       │
│ SELECTED ITEM:                                       │
│ ┌────────────────────────────────────────────────┐  │
│ │ [Icon] Mantis Blades (Epic)                    │  │
│ │        Damage: 45-60                            │  │
│ │        Durability: 95/100                       │  │
│ │        Level: 20                                 │  │
│ │        [Equip] [Drop] [Sell]                    │  │
│ └────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────┘
```

**Features:**
- Grid layout (6x5 = 30 slots)
- Filter tabs
- Item details panel
- Actions: Equip/Drop/Use/Sell
- Sort options (name, type, level, rarity)

---

## Экипировка

```
┌──────────────────────────────────────────────────────┐
│ EQUIPMENT                                            │
├──────────────────────────────────────────────────────┤
│                                                       │
│        [HEAD]                                         │
│                                                       │
│   [CHEST]   [WEAPON]                                 │
│                                                       │
│   [LEGS]    [OFF-HAND]                               │
│                                                       │
│   [FEET]    [ACCESSORY]                              │
│                                                       │
│ STATS:                                               │
│ ⚔️  Damage: 45-60                                    │
│ 🛡️  Armor: 120                                       │
│ 🎯 Accuracy: +15%                                     │
│ ⚡ Speed: +10%                                        │
│                                                       │
└──────────────────────────────────────────────────────┘
```

---

## API Integration

**GET /api/v1/inventory/{characterId}** - получить инвентарь  
**POST /api/v1/inventory/equip** - надеть предмет  
**POST /api/v1/inventory/use** - использовать  
**POST /api/v1/inventory/drop** - выбросить

---

## Связанные документы

- `.BRAIN/05-technical/ui/main-game/ui-features.md` - Features (микрофича 2/3)
- `.BRAIN/05-technical/ui/main-game/ui-system.md` - System (микрофича 3/3)

---

## История изменений

- **v1.0.0 (2025-11-07 06:20)** - Микрофича 1/3 (split from ui-main-game.md)
