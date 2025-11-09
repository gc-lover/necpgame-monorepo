# Task ID: API-TASK-242
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 16:53
**Создатель:** AI Agent (Brain Manager)
**Зависимости:** [API-TASK-241]

---

## 📋 Краткое описание

Спроектировать REST/WS API economy-service для консоли Market Stabilizer: запуск интервенций, отслеживание статусов, аналитика влияния.

**Что нужно сделать:** Создать OpenAPI спецификацию `api/v1/economy/market/stabilizer.yaml` на основе world interaction документа и экономики.

---

## 🎯 Цель задания

Обеспечить управление рыночными интервенциями (freeze, reserve, incentives) для UI Market Stabilizer и World Pulse.

**Зачем это нужно:**
- Регулирование экономики игроками и администраторами.
- Настройка SLA и контроля за вмешательствами.
- Интеграция с World Pulse (уведомления о статусах).

---

## 📚 Источники информации

### Основной источник
- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-world-interaction-ui.md` (верс. 1.0.0, обновлено 2025-11-07 16:53)
  - Разделы 2.5, 3.3, 5.5, 6, 7, 12.5, 13.

### Дополнительные
- `.BRAIN/02-gameplay/economy/economy-analytics.md`
- `.BRAIN/02-gameplay/economy/economy-events.md`
- `.BRAIN/02-gameplay/economy/economy-logistics.md`
- `.BRAIN/05-technical/global-state/global-state-operations.md`
- Существующие OpenAPI: `api/v1/economy/market.yaml`, `api/v1/economy/currency-exchange.yaml` (использовать для консистентности моделей).

---

## 📁 Целевая структура API

- **Файл:** `api/v1/economy/market/stabilizer.yaml`
- **Доп. схемы:** `api/v1/economy/schemas/market-stabilizer.yaml`
- **Async события:** при необходимости `api/v1/economy/channels/market-intervention-events.yaml`

---

## 🏗️ Архитектура
- **Микросервис:** economy-service (8085)
- **Frontend модуль:** `modules/economy/market-terminal`
- **UI компоненты:** `MarketStabilizerConsole`, `InterventionCard`, `RiskMeter`
- **Security:** scopes `economy:interventions:read`, `economy:interventions:write`

---

## 📜 Требуемые эндпоинты
1. `GET /api/v1/economy/market/overview`
   - Возвращает текущие цены, riskIndex, последние интервенции.
2. `GET /api/v1/economy/market/interventions`
   - Параметры фильтрации: `status`, `type`, `createdBy`, `page`, `size`.
3. `POST /api/v1/economy/market/interventions`
   - Запрос запуска интервенции, поля: `type`, `duration`, `budget`, `justification`.
   - Требует MFA (header `X-MFA-Token`) и подтверждения world-service (см. зависимости).
4. `GET /api/v1/economy/market/interventions/{id}`
   - Детали статуса, прогресс, эффекты.
5. `POST /api/v1/economy/market/interventions/{id}/cancel`
   - Принудительное завершение (при провале или решении администрации).
6. `POST /api/v1/economy/market/interventions/{id}/confirm`
   - Подтверждение world-service smart contract (callback)
7. `GET /api/v1/economy/market/analytics`
   - Графики «цена vs интервенции», volatility, abuse detection.

### WebSocket события
- `MARKET_INTERVENTION_STATUS`
- `MARKET_RISK_CHANGED`

---

## 📦 Основные схемы
- `MarketOverview`
- `MarketInterventionRequest`
- `MarketIntervention`
- `MarketInterventionEffect`
- `RiskIndex`
- `MfaVerification`
- `InterventionAnalytics`

---

## ✅ Acceptance Criteria
1. Все эндпоинты описаны с примерами.
2. Указаны требования к MFA и idempotency (header).
3. Структуры ошибок используют `shared/responses.yaml`.
4. Описаны ограничения: max 2 активных интервенции/регион, cooldown 8h.
5. Добавлены поля SLA (execution ≤15s) и автроолбек условий.
6. WebSocket события описаны с payload.
7. Отражены связи с world-service (callback status).
8. Схемы вынесены в отдельный файл при необходимости (≤400 строк).
9. Прописаны security схемы (JWT + scope).
10. Добавлено `x-target-architecture` и ссылки на фронтенд модуль.

---

## FAQ / Примечания
- Типы интервенций: `PRICE_FREEZE`, `STRATEGIC_RESERVE`, `TAX_INCENTIVE`.
- Для симуляции предусмотреть параметр `simulate=true` (GET analytics?).
- Параметр `forecast` в overview для 24h прогноза.
- Поддержать audit trail (link на `api/v1/admin/world/metrics`).

---

## Checklist
- [ ] Анализ источников
- [ ] REST эндпоинты описаны
- [ ] WS события описаны
- [ ] MFA/idempotency отражены
- [ ] SLA задокументированы
- [ ] Схемы валидируются lint
- [ ] Обновить brain-mapping


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

