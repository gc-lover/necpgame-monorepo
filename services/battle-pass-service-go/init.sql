-- Battle Pass Service Database Initialization
-- This file contains the initial database schema and sample data

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Seasons table
CREATE TABLE IF NOT EXISTS seasons (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    max_level INTEGER NOT NULL DEFAULT 50,
    status VARCHAR(20) NOT NULL DEFAULT 'upcoming' CHECK (status IN ('upcoming', 'active', 'ended')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Rewards table
CREATE TABLE IF NOT EXISTS rewards (
    id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK (type IN ('cosmetic', 'weapon', 'currency', 'boost', 'title', 'emote')),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    rarity VARCHAR(20) DEFAULT 'common' CHECK (rarity IN ('common', 'rare', 'epic', 'legendary')),
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Season rewards table
CREATE TABLE IF NOT EXISTS season_rewards (
    season_id VARCHAR(36) NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    level INTEGER NOT NULL,
    free_reward_id VARCHAR(36) REFERENCES rewards(id),
    premium_reward_id VARCHAR(36) REFERENCES rewards(id),
    xp_required INTEGER NOT NULL DEFAULT 100,
    PRIMARY KEY (season_id, level)
);

-- Player progress table
CREATE TABLE IF NOT EXISTS player_progress (
    player_id VARCHAR(36) NOT NULL,
    season_id VARCHAR(36) NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    current_level INTEGER NOT NULL DEFAULT 1,
    current_xp INTEGER NOT NULL DEFAULT 0,
    total_xp INTEGER NOT NULL DEFAULT 0,
    xp_to_next_level INTEGER NOT NULL DEFAULT 100,
    has_premium BOOLEAN NOT NULL DEFAULT FALSE,
    last_updated TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (player_id, season_id)
);

-- Claimed rewards table
CREATE TABLE IF NOT EXISTS claimed_rewards (
    id SERIAL PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    season_id VARCHAR(36) NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    level INTEGER NOT NULL,
    tier VARCHAR(10) NOT NULL CHECK (tier IN ('free', 'premium')),
    reward_id VARCHAR(36) NOT NULL REFERENCES rewards(id),
    inventory_id VARCHAR(36),
    claimed_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Premium purchases table
CREATE TABLE IF NOT EXISTS premium_purchases (
    id SERIAL PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    season_id VARCHAR(36) NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    purchased_at TIMESTAMP NOT NULL DEFAULT NOW(),
    price INTEGER NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD'
);

-- XP events table (for analytics)
CREATE TABLE IF NOT EXISTS xp_events (
    id SERIAL PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    amount INTEGER NOT NULL,
    reason VARCHAR(50) NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Claim events table (for analytics)
CREATE TABLE IF NOT EXISTS claim_events (
    id SERIAL PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    reward_id VARCHAR(36) NOT NULL REFERENCES rewards(id),
    level INTEGER NOT NULL,
    tier VARCHAR(10) NOT NULL CHECK (tier IN ('free', 'premium')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_seasons_status ON seasons(status);
CREATE INDEX IF NOT EXISTS idx_player_progress_player_season ON player_progress(player_id, season_id);
CREATE INDEX IF NOT EXISTS idx_claimed_rewards_player_season ON claimed_rewards(player_id, season_id);
CREATE INDEX IF NOT EXISTS idx_xp_events_player ON xp_events(player_id);
CREATE INDEX IF NOT EXISTS idx_xp_events_created_at ON xp_events(created_at);

-- Insert sample data for testing
INSERT INTO rewards (id, type, name, description, rarity) VALUES
('reward_001', 'cosmetic', 'Neon Visor', 'A glowing cyberpunk visor', 'rare'),
('reward_002', 'currency', '100 Credits', 'Digital currency for in-game purchases', 'common'),
('reward_003', 'boost', 'XP Multiplier', '2x XP for 1 hour', 'epic'),
('reward_004', 'title', 'Cyber Champion', 'Exclusive player title', 'legendary'),
('reward_005', 'emote', 'Victory Dance', 'Celebration emote', 'rare')
ON CONFLICT (id) DO NOTHING;

INSERT INTO seasons (id, name, description, start_date, end_date, max_level, status) VALUES
('season_2024_winter', 'Winter Cyberpunk Season', 'Dive into the neon-lit streets of Neo-Tokyo', '2024-12-01 00:00:00', '2025-02-28 23:59:59', 50, 'active')
ON CONFLICT (id) DO NOTHING;

-- Insert sample season rewards
INSERT INTO season_rewards (season_id, level, free_reward_id, premium_reward_id, xp_required) VALUES
('season_2024_winter', 1, 'reward_002', 'reward_001', 100),
('season_2024_winter', 2, 'reward_002', 'reward_005', 200),
('season_2024_winter', 3, 'reward_002', 'reward_003', 300),
('season_2024_winter', 5, 'reward_002', 'reward_004', 500)
ON CONFLICT (season_id, level) DO NOTHING;