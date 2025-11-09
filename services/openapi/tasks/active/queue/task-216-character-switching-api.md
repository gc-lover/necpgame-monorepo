# Task ID: API-TASK-216
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:08
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-127, API-TASK-198, API-TASK-199

---

## 📋 Краткое описание

Реализовать API для управления переключением персонажей, быстрой сменой активного персонажа в сессии, синхронизацией состояний и ограничениями аккаунта.

**Что нужно сделать:** Подготовить `api/v1/players/character-switching.yaml`, описав REST/WS эндпоинты, модели и правила из документа Part 2.

---

## 🎯 Цель задания

Обеспечить безопасное и удобное переключение персонажей без разрыва сессии и потери прогресса.

**Зачем это нужно:**
- Поддержать игровой UX «быстрой смены персонажа» без релога
- Гарантировать сохранение состояния текущего персонажа и загрузку нового
- Применить ограничения (cooldown, combat lock, queue)
- Интегрировать с session-management, progression, inventory, party системами

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/player-character-mgmt/part2-switching-management.md`
**Версия:** v1.0.1 (2025-11-07 02:23)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Switch character flow (save state, load new, update session)
- Server-side проверки: combat lock, instance lock, party restrictions
- Cooldowns, queue для платных переключений, RP-сценариев
- Character state snapshot (inventory, progression, location)
- API summary: switch, queue, cancel, status, presets

### Дополнительные источники

- `.BRAIN/05-technical/backend/player-character-mgmt/part1-creation-deletion.md`
- `.BRAIN/05-technical/backend/session-management/part1-lifecycle-heartbeat.md`
- `.BRAIN/05-technical/backend/session-management/part2-reconnection-monitoring.md`
- `.BRAIN/05-technical/backend/inventory-system/part2-advanced-features.md`
- `.BRAIN/05-technical/backend/progression-backend.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-127-player-character-management-api.md`
- `API-SWAGGER/tasks/active/queue/task-198-session-lifecycle-api.md`
- `API-SWAGGER/tasks/active/queue/task-199-session-reconnect-monitoring-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/players/character-switching.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + Events)

```
API-SWAGGER/api/v1/players/
 ├── players.yaml                  (базовый API персонажей)
 └── character-switching.yaml      ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** character-service
- **Порт:** 8082
- **API Base Path:** `/api/v1/players`
- **Зависимости:**
  - auth-service – проверка аккаунта, ролей
  - session-service – актуальная сессия, обновление токенов
  - inventory-service – сохранение/загрузка инвентаря
  - progression-service – сохранение XP, навыков
  - party-service – обновление состава группы
  - world-service – перемещение между зонами/инстансами
  - notification-service – уведомления о переключении

### Frontend
- **Модуль:** `modules/players/character-switching`
- **State Store:** `useCharacterStore`
- **State:** `activeCharacter`, `availableSlots`, `switchQueue`, `cooldowns`, `presets`
- **UI компоненты:** `CharacterSwitchPanel`, `CharacterCardList`, `SwitchCooldownTimer`, `SwitchQueueList`, `SwitchConfirmationModal`
- **Формы:** `SwitchRequestForm`, `SwitchPresetForm`
- **Layouts:** `GameLayout`
- **Хуки:** `useCharacterSwitch`, `useSwitchCooldown`, `useSwitchQueue`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: character-service (port 8082)
# - API Base: /api/v1/players
# - Dependencies: auth, session, inventory, progression, party, world, notification
# - Frontend Module: modules/players/character-switching (useCharacterStore)
# - UI: CharacterSwitchPanel, CharacterCardList, SwitchCooldownTimer, SwitchQueueList, SwitchConfirmationModal
# - Forms: SwitchRequestForm, SwitchPresetForm
# - Hooks: useCharacterSwitch, useSwitchCooldown, useSwitchQueue
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать switch flow: валидация, сохранение state snapshot, обновление session, загрузка нового персонажа.
2. Добавить эндпоинты для запроса переключения, подтверждения, отмены, статуса, очереди.
3. Указать ограничения: combat lock, instance lock, cooldown, premium fast switch.
4. Поддержать пресеты (favorite characters, quick slots) и уведомления.
5. Настроить event bus события (`character:switch-started`, `character:switch-completed`, `character:switch-failed`).
6. Интегрировать с session heartbeat/reconnect API (обновление токенов, reconnection window).
7. Обеспечить аудит (auditId) и логи для анти-абуза.
8. Добавить примеры JSON, сценарии тестирования, пройти чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/players/characters`** – список персонажей аккаунта с дополнительными полями (cooldowns, presets).
2. **POST `/api/v1/players/characters/switch`** – запрос на переключение (targetCharacterId, reason, presetId).
3. **GET `/api/v1/players/characters/switch/status`** – статус текущего переключения (step, eta, locks).
4. **POST `/api/v1/players/characters/switch/cancel`** – отмена переключения.
5. **POST `/api/v1/players/characters/switch/confirm`** – подтверждение (для premium fast switch, платных опций).
6. **GET `/api/v1/players/characters/switch/cooldowns`** – информация о перезарядках.
7. **GET `/api/v1/players/characters/switch/queue`** – очередь переключений (если используется batch).
8. **POST `/api/v1/players/characters/switch/preset`** – создать/обновить пресет (избранные персонажи, loadout).
9. **GET `/api/v1/players/characters/switch/preset`** – получить пресеты.
10. **POST `/api/v1/players/characters/switch/session-sync`** – ручной sync после переключения (inventory, progression, buffs).
11. **POST `/api/v1/players/characters/switch/relocate`** – переместить персонажа в безопасную зону при конфликте локаций.
12. **POST `/api/v1/players/characters/switch/admin/force`** – административное принудительное переключение (audit).
13. **GET `/api/v1/players/characters/switch/logs`** – история переключений.
14. **GET `/api/v1/players/characters/switch/locks`** – текущее состояние блокировок (combat, event, raid).
15. **WS `/api/v1/players/characters/switch/stream`** – события: `switch-started`, `switch-progress`, `switch-completed`, `switch-failed`, `switch-cooldown-updated`.

