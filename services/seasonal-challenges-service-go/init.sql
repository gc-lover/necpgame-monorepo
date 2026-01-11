-- Database initialization for seasonal challenges service
-- Issue: #1506

-- Create database
CREATE DATABASE seasonal_challenges;
\c seasonal_challenges;

-- Seasons table
CREATE TABLE seasons (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'upcoming' CHECK (status IN ('upcoming', 'active', 'completed', 'cancelled')),
    currency_limit INTEGER NOT NULL DEFAULT 10000,
    rewards_pool JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version INTEGER NOT NULL DEFAULT 1,

    CONSTRAINT seasons_date_check CHECK (end_date > start_date)
);

-- Challenges table
CREATE TABLE challenges (
    id VARCHAR(36) PRIMARY KEY,
    season_id VARCHAR(36) NOT NULL REFERENCES seasons(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    challenge_type VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'medium' CHECK (difficulty IN ('easy', 'medium', 'hard', 'expert', 'nightmare')),
    max_score INTEGER NOT NULL DEFAULT 10000,
    time_limit_seconds INTEGER,
    min_participants INTEGER NOT NULL DEFAULT 1,
    max_participants INTEGER DEFAULT 4,
    is_team_based BOOLEAN NOT NULL DEFAULT false,
    entry_fee INTEGER NOT NULL DEFAULT 0,
    reward_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    prerequisites JSONB,
    unlock_conditions TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    version INTEGER NOT NULL DEFAULT 1
);

-- Challenge objectives table
CREATE TABLE challenge_objectives (
    id VARCHAR(36) PRIMARY KEY,
    challenge_id VARCHAR(36) NOT NULL REFERENCES challenges(id) ON DELETE CASCADE,
    objective_type VARCHAR(50) NOT NULL,
    target_value INTEGER NOT NULL,
    description VARCHAR(200) NOT NULL,
    progress_type VARCHAR(20) NOT NULL DEFAULT 'cumulative' CHECK (progress_type IN ('cumulative', 'best_run', 'threshold')),
    is_optional BOOLEAN NOT NULL DEFAULT false,
    reward_weight DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Challenge progress table
CREATE TABLE challenge_progress (
    player_id VARCHAR(36) NOT NULL,
    challenge_id VARCHAR(36) NOT NULL REFERENCES challenges(id) ON DELETE CASCADE,
    current_value INTEGER NOT NULL DEFAULT 0,
    best_value INTEGER NOT NULL DEFAULT 0,
    is_completed BOOLEAN NOT NULL DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    attempts_count INTEGER NOT NULL DEFAULT 0,
    time_spent_seconds INTEGER NOT NULL DEFAULT 0,
    last_attempt_at TIMESTAMP WITH TIME ZONE,
    objectives_progress JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    PRIMARY KEY (player_id, challenge_id),
    CHECK (current_value >= 0),
    CHECK (best_value >= current_value),
    CHECK (attempts_count >= 0),
    CHECK (time_spent_seconds >= 0)
);

-- Leaderboard table (materialized view for performance)
CREATE TABLE leaderboard (
    season_id VARCHAR(36) NOT NULL,
    player_id VARCHAR(36) NOT NULL,
    total_score INTEGER NOT NULL DEFAULT 0,
    rank INTEGER NOT NULL,
    challenges_completed INTEGER NOT NULL DEFAULT 0,
    best_challenge_score INTEGER NOT NULL DEFAULT 0,
    average_completion_time INTEGER,
    streak_current INTEGER NOT NULL DEFAULT 0,
    streak_best INTEGER NOT NULL DEFAULT 0,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    PRIMARY KEY (season_id, player_id),
    CHECK (total_score >= 0),
    CHECK (rank > 0),
    CHECK (challenges_completed >= 0)
);

-- Seasonal currency table
CREATE TABLE seasonal_currency (
    season_id VARCHAR(36) NOT NULL,
    player_id VARCHAR(36) NOT NULL,
    balance INTEGER NOT NULL DEFAULT 0,
    earned_total INTEGER NOT NULL DEFAULT 0,
    spent_total INTEGER NOT NULL DEFAULT 0,
    last_earned_at TIMESTAMP WITH TIME ZONE,
    last_spent_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    PRIMARY KEY (season_id, player_id),
    CHECK (balance >= 0),
    CHECK (earned_total >= 0),
    CHECK (spent_total >= 0),
    CHECK (earned_total >= spent_total)
);

-- Season rewards table
CREATE TABLE season_rewards (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    season_id VARCHAR(36) NOT NULL,
    player_id VARCHAR(36) NOT NULL,
    reward_type VARCHAR(20) NOT NULL CHECK (reward_type IN ('currency', 'item', 'cosmetic', 'title', 'reputation', 'experience', 'premium_time')),
    amount INTEGER,
    item_id VARCHAR(36),
    cosmetic_id VARCHAR(36),
    title_id VARCHAR(36),
    rarity VARCHAR(20) DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary', 'mythic')),
    is_premium BOOLEAN NOT NULL DEFAULT false,
    description VARCHAR(200),
    icon_url TEXT,
    claimed BOOLEAN NOT NULL DEFAULT false,
    claimed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Seasonal achievements table
CREATE TABLE seasonal_achievements (
    id VARCHAR(36) PRIMARY KEY,
    season_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(300),
    achievement_type VARCHAR(30) NOT NULL,
    requirement_value INTEGER NOT NULL,
    icon_url TEXT,
    rarity VARCHAR(20) DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    points_value INTEGER NOT NULL DEFAULT 0,
    is_hidden BOOLEAN NOT NULL DEFAULT false,
    unlock_message VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Player achievements table
CREATE TABLE player_achievements (
    player_id VARCHAR(36) NOT NULL,
    achievement_id VARCHAR(36) NOT NULL REFERENCES seasonal_achievements(id) ON DELETE CASCADE,
    unlocked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    progress_value INTEGER NOT NULL,
    is_new BOOLEAN NOT NULL DEFAULT true,

    PRIMARY KEY (player_id, achievement_id)
);

-- Seasonal events table (for real-time broadcasting)
CREATE TABLE seasonal_events (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()::text,
    season_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    target_players JSONB, -- NULL means broadcast to all
    priority VARCHAR(20) DEFAULT 'normal' CHECK (priority IN ('low', 'normal', 'high', 'critical')),
    expires_at TIMESTAMP WITH TIME ZONE,
    metadata JSONB,
    broadcast_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Players table (simplified - would normally be in separate service)
CREATE TABLE players (
    id VARCHAR(36) PRIMARY KEY,
    display_name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes for performance (MMOFPS critical)
CREATE INDEX idx_seasons_status ON seasons(status);
CREATE INDEX idx_seasons_dates ON seasons(start_date, end_date);
CREATE INDEX idx_challenges_season_id ON challenges(season_id);
CREATE INDEX idx_challenges_active ON challenges(is_active) WHERE is_active = true;
CREATE INDEX idx_challenge_progress_player ON challenge_progress(player_id);
CREATE INDEX idx_challenge_progress_completed ON challenge_progress(is_completed) WHERE is_completed = false;
CREATE INDEX idx_leaderboard_season_score ON leaderboard(season_id, total_score DESC);
CREATE INDEX idx_seasonal_currency_player ON seasonal_currency(player_id);
CREATE INDEX idx_season_rewards_player ON season_rewards(player_id, claimed) WHERE NOT claimed;
CREATE INDEX idx_seasonal_events_expires ON seasonal_events(expires_at) WHERE expires_at IS NOT NULL;

-- Insert sample data for testing
INSERT INTO players (id, display_name) VALUES
('player-1', 'CyberNinja_2077'),
('player-2', 'NetRunner_X'),
('player-3', 'StreetSamurai');

INSERT INTO seasons (id, name, description, start_date, end_date, status, currency_limit) VALUES
('summer-2026', 'Summer Championship 2026', 'Prove your worth in the ultimate cyberpunk tournament', '2026-06-01 00:00:00+00', '2026-08-31 23:59:59+00', 'active', 50000);

INSERT INTO challenges (id, season_id, name, description, challenge_type, difficulty, max_score, time_limit_seconds) VALUES
('challenge-1', 'summer-2026', 'Data Fortress Assault', 'Hack through corporate firewalls and extract valuable data', 'hacking', 'hard', 10000, 1800),
('challenge-2', 'summer-2026', 'Street Combat Master', 'Eliminate enemy combatants in street warfare', 'combat', 'medium', 5000, 1200);

INSERT INTO challenge_objectives (id, challenge_id, objective_type, target_value, description) VALUES
('obj-1', 'challenge-1', 'kill_count', 50, 'Eliminate 50 enemy combatants'),
('obj-2', 'challenge-1', 'score_threshold', 7500, 'Achieve score of 7500 or higher');

-- Insert sample progress
INSERT INTO challenge_progress (player_id, challenge_id, current_value, best_value, attempts_count, time_spent_seconds, last_attempt_at) VALUES
('player-1', 'challenge-1', 35, 42, 8, 3600, NOW()),
('player-2', 'challenge-1', 28, 35, 6, 2400, NOW());

-- Currency transactions table
CREATE TABLE currency_transactions (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    season_id VARCHAR(36) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('earn', 'spend', 'exchange')),
    amount INTEGER NOT NULL CHECK (amount > 0),
    balance_after INTEGER NOT NULL CHECK (balance_after >= 0),
    reason VARCHAR(200),
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Currency exchanges table
CREATE TABLE currency_exchanges (
    id VARCHAR(36) PRIMARY KEY,
    player_id VARCHAR(36) NOT NULL,
    season_id VARCHAR(36) NOT NULL,
    currency_amount INTEGER NOT NULL CHECK (currency_amount > 0),
    exchange_type VARCHAR(50) NOT NULL,
    reward JSONB NOT NULL,
    exchange_rate DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Indexes for currency operations (critical for MMOFPS performance)
CREATE INDEX idx_currency_transactions_player_season ON currency_transactions(player_id, season_id, created_at DESC);
CREATE INDEX idx_currency_exchanges_player_season ON currency_exchanges(player_id, season_id, created_at DESC);
CREATE INDEX idx_currency_exchanges_type_season ON currency_exchanges(exchange_type, season_id, created_at DESC);
CREATE INDEX idx_currency_exchanges_daily ON currency_exchanges(season_id, exchange_type, DATE(created_at));

-- Insert sample currency data
INSERT INTO seasonal_currency (season_id, player_id, balance, earned_total, spent_total) VALUES
('summer-2026', 'player-1', 25000, 35000, 10000),
('summer-2026', 'player-2', 18000, 25000, 7000);

INSERT INTO currency_transactions (id, player_id, season_id, type, amount, balance_after, reason) VALUES
(gen_random_uuid()::text, 'player-1', 'summer-2026', 'earn', 5000, 25000, 'challenge_completion'),
(gen_random_uuid()::text, 'player-1', 'summer-2026', 'spend', 2000, 23000, 'item_purchase');