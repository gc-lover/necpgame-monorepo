-- Liquibase formatted SQL
-- Changeset: periodic-effects-table
-- Issue: #1495 - Gameplay Affixes Service implementation

-- Create periodic effects table for timer-based affix effects
CREATE TABLE IF NOT EXISTS gameplay.periodic_effects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    affix_id VARCHAR(255) NOT NULL,
    target_id VARCHAR(255) NOT NULL, -- Player, NPC, or entity ID
    effect_type VARCHAR(100) NOT NULL,
    parameters JSONB NOT NULL DEFAULT '{}',
    interval_seconds INTEGER NOT NULL CHECK (interval_seconds > 0),
    next_tick TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_periodic_effects_target_active (target_id, active),
    INDEX idx_periodic_effects_next_tick (next_tick) WHERE active = true,
    INDEX idx_periodic_effects_affix (affix_id),

    -- Foreign key constraint (assuming affixes table exists)
    CONSTRAINT fk_periodic_effects_affix FOREIGN KEY (affix_id)
        REFERENCES gameplay.affixes(id) ON DELETE CASCADE
);

-- Comments for documentation
COMMENT ON TABLE gameplay.periodic_effects IS 'Stores timer-based affix effects that need periodic processing';
COMMENT ON COLUMN gameplay.periodic_effects.affix_id IS 'Reference to the affix definition';
COMMENT ON COLUMN gameplay.periodic_effects.target_id IS 'ID of the entity affected by the periodic effect';
COMMENT ON COLUMN gameplay.periodic_effects.effect_type IS 'Type of periodic effect (e.g., damage_over_time, heal_over_time)';
COMMENT ON COLUMN gameplay.periodic_effects.parameters IS 'Effect-specific parameters as JSON';
COMMENT ON COLUMN gameplay.periodic_effects.interval_seconds IS 'How often the effect should trigger in seconds';
COMMENT ON COLUMN gameplay.periodic_effects.next_tick IS 'Next time this effect should be processed';
COMMENT ON COLUMN gameplay.periodic_effects.end_time IS 'When the periodic effect should stop (optional)';
COMMENT ON COLUMN gameplay.periodic_effects.active IS 'Whether the effect is still active';

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_periodic_effects_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_periodic_effects_updated_at
    BEFORE UPDATE ON gameplay.periodic_effects
    FOR EACH ROW
    EXECUTE FUNCTION update_periodic_effects_updated_at();

-- Performance optimization: Partition by target_id if needed for high volume
-- This can be added later if periodic effects become performance bottleneck
