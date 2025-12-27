--liquibase formatted sql

--changeset backend:referral-system-tables-creation runOnChange:false
--comment: Create tables for referral system: referral_codes, referrals, referral_milestones, referral_leaderboard, referral_rewards, referral_analytics

-- Create referral_codes table
CREATE TABLE IF NOT EXISTS referral_codes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    code VARCHAR(20) NOT NULL UNIQUE,
    prefix VARCHAR(10) DEFAULT 'REF',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT true,
    usage_count INTEGER DEFAULT 0,
    max_usage INTEGER,
    CONSTRAINT fk_referral_codes_character FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE
);

-- Create indexes for referral_codes
CREATE INDEX IF NOT EXISTS idx_referral_codes_character_id ON referral_codes(character_id);
CREATE INDEX IF NOT EXISTS idx_referral_codes_code ON referral_codes(code);

-- Create referrals table
CREATE TABLE IF NOT EXISTS referrals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    referrer_id UUID NOT NULL,
    referred_id UUID NOT NULL,
    referral_code VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'active', 'milestone_reached', 'inactive')),
    registered_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    level_10_reached_at TIMESTAMP WITH TIME ZONE,
    milestone_reached_at TIMESTAMP WITH TIME ZONE,
    welcome_reward_claimed BOOLEAN DEFAULT false,
    level_10_reward_claimed BOOLEAN DEFAULT false,
    milestone_reward_claimed BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_referrals_referrer FOREIGN KEY (referrer_id) REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_referrals_referred FOREIGN KEY (referred_id) REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT unique_referrer_referred UNIQUE (referrer_id, referred_id)
);

-- Create indexes for referrals
CREATE INDEX IF NOT EXISTS idx_referrals_referrer_status ON referrals(referrer_id, status);
CREATE INDEX IF NOT EXISTS idx_referrals_referred ON referrals(referred_id);
CREATE INDEX IF NOT EXISTS idx_referrals_referral_code ON referrals(referral_code);

-- Create referral_milestones table
CREATE TABLE IF NOT EXISTS referral_milestones (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    milestone_level INTEGER NOT NULL CHECK (milestone_level IN (5, 10, 25, 50, 100)),
    required_referrals INTEGER NOT NULL,
    current_referrals INTEGER DEFAULT 0,
    reward_type VARCHAR(50) NOT NULL,
    reward_amount INTEGER NOT NULL,
    bonus_reward_type VARCHAR(50),
    bonus_reward_amount INTEGER,
    is_completed BOOLEAN DEFAULT false,
    completed_at TIMESTAMP WITH TIME ZONE,
    is_reward_claimed BOOLEAN DEFAULT false,
    reward_claimed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_referral_milestones_character FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT unique_character_milestone UNIQUE (character_id, milestone_level)
);

-- Create indexes for referral_milestones
CREATE INDEX IF NOT EXISTS idx_referral_milestones_character_completed ON referral_milestones(character_id, is_completed);
CREATE INDEX IF NOT EXISTS idx_referral_milestones_milestone_level ON referral_milestones(milestone_level);

-- Create referral_leaderboard table
CREATE TABLE IF NOT EXISTS referral_leaderboard (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    rank INTEGER,
    total_referrals INTEGER DEFAULT 0,
    active_referrals INTEGER DEFAULT 0,
    milestone_referrals INTEGER DEFAULT 0,
    total_rewards DECIMAL(10,2) DEFAULT 0.00,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_referral_leaderboard_character FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE
);

-- Create indexes for referral_leaderboard
CREATE INDEX IF NOT EXISTS idx_referral_leaderboard_rank ON referral_leaderboard(rank);
CREATE INDEX IF NOT EXISTS idx_referral_leaderboard_character_id ON referral_leaderboard(character_id);

-- Create referral_rewards table
CREATE TABLE IF NOT EXISTS referral_rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    referral_id UUID,
    reward_type VARCHAR(50) NOT NULL,
    reward_amount INTEGER NOT NULL,
    currency_type VARCHAR(20) DEFAULT 'eddies',
    item_id UUID,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'distributed', 'claimed', 'expired')),
    distributed_at TIMESTAMP WITH TIME ZONE,
    claimed_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_referral_rewards_character FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE,
    CONSTRAINT fk_referral_rewards_referral FOREIGN KEY (referral_id) REFERENCES referrals(id) ON DELETE SET NULL
);

