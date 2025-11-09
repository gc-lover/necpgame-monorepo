---

---
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 12:20  
**Приоритет:** high  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** Каталог боссов подземелий: фазы, уникальные навыки по уровням сложности, таблицы данных и API world-service. WebSocket обновлён на gateway `wss://api.necp.game/v1/world/dungeons/{instanceId}/boss`. Обновление v1.1.0 добавляет два новых босса, модификаторы Apex+ и аналитические метрики.
---

# Dungeon Bosses Catalog — Инстансовые лидеры

**target-domain:** gameplay-world/dungeons  
**target-microservice:** world-service (8086)  
**target-frontend-module:** modules/world/dungeons  
**интеграции:** combat-session, economy-service (Blueprint Forge), progression-service (skill unlocks)

## 1. Структура инстанса
- Подземелья рассчитаны на 4–10 игроков (Normal), 8–16 (Hard), 10–20 (Apex).
- Каждый босс связан с сюжетной веткой и выдает ключи для Hard Mode.
- Механики комбинируют шутерные фазы, D&D проверки и взломы.

## 2. Каталог боссов
| ID | Подземелье | Тип | Сложность | Лор |
| --- | --- | --- | --- | --- |
| `db-echo-guardian` | Blackwall Echo | Ritual | Gold → Diamond | Guardian AI NetWatch |
| `db-void-maestro` | Neon Trials | Gauntlet | Platinum | Maestro арены Night City |
| `db-bio-harvester` | Substructure 77 | Overrun | Gold | Hydra Biotechnica |
| `db-specter-warden` | Data Reliquary | Heist | Platinum | Arasaka Shadow Ops |
| `db-rail-tyrant` | Ghost Freight | Escort | Gold | Nomad renegade |
| `db-glass-reaper` | Aurora Vault | Heist | Platinum → Diamond | Cult of the Wave |
| `db-cinder-archon` | Reactor Depths | Gauntlet | Mythic | Petrochem Black Ops |

## 3. Подробности боссов
### `db-echo-guardian`
- **Фаза 1 — Resonance Shield:** навык `Mirror Pulse` (WIS DC 18) — отражает урон на 50%.
- **Фаза 2 — Algorithm Shift:** нетраннеры выполняют мини-игры, иначе `Glitch Cascade` (INT DC 19).
- **Фаза 3 — Blackwall Surge:** уникальный навык `Singularity Anchor` — притягивает игроков (STR DC 20).
- **Hard Mode:** постоянный дебафф `Cognitive Static` (-15% энерго-реген).
- **Loot:** `Echo Resonator`, `NetWatch favor`, `Dungeon Token x10`.

### `db-void-maestro`
- **Фаза 1 — Performance Duel:** `Rhythm Strike` — чередование атак, требуется синхронные уклонения.
- **Фаза 2 — Crowd Control:** зрители активируют модификаторы (случайные бафы/дебаффы).
- **Фаза 3 — Final Symphony:** `Neon Crescendo` — шкала звука, WIS DC 19 для сопротивления псих-урону.
- **Apex Mode:** добавлен `Backstage Ambush` — скрытые элиты.
- **Loot:** `Maestro Visor`, `Arena Emote Pack`, `Reputation Underground +20`.

### `db-bio-harvester`
- **Фаза 1 — Spore Swarm:** навык `Biohazard Cloud` (CON DC 18) — отравление.
- **Фаза 2 — Adaptive Mutation:** босс меняет тип урона каждые 30 сек, требуется анализ инженеров.
- **Фаза 3 — Core Extraction:** таймер 120 сек, `Harvest Protocol` — уничтожение игроков без защит.
- **Hard Mode:** глобальный дебафф `Toxic Pressure` (минус 10% макс HP).
- **Loot:** `Biotech Catalyst`, `Crafting Blueprint`, `Battle Pass progress`.

### `db-specter-warden`
- **Фаза 1 — Security Layers:** `Specter Mark` — фиксирует игрока (DEX DC 18 для освобождения).
- **Фаза 2 — Shadow Execution:** навык `Zero Trace` — убивает цель с низким HP, требует активировать `Counter Protocol` (TECH DC 20).
- **Фаза 3 — Silent Collapse:** полностью затемнённая арена, игроки пользуются термальным зрением (импланты обязательны).
- **Apex Mode:** добавлен `Corporate Sanction` — вызов элитных охранников.
- **Loot:** `Specter Cloak`, `Arasaka Black Credit`, `Hard Mode Keycard`.

