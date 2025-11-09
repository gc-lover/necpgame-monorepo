# Random Events Extended Feature
Расширенная система случайных событий - 73 события для всех периодов 2020-2093 (детализированная).

**OpenAPI:** random-events-extended/random-events.yaml | **Роут:** /game/random-events-extended

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- **73 события перемещений:**
  - 2020-2030: Destruction and Restoration
  - 2030-2045: Early Corpo Wars
  - 2045-2060: Time of the Red
  - 2060-2077: Corporate Control
  - 2078-2090: New Equilibrium
  - 2090-2093: Pre-Revolution
- **Механики:**
  - Event triggers (условия появления)
  - Dynamic event generation
  - Event consequences
  - Event chains (цепочки событий)
  - Player choices impact
  - Reputation & faction effects

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **RandomEventExtendedCard** - карточка случайного события
  - Использует: `CompactCard`, `CyberpunkButton` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Цвет по категории
  - Chips для периода, категории, локации, риска

- **RandomEventsExtendedPage** - страница случайных событий
  - Использует: `GameLayout`, `CyberpunkButton` из shared/
  - Компактный layout
  - MMORPG сетка (380px | flex | 320px)

## Категории событий
- COMBAT - боевые события
- SOCIAL - социальные события
- ECONOMY - экономические события
- EXPLORATION - исследование
- FACTION - фракционные события
- STORY - сюжетные события

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.

