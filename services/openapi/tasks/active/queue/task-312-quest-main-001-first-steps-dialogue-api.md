# Task ID: API-TASK-312
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 09:35  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-285, API-TASK-289, API-TASK-294

---

## 📋 Краткое описание

Создать спецификацию `dialogue-quest-main-001-first-steps` для основного квеста «Первые шаги»: описать API получения диалоговых узлов, проверок D&D, реакций на события мира и обновления прогресса.

**Что нужно сделать:** спроектировать `api/v1/narrative/dialogues/quests/main-001-first-steps.yaml`, чтобы narrative-service мог управлять сценариями, флагами и проверками стартового квеста, а фронтенд — отображать ветвления Марко Санчеса и обучающие подсказки.

---

## 🎯 Цель задания

Обеспечить централизованное управление диалогами первой главы:
- синхронизация сцен обучения (arrival, market-run, tutorial-hud, fixer-brief, stealth-route, tech-door, wrap-up);
- экспонирование проверок D&D и условий (Perception, Parkour, Communication, Stealth, Tech) с модификаторами и таймерами;
- учёт реакций на мировые события (`metro_shutdown`, `blackwall_breach`, `solar_flare_2075`);
- управление репутационными и флаговыми исходами (corp/street/freelance ветки, tutorial flags);
- корректная интеграция с последующими квестами (`002-choose-path`) и диалогами NPC (Марко Санчес, Джейк Арчер, Рита Морено).

**Зачем это нужно:**
- Gameplay получает machine-readable сценарий квеста 1.1 для валидаторов и телеметрии.
- Narrative/UI получают единый контракт для отображения подсказок, HUD-журналов и выборов.
- QA может симулировать проверки и события без запуска серверов.
- Службы репутаций и событий знают, какие флаги и события задействованы на старте.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/04-narrative/dialogues/quest-main-001-first-steps.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 09:26  
**Статус документа:** approved  

**Что важно из этого документа:**
- Полный набор сцен и состояний (arrival → wrap-up) с псевдо-YAML узлами.
- Таблица проверок D&D, модификаторы, таймеры и исходы (success, failure, critical).
- Связь с событиями мира и UI подсказками.
- Флаги (`flag.main001.*`), репутации (`rep.fixers.marco`, `rep.street`, `rep.corp`) и награды.
- Ссылки на связанные NPC и последующие квесты.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/npc-marco-fix-sanchez.md`
- `.BRAIN/04-narrative/dialogues/npc-jake-archer-dialogue.md`
- `.BRAIN/04-narrative/quests/main/001-first-steps.md`
- `.BRAIN/04-narrative/quest-system.md`
- `.BRAIN/02-gameplay/social/reputation-formulas.md`
- `.BRAIN/02-gameplay/world/events/live-events-system.md`

### Связанные документы

- API задания на NPC (`API-TASK-285`, `API-TASK-289`).
- `API-TASK-294` (quest-choose-path) — зависимый следующий этап.
- `API-TASK-311` (dialogue audit) — агрегирует готовность квестовых диалогов.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/narrative/dialogues/quests/main-001-first-steps.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            └── dialogues/
                ├── npc-marco-fix-sanchez.yaml
                ├── npc-jake-archer.yaml
                ├── quest-main-001-first-steps.yaml ← создать/обновить
                └── ...
```

**Статус файла:** новый (создать). При необходимости вынести схемы в `components/` (≤ 400 строк на файл).

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)
- **Микросервис:** narrative-service  
- **Порт:** 8087  
- **API Base Path:** `/api/v1/narrative/dialogues/quests/*`  
- **Интеграции:**  
  - gameplay-service (проверки и таймеры)  
  - world-service (события `metro_shutdown`, `blackwall_breach`, `solar_flare_2075`)  
  - social-service (репутации street/corp/fixer/law)

### Frontend (модульная архитектура)
- **Модуль:** `modules/narrative/quests/main-001`  
- **State Store:** `useNarrativeStore` (`quests.main001`)  
- **UI (@shared/ui):** `DialogViewport`, `BranchTree`, `CheckOutcomeBanner`, `Badge`, `Tooltip`, `Timeline`  
- **Forms (@shared/forms):** `DialogueChoiceForm`, `CheckSimulationForm`, `EventToggleForm`  
- **Layouts (@shared/layouts):** `GameLayout`, `QuestSplitPanel`  
- **Hooks (@shared/hooks):** `useRealtime`, `useQuestFlags`, `useDebounce`

