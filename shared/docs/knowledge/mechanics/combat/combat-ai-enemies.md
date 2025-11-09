---
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-09 01:22  
**Приоритет:** highest  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 01:22
**api-readiness-notes:** Комплексная матрица AI для всех категорий NPC. WebSocket `wss://api.necp.game/v1/gameplay/raid/{raidId}`, REST/WS контракты и интеграции перепроверены 2025-11-09 01:22 — документ готов для постановки задач API.
**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (8083)  
**target-frontend-module:** modules/combat/ai
---

# COMBAT AI ENEMIES MATRIX

## 1. Архитектура AI
- **Сервисы:** `gameplay-service (8083)` управляет боевыми состояниями, `world-service (8086)` триггерит события и рейды, `social-service (8084)` обновляет репутацию за взаимодействия с NPC, `economy-service (8085)` выдаёт награды и штрафы.
- **Модули фронтенда:** `combat`, `world`, `social` отображают поведение AI, индикаторы угроз и тактические подсказки.
- **Слои поведения:**
  1. `Street Layer` — базовые NPC эпох 2020-2060 (полиция, банды).
  2. `Tactical Layer` — корпоративные отряды 2060-2093 и элита фракций.
  3. `Mythic Layer` — киберпсихи, легендарные враги и мировые события.
  4. `Raid Layer` — сценарии рейдов (Blackwall Expedition, Corpo Tower Assault).
- **Контроллер:** Behaviour Tree + Utility AI + shooter skill tests (accuracy, resilience, tech mastery). Очередь действий синхронизируется через `combat_session` (WebSocket) и Kafka-топики.

### Kafka топики
| Топик | Producer | Consumer | Payload |
| --- | --- | --- | --- |
| `combat.ai.state` | gameplay-service | analytics-service, world-service | `enemyId`, `state`, `threatLevel`, `timestamp` |
| `world.events.trigger` | world-service | gameplay-service | `eventId`, `stage`, `aiModifier` |
| `raid.telemetry` | gameplay-service | analytics-service | `raidId`, `phase`, `bossHP`, `playerDown`, `checkResult` |

## 2. Матрица уровней сложности

| Слой | Категории NPC | Уровень сложности | Уникальный навык | Описание |
| --- | --- | --- | --- | --- |
| Street | NCPD Patrol, 6th Street, Scavs | Bronze-Silver | `Urban Sweep` | Короткие рывки из укрытий, усиливаются, когда игрок нарушает закон в районе (сюжет: восстановление порядка в Watson 2023-2028). |
| Street | Valentinos, Tyger Claws | Silver-Gold | `Honor Duel` | Предлагают одиночный бой, при отказе вызывают подкрепление. Подвязывается к репутации в `social-service`. |
| Tactical | Arasaka SpecOps, Militech Strike | Gold-Platinum | `Sync Burst` | Синхронизированный залп после счёта | lore: корпоративные войны 2060-2077. |
| Tactical | NetWatch Drones, Kang Tao Mechs | Platinum | `Protocol Lock` | Блокируют способности, пока netrunner не применит `tech_mastery ≥ 72` или контр-имплант `firewall-breaker`. |
| Mythic | MaxTac, Cyberpsycho, Rogue AI | Diamond | `Fear Surge` | Массовый дебафф морали, требует `resilience ≥ 78` либо активного `psy-shield` импланта. |
| Raid | Blackwall Entities | Mythic+ | `Reality Tear` | Перенос игроков в карманы реальности; необходимо удерживать `stability ≥ 80` и поддерживать `sync-link` squad. |
| Raid | Corpo Tower Guardians | Mythic+ | `Corporate Ultimatum` | Таймер на уничтожение целей, иначе массовая казнь NPC союзников (сюжет операции 2088). |

## 3. Поведенческие профили

### Street Layer
- **Изменение тактики:** Стадии тревоги (низкая, эскалация, критическая). Переходы зависят от `threatDetection` (шанс обнаружения ≥ 65%) и `noise_level` из `combat-stealth`.
- **Навыки:**
  - `Urban Sweep` (Cooldown 12s, AoE suppression, наносит `suppressionScore = 40`).
  - `Adrenal Shift` (при HP < 40%, +15% скорость реакции, lore: стимуляторы Arasaka 2055).
  - `Flash Escort` (вызов дронов сопровождения, доступно после сюжетной арки NCPD 2070).
- **Интеграция с лором:** выбор тактик зависит от статуса района в `world-state`: если район под контролем корпорации, NPC используют корпоративные гаджеты.

