# Подготовка пакета для combat abilities

**Приоритет:** high  
**Статус:** in_progress  
**Ответственный:** Brain Manager  
**Старт:** 2025-11-09 01:42  
**Связанные документы:** `.BRAIN/02-gameplay/combat/combat-abilities.md`

---

## Прогресс
- `combat-abilities.md` перепроверен 2025-11-09 01:42: статус `approved`, `api-readiness: ready`, каталог `api/v1/gameplay/combat/abilities.yaml`, фронтенд `modules/combat/abilities`.
- Зафиксированы источники (дерево прокачки, импланты, экипировка), ранги, cooldown, синергии и влияние на киберпсихоз.
- Собраны зависимости: `combat-implants-types`, `combat-combos-synergies`, `combat-shooting`, `combat-stealth`, `combat-ai-enemies`, `progression-backend`, `quest-engine-backend`.

## REST/Events (черновик)
- **REST:** `/combat/abilities/catalog`, `/combat/abilities/{abilityId}`, `/combat/abilities/loadouts`, `/combat/abilities/synergies`, `/combat/abilities/cooldowns`.
- **Events:** `combat.ability.activated`, `combat.ability.cooldown-started`, `combat.ability.cooldown-finished`, `combat.ability.synergy-triggered`, `combat.ability.cyberpsychosis-updated`.
- **WebSocket (опционально):** `wss://api.necp.game/v1/gameplay/combat/abilities/{sessionId}` для real-time обновлений эффектов.

## Storage
- Таблицы `abilities_catalog`, `ability_loadouts`, `ability_synergies`, `ability_cooldowns`, `ability_effects` (JSONB описание эффектов и модификаторов).

## Следующие действия
1. Уточнить с combat wave пакетами необходимость общего WebSocket для сессий (возможно, объединить потоки abilities/freerun/combos).
2. После получения слота у ДУАПИТАСК разложить задачи по Stage P0 (catalog/loadouts/activate), Stage P1 (synergies/cyberpsychosis), Stage P2 (metrics/модерация).
3. Обновить `TODO.md`, `current-status.md`, `readiness-tracker.yaml` при передаче брифа.

---

## Бриф (черновик) — 2025-11-09 14:25

### REST
| Endpoint | Описание | Приоритет | Примечания |
| --- | --- | --- | --- |
| `GET /combat/abilities/catalog` | Возвращает каталог способностей (источник, ранг, тип, эффекты, требования, влияние на киберпсихоз) | P0 | Использует `abilities_catalog`, поддерживает фильтры по источнику/рангу |
| `GET /combat/abilities/{abilityId}` | Детальная карточка способности (JSONB эффектов, модификаторы имплантов/экипировки) | P0 | Интеграция с `combat-implants-types`, `combat-abilities.md` |
| `POST /combat/abilities/loadouts` | Настройка активных способностей персонажа (слоты, синергии) | P0 | Требует проверки progression и слотов; события `combat.ability.loadout-updated` |
| `GET /combat/abilities/loadouts` | Выдаёт текущую конфигурацию персонажа (слоты, cooldown, активные эффекты) | P0 | |
| `POST /combat/abilities/activate` | Активация способности в бою (context, sessionId, target) | P0 | Возвращает эффект и запускает cooldown |
| `POST /combat/abilities/cooldowns` | Принудительное восстановление/обновление кулдауна (GM/скрипт) | P1 | Событие `combat.ability.cooldown-reset` |
| `GET /combat/abilities/synergies` | Список доступных синергий (abilityId ↔ abilityId / импланты) | P1 | Учитывает `combat-combos-synergies` |
| `POST /combat/abilities/synergies/apply` | Зарегистрировать активацию синергии (abilityId, comboId, modifiers) | P1 | Публикует `combat.ability.synergy-triggered` |
| `POST /combat/abilities/cyberpsychosis` | Обновление уровня киберпсихоза персонажа (increment/decrement) | P1 | Триггерит события и ограничения |
| `GET /combat/abilities/metrics` | Аггрегированная статистика использования способностей | P2 | Для аналитики/баланса |

### WebSocket
- `wss://api.necp.game/v1/combat/abilities/{sessionId}` — live события активации (эффекты, цели, модификаторы, состояние киберпсихоза, синергии). Payload: `abilityId`, `source`, `target`, `castTime`, `duration`, `cooldown`, `cyberpsychosisDelta`.
- `wss://api.necp.game/v1/combat/abilities/player/{characterId}` — обновления лоадаута, доступных слотов, отклонённых кастов (зависит от progression/slots).

### Event Bus
- `combat.ability.activated` — `characterId`, `abilityId`, `rank`, `sessionId`, `target`, `context`, `cyberpsychosisImpact`.
- `combat.ability.cooldown-started` / `cooldown-finished` — управление UI и таймерами, консьюмеры: frontend, analytics.
- `combat.ability.synergy-triggered` — связь с `combat-combos-synergies` и progression (даёт бонусы/эффекты).
- `combat.ability.cyberpsychosis-updated` — информирует `social-service`, `quest-engine`, `analytics` о изменении состояния.
- `combat.ability.loadout-updated` — синхронизация с progression/quests (зависимости).  
Входящие: `combat.freerun.mobile-ability`, `combat.combos.apply`, `progression.attribute-updated`, `quest.trigger`.

### Storage
- `abilities_catalog` — `abilityId`, `sourceType`, `rank`, `cooldownBase`, `cost`, `cyberpsychosisImpact`, `effectJson`.
- `ability_loadouts` — привязка способностей к персонажу (`slots`, `priority`, `autoCast`, `updatedAt`).
- `ability_cooldowns` — текущее состояние кулдаунов (`expiresAt`, `modifier`, `source`, `appliedBy`).
- `ability_synergies` — таблица связей (ability ↔ ability / implant / combo), эффекты и требования.
- `ability_effects_history` — журнал активаций (для аналитики, rollback, античита).
- `cyberpsychosis_state` — текущий уровень, пороги, эффекты (условия блокировки/дебаффов).

### Зависимости
- `combat-implants-types`, `combat-abilities.md`, `combat-combos-synergies.md`, `combat-stealth`, `combat-shooting` — для контекстных модификаторов.
- `progression-backend` — проверка рангов, разблокировок, кача навыков.
- `quest-engine` — триггеры и награды, зависящие от использования способностей.
- `analytics-service` — события использования, синергий, уровней киберпсихоза.
- `economy-service` — апгрейды/покупки способностей (если задействовано).

### Требования / ограничения
- Нужна проверка анти-спам (ограничения частоты активации, перма-кулдауны).
- Учёт киберпсихоза: превышение порогов должно ветвить поведение (дебаффы, блокировка определённых способностей).
- Локализация — способности и описания эффектов имеют ключи, текст держится в отдельной системе.
- Поддержка GM/скриптовых операций (ручной сброс кулдауна, изменение киберпсихоза) с логированием `ability_admin_audit`.

### Источники
- `.BRAIN/02-gameplay/combat/combat-abilities.md` (v1.2.0, ready).
- `.BRAIN/02-gameplay/combat/combat-combos-synergies.md` (ready) — синергии.
- `.BRAIN/02-gameplay/combat/combat-implants-types.md` — влияние имплантов.
- `.BRAIN/05-technical/backend/progression-backend.md` — прогрессия и навыки.


