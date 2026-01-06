# Achievement System Database Schema

Полная схема базы данных для системы достижений в NECPGAME.

## 📋 Обзор

Achievement System обеспечивает комплексную систему достижений для мотивации игроков, отслеживания прогресса и награждения за различные действия в игре. Схема поддерживает:

- Категории достижений (Combat, Social, Economy, Exploration, Special)
- Прогрессивные достижения с уровнями сложности
- Тайм-лимитные и скрытые достижения
- Коллекционные и цепочечные достижения
- Сезонные достижения
- Гильдейские достижения
- Комплексную аналитику и телеметрию

## 🏗️ Архитектура базы данных

### Основные компоненты

1. **Каталог достижений** (`achievement_definitions`, `achievement_categories`, `achievement_tags`) - Определения достижений и их классификация
2. **Прогресс игроков** (`player_achievements`, `achievement_progress`, `achievement_progress_events`) - Отслеживание прогресса
3. **Награды** (`achievement_rewards`, `achievement_definition_rewards`, `achievement_claimed_rewards`) - Система наград
4. **Расширенные возможности** (`achievement_chains`, `achievement_seasons`, `guild_achievements`) - Цепочки, сезоны, гильдии
5. **Аналитика** (`achievement_events`, `achievement_daily_stats`, `achievement_player_stats`) - Метрики и отчеты
6. **Уведомления** (`achievement_notification_preferences`, `achievement_scheduled_notifications`) - Коммуникации

## 📊 Схема таблиц

### Каталог достижений

```sql
-- Определения достижений
achievement_definitions
├── id (BIGSERIAL PRIMARY KEY)
├── code (VARCHAR UNIQUE) - уникальный код достижения
├── title, description (JSONB) - мультиязычная поддержка
├── category ('COMBAT', 'SOCIAL', 'ECONOMY', 'EXPLORATION', 'SPECIAL', 'SEASONAL', 'GUILD')
├── difficulty ('EASY', 'MEDIUM', 'HARD', 'LEGENDARY')
├── achievement_type ('STANDARD', 'PROGRESSIVE', 'TIME_LIMITED', 'HIDDEN', 'COLLECTION', 'CHAINED')
├── is_hidden, is_repeatable (BOOLEAN)
├── max_progress (INTEGER)
├── conditions, rewards (JSONB) - условия и награды
├── prerequisites (JSONB) - предварительные условия
├── chain_next_id (BIGINT) - для цепочечных достижений
└── is_active (BOOLEAN)

-- Категории достижений
achievement_categories
├── id (BIGSERIAL PRIMARY KEY)
├── category_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── icon_url, color_code
└── sort_order, is_active

-- Теги достижений
achievement_tags
├── id (BIGSERIAL PRIMARY KEY)
├── tag_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── color_code
└── is_active
```

### Прогресс игроков

```sql
-- Достижения игроков
player_achievements
├── id (BIGSERIAL PRIMARY KEY)
├── player_id (BIGINT)
├── achievement_id (BIGINT FOREIGN KEY)
├── status ('LOCKED', 'UNLOCKED', 'IN_PROGRESS', 'COMPLETED', 'CLAIMED')
├── unlocked_at, completed_at, claimed_at
├── completion_count (INTEGER) - для повторяемых достижений
└── UNIQUE(player_id, achievement_id)

-- Прогресс по достижениям
achievement_progress
├── id (BIGSERIAL PRIMARY KEY)
├── player_achievement_id (BIGINT FOREIGN KEY)
├── progress_key (VARCHAR) - конкретная метрика прогресса
├── current_value, target_value (INTEGER)
├── progress_percentage (DECIMAL GENERATED)
├── is_completed (BOOLEAN)
├── completed_at
└── UNIQUE(player_achievement_id, progress_key)

-- События прогресса
achievement_progress_events
├── id (BIGSERIAL PRIMARY KEY)
├── player_achievement_id (BIGINT FOREIGN KEY)
├── progress_key (VARCHAR)
├── progress_change (INTEGER)
├── event_type, event_reference_id
├── event_data (JSONB)
└── recorded_at (TIMESTAMP)
```

### Награды

