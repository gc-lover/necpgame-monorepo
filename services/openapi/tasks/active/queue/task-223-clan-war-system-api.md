# Task ID: API-TASK-223
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:38
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-135, API-TASK-140, API-TASK-196

---

## 📋 Краткое описание

Реализовать API системы клановых войн: объявления войн, контроль территорий, осады, награды и альянсы.

**Что нужно сделать:** Подготовить `api/v1/gameplay/clans/clan-war-system.yaml`, описав endpoints, события и модели из документа `.BRAIN/.../clan-war-system.md`.

---

## 🎯 Цель задания

Обеспечить геймплей клановых войн крупного масштаба, согласованный с progression, economy и realtime системами.

**Зачем это нужно:**
- Ввести endgame PvP/PvE контент (territory control, sieges)
- Позволить кланам объявлять войны, формировать альянсы, получать награды
- Синхронизировать данные войн с UI, matchmaking и live-ops аналитикой
- Интегрировать систему с экономикой, progression и world events

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/clan-war/clan-war-system.md`
**Версия:** v1.0.0 (2025-11-07 02:34)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- War lifecycle (declaration, preparation, battle, aftermath)
- Territory control, siege mechanics, fortifications
- Alliance system, diplomatic statuses
- War rewards (currency, territory bonuses, cosmetic)
- Events & WebSocket topics

### Дополнительные источники

- `.BRAIN/05-technical/backend/guild-system-backend.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/economy-system.md`
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md`
- `.BRAIN/05-technical/backend/progression-backend.md`

### Связанные документы

- `API-SWAGGER/tasks/completed/2025-11-07-batch3/task-204-clan-war-system-api.md` (референс)
- `API-SWAGGER/tasks/active/queue/task-135-guild-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-196-realtime-server-zones-api.md`
- `API-SWAGGER/tasks/active/queue/task-214-inventory-advanced-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/gameplay/clans/clan-war-system.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/gameplay/clans/
 ├── clan-system.yaml
 ├── clan-members.yaml
 └── clan-war-system.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service (clans module)
- **Порт:** 8083
- **API Base Path:** `/api/v1/gameplay/clans`
- **Зависимости:**
  - guild-service (или clans module) – данные о кланах
  - progression-service – clan XP, buffs
  - economy-service – налоги, награды, upkeep
  - realtime-service – осады, live combat feed
  - notification-service – объявления войн, победы, тревоги
  - analytics-service – статистика войн

### Frontend
- **Модуль:** `modules/clans/clan-war`
- **State Store:** `useClanWarStore`
- **State:** `wars`, `territories`, `alliances`, `siegePlans`, `rewards`
- **UI компоненты:** `ClanWarDashboard`, `WarDeclarationForm`, `TerritoryMap`, `SiegePlanner`, `AlliancePanel`, `WarTimeline`
- **Forms:** `DeclareWarForm`, `AllianceProposalForm`, `SiegeScheduleForm`
- **Layouts:** `ClanHubLayout`
- **Хуки:** `useClanWarStatus`, `useTerritoryControl`, `useAllianceRelations`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/clans
# - Dependencies: guild/clan, progression, economy, realtime, notification, analytics
# - Frontend Module: modules/clans/clan-war (useClanWarStore)
# - UI: ClanWarDashboard, WarDeclarationForm, TerritoryMap, SiegePlanner, AlliancePanel, WarTimeline
# - Forms: DeclareWarForm, AllianceProposalForm, SiegeScheduleForm
# - Hooks: useClanWarStatus, useTerritoryControl, useAllianceRelations
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели войн: `ClanWar`, `Territory`, `Siege`, `Alliance`.
2. Реализовать эндпоинты объявления/отмены войны, управление фазами, расписаниями.
3. Добавить управление территориями: контроль, бонусы, upkeep, fortifications.
4. Разработать механики осад: планирование, тикеты осады, результаты, лог.
5. Описать награды: currency, buffs, cosmetic, leaderboards.
6. Настроить события event bus и WebSocket потоков.
7. Указать требования к безопасности, cooldowns, условиям войны.
8. Подготовить примеры JSON, UI сценарии, чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/gameplay/clans/wars`** – активные/прошедшие войны (пагинация, фильтры).
2. **POST `/api/v1/gameplay/clans/wars`** – объявить войну (targetClanId, reason, proposedStart, auditId).
3. **POST `/api/v1/gameplay/clans/wars/{warId}/cancel`** – отмена до начала (штрафы, возвраты).
4. **GET `/api/v1/gameplay/clans/wars/{warId}`** – детальная информация (фазы, таймлайн, участники, цели).
5. **POST `/api/v1/gameplay/clans/wars/{warId}/phase`** – переключить фазу (требует условий, голосования).
6. **GET `/api/v1/gameplay/clans/territories`** – список территорий, владельцы, бонусы, состояние укреплений.
7. **POST `/api/v1/gameplay/clans/territories/{territoryId}/fortify`** – укрепление (стоимость, время строительства).
8. **POST `/api/v1/gameplay/clans/wars/{warId}/sieges`** – запланировать осаду (цель, время, ресурсы).
9. **GET `/api/v1/gameplay/clans/wars/{warId}/sieges`** – активные/завершённые осады, результаты.
10. **POST `/api/v1/gameplay/clans/alliances`** – создать/обновить альянс, пакт о взаимопомощи.
11. **GET `/api/v1/gameplay/clans/alliances`** – дипломатические отношения (ally, neutral, hostile).
12. **GET `/api/v1/gameplay/clans/wars/{warId}/rewards`** – награды и распределение.
13. **POST `/api/v1/gameplay/clans/wars/{warId}/rewards/distribute`** – выдача наград (economy/inventory integration).
14. **GET `/api/v1/gameplay/clans/wars/leaderboard`** – рейтинг кланов, статистика войн.
15. **WS `/api/v1/gameplay/clans/wars/stream`** – события: `war-declared`, `war-phase-changed`, `territory-updated`, `siege-started`, `siege-ended`, `war-finished`.

