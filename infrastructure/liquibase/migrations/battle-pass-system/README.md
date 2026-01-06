# Battle Pass System Database Schema

Полная схема базы данных для системы сезонной прогрессии Battle Pass в NECPGAME.

## 📋 Обзор

Battle Pass System обеспечивает сезонную прогрессию игроков через уровни с бесплатными и премиум наградами, челленджами и интеграцией монетизации. Схема поддерживает:

- Сезонную систему с жизненным циклом
- Множественные треки прогрессии (Free/Premium/Ultimate)
- Сложную систему наград и кастомизации
- Челленджи различных типов
- Премиум подписку и монетизацию
- Комплексную аналитику и телеметрию

## 🏗️ Архитектура базы данных

### Основные компоненты

1. **Сезоны** (`battle_pass_seasons`, `battle_pass_season_config`) - Управление жизненным циклом сезонов
2. **Треки** (`battle_pass_tracks`, `battle_pass_season_tracks`) - Свободный/премиум/ультимативный треки
3. **Прогрессия** (`battle_pass_levels`, `battle_pass_player_progress`, `battle_pass_xp_transactions`) - Уровни и XP
4. **Награды** (`battle_pass_rewards`, `battle_pass_level_rewards`, `battle_pass_claimed_rewards`) - Система наград
5. **Челленджи** (`battle_pass_challenges`, `battle_pass_player_challenges`) - Задачи и их выполнение
6. **Премиум** (`battle_pass_premium_tiers`, `battle_pass_player_subscriptions`) - Монетизация
7. **Аналитика** (`battle_pass_analytics_events`, `battle_pass_daily_stats`) - Метрики и отчеты
8. **Уведомления** (`battle_pass_notification_preferences`, `battle_pass_scheduled_notifications`) - Коммуникации

## 📊 Схема таблиц

### Управление сезонами

```sql
-- Сезоны Battle Pass
battle_pass_seasons
├── id (BIGSERIAL PRIMARY KEY)
├── season_key (VARCHAR UNIQUE) - уникальный ключ сезона
├── name, description
├── season_type ('REGULAR', 'EVENT', 'LIMITED', 'PERMANENT')
├── status ('DRAFT', 'PREPARATION', 'ACTIVE', 'ENDING', 'COMPLETED', 'ARCHIVED')
├── start_date, end_date (TIMESTAMP)
├── max_level, base_xp_per_level, xp_multiplier
└── is_active (BOOLEAN)

-- Конфигурация сезонов
battle_pass_season_config
├── id (BIGSERIAL PRIMARY KEY)
├── season_id (FOREIGN KEY)
├── config_key, config_value (JSONB)
└── description
```

### Треки прогрессии

```sql
-- Типы треков (Free/Premium/Ultimate)
battle_pass_tracks
├── id (BIGSERIAL PRIMARY KEY)
├── track_key (VARCHAR UNIQUE)
├── name, description
├── track_type ('FREE', 'PREMIUM', 'ULTIMATE')
├── price_cents, currency
└── is_enabled

-- Связь сезонов и треков
battle_pass_season_tracks
├── id (BIGSERIAL PRIMARY KEY)
├── season_id, track_id (FOREIGN KEY)
├── is_default (BOOLEAN)
├── unlock_requirements (JSONB)
```

### Прогрессия игроков

```sql
-- Регистрация игроков в сезоне
battle_pass_player_enrollment
├── id (BIGSERIAL PRIMARY KEY)
├── player_id (BIGINT) - ссылка на игрока
├── season_id, track_id (FOREIGN KEY)
├── enrolled_at, purchase_date, expiration_date
├── is_active (BOOLEAN)
└── UNIQUE(player_id, season_id)

-- Прогресс игрока
battle_pass_player_progress
├── id (BIGSERIAL PRIMARY KEY)
├── player_enrollment_id (FOREIGN KEY)
├── current_level, current_xp, total_xp_earned
├── xp_to_next_level, completed_levels
├── last_progress_update
└── UNIQUE(player_enrollment_id)

-- Транзакции XP
battle_pass_xp_transactions
├── id (BIGSERIAL PRIMARY KEY)
├── player_enrollment_id (FOREIGN KEY)
├── xp_amount, xp_source ('QUEST_COMPLETION', 'COMBAT_VICTORIES', etc.)
├── source_reference_id
├── transaction_data (JSONB)
└── granted_at
```

