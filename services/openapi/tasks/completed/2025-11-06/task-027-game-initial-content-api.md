# Task ID: API-TASK-027
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-06 15:30
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-026 (Character Creation API - должен быть создан)

---

## 📋 Краткое описание

Создать API спецификацию для начального контента при входе в игру (Game Initial Content API) на основе документации из .BRAIN для MVP текстовой версии игры.

**Что нужно сделать:** Создать API спецификацию для получения начального контента при входе в игру, включая приветственный экран, начальное состояние персонажа, стартовую локацию, доступных NPC и первый квест.

---

## 🎯 Цель задания

Создать полноценный API для запуска игры и получения начального контента для MVP текстовой версии NECPGAME в киберпанк сеттинге 2020 года.

**Зачем это нужно:**
- Обеспечить плавный вход игрока в игру после создания персонажа
- Загрузить начальное состояние игры (локация Downtown, Night City, 2020 год)
- Предоставить доступ к стартовому контенту (NPC, квесты, предметы)
- Реализовать приветственный экран с туториалом

**Бизнес-логика:**
- Игра начинается в Night City, Downtown (корпоративный центр) в 2020 году
- Игрок получает стартовое снаряжение (500 eddies, базовое оружие, броню)
- Доступны первые NPC для взаимодействия (Сара Миллер, Джейк Арчер, Виктор Вектор)
- Первый квест "Доставка груза" от Сары Миллер
- Туториал опционален (можно пропустить)

**Связь с общими целями проекта:**
- Реализация MVP текстовой версии игры в киберпанк сеттинге
- Создание первого впечатления от игры
- Введение игрока в мир NECPGAME
- Подготовка к дальнейшему игровому процессу

---

## 📚 Источники информации

### Основные источники концепции

**Репозиторий:** `.BRAIN`

**Документы:**

1. **Сценарий запуска игры**
   - **Путь:** `.BRAIN/05-technical/game-start-scenario.md`
   - **Версия:** v1.0.0
   - **Дата:** 2025-11-05
   - **Статус:** review - детализация завершена
   - **api-readiness:** ready
   - **Что важно:**
     - Последовательность событий при запуске
     - Приветственный экран
     - Основной хаб персонажа
     - Первое взаимодействие с NPC
     - Первый квест
     - Обработка ошибок

2. **Сводка контента для MVP**
   - **Путь:** `.BRAIN/05-technical/mvp-content-summary.md`
   - **Версия:** v1.0.0
   - **Дата:** 2025-11-04
   - **Статус:** draft
   - **Что важно:**
     - 5 локаций (Downtown, Watson, Westbrook, Santo Domingo, Heywood)
     - 10 NPC (торговцы, квестодатели, враги)
     - 15 предметов
     - 5 квестов
     - JSON данные для загрузки

3. **Начальные данные для MVP**
   - **Путь:** `.BRAIN/05-technical/mvp-initial-data.md`
   - **Версия:** v1.0.0
   - **Дата:** 2025-11-04
   - **Статус:** draft
   - **api-readiness:** needs-work
   - **Что важно:**
     - Минимальный набор данных для MVP
     - Локации Night City (5 районов)
     - NPC (10 персонажей)
     - Предметы (15 предметов)
     - Квесты (5 квестов)
     - Классы персонажей (3 класса)
     - Базовые характеристики

4. **UI начала игры**
   - **Путь:** `.BRAIN/05-technical/ui-game-start.md`
   - **Версия:** v1.1.0
   - **Дата:** 2025-11-05
   - **Статус:** review - детализация завершена
   - **api-readiness:** ready
   - **Что важно:**
     - Экран приветствия
     - Основной интерфейс игры (хаб персонажа)
     - Меню действий в локации
     - Боевая система D&D формата (для будущего)

5. **JSON данные MVP**
   - **Путь:** `.BRAIN/05-technical/mvp-data-json/`
   - **Файлы:**
     - `locations.json` - 5 локаций Night City
     - `npcs.json` - 10 NPC
     - `items.json` - 15 предметов
     - `quests.json` - 5 квестов
   - **Что важно:**
     - Конкретные данные для загрузки в БД
     - Структура JSON для API

### Дополнительные источники

- `.BRAIN/01-concepts/vision.md` - основное видение проекта
- `.BRAIN/02-gameplay/progression/classes-overview.md` - классы персонажей
- `.BRAIN/03-lore/locations/locations-overview.md` - концепция локаций
- `.BRAIN/03-lore/timeline/2020-2040-destruction-recovery.md` - лор 2020 года

### Связанные документы

- [Создание персонажа](./task-026-character-creation-api.md) - API создания персонажа (зависимость)
- [Локации](../../../../.BRAIN/03-lore/locations/locations-overview.md) - концепция локаций
- [Квесты](../../../../.BRAIN/04-narrative/quest-system.md) - система квестов

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевые файлы:**
1. `api/v1/game/start.yaml` - API запуска игры
2. `api/v1/game/initial-state.yaml` - API начального состояния

**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── game/
            ├── README.md          # Обзор игровых API
            ├── start.yaml         # API запуска игры (создать)
            └── initial-state.yaml # API начального состояния (создать)
