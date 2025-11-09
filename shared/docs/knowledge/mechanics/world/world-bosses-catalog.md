---

---
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 12:20  
**Приоритет:** highest  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 12:20
**api-readiness-notes:** Каталог мировых боссов с фазами, уникальными навыками, сюжетными событиями и REST/WebSocket контрактами world-service. WebSocket обновлён на gateway `wss://api.necp.game/v1/world/world-boss/{eventId}`. Обновление v1.1.0 добавляет орбитальные и хакерские события, таблицы последствий и расширенную интеграцию.
---

# World Bosses Catalog — Открытые столкновения

**target-domain:** gameplay-world  
**target-microservice:** world-service (8086)  
**target-frontend-module:** modules/world/events  
**интеграции:** combat-session, social-service (репутация), economy-service (награды)

## 1. Краткое описание
- Мировые боссы — глобальные события на 20–60 игроков в открытых зонах Night City и Badlands.
- Активируются циклами лиги (Bronze → Diamond) и сюжетными флагами global-state.
- Используют гибрид D&D проверок + real-time боёвки, поддерживают фазовые WebSocket события.

## 2. Каталог боссов
| ID | Локация | Эпоха | Базовая сложность | Сюжетный триггер |
| --- | --- | --- | --- | --- |
| `wb-neon-titan` | Мега-башня "Neon Crown" | 2089 | Diamond | Завершение арки `Corporate Wars: Finale` |
| `wb-blackwall-wraith` | Разлом за Blackwall | 2091 | Mythic | Флаг `world.flag.blackwall_integrity < 40%` |
| `wb-valentinos-saint` | Собор Санто-Доминго | 2078 | Platinum | Ветка `Valentinos Blood Oath` |
| `wb-nomad-leviathan` | Badlands, зона гидротрассы | 2082 | Gold | Миссия `Nomad Convoy Crisis` |
| `wb-netwatch-sphinx` | Небоскрёб NetWatch | 2087 | Diamond | Выполнение `quest-main-042-black-barrier-heist` |
| `wb-eclipse-seraph` | Орбитальная платформа "Eclipse Array" | 2085 | Mythic | Событие `Arcology Eclipse Failsafe` |
| `wb-hivemind-behemoth` | Мега-свалка Pacifica Reclaim | 2075 | Platinum | Квест `Maelstrom Neuro Swarm` |

## 3. Фазовые сценарии
### `wb-neon-titan`
- **Фаза 1 — Corporate Siege:** уничтожить четыре щита, D&D checks TECH DC 18.
- **Фаза 2 — Drone Overlord:** Titan разделяется на три дрона, требует координации DPS/Tank.
- **Фаза 3 — Reality Sync:** уникальный навык `Spectral Audit` — обнуляет импланты игроков (WIS DC 20, иначе потеря навыков на 15 сек).
- **Лут:** `Legendary Smart Cannon`, `Arasaka Dividends`, `League Token x50`.
- **Сюжет:** исход меняет `corporate_balance` и репутацию Arasaka/Militech.

### `wb-blackwall-wraith`
- **Фаза 1 — Shattered Gate:** массовый DoT `Entropy Leak`, STR DC 21 для стабилизации зарядов.
- **Фаза 2 — Echo Storm:** навык `Neural Fracture` — случайный игрок управляется врагом (COOL DC 22 для сопротивления).
- **Фаза 3 — True Wraith:** чередует физические и цифровые формы, требует нетраннеров.
- **Лут:** `Primordial AI Core`, `Blackwall Shards`, `NetWatch Reputation +40`.
- **Сюжет:** исход влияет на ветку `The Truth Beyond Blackwall`.

### `wb-valentinos-saint`
- **Фаза 1 — Procession Ambush:** защита NPC, использование `Honor Duel` (COOL DC 18).
- **Фаза 2 — Martyr's Rage:** навык `Blazing Devotion` — усиливает боссов в радиусе, требует точечного фокуса.
- **Фаза 3 — Santo Judgment:** массовый AoE, игроки выбирают пощадить или казнить босса, влияет на репутацию Valentinos/NCPD.
- **Лут:** `Saint Relic Cyberarm`, `Valentinos Contacts`, `Romance Unlock Flag`.

### `wb-nomad-leviathan`
- **Фаза 1 — Convoy Defense:** защита колонны, навык босса `Sandstorm Barrage` (REF DC 17 для уклонения).
- **Фаза 2 — Adaptive Armor:** требует инженерного взаимодействия (TECH DC 20) для снятия щитов.
- **Фаза 3 — Reactor Overload:** таймер 90 сек, навык `Reactor Implosion` — провал = wipe.
- **Лут:** `Nomad Supercharger`, `Convoy Access`, `Legendary Vehicle Skin`.

