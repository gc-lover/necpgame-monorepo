# Game API - Запуск игры и начальный контент

**Назначение:** API для запуска игры и получения начального контента для MVP текстовой версии NECPGAME.

**Источники из .BRAIN:**
- `05-technical/game-start-scenario.md` (v1.0.0)
- `05-technical/mvp-initial-data.md` (v1.0.0)
- `05-technical/ui-game-start.md` (v1.1.0)
- `05-technical/mvp-data-json/` (JSON данные)

---

## Файлы API

### 1. `start.yaml` - API запуска игры
- **POST `/api/v1/game/start`** - Начать игру
- **GET `/api/v1/game/welcome`** - Получить приветственный экран
- **POST `/api/v1/game/return`** - Вернуться в игру (повторный вход)

### 2. `initial-state.yaml` - API начального состояния
- **GET `/api/v1/game/initial-state`** - Получить начальное состояние
- **GET `/api/v1/game/tutorial-steps`** - Получить шаги туториала

---

## Последовательность вызовов

### Первый вход в игру:

1. **GET `/api/v1/game/welcome?characterId={id}`** - Показать приветственный экран
2. **POST `/api/v1/game/start`** - Начать игру
3. **GET `/api/v1/game/initial-state?characterId={id}`** - Получить начальное состояние
4. (Опционально) **GET `/api/v1/game/tutorial-steps?characterId={id}`** - Получить туториал

### Повторный вход:

1. **POST `/api/v1/game/return`** - Вернуться в игру
2. **GET `/api/v1/game/initial-state?characterId={id}`** - Получить текущее состояние

---

## Примеры использования

### Пример 1: Начать игру

**Запрос:**
```http
POST /api/v1/game/start
Content-Type: application/json
Authorization: Bearer {token}

{
  "characterId": "550e8400-e29b-41d4-a716-446655440000",
  "skipTutorial": false
}
```

**Ответ:**
```json
{
  "gameSessionId": "660f9511-f30c-52e5-b827-557766551111",
  "characterId": "550e8400-e29b-41d4-a716-446655440000",
  "currentLocation": {
    "id": "loc-downtown-001",
    "name": "Downtown - Корпоративный центр",
    "description": "Вы стоите в центре корпоративного района...",
    "dangerLevel": "low"
  },
  "characterState": {
    "health": 100,
    "energy": 100,
    "humanity": 100,
    "money": 500,
    "level": 1
  },
  "startingEquipment": [
    {"itemId": "item-pistol-liberty", "quantity": 1},
    {"itemId": "item-armor-street", "quantity": 1}
  ],
  "welcomeMessage": "Добро пожаловать в Night City...",
  "tutorialEnabled": true
}
```

### Пример 2: Получить начальное состояние

**Запрос:**
```http
GET /api/v1/game/initial-state?characterId=550e8400-e29b-41d4-a716-446655440000
Authorization: Bearer {token}
```

**Ответ:**
```json
{
  "location": {
    "id": "loc-downtown-001",
    "name": "Downtown - Корпоративный центр",
    "description": "Вы стоите в центре корпоративного района...",
    "dangerLevel": "low",
    "connectedLocations": ["loc-watson-001"]
  },
  "availableNPCs": [
    {
      "id": "npc-sarah-miller",
      "name": "Сара Миллер",
      "type": "quest_giver",
      "greeting": "Привет. Я офицер Миллер...",
      "availableQuests": ["quest-delivery-001"]
    }
  ],
  "firstQuest": {
    "id": "quest-delivery-001",
    "name": "Доставка груза",
    "description": "Офицер NCPD Сара Миллер просит доставить посылку...",
    "level": 1,
    "rewards": {
      "experience": 100,
      "money": 200,
      "reputation": {"faction": "ncpd", "amount": 5}
    }
  },
  "availableActions": [
    {"id": "look-around", "label": "Осмотреть окрестности"},
    {"id": "talk-to-npc", "label": "Поговорить с NPC"},
    {"id": "move", "label": "Переместиться"}
  ]
}
```

---

## Связанные API

- `../auth/onboarding/auth-sessions.yaml` - Создание аккаунта и аутентификация (зависимость)
- `../lore/locations.yaml`