### Уровни и награды

```sql
-- Уровни сезонов
battle_pass_levels
├── id (BIGSERIAL PRIMARY KEY)
├── season_id, track_id (FOREIGN KEY)
├── level (INTEGER)
├── xp_required (BIGINT)
├── reward_data, bonus_reward_data (JSONB)
└── is_premium_locked (BOOLEAN)

-- Каталог наград
battle_pass_rewards
├── id (BIGSERIAL PRIMARY KEY)
├── reward_key (VARCHAR UNIQUE)
├── name, description
├── reward_type ('COSMETICS', 'CURRENCY', 'ITEMS', 'BOOSTERS', 'TITLES', 'EXCLUSIVE')
├── rarity ('common', 'uncommon', 'rare', 'epic', 'legendary')
├── value_data (JSONB)
├── is_stackable, max_stack
└── is_enabled

-- Награды за уровни
battle_pass_level_rewards
├── id (BIGSERIAL PRIMARY KEY)
├── level_id, reward_id (FOREIGN KEY)
├── quantity, is_guaranteed
├── drop_chance (DECIMAL)
```

### Челленджи

```sql
-- Шаблоны челленджей
battle_pass_challenges
├── id (BIGSERIAL PRIMARY KEY)
├── challenge_key (VARCHAR UNIQUE)
├── name, description
├── challenge_type ('DAILY', 'WEEKLY', 'SEASONAL', 'LIMITED_TIME', 'PERSONAL')
├── challenge_category ('COMBAT', 'SOCIAL', 'PROGRESSION', 'COLLECTION', 'EXPLORATION')
├── target_value, reward_xp
├── reward_data (JSONB)
├── start_date, end_date
├── max_completions, is_active

-- Прогресс игроков по челленджам
battle_pass_player_challenges
├── id (BIGSERIAL PRIMARY KEY)
├── player_enrollment_id, challenge_id (FOREIGN KEY)
├── current_progress, is_completed
├── completed_at, times_completed
├── last_progress_update
```

### Премиум система

```sql
-- Уровни премиум подписки
battle_pass_premium_tiers
├── id (BIGSERIAL PRIMARY KEY)
├── tier_key (VARCHAR UNIQUE)
├── name, description
├── price_cents, currency, duration_days
├── features (JSONB)
└── is_enabled

-- Подписки игроков
battle_pass_player_subscriptions
├── id (BIGSERIAL PRIMARY KEY)
├── player_id, premium_tier_id (FOREIGN KEY)
├── season_id (может быть NULL для всех сезонов)
├── purchase_date, expiration_date
├── payment_reference_id, is_active
├── auto_renew
```

### Аналитика и телеметрия

```sql
-- События аналитики
battle_pass_analytics_events
├── id (BIGSERIAL PRIMARY KEY)
├── event_type ('LEVEL_UP', 'REWARD_CLAIMED', 'CHALLENGE_COMPLETED', etc.)
├── player_id, season_id
├── event_data, session_id (JSONB, VARCHAR)
├── client_version, platform, region
└── event_timestamp

-- Ежедневная статистика
battle_pass_daily_stats
├── id (BIGSERIAL PRIMARY KEY)
├── date (DATE)
├── season_id (FOREIGN KEY)
├── total_players, active_players, premium_players
├── average_level, total_xp_earned
├── rewards_claimed, challenges_completed
├── revenue_cents
```

## 🔍 Оптимизации производительности

