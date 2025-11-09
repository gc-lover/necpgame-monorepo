# Realtime Server Zones Feature
Консоль управления realtime-инстансами и распределением зон Night City.

**OpenAPI:** technical/realtime/server-zones.yaml | **Роут:** /technical/realtime-server-zones

## UI
- `RealtimeServerZonesPage` — SPA (380 / flex / 320), фильтры статуса/региона, auto drain toggle
- Компоненты:
  - `RealtimeInstanceCard`
  - `ZoneCard`
  - `TransferPlanCard`
  - `EvacuationPlanCard`
  - `CellHeatmapCard`
  - `TickRateChart`
  - `AlertFeedCard`

## Возможности
- Мониторинг realtime-инстансов (tickRate, load, зона типы)
- Зоны Night City, PvP статус и распределение игроков
- Планирование переноса зон и эвакуации игроков
- Heatmap cell/latency, tick duration, SLA alerts
- Компактная cyberpunk сетка на одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