```

**Если файлы уже существуют:** Обновить существующие файлы, сохраняя совместимость.

---

## ✅ Что нужно сделать (детальный план)

### Шаг 1: Создать директорию и README

**Действия:**
1. Создать директорию `api/v1/game/` (если не существует)
2. Создать `README.md` с обзором игровых API:
   - Описание назначения директории
   - Список API файлов
   - Примеры использования
   - Связь с документацией .BRAIN

**Ожидаемый результат:**
- Директория `api/v1/game/` существует
- `README.md` содержит полное описание

### Шаг 2: Анализ исходных документов

**Действия:**
1. Прочитать `.BRAIN/05-technical/game-start-scenario.md`
2. Выделить ключевые концепции:
   - Последовательность запуска игры (8 этапов)
   - Приветственный экран
   - Основной хаб персонажа
   - Стартовая локация (Downtown)
   - Доступные NPC (Сара Миллер, Джейк Арчер, Виктор Вектор)
   - Первый квест ("Доставка груза")
   - Туториал (опционально)
3. Прочитать `.BRAIN/05-technical/mvp-initial-data.md`
4. Выделить бизнес-правила:
   - Стартовая локация: Downtown, Night City
   - Стартовые деньги: 500 eddies
   - Стартовое снаряжение: базовое оружие, броня
   - Начальный уровень: 1
   - Начальные характеристики: здоровье 100, энергия 100, человечность 100
5. Прочитать JSON файлы (`locations.json`, `npcs.json`, `quests.json`)
6. Определить структуру данных для API

**Ожидаемый результат:**
- Список концепций для отражения в API
- Список бизнес-правил
- Структура данных для API ответов

### Шаг 3: Определение API endpoints

**Что нужно создать:**

#### Файл 1: `api/v1/game/start.yaml`

**Endpoints:**

1. **POST `/api/v1/game/start`**
   - **Назначение:** Начать игру после создания персонажа
   - **Параметры запроса:**
     - `characterId` (string, required) - ID созданного персонажа
     - `skipTutorial` (boolean, optional) - Пропустить туториал (default: false)
   - **Ответы:**
     - `200 OK` - Игра началась успешно
       ```json
       {
         "gameSessionId": "uuid",
         "characterId": "uuid",
         "currentLocation": {
           "id": "loc-downtown-001",
           "name": "Downtown - Корпоративный центр",
           "description": "..."
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
     - `400 Bad Request` - Невалидные параметры
     - `404 Not Found` - Персонаж не найден
     - `409 Conflict` - Игра уже началась для этого персонажа
     - `500 Internal Server Error` - Ошибка сервера

2. **GET `/api/v1/game/welcome`**
   - **Назначение:** Получить приветственный экран
   - **Параметры запроса:**
     - `characterId` (string, required) - ID персонажа
   - **Ответы:**
     - `200 OK` - Приветственный экран
       ```json
       {
         "message": "Добро пожаловать в NECPGAME",
         "subtitle": "Ночь в Night City только начинается...",
         "character": {
           "name": "Джон Доу",
           "class": "Соло",
           "level": 1
         },
         "startingLocation": "Night City - Downtown",
         "buttons": [
           {"id": "start-game", "label": "Начать игру"},
           {"id": "skip-tutorial", "label": "Пропустить туториал"}
         ]
       }
       ```
     - `404 Not Found` - Персонаж не найден

3. **POST `/api/v1/game/return`**
   - **Назначение:** Вернуться в игру (повторный вход)
   - **Параметры запроса:**
     - `characterId` (string, required) - ID персонажа
   - **Ответы:**
     - `200 OK` - Возврат в игру
       ```json
       {
         "gameSessionId": "uuid",
         "characterId": "uuid",
         "currentLocation": {
           "id": "loc-watson-001",
           "name": "Watson - Индустриальный район",
           "description": "..."
         },
         "characterState": {
           "health": 80,
           "energy": 70,
           "humanity": 100,
           "money": 750,
           "level": 1
         },
         "activeQuests": [
           {"questId": "quest-delivery-001", "progress": 50}
         ]
       }
       ```
     - `404 Not Found` - Персонаж не найден или игра не началась

#### Файл 2: `api/v1/game/initial-state.yaml`

**Endpoints:**

1. **GET `/api/v1/game/initial-state`**
   - **Назначение:** Получить начальное состояние игры
   - **Параметры запроса:**
     - `characterId` (string, required) - ID персонажа
   - **Ответы:**
     - `200 OK` - Начальное состояние
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
           },
           {
             "id": "npc-jake-archer",
             "name": "Джейк Арчер",
             "type": "trader",
             "greeting": "Привет, чомбата. Что тебе нужно?..."
           },
           {
             "id": "npc-victor-vector",
             "name": "Виктор Вектор",
             "type": "citizen",
             "greeting": "Привет. Я Виктор, риппердок..."
           }
         ],
         "firstQuest": {
           "id": "quest-delivery-001",
           "name": "Доставка груза",
           "description": "Офицер NCPD Сара Миллер просит доставить посылку...",
           "level": 1,
           "giverNpcId": "npc-sarah-miller",
           "rewards": {
             "experience": 100,
             "money": 200,
             "reputation": {"faction": "ncpd", "amount": 5}
           }
         },
         "availableActions": [
           {"id": "look-around", "label": "Осмотреть окрестности"},
           {"id": "talk-to-npc", "label": "Поговорить с NPC"},
           {"id": "move", "label": "Переместиться"},
           {"id": "rest", "label": "Отдохнуть"},
           {"id": "inventory", "label": "Открыть инвентарь"}
         ]
       }
       ```
     - `404 Not Found` - Персонаж не найден

2. **GET `/api/v1/game/tutorial-steps`**
   - **Назначение:** Получить шаги туториала
   - **Параметры запроса:**
     - `characterId` (string, required) - ID персонажа
   - **Ответы:**
     - `200 OK` - Шаги туториала
       ```json
       {
         "steps": [
           {
             "id": "step-1",
             "title": "Добро пожаловать",
             "description": "Это ваш первый день в Night City...",
             "hint": "Изучите интерфейс и выберите действие"
           },
           {
             "id": "step-2",
             "title": "Осмотрите локацию",
             "description": "Изучите описание Downtown...",
             "hint": "Нажмите 'Осмотреть окрестности'"
           },
           {
             "id": "step-3",
             "title": "Поговорите с NPC",
             "description": "Найдите офицера Сару Миллер...",
             "hint": "Выберите 'Поговорить с NPC' и найдите Сару Миллер"
           },
           {
             "id": "step-4",
             "title": "Примите первый квест",
             "description": "Сара Миллер даст вам первое задание...",
             "hint": "Выберите опцию 'Какие задания у тебя есть?'"
           }
         ],
         "currentStep": 0,
         "totalSteps": 4,
         "canSkip": true
       }
       ```
     - `404 Not Found` - Персонаж не найден

### Шаг 4: Определение моделей данных

**Что нужно создать:**

#### Модели в `api/v1/game/start.yaml`:

1. **GameStartRequest** (запрос начала игры)
   ```yaml
   GameStartRequest:
     type: object
     required:
       - characterId
     properties:
       characterId:
         type: string
         format: uuid
         description: ID созданного персонажа
         example: "550e8400-e29b-41d4-a716-446655440000"
       skipTutorial:
         type: boolean
         description: Пропустить туториал
         default: false
         example: false
   ```

2. **GameStartResponse** (ответ начала игры)
   ```yaml
   GameStartResponse:
     type: object
     required:
       - gameSessionId
       - characterId
       - currentLocation
       - characterState
       - startingEquipment
       - welcomeMessage
       - tutorialEnabled
     properties:
       gameSessionId:
         type: string
         format: uuid
         description: ID игровой сессии
         example: "660f9511-f30c-52e5-b827-557766551111"
       characterId:
         type: string
         format: uuid
         description: ID персонажа
       currentLocation:
         $ref: '#/components/schemas/GameLocation'
       characterState:
         $ref: '#/components/schemas/GameCharacterState'
       startingEquipment:
         type: array
         items:
           $ref: '#/components/schemas/GameStartingItem'
       welcomeMessage:
         type: string
         description: Приветственное сообщение
         example: "Добро пожаловать в Night City. Вы стоите в центре корпоративного района Downtown. Неоновые вывески мигают на стенах зданий. Ваше приключение начинается..."
       tutorialEnabled:
         type: boolean
         description: Включен ли туториал
   ```

3. **GameLocation** (локация)
   ```yaml
   GameLocation:
     type: object
     required:
       - id
       - name
       - description
       - dangerLevel
     properties:
       id:
         type: string
         description: ID локации
         example: "loc-downtown-001"
       name:
         type: string
         description: Название локации
         example: "Downtown - Корпоративный центр"
       description:
         type: string
         description: Описание локации
         example: "Вы стоите в центре корпоративного района Night City..."
       city:
         type: string
         description: Город
         example: "Night City"
       district:
         type: string
         description: Район
         example: "Downtown"
       dangerLevel:
         type: string
         enum: [low, medium, high]
         description: Уровень опасности
         example: "low"
       minLevel:
         type: integer
         description: Минимальный уровень персонажа
         example: 1
       type:
         type: string
         enum: [corporate, industrial, residential, criminal]
         description: Тип локации
         example: "corporate"
       connectedLocations:
         type: array
         items:
           type: string
         description: Связанные локации (ID)
         example: ["loc-watson-001"]
   ```

4. **GameCharacterState** (состояние персонажа)
   ```yaml
   GameCharacterState:
     type: object
     required:
       - health
       - energy
       - humanity
       - money
       - level
     properties:
       health:
         type: integer
         minimum: 0
         maximum: 100
         description: Здоровье персонажа
         example: 100
       energy:
         type: integer
         minimum: 0
         maximum: 100
         description: Энергия персонажа
         example: 100
       humanity:
         type: integer
         minimum: 0
         maximum: 100
         description: Человечность персонажа
         example: 100
       money:
         type: integer
         minimum: 0
         description: Деньги (eddies)
         example: 500
       level:
         type: integer
         minimum: 1
         description: Уровень персонажа
         example: 1
       experience:
         type: integer
         minimum: 0
         description: Опыт персонажа
         example: 0
   ```

5. **GameStartingItem** (стартовый предмет)
   ```yaml
   GameStartingItem:
     type: object
     required:
       - itemId
       - quantity
     properties:
       itemId:
         type: string
         description: ID предмета
         example: "item-pistol-liberty"
       quantity:
         type: integer
         minimum: 1
         description: Количество
         example: 1
   ```

6. **WelcomeScreenResponse** (приветственный экран)
   ```yaml
   WelcomeScreenResponse:
     type: object
     required:
       - message
       - subtitle
       - character
       - startingLocation
       - buttons
     properties:
       message:
         type: string
         description: Приветственное сообщение
         example: "Добро пожаловать в NECPGAME"
       subtitle:
         type: string
         description: Подзаголовок
         example: "Ночь в Night City только начинается..."
       character:
         type: object
         properties:
           name:
             type: string
             example: "Джон Доу"
           class:
             type: string
             example: "Соло"
           level:
             type: integer
             example: 1
       startingLocation:
         type: string
         description: Стартовая локация
         example: "Night City - Downtown"
       buttons:
         type: array
         items:
           type: object
           properties:
             id:
               type: string
             label:
               type: string
         example:
           - id: "start-game"
             label: "Начать игру"
           - id: "skip-tutorial"
             label: "Пропустить туториал"
   ```

#### Модели в `api/v1/game/initial-state.yaml`:

1. **InitialStateResponse** (начальное состояние)
   ```yaml
   InitialStateResponse:
     type: object
     required:
       - location
       - availableNPCs
       - firstQuest
       - availableActions
     properties:
       location:
         $ref: '../start.yaml#/components/schemas/GameLocation'
       availableNPCs:
         type: array
         items:
           $ref: '#/components/schemas/GameNPC'
       firstQuest:
         $ref: '#/components/schemas/GameQuest'
       availableActions:
         type: array
         items:
           $ref: '#/components/schemas/GameAction'
   ```

2. **GameNPC** (NPC)
   ```yaml
   GameNPC:
     type: object
     required:
       - id
       - name
       - type
       - greeting
     properties:
       id:
         type: string
         description: ID NPC
         example: "npc-sarah-miller"
       name:
         type: string
         description: Имя NPC
         example: "Сара Миллер"
       description:
         type: string
         description: Описание NPC
         example: "Офицер NCPD, работает в корпоративном районе..."
       type:
         type: string
         enum: [trader, quest_giver, citizen, enemy]
         description: Тип NPC
         example: "quest_giver"
       faction:
         type: string
         description: Фракция NPC
         example: "ncpd"
       greeting:
         type: string
         description: Приветствие NPC
         example: "Привет. Я офицер Миллер. Если ты хочешь помочь полиции..."
       availableQuests:
         type: array
         items:
           type: string
         description: Доступные квесты (ID)
         example: ["quest-delivery-001"]
   ```

3. **GameQuest** (квест)
   ```yaml
   GameQuest:
     type: object
     required:
       - id
       - name
       - description
       - level
       - giverNpcId
       - rewards
     properties:
       id:
         type: string
         description: ID квеста
         example: "quest-delivery-001"
       name:
         type: string
         description: Название квеста
         example: "Доставка груза"
       description:
         type: string
         description: Описание квеста
         example: "Офицер NCPD Сара Миллер просит доставить посылку..."
       type:
         type: string
         enum: [main, side, contract]
         description: Тип квеста
         example: "side"
       level:
         type: integer
         minimum: 1
         description: Уровень квеста
         example: 1
       giverNpcId:
         type: string
         description: ID NPC, дающего квест
         example: "npc-sarah-miller"
       rewards:
         $ref: '#/components/schemas/GameQuestRewards'
   ```

4. **GameQuestRewards** (награды за квест)
   ```yaml
   GameQuestRewards:
     type: object
     properties:
       experience:
         type: integer
         minimum: 0
         description: Опыт
         example: 100
       money:
         type: integer
         minimum: 0
         description: Деньги (eddies)
         example: 200
       items:
         type: array
         items:
           type: string
         description: Предметы (ID)
         example: []
       reputation:
         type: object
         properties:
           faction:
             type: string
             example: "ncpd"
           amount:
             type: integer
             example: 5
         description: Репутация
   ```

5. **GameAction** (действие)
   ```yaml
   GameAction:
     type: object
     required:
       - id
       - label
     properties:
       id:
         type: string
         description: ID действия
         example: "look-around"
       label:
         type: string
         description: Название действия
         example: "Осмотреть окрестности"
       description:
         type: string
         description: Описание действия
         example: "Осмотрите окрестности, чтобы найти точки интереса"
       enabled:
         type: boolean
         description: Доступно ли действие
         default: true
   ```

6. **TutorialStepsResponse** (шаги туториала)
   ```yaml
   TutorialStepsResponse:
     type: object
     required:
       - steps
       - currentStep
       - totalSteps
       - canSkip
     properties:
       steps:
         type: array
         items:
           $ref: '#/components/schemas/TutorialStep'
       currentStep:
         type: integer
         minimum: 0
         description: Текущий шаг (0-based)
         example: 0
       totalSteps:
         type: integer
         minimum: 1
         description: Всего шагов
         example: 4
       canSkip:
         type: boolean
         description: Можно ли пропустить туториал
         example: true
   ```

7. **TutorialStep** (шаг туториала)
   ```yaml
   TutorialStep:
     type: object
     required:
       - id
       - title
       - description
       - hint
     properties:
       id:
         type: string
         description: ID шага
         example: "step-1"
       title:
         type: string
         description: Название шага
         example: "Добро пожаловать"
       description:
         type: string
         description: Описание шага
         example: "Это ваш первый день в Night City..."
       hint:
         type: string
         description: Подсказка
         example: "Изучите интерфейс и выберите действие"
   ```

### Шаг 5: Создание OpenAPI спецификации

**Требования к файлам:**

#### Файл `api/v1/game/start.yaml`:

```yaml
openapi: 3.0.3
info:
  title: Game Start API
  version: 1.0.0
  description: |
    API для запуска игры и получения начального контента.
    
    **Источники:**
    - `.BRAIN/05-technical/game-start-scenario.md` (v1.0.0)
    - `.BRAIN/05-technical/mvp-initial-data.md` (v1.0.0)
    - `.BRAIN/05-technical/ui-game-start.md` (v1.1.0)
    
    **Игра начинается:**
    - Локация: Downtown, Night City
    - Год: 2020
    - Стартовые деньги: 500 eddies
    - Уровень: 1
    
