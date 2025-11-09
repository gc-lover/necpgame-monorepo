# Combat Systems Wave 1 Package

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:15  
**Связанные документы:**  
- `.BRAIN/05-technical/backend/combat-session-backend.md`  
- `.BRAIN/02-gameplay/combat/combat-ai-enemies.md`  
- `.BRAIN/02-gameplay/combat/combat-implants-types.md`  
- `.BRAIN/02-gameplay/combat/combat-shooter-core.md`  
- `.BRAIN/02-gameplay/combat/combat-abilities.md`  
- `.BRAIN/02-gameplay/combat/combat-shooting.md`  
- `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`  
- `.BRAIN/02-gameplay/combat/combat-extract.md`  
- `.BRAIN/02-gameplay/combat/combat-freerun.md`  
- `.BRAIN/02-gameplay/combat/combat-hacking-networks.md`  
- `.BRAIN/02-gameplay/combat/combat-hacking-combat-integration.md`  
- `.BRAIN/02-gameplay/combat/combat-cyberspace.md`  
- `.BRAIN/02-gameplay/combat/combat-stealth.md`  
- `.BRAIN/02-gameplay/combat/arena-system.md`

> Архив: `combat-dnd-core.md`, `combat-dnd-integration-shooter.md` перенесены в архив и не учитываются в wave 1.

---

## 1. Сводка готовности

| Документ | Версия | Статус | API каталог | Фронтенд модуль | Ключевые акценты |
| --- | --- | --- | --- | --- | --- |
| combat-session-backend.md | 1.0.0 | ready | `api/v1/gameplay/combat/combat-session.yaml` | `modules/combat` | lifecycle, damage loop, event bus |
| combat-ai-enemies.md | 1.0.0 | ready | `api/v1/gameplay/combat/ai-enemies.yaml` | `modules/combat/ai` | AI матрица, WS рейдов, античит |
| combat-implants-types.md | 1.1.0 | ready | `api/v1/gameplay/combat/implants.yaml` | `modules/combat/implants` | типы имплантов, ограничения, синергии |
| combat-shooter-core.md | 0.1.0 | in_progress | `api/v1/gameplay/combat/shooter-core.yaml` | `modules/combat/mechanics` | оружие, баллистика, recoil, suppression |
| combat-abilities.md | 1.2.0 | ready | `api/v1/gameplay/combat/abilities.yaml` | `modules/combat/abilities` | источники, ранги, киберпсихоз |
| combat-shooting.md | 1.1.0 | ready | `api/v1/gameplay/combat/shooting.yaml` | `modules/combat/shooting` | TTK, отдача, имплант-модификаторы |
| combat-combos-synergies.md | 1.0.0 | ready | `api/v1/gameplay/combat/combos-synergies.yaml` | `modules/combat/combos` | цепочки умений, эффект синергий |
| combat-extract.md | 1.3.0 | ready | `api/v1/gameplay/combat/extraction.yaml` | `modules/combat/extraction` | уровни риска, эвакуация, динамика лута |
| combat-freerun.md | 1.1.0 | ready | `api/v1/gameplay/combat/freerun.yaml` | `modules/combat/movement` | паркур, боевые манёвры, импланты |
| combat-hacking-networks.md | 1.0.0 | ready | `api/v1/gameplay/combat/hacking/networks.yaml` | `modules/combat/hacking` | уровни сетей, защита, перехват |
| combat-hacking-combat-integration.md | 1.0.0 | ready | `api/v1/gameplay/combat/hacking/combat-integration.yaml` | `modules/combat/hacking` | боевое хакерство, защитные протоколы |
| combat-cyberspace.md | 1.0.0 | ready | `api/v1/gameplay/combat/hacking/cyberspace.yaml` | `modules/combat/cyberspace` | режимы киберпространства, события |
| combat-stealth.md | 1.1.0 | ready | `api/v1/gameplay/combat/stealth.yaml` | `modules/combat/stealth` | каналы обнаружения, импланты, социалка |
| arena-system.md | 1.0.0 | ready | `api/v1/gameplay/combat/arena-system.yaml` | `modules/combat/arenas` | арены, рейтинги, voice-lobby |