### Комментарий для API файла
Добавить вверху YAML:
```
# Target Architecture:
# - Microservice: narrative-service (port 8087)
# - Frontend Module: modules/narrative/quests/main-001
# - State: useNarrativeStore(quests.main001)
# - UI: @shared/ui (DialogViewport, BranchTree, CheckOutcomeBanner, Badge, Tooltip, Timeline)
# - Forms: @shared/forms (DialogueChoiceForm, CheckSimulationForm, EventToggleForm)
# - Layouts: @shared/layouts (GameLayout, QuestSplitPanel)
# - Hooks: @shared/hooks (useRealtime, useQuestFlags, useDebounce)
# - Related Services: gameplay-service, world-service, social-service
# - API Base: /api/v1/narrative/dialogues/quests/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Изучить исходник**: собрать список сцен, узлов, флагов, проверок, мировых реакций и репутаций.  
   _Результат:_ карта узлов и зависимостей (flags, reputation, events).
2. **Определить сущности и модели**: сформировать схемы `QuestDialogue`, `DialogueNode`, `DialogueOption`, `CheckDefinition`, `OutcomeBundle`, `WorldReaction`, `ReputationImpact`.  
   _Результат:_ таблица полей, типы, enum для состояний (`arrival`, `market-run`, ...).
3. **Спроектировать endpoints**: минимум три метода (получение сценария, симуляция проверки, обновление прогресса) плюс опционально событие/реплей.  
   _Результат:_ спецификация путей, параметры, тела запросов/ответов, статусы.
4. **Оформить компоненты**: вынести повторяющиеся структуры, связать ошибки через `shared/common/responses.yaml`, использовать пагинацию, где нужно (например, история выборов).  
   _Результат:_ корректные `$ref`, переиспользование компонентов.
5. **Подготовить примеры**: включить примеры JSON для ключевых сцен, проверок (Perception, Parkour, Tech) и реакций событий.  
   _Результат:_ секция `examples` покрывает success/failure/critical сценарии.
6. **Проверка**: прогнать линтер, убедиться в совместимости с предыдущими спецификациями narrative, добавить комментарий об архитектуре.  
   _Результат:_ валидный OpenAPI файл, готовый для codegen.

---

## 🔗 Endpoints (рекомендованные)

1. **GET `/api/v1/narrative/dialogues/quests/main-001`**
   - Возвращает полный сценарий (узлы, состояния, последовательности, зависимости).
   - Параметры:
     - `includeStates` (bool, default `true`)
     - `includeChecks` (bool, default `true`)
     - `events[]` (array of strings) — симулируемые активные события мира.
   - Ответ `200 OK` → `QuestDialogue`
   - Ошибки: `404` (квест не найден), `422` (некорректные события) через `shared/common/responses.yaml`.

2. **POST `/api/v1/narrative/dialogues/quests/main-001/checks/simulate`**
   - Симулирует конкретную проверку D&D с учётом модификаторов, gear и времени.
   - Тело: `CheckSimulationRequest` (nodeId, optionId, stat, modifiers, gear, eventContext).
   - Ответ `200 OK` → `CheckSimulationResult` (success/failure/critical, репутация, флаги, HUD).
   - Ошибки `404`, `409` (недоступный узел), `422` (отсутствуют обязательные параметры).

3. **POST `/api/v1/narrative/dialogues/quests/main-001/progress`**
   - Обновляет прогресс игрока и возвращает следующее состояние диалога.
   - Тело: `ProgressUpdateRequest` (currentState, chosenOptionId, checkOutcome, contextFlags).
   - Ответ `200 OK` → `ProgressUpdateResponse` (nextState, setFlags, reputationChanges, followUpNodes, questRedirects).
   - Ошибки `409` (ветка заблокирована), `422` (некорректные флаги).

4. **GET `/api/v1/narrative/dialogues/quests/main-001/history`** *(опционально)*  
   - Пагинированная история выборов/проверок для аналитики. Использовать `shared/common/pagination.yaml`.

5. **POST `/api/v1/narrative/dialogues/quests/main-001/events/apply`** *(опционально)*  
   - Применение либо откат мировых событий к сценарию (например `metro_shutdown`).

---

## 🧱 Модели данных (минимальный состав)

- `QuestDialogue`
  - `questId` (string, pattern `quest-main-001`)
  - `title` (string)
  - `states` (array of `DialogueState`)
  - `nodes` (array of `DialogueNode`, required)
  - `checks` (array of `CheckDefinition`)
  - `worldReactions` (array of `WorldReaction`)
  - `defaultFlags` (array of strings)
  - `lastUpdatedAt` (date-time)

- `DialogueState`
  - `id` (enum: `arrival`, `market-run`, `tutorial-hud`, `fixer-brief`, `stealth-route`, `tech-door`, `wrap-up`)
  - `description` (string)
  - `entryConditions` (array of `Condition`)
  - `linkedNodes` (array of nodeIds)

- `DialogueNode`
  - `id` (string)
  - `label` (string)
  - `speakerOrder` (array of strings)
  - `lines` (array of `DialogueLine`)
  - `options` (array of `DialogueOption`)
  - `hudHints` (array of `HudHint`)

- `DialogueOption`
  - `optionId` (string)
  - `text` (string)
  - `check` (`CheckReference`, nullable)
  - `outcomes` (`OutcomeBundle`)
  - `nextNodeId` (string, nullable)

- `CheckDefinition`
  - `checkId` (string, pattern like `arrival.arrival-curious`)
  - `stat` (enum: `Perception`, `Parkour`, `Communication`, `Stealth`, `Tech`)
  - `dc` (integer)
  - `timerSeconds` (integer, nullable)
  - `modifiers` (array of `Modifier`)
  - `successOutcome` (`OutcomeBundle`)
  - `failureOutcome` (`OutcomeBundle`)
  - `criticalSuccessOutcome` (`OutcomeBundle`)
  - `criticalFailureOutcome` (`OutcomeBundle`)

- `OutcomeBundle`
  - `setFlags` (array of strings)
  - `clearFlags` (array of strings)
  - `reputation` (array of `ReputationImpact`)
  - `rewards` (array of `RewardReference`)
  - `triggers` (array of `TriggerReference`)
  - `hud` (`HudEffect`, nullable)

- `WorldReaction`
  - `eventId` (enum: `world.event.metro_shutdown`, `world.event.blackwall_breach`, `world.event.solar_flare_2075`)
  - `description` (string)
  - `effects` (`WorldReactionEffect`)

- `CheckSimulationRequest`, `CheckSimulationResult`, `ProgressUpdateRequest`, `ProgressUpdateResponse` — подробные структуры с примерами (успех/провал/крит).

Все схемы снабдить `examples`, `required`, `enum` и ограничениями (`minItems`, `maxItems`).

---

## 📐 Принципы и правила

- Соблюдать SOLID / DRY / KISS; вынести повторяющиеся схемы в `components`.
- OpenAPI 3.0.3; использовать `$ref` на `shared/common/responses.yaml` и `shared/common/pagination.yaml`.
- Не дублировать модели репутаций/флагов, если уже есть общие компоненты.
- Лимит 400 строк — при необходимости вынести компоненты в `components/quests/main-001/*.yaml`.
- Не хардкодить данные — описывать структуры, значения должны приходить из БД/сервисов.

---

## 📊 Критерии приемки

1. Создан файл `api/v1/narrative/dialogues/quests/main-001-first-steps.yaml` (валидный OpenAPI 3.0.3).
2. В начале файла присутствует комментарий с целевой архитектурой.
3. Определены и задокументированы минимум три endpoints (GET сценария, POST симуляции, POST прогресса).
4. Все состояния (`arrival`, `market-run`, `tutorial-hud`, `fixer-brief`, `stealth-route`, `tech-door`, `wrap-up`) отражены в схемах.
5. Проверки D&D включены с модификаторами и исходами (success/failure/critical).
6. Реакции на события мира (`metro_shutdown`, `blackwall_breach`, `solar_flare_2075`) задокументированы и доступны через API.
7. Репутационные и флаговые изменения представлены в конечных моделях.
8. Все ошибки подключены через `shared/common/responses.yaml`; история использует `shared/common/pagination.yaml`, если реализована.
9. Добавлены примеры запросов/ответов (success, failure, critical) для симуляции и прогресса.
10. Спецификация проходит линтер без ошибок/варнингов.
11. `brain-mapping.yaml` и `.BRAIN` документ обновлены, статусы синхронизированы.

---

## ❓ FAQ

- **Нужен ли WebSocket/stream?**  
  Нет, достаточно REST. Реалтайм-пуши реализуются позже через `useRealtime`.

- **Как учитывать gear/классы?**  
  Через массив `modifiers` и `contextFlags` в запросах; backend сам рассчитывает итоговые бонусы.

- **Что делать с критическими исходами?**  
  Все критические исходы должны быть в `OutcomeBundle` и возвращаться в ответах симуляции/прогресса.

- **Как связать с `002-choose-path`?**  
  Возвращать `nextQuestId` в `ProgressUpdateResponse` при выборе wrap-up опций.

- **Можно ли пропустить опциональные события?**  
  Да, `events[]` не обязательны; backend применяет только признанные события, остальные игнорирует с предупреждением.

- **Как хранить HUD подсказки?**  
  Использовать структуру `HudEffect` с типом (`toast`, `timeline`, `replay`) и payload.

- **Поддерживается ли локализация?**  
  Добавить optional `localizationKey` в `DialogueLine`/`DialogueOption` для будущего расширения.

---

Эта спецификация зафиксирует ключевой стартовый квест и позволит командам геймплея, UI и narrative работать по единому контракту.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

