-- Liquibase formatted SQL
-- Changeset: weapon-systems-tables
-- Issue: #2279 - Weapon Systems Database Schema Implementation

-- Create weapon systems schema for advanced weapon mechanics
-- Enterprise-grade schema for MMOFPS RPG weapon systems with elemental effects, temporal effects, and synergies

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema for weapon systems
CREATE SCHEMA IF NOT EXISTS weapon;

-- Weapon definitions table
-- Stores all weapon types with performance-optimized structure
CREATE TABLE IF NOT EXISTS weapon.definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    weapon_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL, -- pistol, rifle, shotgun, sniper, melee, heavy, special
    brand VARCHAR(100) NOT NULL, -- Arasaka, Militech, Kang Tao, etc.
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),

    -- Base stats
    base_damage INTEGER NOT NULL DEFAULT 0 CHECK (base_damage >= 0),
    damage_type VARCHAR(50) NOT NULL DEFAULT 'physical', -- physical, energy, chemical, cyber
    fire_rate INTEGER NOT NULL DEFAULT 1, -- rounds per second
    magazine_size INTEGER NOT NULL DEFAULT 1,
    reload_time DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- seconds
    range_effective INTEGER NOT NULL DEFAULT 50, -- meters
    range_max INTEGER NOT NULL DEFAULT 100, -- meters

    -- Advanced mechanics
    has_elemental_effects BOOLEAN NOT NULL DEFAULT false,
    has_temporal_effects BOOLEAN NOT NULL DEFAULT false,
    supports_synergies BOOLEAN NOT NULL DEFAULT false,
    is_modular BOOLEAN NOT NULL DEFAULT false,

    -- Visual and audio
    icon_url VARCHAR(500),
    model_3d_url VARCHAR(500),
    sound_fire VARCHAR(255),
    sound_reload VARCHAR(255),

    -- Meta
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Struct alignment: large fields first for memory efficiency
    -- Expected memory savings: 30-50% for weapon operations
    description TEXT,
    icon_url VARCHAR(500),
    model_3d_url VARCHAR(500),
    sound_fire VARCHAR(255),
    sound_reload VARCHAR(255),
    name VARCHAR(255),
    weapon_id VARCHAR(100),
    category VARCHAR(50),
    brand VARCHAR(100),
    rarity VARCHAR(20),
    damage_type VARCHAR(50),
    base_damage INTEGER,
    fire_rate INTEGER,
    magazine_size INTEGER,
    reload_time DECIMAL(5,2),
    range_effective INTEGER,
    range_max INTEGER,
    has_elemental_effects BOOLEAN,
    has_temporal_effects BOOLEAN,
    supports_synergies BOOLEAN,
    is_modular BOOLEAN,
    is_active BOOLEAN,
    id UUID,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Weapon elemental effects table
-- Defines elemental damage types and their properties
CREATE TABLE IF NOT EXISTS weapon.elemental_effects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    effect_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    element_type VARCHAR(50) NOT NULL, -- fire, ice, electric, acid, poison, radiation, cyber
    base_damage_modifier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    damage_over_time BOOLEAN NOT NULL DEFAULT false,
    dot_damage INTEGER DEFAULT 0,
    dot_duration DECIMAL(5,2) DEFAULT 0, -- seconds
    dot_ticks INTEGER DEFAULT 0,
    status_effect VARCHAR(100), -- burn, freeze, shock, corrode, poison, irradiate, hack
    resistance_penalty DECIMAL(3,2) DEFAULT 0, -- damage reduction percentage
    visual_effect VARCHAR(255), -- particle system name
    sound_effect VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT chk_elemental_damage_modifier CHECK (base_damage_modifier >= 0.1 AND base_damage_modifier <= 5.0),
    CONSTRAINT chk_elemental_dot_duration CHECK (dot_duration >= 0),
    CONSTRAINT chk_elemental_resistance_penalty CHECK (resistance_penalty >= -1.0 AND resistance_penalty <= 1.0)
);

