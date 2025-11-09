# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-101  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 18:24  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-097 (character-management)

## Summary
Сформировать спецификацию `api/v1/gameplay/progression/progression-core.yaml`, покрывающую систему прогрессии персонажа: начисление опыта, уровень, атрибуты, навыки, выдачу очков, события и интеграции с combat/quest/character сервисами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/05-technical/backend/progression-backend.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 02:47) |

**Key points:** экспоненциальная формула опыта и LevelUp события; начисление опыта из combat, quest, skill usage; распределение атрибутов/навыков с капами и аудитом; события `character:level-up`, `character:skill-leveled`, `character:attribute-increased`.  
**Related docs:** `.BRAIN/02-gameplay/progression/progression-attributes.md`, `.BRAIN/02-gameplay/progression/progression-skills.md`, `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`, `.BRAIN/02-gameplay/combat/combat-shooting.md`, `.BRAIN/05-technical/backend/quest-engine-backend.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay/progression  
- **API directory:** `api/v1/gameplay/progression/progression-core.yaml`  
- **base-path:** `/api/v1/gameplay/progression`  
- **Java package:** `com.necpgame.gameplay`  
- **Frontend module:** `modules/progression/core`  
- **Shared UI/Form components:** `@shared/ui/LevelProgressBar`, `@shared/ui/SkillTree`, `@shared/ui/AttributeAllocation`, `@shared/forms/SpendAttributeForm`, `@shared/forms/SpendSkillPointsForm`, `@shared/layouts/GameLayout`, `@shared/state/useProgressionStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для получения состояния прогрессии, начисления опыта, распределения очков, настройки навыков и просмотра истории.
2. Смоделировать схемы данных (`ProgressionState`, `LevelInfo`, `ExperienceAwardRequest`, `AttributeSpendRequest`, `SkillProgress`, `SkillExperienceAward`, `ProgressionHistoryEntry`).
3. Учесть ограничения: капы уровней/атрибутов/навыков, блокировки при отсутствии очков, rate limits на spend операции.
4. Задокументировать события EventBus и их payload, интеграции с combat/quest/character/achievement/economy сервисами.
5. Добавить требования фронтенда, Orval клиента, примеры JSON для основных операций, ошибки и коды ответов.

## Endpoints
- `GET /characters/{characterId}` — текущее состояние прогрессии (level, exp, points, активные навыки).
- `POST /characters/{characterId}/experience` — начислить опыт (source: combat, quest, exploration, crafting).
- `POST /characters/{characterId}/experience/batch` — массовое начисление по событиям (батч от combat-service).
- `POST /characters/{characterId}/attributes/spend` — потратить атрибутные очки (включая валидацию капов).
- `POST /characters/{characterId}/skills/spend` — потратить skill points на дерево навыков.
- `POST /characters/{characterId}/skills/experience` — добавить опыт навыку (skill usage).
- `GET /characters/{characterId}/skills` — состояние всех навыков и xp до следующего уровня.
- `GET /characters/{characterId}/history` — журнал прогрессии (level ups, skill ups, распределение очков).
- `POST /characters/{characterId}/respec` — запрос на перераспределение (использует economy-service, опционально).
- `POST /characters/{characterId}/sync` — синхронизация с character-service после внешних изменений.

## Data Models
- `ProgressionState` — основные показатели: level, currentExp, expToNextLevel, unspentAttribute/SkillPoints, totals.
- `ExperienceAwardRequest` — `amount`, `source`, `reason`, `metadata`, поддержка распределения на party.
- `ExperienceBatchRequest` — массив событий (combat, quest, etc.) с multiplier/bonus полями.
- `AttributeSpendRequest` — список {attributeName, points} с проверкой капов и синхронизацией derived stats.
- `SkillProgress` — состояние каждого навыка (`currentLevel`, `experience`, `experienceToNextLevel`, `milestones`).
- `SkillExperienceAwardRequest` — `skillName`, `amount`, `source`, `criticalSuccess`.
- `ProgressionHistoryEntry` — тип события, значения до/после, timestamp, источник.
- `RespecRequest` — параметры ресета, стоимость, подтверждение оплаты.
- Ошибочные модели: `NoPointsError`, `AttributeCapError`, `SkillMaxLevelError`, `RespecBlockedError`.
- Использовать `$ref` на общие компоненты (`responses.yaml`, `pagination.yaml`, `security.yaml`).

## Integrations & Events
- Kafka topics: `gameplay.progression.level-up`, `gameplay.progression.skill-leveled`, `gameplay.progression.attribute-increased`, `gameplay.progression.experience-awarded`. Payload включает `characterId`, `level`, `skill`, `source`, `metadata`.
- Subscriptions: `combat.enemy-killed`, `quest.completed`, `skill.used`, `achievement.unlocked`, `economy.contract.completed`.
- REST зависимости: `character-service` (атрибуты, навыки), `quest-engine` (branch rewards), `economy-service` (ресурсы для respec), `notification-service` (уведомления), `telemetry-service` (аналитика).
- Метрики / аналитика: `AverageLevel`, `LevelUpRate`, `SkillProgressDistribution`, `ExperienceOverflow`.
- Frontend: `modules/progression/core`, Orval клиент `@api/gameplay/progression`, state hook `useProgressionStore`, уведомления о level up и skill milestones.

## Acceptance Criteria
1. Файл `api/v1/gameplay/progression/progression-core.yaml` создан, ≤ 500 строк, `info.x-microservice` настроен на gameplay-service.
2. Все endpoints задокументированы с параметрами, примерами и кодами ошибок; указаны ограничения и rate limits.
3. Модели данных отражают опыт, уровни, атрибуты, навыки, историю, включая примеры JSON.
4. Kafka события описаны с payload и списком агентов, которые их слушают; указаны retry policy и idempotency ключи.
5. Синхронизация с другими сервисами и зависимость от API-TASK-097 отмечены в разделе `Dependencies`.
6. Указаны требования к фронтенду и Orval клиенту, включены сценарии уведомлений UI.
7. Обновлён `tasks/config/brain-mapping.yaml` (API-TASK-101, статус `queued`, приоритет `critical`).
8. `.BRAIN/05-technical/backend/progression-backend.md` содержит секцию `API Tasks Status`.
9. `tasks/queues/queued.md` пополнен записью.
10. После реализации запускается `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\gameplay\progression\`.

## FAQ / Notes
- **Нужен ли отдельный файл для skill trees?** Если спецификация превысит лимит, вынести в `progression-skills.yaml`; пока держим core в одном файле.
- **Как обрабатывать respec?** Endpoint описывает workflow и зависимости на economy-service/consumables; реализация может быть опциональной.
- **Есть ли локализация для sources?** Использовать enum с кодами (`COMBAT`, `QUEST`, `CRAFTING`, `PVP`, `ACHIEVEMENT`, `EVENT`).

## Change Log
- 2025-11-09 18:24 — Задание создано (API Task Creator Agent)


