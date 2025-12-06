-- Issue: #1574
-- liquibase formatted sql

-- changeset database:090-create-weapon-game-mechanics-tables
-- comment: Create tables for weapon game mechanics system (resources, progression, perks, mastery)

-- ============================================================================
-- WEAPON RESOURCES
-- ============================================================================

CREATE TABLE IF NOT EXISTS weapon_resources (
  weapon_instance_id UUID PRIMARY KEY,
  cooldowns JSONB NOT NULL DEFAULT '{}',
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  heat_current DECIMAL(5,2) NOT NULL DEFAULT 0.0,
  energy_current DECIMAL(5,2) NOT NULL DEFAULT 100.0,
  ammunition_current INT NOT NULL DEFAULT 0,
  ammunition_max INT NOT NULL DEFAULT 100,
  CONSTRAINT ammunition_valid CHECK (ammunition_current >= 0 AND ammunition_current <= ammunition_max),
  CONSTRAINT heat_valid CHECK (heat_current >= 0 AND heat_current <= 100),
  CONSTRAINT energy_valid CHECK (energy_current >= 0 AND energy_current <= 100)
);

CREATE INDEX idx_weapon_resources_updated ON weapon_resources(updated_at);

COMMENT ON TABLE weapon_resources IS 'Stores resource state for weapon instances (ammo, heat, energy, cooldowns)';
COMMENT ON COLUMN weapon_resources.ammunition_current IS 'Current ammunition count';
COMMENT ON COLUMN weapon_resources.ammunition_max IS 'Maximum ammunition capacity';
COMMENT ON COLUMN weapon_resources.heat_current IS 'Current heat level (0-100)';
COMMENT ON COLUMN weapon_resources.energy_current IS 'Current energy level (0-100)';
COMMENT ON COLUMN weapon_resources.cooldowns IS 'JSON object with active cooldowns {ability_id: expires_at}';

-- ============================================================================
-- WEAPON UPGRADES
-- ============================================================================

CREATE TABLE IF NOT EXISTS weapon_upgrades (
  weapon_instance_id UUID PRIMARY KEY,
  unlocked_perks UUID[] NOT NULL DEFAULT '{}',
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  upgrade_level INT NOT NULL DEFAULT 0,
  experience INT NOT NULL DEFAULT 0,
  CONSTRAINT upgrade_level_valid CHECK (upgrade_level >= 0 AND upgrade_level <= 10),
  CONSTRAINT experience_valid CHECK (experience >= 0)
);

CREATE INDEX idx_weapon_upgrades_level ON weapon_upgrades(upgrade_level);
CREATE INDEX idx_weapon_upgrades_updated ON weapon_upgrades(updated_at);

COMMENT ON TABLE weapon_upgrades IS 'Stores upgrade progression for weapon instances';
COMMENT ON COLUMN weapon_upgrades.upgrade_level IS 'Current upgrade level (0-10)';
COMMENT ON COLUMN weapon_upgrades.experience IS 'Weapon experience points';
COMMENT ON COLUMN weapon_upgrades.unlocked_perks IS 'Array of unlocked perk IDs for this weapon';

-- ============================================================================
-- WEAPON PERKS (DEFINITIONS)
-- ============================================================================

CREATE TABLE IF NOT EXISTS weapon_perks (
  perk_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  description TEXT NOT NULL,
  weapon_type VARCHAR(50) NOT NULL,
  name VARCHAR(100) NOT NULL,
  effect JSONB NOT NULL,
  unlock_requirements JSONB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  UNIQUE(weapon_type, name)
);

CREATE INDEX idx_weapon_perks_type ON weapon_perks(weapon_type);

COMMENT ON TABLE weapon_perks IS 'Definitions of weapon perks (unlockable abilities for weapon types)';
COMMENT ON COLUMN weapon_perks.weapon_type IS 'Type of weapon (assault_rifle, sniper_rifle, etc.)';
COMMENT ON COLUMN weapon_perks.effect IS 'JSON object describing perk effect {type, value, duration}';
COMMENT ON COLUMN weapon_perks.unlock_requirements IS 'JSON object with requirements {player_level, weapon_mastery, achievement}';

-- ============================================================================
-- WEAPON MASTERY
-- ============================================================================

