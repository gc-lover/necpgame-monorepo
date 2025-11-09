# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-106  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 19:40  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-102 (economy-contracts), API-TASK-104 (economy-events), API-TASK-105 (economy-investments)  

## Summary
Создать спецификацию `api/v1/economy/crafting/crafting.yaml`, описывающую гибридную систему крафта: каталог рецептов, профессии, станции, прогресс, производство в несколько этапов, интеграции с инвентарём, экономическими событиями и фракциями.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-crafting.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:32) |

**Key points:** гибридный крафт (базовый доступен всем, продвинутый через профессии, сложное производство), типы крафта из лора Cyberpunk (кибервар, хакерские программы, модификации), источники рецептов (прокачка, лут, взлом), TODO на баланс.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-currencies-resources.md`, `.BRAIN/02-gameplay/economy/economy-inventory.md`, `.BRAIN/02-gameplay/combat/combat-implants.md`, `.BRAIN/02-gameplay/economy/economy-events.md`, `.BRAIN/05-technical/backend/economy-telemetry.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/crafting  
- **API directory:** `api/v1/economy/crafting/crafting.yaml`  
- **base-path:** `/api/v1/economy/crafting`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/crafting`  
- **Shared UI/Form components:** `@shared/ui/CraftingRecipeList`, `@shared/ui/CraftingWorkbench`, `@shared/ui/ProfessionProgress`, `@shared/ui/ProductionQueue`, `@shared/forms/CraftingStartForm`, `@shared/forms/RecipeUnlockForm`, `@shared/forms/ProductionCompleteForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для каталога рецептов, управления профессиями, крафтовых станций, планирования производства, получения результатов, разбора предметов и получения рецептов.
2. Смоделировать `CraftingRecipe`, `CraftingIngredient`, `CraftingProduct`, `CraftingProfession`, `CraftingStation`, `CraftingQueueItem`, `CraftingProgress`, `RecipeUnlock`, `ReverseEngineering`, `CraftingEvent`.
3. Учесть гибридную систему: базовый крафт (доступен всем), продвинутый (профессии, навыки), сложное производство (станции, время, кооп).
4. Документировать интеграции с inventory, economy-events (модификаторы), faction/quest (рецепты), anti-fraud, notification и telemetry сервисами.
5. Добавить требования к фронтенду, real-time обновлениям, прогресс-барам, уведомлениям о завершении и системе наград профессий.

## Endpoints
- `GET /recipes` — каталог рецептов (фильтры: тип, профессия, доступность, источник).
- `GET /recipes/{recipeId}` — подробности рецепта, требуемые ингредиенты, станции, время, источник.
- `POST /recipes/{recipeId}/unlock` — разблокировка рецепта (прокачка, покупка, взлом).
- `POST /crafting/start` — начать крафт (рецепт, количество, станция, модификаторы).
- `GET /crafting/queue` — текущие задания, прогресс, время до завершения.
- `POST /crafting/{queueId}/complete` — завершить крафт, выдать предметы, XP, побочные эффекты.
- `POST /crafting/{queueId}/cancel` — отменить, вернуть часть ресурсов.
- `GET /professions` — состояния профессий, уровни, XP, перки.
- `POST /professions/{professionId}/train` — тренировка профессии (использование XP, медиа).
- `POST /reverse-engineering` — разобрать предмет, получить рецепты/компоненты.
- `GET /stations` — доступные станции, местоположение, модификаторы, очередь.
- `POST /stations/{stationId}/upgrade` — улучшить станцию, открыть модификаторы.
- `GET /events/modifiers` — экономические/фракционные модификаторы, влияющие на крафт (скидки, ускорения).
- `GET /telemetry` — статистика крафта (успешность, популярность рецептов).
- WebSocket `ws://.../streams/crafting` — real-time прогресс, уведомления об окончании, кооперативный крафт.

## Data Models
- `CraftingRecipe` — `id`, `name`, `category`, `tier`, `difficulty`, `ingredients[]`, `products[]`, `requiredProfession`, `requiredStation`, `time`, `energy`, `unlockSources`, `metadata`.
- `CraftingIngredient` — `itemId`, `quantity`, `quality`, `consumed`.
- `CraftingProduct` — `itemId`, `quantity`, `quality`, `bindType`.
- `CraftingProfession` — `id`, `name`, `level`, `experience`, `perks`, `allowedRecipes`.
- `CraftingStation` — `id`, `type`, `location`, `modifiers`, `queueCapacity`, `owner`.
- `CraftingQueueItem` — `queueId`, `recipeId`, `playerId`, `stationId`, `status`, `startTime`, `endTime`, `progress`, `boosters`.
- `ReverseEngineeringRequest` — `itemId`, `stationId`, `goal`.
- `RecipeUnlock` — `recipeId`, `method`, `requirements`, `cost`.
- `CraftingEvent` — `eventType`, `modifier`, `duration`, `source`.
- Ошибки: `RecipeLockedError`, `InsufficientIngredientsError`, `StationUnavailableError`, `ProfessionLevelError`, `QueueFullError`, `ReverseEngineeringFailedError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.crafting.started`, `economy.crafting.completed`, `economy.crafting.cancelled`, `economy.crafting.recipe-unlocked`, `economy.crafting.reverse-engineered`.
- Inventory-service: списание ингредиентов, добавление продукции, проверка качества (`POST /inventory/allocate`, `POST /inventory/add`).
- Economy-events: модификаторы времени/стоимости (REST/WS).
- Faction/quest: открытие рецептов (`POST /factions/recipes`, `POST /quests/rewards`).
- Anti-fraud: проверка подозрительных массовых крафтов (`POST /anti-fraud/crafting/check`).
- Notification-service: уведомления о завершении, профессиях, новых рецептах (`POST /notifications/crafting`).
- Telemetry-service: метрики (`POST /analytics/crafting`).
- Rate limits: максимум 10 активных крафтов/персонажа, unlock ≤ 20/день, reverse engineering ≤ 5/день.
- Security: OAuth2 PlayerSession, проверка профессий, станций, прав владельца.

## Acceptance Criteria
1. Файл `api/v1/economy/crafting/crafting.yaml` создан, ≤ 500 строк, `info.x-microservice` → economy-service.
2. Задокументированы все endpoints, WebSocket поток, параметры, примеры, ошибки и ограничения гибридной системы.
3. Модели рецептов, ингредиентов, продуктов, профессий, станций, очереди, реверс-инжиниринга и событий оформлены с обязательными полями.
4. Kafka события и интеграции с inventory, events, factions, quests, anti-fraud, notification, telemetry описаны, указаны payload и retry.
5. Указаны требования к фронтенду (UI, формы, real-time), XP/прогресс профессий, уведомления.
6. Обновлён `tasks/config/brain-mapping.yaml` (API-TASK-106, статус `queued`, приоритет `high`).
7. `.BRAIN/02-gameplay/economy/economy-crafting.md` включает секцию `API Tasks Status`.
8. `tasks/queues/queued.md` пополнен записью.
9. После реализации запустить `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\crafting\`.

## FAQ / Notes
- **Как обрабатывать TODO по балансу?** Отметить в примечаниях, что числовые значения подлежат уточнению; API должно поддерживать конфигурацию.
- **Поддерживаются ли кооперативные крафты?** Заложить поле `participants[]` и синхронизацию через WebSocket.
- **Как учитывать станции guild?** Добавить поля `guildId`, `accessLevel`, `sharedQueue`.

## Change Log
- 2025-11-09 19:40 — Задание создано (API Task Creator Agent)


