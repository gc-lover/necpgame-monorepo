-- Reputation System Database Schema
-- Issue: #2174 - Reputation Decay & Recovery mechanics

-- Create schema for reputation system
CREATE SCHEMA IF NOT EXISTS reputation;

-- Player reputations table - stores reputation scores for all players
CREATE TABLE reputation.player_reputations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    reputation_type VARCHAR(50) NOT NULL CHECK (reputation_type IN ('player', 'faction', 'region', 'global')),
    target_id UUID NULL, -- faction_id, region_id, etc.
    current_value DECIMAL(8,2) NOT NULL CHECK (current_value BETWEEN -1000 AND 1000),
    base_value DECIMAL(8,2) NOT NULL CHECK (base_value BETWEEN -1000 AND 1000),
    decay_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 CHECK (decay_rate BETWEEN 0 AND 1),
    recovery_rate DECIMAL(5,4) NOT NULL DEFAULT 1.0 CHECK (recovery_rate >= 0),
    last_decay_unix BIGINT NOT NULL DEFAULT 0,
    next_decay_unix BIGINT NOT NULL DEFAULT 0,
    version INTEGER NOT NULL DEFAULT 1,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_decay_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_recovery_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID NULL,
    updated_by UUID NULL,
    UNIQUE(player_id, reputation_type, target_id)
);

-- Reputation changes table - audit trail of all reputation changes
CREATE TABLE reputation.reputation_changes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    reputation_type VARCHAR(50) NOT NULL CHECK (reputation_type IN ('player', 'faction', 'region', 'global', 'decay', 'recovery')),
    target_id UUID NULL,
    reason VARCHAR(200) NOT NULL,
    old_value DECIMAL(8,2) NOT NULL,
    new_value DECIMAL(8,2) NOT NULL,
    change_amount DECIMAL(8,2) NOT NULL,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    executed_at_unix BIGINT NOT NULL,
    decay_applied INTEGER NOT NULL DEFAULT 0 CHECK (decay_applied IN (0, 1)),
    event_id INTEGER NULL,
    created_by UUID NULL
);

-- Decay rules table - configurable decay parameters by reputation type
CREATE TABLE reputation.decay_rules (
    reputation_type VARCHAR(50) PRIMARY KEY CHECK (reputation_type IN ('player', 'faction', 'region', 'global')),
    base_decay_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 CHECK (base_decay_rate BETWEEN 0 AND 1),
    decay_interval_hours INTEGER NOT NULL DEFAULT 24 CHECK (decay_interval_hours > 0),
    min_reputation DECIMAL(8,2) NOT NULL DEFAULT -1000.00,
    max_reputation DECIMAL(8,2) NOT NULL DEFAULT 1000.00,
    activity_multiplier DECIMAL(5,4) NOT NULL DEFAULT 0.50 CHECK (activity_multiplier > 0),
    faction_modifier DECIMAL(5,4) NOT NULL DEFAULT 1.00 CHECK (faction_modifier > 0),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    priority INTEGER NOT NULL DEFAULT 1 CHECK (priority >= 1),
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID NULL,
    updated_by UUID NULL
);

-- Recovery events table - tracks recovery applications
CREATE TABLE reputation.recovery_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('time_based', 'action_based', 'event_based', 'bonus')),
    recovery_amount DECIMAL(8,2) NOT NULL CHECK (recovery_amount >= 0),
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    event_id INTEGER NULL,
    recovery_streak INTEGER NOT NULL DEFAULT 0,
    cooldown_hours INTEGER NOT NULL DEFAULT 6,
    is_bonus_recovery BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by UUID NULL
);

-- Player reputation statistics table - aggregated stats per player
CREATE TABLE reputation.player_stats (
    player_id UUID PRIMARY KEY,
    total_decay DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_recovery DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    change_count INTEGER NOT NULL DEFAULT 0,
    last_activity TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    reputation_level VARCHAR(20) NOT NULL DEFAULT 'neutral' CHECK (reputation_level IN ('outcast', 'neutral', 'respected', 'honored', 'legendary')),
    decay_status JSONB NULL, -- Latest decay status
    recovery_status JSONB NULL, -- Latest recovery status
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_by UUID NULL
);

