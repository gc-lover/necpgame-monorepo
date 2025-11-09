# Player Market Feature
Рынок игроков с системой ордеров - Order Book, Buy/Sell orders (EVE Online / GW2 / Albion Online).

**OpenAPI:** player-market-core.yaml | **Роут:** /game/player-market

## Функционал
- **Order Book (Стакан):** Buy orders (зеленые), Sell orders (красные), Spread, Last Trade Price
- **Создание ордеров:**
  - Market orders: мгновенное исполнение по лучшей цене
  - Limit orders: исполнение при достижении указанной цены
  - Buy/Sell side
- **Мои ордера:** Active, Filled, Cancelled
- **Исполнение:** Частичное, полное, автоматическое
- **Комиссии:** Listing fee (0.5%) + Exchange fee (2-5%)
- **Priority:** Price/Time priority в стакане

## Компоненты
- **OrderBookDisplay** - визуализация стакана заявок (buy/sell orders)
- **PlayerMarketPage** - создание ордеров, мои ордера, order book

## Механики
- Order matching engine (автоматическое исполнение)
- Price/Time priority
- Частичное исполнение ордеров
- Возврат средств при отмене

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI компоненты.