servers:
  - url: https://api.necp.game/v1
    description: Production server
  - url: http://localhost:8080/v1
    description: Development server

tags:
  - name: Game Start
    description: Запуск игры и начальный контент

paths:
  # Endpoints здесь (POST /game/start, GET /game/welcome, POST /game/return)

components:
  schemas:
    # Модели данных здесь

  responses:
    # Используй общие ответы из api/v1/shared/common/responses.yaml
    BadRequest:
      $ref: '../shared/common/responses.yaml#/components/responses/BadRequest'
    NotFound:
      $ref: '../shared/common/responses.yaml#/components/responses/NotFound'
    Conflict:
      $ref: '../shared/common/responses.yaml#/components/responses/Conflict'
    InternalServerError:
      $ref: '../shared/common/responses.yaml#/components/responses/InternalServerError'

  securitySchemes:
    # Используй общие схемы безопасности
    BearerAuth:
      $ref: '../shared/common/security.yaml#/components/securitySchemes/BearerAuth'

security:
  - BearerAuth: []
```

#### Файл `api/v1/game/initial-state.yaml`:

```yaml
openapi: 3.0.3
info:
  title: Game Initial State API
  version: 1.0.0
  description: |
    API для получения начального состояния игры.
    
    **Источники:**
    - `.BRAIN/05-technical/game-start-scenario.md` (v1.0.0)
    - `.BRAIN/05-technical/mvp-data-json/locations.json`
    - `.BRAIN/05-technical/mvp-data-json/npcs.json`
    - `.BRAIN/05-technical/mvp-data-json/quests.json`
    
