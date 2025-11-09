# NPCs API

**Источник:** `.BRAIN/05-technical/ui-main-game.md` (v1.1.0)  
**Task ID:** API-TASK-031  
**Версия API:** v1.0.0

## Описание
API для взаимодействия с NPC и диалогами.

## Endpoints
- `GET /npcs` - Список всех NPC
- `GET /npcs/location/{locationId}` - NPC в локации
- `GET /npcs/{npcId}` - Детали NPC
- `GET /npcs/{npcId}/dialogue` - Диалог с NPC
- `POST /npcs/{npcId}/interact` - Взаимодействие
- `POST /npcs/{npcId}/dialogue/respond` - Ответить в диалоге

