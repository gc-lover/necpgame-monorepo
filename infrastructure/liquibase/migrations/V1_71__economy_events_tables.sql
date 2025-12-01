-- Issue: #140890219
-- Economy Events System Database Schema
-- Создание таблиц для системы экономических событий:
-- - economic_events (экономические события)
-- - economic_event_history (история изменений событий)
-- - economic_event_metrics (метрики событий для мониторинга)

-- Создание схемы economy, если её нет
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economic_event_type') THEN
        CREATE TYPE economic_event_type AS ENUM ('CRISIS', 'BOOM', 'INFLATION', 'EMBARGO', 'SANCTIONS', 'TARIFFS', 'SCANDAL', 'TECH_BREAKTHROUGH');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economic_event_scope') THEN
        CREATE TYPE economic_event_scope AS ENUM ('GLOBAL', 'REGIONAL');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economic_event_status') THEN
        CREATE TYPE economic_event_status AS ENUM ('PLANNED', 'ANNOUNCED', 'ACTIVE', 'COOLDOWN', 'ARCHIVED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economic_event_action') THEN
        CREATE TYPE economic_event_action AS ENUM ('CREATED', 'UPDATED', 'ANNOUNCED', 'ACTIVATED', 'ARCHIVED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'economic_event_metric_type') THEN
        CREATE TYPE economic_event_metric_type AS ENUM ('PRICE_DEVIATION', 'TRANSACTION_VOLUME', 'EVENT_UPTIME');
    END IF;
END $$;

-- Таблица экономических событий
CREATE TABLE IF NOT EXISTS economy.economic_events (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_type economic_event_type NOT NULL,
    scope economic_event_scope NOT NULL,
    region_id UUID, -- для региональных событий (может быть NULL для глобальных)
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status economic_event_status NOT NULL DEFAULT 'PLANNED',
    planned_start TIMESTAMP,
    announced_at TIMESTAMP,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    archived_at TIMESTAMP,
    effects JSONB NOT NULL DEFAULT '{}',
    coverage JSONB NOT NULL DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (
        (scope = 'REGIONAL' AND region_id IS NOT NULL) OR
        (scope = 'GLOBAL' AND region_id IS NULL)
    )
);

-- Индексы для economic_events
CREATE INDEX IF NOT EXISTS idx_economic_events_event_type 
    ON economy.economic_events(event_type);
CREATE INDEX IF NOT EXISTS idx_economic_events_status 
    ON economy.economic_events(status);
CREATE INDEX IF NOT EXISTS idx_economic_events_scope 
    ON economy.economic_events(scope);
CREATE INDEX IF NOT EXISTS idx_economic_events_region_id 
    ON economy.economic_events(region_id) WHERE region_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economic_events_planned_start 
    ON economy.economic_events(planned_start) WHERE planned_start IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economic_events_started_at 
    ON economy.economic_events(started_at) WHERE started_at IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_economic_events_status_started 
    ON economy.economic_events(status, started_at) WHERE status = 'ACTIVE';

-- Таблица истории изменений событий
CREATE TABLE IF NOT EXISTS economy.economic_event_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES economy.economic_events(id) ON DELETE CASCADE,
    action economic_event_action NOT NULL,
    changed_by UUID, -- user_id или system (может быть NULL для системных изменений)
    changes JSONB NOT NULL DEFAULT '{}',
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для economic_event_history
CREATE INDEX IF NOT EXISTS idx_economic_event_history_event_id 
    ON economy.economic_event_history(event_id);
CREATE INDEX IF NOT EXISTS idx_economic_event_history_timestamp 
    ON economy.economic_event_history(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_economic_event_history_action 
    ON economy.economic_event_history(action);

-- Таблица метрик событий для мониторинга
CREATE TABLE IF NOT EXISTS economy.economic_event_metrics (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES economy.economic_events(id) ON DELETE CASCADE,
    metric_type economic_event_metric_type NOT NULL,
    value DECIMAL(20,8) NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для economic_event_metrics
CREATE INDEX IF NOT EXISTS idx_economic_event_metrics_event_id 
    ON economy.economic_event_metrics(event_id);
CREATE INDEX IF NOT EXISTS idx_economic_event_metrics_metric_type 
    ON economy.economic_event_metrics(metric_type);
CREATE INDEX IF NOT EXISTS idx_economic_event_metrics_recorded_at 
    ON economy.economic_event_metrics(recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_economic_event_metrics_event_type_recorded 
    ON economy.economic_event_metrics(event_id, metric_type, recorded_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE economy.economic_events IS 'Экономические события, влияющие на цены, курсы и активность игроков';
COMMENT ON TABLE economy.economic_event_history IS 'История изменений событий для аудита';
COMMENT ON TABLE economy.economic_event_metrics IS 'Метрики событий для мониторинга (PriceDeviation, TransactionVolume, EventUptime)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.economic_events.event_type IS 'Тип события: CRISIS, BOOM, INFLATION, EMBARGO, SANCTIONS, TARIFFS, SCANDAL, TECH_BREAKTHROUGH';
COMMENT ON COLUMN economy.economic_events.scope IS 'Охват события: GLOBAL (глобальное), REGIONAL (региональное)';
COMMENT ON COLUMN economy.economic_events.region_id IS 'ID региона для региональных событий (NULL для глобальных)';
COMMENT ON COLUMN economy.economic_events.title IS 'Название события';
COMMENT ON COLUMN economy.economic_events.description IS 'Описание события';
COMMENT ON COLUMN economy.economic_events.status IS 'Статус события: PLANNED, ANNOUNCED, ACTIVE, COOLDOWN, ARCHIVED';
COMMENT ON COLUMN economy.economic_events.planned_start IS 'Запланированное время начала события';
COMMENT ON COLUMN economy.economic_events.announced_at IS 'Время анонса события';
COMMENT ON COLUMN economy.economic_events.started_at IS 'Время начала события';
COMMENT ON COLUMN economy.economic_events.ended_at IS 'Время окончания события';
COMMENT ON COLUMN economy.economic_events.archived_at IS 'Время архивации события';
COMMENT ON COLUMN economy.economic_events.effects IS 'Модификаторы цен, курсов, ликвидности в JSONB';
COMMENT ON COLUMN economy.economic_events.coverage IS 'Охват события (регионы, отрасли, товары) в JSONB';
COMMENT ON COLUMN economy.economic_event_history.event_id IS 'ID события';
COMMENT ON COLUMN economy.economic_event_history.action IS 'Действие: CREATED, UPDATED, ANNOUNCED, ACTIVATED, ARCHIVED';
COMMENT ON COLUMN economy.economic_event_history.changed_by IS 'ID пользователя или system (NULL для системных изменений)';
COMMENT ON COLUMN economy.economic_event_history.changes IS 'Детали изменений в JSONB';
COMMENT ON COLUMN economy.economic_event_history.timestamp IS 'Время изменения';
COMMENT ON COLUMN economy.economic_event_metrics.event_id IS 'ID события';
COMMENT ON COLUMN economy.economic_event_metrics.metric_type IS 'Тип метрики: PRICE_DEVIATION, TRANSACTION_VOLUME, EVENT_UPTIME';
COMMENT ON COLUMN economy.economic_event_metrics.value IS 'Значение метрики';
COMMENT ON COLUMN economy.economic_event_metrics.recorded_at IS 'Время записи метрики';


