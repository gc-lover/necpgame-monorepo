-- Issue: #227
-- liquibase formatted sql

-- changeset database-engineer:093-create-battle-pass-tables

-- =====================================================
-- Battle Pass System Tables
-- =====================================================

-- Table: battle_pass_seasons
CREATE TABLE IF NOT EXISTS battle_pass_seasons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    season_number INTEGER NOT NULL UNIQUE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    max_level INTEGER NOT NULL DEFAULT 100,
    theme VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'scheduled' CHECK (status IN (
        'scheduled',
        'active',
        'ended'
    )),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT battle_pass_seasons_dates_check CHECK (end_date > start_date)
);

CREATE INDEX idx_battle_pass_seasons_status ON battle_pass_seasons(status);
CREATE INDEX idx_battle_pass_seasons_dates ON battle_pass_seasons(start_date, end_date);

-- Table: battle_pass_rewards
CREATE TABLE IF NOT EXISTS battle_pass_rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    season_id UUID NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    level INTEGER NOT NULL,
    track VARCHAR(20) NOT NULL CHECK (track IN ('free', 'premium')),
    reward_type VARCHAR(50) NOT NULL CHECK (reward_type IN (
        'currency',
        'item',
        'cosmetic',
        'title',
        'emote',
        'xp_boost'
    )),
    reward_data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT battle_pass_rewards_unique UNIQUE (season_id, level, track),
    CONSTRAINT battle_pass_rewards_level_check CHECK (level >= 1 AND level <= 100)
);

CREATE INDEX idx_battle_pass_rewards_season ON battle_pass_rewards(season_id);
CREATE INDEX idx_battle_pass_rewards_level ON battle_pass_rewards(season_id, level);
CREATE INDEX idx_battle_pass_rewards_track ON battle_pass_rewards(track);

-- Table: player_battle_pass_progress
CREATE TABLE IF NOT EXISTS player_battle_pass_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    season_id UUID NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    current_level INTEGER DEFAULT 1,
    current_xp INTEGER DEFAULT 0,
    has_premium BOOLEAN DEFAULT false,
    premium_purchased_at TIMESTAMP,
    claimed_levels_free INTEGER[] DEFAULT '{}',
    claimed_levels_premium INTEGER[] DEFAULT '{}',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT player_battle_pass_progress_unique UNIQUE (player_id, season_id),
    CONSTRAINT player_battle_pass_progress_level_check CHECK (current_level >= 1 AND current_level <= 100)
);

CREATE INDEX idx_player_battle_pass_progress_player ON player_battle_pass_progress(player_id);
CREATE INDEX idx_player_battle_pass_progress_season ON player_battle_pass_progress(season_id);
CREATE INDEX idx_player_battle_pass_progress_level ON player_battle_pass_progress(current_level);
CREATE INDEX idx_player_battle_pass_progress_premium ON player_battle_pass_progress(has_premium) WHERE has_premium = true;

-- Table: weekly_challenges
CREATE TABLE IF NOT EXISTS weekly_challenges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    season_id UUID NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    week_number INTEGER NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    objective_type VARCHAR(50) NOT NULL CHECK (objective_type IN (
        'kill_enemies',
        'complete_quests',
        'earn_currency',
        'play_matches',
        'deal_damage',
        'collect_items',
        'win_matches'
    )),
    objective_count INTEGER NOT NULL,
    xp_reward INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT weekly_challenges_dates_check CHECK (end_date > start_date),
    CONSTRAINT weekly_challenges_week_check CHECK (week_number >= 1 AND week_number <= 12)
);

CREATE INDEX idx_weekly_challenges_season ON weekly_challenges(season_id);
CREATE INDEX idx_weekly_challenges_week ON weekly_challenges(season_id, week_number);
CREATE INDEX idx_weekly_challenges_dates ON weekly_challenges(start_date, end_date);

