# Task ID: API-TASK-139
**Тип:** API Generation  
**Приоритет:** критический  
**Статус:** queued  
**Создано:** 2025-11-07 10:38  
**Создатель:** AI Agent  
**Зависимости:** API-TASK-128

---

## 📋 Краткое описание
**MVP блокер.** Требуется OpenAPI для боевых сессий: создание, действия, лаг-компенсация, награды.

**Что нужно сделать:** описать API gameplay-service по `.BRAIN/05-technical/backend/combat-session-backend.md`.

---

## 🎯 Цель задания
Предоставить контракт, который управляет PvE/PvP боями, шагами по ходам, расчетом урона, логами и наградами.

**Зачем это нужно:**
- Основной runtime боёв — без него нет игрового процесса.  
- Синхронизация клиента, серверной логики и аналитики.  
- Поддержка лаг-компенсации и realtime broadcast.

---

## 📚 Источники информации

### Основной источник
**Путь:** `.BRAIN/05-technical/backend/combat-session-backend.md`  
**Версия:** v1.0.0 · **Статус:** ready · **Дата:** 2025-11-07  

**Важно:**
- Combat instance lifecycle, turn order, damage calculation.  
- Death handling, respawn, loot, reward pipeline.  
- Combat logs, PvP/PvE зоны, лаг-компенсация.

### Дополнительные источники
- `.BRAIN/05-technical/backend/lag-compensation.md` — алгоритмы компенсации.  
- `.BRAIN/05-technical/backend/loot-system.md` — награды.  
- `.BRAIN/05-technical/backend/progression-backend.md` — опыт и уровни.  
- `.BRAIN/05-technical/backend/analytics-combat-dashboard.md` — логирование событий.

### Связанные документы
- `.BRAIN/02-gameplay/combat/combat-design.md` — геймдизайн боёв.  
- `.BRAIN/02-gameplay/combat/shooter-mechanics.md` — взаимодействие с оружием.  
- `.BRAIN/05-technical/backend/matchmaking-system.md` — предварительные очереди.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/gameplay/combat/combat-session.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/gameplay` и `http://localhost:8080/api/v1/gameplay`.

**Тип:** OpenAPI 3.0.3 · **Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                └── combat-session.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service  
- **Порт:** 8083  
- **API Base:** `/api/v1/gameplay/combat`  
- **Интеграции:** progression (exp), loot, analytics, matchmaking, notification-service, party-system.  
- **Комментарий в спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: gameplay-service (port 8083)
  # - API Base: /api/v1/gameplay/combat
  # - Dependencies: matchmaking-service, progression-service, loot-service, analytics-service, notification-service
  # - Frontend Module: modules/combat/session
  # - UI: CombatHUD, TurnOrderTimeline, DamageLog, StatusEffectBar
  # - Hooks: useCombatStore, useRealtime, useLagCompensation
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: gameplay-service
    port: 8083
    domain: gameplay
    base-path: /api/v1/gameplay/combat
    directory: api/v1/gameplay/combat
    package: com.necpgame.gameplayservice
  ```
- `servers` как указано.  
- `x-websocket`: `wss://api.necp.game/v1/gameplay/combat/sessions/{sessionId}/stream` — broadcast боевых событий.

### Frontend
- **Модуль:** `modules/combat/session`.  
- **State Store:** `useCombatStore` (`session`, `participants`, `timeline`, `logs`).  
- **UI:** CombatHUD, TurnOrderTimeline, DamageLog, StatusEffectBar, ActionBar.  
- **Формы/компоненты:** AbilityCastForm, ConsumableUseForm, LagCompensationForm.  
- **Хуки:** useRealtime, useLagCompensation, useInputBuffer.  
- **Layouts:** CombatLayout.

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Определить state модель сессии (создана, активна, завершена, аварийно завершена).  
- Описать структуру действий, лаг-компенсации, лога.  
- Финальный reward pipeline (exp, loot, achievements).

### Шаг 2. Endpoints
1. **POST `/api/v1/gameplay/combat/sessions`** — создание боевой сессии.  
2. **GET `/api/v1/gameplay/combat/sessions/{sessionId}`** — текущее состояние.  
3. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/actions`** — выполнение действия.  
4. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/turn/end`** — завершение хода.  
5. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/lag-compensation`** — пересчёт события.  
6. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/complete`** — выдача наград.  
7. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/abort`** — аварийное завершение.  
8. **GET `/api/v1/gameplay/combat/sessions/{sessionId}/log`** — история действий.  
9. **GET `/api/v1/gameplay/combat/sessions/{sessionId}/metrics`** — аналитика.  
10. **POST `/api/v1/gameplay/combat/sessions/{sessionId}/simulate`** — симуляции (design/debug, service token).

### Шаг 3. Модели
- `CombatSession`, `CombatParticipant`, `ActionRequest`, `DamagePreview`, `LagCompensationRequest/Response`, `SessionComplete`, `CombatLogEntry`, `CombatMetrics`.  
- Ошибки: `CombatError` (`VAL_INVALID_TARGET`, `BIZ_OUT_OF_TURN`, `BIZ_SESSION_FINISHED`).  
- WebSocket события: `actionExecuted`, `turnStarted`, `statusUpdated`, `sessionCompleted`.

### Шаг 4. OpenAPI оформление
- `paths` с методами и примерами.  
- Ссылки на `shared/common` (responses, security).  
- В `components` описать schema действий, участников, наград.  
- `security`: `BearerAuth`; для simulate/abort может требоваться `ServiceToken`.  
- Примеры действий (ability cast, damage preview), лаг-компенсации, завершения.

### Шаг 5. Проверки
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/gameplay/combat/`.  
- Проверить лимит 400 строк, README `gameplay/combat` актуализировать.  
- Обновить brain-mapping, `.BRAIN`, связанный README.

---

## 🔍 Критерии приемки
1. `info.x-microservice` = `gameplay-service`, порт `8083`, домен `gameplay`.  
2. Все публичные маршруты под `/api/v1/gameplay/combat`.  
3. Поддержаны действия, лаг-компенсация, награды, логи, метрики.  
4. WebSocket события описаны.  
5. Ошибки используют общую модель `Error`.  
6. Примеры включают ключевые сценарии.  
7. Валидаторы проходят без ошибок.  
8. Обновлены brain-mapping и `.BRAIN`.  
9. README `gameplay/combat` содержит актуальные эндпоинты.  
10. Описаны ограничения (тайм-аут хода, max участников).  
11. Симуляция/админ функции защищены `ServiceToken`.

---

## FAQ
- **Как работает лаг-компенсация?** Endpoint принимает событие с timestamp и возвращает пересчитанный результат.  
- **Можно ли пересоздать бой?** Только через abort + новое создание.  
- **Как логируются события?** Через analytics-service и combat log endpoint.  
- **Поддерживается cross-server бой?** В спецификации указать `SessionShard` поле.  
- **Как выдаются награды?** Через `complete`, интеграция с loot/progression сервисами.

---

**Источник:** `.BRAIN/05-technical/backend/combat-session-backend.md` (v1.0.0, ready)

