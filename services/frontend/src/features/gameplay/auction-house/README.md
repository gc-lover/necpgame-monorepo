# Auction House Mechanics (Frontend)
Модуль визуализирует и тестирует механику аукционного дома economy-service на основе спецификации `api/v1/economy/auction-house/auction-mechanics.yaml`.

**Роут:** `/game/auction-house`

## Функциональные блоки
- **Конфигурация аукциона** — чтение `AuctionConfig` и сравнений с Player Market.
- **Валидация создания лота** — форма отправляет `validateAuctionCreation`.
- **Жизненный цикл** — ставки, buyout, отмена, продление, обработка истёкших лотов через lifecycle-эндпоинты.
- **Уведомления** — примеры payload из `getAuctionNotificationSamples`.
- **Журнал операций** — прямое отражение действий агента (React Query mutations).

## Компоненты
- **AuctionConfigCard** — карточка конфигурации (creation/bidding/buyout/commission/scheduler + сравнение).
- **AuctionHousePage** — страница с панелями управления, формами операций и журналом.

## Особенности реализации
- React Query `useGetAuctionConfig`, `useValidateAuctionCreation`, lifecycle хуки из Orval.
- Material UI + GameLayout. Все формы соответствуют сценариям агента из `ФРОНТТАСК.MD`.
- Ограничение ≤500 строк соблюдено, данные не хардкодноятся (только значения по умолчанию для форм).

## Дальнейшие шаги
- Подключить real-time поток `x-websocket.auctionStream` (websocket клиент).
- Добавить e2e сценарии для QA агента после появления бекенда.

