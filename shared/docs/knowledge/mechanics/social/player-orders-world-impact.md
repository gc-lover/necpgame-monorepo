---

- **Status:** queued
- **Last Updated:** 2025-11-08 10:05
---


# Система заказов — влияние на мир

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-03  
**Последнее обновление:** 2025-11-08 10:05  
**Приоритет:** высокий

**target-domain:** world  
**target-microservice:** world-service (port 8092)  
**target-microservice-secondary:** economy-service (port 8089), social-service (port 8084)  
**target-frontend-module:** modules/world/insights, modules/social/player-orders

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 10:05  
**api-readiness-notes:** Определены мировые эффекты заказов: индексы экономики, события мирового состояния, интеграция с UX картой влияния и REST/Kafka контурами world/economy/social сервисов.

---

## 1. Ключевые эффекты

- **Экономика:** перераспределение капитала, рост сервисного сектора, динамическая стоимость заказов, изменение доступности ресурсов (`economy-service`).
- **Социум:** усиление сетей доверия, рост конфликтов, изменение репутации регионов, триггеры социальных кризисов (`social-service`).
- **Политика и фракции:** изменение дипломатии, запуск скрытых операций, корректировка налогов и законов в ответ на активность.
- **Мир и локации:** обновление статуса городов (heat-map), появление мировых событий, перераспределение NPC и торговых маршрутов (`world-service`).

---

## 2. Индикаторы влияния

| Индикатор | Описание | Порог действия | Интеграция |
|-----------|----------|----------------|------------|
| `OrderEconomicIndex` | Совокупный вклад заказов в экономику региона | >0.8 — стимулирует инвестиции, <0.3 — запускает поддержку | `economy-service` |
| `ServiceDemandIndex` | Спрос на сервисы сопровождения | >0.65 — открывает временные бонусные задания | `economy-service`, `world-service` |
| `ConflictHeatMap` | Уровень конфликтности | 0.7 — активирует world-events «Crisis» | `world-service`, telemetry |
| `TrustDelta` | Сдвиг доверия между фракциями | ±15% — корректирует дипломатические отношения | `social-service` |

---

## 3. События мирового состояния

- **Economic Surge:** генерация временных бустов для торговли и производства; требует подтверждения мониторингом.
- **Security Lockdown:** ограничение перемещения, рост стоимости охраны, запускает PvP-заказы на охрану.
- **Shadow Campaign:** скрытые операции фракций, изменяют дипломатические коэффициенты.
- **Reconstruction Drive:** мировое событие по восстановлению инфраструктуры, привязано к `city-life-population-algorithm.md`.

---

## 4. Механика регионального влияния

- Каждому городу присваиваются веса по категориям (экономика, безопасность, медиа); заказы обновляют веса каждые 15 минут.
- Пороговые значения активируют модификаторы: скидки на услуги, повышенные налоги, изменение доступности редких заказов.
- Результаты синхронизируются с `player-orders-world-impact-детально.md` для детальных DnD узлов и UX.

---

## 5. UX и аналитика

- **Карта влияния:** отображает индексы, события и прогнозы; данные берутся из `GET /world/player-orders/effects`.
- **Новости и дайджесты:** `GET /social/player-orders/news` формирует ленту, рассылаемую в UIS.
- **Dashboard аналитики:** отображение `OrderEconomicIndex`, `ServiceDemandIndex`, `ConflictHeatMap`, `TrustDelta`.
- **UX сценарии:** уведомления об угрозах, предложениях помощи, инвестиционных возможностях.

---

## 6. REST и JSON контуры

- `GET /world/player-orders/effects` — агрегированное влияние по городам и фракциям (`PlayerOrderImpact`).
- `POST /world/player-orders/effects/recalculate` — перерасчёт индексов и генерация world-events (batch job).
- `GET /economy/player-orders/index` — экономические индексы и прогнозы по секторам.
- `GET /world/player-orders/events` — активные мировые события, связанные с заказами.
- JSON схемы:  
  - `schemas/world/player-order-impact.schema.json`  
  - `schemas/world/player-order-event.schema.json`  
  - `schemas/economy/player-order-index.schema.json`

---

## 7. Kafka и асинхронные интеграции

| Topic | Producer | Payload | Консьюмеры |
|-------|----------|---------|-----------|
| `world.player-orders.impact` | world-service | `{ effectId, cityId, metrics[], issuedAt }` | telemetry, notification, quest-service |
| `economy.player-orders.index` | economy-service | `{ regionId, orderEconomicIndex, serviceDemandIndex }` | trading-service, analytics |
| `world.player-orders.crisis` | world-service | `{ crisisId, cityId, severity, triggers[] }` | factions-service, npc-ai |

- Все события логируются в telemetry (`tutorial-perception-success-rate`, `TickDuration p95`).

---

## 8. Зависимости и ссылки

- `player-orders-system-детально.md`, `player-orders-reputation-детально.md`, `player-orders-world-impact-детально.md`.
- `visual-style-locations-детально.md` — визуализация досок, метки активностей.
- `city-life-population-algorithm.md` — перераспределение NPC и инфраструктуры.

---

## 9. История изменений

- v1.1.0 (2025-11-08) — добавлены индексы, REST/Kafka контуры, UX сценарии и новые API задачи.
- v1.0.0 (2025-11-03) — выделение из `player-orders-system.md`.

