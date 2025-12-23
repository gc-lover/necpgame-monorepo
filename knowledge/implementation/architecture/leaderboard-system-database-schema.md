<!-- Issue: #140887681 -->

# Leaderboard System - Database Schema

## Обзор

Схема базы данных для системы лидербордов, включающая определения лидербордов, записи игроков, снапшоты для сезонов,
сезоны и награды.

## ERD Диаграмма

```mermaid
erDiagram
    leaderboards ||--o{ leaderboard_entries : "contains"
    leaderboards ||--o{ leaderboard_snapshots : "snapshotted"
    leaderboards ||--o{ leaderboard_rewards : "rewards"
    leaderboard_seasons ||--o{ leaderboards : "has"
    leaderboard_seasons ||--o{ leaderboard_entries : "tracks"
    leaderboard_seasons ||--o{ leaderboard_snapshots : "snapshotted"
    leaderboard_seasons ||--o{ leaderboard_rewards : "rewards"
    character ||--o{ leaderboard_entries : "ranked_in"

    leaderboards {
        uuid id PK
        varchar code UNIQUE
        varchar name
        varchar type
        varchar scope
        varchar metric_type
        varchar update_frequency
        boolean is_active
        uuid season_id FK
        timestamp created_at
        timestamp updated_at
    }

    leaderboard_entries {
        uuid id PK
        uuid leaderboard_id FK
        uuid player_id FK
        decimal score
        integer rank
        integer previous_rank
        varchar tier
        jsonb metadata
        uuid season_id FK
        timestamp updated_at
    }

    leaderboard_snapshots {
        uuid id PK
        uuid leaderboard_id FK
        uuid season_id FK
        jsonb snapshot_data
        timestamp created_at
    }

    leaderboard_seasons {
        uuid id PK
        varchar name
        timestamp start_date
        timestamp end_date
        varchar status
        boolean rewards_distributed
        timestamp created_at
    }

    leaderboard_rewards {
        uuid id PK
        uuid leaderboard_id FK
        uuid season_id FK
        varchar tier
        integer rank_min
        integer rank_max
        jsonb rewards
        timestamp created_at
    }
```

## Описание таблиц

### leaderboards

Таблица определений лидербордов. Хранит информацию о различных типах лидербордов.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `code`: Уникальный код лидерборда (VARCHAR(100), UNIQUE, NOT NULL)
- `name`: Название лидерборда (VARCHAR(255), NOT NULL)
- `type`: Тип лидерборда (VARCHAR(20), NOT NULL: 'global', 'class', 'seasonal', 'friend', 'guild')
- `scope`: Область видимости (VARCHAR(100), NOT NULL: 'server', 'class', 'season', 'friends', 'guild')
- `metric_type`: Тип метрики (VARCHAR(100), NOT NULL: 'overall_power', 'combat_score', 'economic_score', 'social_score')
- `update_frequency`: Частота обновления (VARCHAR(20), NOT NULL: 'realtime', 'hourly', 'daily', 'weekly')
- `is_active`: Активен ли лидерборд (BOOLEAN, NOT NULL, default: true)
- `season_id`: ID сезона для сезонных лидербордов (UUID, nullable, FK к leaderboard_seasons)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(code, type)` для поиска по коду и типу
- По `(type, is_active)` для активных лидербордов
- По `season_id` для сезонных лидербордов

### leaderboard_entries

Таблица записей в лидербордах. Хранит позиции игроков в лидербордах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `leaderboard_id`: ID лидерборда (FK к leaderboards, NOT NULL)
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `score`: Очки игрока (DECIMAL(20, 2), NOT NULL, default: 0)
- `rank`: Текущая позиция (INTEGER, NOT NULL, default: 0)
- `previous_rank`: Предыдущая позиция (INTEGER, nullable)
- `tier`: Тир игрока (VARCHAR(20), nullable: 'diamond', 'platinum', 'gold', 'silver', 'bronze')
- `metadata`: Дополнительные данные (JSONB, default: {})
- `season_id`: ID сезона для сезонных записей (UUID, nullable, FK к leaderboard_seasons)
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(leaderboard_id, rank)` для сортировки по позициям
- По `(player_id, leaderboard_id)` для поиска позиции игрока
- По `(leaderboard_id, score DESC)` для сортировки по очкам
- По `(season_id, rank)` для сезонных записей

**Constraints:**

- UNIQUE(leaderboard_id, player_id, season_id): Одна запись на игрока в лидерборде/сезоне

### leaderboard_snapshots

