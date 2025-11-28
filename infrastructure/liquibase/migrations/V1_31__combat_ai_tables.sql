-- V1.31: Combat AI Tables
-- Таблицы для системы AI врагов, встреч, рейдов и телеметрии

-- Профили AI врагов
CREATE TABLE IF NOT EXISTS ai_profiles (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    tier VARCHAR(20) NOT NULL CHECK (tier IN ('street', 'tactical', 'mythic', 'raid')),
    faction VARCHAR(50),
    level_range_min INT NOT NULL CHECK (level_range_min > 0),
    level_range_max INT NOT NULL CHECK (level_range_max >= level_range_min),
    base_health INT NOT NULL CHECK (base_health > 0),
    base_armor INT NOT NULL CHECK (base_armor >= 0),
    base_damage INT NOT NULL CHECK (base_damage > 0),
    behavior_tree JSONB NOT NULL,
    abilities JSONB NOT NULL DEFAULT '[]',
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ai_profiles_tier ON ai_profiles(tier);
CREATE INDEX idx_ai_profiles_faction ON ai_profiles(faction);
CREATE INDEX idx_ai_profiles_level_range ON ai_profiles(level_range_min, level_range_max);

-- Встречи с врагами
CREATE TABLE IF NOT EXISTS encounters (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type VARCHAR(50) NOT NULL,
    enemy_profile_ids JSONB NOT NULL,
    difficulty_multiplier FLOAT NOT NULL DEFAULT 1.0 CHECK (difficulty_multiplier > 0),
    location VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'active', 'completed', 'failed')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_encounters_status ON encounters(status);
CREATE INDEX idx_encounters_location ON encounters(location);
CREATE INDEX idx_encounters_created_at ON encounters(created_at DESC);

-- Фазы рейдов
CREATE TABLE IF NOT EXISTS raid_phases (
    id SERIAL PRIMARY KEY,
    raid_id VARCHAR(50) NOT NULL,
    phase_number INT NOT NULL CHECK (phase_number > 0),
    phase_name VARCHAR(100) NOT NULL,
    boss_id VARCHAR(50),
    mechanics JSONB,
    started_at TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMP,
    UNIQUE(raid_id, phase_number)
);

CREATE INDEX idx_raid_phases_raid_id ON raid_phases(raid_id);
CREATE INDEX idx_raid_phases_started_at ON raid_phases(started_at DESC);

-- Телеметрия AI
CREATE TABLE IF NOT EXISTS ai_telemetry (
    id SERIAL PRIMARY KEY,
    profile_id VARCHAR(50) NOT NULL,
    encounter_id UUID,
    session_id VARCHAR(50) NOT NULL,
    player_level INT NOT NULL,
    ttk_seconds FLOAT NOT NULL CHECK (ttk_seconds >= 0),
    damage_dealt INT NOT NULL CHECK (damage_dealt >= 0),
    damage_taken INT NOT NULL CHECK (damage_taken >= 0),
    abilities_used JSONB NOT NULL DEFAULT '[]',
    death_reason VARCHAR(100),
    recorded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_ai_telemetry_profile_id ON ai_telemetry(profile_id);
CREATE INDEX idx_ai_telemetry_encounter_id ON ai_telemetry(encounter_id);
CREATE INDEX idx_ai_telemetry_session_id ON ai_telemetry(session_id);
CREATE INDEX idx_ai_telemetry_recorded_at ON ai_telemetry(recorded_at DESC);

-- Seed данные: базовые профили AI
INSERT INTO ai_profiles (id, name, tier, faction, level_range_min, level_range_max, base_health, base_armor, base_damage, behavior_tree, abilities)
VALUES
    ('street-thug', 'Street Thug', 'street', NULL, 1, 5, 100, 0, 10, '{"type": "aggressive", "priority": ["attack", "flee"]}', '["punch", "kick"]'),
    ('tactical-corpo', 'Corpo Soldier', 'tactical', 'arasaka', 11, 15, 250, 50, 25, '{"type": "tactical", "priority": ["cover", "flank", "attack"]}', '["rifle_burst", "flashbang", "smoke_grenade"]'),
    ('mythic-cyberpsycho', 'Cyberpsycho', 'mythic', NULL, 21, 25, 500, 100, 50, '{"type": "berserker", "priority": ["charge", "ability", "attack"]}', '["berserk", "sandevistan", "gorilla_arms"]'),
    ('raid-boss-smasher', 'Adam Smasher', 'raid', 'arasaka', 31, 35, 10000, 500, 150, '{"type": "boss", "priority": ["phase_ability", "heavy_attack", "summon_adds"]}', '["missile_barrage", "emp_blast", "gravity_well", "berserk_mode"]')
ON CONFLICT (id) DO NOTHING;

COMMENT ON TABLE ai_profiles IS 'Профили AI врагов с поведением и способностями';
COMMENT ON TABLE encounters IS 'Встречи игроков с AI врагами';
COMMENT ON TABLE raid_phases IS 'Фазы рейдовых боссов';
COMMENT ON TABLE ai_telemetry IS 'Телеметрия AI для балансировки';

