# Подготовка пакета для quest engine backend

**Приоритет:** critical  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:05  
**Связанные документы:** .BRAIN/05-technical/backend/quest-engine-backend.md, .BRAIN/04-narrative/quest-system.md, .BRAIN/02-gameplay/combat/combat-shooter-core.md (готовится взамен D&D)

---

## Прогресс
- Перепроверен `.BRAIN/05-technical/backend/quest-engine-backend.md`: статус `approved`, `api-readiness: ready`, целевой каталог `api/v1/gameplay/quests/quest-engine.yaml`, фронтенд `modules/quests`.
- Связаны опорные материалы по проверкам навыков без кубиков (`combat-shooter-core.md`) и структуре квестов (`quest-system.md`) для ссылок в задачах.
- Сформирована матрица зависимостей: character-service (статы и флаги), economy-service (награды), social-service (репутация), combat-service (килл-триггеры), analytics-service (телеметрия skill tests).
- Зафиксирован набор событий Event Bus: `quest:started`, `quest:objective-completed`, `quest:completed`, `quest:failed`; входящие `combat:enemy-killed`, `item:collected`, `npc:talked`.
- Выписаны REST точки для постановки задач: `POST /api/v1/quests/start`, `GET /api/v1/quests/active`, `POST /api/v1/quests/{id}/dialogue/choice`, `GET /api/v1/quests/{id}/dialogue/current`, `POST /api/v1/quests/{id}/complete`, `POST /api/v1/quests/{id}/abandon`.
- Зафиксированы основные сущности хранения: `quest_instances`, `dialogue_state`, `skill_check_results` (связь с персонажем, состояние диалога, результаты проверок).
- Обновлена очередь `ready.md`: карточка с путём `.BRAIN/05-technical/backend/quest-engine-backend.md` содержит таргет `api/v1/gameplay/quests/quest-engine.yaml` и задание на подготовку пакета для ДУАПИТАСК.

## Резюме для передачи
- **Каталог OpenAPI:** `api/v1/gameplay/quests/quest-engine.yaml` (микросервис `gameplay-service`, порт 8083).
- **Фронтенд:** `modules/quests`.
- **Статус:** `ready`, проверка 2025-11-09 01:03, блокирующих TODO нет.
- **Контекст:** REST, WebSocket, Event Bus, хранилище и зависимости перечислены выше; опорные документы `combat-shooter-core.md`, `quest-system.md`, `quest-engine-backend.md` синхронизированы.
- **Следующий шаг:** подготовить бриф для ДУАПИТАСК (без запуска в API-SWAGGER) и по готовности обновить `ready.md`, `readiness-tracker.yaml`, `current-status.md`.

## Блокеры
- Нет.

## Следующие действия
- Подготовить для ДУАПИТАСК сводку REST/WS/Events + хранение (таблицы, JSON поля) в формате брифа.
- Свести зависимости в отдельный блок (character/economy/social/combat/analytics) и обозначить требования к API контрактам для каждой связки.
- Подготовить приложение с перечнем готовых документов (`combat-shooter-core`, `quest-system`, `quest-engine-backend`) и их версиями.
- Отметить прогресс в `implementation-tracker.yaml` после получения окна на постановку задач.

---

## Бриф (черновик для ДУАПИТАСК) — 2025-11-09 14:05

### REST
- `POST /api/v1/quests/start` — старт квеста (проверка условий, создание `quest_instance`, публикация `quest:started`).
- `GET /api/v1/quests/active` — список активных квестов персонажа, возвращает текущие цели, прогресс и скрытые флаги.
- `GET /api/v1/quests/{questInstanceId}` — детальный статус (этапы, ветвления, доступные выборы, таймеры).
- `POST /api/v1/quests/{questInstanceId}/objective/{objectiveId}/complete` — ручное завершение цели (с учётом входящих событий и проверок).
- `POST /api/v1/quests/{questInstanceId}/dialogue/choice` — выбор ветви диалога; запуск skill test при необходимости, добавление записи в `dialogue_state`.
- `POST /api/v1/quests/{questInstanceId}/complete` — финализация (награды, обновление репутации, события `quest:completed`).
- `POST /api/v1/quests/{questInstanceId}/fail` / `/abandon` — обработка провала или отказа, фиксация в истории, публикация `quest:failed`.
- `POST /api/v1/quests/{questInstanceId}/flags` — принудительное обновление флагов (для GM/скриптов).
- `POST /api/v1/quests/{questInstanceId}/skill-test` — ручной вызов проверки навыка (использует shooter-параметры персонажа, без кубиков).

