# Combat Shooting API

**Разбито на несколько файлов** (превышение лимита 400 строк)

## Структура файлов:

- **shooting.yaml** - главный файл (paths, endpoints) ~260 строк
- **shooting-models.yaml** - модели данных (components/schemas) ~270 строк

## Использование:

Главный файл: `shooting.yaml` содержит все endpoints и ссылается на модели через `$ref`.

Модели: `shooting-models.yaml` содержит все schemas для переиспользования.

## Источник:

`.BRAIN/02-gameplay/combat/combat-shooting.md` v1.1.0

## Endpoints:

1. `POST /gameplay/combat/shoot` - Выполнить выстрел
2. `POST /gameplay/combat/calculate-damage` - Рассчитать урон
3. `GET /gameplay/combat/weapons/{weapon_id}` - Получить оружие
4. `GET /gameplay/combat/damage-modifiers` - Модификаторы урона
5. `POST /gameplay/combat/reload` - Перезарядить оружие
6. `POST /gameplay/combat/cover-penetration` - Проверить проникновение

## Модели:

- ShootRequest, ShootResult
- DamageCalculationRequest, DamageCalculationResult
- Weapon
- BodyPartModifiers
- CoverPenetrationRequest, CoverPenetrationResult


