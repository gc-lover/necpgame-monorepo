# Очередь документов — статус `ready`
---

**Статус:** active  
**Версия:** 1.0.0  
**Последнее обновление:** 2025-11-09 09:55  

---

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 09:55  
**api-readiness-notes:** Перепроверено 2025-11-09 09:55: очередь служебная, используется для трекинга статуса ready и не формирует API задачи.

- Лимит файла: ≤ 500 строк. При превышении создайте `ready_0001.md`, `ready_0002.md`, указывая ссылку на продолжение.
- Формат записи:

```markdown
- **Документ:** .BRAIN/05-technical/backend/auth/README.md (v1.0.1)  
  **Проверено:** 2025-11-08 23:56 — Brain Manager | Следующий шаг: дождаться одобрения на постановку задач для auth-service.
```

- При изменении статуса переместите запись в соответствующий файл (`needs-work.md`, `blocked.md`, и т.д.) и обновите дату.

- **Документ:** .BRAIN/05-technical/backend/auth/README.md (v1.0.1)  
  **Проверено:** 2025-11-09 02:47 — Brain Manager | Следующий шаг: подготовить пакет REST `/api/v1/auth/*`, событий auth-service и OAuth потоков для ДУАПИТАСК.
- **Документ:** .BRAIN/02-gameplay/social/npc-relationships-system-детально.md (v1.0.0)  
  **Проверено:** 2025-11-09 12:15 — Brain Manager | Следующий шаг: передать документ в ДУАПИТАСК и связать `api/v1/social/npc-relationships.yaml` с модулем `modules/social/npc-relations`.
- **Документ:** .BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-009-granville-island.md (v1.0.0)  
  **Проверено:** 2025-11-09 11:09 — Brain Readiness Checker | Следующий шаг: передать документ в ДУАПИТАСК и обновить `brain-mapping.yaml` для каталога `api/v1/narrative/quests/america/vancouver/granville-island.yaml`.
- **Документ:** .BRAIN/05-technical/backend/player-character-mgmt/character-management.md (v1.1.0)  
  **Проверено:** 2025-11-09 02:47 — Brain Manager | Следующий шаг: оформить REST/Events по CRUD, переключению и восстановлению персонажей (`api/v1/characters/players.yaml`) для character-service.
- **Документ:** .BRAIN/05-technical/backend/progression-backend.md (v1.0.0)  
  **Проверено:** 2025-11-09 02:47 — Brain Manager | Следующий шаг: сформировать пакет задач по EXP/level progression (`api/v1/gameplay/progression/progression-core.yaml`) для gameplay-service.
- **Документ:** .BRAIN/05-technical/backend/inventory-system/part1-core-system.md (v1.0.1)  
  **Проверено:** 2025-11-09 01:30 — Brain Manager | Следующий шаг: подготовить пакет REST/Events по инвентарю (`api/v1/inventory/inventory-core.yaml`, economy-service).
- **Документ:** .BRAIN/05-technical/backend/combat-session-backend.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:24 — Brain Manager | Следующий шаг: использовать конспект `2025-11-09-combat-wave-package.md` для разбивки combat session API.
- **Документ:** .BRAIN/05-technical/backend/trade-system.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:30 — Brain Manager | Следующий шаг: подготовить задание по P2P трейду (economy-service, `api/v1/trade/trade-system.yaml`) с антифродом и очередью подтверждений.
- **Документ:** .BRAIN/02-gameplay/economy/auction-house/auction-database.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:09 — Brain Manager | Следующий шаг: подготовить задачи для economy-service по БД аукциона (`api/v1/economy/auction-house/auction-database.yaml`) и синхронизировать с фронтенд модулем `modules/economy/auction-house`.
- **Документ:** .BRAIN/03-lore/_03-lore/characters/characters-overview.md (v1.2.0)  
  **Проверено:** 2025-11-09 11:21 — Brain Manager | Следующий шаг: подготовить пакет API по каталогам NPC (`api/v1/narrative/characters/characters-overview.yaml`) и синхронизировать модуль `modules/narrative/characters`.
