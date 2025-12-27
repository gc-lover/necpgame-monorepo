-- Issue: #2262 - Cyberspace Easter Eggs Database Schema
-- liquibase formatted sql

--changeset backend:cyberspace-easter-eggs-schema dbms:postgresql
--comment: Create complete schema for cyberspace easter eggs system

BEGIN;

-- Create easter_eggs table
CREATE TABLE IF NOT EXISTS easter_eggs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('technology', 'cultural', 'historical', 'humorous')),
    difficulty VARCHAR(20) NOT NULL CHECK (difficulty IN ('easy', 'medium', 'hard', 'legendary')),
    description TEXT,
    content TEXT,
    location JSONB NOT NULL,
    discovery_method JSONB NOT NULL,
    rewards JSONB DEFAULT '[]'::jsonb,
    lore_connections JSONB DEFAULT '[]'::jsonb,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'disabled', 'maintenance')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create player_easter_egg_progress table
CREATE TABLE IF NOT EXISTS player_easter_egg_progress (
    player_id VARCHAR(100) NOT NULL,
    easter_egg_id VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'undiscovered' CHECK (status IN ('undiscovered', 'discovered', 'completed', 'revisited')),
    discovered_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    rewards_claimed JSONB DEFAULT '[]'::jsonb,
    hint_level INTEGER NOT NULL DEFAULT 0 CHECK (hint_level >= 0 AND hint_level <= 3),
    visit_count INTEGER NOT NULL DEFAULT 0,
    last_visited TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (player_id, easter_egg_id),
    FOREIGN KEY (easter_egg_id) REFERENCES easter_eggs(id) ON DELETE CASCADE
);

-- Create easter_egg_discovery_attempts table
CREATE TABLE IF NOT EXISTS easter_egg_discovery_attempts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id VARCHAR(100) NOT NULL,
    easter_egg_id VARCHAR(100) NOT NULL,
    attempt_type VARCHAR(50) NOT NULL,
    attempt_data JSONB,
    success BOOLEAN NOT NULL DEFAULT FALSE,
    attempted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    response_time INTEGER, -- milliseconds
    ip_address INET,
    user_agent TEXT,
    FOREIGN KEY (easter_egg_id) REFERENCES easter_eggs(id) ON DELETE CASCADE
);

-- Create easter_egg_statistics table
CREATE TABLE IF NOT EXISTS easter_egg_statistics (
    easter_egg_id VARCHAR(100) PRIMARY KEY,
    total_discoveries INTEGER NOT NULL DEFAULT 0,
    unique_players INTEGER NOT NULL DEFAULT 0,
    average_discovery_time INTEGER, -- seconds
    success_rate DECIMAL(3,2) CHECK (success_rate >= 0 AND success_rate <= 1),
    popular_discovery_method VARCHAR(100),
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (easter_egg_id) REFERENCES easter_eggs(id) ON DELETE CASCADE
);

-- Create discovery_hints table
CREATE TABLE IF NOT EXISTS discovery_hints (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    easter_egg_id VARCHAR(100) NOT NULL,
    hint_level INTEGER NOT NULL CHECK (hint_level >= 1 AND hint_level <= 3),
    hint_text TEXT NOT NULL,
    hint_type VARCHAR(20) NOT NULL DEFAULT 'direct' CHECK (hint_type IN ('direct', 'indirect', 'misleading')),
    cost INTEGER NOT NULL DEFAULT 0,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (easter_egg_id) REFERENCES easter_eggs(id) ON DELETE CASCADE
);

-- Create easter_egg_events table
CREATE TABLE IF NOT EXISTS easter_egg_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(50) NOT NULL,
    player_id VARCHAR(100) NOT NULL,
    easter_egg_id VARCHAR(100) NOT NULL,
    event_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (easter_egg_id) REFERENCES easter_eggs(id) ON DELETE CASCADE
);

