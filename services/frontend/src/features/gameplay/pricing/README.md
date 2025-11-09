# Pricing System Feature
Ценообразование предметов, расчёт модификаторов и рыночные данные.

**OpenAPI:** pricing.yaml | **Роут:** /game/pricing

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Получение текущей цены предмета /vendor цен
- Рассчёт цены с quality/rarity/durability модификаторами
- Просмотр рыночных данных (trends, supply/demand)
- Активные региональные/фракционные/ивент модификаторы

## Компоненты

**ItemPriceCard**
- Использует: `CompactCard`, `cyberpunkTokens`
- Показ базовой/текущей цены и мультипликаторов

**PriceBreakdownCard**
- Использует: `CompactCard`
- Расшифровка price_breakdown + модификаторы

**MarketDataCard**
- Использует: `CompactCard`
- Средние цены, тренды, спрос/предложение

**ModifiersCard**
- Использует: `CompactCard`
- Активные региональные/фракционные/событийные модификаторы

**PricingPage**
- Использует: `GameLayout`, `CyberpunkButton`, `cyberpunkTokens`
- MMORPG сетка 380px | flex | 320px, формы запроса цен, карточки данных

## Вдохновение
- EVE Online Market Analytics
- FFXIV vendor pricing
- Albion Online market modifiers

**Соответствие:** SPA, компактный UI, шрифты 0.65-0.875rem, киберпанк стиль.



