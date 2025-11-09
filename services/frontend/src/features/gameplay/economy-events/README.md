# Economy Events Feature
Монитор экономических событий, влияния и предсказаний.

**OpenAPI:** economy-events.yaml | **Роут:** /game/economy-events

**⭐ UI на shared библиотеке (`GameLayout`, `CompactCard`, `CyberpunkButton`).**

## Функционал
- Просмотр активных событий (тип, серьезность, регионы)
- Аналитика влияния на сектора, индекс цен
- История событий и их эффектов
- AI-предсказания будущих событий
- Фильтры по типу и серьезности

## Компоненты
- **EconomyEventsPage** — SPA (380px | flex | 320px) с фильтрами
- **EconomyEventCard** — краткая карточка события
- **EconomyImpactCard** — общий индекс и сектора
- **EventHistoryCard** — недавние события
- **EventPredictionsCard** — AI прогнозы

## Примечания
- Шрифты 0.65–0.875rem, киберпанк стиль
- Тесты добавлены (не запускались)
- Готово к интеграции с `economy-events` API


