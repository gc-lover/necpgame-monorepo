# Task ID: API-TASK-369
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-365 (social anomalies participants API), API-TASK-338 (player-orders-reviews API), API-TASK-343 (mentorship-programs API), API-TASK-340 (relationships-status API)

---

## 📋 Краткое описание

Сформировать спецификацию `subliminal-network.yaml` для social-service, описывающую подпольную сеть сигналов: HUD «Подмигиватель», почтовые коды, скрытые меню, `/knock` протокол, анти-спам и уведомления.

---

## 🎯 Цель

Создать API, позволяющее:
- активировать и отслеживать сигналы `/knock`, уведомления, кодовые письма;
- управлять opt-in/opt-out флагами игроков и mute санкциями;
- выдавать награды (`Afterglow UI`, `Encrypted Bookmark`, `Pixel Snap`);
- собирать аналитику и синхронизироваться с world/audio сервисами.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-subliminal-easter-network.md` (v1.0.0, ready).
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — подтверждение push шаблонов (2025-11-08 17:03).
- `.BRAIN/05-technical/backend/chat/chat-moderation.md` — фильтры токсичности.
- `.BRAIN/05-technical/backend/notification-system.md` — push каналы.
- `.BRAIN/02-gameplay/social/reputation-formulas.md`.

---

## 📁 Целевая структура

- **Файл:** `api/v1/social/subliminal-network.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    social/
      subliminal/
        network.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Subliminal Network API
  version: 1.0.0
  description: Управление подпольными сигналами, кодами и протоколом /knock
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    basePath: /api/v1/social
    package: com.necp.social.subliminal
```

---

## 🏗️ Архитектура

- **Backend:** social-service → взаимодействие с auth-service (opt-in, mute), world-service (billboard state), audio-service (markers), notification-service.
- **Kafka:** `social.subliminal.knock`, `social.subliminal.mail`, `social.subliminal.optin`.
- **Frontend:** `modules/ui/hud`, `modules/settings/ui`, `modules/social/mail`.
  - State: `useUiStore` (`subliminalSignals`, `knockCooldown`, `afterglowUnlocked`).
  - UI: `@shared/ui/HUDIndicator`, `@shared/ui/NotificationToast`, `@shared/forms/SettingsToggle`.

---

## 🔧 План

1. Извлечь из документа `.BRAIN` требования по rate limits, наградам, mute флагам.
2. Спроектировать endpoints: `/knock`, логи, redeem кодов, opt-in/out, уведомления.
3. Добавить выдачу наград и интеграцию с inventory-service.
4. Описать Kafka payload, безопасность, мониторинг.
5. Учесть анти-спам механизмы и фильтрацию токсичности.
6. Обновить mapping и `.BRAIN` документ.

---

## 🌐 Endpoints

1. `POST /api/v1/social/subliminal/knock`
   - Запуск `/knock`.
   - Тело: `channel` (GLOBAL/GUILD/PRIVATE), `payload`, `clientTimestamp`.
   - Ответ: 202 Accepted (cooldown, status).

2. `GET /api/v1/social/subliminal/knock/log`
   - История активаций (последние 50).
   - Параметры: `playerId`, `guildId`, `range`.

3. `POST /api/v1/social/subliminal/mail/redeem`
   - Ввод кодов из писем, выдача `Encrypted Bookmark`.

4. `GET /api/v1/social/subliminal/mail/history`
   - История кодов, статус (ACTIVE/CLAIMED/EXPIRED).

5. `POST /api/v1/social/subliminal/opt-in`
   - Включение/выключение подпольных сигналов (toggle).

6. `GET /api/v1/social/subliminal/status`
   - Сводка: opt-in, mute, afterglow прогресс, нарушения.

7. `POST /api/v1/social/subliminal/violations`
   - Запись нарушений (`violationType`, `evidence`, `cooldown`).

8. `POST /api/v1/social/subliminal/notifications`
   - Триггер шаблонов `ANOMALY_LIVE`, `/KNOCK_ALERT`.

---

## 🧱 Модели

- `KnockRequest`: `channel`, `payload`, `clientTimestamp`, `location`, `metadata`.
- `KnockResponse`: `status`, `cooldownSeconds`, `muteUntil`, `warnings`.
- `OptInStatus`: `playerId`, `optIn`, `lastChanged`, `changedBy`.
- `ViolationRecord`: `violationId`, `playerId`, `type`, `createdAt`, `muteUntil`, `notes`.
- `MailRedeemRequest`: `messageId`, `code`, `context`.
- `SubliminalStatus`: `optIn`, `knockCooldown`, `afterglowUnlocked`, `pixelSnapUnlocked`, `muteReason`.

---

## 📊 Бизнес-правила

- `/knock` rate limit: 1/10 минут, shard 120/10 минут.
- Нарушения >5/сутки → `knock_muted` на 24 часа.
- Opt-out пользователи получают недельные дайджесты.
- Награды: `Afterglow UI` после 3 HUD сигналов; `Pixel Snap` за билборд; синхронизация через inventory-service.
- Фильтрация payload через chat-moderation (токсичность, запрещенные слова).
- Логи сохраняются 30 дней (ClickHouse).

---

## ✅ Acceptance Criteria

1. Файл `api/v1/social/subliminal/network.yaml` валиден и использует shared компоненты.
2. `info.x-microservice` заполнен корректно (social-service 8084).
3. Все endpoints описаны с примерами и кодами ошибок `SUBSIGNAL_*`, `KNOCK_RATE_LIMITED`, `KNOCK_MUTED`.
4. Модели данных отражают требования документа .BRAIN.
5. Kafka события (`social.subliminal.knock`, `social.subliminal.mail`, `social.subliminal.optin`) задокументированы.
6. Прописаны правила rate limit, mute, оповещения.
7. Добавлены `x-examples` (удачный `/knock`, redeem кода, opt-out).
8. Обновлен `brain-mapping.yaml` (source `.BRAIN/.../subliminal-easter-network.md` → target `api/v1/social/subliminal/network.yaml`, статус `queued`).
9. `.BRAIN/2025-11-07-subliminal-easter-network.md` содержит обновлённый блок `API Tasks Status`.
10. Проведена интеграция с inventory-service в виде описанного hook'а в responses.

---

## ❓FAQ

- **Нужен ли WebSocket?** Нет, события идут через Kafka + push уведомления; в UI используется существующий realtime слой.
- **Где хранить mute-флаги?** В auth-service, но API возвращает состояние и отражает санкции.
- **Можно ли расширять каналами?** Да, предусмотреть enum с возможностью добавления.

---

После подготовки спецификации обновить mapping, документ .BRAIN и инициировать генерацию клиентов.

