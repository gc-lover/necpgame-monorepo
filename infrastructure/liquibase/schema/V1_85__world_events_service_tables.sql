-- Issue: #2224 - World Events Service Database Schema
-- liquibase formatted sql

--changeset backend:world-events-service-schema dbms:postgresql
--comment: Create complete schema for world events service

BEGIN;

-- Table: gameplay.world_events
-- Core world event data
CREATE TABLE IF NOT EXISTS gameplay.world_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('DISASTER', 'FESTIVAL', 'WAR', 'INVASION', 'TOURNAMENT', 'QUEST')),
    region VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ANNOUNCED' CHECK (status IN ('ANNOUNCED', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,
    description TEXT,
    objectives JSONB DEFAULT '[]'::jsonb,
    rewards JSONB DEFAULT '[]'::jsonb,
    max_participants INTEGER,
    current_participants INTEGER NOT NULL DEFAULT 0,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD', 'EXTREME')),
    event_data JSONB, -- Additional event-specific data
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: gameplay.event_participants
-- Player participation in world events
CREATE TABLE IF NOT EXISTS gameplay.event_participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES gameplay.world_events(id) ON DELETE CASCADE,
    player_id VARCHAR(100) NOT NULL,
    join_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMPLETED', 'LEFT', 'KICKED')),
    progress_data JSONB DEFAULT '{}'::jsonb, -- Player-specific progress
    score INTEGER DEFAULT 0,
    achievements JSONB DEFAULT '[]'::jsonb, -- Earned achievements during event
    last_activity TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_id, player_id)
);

-- Table: gameplay.event_rewards
-- Rewards earned by players in events
CREATE TABLE IF NOT EXISTS gameplay.event_rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES gameplay.world_events(id) ON DELETE CASCADE,
    player_id VARCHAR(100) NOT NULL,
    reward_type VARCHAR(50) NOT NULL, -- 'xp', 'currency', 'item', 'achievement', etc.
    reward_id VARCHAR(100), -- Item ID, achievement ID, etc.
    amount INTEGER DEFAULT 1,
    claimed BOOLEAN NOT NULL DEFAULT FALSE,
    claimed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_id, player_id, reward_type, COALESCE(reward_id, ''))
);

-- Table: gameplay.event_templates
-- Reusable event templates
CREATE TABLE IF NOT EXISTS gameplay.event_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('DISASTER', 'FESTIVAL', 'WAR', 'INVASION', 'TOURNAMENT', 'QUEST')),
    difficulty VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD', 'EXTREME')),
    description TEXT,
    objectives_template JSONB DEFAULT '[]'::jsonb,
    rewards_template JSONB DEFAULT '[]'::jsonb,
    duration_minutes INTEGER, -- Default duration in minutes
    max_participants INTEGER,
    region_restrictions JSONB DEFAULT '[]'::jsonb, -- Allowed regions
    event_data_template JSONB DEFAULT '{}'::jsonb,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: gameplay.event_analytics
