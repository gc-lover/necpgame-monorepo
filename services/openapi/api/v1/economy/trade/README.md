# Trade System API

## Назначение
Спецификация описывает контракты P2P торговли между игроками: создание торговых
сессий, обновление предложений, двойное подтверждение, проверки дистанции и
историю сделок для аналитики и аудита.

## Структура
- `trade-system.yaml` — корневой файл OpenAPI c метаданными, серверами и ссылками
  на подфайлы.
- `paths/sessions.yaml` — операции жизненного цикла торговых сессий.
- `paths/history.yaml` — получение истории P2P обменов.
- `schemas/index.yaml` — агрегатор схем и параметров.
- `schemas/session.yaml` — объекты торговых сессий, офферов и realtime событий.
- `schemas/history.yaml` — схемы истории обменов и комплектов ресурсов.

## Интеграции
- Микросервис: `economy-service` (порт `8085`, base-path `/api/v1/economy/trade`).
- Фронтенд: `modules/economy/trade` (компоненты `TradeWindow`, `ItemCard`,
  `CurrencyTag`, `DualConfirmBadge`, формы `TradeForm`, `ConfirmExchangeForm`,
  хуки `useEconomyStore`, `useRealtime`).
- Зависящие сервисы: `inventory-service`, `analytics-service`, `security-service`.

## Контроль
- Все ошибки и статусы используют общие компоненты `api/v1/shared/common`.
- Файл валидации: `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\trade`.

