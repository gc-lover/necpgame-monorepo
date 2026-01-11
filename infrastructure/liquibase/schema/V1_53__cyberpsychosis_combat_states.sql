--liquibase formatted sql

--changeset cyberpsychosis:001-create-cyberpsychosis-schema
--comment: Create cyberpsychosis schema and tables for combat states management

-- Создание схемы для cyberpsychosis
CREATE SCHEMA IF NOT EXISTS cyberpsychosis;
--rollback DROP SCHEMA IF EXISTS cyberpsychosis CASCADE;

--changeset cyberpsychosis:002-create-states-table
--comment: Create cyberpsychosis states table with performance optimizations

-- Таблица состояний киберпсихоза
CREATE TABLE cyberpsychosis.states (
    state_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    state_type INTEGER NOT NULL CHECK (state_type >= 0 AND state_type <= 5),
    severity_level INTEGER NOT NULL DEFAULT 1 CHECK (severity_level >= 1 AND severity_level <= 10),
    is_active BOOLEAN NOT NULL DEFAULT true,
    damage_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (damage_multiplier >= 0.1 AND damage_multiplier <= 5.0),
    speed_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (speed_multiplier >= 0.1 AND speed_multiplier <= 5.0),
    accuracy_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (accuracy_multiplier >= 0.1 AND accuracy_multiplier <= 3.0),
    health_drain_rate DECIMAL(4,1) NOT NULL DEFAULT 0.0 CHECK (health_drain_rate >= 0.0 AND health_drain_rate <= 50.0),
    neural_overload_level DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (neural_overload_level >= 0.0 AND neural_overload_level <= 1.0),
    system_instability DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (system_instability >= 0.0 AND system_instability <= 1.0),
    is_controllable BOOLEAN NOT NULL DEFAULT true,
    can_be_cured BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности (MMOFPS оптимизации)
CREATE INDEX CONCURRENTLY idx_states_player_active ON cyberpsychosis.states(player_id, is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_states_state_type ON cyberpsychosis.states(state_type);
CREATE INDEX CONCURRENTLY idx_states_severity ON cyberpsychosis.states(severity_level);
CREATE INDEX CONCURRENTLY idx_states_created_at ON cyberpsychosis.states(created_at DESC);

-- Row Level Security
ALTER TABLE cyberpsychosis.states ENABLE ROW LEVEL SECURITY;

-- Политика: игроки могут видеть только свои состояния
CREATE POLICY states_player_access ON cyberpsychosis.states
    FOR ALL USING (player_id = current_setting('app.current_player_id')::UUID);

--changeset cyberpsychosis:003-create-state-transitions-table
--comment: Create state transitions audit table

-- Таблица переходов состояний для аудита
CREATE TABLE cyberpsychosis.state_transitions (
    transition_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    state_id UUID NOT NULL REFERENCES cyberpsychosis.states(state_id) ON DELETE CASCADE,
    from_state INTEGER NOT NULL CHECK (from_state >= 0 AND from_state <= 5),
    to_state INTEGER NOT NULL CHECK (to_state >= 0 AND to_state <= 5),
    transition_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trigger_reason TEXT NOT NULL CHECK (length(trigger_reason) > 0 AND length(trigger_reason) <= 256),
    severity_change INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_transitions_state_id ON cyberpsychosis.state_transitions(state_id);
CREATE INDEX CONCURRENTLY idx_transitions_time ON cyberpsychosis.state_transitions(transition_time DESC);
CREATE INDEX CONCURRENTLY idx_transitions_from_to ON cyberpsychosis.state_transitions(from_state, to_state);

--changeset cyberpsychosis:004-create-combat-sessions-table
--comment: Create combat sessions table for context tracking

-- Таблица боевых сессий
CREATE TABLE cyberpsychosis.combat_sessions (
    session_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_time TIMESTAMP WITH TIME ZONE,
    kills INTEGER NOT NULL DEFAULT 0 CHECK (kills >= 0),
    deaths INTEGER NOT NULL DEFAULT 0 CHECK (deaths >= 0),
    damage_dealt DECIMAL(10,2) NOT NULL DEFAULT 0.0 CHECK (damage_dealt >= 0.0),
    damage_taken DECIMAL(10,2) NOT NULL DEFAULT 0.0 CHECK (damage_taken >= 0.0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT valid_session_times CHECK (end_time IS NULL OR end_time > start_time)
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_sessions_player_active ON cyberpsychosis.combat_sessions(player_id, is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_sessions_start_time ON cyberpsychosis.combat_sessions(start_time DESC);
CREATE INDEX CONCURRENTLY idx_sessions_end_time ON cyberpsychosis.combat_sessions(end_time) WHERE end_time IS NOT NULL;

--changeset cyberpsychosis:005-create-system-config-table
--comment: Create system configuration table

-- Таблица конфигурации системы
CREATE TABLE cyberpsychosis.system_config (
    config_key VARCHAR(100) PRIMARY KEY,
    config_value JSONB NOT NULL,
    description TEXT,
    updated_by VARCHAR(100),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Вставка дефолтных настроек
INSERT INTO cyberpsychosis.system_config (config_key, config_value, description) VALUES
('max_severity_level', '10', 'Maximum allowed severity level'),
('state_transition_time', '"30s"', 'Time between automatic state transitions'),
('health_drain_interval', '"5s"', 'Interval for health drain processing'),
('cure_cooldown_time', '"5m"', 'Cooldown between cure attempts'),
('berserk_duration', '"30s"', 'Duration of berserk state'),
('adrenal_overload_duration', '"45s"', 'Duration of adrenal overload state'),
('neural_overload_duration', '"60s"', 'Duration of neural overload state'),
('system_shock_duration', '"10s"', 'Duration of system shock state');

--changeset cyberpsychosis:006-create-health-monitoring-table
--comment: Create health monitoring table

-- Таблица мониторинга здоровья системы
CREATE TABLE cyberpsychosis.health_monitoring (
    check_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    check_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active_states_count INTEGER NOT NULL DEFAULT 0,
    total_states_count INTEGER NOT NULL DEFAULT 0,
    average_severity DECIMAL(4,2) NOT NULL DEFAULT 0.0,
    health_score DECIMAL(5,4) NOT NULL DEFAULT 1.0 CHECK (health_score >= 0.0 AND health_score <= 1.0),
    response_time_ms INTEGER NOT NULL DEFAULT 0,
    error_count INTEGER NOT NULL DEFAULT 0,
    check_duration_ms INTEGER NOT NULL DEFAULT 0
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_health_check_time ON cyberpsychosis.health_monitoring(check_time DESC);
CREATE INDEX CONCURRENTLY idx_health_score ON cyberpsychosis.health_monitoring(health_score);

--changeset cyberpsychosis:007-create-triggers
--comment: Create update triggers for timestamp management

-- Триггер для обновления updated_at в states
CREATE OR REPLACE FUNCTION cyberpsychosis.update_states_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_states_updated_at
    BEFORE UPDATE ON cyberpsychosis.states
    FOR EACH ROW
    EXECUTE FUNCTION cyberpsychosis.update_states_updated_at();

-- Триггер для обновления updated_at в combat_sessions
CREATE OR REPLACE FUNCTION cyberpsychosis.update_sessions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_sessions_updated_at
    BEFORE UPDATE ON cyberpsychosis.combat_sessions
    FOR EACH ROW
    EXECUTE FUNCTION cyberpsychosis.update_sessions_updated_at();

--changeset cyberpsychosis:008-create-performance-views
--comment: Create performance monitoring views

-- Представление для активных состояний
CREATE OR REPLACE VIEW cyberpsychosis.active_states_view AS
SELECT
    s.state_id,
    s.player_id,
    s.state_type,
    s.severity_level,
    s.damage_multiplier,
    s.speed_multiplier,
    s.accuracy_multiplier,
    s.health_drain_rate,
    s.neural_overload_level,
    s.system_instability,
    s.is_controllable,
    s.can_be_cured,
    s.created_at,
    EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - s.created_at)) as state_duration_seconds
FROM cyberpsychosis.states s
WHERE s.is_active = true;

-- Представление для статистики состояний
CREATE OR REPLACE VIEW cyberpsychosis.state_statistics AS
SELECT
    state_type,
    COUNT(*) as total_count,
    COUNT(*) FILTER (WHERE is_active = true) as active_count,
    AVG(severity_level) as avg_severity,
    MIN(severity_level) as min_severity,
    MAX(severity_level) as max_severity,
    AVG(damage_multiplier) as avg_damage_mult,
    AVG(speed_multiplier) as avg_speed_mult,
    AVG(health_drain_rate) as avg_drain_rate
FROM cyberpsychosis.states
GROUP BY state_type;

--changeset cyberpsychosis:009-create-notifications-table
--comment: Create notifications table for state change alerts

-- Таблица уведомлений о изменениях состояний
CREATE TABLE cyberpsychosis.notifications (
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    state_id UUID REFERENCES cyberpsychosis.states(state_id) ON DELETE CASCADE,
    notification_type VARCHAR(50) NOT NULL CHECK (notification_type IN ('state_triggered', 'state_deactivated', 'severity_changed', 'cure_available')),
    message TEXT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для производительности
CREATE INDEX CONCURRENTLY idx_notifications_player ON cyberpsychosis.notifications(player_id, is_read, created_at DESC);
CREATE INDEX CONCURRENTLY idx_notifications_type ON cyberpsychosis.notifications(notification_type);

--changeset cyberpsychosis:010-grant-permissions
--comment: Grant necessary permissions

-- Предоставление прав на схему
GRANT USAGE ON SCHEMA cyberpsychosis TO app_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA cyberpsychosis TO app_user;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA cyberpsychosis TO app_user;

-- Предоставление прав на представления
GRANT SELECT ON cyberpsychosis.active_states_view TO app_user;
GRANT SELECT ON cyberpsychosis.state_statistics TO app_user;

-- Предоставление прав на функции
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA cyberpsychosis TO app_user;