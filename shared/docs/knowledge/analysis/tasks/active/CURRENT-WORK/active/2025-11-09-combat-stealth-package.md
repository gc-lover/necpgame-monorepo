# Подготовка пакета для combat stealth

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:39  
**Связанные документы:** `.BRAIN/02-gameplay/combat/combat-stealth.md`

---

## Прогресс
- `combat-stealth.md` перепроверен 2025-11-09 01:39: статус `approved`, `api-readiness: ready`, каталог `api/v1/gameplay/combat/stealth.yaml`, фронтенд `modules/combat/stealth`.
- Подтверждены каналы обнаружения (визуальный, аудио, термо, импланты), социальная инженерия и взаимодействие с боевыми системами.
- Уточнены зависимости: `combat-ai-enemies` (threat levels), `combat-hacking-networks` (сенсоры), `combat-abilities` (stealth perks), `quest-engine-backend` (скрытные проверки), analytics (stealth metrics).

## REST/Events/WS (черновик)
- **REST:** `/combat/stealth/profiles`, `/combat/stealth/detection`, `/combat/stealth/abilities`, `/combat/stealth/zones`, `/combat/stealth/alerts`.
- **Events:** `stealth:detection-triggered`, `stealth:alert-cleared`, `stealth:ability-activated`, `stealth:noise-generated`.
- **WebSocket:** `wss://api.necp.game/v1/gameplay/combat/stealth/{sessionId}` — поток уведомлений об уровнях угрозы и статусе скрытности.

## Storage
- Таблицы `stealth_profiles`, `detection_zones`, `stealth_abilities`, `stealth_alerts`, `stealth_noise_events` (JSONB для динамических параметров).

## Следующие действия
1. Декомпозировать REST/Events/WS на задачи для ДУАПИТАСК (описать payload/ответы).
2. Зафиксировать интеграции с боевым AI и хакерством в приложении к брифу.
3. После разрешения на API-SWAGGER подготовить пакет задач и обновить очереди/трекеры.

