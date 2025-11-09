# Task ID: API-TASK-239
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 16:30
**Создатель:** GPT-5 Codex (Brain Manager)
**Зависимости:** API-TASK-158, API-TASK-209, API-TASK-236

---

## 📋 Краткое описание

Спроектировать OpenAPI спецификацию сезонного социального эвента «Neon Popularity Reset», включая рейтинговую экономику, анти-абьюз механики и выдачу наград (Neon Merit, Jun Coin, титул «Mirror Sovereign»).

**Что нужно сделать:** Создать `api/v1/gameplay/social/neon-popularity-reset.yaml`, описав REST/WS интерфейсы для управления эвентом, рейтингов, наград, жалоб и уведомлений.

---

## 🎯 Цель задания

Обеспечить `social-service` и фронтенд готовым контрактом для сезонного события с гибридной антиутопической эстетикой и экономикой.

**Зачем это нужно:**
- Управлять 6-недельным циклом эвента и рейтингами игроков/гильдий.
- Выдавать валюты `Neon Merit`, `Jun Coin` и уникальные визуальные награды.
- Поддерживать UX анти-абьюза (анализ аномалий, апелляции, штрафы).
- Синхронизировать уведомления, UI фильтры и социальные баффы.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/06-tasks/active/CURRENT-WORK/archive/2025-11-07-hybrid-media-references-expansion.md`
**Версия:** v1.1.0 (2025-11-07 16:14)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- «Сезонный эвент “Neon Popularity Reset”» (циклы, награды, валюты).
- «UX и анти-абьюз: резюме» + «UX анти-абьюз рейтинга» (раздел 9).
- «Moodboard и аудио-пакеты» (ID `mood/neon-v1`, `audio/neon-loop`).

### Дополнительные источники

- `.BRAIN/06-tasks/ideas/2025-11-07-IDEA-hybrid-media-references.md`
- `.BRAIN/02-gameplay/social/reputation-tiers-detailed.md`
- `.BRAIN/02-gameplay/social/social-mechanics-overview.md`
- `.BRAIN/02-gameplay/economy/economy-currencies-detailed.md`
- `.BRAIN/05-technical/backend/notification-system.md`

### Связанные документы

- `API-SWAGGER/api/v1/social/reputation-system.yaml`
- `API-SWAGGER/api/v1/notifications/notifications.yaml`
- `API-SWAGGER/api/v1/gameplay/social/mentorship-system.yaml`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/gameplay/social/neon-popularity-reset.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/gameplay/social/
 ├── reputation-system.yaml
 ├── mentorship-system.yaml
 ├── social-hubs.yaml
 ├── neon-popularity-reset.yaml    ← создать
 └── ...
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

```yaml
# Target Architecture:
# - Microservice: social-service (порт 8084), с модулем rating-events
# - Base Path: /api/v1/social/events/neon-reset
# - Subsystems: rating-engine, anti-abuse analyzer, reward distributor, notification integrator
# - Datastores: Postgres (tables neon_event_states, neon_ratings, neon_appeals), Redis cache neon:ratings:{segment}
# - Stream Processing: Kafka topic neon.rating.anomaly -> notification-service & analytics-service
# - Frontend Module: modules/social/neon-reset (store useNeonResetStore)
# - UI Components: NeonEventDashboard, RatingBloomOverlay, AppealFormModal, RewardTrackCarousel
# - Analytics: export to analytics-service dashboards (participation_rate, appeal_rate, diamond_share)
```

---

## ✅ Что нужно сделать (детальный план)

1. Смоделировать жизненный цикл эвента (период 6 недель, фазы warm-up, active, cooldown).
2. Описать рейтинговую систему с порогами (Bronze → Diamond) и весом 20% в гильдийском social score.
3. Реализовать выдачу валют (`Neon Merit`, `Jun Coin`) и наград (ауры, эмоции, виджет HUD, баффы).
4. Добавить механики анти-абьюза: детект аномалий, подтверждения, апелляции, штрафы, блокировки.
5. Подготовить WebSocket поток обновлений для UI (рейтинги, предупреждения, баффы).
6. Интегрировать уведомления (toast, баннеры, почта) через notification-service.
7. Определить API для гильдийского отчёта и лидерборда.
8. Задокументировать метрики мониторинга (Grafana: participation_rate, anomaly_rate, appeal_sla).

---

## 🔀 Endpoints (минимальный набор)

1. **GET `/events/neon-reset/config`** – параметры сезона (даты, веса, награды, фильтры).
2. **POST `/events/neon-reset/start`** – запуск нового цикла, установка конфигурации.
3. **GET `/events/neon-reset/ratings`** – глобальный рейтинг (пагинация, фильтры, сегменты).
4. **GET `/events/neon-reset/player/{playerId}`** – персональный статус (рейтинг, баффы, штрафы).
5. **POST `/events/neon-reset/ratings/{playerId}`** – обновление рейтинга (веса, источники, модерация).
6. **POST `/events/neon-reset/rewards/claim`** – получение наград по достигнутому порогу (Bronze→Diamond).
7. **GET `/events/neon-reset/guilds/{guildId}`** – вклад гильдии, штрафы, бонусы.
8. **POST `/events/neon-reset/anomalies/report`** – регистрация аномального поведения (детекторы).
9. **POST `/events/neon-reset/anomalies/confirm`** – подтверждение пользователем, что действия легитимны.
10. **POST `/events/neon-reset/appeals`** – подача апелляции (форма жалобы, вложения).
11. **PATCH `/events/neon-reset/appeals/{appealId}`** – обновление статуса апелляции модератором.
12. **POST `/events/neon-reset/penalties`** – применение санкций (рейтинговый штраф, блок отзывов).
13. **GET `/events/neon-reset/rewards/track`** – таблица наград и прогресс по уровням.
14. **GET `/events/neon-reset/notifications`** – расписание уведомлений (T-24h, T-1h, T-5m, start, end).
15. **WS `/events/neon-reset/stream`** – события `rating-updated`, `anomaly-detected`, `appeal-opened`, `penalty-applied`, `reward-claimed`, `tier-unlocked`.

---

## 🧱 Модели данных

- **NeonEventConfig**: `cycleId`, `startAt`, `endAt`, `weights`, `rewardTable`, `participationRules`, `guildWeight`.
- **NeonRatingEntry**: `playerId`, `guildId`, `tier`, `score`, `trend`, `lastUpdated`.
- **NeonReward**: `tier`, `neonMerit`, `junCoin`, `cosmetics`, `buffs`, `unlockConditions`.
- **AnomalyRecord**: `anomalyId`, `playerId`, `detector`, `signals`, `severity`, `issuedAt`, `status`.
- **ConfirmationPayload**: `anomalyId`, `playerStatement`, `answers`, `confirmedAt`.
- **AppealRequest**: `appealId`, `playerId`, `anomalyId`, `reason`, `attachments`, `status`, `slaDue`.
- **PenaltyAction**: `penaltyId`, `playerId`, `type`, `duration`, `ratingDelta`, `flags` (`rating_suppressed`).
- **GuildScoreSnapshot**: `guildId`, `cycleId`, `averageRating`, `participation`, `penalties`.
- **NotificationPlan**: `schedule`, `channel`, `templateId`, `audience` (player segments, guild leaders).

---

## 🧭 Принципы и правила

- **Cycle cadence:** эвент активируется каждые 6 недель, API должно хранить историю предыдущих циклов.
- **Weighted contributions:** топ-10% могут терять право голоса в финальном голосовании (только наблюдение).
- **Anti-abuse SLA:** апелляции обрабатываются ≤12 часов, все состояния логируются.
- **Reward integrity:** `Jun Coin` необмениваемые, `Neon Merit` авто-конвертируется 1:1 в reputation при капе 6000.
- **Localization & theming:** ответы содержат theme tokens (`loop_flux`, `mirror_sovereign`) для UI.
- **Privacy:** обработка апелляций и аномалий должна скрывать персональные данные от других игроков.

---

## 🧪 Примеры

- Игрок достигает уровня `Gold` → получает 1400 `Neon Merit`, 50 `Jun Coin`, аура «Loop Flux» и дебаф комиссий.
- Детектор фиксирует подозрительные отзывы → создаётся `AnomalyRecord`, игроку приходит модальное окно с подтверждением.
- Гильдия пропускает сезон → штраф -10% к накопленным бонусам, отображается предупреждение лидеру.
- Игрок из Top-1% достигает `Diamond` → выдаётся титул «Mirror Sovereign», виджет HUD, push уведомление.

---

## 🔗 Связности и зависимости

- `notification-service` – рассылка уведомлений (toast, email, push).
- `analytics-service` – обработка статистики (participation, appeals, anomaly rate).
- `economy-service` – конвертация `Neon Merit`, учёт `Jun Coin`.
- `social-service` – интеграция с общим social influence score гильдий.
- `reputation-system` – обновление базовой репутации и баффов.
- `maintenance-mode` (API-TASK-236) – пауза эвента во время maintenance.

---

## ✅ Критерии приемки

1. `neon-popularity-reset.yaml` содержит REST/WS описание всех стадий эвента.
2. В спецификации отражены наградные таблицы и валюты из документа .BRAIN.
3. Реализованы модели анти-абьюза, апелляций и уведомлений согласно UX потокам.
4. Описаны интеграции с notification-, analytics-, economy-, social-service.
5. Добавлены примеры ответов для каждого рейтингового уровня и кейсов анти-абьюза.
6. Прописаны метрики мониторинга и параметры цикла (6 недель, warm-up/active/cooldown).
7. Все зависимости с существующими API задокументированы, конфликтов нет.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

