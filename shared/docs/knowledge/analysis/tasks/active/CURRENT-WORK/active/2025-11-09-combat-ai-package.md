# Подготовка пакета для combat AI enemies

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 00:57  
**Связанные документы:**
- `.BRAIN/02-gameplay/combat/combat-ai-enemies.md`
- `.BRAIN/02-gameplay/combat/combat-shooter-core.md`
- `.BRAIN/02-gameplay/combat/combat-implants-types.md`
- `.BRAIN/02-gameplay/combat/combat-combos-synergies.md`
- `.BRAIN/02-gameplay/combat/combat-extract.md`
- `.BRAIN/02-gameplay/combat/combat-hacking-networks.md`
- `.BRAIN/02-gameplay/combat/combat-hacking-combat-integration.md`
- `.BRAIN/02-gameplay/combat/combat-cyberspace.md`
- `.BRAIN/02-gameplay/combat/combat-shooting.md`
- `.BRAIN/02-gameplay/combat/combat-stealth.md`
- `.BRAIN/02-gameplay/combat/combat-abilities.md`
- `.BRAIN/02-gameplay/combat/arena-system.md`
- `.BRAIN/05-technical/backend/combat-session-backend.md`

---

## Прогресс
- Перепроверены метаданные `combat-ai-enemies`: статус `approved`, `api-readiness: ready`, актуализирован приоритет `highest`.
- Зафиксированы ключевые REST (`/combat/ai/...`) и WebSocket (`wss://api.necp.game/v1/gameplay/raid/{raidId}`) контракты, Kafka-потоки (`combat.ai.state`, `world.events.trigger`, `raid.telemetry`).
- Зафиксирован pivot на shooter-ядро: D&D документы переведены в `archived`, подготовлена новая спецификация `combat-shooter-core`.
- Обновлены `ready.md` и `readiness-tracker.yaml` для combat-комплекта (AI, shooter core, arenas, combat session, implants, combos, extraction, hacking) — архивные строки исключены из рабочих очередей.
- Выявлены зависимости между документами (combat session, hacking, extraction, implants) — все находятся в статусе `ready` и перечислены в пакете.
- Подготовлен отдельный brief `2025-11-09-combat-systems-wave1-brief.md` для передачи ДУАПИТАСК (сводка каталогов, контрактов, зависимостей).

## Сводка документов для пакета
| Документ | Версия | Каталог API | Следующий шаг |
| --- | --- | --- | --- |
| combat-ai-enemies.md | 1.0.0 | `api/v1/gameplay/combat/ai-enemies.yaml` | Подготовить задания ДУАПИТАСК по REST/WS/Kafka и shooter телеметрии |
| combat-shooter-core.md | _в работе_ | `api/v1/gameplay/combat/shooter-core.yaml` | Сформировать спецификацию оружия, TTK, recoil, suppression |
| combat-session-backend.md | 1.0.0 | `api/v1/gameplay/combat/combat-session.yaml` | Разделить по lifecycle, damage loop, событиям |
| combat-implants-types.md | 1.1.0 | `api/v1/gameplay/combat/implants.yaml` | Согласовать задания по имплантам и модификаторам |
| combat-combos-synergies.md | 1.0.0 | `api/v1/gameplay/combat/combos-synergies.yaml` | Вторая волна задач по синергиям |
| combat-extract.md | 1.3.0 | `api/v1/gameplay/combat/extraction.yaml` | План задач для экстрактшутер механик |
| combat-hacking-networks.md | 1.0.0 | `api/v1/gameplay/combat/hacking/networks.yaml` | Задания по сетевому хакерству |
| combat-hacking-combat-integration.md | 1.0.0 | `api/v1/gameplay/combat/hacking/combat-integration.yaml` | Интеграция хакерства в бою |
| combat-cyberspace.md | 1.0.0 | `api/v1/gameplay/combat/hacking/cyberspace.yaml` | Контракты по cyberspace режимам |
| combat-shooting.md | 1.1.0 | `api/v1/gameplay/combat/shooting.yaml` | Подготовить задания по TTK/отдаче |
| combat-stealth.md | 1.1.0 | `api/v1/gameplay/combat/stealth.yaml` | Описать задачи по скрытности и обнаружению |
| combat-abilities.md | 1.2.0 | `api/v1/gameplay/combat/abilities.yaml` | План задач по активным способностям |
| arena-system.md | 1.0.0 | `api/v1/gameplay/combat/arena-system.yaml` | Задания по аренам и рейтинговым циклам |