Все документы проверены 2025-11-09 01:13–01:15, статусы и каталоги синхронизированы в `ready.md` и `readiness-tracker.yaml`.

---

## 2. Общие зависимости и интеграции

- **Микросервисы:** базовый `gameplay-service (8083)`, дополнительно `economy-service` (награждения), `social-service` (репутация), `world-service` (события зон), `analytics-service` (телеметрия), `auth-service` (валидация WebSocket).
- **Event Bus:** `combat.ai.state`, `raid.telemetry`, `combat.session.events`, `combat.freerun.movement`, `combat.hacking.alert`. Продюсеры/консьюмеры описаны в исходных документах.
- **WebSocket:** основной канал `wss://api.necp.game/v1/gameplay/raid/{raidId}`, дополнительные каналы для арен (`/arenas/{arenaId}/stream`) и киберпространства (`/cyberspace/{instanceId}`) — отразить в постановке задач.
- **Базы данных:** `enemy_ai_profiles`, `combat_sessions`, `combat_loadouts`, `cyberspace_instances`, `arena_matches` — схемы указаны в соответствующих документах.
- **Shooter слой:** единая спецификация `combat-shooter-core` (в разработке) описывает оружие, баллистику, TTK и используется всеми подсистемами (abilities, stealth, freerun, hacking).

---

## 3. План для ДУАПИТАСК (без создания задач)

1. **REST**  
   - `/combat/sessions/*` — lifecycle, события, результаты;  
   - `/combat/ai/*` — профили врагов, телеметрия;  
   - `/combat/abilities/*` — CRUD по активным способностям, синергия;  
   - `/combat/hacking/*` — сетевые уровни, интеграция в бою, cyberspace;  
   - `/combat/stealth/*`, `/combat/freerun/*`, `/combat/extraction/*`, `/combat/arenas/*`;  
   - `/combat/shooter/*` — выстрелы, попадания, перезарядки, suppression;  
   - вспомогательные `/combat/combos`, `/combat/freerun/*`, `/combat/stealth/*`.

2. **WebSocket**  
   - `raid/{raidId}` — события AI, механики, проверки;  
   - `arenas/{arenaId}/match` — рейтинговые события и голосовые приглашения;  
   - `cyberspace/{instanceId}` — VR-события, уровни доступа;  
   - мониторинг `freerun` и `stealth` (опционально через session stream).

3. **Kafka/Event Bus**  
   - подтвердить payload из документов (Combat AI, sessions, hacking, extract).  
   - подготовить схемы telemetry topics для analytics-service.

4. **Cross-cutting**  
   - единый reference по shooter спецификации (`combat-shooter-core`), синхронизация с quest-engine и analytics.  
   - согласование security (anti-cheat, rate-limits) с `combat-session-backend`.

### 3.1 REST backlog (черновая декомпозиция)
| Приоритет | Endpoint | Источник документа | Краткое описание |
| --- | --- | --- | --- |
| P0 | `POST /combat/sessions` | combat-session-backend | запуск сессии, регистрация участников, выдача sessionId |
| P0 | `POST /combat/sessions/{id}/events` | combat-session-backend | публикация событий (damage, revive, state change) |
| P0 | `POST /combat/ai/profiles` | combat-ai-enemies | регистрация AI профиля врага (behaviour tree, skill deck) |
| P0 | `PUT /combat/ai/profiles/{enemyId}/phase` | combat-ai-enemies | обновление фазы рейда, механики, телеметрия |
| P1 | `GET /combat/abilities` | combat-abilities | перечень активных способностей и связанной синергии |
| P1 | `POST /combat/abilities/{abilityId}/sync` | combat-abilities + combat-combos-synergies | синхронизация способностей с комбо и шутерными модификаторами |
| P1 | `POST /combat/shooter/fire` | combat-shooter-core | регистрация выстрела (оружие, позиция, баллистика) |
| P1 | `POST /combat/shooter/hit` | combat-shooter-core | подтверждение попадания и нанесённого урона |
| P1 | `POST /combat/hacking/networks/{networkId}/breach` | combat-hacking-networks | запуск взлома, уровни доступа, время |
| P1 | `POST /combat/hacking/combat/{sessionId}` | combat-hacking-combat-integration | интеграция боевого хакерства (offensive/defensive) |
| P1 | `POST /combat/cyberspace/instances` | combat-cyberspace | создание VR-сессии, уровни доступа |
| P1 | `POST /combat/stealth/events` | combat-stealth | фиксация обнаружения/стелс событий, модификаторы |
| P1 | `POST /combat/freerun/actions` | combat-freerun | паркур/манёвры, проверка ограничений |
| P2 | `POST /combat/extraction/zones/{zoneId}/extract` | combat-extract | старт/результат экстракции |
| P2 | `POST /combat/arenas/{arenaId}/match` | arena-system | старт арены, рейтинги, награды |
| P2 | `POST /combat/combos/apply` | combat-combos-synergies | применение комбо, проверка требований |

