-- Issue: #2264 - Analytics Dashboard Service Database Schema
-- liquibase formatted sql
-- changeset backend:analytics-dashboard-service-tables dbms:postgresql
-- comment: Create analytics dashboard service tables for comprehensive game analytics

BEGIN;

-- Create analytics schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS analytics;

-- Table: analytics.player_sessions
-- Tracks player session data for engagement analytics
CREATE TABLE IF NOT EXISTS analytics.player_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    session_start TIMESTAMP WITH TIME ZONE NOT NULL,
    session_end TIMESTAMP WITH TIME ZONE,
    duration_seconds INTEGER,
    game_mode VARCHAR(50),
    region VARCHAR(10),
    device_type VARCHAR(20),
    ip_address INET,
    user_agent TEXT,
    events_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: analytics.player_events
-- Stores individual player events for behavior analysis
CREATE TABLE IF NOT EXISTS analytics.player_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_data JSONB,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    session_id UUID REFERENCES analytics.player_sessions(id),
    game_mode VARCHAR(50),
    region VARCHAR(10)
);

-- Table: analytics.economic_transactions
-- Records all economic transactions for revenue and market analytics
CREATE TABLE IF NOT EXISTS analytics.economic_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- purchase, sale, trade, reward, etc.
    currency_type VARCHAR(20) NOT NULL, -- eddies, crypto, premium
    amount DECIMAL(18,2) NOT NULL,
    item_id VARCHAR(100),
    transaction_data JSONB,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    session_id UUID REFERENCES analytics.player_sessions(id)
);

-- Table: analytics.combat_matches
-- Stores combat match data for performance analytics
CREATE TABLE IF NOT EXISTS analytics.combat_matches (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    match_id VARCHAR(100) NOT NULL UNIQUE,
    game_mode VARCHAR(50) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,
    duration_seconds INTEGER,
    winner_team VARCHAR(20),
    total_players INTEGER NOT NULL,
    region VARCHAR(10),
    server_id VARCHAR(50),
    match_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: analytics.player_combat_stats
-- Individual player combat performance data
CREATE TABLE IF NOT EXISTS analytics.player_combat_stats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    match_id VARCHAR(100) NOT NULL REFERENCES analytics.combat_matches(match_id),
    kills INTEGER DEFAULT 0,
    deaths INTEGER DEFAULT 0,
    assists INTEGER DEFAULT 0,
    score INTEGER DEFAULT 0,
    damage_dealt DECIMAL(10,2) DEFAULT 0,
    damage_taken DECIMAL(10,2) DEFAULT 0,
    healing_done DECIMAL(10,2) DEFAULT 0,
    accuracy DECIMAL(5,2) DEFAULT 0,
    player_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: analytics.guild_activity
-- Guild-related analytics and social metrics
CREATE TABLE IF NOT EXISTS analytics.guild_activity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL,
    activity_type VARCHAR(50) NOT NULL, -- join, leave, event, tournament, etc.
    player_id UUID NOT NULL,
    activity_data JSONB,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: analytics.system_metrics
-- System performance and health monitoring data
CREATE TABLE IF NOT EXISTS analytics.system_metrics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    metric_type VARCHAR(50) NOT NULL, -- cpu, memory, network, latency, etc.
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(18,6) NOT NULL,
    unit VARCHAR(20),
    server_id VARCHAR(50),
    region VARCHAR(10),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metadata JSONB
);

-- Table: analytics.alerts
-- Analytics alerts and notifications
CREATE TABLE IF NOT EXISTS analytics.alerts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    alert_type VARCHAR(50) NOT NULL, -- performance, security, economic, player, etc.
    severity VARCHAR(20) NOT NULL DEFAULT 'medium' CHECK (severity IN ('low', 'medium', 'high', 'critical')),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    alert_data JSONB,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_by UUID,
    acknowledged_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE
);

-- Table: analytics.dashboards
-- Saved dashboard configurations for analysts
CREATE TABLE IF NOT EXISTS analytics.dashboards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    config JSONB NOT NULL,
    created_by UUID NOT NULL,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    tags TEXT[],
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance optimization
-- Player sessions indexes
CREATE INDEX IF NOT EXISTS idx_player_sessions_player_id ON analytics.player_sessions(player_id);
CREATE INDEX IF NOT EXISTS idx_player_sessions_start_time ON analytics.player_sessions(session_start DESC);
CREATE INDEX IF NOT EXISTS idx_player_sessions_region ON analytics.player_sessions(region);
CREATE INDEX IF NOT EXISTS idx_player_sessions_game_mode ON analytics.player_sessions(game_mode);

