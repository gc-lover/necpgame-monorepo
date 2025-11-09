# Task ID: API-TASK-353
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-352 (статус отношений)

---

## 📋 Краткое описание

Разработать спецификацию `NPC Interactions API`, отвечающую за фиксацию, модерацию и аналитику взаимодействий игрок ↔ NPC, включая подарки, диалоги, события и романтические сцены.  
**Целевой файл:** `api/v1/social/npc-relationships/interactions.yaml`

---

## 🎯 Цель задания

Создать social-service API, которое:
- регистрирует все типы взаимодействий (quests, dialogues, gifts, romance, betrayal, emergency support);  
- запускает автоматические эффекты (изменение репутации, эмоций, квестовых триггеров) и хранит доказательства/контент;  
- поддерживает модерацию, жалобы, SLAs на обработку конфликтов;  
- обеспечивает аналитику (частота взаимодействий, mood deltas, romance progression) и интеграцию с notification-service, gameplay-service, economy-service.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:47  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §3–6: механики влияния, подарки, романтика, предательство.  
- §7–8: особые типы отношений (mentorship, guild, family, business).  
- §11: история и арбитраж.  
- §13–14: REST макеты (`POST /social/npc-relationships/interactions`, `GET /history`) и Kafka события.  
- §15: метрики (InteractionFrequency, MoodDelta, RomanceSuccessRate).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md` — взаимодействия с нанятыми NPC.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — жалобы и арбитраж.  
- `.BRAIN/05-technical/content-generation/npc-dialogue-authoring.md` — хранение диалогов.  
- `.BRAIN/05-technical/compliance/npc-interaction-moderation.md` — модерация и регуляторика.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — визуальные элементы лога.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/npc-relationships/interactions.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── npc-relationships/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── interactions.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** gameplay-service (quests, combat outcomes), economy-service (trade, gifts), world-service (events), notification-service (alerts), content-service (media evidence), analytics-service (relationship dashboards), moderation-service.  
- **Kafka:** `social.npc-interaction.logged`, `social.npc-interaction.moderation`, `social.npc-romance.scene`, `economy.npc-gift.transaction`, `world.npc-relationships.event`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/npc-relations  
- **State Store:** `useSocialStore(npcInteractions)`  
- **UI:** `NpcInteractionTimeline`, `NpcInteractionFilter`, `NpcInteractionDetailModal`, `NpcInteractionModerationQueue`, `NpcInteractionAnalytics`  
- **Формы:** `NpcInteractionLogForm`, `NpcGiftForm`, `NpcRomanceSceneForm`, `NpcComplaintForm`  
- **Layouts:** `NpcInteractionLogLayout`, `NpcInteractionModerationLayout`  
- **Hooks:** `useNpcInteractions`, `useNpcInteraction`, `useNpcInteractionAnalytics`, `useNpcInteractionComplaints`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/npc-relations
# - State Store: useSocialStore(npcInteractions)
# - UI: NpcInteractionTimeline, NpcInteractionFilter, NpcInteractionDetailModal, NpcInteractionModerationQueue, NpcInteractionAnalytics
# - Forms: NpcInteractionLogForm, NpcGiftForm, NpcRomanceSceneForm, NpcComplaintForm
# - Layouts: NpcInteractionLogLayout, NpcInteractionModerationLayout
# - Hooks: useNpcInteractions, useNpcInteraction, useNpcInteractionAnalytics, useNpcInteractionComplaints
# - Events: social.npc-interaction.logged, social.npc-interaction.moderation, social.npc-romance.scene, economy.npc-gift.transaction, world.npc-relationships.event
# - API Base: /api/v1/social/npc-relationships/*
```

---

## ✅ Детальный план