### Tactical Layer
- **Фракции:** Arasaka, Militech, Kang Tao, NetWatch, Voodoo Boys (сетевые фантомы).
- **Ротация фаз:**
  1. `Recon` — анализ укрытий, скан игроков (`Scan Pulse`, требует `stealth_resistance ≥ 62` для сохранения невидимости).
  2. `Engage` — применение `Sync Burst`, `Protocol Lock`.
  3. `Adapt` — смена паттернов в зависимости от действий игроков (анализ через аналитический сервис).
- **Уникальные навыки:**
  - Arasaka: `Silent Rift` (мгновенное перемещение двух бойцов, история спецоперации 2072).
  - Militech: `Saturation Fire` (LMG залп, подавление, требуется Tank блокировка).
  - Kang Tao: `Smart Swarm` (дроны, наведение по тепловой сигнатуре).
  - NetWatch: `ICE Burst` (заморозка кибердеков; контрится `tech_mastery ≥ 74`).
  - Voodoo Boys: `Digital Possession` (краткое управление имплантом игрока; требуется `psy_resistance ≥ 77`).

### Mythic Layer
- **Типы:** MaxTac, Cyberpsycho, Rogue AI Avatars, Legendary Fixers.
- **Триггеры сюжета:**
  - `Cyberpsycho Resurgence` (2084) — миссии NCPD, игроки выбирают спасти или устранить.
  - `Blackwall Breach` (2090) — Rogue AI выходит в мир, приводит к активу Rogue AI Avatars.
- **Навыки:**
  - MaxTac: `Zero Strike` (одно целевое отключение, требует синхронный `guard_response ≥ 82` от Tank).
  - Cyberpsycho: `Overclock Rage` (stack damage, прекращается при `tech_mastery ≥ 76` или импланте `neuro-dampener`).
  - Rogue AI: `Adaptive Mirror` (копирует последнюю способность игрока, требует вариативность в тимплее).
- **Мораль и страх:** AI отслеживает `morale` и `fear` по таблице, влияет на вероятность отступления или berserk; игроки снижают эффект через `resilience` и способности `combat-abilities`.

### Raid Layer
- **Сценарии:** `raid-blackwall-expedition`, `raid-corpo-tower-assault`.
- **Фазовые состояния:** `Preparation`, `PhaseCombat`, `Intermission`, `Finale`. Каждое состояние публикуется в `raid.telemetry`.
- **Навыки босса:**
  - Blackwall Entity: `Reality Tear`, `Neural Cascade`, `Entropy Spiral` (stacking DoT, очищается при `tech_mastery ≥ 80` или активации `stability_matrix`).
  - Corpo Tower AI: `Corporate Ultimatum`, `Omni Turret Grid`, `Collateral Threat` (NPC заложники, выбор игрока влияет на сюжетные флаги).
- **Уникальные рейдовые механики:** связки с квестами (`quest-main-042-black-barrier-heist`, `quest-main-022-corporate-wars-operation`). Разрешение влияет на глобальные флаги `world.flag.corporate_balance` и `world.flag.blackwall_integrity`.

## 4. YAML профиль AI

```yaml
aiprofile:
  id: "max-tac-captain-2091"
  layer: "Mythic"
  faction: "NCPD-MaxTac"
  difficulty: "Diamond"
  narrativeContext:
    era: "2091"
    event: "Cyberpsycho Resurgence"
    questHook: "quest-side-maelstrom-double-cross"
  stats:
    level: 55
    hp: 3800
    defenseRating: 24
    morale: 95
  abilities:
    - id: "zero-strike"
      cooldown: 45
      effect: "singleTargetDisable"
      counters: ["tank-taunt", "shield-dome"]
    - id: "fear-surge"
      cooldown: 30
      effect: "aoe-morale-break"
      skillTest:
        metric: "resilience"
        threshold: 78
    - id: "drone-command"
      cooldown: 25
      effect: "summon"
      spawn:
        type: "SupportDrone"
        count: 2
  lootTable:
    guaranteed: ["max-tac-insignia"]
    legendaryChance: 0.18
  worldImpact:
    reputation:
      rep.law.ncpd: 18
    flags:
      world.flag.maxTac_trust: true
```

## 5. Схемы данных