-- Player events indexes
CREATE INDEX IF NOT EXISTS idx_player_events_player_id ON analytics.player_events(player_id);
CREATE INDEX IF NOT EXISTS idx_player_events_type ON analytics.player_events(event_type);
CREATE INDEX IF NOT EXISTS idx_player_events_timestamp ON analytics.player_events(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_player_events_session_id ON analytics.player_events(session_id);

-- Economic transactions indexes
CREATE INDEX IF NOT EXISTS idx_economic_transactions_player_id ON analytics.economic_transactions(player_id);
CREATE INDEX IF NOT EXISTS idx_economic_transactions_type ON analytics.economic_transactions(transaction_type);
CREATE INDEX IF NOT EXISTS idx_economic_transactions_timestamp ON analytics.economic_transactions(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_economic_transactions_currency ON analytics.economic_transactions(currency_type);

-- Combat matches indexes
CREATE INDEX IF NOT EXISTS idx_combat_matches_game_mode ON analytics.combat_matches(game_mode);
CREATE INDEX IF NOT EXISTS idx_combat_matches_start_time ON analytics.combat_matches(start_time DESC);
CREATE INDEX IF NOT EXISTS idx_combat_matches_region ON analytics.combat_matches(region);
CREATE INDEX IF NOT EXISTS idx_combat_matches_winner ON analytics.combat_matches(winner_team);

-- Player combat stats indexes
CREATE INDEX IF NOT EXISTS idx_player_combat_stats_player_id ON analytics.player_combat_stats(player_id);
CREATE INDEX IF NOT EXISTS idx_player_combat_stats_match_id ON analytics.player_combat_stats(match_id);
CREATE INDEX IF NOT EXISTS idx_player_combat_stats_score ON analytics.player_combat_stats(score DESC);

-- Guild activity indexes
CREATE INDEX IF NOT EXISTS idx_guild_activity_guild_id ON analytics.guild_activity(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_activity_type ON analytics.guild_activity(activity_type);
CREATE INDEX IF NOT EXISTS idx_guild_activity_timestamp ON analytics.guild_activity(timestamp DESC);

-- System metrics indexes
CREATE INDEX IF NOT EXISTS idx_system_metrics_type ON analytics.system_metrics(metric_type);
CREATE INDEX IF NOT EXISTS idx_system_metrics_name ON analytics.system_metrics(metric_name);
CREATE INDEX IF NOT EXISTS idx_system_metrics_timestamp ON analytics.system_metrics(timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_system_metrics_server ON analytics.system_metrics(server_id);

-- Alerts indexes
CREATE INDEX IF NOT EXISTS idx_alerts_type ON analytics.alerts(alert_type);
CREATE INDEX IF NOT EXISTS idx_alerts_severity ON analytics.alerts(severity);
CREATE INDEX IF NOT EXISTS idx_alerts_acknowledged ON analytics.alerts(acknowledged);
CREATE INDEX IF NOT EXISTS idx_alerts_created_at ON analytics.alerts(created_at DESC);

-- Dashboards indexes
CREATE INDEX IF NOT EXISTS idx_dashboards_created_by ON analytics.dashboards(created_by);
CREATE INDEX IF NOT EXISTS idx_dashboards_public ON analytics.dashboards(is_public);
CREATE INDEX IF NOT EXISTS idx_dashboards_tags ON analytics.dashboards USING GIN (tags);

-- GIN indexes for JSONB fields (high-performance JSON queries)
CREATE INDEX IF NOT EXISTS idx_player_events_data_gin ON analytics.player_events USING GIN (event_data);
CREATE INDEX IF NOT EXISTS idx_economic_transactions_data_gin ON analytics.economic_transactions USING GIN (transaction_data);
CREATE INDEX IF NOT EXISTS idx_combat_matches_data_gin ON analytics.combat_matches USING GIN (match_data);
CREATE INDEX IF NOT EXISTS idx_player_combat_stats_data_gin ON analytics.player_combat_stats USING GIN (player_data);
CREATE INDEX IF NOT EXISTS idx_guild_activity_data_gin ON analytics.guild_activity USING GIN (activity_data);
CREATE INDEX IF NOT EXISTS idx_system_metrics_metadata_gin ON analytics.system_metrics USING GIN (metadata);
CREATE INDEX IF NOT EXISTS idx_alerts_data_gin ON analytics.alerts USING GIN (alert_data);
CREATE INDEX IF NOT EXISTS idx_dashboards_config_gin ON analytics.dashboards USING GIN (config);

-- Auto-update triggers for updated_at columns
CREATE OR REPLACE FUNCTION update_analytics_dashboards_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_analytics_dashboards_updated_at
    BEFORE UPDATE ON analytics.dashboards
    FOR EACH ROW EXECUTE FUNCTION update_analytics_dashboards_updated_at();

-- Comments for API documentation
COMMENT ON SCHEMA analytics IS 'Analytics schema for comprehensive game analytics and monitoring';
COMMENT ON TABLE analytics.player_sessions IS 'Player session tracking for engagement analytics';
COMMENT ON TABLE analytics.player_events IS 'Individual player events for behavior analysis';
COMMENT ON TABLE analytics.economic_transactions IS 'Economic transaction records for revenue analytics';
COMMENT ON TABLE analytics.combat_matches IS 'Combat match data for performance analytics';
COMMENT ON TABLE analytics.player_combat_stats IS 'Individual player combat performance data';
COMMENT ON TABLE analytics.guild_activity IS 'Guild-related analytics and social metrics';
COMMENT ON TABLE analytics.system_metrics IS 'System performance and health monitoring data';
COMMENT ON TABLE analytics.alerts IS 'Analytics alerts and notifications system';
COMMENT ON TABLE analytics.dashboards IS 'Saved dashboard configurations for analysts';

COMMIT;
