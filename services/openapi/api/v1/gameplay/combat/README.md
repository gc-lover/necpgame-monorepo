# Combat API
**Источник:** `.BRAIN/05-technical/ui-main-game.md` (v1.1.0)  
**Task ID:** API-TASK-032  
**Версия API:** v1.0.0

## Описание
API для управления боевыми сессиями в геймплейном микросервисе.

## Endpoints
- `POST /gameplay/combat/sessions` — создать боевую сессию
- `GET /gameplay/combat/sessions/{sessionId}` — получить состояние боя
- `POST /gameplay/combat/sessions/{sessionId}/actions` — выполнить действие
- `POST /gameplay/combat/sessions/{sessionId}/turn/end` — завершить текущий ход
- `POST /gameplay/combat/sessions/{sessionId}/complete` — выдать награды и завершить бой
- `GET /gameplay/combat/sessions/{sessionId}/log` — получить журнал событий

