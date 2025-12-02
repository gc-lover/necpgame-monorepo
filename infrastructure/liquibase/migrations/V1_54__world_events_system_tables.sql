-- Issue: #44
-- World Events System Database Schema
-- Создание таблиц для системы мировых событий:
-- - Мировые события (world_events)
-- - Эффекты событий (event_effects)
-- - Расписание событий (event_schedules)
-- - История событий (event_history)
-- - Аналитика событий (event_analytics)

-- Создание схемы world_events, если её нет
CREATE SCHEMA IF NOT EXISTS world_events;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_event_type') THEN
        CREATE TYPE world_event_type AS ENUM ('STORY', 'TECHNOLOGICAL', 'ECONOMIC', 'POLITICAL', 'MILITARY', 'SPORTS', 'ENVIRONMENTAL');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_event_scale') THEN
        CREATE TYPE world_event_scale AS ENUM ('GLOBAL', 'REGIONAL', 'CITY', 'LOCAL');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_event_frequency') THEN
        CREATE TYPE world_event_frequency AS ENUM ('ONE_TIME', 'PERIODIC', 'REGULAR');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_event_status') THEN
        CREATE TYPE world_event_status AS ENUM ('PLANNED', 'ANNOUNCED', 'ACTIVE', 'COOLDOWN', 'ARCHIVED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'event_effect_target_system') THEN
        CREATE TYPE event_effect_target_system AS ENUM ('ECONOMY', 'SOCIAL', 'GAMEPLAY', 'REPUTATION', 'QUEST');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'event_schedule_trigger_type') THEN
        CREATE TYPE event_schedule_trigger_type AS ENUM ('CRON', 'MANUAL', 'QUEST', 'SIMULATION');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'event_schedule_status') THEN
        CREATE TYPE event_schedule_status AS ENUM ('SCHEDULED', 'TRIGGERED', 'CANCELLED');
    END IF;
END $$;

-- Таблица мировых событий
CREATE TABLE IF NOT EXISTS world_events.world_events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type world_event_type NOT NULL,
    scale world_event_scale NOT NULL,
    frequency world_event_frequency NOT NULL,
    status world_event_status NOT NULL DEFAULT 'PLANNED',
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    duration INTERVAL,
    target_regions TEXT[],
    target_factions UUID[],
    prerequisites UUID[],
    cooldown_duration INTERVAL,
    max_concurrent INTEGER NOT NULL DEFAULT 1 CHECK (max_concurrent > 0),
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для world_events
CREATE INDEX IF NOT EXISTS idx_world_events_type ON world_events.world_events(type);
CREATE INDEX IF NOT EXISTS idx_world_events_scale ON world_events.world_events(scale);
CREATE INDEX IF NOT EXISTS idx_world_events_status ON world_events.world_events(status, start_time);
CREATE INDEX IF NOT EXISTS idx_world_events_start_time ON world_events.world_events(start_time) WHERE start_time IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_world_events_end_time ON world_events.world_events(end_time) WHERE end_time IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_world_events_active ON world_events.world_events(status) WHERE status = 'ACTIVE';

