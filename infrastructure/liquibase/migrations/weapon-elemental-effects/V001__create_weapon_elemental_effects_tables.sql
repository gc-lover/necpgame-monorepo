-- Weapon Elemental Effects Database Schema
-- Version: V001
-- Description: Complete database schema for Weapon Elemental Effects System with elements, effects, interactions, and weapon configurations

-- =================================================================================================
-- ELEMENTAL TYPES AND EFFECTS TABLES
-- =================================================================================================

-- Elemental types (Fire, Ice, Poison, Acid)
CREATE TABLE elemental_types (
    id BIGSERIAL PRIMARY KEY,
    element_key VARCHAR(50) UNIQUE NOT NULL,
    name JSONB NOT NULL, -- Multi-language support: {"en": "Fire", "ru": "Огонь"}
    description JSONB NOT NULL,
    color_code VARCHAR(7), -- Hex color code
    icon_url VARCHAR(500),
    base_damage_type VARCHAR(20) NOT NULL, -- 'FIRE', 'COLD', 'POISON', 'ACID'
    visual_effect_type VARCHAR(30) NOT NULL, -- 'PARTICLES', 'SCREEN_DISTORTION', 'MODEL_OVERLAY'
    sound_effect_type VARCHAR(30) NOT NULL, -- 'CONTINUOUS', 'BURST', 'LOOP'
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Elemental effects templates
CREATE TABLE elemental_effects (
    id BIGSERIAL PRIMARY KEY,
    effect_key VARCHAR(100) UNIQUE NOT NULL,
    element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    name JSONB NOT NULL,
    description JSONB,
    effect_type VARCHAR(30) NOT NULL, -- 'DIRECT_DAMAGE', 'DOT_DAMAGE', 'STATUS_EFFECT', 'MOVEMENT_MODIFIER', 'DEFENSE_MODIFIER'
    damage_type VARCHAR(20) NOT NULL, -- 'PHYSICAL', 'FIRE', 'COLD', 'POISON', 'ACID'
    base_damage INTEGER NOT NULL DEFAULT 0,
    damage_per_second INTEGER DEFAULT 0, -- For DoT effects
    duration_seconds INTEGER NOT NULL DEFAULT 0,
    tick_interval_seconds DECIMAL(3,1) NOT NULL DEFAULT 1.0, -- For DoT effects
    max_stacks INTEGER NOT NULL DEFAULT 1,
    stack_duration_seconds INTEGER, -- NULL for permanent stacks
    stat_modifiers JSONB NOT NULL DEFAULT '{}', -- {"movement_speed": -0.3, "attack_speed": -0.2}
    visual_config JSONB NOT NULL DEFAULT '{}', -- Particle effects, colors, etc.
    sound_config JSONB NOT NULL DEFAULT '{}', -- Sound effects configuration
    is_chainable BOOLEAN NOT NULL DEFAULT false, -- Can be chained with other effects
    chain_trigger_condition VARCHAR(50), -- Condition to trigger chain (e.g., 'ON_DEATH', 'ON_DAMAGE')
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Elemental effect modifiers (for different weapon types, armor types, etc.)
CREATE TABLE elemental_effect_modifiers (
    id BIGSERIAL PRIMARY KEY,
    effect_id BIGINT NOT NULL REFERENCES elemental_effects(id) ON DELETE CASCADE,
    modifier_type VARCHAR(30) NOT NULL, -- 'WEAPON_TYPE', 'ARMOR_TYPE', 'TARGET_TYPE', 'ENVIRONMENT'
    modifier_key VARCHAR(50) NOT NULL, -- 'rifle', 'light_armor', 'organic', 'water'
    damage_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    duration_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    effect_chance_bonus DECIMAL(5,2) NOT NULL DEFAULT 0.0, -- Percentage bonus to effect application
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(effect_id, modifier_type, modifier_key)
);

-- =================================================================================================
-- ELEMENTAL INTERACTIONS TABLES
-- =================================================================================================

-- Elemental interaction rules
CREATE TABLE elemental_interactions (
    id BIGSERIAL PRIMARY KEY,
    primary_element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    secondary_element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    interaction_type VARCHAR(30) NOT NULL, -- 'AMPLIFY', 'COUNTER', 'NEUTRALIZE', 'COMBINE', 'CHAIN_REACTION'
    result_element_id BIGINT REFERENCES elemental_types(id), -- NULL if no resulting element
    result_effect_id BIGINT REFERENCES elemental_effects(id), -- NULL if no specific effect
    damage_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    duration_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    description JSONB,
    visual_config JSONB NOT NULL DEFAULT '{}', -- Special visual effects for interaction
    sound_config JSONB NOT NULL DEFAULT '{}', -- Special sound effects for interaction
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CHECK (primary_element_id != secondary_element_id),
    UNIQUE(primary_element_id, secondary_element_id)
);

-- Elemental interaction triggers
CREATE TABLE elemental_interaction_triggers (
    id BIGSERIAL PRIMARY KEY,
    interaction_id BIGINT NOT NULL REFERENCES elemental_interactions(id) ON DELETE CASCADE,
    trigger_type VARCHAR(30) NOT NULL, -- 'ON_CONTACT', 'ON_STACK_OVERFLOW', 'ON_TIME_EXPIRE', 'ON_DAMAGE_RECEIVED'
    trigger_condition JSONB NOT NULL DEFAULT '{}', -- Specific conditions for trigger
    effect_config JSONB NOT NULL DEFAULT '{}', -- Additional effects from trigger
    probability DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- Chance of trigger occurring
    cooldown_seconds INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- WEAPON ELEMENTAL CONFIGURATION TABLES
-- =================================================================================================

-- Weapon elemental configurations
CREATE TABLE weapon_elemental_configs (
    id BIGSERIAL PRIMARY KEY,
    weapon_type VARCHAR(50) NOT NULL, -- 'rifle', 'shotgun', 'pistol', 'melee', 'grenade'
    weapon_subtype VARCHAR(50), -- 'assault_rifle', 'combat_shotgun', etc.
    element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    base_effect_chance DECIMAL(5,2) NOT NULL DEFAULT 0.0, -- Base chance to apply effect
    effect_duration_seconds INTEGER NOT NULL DEFAULT 5,
    effect_damage_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    ammo_consumption_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- How much ammo this costs
    heat_generation_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- Weapon overheating
    recoil_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- Recoil changes
    fire_rate_modifier DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- Fire rate changes
    config_data JSONB NOT NULL DEFAULT '{}', -- Additional weapon-specific config
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(weapon_type, weapon_subtype, element_id)
);

-- Weapon elemental upgrade paths
CREATE TABLE weapon_elemental_upgrades (
    id BIGSERIAL PRIMARY KEY,
    base_config_id BIGINT NOT NULL REFERENCES weapon_elemental_configs(id),
    upgrade_level INTEGER NOT NULL,
    upgrade_cost JSONB NOT NULL DEFAULT '{}', -- {"credits": 1000, "materials": {"rare_metal": 5}}
    effect_chance_bonus DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    damage_multiplier_bonus DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    duration_bonus_seconds INTEGER NOT NULL DEFAULT 0,
    additional_effects JSONB NOT NULL DEFAULT '[]', -- Array of additional effect IDs
    unlock_requirements JSONB NOT NULL DEFAULT '{}', -- Prerequisites to unlock this upgrade
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(base_config_id, upgrade_level)
);

-- =================================================================================================
-- ACTIVE EFFECTS AND APPLICATION TABLES
-- =================================================================================================

-- Active elemental effects on characters
CREATE TABLE character_elemental_effects (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    effect_id BIGINT NOT NULL REFERENCES elemental_effects(id),
    source_weapon_id BIGINT, -- NULL if from environment or other source
    source_character_id BIGINT, -- Who applied this effect
    current_stacks INTEGER NOT NULL DEFAULT 1,
    max_stacks INTEGER NOT NULL DEFAULT 1,
    remaining_duration_seconds DECIMAL(8,2) NOT NULL,
    total_damage_dealt BIGINT NOT NULL DEFAULT 0,
    effect_data JSONB NOT NULL DEFAULT '{}', -- Runtime effect data
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE,
    last_tick_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_id, effect_id, source_character_id)
);

-- Elemental effect damage history
CREATE TABLE elemental_effect_damage (
    id BIGSERIAL PRIMARY KEY,
    effect_instance_id BIGINT NOT NULL REFERENCES character_elemental_effects(id) ON DELETE CASCADE,
    damage_amount INTEGER NOT NULL,
    damage_type VARCHAR(20) NOT NULL,
    is_critical BOOLEAN NOT NULL DEFAULT false,
    target_character_id BIGINT NOT NULL,
    target_body_part VARCHAR(30), -- 'head', 'torso', 'left_arm', etc.
    damage_location JSONB, -- Precise 3D coordinates if needed
    damage_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Elemental effect interactions (when effects interact on same target)
CREATE TABLE elemental_effect_interactions (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    primary_effect_id BIGINT NOT NULL REFERENCES character_elemental_effects(id),
    secondary_effect_id BIGINT NOT NULL REFERENCES character_elemental_effects(id),
    interaction_id BIGINT NOT NULL REFERENCES elemental_interactions(id),
    result_damage INTEGER,
    result_effect_id BIGINT REFERENCES character_elemental_effects(id), -- New effect created
    interaction_data JSONB NOT NULL DEFAULT '{}',
    interaction_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    CHECK (primary_effect_id != secondary_effect_id)
);

-- =================================================================================================
-- ENVIRONMENTAL EFFECTS TABLES
-- =================================================================================================

-- Environmental elemental zones
CREATE TABLE environmental_elemental_zones (
    id BIGSERIAL PRIMARY KEY,
    zone_key VARCHAR(100) UNIQUE NOT NULL,
    zone_type VARCHAR(30) NOT NULL, -- 'WATER', 'FIRE_SOURCE', 'TOXIC_AREA', 'ACID_POOL'
    element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    effect_id BIGINT NOT NULL REFERENCES elemental_effects(id),
    zone_bounds JSONB NOT NULL DEFAULT '{}', -- 3D bounds of the zone
    zone_center JSONB NOT NULL DEFAULT '{}', -- Center point coordinates
    zone_radius DECIMAL(8,2), -- For circular zones
    zone_height DECIMAL(8,2), -- For vertical bounds
    effect_strength DECIMAL(5,2) NOT NULL DEFAULT 1.0, -- How strong the effect is
    effect_interval_seconds DECIMAL(3,1) NOT NULL DEFAULT 1.0, -- How often effect applies
    max_concurrent_effects INTEGER NOT NULL DEFAULT 5, -- Max characters affected simultaneously
    visual_config JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Environmental zone active effects
CREATE TABLE environmental_zone_effects (
    id BIGSERIAL PRIMARY KEY,
    zone_id BIGINT NOT NULL REFERENCES environmental_elemental_zones(id) ON DELETE CASCADE,
    character_id BIGINT NOT NULL,
    effect_instance_id BIGINT REFERENCES character_elemental_effects(id),
    entered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_effect_applied_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    total_effects_applied INTEGER NOT NULL DEFAULT 0,
    is_still_in_zone BOOLEAN NOT NULL DEFAULT true,
    exited_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(zone_id, character_id, effect_instance_id)
);

-- =================================================================================================
-- ANALYTICS AND TELEMETRY TABLES
-- =================================================================================================

-- Elemental effects usage statistics
CREATE TABLE elemental_effects_stats (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,
    element_id BIGINT REFERENCES elemental_types(id),
    effect_id BIGINT REFERENCES elemental_effects(id),
    weapon_type VARCHAR(50),
    total_applications BIGINT NOT NULL DEFAULT 0,
    total_damage_dealt BIGINT NOT NULL DEFAULT 0,
    total_duration_seconds BIGINT NOT NULL DEFAULT 0,
    average_stacks DECIMAL(5,2) DEFAULT 0,
    interaction_count BIGINT NOT NULL DEFAULT 0,
    kill_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(date, element_id, effect_id, weapon_type)
);

-- Elemental effect telemetry events
CREATE TABLE elemental_telemetry_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL, -- 'EFFECT_APPLIED', 'EFFECT_INTERACTION', 'DAMAGE_DEALT', 'EFFECT_EXPIRED'
    character_id BIGINT NOT NULL,
    target_character_id BIGINT,
    element_id BIGINT REFERENCES elemental_types(id),
    effect_id BIGINT REFERENCES elemental_effects(id),
    weapon_type VARCHAR(50),
    damage_amount INTEGER,
    effect_duration_seconds INTEGER,
    stacks_count INTEGER,
    event_data JSONB NOT NULL DEFAULT '{}',
    session_id VARCHAR(100),
    client_version VARCHAR(20),
    match_id VARCHAR(100),
    event_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Weapon elemental performance metrics
CREATE TABLE weapon_elemental_performance (
    id BIGSERIAL PRIMARY KEY,
    weapon_type VARCHAR(50) NOT NULL,
    element_id BIGINT NOT NULL REFERENCES elemental_types(id),
    total_shots BIGINT NOT NULL DEFAULT 0,
    effects_applied BIGINT NOT NULL DEFAULT 0,
    effect_accuracy DECIMAL(5,2), -- Percentage of shots that applied effects
    average_damage_per_effect DECIMAL(8,2),
    average_effect_duration DECIMAL(8,2),
    kill_to_effect_ratio DECIMAL(5,2), -- Kills per effect application
    measured_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(weapon_type, element_id, measured_at)
);

-- =================================================================================================
-- CONFIGURATION AND BALANCE TABLES
-- =================================================================================================

-- Elemental effect balance configurations
CREATE TABLE elemental_balance_configs (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_type VARCHAR(30) NOT NULL, -- 'GLOBAL_MULTIPLIER', 'ELEMENT_SPECIFIC', 'WEAPON_SPECIFIC', 'PLAYER_LEVEL'
    target_element_id BIGINT REFERENCES elemental_types(id), -- NULL for global configs
    target_weapon_type VARCHAR(50), -- NULL for non-weapon specific
    config_data JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    activated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    deactivated_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- A/B testing configurations for elemental effects
CREATE TABLE elemental_ab_tests (
    id BIGSERIAL PRIMARY KEY,
    test_key VARCHAR(100) UNIQUE NOT NULL,
    test_name VARCHAR(255) NOT NULL,
    description TEXT,
    test_type VARCHAR(30) NOT NULL, -- 'DAMAGE_MULTIPLIER', 'EFFECT_CHANCE', 'DURATION', 'INTERACTION'
    target_element_id BIGINT REFERENCES elemental_types(id),
    control_group_config JSONB NOT NULL DEFAULT '{}',
    test_group_config JSONB NOT NULL DEFAULT '{}',
    test_percentage DECIMAL(5,2) NOT NULL DEFAULT 50.0, -- Percentage of players in test group
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- INDEXES
-- =================================================================================================

-- Elemental types indexes
CREATE INDEX idx_elemental_types_key ON elemental_types(element_key);
CREATE INDEX idx_elemental_types_active ON elemental_types(is_active) WHERE is_active = true;

-- Elemental effects indexes
CREATE INDEX idx_elemental_effects_key ON elemental_effects(effect_key);
CREATE INDEX idx_elemental_effects_element ON elemental_effects(element_id);
CREATE INDEX idx_elemental_effects_type ON elemental_effects(effect_type);
CREATE INDEX idx_elemental_effects_active ON elemental_effects(is_active) WHERE is_active = true;

-- Interactions indexes
CREATE INDEX idx_elemental_interactions_elements ON elemental_interactions(primary_element_id, secondary_element_id);
CREATE INDEX idx_elemental_interactions_type ON elemental_interactions(interaction_type);

-- Weapon configs indexes
CREATE INDEX idx_weapon_elemental_configs_weapon ON weapon_elemental_configs(weapon_type, weapon_subtype);
CREATE INDEX idx_weapon_elemental_configs_element ON weapon_elemental_configs(element_id);
CREATE INDEX idx_weapon_elemental_configs_active ON weapon_elemental_configs(is_active) WHERE is_active = true;

-- Character effects indexes
CREATE INDEX idx_character_elemental_effects_character ON character_elemental_effects(character_id);
CREATE INDEX idx_character_elemental_effects_effect ON character_elemental_effects(effect_id);
CREATE INDEX idx_character_elemental_effects_active ON character_elemental_effects(is_active) WHERE is_active = true;
CREATE INDEX idx_character_elemental_effects_expires ON character_elemental_effects(expires_at) WHERE expires_at IS NOT NULL;

-- Environmental zones indexes
CREATE INDEX idx_environmental_elemental_zones_type ON environmental_elemental_zones(zone_type);
CREATE INDEX idx_environmental_elemental_zones_element ON environmental_elemental_zones(element_id);
CREATE INDEX idx_environmental_elemental_zones_active ON environmental_elemental_zones(is_active) WHERE is_active = true;

-- Analytics indexes
CREATE INDEX idx_elemental_effects_stats_date ON elemental_effects_stats(date DESC);
CREATE INDEX idx_elemental_effects_stats_element ON elemental_effects_stats(element_id);
CREATE INDEX idx_elemental_telemetry_events_type ON elemental_telemetry_events(event_type);
CREATE INDEX idx_elemental_telemetry_events_character ON elemental_telemetry_events(character_id);
CREATE INDEX idx_elemental_telemetry_events_timestamp ON elemental_telemetry_events(event_timestamp DESC);

-- =================================================================================================
-- CONSTRAINTS
-- =================================================================================================

-- Check constraints
ALTER TABLE elemental_types ADD CONSTRAINT chk_damage_type CHECK (damage_type IN ('FIRE', 'COLD', 'POISON', 'ACID'));
ALTER TABLE elemental_types ADD CONSTRAINT chk_visual_effect_type CHECK (visual_effect_type IN ('PARTICLES', 'SCREEN_DISTORTION', 'MODEL_OVERLAY'));
ALTER TABLE elemental_types ADD CONSTRAINT chk_sound_effect_type CHECK (sound_effect_type IN ('CONTINUOUS', 'BURST', 'LOOP'));

ALTER TABLE elemental_effects ADD CONSTRAINT chk_effect_type CHECK (effect_type IN ('DIRECT_DAMAGE', 'DOT_DAMAGE', 'STATUS_EFFECT', 'MOVEMENT_MODIFIER', 'DEFENSE_MODIFIER'));
ALTER TABLE elemental_effects ADD CONSTRAINT chk_damage_type CHECK (damage_type IN ('PHYSICAL', 'FIRE', 'COLD', 'POISON', 'ACID'));

ALTER TABLE elemental_interactions ADD CONSTRAINT chk_interaction_type CHECK (interaction_type IN ('AMPLIFY', 'COUNTER', 'NEUTRALIZE', 'COMBINE', 'CHAIN_REACTION'));

ALTER TABLE weapon_elemental_configs ADD CONSTRAINT chk_weapon_type CHECK (weapon_type IN ('rifle', 'shotgun', 'pistol', 'melee', 'grenade', 'launcher', 'special'));

ALTER TABLE elemental_telemetry_events ADD CONSTRAINT chk_event_type CHECK (event_type IN ('EFFECT_APPLIED', 'EFFECT_INTERACTION', 'DAMAGE_DEALT', 'EFFECT_EXPIRED', 'ZONE_ENTERED', 'ZONE_EXITED'));

-- =================================================================================================
-- TRIGGERS
-- =================================================================================================

-- Function to update updated_at timestamps
CREATE OR REPLACE FUNCTION update_elemental_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers to relevant tables
CREATE TRIGGER update_elemental_types_updated_at BEFORE UPDATE ON elemental_types FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_elemental_effects_updated_at BEFORE UPDATE ON elemental_effects FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_elemental_interactions_updated_at BEFORE UPDATE ON elemental_interactions FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_weapon_elemental_configs_updated_at BEFORE UPDATE ON weapon_elemental_configs FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_environmental_elemental_zones_updated_at BEFORE UPDATE ON environmental_elemental_zones FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_character_elemental_effects_updated_at BEFORE UPDATE ON character_elemental_effects FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_elemental_balance_configs_updated_at BEFORE UPDATE ON elemental_balance_configs FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
CREATE TRIGGER update_elemental_ab_tests_updated_at BEFORE UPDATE ON elemental_ab_tests FOR EACH ROW EXECUTE FUNCTION update_elemental_updated_at_column();
