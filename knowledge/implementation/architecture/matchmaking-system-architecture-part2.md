<!-- Issue: #150 -->

# Архитектура системы матчмейкинга - Part 2: Rating System & Future

**См. также:** [Part 1: Core Architecture](./matchmaking-system-architecture-part1.md)

---

## API Endpoints (продолжение)

### Rating Management

#### GET /api/v1/matchmaking/rating/{player_id}
**Получение рейтинга**

Response:
```json
{
  "player_id": "uuid",
  "ratings": [{
    "activity_type": "pvp_5v5",
    "current_rating": 1523,
    "tier": "gold",
    "league": 3,
    "wins": 45,
    "losses": 38
  }]
}
```

#### GET /api/v1/matchmaking/leaderboard/{activity_type}
**Таблица лидеров**

Parameters: `limit`, `season_id`

Response:
```json
{
  "leaderboard": [{
    "rank": 1,
    "player_name": "ProGamer",
    "rating": 2845,
    "tier": "grandmaster"
  }]
}
```

---

## Рейтинговая система

### MMR/ELO Расчёт

**Классическая формула:**
```
New Rating = Old + K * (Actual - Expected)
Expected = 1 / (1 + 10^((Opp - Player) / 400))
```

**K-факторы:**
- <20 игр: K=40
- 20-100 игр: K=30
- 100+ игр: K=20

### Рейтинговые тиры

| Tier | MMR Range |
|------|-----------|
| Bronze | 0-999 |
| Silver | 1000-1299 |
| Gold | 1300-1599 |
| Platinum | 1600-1899 |
| Diamond | 1900-2199 |
| Master | 2200-2499 |
| Grandmaster | 2500+ |

### Анти-Smurf

**Критерии подозрения:**
- WR > 75% (первые 20)
- Рост > 200 MMR за 10 игр
- KDA > 5.0 в Bronze/Silver

**Действия:**
- K=60 (ускоренный рост)
- Пометка аккаунта

---

## Будущие улучшения

### Phase 2 (3-6 месяцев)

1. **ML алгоритмы**
   - Предсказание качества матча
   - Персонализированный матчмейкинг
   - Обучение на данных

2. **Кросс-регион**
   - Подбор из разных регионов
   - Учёт пинга

3. **Социальный матчмейкинг**
   - Приоритет для друзей
   - Избежание токсиков

### Phase 3 (6-12 месяцев)

1. **Adaptive Matchmaking**
   - Динамические алгоритмы
   - A/B тестирование

2. **Seasonal Events**
   - Специальные режимы
   - Турниры

3. **Advanced Analytics**
   - Анализ качества
   - Предсказание винрейта

---

## Глоссарий

- **MMR** - Matchmaking Rating
- **ELO** - Система рейтинга
- **Snake Draft** - Алгоритм балансировки
- **Smurf** - Опытный игрок на новом аккаунте
- **MQS** - Match Quality Score

---

**Конец Part 2**
