# Task ID: API-TASK-310
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:32
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-304], [API-TASK-307], [API-TASK-243]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы социальных правил для лодаутов (Combat Loadout Social Governance) в `social-service`: управление клановыми библиотеками, голосованиями, обязательными комплектами и мониторингом соблюдения.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для обмена лодаутами внутри кланов/отрядов, назначения обязательных пресетов, голосований и уведомлений о нарушениях.

---

## 🎯 Цель задания

Дать социальным структурам инструменты для координации лодаутов: клановые библиотеки, стандарты для рейдов, голосования за пресеты, контроль использования фракционных комплектов.

**Зачем это нужно:**
- Управлять обязательными лодаутами для клановых событий и рейдов.
- Делитьcя пресетами, голосовать и предлагать улучшения.
- Отслеживать соблюдение и поощрять выполнение стандартов.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Комплекты и переиспользование» — командные комплекты, фракционные библиотеки.
- Раздел «Обмен лодаутами между персонажами» — blueprintToken, sharing, аудит.
- Раздел «Политики фракционных комплектов» — ограничения и разрешения.
- Раздел «Интеграция с другими системами» — социальные функции, публичные лодауты, обязательные комплекты.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/clan-governance.md` (если есть)
- `.BRAIN/02-gameplay/combat/arena-system.md`
- `.BRAIN/02-gameplay/combat/loot-hunt-system.md`
- `.BRAIN/02-gameplay/social/social-features-overview.md`
- `.BRAIN/02-gameplay/economy/blueprint-market.md`
- `.BRAIN/_05-technical/backend/notification-system.md`

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-307-combat-loadout-blueprints-api.md`
- `API-SWAGGER/tasks/active/queue/task-243-social-resonance-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/social/loadouts/loadout-governance.yaml`  
**Формат:** OpenAPI 3.0.3 + AsyncAPI (при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── loadouts/
                ├── loadout-governance.yaml            ← создать
                ├── loadout-governance-components.yaml
                └── loadout-governance-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** social-service
- **Порт:** 8084
- **API Base:** `/api/v1/social/loadouts*`
- **Интеграции:** gameplay-service (loadouts), economy-service (blueprints), notification-service (сообщения), analytics-service (соблюдение), guild-service (управление составом).
- **Очереди:** Kafka `social.loadouts.*`, подписки на `blueprint.*`, `loadout.maintenance.*`, `notification.loadout.*`.

### Frontend
- **Модуль:** `modules/social/loadouts`
- **State Store:** `useSocialLoadoutStore`
- **UI компоненты:** `ClanLoadoutLibrary`, `MandatoryPresetPanel`, `VoteBoard`, `ComplianceDashboard`, `SuggestionQueue`, `MemberUsageTable`
- **Формы:** `MandatoryLoadoutForm`, `VoteCreationForm`, `ComplianceAppealForm`, `SuggestionSubmissionForm`
- **Хуки:** `useClanLoadouts`, `useMandatoryPresets`, `useLoadoutVotes`, `useComplianceTracking`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: social-service (port 8084)
# - API Base: /api/v1/social/loadouts*
# - Dependencies: gameplay, economy, notification, analytics, guild-service
# - Events: social.loadouts.*, consumes blueprint.*, loadout.maintenance.*, notification.loadout.*
# - Frontend Module: modules/social/loadouts (useSocialLoadoutStore)
# - UI: ClanLoadoutLibrary, MandatoryPresetPanel, VoteBoard, ComplianceDashboard, SuggestionQueue
# - Forms: MandatoryLoadoutForm, VoteCreationForm, ComplianceAppealForm, SuggestionSubmissionForm
# - Hooks: useClanLoadouts, useMandatoryPresets, useLoadoutVotes, useComplianceTracking
```

---

## ✅ Что нужно сделать (детальный план)

1. Извлечь из `.BRAIN` процессы обмена и социальных правил: клановые библиотеки, обязательные комплекты, голосования.
2. Спроектировать REST endpoints для библиотек, назначений, голосований, контроля соблюдения и жалоб.
3. Описать схемы `ClanLoadout`, `MandatoryPreset`, `Vote`, `VoteOption`, `VoteResult`, `ComplianceRecord`, `Suggestion`, `MemberUsage`.
4. Добавить endpoints для интеграции с blueprint API (импорт/экспорт), управления доступом и настройками (роль лидера/офицера).
5. Спроектировать события (`social.loadout.mandatory-set`, `social.loadout.vote-started`, `social.loadout.vote-completed`, `social.loadout.noncompliance`, `social.loadout.suggestion-submitted`) с payload и retry.
6. Прописать безопасность (guild roles, RBAC), аудит, rate limits.
7. Подготовить примеры запросов/ответов (создание обязательного пресета, голосование, проверка соблюдения).
8. Интегрировать с notification, analytics, economy (blueprints) — указать `$ref` и процессы.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/social/loadouts/clans/{clanId}/library` — библиотека клановых лодаутов (фильтры по роли, событию).
2. `POST /api/v1/social/loadouts/clans/{clanId}/library` — добавление/обновление лодаута (ссылка на blueprint, описание).
3. `DELETE /api/v1/social/loadouts/clans/{clanId}/library/{loadoutId}` — удаление/архивирование записи.
4. `POST /api/v1/social/loadouts/clans/{clanId}/mandatory` — назначение обязательного пресета (режим, событие, срок).
5. `GET /api/v1/social/loadouts/clans/{clanId}/mandatory` — список активных обязательных пресетов.
6. `POST /api/v1/social/loadouts/clans/{clanId}/votes` — запуск голосования по пресету/изменению.
7. `POST /api/v1/social/loadouts/clans/{clanId}/votes/{voteId}/ballot` — подача голоса (игрок, офицер).
8. `GET /api/v1/social/loadouts/clans/{clanId}/compliance` — мониторинг соблюдения (статистика, нарушения).
9. `POST /api/v1/social/loadouts/clans/{clanId}/compliance/{memberId}/appeal` — подача апелляции/объяснения.
10. `POST /api/v1/social/loadouts/clans/{clanId}/suggestions` — предложения по улучшениям (workflow).
11. `GET /api/v1/social/loadouts/clans/{clanId}/analytics` — аналитика использования (popularity, success rate).
12. `POST /api/v1/social/loadouts/clans/{clanId}/shares` — шаринг библиотек между союзными кланами.
13. `GET /api/v1/social/loadouts/clans/{clanId}/audit` — аудит всех действий (назначения, голосования, нарушения).
14. `POST /api/v1/social/loadouts/clans/{clanId}/notifications` — настройка внутренних уведомлений о лодаутах (подписки).

---

## 🧱 Модели данных

- **ClanLoadout** — `clanLoadoutId`, `clanId`, `blueprintId`, `roleTags`, `eventTags`, `ownerId`, `sharedScope`, `status`.
- **MandatoryPreset** — `mandatoryId`, `clanId`, `mode`, `eventCode`, `required`, `startAt`, `endAt`, `enforcedBy`.
- **Vote** — `voteId`, `clanId`, `topic`, `options[]`, `status`, `quorum`, `duration`, `initiator`.
- **VoteOption** — `optionId`, `description`, `votes`, `percentage`.
- **ComplianceRecord** — `recordId`, `memberId`, `loadoutId`, `status`, `violations`, `penalties`.
- **Suggestion** — `suggestionId`, `clanId`, `submittedBy`, `content`, `status`, `votes`.
- **MemberUsage** — `memberId`, `loadoutId`, `usageCount`, `winRate`, `lastUsedAt`.
- **ShareConfig** — `shareId`, `fromClanId`, `toClanId`, `scope`, `permissions`, `expiresAt`.
- **SocialLoadoutMetric** — `time`, `adoptionRate`, `complianceRate`, `votesHeld`, `suggestionsApproved`.
- **Async Events** — payloads `social.loadout.mandatory-set`, `social.loadout.mandatory-expired`, `social.loadout.vote-started`, `social.loadout.vote-completed`, `social.loadout.noncompliance`, `social.loadout.suggestion-submitted`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3 и AsyncAPI, вынести повторяющиеся схемы при необходимости.
- Использовать `$ref` на loadouts, availability, blueprint, notification компоненты.
- Обеспечить RBAC по уровням клана (лидер, офицер, член).
- Учесть GDPR/гильдийную политику, возможность удаления данных по запросу.
- События должны содержать данные для notification и analytics сервисов.
- Документировать обработку конфликтов между обязательными пресетами и индивидуальными лодаутами.
- Прописать rate limits и защиту от спама предложениями/голосованиями.

---

## ✅ Критерии приемки

1. Все эндпоинты описаны с параметрами, схемами, примерами.
2. Workflow обязательных пресетов и голосований задокументирован.
3. Интеграции с blueprint, availability, notification, analytics отражены.
4. Метрики и аудит библиотек доступны.
5. Security (RBAC), idempotency и rate limits описаны.
6. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Шаблон заполнен полностью, ссылки на `.BRAIN` корректны.
- [ ] OpenAPI/AsyncAPI проходит lint, компоненты вынесены при необходимости.
- [ ] Примеры покрывают добавление обязательного пресета, голосование, регистрацию нарушения.
- [ ] События синхронизированы с notification/analytics.
- [ ] Инструкции по обновлению mapping и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Можно ли сделать обязательный пресет только для части клана?  
**A:** Да, `MandatoryPreset` поддерживает таргет по ролям и рангу. Документировать поле `targetGroups`.

**Q:** Как обрабатываются нарушения?  
**A:** Через `compliance` endpoints и событие `social.loadout.noncompliance`. Возможны санкции (штрафы, предупреждения), связанные с economy-service.

**Q:** Поддерживается ли голосование по альтернативам?  
**A:** Да, `Vote` содержит несколько `VoteOption`; результаты публикуются событием `social.loadout.vote-completed`.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml`, обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-310).
- Согласовать спецификацию с social-service и blueprint/notification API.
- После генерации спецификации инициировать задачи для UI клановой библиотеки и модерации.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

