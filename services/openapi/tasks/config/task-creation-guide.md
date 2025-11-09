# Инструкция для AI создателя заданий

**Цель:** Создавать полные задания для агентов-исполнителей на основе документов из .BRAIN.

**Минимальный вход:**
1. ПРАВИЛО: `API-SWAGGER/.cursor/rules/api-task-creator-rules.mdc`
2. ИНСТРУКЦИЯ: Этот файл
3. ДОКУМЕНТ из .BRAIN: Путь к файлу с фичей для проработки

---

## Процесс работы

### Шаг 1: Анализ входных данных

**Что нужно сделать:**
1. Прочитай правило (`api-task-creator-rules.mdc`) - пойми свою роль и задачи
2. Прочитай этот файл (инструкцию) - пойми процесс работы
3. Прочитай документ из .BRAIN полностью - извлеки всю информацию

**Что важно понять из документа .BRAIN:**
- Концепции и механики, которые нужно отразить в API
- Бизнес-правила и ограничения
- Связанные документы и зависимости
- Примеры и вдохновение из источников
- Детали реализации (если есть)

---

### Шаг 2: Определение целевой структуры API и архитектуры

**Что нужно определить:**
1. Какой модуль/раздел API нужен? (gameplay, lore, narrative, social и т.д.)
2. Какая версия API? (v1, v2 и т.д.)
3. Какой путь к файлу? (api/v1/gameplay/combat/shooting.yaml и т.д.)
4. Какое имя файла? (основано на содержимом документа)
5. **⚠️ Целевой микросервис** (бекенд всегда реализуется через микросервис)
6. **⚠️ Целевой фронтенд-модуль** (для фронтенда)
7. **⚠️ Требуемые UI компоненты** (@shared/ui, @shared/forms)
8. Production URL `https://api.necp.game/v1` для всех OpenAPI servers
9. Полный запрет на использование сторонних доменов (staging/dev) в заданиях и спецификациях

**Правила именования:**
- Файлы: kebab-case (shooting.yaml, combat-mechanics.yaml)
- Директории: kebab-case (gameplay, combat)
- Модели в API: PascalCase (Weapon, Shot)
- Endpoints: kebab-case (/gameplay/combat/shoot)

**Определение микросервиса (для бекенда):**

На основе API path определи целевой микросервис:

