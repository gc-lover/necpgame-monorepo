# Task ID: API-TASK-313
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 09:44  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-285, API-TASK-286, API-TASK-168

---

## 📋 Краткое описание

Создать спецификацию `dialogue-quest-fq-arasaka-001` для фракционного квеста Arasaka «Токийская штаб-квартира»: описать API выдачи сцен, проверок и исходов международной миссии с вариантами лояльности, предательства и двойной игры.

**Что нужно сделать:** разработать OpenAPI файл `api/v1/narrative/dialogues/quests/fq-arasaka-001.yaml`, обеспечив управление состояниями (briefing, transit, temptation, extraction), проверками Persuasion/Stealth/Hacking/Insight и реакциями на корпоративные события.

---

## 🎯 Цель задания

Зафиксировать в contract-first подходе фракционный диалог Arasaka, чтобы:
- narrative-service управлял сложными ветвлениями (лояльность, вскрытие кейса, работа на Militech);
- gameplay-service мог симулировать высокие DC проверки и учитывать модификаторы (gear, флаги, reputation);
- world-service реагировал на события (`corporate_war_escalation`, `militech_contact`, глобальный транзит);
- фронтенд модуль `modules/narrative/quests/arasaka` отображал сценографию, HUD-подсказки и последствия выбора;
- аналитика отслеживала лояльность игроков и влияние на международную репутацию.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/04-narrative/dialogues/quest-faction-arasaka-001-tokyo.md`  
**Версия:** 1.1.0  
**Дата обновления:** 2025-11-07 19:32  
**Статус:** approved  

**Что важно:**
- Состояния: `briefing`, `transit`, `temptation`, `extraction`.
- Диалоговые узлы с YAML-псевдокодом, включая варианты лояльности/предательства.
- Проверки: Persuasion, Stealth, Hacking, Insight (используем shooter skill hooks, см. обновление `.BRAIN/04-narrative/quest-system.md`).
- Флаги (`flag.fqara001.*`), репутации (`corp_arasaka`, `militech`, `street`, `law`), награды и штрафы.
- Реакции событий: `world.event.corporate_war_escalation`, `flag.militech.arasaka_contact`.

### Дополнительные источники

- `.BRAIN/04-narrative/quests/faction-world/arasaka-world-quests.md`
- `.BRAIN/04-narrative/dialogues/npc-hiroshi-tanaka.md`
- `.BRAIN/04-narrative/dialogues/npc-james-iron-reed.md`
- `.BRAIN/04-narrative/dialogues/npc-kaede-ishikawa.md`
- `.BRAIN/04-narrative/quest-skill-challenges.md`
- `.BRAIN/02-gameplay/world/events/live-events-system.md`
- `.BRAIN/02-gameplay/social/reputation-formulas.md`

### Связанные задания

- NPC диалоги (Hiroshi, James Iron Reed) — `API-TASK-285`, `API-TASK-287`.
- Arasaka world quest пакет — `API-TASK-168`.
- Dialogue audit — `API-TASK-311`.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/narrative/dialogues/quests/fq-arasaka-001.yaml`  
**API версия:** v1  
**Файл новый** (создать; при превышении 400 строк вынести схемы в `components/narrative/factions/arasaka/`).