### `wb-netwatch-sphinx`
- **Фаза 1 — Firewall Gauntlet:** D&D INT DC 19 для отключения ICE.
- **Фаза 2 — Logic Puzzles:** мини-игры, навык `Logic Bomb` (WIS DC 19).
- **Фаза 3 — Sphinx Core:** чередует вопросы/ответы (диалоговые проверки) и боевые арены.
- **Лут:** `Sphinx Cyberdeck`, `NetWatch Clearance`, `Global Event Trigger`.

### `wb-eclipse-seraph`
- **Фаза 1 — Orbital Lockdown:** `Solar Lance` — линейный луч по спутниковому наведению (DEX DC 21 для уклонения).
- **Фаза 2 — Gravity Rift:** `Vector Break` — меняет гравитацию, игроки прилипают к стенам (STR DC 20 для удержания пози).
- **Фаза 3 — Failsafe Override:** `Ablation Protocol` — отключает все щиты и импланты, требуется синхронный взлом (TECH DC 22) + координация с world-service дронами.
- **Лут:** `Eclipse Thruster`, `Orbital Shield Core`, `League Token x70`.
- **Сюжет:** успех стабилизирует орбитальный щит Night City, провал вызывает событие `Solar Storm`.

### `wb-hivemind-behemoth`
- **Фаза 1 — Scrap Swarm:** призывает волны дронов-хламов, навык `Rust Flood` (CON DC 18).
- **Фаза 2 — Neuro Mesh:** `Hivemind Overclock` — связывает игроков нейронным полем, успех зависит от командных проверок (COOP DC 17).
- **Фаза 3 — Core Detonation:** `Waste Cascade` — ядовитая буря, требуется активация очистителей (ENGINEER role, TECH DC 19).
- **Лут:** `Hivemind Neural Lens`, `Maelstrom Reputation +40`, `Rare Cyberware Components`.
- **Сюжет:** влияет на Maelstrom/NCPD отношения и доступ к подземным аренам Pacifica.

## 4. Уникальные навыки по лигам
| Лига | Модификатор | Пример способности |
| --- | --- | --- |
| Bronze | Вводные механики | `Shield Pulse` (простое снятие щита) |
| Silver | +Добавочные адды | `Corpo Reinforcements` (вызов волн) |
| Gold | Усложнённые D&D проверки | `Network Spike` (INT DC +2) |
| Platinum | Адаптивные фазы | `Behavior Shift` (меняет паттерн после 50%) |
| Diamond | Комбинированные навыки | `Reality Sync` + `Logic Bomb` |
| Mythic | Постоянные дебаффы | `Existential Overwrite` (перманентный -10% стат) |

## 5. Телеметрия и динамика
- WebSocket канал `wss://api.necp.game/v1/world/world-boss/{eventId}` публикует `PhaseStart`, `AbilityCast`, `DndCheck`, `PlayerDown`, `LootRoll`.
- Kafka topics: `world.boss.spawn`, `world.boss.telemetry`, `world.boss.outcome`.
- Автотюнинг: analytics-service корректирует HP и урон на основе среднего времени убийства.

### Таблица последствий (post-event)
| Исход | Мировой флаг | Эффект |
| --- | --- | --- |
| Поражение `wb-blackwall-wraith` | `world.flag.blackwall_integrity -10` | Усиление событий за пределами Blackwall, рост агрессии Rogue AI |
| Победа над `wb-valentinos-saint` (пощадить) | `rep.street.valentinos +35` | Новые ветки романов и скидки в социальном модуле |
| Победа над `wb-eclipse-seraph` | `world.flag.orbital_shield = stable` | Снижение сложности воздушных рейдов, открытие миссий Aero Division |
| Провал `wb-hivemind-behemoth` | `rep.law.ncpd -15` | Усиление патрулей NCPD и рост цены на киберимпланты |