### `db-rail-tyrant`
- **Фаза 1 — Rail Assault:** `Electro Volley` (REF DC 17) — поражает конвой.
- **Фаза 2 — Hijack Maneuver:** игроки разделяются на две группы для защиты и атаки.
- **Фаза 3 — Reactor Meltdown:** `Overclock Crash` — таймер 60 сек, синхронное отключение четырёх модулей.
- **Hard Mode:** добавлена `Nomad Betrayal` — случайные NPC-дезертиры.
- **Loot:** `Nomad Railgun`, `Convoy Route Access`, `Legendary Vehicle Mod`.

### `db-glass-reaper`
- **Фаза 1 — Prism Vault:** `Light Fracture` — зеркала рассеивают урон, требуется позиционирование (INT DC 19).
- **Фаза 2 — Mirror Assassins:** босс создает зеркальные тени, активирует `Spectral Execution` (COOL DC 20).
- **Фаза 3 — Wave Resonance:** `Aurora Collapse` — синхронный взлом четырёх кристаллов (TECH DC 21).
- **Apex Mode:** добавлен `Reality Fade` — игроки временно оказываются в теневом мире (WIS DC 21).
- **Loot:** `Prism Blade`, `Wave Cult Reputation`, `Aurora Keycard`.

### `db-cinder-archon`
- **Фаза 1 — Ignition Matrix:** `Thermal Lance` — прожигает пол, игроки переходят на верхние платформы.
- **Фаза 2 — Reactor Shields:** `Cinder Reflector` — требует танкование и синхронный сброс энергии (STR DC 20 + TECH DC 20).
- **Фаза 3 — Archon Verdict:** `Inferno Protocol` — 90 секунд на уничтожение ядерного ядра.
- **Apex Mode:** добавлен `Ash Storm` — постепенное заполнение арены, каждую минуту CON DC +2.
- **Loot:** `Archon Reactor Core`, `Petrochem Favors`, `Mythic Blueprint`.

## 4. Уровни сложности и уникальные навыки
| Mode | Особенности | Уникальные навыки |
| --- | --- | --- |
| Normal | Базовые механики, минимальные проверки | `Mirror Pulse`, `Rhythm Strike`, `Biohazard Cloud`, `Specter Mark`, `Electro Volley`, `Light Fracture`, `Thermal Lance` |
| Hard | Усложнённые проверки, новые адды | `Glitch Cascade`, `Backstage Ambush`, `Adaptive Mutation`, `Zero Trace`, `Hijack Maneuver`, `Spectral Execution`, `Cinder Reflector` |
| Apex | Постоянные дебаффы, таймеры | `Singularity Anchor`, `Neon Crescendo`, `Harvest Protocol`, `Silent Collapse`, `Overclock Crash`, `Aurora Collapse`, `Inferno Protocol` |
| Apex+ | Перманентные DoT, двойные таймеры | `Reality Fade`, `Ash Storm` |

## 5. REST API (world-service)
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/world/dungeons/{dungeonId}/bosses` | `GET` | Каталог боссов подземелья |
| `/world/dungeons/{dungeonId}/bosses/{bossId}` | `GET` | Детали босса, фазы, навыки |
| `/world/dungeons/bosses/{bossId}/checkpoint` | `POST` | Фиксация прогресса фазы |
| `/world/dungeons/bosses/{bossId}/difficulty` | `PUT` | Смена сложности (Normal/Hard/Apex) |
| `/world/dungeons/bosses/{bossId}/rewards` | `POST` | Распределение наград |
| `/world/dungeons/bosses/{bossId}/aftermath` | `POST` | Применение world flags и репутаций после победы/поражения |

## 6. WebSocket и телеметрия
- `wss://api.necp.game/v1/world/dungeons/{instanceId}/boss` — события `PhaseStart`, `AbilityTrigger`, `DndCheck`, `Failure`, `Success`.
- Kafka: `dungeon.boss.telemetry`, `dungeon.boss.outcome`, `dungeon.boss.progress`.
- Analytics: отслеживание wipe rate, время убийства, эффективность контр-стратегий.

