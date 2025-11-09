# Task ID: API-TASK-152
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:06 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для контрактов между игроками. 4 типа контрактов, escrow, collateral, арбитраж.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-contracts.md` (v1.0.0, ready)

**Ключевые механики:**
- 4 типа контрактов (exchange, service, courier, auction)
- Escrow system (третья сторона держит деньги)
- Collateral (залог)
- Репутационная система
- Арбитраж и dispute resolution
- Условия выполнения
- Автоматическое исполнение

---

## 📁 Целевой файл

`api/v1/economy/contracts.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/contracts/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/contracts  
**State Store:** useEconomyStore (contracts, disputes, escrow)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, ContractCard, EscrowDisplay, DisputeButton

**Готовые формы (@shared/forms):**
- ContractCreationForm, DisputeForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce
- useRealtime (для contract status)

---

## ✅ Endpoints

1. **POST /api/v1/economy/contracts** - Создать контракт
2. **POST /api/v1/economy/contracts/{id}/accept** - Принять контракт
3. **POST /api/v1/economy/contracts/{id}/complete** - Завершить контракт
4. **POST /api/v1/economy/contracts/{id}/dispute** - Открыть спор
5. **GET /api/v1/economy/contracts/available** - Доступные контракты

**Models:** Contract, Escrow, ContractTerms, Dispute

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-contracts.md`

