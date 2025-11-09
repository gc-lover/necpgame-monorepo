# Player Market Core Feature
Рынок игроков с системой ордеров (стакан, создания ордеров, мои ордера).

**OpenAPI:** player-market-core.yaml | **Роут:** /game/player-market-core

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Order book (buy/sell orders, spread, last trade)
- Market & limit orders (buy/sell)
- Price/time priority
- Комиссии (listing fee + exchange fee)
- Мои ордера: статус, исполнение, отмена

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **OrderBookCard** - карточка стакана заявок
  - Использует: `CompactCard`, `cyberpunkTokens`
  - BUY/SELL секции с ценой, количеством, количеством ордеров
  - Chips: spread, last price

- **PlayerOrderCard** - карточка ордера игрока
  - Использует: `CompactCard`, `CyberpunkButton`
  - Цвет по стороне (BUY cyan / SELL yellow)
  - Статус, заполнение, отмена ордера

- **PlayerMarketCorePage** - страница рынка игроков
  - Использует: `GameLayout`, `CyberpunkButton`, `cyberpunkTokens`
  - MMORPG сетка (380px | flex | 320px)

## Вдохновение
- EVE Online Market
- GW2 Trading Post
- Albion Online Black Market

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.