### WebSocket / Streaming
- `wss://api.necp.game/v1/quests/{questInstanceId}` — push обновлений по конкретной ветке (новые цели, состояние диалога, результаты проверок).
- `wss://api.necp.game/v1/quests/player/{characterId}` — общая лента изменений для клиента UI (переходы, таймеры, уведомления).

### Event Bus
- Исходящие: `quest:started`, `quest:objective-progress`, `quest:objective-completed`, `quest:completed`, `quest:failed`, `quest:dialogue-choice`, `quest:skill-test`.
- Входящие: `combat:enemy-killed`, `combat:session-events`, `inventory:item-collected`, `economy:transaction`, `social:reputation-changed`, `world:phase-updated`.
- Все события несут `characterId`, `questInstanceId`, `questId`, контекст (objectiveId, branchId, skillTestId).

### Storage
- `quest_instances` — `questInstanceId`, `questId`, `characterId`, состояние (stage, branch, expiration, flags JSONB).
- `quest_objectives_state` — прогресс по целям (counts, timers, success/failure).
- `dialogue_state` — активные узлы, доступные выборы, история; JSONB `variables`.
- `skill_test_results` — профиль проверок (`testType`, `difficulty`, `threshold`, `score`, `outcome`).
- `quest_rewards_history` — выданные награды (credits, items, reputation).
- `quest_dependencies_cache` — кеш условий (комбо с shooter-системами, economy, social).

### Зависимости
- `character-service`: доступ к статам, флагам, слоты персонажа.
- `economy-service`: расчет и выдача наград, списание ресурсов.
- `social-service`: репутация, отношения NPC.
- `combat-service`: килл-триггеры, события raid/arena.
- `analytics-service`: телеметрия ветвлений, успешность skill tests.
- `auth/characters package`: использование событий `CharacterCreated`, активный персонаж.
- `quest-branching schema`: JSON структуры `dialogue_nodes`, `dialogue_choices`, `skill_tests` (обновлены миграциями).

### Требования / ограничения
- Поддержка проверок навыков на основе shooter-атрибутов (`accuracy`, `tech_mastery`, `influence`) с возможностью повторной попытки через перки/импланты.
- Механизм таймеров (objective deadlines) — cron/worker или Redis ключи + события `quest:timer-expired`.
- Версионность сценариев: каждое `questId` содержит `revision`, клиент должен получать схему, соответствующую instance.
- Логи безопасности: любые GM операции (`flags`, `abandon`, `complete`) пишутся в `quest_audit`.
- Локализация диалогов — хранить ключи, сами тексты в локализационном сервисе.
- Масштабирование: предполагается шардирование `quest_instances` по `characterId`.

### Приложение — источники и версии
- `quest-engine-backend.md` — v1.0.0 (ready).
- `combat-shooter-core.md` — v0.1.0 (in work; заменяет архивный D&D документ).
- `quest-system.md` — v1.1.0 (ready; описывает narrative структуру и шаблоны).
- `2025-11-06-quest-branching-database-design.md` + Liquibase v1 — подтверждают схему БД (JSONB, triggers, RLS).

---

## История
- 2025-11-09 01:05 — перепроверен `quest-engine-backend.md`, зафиксированы REST точки и базовые события.
- 2025-11-09 14:05 — подготовлен бриф для ДУАПИТАСК (REST/WS/Events/Storage/зависимости).