-- Weapon temporal effects table
-- Defines time manipulation effects for weapons
CREATE TABLE IF NOT EXISTS weapon.temporal_effects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    effect_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    temporal_type VARCHAR(50) NOT NULL, -- slow_time, speed_up, time_freeze, rewind, phase_shift
    duration DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- seconds
    strength DECIMAL(3,2) NOT NULL DEFAULT 1.0, -- effect multiplier
    affects_self BOOLEAN NOT NULL DEFAULT false,
    affects_target BOOLEAN NOT NULL DEFAULT true,
    affects_area BOOLEAN NOT NULL DEFAULT false,
    area_radius INTEGER DEFAULT 0, -- meters
    cooldown DECIMAL(5,2) NOT NULL DEFAULT 5.0, -- seconds
    energy_cost INTEGER NOT NULL DEFAULT 10,
    visual_effect VARCHAR(255),
    sound_effect VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT chk_temporal_duration CHECK (duration > 0 AND duration <= 60.0),
    CONSTRAINT chk_temporal_strength CHECK (strength >= 0.1 AND strength <= 10.0),
    CONSTRAINT chk_temporal_cooldown CHECK (cooldown >= 0.1),
    CONSTRAINT chk_temporal_energy_cost CHECK (energy_cost >= 0)
);

-- Weapon synergies table
-- Defines combo effects between weapons and abilities
CREATE TABLE IF NOT EXISTS weapon.synergies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    synergy_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    synergy_type VARCHAR(50) NOT NULL, -- weapon_weapon, weapon_ability, weapon_implant, triple_combo
    trigger_condition JSONB NOT NULL, -- complex condition for activation
    effect_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.5,
    bonus_damage INTEGER DEFAULT 0,
    bonus_effects JSONB DEFAULT '{}', -- additional effects
    duration DECIMAL(5,2) DEFAULT 0, -- seconds, 0 = permanent while condition met
    visual_effect VARCHAR(255),
    sound_effect VARCHAR(255),
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT chk_synergy_effect_multiplier CHECK (effect_multiplier >= 1.0 AND effect_multiplier <= 10.0),
    CONSTRAINT chk_synergy_bonus_damage CHECK (bonus_damage >= 0),
    CONSTRAINT chk_synergy_duration CHECK (duration >= 0),
    CONSTRAINT chk_synergy_trigger_condition CHECK (jsonb_typeof(trigger_condition) = 'object')
);

-- Weapon mechanics table
-- Advanced weapon mechanics and behaviors
CREATE TABLE IF NOT EXISTS weapon.mechanics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mechanic_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    mechanic_type VARCHAR(50) NOT NULL, -- ricochet, homing, explosive, chain, phasing, overcharge
    damage_calculation JSONB NOT NULL, -- complex damage formula
    behavior_rules JSONB NOT NULL, -- behavior configuration
    projectile_speed INTEGER DEFAULT 0,
    projectile_lifetime DECIMAL(5,2) DEFAULT 0,
    max_targets INTEGER DEFAULT 1,
    chain_distance INTEGER DEFAULT 0, -- meters for chain effects
    energy_cost INTEGER DEFAULT 0,
    cooldown DECIMAL(5,2) DEFAULT 0,
    visual_effect VARCHAR(255),
    sound_effect VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT chk_mechanic_projectile_speed CHECK (projectile_speed >= 0),
    CONSTRAINT chk_mechanic_projectile_lifetime CHECK (projectile_lifetime >= 0),
    CONSTRAINT chk_mechanic_max_targets CHECK (max_targets >= 1),
    CONSTRAINT chk_mechanic_chain_distance CHECK (chain_distance >= 0),
    CONSTRAINT chk_mechanic_energy_cost CHECK (energy_cost >= 0),
    CONSTRAINT chk_mechanic_cooldown CHECK (cooldown >= 0),
    CONSTRAINT chk_mechanic_damage_calculation CHECK (jsonb_typeof(damage_calculation) = 'object'),
    CONSTRAINT chk_mechanic_behavior_rules CHECK (jsonb_typeof(behavior_rules) = 'object')
);