-- Global reputation statistics table - system-wide analytics
CREATE TABLE reputation.global_stats (
    stat_date DATE PRIMARY KEY,
    total_players INTEGER NOT NULL DEFAULT 0,
    active_players INTEGER NOT NULL DEFAULT 0,
    decay_events INTEGER NOT NULL DEFAULT 0,
    recovery_events INTEGER NOT NULL DEFAULT 0,
    average_reputation_global DECIMAL(8,2) NULL,
    average_reputation_player DECIMAL(8,2) NULL,
    average_reputation_faction DECIMAL(8,2) NULL,
    reputation_distribution JSONB NULL, -- Count by levels
    most_changed_players JSONB NULL, -- Top 10 players by reputation change
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_by UUID NULL
);

-- Indexes for performance optimization
CREATE INDEX CONCURRENTLY idx_player_reputations_player ON reputation.player_reputations(player_id);
CREATE INDEX CONCURRENTLY idx_player_reputations_type ON reputation.player_reputations(reputation_type);
CREATE INDEX CONCURRENTLY idx_player_reputations_active ON reputation.player_reputations(is_active) WHERE is_active = TRUE;
CREATE INDEX CONCURRENTLY idx_player_reputations_decay ON reputation.player_reputations(next_decay_unix) WHERE is_decay_enabled = TRUE;
CREATE INDEX CONCURRENTLY idx_player_reputations_updated ON reputation.player_reputations(updated_at DESC);

CREATE INDEX CONCURRENTLY idx_reputation_changes_player ON reputation.reputation_changes(player_id);
CREATE INDEX CONCURRENTLY idx_reputation_changes_type ON reputation.reputation_changes(reputation_type);
CREATE INDEX CONCURRENTLY idx_reputation_changes_executed ON reputation.reputation_changes(executed_at DESC);
CREATE INDEX CONCURRENTLY idx_reputation_changes_decay ON reputation.reputation_changes(decay_applied) WHERE decay_applied = 1;

CREATE INDEX CONCURRENTLY idx_recovery_events_player ON reputation.recovery_events(player_id);
CREATE INDEX CONCURRENTLY idx_recovery_events_type ON reputation.recovery_events(event_type);
CREATE INDEX CONCURRENTLY idx_recovery_events_executed ON reputation.recovery_events(executed_at DESC);

CREATE INDEX CONCURRENTLY idx_global_stats_date ON reputation.global_stats(stat_date DESC);

-- Partial indexes for active records
CREATE INDEX CONCURRENTLY idx_active_reputations ON reputation.player_reputations(player_id, reputation_type) WHERE is_active = TRUE;
CREATE INDEX CONCURRENTLY idx_pending_decay ON reputation.player_reputations(next_decay_unix) WHERE next_decay_unix <= extract(epoch from NOW()) AND is_decay_enabled = TRUE;

-- JSONB indexes for flexible querying
CREATE INDEX CONCURRENTLY idx_player_stats_decay_status ON reputation.player_stats USING GIN(decay_status);
CREATE INDEX CONCURRENTLY idx_player_stats_recovery_status ON reputation.player_stats USING GIN(recovery_status);
CREATE INDEX CONCURRENTLY idx_global_stats_distribution ON reputation.global_stats USING GIN(reputation_distribution);

-- Triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION reputation.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_player_reputations_updated_at
    BEFORE UPDATE ON reputation.player_reputations
    FOR EACH ROW EXECUTE FUNCTION reputation.update_updated_at_column();

CREATE TRIGGER update_decay_rules_updated_at
    BEFORE UPDATE ON reputation.decay_rules
    FOR EACH ROW EXECUTE FUNCTION reputation.update_updated_at_column();

CREATE TRIGGER update_player_stats_updated_at
    BEFORE UPDATE ON reputation.player_stats
    FOR EACH ROW EXECUTE FUNCTION reputation.update_updated_at_column();