```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            └── dialogues/
                ├── quests/
                │   ├── main-001-first-steps.yaml
                │   └── fq-arasaka-001.yaml   ← создать
                └── npc-hiroshi-tanaka.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** narrative-service  
- **Порт:** 8087  
- **API Base:** `/api/v1/narrative/dialogues/quests/*`  
- **Интеграции:** gameplay-service (проверки), world-service (корпоративные события, международные рейсы), social-service (репутации corp/militech/street/law), economy-service (награды).

### Frontend
- **Модуль:** `modules/narrative/quests/arasaka`  
- **State Store:** `useNarrativeStore` (`quests.arasaka.fq001`)  
- **UI (@shared/ui):** `DialogViewport`, `BranchTree`, `IntelLog`, `StatusPill`, `Timeline`, `Tooltip`  
- **Forms (@shared/forms):** `OutcomePickerForm`, `RiskAssessmentForm`, `EventToggleForm`  
- **Layouts (@shared/layouts):** `GameLayout`, `MissionBriefingLayout`  
- **Hooks (@shared/hooks):** `useRealtime`, `useQuestFlags`, `useEventFeed`, `useDebounce`

### Комментарий для спецификации
Начало файла должно содержать:
```
# Target Architecture:
# - Microservice: narrative-service (port 8087)
# - Frontend Module: modules/narrative/quests/arasaka
# - State: useNarrativeStore(quests.arasaka.fq001)
# - UI: @shared/ui (DialogViewport, BranchTree, IntelLog, StatusPill, Timeline, Tooltip)
# - Forms: @shared/forms (OutcomePickerForm, RiskAssessmentForm, EventToggleForm)
# - Layouts: @shared/layouts (GameLayout, MissionBriefingLayout)
# - Hooks: @shared/hooks (useRealtime, useQuestFlags, useEventFeed, useDebounce)
# - Related Services: gameplay-service, world-service, social-service, economy-service
# - API Base: /api/v1/narrative/dialogues/quests/*
```

---

## ✅ План работ

1. **Анализ документа** — выделить сцены, узлы, checks, события, флаги, репутации, награды.  
   _Выход:_ таблица сущностей + mapping флагов/событий.
2. **Схемы** — описать `FactionQuestDialogue`, `DialogueNode`, `BranchOption`, `CheckDefinition`, `OutcomeBundle`, `WorldReaction`, `ReputationImpact`, `RiskModifier`.  
   _Выход:_ дизайн моделей в `components`.
3. **Endpoints** — минимум:
   - GET `quests/fq-arasaka-001` — сценарий;
   - POST `quests/fq-arasaka-001/checks/simulate` — симуляция;
   - POST `quests/fq-arasaka-001/progress` — обновление прогресса;
   - GET `quests/fq-arasaka-001/intel` — список улик/досье (опционально);
   - POST `quests/fq-arasaka-001/events/apply` — применение корпоративных событий.
4. **Ошибки и повторное использование** — привязать `shared/common/responses.yaml`, `shared/common/pagination.yaml` (для истории/интел).
5. **Примеры** — success/failure/critical outcomes (лояльность, предательство, двойная игра), события (`corporate_war_escalation`).
6. **Валидация** — прогнать линтер, проверить ≤400 строк (иначе вынести components), удостовериться в корректности комментария.

---

## 🔗 Эндпоинты (рекомендации)

1. **GET `/api/v1/narrative/dialogues/quests/fq-arasaka-001`**  
   Возвращает `FactionQuestDialogue` (состояния, узлы, проверки, события, награды, зависимости).  
   Query-параметры: `includeChecks`, `includeWorldReactions`, `reputationContext`, `gear[]`.

2. **POST `/api/v1/narrative/dialogues/quests/fq-arasaka-001/checks/simulate`**  
   Тело: `FactionCheckSimulationRequest` (nodeId, optionId, stat, modifiers, gear, eventContext).  
   Ответ: `FactionCheckSimulationResult`.

3. **POST `/api/v1/narrative/dialogues/quests/fq-arasaka-001/progress`**  
   Тело: `FactionProgressUpdateRequest`.  
   Ответ: `FactionProgressUpdateResponse` (следующее состояние, флаги, reputation, triggers, rewards, branchRedirects).

4. **GET `/api/v1/narrative/dialogues/quests/fq-arasaka-001/intel`** *(опционально)*  
   Пагинация (`shared/common/pagination.yaml`), возвращает список добытой информации/артефактов.

5. **POST `/api/v1/narrative/dialogues/quests/fq-arasaka-001/events/apply`** *(опционально)*  
   Применяет или откатывает глобальные события (например `corporate_war_escalation`).

Все ответы должны использовать `$ref` на единые ошибки (400/401/403/404/409/422/500).

---

## 🧱 Модели (минимум)

- `FactionQuestDialogue`
  - `questId`, `title`, `description`, `difficulty`, `recommendedLevel`
  - `states` (array `DialogueState`)
  - `nodes` (array `DialogueNode`)
  - `checks` (array `CheckDefinition`)
  - `worldReactions` (array `WorldReaction`)
  - `riskMatrix` (array `RiskModifier`)
  - `defaultFlags`, `reputationBaseline`, `rewards`, `lastUpdatedAt`

- `DialogueNode`
  - `nodeId`, `label`, `speakerOrder`, `lines`, `options`, `intelGain`, `hudHints`

- `BranchOption`
  - `optionId`, `text`, `checkId`, `outcomes` (`OutcomeBundle`), `nextNodeId`, `requiresFlags`, `forbiddenFlags`

- `CheckDefinition`
  - `checkId`, `stat` (enum: Persuasion, Stealth, Hacking, Insight), `dc`, `timerSeconds`, `modifiers`
  - Outcomes: success/failure/critical (via `OutcomeBundle`)

- `OutcomeBundle`
  - `setFlags`, `clearFlags`, `reputationChanges` (`ReputationImpact[]`), `rewards`, `triggers`, `hud`, `telemetry`, `branchRedirect`

- `WorldReaction`
  - `eventId`, `description`, `effects` (`WorldReactionEffect`), `dcAdjustments`, `securityLevel`

- `FactionProgressUpdateRequest/Response`, `FactionCheckSimulationRequest/Result`, `IntelEntry`, `RiskModifier`, `RewardReference`

Все схемы снабдить примерами (лояльность, вскрытие, двойная игра).

---

## 📐 Принципы

- OpenAPI 3.0.3; использовать `$ref` для общих ошибок/пагинации.
- Придерживаться SOLID/DRY/KISS, выносить повторные структуры.
- Не дублировать `ReputationImpact` и другие уже существующие общие компоненты (если есть) — иначе создать в components.
- Следить за ограничением 400 строк.
- Не хардкодить фактические значения — только структуры и правила.

---

## 📊 Критерии приемки

1. Файл `api/v1/narrative/dialogues/quests/fq-arasaka-001.yaml` создан, валиден для OpenAPI 3.0.3.
2. В начале файла присутствует комментарий с целевой архитектурой.
3. Документированы минимум три эндпоинта (GET сценария, POST симуляции, POST прогресса) + описаны дополнительные при необходимости.
4. Все состояния (`briefing`, `transit`, `temptation`, `extraction`) представлены в схемах и примерах.
5. Проверки (Persuasion/Stealth/Hacking/Insight) имеют исходы success/failure/critical.
6. Реакции на события (`corporate_war_escalation`, `militech_contact`) оформлены и подключены к API.
7. Репутации и награды описаны в моделях; предусмотрены поля для двойной игры и предательства.
8. Ошибки подключены через `shared/common/responses.yaml`, история/интел — через `shared/common/pagination.yaml`.
9. Примеры покрывают лояльность, предательство, двойную игру, критические исходы.
10. Спецификация проходит линтер без предупреждений.
11. Обновлены `brain-mapping.yaml` и `.BRAIN` документ (секция `API Tasks Status`).

---

## ❓ FAQ

- **Нужно ли описывать международные логистические этапы?**  
  Да, как `WorldReaction`/`RiskModifier` (transit, security level).

- **Как учитывать предательство Militech?**  
  Добавьте флаг `flag.militech.arasaka_contact` и outcomes с отрицательной репутацией Arasaka/положительной Militech.

- **Предусматривать ли GraphQL/Streaming?**  
  Нет, REST достаточно; telemetry можно указать как часть `OutcomeBundle.telemetry`.

- **Как обрабатывать критический провал в транзите?**  
  Через outcomes, возвращающие `branchRedirect` (например, аварийный сценарий) и `WorldReactionEffect`.

- **Можно ли расширять список событий?**  
  Да, предусмотреть enum или массив `additionalEvents` для будущих апдейтов.

- **Как работает Intel лог?**  
  Возвращается массив `IntelEntry` (id, source, description, timestamp), пагинация обязательна.

---

API зафиксирует сложный международный квест Arasaka и позволит командам последовательно реализовать механику двойной игры и корпоративных конфликтов.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

