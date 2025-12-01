<!-- Issue: #140875800 -->
# League System and Meta Mechanics - Database Schema

## Обзор

Схема базы данных для системы лиг и мета-механик, обеспечивающей сезонные циклы с глобальным сбросом, мета-прогресс между сезонами, Hall of Fame и Legacy Shop.

## ERD Диаграмма

```mermaid
erDiagram
    leagues ||--o{ league_statistics : "has"
    leagues ||--o{ hall_of_fame_entries : "contains"
    leagues ||--o{ player_legacy_items : "used_in"
    leagues ||--o{ legacy_purchase_history : "purchased_in"
    leagues ||--o{ end_event_registrations : "registers"
    player_legacy ||--o{ player_legacy_items : "owns"
    legacy_shop_items ||--o{ player_legacy_items : "purchased"
    legacy_shop_items ||--o{ legacy_purchase_history : "purchased"

    leagues {
        uuid id PK
        varchar name
        bigint seed
        timestamp start_date
        timestamp end_date
        league_phase current_phase
        decimal time_acceleration
        timestamp game_date
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    player_legacy {
        uuid id PK
        uuid account_id UNIQUE
        integer legacy_points
        decimal global_rating
        text[] titles
        uuid[] cosmetics
        timestamp created_at
        timestamp updated_at
    }

    league_statistics {
        uuid id PK
        uuid league_id FK
        league_phase phase
        integer player_count
        jsonb economy_metrics
        jsonb pvp_metrics
        jsonb quest_metrics
        jsonb top_players
        timestamp created_at
        timestamp updated_at
    }

    hall_of_fame_entries {
        uuid id PK
        uuid league_id FK
        uuid account_id
        hall_of_fame_category category
        integer rank
        varchar achievement
        uuid statue_model
        timestamp created_at
    }

    legacy_shop_items {
        uuid id PK
        varchar item_name
        text item_description
        varchar item_type
        integer legacy_points_cost
        boolean is_available
        integer max_purchases_per_league
        jsonb item_data
        timestamp created_at
        timestamp updated_at
    }

    player_legacy_items {
        uuid id PK
        uuid account_id
        uuid league_id FK
        uuid shop_item_id FK
        timestamp purchased_at
        boolean is_used
        timestamp used_at
        jsonb item_data
    }
```

## Описание таблиц

### leagues

Таблица лиг (сезонов). Хранит информацию о текущей и прошлых лигах с фазами и временным ускорением.

**Ключевые поля:**
- `name`: Название лиги
- `seed`: Seed для генерации вариаций мира (BIGINT)
- `start_date`, `end_date`: Даты начала и окончания лиги
- `current_phase`: Текущая фаза лиги (Start, Rise, Crisis, Endgame, Finale)
- `time_acceleration`: Ускорение времени (15-30 игровых дней за реальный день)
- `game_date`: Текущая игровая дата
- `is_active`: Флаг активности лиги

**Индексы:**
- По `(is_active, start_date DESC)` для активной лиги
- По `(current_phase, is_active)` для фильтрации по фазе
- По `(start_date, end_date)` для временных запросов

### player_legacy

Таблица мета-прогресса игроков. Хранит информацию о титулах, косметике, Legacy Points и глобальном рейтинге.

**Ключевые поля:**
- `account_id`: ID аккаунта (UNIQUE)
- `legacy_points`: Legacy Points для покупки Legacy Items
- `global_rating`: Глобальный рейтинг с мягким сбросом (20%)
- `titles`: Титулы игрока (TEXT[])
- `cosmetics`: Косметика игрока (UUID[])

**Индексы:**
- По `account_id` для поиска по аккаунту
- По `global_rating DESC` для рейтинга
- По `legacy_points DESC` для Legacy Points

### league_statistics

Таблица статистики лиг. Хранит статистику по фазам лиги (экономика, PvP, квесты, топ игроки).

**Ключевые поля:**
- `league_id`: ID лиги (FK к leagues)
- `phase`: Фаза лиги для статистики
- `player_count`: Количество игроков
- `economy_metrics`: Экономические метрики (JSONB)
- `pvp_metrics`: PvP метрики (JSONB)
- `quest_metrics`: Квестовые метрики (JSONB)
- `top_players`: Топ игроки (JSONB)

