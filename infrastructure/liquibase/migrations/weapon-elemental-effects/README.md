# Weapon Elemental Effects Database Schema

Полная схема базы данных для системы стихийных эффектов оружия в NECPGAME.

## 📋 Обзор

Weapon Elemental Effects System обеспечивает комплексную систему стихийных эффектов (огонь, лед, яд, кислота) с механиками DoT-урона, взаимодействий между стихиями, конфигурациями оружия и интеграцией с окружающей средой. Схема поддерживает:

- 4 базовые стихии с уникальными эффектами
- Сложные взаимодействия между стихиями
- Конфигурации оружия и апгрейды
- Активные эффекты и их взаимодействие
- Экологические зоны с эффектами
- Комплексную аналитику и телеметрию

## 🏗️ Архитектура базы данных

### Основные компоненты

1. **Стихии и эффекты** (`elemental_types`, `elemental_effects`, `elemental_effect_modifiers`) - Определения стихий и их эффектов
2. **Взаимодействия** (`elemental_interactions`, `elemental_interaction_triggers`) - Правила взаимодействия стихий
3. **Конфигурации оружия** (`weapon_elemental_configs`, `weapon_elemental_upgrades`) - Настройки оружия и апгрейдов
4. **Активные эффекты** (`character_elemental_effects`, `elemental_effect_damage`, `elemental_effect_interactions`) - Примененные эффекты
5. **Экологическая система** (`environmental_elemental_zones`, `environmental_zone_effects`) - Зоны с эффектами
6. **Аналитика** (`elemental_effects_stats`, `elemental_telemetry_events`, `weapon_elemental_performance`) - Метрики и телеметрия

## 📊 Схема таблиц

### Стихии и эффекты

```sql
-- Типы стихий
elemental_types
├── id (BIGSERIAL PRIMARY KEY)
├── element_key (VARCHAR UNIQUE) - 'fire', 'ice', 'poison', 'acid'
├── name, description (JSONB) - мультиязычная поддержка
├── color_code (VARCHAR) - hex код цвета
├── base_damage_type ('FIRE', 'COLD', 'POISON', 'ACID')
├── visual_effect_type ('PARTICLES', 'SCREEN_DISTORTION', 'MODEL_OVERLAY')
├── sound_effect_type ('CONTINUOUS', 'BURST', 'LOOP')
└── is_active (BOOLEAN)

-- Эффекты стихий
elemental_effects
├── id (BIGSERIAL PRIMARY KEY)
├── element_id (BIGINT FOREIGN KEY)
├── effect_key (VARCHAR UNIQUE)
├── name, description (JSONB)
├── effect_type ('DIRECT_DAMAGE', 'DOT_DAMAGE', 'STATUS_EFFECT', 'MOVEMENT_MODIFIER', 'DEFENSE_MODIFIER')
├── damage_type, base_damage, damage_per_second
├── duration_seconds, tick_interval_seconds
├── max_stacks, stat_modifiers (JSONB)
├── visual_config, sound_config (JSONB)
├── is_chainable, chain_trigger_condition
└── is_active (BOOLEAN)

-- Модификаторы эффектов
elemental_effect_modifiers
├── id (BIGSERIAL PRIMARY KEY)
├── effect_id, modifier_type ('WEAPON_TYPE', 'ARMOR_TYPE', 'TARGET_TYPE', 'ENVIRONMENT')
├── modifier_key (VARCHAR) - тип оружия/брони/цели
├── damage_multiplier, duration_multiplier
├── effect_chance_bonus
└── created_at
```

### Взаимодействия стихий

```sql
-- Правила взаимодействий
elemental_interactions
├── id (BIGSERIAL PRIMARY KEY)
├── primary_element_id, secondary_element_id (BIGINT FOREIGN KEY)
├── interaction_type ('AMPLIFY', 'COUNTER', 'NEUTRALIZE', 'COMBINE', 'CHAIN_REACTION')
├── result_element_id, result_effect_id (BIGINT FOREIGN KEY)
├── damage_multiplier, duration_multiplier
├── description (JSONB)
├── visual_config, sound_config (JSONB)
└── is_active (BOOLEAN)

-- Триггеры взаимодействий
elemental_interaction_triggers
├── id (BIGSERIAL PRIMARY KEY)
├── interaction_id (BIGINT FOREIGN KEY)
├── trigger_type ('ON_CONTACT', 'ON_STACK_OVERFLOW', 'ON_TIME_EXPIRE', 'ON_DAMAGE_RECEIVED')
├── trigger_condition (JSONB)
├── effect_config (JSONB)
├── probability, cooldown_seconds
```

### Конфигурации оружия