---

## 🧱 Модели данных

- **ClanWar** – `warId`, `attackerClanId`, `defenderClanId`, `status`, `phases[]`, `startAt`, `endAt`, `allianceSupport`, `winCondition`, `score`.
- **WarPhase** – `phaseType` (`PREPARATION|BATTLE|TRUCE|AFTERMATH`), `startAt`, `endAt`, `objectives[]`.
- **Territory** – `territoryId`, `name`, `region`, `ownerClanId`, `fortificationLevel`, `resourceBonus`, `upkeepCost`, `lastContestedAt`.
- **Siege** – `siegeId`, `warId`, `targetTerritoryId`, `attackerPlan`, `defenderPlan`, `startAt`, `endAt`, `result`, `logs[]`.
- **Alliance** – `allianceId`, `clans[]`, `status`, `treaties[]`, `startAt`, `terms`.
- **WarReward** – `rewardType`, `payload`, `distribution`, `deliveredAt`.
- **WarEvent** – `timestamp`, `eventType`, `payload` (battle result, objective capture, casualty report).
- **WarLeaderboardEntry** – `clanId`, `warsWon`, `warsLost`, `territories`, `prestigePoints`.
- **RealtimeEventPayload** – события для WS потока (warDeclared, warPhaseChanged, territoryUpdated, siegeStarted, siegeEnded, warFinished).
- **Error Schema (`ClanWarError`)** – codes (`WAR_ALREADY_ACTIVE`, `TERRITORY_LOCKED`, `INSUFFICIENT_RESOURCES`, `ALLIANCE_CONFLICT`, `SIEGE_LIMIT`, `PHASE_NOT_ALLOWED`).

---

## 🧭 Принципы и правила

- Авторизация: `ClanLeader`/`Officer` (роль), `ServiceToken` для внутр. систем.
- Ограничения: cooldown на объявление войны, минимальный уровень клана.
- Экономика: налоги/upkeep списываются автоматически, награды распределяются через economy-service.
- Реалтайм: события транслируются в realtime-service, notification-service.
- Analytics: каждое событие войны логируется для live-ops.
- Безопасность: двухэтапное подтверждение для критичных действий (war declaration, alliance treaty).
- DRY: использовать общие компоненты (`responses.yaml`, `security.yaml`).

---

## 🧪 Примеры

- Объявление войны между кланами с временем подготовки.
- Планирование осады территории и получение результатов.
- Обновление союзов (альянс с третьим кланом).
- Выдача наград победителю и обновление лидерборда.
- WebSocket событие `siege-started` с данными боя.

---

## 🔗 Связности и зависимости

- Интегрирована с clan/guild system, progression, economy и realtime.
- Поддерживает UI админ-панели и clan UI (dashboard, map).
- Зависит от API задач 135 (guild system), 196 (realtime zones).

---

## ✅ Критерии приемки

1. `clan-war-system.yaml` создан и описывает все процессы войны и осад.
2. Модели войн, территорий, осад, наград, союзов задокументированы.
3. Прописаны события, ограничения, интеграции и примеры.
4. Чеклист выполнен, API готов для реализации.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают все фазы войны
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как отслеживается нарушение перемирия?**
**A:** Через события `war-phase-changed` и логи боя; система вешает штрафы, уведомляет обе стороны.

**Q:** Можно ли иметь несколько войн одновременно?**
**A:** Да, но ограничено конфигурацией (количество активных войн, осад) — документ указать лимиты.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

