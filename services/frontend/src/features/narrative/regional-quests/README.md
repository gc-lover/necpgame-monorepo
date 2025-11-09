# Regional Quests Feature
Управление региональными квестами: daily/weekly пулы, мировые задания и повторяемые цепочки.

**OpenAPI:** narrative/regional-quests.yaml | **Роут:** /narrative/regional-quests

## UI
- `RegionalQuestsPage` — SPA (380 / flex / 320), фильтры региона/фракции, toggle repeatable
- Компоненты:
  - `DailyQuestCard`
  - `WeeklyQuestCard`
  - `RegionalQuestCard`
  - `WorldQuestCard`
  - `QuestAvailabilityCard`

## Возможности
- Daily/Weekly блоки с доступными слотами
- Региональные квесты по 8 макрорегионам
- Фракционные World quests с глобальным влиянием
- Отображение доступности и reset таймера
- Компактная киберпанк сетка в одном экране

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


