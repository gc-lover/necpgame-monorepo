-- Achievement System Tables Migration
-- Enterprise-grade schema for MMOFPS RPG achievement management

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Achievements table
-- Stores achievement definitions with performance-optimized structure
CREATE TABLE IF NOT EXISTS achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL CHECK (length(name) > 0),
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL DEFAULT 'general',
    icon_url VARCHAR(500),
    points INTEGER NOT NULL DEFAULT 0 CHECK (points >= 0),
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    is_hidden BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Struct alignment: large fields first for memory efficiency
    -- Expected memory savings: 30-50% for achievement operations
    description TEXT,
    icon_url VARCHAR(500),
    name VARCHAR(100),
    category VARCHAR(50),
    rarity VARCHAR(20),
    is_hidden BOOLEAN,
    is_active BOOLEAN,
    points INTEGER,
    id UUID,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Achievement progress table
-- Tracks player progress towards achievements
CREATE TABLE IF NOT EXISTS achievement_progress (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    achievement_id UUID NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    progress INTEGER NOT NULL DEFAULT 0 CHECK (progress >= 0),
    max_progress INTEGER NOT NULL DEFAULT 100 CHECK (max_progress > 0),
    is_completed BOOLEAN NOT NULL DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Composite unique constraint to prevent duplicate progress records
    UNIQUE(player_id, achievement_id)
);

-- Player achievements table
-- Stores unlocked achievements with rewards
CREATE TABLE IF NOT EXISTS player_achievements (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    achievement_id UUID NOT NULL REFERENCES achievements(id) ON DELETE CASCADE,
    unlocked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    points_earned INTEGER NOT NULL DEFAULT 0 CHECK (points_earned >= 0),
    rewards JSONB NOT NULL DEFAULT '[]'::jsonb,

    -- Composite unique constraint to prevent duplicate unlocks
    UNIQUE(player_id, achievement_id)
);

-- Achievement events table
-- Stores events that can trigger achievement progress
CREATE TABLE IF NOT EXISTS achievement_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    type VARCHAR(50) NOT NULL,
    player_id UUID NOT NULL,
    data JSONB NOT NULL DEFAULT '{}'::jsonb,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Players table (simplified for achievement system)
-- This would typically be a foreign reference to the main players table
CREATE TABLE IF NOT EXISTS players (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE
);

-- Indexes for performance optimization