-- Create player_easter_egg_profiles table
CREATE TABLE IF NOT EXISTS player_easter_egg_profiles (
    player_id VARCHAR(100) PRIMARY KEY,
    total_discovered INTEGER NOT NULL DEFAULT 0,
    total_completed INTEGER NOT NULL DEFAULT 0,
    favorite_category VARCHAR(50),
    average_difficulty DECIMAL(3,2),
    total_hints_used INTEGER NOT NULL DEFAULT 0,
    collection_progress DECIMAL(5,2) CHECK (collection_progress >= 0 AND collection_progress <= 100),
    achievement_level VARCHAR(20) NOT NULL DEFAULT 'explorer' CHECK (achievement_level IN ('explorer', 'hunter', 'master', 'legend')),
    last_activity TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create easter_egg_challenges table
CREATE TABLE IF NOT EXISTS easter_egg_challenges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    easter_eggs JSONB NOT NULL DEFAULT '[]'::jsonb,
    rewards JSONB DEFAULT '[]'::jsonb,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create player_challenge_progress table
CREATE TABLE IF NOT EXISTS player_challenge_progress (
    player_id VARCHAR(100) NOT NULL,
    challenge_id UUID NOT NULL,
    progress INTEGER NOT NULL DEFAULT 0,
    completed_at TIMESTAMP WITH TIME ZONE,
    rewards_claimed JSONB DEFAULT '[]'::jsonb,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (player_id, challenge_id),
    FOREIGN KEY (challenge_id) REFERENCES easter_egg_challenges(id) ON DELETE CASCADE
);

-- Create indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_easter_eggs_category ON easter_eggs(category);
CREATE INDEX IF NOT EXISTS idx_easter_eggs_difficulty ON easter_eggs(difficulty);
CREATE INDEX IF NOT EXISTS idx_easter_eggs_status ON easter_eggs(status);
CREATE INDEX IF NOT EXISTS idx_easter_eggs_created_at ON easter_eggs(created_at DESC);

CREATE INDEX IF NOT EXISTS idx_player_progress_player_id ON player_easter_egg_progress(player_id);
CREATE INDEX IF NOT EXISTS idx_player_progress_status ON player_easter_egg_progress(status);
CREATE INDEX IF NOT EXISTS idx_player_progress_last_visited ON player_easter_egg_progress(last_visited DESC);

CREATE INDEX IF NOT EXISTS idx_discovery_attempts_player_egg ON easter_egg_discovery_attempts(player_id, easter_egg_id);
CREATE INDEX IF NOT EXISTS idx_discovery_attempts_attempted_at ON easter_egg_discovery_attempts(attempted_at DESC);
CREATE INDEX IF NOT EXISTS idx_discovery_attempts_success ON easter_egg_discovery_attempts(success);

CREATE INDEX IF NOT EXISTS idx_discovery_hints_egg_level ON discovery_hints(easter_egg_id, hint_level);
CREATE INDEX IF NOT EXISTS idx_discovery_hints_enabled ON discovery_hints(is_enabled) WHERE is_enabled = true;

CREATE INDEX IF NOT EXISTS idx_easter_egg_events_type ON easter_egg_events(event_type);
CREATE INDEX IF NOT EXISTS idx_easter_egg_events_player ON easter_egg_events(player_id);
CREATE INDEX IF NOT EXISTS idx_easter_egg_events_created_at ON easter_egg_events(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_easter_egg_events_processed ON easter_egg_events(processed) WHERE processed = false;

CREATE INDEX IF NOT EXISTS idx_player_profiles_achievement ON player_easter_egg_profiles(achievement_level);
CREATE INDEX IF NOT EXISTS idx_player_profiles_last_activity ON player_easter_egg_profiles(last_activity DESC);

CREATE INDEX IF NOT EXISTS idx_challenges_active ON easter_egg_challenges(is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_challenges_time_range ON easter_egg_challenges(start_time, end_time);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_easter_eggs_updated_at
    BEFORE UPDATE ON easter_eggs
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_player_profiles_updated_at
    BEFORE UPDATE ON player_easter_egg_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_player_challenge_progress_updated_at
    BEFORE UPDATE ON player_challenge_progress
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_easter_eggs_rewards_gin ON easter_eggs USING GIN (rewards);
CREATE INDEX IF NOT EXISTS idx_easter_eggs_lore_connections_gin ON easter_eggs USING GIN (lore_connections);
CREATE INDEX IF NOT EXISTS idx_player_progress_rewards_claimed_gin ON player_easter_egg_progress USING GIN (rewards_claimed);
CREATE INDEX IF NOT EXISTS idx_discovery_attempts_data_gin ON easter_egg_discovery_attempts USING GIN (attempt_data);
CREATE INDEX IF NOT EXISTS idx_easter_egg_events_data_gin ON easter_egg_events USING GIN (event_data);

COMMIT;