| API Path | Микросервис | Порт |
|----------|-------------|------|
| /api/v1/auth/* | auth-service | 8081 |
| /api/v1/characters/* | character-service | 8082 |
| /api/v1/gameplay/combat/* | gameplay-service | 8083 |
| /api/v1/gameplay/progression/* | gameplay-service | 8083 |
| /api/v1/gameplay/social/* | social-service | 8084 |
| /api/v1/social/* | social-service | 8084 |
| /api/v1/gameplay/economy/* | economy-service | 8085 |
| /api/v1/gameplay/world/* | world-service | 8086 |
| /api/v1/lore/* | world-service | 8086 |
| /api/v1/narrative/* | narrative-service | 8087 |
| /api/v1/admin/* | admin-service | 8088 |

**Определение фронтенд-модуля:**

На основе категории API определи модуль:

| Категория | Фронтенд-модуль | State Store |
|-----------|-----------------|-------------|
| social | modules/social/ | useSocialStore |
| economy | modules/economy/ | useEconomyStore |
| combat | modules/combat/ | useCombatStore |
| world | modules/world/ | useWorldStore |
| progression | modules/progression/ | useProgressionStore |
| narrative | modules/narrative/ | useNarrativeStore |

**Определение UI компонентов:**

На основе типа API определи типичные компоненты:

- **Социальные:** PersonalNpcCard, FriendCard, GuildCard, NpcInteractionForm
- **Экономика:** AuctionCard, ItemCard, TradeForm, AuctionBidForm
- **Боевая:** WeaponCard, HealthBar, AbilityButton, WeaponConfigForm
- **Мир:** LocationCard, EventCard, MapView
- **Прогрессия:** SkillTree, LevelProgress, StatBlock
- **Нарратив:** QuestCard, DialogueBox, StoryPanel

**Структура директорий:**
```
api/v1/
├── gameplay/
│   ├── combat/
│   ├── progression/
│   ├── economy/
│   ├── social/
│   └── world/
├── lore/
│   ├── factions/
│   ├── locations/
│   └── characters/
└── narrative/
    └── quests/
```

**Шаг 2.5: Определение целевой архитектуры**

> Бекенд больше не поддерживает монолит — каждое API задание должно ссылаться на конкретный микросервис и корректно заполнять `info.x-microservice`. Убедись, что в секции `servers` указан только `https://api.necp.game/v1`.

**ОБЯЗАТЕЛЬНО определить для каждого API:**

1. **Целевой микросервис (Backend):**
   - Определи по API пути, куда пойдёт реализация
   - Укажи порт микросервиса
   - Укажи паттерн API путей

2. **Целевой фронтенд-модуль:**
   - Определи по домену функциональности
   - Укажи путь к модулю
   - Укажи state store

3. **Требуемые библиотеки компонентов:**
   - UI компоненты из @shared/ui
   - Готовые формы из @shared/forms
   - Layouts из @shared/layouts

**Таблица маппинга API путей → Микросервисы:**

| API Путь | Микросервис | Порт | Описание |
|----------|-------------|------|----------|
| `/api/v1/auth/*` | auth-service | 8081 | Аутентификация, аккаунты |
| `/api/v1/characters/*` | character-service | 8082 | Персонажи, управление |
| `/api/v1/gameplay/combat/*` | gameplay-service | 8083 | Боевая система, способности |
| `/api/v1/gameplay/progression/*` | gameplay-service | 8083 | Прокачка, навыки |
| `/api/v1/gameplay/social/*` | social-service | 8084 | Социальные механики, NPC |
| `/api/v1/social/*` | social-service | 8084 | Друзья, гильдии, чат |
| `/api/v1/gameplay/economy/*` | economy-service | 8085 | Экономика, торговля |
| `/api/v1/gameplay/world/*` | world-service | 8086 | Локации, события, мир |
| `/api/v1/world/*` | world-service | 8086 | Мировые события |
| `/api/v1/lore/*` | world-service | 8086 | Лор, фракции, вселенная |
| `/api/v1/narrative/*` | narrative-service | 8087 | Квесты, диалоги, сюжет |

**Таблица маппинга Домен → Фронтенд-модули:**

| Домен | Фронтенд-модуль | State Store | Типичные компоненты |
|-------|-----------------|-------------|---------------------|
| Social | modules/social/ | useSocialStore | PersonalNpcCard, FriendCard, GuildCard |
| Economy | modules/economy/ | useEconomyStore | AuctionCard, TradeForm, PriceDisplay |
| Combat | modules/combat/ | useCombatStore | WeaponCard, HealthBar, AbilityButton |
| World | modules/world/ | useWorldStore | LocationCard, EventCard, MapView |
| Progression | modules/progression/ | useProgressionStore | SkillTree, LevelProgress, StatBlock |
| Narrative | modules/narrative/ | useNarrativeStore | QuestCard, DialogueBox, StoryPanel |

**Пример определения целевой архитектуры:**

Для API: `api/v1/gameplay/social/personal-npc-tool.yaml`

**Backend:**
- Микросервис: social-service (порт 8084)
- API пути: /api/v1/gameplay/social/*

**Frontend:**
- Модуль: modules/social/personal-npc
- State: useSocialStore (personalNpcs state)
- UI компоненты: @shared/ui (PersonalNpcCard, ItemCard, HealthBar)
- Формы: @shared/forms (NpcInteractionForm, NpcHiringForm)
- Layouts: @shared/layouts (GameLayout)

---

### Шаг 3: Извлечение информации для задания

**Из документа .BRAIN нужно извлечь:**

1. **Концепции и механики:**
   - Основные концепции, которые нужно отразить в API
   - Механики игрового процесса
   - Правила и ограничения

2. **Endpoints (если можно определить):**
   - Какие действия нужно выполнять?
   - Какие данные нужно получать/изменять?
   - Какие операции нужны?

3. **Модели данных:**
   - Какие сущности упоминаются?
   - Какие свойства у этих сущностей?
   - Какие связи между сущностями?

4. **Бизнес-правила:**
   - Валидация данных
   - Ограничения использования
   - Условия применения

5. **Связанные документы:**
   - Документы, на которые ссылается текущий
   - Документы, которые зависят от текущего
   - Связанные концепции

**Если информации недостаточно:**
- Используй принципы проекта (SOLID, DRY, KISS)
- Используй стандарты OpenAPI 3.0.3
- Используй примеры из существующих API (если есть)
- Создай логичную структуру на основе концепций

---

### Шаг 4: Создание задания по шаблону

**Используй шаблон:** `tasks/templates/api-generation-task-template.md`

**Заполни все секции:**

1. **Метаданные:**
   - Task ID: API-TASK-XXX (уникальный номер)
   - Тип: API Generation
   - Приоритет: на основе важности фичи
   - Статус: queued
   - Зависимости: другие задания (если есть)

2. **Источники:**
   - Путь к документу .BRAIN
   - Версия документа (если есть)
   - Статус документа (draft, review, approved)
   - Связанные документы

3. **Целевая структура:**
   - Полный путь к файлу API
   - Версия API
   - Структура директории

4. **Детальный план:**
   - Шаг 1: Анализ исходного документа
   - Шаг 2: Определение API endpoints
   - Шаг 3: Определение моделей данных
   - Шаг 4: Создание OpenAPI спецификации
   - Шаг 5: Валидация и проверка

5. **Endpoints:**
   - Для каждого endpoint: метод, путь, назначение, параметры, ответы, примеры

6. **Модели данных:**
   - Для каждой модели: все поля, типы, валидация, примеры

7. **Принципы и правила:**
   - Принципы проекта (SOLID, DRY, KISS)
   - Стандарты OpenAPI
   - Правила из workspace rules

8. **Критерии приемки:**
   - Чеклист из 10+ пунктов
   - Каждый пункт проверяемый и измеримый

9. **FAQ:**
   - Ответы на типичные вопросы
   - Разрешение возможных проблем

---

### Шаг 5: Проверка по чеклисту

**Используй чеклист:** `tasks/config/checklist.md`

**Проверь:**
- Все обязательные секции заполнены
- Все блоки детализированы
- Задание самодостаточно
- Ссылки на документы корректны
- Примеры включены
- Критерии приемки измеримы

---

### Шаг 6: Сохранение задания

**Сохрани задание:**
- Путь: `tasks/active/queue/task-XXX-description.md`
- Имя файла: на основе описания задания
- Формат: Markdown

**Пример имени:**
- `task-001-shooting-api.md`
- `task-002-combat-abilities-api.md`
- `task-003-progression-api.md`

---

### Шаг 7: Обновление маппинга

**Обнови:** `tasks/config/brain-mapping.yaml`

**Добавь запись:**
```yaml
mappings:
  - source: ".BRAIN/02-gameplay/combat/shooter-mechanics.md"
    target: "api/v1/gameplay/combat/shooting.yaml"
    task_id: "API-TASK-001"
    task_file: "tasks/active/queue/task-001-shooting-api.md"
    status: "queued"
    created: "2025-11-02 15:30"
    version: "v1.0.0"
```

---

### Шаг 8: Обновление документа .BRAIN

**Добавь секцию в начало или конец документа .BRAIN:**

```markdown
---
**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-001: api/v1/gameplay/combat/shooting.yaml (2025-11-02)
- Last Updated: 2025-11-02 15:30
---
```

**Или в конец документа:**

```markdown
---

## API Tasks

**Status:** created
**Tasks:**
- [API-TASK-001](../API-SWAGGER/tasks/active/queue/task-001-shooting-api.md): api/v1/gameplay/combat/shooting.yaml
**Last Updated:** 2025-11-02 15:30
```

---

## Быстрый шаблон для создания задания

Если нужно быстро создать задание:

1. Прочитай документ .BRAIN
2. Определи: что нужно в API? (endpoints, модели)
3. Определи путь: `api/v1/.../filename.yaml`
4. Создай задание по шаблону с заполнением всех секций
5. Проверь по чеклисту
6. Сохрани в `tasks/active/queue/`
7. Обнови маппинг и документ .BRAIN

---

## Примеры хороших заданий

**Хорошее задание:**
- Детальное описание всех шагов
- Все endpoints описаны с примерами
- Все модели описаны с валидацией
- Критерии приемки измеримые
- FAQ отвечает на типичные вопросы

**Плохое задание:**
- Неполное описание
- Нет примеров
- Неясные критерии приемки
- Пропущены важные детали

---

## Частые ошибки

1. **Недостаточно деталей** - задание должно быть самодостаточным
2. **Нет примеров** - агент-исполнитель нуждается в примерах
3. **Неясные критерии** - критерии должны быть измеримыми
4. **Пропущены зависимости** - нужно указывать все зависимости
5. **Не обновлен маппинг** - обязательно обновлять brain-mapping.yaml

---

## Полезные ссылки

- [Шаблон задания](../templates/api-generation-task-template.md)
- [Чеклист проверки](./checklist.md)
- [Правила работы](../../.cursor/rules/api-task-creator-rules.mdc)
- [Маппинг .BRAIN -> задания](./brain-mapping.yaml)
- [OpenAPI Specification](https://swagger.io/specification/)
