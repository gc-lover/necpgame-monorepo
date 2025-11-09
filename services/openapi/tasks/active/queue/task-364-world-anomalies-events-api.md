# Task ID: API-TASK-364
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-246 (live-events API), API-TASK-241 (world-interaction-suite API), API-TASK-299 (combat-loadouts API), API-TASK-320 (player-orders-economy-index API)

---

## 📋 Краткое описание

Спроектировать OpenAPI спецификацию `anomalies.yaml` для world-service, описывающую управление аномальными событиями (расписания, активация, визуальные эффекты, награды, аналитика) в мире NECPGAME.

**Что нужно сделать:** на основе `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-anomalous-easter-scenarios.md` подготовить полный REST API для orchestration аномалий, включая Kafka события, контроль анти-абьюза и интеграцию с социальными и экономическими системами.

---

## 🎯 Цель задания

Обеспечить world-service единым API для редких аномальных сценариев, чтобы:
- планировать и активировать события с учётом календарей, погодных триггеров и кооперативных условий;
- синхронизировать эффекты с социальными заказами, экономикой, визуальными системами и HUD;
- собирать телеметрию участия, награды и визуальные воздействия для аналитики и UI.

---

## 📚 Источники информации

### Основной источник
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-anomalous-easter-scenarios.md` — версия 1.0.0, обновлено 2025-11-08 16:51, статус approved / api-readiness: ready.

### Дополнительные документы
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — общие шкалы сложности и события.
- `.BRAIN/02-gameplay/social/reputation-formulas.md` — влияние на репутацию.
- `.BRAIN/04-narrative/dialogues/` (выдача кат-сцен) — ссылки для архивов `Эхо-письма`.
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — блок «Metropolis Threads & Subliminal Network» (решения от 2025-11-08 17:03).

---

## 📁 Целевая структура API

- **Репозиторий:** `API-SWAGGER`
- **Файл:** `api/v1/world/events/anomalies.yaml`
- **Формат:** OpenAPI 3.0.3 (YAML)
- **Версия API:** v1

Структура каталога:
```
api/
  v1/
    world/
      events/
        anomalies.yaml
```

Требования к спецификации:
- `info.x-microservice`:
  ```yaml
  info:
    title: World Anomalies API
    version: 1.0.0
    description: Управление аномальными событиями NECPGAME
    x-microservice:
      name: world-service
      port: 8086
      domain: world
      basePath: /api/v1/world
      package: com.necp.world.anomalies
  ```
- `servers`:
  - `https://api.necp.game/v1`
  - `http://localhost:8080/api/v1`
- Использовать общие компоненты (`api/v1/shared/common/security.yaml`, `responses.yaml`, `pagination.yaml`).

---

## 🏗️ Целевая архитектура

### Backend (world-service 8086)
- Точки входа: `/api/v1/world/anomalies/*`.
- Связанные сервисы: social-service (участники), economy-service (награды), inventory-service (выдача предметов), analytics-service, notification-service, auth-service.
- Kafka события:
  - `world.anomalies.lifecycle`
  - `world.anomalies.rewards`
  - `world.anomalies.visual-state`

### Frontend
- Модуль: `modules/world/events`
- Доп. подключение: `modules/social/orders`, `modules/economy/dashboard`, `modules/ui/hud`
- Состояние: `useWorldStore` (коллекции `anomalyCalendar`, `activeAnomalies`, `anomalyImpacts`)
- UI: `@shared/ui/HUDIndicator`, `@shared/ui/EventTimeline`, `@shared/ui/AlertBanner`, `@shared/forms/AnomalyOverrideForm`, `@shared/forms/RewardDistributionForm`
- Хуки: `useCountdown`, `useRealtime`, `useAnalyticsQuery`

---

## 🔧 Детальный план выполнения

1. Зафиксировать словарь аномалий (spectrum square, reverse river, resonance tower, echo archive, photon storm, sync station) и их поля (доступ, награды, триггеры, GM override).
2. Спроектировать REST endpoints для расписания, состояния, активации, завершения, фиксации появлений и метрик.
3. Описать модели данных (`AnomalySchedule`, `AnomalyEvent`, `AnomalyState`, `AnomalyRewardPackage`) с полями из SQL-схемы в документе .BRAIN.
4. Добавить раздел о Kafka сообщениях и ожиданиях payload (включая ретеншен, подписчиков).
5. Описать бизнес-правила (rate-limit, safety режим, анти-абьюз, fallback 72 часа).
6. Добавить раздел «Monitoring» с метриками Prometheus и alert правилами.
7. Подготовить примеры запросов/ответов (создание override, получение метрик, завершение события).
8. Проверить по чеклисту, согласовать рефы shared components, выполнить валидацию `swagger-cli validate`.

---

## 🌐 Endpoints (предварительный список)

1. `GET /api/v1/world/anomalies/schedule`
   - Возвращает расписание, ближайшие окна, тип триггера, GM override, `nextRun`.
   - Параметры: `anomalyType`, `window`, `includeHistory` (bool).

2. `GET /api/v1/world/anomalies/{anomalyId}/state`
   - Детали текущего состояния: фаза, таймер, активные эффекты, ограничения доступа, участие.
   - Поддержка заголовка `If-None-Match` (ETag).