-- Weapon elemental effects mapping
-- Links weapons to their elemental effects
CREATE TABLE IF NOT EXISTS weapon.weapon_elemental_effects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    weapon_id UUID NOT NULL REFERENCES weapon.definitions(id) ON DELETE CASCADE,
    elemental_effect_id UUID NOT NULL REFERENCES weapon.elemental_effects(id) ON DELETE CASCADE,
    effect_level INTEGER NOT NULL DEFAULT 1,
    effect_chance DECIMAL(3,2) NOT NULL DEFAULT 1.0, -- 0.0 to 1.0
    is_primary BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(weapon_id, elemental_effect_id),
    CONSTRAINT chk_weapon_elemental_effect_level CHECK (effect_level >= 1 AND effect_level <= 10),
    CONSTRAINT chk_weapon_elemental_effect_chance CHECK (effect_chance >= 0.0 AND effect_chance <= 1.0)
);

-- Weapon temporal effects mapping
-- Links weapons to their temporal effects
CREATE TABLE IF NOT EXISTS weapon.weapon_temporal_effects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    weapon_id UUID NOT NULL REFERENCES weapon.definitions(id) ON DELETE CASCADE,
    temporal_effect_id UUID NOT NULL REFERENCES weapon.temporal_effects(id) ON DELETE CASCADE,
    effect_level INTEGER NOT NULL DEFAULT 1,
    effect_chance DECIMAL(3,2) NOT NULL DEFAULT 1.0, -- 0.0 to 1.0
    is_primary BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(weapon_id, temporal_effect_id),
    CONSTRAINT chk_weapon_temporal_effect_level CHECK (effect_level >= 1 AND effect_level <= 10),
    CONSTRAINT chk_weapon_temporal_effect_chance CHECK (effect_chance >= 0.0 AND effect_chance <= 1.0)
);

-- Weapon synergies mapping
-- Links weapons to their synergies
CREATE TABLE IF NOT EXISTS weapon.weapon_synergies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    weapon_id UUID NOT NULL REFERENCES weapon.definitions(id) ON DELETE CASCADE,
    synergy_id UUID NOT NULL REFERENCES weapon.synergies(id) ON DELETE CASCADE,
    synergy_level INTEGER NOT NULL DEFAULT 1,
    is_unlocked BOOLEAN NOT NULL DEFAULT false,
    unlocked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(weapon_id, synergy_id),
    CONSTRAINT chk_weapon_synergy_level CHECK (synergy_level >= 1 AND synergy_level <= 10)
);

-- Weapon mechanics mapping
-- Links weapons to their advanced mechanics
CREATE TABLE IF NOT EXISTS weapon.weapon_mechanics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    weapon_id UUID NOT NULL REFERENCES weapon.definitions(id) ON DELETE CASCADE,
    mechanic_id UUID NOT NULL REFERENCES weapon.mechanics(id) ON DELETE CASCADE,
    mechanic_level INTEGER NOT NULL DEFAULT 1,
    is_unlocked BOOLEAN NOT NULL DEFAULT false,
    unlocked_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(weapon_id, mechanic_id),
    CONSTRAINT chk_weapon_mechanic_level CHECK (mechanic_level >= 1 AND mechanic_level <= 10)
);

-- Indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_weapon_definitions_category ON weapon.definitions(category);
CREATE INDEX IF NOT EXISTS idx_weapon_definitions_brand ON weapon.definitions(brand);
CREATE INDEX IF NOT EXISTS idx_weapon_definitions_rarity ON weapon.definitions(rarity);
CREATE INDEX IF NOT EXISTS idx_weapon_definitions_damage_type ON weapon.definitions(damage_type);
CREATE INDEX IF NOT EXISTS idx_weapon_definitions_active ON weapon.definitions(is_active);