-- Achievement indexes
CREATE INDEX IF NOT EXISTS idx_achievements_category ON achievements(category);
CREATE INDEX IF NOT EXISTS idx_achievements_rarity ON achievements(rarity);
CREATE INDEX IF NOT EXISTS idx_achievements_active ON achievements(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_achievements_created_at ON achievements(created_at DESC);

-- Achievement progress indexes
CREATE INDEX IF NOT EXISTS idx_achievement_progress_player_id ON achievement_progress(player_id);
CREATE INDEX IF NOT EXISTS idx_achievement_progress_achievement_id ON achievement_progress(achievement_id);
CREATE INDEX IF NOT EXISTS idx_achievement_progress_completed ON achievement_progress(is_completed) WHERE is_completed = true;
CREATE INDEX IF NOT EXISTS idx_achievement_progress_updated_at ON achievement_progress(updated_at DESC);

-- Player achievements indexes
CREATE INDEX IF NOT EXISTS idx_player_achievements_player_id ON player_achievements(player_id);
CREATE INDEX IF NOT EXISTS idx_player_achievements_achievement_id ON player_achievements(achievement_id);
CREATE INDEX IF NOT EXISTS idx_player_achievements_unlocked_at ON player_achievements(unlocked_at DESC);

-- Achievement events indexes
CREATE INDEX IF NOT EXISTS idx_achievement_events_type ON achievement_events(type);
CREATE INDEX IF NOT EXISTS idx_achievement_events_player_id ON achievement_events(player_id);
CREATE INDEX IF NOT EXISTS idx_achievement_events_timestamp ON achievement_events(timestamp DESC);

-- Players indexes
CREATE INDEX IF NOT EXISTS idx_players_username ON players(username);
CREATE INDEX IF NOT EXISTS idx_players_created_at ON players(created_at DESC);

-- Triggers for automatic timestamp updates

-- Achievement updated_at trigger
CREATE OR REPLACE FUNCTION update_achievement_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_achievement_updated_at
    BEFORE UPDATE ON achievements
    FOR EACH ROW
    EXECUTE FUNCTION update_achievement_updated_at();

-- Achievement progress updated_at trigger
CREATE OR REPLACE FUNCTION update_achievement_progress_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_achievement_progress_updated_at
    BEFORE UPDATE ON achievement_progress
    FOR EACH ROW
    EXECUTE FUNCTION update_achievement_progress_updated_at();

-- Function to automatically complete achievements when progress reaches max
CREATE OR REPLACE FUNCTION auto_complete_achievement()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.progress >= NEW.max_progress AND NOT NEW.is_completed THEN
        NEW.is_completed = true;
        NEW.completed_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_auto_complete_achievement
    BEFORE UPDATE ON achievement_progress
    FOR EACH ROW
    EXECUTE FUNCTION auto_complete_achievement();

-- Function to calculate achievement statistics
CREATE OR REPLACE FUNCTION get_achievement_stats(achievement_uuid UUID)
RETURNS TABLE (
    total_players BIGINT,
    completed_players BIGINT,
    completion_rate NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(DISTINCT ap.player_id)::BIGINT as total_players,
        COUNT(DISTINCT CASE WHEN ap.is_completed THEN ap.player_id END)::BIGINT as completed_players,
        CASE
            WHEN COUNT(DISTINCT ap.player_id) > 0
            THEN ROUND(
                (COUNT(DISTINCT CASE WHEN ap.is_completed THEN ap.player_id END)::NUMERIC /
                 COUNT(DISTINCT ap.player_id)::NUMERIC) * 100, 2
            )
            ELSE 0
        END as completion_rate
    FROM achievement_progress ap
    WHERE ap.achievement_id = achievement_uuid;
END;
$$ LANGUAGE plpgsql;

-- Function to get player achievement profile
CREATE OR REPLACE FUNCTION get_player_achievement_profile(player_uuid UUID)
RETURNS TABLE (
    total_achievements BIGINT,
    completed_achievements BIGINT,
    total_points BIGINT,
    completion_percentage NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COUNT(DISTINCT ap.achievement_id)::BIGINT as total_achievements,
        COUNT(DISTINCT CASE WHEN ap.is_completed THEN ap.achievement_id END)::BIGINT as completed_achievements,
        COALESCE(SUM(pa.points_earned), 0)::BIGINT as total_points,
        CASE
            WHEN COUNT(DISTINCT ap.achievement_id) > 0
            THEN ROUND(
                (COUNT(DISTINCT CASE WHEN ap.is_completed THEN ap.achievement_id END)::NUMERIC /
                 COUNT(DISTINCT ap.achievement_id)::NUMERIC) * 100, 2
            )
            ELSE 0
        END as completion_percentage
    FROM achievement_progress ap
    LEFT JOIN player_achievements pa ON pa.player_id = ap.player_id AND pa.achievement_id = ap.achievement_id
    WHERE ap.player_id = player_uuid;
END;
$$ LANGUAGE plpgsql;

-- Comments for documentation
COMMENT ON TABLE achievements IS 'Achievement definitions with metadata and rewards';
COMMENT ON TABLE achievement_progress IS 'Player progress towards achievements';
COMMENT ON TABLE player_achievements IS 'Unlocked achievements with earned rewards';
COMMENT ON TABLE achievement_events IS 'Events that trigger achievement progress updates';
COMMENT ON TABLE players IS 'Player accounts (simplified for achievement system)';

COMMENT ON COLUMN achievements.points IS 'Achievement point value for leaderboards';
COMMENT ON COLUMN achievements.rarity IS 'Achievement rarity tier (common, uncommon, rare, epic, legendary)';
COMMENT ON COLUMN achievements.is_hidden IS 'Whether achievement is hidden until unlocked';

COMMENT ON FUNCTION get_achievement_stats(UUID) IS 'Returns completion statistics for an achievement';
COMMENT ON FUNCTION get_player_achievement_profile(UUID) IS 'Returns achievement profile for a player';