CREATE TABLE IF NOT EXISTS weapon_mastery (
  player_id UUID NOT NULL,
  weapon_type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
  uses_count INT NOT NULL DEFAULT 0,
  kills_count INT NOT NULL DEFAULT 0,
  mastery_level INT NOT NULL DEFAULT 0,
  PRIMARY KEY (player_id, weapon_type),
  CONSTRAINT uses_valid CHECK (uses_count >= 0),
  CONSTRAINT kills_valid CHECK (kills_count >= 0),
  CONSTRAINT mastery_level_valid CHECK (mastery_level >= 0 AND mastery_level <= 100)
);

CREATE INDEX idx_weapon_mastery_player ON weapon_mastery(player_id);
CREATE INDEX idx_weapon_mastery_type ON weapon_mastery(weapon_type);
CREATE INDEX idx_weapon_mastery_level ON weapon_mastery(mastery_level DESC);

COMMENT ON TABLE weapon_mastery IS 'Tracks player mastery of weapon types';
COMMENT ON COLUMN weapon_mastery.uses_count IS 'Number of times weapon type was used';
COMMENT ON COLUMN weapon_mastery.kills_count IS 'Number of kills with weapon type';
COMMENT ON COLUMN weapon_mastery.mastery_level IS 'Calculated mastery level (0-100)';

-- ============================================================================
-- INITIAL DATA: WEAPON PERKS
-- ============================================================================

-- Assault Rifle Perks
INSERT INTO weapon_perks (weapon_type, name, description, effect, unlock_requirements) VALUES
('assault_rifle', 'Fast Reload', 'Reduce reload time by 30%', 
 '{"type": "reload_speed", "value": 0.3}'::jsonb,
 '{"player_level": 5, "weapon_mastery": 0}'::jsonb),

('assault_rifle', 'Penetration', 'Increase armor penetration by 50%', 
 '{"type": "armor_pierce", "value": 0.5}'::jsonb,
 '{"player_level": 10, "weapon_mastery": 20}'::jsonb),

('assault_rifle', 'Suppression', 'Enemies hit take -20% accuracy for 3 seconds', 
 '{"type": "debuff", "stat": "accuracy", "value": -0.2, "duration": 3}'::jsonb,
 '{"player_level": 15, "weapon_mastery": 40}'::jsonb);

-- Sniper Rifle Perks
INSERT INTO weapon_perks (weapon_type, name, description, effect, unlock_requirements) VALUES
('sniper_rifle', 'Headhunter', 'Increase headshot damage by 100%', 
 '{"type": "headshot_damage", "value": 1.0}'::jsonb,
 '{"player_level": 5, "weapon_mastery": 0}'::jsonb),

('sniper_rifle', 'Hold Breath', 'Increase accuracy by 50% when aiming', 
 '{"type": "accuracy", "value": 0.5, "condition": "aiming"}'::jsonb,
 '{"player_level": 10, "weapon_mastery": 20}'::jsonb),

('sniper_rifle', 'Penetrating Shot', 'Bullets pierce through enemies', 
 '{"type": "pierce", "targets": 3}'::jsonb,
 '{"player_level": 15, "weapon_mastery": 40}'::jsonb);

-- Shotgun Perks
INSERT INTO weapon_perks (weapon_type, name, description, effect, unlock_requirements) VALUES
('shotgun', 'Point Blank', 'Increase damage by 50% at close range', 
 '{"type": "damage", "value": 0.5, "condition": "close_range"}'::jsonb,
 '{"player_level": 5, "weapon_mastery": 0}'::jsonb),

('shotgun', 'Spread Control', 'Reduce spread by 30%', 
 '{"type": "spread", "value": -0.3}'::jsonb,
 '{"player_level": 10, "weapon_mastery": 20}'::jsonb),

('shotgun', 'Knockback', 'Push enemies back on hit', 
 '{"type": "knockback", "force": 500}'::jsonb,
 '{"player_level": 15, "weapon_mastery": 40}'::jsonb);

-- Pistol Perks
INSERT INTO weapon_perks (weapon_type, name, description, effect, unlock_requirements) VALUES
('pistol', 'Dual Wield', 'Equip and use two pistols simultaneously', 
 '{"type": "dual_wield", "enabled": true}'::jsonb,
 '{"player_level": 5, "weapon_mastery": 0}'::jsonb),

('pistol', 'Quick Draw', 'Instant weapon switch to pistol', 
 '{"type": "switch_speed", "value": 1.0}'::jsonb,
 '{"player_level": 10, "weapon_mastery": 20}'::jsonb),

('pistol', 'Deadeye', 'Auto-aim to critical spots for 2 seconds', 
 '{"type": "auto_aim", "duration": 2, "target": "critical"}'::jsonb,
 '{"player_level": 15, "weapon_mastery": 40}'::jsonb);

