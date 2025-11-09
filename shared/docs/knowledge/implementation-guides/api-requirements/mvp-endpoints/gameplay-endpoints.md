---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:05
**api-readiness-notes:** MVP Gameplay Endpoints. Characters (CRUD), Locations, Inventory. ~380 строк.
---

# MVP Gameplay Endpoints - Игровая механика

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 (обновлено для микросервисов)  
**Приоритет:** КРИТИЧЕСКИЙ (MVP)  
**Автор:** AI Brain Manager

**Микрофича:** Characters, Locations, Inventory  
**Размер:** ~380 строк ✅  

---

## Микросервисная архитектура

**Endpoints распределены по сервисам:**
- `/api/v1/characters/*` → character-service (8082) 📋
- `/api/v1/gameplay/locations/*` → world-service (8086) 📋
- `/api/v1/economy/inventory/*` → economy-service (8085) 📋

**Доступ:** Все через API Gateway (http://localhost:8080)

---

## Characters

### GET /api/v1/characters

**Response 200:**
```json
{
  "characters": [
    {
      "id": "uuid",
      "name": "string",
      "class": "string",
      "level": 1,
      "city": "string",
      "faction": "string",
      "createdAt": "datetime"
    }
  ]
}
```

**Max:** 5 персонажей на аккаунт

---

### POST /api/v1/characters

**Request:**
```json
{
  "name": "string",
  "class": "solo|netrunner|techie|nomad|corpo",
  "origin": "street|nomad|corpo",
  "faction": "arasaka|militech|netwatch|independent",
  "city": "nightCity|tokyo|london",
  "gender": "male|female|other",
  "appearance": {
    "skinTone": "string",
    "hairStyle": "string",
    "hairColor": "string",
    "eyeColor": "string",
    "bodyType": "slim|athletic|muscular"
  }
}
```

**Response 201:**
```json
{
  "success": true,
  "character": {
    "id": "uuid",
    "name": "string",
    "class": "string",
    "level": 1
  }
}
```

---

### GET /api/v1/characters/{id}

**Response 200:**
```json
{
  "id": "uuid",
  "name": "string",
  "class": "string",
  "level": 1,
  "experience": 0,
  "attributes": {
    "STR": 10,
    "DEX": 10,
    "INT": 10,
    "TECH": 10,
    "COOL": 10
  },
  "location": {
    "city": "string",
    "zone": "string",
    "coords": {"x": 0, "y": 0}
  },
  "faction": "string",
  "reputation": {}
}
```

---

### DELETE /api/v1/characters/{id}

**Response 200:**
```json
{
  "success": true,
  "message": "Character deleted"
}
```

---

## Locations

### GET /api/v1/locations/{city}

**Response 200:**
```json
{
  "city": "nightCity",
  "zones": [
    {
      "id": "watson",
      "name": "Watson",
      "type": "residential",
      "danger": "low",
      "description": "string",
      "availableActivities": ["shop", "quest", "extract"]
    }
  ]
}
```

---

### GET /api/v1/locations/{city}/{zone}

**Response 200:**
```json
{
  "zone": {
    "id": "watson",
    "name": "Watson",
    "description": "string",
    "locations": [
      {
        "id": "kabuki_market",
        "name": "Kabuki Market",
        "type": "market",
        "npcs": ["vendor_01", "quest_npc_morgana"],
        "quests": ["NCPD-MORGANA-001"]
      }
    ]
  }
}
```

---

### POST /api/v1/locations/travel

**Request:**
```json
{
  "characterId": "uuid",
  "destinationCity": "tokyo",
  "destinationZone": "shibuya"
}
```

**Response 200:**
```json
{
  "success": true,
  "newLocation": {
    "city": "tokyo",
    "zone": "shibuya"
  },
  "travelCost": 500
}
```

---

## Inventory

### GET /api/v1/inventory/{characterId}

**Response 200:**
```json
{
  "items": [
    {
      "id": "uuid",
      "itemId": "pistol_basic",
      "name": "Basic Pistol",
      "type": "weapon",
      "quantity": 1,
      "equipped": true,
      "stats": {
        "damage": 10,
        "durability": 100
      }
    }
  ],
  "capacity": 20,
  "weight": 15.5
}
```

---

### POST /api/v1/inventory/equip

**Request:**
```json
{
  "characterId": "uuid",
  "itemId": "uuid"
}
```

**Response 200:**
```json
{
  "success": true,
  "equipped": true
}
```

---

### POST /api/v1/inventory/unequip

**Request:**
```json
{
  "characterId": "uuid",
  "itemId": "uuid"
}
```

---

### POST /api/v1/inventory/use

**Request:**
```json
{
  "characterId": "uuid",
  "itemId": "uuid"
}
```

**Response 200:**
```json
{
  "success": true,
  "effect": "healed 50 HP"
}
```

---

## Связанные документы

- `.BRAIN/05-technical/api-requirements/mvp-endpoints/auth-endpoints.md` - Auth (микрофича 1/4)
- `.BRAIN/05-technical/api-requirements/mvp-endpoints/content-endpoints.md` - Content (микрофича 3/4)
- `.BRAIN/05-technical/api-requirements/mvp-endpoints/system-endpoints.md` - System (микрофича 4/4)

---

## История изменений

- **v1.0.0 (2025-11-07 06:05)** - Микрофича 2/4: Gameplay Endpoints (split from mvp-endpoints.md)