```sql
-- Конфигурации оружия
weapon_elemental_configs
├── id (BIGSERIAL PRIMARY KEY)
├── weapon_type ('rifle', 'shotgun', 'pistol', 'melee', 'grenade')
├── weapon_subtype, element_id (BIGINT FOREIGN KEY)
├── base_effect_chance, effect_duration_seconds
├── effect_damage_multiplier, ammo_consumption_modifier
├── heat_generation_modifier, recoil_modifier, fire_rate_modifier
├── config_data (JSONB) - дополнительные настройки
└── is_active (BOOLEAN)

-- Апгрейды оружия
weapon_elemental_upgrades
├── id (BIGSERIAL PRIMARY KEY)
├── base_config_id (BIGINT FOREIGN KEY)
├── upgrade_level (INTEGER)
├── upgrade_cost (JSONB)
├── effect_chance_bonus, damage_multiplier_bonus, duration_bonus_seconds
├── unlock_requirements (JSONB)
└── created_at
```

### Активные эффекты

```sql
-- Эффекты на персонажах
character_elemental_effects
├── id (BIGSERIAL PRIMARY KEY)
├── character_id, effect_id (BIGINT FOREIGN KEY)
├── source_weapon_id, source_character_id
├── current_stacks, max_stacks, remaining_duration_seconds
├── total_damage_dealt, applied_at, expires_at
├── effect_data (JSONB) - runtime данные эффекта
└── is_active (BOOLEAN)

-- История урона от эффектов
elemental_effect_damage
├── id (BIGSERIAL PRIMARY KEY)
├── effect_instance_id (BIGINT FOREIGN KEY)
├── damage_amount, damage_type, is_critical
├── target_character_id, target_body_part
├── damage_location (JSONB) - 3D координаты
└── damage_timestamp (TIMESTAMP)

-- Взаимодействия эффектов
elemental_effect_interactions
├── id (BIGSERIAL PRIMARY KEY)
├── character_id, primary_effect_id, secondary_effect_id (BIGINT FOREIGN KEY)
├── interaction_id (BIGINT FOREIGN KEY)
├── result_damage, result_effect_id
├── interaction_data (JSONB)
└── interaction_timestamp (TIMESTAMP)
```

### Экологическая система

```sql
-- Экологические зоны
environmental_elemental_zones
├── id (BIGSERIAL PRIMARY KEY)
├── zone_key (VARCHAR UNIQUE)
├── zone_type ('WATER', 'FIRE_SOURCE', 'TOXIC_AREA', 'ACID_POOL')
├── element_id, effect_id (BIGINT FOREIGN KEY)
├── zone_bounds, zone_center (JSONB) - границы зоны
├── zone_radius, zone_height, effect_strength
├── effect_interval_seconds, max_concurrent_effects
├── visual_config (JSONB)
└── is_active (BOOLEAN)

-- Эффекты зон на персонажей
environmental_zone_effects
├── id (BIGSERIAL PRIMARY KEY)
├── zone_id, character_id (BIGINT FOREIGN KEY)
├── effect_instance_id, entered_at, last_effect_applied_at
├── total_effects_applied, is_still_in_zone
└── exited_at (TIMESTAMP)
```

### Аналитика и телеметрия

```sql
-- Статистика эффектов
elemental_effects_stats
├── id (BIGSERIAL PRIMARY KEY)
├── date (DATE), element_id, effect_id, weapon_type
├── total_applications, total_damage_dealt, total_duration_seconds
├── average_stacks, completion_rate, interaction_count
└── created_at

-- События телеметрии
elemental_telemetry_events
├── id (BIGSERIAL PRIMARY KEY)
├── event_type ('EFFECT_APPLIED', 'EFFECT_INTERACTION', 'DAMAGE_DEALT', 'EFFECT_EXPIRED')
├── character_id, target_character_id, element_id, effect_id
├── weapon_type, damage_amount, effect_duration_seconds
├── event_data (JSONB), session_id, client_version, match_id
└── event_timestamp (TIMESTAMP)

-- Производительность оружия
weapon_elemental_performance
├── id (BIGSERIAL PRIMARY KEY)
├── weapon_type, element_id, total_shots, effects_applied
├── effect_accuracy, average_damage_per_effect
├── average_effect_duration, kill_to_effect_ratio
└── measured_at (TIMESTAMP)
```

## 🔍 Оптимизации производительности

### Индексы

- **Составные индексы** для комплексных запросов (character + effect + status)
- **Частичные индексы** для активных/истекающих эффектов
- **Пространственные индексы** для экологических зон (GIN indexes)
- **Временные индексы** для недавних событий (last hour/day)
- **Партиционирование** для высоконагруженных таблиц (effects, damage, telemetry по месяцам)

### Материализованные представления

```sql
-- Суммарная статистика персонажей
character_elemental_summary

-- Эффективность оружия
weapon_elemental_effectiveness

-- Частота взаимодействий
elemental_interaction_frequency

-- Эффективность экологических зон
environmental_zone_effectiveness

-- Ежедневная статистика боя
daily_elemental_combat_stats
```