## 6. REST API (world-service)
| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/world/bosses` | `GET` | Каталог боссов, фильтр по лиге/эпохе/фракции |
| `/world/bosses/{id}` | `GET` | Детали босса, фазы, лут, D&D проверки |
| `/world/bosses/{id}/schedule` | `GET` | Таймслоты появления, привязка к world-state |
| `/world/bosses/{id}/signup` | `POST` | Запись отряда на событие (до начала) |
| `/world/bosses/{id}/outcome` | `POST` | Финальный результат, выдача наград |
| `/world/bosses/{id}/aftermath` | `POST` | Фиксация последствий (world flags, reputation), публикация в analytics-service |

## 7. Схемы данных
```sql
CREATE TABLE world_bosses (
    boss_id VARCHAR(64) PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    location VARCHAR(120) NOT NULL,
    timeline VARCHAR(32) NOT NULL,
    base_difficulty VARCHAR(16) NOT NULL,
    narrative_trigger JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE world_boss_phases (
    boss_id VARCHAR(64) REFERENCES world_bosses(boss_id) ON DELETE CASCADE,
    phase_index INTEGER NOT NULL,
    description TEXT NOT NULL,
    unique_ability VARCHAR(64) NOT NULL,
    dnd_checks JSONB,
    rewards JSONB,
    PRIMARY KEY (boss_id, phase_index)
);

CREATE TABLE world_boss_schedules (
    boss_id VARCHAR(64) REFERENCES world_bosses(boss_id) ON DELETE CASCADE,
    league VARCHAR(16) NOT NULL,
    window_start TIMESTAMP NOT NULL,
    window_end TIMESTAMP NOT NULL,
    spawned BOOLEAN DEFAULT FALSE,
    world_flags JSONB,
    PRIMARY KEY (boss_id, league, window_start)
);
```

## 8. Награды и экономика
- Награды масштабируются от Bronze до Mythic (x1.0 → x2.5).
- Привязка к economy-service: таблица `world_boss_loot` синхронизируется с аукционом и крафтом.
- Репутация: social-service обновляет фракционные очки (`rep.corp.arasaka`, `rep.street.valentinos`).
- Battle Pass: world-service публикует `battle-pass.progress` события.

## 9. Лор и NPC
- `Operator Lynx` — координатор NetWatch, запускает `wb-blackwall-wraith`.
- `Fixer Alma "Spark"` — даёт оповещения о `wb-neon-titan`.
- `Padre Alvarez` — решает исход `wb-valentinos-saint` (влияет на диалоги романсов).
- `Nomad Charon` — связывает `wb-nomad-leviathan` с кланами Badlands.
- `Commander Rhea Tesla` — управляет орбитальной платформой `Eclipse Array`, выдаёт weekly рейды.
- `Maelstrom Handler "Krieg"` — запускает событие `Hivemind Behemoth`, открывает нелегальные магазины.

## 10. Наблюдаемость и аналитика
- Метрика `worldBossClearTime` — среднее время убийства по лиге и фракции.
- Метрика `worldBossDndFailRate` — процент проваленных проверок (чтобы балансировать DC).
- Метрика `worldBossAftermathImpact` — изменение world flags и репутаций.
- Логи: `spawn`, `phase_transition`, `ability_cast`, `dnd_check`, `outcome`, `aftermath_applied`.

## 11. Готовность
- Каталог расширен (v1.1.0), добавлены орбитальные/свалочные события и таблица последствий.
- REST/WS контракты дополнены эндпоинтом `aftermath`, схемы данных актуальны.
- Документ готов к передаче в API Task Creator и связан с `dungeon-bosses-catalog.md` и `combat-ai-enemies.md`.

## 12. Пример payload (analytics-service)
```json
{
  "eventId": "wb-eclipse-seraph",
  "league": "MYTHIC",
  "phase": 3,
  "ability": "Ablation Protocol",
  "dndCheck": {
    "attribute": "TECH",
    "dc": 22,
    "successRate": 0.64
  },
  "clearTimeSeconds": 982,
  "aftermath": {
    "worldFlag": "world.flag.orbital_shield",
    "value": "stable",
    "reputation": {
      "rep.corp.arasaka": 12,
      "rep.law.ncpd": 5
    }
  }
}
```

## 13. Пример ротации (world-service schedule)
| Лига | День цикла | Босс | Особенность |
| --- | --- | --- | --- |
| Bronze | Пн/Чт | `wb-nomad-leviathan` | Активирует караваны Nomad Coalition |
| Silver | Вт/Пт | `wb-valentinos-saint` | Открывает romance-провокации |
| Gold | Ср/Сб | `wb-neon-titan` | Усиливает корпоративные рейды |
| Platinum | Вс | `wb-hivemind-behemoth` | Запускает Maelstrom нейро-шкалу |
| Mythic | Каждые две недели | `wb-eclipse-seraph` / `wb-blackwall-wraith` | Переключается в зависимости от world.flag.blackwall_integrity |

