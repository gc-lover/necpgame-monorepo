-- Engram Compatibility Tables for Character Engram Compatibility Service Go
-- BACKEND NOTE: High-performance tables for real-time engram compatibility calculations
-- Optimized for 1000+ RPS compatibility checks with zero allocations in hot path

-- Table for tracking active engrams per character
CREATE TABLE IF NOT EXISTS gameplay.character_active_engrams
(
    character_id UUID NOT NULL,
    engram_id UUID NOT NULL,
    slot_number INTEGER NOT NULL CHECK (slot_number BETWEEN 1 AND 3),
    installed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    influence_level DECIMAL(5,2) NOT NULL DEFAULT 0 CHECK (influence_level BETWEEN -100 AND 100),
    usage_points INTEGER NOT NULL DEFAULT 0,
    last_used_at TIMESTAMP WITH TIME ZONE,

    PRIMARY KEY (character_id, slot_number),
    FOREIGN KEY (character_id) REFERENCES gameplay.characters(id) ON DELETE CASCADE,

    -- PERFORMANCE: Optimized for active engram queries
    INDEX idx_character_active_engrams_character_id (character_id),
    INDEX idx_character_active_engrams_engram_id (engram_id),
    INDEX idx_character_active_engrams_slot_number (slot_number)
);

-- Table for engram conflict tracking
CREATE TABLE IF NOT EXISTS gameplay.engram_conflicts
(
    conflict_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    engram_1_id UUID NOT NULL,
    engram_2_id UUID NOT NULL,
    conflict_type VARCHAR(50) NOT NULL CHECK (conflict_type IN ('dominance_struggle', 'value_conflict', 'reputation_conflict', 'usage_imbalance')),
    conflict_level INTEGER NOT NULL CHECK (conflict_level BETWEEN 1 AND 10),
    usage_points_diff INTEGER NOT NULL DEFAULT 0,
    started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,

    FOREIGN KEY (character_id) REFERENCES gameplay.characters(id) ON DELETE CASCADE,

    -- PERFORMANCE: Optimized for active conflict queries and resolution
    INDEX idx_engram_conflicts_character_id (character_id),
    INDEX idx_engram_conflicts_active (character_id, is_active) WHERE is_active = TRUE,
    INDEX idx_engram_conflicts_started_at (started_at DESC),
    INDEX idx_engram_conflicts_engram_pair (character_id, engram_1_id, engram_2_id)
);

-- Table for conflict event history
CREATE TABLE IF NOT EXISTS gameplay.engram_conflict_events
(
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL,
    conflict_type VARCHAR(50) NOT NULL CHECK (conflict_type IN ('dominance_struggle', 'temporary_takeover', 'mental_breakdown')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    event_data JSONB,
    engram_ids UUID[] NOT NULL,

    FOREIGN KEY (character_id) REFERENCES gameplay.characters(id) ON DELETE CASCADE,

    -- PERFORMANCE: Optimized for event history queries
    INDEX idx_engram_conflict_events_character_id (character_id),
    INDEX idx_engram_conflict_events_created_at (created_at DESC),
    INDEX idx_engram_conflict_events_type (conflict_type),
    INDEX idx_engram_conflict_events_engram_ids USING GIN (engram_ids)
);

-- Table for engram compatibility cache (performance optimization)
CREATE TABLE IF NOT EXISTS gameplay.engram_compatibility_cache
(
    cache_key VARCHAR(128) PRIMARY KEY,
    character_id UUID NOT NULL,
    engram_ids UUID[] NOT NULL,
    compatibility_level VARCHAR(50) NOT NULL,
    compatibility_percentage DECIMAL(5,2) NOT NULL,
    synergy_bonus DECIMAL(5,2) NOT NULL,
    cached_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,

    FOREIGN KEY (character_id) REFERENCES gameplay.characters(id) ON DELETE CASCADE,

    -- PERFORMANCE: Optimized for cache lookups and cleanup
    INDEX idx_engram_compatibility_cache_character_id (character_id),
    INDEX idx_engram_compatibility_cache_expires_at (expires_at),
    INDEX idx_engram_compatibility_cache_engram_ids USING GIN (engram_ids)
);

-- Function to calculate engram compatibility score (for triggers and queries)
CREATE OR REPLACE FUNCTION gameplay.calculate_engram_compatibility_score(
    p_character_id UUID,
    p_engram_1_id UUID,
    p_engram_2_id UUID
) RETURNS DECIMAL(5,2) AS $$
DECLARE
    v_score DECIMAL(5,2) := 50.0;
    v_rep_score DECIMAL(5,2) := 0.0;
    v_values_score DECIMAL(5,2) := 0.0;
BEGIN
    -- Reputation compatibility (mock implementation - would query actual reputation data)
    -- This is a placeholder for the actual reputation calculation logic
    v_rep_score := 5.0; -- Mock score

    -- Values compatibility (mock implementation)
    v_values_score := 10.0; -- Mock score

    -- Calculate final score
    v_score := v_score + v_rep_score + v_values_score;

    -- Clamp to valid range
    RETURN GREATEST(-50.0, LEAST(50.0, v_score));
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to automatically resolve expired conflicts
CREATE OR REPLACE FUNCTION gameplay.auto_resolve_expired_conflicts()
RETURNS INTEGER AS $$
DECLARE
    v_resolved_count INTEGER := 0;
BEGIN
    -- Mark conflicts as resolved if they've been active for more than 24 hours
    UPDATE gameplay.engram_conflicts
    SET is_active = FALSE,
        resolved_at = CURRENT_TIMESTAMP
    WHERE is_active = TRUE
      AND started_at < CURRENT_TIMESTAMP - INTERVAL '24 hours';

    GET DIAGNOSTICS v_resolved_count = ROW_COUNT;
    RETURN v_resolved_count;
END;
$$ LANGUAGE plpgsql;

-- Function to clean expired compatibility cache
CREATE OR REPLACE FUNCTION gameplay.clean_expired_compatibility_cache()
RETURNS INTEGER AS $$
DECLARE
    v_deleted_count INTEGER := 0;
BEGIN
    DELETE FROM gameplay.engram_compatibility_cache
    WHERE expires_at < CURRENT_TIMESTAMP;

    GET DIAGNOSTICS v_deleted_count = ROW_COUNT;
    RETURN v_deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update resolved_at when conflict becomes inactive
CREATE OR REPLACE FUNCTION gameplay.update_conflict_resolved_at()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.is_active = TRUE AND NEW.is_active = FALSE AND NEW.resolved_at IS NULL THEN
        NEW.resolved_at = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_engram_conflicts_resolved_at
    BEFORE UPDATE ON gameplay.engram_conflicts
    FOR EACH ROW
    EXECUTE FUNCTION gameplay.update_conflict_resolved_at();

-- Performance comments:
-- - Tables use UUID primary keys for global uniqueness
-- - Foreign key constraints ensure data integrity
-- - Indexes optimized for common query patterns
-- - JSONB used for flexible event data storage
-- - Array type used for engram ID lists (PostgreSQL specific)
-- - Functions provide reusable compatibility calculations
-- - Triggers maintain data consistency automatically
-- - Cache table prevents redundant calculations
-- - Cleanup functions help maintain performance over time