-- SMG Perks
INSERT INTO weapon_perks (weapon_type, name, description, effect, unlock_requirements) VALUES
('smg', 'Run and Gun', 'No accuracy penalty when moving', 
 '{"type": "accuracy", "condition": "moving", "penalty_removal": true}'::jsonb,
 '{"player_level": 5, "weapon_mastery": 0}'::jsonb),

('smg', 'Spray and Pray', 'Increase fire rate by 25%', 
 '{"type": "fire_rate", "value": 0.25}'::jsonb,
 '{"player_level": 10, "weapon_mastery": 20}'::jsonb),

('smg', 'Hip Fire Master', 'Increase hip fire accuracy by 40%', 
 '{"type": "hip_fire_accuracy", "value": 0.4}'::jsonb,
 '{"player_level": 15, "weapon_mastery": 40}'::jsonb);

-- ============================================================================
-- FUNCTIONS
-- ============================================================================

-- Function to update weapon mastery level based on uses and kills
CREATE OR REPLACE FUNCTION calculate_weapon_mastery_level(
    p_uses_count INT,
    p_kills_count INT
) RETURNS INT AS $$
DECLARE
    v_mastery_level INT;
BEGIN
    -- Formula: (uses / 100) + (kills / 10)
    -- Capped at 100
    v_mastery_level := LEAST(100, (p_uses_count / 100) + (p_kills_count / 10));
    RETURN v_mastery_level;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to get upgrade cost for weapon
CREATE OR REPLACE FUNCTION get_weapon_upgrade_cost(
    p_current_level INT,
    p_rarity VARCHAR
) RETURNS INT AS $$
DECLARE
    v_base_cost INT;
    v_final_cost INT;
BEGIN
    -- Base cost by rarity
    v_base_cost := CASE p_rarity
        WHEN 'common' THEN 100
        WHEN 'uncommon' THEN 250
        WHEN 'rare' THEN 500
        WHEN 'epic' THEN 1000
        WHEN 'legendary' THEN 2500
        ELSE 100
    END;
    
    -- Exponential cost: base_cost * (level ^ 1.5)
    v_final_cost := ROUND(v_base_cost * POWER(p_current_level + 1, 1.5));
    
    RETURN v_final_cost;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Trigger to auto-update mastery level
CREATE OR REPLACE FUNCTION update_mastery_level_trigger()
RETURNS TRIGGER AS $$
BEGIN
    NEW.mastery_level := calculate_weapon_mastery_level(NEW.uses_count, NEW.kills_count);
    NEW.updated_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_mastery_level
    BEFORE INSERT OR UPDATE OF uses_count, kills_count ON weapon_mastery
    FOR EACH ROW
    EXECUTE FUNCTION update_mastery_level_trigger();

-- Trigger to auto-update weapon_upgrades.updated_at
CREATE OR REPLACE FUNCTION update_weapon_upgrades_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_weapon_upgrades_timestamp
    BEFORE UPDATE ON weapon_upgrades
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_upgrades_timestamp();

-- Trigger to auto-update weapon_resources.updated_at
CREATE OR REPLACE FUNCTION update_weapon_resources_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at := NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_weapon_resources_timestamp
    BEFORE UPDATE ON weapon_resources
    FOR EACH ROW
    EXECUTE FUNCTION update_weapon_resources_timestamp();

-- rollback DROP TRIGGER IF EXISTS trigger_update_weapon_resources_timestamp ON weapon_resources;
-- rollback DROP TRIGGER IF EXISTS trigger_update_weapon_upgrades_timestamp ON weapon_upgrades;
-- rollback DROP TRIGGER IF EXISTS trigger_update_mastery_level ON weapon_mastery;
-- rollback DROP FUNCTION IF EXISTS update_weapon_resources_timestamp();
-- rollback DROP FUNCTION IF EXISTS update_weapon_upgrades_timestamp();
-- rollback DROP FUNCTION IF EXISTS update_mastery_level_trigger();
-- rollback DROP FUNCTION IF EXISTS get_weapon_upgrade_cost(INT, VARCHAR);
-- rollback DROP FUNCTION IF EXISTS calculate_weapon_mastery_level(INT, INT);
-- rollback DROP TABLE IF EXISTS weapon_mastery;
-- rollback DROP TABLE IF EXISTS weapon_perks;
-- rollback DROP TABLE IF EXISTS weapon_upgrades;
-- rollback DROP TABLE IF EXISTS weapon_resources;





