3. `POST /api/v1/world/anomalies/{anomalyId}/activate`
   - Ручной запуск/принудительная активация (роль `world.supervisor`).
   - Тело (`AnomalyActivationRequest`): `activationMode`, `overrideReason`, `effectOverrides`, `visualProfile`.

4. `POST /api/v1/world/anomalies/{anomalyId}/complete`
   - Завершение события, расчёт наград, архивирование.
   - Тело (`AnomalyCompletionRequest`): `resolutionType`, `rewardSummary`, `analyticsSnapshot`.

5. `POST /api/v1/world/anomalies/{anomalyId}/sightings`
   - Регистрация проявлений (глитчи, визуальные эффекты, гражданские отчёты).
   - Используется для мониторинга визуальных аномалий и адаптации HUD.

6. `GET /api/v1/world/anomalies/insight-metrics`
   - Агрегированные показатели: `participants`, `avgDuration`, `economicImpact`, `hudIncidents`.
   - Параметры фильтра: `rangeStart`, `rangeEnd`, `metric`, `anomalyType`.

7. `POST /api/v1/world/anomalies/{anomalyId}/alerts`
   - (Доп.) Создание дополнительных уведомлений (push/voice) через notification-service.

Все endpoints обязаны ссылаться на общие ответы и коды ошибок `ANOMALY_*`, `ANOMALY_OVERRIDE_*`, `ANOMALY_VISUAL_*`.

---

## 🧱 Модели данных

- `AnomalySchedule`: `anomalyId`, `anomalyType`, `cronExpression`, `triggerType`, `gmOverrideEnabled`, `manualCooldownMinutes`, `nextRun`, `metadata`.
- `AnomalyEvent`: `eventId`, `anomalyId`, `triggerSource`, `phase`, `startedAt`, `endedAt`, `metrics`, `createdBy`, `activationMode`.
- `AnomalyState`: `eventId`, `phase`, `timeRemaining`, `activeEffects[]`, `accessRequirements`, `visualProfile`, `safetyMode`.
- `AnomalyRewardPackage`: `baseEddies`, `reputation`, `uniqueItems[]`, `seasonalCurrency`, `cooldown`.
- `AnomalySightingRequest`: `location`, `shard`, `visualIntensity`, `reporterId`, `evidenceUrl`.
- `AnomalyAnalyticsResponse`: `participants`, `completionRate`, `economyImpact`, `cityUnrestDelta`, `latency`.

---

## 📊 Бизнес-правила и мониторинг

- Fallback повтор через 72 часа при пропуске (автоматическое расписание).
- Rate limit активаций: 3 в сутки, логирование `overrideReason`.
- Safety mode: для игроков с `visual-effects=LOW` применять `AFTERGLOW_LOW`.
- Rewards: синхронизация с inventory-service; уникальные награды ограничены (weekly/monthly).
- Prometheus метрики: `world_anomaly_active_total`, `world_anomaly_override_total`, `world_anomaly_latency_ms`, `world_anomaly_visual_safety_total`.
- Алерты: `override_spike`, `participants_drop`, `latency_high`.

---

## ✅ Acceptance Criteria

1. Спецификация `api/v1/world/events/anomalies.yaml` соответствует OpenAPI 3.0.3 и проходит `swagger-cli validate`.
2. В `info.x-microservice` указан world-service (8086) и basePath `/api/v1/world`.
3. Все перечисленные endpoints задокументированы с примерами запросов и ответов.
4. Используются общие схемы безопасности, пагинации и ошибок из `api/v1/shared/common`.
5. Kafka события описаны в разделе `x-events` (payload, отправители, потребители).
6. Модели данных содержат все поля из `.BRAIN` (schedule, events, rewards, analytics).
7. Описаны и документированы rate limits, safety mode, GM override.
8. Добавлены примеры (`x-examples`) для активации и завершения аномалии.
9. Включены мониторинговые метрики и ожидания по SLA (latency, повтор).
10. `brain-mapping.yaml` обновлен: source `.BRAIN/.../anomalous-easter-scenarios.md` → target `api/v1/world/events/anomalies.yaml` со статусом `queued`.
11. Документ `.BRAIN/2025-11-07-anomalous-easter-scenarios.md` обновлён секцией `API Tasks Status` (статус `created`/`queued`).

---

## ❓FAQ

**Q:** Нужно ли объединять social endpoints в этой спецификации?  
**A:** Нет. Для social-service задач создаётся отдельный файл (API-TASK-365). В этой спецификации только world-service.

**Q:** Как учитывать влияние на UI и HUD?  
**A:** Указать необходимые компоненты и webhook уведомления в разделе архитектуры; для HUD используется `@shared/ui/HUDIndicator`.

**Q:** Какие доп. зависимости?  
**A:** Live events, voice lobby, player orders economic index — перечислены в зависимостях и должны учитываться при тестировании.

---

После реализации спецификации обязательно обновить `brain-mapping.yaml`, документ .BRAIN и запланировать генерацию клиентов в BACK-GO/FRONT-WEB.