- **Документ:** .BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:09 — Brain Manager | Следующий шаг: сформировать REST/Events пакет по механикам ставок и buyout (`api/v1/economy/auction-house/auction-mechanics.yaml`) и передать в ДУАПИТАСК.
- **Документ:** .BRAIN/02-gameplay/economy/auction-house/auction-operations.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:09 — Brain Manager | Следующий шаг: сверстать задачи для economy-service по операционным REST эндпоинтам аукциона (`api/v1/economy/auction-house/auction-operations.yaml`) и мониторингу.
- **Документ:** .BRAIN/02-gameplay/economy/economy-analytics.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:20 — Brain Manager | Следующий шаг: подготовить задания economy-service по аналитическим REST/WS потокам (`api/v1/economy/analytics/analytics.yaml`) и синхронизировать модуль `modules/economy/analytics`.
- **Документ:** .BRAIN/02-gameplay/economy/economy-investments.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:25 — Brain Manager | Следующий шаг: сформировать задачи economy-service по инвестиционному модулю (`api/v1/economy/investments/investments.yaml`) и модулю `modules/economy/investments`.
- **Документ:** .BRAIN/02-gameplay/economy/economy-contracts.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:20 — Brain Manager | Следующий шаг: оформить задачи economy-service по контрактам и escrow (`api/v1/economy/contracts/contracts.yaml`) и связать с модулем `modules/economy/contracts`.
- **Документ:** .BRAIN/02-gameplay/economy/economy-crafting.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:20 — Brain Manager | Следующий шаг: подготовить API пакет по гибридному крафту (`api/v1/economy/crafting/crafting.yaml`) и модулю `modules/economy/crafting`.
- **Документ:** .BRAIN/02-gameplay/economy/economy-events.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:25 — Brain Manager | Следующий шаг: сформировать задачи economy-service по экономическим событиям (`api/v1/economy/events/events.yaml`) и обновить модуль `modules/economy/events`.
- **Документ:** .BRAIN/05-technical/backend/quest-engine-backend.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:19 — Brain Manager | Следующий шаг: оформить REST/WS/EventBus задачи по конспекту `2025-11-09-quest-engine-package.md` и подготовить передачу в ДУАПИТАСК (`api/v1/gameplay/quests/quest-engine.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-implants-types.md (v1.1.0)  
  **Проверено:** 2025-11-09 00:47 — Brain Manager | Следующий шаг: сформировать API задачи по боевым имплантам (gameplay-service).
- **Документ:** .BRAIN/02-gameplay/combat/combat-combos-synergies.md (v1.0.0)  
  **Проверено:** 2025-11-09 02:48 — Brain Manager | Следующий шаг: запланировать API задачи на систему комбо/синергий (gameplay-service, волна 2, `api/v1/gameplay/combat/combos-synergies.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-ai-enemies.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:22 — Brain Manager | Следующий шаг: подготовить пакет API задач по AI и WebSocket потокам (gameplay-service, `api/v1/gameplay/combat/ai-enemies.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/arena-system.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:22 — Brain Manager | Следующий шаг: синхронизировать постановку задач по аренам и рейтингам (gameplay-service, api/v1/gameplay/combat/arena-system.yaml).
- **Документ:** .BRAIN/02-gameplay/combat/combat-shooting.md (v1.1.0)  
  **Проверено:** 2025-11-09 01:37 — Brain Manager | Следующий шаг: подготовить REST/WS пакет по TTK, отдаче и имплант-модификаторам (gameplay-service, `api/v1/gameplay/combat/shooting.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-extract.md (v1.3.0)  
  **Проверено:** 2025-11-09 01:44 — Brain Manager | Следующий шаг: сформировать пакет API задач по экстракт-зонам (gameplay-service, `api/v1/gameplay/combat/extraction.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/loot-hunt-system.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:09 — Brain Manager | Следующий шаг: подготовить API пакет по Loot Hunt (контракты, эвенты, интеграции; gameplay-service, `api/v1/gameplay/combat/loot-hunt.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-hacking-networks.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:44 — Brain Manager | Следующий шаг: сформировать API задачи на сетевой хакерский слой (gameplay-service, `api/v1/gameplay/combat/hacking/networks.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-hacking-combat-integration.md (v1.0.0)  
  **Проверено:** 2025-11-09 01:44 — Brain Manager | Следующий шаг: подготовить API задачи по интеграции хакерства в бою (gameplay-service, `api/v1/gameplay/combat/hacking/combat-integration.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-cyberpsychosis.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:19 — Brain Manager | Следующий шаг: сформировать пакет API задач по человечности, стадиям симптомов и управлению киберпсихозом (gameplay-service, `api/v1/gameplay/combat/cyberpsychosis.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-cyberspace.md (v1.0.0)  
  **Проверено:** 2025-11-09 00:54 — Brain Manager | Следующий шаг: подготовить задания для киберпространства и связанных режимов (gameplay-service).
- **Документ:** .BRAIN/02-gameplay/combat/combat-abilities.md (v1.2.0)  
  **Проверено:** 2025-11-09 02:49 — Brain Manager | Следующий шаг: подготовить API задачи по системе боевых способностей (gameplay-service, `api/v1/gameplay/combat/abilities.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-roles-detailed.md (v1.0.0)  
  **Проверено:** 2025-11-09 03:14 — Brain Manager | Следующий шаг: подготовить API пакет по ролям и билд-комбинаторике (gameplay-service, `api/v1/gameplay/combat/roles.yaml`) и синхронизировать с модулем `modules/combat/roles`.
- **Документ:** .BRAIN/02-gameplay/combat/combat-implants-acquisition.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:14 — Brain Manager | Следующий шаг: сформировать REST/Events задачи по получению, апгрейдам и редкости имплантов (gameplay-service, `api/v1/gameplay/combat/implants/acquisition.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-implants-mechanics.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:14 — Brain Manager | Следующий шаг: подготовить API задачи по хакерству, совместимости и износу имплантов (gameplay-service, `api/v1/gameplay/combat/implants/mechanics.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-implants-visuals.md (v1.1.0)  
  **Проверено:** 2025-11-09 03:14 — Brain Manager | Следующий шаг: оформить фронт/бэк задачи по визуализации и кастомизации имплантов (gameplay-service, `api/v1/gameplay/combat/implants/visuals.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-loadouts-system.md (v0.3.0)  
  **Проверено:** 2025-11-09 03:14 — Brain Manager | Следующий шаг: сформировать пакет API задач по управлению лодаутами и комплектами (gameplay-service, `api/v1/gameplay/combat/loadouts.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-stealth.md (v1.1.0)  
  **Проверено:** 2025-11-09 01:39 — Brain Manager | Следующий шаг: подготовить REST/Events пакет по скрытности и обнаружению (gameplay-service, `api/v1/gameplay/combat/stealth.yaml`).
- **Документ:** .BRAIN/02-gameplay/combat/combat-freerun.md (v1.1.0)  
  **Проверено:** 2025-11-09 02:50 — Brain Manager | Следующий шаг: сформировать пакет задач по паркуру и мобильным комбо (gameplay-service, `api/v1/gameplay/combat/freerun.yaml`).
- **Документ:** .BRAIN/02-gameplay/world/world-state/player-impact-systems.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:35 — Brain Manager | Следующий шаг: оформить пакет API мирового состояния по player impact (`api/v1/world/state/player-impact-systems.yaml`) и синхронизировать модуль `modules/world/state/player-impact` для world-service.
- **Документ:** .BRAIN/02-gameplay/social/npc-hiring-world-impact-детально.md (v1.0.0)  
  **Проверено:** 2025-11-09 09:55 — Brain Manager | Следующий шаг: подготовить пакет API влияния найма (`api/v1/world/npc-hiring/impact.yaml`) и передать задачу ДУАПИТАСК для world-service (economy/social зависимости учтены).
