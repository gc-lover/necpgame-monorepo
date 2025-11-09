# Task ID: API-TASK-271
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:15
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-266 (specter-helios balance API), API-TASK-267 (specter HQ suite API), API-TASK-265 (helios ops API), API-TASK-264 (city unrest API)

---

## 📋 Краткое описание

Сконструировать API/Frontend контракт для `Guild Contract Board` — единого интерфейса гильдий, связывающего Specter HQ контракты, Helios Ops и City Unrest действия.

**Что нужно сделать:** Создать (или обновить) спецификацию `guild-contract-board.yaml` (допустимо разделить на world/economy/social endpoints + frontend contract), описывающую REST, WebSocket, компоненты UI и телеметрию.

---

## 🎯 Цель задания

Обеспечить:
- Агрегированный `GET /board` (контракты Specter, Helios, City Response)
- Действия по контрактам (accept, progress, complete, distribute rewards)
- Отображение City Unrest, Helios alert, Specter prestige в реальном времени
- Управление ролями (`guild.officer`, `member`, `helios.collaborator`)
- Интеграции с Specter HQ магазином, Helios Ops, City Unrest API
- Telemetry и KPI UI/Backend взаимодействий

---

## 📚 Источники информации

- `.BRAIN/05-technical/ui/guild-contract-board.md` — UX потоки, API требования, telemetry, RBAC, ASCII мокап.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/02-gameplay/world/raids/specter-surge-loot.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/guilds/guild-contract-board.yaml` (или набор файлов по сервисам)  
**Микросервисы:** world-service (агрегация), economy-service (награды), social-service (уведомления), analytics-service  
**Frontend:** `modules/guild/contract-board`

---

## 🧩 Обязательные секции

1. `GET /api/v1/guilds/{guildId}/board` — агрегированные данные (Specter/Helios/City).
2. `GET /api/v1/guilds/{guildId}/contracts` — список контрактов (фильтр по статусу, фракции).
3. `POST /api/v1/guilds/{guildId}/contracts/{contractId}/accept` — принятие (с проверками ролей, лимитов).
4. `POST /api/v1/guilds/{guildId}/contracts/{contractId}/complete` — завершение (распределение наград).
5. `POST /api/v1/guilds/{guildId}/contracts/{contractId}/progress` — регистрация прогресса/фазы.
6. `POST /api/v1/guilds/{guildId}/helios-ops/{opId}/join` — интеграция с Helios Ops.
7. `GET /api/v1/world/city-unrest/state` (reuse) + в board ответ включить unrest info.
8. WebSocket `/ws/guilds/{guildId}/board` — обновления: `ContractUpdate`, `HeliosOpUpdate`, `CityUnrestUpdate`, `RewardDistributed`.
9. RBAC: role `guild.officer` для управления, `member` для участия.
10. Telemetry: `contract_viewed`, `contract_accepted`, `contract_completed`, `helios_op_joined`, KPI (acceptance time, completion rate).

---

## ✅ Критерии приемки

1. Target Architecture (microservices + frontend) описан в шапке.
2. Агрегированный `GET /board` возвращает секции `specterOps`, `heliosOps`, `cityResponse`, `recentResults`.
3. Контракты поддерживают статусы `pending`, `active`, `completed`, `failed`, `cooldown`.
4. Endpoints проверяют лимиты и weekly caps (429/409 при превышении).
5. В ответах присутствуют поля `impactOnUnrest`, `specterPrestigeDelta`, `heliosAlertDelta`.
6. Экономические награды передаются в economy-service с ledgerId.
7. Уведомления и объявления (social-service) описаны с payload.
8. WebSocket содержит описание payload и heartbeat (30 сек).
9. Телеметрия и KPI соответствуют документу (latency, success rate).
10. FAQ: сброс лимитов, rollback контрактов, отображение для Helios ролей, оффлайн награды.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

