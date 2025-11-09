# Auction House Core Feature
Аукцион дом - центральная система торговли между игроками (WOW, GW2, FFXIV, Albion Online).

**OpenAPI:** auction-house-core.yaml | **Роут:** /game/auction-house-core

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- **Размещение лотов:**
  - Длительность: 12ч, 24ч, 48ч
  - Listing fee (зависит от длительности)
  - Exchange fee (при продаже)
- **Система ордеров:**
  - Buy orders
  - Sell orders
- **Bid/buyout system:**
  - Ставки
  - Мгновенный выкуп
- **Поиск и фильтрация:**
  - По категории
  - По редкости
  - По цене
- **История цен**
- **Региональные рынки**

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **AuctionLotCard** - карточка аукционного лота
  - Использует: `CompactCard`, `CyberpunkButton` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Цвет по редкости
  - Chips для количества, редкости, времени
  - Кнопки "Ставка" и "Выкуп"

- **AuctionHouseCorePage** - страница аукцион дома
  - Использует: `GameLayout`, `CyberpunkButton` из shared/
  - Компактный layout
  - MMORPG сетка (380px | flex | 320px)

## Вдохновение
- WOW - аукцион дом
- GW2 - система ордеров
- FFXIV - региональные рынки
- Albion Online - рыночная экономика

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.

