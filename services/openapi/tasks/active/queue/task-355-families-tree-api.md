# Task ID: API-TASK-355
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 20:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Подготовить спецификацию `Families Tree API`, фиксирующую структуру семей NPC/игроков, взаимосвязи, статусы и метаданные для социальных систем.  
**Целевой файл:** `api/v1/social/families/tree.yaml`

---

## 🎯 Цель задания

Дать social-service REST контракт, который:
- строит и возвращает древовидную структуру семей (родственные и брачные связи, состояния, эмоции);  
- поддерживает операции обновления (создание связей, разрыв, усыновление, вхождение игрока в семью);  
- сохраняет расширенную информацию (секреты, роли, фракции, статусы, кланы) и ссылки на другие системы;  
- интегрируется с world-service (события), economy-service (наследство), npc-relationships, mentorship и player orders.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/family-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:53  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §2–6: структуры семей, эмоциональные уровни, события, усыновление.  
- §9–10: UX дерева, связи с другими системами.  
- §12: REST макеты (`GET /social/families/{familyId}/tree`, `POST /social/families/tree/update`).  
- §13: Kafka события `social.family.status.changed`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md` — эмоции и отношения NPC.  
- `.BRAIN/02-gameplay/social/mentorship-system-детально.md` — наставничество внутри семей.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — семейные поручения.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — визуальные элементы дерева.  
- `.BRAIN/05-technical/content-generation/family-history-archive.md` — архивы биографий.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/families/tree.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── families/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── tree.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** world-service (события, кризисы), economy-service (наследство), character-service (биографии), notification-service (alerts), analytics-service (FamilyStabilityIndex), content-service (VR-архивы).  
- **Kafka:** `social.family.status.changed`, `social.family.event`, `world.family.crisis`, `economy.family.heritage`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/families  
- **State Store:** `useSocialStore(families)`  
- **UI:** `FamilyTreeView`, `FamilyMemberCard`, `FamilyTimeline`, `FamilyAlertsPanel`, `FamilySecretsDrawer`  
- **Формы:** `FamilyLinkEditorForm`, `FamilyAdoptionForm`, `FamilyClanJoinForm`  
- **Layouts:** `FamilyTreeLayout`, `FamilyDashboardLayout`  
- **Hooks:** `useFamilyTree`, `useFamilyMember`, `useFamilyAlerts`, `useFamilyTimeline`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/families
# - State Store: useSocialStore(families)
# - UI: FamilyTreeView, FamilyMemberCard, FamilyTimeline, FamilyAlertsPanel, FamilySecretsDrawer
# - Forms: FamilyLinkEditorForm, FamilyAdoptionForm, FamilyClanJoinForm
# - Layouts: FamilyTreeLayout, FamilyDashboardLayout
# - Hooks: useFamilyTree, useFamilyMember, useFamilyAlerts, useFamilyTimeline
# - Events: social.family.status.changed, social.family.event, world.family.crisis, economy.family.heritage
# - API Base: /api/v1/social/families/*
```

---

## ✅ Детальный план

1. **Определить структуру дерева:** члены семьи, типы связей (биологические, брачные, опека), статусы, метаданные.  
2. **Спроектировать схемы:** `FamilyTree`, `FamilyMember`, `FamilyRelationship`, `FamilyClan`, `FamilySecret`, `FamilyLinkUpdateRequest`, `FamilyAdoptionRequest`, `FamilyFavorite`.  
3. **Реализовать эндпоинты:** получение дерева, фильтры, обновление связей, управление усыновлением/вступлением, таймлайн, избранные семьи.  
4. **Интеграции:** ссылки на npc-relationships, mentorship, player orders, economy heritage.  
5. **Добавить примеры:** родовое дерево, усыновление, вступление игрока, клановый союз, скрытый член семьи.  
6. **Shared компоненты:** security/responses/pagination (для истории), вынести схемы/примеры в components, соблюдать лимит 400 строк.  
7. **Документировать Kafka события и очереди (`family-event-review`, `family-heritage-validation`).**  
8. **Определить коды ошибок и правила (конфликт связей, циклы, разрешения).**  
9. **Прописать метрики (`FamilyStabilityIndex`, `ClanAllianceStrength`).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **GET `/social/families/{familyId}/tree`** — полное дерево семьи.  
2. **GET `/social/families`** — список семей (фильтры по статусу, фракции, региону, уровню влияния).  
3. **POST `/social/families/tree/update`** — обновление/создание связей (добавление, разрыв, изменение статуса).  
4. **POST `/social/families/adoption`** — процессы усыновления/опеки (проверки, согласования).  
5. **POST `/social/families/{familyId}/join`** — вступление игрока или NPC в семью.  
6. **GET `/social/families/{familyId}/timeline`** — история событий (рождения, свадьбы, кризисы).  
7. **GET `/social/families/{familyId}/alerts`** — активные предупреждения (кризисы, запросы о помощи).  
8. **POST `/social/families/{familyId}/alerts/ack`** — подтверждение обработки alert.  
9. **GET `/social/families/{familyId}/secrets`** — доступные секреты (с учётом прав).  
10. **POST `/social/families/{familyId}/favorite`** — маркировка семьи/ветви как избранной (UI).  
11. **GET `/social/families/search`** — поиск по членам, профессиям, отношениям.  
12. **GET `/social/families/{familyId}/links`** — зависимости с другими системами (контракты, наследство, кланы).