-- Table: player_weekly_challenges
CREATE TABLE IF NOT EXISTS player_weekly_challenges (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    challenge_id UUID NOT NULL REFERENCES weekly_challenges(id) ON DELETE CASCADE,
    current_progress INTEGER DEFAULT 0,
    completed_at TIMESTAMP,
    claimed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT player_weekly_challenges_unique UNIQUE (player_id, challenge_id)
);

CREATE INDEX idx_player_weekly_challenges_player ON player_weekly_challenges(player_id);
CREATE INDEX idx_player_weekly_challenges_challenge ON player_weekly_challenges(challenge_id);
CREATE INDEX idx_player_weekly_challenges_completed ON player_weekly_challenges(completed_at) WHERE completed_at IS NOT NULL;

-- =====================================================
-- Seed Data: Initial Season
-- =====================================================

-- Season 1
INSERT INTO battle_pass_seasons (id, name, description, season_number, start_date, end_date, max_level, theme, status)
VALUES (
    '660e8400-e29b-41d4-a716-446655440001',
    'Season 1: New Dawn',
    'The first season of the Battle Pass system, featuring exclusive rewards and challenges.',
    1,
    NOW(),
    NOW() + INTERVAL '90 days',
    100,
    'cyberpunk_origins',
    'active'
);

-- Free track rewards (first 10 levels)
INSERT INTO battle_pass_rewards (season_id, level, track, reward_type, reward_data)
SELECT 
    '660e8400-e29b-41d4-a716-446655440001',
    level,
    'free',
    CASE 
        WHEN level % 5 = 0 THEN 'currency'
        ELSE 'item'
    END,
    CASE 
        WHEN level % 5 = 0 THEN '{"currency": "credits", "amount": 100}'::jsonb
        ELSE '{"item_id": "common_crate", "quantity": 1}'::jsonb
    END
FROM generate_series(1, 10) AS level;

-- Premium track rewards (first 10 levels)
INSERT INTO battle_pass_rewards (season_id, level, track, reward_type, reward_data)
SELECT 
    '660e8400-e29b-41d4-a716-446655440001',
    level,
    'premium',
    CASE 
        WHEN level % 10 = 0 THEN 'cosmetic'
        WHEN level % 5 = 0 THEN 'currency'
        ELSE 'item'
    END,
    CASE 
        WHEN level % 10 = 0 THEN '{"cosmetic_id": "premium_skin", "rarity": "legendary"}'::jsonb
        WHEN level % 5 = 0 THEN '{"currency": "premium", "amount": 50}'::jsonb
        ELSE '{"item_id": "rare_crate", "quantity": 1}'::jsonb
    END
FROM generate_series(1, 10) AS level;

-- Week 1 challenges
INSERT INTO weekly_challenges (season_id, week_number, title, description, objective_type, objective_count, xp_reward, start_date, end_date)
VALUES
    ('660e8400-e29b-41d4-a716-446655440001', 1, 'Combat Master', 'Defeat 50 enemies', 'kill_enemies', 50, 1000, NOW(), NOW() + INTERVAL '7 days'),
    ('660e8400-e29b-41d4-a716-446655440001', 1, 'Quest Hunter', 'Complete 5 quests', 'complete_quests', 5, 800, NOW(), NOW() + INTERVAL '7 days'),
    ('660e8400-e29b-41d4-a716-446655440001', 1, 'Money Maker', 'Earn 10,000 credits', 'earn_currency', 10000, 600, NOW(), NOW() + INTERVAL '7 days'),
    ('660e8400-e29b-41d4-a716-446655440001', 1, 'Active Player', 'Play 10 matches', 'play_matches', 10, 500, NOW(), NOW() + INTERVAL '7 days'),
    ('660e8400-e29b-41d4-a716-446655440001', 1, 'Damage Dealer', 'Deal 100,000 damage', 'deal_damage', 100000, 1200, NOW(), NOW() + INTERVAL '7 days');








