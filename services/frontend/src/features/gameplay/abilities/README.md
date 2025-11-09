# Abilities Feature (Система способностей)

## Описание

Feature для системы способностей в стиле VALORANT (Q/E/R структура). Способности получаются из экипировки, имплантов, навыков и кибердеки.

## OpenAPI Спецификация

`API-SWAGGER/api/v1/gameplay/combat/abilities.yaml`

### Эндпоинты

- `GET /gameplay/combat/abilities` - Все доступные способности персонажа
- `GET /gameplay/combat/abilities/{ability_id}` - Детали способности
- `POST /gameplay/combat/abilities/use` - Использовать способность
- `GET /gameplay/combat/abilities/loadout` - Текущая конфигурация Q/E/R
- `PUT /gameplay/combat/abilities/loadout` - Обновить loadout

## Структура

```
src/features/gameplay/abilities/
├── components/
│   ├── AbilityCard.tsx
│   └── __tests__/
├── pages/
│   └── AbilitiesPage.tsx
└── README.md
```

## Особенности

- Q/E/R система (VALORANT стиль)
- Источники: экипировка, импланты, навыки, кибердека
- Кулдауны и ресурсы (энергия, здоровье)
- Синергии между способностями
- Влияние на киберпсихоз

## Роутинг

`/game/abilities` - защищен через `ProtectedRoute`

## Автор

AI Agent, OpenAPI First подход

