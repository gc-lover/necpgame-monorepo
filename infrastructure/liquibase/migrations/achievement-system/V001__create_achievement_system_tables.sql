-- Achievement System Database Schema
-- Version: V001
-- Description: Complete database schema for Achievement System with definitions, progress tracking, rewards, and analytics

-- =================================================================================================
-- ACHIEVEMENT DEFINITIONS TABLES
-- =================================================================================================

-- Achievement definitions catalog
CREATE TABLE achievement_definitions (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    title JSONB NOT NULL, -- Multi-language support: {"en": "Title", "ru": "Заголовок"}
    description JSONB NOT NULL, -- Multi-language descriptions
    category VARCHAR(30) NOT NULL, -- 'COMBAT', 'SOCIAL', 'ECONOMY', 'EXPLORATION', 'SPECIAL', 'SEASONAL', 'GUILD'
    difficulty VARCHAR(15) NOT NULL, -- 'EASY', 'MEDIUM', 'HARD', 'LEGENDARY'
    achievement_type VARCHAR(20) NOT NULL DEFAULT 'STANDARD', -- 'STANDARD', 'PROGRESSIVE', 'TIME_LIMITED', 'HIDDEN', 'COLLECTION', 'CHAINED'
    is_hidden BOOLEAN NOT NULL DEFAULT false,
    is_repeatable BOOLEAN NOT NULL DEFAULT false,
    max_progress INTEGER NOT NULL DEFAULT 1,
    conditions JSONB NOT NULL DEFAULT '{}', -- Achievement completion conditions
    rewards JSONB NOT NULL DEFAULT '{}', -- Reward specifications
    prerequisites JSONB, -- Prerequisites for unlocking
    chain_next_id BIGINT REFERENCES achievement_definitions(id), -- For chained achievements
    sort_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Achievement categories for organization
CREATE TABLE achievement_categories (
    id BIGSERIAL PRIMARY KEY,
    category_key VARCHAR(30) UNIQUE NOT NULL,
    name JSONB NOT NULL,
    description JSONB,
    icon_url VARCHAR(500),
    color_code VARCHAR(7), -- Hex color code
    sort_order INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Achievement tags for advanced filtering
CREATE TABLE achievement_tags (
    id BIGSERIAL PRIMARY KEY,
    tag_key VARCHAR(50) UNIQUE NOT NULL,
    name JSONB NOT NULL,
    description JSONB,
    color_code VARCHAR(7),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Achievement-tag associations
CREATE TABLE achievement_definition_tags (
    id BIGSERIAL PRIMARY KEY,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES achievement_tags(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(achievement_id, tag_id)
);

-- =================================================================================================
-- PLAYER ACHIEVEMENT PROGRESS TABLES
-- =================================================================================================

-- Player achievements status
CREATE TABLE player_achievements (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'LOCKED', -- 'LOCKED', 'UNLOCKED', 'IN_PROGRESS', 'COMPLETED', 'CLAIMED'
    unlocked_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    claimed_at TIMESTAMP WITH TIME ZONE,
    completion_count INTEGER NOT NULL DEFAULT 0, -- For repeatable achievements
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_id, achievement_id)
);

-- Achievement progress tracking
CREATE TABLE achievement_progress (
    id BIGSERIAL PRIMARY KEY,
    player_achievement_id BIGINT NOT NULL REFERENCES player_achievements(id) ON DELETE CASCADE,
    progress_key VARCHAR(100) NOT NULL, -- Specific progress metric (kills, quests, etc.)
    current_value INTEGER NOT NULL DEFAULT 0,
    target_value INTEGER NOT NULL,
    progress_percentage DECIMAL(5,2) GENERATED ALWAYS AS (
        CASE WHEN target_value > 0 THEN (current_value::DECIMAL / target_value) * 100 ELSE 0 END
    ) STORED,
    is_completed BOOLEAN NOT NULL DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(player_achievement_id, progress_key)
);

-- Achievement progress events (for detailed tracking)
CREATE TABLE achievement_progress_events (
    id BIGSERIAL PRIMARY KEY,
    player_achievement_id BIGINT NOT NULL REFERENCES player_achievements(id) ON DELETE CASCADE,
    progress_key VARCHAR(100) NOT NULL,
    progress_change INTEGER NOT NULL, -- Amount added/subtracted
    event_type VARCHAR(30) NOT NULL, -- 'GAMEPLAY', 'QUEST', 'COMBAT', 'SOCIAL', etc.
    event_reference_id BIGINT, -- Reference to source event
    event_data JSONB NOT NULL DEFAULT '{}',
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- REWARD SYSTEM TABLES
-- =================================================================================================

-- Reward templates
CREATE TABLE achievement_rewards (
    id BIGSERIAL PRIMARY KEY,
    reward_key VARCHAR(100) UNIQUE NOT NULL,
    name JSONB NOT NULL,
    description JSONB,
    reward_type VARCHAR(20) NOT NULL, -- 'CURRENCY', 'ITEM', 'COSMETIC', 'TITLE', 'BOOSTER', 'UNLOCK', 'EXCLUSIVE'
    reward_category VARCHAR(30), -- 'WEAPON', 'ARMOR', 'VEHICLE', 'PET', etc.
    value_data JSONB NOT NULL DEFAULT '{}', -- Specific reward values
    icon_url VARCHAR(500),
    rarity VARCHAR(15) NOT NULL DEFAULT 'common', -- 'common', 'uncommon', 'rare', 'epic', 'legendary'
    is_stackable BOOLEAN NOT NULL DEFAULT false,
    max_stack INTEGER DEFAULT 1,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Achievement-reward associations
CREATE TABLE achievement_definition_rewards (
    id BIGSERIAL PRIMARY KEY,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    reward_id BIGINT NOT NULL REFERENCES achievement_rewards(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 1,
    is_guaranteed BOOLEAN NOT NULL DEFAULT true,
    drop_chance DECIMAL(5,2), -- NULL for guaranteed rewards
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(achievement_id, reward_id)
);

-- Claimed rewards tracking
CREATE TABLE achievement_claimed_rewards (
    id BIGSERIAL PRIMARY KEY,
    player_achievement_id BIGINT NOT NULL REFERENCES player_achievements(id) ON DELETE CASCADE,
    reward_id BIGINT NOT NULL REFERENCES achievement_rewards(id),
    quantity INTEGER NOT NULL DEFAULT 1,
    claimed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    delivery_status VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- 'PENDING', 'DELIVERED', 'FAILED'
    delivery_reference_id BIGINT, -- Reference to inventory/economy transaction
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- ANALYTICS AND TELEMETRY TABLES
-- =================================================================================================

-- Achievement events for analytics
CREATE TABLE achievement_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(30) NOT NULL, -- 'UNLOCKED', 'PROGRESS_UPDATE', 'COMPLETED', 'CLAIMED', 'RESET'
    player_id BIGINT NOT NULL,
    achievement_id BIGINT REFERENCES achievement_definitions(id),
    event_data JSONB NOT NULL DEFAULT '{}',
    session_id VARCHAR(100),
    client_version VARCHAR(20),
    platform VARCHAR(20),
    region VARCHAR(10),
    event_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Daily achievement statistics
CREATE TABLE achievement_daily_stats (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    achievement_id BIGINT REFERENCES achievement_definitions(id),
    total_unlocked BIGINT NOT NULL DEFAULT 0,
    total_completed BIGINT NOT NULL DEFAULT 0,
    total_claimed BIGINT NOT NULL DEFAULT 0,
    avg_completion_time INTERVAL, -- Average time to complete
    completion_rate DECIMAL(5,2), -- Percentage of unlocked that were completed
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(date, achievement_id)
);

-- Player achievement statistics
CREATE TABLE player_achievement_stats (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,
    total_achievements INTEGER NOT NULL DEFAULT 0,
    completed_achievements INTEGER NOT NULL DEFAULT 0,
    claimed_rewards INTEGER NOT NULL DEFAULT 0,
    favorite_category VARCHAR(30),
    completion_rate DECIMAL(5,2),
    average_completion_time INTERVAL,
    last_activity TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- ADVANCED FEATURES TABLES
-- =================================================================================================

-- Achievement chains and series
CREATE TABLE achievement_chains (
    id BIGSERIAL PRIMARY KEY,
    chain_key VARCHAR(100) UNIQUE NOT NULL,
    name JSONB NOT NULL,
    description JSONB,
    chain_type VARCHAR(20) NOT NULL DEFAULT 'LINEAR', -- 'LINEAR', 'BRANCHING', 'COLLECTION'
    total_achievements INTEGER NOT NULL,
    reward_data JSONB NOT NULL DEFAULT '{}', -- Chain completion rewards
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Achievement chain membership
CREATE TABLE achievement_chain_members (
    id BIGSERIAL PRIMARY KEY,
    chain_id BIGINT NOT NULL REFERENCES achievement_chains(id) ON DELETE CASCADE,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    position INTEGER NOT NULL, -- Order in chain
    is_required BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(chain_id, achievement_id),
    UNIQUE(chain_id, position)
);

-- Seasonal achievements
CREATE TABLE achievement_seasons (
    id BIGSERIAL PRIMARY KEY,
    season_key VARCHAR(100) UNIQUE NOT NULL,
    name JSONB NOT NULL,
    description JSONB,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    theme_data JSONB NOT NULL DEFAULT '{}', -- Seasonal theme configuration
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Seasonal achievement associations
CREATE TABLE achievement_season_members (
    id BIGSERIAL PRIMARY KEY,
    season_id BIGINT NOT NULL REFERENCES achievement_seasons(id) ON DELETE CASCADE,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    is_featured BOOLEAN NOT NULL DEFAULT false,
    bonus_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(season_id, achievement_id)
);

-- Guild/clan achievements
CREATE TABLE guild_achievements (
    id BIGSERIAL PRIMARY KEY,
    guild_id BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL REFERENCES achievement_definitions(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'LOCKED',
    unlocked_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    current_progress INTEGER NOT NULL DEFAULT 0,
    target_progress INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(guild_id, achievement_id)
);

-- =================================================================================================
-- NOTIFICATION SYSTEM TABLES
-- =================================================================================================

-- Player notification preferences for achievements
CREATE TABLE achievement_notification_preferences (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,
    unlocked_notifications BOOLEAN NOT NULL DEFAULT true,
    progress_notifications BOOLEAN NOT NULL DEFAULT true,
    completed_notifications BOOLEAN NOT NULL DEFAULT true,
    reward_available_notifications BOOLEAN NOT NULL DEFAULT true,
    chain_progress_notifications BOOLEAN NOT NULL DEFAULT true,
    seasonal_notifications BOOLEAN NOT NULL DEFAULT true,
    leaderboard_notifications BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Scheduled achievement notifications
CREATE TABLE achievement_scheduled_notifications (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,
    achievement_id BIGINT REFERENCES achievement_definitions(id),
    notification_type VARCHAR(30) NOT NULL, -- 'UNLOCKED', 'PROGRESS', 'COMPLETED', 'REWARD_AVAILABLE'
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

-- Achievement definitions indexes
CREATE INDEX idx_achievement_definitions_code ON achievement_definitions(code);
CREATE INDEX idx_achievement_definitions_category ON achievement_definitions(category);
CREATE INDEX idx_achievement_definitions_difficulty ON achievement_definitions(difficulty);
CREATE INDEX idx_achievement_definitions_type ON achievement_definitions(achievement_type);
CREATE INDEX idx_achievement_definitions_active ON achievement_definitions(is_active) WHERE is_active = true;
CREATE INDEX idx_achievement_definitions_chain ON achievement_definitions(chain_next_id) WHERE chain_next_id IS NOT NULL;

-- Player achievements indexes
CREATE INDEX idx_player_achievements_player ON player_achievements(player_id);
CREATE INDEX idx_player_achievements_achievement ON player_achievements(achievement_id);
CREATE INDEX idx_player_achievements_status ON player_achievements(status);
CREATE INDEX idx_player_achievements_player_status ON player_achievements(player_id, status);
CREATE INDEX idx_player_achievements_completed ON player_achievements(completed_at) WHERE completed_at IS NOT NULL;

-- Achievement progress indexes
CREATE INDEX idx_achievement_progress_player_achievement ON achievement_progress(player_achievement_id);
CREATE INDEX idx_achievement_progress_completed ON achievement_progress(is_completed) WHERE is_completed = true;
CREATE INDEX idx_achievement_progress_key ON achievement_progress(progress_key);

-- Achievement events indexes
CREATE INDEX idx_achievement_events_player ON achievement_events(player_id);
CREATE INDEX idx_achievement_events_type ON achievement_events(event_type);
CREATE INDEX idx_achievement_events_achievement ON achievement_events(achievement_id);
CREATE INDEX idx_achievement_events_timestamp ON achievement_events(event_timestamp DESC);
CREATE INDEX idx_achievement_events_player_timestamp ON achievement_events(player_id, event_timestamp DESC);

-- Reward indexes
CREATE INDEX idx_achievement_rewards_type ON achievement_rewards(reward_type);
CREATE INDEX idx_achievement_rewards_category ON achievement_rewards(reward_category);
CREATE INDEX idx_achievement_rewards_rarity ON achievement_rewards(rarity);

-- Claimed rewards indexes
CREATE INDEX idx_achievement_claimed_rewards_player_achievement ON achievement_claimed_rewards(player_achievement_id);
CREATE INDEX idx_achievement_claimed_rewards_delivery ON achievement_claimed_rewards(delivery_status, claimed_at DESC);

-- Seasonal indexes
CREATE INDEX idx_achievement_seasons_dates ON achievement_seasons(start_date, end_date);
CREATE INDEX idx_achievement_seasons_active ON achievement_seasons(is_active) WHERE is_active = true;

-- Guild achievements indexes
CREATE INDEX idx_guild_achievements_guild ON guild_achievements(guild_id);
CREATE INDEX idx_guild_achievements_status ON guild_achievements(status);

-- Notification indexes
CREATE INDEX idx_achievement_scheduled_notifications_player ON achievement_scheduled_notifications(player_id);
CREATE INDEX idx_achievement_scheduled_notifications_scheduled ON achievement_scheduled_notifications(scheduled_for);
CREATE INDEX idx_achievement_scheduled_notifications_status ON achievement_scheduled_notifications(delivery_status);

-- =================================================================================================
-- CONSTRAINTS
-- =================================================================================================

-- Check constraints
ALTER TABLE achievement_definitions ADD CONSTRAINT chk_category CHECK (category IN ('COMBAT', 'SOCIAL', 'ECONOMY', 'EXPLORATION', 'SPECIAL', 'SEASONAL', 'GUILD'));
ALTER TABLE achievement_definitions ADD CONSTRAINT chk_difficulty CHECK (difficulty IN ('EASY', 'MEDIUM', 'HARD', 'LEGENDARY'));
ALTER TABLE achievement_definitions ADD CONSTRAINT chk_achievement_type CHECK (achievement_type IN ('STANDARD', 'PROGRESSIVE', 'TIME_LIMITED', 'HIDDEN', 'COLLECTION', 'CHAINED'));

ALTER TABLE player_achievements ADD CONSTRAINT chk_status CHECK (status IN ('LOCKED', 'UNLOCKED', 'IN_PROGRESS', 'COMPLETED', 'CLAIMED'));

ALTER TABLE achievement_rewards ADD CONSTRAINT chk_reward_type CHECK (reward_type IN ('CURRENCY', 'ITEM', 'COSMETIC', 'TITLE', 'BOOSTER', 'UNLOCK', 'EXCLUSIVE'));
ALTER TABLE achievement_rewards ADD CONSTRAINT chk_rarity CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary'));

ALTER TABLE achievement_events ADD CONSTRAINT chk_event_type CHECK (event_type IN ('UNLOCKED', 'PROGRESS_UPDATE', 'COMPLETED', 'CLAIMED', 'RESET'));

ALTER TABLE achievement_scheduled_notifications ADD CONSTRAINT chk_delivery_status CHECK (delivery_status IN ('PENDING', 'SENT', 'DELIVERED', 'FAILED'));

-- =================================================================================================
-- TRIGGERS
-- =================================================================================================

-- Function to update updated_at timestamps
CREATE OR REPLACE FUNCTION update_achievement_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers to relevant tables
CREATE TRIGGER update_achievement_definitions_updated_at BEFORE UPDATE ON achievement_definitions FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_categories_updated_at BEFORE UPDATE ON achievement_categories FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_tags_updated_at BEFORE UPDATE ON achievement_tags FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_player_achievements_updated_at BEFORE UPDATE ON player_achievements FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_rewards_updated_at BEFORE UPDATE ON achievement_rewards FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_player_achievement_stats_updated_at BEFORE UPDATE ON player_achievement_stats FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_chains_updated_at BEFORE UPDATE ON achievement_chains FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_seasons_updated_at BEFORE UPDATE ON achievement_seasons FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_guild_achievements_updated_at BEFORE UPDATE ON guild_achievements FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
CREATE TRIGGER update_achievement_notification_preferences_updated_at BEFORE UPDATE ON achievement_notification_preferences FOR EACH ROW EXECUTE FUNCTION update_achievement_updated_at_column();