```sql
CREATE TABLE enemy_ai_profiles (
    id VARCHAR(64) PRIMARY KEY,
    layer VARCHAR(16) NOT NULL,
    faction VARCHAR(64) NOT NULL,
    difficulty VARCHAR(16) NOT NULL,
    narrative_context JSONB NOT NULL,
    base_stats JSONB NOT NULL,
    morale INTEGER NOT NULL,
    fear INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE enemy_ai_abilities (
    profile_id VARCHAR(64) REFERENCES enemy_ai_profiles(id) ON DELETE CASCADE,
    ability_id VARCHAR(64) NOT NULL,
    cooldown_seconds INTEGER NOT NULL,
    effect VARCHAR(64) NOT NULL,
    saving_throw JSONB,
    counters JSONB,
    PRIMARY KEY (profile_id, ability_id)
);

CREATE TABLE raid_boss_phases (
    boss_id VARCHAR(64) NOT NULL,
    phase INTEGER NOT NULL,
    hp_threshold INTEGER NOT NULL,
    mechanics JSONB NOT NULL,
    skill_tests JSONB NOT NULL,
    PRIMARY KEY (boss_id, phase)
);
```

## 6. REST и WebSocket контракты (`gameplay-service`)

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/combat/ai/profiles` | `GET` | Список профилей с фильтрами `layer`, `faction`, `difficulty`. |
| `/combat/ai/profiles/{id}` | `GET` | Полный профиль AI, включая способности и лор-контекст. |
| `/combat/ai/profiles/{id}/telemetry` | `POST` | Отправка телеметрии с клиента (damage dealt, counters применены). |
| `/combat/raids/{raidId}/phase` | `POST` | Фиксация смены фазы, публикация в `raid.telemetry`. |
| `/combat/ai/encounter` | `POST` | Старт встречи, принимает `locationId`, `playerParty`, `worldFlags`. |

**WebSocket:** `wss://api.necp.game/v1/gameplay/raid/{raidId}` — поток состояния фаз, HP босса, активных механик. События: `PhaseStart`, `MechanicTrigger`, `PlayerDown`, `CheckRequired`.

## 7. Shooter skill tests и пороги
- **Street Layer:** `evasion_threshold ≥ 66` для ухода от `Urban Sweep`, `tech_mastery ≥ 62` для отключения дронов сопровождения.
- **Tactical Layer:** `tech_mastery ≥ 74` для взлома `Protocol Lock`, `awareness ≥ 70` чтобы заметить подготовку `Silent Rift`.
- **Mythic Layer:** `resilience ≥ 78` против `Fear Surge`, `tech_mastery ≥ 76` для стабилизации киберпсихов без летального исхода (поддерживает репутацию NCPD).
- **Raid Layer:** `stability ≥ 80` для противодействия `Reality Tear`, `strength_support ≥ 82` для удержания силовых колонн в Corpo Tower (операция 2088).

## 8. Баланс и телеметрия
- **Цели сложности:** Street — 65% победа соло, Tactical — 55% для групп, Mythic — 35%, Raid — 20% (первый clear).
- **Метрики:** средний урон по игрокам, время до первого падения, доля успешных shooter skill tests, использование контр-способностей.
- **Автотюнинг:** analytics-service пересчитывает коэффициенты каждые 24 часа, обновления откладываются, пока world-event активен.
- **Анти-эксплойт:** повторяющиеся однообразные действия снижают эффективность навыка AI (Utility AI снижает вес паттерна).

## 9. Сюжетные привязки
- **Watson Purge 2023:** появление NCPD патрулей с `Urban Sweep` после рейда на Maelstrom.
- **Corporate Wars 2060-2077:** усиление `Sync Burst` и `Saturation Fire`, открытие рейда Corpo Tower.
- **Cyberpsycho Surge 2084:** активирует Mythic Layer, добавляет уникальные награды `max-tac-insignia`.
- **Blackwall Crisis 2090-2093:** запускает рейдовые механики, изменяет мировые флаги и диалоги NPC (`npc-hiroshi-tanaka`, `npc-viktор-vektor`).

## 10. Тест-подход
- **Unit:** валидация Behaviour Tree узлов, проверка cooldown и условий с Mock-сессией.
- **Integration:** симуляция встречи 4 типов сложностей, проверка Kafka сообщений и синхронизации фаз.
- **Narrative QA:** соответствие реплик NPC изменению мировых флагов и репутации.
- **Load:** стресс-тест рейдов на 20k событий в минуту, проверка лагов и отката способности `Reality Tear`.
- **Telemetry Review:** еженедельный отчёт по провалам shooter skill tests, корректировка порогов `accuracy/resilience/tech_mastery`.

## 11. История изменений
- v1.0.0 (2025-11-07) — базовая матрица AI по слоям NPC, рейдам и сюжетным событиям.
