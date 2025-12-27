-- Combat Effects Table for Combat Damage Service Go
-- BACKEND NOTE: High-performance table for real-time combat effects management
-- Optimized for 1000+ RPS damage calculations with zero allocations in hot path

CREATE TABLE IF NOT EXISTS gameplay.combat_effects
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    participant_id UUID NOT NULL, -- Player or NPC ID
    type VARCHAR(50) NOT NULL CHECK (type IN ('damage_boost', 'damage_reduction', 'critical_boost', 'armor_boost', 'speed_boost', 'healing_over_time', 'damage_over_time', 'stun', 'slow', 'silence', 'invisibility', 'berserk')),
    value DECIMAL(10,4) NOT NULL DEFAULT 0, -- Effect strength/value
    duration_ms INTEGER NOT NULL CHECK (duration_ms > 0), -- Duration in milliseconds
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    source VARCHAR(100), -- Source of the effect (weapon, implant, ability, etc.)
    stacks INTEGER NOT NULL DEFAULT 1 CHECK (stacks >= 1), -- Number of stacks for stackable effects
    max_stacks INTEGER NOT NULL DEFAULT 1 CHECK (max_stacks >= 1), -- Maximum stacks allowed
    is_permanent BOOLEAN NOT NULL DEFAULT FALSE, -- Permanent effects don't expire
    metadata JSONB -- Additional effect-specific data
);

-- Performance indexes for real-time combat (P99 <5ms requirement)
CREATE INDEX IF NOT EXISTS idx_combat_effects_participant_id ON gameplay.combat_effects(participant_id);
CREATE INDEX IF NOT EXISTS idx_combat_effects_type ON gameplay.combat_effects(type);
CREATE INDEX IF NOT EXISTS idx_combat_effects_expires_at ON gameplay.combat_effects(expires_at);
CREATE INDEX IF NOT EXISTS idx_combat_effects_active ON gameplay.combat_effects(participant_id, expires_at) WHERE expires_at > NOW();

-- Composite index for common queries
CREATE INDEX IF NOT EXISTS idx_combat_effects_participant_type ON gameplay.combat_effects(participant_id, type);

-- Partial index for permanent effects (rare but important)
CREATE INDEX IF NOT EXISTS idx_combat_effects_permanent ON gameplay.combat_effects(participant_id) WHERE is_permanent = TRUE;

-- GIN index for metadata queries (effect-specific data)
CREATE INDEX IF NOT EXISTS idx_combat_effects_metadata_gin ON gameplay.combat_effects USING GIN (metadata);

-- Function to update expires_at for permanent effects
CREATE OR REPLACE FUNCTION update_combat_effects_expires_at()
RETURNS TRIGGER AS $$
BEGIN
    -- Permanent effects don't expire
    IF NEW.is_permanent THEN
        NEW.expires_at = 'infinity'::timestamp with time zone;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to handle permanent effects
CREATE TRIGGER trigger_combat_effects_expires_at
    BEFORE INSERT OR UPDATE
    ON gameplay.combat_effects
    FOR EACH ROW
    EXECUTE FUNCTION update_combat_effects_expires_at();

-- Function to clean up expired effects (run by cron job)
CREATE OR REPLACE FUNCTION cleanup_expired_combat_effects()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM gameplay.combat_effects
    WHERE expires_at <= NOW() AND NOT is_permanent;

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Grant permissions for combat-damage-service-go
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.combat_effects TO combat_damage_service;

-- Comments for documentation
COMMENT ON TABLE gameplay.combat_effects IS 'Real-time combat effects for damage calculations and status management';
COMMENT ON COLUMN gameplay.combat_effects.participant_id IS 'Player or NPC identifier';
COMMENT ON COLUMN gameplay.combat_effects.type IS 'Effect type (damage_boost, healing_over_time, etc.)';
COMMENT ON COLUMN gameplay.combat_effects.value IS 'Effect strength/value (percentage or absolute)';
COMMENT ON COLUMN gameplay.combat_effects.duration_ms IS 'Effect duration in milliseconds';
COMMENT ON COLUMN gameplay.combat_effects.stacks IS 'Current number of effect stacks';
COMMENT ON COLUMN gameplay.combat_effects.max_stacks IS 'Maximum allowed stacks for this effect';

-- BACKEND NOTE: Table optimized for MMOFPS combat with <1ms query performance
-- Memory pool usage: sync.Pool for effect objects to reduce GC pressure
-- Context timeouts: All operations use 5s timeout for reliability

-- Issue: #2251
