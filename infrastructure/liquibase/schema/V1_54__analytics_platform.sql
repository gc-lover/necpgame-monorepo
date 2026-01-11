--liquibase formatted sql

--changeset analytics:001-create-analytics-schema
--comment: Create analytics schema and core tables for player behavior analysis

-- Создание схемы для аналитики
CREATE SCHEMA IF NOT EXISTS analytics;
--rollback DROP SCHEMA IF EXISTS analytics CASCADE;

--changeset analytics:002-create-player-behaviors-table
--comment: Create player behaviors table with performance optimizations

-- Таблица поведения игроков
CREATE TABLE analytics.player_behaviors (
    player_id UUID PRIMARY KEY,
    session_duration DECIMAL(8,2) NOT NULL DEFAULT 0.0,
    average_session_time DECIMAL(6,2) NOT NULL DEFAULT 0.0,
    play_frequency DECIMAL(4,2) NOT NULL DEFAULT 0.0,
    retention_rate DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (retention_rate >= 0.0 AND retention_rate <= 1.0),
    churn_probability DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (churn_probability >= 0.0 AND churn_probability <= 1.0),
    engagement_score DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (engagement_score >= 0.0 AND engagement_score <= 1.0),
    total_sessions INTEGER NOT NULL DEFAULT 0 CHECK (total_sessions >= 0),
    total_play_time INTEGER NOT NULL DEFAULT 0 CHECK (total_play_time >= 0),
    level INTEGER NOT NULL DEFAULT 1 CHECK (level >= 1),
    days_since_first INTEGER NOT NULL DEFAULT 0 CHECK (days_since_first >= 0),
    days_since_last INTEGER NOT NULL DEFAULT 0 CHECK (days_since_last >= 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_churned BOOLEAN NOT NULL DEFAULT false,
    first_session TIMESTAMP WITH TIME ZONE,
    last_session TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_player_behaviors_active ON analytics.player_behaviors(is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_player_behaviors_churn ON analytics.player_behaviors(is_churned) WHERE is_churned = true;
CREATE INDEX CONCURRENTLY idx_player_behaviors_engagement ON analytics.player_behaviors(engagement_score DESC);
CREATE INDEX CONCURRENTLY idx_player_behaviors_level ON analytics.player_behaviors(level);
CREATE INDEX CONCURRENTLY idx_player_behaviors_last_session ON analytics.player_behaviors(last_session DESC);

--changeset analytics:003-create-session-events-table
--comment: Create session events table for detailed player activity tracking

-- Таблица событий сессий
CREATE TABLE analytics.session_events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL REFERENCES analytics.player_behaviors(player_id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('session_start', 'session_end', 'level_up', 'achievement')),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duration_minutes INTEGER CHECK (duration_minutes >= 0),
    level_start INTEGER CHECK (level_start >= 1),
    level_end INTEGER CHECK (level_end >= 1),
    xp_start INTEGER DEFAULT 0 CHECK (xp_start >= 0),
    xp_end INTEGER DEFAULT 0 CHECK (xp_end >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_session_events_player_timestamp ON analytics.session_events(player_id, timestamp DESC);
CREATE INDEX CONCURRENTLY idx_session_events_type ON analytics.session_events(event_type);
CREATE INDEX CONCURRENTLY idx_session_events_timestamp ON analytics.session_events(timestamp DESC);

--changeset analytics:004-create-game-events-table
--comment: Create game events table for comprehensive event tracking

-- Таблица игровых событий
CREATE TABLE analytics.game_events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(100) NOT NULL,
    player_id UUID NOT NULL REFERENCES analytics.player_behaviors(player_id) ON DELETE CASCADE,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_game_events_player_timestamp ON analytics.game_events(player_id, timestamp DESC);
CREATE INDEX CONCURRENTLY idx_game_events_type ON analytics.game_events(event_type);
CREATE INDEX CONCURRENTLY idx_game_events_timestamp ON analytics.game_events(timestamp DESC);
CREATE INDEX CONCURRENTLY idx_game_events_data ON analytics.game_events USING GIN (data);

--changeset analytics:005-create-ab-tests-table
--comment: Create A/B tests table for experimentation framework

-- Таблица A/B тестов
CREATE TABLE analytics.ab_tests (
    test_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    confidence_level DECIMAL(3,2) NOT NULL DEFAULT 0.95 CHECK (confidence_level >= 0.8 AND confidence_level <= 0.99),
    statistical_power DECIMAL(3,2) NOT NULL DEFAULT 0.8 CHECK (statistical_power >= 0.8 AND statistical_power <= 0.99),
    min_sample_size INTEGER NOT NULL DEFAULT 1000 CHECK (min_sample_size >= 100),
    current_sample_size INTEGER NOT NULL DEFAULT 0 CHECK (current_sample_size >= 0),
    status INTEGER NOT NULL DEFAULT 0 CHECK (status >= 0 AND status <= 3), -- 0=Draft, 1=Running, 2=Completed, 3=Stopped
    is_active BOOLEAN NOT NULL DEFAULT false,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT valid_test_dates CHECK (end_date IS NULL OR end_date > start_date)
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_ab_tests_status ON analytics.ab_tests(status);
CREATE INDEX CONCURRENTLY idx_ab_tests_active ON analytics.ab_tests(is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_ab_tests_start_date ON analytics.ab_tests(start_date DESC);

--changeset analytics:006-create-ab-test-variants-table
--comment: Create A/B test variants table

-- Таблица вариантов A/B тестов
CREATE TABLE analytics.ab_test_variants (
    variant_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_id UUID NOT NULL REFERENCES analytics.ab_tests(test_id) ON DELETE CASCADE,
    variant_name VARCHAR(50) NOT NULL,
    weight DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (weight >= 0.0 AND weight <= 1.0),
    config JSONB,
    sample_size INTEGER NOT NULL DEFAULT 0 CHECK (sample_size >= 0),
    conversion DECIMAL(5,4) NOT NULL DEFAULT 0.0 CHECK (conversion >= 0.0 AND conversion <= 1.0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(test_id, variant_name)
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_ab_test_variants_test ON analytics.ab_test_variants(test_id);
CREATE INDEX CONCURRENTLY idx_ab_test_variants_conversion ON analytics.ab_test_variants(conversion DESC);

--changeset analytics:007-create-ab-test-assignments-table
--comment: Create A/B test assignments table for tracking player assignments

-- Таблица присваиваний A/B тестов
CREATE TABLE analytics.ab_test_assignments (
    assignment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_id UUID NOT NULL REFERENCES analytics.ab_tests(test_id) ON DELETE CASCADE,
    player_id UUID NOT NULL,
    variant_id UUID NOT NULL REFERENCES analytics.ab_test_variants(variant_id) ON DELETE CASCADE,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    conversion_events INTEGER NOT NULL DEFAULT 0 CHECK (conversion_events >= 0),

    UNIQUE(test_id, player_id)
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_ab_assignments_test_player ON analytics.ab_test_assignments(test_id, player_id);
CREATE INDEX CONCURRENTLY idx_ab_assignments_variant ON analytics.ab_test_assignments(variant_id);
CREATE INDEX CONCURRENTLY idx_ab_assignments_assigned_at ON analytics.ab_test_assignments(assigned_at DESC);

--changeset analytics:008-create-reports-table
--comment: Create analytics reports table

-- Таблица отчетов аналитики
CREATE TABLE analytics.reports (
    report_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    report_type VARCHAR(50) NOT NULL CHECK (report_type IN ('retention', 'behavior', 'ab_test', 'performance')),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metrics JSONB,
    insights TEXT[],
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT valid_report_dates CHECK (end_date >= start_date)
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_reports_type_generated ON analytics.reports(report_type, generated_at DESC);
CREATE INDEX CONCURRENTLY idx_reports_date_range ON analytics.reports(start_date, end_date);
CREATE INDEX CONCURRENTLY idx_reports_generated_at ON analytics.reports(generated_at DESC);

--changeset analytics:009-create-system-health-table
--comment: Create system health monitoring table

-- Таблица мониторинга здоровья системы
CREATE TABLE analytics.system_health (
    check_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    check_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_events BIGINT NOT NULL DEFAULT 0 CHECK (total_events >= 0),
    processed_events BIGINT NOT NULL DEFAULT 0 CHECK (processed_events >= 0),
    failed_events BIGINT NOT NULL DEFAULT 0 CHECK (failed_events >= 0),
    active_tests INTEGER NOT NULL DEFAULT 0 CHECK (active_tests >= 0),
    response_time_ms INTEGER NOT NULL DEFAULT 0 CHECK (response_time_ms >= 0),
    error_rate DECIMAL(6,4) NOT NULL DEFAULT 0.0 CHECK (error_rate >= 0.0 AND error_rate <= 1.0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_system_health_check_time ON analytics.system_health(check_time DESC);
CREATE INDEX CONCURRENTLY idx_system_health_error_rate ON analytics.system_health(error_rate);

--changeset analytics:010-create-retention-cohorts-table
--comment: Create retention cohorts table for advanced cohort analysis

-- Таблица когорт удержания
CREATE TABLE analytics.retention_cohorts (
    cohort_date DATE NOT NULL,
    cohort_size INTEGER NOT NULL CHECK (cohort_size > 0),
    day1_retained INTEGER NOT NULL DEFAULT 0 CHECK (day1_retained >= 0),
    day7_retained INTEGER NOT NULL DEFAULT 0 CHECK (day7_retained >= 0),
    day30_retained INTEGER NOT NULL DEFAULT 0 CHECK (day30_retained >= 0),
    day90_retained INTEGER NOT NULL DEFAULT 0 CHECK (day90_retained >= 0),
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (cohort_date),

    CONSTRAINT valid_retention_counts CHECK (
        day1_retained <= cohort_size AND
        day7_retained <= cohort_size AND
        day30_retained <= cohort_size AND
        day90_retained <= cohort_size
    )
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_retention_cohorts_size ON analytics.retention_cohorts(cohort_size DESC);
CREATE INDEX CONCURRENTLY idx_retention_cohorts_last_updated ON analytics.retention_cohorts(last_updated DESC);

--changeset analytics:011-create-triggers
--comment: Create update triggers for timestamp management

-- Триггер для обновления updated_at в player_behaviors
CREATE OR REPLACE FUNCTION analytics.update_player_behaviors_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_player_behaviors_updated_at
    BEFORE UPDATE ON analytics.player_behaviors
    FOR EACH ROW
    EXECUTE FUNCTION analytics.update_player_behaviors_updated_at();

-- Триггер для обновления updated_at в ab_tests
CREATE OR REPLACE FUNCTION analytics.update_ab_tests_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_ab_tests_updated_at
    BEFORE UPDATE ON analytics.ab_tests
    FOR EACH ROW
    EXECUTE FUNCTION analytics.update_ab_tests_updated_at();

-- Триггер для обновления updated_at в ab_test_variants
CREATE OR REPLACE FUNCTION analytics.update_ab_test_variants_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_ab_test_variants_updated_at
    BEFORE UPDATE ON analytics.ab_test_variants
    FOR EACH ROW
    EXECUTE FUNCTION analytics.update_ab_test_variants_updated_at();

--changeset analytics:012-create-performance-views
--comment: Create performance views for analytics queries

-- Представление для активных игроков
CREATE OR REPLACE VIEW analytics.active_players_view AS
SELECT
    pb.player_id,
    pb.level,
    pb.engagement_score,
    pb.total_sessions,
    pb.total_play_time,
    pb.days_since_first,
    pb.days_since_last,
    pb.last_session,
    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - pb.last_session)) / 86400 as days_since_last_session
FROM analytics.player_behaviors pb
WHERE pb.is_active = true
ORDER BY pb.engagement_score DESC, pb.last_session DESC;

-- Представление для когортного анализа
CREATE OR REPLACE VIEW analytics.cohort_analysis_view AS
SELECT
    cohort_date,
    cohort_size,
    ROUND(day1_retained::numeric / cohort_size * 100, 2) as day1_retention_pct,
    ROUND(day7_retained::numeric / cohort_size * 100, 2) as day7_retention_pct,
    ROUND(day30_retained::numeric / cohort_size * 100, 2) as day30_retention_pct,
    ROUND(day90_retained::numeric / cohort_size * 100, 2) as day90_retention_pct,
    last_updated
FROM analytics.retention_cohorts
ORDER BY cohort_date DESC;

-- Представление для A/B тестов с результатами
CREATE OR REPLACE VIEW analytics.ab_test_results_view AS
SELECT
    t.test_id,
    t.test_name,
    t.status,
    t.min_sample_size,
    t.current_sample_size,
    v.variant_name,
    v.sample_size as variant_sample_size,
    ROUND(v.conversion * 100, 2) as conversion_percentage,
    v.weight,
    t.start_date,
    t.end_date
FROM analytics.ab_tests t
JOIN analytics.ab_test_variants v ON t.test_id = v.test_id
ORDER BY t.test_id, v.variant_name;

--changeset analytics:013-create-partitions
--comment: Create table partitions for better performance

-- Партиционирование таблицы game_events по месяцам
CREATE TABLE analytics.game_events_y2024m01 PARTITION OF analytics.game_events
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

CREATE TABLE analytics.game_events_y2024m02 PARTITION OF analytics.game_events
    FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');

-- Индексы на партициях
CREATE INDEX CONCURRENTLY idx_game_events_y2024m01_timestamp ON analytics.game_events_y2024m01(timestamp DESC);
CREATE INDEX CONCURRENTLY idx_game_events_y2024m02_timestamp ON analytics.game_events_y2024m02(timestamp DESC);

--changeset analytics:014-grant-permissions
--comment: Grant necessary permissions for analytics service

-- Предоставление прав на схему
GRANT USAGE ON SCHEMA analytics TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA analytics TO app_user;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA analytics TO app_user;

-- Предоставление прав на представления
GRANT SELECT ON analytics.active_players_view TO app_user;
GRANT SELECT ON analytics.cohort_analysis_view TO app_user;
GRANT SELECT ON analytics.ab_test_results_view TO app_user;

-- Предоставление прав на функции
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA analytics TO app_user;

--changeset analytics:015-insert-sample-data
--comment: Insert sample data for testing and development

-- Вставка тестовых данных для player_behaviors
INSERT INTO analytics.player_behaviors (
    player_id, session_duration, average_session_time, play_frequency,
    retention_rate, churn_probability, engagement_score, total_sessions,
    total_play_time, level, days_since_first, days_since_last,
    is_active, is_churned, first_session, last_session
) VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 156.5, 3.2, 5.1, 0.85, 0.05, 0.78, 49, 234, 25, 45, 1, true, false, '2024-11-27', '2024-12-10'),
    ('550e8400-e29b-41d4-a716-446655440002', 89.2, 2.1, 3.8, 0.72, 0.15, 0.65, 42, 187, 18, 38, 3, true, false, '2024-11-03', '2024-12-08'),
    ('550e8400-e29b-41d4-a716-446655440003', 234.8, 4.5, 6.2, 0.91, 0.02, 0.89, 52, 421, 32, 52, 0, true, false, '2024-10-20', '2024-12-11');

-- Вставка данных для retention_cohorts
INSERT INTO analytics.retention_cohorts (
    cohort_date, cohort_size, day1_retained, day7_retained, day30_retained, day90_retained
) VALUES
    ('2024-12-01', 1000, 780, 650, 450, 320),
    ('2024-11-15', 950, 760, 620, 420, 290),
    ('2024-11-01', 1100, 820, 680, 480, 350);