# Narrative Coherence Feature
Монитор целостности сюжета Night City.

**OpenAPI:** narrative-coherence.yaml | **Роут:** /narrative/coherence

## UI
- `NarrativeCoherencePage` — SPA (380 / flex / 320) с фильтрами и командным центром
- Карточки на `CompactCard`/`ProgressBar`:
  - `PlotThreadCard`
  - `ArcStatusCard`
  - `NarrativeRiskCard`
  - `ContinuityAlertCard`
  - `NarrativeSummaryCard`

## Возможности
- Отслеживание сюжетных нитей, арок и ветвлений
- Риски целостности, предупреждения и действия
- Сводка: количество нитей, арок, открытых событий, общий уровень coherence
- Фильтры по типу нитей и severity алертов
- Лёгкая киберпанк сетка, компактные шрифты (0.65–0.875rem)

## Тесты
- Unit-тесты для карточек (в `components/__tests__`) — написаны, **не запускались**

