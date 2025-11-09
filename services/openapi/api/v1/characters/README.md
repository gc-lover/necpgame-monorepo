# Characters API
**Источник:** `.BRAIN/05-technical/ui-main-game.md` (v1.1.0)  
**Task ID:** API-TASK-035  
**Версия API:** v1.0.0

## Описание
API для управления состоянием персонажа (здоровье, энергия, характеристики, навыки).

## Endpoints
- `GET /characters/{characterId}/status` - Текущий статус персонажа
- `GET /characters/{characterId}/stats` - Характеристики
- `GET /characters/{characterId}/skills` - Навыки
- `POST /characters/{characterId}/status/update` - Обновить статус