-- Create indexes for referral_rewards
CREATE INDEX IF NOT EXISTS idx_referral_rewards_character_status ON referral_rewards(character_id, status);
CREATE INDEX IF NOT EXISTS idx_referral_rewards_status ON referral_rewards(status);

-- Create referral_analytics table
CREATE TABLE IF NOT EXISTS referral_analytics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date DATE NOT NULL UNIQUE,
    total_codes_generated INTEGER DEFAULT 0,
    total_registrations INTEGER DEFAULT 0,
    total_active_referrals INTEGER DEFAULT 0,
    total_milestone_reached INTEGER DEFAULT 0,
    total_rewards_distributed DECIMAL(10,2) DEFAULT 0.00,
    conversion_rate DECIMAL(5,4) DEFAULT 0.0000,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create index for referral_analytics
CREATE INDEX IF NOT EXISTS idx_referral_analytics_date ON referral_analytics(date);

-- Insert default milestone configurations
INSERT INTO referral_milestones (character_id, milestone_level, required_referrals, reward_type, reward_amount, bonus_reward_type, bonus_reward_amount)
SELECT
    c.id,
    5,
    5,
    'currency',
    1000,
    'item',
    1
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_milestones rm
    WHERE rm.character_id = c.id AND rm.milestone_level = 5
);

INSERT INTO referral_milestones (character_id, milestone_level, required_referrals, reward_type, reward_amount, bonus_reward_type, bonus_reward_amount)
SELECT
    c.id,
    10,
    10,
    'currency',
    2500,
    'item',
    2
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_milestones rm
    WHERE rm.character_id = c.id AND rm.milestone_level = 10
);

INSERT INTO referral_milestones (character_id, milestone_level, required_referrals, reward_type, reward_amount, bonus_reward_type, bonus_reward_amount)
SELECT
    c.id,
    25,
    25,
    'currency',
    7500,
    'cosmetic',
    1
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_milestones rm
    WHERE rm.character_id = c.id AND rm.milestone_level = 25
);

INSERT INTO referral_milestones (character_id, milestone_level, required_referrals, reward_type, reward_amount, bonus_reward_type, bonus_reward_amount)
SELECT
    c.id,
    50,
    50,
    'premium_currency',
    500,
    'cosmetic',
    2
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_milestones rm
    WHERE rm.character_id = c.id AND rm.milestone_level = 50
);

INSERT INTO referral_milestones (character_id, milestone_level, required_referrals, reward_type, reward_amount, bonus_reward_type, bonus_reward_amount)
SELECT
    c.id,
    100,
    100,
    'premium_currency',
    1500,
    'cosmetic',
    3
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_milestones rm
    WHERE rm.character_id = c.id AND rm.milestone_level = 100
);

-- Initialize leaderboard for existing characters
INSERT INTO referral_leaderboard (character_id, rank, total_referrals, active_referrals, milestone_referrals, total_rewards)
SELECT
    c.id,
    ROW_NUMBER() OVER (ORDER BY c.created_at),
    0,
    0,
    0,
    0.00
FROM characters c
WHERE NOT EXISTS (
    SELECT 1 FROM referral_leaderboard rl WHERE rl.character_id = c.id
);

-- Update initial ranks
UPDATE referral_leaderboard
SET rank = sub.rnk
FROM (
    SELECT id, ROW_NUMBER() OVER (ORDER BY total_referrals DESC, active_referrals DESC, created_at ASC) as rnk
    FROM referral_leaderboard
) sub
WHERE referral_leaderboard.id = sub.id;

--rollback DROP TABLE IF EXISTS referral_analytics;
--rollback DROP TABLE IF EXISTS referral_rewards;
--rollback DROP TABLE IF EXISTS referral_leaderboard;
--rollback DROP TABLE IF EXISTS referral_milestones;
--rollback DROP TABLE IF EXISTS referrals;
--rollback DROP TABLE IF EXISTS referral_codes;