-- Таблица эффектов событий
CREATE TABLE IF NOT EXISTS world_events.event_effects (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES world_events.world_events(id) ON DELETE CASCADE,
    target_system event_effect_target_system NOT NULL,
    effect_type VARCHAR(100) NOT NULL,
    parameters JSONB NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для event_effects
CREATE INDEX IF NOT EXISTS idx_event_effects_event_id ON world_events.event_effects(event_id, is_active);
CREATE INDEX IF NOT EXISTS idx_event_effects_target_system ON world_events.event_effects(target_system, is_active);
CREATE INDEX IF NOT EXISTS idx_event_effects_active ON world_events.event_effects(is_active, start_time) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_event_effects_time_range ON world_events.event_effects(start_time, end_time) WHERE is_active = true;

-- Таблица расписания событий
CREATE TABLE IF NOT EXISTS world_events.event_schedules (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES world_events.world_events(id) ON DELETE CASCADE,
    scheduled_time TIMESTAMP NOT NULL,
    trigger_type event_schedule_trigger_type NOT NULL,
    trigger_parameters JSONB,
    status event_schedule_status NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для event_schedules
CREATE INDEX IF NOT EXISTS idx_event_schedules_event_id ON world_events.event_schedules(event_id, status);
CREATE INDEX IF NOT EXISTS idx_event_schedules_scheduled_time ON world_events.event_schedules(scheduled_time, status) WHERE status = 'SCHEDULED';
CREATE INDEX IF NOT EXISTS idx_event_schedules_trigger_type ON world_events.event_schedules(trigger_type, status);
CREATE INDEX IF NOT EXISTS idx_event_schedules_status ON world_events.event_schedules(status, scheduled_time);

-- Таблица истории событий
CREATE TABLE IF NOT EXISTS world_events.event_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES world_events.world_events(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL CHECK (action IN ('CREATED', 'UPDATED', 'ANNOUNCED', 'ACTIVATED', 'DEACTIVATED', 'ARCHIVED', 'CANCELLED')),
    changed_by UUID,
    changes JSONB,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для event_history
CREATE INDEX IF NOT EXISTS idx_event_history_event_id ON world_events.event_history(event_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_event_history_action ON world_events.event_history(action, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_event_history_timestamp ON world_events.event_history(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_event_history_changed_by ON world_events.event_history(changed_by, timestamp DESC) WHERE changed_by IS NOT NULL;

-- Таблица аналитики событий
CREATE TABLE IF NOT EXISTS world_events.event_analytics (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES world_events.world_events(id) ON DELETE CASCADE,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15, 4),
    metric_data JSONB,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для event_analytics
CREATE INDEX IF NOT EXISTS idx_event_analytics_event_id ON world_events.event_analytics(event_id, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_event_analytics_metric_name ON world_events.event_analytics(metric_name, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_event_analytics_recorded_at ON world_events.event_analytics(recorded_at DESC);

-- Таблица участников событий (для отслеживания участия игроков)
CREATE TABLE IF NOT EXISTS world_events.event_participants (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES world_events.world_events(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    participation_type VARCHAR(50),
    participation_data JSONB,
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP,
    UNIQUE(event_id, character_id)
);

-- Индексы для event_participants
CREATE INDEX IF NOT EXISTS idx_event_participants_event_id ON world_events.event_participants(event_id);
CREATE INDEX IF NOT EXISTS idx_event_participants_character_id ON world_events.event_participants(character_id);
CREATE INDEX IF NOT EXISTS idx_event_participants_active ON world_events.event_participants(event_id) WHERE left_at IS NULL;

-- Комментарии к таблицам
COMMENT ON TABLE world_events.world_events IS 'Мировые события (технологические, политические, военные, спортивные, экологические, сюжетные)';
COMMENT ON TABLE world_events.event_effects IS 'Эффекты событий, применяемые к различным системам (экономика, социальная, геймплей, репутация, квесты)';
COMMENT ON TABLE world_events.event_schedules IS 'Расписание событий (CRON, ручной запуск, квест, симуляция)';
COMMENT ON TABLE world_events.event_history IS 'История изменений событий для аудита';
COMMENT ON TABLE world_events.event_analytics IS 'Аналитика событий (метрики, статистика)';
COMMENT ON TABLE world_events.event_participants IS 'Участники событий (игроки, персонажи)';

-- Комментарии к колонкам
COMMENT ON COLUMN world_events.world_events.type IS 'Тип события: STORY, TECHNOLOGICAL, ECONOMIC, POLITICAL, MILITARY, SPORTS, ENVIRONMENTAL';
COMMENT ON COLUMN world_events.world_events.scale IS 'Масштаб события: GLOBAL, REGIONAL, CITY, LOCAL';
COMMENT ON COLUMN world_events.world_events.frequency IS 'Частота события: ONE_TIME, PERIODIC, REGULAR';
COMMENT ON COLUMN world_events.world_events.status IS 'Статус события: PLANNED, ANNOUNCED, ACTIVE, COOLDOWN, ARCHIVED';
COMMENT ON COLUMN world_events.world_events.target_regions IS 'Целевые регионы (массив строк)';
COMMENT ON COLUMN world_events.world_events.target_factions IS 'Целевые фракции (массив UUID)';
COMMENT ON COLUMN world_events.world_events.prerequisites IS 'Предварительные условия (массив UUID других событий)';
COMMENT ON COLUMN world_events.world_events.max_concurrent IS 'Максимальное количество одновременных экземпляров события';
COMMENT ON COLUMN world_events.event_effects.target_system IS 'Целевая система: ECONOMY, SOCIAL, GAMEPLAY, REPUTATION, QUEST';
COMMENT ON COLUMN world_events.event_effects.parameters IS 'Параметры эффекта (JSONB)';
COMMENT ON COLUMN world_events.event_schedules.trigger_type IS 'Тип триггера: CRON, MANUAL, QUEST, SIMULATION';
COMMENT ON COLUMN world_events.event_schedules.trigger_parameters IS 'Параметры триггера (JSONB, например, CRON выражение)';
COMMENT ON COLUMN world_events.event_history.action IS 'Действие: CREATED, UPDATED, ANNOUNCED, ACTIVATED, DEACTIVATED, ARCHIVED, CANCELLED';
COMMENT ON COLUMN world_events.event_history.changes IS 'Детали изменений (JSONB)';
COMMENT ON COLUMN world_events.event_analytics.metric_name IS 'Название метрики';
COMMENT ON COLUMN world_events.event_analytics.metric_value IS 'Значение метрики (числовое)';
COMMENT ON COLUMN world_events.event_analytics.metric_data IS 'Дополнительные данные метрики (JSONB)';


