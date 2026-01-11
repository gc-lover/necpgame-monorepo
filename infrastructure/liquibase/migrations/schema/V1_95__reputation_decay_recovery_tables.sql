--liquibase formatted sql

--changeset gc:reputation_decay_recovery_tables_v1_95
--comment: Create tables for reputation decay and recovery mechanics

-- Table for reputation decay processes
CREATE TABLE IF NOT EXISTS reputation_decay_processes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    faction_id UUID NOT NULL,
    current_value DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    decay_rate DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- % per day
    last_decay_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    next_decay_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() + INTERVAL '1 hour',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_decay_character FOREIGN KEY (character_id)
        REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_decay_faction FOREIGN KEY (faction_id)
        REFERENCES factions(id) ON DELETE CASCADE,
    CONSTRAINT chk_decay_rate CHECK (decay_rate >= 0 AND decay_rate <= 100),
    CONSTRAINT chk_current_value CHECK (current_value >= -1000 AND current_value <= 1000)
);

-- Table for reputation recovery processes
CREATE TABLE IF NOT EXISTS reputation_recovery_processes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    faction_id UUID NOT NULL,
    method VARCHAR(50) NOT NULL CHECK (method IN ('time_based', 'quest_based', 'payment_based', 'action_based', 'hybrid')),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'active', 'paused', 'completed', 'failed', 'cancelled')),
    start_value DECIMAL(10,2) NOT NULL,
    target_value DECIMAL(10,2) NOT NULL,
    current_value DECIMAL(10,2) NOT NULL,
    progress DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (progress >= 0 AND progress <= 1),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    estimated_end TIMESTAMP WITH TIME ZONE NOT NULL,
    actual_end TIMESTAMP WITH TIME ZONE,
    cost_currency_type VARCHAR(50) NOT NULL DEFAULT 'eddies',
    cost_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    cost_item_id UUID,
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_recovery_character FOREIGN KEY (character_id)
        REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_recovery_faction FOREIGN KEY (faction_id)
        REFERENCES factions(id) ON DELETE CASCADE,
    CONSTRAINT chk_recovery_values CHECK (start_value < target_value),
    CONSTRAINT chk_recovery_progress CHECK (progress >= 0 AND progress <= 1)
);

-- Table for reputation events (audit trail)
CREATE TABLE IF NOT EXISTS reputation_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    faction_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL, -- 'decay', 'recovery', 'action', 'manual'
    old_value DECIMAL(10,2) NOT NULL,
    new_value DECIMAL(10,2) NOT NULL,
    delta DECIMAL(10,2) NOT NULL,
    reason TEXT NOT NULL,
    source VARCHAR(100) NOT NULL, -- 'decay_worker', 'recovery_process', 'admin', etc.
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    metadata JSONB DEFAULT '{}',

    CONSTRAINT fk_event_character FOREIGN KEY (character_id)
        REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_faction FOREIGN KEY (faction_id)
        REFERENCES factions(id) ON DELETE CASCADE
);

-- Table for decay configuration per faction
CREATE TABLE IF NOT EXISTS reputation_decay_configs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    faction_id UUID NOT NULL UNIQUE,
    base_decay_rate DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- % per day
    time_threshold INTERVAL NOT NULL DEFAULT '7 days',
    min_reputation DECIMAL(10,2) NOT NULL DEFAULT -500.0,
    max_decay_rate DECIMAL(5,2) NOT NULL DEFAULT 5.0, -- % per day max
    activity_boost DECIMAL(3,2) NOT NULL DEFAULT 0.5, -- Boost factor for activity
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_decay_config_faction FOREIGN KEY (faction_id)
        REFERENCES factions(id) ON DELETE CASCADE,
    CONSTRAINT chk_decay_config_rates CHECK (base_decay_rate >= 0 AND max_decay_rate >= base_decay_rate)
);

-- Table for recovery configuration per method
CREATE TABLE IF NOT EXISTS reputation_recovery_configs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    method VARCHAR(50) NOT NULL UNIQUE CHECK (method IN ('time_based', 'quest_based', 'payment_based', 'action_based', 'hybrid')),
    base_recovery_rate DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- units per hour
    time_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    cost_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    min_duration INTERVAL NOT NULL DEFAULT '1 hour',
    max_duration INTERVAL NOT NULL DEFAULT '30 days',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes for performance (MMOFPS optimization)
CREATE INDEX IF NOT EXISTS idx_reputation_decay_character ON reputation_decay_processes(character_id);
CREATE INDEX IF NOT EXISTS idx_reputation_decay_faction ON reputation_decay_processes(faction_id);
CREATE INDEX IF NOT EXISTS idx_reputation_decay_active ON reputation_decay_processes(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_reputation_decay_next_time ON reputation_decay_processes(next_decay_time) WHERE is_active = true;

CREATE INDEX IF NOT EXISTS idx_reputation_recovery_character ON reputation_recovery_processes(character_id);
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_faction ON reputation_recovery_processes(faction_id);
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_status ON reputation_recovery_processes(status);
CREATE INDEX IF NOT EXISTS idx_reputation_recovery_end_time ON reputation_recovery_processes(estimated_end) WHERE status = 'active';

CREATE INDEX IF NOT EXISTS idx_reputation_events_character ON reputation_events(character_id);
CREATE INDEX IF NOT EXISTS idx_reputation_events_faction ON reputation_events(faction_id);
CREATE INDEX IF NOT EXISTS idx_reputation_events_timestamp ON reputation_events(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_reputation_events_type ON reputation_events(event_type);

-- Insert default recovery configurations
INSERT INTO reputation_recovery_configs (method, base_recovery_rate, time_multiplier, cost_multiplier, min_duration, max_duration)
VALUES
    ('time_based', 0.5, 1.0, 0.0, '1 hour', '7 days'),
    ('quest_based', 2.0, 0.8, 0.0, '30 minutes', '3 days'),
    ('payment_based', 5.0, 0.5, 2.0, '10 minutes', '1 day'),
    ('action_based', 1.0, 1.2, 0.0, '1 hour', '14 days'),
    ('hybrid', 3.0, 0.9, 1.5, '30 minutes', '5 days')
ON CONFLICT (method) DO NOTHING;

-- Insert default decay configurations for major factions
-- This would be populated with actual faction data
-- For now, we'll insert a sample entry
INSERT INTO reputation_decay_configs (faction_id, base_decay_rate, time_threshold, min_reputation, max_decay_rate, activity_boost)
SELECT
    id, 1.0, '7 days', -500.0, 5.0, 0.5
FROM factions
WHERE name = 'Night City Police'
ON CONFLICT (faction_id) DO NOTHING;

--rollback DROP TABLE IF EXISTS reputation_recovery_configs;
--rollback DROP TABLE IF EXISTS reputation_decay_configs;
--rollback DROP TABLE IF EXISTS reputation_events;
--rollback DROP TABLE IF EXISTS reputation_recovery_processes;
--rollback DROP TABLE IF EXISTS reputation_decay_processes;