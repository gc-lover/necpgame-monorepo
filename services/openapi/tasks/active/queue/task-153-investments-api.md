# Task ID: API-TASK-153
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:08 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для инвестиций. 5 типов инвестиций, portfolio management, ROI расчёты.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-investments.md` (v1.0.0, ready)

**Ключевые механики:**
- 5 типов инвестиций (corporate, faction, regional, real estate, production chains)
- Portfolio management
- Диверсификация рисков
- Risk analysis
- ROI расчёты
- Дивиденды
- Инвестиционные фонды

---

## 📁 Целевой файл

`api/v1/economy/investments.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/investments/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/investments  
**State Store:** useEconomyStore (portfolio, investments, roi)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, InvestmentCard, PortfolioChart, ROIDisplay, RiskIndicator

**Готовые формы (@shared/forms):**
- InvestmentForm, WithdrawForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для ROI updates)
- useDebounce

---

## ✅ Endpoints

1. **POST /api/v1/economy/investments/invest** - Инвестировать
2. **GET /api/v1/economy/investments/portfolio** - Портфель инвестиций
3. **POST /api/v1/economy/investments/withdraw** - Вывести средства
4. **GET /api/v1/economy/investments/opportunities** - Доступные инвестиции
5. **GET /api/v1/economy/investments/roi** - ROI расчёт

**Models:** Investment, Portfolio, InvestmentOpportunity, ROI

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-investments.md`