```sql
-- Каталог наград
achievement_rewards
├── id (BIGSERIAL PRIMARY KEY)
├── reward_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── reward_type ('CURRENCY', 'ITEM', 'COSMETIC', 'TITLE', 'BOOSTER', 'UNLOCK', 'EXCLUSIVE')
├── reward_category (VARCHAR)
├── value_data (JSONB)
├── rarity ('common', 'uncommon', 'rare', 'epic', 'legendary')
├── is_stackable, max_stack
└── is_enabled (BOOLEAN)

-- Награды за достижения
achievement_definition_rewards
├── id (BIGSERIAL PRIMARY KEY)
├── achievement_id, reward_id (BIGINT FOREIGN KEY)
├── quantity, is_guaranteed
├── drop_chance (DECIMAL)
└── UNIQUE(achievement_id, reward_id)

-- Полученные награды
achievement_claimed_rewards
├── id (BIGSERIAL PRIMARY KEY)
├── player_achievement_id (BIGINT FOREIGN KEY)
├── reward_id (BIGINT FOREIGN KEY)
├── quantity, claimed_at
├── delivery_status ('PENDING', 'DELIVERED', 'FAILED')
├── delivery_reference_id (BIGINT)
```

### Расширенные возможности

```sql
-- Цепочки достижений
achievement_chains
├── id (BIGSERIAL PRIMARY KEY)
├── chain_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── chain_type ('LINEAR', 'BRANCHING', 'COLLECTION')
├── total_achievements (INTEGER)
├── reward_data (JSONB)
└── is_active (BOOLEAN)

-- Элементы цепочек
achievement_chain_members
├── id (BIGSERIAL PRIMARY KEY)
├── chain_id, achievement_id (BIGINT FOREIGN KEY)
├── position (INTEGER)
└── is_required (BOOLEAN)

-- Сезонные достижения
achievement_seasons
├── id (BIGSERIAL PRIMARY KEY)
├── season_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── start_date, end_date
├── theme_data (JSONB)
└── is_active (BOOLEAN)

-- Сезонные элементы
achievement_season_members
├── id (BIGSERIAL PRIMARY KEY)
├── season_id, achievement_id (BIGINT FOREIGN KEY)
├── is_featured (BOOLEAN)
└── bonus_multiplier (DECIMAL)
```

### Аналитика и телеметрия

```sql
-- События достижений
achievement_events
├── id (BIGSERIAL PRIMARY KEY)
├── event_type ('UNLOCKED', 'PROGRESS_UPDATE', 'COMPLETED', 'CLAIMED', 'RESET')
├── player_id, achievement_id
├── event_data, session_id (JSONB, VARCHAR)
├── client_version, platform, region
└── event_timestamp (TIMESTAMP)

-- Ежедневная статистика
achievement_daily_stats
├── id (BIGSERIAL PRIMARY KEY)
├── date (DATE)
├── achievement_id (BIGINT FOREIGN KEY)
├── total_unlocked, total_completed, total_claimed
├── avg_completion_time (INTERVAL)
├── completion_rate (DECIMAL)
```

### Уведомления

```sql
-- Настройки уведомлений
achievement_notification_preferences
├── id (BIGSERIAL PRIMARY KEY)
├── player_id (BIGINT UNIQUE)
├── unlocked_notifications, progress_notifications (BOOLEAN)
├── completed_notifications, reward_available_notifications
├── chain_progress_notifications, seasonal_notifications
└── marketing_notifications (BOOLEAN)

-- Запланированные уведомления
achievement_scheduled_notifications
├── id (BIGSERIAL PRIMARY KEY)
├── player_id, achievement_id
├── notification_type, title, message
├── data (JSONB)
├── scheduled_for, sent_at
├── delivery_status ('PENDING', 'SENT', 'DELIVERED', 'FAILED')
```

## 🔍 Оптимизации производительности

### Индексы

- **Составные индексы** для комплексных запросов (player + achievement + status)
- **Частичные индексы** для активных/завершенных записей
- **Временные индексы** для недавних данных (last 30/7 days)
- **JSONB индексы** для поиска в конфигурационных данных
- **Партиционирование** для высоконагруженных таблиц (events по месяцам)

### Материализованные представления

