# Romance System Feature
Панель управления MEGA-ROMANCE-SYSTEM с 9 стадиями отношений, ревностью и романтическими событиями.

**OpenAPI:** romance-system.yaml | **Роут:** /game/romance-system (доступен также /game/romance)

## UI
- `RomanceSystemPage` — SPA страница (380px / flex / 320px)
- Компактные карточки на `CompactCard` из shared:
  - `RomanceNPCCard`
  - `RomanceRelationshipCard`
  - `RomanceEventCard`
  - `RomanceChoiceCard`
  - `RomanceSummaryCard`

## Возможности
- Фильтры по региону, стадии и минимальной совместимости
- Список доступных NPC с совместимостью и сложностью романса
- Активные отношения: affection/trust/jealousy, события, даты
- Романтические события и выборы с affection impact и skill-check
- Правила и советы по ревности, коммитменту, региональным особенностям

## Тесты
- Юнит-тесты для всех карточек (`components/__tests__`) — написаны, **не запускались** (по инструкции)