Таблица снапшотов лидербордов. Хранит полные данные рейтингов на момент создания снапшота (для сезонов).

**Ключевые поля:**

- `id`: UUID первичный ключ
- `leaderboard_id`: ID лидерборда (FK к leaderboards, NOT NULL)
- `season_id`: ID сезона (FK к leaderboard_seasons, NOT NULL)
- `snapshot_data`: Полные данные рейтинга (JSONB, NOT NULL)
- `created_at`: Время создания снапшота

**Индексы:**

- По `(leaderboard_id, season_id)` для поиска снапшотов
- По `season_id` для всех снапшотов сезона

### leaderboard_seasons

Таблица сезонов лидербордов. Хранит информацию о сезонах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `name`: Название сезона (VARCHAR(255), NOT NULL)
- `start_date`: Дата начала (TIMESTAMP, NOT NULL)
- `end_date`: Дата окончания (TIMESTAMP, NOT NULL)
- `status`: Статус сезона (VARCHAR(20), NOT NULL, default: 'active': 'active', 'ended', 'archived')
- `rewards_distributed`: Распределены ли награды (BOOLEAN, NOT NULL, default: false)
- `created_at`: Время создания

**Индексы:**

- По `(status, end_date)` для активных сезонов
- По `(start_date, end_date)` для поиска по датам

**Constraints:**

- CHECK (end_date > start_date): Дата окончания должна быть позже даты начала

### leaderboard_rewards

Таблица наград за позиции в лидербордах. Хранит информацию о наградах по тирам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `leaderboard_id`: ID лидерборда (FK к leaderboards, NOT NULL)
- `season_id`: ID сезона (FK к leaderboard_seasons, nullable, ON DELETE CASCADE)
- `tier`: Тир награды (VARCHAR(20), NOT NULL: 'diamond', 'platinum', 'gold', 'silver', 'bronze')
- `rank_min`: Минимальная позиция для тира (INTEGER, NOT NULL, CHECK > 0)
- `rank_max`: Максимальная позиция для тира (INTEGER, NOT NULL, CHECK >= rank_min)
- `rewards`: Награды (JSONB, NOT NULL, default: {})
- `created_at`: Время создания

**Индексы:**

- По `(leaderboard_id, tier)` для наград лидерборда
- По `(season_id, tier)` для сезонных наград

**Constraints:**

- CHECK (rank_min > 0): Минимальная позиция должна быть больше 0
- CHECK (rank_max >= rank_min): Максимальная позиция должна быть >= минимальной

## ENUM типы

### leaderboard_type

- `global`: Глобальные рейтинги
- `class`: Рейтинги по классам
- `seasonal`: Сезонные рейтинги
- `friend`: Дружественные рейтинги
- `guild`: Гильдийные рейтинги

### update_frequency

- `realtime`: Обновление в реальном времени
- `hourly`: Ежечасное обновление
- `daily`: Ежедневное обновление
- `weekly`: Еженедельное обновление

### leaderboard_tier

- `diamond`: Алмазный тир
- `platinum`: Платиновый тир
- `gold`: Золотой тир
- `silver`: Серебряный тир
- `bronze`: Бронзовый тир

### season_status

- `active`: Активный сезон
- `ended`: Завершенный сезон
- `archived`: Архивированный сезон

## Constraints и валидация

### CHECK Constraints

- `leaderboards.type`: Должно быть одним из: 'global', 'class', 'seasonal', 'friend', 'guild'
- `leaderboards.update_frequency`: Должно быть одним из: 'realtime', 'hourly', 'daily', 'weekly'
- `leaderboard_entries.tier`: Должно быть одним из: 'diamond', 'platinum', 'gold', 'silver', 'bronze' (nullable)
- `leaderboard_seasons.status`: Должно быть одним из: 'active', 'ended', 'archived'
- `leaderboard_seasons.end_date > start_date`: Дата окончания должна быть позже даты начала
- `leaderboard_rewards.tier`: Должно быть одним из: 'diamond', 'platinum', 'gold', 'silver', 'bronze'
- `leaderboard_rewards.rank_min > 0`: Минимальная позиция должна быть больше 0
- `leaderboard_rewards.rank_max >= rank_min`: Максимальная позиция должна быть >= минимальной

### Foreign Keys

