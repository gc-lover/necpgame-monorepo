# Progression Detailed Feature
Детализированная система прогрессии: атрибуты, матрицы, синергии и навыки.

**OpenAPI:** progression-detailed.yaml | **Роут:** /game/progression-detailed

## UI
- `ProgressionDetailedPage` — 3-колоночная панель (380 / flex / 320)
- Карточки на `CompactCard` и `ProgressBar`:
  - `AttributeDefinitionCard`
  - `AttributesMatrixCard`
  - `AttributeModifiersCard`
  - `SkillsMappingCard`
  - `SkillRequirementsCard`
  - `ClassBonusesCard`
  - `SynergiesCard`
  - `CapsCard`

## Возможности
- Обзор 9 атрибутов и их ростов
- Матрица стартовых значений по классам
- Расчёт модификаторов (база/экипировка/баффы)
- Маппинги навыков на предметы/импланты/классы
- Проверка требований предмета по навыкам
- Классовые бонусы и активные синергии
- Caps и лимиты с визуализацией прогресса

## Тесты
- Юнит-тесты для всех карточек в `components/__tests__` (написаны, **не запускались**)


