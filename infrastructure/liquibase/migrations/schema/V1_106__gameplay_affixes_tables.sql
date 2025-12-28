-- Gameplay Affixes Service Database Schema
-- Enterprise-grade schema for MMOFPS RPG affix system
-- Issue: #1495 - Gameplay Affixes Service implementation

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS gameplay;

-- Affixes table
-- Stores all available affixes with their properties and effects
CREATE TABLE IF NOT EXISTS gameplay.affixes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('combat', 'environmental', 'debuff', 'defensive')),
    reward_modifier DECIMAL(3,2) NOT NULL DEFAULT 1.00 CHECK (reward_modifier >= 1.00),
    difficulty_modifier DECIMAL(3,2) NOT NULL DEFAULT 1.00 CHECK (difficulty_modifier >= 1.00),
    mechanics JSONB, -- Flexible storage for affix mechanics (triggers, effects, etc.)
    visual_effects JSONB, -- Visual effects configuration
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Performance indexes
    INDEX idx_affixes_category (category),
    INDEX idx_affixes_created_at (created_at DESC)
);

-- Affix rotations table
-- Tracks weekly affix rotations and their schedules
CREATE TABLE IF NOT EXISTS gameplay.affix_rotations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    week_start TIMESTAMP WITH TIME ZONE NOT NULL,
    week_end TIMESTAMP WITH TIME ZONE NOT NULL,
    seasonal_affix_id UUID REFERENCES gameplay.affixes(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CHECK (week_end > week_start),
    UNIQUE (week_start, week_end),

    -- Performance indexes
    INDEX idx_affix_rotations_week_start (week_start),
    INDEX idx_affix_rotations_week_end (week_end),
    INDEX idx_affix_rotations_current (week_start, week_end) WHERE week_end > NOW()
);

-- Active affixes table
-- Links affixes to their rotation periods
CREATE TABLE IF NOT EXISTS gameplay.active_affixes (
    rotation_id UUID NOT NULL REFERENCES gameplay.affix_rotations(id) ON DELETE CASCADE,
    affix_id UUID NOT NULL REFERENCES gameplay.affixes(id) ON DELETE CASCADE,

    PRIMARY KEY (rotation_id, affix_id),

    -- Performance indexes
    INDEX idx_active_affixes_affix_id (affix_id),
    INDEX idx_active_affixes_rotation_id (rotation_id)
);

-- Instance affixes table
-- Tracks which affixes are applied to specific dungeon/raids instances
CREATE TABLE IF NOT EXISTS gameplay.instance_affixes (
    instance_id UUID NOT NULL, -- References game instances (not enforced for flexibility)
    affix_id UUID NOT NULL REFERENCES gameplay.affixes(id) ON DELETE CASCADE,
    applied_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    PRIMARY KEY (instance_id, affix_id),

    -- Performance indexes
    INDEX idx_instance_affixes_instance_id (instance_id),
    INDEX idx_instance_affixes_affix_id (affix_id),
    INDEX idx_instance_affixes_applied_at (applied_at DESC)
);

-- Insert sample affixes for testing
-- Combat affixes
INSERT INTO gameplay.affixes (id, name, description, category, reward_modifier, difficulty_modifier, mechanics, visual_effects) VALUES
(uuid_generate_v4(), 'Volatile', 'Враги взрываются при смерти, нанося урон в радиусе', 'combat', 1.25, 1.15,
 '{"trigger": "enemy_death", "effect_type": "area_damage", "radius": 5.0, "damage_percent": 50, "damage_type": "fire"}',
 '{"explosion_particle": "volatile_explosion", "sound_effect": "volatile_explode", "screen_shake": true}'),

(uuid_generate_v4(), 'Raging', 'Враги наносят на 25% больше урона', 'combat', 1.35, 1.25,
 '{"trigger": "damage_dealt", "effect_type": "damage_multiplier", "multiplier": 1.25}',
 '{"color_tint": "red", "particle_effect": "rage_aura"}'),

(uuid_generate_v4(), 'Necrotic', 'Враги накладывают эффект некроза, снижающий максимальное HP', 'combat', 1.20, 1.10,
 '{"trigger": "damage_taken", "effect_type": "debuff", "debuff_type": "necrotic", "duration": 10}',
 '{"particle_effect": "necrotic_cloud", "color_tint": "purple"}'),

-- Defensive affixes
(uuid_generate_v4(), 'Fortified', 'Враги имеют +50% HP', 'defensive', 1.35, 1.25,
 '{"trigger": "spawn", "effect_type": "health_multiplier", "multiplier": 1.5}',
 '{"particle_effect": "fortified_aura", "color_tint": "blue"}'),