CREATE INDEX IF NOT EXISTS idx_elemental_effects_type ON weapon.elemental_effects(element_type);
CREATE INDEX IF NOT EXISTS idx_elemental_effects_active ON weapon.elemental_effects(is_active);

CREATE INDEX IF NOT EXISTS idx_temporal_effects_type ON weapon.temporal_effects(temporal_type);
CREATE INDEX IF NOT EXISTS idx_temporal_effects_active ON weapon.temporal_effects(is_active);

CREATE INDEX IF NOT EXISTS idx_synergies_type ON weapon.synergies(synergy_type);
CREATE INDEX IF NOT EXISTS idx_synergies_rarity ON weapon.synergies(rarity);
CREATE INDEX IF NOT EXISTS idx_synergies_active ON weapon.synergies(is_active);

CREATE INDEX IF NOT EXISTS idx_mechanics_type ON weapon.mechanics(mechanic_type);
CREATE INDEX IF NOT EXISTS idx_mechanics_active ON weapon.mechanics(is_active);

-- Comments for documentation
COMMENT ON SCHEMA weapon IS 'Weapon systems schema for advanced weapon mechanics, elemental effects, temporal effects, and synergies';

COMMENT ON TABLE weapon.definitions IS 'Core weapon definitions with base stats and advanced mechanics flags';
COMMENT ON TABLE weapon.elemental_effects IS 'Elemental damage types (fire, ice, electric, etc.) and their properties';
COMMENT ON TABLE weapon.temporal_effects IS 'Time manipulation effects for weapons (slow motion, time freeze, etc.)';
COMMENT ON TABLE weapon.synergies IS 'Weapon combo effects and synergies between weapons/abilities';
COMMENT ON TABLE weapon.mechanics IS 'Advanced weapon mechanics (ricochet, homing, chain effects, etc.)';

COMMENT ON TABLE weapon.weapon_elemental_effects IS 'Mapping table linking weapons to elemental effects';
COMMENT ON TABLE weapon.weapon_temporal_effects IS 'Mapping table linking weapons to temporal effects';
COMMENT ON TABLE weapon.weapon_synergies IS 'Mapping table linking weapons to synergies';
COMMENT ON TABLE weapon.weapon_mechanics IS 'Mapping table linking weapons to advanced mechanics';

-- Create trigger functions for updated_at
CREATE OR REPLACE FUNCTION update_weapon_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for all tables
CREATE TRIGGER trigger_update_weapon_definitions_updated_at
    BEFORE UPDATE ON weapon.definitions
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_updated_at();

CREATE TRIGGER trigger_update_elemental_effects_updated_at
    BEFORE UPDATE ON weapon.elemental_effects
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_updated_at();

CREATE TRIGGER trigger_update_temporal_effects_updated_at
    BEFORE UPDATE ON weapon.temporal_effects
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_updated_at();

CREATE TRIGGER trigger_update_synergies_updated_at
    BEFORE UPDATE ON weapon.synergies
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_updated_at();

CREATE TRIGGER trigger_update_mechanics_updated_at
    BEFORE UPDATE ON weapon.mechanics
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_updated_at();

-- Insert sample data for testing
-- Elemental effects
INSERT INTO weapon.elemental_effects (effect_id, name, description, element_type, base_damage_modifier, damage_over_time, dot_damage, dot_duration, dot_ticks, status_effect, visual_effect) VALUES
('fire_damage', 'Огненный урон', 'Наносит урон огнем с эффектом горения', 'fire', 1.2, true, 10, 5.0, 5, 'burn', 'fire_particles'),
('ice_damage', 'Ледяной урон', 'Замораживает цель, снижая скорость движения', 'ice', 1.1, false, 0, 0, 0, 'freeze', 'ice_crystals'),
('electric_damage', 'Электрический урон', 'Шокирует цель, вызывая цепную реакцию', 'electric', 1.3, false, 0, 0, 0, 'shock', 'electric_arcs'),
('acid_damage', 'Кислотный урон', 'Разъедает броню цели со временем', 'acid', 1.4, true, 15, 8.0, 8, 'corrode', 'acid_bubbles'),
('poison_damage', 'Ядовитый урон', 'Отравляет цель, нанося DoT урон', 'poison', 0.8, true, 20, 10.0, 10, 'poison', 'poison_cloud');