-- Insert default decay rules
INSERT INTO reputation.decay_rules (
    reputation_type, base_decay_rate, decay_interval_hours,
    min_reputation, max_reputation, activity_multiplier, faction_modifier
) VALUES
('player', 0.02, 24, -500.00, 1000.00, 0.8, 1.0),
('faction', 0.05, 12, -1000.00, 1000.00, 0.6, 1.5),
('region', 0.03, 48, -800.00, 800.00, 0.9, 1.0),
('global', 0.01, 168, -200.00, 500.00, 1.0, 1.0);

-- Insert sample player reputations for testing
INSERT INTO reputation.player_reputations (
    player_id, reputation_type, target_id, current_value, base_value,
    decay_rate, recovery_rate, last_decay_unix, next_decay_unix
) VALUES
-- Player 1 - Good standing
('550e8400-e29b-41d4-a716-446655440000', 'player', NULL, 250.00, 250.00, 0.02, 1.0,
 extract(epoch from NOW() - INTERVAL '1 day'), extract(epoch from NOW() + INTERVAL '1 day')),
('550e8400-e29b-41d4-a716-446655440000', 'faction', '550e8400-e29b-41d4-a716-446655440001', 150.00, 150.00, 0.05, 1.0,
 extract(epoch from NOW() - INTERVAL '12 hours'), extract(epoch from NOW() + INTERVAL '12 hours')),
('550e8400-e29b-41d4-a716-446655440000', 'region', '550e8400-e29b-41d4-a716-446655440002', 100.00, 100.00, 0.03, 1.0,
 extract(epoch from NOW() - INTERVAL '2 days'), extract(epoch from NOW() + INTERVAL '1 day')),

-- Player 2 - Neutral standing
('550e8400-e29b-41d4-a716-446655440003', 'player', NULL, 0.00, 0.00, 0.02, 1.0,
 extract(epoch from NOW() - INTERVAL '2 days'), extract(epoch from NOW() - INTERVAL '1 hour')),
('550e8400-e29b-41d4-a716-446655440003', 'faction', '550e8400-e29b-41d4-a716-446655440001', -50.00, -50.00, 0.05, 1.0,
 extract(epoch from NOW() - INTERVAL '1 day'), extract(epoch from NOW() + INTERVAL '12 hours')),

-- Player 3 - Poor standing (needs recovery)
('550e8400-e29b-41d4-a716-446655440004', 'player', NULL, -150.00, -50.00, 0.02, 1.0,
 extract(epoch from NOW() - INTERVAL '3 days'), extract(epoch from NOW() - INTERVAL '2 hours')),
('550e8400-e29b-41d4-a716-446655440004', 'faction', '550e8400-e29b-41d4-a716-446655440001', -200.00, -100.00, 0.05, 1.0,
 extract(epoch from NOW() - INTERVAL '2 days'), extract(epoch from NOW() + INTERVAL '6 hours'));

-- Insert sample reputation changes
INSERT INTO reputation.reputation_changes (
    player_id, reputation_type, target_id, reason, old_value, new_value, change_amount, executed_at_unix
) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'player', NULL, 'Quest completed', 200.00, 250.00, 50.00, extract(epoch from NOW() - INTERVAL '2 days')),
('550e8400-e29b-41d4-a716-446655440000', 'faction', '550e8400-e29b-41d4-a716-446655440001', 'Faction quest', 100.00, 150.00, 50.00, extract(epoch from NOW() - INTERVAL '1 day')),
('550e8400-e29b-41d4-a716-446655440004', 'player', NULL, 'Failed quest', 0.00, -50.00, -50.00, extract(epoch from NOW() - INTERVAL '5 days')),
('550e8400-e29b-41d4-a716-446655440004', 'faction', '550e8400-e29b-41d4-a716-446655440001', 'Faction betrayal', 0.00, -100.00, -100.00, extract(epoch from NOW() - INTERVAL '4 days'));

-- Insert initial global stats
INSERT INTO reputation.global_stats (stat_date, total_players, active_players, decay_events, recovery_events)
VALUES (CURRENT_DATE, 3, 3, 0, 0);