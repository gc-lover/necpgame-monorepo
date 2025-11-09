# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-100  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 18:05  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-097 (character-management)  

## Summary
Создать спецификацию `api/v1/economy/inventory/inventory-core.yaml`, описывающую базовую систему инвентаря: хранение предметов, стеки, вес/перегрузку, экипировку, банк/тайник, операции подбора/снятия/перемещения, события и интеграции с другими сервисами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md` |
| Version | v1.0.1 |
| Status | approved |
| API readiness | ready (2025-11-09 01:30) |

**Key points:** инвентарь с лимитом слотов и весом; стекуемые предметы и авто-сплит overflow; оборудование с durability и слотами; банк/тайник; бинды (pickup/equip/account); логика доступа (trade, mail, auction); события и уведомления при операциях.  
**Related docs:** `.BRAIN/05-technical/backend/inventory-system/part2-advanced-features.md`, `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`, `.BRAIN/02-gameplay/economy/economy-contracts.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md`, `.BRAIN/05-technical/backend/notification-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/inventory  
- **API directory:** `api/v1/economy/inventory/inventory-core.yaml`  
- **base-path:** `/api/v1/economy/inventory`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/inventory`  
- **Shared UI/Form components:** `@shared/ui/InventoryGrid`, `@shared/ui/ItemTooltip`, `@shared/ui/WeightIndicator`, `@shared/forms/ItemTransferForm`, `@shared/forms/ItemSplitStackForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для управления инвентарём: получение состояния, перемещение предметов, подбор, удаление, экипировка, стойки банка/тайника.
2. Определить схемы данных: `InventoryState`, `InventorySlot`, `CharacterItem`, `ItemTemplate`, `PickupRequest/Response`, `TransferRequest`, `EquipRequest`, `DurabilityState`.
3. Зафиксировать валидации и ограничения: лимит слотов, вес, stack size, уникальные предметы, binding правила.
4. Добавить события и интеграции: уведомления, kafka topics, взаимодействие с character-service (slots), gameplay-service (combat restrictions), auction/trade/mail сервисами.
5. Включить метрики и аналитические поля (перегрузка, статистика предметов), требования фронтенда и Orval клиентов.

## Endpoints
- `GET /characters/{characterId}` — текущее состояние инвентаря (backpack, equipment, stash) с весом и слотами.
- `POST /characters/{characterId}/pickup` — подобрать предмет (stacking, вес, bind-on-pickup).
- `POST /characters/{characterId}/drop` — удалить/бросить предмет (обновление веса, логирование).
- `POST /characters/{characterId}/move` — переместить предмет (между слотами, в банк, между персонажами при trade).
- `POST /characters/{characterId}/split` — разделить стек на заданное количество.
- `POST /characters/{characterId}/equip` — экипировать предмет, проверить requirements и binding.
- `POST /characters/{characterId}/unequip` — снять предмет с переносом в инвентарь/банк.
- `POST /characters/{characterId}/stash/deposit` — переместить предмет в банк/тайник.
- `POST /characters/{characterId}/stash/withdraw` — забрать предмет из банка.
- `POST /characters/{characterId}/consumables/use` — использовать consumable (триггеры эффектов).
- `GET /characters/{characterId}/history` — аудит операций (pickup, drop, equip, trade).
- `POST /characters/{characterId}/weight/recalculate` — пересчитать вес/перегрузку при внешних изменениях.

## Data Models
- `InventoryState` — общее состояние инвентаря (`slots`, `maxSlots`, `currentWeight`, `maxWeight`, `overweight`, `stashSlots`, `equipmentSlots`).
- `InventorySlot` — описание слота (`storageType`, `slotIndex`, `item`, `locked`, `cooldown`).
- `CharacterItem` — объект предмета (`id`, `templateId`, `quantity`, `durability`, `bindType`, `isBound`, `modifiers`, `enchantments`, `acquiredFrom`).
- `ItemTemplate` — ссылка на шаблон (`itemType`, `subtype`, `rarity`, `stackable`, `maxStackSize`, `weight`, `requirements`, `stats`, `effects`).
- `PickupRequest`, `PickupResponse`, `MoveRequest`, `SplitRequest`, `EquipRequest`, `ConsumableUseRequest`.
- `DurabilityState` — текущая прочность и правила ремонта.
- `InventoryHistoryEntry` — запись аудита (`action`, `item`, `quantity`, `source`, `target`, `timestamp`, `metadata`).
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.inventory.updated`, `economy.inventory.overweight`, `economy.inventory.item-picked`, `economy.inventory.item-bound`; payload включает `characterId`, `item`, `action`, `metadata`.
- REST зависимости: `character-service` (валидация слотов, активный персонаж), `gameplay-service` (запрет операций в бою), `auction-service` / `trade-service` / `mail-service` (перенос предметов), `notification-service` (уведомления), `telemetry-service` (метрики).
- WebSocket/SignalR канал: `inventory.updates` для live-отображения.
- Метрики: `InventoryOverloadRate`, `ItemPickupRate`, `BoundItemRatio`, `EquipmentDurabilityAverage`.
- Security: OAuth2 PlayerSession, роль `player`, проверки `X-Session-Token`, антиспам (rate-limit на `pickup`, `move`).

## Acceptance Criteria
1. Спецификация `api/v1/economy/inventory/inventory-core.yaml` ≤ 500 строк, заполнен `info.x-microservice` для economy-service.
2. Все перечисленные endpoints документированы с входами/выходами, ошибками (`InventoryFull`, `Overweight`, `BindViolation`, `SlotLocked`).
3. Схемы данных включают binding, durability, stack, weight поля; есть примеры JSON (pickup, equipment, stash).
4. Kafka события и интеграции задокументированы; указаны продюсеры/консьюмеры, ключи и сценарии ретраев.
5. В спецификации описаны ограничения: макс. 50 слотов базово, peso, авто-бинды, уникальные предметы, правила банка.
6. Прописаны фронтенд требования: модуль, UI компоненты, Orval клиент `@api/economy/inventory`, обработка push-уведомлений.
7. Обновлён `tasks/config/brain-mapping.yaml` (запись API-TASK-100, статус `queued`, приоритет `critical`).
8. `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md` содержит секцию `API Tasks Status` с задачей.
9. `tasks/queues/queued.md` дополнен записью.
10. После реализации запуск `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\inventory\`.

## FAQ / Notes
- **Нужно ли включать Part 2?** Данная задача охватывает только Core; расширенные механики (crafting, sockets, loadouts) пойдут отдельным заданием.
- **Как обрабатывать trade/auction?** Указать зависимости и связь с соответствующими API, но без детализации логики — только контракт взаимодействия.
- **Что с удалением предметов?** Описать ошибки `is_deletable=false` и сценарии soft-delete (архив).
- **Как хранить историю?** Inventory history через `inventory_history` таблицу, выдавать пагинированно с фильтрами.

## Change Log
- 2025-11-09 18:05 — Задание создано (API Task Creator Agent)