1. **Определить типы взаимодействий:** quest, dialogue, gift, romance, betrayal, support, employment, emergency, media.  
2. **Спроектировать схемы:** `NpcInteractionLog`, `NpcInteractionPayload`, `NpcGift`, `NpcRomanceScene`, `NpcComplaint`, `NpcInteractionModeration`, `NpcInteractionAnalytics`, `NpcInteractionFilter`.  
3. **Реализовать endpoint'ы:** log, list, detail, analytics, moderation, complaints, attachments, export.  
4. **Добавить связь с `status` API (обновление отношений) и `events` API (world broadcast).**  
5. **Документировать модерацию и SLA: статусы (`pending`, `approved`, `rejected`, `escalated`).**  
6. **Примеры:** подарок, романтическая сцена, предательство, жалоба, модерация.  
7. **Shared components:** security/responses/pagination; вынести схемы/примеры, соблюдать лимит 400 строк.  
8. **Коды ошибок:** модерация, лимиты, конфликт настроек, отсутствующие данные.  
9. **Kafka и очереди (`npc-interaction-moderation`, `npc-romance-cutscene`).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/social/npc-relationships/interactions`** — логирование взаимодействия (с payload, вложениями, эффектами).  
2. **GET `/social/npc-relationships/interactions/{interactionId}`** — детальный просмотр.  
3. **GET `/social/npc-relationships/interactions`** — список по фильтрам (npcId, playerId, type, severity, romance, gift, quest, timeframe).  
4. **POST `/social/npc-relationships/interactions/{interactionId}/effects`** — ручная корректировка эффектов (devops/moderation).  
5. **POST `/social/npc-relationships/interactions/{interactionId}/complaint`** — жалоба/арбитраж.  
6. **GET `/social/npc-relationships/interactions/{interactionId}/attachments`** — связанные медиа/доказательства.  
7. **POST `/social/npc-relationships/interactions/{interactionId}/attachments`** — добавление ссылок на контент (через content-service).  
8. **GET `/social/npc-relationships/interactions/analytics`** — агрегаты (InteractionFrequency, MoodDelta, RomanceSceneCount).  
9. **GET `/social/npc-relationships/interactions/moderation`** — очередь модерации.  
10. **POST `/social/npc-relationships/interactions/{interactionId}/moderation`** — решение модератора.  
11. **GET `/social/npc-relationships/interactions/export`** — экспорт логов (CSV/JSON) с фильтрами.  
12. **GET `/social/npc-relationships/interactions/romance`** — список романтических сцен (фильтры, рейтинги).

---

## 🧱 Модели данных

- **NpcInteractionLog** — `interactionId`, `npcId`, `playerId`, `type`, `subtype`, `outcome`, `effect`, `romanceStage`, `moodDelta`, `trustDelta`, `timestamp`, `location`, `media[]`, `metadata`.  
- **NpcInteractionPayload** — детали взаимодействия (dialogueId, questId, giftId, contractId, choices, score).  
- **NpcGift** — `giftId`, `itemRef`, `value`, `npcPreference`, `reaction`, `economyTransactionId`.  
- **NpcRomanceScene** — `sceneId`, `stage`, `requirements`, `duration`, `contentRefs`, `maturityRating`.  
- **NpcComplaint** — `complaintId`, `filedBy`, `reason`, `evidence`, `status`, `resolution`.  
- **NpcInteractionModeration** — `moderationId`, `moderatorId`, `decision`, `notes`, `timestamp`.  
- **NpcInteractionAnalytics** — агрегаты (frequency, moodDeltaAvg, romanceSuccessRate, betrayalRate).  
- **NpcInteractionFilter** — параметры поиска/аналитики.  
- **PaginatedNpcInteractions** — стандартная пагинация.  
- **NpcInteractionEffectOverride** — ручные корректировки (signedBy, changeReason).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, схемы/примеры вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_INTERACTION_INVALID`, `BIZ_NPC_INTERACTION_LIMIT_REACHED`, `BIZ_NPC_INTERACTION_MODERATION_PENDING`, `BIZ_NPC_INTERACTION_ATTACHMENT_FORBIDDEN`, `INT_NPC_INTERACTION_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, UX, модерацию, интеграции.  
- Теги: `NPC Relationships`, `Interactions`, `Moderation`, `Romance`, `Analytics`.  
- Указать зависимости на `npc-relationships/status.yaml`, `npc-hiring/contracts.yaml`, `player-orders/ratings.yaml`, `world/npc-relationships/events.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/npc-relationships/interactions.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. `Target Architecture` блок добавлен.  
3. Реализованы все заявленные эндпоинты, модели и примеры.  
4. Подключены shared security/responses/pagination.  
5. Kafka события, очереди модерации и интеграции документированы.  
6. README обновлён (в рамках реализации).  
7. Task добавлен в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён (`API Tasks Status`).  
9. Указаны зависимости на статус/события/экономику/найм.  
10. Обозначены метрики (`InteractionFrequency`, `MoodDelta`, `RomanceSuccessRate`).  
11. Описаны правила жалоб, модерации и SLA.

---

## ❓ FAQ

**Q:** Как хранить приватные взаимодействия?  
A: Добавить поле `visibility` (`private`, `friends`, `public`) и фильтрацию; приватные записи доступны только владельцу/модерации.  

**Q:** Можно ли редактировать взаимодействие?  
A: Только через endpoint `/effects` с аудитом; оригинальная запись неизменна, изменения логируются.  

**Q:** Нужна ли поддержка вложенных сцен?  
A: Да, `NpcRomanceScene` хранит ссылки на chapthers/segments; указать `sequence`.  

**Q:** Как отслеживать подарки и экономику?  
A: Возвращать `economyTransactionId`, синхронизировать с economy-service событиями `economy.npc-gift.transaction`.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

