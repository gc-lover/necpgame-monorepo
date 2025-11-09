# Combat Systems — Wave 1 (готовность)
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:25
**api-readiness-notes:** Пакет combat wave 1 перепроверен 2025-11-09 03:25; бриф аккумулирует готовые документы для ДУАПИТАСК и готов к передаче.
**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (8083)  
**target-frontend-module:** modules/combat/*

**Обновлено:** 2025-11-09 02:55  
**Ответственный:** Brain Manager  
**Статус пакета:** материалы `.BRAIN` перепроверены, готовы к постановке задач (ожидаем разрешения на ДУАПИТАСК/АПИТАСК).

---

## Пакет документов (готовность: `ready`)
- `.BRAIN/02-gameplay/combat/combat-ai-enemies.md` — AI-матрица, REST/WS, Kafka топики, схемы БД.
- `.BRAIN/02-gameplay/combat/combat-shooter-core.md` — боевое ядро 3D-шутера (оружие, TTK, баллистика, хитбоксы, anti-cheat).
- `.BRAIN/02-gameplay/combat/combat-abilities.md` — источники/типы способностей, ограничения, киберпсихоз.
- `.BRAIN/02-gameplay/combat/combat-shooting.md` — TTK, отдача, имплант-модификаторы, режимы стрельбы.
- `.BRAIN/02-gameplay/combat/combat-stealth.md` — скрытность, обнаружение, социальная инженерия.
- `.BRAIN/02-gameplay/combat/combat-freerun.md` — паркур и боевые манёвры.
- `.BRAIN/02-gameplay/combat/combat-extract.md` — экстрактшутер механики, страховка, риски.
- `.BRAIN/02-gameplay/combat/combat-hacking-networks.md` — архитектура сетей, уровни ICE.
- `.BRAIN/02-gameplay/combat/combat-hacking-combat-integration.md` — хакерство в бою, контрмеры.
- `.BRAIN/02-gameplay/combat/arena-system.md` — режимы арен, рейтинги, экономические связи.
- `.BRAIN/05-technical/backend/combat-session-backend.md` — ядро боевых сессий (инстансы, логи, события).
- `.BRAIN/05-technical/backend/trade-system.md` — P2P торговля (антифрод, подтверждения).
- `.BRAIN/05-technical/backend/quest-engine-backend.md` — state machine квестов, диалоги, события.

Все документы имеют актуальные записи в `readiness-tracker.yaml` и очередь `ready.md`. Приоритеты: `critical` для боевого ядра и квестов, `high` для остальных боевых подсистем.

---

## Интеграции и зависимости
- **gameplay-service (8083):** combat AI, shooter core, abilities, shooting, stealth, freerun, extract, hacking, arena, quest engine, combat session.
- **economy-service (8085):** trade system, inventory core (зависимость для боевых задач).
- **character-service (8082):** события персонажей (`Character*`), слоты, восстановление — требуется для боевых задач.
- **shared фронтенд-модули:** `modules/combat/*`, `modules/quests`, `modules/economy/*`.
- **Event/Kafka:** `combat.ai.state`, `world.events.trigger`, `raid.telemetry`, `combat.*`, `quest.*`, `trade.*`.

---

## Следующие шаги (после разрешения на постановку задач)
1. Для каждого документа сформировать бриф ДУАПИТАСК (использовать конспект в `2025-11-09-combat-ai-package.md` и актуальные заметки).
2. Синхронизировать пакеты задач между `combat` блоками (AI, abilities, shooting, stealth, freerun) так, чтобы они учитывали общие модели и события.
3. Подготовить сводную таблицу зависимостей (REST/WS/Kafka) для включения в задания.
4. После постановки задач обновить `readiness-tracker.yaml`, `ready.md`, `CURRENT-WORK/current-status.md` и проинформировать смежных агентов.

---

> Пока действует запрет на создание задач в `API-SWAGGER`, пакет фиксируем в `.BRAIN`. После снятия запрета используем эту сводку как основу для wave 1 combat задач.

# Combat Systems Wave 1 — Brief для ДУАПИТАСК

**Статус:** ready-to-hand-off  
**Версия:** 1.0.0  
**Подготовлено:** 2025-11-09 01:10  
**Ответственный:** Brain Manager

---

## 1. Общее описание
- Волна охватывает боевые подсистемы gameplay-service: AI врагов, shooter ядро, импланты, комбо, экстракцию, хакерство, арену, стрельбу, скрытность, способности и сессию боя.
- Почти все документы отмечены `api-readiness: ready`; `combat-shooter-core.md` находится в статусе `in-progress` и заменяет архивированные D&D материалы.
- Таргет: `gameplay-service` с каталогами `api/v1/gameplay/combat/...` (полный перечень ниже).

---

## 2. Документы и каталоги API
| Путь документа | Версия | Каталог OpenAPI | Фронтенд модуль | Примечание |
| --- | --- | --- | --- | --- |
| `02-gameplay/combat/combat-ai-enemies.md` | 1.0.0 | `api/v1/gameplay/combat/ai-enemies.yaml` | `modules/combat/ai` | REST/WS контракты, Kafka-топики, матрица сложностей |
| `02-gameplay/combat/combat-shooter-core.md` | 0.1.0 | `api/v1/gameplay/combat/shooter-core.yaml` | `modules/combat/mechanics` | Боевое ядро шутера, оружие, баллистика, TTK, anti-cheat |
| `05-technical/backend/combat-session-backend.md` | 1.0.0 | `api/v1/gameplay/combat/combat-session.yaml` | `modules/combat` | Lifecycle сессии, события, damage loop |
| `02-gameplay/combat/combat-implants-types.md` | 1.1.0 | `api/v1/gameplay/combat/implants.yaml` | `modules/combat/implants` | Типы имплантов, модификаторы |
| `02-gameplay/combat/combat-combos-synergies.md` | 1.0.0 | `api/v1/gameplay/combat/combos-synergies.yaml` | `modules/combat/combos` | Волна 2, синергии и цепочки |
| `02-gameplay/combat/combat-extract.md` | 1.3.0 | `api/v1/gameplay/combat/extraction.yaml` | `modules/combat/extraction` | Экстрактшутер механики |
| `02-gameplay/combat/combat-hacking-networks.md` | 1.0.0 | `api/v1/gameplay/combat/hacking/networks.yaml` | `modules/combat/hacking` | Сетевой слой хакерства |
| `02-gameplay/combat/combat-hacking-combat-integration.md` | 1.0.0 | `api/v1/gameplay/combat/hacking/combat-integration.yaml` | `modules/combat/hacking` | Интеграция хакерства в бою |
| `02-gameplay/combat/combat-cyberspace.md` | 1.0.0 | `api/v1/gameplay/combat/hacking/cyberspace.yaml` | `modules/combat/cyberspace` | Режимы киберпространства |
| `02-gameplay/combat/combat-shooting.md` | 1.1.0 | `api/v1/gameplay/combat/shooting.yaml` | `modules/combat/shooting` | TTK, отдача, имплант-модификаторы |
| `02-gameplay/combat/combat-stealth.md` | 1.1.0 | `api/v1/gameplay/combat/stealth.yaml` | `modules/combat/stealth` | Скрытность, обнаружение, импланты |
| `02-gameplay/combat/combat-abilities.md` | 1.2.0 | `api/v1/gameplay/combat/abilities.yaml` | `modules/combat/abilities` | Активные способности и синергии |
| `02-gameplay/combat/arena-system.md` | 1.0.0 | `api/v1/gameplay/combat/arena-system.yaml` | `modules/combat/arenas` | Рейтинги, режимы, voice-lobby |

---

## 3. Ключевые контракты
- **REST:** `/combat/ai/profiles`, `/combat/ai/profiles/{id}`, `/combat/ai/profiles/{id}/telemetry`, `/combat/raids/{raidId}/phase`, `/combat/ai/encounter`, `/combat/skills/abilities`, `/combat/implants`, `/combat/extract/operations`, `/combat/hacking/networks`, `/combat/hacking/encounter`, `/combat/arenas/*`.
- **WebSocket:** `wss://api.necp.game/v1/gameplay/raid/{raidId}` — события `PhaseStart`, `MechanicTrigger`, `PlayerDown`, `CheckRequired`.
- **Kafka:** `combat.ai.state`, `world.events.trigger`, `raid.telemetry`, `combat.session.events`, `combat.hacking.alerts`, `combat.arena.results`.
- **DB:** таблицы и JSONB структуры для AI профилей, способностей, фаз рейдов, combat session state, arena rankings (см. документы).
- **Shooter проверки:** пороги точности/контроля отдачи (`accuracy`, `handling`, `stability`, `resilience`), расчёт TTK, валидация сетевых попаданий.

---

## 4. Зависимости и связки
- **Сервисы:** `world-service` (ивенты, контроль зон), `social-service` (репутация), `economy-service` (награды/штрафы), `analytics-service` (телеметрия).
- **Синхронизация:** combat session ↔ combat AI ↔ arena ↔ hacking ↔ extraction.
- **Фронтенд:** модули `modules/combat/*` готовы к потреблению контрактов после генерации.
- **Связанные документы:** `combat-extract`, `combat-hacking*`, `combat-abilities`, `combat-session`, `combat-shooter-core` — все в статусе `ready` или в финализации.

---

## 5. Рекомендованный порядок задач
1. **Core AI & Shooter** — спецификации `ai-enemies`, `shooter-core`, `combat-session`.
2. **Combat Session & Telemetry** — `combat-session`, WebSocket/Kafka потоки.
3. **Support Systems** — импланты, стрельба, скрытность, способности.
4. **Hacking & Extraction** — `combat-hacking-*`, `combat-extract`, `combat-cyberspace`.
5. **Arena System** — рейтинги, голосовые лобби, matchmaking.

---

## 6. Чеклист для передачи
- [x] Все документы в `ready.md` и `readiness-tracker.yaml`.
- [x] В `current-status.md` и `TODO.md` отмечено активное направление.
- [ ] Получить разрешение на постановку задач в `API-SWAGGER`.
- [ ] Передать brief агенту ДУАПИТАСК после снятия ограничений.
- [ ] Обновить `implementation-tracker.yaml` после старта работ backend/frontend.


