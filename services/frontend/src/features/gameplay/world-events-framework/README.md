# World Events Framework Feature
Фреймворк мировых событий - 7 эпох (1990-2093), DC scaling, D&D generators (детализированный).

**OpenAPI:** world-events-framework.yaml | **Роут:** /game/world-events-framework

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- **7 эпох:**
  - 1990-2000: Pre-Collapse
  - 2000-2020: Fourth Corporate War
  - 2020-2040: Destruction & Restoration
  - 2040-2060: Time of the Red
  - 2060-2077: Corporate Control (Canon)
  - 2078-2090: New Equilibrium
  - 2090-2093: Pre-Revolution
- **Механики:**
  - DC scaling по временным периодам
  - AI sliders для фракций
  - D&D event generators (d100)
  - Economic multipliers
  - Technology access levels
  - Quest hooks
  - Travel events integration
  - Era-specific mechanics

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **WorldEventCard** - карточка мирового события
  - Использует: `CompactCard`, `CyberpunkButton` из shared/
  - Маленькие шрифты (cyberpunkTokens)
  - Цвет по типу события
  - Chips для эпохи, типа, уровня воздействия, статуса

- **WorldEventsFrameworkPage** - страница мировых событий
  - Использует: `GameLayout`, `CyberpunkButton` из shared/
  - Компактный layout
  - MMORPG сетка (380px | flex | 320px)

## Типы событий
- GLOBAL - глобальные события
- REGIONAL - региональные события
- LOCAL - локальные события
- FACTION - фракционные события
- ECONOMIC - экономические события
- TECHNOLOGICAL - технологические события

**Соответствие:** Полная реализация на основе OpenAPI, React Query хуки, MUI + shared/ компоненты.

