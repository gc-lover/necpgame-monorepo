# Подготовка пакета для combat shooting

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:37  
**Связанные документы:** `.BRAIN/02-gameplay/combat/combat-shooting.md`

---

## Прогресс
- Перепроверен `combat-shooting.md`: статус `approved`, `api-readiness: ready`, каталог `api/v1/gameplay/combat/shooting.yaml`, фронтенд `modules/combat/shooting`.
- Уточнены ключевые разделы: TTK формулы, отдача (per weapon class), имплант-модификаторы, режимы стрельбы (burst/auto/charged shots).
- Собраны зависимости: `combat-abilities`, `combat-implants-types`, `combat-extract`, `combat-hacking-networks`, `combat-ai-enemies` (delay modifiers, telemetries), `analytics-service` (shooting metrics).

## REST/WS/Events (черновик)
- **REST:** `/combat/shooting/profiles`, `/combat/shooting/weapons/{weaponId}/recoil`, `/combat/shooting/ttk`, `/combat/shooting/modifiers`, `/combat/shooting/loadouts`.
- **WebSocket:** `wss://api.necp.game/v1/gameplay/combat/shooting/{sessionId}` — поток распределения отдачи/TTK обновлений в real-time боях.
- **Events:** `combat.shooting.recoil-updated`, `combat.shooting.modifier-applied`, `combat.shooting.ttk-adjusted`, `combat.shooting.loadout-saved`.

## Storage
- Таблицы `shooting_profiles`, `weapon_recoil_patterns`, `ttk_matrix`, `shooting_modifiers`, `player_loadouts` (JSONB для индивидуальных настроек).

## Следующие действия
1. Разложить REST/WS/Events по отдельным задачам для ДУАПИТАСК, подготовить требования к payload и ответам.
2. Свести зависимости с имплантами/abilities в приложении к брифу.
3. После разрешения на API-SWAGGER подготовить пакет задач и обновить очереди/трекеры.

