# Logistics Feature
Система логистики и доставки - 5 типов транспорта, convoy, insurance (детализированная система).

**OpenAPI:** logistics.yaml | **Роут:** /game/logistics

## Функционал
- **Типы транспорта:**
  - ON_FOOT (пешком) - дешево, медленно, малый груз
  - MOTORCYCLE - быстро, средний риск
  - CAR - balanced (сбалансированный)
  - TRUCK - большой груз, медленно
  - AERODYNE - быстро, дорого, низкий риск
- **Маршруты:**
  - Local (локальные)
  - Regional (региональные)
  - Global (глобальные)
- **Механики:**
  - Shipment tracking (отслеживание доставок)
  - Risk management (управление рисками)
  - Cargo insurance (страхование груза - 3 плана)
  - Convoy & escort (конвой и эскорт)
  - Delivery speed (скорость доставки)
  - Cargo management (вес, объём)
  - Real-time tracking (отслеживание в реальном времени)
- **Риски:**
  - Ambush (засада)
  - Weather (погода)
  - Mechanical (поломка)
  - Accidents (аварии)
- **Insurance Plans:**
  - Basic (50% компенсация)
  - Standard (75% компенсация)
  - Premium (100% компенсация)

## Компоненты
- **LogisticsPage** — SPA на `GameLayout`, кнопки `CyberpunkButton`, compact UI (380px | flex | 320px)
- **ShipmentCard** — компактная карточка доставки (транспорт, риск, ETA)
- **RoutesCard** — варианты маршрутов (distance, risk, рекомендуемый транспорт)
- **VehicleStatsCard** — сравнение транспорта (скорость, грузоподъемность, риск, стоимость)
- **InsurancePlansCard** — планы страхования и бонусы
- **RiskMatrixCard** — риски и стратегии смягчения
- **ConvoyStatusCard** — состояние эскорта и сила конвоя

## Механики
- Создание доставки
- Выбор транспорта
- Страхование груза
- Конвой для защиты
- Отслеживание статуса

**Соответствие:** SPA, компактный 3-колоночный UI, шрифты 0.65-0.875rem, shared/ui, MUI.