### Индексы

- **Составные индексы** для комплексных запросов (player + season + level)
- **Частичные индексы** для активных записей (is_active = true)
- **Временные индексы** для недавних данных (last 30 days)
- **JSONB индексы** для поиска в конфигурационных данных

### Материализованные представления

```sql
-- Сводка по игрокам
battle_pass_player_summary

-- Производительность сезонов
battle_pass_season_performance

-- Статистика челленджей
battle_pass_challenge_stats

-- Аналитика доходов
battle_pass_revenue_analytics
```

### Функции для оптимизации

```sql
-- Статус игрока
get_player_battle_pass_status(player_id, season_id)

-- Требования уровней
get_level_progression_requirements(season_id, track_id, start_level, end_level)

-- Доступные награды
get_available_rewards_for_level(player_id, season_id, level)
```

### Партиционирование

- **XP транзакции** партиционированы по месяцам
- **Аналитика событий** партиционирована по месяцам
- Автоматическое создание партиций для новых месяцев

## 🚀 Миграции

### V001 - Основные таблицы
Создание полной схемы со всеми таблицами, индексами и ограничениями.

### V002 - Начальные данные
- **3 трека**: Free, Premium, Ultimate
- **1 активный сезон**: Winter 2025 с полной конфигурацией
- **20 уровней** для каждого трека с наградами
- **9 челленджей**: daily/weekly/seasonal
- **4 премиум уровня**: Basic, Advanced, Ultimate, Lifetime
- **12 типов наград**: от косметики до эксклюзивных предметов

### V003 - Оптимизации производительности
- Дополнительные индексы для высоконагруженных запросов
- Материализованные представления для аналитики
- Партиционирование для больших таблиц
- Функции для сложных операций
- Мониторинг производительности запросов

## 📊 Ключевые возможности

### Прогрессия
- **Множественные треки** с разными наградами
- **Гибкая система XP** с множественными источниками
- **Сезонный жизненный цикл** с автоматическим управлением

### Монетизация
- **Гибкие премиум уровни** с разными сроками
- **Автопродление** подписок
- **Скидки и акции** с промо-кодами

### Челленджи
- **Различные типы**: ежедневные, еженедельные, сезонные
- **Персонализация** сложности и наград
- **Автоматический сброс** по расписанию

### Аналитика
- **Комплексные метрики** вовлеченности игроков
- **Доходная аналитика** с конверсиями
- **A/B тестирование** наград и челленджей

## 🔒 Безопасность

### Валидация данных
- **Database-level constraints** для всех полей
- **JSON Schema validation** для конфигурационных данных
- **Referential integrity** между всеми таблицами

### Аудит и мониторинг
- **Полное логирование** всех транзакций XP
- **Отслеживание изменений** прогресса игроков
- **Мониторинг** попыток эксплуатации системы

## 📈 Масштабируемость

### Производительность
- **Партиционирование** для исторических данных
- **Материализованные представления** для тяжелых запросов
- **Оптимизированные индексы** для всех типичных запросов

### Горизонтальное масштабирование
- **Shard-friendly design** с player_id в качестве ключа
- **Read replicas** для аналитики
- **Кеширование** часто запрашиваемых данных

## 🔄 Техническое обслуживание

### Автоматизированные процедуры

```sql
-- Очистка истекших подписок
SELECT cleanup_expired_battle_pass_subscriptions();

-- Сброс ежедневных челленджей
SELECT reset_daily_battle_pass_challenges();

-- Обновление аналитики
SELECT refresh_battle_pass_analytics();
```

### Мониторинг
- **Query performance tracking** для медленных запросов
- **Deadlock detection** и автоматическое разрешение
- **Storage monitoring** с автоматическим архивированием

Эта схема обеспечивает enterprise-grade Battle Pass систему с полной поддержкой сезонной прогрессии, монетизации и аналитики для MMOFPS RPG.