### 3.2 WebSocket backlog
| Приоритет | Канал | Источник | Описание payload |
| --- | --- | --- | --- |
| P0 | `wss://.../combat/raid/{raidId}` | combat-ai-enemies, combat-session-backend | `phase`, `mechanic`, `target`, `damage`, `suppression` |
| P0 | `wss://.../combat/sessions/{sessionId}` | combat-session-backend | события damage/heal, состояние участников |
| P1 | `wss://.../combat/stealth/{sessionId}` | combat-stealth | `threatLevel`, `channel`, `detectedBy`, `timestamp` |
| P1 | `wss://.../combat/freerun/{sessionId}` | combat-freerun | `action`, `success`, `buffs`, `momentum` |
| P1 | `wss://.../combat/cyberspace/{instanceId}` | combat-cyberspace | `layer`, `eventType`, `accessLevel`, `reward` |
| P2 | `wss://.../combat/arenas/{arenaId}/match` | arena-system | `score`, `phase`, `voiceInvite`, `rewardPreview` |

### 3.3 Event Bus backlog
| Topic | Producer | Consumer | Поля |
| --- | --- | --- | --- |
| `combat.ai.state` | gameplay-service (AI) | analytics-service, quest-engine | `enemyId`, `phase`, `threat`, `timestamp` |
| `combat.session.events` | gameplay-service (session) | analytics-service, economy-service | `sessionId`, `eventType`, `payload` |
| `combat.freerun.movement` | gameplay-service (movement) | analytics-service | `characterId`, `action`, `success`, `position` |
| `combat.hacking.alert` | gameplay-service (hacking) | security-service, quest-engine | `networkId`, `severity`, `source`, `actionRequired` |
| `combat.extraction.result` | gameplay-service (extract) | economy-service, inventory-service | `zoneId`, `outcome`, `loot`, `squad` |
| `combat.arena.match` | gameplay-service (arenas) | social-service, leaderboard-service | `arenaId`, `result`, `ratingDelta`, `reward` |

### 3.4 Зависимости и этапы
- **Этап 1 (P0):** combat sessions + raid AI (REST + WS + Kafka) — блокирует остальные подсистемы.
- **Этап 2 (P1):** shooter ядро (`combat-shooter-core`), abilities, hacking, stealth/freerun — требует Stage 1.
- **Этап 3 (P2):** extraction, arenas, combos — можно запускать после стабилизации предыдущих этапов.
- **Shared requirements:** security (rate limits, anti-cheat), telemetry (analytics-service), economy (награждения).

---

## 4. Следующие действия

1. Сформировать резюме по каждому REST/WS endpoint с указанием источника документа и приоритета (внутри этой заметки или отдельным приложением).
2. Подготовить сетку зависимостей (какие задачи блокируют другие), чтобы этапировать создание API задач.
3. Проверить, нужно ли дополнительно обновить `implementation-tracker.yaml` (сейчас awaiting slot для всех боевых задач).
4. После снятия запрета на API-SWAGGER — передать пакет в ДУАПИТАСК одной волной либо несколькими подпакетами (AI, Hacking, Movement, Stealth/Abilities).
5. Продолжить синхронизацию с Quest Engine, чтобы shooter-события (damage, detection, stealth) были согласованы до постановки задач.