---

## 🧱 Модели данных

- **FamilyTree** — `familyId`, `name`, `clanId`, `origin`, `members[]`, `relationships[]`, `alliances[]`, `secrets[]`, `metadata`.  
- **FamilyMember** — `memberId`, `npcId/playerId`, `role`, `status`, `location`, `faction`, `relationshipScores`, `trust`, `loyalty`, `tags`, `notes`.  
- **FamilyRelationship** — `from`, `to`, `type`, `state`, `startAt`, `endAt`, `confidence`, `legalStatus`.  
- **FamilyLinkUpdateRequest** — изменения (create/update/delete), причины, подтверждения.  
- **FamilyAdoptionRequest** — `guardianId`, `wardId`, условия, approvals, ceremonyDate.  
- **FamilyTimelineEvent** — `eventId`, `eventType`, `timestamp`, `participants[]`, `impact`.  
- **FamilyAlert** — `alertId`, `severity`, `message`, `actionRequired`, `deadline`.  
- **FamilySecret** — `secretId`, `visibility`, `description`, `unlockConditions`.  
- **FamilyFavorite** — пользовательские метки.  
- **PaginatedFamilyTimeline** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, вынести схемы/примеры.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_FAMILY_TREE_INVALID`, `BIZ_FAMILY_LINK_CONFLICT`, `BIZ_FAMILY_ADOPTION_DENIED`, `BIZ_FAMILY_JOIN_RESTRICTED`, `INT_FAMILY_TREE_PIPELINE_FAILURE`.  
- `info.description` — перечислить `.BRAIN` источники, UX подтверждения, связанные сервисы.  
- Теги: `Families`, `Relationships`, `Tree`, `Adoption`, `Alliances`.  
- Указать зависимости на `npc-relationships/status.yaml`, `families/events.yaml`, `economy/families/heritage.yaml`.

---

## ✅ Критерии приемки

1. Создан файл `api/v1/social/families/tree.yaml`, проходит `scripts/validate-swagger.ps1`.  
2. В начале файла добавлен `Target Architecture` блок.  
3. Реализованы эндпоинты и модели из задания.  
4. Подключены shared security/responses/pagination.  
5. Добавлены примеры (родовое дерево, усыновление, вступление игрока, alert).  
6. Kafka события и очереди описаны.  
7. README в каталоге обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. `.BRAIN` документ обновлён (API Tasks Status).  
10. Указаны зависимости на другие API (events, heritage, relationships).  
11. Обозначены метрики `FamilyStabilityIndex`, `ClanAllianceStrength`, `AdoptionSuccessRate`.

---

## ❓ FAQ

**Q:** Как предотвратить циклы в дереве?  
A: API должно валидировать связи (родитель ≠ потомок и т.д.), возвращать `BIZ_FAMILY_LINK_CONFLICT`.  

**Q:** Поддерживаются ли скрытые ветви?  
A: Да, через `visibility` и `unlockConditions`; доступны только при выполнении условий.  

**Q:** Можно ли объединять семьи/кланы?  
A: Да, через `alliances[]`; потребует доп. эндпоинтов, описать структуру и связи.  

**Q:** Как хранить историю изменений?  
A: Использовать `timeline` + `history` эндпоинты; изменения фиксируются с audit данными.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

