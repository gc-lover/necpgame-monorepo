# Task ID: API-TASK-217
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 02:24
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-127, API-TASK-128, API-TASK-182

---

## 📋 Краткое описание

Реализовать API управления аккаунтами и персонажами (создание, удаление, восстановление, слоты, кастомизация) на основе документа Part 1.

**Что нужно сделать:** Создать `api/v1/players/character-management.yaml`, описав REST/WS контракты для CRUD персонажей, проверки имён, кастомизации и управления слотами.

---

## 🎯 Цель задания

Обеспечить стабильный поток создания и администрирования персонажей, лежащий в основе стартового опыта игрока.

**Зачем это нужно:**
- Позволить игрокам создавать персонажей с кастомизацией и валидациями
- Управлять слотами, удалением, восстановлением, архивом
- Интегрировать профили аккаунта, настройки и ограничения
- Подготовить данные для UI, progression, inventory, session систем

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/player-character-mgmt/part1-creation-deletion.md`
**Версия:** v1.0.1 (2025-11-07 02:22)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Таблицы `players`, `characters`, `character_slots`
- Процедуры `createCharacter`, `deleteCharacter`, `restoreCharacter`
- Валидация имён, кастомизация, стартовые данные
- Распределение слотов (base + premium + perks)
- Архивация и soft-delete, очередь на удаление

### Дополнительные источники

- `.BRAIN/05-technical/backend/player-character-mgmt/part2-switching-management.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`
- `.BRAIN/05-technical/backend/authentication-authorization-system.md`
- `.BRAIN/05-technical/backend/party-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-127-player-character-management-api.md`
- `API-SWAGGER/tasks/active/queue/task-216-character-switching-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/players/character-management.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/players/
 ├── players.yaml
 ├── character-switching.yaml
 └── character-management.yaml  ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** character-service
- **Порт:** 8082
- **API Base Path:** `/api/v1/players`
- **Зависимости:**
  - auth-service – регистрация/аутентификация, лимиты аккаунтов
  - session-service – привязка активного персонажа, старт сессии
  - inventory-service – стартовые предметы и слоты
  - progression-service – стартовые уровни, XP, навыки
  - world-service – стартовые локации, spawn points
  - notification-service – подтверждения удаления/восстановления
  - analytics-service – статистика создания/удаления

