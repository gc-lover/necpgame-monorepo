# NPC Hiring Extended Feature
Командный центр для найма NPC: контракты, миссии, поддержка, программы лояльности.

**OpenAPI:** npc-hiring-extended.yaml | **Роут:** /game/npc-hiring-extended

## UI
- `NPCHiringExtendedPage` — SPA (380 / flex / 320), фильтры по типу, риску, легендарности
- Карточки на `CompactCard`/`ProgressBar`:
  - `HireableNPCExtendedCard`
  - `HiringContractCard`
  - `MissionAssignmentCard`
  - `SupportAssetCard`
  - `LoyaltyProgramCard`
  - `HiringSummaryCard`

## Возможности
- Каталог кандидатов (tier, legendary, KPI, traits)
- Контракты с риском, бонусами и условиями
- Назначение миссий, состав отряда, успех
- Поддержка: дроны, VTOL, экипировка
- Программа лояльности и суммарные метрики найма
- Малые шрифты (0.65–0.875rem), киберпанк токены, 3‑колоночная сетка

## Тесты
- Юнит-тесты для всех карточек (`components/__tests__`) — написаны, **не запускались**

