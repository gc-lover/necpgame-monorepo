# Подготовка пакета для combat combos & synergies

**Приоритет:** medium  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 02:48  
**Связанные документы:** `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`

---

## Прогресс
- `combat-combos-synergies.md` перепроверен 2025-11-09 02:48: статус `approved`, `api-readiness: ready`, каталог `api/v1/gameplay/combat/combos-synergies.yaml`, фронтенд `modules/combat/combos`.
- Зафиксированы типы комбо: solo/team/equipment/implant/timing, а также параметры scoring и условия активации.
- Определены зависимости: `combat-abilities`, `combat-shooting`, `combat-stealth`, `combat-implants`, `combat-extract`, `combat-hacking`, `progression-backend` (перки), analytics (combo метрики).

## REST/Events (черновик)
- **REST:** `/combat/combos/catalog`, `/combat/combos/{comboId}`, `/combat/combos/loadouts`, `/combat/combos/team`, `/combat/combos/scoring`.
- **Events:** `combat.combo.triggered`, `combat.combo.failed`, `combat.combo.score-updated`, `combat.combo.team-synergy`, `combat.combo.implant-activated`.
- **WebSocket:** `wss://api.necp.game/v1/gameplay/combat/combos/{sessionId}` — real-time уведомления о синергиях и показателях scoring.

## Storage
- Таблицы `combo_catalog`, `combo_loadouts`, `combo_team_synergies`, `combo_scores`, `combo_logs` (JSONB для условий и эффектов).

## Следующие действия
1. Разложить REST/Events/WS на задачи для ДУАПИТАСК (описать payload/ответы, сценарии).
2. Зафиксировать пересечения с abilities/implants/shooting в приложении к брифу.
3. После разрешения на API-SWAGGER подготовить пакет задач и обновить очереди/трекеры.

