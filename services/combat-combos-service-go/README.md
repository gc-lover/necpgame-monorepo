# Combat Combos Service

Микросервис для управления комбо и синергиями в NECPGAME.

## Возможности

- **Combo Catalog** - каталог комбо (solo, team, legendary)
- **Activation** - активация комбо и валидация последовательностей
- **Synergies** - система синергий (ability, team, equipment, implant, timing)
- **Scoring** - оценка выполнения комбо (Bronze to Legendary)
- **Loadouts** - лоадауты комбо
- **Analytics** - аналитика эффективности

## API Endpoints

### Catalog
- `GET /api/v1/gameplay/combat/combos/catalog` - список комбо
- `GET /api/v1/gameplay/combat/combos/{comboId}` - детали комбо

### Activation
- `POST /api/v1/gameplay/combat/combos/activate` - активировать комбо
- `POST /api/v1/gameplay/combat/combos/synergy` - применить синергию

### Scoring
- `POST /api/v1/gameplay/combat/combos/score` - результаты scoring

### Loadout
- `POST /api/v1/gameplay/combat/combos/loadout` - создать/обновить лоадаут

### Analytics
- `GET /api/v1/gameplay/combat/combos/analytics` - аналитика эффективности

## Разработка

```bash
make generate-api  # Генерация кода
go build .         # Сборка
docker build -t combat-combos-service:latest .
```

## Метрики

- `combat_combos_http_requests_total`
- `combat_combos_activations_total`
- `combat_combos_synergies_total`
- `combat_combos_score_distribution`
