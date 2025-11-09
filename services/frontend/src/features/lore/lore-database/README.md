# Lore Database Feature
Командный центр лора: города, фракции, технологии, таймлайны, события, культура, боевые данные.

**OpenAPI:** lore/lore-database.yaml | **Роут:** /lore/database

## UI
- `LoreDatabasePage` — SPA (380 / flex / 320), фильтры разделов, Night City toggle
- Компоненты:
  - `CityLoreCard`
  - `FactionLoreCard`
  - `TechnologyLoreCard`
  - `TimelineLoreCard`
  - `EventLoreCard`
  - `CultureLoreCard`
  - `CombatAbilityCard`
  - `EnemyAICard`

## Возможности
- Детали городов (районы, timeline, контроль фракций)
- Фракции: тип, влияние, ключевые теги
- Технологии и Net архитектура
- Глобальные timeline события и корпоративные войны
- Исторические события, культура, сленг
- Боевые способности и ИИ противников
- Компактная киберпанк сетка, всё на одном экране

## Тесты
- Юнит-тесты для карточек — `components/__tests__`
- Написаны, **не запускались**