---

## 5. История

- 2025-11-09 01:15 — собрана сводка Wave 1, подтверждена готовность документов, зафиксированы зависимости и план передачи.
- 2025-11-09 13:25 — подготовлен подробный резюме-блок по REST/WebSocket/Kafka каналам для быстрой постановки задач ДУАПИТАСК.
- 2025-11-09 15:10 — пересобрано под шутерную боевую модель, удалены зависимости от D&D.

---

## 6. Резюме по ключевым контрактам

### REST
- `POST /combat/sessions` — запускает боевую сессию, валидирует состав и выдаёт `sessionId`; потребляет combat-session backend, блокирует остальные подсистемы.
- `POST /combat/sessions/{id}/events` — журналирует боевые события (damage, revive, state change) с обязательным указанием источника и времени; синхронизируется с analytics и economy.
- `POST /combat/ai/profiles` / `PUT /combat/ai/profiles/{enemyId}/phase` — регистрируют и обновляют профили врагов, включая поведение и телеметрию фаз; критично для raid AI.
- `POST /combat/abilities/{abilityId}/sync` — стыкует способности с комбо и шутерными модификаторами; учитывает импланты и киберпсихоз.
- `POST /combat/shooter/fire` + `/combat/shooter/hit` — центральные точки для стрельбы и регистрации попаданий; используются всеми боевыми подсистемами.
- `POST /combat/hacking/networks/{networkId}/breach` / `POST /combat/hacking/combat/{sessionId}` — запускают сетевой взлом и боевое хакерство, описывают тайминги, уровни доступа и ответные действия.
- `POST /combat/cyberspace/instances` — создаёт VR-инстанс, возвращает токены доступа и лимиты; увязан с quest-engine и analytics.
- `POST /combat/stealth/events` / `/combat/freerun/actions` — фиксируют стелс и паркур события, возвращают параметры угрозы, выносливости и скрытности.
- `POST /combat/extraction/zones/{zoneId}/extract` — управляет эвакуацией, подтягивает расчёт лута и риск; триггерит economy/inventory.
- `POST /combat/arenas/{arenaId}/match` — стартует матч арены, управляет рейтингами и наградами; готовит payload для лидербордов.
- `POST /combat/combos/apply` — проверяет условия исполнения комбо, возвращает эффекты, cooldown и потребление ресурсов.

### WebSocket
- `wss://.../combat/raid/{raidId}` / `/combat/sessions/{sessionId}` — главный поток рейда: AI фазы, состояния игроков, события стрельбы; требуется минимальная задержка.
- `wss://.../combat/stealth/{sessionId}` и `/combat/freerun/{sessionId}` — пушат оповещения об обнаружении и манёврах паркура; работают на основе shooter-телеметрии (скорость, шум, видимость).
- `wss://.../combat/cyberspace/{instanceId}` — real-time события VR уровня, передаёт слои доступа и награды.
- `wss://.../combat/arenas/{arenaId}/match` — рейтинговые события, voice-инвайты и live-таблицы.

### Event Bus
- `combat.ai.state` — телеметрия врагов, которую используют analytics и quest-engine для прогресса/эвентов.
- `combat.session.events` — поток боевых событий для аналитики и экономики; хранит оригинальный payload REST `/events`.
- `combat.freerun.movement` — движение персонажей, KPI мобилиности.
- `combat.hacking.alert` — алерты взлома, потребляются security-service и quest-engine.
- `combat.extraction.result` — результат эвакуации, инициирует обновление экономики и инвентаря.
- `combat.arena.match` — выдаёт финальные рейтинги/награды матчей, подключает social/leaderboard сервисы.

### Этапность
- **Stage P0:** `/combat/sessions*`, `/combat/ai/*`, raid WebSocket и `combat.ai.state` / `combat.session.events` должны быть подняты первыми.
- **Stage P1:** shooter core (`combat-shooter-core`), abilities, hacking, stealth/freerun — требуют готовности Stage P0.
- **Stage P2:** extraction, arenas, combos — ждут стабилизации предыдущих уровней и обратной связи analytics/economy.

