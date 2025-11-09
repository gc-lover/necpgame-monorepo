# Starter Content Feature
Органайзер стартовых историй и квестов (origin, class, main story, tutorials).

**OpenAPI:** narrative/starter-content.yaml | **Роут:** /narrative/starter-content

## UI
- `StarterContentPage` — SPA (380 / flex / 320), фильтры класса/периода, toggles
- Компоненты:
  - `OriginStoryCard`
  - `ClassQuestCard`
  - `MainStoryQuestCard`
  - `StarterProgressionCard`
  - `RecommendedContentCard`

## Возможности
- Просмотр origin stories и стартовых локаций
- Классовые квесты с наградами и типами
- Основной сюжет по периодам с целями
- Рекомендации (origin + класс + туториалы)
- Прогрессия шагов для новых игроков
- Компактная киберпанк сетка под один экран

## Тесты
- Юнит-тесты для карточек в `components/__tests__`
- Написаны, **не запускались**