### Функции для оптимизации

```sql
-- Статус стихийных эффектов персонажа
get_character_elemental_status(character_id)

-- Производительность оружия
calculate_weapon_elemental_performance(weapon_type, element_id, time_window)

-- Экологические эффекты персонажа
get_character_environmental_effects(character_id)
```

## 🚀 Миграции

### V001 - Основные таблицы
Создание полной схемы со всеми таблицами, индексами и ограничениями.

### V002 - Начальные данные
- **4 стихии**: Fire (огонь), Ice (лед), Poison (яд), Acid (кислота) с уникальными эффектами
- **8 базовых эффектов**: Burn, Frost Slow, Toxin Buildup, Armor Corrosion и другие
- **5 взаимодействий стихий**: Fire+Ice=Steam, Fire+Poison=Toxic Explosion, Ice+Poison=Frozen Toxin, Ice+Acid=Corrosive Sludge, Poison+Acid=Mutagenic Poison
- **10 конфигураций оружия**: Rifles, Shotguns, Pistols, Melee, Grenades, Launchers с элементами
- **4 экологические зоны**: Volcano lava pool, Arctic ice field, Chemical spill, Toxic waste dump
- **Баланс и A/B тесты**: Конфигурации для балансировки и тестирования

### V003 - Оптимизации производительности
- Дополнительные индексы для высоконагруженных запросов
- Материализованные представления для аналитики
- Партиционирование для больших таблиц
- Функции для сложных операций
- Мониторинг производительности запросов

## 📊 Ключевые возможности

### Стихии и эффекты
- **4 уникальные стихии** с различными типами урона и визуальными эффектами
- **Многоуровневые эффекты**: Прямой урон, DoT, статус-эффекты, модификаторы движения/защиты
- **Стеки эффектов** с накоплением и комбо-эффектами
- **Модификаторы** для разных типов оружия, брони и целей

### Взаимодействия стихий
- **5 типов взаимодействий**: Усиление, Противодействие, Нейтрализация, Комбинирование, Цепная реакция
- **Триггеры взаимодействий**: По контакту, переполнению стаков, истечению времени, получению урона
- **Результаты взаимодействий**: Новые эффекты, измененный урон, специальные визуальные эффекты
- **Вероятностные исходы** с настраиваемыми шансами

### Интеграция с оружием
- **Гибкие конфигурации**: Шанс применения, длительность, множители урона для каждого типа оружия
- **Апгрейд система**: Многоуровневые улучшения с требованиями разблокировки
- **Баланс оружие-элемент**: Различное поведение элементов на разных типах оружия
- **Дополнительные эффекты**: Перегрев, отдача, скорострельность

### Экологические эффекты
- **Зоны влияния**: Круговые, прямоугольные зоны с эффектами стихий
- **Динамические эффекты**: Интервальное применение эффектов с ограничением количества целей
- **Визуальная интеграция**: Частицы, цвета, звуки для каждой зоны
- **Отслеживание экспозиции**: Время нахождения в зоне, количество примененных эффектов

### Аналитика и телеметрия
- **Детальная телеметрия**: Все применения эффектов, взаимодействия, урон, истечение
- **Производительность оружия**: Точность эффектов, средний урон, соотношение убийств
- **Статистика взаимодействий**: Частота, урон, время до взаимодействия
- **Ежедневные отчеты**: Активность, вовлеченность, эффективность

## 🔒 Безопасность

### Валидация данных
- **Database-level constraints** для всех полей и связей
- **JSON Schema validation** для конфигураций эффектов
- **Referential integrity** между всеми таблицами

### Анти-чит защита
- **Серверная валидация** всех применений эффектов
- **Аудит взаимодействий** с паттернами подозрительной активности
- **Валидация источников** эффектов и их параметров
- **Ограничения стаков** и длительности эффектов

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
- **Zone-based partitioning** для экологических эффектов

## 🔄 Техническое обслуживание

### Автоматизированные процедуры

```sql
-- Очистка истекших эффектов
SELECT cleanup_expired_elemental_effects();

-- Очистка старой телеметрии
SELECT cleanup_old_elemental_telemetry(90);

-- Обновление экологических эффектов
SELECT update_environmental_zone_effects();

-- Обновление аналитики
SELECT refresh_weapon_elemental_analytics();

-- Валидация целостности
SELECT validate_elemental_effects_integrity();
```

### Мониторинг
- **Query performance tracking** для медленных запросов
- **Effect application rates** для мониторинга нагрузки
- **Interaction frequency** для балансировки
- **Zone effectiveness** для оптимизации окружения

Эта схема обеспечивает enterprise-grade Weapon Elemental Effects System с полной поддержкой стихийных механик, взаимодействий, оружия и аналитики для MMOFPS RPG.
