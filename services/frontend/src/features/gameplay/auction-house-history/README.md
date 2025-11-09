# Auction House History Feature
История цен и статистика аукцион дома (графики, статистика, тренды).

**OpenAPI:** auction-house-history.yaml | **Роут:** /game/auction-house-history

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- История цен (графики) с интервалами 1h/4h/1d
- Статистика: average, median, min, max, volume, trades
- Тренды: increasing/decreasing/stable/volatile
- Изменение цены за 7d/30d
- Волатильность, объем торгов

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **PriceHistoryCard** - карточка истории цен
  - Использует: `CompactCard` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Chips для периода и интервала

- **PriceTrendCard** - карточка трендов
  - Использует: `CompactCard` из shared/
  - Цвет по тренду (success/error/info)
  - Иконки трендов

- **AuctionHouseHistoryPage** - страница истории
  - Использует: `GameLayout`, `CyberpunkButton`, `cyberpunkTokens` из shared/
  - MMORPG сетка (380px | flex | 320px)

## Вдохновение
- GW2 Trading Post Charts
- EVE Online Market History

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.


