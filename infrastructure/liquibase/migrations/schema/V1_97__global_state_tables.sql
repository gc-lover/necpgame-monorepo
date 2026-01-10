-- Global State Service Tables
-- Migration: V1_97__global_state_tables.sql

-- Create global_state schema if not exists
CREATE SCHEMA IF NOT EXISTS global_state;

-- Global state table for storing current aggregate states
CREATE TABLE IF NOT EXISTS global_state.global_state (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(200) NOT NULL,
    version BIGINT NOT NULL DEFAULT 1,
    data JSONB NOT NULL,
    last_modified TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    checksum VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT global_state_unique_aggregate UNIQUE (aggregate_type, aggregate_id),
    CONSTRAINT global_state_version_positive CHECK (version > 0),
    CONSTRAINT global_state_checksum_not_empty CHECK (length(checksum) > 0)
);

-- Game events table for event sourcing
CREATE TABLE IF NOT EXISTS global_state.game_events (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID UNIQUE NOT NULL DEFAULT gen_random_uuid(),
    event_type VARCHAR(100) NOT NULL,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(200) NOT NULL,
    event_version BIGINT NOT NULL DEFAULT 1,
    correlation_id UUID,
    causation_id UUID,
    event_data JSONB NOT NULL,
    metadata JSONB,
    server_id VARCHAR(100) NOT NULL DEFAULT 'unknown',
    player_id UUID,
    session_id UUID,
    event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    processed_at TIMESTAMP WITH TIME ZONE,
    state_changes JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT game_events_event_version_positive CHECK (event_version > 0),
    CONSTRAINT game_events_event_timestamp_not_future CHECK (event_timestamp <= NOW() + INTERVAL '1 minute')
);

-- Indexes for performance (optimized for MMOFPS queries)
CREATE INDEX IF NOT EXISTS idx_global_state_aggregate ON global_state.global_state (aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS idx_global_state_version ON global_state.global_state (aggregate_type, aggregate_id, version DESC);
CREATE INDEX IF NOT EXISTS idx_global_state_modified ON global_state.global_state (last_modified DESC);

CREATE INDEX IF NOT EXISTS idx_game_events_aggregate ON global_state.game_events (aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS idx_game_events_version ON global_state.game_events (aggregate_type, aggregate_id, event_version);
CREATE INDEX IF NOT EXISTS idx_game_events_timestamp ON global_state.game_events (event_timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_game_events_type ON global_state.game_events (event_type);
CREATE INDEX IF NOT EXISTS idx_game_events_correlation ON global_state.game_events (correlation_id);
CREATE INDEX IF NOT EXISTS idx_game_events_player ON global_state.game_events (player_id);
CREATE INDEX IF NOT EXISTS idx_game_events_session ON global_state.game_events (session_id);

-- Partial indexes for common queries
CREATE INDEX IF NOT EXISTS idx_game_events_unprocessed ON global_state.game_events (event_timestamp)
WHERE processed_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_game_events_recent ON global_state.game_events (aggregate_type, aggregate_id, event_timestamp DESC)
WHERE event_timestamp > NOW() - INTERVAL '24 hours';

-- Snapshots table for state reconstruction optimization
CREATE TABLE IF NOT EXISTS global_state.state_snapshots (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id VARCHAR(200) NOT NULL,
    snapshot_version BIGINT NOT NULL,
    snapshot_data JSONB NOT NULL,
    event_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT state_snapshots_unique_snapshot UNIQUE (aggregate_type, aggregate_id, snapshot_version),
    CONSTRAINT state_snapshots_version_positive CHECK (snapshot_version > 0),
    CONSTRAINT state_snapshots_event_count_positive CHECK (event_count >= 0)
);

-- Indexes for snapshots
CREATE INDEX IF NOT EXISTS idx_state_snapshots_aggregate ON global_state.state_snapshots (aggregate_type, aggregate_id);
CREATE INDEX IF NOT EXISTS idx_state_snapshots_version ON global_state.state_snapshots (aggregate_type, aggregate_id, snapshot_version DESC);

-- Comments
COMMENT ON TABLE global_state.global_state IS 'Stores current state of all aggregates with versioning';
COMMENT ON TABLE global_state.game_events IS 'Event store for complete audit trail and state reconstruction';
COMMENT ON TABLE global_state.state_snapshots IS 'Snapshots for fast state reconstruction from large event histories';

COMMENT ON COLUMN global_state.global_state.aggregate_type IS 'Type of aggregate (player, guild, world, economy, combat)';
COMMENT ON COLUMN global_state.global_state.aggregate_id IS 'Unique identifier for the aggregate instance';
COMMENT ON COLUMN global_state.global_state.version IS 'Current version number for optimistic locking';
COMMENT ON COLUMN global_state.global_state.checksum IS 'MD5 checksum of state data for integrity validation';

COMMENT ON COLUMN global_state.game_events.event_version IS 'Version of this event within the aggregate stream';
COMMENT ON COLUMN global_state.game_events.correlation_id IS 'ID linking related events across aggregates';
COMMENT ON COLUMN global_state.game_events.causation_id IS 'ID of the event that caused this event';