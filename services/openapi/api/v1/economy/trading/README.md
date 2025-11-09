# Trading API
**Источник:** `.BRAIN/05-technical/ui-main-game.md` (v1.1.0)  
**Task ID:** API-TASK-033  
**Версия API:** v1.0.0

## Описание
API для торговли с NPC-торговцами.

## Endpoints
- `GET /trading/vendors` - Список торговцев
- `GET /trading/vendors/{vendorId}/inventory` - Ассортимент
- `POST /trading/buy` - Купить предмет
- `POST /trading/sell` - Продать предмет
- `GET /trading/prices/{itemId}` - Цена предмета