-- Analytics and statistics for events
CREATE TABLE IF NOT EXISTS gameplay.event_analytics (
    event_id UUID PRIMARY KEY REFERENCES gameplay.world_events(id) ON DELETE CASCADE,
    total_participants INTEGER NOT NULL DEFAULT 0,
    completed_participants INTEGER NOT NULL DEFAULT 0,
    average_completion_time INTERVAL,
    average_score DECIMAL(10,2),
    participation_rate DECIMAL(5,2), -- Percentage of region population
    satisfaction_rating DECIMAL(3,2), -- Average player rating 0.00-5.00
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Performance indexes for world events
CREATE INDEX IF NOT EXISTS idx_world_events_status_region ON gameplay.world_events(status, region);
CREATE INDEX IF NOT EXISTS idx_world_events_type ON gameplay.world_events(type);
CREATE INDEX IF NOT EXISTS idx_world_events_start_time ON gameplay.world_events(start_time DESC);
CREATE INDEX IF NOT EXISTS idx_world_events_region ON gameplay.world_events(region);
CREATE INDEX IF NOT EXISTS idx_world_events_status ON gameplay.world_events(status) WHERE status = 'ACTIVE';

-- Performance indexes for event participants
CREATE INDEX IF NOT EXISTS idx_event_participants_event_player ON gameplay.event_participants(event_id, player_id);
CREATE INDEX IF NOT EXISTS idx_event_participants_player_status ON gameplay.event_participants(player_id, status);
CREATE INDEX IF NOT EXISTS idx_event_participants_event_status ON gameplay.event_participants(event_id, status);
CREATE INDEX IF NOT EXISTS idx_event_participants_last_activity ON gameplay.event_participants(last_activity DESC);

-- Performance indexes for event rewards
CREATE INDEX IF NOT EXISTS idx_event_rewards_event_player ON gameplay.event_rewards(event_id, player_id);
CREATE INDEX IF NOT EXISTS idx_event_rewards_player_type ON gameplay.event_rewards(player_id, reward_type);
CREATE INDEX IF NOT EXISTS idx_event_rewards_claimed ON gameplay.event_rewards(claimed) WHERE claimed = false;

-- Performance indexes for event templates
CREATE INDEX IF NOT EXISTS idx_event_templates_type_difficulty ON gameplay.event_templates(type, difficulty);
CREATE INDEX IF NOT EXISTS idx_event_templates_active ON gameplay.event_templates(is_active) WHERE is_active = true;

-- GIN indexes for JSONB searches (critical for MMORPG scale)
CREATE INDEX IF NOT EXISTS idx_world_events_objectives_gin ON gameplay.world_events USING GIN (objectives);
CREATE INDEX IF NOT EXISTS idx_world_events_rewards_gin ON gameplay.world_events USING GIN (rewards);
CREATE INDEX IF NOT EXISTS idx_world_events_event_data_gin ON gameplay.world_events USING GIN (event_data);

CREATE INDEX IF NOT EXISTS idx_event_participants_progress_gin ON gameplay.event_participants USING GIN (progress_data);
CREATE INDEX IF NOT EXISTS idx_event_participants_achievements_gin ON gameplay.event_participants USING GIN (achievements);

-- Triggers for automatic timestamp updates
CREATE OR REPLACE FUNCTION update_world_events_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_world_events_updated_at
    BEFORE UPDATE ON gameplay.world_events
    FOR EACH ROW EXECUTE FUNCTION update_world_events_updated_at();

CREATE OR REPLACE FUNCTION update_event_templates_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_event_templates_updated_at
    BEFORE UPDATE ON gameplay.event_templates
    FOR EACH ROW EXECUTE FUNCTION update_event_templates_updated_at();

-- Function to automatically update event participant count
CREATE OR REPLACE FUNCTION update_event_participant_count()
RETURNS TRIGGER AS $$
BEGIN
    -- Update current_participants count
    UPDATE gameplay.world_events
    SET current_participants = (
        SELECT COUNT(*) FROM gameplay.event_participants
        WHERE event_id = COALESCE(NEW.event_id, OLD.event_id)
        AND status IN ('ACTIVE', 'COMPLETED')
    )
    WHERE id = COALESCE(NEW.event_id, OLD.event_id);

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_participant_count
    AFTER INSERT OR UPDATE OR DELETE ON gameplay.event_participants
    FOR EACH ROW EXECUTE FUNCTION update_event_participant_count();

-- Comments for API documentation
COMMENT ON TABLE gameplay.world_events IS 'Dynamic world events for MMORPG gameplay';
COMMENT ON TABLE gameplay.event_participants IS 'Player participation tracking in world events';
COMMENT ON TABLE gameplay.event_rewards IS 'Rewards earned by players in events';
COMMENT ON TABLE gameplay.event_templates IS 'Reusable templates for creating world events';
COMMENT ON TABLE gameplay.event_analytics IS 'Analytics and statistics for event performance';

COMMENT ON COLUMN gameplay.world_events.objectives IS 'JSONB array of event objectives';
COMMENT ON COLUMN gameplay.world_events.rewards IS 'JSONB array of available rewards';
COMMENT ON COLUMN gameplay.world_events.event_data IS 'Additional event-specific configuration data';

COMMENT ON COLUMN gameplay.event_participants.progress_data IS 'Player-specific progress tracking';
COMMENT ON COLUMN gameplay.event_participants.achievements IS 'Achievements earned during event';

-- BACKEND NOTE: Schema optimized for MMORPG scale
-- - GIN indexes on JSONB for fast queries
-- - Partial indexes for common filters
-- - Triggers for automatic data consistency
-- - Struct alignment considered for Go memory layout

COMMIT;