servers:
  - url: https://api.necp.game/v1
    description: Production server
  - url: http://localhost:8080/v1
    description: Development server

tags:
  - name: Initial State
    description: Начальное состояние игры

paths:
  # Endpoints здесь (GET /game/initial-state, GET /game/tutorial-steps)

components:
  schemas:
    # Модели данных здесь

  responses:
    # Используй общие ответы
    BadRequest:
      $ref: '../shared/common/responses.yaml#/components/responses/BadRequest'
    NotFound:
      $ref: '../shared/common/responses.yaml#/components/responses/NotFound'
    InternalServerError:
      $ref: '../shared/common/responses.yaml#/components/responses/InternalServerError'

  securitySchemes:
    BearerAuth:
      $ref: '../shared/common/security.yaml#/components/securitySchemes/BearerAuth'

security:
  - BearerAuth: []
```

**Обязательные элементы:**
- ✅ OpenAPI версия: 3.0.3
- ✅ Info блок с описанием и ссылками на .BRAIN
- ✅ Servers блок
- ✅ Tags для группировки
- ✅ Paths со всеми endpoints
- ✅ Components/schemas со всеми моделями
- ✅ Примеры запросов/ответов
- ✅ Коды ошибок (используй общие из `shared/common/responses.yaml`)
- ✅ Валидация параметров
- ✅ Security схемы

### Шаг 6: Создание README.md

**Действия:**
1. Создать `api/v1/game/README.md`
2. Добавить обзор игровых API:
   - Назначение директории
   - Список API файлов с описаниями
   - Примеры использования (последовательность вызовов)
   - Связь с документацией .BRAIN
   - Диаграмма последовательности вызовов

**Содержимое README.md:**

```markdown
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

