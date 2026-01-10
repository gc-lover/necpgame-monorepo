-- Combat Implants Stats Service Tables
-- Migration: V1_96__combat_implant_stats_tables.sql

-- Create combat schema if not exists
CREATE SCHEMA IF NOT EXISTS combat;

-- Implant stats table for tracking implant performance
CREATE TABLE IF NOT EXISTS combat.implant_stats (
    id BIGSERIAL PRIMARY KEY,
    implant_id VARCHAR(255) NOT NULL,
    player_id VARCHAR(255) NOT NULL,
    usage_count BIGINT NOT NULL DEFAULT 0,
    success_rate DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    avg_duration DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    last_used TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT implant_stats_unique_implant_player UNIQUE (implant_id, player_id),
    CONSTRAINT implant_stats_usage_count_positive CHECK (usage_count >= 0),
    CONSTRAINT implant_stats_success_rate_valid CHECK (success_rate >= 0.0 AND success_rate <= 1.0),
    CONSTRAINT implant_stats_avg_duration_positive CHECK (avg_duration >= 0.0)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_implant_stats_implant_id ON combat.implant_stats (implant_id);
CREATE INDEX IF NOT EXISTS idx_implant_stats_player_id ON combat.implant_stats (player_id);
CREATE INDEX IF NOT EXISTS idx_implant_stats_last_used ON combat.implant_stats (last_used DESC);
CREATE INDEX IF NOT EXISTS idx_implant_stats_success_rate ON combat.implant_stats (success_rate DESC);

-- Performance monitoring table for real-time metrics
CREATE TABLE IF NOT EXISTS combat.implant_performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    implant_id VARCHAR(255) NOT NULL,
    player_id VARCHAR(255) NOT NULL,
    metric_type VARCHAR(100) NOT NULL, -- 'usage', 'success', 'duration', 'efficiency'
    metric_value DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT implant_metrics_implant_player_timestamp UNIQUE (implant_id, player_id, metric_type, timestamp)
);

-- Indexes for performance metrics
CREATE INDEX IF NOT EXISTS idx_implant_metrics_implant_id ON combat.implant_performance_metrics (implant_id);
CREATE INDEX IF NOT EXISTS idx_implant_metrics_player_id ON combat.implant_performance_metrics (player_id);
CREATE INDEX IF NOT EXISTS idx_implant_metrics_timestamp ON combat.implant_performance_metrics (timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_implant_metrics_type ON combat.implant_performance_metrics (metric_type);

-- Comments
COMMENT ON TABLE combat.implant_stats IS 'Stores statistical data about implant usage and performance';
COMMENT ON TABLE combat.implant_performance_metrics IS 'Real-time performance metrics for implant monitoring';

COMMENT ON COLUMN combat.implant_stats.usage_count IS 'Total number of times this implant was used by the player';
COMMENT ON COLUMN combat.implant_stats.success_rate IS 'Percentage of successful implant usages (0.0 to 1.0)';
COMMENT ON COLUMN combat.implant_stats.avg_duration IS 'Average duration of implant effect in seconds';