## 7. Схемы данных
```sql
CREATE TABLE dungeon_bosses (
    boss_id VARCHAR(64) PRIMARY KEY,
    dungeon_id VARCHAR(64) NOT NULL,
    boss_type VARCHAR(32) NOT NULL,
    base_difficulty VARCHAR(16) NOT NULL,
    lore_hook TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dungeon_boss_phases (
    boss_id VARCHAR(64) REFERENCES dungeon_bosses(boss_id) ON DELETE CASCADE,
    phase_index INTEGER NOT NULL,
    description TEXT NOT NULL,
    unique_ability VARCHAR(64) NOT NULL,
    dnd_checks JSONB,
    loot JSONB,
    PRIMARY KEY (boss_id, phase_index)
);

CREATE TABLE dungeon_boss_difficulties (
    boss_id VARCHAR(64) REFERENCES dungeon_bosses(boss_id) ON DELETE CASCADE,
    mode VARCHAR(16) NOT NULL,
    modifiers JSONB NOT NULL,
    rewards_multiplier NUMERIC(4,2) NOT NULL,
    PRIMARY KEY (boss_id, mode)
);
```

## 8. Награды и прогрессия
- Loot распределяется через economy-service, таблица `dungeon_boss_loot`.
- Прогрессия: progression-service выдаёт навыки/таланты (например, `Neon Crescendo Resist`).
- Battle Pass: каждая победа даёт `Dungeon Badge`.
- Reputation: social-service обновляет очки (`rep.underground`, `rep.corp.arasaka`, `rep.nomad`, `rep.wave_cult`, `rep.petrochem`).
- Таблица последствий:
  - Победа над `db-glass-reaper` открывает миссии `Wave Cult Shadow Contracts`.
  - Победа над `db-cinder-archon` снижает риск событий Petrochem в Badlands.
  - Провал `db-rail-tyrant` усиливает налеты marauders (world flag `nomad_routes` -10).

## 9. Связь с Dungeon Scenarios
- Соотнесён с `dungeon-scenarios-catalog.md` (идентификаторы совпадают).
- Готовые данные для API-SWAGGER: `api/v1/gameplay/world/dungeons.yaml` получает блок `bosses`.
- Контент синхронизирован с `combat-ai-enemies.md` (уникальные навыки и уровни сложности).
- `wb-hivemind-behemoth` и `db-bio-harvester` делят общие компоненты лута и влияют на одни и те же события экономики.

## 10. Аналитика
- Метрика `dungeonBossClearRate` — процент успешных проходов по сложности и составу группы.
- Метрика `dungeonBossDndFailRate` — частота провалов D&D проверок, используется для балансировки DC.
- Метрика `dungeonBossTimeToKill` — среднее время убийства.
- Логи: `phase_transition`, `ability_trigger`, `modifier_active`, `aftermath_applied`.

## 11. Готовность
- Каталог расширен до v1.1.0 (новые боссы, Apex+ мод и последствия).
- REST/WS контракты дополнены эндпоинтом `aftermath`, схемы данных актуальны.
- Документ готов к передаче в API Task Creator и синхронизирован с `world-bosses-catalog.md` и `dungeon-scenarios-catalog.md`.

## 12. План ротации инстансов
| Неделя | Подземелье | Босс | Бонусный модификатор |
| --- | --- | --- | --- |
| 1 | Blackwall Echo | `db-echo-guardian` | +10% NetWatch репутация |
| 2 | Neon Trials | `db-void-maestro` | +15% Arena Fame |
| 3 | Substructure 77 | `db-bio-harvester` | Удвоенные био-компоненты |
| 4 | Data Reliquary | `db-specter-warden` | Эксклюзивные Arasaka чертежи |
| 5 | Ghost Freight | `db-rail-tyrant` | Двойные Nomad Tokens |
| 6 | Aurora Vault | `db-glass-reaper` | +20% Wave Cult Favor |
| 7 | Reactor Depths | `db-cinder-archon` | +10% Petrochem Credits |
| 8 | Ротация повторяется, Hard/Apex получают +1 уровень аффиксов |