### Frontend
- **Модуль:** `modules/auth/character-creation`
- **State Store:** `useCharacterCreationStore`
- **State:** `accountProfile`, `availableSlots`, `characters`, `customization`, `nameCheck`, `deleteQueue`
- **UI компоненты:** `CharacterCreationWizard`, `NameValidator`, `CustomizationPanel`, `SlotProgress`, `DeleteConfirmationModal`, `RestoreList`
- **Формы:** `CharacterCreationForm`, `RestoreCharacterForm`, `CustomizeAppearanceForm`
- **Layouts:** `AuthLayout`, `GameLayout`
- **Хуки:** `useCharacterSlots`, `useNameValidation`, `useRestoreQueue`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: character-service (port 8082)
# - API Base: /api/v1/players
# - Dependencies: auth, session, inventory, progression, world, notification, analytics
# - Frontend Module: modules/auth/character-creation (useCharacterCreationStore)
# - UI: CharacterCreationWizard, NameValidator, CustomizationPanel, SlotProgress, DeleteConfirmationModal, RestoreList
# - Forms: CharacterCreationForm, RestoreCharacterForm, CustomizeAppearanceForm
# - Hooks: useCharacterSlots, useNameValidation, useRestoreQueue
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить модели аккаунта и персонажей, стартовые данные, кастомизацию.
2. Описать эндпоинты: создание, удаление (soft delete), восстановление, проверка имён, слоты.
3. Добавить операции по управлению пресетами кастомизации, стартовыми loadout.
4. Настроить правила валидации: уникальность имен, фильтры по запрещённым словам.
5. Поддержать очередь удаления (timer), уведомления, аудиты.
6. Описать события (`character:created`, `character:deleted`, `character:restored`).
7. Указать интеграции с progression/inventory/world (стартовые пакеты).
8. Добавить примеры JSON, тест-кейсы, выполнить чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/players/profile`** – профиль аккаунта (слоты, настройки, статистика создания).
2. **GET `/api/v1/players/characters`** – список персонажей с состоянием (active, pending deletion, archived).
3. **POST `/api/v1/players/characters`** – создание персонажа (имя, кастомизация, стартовый preset).
4. **POST `/api/v1/players/characters/validate-name`** – проверка имени на уникальность/цензуру.
5. **GET `/api/v1/players/characters/customization-options`** – доступные опции кастомизации.
6. **POST `/api/v1/players/characters/{characterId}/appearance`** – обновить кастомизацию (до выхода из обучающего хаба).
7. **DELETE `/api/v1/players/characters/{characterId}`** – soft delete (перемещение в архив, таймер удаления).
8. **POST `/api/v1/players/characters/{characterId}/restore`** – восстановить персонажа из архива.
9. **GET `/api/v1/players/characters/restore-queue`** – очередь удалённых персонажей с таймерами.
10. **GET `/api/v1/players/slots`** – информация о слотах (базовые, открытые, premium).
11. **POST `/api/v1/players/slots/unlock`** – покупка/открытие дополнительного слота (валюта, токен).
12. **POST `/api/v1/players/characters/{characterId}/preset`** – сохранить/обновить пресет кастомизации/loadout.
13. **GET `/api/v1/players/characters/{characterId}`** – детальная информация (appearance, progression, metadata).
14. **POST `/api/v1/players/characters/{characterId}/archive`** – перенести в архив (для GM/поддержки).
15. **WS `/api/v1/players/characters/stream`** – события: `character-created`, `character-deleted`, `character-restore-available`, `slot-unlocked`.

---

## 🧱 Модели данных

- **PlayerProfile** – `accountId`, `totalPlaytime`, `premiumCurrency`, `settings`, `slots`, `limits`.
- **CharacterSummary** – `characterId`, `name`, `class`, `level`, `status`, `createdAt`, `pendingDeletionAt`, `lastPlayedAt`.
- **CharacterCreateRequest** – `name`, `class`, `appearance`, `origin`, `starterPackage`, `presetId`.
- **CharacterCreateResponse** – `characterId`, `startingLocation`, `slotsRemaining`, `tutorialEnabled`.
- **NameValidationResponse** – `isAvailable`, `violations[]`, `suggestions[]`.
- **DeleteRequest** – `reason`, `confirm`, `survey`, `auditId`.
- **RestoreQueueItem** – `characterId`, `name`, `expiresAt`, `restoreCost`, `status`.
- **SlotInfo** – `baseSlots`, `unlockedSlots`, `premiumSlots`, `maxSlots`, `nextUnlockCost`.
- **PresetDefinition** – `presetId`, `name`, `appearance`, `loadout`, `createdAt`.
- **RealtimeEventPayload** – union (`characterCreated`, `characterDeleted`, `characterRestoreAvailable`, `slotUnlocked`).
- **Error Schema (`CharacterManagementError`)** – codes (`NAME_TAKEN`, `NAME_INVALID`, `SLOT_LIMIT`, `DELETE_PENDING`, `RESTORE_EXPIRED`, `APPEARANCE_LOCKED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` для игрока, `ServiceToken` для GM/поддержки.
- Лимиты: базовые слоты (3), максимум 10 (премиум/перки). Удаление → 7-дневный таймер.
- Уникальность имён: проверка в `characters` + резервирование при создании.
- Безопасность: подтверждение email/SMS для удаления (конфигурируемо).
- События: `character:created` и др. публикуются для progression, analytics, notifications.
- Audit: операции удаления/восстановления требуют `auditId` и логируются.
- Кэширование: `characters` ответ можно кешировать на 5 секунд, POST инвалидирует.
- Доступность: API должен возвращать лилы для UI (подсказки, локализация).

---

## 🧪 Примеры

- Запрос на создание персонажа с кастомизацией и стартовым пакетом.
- Проверка имени и получение предложений.
- Мягкое удаление персонажа и просмотр таймера восстановления.
- Покупка нового слота за премиум валюту.
- Событие `character-restore-available` по WebSocket.

---

## 🔗 Связности и зависимости

- Интеграция с session API (создание стартовой сессии).
- Inventory/Progression для стартовых наборов и уровней.
- Notification-service для подтверждений удаления/восстановления.
- Analytics-service для трекинга создания/удаления персонажей.

---

## ✅ Критерии приемки

1. Файл `character-management.yaml` создан с полным описанием CRUD персонажей и управлением слотами.
2. Модели данных описывают профили, персонажи, пресеты, очередь, ошибки.
3. Описаны правила безопасности, подтверждения, интеграции и события.
4. Примеры и тест-кейсы представлены, чеклист выполнен.

---

## 📎 Checklist

- [ ] Соблюдён шаблон `api-generation-task-template.md`
- [ ] Указаны микросервис, модуль фронтенда, зависимости, UI компоненты
- [ ] Эндпоинты + WS покрывают все сценарии Part 1
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Нужно ли требовать подтверждение при удалении?**
**A:** Да, по умолчанию требуется подтверждение (код, пароль, MFA). API поддерживает флаг `requiresConfirmation` и уведомление.

**Q:** Можно ли восстановить персонажа после истечения таймера?**
**A:** Нет, после `restoreExpiresAt` персонаж удаляется безвозвратно; допускаются GM инструменты через админ API.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

