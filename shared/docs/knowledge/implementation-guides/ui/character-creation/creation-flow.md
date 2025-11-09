---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:35
**api-readiness-notes:** Character Creation Flow. Поток создания персонажа, выборы. ~390 строк.
---

# Character Creation Flow - Создание персонажа

**Статус:** approved  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 06:35  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Creation flow  
**Размер:** ~390 строк ✅

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Steps

### 1. Name & Gender

```
┌──────────────────────────────────────────────────────┐
│ CHARACTER NAME                                       │
│ [__________________________]                         │
│                                                       │
│ GENDER:                                              │
│ ( ) Male   ( ) Female   ( ) Other                    │
│                                                       │
│ [Next →]                                             │
└──────────────────────────────────────────────────────┘
```

### 2. Class Selection

```
┌──────────────────────────────────────────────────────┐
│ SELECT CLASS                                         │
├──────────────────────────────────────────────────────┤
│ ┌──────────┐ ┌──────────┐ ┌──────────┐             │
│ │  SOLO    │ │NETRUNNER │ │ TECHIE   │             │
│ │  Combat  │ │ Hacking  │ │ Crafting │             │
│ └──────────┘ └──────────┘ └──────────┘             │
│                                                       │
│ [← Back]                           [Next →]          │
└──────────────────────────────────────────────────────┘
```

### 3. Origin & Faction

```
ORIGIN:
- Street Kid (Night City streets)
- Nomad (Badlands)
- Corpo (Corporate)

FACTION:
- Arasaka
- Militech
- NetWatch
- Independent
```

---

## API

**POST /api/v1/characters** - create character

---

## Связанные документы

- `.BRAIN/05-technical/ui/character-creation/appearance-editor.md` - Appearance (микрофича 2/2)

---

## История изменений

- **v1.0.0 (2025-11-07 06:35)** - Микрофича 1/2 (split from ui-character-creation.md)