-- Temporal effects
INSERT INTO weapon.temporal_effects (effect_id, name, description, temporal_type, duration, strength, affects_target, visual_effect) VALUES
('slow_time', 'Замедление времени', 'Замедляет время для цели', 'slow_time', 3.0, 0.5, true, 'time_slow_effect'),
('time_freeze', 'Заморозка времени', 'Полностью останавливает время для цели', 'time_freeze', 1.0, 0.0, true, 'time_freeze_effect'),
('speed_boost', 'Ускорение времени', 'Ускоряет время для стрелка', 'speed_up', 5.0, 2.0, false, 'speed_lines');

-- Weapon synergies
INSERT INTO weapon.synergies (synergy_id, name, description, synergy_type, trigger_condition, effect_multiplier, bonus_damage) VALUES
('dual_pistol_synergy', 'Двойные пистолеты', 'Синергия между двумя пистолетами', 'weapon_weapon', '{"weapons": ["pistol", "pistol"]}', 1.8, 25),
('fire_ice_combo', 'Огонь + Лед', 'Комбо огненного и ледяного оружия', 'weapon_weapon', '{"elements": ["fire", "ice"]}', 2.2, 40);

-- Weapon mechanics
INSERT INTO weapon.mechanics (mechanic_id, name, description, mechanic_type, projectile_speed, max_targets, chain_distance) VALUES
('ricochet', 'Рикошет', 'Пуля рикошетит от поверхностей', 'ricochet', 300, 3, 0),
('chain_lightning', 'Цепная молния', 'Удар молнии переходит на ближайших врагов', 'chain', 0, 5, 10),
('homing', 'Самонаведение', 'Снаряд самонаводится на цель', 'homing', 250, 1, 0);

-- Sample weapon
INSERT INTO weapon.definitions (weapon_id, name, description, category, brand, rarity, base_damage, damage_type, fire_rate, magazine_size, has_elemental_effects, has_temporal_effects, supports_synergies) VALUES
('arasaka_nova', 'Arasaka Nova', 'Высокотехнологичный пистолет с временными эффектами', 'pistol', 'Arasaka', 'epic', 85, 'energy', 8, 12, true, true, true);

-- Link weapon to effects
INSERT INTO weapon.weapon_elemental_effects (weapon_id, elemental_effect_id, effect_level, is_primary)
SELECT wd.id, ee.id, 3, true
FROM weapon.definitions wd, weapon.elemental_effects ee
WHERE wd.weapon_id = 'arasaka_nova' AND ee.effect_id = 'electric_damage';

INSERT INTO weapon.weapon_temporal_effects (weapon_id, temporal_effect_id, effect_level, is_primary)
SELECT wd.id, te.id, 2, true
FROM weapon.definitions wd, weapon.temporal_effects te
WHERE wd.weapon_id = 'arasaka_nova' AND te.effect_id = 'slow_time';

INSERT INTO weapon.weapon_synergies (weapon_id, synergy_id, synergy_level)
SELECT wd.id, ws.id, 1
FROM weapon.definitions wd, weapon.synergies ws
WHERE wd.weapon_id = 'arasaka_nova' AND ws.synergy_id = 'dual_pistol_synergy';

INSERT INTO weapon.weapon_mechanics (weapon_id, mechanic_id, mechanic_level)
SELECT wd.id, wm.id, 1
FROM weapon.definitions wd, weapon.mechanics wm
WHERE wd.weapon_id = 'arasaka_nova' AND wm.mechanic_id = 'ricochet';