(uuid_generate_v4(), 'Bolstering', 'Враги становятся сильнее при смерти союзников', 'defensive', 1.30, 1.20,
 '{"trigger": "ally_death", "effect_type": "buff_allies", "buff_type": "damage_boost", "boost_percent": 25}',
 '{"particle_effect": "bolstering_wave", "sound_effect": "power_up"}'),

-- Environmental affixes
(uuid_generate_v4(), 'Frostbite', 'Периодически накладывает замедление на игроков', 'environmental', 1.50, 1.40,
 '{"trigger": "timer", "effect_type": "periodic_debuff", "interval": 30, "debuff_type": "slow", "duration": 8}',
 '{"particle_effect": "frost_cloud", "color_tint": "cyan", "screen_shake": false}'),

(uuid_generate_v4(), 'Heat Wave', 'Повышенная температура вызывает урон со временем', 'environmental', 1.40, 1.30,
 '{"trigger": "timer", "effect_type": "dot_damage", "interval": 5, "damage": 100, "damage_type": "fire"}',
 '{"particle_effect": "heat_waves", "color_tint": "orange"}'),

-- Debuff affixes
(uuid_generate_v4(), 'Afflicted', 'Враги накладывают случайные дебаффы', 'debuff', 1.28, 1.18,
 '{"trigger": "damage_taken", "effect_type": "random_debuff", "debuffs": ["poison", "curse", "weakness"]}',
 '{"particle_effect": "afflicted_sparks", "color_tint": "green"}'),

(uuid_generate_v4(), 'Sanguine', 'Враги оставляют лужи крови, замедляющие игроков', 'debuff', 1.22, 1.12,
 '{"trigger": "death", "effect_type": "blood_pool", "slow_percent": 30, "duration": 15}',
 '{"particle_effect": "blood_pool", "color_tint": "dark_red"}'),

(uuid_generate_v4(), 'Storming', 'Молнии периодически бьют по случайным игрокам', 'environmental', 1.45, 1.35,
 '{"trigger": "timer", "effect_type": "lightning_strike", "interval": 20, "targets": "random_players", "damage": 200}',
 '{"particle_effect": "lightning_strike", "sound_effect": "thunder", "screen_shake": true}')
ON CONFLICT DO NOTHING;

-- Comments for documentation
COMMENT ON TABLE gameplay.affixes IS 'Stores all available affixes with their properties and effects';
COMMENT ON TABLE gameplay.affix_rotations IS 'Tracks weekly affix rotations and their schedules';
COMMENT ON TABLE gameplay.active_affixes IS 'Links affixes to their rotation periods';
COMMENT ON TABLE gameplay.instance_affixes IS 'Tracks which affixes are applied to specific dungeon/raids instances';

-- Performance optimization: Create composite indexes for common queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_affix_rotations_current_active
ON gameplay.affix_rotations (week_start, week_end)
WHERE week_end > NOW();

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_instance_affixes_instance_applied
ON gameplay.instance_affixes (instance_id, applied_at DESC);

-- Function to get current active affixes (for performance)
CREATE OR REPLACE FUNCTION gameplay.get_current_active_affixes()
RETURNS TABLE (
    affix_id UUID,
    name VARCHAR(100),
    category VARCHAR(20),
    reward_modifier DECIMAL(3,2),
    difficulty_modifier DECIMAL(3,2)
)
LANGUAGE sql
STABLE
AS $$
    SELECT
        a.id,
        a.name,
        a.category,
        a.reward_modifier,
        a.difficulty_modifier
    FROM gameplay.affixes a
    JOIN gameplay.active_affixes aa ON a.id = aa.affix_id
    JOIN gameplay.affix_rotations ar ON aa.rotation_id = ar.id
    WHERE ar.week_start <= NOW() AND ar.week_end > NOW()
    ORDER BY a.category, a.name;
$$;

-- Function to calculate instance modifiers
CREATE OR REPLACE FUNCTION gameplay.calculate_instance_modifiers(instance_uuid UUID)
RETURNS TABLE (
    total_reward_modifier DECIMAL(5,2),
    total_difficulty_modifier DECIMAL(5,2),
    affix_count INTEGER
)
LANGUAGE sql
STABLE
AS $$
    SELECT
        COALESCE(EXP(SUM(LN(a.reward_modifier))), 1.0)::DECIMAL(5,2),
        COALESCE(EXP(SUM(LN(a.difficulty_modifier))), 1.0)::DECIMAL(5,2),
        COUNT(*)::INTEGER
    FROM gameplay.affixes a
    JOIN gameplay.instance_affixes ia ON a.id = ia.affix_id
    WHERE ia.instance_id = instance_uuid;
$$;
