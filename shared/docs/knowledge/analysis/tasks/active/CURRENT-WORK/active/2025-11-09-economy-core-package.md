# Подготовка пакета для Economy Core

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:35  
**Связанные документы:**  
- `.BRAIN/05-technical/backend/trade-system.md`  
- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`

---

## Прогресс
- Перепроверены `trade-system.md` и `inventory-system/part1-core-system.md` — подтверждён статус `ready`, указаны каталоги `api/v1/economy/trade/trade-system.yaml` и `api/v1/inventory/inventory-core.yaml`, фронтенд модули `modules/economy/trade` и `modules/economy/inventory`.
- Уточнены зависимости: взаимодействие economy-service с inventory-service, character-service, world-service и аналитикой; события `trade:started/completed/cancelled`, интеграция с transfer предметов.
- Сверены записи в `ready.md` и `readiness-tracker.yaml` — приоритет high, актуализирована дата проверки 2025-11-09 01:30.

## Задачи для брифа
- REST-контракты P2P трейда: создание/обновление trade session, подтверждение, отмена, аудит.
- REST-контракты инвентаря: CRUD по слотам, перенос предметов, проверка веса/лимитов, банк/стэш.
- Event Bus контракты и antifraud: события трейда, проверки расстояния, блокировка предметов.
- Справочники ограничений (bind rules, категории предметов) и связь с имплантами/квестами.

## Блокеры
- Требуется согласование окна для economy-service перед передачей ДУАПИТАСК; до подтверждения API-SWAGGER не трогаем.

## Следующие действия
1. Сформировать бриф ДУАПИТАСК: оценка трудозатрат, разбиение задач (trade REST, inventory REST, events/antifraud, справочники).
2. Подготовить список связанных документов (Part 2 inventory, mail/auction зависимости) и отметить их статус (при необходимости — `needs-work`).
3. Обновить `current-status.md` и `TODO.md`, синхронизировать `readiness-tracker.yaml` при появлении новых зависимостей.
# Подготовка пакета для economy core (inventory + trade)

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:30  
**Связанные документы:** `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/05-technical/backend/trade-system.md`

---

## Прогресс
- Инвентарь (`inventory-system/part1-core-system.md`) перепроверен 2025-11-09 01:30: подтверждены CRUD, equipment, weight, stash; каталог `api/v1/inventory/inventory-core.yaml`, фронтенд `modules/economy/inventory`.
- Торговля (`trade-system.md`) перепроверена 2025-11-09 01:30: подтверждены double-confirmation flow, антифрод, события; каталог `api/v1/trade/trade-system.yaml`, фронтенд `modules/economy/trade`.
- Уточнены зависимости: inventory-service (economy), character-service, world-service (distance), analytics-service (trade telemetry), mail/auction интеграции.

## Требования к пакетам задач (черновик)
- **Inventory REST:** `/inventory/slots`, `/inventory/items`, `/inventory/equipment`, `/inventory/stash`, `/inventory/transfer`.
- **Inventory Events:** `inventory:item-added`, `inventory:item-removed`, `inventory:weight-updated`.
- **Trade REST:** `/trade/sessions`, `/trade/sessions/{sessionId}/offers`, `/trade/sessions/{sessionId}/confirm`, `/trade/history`.
- **Trade Events:** `trade:started`, `trade:confirmation-requested`, `trade:completed`, `trade:cancelled`, `trade:fraud-flagged`.
- **Storage:** таблицы `inventories`, `inventory_items`, `equipment_slots`, `trade_sessions`, `trade_audit`.

## Следующие действия
1. Разложить inventory/trade на отдельные задачи для ДУАПИТАСК (REST, events, схемы БД).
2. Зафиксировать зависимости с economy-service (порт 8085) и фронтендом (`modules/economy/*`) в итоговом брифе.
3. Обновить `TODO.md` и `current-status.md` после подготовки пакета; ожидать разрешения на передачу в API-SWAGGER.

---

## Резюме контрактов и этапов (2025-11-09 13:40)

### REST — inventory-service
- `POST /inventory/slots` — инициализация инвентаря персонажа (лимиты, тип слота); блокирует остальные операции, требует валидации подписки/персонажа.
- `GET/PUT /inventory/items/{itemId}` — чтение и обновление свойств предмета (прочность, привязка, состояние); включает проверки bind rules.
- `POST /inventory/transfer` — массовое перемещение предметов между контейнерами (персонаж ↔ stash ↔ банк); учитывает вес, расстояние, антифрод.
- `POST /inventory/equipment/{slot}/equip|unequip` — управление экипировкой, триггерит recalculation атрибутов и интеграцию с progression.
- `GET /inventory/stash` / `POST /inventory/stash/expand` — операции с персональным хранилищем, требуют экономики (стоимость расширения).

### REST — trade-system
- `POST /trade/sessions` — создаёт P2P trade session (участники, валюты, ограничения); проверяет дистанцию и доступность предметов.
- `POST /trade/sessions/{sessionId}/offers` — обновляет офферы сторон, поддерживает частичное редактирование и логирует аудит.
- `POST /trade/sessions/{sessionId}/confirm` (+ `/decline`) — double-confirmation, синхронизация состояния предметов/валют.
- `DELETE /trade/sessions/{sessionId}` — корректное завершение/отмена сделки, фиксирует причины и штрафы.
- `GET /trade/history` — выдаёт историю сделок с фильтрами (период, контрагент), подключает analytics.

### Event Bus
- `inventory:item-added` / `inventory:item-removed` — рассылаются после CRUD/transfer; содержат `characterId`, `itemId`, источник операции.
- `inventory:weight-updated` — срабатывает при изменении веса; потребители — movement, encumbrance, analytics.
- `trade:started` / `trade:confirmation-requested` / `trade:completed` / `trade:cancelled` — отражают весь цикл сделки и служат для UI/антифрода.
- `trade:fraud-flagged` — генерируется antifraud модулем (дистанция, подозрительные предметы); потребители — security-service, analytics.

### Storage
- `inventories`, `inventory_items`, `inventory_slots` — схемы распределения предметов (UUID, bind, weight, durability).
- `equipment_slots`, `equipment_history` — история экипировки, поддержка отката/аудита.
- `trade_sessions`, `trade_offers`, `trade_audit`, `trade_penalties` — полный лог сделок, штрафов и подозрений.

### Этапность
- **Stage P0:** базовые REST (`/inventory/slots`, `/trade/sessions`) + события `inventory:item-*`, `trade:started` — необходимы для всего остального.
- **Stage P1:** операции transfer/equip/confirm + вес и anti-fraud (`inventory:weight-updated`, `trade:confirmation-requested`, `trade:fraud-flagged`).
- **Stage P2:** история, penalties, расширения stash, аналитические выборки; можно запускать после стабилизации P0/P1.

### Зависимости
- Economy-service (8085) как основной хост REST/Events.
- Inventory-service тесно связан с character-service (валидация персонажа) и world-service (дистанции/зоны).
- Analytics-service получает все trade/inventory события; mail/auction-house зависят от history API.
- Frontend модули `modules/economy/inventory` и `modules/economy/trade` должны использовать одни и те же словари ограничений.

---

## История
- 2025-11-09 01:30 — перепроверены `trade-system.md` и `inventory-system/part1-core-system.md`, обновлены статусы и каталоги.
- 2025-11-09 13:40 — подготовлено резюме REST/Event/Storage контрактов и этапов для быстрой постановки задач.