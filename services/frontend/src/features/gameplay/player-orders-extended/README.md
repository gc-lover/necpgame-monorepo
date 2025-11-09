# Player Orders Extended Feature
Расширенная система заказов между игроками - crafting, gathering, combat, transport (детализированная).

**OpenAPI:** player-orders-extended.yaml | **Роут:** /game/player-orders-extended

**⭐ ИСПОЛЬЗУЕТ shared/ библиотеку компонентов!**

## Функционал
- **Типы заказов:**
  - CRAFTING - создание предметов
  - GATHERING - сбор ресурсов
  - COMBAT_ASSISTANCE - помощь в бою
  - TRANSPORTATION - доставка
  - SERVICE - различные услуги
- **Механики:**
  - Order creation (детальные требования)
  - Order execution (выполнение заказов)
  - Via NPC execution (выполнение через нанятых NPC)
  - Economy integration (ценообразование, комиссии, escrow)
  - Reputation system (рейтинг исполнителей)
  - Advanced features (bulk orders, recurring, premium)
  - World impact (спрос и предложение влияют на экономику)
- **Сложность:**
  - EASY - простые заказы
  - MEDIUM - средние заказы
  - HARD - сложные заказы
  - EXPERT - экспертные заказы

## Компоненты

**⭐ Используют shared/ библиотеку!**

- **PlayerOrderCard** — карточка заказа (CompactCard + CyberpunkButton)
- **OrderBoardCard** — доска хайлайтов (мелкие карточки, ProgressBar)
- **OrderDetailCard** — расширенные детали заказа, escrow, требования
- **OrderEconomyCard** — экономики рынка заказов (volume, escrow, demand)
- **OrderReputationCard** — рейтинг исполнителя (tier, score, cancel rate)
- **OrderActionsCard** — быстрые действия (создать, escrow, контракты)
- **PlayerOrdersExtendedPage** — SPA (380 | flex | 320), фильтры, компактный UI

## Механики
- Создание заказов
- Принятие заказов
- Выполнение через NPC
- Escrow система
- Рейтинг исполнителей
- Экономические метрики и доска заказов

## Тесты
- Юнит-тесты на компоненты в `components/__tests__`
- Написаны, **не запускались** (по инструкции)