---

## Связанные API

- `../character/creation.yaml` - Создание персонажа (зависимость)
- `../lore/locations.yaml` - Локации
- `../narrative/quests.yaml` - Квесты

---

## История изменений

- v1.0.0 (2025-11-06) - Создание API для запуска игры и начального контента
```

### Шаг 7: Валидация и проверка

**Что проверить:**

1. **Валидация YAML:**
   - Синтаксис YAML корректен
   - OpenAPI спецификация валидна (используй валидатор OpenAPI)

2. **Проверка полноты:**
   - Все endpoints из документации включены
   - Все модели данных описаны
   - Все бизнес-правила отражены

3. **Проверка соответствия:**
   - API соответствует документации из `.BRAIN`
   - Соответствует принципам проекта
   - Соответствует стандартам OpenAPI 3.0.3

4. **Проверка размера файлов:**
   - Каждый файл не превышает 400 строк
   - Если превышает - разбить на несколько файлов

5. **Проверка связности:**
   - Связанные API совместимы
   - Нет конфликтов с существующими API
   - Используются общие компоненты из `shared/common/`

6. **Проверка ошибок:**
   - НЕ создавай множество разных типов ошибок
   - Используй ЕДИНУЮ модель Error из `shared/common/responses.yaml`
   - Backend определяет HTTP статус по коду ошибки

---

## 🎨 Принципы и правила

### Общие принципы проекта

1. **API First** - сначала спецификация, потом реализация
2. **RESTful API** - стандартные HTTP методы и коды ответов
3. **Версионирование** - семантическое версионирование API
4. **Документация** - все endpoints, модели и примеры должны быть задокументированы
5. **Безопасность** - все endpoints требуют аутентификации (Bearer Token)

### Стиль кодирования

1. **Именование:**
   - Endpoints: kebab-case (`/api/v1/game/start`, `/api/v1/game/initial-state`)
   - Модели: PascalCase (`GameStartRequest`, `GameLocation`, `GameCharacterState`)
   - Параметры: camelCase (`characterId`, `skipTutorial`)

2. **Формат:**
   - YAML формат
   - Отступы: 2 пробела
   - Строки не длиннее 120 символов

3. **Документация:**
   - Все поля должны иметь `description`
   - Все enum должны иметь описания значений
   - Примеры обязательны для основных endpoints
   - Ссылки на .BRAIN документы в `info.description`

### Правила из workspace rules

1. **SOLID** - одна фича - один файл
2. **DRY** - избегать дублирования (используй общие компоненты из `shared/common/`)
3. **KISS** - простота решений
4. **Данные в БД** - не хардкодить, хранить в БД
5. **Максимум 400 строк на файл** - если больше, разбить на несколько файлов

### Обработка ошибок - МИНИМАЛИСТИЧНО

**ВАЖНО:** НЕ создавай множество разных типов ошибок для каждого HTTP статуса!

**Используй ЕДИНУЮ модель Error:**
- Из `shared/common/responses.yaml`
- Backend определяет HTTP статус по коду ошибки (`ErrorCode` enum)
- Коды ошибок:
  - `AUTH_*` → 401/403 (аутентификация/авторизация)
  - `BIZ_*` → 404/409 (бизнес-логика: not found, conflict)
  - `VAL_*` → 400 (валидация)
  - `INT_*` → 500/503 (внутренние ошибки)

**Пример использования:**
```yaml
responses:
  '400':
    $ref: '../shared/common/responses.yaml#/components/responses/BadRequest'
  '404':
    $ref: '../shared/common/responses.yaml#/components/responses/NotFound'
  '409':
    $ref: '../shared/common/responses.yaml#/components/responses/Conflict'
  '500':
    $ref: '../shared/common/responses.yaml#/components/responses/InternalServerError'