**Индексы:**
- По `(league_id, phase)` для статистики лиги по фазе
- По `(phase, updated_at DESC)` для фильтрации по фазе

### hall_of_fame_entries

Таблица Hall of Fame. Хранит информацию о лучших игроках каждой лиги по категориям.

**Ключевые поля:**
- `league_id`: ID лиги (FK к leagues)
- `account_id`: ID аккаунта
- `category`: Категория (Story, Economy, PvP, Alternative)
- `rank`: Ранг в категории
- `achievement`: Достижение
- `statue_model`: UUID 3D-модели статуи победителя

**Индексы:**
- По `(league_id, category, rank)` для Hall of Fame лиги
- По `account_id` для достижений игрока
- По `(category, rank)` для категорий

### legacy_shop_items

Таблица предметов Legacy Shop. Хранит информацию о предметах, доступных для покупки за Legacy Points.

**Ключевые поля:**
- `item_name`: Название предмета
- `item_description`: Описание предмета
- `item_type`: Тип предмета
- `legacy_points_cost`: Стоимость в Legacy Points
- `is_available`: Доступность предмета
- `max_purchases_per_league`: Максимальное количество покупок за лигу (NULL = без ограничений)
- `item_data`: Данные предмета (JSONB)

**Индексы:**
- По `(is_available, legacy_points_cost)` для доступных предметов
- По `(item_type, is_available)` для фильтрации по типу

### player_legacy_items

Таблица Legacy Items игроков. Хранит информацию о купленных Legacy Items для использования в новой лиге.

**Ключевые поля:**
- `account_id`: ID аккаунта
- `league_id`: ID лиги (FK к leagues)
- `shop_item_id`: ID предмета из Legacy Shop (FK к legacy_shop_items)
- `purchased_at`: Время покупки
- `is_used`: Флаг использования Legacy Item в лиге
- `used_at`: Время использования
- `item_data`: Данные предмета (JSONB)

**Индексы:**
- По `(account_id, league_id)` для предметов игрока в лиге
- По `(league_id, is_used)` для использованных предметов
- По `shop_item_id` для предметов из магазина

### legacy_purchase_history

Таблица истории покупок Legacy Items. Хранит информацию о всех покупках Legacy Items.

**Ключевые поля:**
- `account_id`: ID аккаунта
- `shop_item_id`: ID предмета из Legacy Shop (FK к legacy_shop_items)
- `league_id`: ID лиги (FK к leagues)
- `legacy_points_spent`: Потраченные Legacy Points
- `purchased_at`: Время покупки

**Индексы:**
- По `(account_id, purchased_at DESC)` для истории игрока
- По `(league_id, purchased_at DESC)` для покупок в лиге
- По `shop_item_id` для покупок предмета

### end_event_registrations

Таблица регистраций на финальные события. Хранит информацию о регистрациях игроков на финальные события лиги.

**Ключевые поля:**
- `league_id`: ID лиги (FK к leagues)
- `account_id`: ID аккаунта
- `character_id`: ID персонажа (FK к characters, nullable)
- `registration_data`: Данные регистрации (JSONB)
- `registered_at`: Время регистрации

**Индексы:**
- По `league_id` для регистраций в лиге
- По `account_id` для регистраций игрока

## Constraints и валидация

### CHECK Constraints

- `leagues.time_acceleration`: Должно быть > 0
- `player_legacy.legacy_points`: Должно быть >= 0
- `player_legacy.global_rating`: Должно быть >= 0
- `league_statistics.player_count`: Должно быть >= 0
- `hall_of_fame_entries.rank`: Должно быть > 0
- `legacy_shop_items.legacy_points_cost`: Должно быть > 0
- `legacy_shop_items.max_purchases_per_league`: Должно быть > 0 (если указано)
- `legacy_purchase_history.legacy_points_spent`: Должно быть > 0

### ENUM Types

- `league_phase`: Start, Rise, Crisis, Endgame, Finale
- `hall_of_fame_category`: Story, Economy, PvP, Alternative

### Foreign Keys

