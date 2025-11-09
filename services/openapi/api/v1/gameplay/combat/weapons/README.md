# Combat Weapons API

**Разбито на несколько файлов** (превышение лимита 400 строк)

## Структура файлов:

- **weapons.yaml** - главный файл (paths, endpoints) ~260 строк
- **weapons-models.yaml** - модели данных (components/schemas) ~190 строк

## Endpoints:

1. `GET /gameplay/combat/weapons` - Каталог оружия
2. `GET /gameplay/combat/weapons/{weapon_id}` - Детали оружия
3. `GET /gameplay/combat/weapons/brands/{brand}` - Оружие по бренду
4. `GET /gameplay/combat/weapons/classes/{class}` - Оружие по классу
5. `GET /gameplay/combat/weapons/mastery/{character_id}` - Mastery progress
6. `PUT /gameplay/combat/weapons/mastery` - Обновить mastery
7. `GET /gameplay/combat/weapons/mods` - Доступные моды
8. `GET /gameplay/combat/weapons/meta/{content_type}` - Meta weapons

## Модели:

- WeaponSummary, WeaponDetails
- WeaponStats
- WeaponMasteryProgress
- WeaponMod

## Источник:

`.BRAIN/02-gameplay/combat/combat-weapon-classes-detailed.md` v1.0.0