```

### Именование моделей - ИЗБЕГАЙ КОНФЛИКТОВ

**НЕ используй** имена встроенных классов Java и JavaScript:
- Проблемные: `Character`, `String`, `Number`, `Boolean`, `Date`, `Error`, `Event`, `File`, `Map`, `Set`, `List`
- **Используй префиксы:** `GameCharacter`, `GameEvent`, `GameItem`, `PlayerProfile`

**В этом задании:**
- ✅ `GameStartRequest`, `GameStartResponse` (хорошо)
- ✅ `GameLocation`, `GameCharacterState` (хорошо)
- ❌ `Character`, `Location` (плохо, конфликты)

### Разбиение файлов

**ВАЖНО:** OpenAPI Generator НЕ поддерживает `$ref` для paths (endpoints) из внешних файлов!

**Правило:**
- **Paths (endpoints)** ВСЕГДА должны быть в главном файле
- **Components (schemas, responses)** МОЖНО выносить в отдельные файлы через `$ref`

**Если файл превышает 400 строк:**
1. Проверь, можно ли вынести компоненты в отдельные файлы
2. Если нет - создай несколько файлов для разных групп endpoints
3. Примеры:
   - `game/start.yaml` - основные endpoints (POST /start, GET /welcome)
   - `game/initial-state.yaml` - состояние игры (GET /initial-state, GET /tutorial-steps)

---

## 📝 Примеры и шаблоны

### Пример структуры endpoint:

```yaml
paths:
  /game/start:
    post:
      summary: Начать игру
      description: |
        Начинает игру для созданного персонажа.
        
        **Бизнес-логика:**
        - Персонаж появляется в стартовой локации (Downtown)
        - Выдается стартовое снаряжение (пистолет, броня)
        - Выдаются стартовые деньги (500 eddies)
        - Устанавливаются начальные характеристики (здоровье 100, энергия 100, человечность 100)
        - Создается игровая сессия
        
        **Источник:** `.BRAIN/05-technical/game-start-scenario.md` (Шаг 1.1, 1.2)
      operationId: startGame
      tags:
        - Game Start
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/GameStartRequest'
            examples:
              with_tutorial:
                summary: Начать с туториалом
                value:
                  characterId: "550e8400-e29b-41d4-a716-446655440000"
                  skipTutorial: false
              skip_tutorial:
                summary: Пропустить туториал
                value:
                  characterId: "550e8400-e29b-41d4-a716-446655440000"
                  skipTutorial: true
      responses:
        '200':
          description: Игра началась успешно
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/GameStartResponse'
              example:
                gameSessionId: "660f9511-f30c-52e5-b827-557766551111"
                characterId: "550e8400-e29b-41d4-a716-446655440000"
                currentLocation:
                  id: "loc-downtown-001"
                  name: "Downtown - Корпоративный центр"
                  description: "Вы стоите в центре корпоративного района..."
                  dangerLevel: "low"
                characterState:
                  health: 100
                  energy: 100
                  humanity: 100
                  money: 500
                  level: 1
                startingEquipment:
                  - itemId: "item-pistol-liberty"
                    quantity: 1
                  - itemId: "item-armor-street"
                    quantity: 1
                welcomeMessage: "Добро пожаловать в Night City..."
                tutorialEnabled: true
        '400':
          $ref: '../shared/common/responses.yaml#/components/responses/BadRequest'
        '404':
          $ref: '../shared/common/responses.yaml#/components/responses/NotFound'
        '409':
          $ref: '../shared/common/responses.yaml#/components/responses/Conflict'
        '500':
          $ref: '../shared/common/responses.yaml#/components/responses/InternalServerError'
      security:
        - BearerAuth: []