- `league_statistics.league_id` → `leagues.id` (ON DELETE CASCADE)
- `hall_of_fame_entries.league_id` → `leagues.id` (ON DELETE CASCADE)
- `player_legacy_items.league_id` → `leagues.id` (ON DELETE CASCADE)
- `player_legacy_items.shop_item_id` → `legacy_shop_items.id` (ON DELETE CASCADE)
- `legacy_purchase_history.league_id` → `leagues.id` (ON DELETE CASCADE)
- `legacy_purchase_history.shop_item_id` → `legacy_shop_items.id` (ON DELETE CASCADE)
- `end_event_registrations.league_id` → `leagues.id` (ON DELETE CASCADE)
- `end_event_registrations.character_id` → `mvp_core.character.id` (ON DELETE SET NULL)

### Unique Constraints

- `player_legacy(account_id)`: Один аккаунт - один мета-прогресс
- `league_statistics(league_id, phase)`: Одна статистика на лигу и фазу
- `player_legacy_items(account_id, league_id, shop_item_id)`: Один предмет на аккаунт, лигу и магазин
- `end_event_registrations(league_id, account_id)`: Одна регистрация на лигу и аккаунт

## Оптимизация запросов

### Частые запросы

1. **Получение текущей активной лиги:**
   ```sql
   SELECT * FROM league.leagues 
   WHERE is_active = true 
   ORDER BY start_date DESC 
   LIMIT 1;
   ```
   Использует индекс `(is_active, start_date DESC)`.

2. **Получение мета-прогресса игрока:**
   ```sql
   SELECT * FROM league.player_legacy 
   WHERE account_id = $1;
   ```
   Использует индекс `account_id`.

3. **Получение Hall of Fame лиги:**
   ```sql
   SELECT * FROM league.hall_of_fame_entries 
   WHERE league_id = $1 
   ORDER BY category, rank ASC;
   ```
   Использует индекс `(league_id, category, rank)`.

4. **Получение доступных Legacy Items:**
   ```sql
   SELECT * FROM league.legacy_shop_items 
   WHERE is_available = true 
   ORDER BY legacy_points_cost ASC;
   ```
   Использует индекс `(is_available, legacy_points_cost)`.

## Миграции

### Существующие миграции:
- `V1_55__league_system_meta_mechanics_tables.sql` - создание всех таблиц системы лиг и мета-механик

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/league-system-architecture.yaml`:
- OK Все таблицы из архитектуры созданы
- OK Все поля соответствуют описанию
- OK ENUM типы созданы для фаз и категорий
- OK Индексы оптимизированы для частых запросов
- OK Constraints обеспечивают целостность данных
- OK Foreign Keys настроены с CASCADE для автоматической очистки
- OK Поддержка JSONB для гибкого хранения данных
- OK Поддержка массивов (TEXT[], UUID[]) для титулов и косметики

## Особенности реализации

### JSONB поля

Использование JSONB для гибкого хранения:
- `economy_metrics`: Экономические метрики
- `pvp_metrics`: PvP метрики
- `quest_metrics`: Квестовые метрики
- `top_players`: Топ игроки
- `item_data`: Данные предметов
- `registration_data`: Данные регистрации

### Массивы

Использование массивов PostgreSQL:
- `titles`: TEXT[] - титулы игрока
- `cosmetics`: UUID[] - косметика игрока

### Фазы лиги

Система поддерживает следующие фазы:
- **Start**: Месяц 1 - создание персонажей, выбор фракций
- **Rise**: Месяцы 2-3 - корпоративные войны
- **Crisis**: Месяцы 4-5 - поздние конфликты
- **Endgame**: Последние 2 недели - кульминация
- **Finale**: 27 июля 2093 - глобальный сброс

### Категории Hall of Fame

Hall of Fame включает следующие категории:
- **Story**: Сюжетные достижения
- **Economy**: Экономические достижения
- **PvP**: PvP достижения
- **Alternative**: Альтернативные режимы

### Мета-прогресс

Мета-прогресс включает:
- **Legacy Points**: Очки для покупки Legacy Items
- **Global Rating**: Глобальный рейтинг с мягким сбросом (20%)
- **Titles**: Титулы игрока
- **Cosmetics**: Косметика игрока
- **Legacy Items**: Мощные предметы для старта новой лиги

### Временное ускорение

Система поддерживает ускорение времени:
- 15-30 игровых дней за реальный день
- Учет сценарных событий (замедление для драматургии)
- Синхронизация с World Service


