# Task ID: API-TASK-365
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-364 (world anomalies API), API-TASK-317 (player-orders-creation API), API-TASK-338 (player-orders-reviews API), API-TASK-343 (mentorship-programs API)

---

## 📋 Краткое описание

Создать спецификацию `anomalies-participants.yaml` для social-service, описывающую работу с участниками аномалий: регистрацию действий, выдачу наград, историю участия, анти-спам `/knock` и мониторинг активности.

---

## 🎯 Цель задания

Дать social-service востребованный API, который:
- фиксирует участие игроков и гильдий в аномалиях;
- управляет наградами и их ограничениями;
- предоставляет историю участия и статистику для UI и аналитики;
- синхронизируется с world-service и inventory-service по Kafka событиям.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-anomalous-easter-scenarios.md` (v1.0.0, 2025-11-08 16:51, ready) — разделы REST/Kafka/структура данных.
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — решение по push-шаблонам (2025-11-08 17:03).
- `.BRAIN/02-gameplay/social/reputation-formulas.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/mail-system.md`

---

## 📁 Целевая структура

- **Репозиторий:** `API-SWAGGER`
- **Файл:** `api/v1/social/anomalies/participants.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** v1

```
api/
  v1/
    social/
      anomalies/
        participants.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Social Anomalies Participants API
  version: 1.0.0
  description: Управление участием игроков в аномальных событиях
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    basePath: /api/v1/social
    package: com.necp.social.anomalies
```

---

## 🏗️ Архитектура и интеграции

- **Backend:** social-service (8084), взаимодействует с world-service (получение состояния), inventory-service (выдача наград), auth-service (флаги mute), analytics-service (метрики).
- **Kafka:** `social.anomalies.participants` (publisher), подписчики world-service, analytics-service.
- **Frontend:** `modules/social/orders`, `modules/world/events`, `modules/ui/hud`.
  - state store: `useSocialStore` (`anomalyParticipants`, `rewardHistory`, `knockStatus`).
  - UI: `@shared/ui/ParticipantTable`, `@shared/ui/RewardHistory`, `@shared/forms/ParticipantFilterForm`.
  - hooks: `useRealtime`, `usePagination`, `useRateLimitNotice`.

---

## 🔧 План

1. Сконвертировать таблицу `social.anomaly_participants` в модели API (учитывая связи с `world.anomaly_events`).
2. Спроектировать endpoints регистрации участия, просмотра истории, получения наград, управления mute-статусами.
3. Добавить endpoint для анти-спам `/knock` (регистрация нарушений, выдача предупреждений).
4. Описать бизнес-правила: rate limits, санкции, cooldown уведомлений, ограничения наград.
5. Зафиксировать Kafka payload и связи с world-service (идентификаторы событий).
6. Задокументировать webhooks/notifications (`ANOMALY_LIVE`, `KNOCK_ALERT`).
7. Проверить через чеклист, обновить mapping и документ .BRAIN.

---

## 🌐 Endpoints (черновик)

1. `POST /api/v1/social/anomalies/{eventId}/participants`
   - Регистрация участия игрока/гильдии.
   - Тело: `participantId`, `guildId?`, `action` (JOINED, COMPLETED, SUPPORT), `timestamp`, `proof`.
   - Ответ: 201 Created (`ParticipantRecord`).
   - Rate limit: 3/мин/аккаунт, капча на превышении.

2. `GET /api/v1/social/anomalies/{eventId}/participants`
   - Список участников с фильтрами (`action`, `guildId`, `rewardStatus`).
   - Пагинация, сортировка по времени / репутации.

3. `GET /api/v1/social/anomalies/{eventId}/rewards`
   - История наград игрока/гильдии.
   - Параметры: `playerId`, `guildId`, `period`.

4. `POST /api/v1/social/anomalies/{eventId}/rewards`
   - Вручную инициировать выдачу наград (GM).
   - Тело: `playerIds[]`, `rewardPackageId`, `reason`.

5. `GET /api/v1/social/anomalies/{eventId}/stats`
   - Аггрегированная статистика участия (участники, среднее пребывание, аннулированные попытки).

6. `POST /api/v1/social/anomalies/{eventId}/violations`
   - Фиксация нарушения (spam, exploit).
   - Тело: `playerId`, `violationType`, `evidenceUrl`, `cooldownUntil`.

7. `POST /api/v1/social/anomalies/{eventId}/notifications`
   - Отправка напоминаний (push/email/mail) по шаблонам `ANOMALY_LIVE`, `KNOCK_ALERT`.

---

## 🧱 Модели

- `ParticipantRecord`: `recordId`, `eventId`, `playerId`, `guildId`, `action`, `timestamp`, `score`, `rewardsGranted`.
- `RewardPackage`: `rewardId`, `eddies`, `reputation`, `items[]`, `cooldowns`.
- `AnomalyStats`: `eventId`, `totalParticipants`, `uniqueGuilds`, `averageSession`, `violations`.
- `ViolationReport`: `violationId`, `playerId`, `type`, `createdAt`, `muteUntil`, `notes`.
- `NotificationRequest`: `channels[]`, `templateId`, `variables`, `targetSegments`.

---

## 📊 Бизнес-правила

- Нарушения >5/сутки → `knock_muted` на 24 часа.
- `/knock` cooldown: пользователь 10 минут, shard 120 активаций/10 минут.
- Награды ограничены: `Reverse Token` 3/месяц, `Glitch Veil` 1/неделю, `Looped Wave` единожды.
- Отчёты должны позволять GM отменять участие (`DELETE`/`PATCH`? описать в errors).
- Все операции журналируются (`auditId`, `performedBy`).

---

## ✅ Acceptance Criteria

1. Создана спецификация `api/v1/social/anomalies/participants.yaml`, проходящая валидацию OpenAPI.
2. В `info.x-microservice` указан social-service (8084).
3. Описаны все основные endpoints с примерами и кодами ошибок `ANOMALY_PARTICIPANT_*`, `ANOMALY_VIOLATION_*`.
4. Модели данных отражают структуру таблицы `social.anomaly_participants` и бизнес-правила.
5. Прописаны прометей-метрики (`social_anomaly_participants_total`, `social_anomaly_violations_total`, `knock_muted_total`).
6. В разделе `security` используется глобальный bearerAuth + scope `anomalies.manage`.
7. Kafka событие `social.anomalies.participants` добавлено в спецификацию с payload.
8. Описаны уведомления и шаблоны (`ANOMALY_LIVE`, `/KNOCK_ALERT`).
9. `brain-mapping.yaml` обновлён записью source → target с task_id API-TASK-365.
10. Документ `.BRAIN/2025-11-07-anomalous-easter-scenarios.md` содержит обновлённый блок `API Tasks Status` с ID 364 и 365.

---

## ❓FAQ

- **Можно ли объединить с world-аналогом?** Нет, world и social находятся в разных микросервисах и имеют отдельные base path / команды.
- **Как обрабатывать GM override?** Указать флаг `gmAction` и обязательное поле `reason`.
- **Нужно ли поддерживать gRPC?** Нет, только REST + Kafka.

---

После завершения спецификации не забыть синхронизировать клиентов (BACK-GO/FRONT-WEB) и обновить readiness-чеклист.

