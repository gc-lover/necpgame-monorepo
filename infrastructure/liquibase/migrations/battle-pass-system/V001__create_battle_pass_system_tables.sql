-- Battle Pass System Database Schema
-- Version: V001
-- Description: Complete database schema for Battle Pass System with seasons, progression, challenges, and rewards

-- =================================================================================================
-- SEASON MANAGEMENT TABLES
-- =================================================================================================

-- Battle Pass seasons
CREATE TABLE battle_pass_seasons (
    id BIGSERIAL PRIMARY KEY,
    season_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    season_type VARCHAR(20) NOT NULL DEFAULT 'REGULAR', -- 'REGULAR', 'EVENT', 'LIMITED', 'PERMANENT'
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT', -- 'DRAFT', 'PREPARATION', 'ACTIVE', 'ENDING', 'COMPLETED', 'ARCHIVED'
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    max_level INTEGER NOT NULL DEFAULT 100,
    base_xp_per_level BIGINT NOT NULL DEFAULT 1000,
    xp_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Season configuration
CREATE TABLE battle_pass_season_config (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    config_key VARCHAR(100) NOT NULL,
    config_value JSONB NOT NULL DEFAULT '{}',
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(season_id, config_key)
);

-- =================================================================================================
-- PROGRESSION TRACK TABLES
-- =================================================================================================

-- Progression tracks (Free, Premium, Ultimate)
CREATE TABLE battle_pass_tracks (
    id BIGSERIAL PRIMARY KEY,
    track_key VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    track_type VARCHAR(20) NOT NULL, -- 'FREE', 'PREMIUM', 'ULTIMATE'
    price_cents INTEGER, -- NULL for free tracks
    currency VARCHAR(10) DEFAULT 'USD',
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Season-track associations
CREATE TABLE battle_pass_season_tracks (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    track_id BIGINT NOT NULL REFERENCES battle_pass_tracks(id) ON DELETE CASCADE,
    is_default BOOLEAN NOT NULL DEFAULT false,
    unlock_requirements JSONB NOT NULL DEFAULT '{}', -- requirements to unlock this track
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(season_id, track_id)
);

-- =================================================================================================
-- LEVEL AND REWARD TABLES
-- =================================================================================================

-- Battle Pass levels
CREATE TABLE battle_pass_levels (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    track_id BIGINT NOT NULL REFERENCES battle_pass_tracks(id) ON DELETE CASCADE,
    level INTEGER NOT NULL,
    xp_required BIGINT NOT NULL,
    reward_data JSONB NOT NULL DEFAULT '{}', -- reward details
    bonus_reward_data JSONB, -- premium track bonus rewards
    is_premium_locked BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(season_id, track_id, level)
);

-- Reward catalog
CREATE TABLE battle_pass_rewards (
    id BIGSERIAL PRIMARY KEY,
    reward_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    reward_type VARCHAR(20) NOT NULL, -- 'COSMETICS', 'CURRENCY', 'ITEMS', 'BOOSTERS', 'TITLES', 'EXCLUSIVE'
    reward_category VARCHAR(50), -- 'WEAPON', 'ARMOR', 'VEHICLE', 'PET', 'EMOTE', etc.
    rarity VARCHAR(20) NOT NULL DEFAULT 'common', -- 'common', 'uncommon', 'rare', 'epic', 'legendary'
    value_data JSONB NOT NULL DEFAULT '{}', -- specific reward values/items
    icon_url VARCHAR(500),
    is_stackable BOOLEAN NOT NULL DEFAULT false,
    max_stack INTEGER DEFAULT 1,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Level-reward associations
CREATE TABLE battle_pass_level_rewards (
    id BIGSERIAL PRIMARY KEY,
    level_id BIGINT NOT NULL REFERENCES battle_pass_levels(id) ON DELETE CASCADE,
    reward_id BIGINT NOT NULL REFERENCES battle_pass_rewards(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    is_guaranteed BOOLEAN NOT NULL DEFAULT true,
    drop_chance DECIMAL(5,2), -- NULL for guaranteed rewards
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(level_id, reward_id)
);

-- =================================================================================================
-- PLAYER PROGRESSION TABLES
-- =================================================================================================

-- Player battle pass enrollment
CREATE TABLE battle_pass_player_enrollment (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL, -- Reference to player service
    season_id BIGINT NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    track_id BIGINT NOT NULL REFERENCES battle_pass_tracks(id),
    enrolled_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    purchase_date TIMESTAMP WITH TIME ZONE, -- NULL for free track
    expiration_date TIMESTAMP WITH TIME ZONE, -- NULL for lifetime
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_id, season_id)
);

-- Player progression tracking
CREATE TABLE battle_pass_player_progress (
    id BIGSERIAL PRIMARY KEY,
    player_enrollment_id BIGINT NOT NULL REFERENCES battle_pass_player_enrollment(id) ON DELETE CASCADE,
    current_level INTEGER NOT NULL DEFAULT 1,
    current_xp BIGINT NOT NULL DEFAULT 0,
    total_xp_earned BIGINT NOT NULL DEFAULT 0,
    xp_to_next_level BIGINT NOT NULL,
    completed_levels INTEGER NOT NULL DEFAULT 0,
    last_progress_update TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_enrollment_id)
);

-- Player XP transactions
CREATE TABLE battle_pass_xp_transactions (
    id BIGSERIAL PRIMARY KEY,
    player_enrollment_id BIGINT NOT NULL REFERENCES battle_pass_player_enrollment(id) ON DELETE CASCADE,
    xp_amount BIGINT NOT NULL,
    xp_source VARCHAR(50) NOT NULL, -- 'QUEST_COMPLETION', 'COMBAT_VICTORIES', etc.
    source_reference_id BIGINT, -- reference to source event
    transaction_data JSONB NOT NULL DEFAULT '{}',
    granted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Claimed rewards tracking
CREATE TABLE battle_pass_claimed_rewards (
    id BIGSERIAL PRIMARY KEY,
    player_enrollment_id BIGINT NOT NULL REFERENCES battle_pass_player_enrollment(id) ON DELETE CASCADE,
    level_id BIGINT NOT NULL REFERENCES battle_pass_levels(id),
    reward_id BIGINT NOT NULL REFERENCES battle_pass_rewards(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    claimed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'DELIVERED', 'FAILED'
    delivery_reference_id BIGINT, -- reference to inventory/economy transaction
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_enrollment_id, level_id, reward_id)
);

-- =================================================================================================
-- CHALLENGES SYSTEM TABLES
-- =================================================================================================

-- Challenge templates
CREATE TABLE battle_pass_challenges (
    id BIGSERIAL PRIMARY KEY,
    challenge_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    challenge_type VARCHAR(20) NOT NULL, -- 'DAILY', 'WEEKLY', 'SEASONAL', 'LIMITED_TIME', 'PERSONAL'
    challenge_category VARCHAR(30) NOT NULL, -- 'COMBAT', 'SOCIAL', 'PROGRESSION', 'COLLECTION', 'EXPLORATION'
    target_value INTEGER NOT NULL,
    current_value INTEGER NOT NULL DEFAULT 0,
    reward_xp BIGINT NOT NULL DEFAULT 0,
    reward_data JSONB NOT NULL DEFAULT '{}',
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    max_completions INTEGER DEFAULT 1,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Season-challenge associations
CREATE TABLE battle_pass_season_challenges (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    challenge_id BIGINT NOT NULL REFERENCES battle_pass_challenges(id) ON DELETE CASCADE,
    is_required BOOLEAN NOT NULL DEFAULT false,
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(season_id, challenge_id)
);

-- Player challenge progress
CREATE TABLE battle_pass_player_challenges (
    id BIGSERIAL PRIMARY KEY,
    player_enrollment_id BIGINT NOT NULL REFERENCES battle_pass_player_enrollment(id) ON DELETE CASCADE,
    challenge_id BIGINT NOT NULL REFERENCES battle_pass_challenges(id) ON DELETE CASCADE,
    current_progress INTEGER NOT NULL DEFAULT 0,
    is_completed BOOLEAN NOT NULL DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    times_completed INTEGER NOT NULL DEFAULT 0,
    last_progress_update TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_enrollment_id, challenge_id)
);

-- Challenge progress events
CREATE TABLE battle_pass_challenge_progress (
    id BIGSERIAL PRIMARY KEY,
    player_challenge_id BIGINT NOT NULL REFERENCES battle_pass_player_challenges(id) ON DELETE CASCADE,
    progress_amount INTEGER NOT NULL,
    progress_source VARCHAR(50) NOT NULL,
    source_reference_id BIGINT,
    progress_data JSONB NOT NULL DEFAULT '{}',
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- PREMIUM SYSTEM TABLES
-- =================================================================================================

-- Premium subscription tiers
CREATE TABLE battle_pass_premium_tiers (
    id BIGSERIAL PRIMARY KEY,
    tier_key VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price_cents INTEGER NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'USD',
    duration_days INTEGER NOT NULL, -- NULL for lifetime
    features JSONB NOT NULL DEFAULT '{}', -- premium features included
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Player premium subscriptions
CREATE TABLE battle_pass_player_subscriptions (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    premium_tier_id BIGINT NOT NULL REFERENCES battle_pass_premium_tiers(id),
    season_id BIGINT REFERENCES battle_pass_seasons(id), -- NULL for all seasons
    purchase_date TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expiration_date TIMESTAMP WITH TIME ZONE,
    payment_reference_id BIGINT, -- reference to payment system
    is_active BOOLEAN NOT NULL DEFAULT true,
    auto_renew BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Premium discounts and promotions
CREATE TABLE battle_pass_premium_promotions (
    id BIGSERIAL PRIMARY KEY,
    promotion_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    discount_percentage DECIMAL(5,2) NOT NULL,
    discount_amount_cents INTEGER,
    applicable_tiers JSONB NOT NULL DEFAULT '[]', -- list of applicable tier keys
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    usage_limit INTEGER, -- NULL for unlimited
    times_used INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- ANALYTICS AND TELEMETRY TABLES
-- =================================================================================================

-- Battle Pass analytics events
CREATE TABLE battle_pass_analytics_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL, -- 'LEVEL_UP', 'REWARD_CLAIMED', 'CHALLENGE_COMPLETED', etc.
    player_id BIGINT NOT NULL,
    season_id BIGINT REFERENCES battle_pass_seasons(id),
    event_data JSONB NOT NULL DEFAULT '{}',
    session_id VARCHAR(100),
    client_version VARCHAR(50),
    platform VARCHAR(20),
    region VARCHAR(10),
    event_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Daily aggregated statistics
CREATE TABLE battle_pass_daily_stats (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    season_id BIGINT REFERENCES battle_pass_seasons(id),
    total_players BIGINT NOT NULL DEFAULT 0,
    active_players BIGINT NOT NULL DEFAULT 0,
    premium_players BIGINT NOT NULL DEFAULT 0,
    average_level DECIMAL(5,2) DEFAULT 0,
    total_xp_earned BIGINT NOT NULL DEFAULT 0,
    rewards_claimed BIGINT NOT NULL DEFAULT 0,
    challenges_completed BIGINT NOT NULL DEFAULT 0,
    revenue_cents BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(date, season_id)
);

-- =================================================================================================
-- NOTIFICATIONS TABLES
-- =================================================================================================

-- Player notification preferences
CREATE TABLE battle_pass_notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,
    level_up_notifications BOOLEAN NOT NULL DEFAULT true,
    reward_available_notifications BOOLEAN NOT NULL DEFAULT true,
    challenge_completed_notifications BOOLEAN NOT NULL DEFAULT true,
    season_ending_notifications BOOLEAN NOT NULL DEFAULT true,
    premium_offers_notifications BOOLEAN NOT NULL DEFAULT true,
    marketing_notifications BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Scheduled notifications
CREATE TABLE battle_pass_scheduled_notifications (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    data JSONB NOT NULL DEFAULT '{}',
    scheduled_for TIMESTAMP WITH TIME ZONE NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE,
    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'SENT', 'DELIVERED', 'FAILED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- INDEXES
-- =================================================================================================

-- Season indexes
CREATE INDEX idx_battle_pass_seasons_status ON battle_pass_seasons(status);
CREATE INDEX idx_battle_pass_seasons_dates ON battle_pass_seasons(start_date, end_date);
CREATE INDEX idx_battle_pass_seasons_active ON battle_pass_seasons(is_active) WHERE is_active = true;

-- Player enrollment indexes
CREATE INDEX idx_battle_pass_player_enrollment_player ON battle_pass_player_enrollment(player_id);
CREATE INDEX idx_battle_pass_player_enrollment_season ON battle_pass_player_enrollment(season_id);
CREATE INDEX idx_battle_pass_player_enrollment_active ON battle_pass_player_enrollment(is_active) WHERE is_active = true;

-- Progress indexes
CREATE INDEX idx_battle_pass_player_progress_enrollment ON battle_pass_player_progress(player_enrollment_id);
CREATE INDEX idx_battle_pass_player_progress_level ON battle_pass_player_progress(current_level);

-- XP transactions indexes
CREATE INDEX idx_battle_pass_xp_transactions_enrollment ON battle_pass_xp_transactions(player_enrollment_id);
CREATE INDEX idx_battle_pass_xp_transactions_source ON battle_pass_xp_transactions(xp_source);
CREATE INDEX idx_battle_pass_xp_transactions_time ON battle_pass_xp_transactions(granted_at DESC);

-- Rewards indexes
CREATE INDEX idx_battle_pass_claimed_rewards_enrollment ON battle_pass_claimed_rewards(player_enrollment_id);
CREATE INDEX idx_battle_pass_claimed_rewards_level ON battle_pass_claimed_rewards(level_id);
CREATE INDEX idx_battle_pass_claimed_rewards_status ON battle_pass_claimed_rewards(delivery_status);

-- Challenges indexes
CREATE INDEX idx_battle_pass_player_challenges_enrollment ON battle_pass_player_challenges(player_enrollment_id);
CREATE INDEX idx_battle_pass_player_challenges_completed ON battle_pass_player_challenges(is_completed) WHERE is_completed = true;
CREATE INDEX idx_battle_pass_challenges_active ON battle_pass_challenges(is_active) WHERE is_active = true;
CREATE INDEX idx_battle_pass_challenges_dates ON battle_pass_challenges(start_date, end_date);

-- Premium indexes
CREATE INDEX idx_battle_pass_player_subscriptions_player ON battle_pass_player_subscriptions(player_id);
CREATE INDEX idx_battle_pass_player_subscriptions_active ON battle_pass_player_subscriptions(is_active) WHERE is_active = true;
CREATE INDEX idx_battle_pass_player_subscriptions_expiration ON battle_pass_player_subscriptions(expiration_date);

-- Analytics indexes
CREATE INDEX idx_battle_pass_analytics_events_player ON battle_pass_analytics_events(player_id);
CREATE INDEX idx_battle_pass_analytics_events_type ON battle_pass_analytics_events(event_type);
CREATE INDEX idx_battle_pass_analytics_events_time ON battle_pass_analytics_events(event_timestamp DESC);

-- Notifications indexes
CREATE INDEX idx_battle_pass_scheduled_notifications_player ON battle_pass_scheduled_notifications(player_id);
CREATE INDEX idx_battle_pass_scheduled_notifications_scheduled ON battle_pass_scheduled_notifications(scheduled_for);
CREATE INDEX idx_battle_pass_scheduled_notifications_status ON battle_pass_scheduled_notifications(delivery_status);

-- =================================================================================================
-- CONSTRAINTS
-- =================================================================================================

-- Check constraints
ALTER TABLE battle_pass_seasons ADD CONSTRAINT chk_season_type CHECK (season_type IN ('REGULAR', 'EVENT', 'LIMITED', 'PERMANENT'));
ALTER TABLE battle_pass_seasons ADD CONSTRAINT chk_season_status CHECK (status IN ('DRAFT', 'PREPARATION', 'ACTIVE', 'ENDING', 'COMPLETED', 'ARCHIVED'));
ALTER TABLE battle_pass_tracks ADD CONSTRAINT chk_track_type CHECK (track_type IN ('FREE', 'PREMIUM', 'ULTIMATE'));
ALTER TABLE battle_pass_rewards ADD CONSTRAINT chk_reward_type CHECK (reward_type IN ('COSMETICS', 'CURRENCY', 'ITEMS', 'BOOSTERS', 'TITLES', 'EXCLUSIVE'));
ALTER TABLE battle_pass_rewards ADD CONSTRAINT chk_reward_rarity CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary'));
ALTER TABLE battle_pass_challenges ADD CONSTRAINT chk_challenge_type CHECK (challenge_type IN ('DAILY', 'WEEKLY', 'SEASONAL', 'LIMITED_TIME', 'PERSONAL'));
ALTER TABLE battle_pass_challenges ADD CONSTRAINT chk_challenge_category CHECK (challenge_category IN ('COMBAT', 'SOCIAL', 'PROGRESSION', 'COLLECTION', 'EXPLORATION'));
ALTER TABLE battle_pass_analytics_events ADD CONSTRAINT chk_event_type CHECK (event_type IN ('LEVEL_UP', 'REWARD_CLAIMED', 'CHALLENGE_COMPLETED', 'PURCHASE', 'SEASON_START', 'SEASON_END'));
ALTER TABLE battle_pass_scheduled_notifications ADD CONSTRAINT chk_delivery_status CHECK (delivery_status IN ('PENDING', 'SENT', 'DELIVERED', 'FAILED'));

-- =================================================================================================
-- TRIGGERS
-- =================================================================================================

-- Function to update updated_at timestamps
CREATE OR REPLACE FUNCTION update_battle_pass_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers to relevant tables
CREATE TRIGGER update_battle_pass_seasons_updated_at BEFORE UPDATE ON battle_pass_seasons FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_tracks_updated_at BEFORE UPDATE ON battle_pass_tracks FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_rewards_updated_at BEFORE UPDATE ON battle_pass_rewards FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_player_enrollment_updated_at BEFORE UPDATE ON battle_pass_player_enrollment FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_player_progress_updated_at BEFORE UPDATE ON battle_pass_player_progress FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_challenges_updated_at BEFORE UPDATE ON battle_pass_challenges FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_premium_tiers_updated_at BEFORE UPDATE ON battle_pass_premium_tiers FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_player_subscriptions_updated_at BEFORE UPDATE ON battle_pass_player_subscriptions FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
CREATE TRIGGER update_battle_pass_notification_preferences_updated_at BEFORE UPDATE ON battle_pass_notification_preferences FOR EACH ROW EXECUTE FUNCTION update_battle_pass_updated_at_column();