---

## 🧱 Модели данных

- **CharacterSwitchRequest** – `characterId`, `presetId`, `reason`, `fastSwitch`, `auditId`.
- **CharacterSwitchStatus** – `state` (`IDLE|SAVING|LOADING|COMPLETED|FAILED`), `step`, `eta`, `locks[]`, `warnings[]`.
- **CharacterSnapshot** – `inventoryChecksum`, `progression`, `location`, `party`, `buffs`.
- **SwitchCooldownInfo** – `characterId`, `cooldownEndsAt`, `fastSwitchAvailable`, `charges`.
- **SwitchQueueItem** – `queueId`, `requestedAt`, `priority`, `status`.
- **SwitchPreset** – `presetId`, `name`, `characterIds[]`, `default`, `createdAt`.
- **SwitchLogEntry** – `timestamp`, `source`, `oldCharacterId`, `newCharacterId`, `result`, `duration`, `auditId`.
- **SwitchLockInfo** – `type` (`COMBAT|INSTANCE|QUEST|EVENT|PARTY`), `active`, `expiresAt`, `details`.
- **RealtimeEventPayload** – union (`switchStarted`, `switchProgress`, `switchCompleted`, `switchFailed`, `switchCooldownUpdated`).
- **Error Schema (`CharacterSwitchError`)** – codes (`LOCK_ACTIVE`, `COOLDOWN_ACTIVE`, `SESSION_NOT_FOUND`, `QUEUE_FULL`, `SNAPSHOT_FAILED`, `FAST_SWITCH_DENIED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` (аккаунт), `ServiceToken` для системных процессов (admin/GM).
- Ограничения: нельзя переключаться в бою/рейде; cooldown 5 минут (конфигурируемо).
- Сохранность: обязательно сохранять snapshot текущего персонажа перед переключением.
- Сессии: обновлять авторизационные токены, heartbeat продолжать без обрыва.
- Аудит: операции логируются, `auditId` обязателен для fast switch/premium/GM.
- Инциденты: ошибки `SNAPSHOT_FAILED` → incident-service с критическим приоритетом.
- Кэширование: `GET characters` допускает `Cache-Control: max-age=5`; остальные POST/WS invalidate.
- DRY: использовать общие компоненты из `api/v1/shared/common/`.

---

## 🧪 Примеры

- Переключение между основным и альт-персонажем с подтверждением и уведомлением.
- Получение статуса переключения (этап SAVING) с активным combat lock.
- Создание пресета «Raid Team» и быстрый выбор персонажей.
- Событие `switch-completed` в WebSocket после успешной загрузки.
- Админское форсирование переключения для проблемного аккаунта.

---

## 🔗 Связности и зависимости

- Синхронизация с session lifecycle/reconnect API (`API-TASK-198/199`).
- Использует inventory/progression для состояния персонажа.
- Party-service обновляет активного персонажа в группе.
- Notification-service отправляет push/ingame уведы игроку/пати.

---

## ✅ Критерии приемки

1. Создан файл `character-switching.yaml` с архитектурным комментарием и полным набором эндпоинтов/WS.
2. Модели данных описывают запросы, статус, очереди, логи, события.
3. Определены правила безопасности, lock ограничения, auditing, интеграции.
4. Подготовлены примеры и сценарии тестирования; чеклист пройден.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, фронтенд модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают сценарии документа
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Нужно ли отключать heartbeat при переключении?
**A:** Нет, heartbeat остаётся активным, Session-service переводит сессию в `SWITCHING` и поддерживает соединение.

**Q:** Что делать, если snapshot не сохранён?
**A:** Переключение отменяется, возвращается ошибка `SNAPSHOT_FAILED`, инициируется incident и игрок остается на прежнем персонаже.

**Q:** Поддерживается ли fast switch без cooldown?
**A:** Да, при наличии premium charge или GM прав и успешной проверки блокировок; требуется подтверждение и аудит.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

