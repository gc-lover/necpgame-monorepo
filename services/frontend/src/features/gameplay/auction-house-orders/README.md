# Auction House Orders Feature
Система ордеров аукциона - buy/sell orders с автоматическим исполнением (GW2, EVE Online).

**OpenAPI:** auction-house-orders.yaml | **Роут:** /game/auction-house-orders

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- **Buy orders:**
  - Заявки на покупку по максимальной цене
  - Автоматическое исполнение при появлении подходящего лота
  - Частичное исполнение
- **Sell orders:**
  - Заявки на продажу по минимальной цене
  - Автоматическое исполнение при появлении подходящего buy order
  - Частичное исполнение
- **Механики:**
  - Срок жизни ордера (24-48ч, default 24ч)
  - Статусы: filled, partially_filled, pending
  - Отмена ордера
  - Автоматическое сопоставление

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **AuctionOrderCard** - карточка аукционного ордера
  - Использует: `CompactCard`, `CyberpunkButton` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Цвет по типу (BUY = cyan, SELL = yellow)
  - Chips для количества, статуса, срока
  - Процент исполнения

- **AuctionHouseOrdersPage** - страница ордеров
  - Использует: `GameLayout`, `CyberpunkButton` из shared/
  - Компактный layout
  - MMORPG сетка (380px | flex | 320px)

## Вдохновение
- GW2 - система ордеров
- EVE Online - автоматическое исполнение

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.