```

### Пример структуры модели:

```yaml
components:
  schemas:
    GameLocation:
      type: object
      required:
        - id
        - name
        - description
        - dangerLevel
      properties:
        id:
          type: string
          description: Уникальный идентификатор локации
          example: "loc-downtown-001"
        name:
          type: string
          description: Название локации
          minLength: 1
          maxLength: 200
          example: "Downtown - Корпоративный центр"
        description:
          type: string
          description: Детальное описание локации
          minLength: 10
          maxLength: 2000
          example: "Вы стоите в центре корпоративного района Night City. Вокруг вас возвышаются небоскребы мегакорпораций..."
        city:
          type: string
          description: Город
          example: "Night City"
        district:
          type: string
          description: Район города
          example: "Downtown"
        dangerLevel:
          type: string
          enum: [low, medium, high]
          description: |
            Уровень опасности локации:
            - low: безопасная зона (Downtown, Westbrook)
            - medium: средняя опасность (Watson, Santo Domingo)
            - high: опасная зона (Heywood)
          example: "low"
        minLevel:
          type: integer
          minimum: 1
          maximum: 100
          description: Минимальный уровень персонажа для посещения
          example: 1
        type:
          type: string
          enum: [corporate, industrial, residential, criminal]
          description: |
            Тип локации:
            - corporate: корпоративная зона
            - industrial: индустриальная зона
            - residential: жилая зона
            - criminal: криминальная зона
          example: "corporate"
        connectedLocations:
          type: array
          items:
            type: string
          description: Список ID связанных локаций (для перемещения)
          example: ["loc-watson-001"]
