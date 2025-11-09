# Player Market Orders Feature
Расширенная система ордеров игрока: создание, отмена, активные заявки и история.

**OpenAPI:** player-market-orders.yaml | **Роут:** /game/player-market-orders

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Создание buy/sell ордеров (market / limit)
- Поддержка time-in-force (GTC / IOC / FOK)
- Список активных заявок с прогрессом исполнения
- История ордеров с PnL и комиссиями
- Фильтры по стороне (BUY/SELL)

## Компоненты

**MarketOrderCard**
- Использует: `CompactCard`, `CyberpunkButton`, `cyberpunkTokens`
- Отображает статус, TIF, прогресс исполнения, кнопку отмены

**OrderHistoryCard**
- Использует: `CompactCard`, `cyberpunkTokens`
- История исполнений с PnL, комиссиями, деталями

**PlayerMarketOrdersPage**
- Использует: `GameLayout`, `CyberpunkButton`, `cyberpunkTokens`
- MMORPG сетка 380px | flex | 320px, компактный UI, фильтры и формы

## Вдохновение
- EVE Online Market (ордербуки, TIF)
- Albion Online Marketplace (история, комиссии)
- GW2 Trading Post (интерфейс торговли)

**Соответствие:** SPA, компактная сетка, шрифты 0.65-0.875rem, киберпанк стиль.


