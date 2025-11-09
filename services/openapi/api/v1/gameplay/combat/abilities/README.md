# Combat Abilities API

**Разбито на несколько файлов** (превышение лимита 400 строк)

## Структура файлов:

- **abilities.yaml** - главный файл (paths, endpoints) ~260 строк
- **abilities-models.yaml** - модели данных (components/schemas) ~230 строк

## Endpoints:

1. `GET /gameplay/combat/abilities` - Все доступные способности
2. `GET /gameplay/combat/abilities/{ability_id}` - Детали способности
3. `POST /gameplay/combat/abilities/use` - Использовать способность
4. `GET /gameplay/combat/abilities/loadout` - Текущий loadout
5. `PUT /gameplay/combat/abilities/loadout` - Обновить loadout
6. `GET /gameplay/combat/abilities/synergies` - Синергии
7. `GET /gameplay/combat/abilities/cooldowns` - Кулдауны

## Модели:

- Ability, AbilitySource, AbilityCost
- AbilityLoadout
- AbilityUseRequest, AbilityUseResult
- AbilitySynergy, AbilityCooldown

## Источник:

`.BRAIN/02-gameplay/combat/combat-abilities.md` v1.2.0


