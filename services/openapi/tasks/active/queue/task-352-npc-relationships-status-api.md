# Task ID: API-TASK-352
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Подготовить спецификацию `NPC Relationships Status API`, описывающую хранение и выдачу статусов отношений, эмоций, доверия и романтики с NPC.  
**Целевой файл:** `api/v1/social/npc-relationships/status.yaml`

---

## 🎯 Цель задания

Обеспечить social-service API, которое:
- возвращает полный профиль отношений с NPC (уровень репутации, доверие, эмоции, романтика, лояльность, фракционные модификаторы);  
- поддерживает пакетные обновления шкал (adjust), audit истории, фильтры по типам NPC и событиям;  
- синхронизируется с world-service (глобальные эффекты), gameplay-service (квесты, миссии), economy-service (скидки, сделки) и character-service (биографии NPC);  
- публикует события об изменениях (`social.npc-relationships.changed`, `social.npc-relationships.alert`) для UI и аналитики.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:47  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §2–6: типы NPC, уровни отношений, эмоции, романтика, влияния.  
- §9–11: влияние на мир, UX, журнал отношений.  
- §13–14: REST макеты (`GET /social/npc-relationships/{npcId}`, `POST /social/npc-relationships/adjust`) и Kafka (`social.npc-relationships.changed`).  
- §15: метрики (`NpcRelationshipSatisfaction`, `RomanceSuccessRate`, `NpcLoyaltyTrend`).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — общая система отношений.  
- `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md` — связка с наймом и лояльностью.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — визуализация карточек NPC.  
- `.BRAIN/02-gameplay/social/family-relationships-system-детально.md` — семейные связи и наследственные эффекты.  
- `.BRAIN/05-technical/telemetry/npc-relationships-monitoring.md` — аналитика и алерты.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/npc-relationships/status.yaml`  
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
                └── status.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** world-service (события и глобальные модификаторы), gameplay-service (квесты, романтические сцены), economy-service (скидки, комм. соглашения), character-service (биографии, статусы), notification-service (alerts), analytics-service (dashboards).  
- **Kafka:** `social.npc-relationships.changed`, `social.npc-relationships.alert`, `world.npc-relationships.event`, `social.npc-romance.state`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/npc-relations  
- **State Store:** `useSocialStore(npcRelationships)`  
- **UI:** `NpcRelationshipCard`, `NpcEmotionsWidget`, `NpcRomanceTracker`, `NpcRelationshipHistory`, `NpcAlertBanner`  
- **Формы:** `NpcRelationshipFilterForm`, `NpcRelationshipAdjustForm`, `NpcRomanceActionForm`  
- **Layouts:** `NpcRelationsLayout`, `NpcRomanceLayout`  
- **Hooks:** `useNpcRelationship`, `useNpcRelationshipHistory`, `useNpcRomance`, `useNpcRelationshipAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/npc-relations
# - State Store: useSocialStore(npcRelationships)
# - UI: NpcRelationshipCard, NpcEmotionsWidget, NpcRomanceTracker, NpcRelationshipHistory, NpcAlertBanner
# - Forms: NpcRelationshipFilterForm, NpcRelationshipAdjustForm, NpcRomanceActionForm
# - Layouts: NpcRelationsLayout, NpcRomanceLayout
# - Hooks: useNpcRelationship, useNpcRelationshipHistory, useNpcRomance, useNpcRelationshipAlerts
# - Events: social.npc-relationships.changed, social.npc-relationships.alert, world.npc-relationships.event, social.npc-romance.state
# - API Base: /api/v1/social/npc-relationships/*
```

---

## ✅ Детальный план

1. **Определить модель статуса:** уровни (reputation, trust, loyalty, mood, romance), фракционные модификаторы, классовые бонусы, семейные связи.  
2. **Спроектировать схемы:** `NpcRelationshipStatus`, `NpcRelationshipEmotion`, `NpcRomanceStatus`, `NpcRelationshipAdjustRequest`, `NpcRelationshipHistoryEntry`, `NpcRelationshipAlert`.  
3. **Продумать фильтры и параметры (`GET /status`), поддержать пагинацию и сортировку.**  
4. **Реализовать обновления:** batch adjust с проверками cooldown, лимитов, лицензий.  
5. **Документировать связь с наймом, mentorship, player orders (обратное влияние).**  
6. **Добавить примеры:** союз с сюжетным NPC, романтический статус, падение доверия, alert от betrayal.  
7. **Использовать shared security/responses/pagination, вынести схемы/примеры в components, соблюдать лимит 400 строк.**  
8. **Задокументировать Kafka события, очереди мониторинга (`npc-relationship-monitoring`).**  
9. **Прописать коды ошибок и бизнес-правила (cooldown, блокировки, конфликт романтики).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README в каталоге.**

---

## 🔌 Эндпоинты

1. **GET `/social/npc-relationships/{npcId}`** — подробный статус отношений с NPC.  
2. **GET `/social/npc-relationships`** — список по фильтрам (тип NPC, уровень, фракция, эмоции, романтика).  
3. **POST `/social/npc-relationships/adjust`** — пакетное изменение уровней (reputation, trust, loyalty, mood).  
4. **POST `/social/npc-relationships/romance`** — управление романтическим прогрессом (start, progress, break).  
5. **GET `/social/npc-relationships/history/{npcId}`** — история взаимодействий и событий.  
6. **GET `/social/npc-relationships/alerts`** — активные предупреждения (crisis, betrayal, burnout).  
7. **POST `/social/npc-relationships/alerts/ack`** — подтверждение обработки alert.  
8. **GET `/social/npc-relationships/summary`** — агрегаты по регионам, фракциям, типам NPC.  
9. **GET `/social/npc-relationships/romance`** — список романтических статусов (фильтры по фазам).  
10. **POST `/social/npc-relationships/{npcId}/favorite`** — отметка важного NPC (опционально, для UI-меток).

---

## 🧱 Модели данных

- **NpcRelationshipStatus** — `npcId`, `npcType`, `importance`, `reputation`, `trust`, `loyalty`, `mood`, `romance`, `factionModifier`, `classModifier`, `familyLinks[]`, `lastInteraction`, `alerts[]`.  
- **NpcRelationshipEmotion** — текущие эмоции (mood score, emotion tags, triggers, decay).  
- **NpcRomanceStatus** — стадия, progress, gating requirements, activeScene, cooldown.  
- **NpcRelationshipAdjustRequest** — список изменений (npcId, deltas, reason, source, metadata).  
- **NpcRelationshipHistoryEntry** — события (quest, gift, betrayal, event), эффекты и контекст.  
- **NpcRelationshipAlert** — `alertId`, `severity`, `category`, `message`, `npcId`, `actionRequired`, `createdAt`.  
- **NpcRelationshipSummary** — агрегаты (counts by tier, romance stats, loyalty trends).  
- **PaginatedNpcRelationshipHistory** — стандартная пагинация.  
- **NpcFavoriteMarker** — пометка пользователя (note, priority, pinExpiration).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, схемы/примеры вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_RELATIONSHIP_INVALID`, `BIZ_NPC_RELATIONSHIP_COOLDOWN`, `BIZ_NPC_ROMANCE_CONFLICT`, `BIZ_NPC_RELATIONSHIP_LOCKED`, `INT_NPC_RELATIONSHIP_PIPELINE_FAILURE`.  
- `info.description` — перечислить `.BRAIN` источники, UX подтверждения, связи с наймом/фракциями.  
- Теги: `NPC Relationships`, `Status`, `Romance`, `Alerts`, `Analytics`.  
- Указать зависимости на `npc-hiring/contracts.yaml`, `npc-hiring/workforce.yaml`, `families/tree.yaml`, `relationships/status.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/npc-relationships/status.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. В начале присутствует `Target Architecture` блок.  
3. Описаны все указанные эндпоинты, модели, ошибки и события.  
4. Подключены shared security/responses/pagination.  
5. Добавлены примеры (союз, романтика, betrayal alert, summary).  
6. Kafka события и очередь мониторинга документированы.  
7. README в каталоге обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. `.BRAIN` документ обновлён (API Tasks Status).  
10. Указаны зависимости на найм, романтику, семьи, фракции.  
11. Обозначены метрики (`NpcRelationshipSatisfaction`, `NpcLoyaltyTrend`, `RomanceSuccessRate`).

---

## ❓ FAQ

**Q:** Как обрабатывать конфликт романтик с несколькими NPC?  
A: Возвращать ошибку `BIZ_NPC_ROMANCE_CONFLICT`, поддерживать флаги `exclusive`, `poly` и требования к разрешению.  

**Q:** Можно ли скрывать отношения от других игроков?  
A: Да, добавить поля `visibility`, `shareable`; API должно учитывать приватные NPC (без публичного отображения).  

**Q:** Требуются ли уведомления от world событий?  
A: Да, `world.npc-relationships.event` должен отражать глобальные изменения и попадать в summary/alerts.  

**Q:** Как связать с семьями?  
A: Использовать `familyLinks[]` и обращения к `api/v1/social/families/tree.yaml` (будет реализовано отдельно).  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

