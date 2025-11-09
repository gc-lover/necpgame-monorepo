# Auction House Search Feature
Поиск и фильтрация аукцион дома - мощные фильтры и сортировка.

**OpenAPI:** auction-house-search.yaml | **Роут:** /game/auction-house-search

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- Текстовый поиск по названию
- Фильтры:
  - Категория
  - Тип предмета (weapon, armor, implant и т.д.)
  - Редкость (COMMON → ICONIC)
  - Уровень (min/max)
  - Цена (min/max)
  - Бренд
- Сортировка: price_asc, price_desc, time_asc, time_desc, rarity
- Пагинация (page, limit)
- Endpoint доступных фильтров

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **AuctionSearchResultCard** - карточка результата поиска
  - Использует: `CompactCard`, `CyberpunkButton` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Цвет по редкости
  - Chips: категория, редкость, время

- **AuctionHouseSearchPage** - страница поиска
  - Использует: `GameLayout`, `CyberpunkButton` из shared/
  - Компактный layout
  - MMORPG сетка (380px | flex | 320px)

## Вдохновение
- GW2 - торговый пост с фильтрами
- EVE Online - мощный поиск и сортировка

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.