## Блокеры
- Действует запрет на создание задач в `API-SWAGGER` до отдельного разрешения.

## Черновик пакета для ДУАПИТАСК
- **REST:** `/combat/ai/profiles`, `/combat/ai/profiles/{id}`, `/combat/ai/profiles/{id}/telemetry`, `/combat/raids/{raidId}/phase`, `/combat/ai/encounter`.
- **WebSocket:** `wss://api.necp.game/v1/gameplay/raid/{raidId}` с событиями `PhaseStart`, `MechanicTrigger`, `PlayerDown`, `CheckRequired`.
- **Kafka:** `combat.ai.state`, `world.events.trigger`, `raid.telemetry` — указаны producer/consumer и payload.
- **Shooter параметры:** TTK профили, recoil кривые, suppression и поведение AI (будут описаны в `combat-shooter-core`).
- **Схемы БД:** `enemy_ai_profiles`, `enemy_ai_abilities`, `raid_boss_phases` (JSONB поля для поведений и механик).
- **Зависимости:** материалы `combat-extract`, `combat-hacking`, `combat-combos`, `combat-implants`, `combat-session` (все в статусе ready) — обеспечивают связность навыков и телеметрии.

## Бриф для ДУАПИТАСК — Combat Systems Wave 1
- **Приоритет:** критический (подготовка боевого ядра для gameplay-service).
- **Оценка объёма:** 5-6 задач (REST 3, WebSocket 1, Kafka 1, вспомогательные справочники 1).
- **Готовые документы:** combat-ai-enemies, combat-shooter-core, combat-abilities, combat-stealth, combat-freerun, combat-combos-synergies, combat-extract, combat-hacking-networks, combat-hacking-combat-integration, combat-session-backend, arena-system.

### Рекомендуемое разбиение задач
- `combat-ai-profiles-api` — CRUD профилей AI, фильтры, связанные телеметрии (REST).
- `combat-raid-lifecycle-api` — REST для фаз рейда и интеграция с WebSocket.
- `combat-ai-telemetry-socket` — WebSocket поток `wss://api.necp.game/v1/gameplay/raid/{raidId}` с событиями Phase/Mechanics/ShooterDamage.
- `combat-ai-kafka-streams` — контракты Kafka `combat.ai.state`, `world.events.trigger`, `raid.telemetry` (+ схемы payload).
- `combat-shooter-core` — справочники оружия, TTK, recoil, suppression.
- `combat-abilities-metadata` — REST для источников и ограничений способностей ( CRUD + фильтры ), завязан на импланты/экипировку.

### Зависимости и интеграции
- **Gameplay-service:** основной исполнитель; требует обновления модулей combat/ai, combat/abilities, combat/stealth, combat/movement.
- **World-service:** получение событий `world.events.trigger` для рейдов и world-state.
- **Economy-service:** выдача наград и санкций (`combat.ai.state` потребляется для экономических штрафов).
- **Analytics-service:** потребитель `raid.telemetry`, расчёты баланса и автотюнинг.
- **Frontend:** модули `modules/combat/ai`, `modules/combat/abilities`, `modules/combat/stealth`, `modules/combat/movement`, `modules/combat/hacking` готовы к генерации API.

### Checks перед передачей
- Уточнить конечные SLA для WebSocket событий (частота, размер payload).
- Согласовать shooter-метрики (TTK, accuracy thresholds) с narrative и progression.
- Подготовить сводную таблицу маппинга способностей ↔ имплантов/экипировки для задачи `combat-abilities-metadata`.

## Следующие действия
1. Оформить бриф ДУАПИТАСК (использовать резюме выше, добавить уровни приоритета и оценки трудозатрат).
2. Приложить список готовых документов (abilities, shooter-core, freerun, combos, hacking, extract) с версиями и целевыми каталогами в виде приложения.
3. Сверить зависимости с другими документами (`combat-extract`, `combat-hacking`, `combat-stealth`) и указать их в итоговом пакете.
4. После разрешения на работу в `API-SWAGGER` подготовить задачу и обновить очереди/трекеры.

