--liquibase formatted sql

--changeset necpgame:V1_44_global_state_tables
--comment: Create tables for global state system (Issue: #140876058)

CREATE SCHEMA IF NOT EXISTS world;

CREATE TABLE IF NOT EXISTS world.global_state (
    key VARCHAR(255) PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    value JSONB NOT NULL DEFAULT '{}',
    version INTEGER NOT NULL DEFAULT 1,
    sync_type VARCHAR(20) NOT NULL DEFAULT 'SERVER_WIDE' CHECK (sync_type IN ('SERVER_WIDE', 'PLAYER_SPECIFIC', 'PHASED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_global_state_category ON world.global_state(category);
CREATE INDEX IF NOT EXISTS idx_global_state_sync_type ON world.global_state(sync_type);
CREATE INDEX IF NOT EXISTS idx_global_state_updated_at ON world.global_state(updated_at DESC);

-- Trigger for updating updated_at
CREATE OR REPLACE FUNCTION world.update_global_state_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_global_state_updated_at
    BEFORE UPDATE ON world.global_state
    FOR EACH ROW
    EXECUTE FUNCTION world.update_global_state_updated_at();