```sql
-- Статистика игроков
achievement_player_stats

-- Популярность достижений
achievement_popularity_stats

-- Ежедневная активность
achievement_daily_activity

-- Прогресс цепочек
achievement_chain_progress

-- Сезонная производительность
achievement_seasonal_performance
```

### Функции для оптимизации

```sql
-- Суммарная статистика игрока
get_player_achievement_summary(player_id)

-- Статистика завершения достижения
get_achievement_completion_stats(achievement_id, days_back)

-- Доступные достижения для игрока
get_available_achievements_for_player(player_id, category_filter, limit)
```

## 🚀 Миграции

### V001 - Основные таблицы
Создание полной схемы со всеми таблицами, индексами и ограничениями.

### V002 - Начальные данные
- **7 категорий**: Combat, Social, Economy, Exploration, Special, Seasonal, Guild
- **17 достижений**: от базовых до легендарных с различными условиями
- **9 типов наград**: валюта, косметика, предметы, усилители, титулы, эксклюзив
- **2 цепочки достижений**: Combat Journey и Social Circle
- **1 сезон**: Winter 2025 с тематическими достижениями
- **7 тегов**: First Steps, Master, Legend, Speedrun, Collection, Social, Rare

### V003 - Оптимизации производительности
- Дополнительные индексы для высоконагруженных запросов
- Материализованные представления для аналитики
- Партиционирование для больших таблиц
- Функции для сложных операций
- Мониторинг производительности запросов

## 📊 Ключевые возможности

### Типы достижений
- **Standard**: Одноразовые достижения с фиксированными условиями
- **Progressive**: Достижения с несколькими уровнями сложности
- **Time Limited**: Ограниченные по времени достижения
- **Hidden**: Скрытые достижения без явных подсказок
- **Collection**: Достижения за сбор коллекций
- **Chained**: Последовательные достижения в цепочке

### Прогресс и мотивация
- **Многоуровневый прогресс**: От простых действий до сложных комбо
- **Персонализация**: Адаптация под стиль игры игрока
- **Социальная конкуренция**: Лидерборды и сравнение прогресса
- **Временные бонусы**: Ускорение прогресса за премиум

### Награды и монетизация
- **Гибкая система наград**: От косметики до эксклюзивных предметов
- **Многоуровневая редкость**: 5 уровней от common до legendary
- **Стекируемые предметы**: Конфигурируемые лимиты
- **Гарантированные vs Вероятностные**: Смешанная система дропов

### Аналитика и метрики
- **Комплексные метрики**: Завершение, вовлеченность, монетизация
- **A/B тестирование**: Сравнение эффективности достижений
- **Прогнозы**: Предсказание поведения игроков
- **Оптимизация**: Автоматическая балансировка сложности

## 🔒 Безопасность

### Валидация данных
- **Database-level constraints** для всех полей и связей
- **JSON Schema validation** для конфигураций и условий
- **Referential integrity** между всеми таблицами

### Анти-чит защита
- **Серверная валидация** всех условий достижений
- **Аудит прогресса** с паттернами поведения
- **Блокировка подозрительных** действий игроков
- **Откат транзакций** при обнаружении эксплойтов

## 📈 Масштабируемость

### Производительность
- **Партиционирование** для исторических данных
- **Материализованные представления** для тяжелых запросов
- **Оптимизированные индексы** для всех типичных запросов
- **Кеширование** часто запрашиваемых данных

### Горизонтальное масштабирование
- **Player-id based sharding** для распределения нагрузки
- **Read replicas** для аналитики и статистики
- **Event-driven architecture** для асинхронной обработки

## 🔄 Техническое обслуживание

### Автоматизированные процедуры

```sql
-- Очистка старых событий
SELECT cleanup_old_achievement_events(90);

-- Очистка событий прогресса
SELECT cleanup_old_achievement_progress_events(30);

-- Обновление аналитики
SELECT refresh_achievement_analytics();

-- Валидация целостности
SELECT validate_achievement_progress_integrity();
```

### Мониторинг
- **Query performance tracking** для медленных запросов
- **Data integrity checks** для валидации связей
- **Storage monitoring** с автоматическим архивированием
- **Achievement completion rates** для балансировки сложности

Эта схема обеспечивает enterprise-grade Achievement System с полной поддержкой прогрессии, наград, аналитики и монетизации для MMOFPS RPG.