```

---

## 🔗 Связанные API и зависимости

### Зависимости (API, которые должны быть созданы/обновлены раньше):

- `api/v1/character/creation.yaml` - **API-TASK-026** - должен существовать (для создания персонажа перед началом игры)
- `api/v1/shared/common/responses.yaml` - общие ответы (должны существовать)
- `api/v1/shared/common/security.yaml` - схемы безопасности (должны существовать)

### Связанные API (ссылаются друг на друга):

- `api/v1/lore/locations.yaml` - локации (будет создан позже, используй модель GameLocation)
- `api/v1/narrative/quests.yaml` - квесты (будет создан позже, используй модель GameQuest)
- `api/v1/character/state.yaml` - состояние персонажа (будет создан позже, используй модель GameCharacterState)

### Будущие зависимые API (будут созданы позже):

- `api/v1/gameplay/actions.yaml` - действия в локации
- `api/v1/gameplay/movement.yaml` - перемещение между локациями
- `api/v1/narrative/dialogues.yaml` - диалоги с NPC

---

## ✅ Критерии приемки

Задание считается выполненным, если:

1. ✅ **Файлы созданы:**
   - `api/v1/game/README.md` существует
   - `api/v1/game/start.yaml` существует
   - `api/v1/game/initial-state.yaml` существует

2. ✅ **OpenAPI валидный:** 
   - Все файлы проходят валидацию OpenAPI 3.0.3
   - Нет синтаксических ошибок YAML

3. ✅ **Все endpoints:**
   - POST `/api/v1/game/start` - начать игру
   - GET `/api/v1/game/welcome` - приветственный экран
   - POST `/api/v1/game/return` - вернуться в игру
   - GET `/api/v1/game/initial-state` - начальное состояние
   - GET `/api/v1/game/tutorial-steps` - шаги туториала

4. ✅ **Все модели данных:**
   - GameStartRequest, GameStartResponse
   - WelcomeScreenResponse
   - GameLocation, GameCharacterState, GameStartingItem
   - InitialStateResponse
   - GameNPC, GameQuest, GameQuestRewards, GameAction
   - TutorialStepsResponse, TutorialStep

5. ✅ **Документация:**
   - Все endpoints задокументированы
   - Все модели имеют описания полей
   - Примеры запросов/ответов включены
   - README.md содержит обзор и примеры

6. ✅ **Соответствие документации:**
   - API соответствует `.BRAIN/05-technical/game-start-scenario.md`
   - API соответствует `.BRAIN/05-technical/mvp-initial-data.md`
   - Все данные из JSON файлов отражены

7. ✅ **Валидация:**
   - Все параметры имеют валидацию (required, minimum, maximum, minLength, maxLength)
   - Все enum имеют описания значений
   - Все модели имеют required поля

8. ✅ **Обработка ошибок:**
   - Используется ЕДИНАЯ модель Error из `shared/common/responses.yaml`
   - Все endpoints имеют стандартные коды ошибок (400, 404, 409, 500)
   - НЕТ дублирования кодов ошибок

9. ✅ **Именование моделей:**
   - Используются префиксы для избежания конфликтов (Game*)
   - НЕТ использования проблемных имен (Character, Location без префиксов)

10. ✅ **Размер файлов:**
    - Каждый файл не превышает 400 строк
    - Если превышает - разбит на несколько файлов

11. ✅ **Общие компоненты:**
    - Используются общие ответы из `shared/common/responses.yaml`
    - Используются схемы безопасности из `shared/common/security.yaml`
    - НЕТ дублирования общих компонентов

12. ✅ **Связность:**
    - Связанные модели совместимы
    - Нет конфликтов с существующими API
    - Ссылки на другие API корректны

13. ✅ **Ссылки на .BRAIN:**
    - В `info.description` указаны пути к документам .BRAIN
    - В комментариях к endpoints указаны ссылки на секции документов

14. ✅ **Безопасность:**
    - Все endpoints требуют аутентификации (BearerAuth)
    - Security схемы подключены

15. ✅ **README.md:**
    - Обзор директории
    - Список файлов с описаниями
    - Примеры использования
    - Последовательность вызовов API
    - Связанные API

---

## ❓ Возможные вопросы и ответы

**Q: Что делать, если в исходном документе нет всех деталей?**
A: Использовать принципы и стиль проекта, создать логичную структуру на основе существующих API. Обратиться к JSON файлам (`mvp-data-json/`) для конкретных данных.

**Q: Как обрабатывать опциональные поля?**
A: Четко обозначить в схеме, какие поля required, какие optional. Добавить описание для каждого. Для опциональных полей указать default значения.

**Q: Нужно ли версионирование с первого дня?**
A: Да, использовать семантическое версионирование. Начать с v1.0.0.

**Q: Что делать, если файл превышает 400 строк?**
A: Разбить на несколько файлов по логическим группам:
- `start.yaml` - запуск игры (POST /start, GET /welcome, POST /return)
- `initial-state.yaml` - начальное состояние (GET /initial-state, GET /tutorial-steps)

**Q: Можно ли использовать $ref для endpoints?**
A: НЕТ! OpenAPI Generator НЕ поддерживает `$ref` для paths. Все endpoints должны быть в главном файле. Только components можно выносить через `$ref`.

**Q: Как обрабатывать ошибки?**
A: Используй ЕДИНУЮ модель Error из `shared/common/responses.yaml`. Backend определяет HTTP статус по коду ошибки. НЕ создавай множество разных типов ошибок.

**Q: Какие префиксы использовать для моделей?**
A: Используй префикс `Game*` для всех моделей в этом API (GameStartRequest, GameLocation, GameCharacterState), чтобы избежать конфликтов с встроенными классами Java/JavaScript.

**Q: Что делать с туториалом?**
A: Туториал опционален (можно пропустить). Если `skipTutorial: true`, игрок сразу попадает в игру без подсказок. Если `false` - показываются шаги туториала из GET `/game/tutorial-steps`.

**Q: Какие данные должны быть в БД?**
A: ВСЕ данные должны быть в БД: локации, NPC, квесты, предметы. API только предоставляет доступ к данным, не хардкодит их. Используй JSON файлы из `mvp-data-json/` для загрузки в БД.

---

## 📞 Дополнительная информация

**Если возникли вопросы:**
- Проверить связанные документы в `.BRAIN`
- Посмотреть примеры существующих API в `API-SWAGGER/api/v1/`
- Проверить правила в `.cursor/rules/`
- Проверить JSON данные в `.BRAIN/05-technical/mvp-data-json/`

**Полезные ссылки:**
- [OpenAPI Specification](https://swagger.io/specification/)
- [REST API Design](https://restfulapi.net/)
- [Workspace Rules](../../../../README.md)
- [ARCHITECTURE.md](../../ARCHITECTURE.md)

**Контекст игры:**
- **Год:** 2020 (начало игры)
- **Место:** Night City, Downtown (корпоративный центр)
- **Сеттинг:** Cyberpunk 2077
- **Тип игры:** MMORPG с элементами шутера и экстрактшутера
- **MVP:** Текстовая веб-версия

---

## 📊 История выполнения

- `2025-11-06 15:30` - Задание создано
- `YYYY-MM-DD HH:MM` - Назначено агенту
- `YYYY-MM-DD HH:MM` - Начато выполнение
- `YYYY-MM-DD HH:MM` - Завершено

---

**ВНИМАНИЕ:** Это задание для AI агента Cursor. Выполняй его пошагово, следуя всем инструкциям выше. После завершения ОБЯЗАТЕЛЬНО сделай коммит изменений.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

