# Travel Events Feature
Перемещения по миру с событийными триггерами (эпохи, регионы, транспорт).

**OpenAPI:** gameplay/world/travel-events.yaml | **Роут:** /game/travel-events

## UI
- `TravelEventsPage` — SPA (380 / flex / 320), фильтры период/локация/транспорт, auto-generate toggle
- Компоненты:
  - `TravelEventCard`
  - `TravelEventsPeriodCard`
  - `TravelEventGenerationCard`
  - `TravelEncounterCard`

## Возможности
- Пять эпох (2030-2093) и разные режимы (on foot, vehicle, fast travel)
- Просмотр событий периода с характеристиками
- Генерация события с контекстом (origin, destination, last encounter)
- Оценка encounter риска и потенциальных наград
- Компактный киберпанк UI, всё на одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**