- `leaderboards.season_id` → `world.leaderboard_seasons.id` (ON DELETE SET NULL)
- `leaderboard_entries.leaderboard_id` → `world.leaderboards.id` (ON DELETE CASCADE)
- `leaderboard_entries.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `leaderboard_entries.season_id` → `world.leaderboard_seasons.id` (ON DELETE SET NULL)
- `leaderboard_snapshots.leaderboard_id` → `world.leaderboards.id` (ON DELETE CASCADE)
- `leaderboard_snapshots.season_id` → `world.leaderboard_seasons.id` (ON DELETE CASCADE)
- `leaderboard_rewards.leaderboard_id` → `world.leaderboards.id` (ON DELETE CASCADE)
- `leaderboard_rewards.season_id` → `world.leaderboard_seasons.id` (ON DELETE CASCADE)

### Unique Constraints

- `leaderboards.code`: Уникальный код лидерборда
- `leaderboard_entries(leaderboard_id, player_id, season_id)`: Одна запись на игрока в лидерборде/сезоне

## Оптимизация запросов

### Частые запросы

1. **Получение топ-100 игроков:**
   ```sql
   SELECT * FROM world.leaderboard_entries 
   WHERE leaderboard_id = $1 
   ORDER BY score DESC, rank ASC 
   LIMIT 100;
   ```
   Использует индекс `(leaderboard_id, score DESC)`.

2. **Получение позиции игрока:**
   ```sql
   SELECT * FROM world.leaderboard_entries 
   WHERE leaderboard_id = $1 AND player_id = $2;
   ```
   Использует индекс `(player_id, leaderboard_id)`.

3. **Получение активных лидербордов:**
   ```sql
   SELECT * FROM world.leaderboards 
   WHERE type = $1 AND is_active = true;
   ```
   Использует индекс `(type, is_active)`.

4. **Получение сезонных записей:**
   ```sql
   SELECT * FROM world.leaderboard_entries 
   WHERE season_id = $1 
   ORDER BY rank ASC 
   LIMIT 100;
   ```
   Использует индекс `(season_id, rank)`.

5. **Получение активных сезонов:**
   ```sql
   SELECT * FROM world.leaderboard_seasons 
   WHERE status = 'active' AND end_date > CURRENT_TIMESTAMP;
   ```
   Использует индекс `(status, end_date)`.

## Миграции

### Существующие миграции:

- `V1_65__leaderboard_system_tables.sql` - все таблицы системы лидербордов

### Применение миграций:

```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из
`knowledge/implementation/architecture/leaderboard-system-architecture.yaml`:

- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE/SET NULL для автоматической очистки
- [OK] Интеграция с существующими таблицами (characters)

## Особенности реализации

### Лидерборды

Система лидербордов включает:

- **Типы**: global, class, seasonal, friend, guild
- **Области видимости**: server, class, season, friends, guild
- **Метрики**: overall_power, combat_score, economic_score, social_score
- **Частота обновления**: realtime, hourly, daily, weekly
- **Сезонность**: поддержка сезонных лидербордов через season_id

### Записи

Система записей включает:

- **Очки**: score (DECIMAL(20, 2)) для точных значений
- **Позиции**: rank и previous_rank для отслеживания изменений
- **Тиры**: diamond, platinum, gold, silver, bronze
- **Метаданные**: metadata (JSONB) для дополнительных данных
- **Уникальность**: одна запись на игрока в лидерборде/сезоне

### Снапшоты

Система снапшотов включает:

- **Полные данные**: snapshot_data (JSONB) для архивации
- **Сезонность**: привязка к сезонам через season_id
- **История**: created_at для отслеживания времени создания

### Сезоны

Система сезонов включает:

- **Статусы**: active, ended, archived
- **Даты**: start_date и end_date для управления жизненным циклом
- **Награды**: rewards_distributed для отслеживания распределения

### Награды

Система наград включает:

- **Тиры**: diamond, platinum, gold, silver, bronze
- **Диапазоны**: rank_min и rank_max для определения тиров
- **Награды**: rewards (JSONB) для гибкой структуры наград

### Интеграция с Redis

Система лидербордов интегрируется с Redis для hot data:

- **Sorted Sets**: для хранения топ-1000 игроков
- **Ключи**: `leaderboard:{code}:{scope}` для основных рейтингов
- **Синхронизация**: периодическая синхронизация Redis ↔ PostgreSQL

### Интеграция с другими системами

Система лидербордов интегрируется с:

- **Characters**: через player_id для данных игроков
- **Achievement Service**: для расчёта overall_power
- **Economy Service**: для расчёта economic_score
- **Gameplay Service**: для расчёта combat_score
- **Social Service**: для friend и guild лидербордов

