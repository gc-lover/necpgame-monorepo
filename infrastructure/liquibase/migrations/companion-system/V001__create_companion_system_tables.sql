-- Companion System Database Schema
-- Version: V001
-- Description: Complete database schema for Companion System with catalog, ownership, progression, and abilities

-- =================================================================================================
-- CATALOG TABLES
-- =================================================================================================

-- Companion types catalog
CREATE TABLE companion_types (
    id BIGSERIAL PRIMARY KEY,
    type_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL, -- 'combat_drone', 'utility', 'pet', 'vehicle'
    rarity VARCHAR(20) NOT NULL DEFAULT 'common', -- 'common', 'uncommon', 'rare', 'epic', 'legendary'
    base_level INTEGER NOT NULL DEFAULT 1,
    max_level INTEGER NOT NULL DEFAULT 50,
    purchase_cost BIGINT NOT NULL DEFAULT 0,
    currency_type VARCHAR(20) NOT NULL DEFAULT 'credits', -- 'credits', 'premium', 'tokens'
    is_purchasable BOOLEAN NOT NULL DEFAULT true,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Companion templates (specific instances of types)
CREATE TABLE companion_templates (
    id BIGSERIAL PRIMARY KEY,
    companion_type_id BIGINT NOT NULL REFERENCES companion_types(id) ON DELETE CASCADE,
    template_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    icon_url VARCHAR(500),
    model_url VARCHAR(500),
    base_stats JSONB NOT NULL DEFAULT '{}', -- health, damage, armor, speed, etc.
    appearance_data JSONB NOT NULL DEFAULT '{}', -- visual customization options
    is_default BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Companion abilities catalog
CREATE TABLE companion_abilities (
    id BIGSERIAL PRIMARY KEY,
    ability_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL, -- 'active', 'passive', 'toggle'
    category VARCHAR(50), -- 'attack', 'defense', 'support', 'utility'
    cooldown_seconds INTEGER DEFAULT 0,
    energy_cost INTEGER DEFAULT 0,
    range INTEGER DEFAULT 0,
    duration_seconds INTEGER DEFAULT 0,
    effect_data JSONB NOT NULL DEFAULT '{}', -- ability effects and parameters
    icon_url VARCHAR(500),
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Template abilities mapping
CREATE TABLE companion_template_abilities (
    id BIGSERIAL PRIMARY KEY,
    companion_template_id BIGINT NOT NULL REFERENCES companion_templates(id) ON DELETE CASCADE,
    companion_ability_id BIGINT NOT NULL REFERENCES companion_abilities(id) ON DELETE CASCADE,
    unlock_level INTEGER NOT NULL DEFAULT 1,
    is_default BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(companion_template_id, companion_ability_id)
);

-- =================================================================================================
-- OWNERSHIP TABLES
-- =================================================================================================

-- Character companion ownership
CREATE TABLE character_companions (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL, -- Reference to character service
    companion_template_id BIGINT NOT NULL REFERENCES companion_templates(id),
    custom_name VARCHAR(100),
    current_level INTEGER NOT NULL DEFAULT 1,
    current_experience BIGINT NOT NULL DEFAULT 0,
    experience_to_next BIGINT NOT NULL DEFAULT 100,
    is_active BOOLEAN NOT NULL DEFAULT false,
    is_locked BOOLEAN NOT NULL DEFAULT false,
    acquired_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_used_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_id, companion_template_id)
);

-- Companion inventory slots
CREATE TABLE companion_inventory_slots (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    slot_number INTEGER NOT NULL,
    companion_id BIGINT REFERENCES character_companions(id) ON DELETE SET NULL,
    is_unlocked BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_id, slot_number)
);

-- =================================================================================================
-- PROGRESSION TABLES
-- =================================================================================================

-- Companion level progression
CREATE TABLE companion_levels (
    id BIGSERIAL PRIMARY KEY,
    companion_template_id BIGINT NOT NULL REFERENCES companion_templates(id),
    level INTEGER NOT NULL,
    experience_required BIGINT NOT NULL,
    stat_multipliers JSONB NOT NULL DEFAULT '{}', -- level-based stat bonuses
    unlock_abilities JSONB NOT NULL DEFAULT '[]', -- abilities unlocked at this level
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(companion_template_id, level)
);

-- Companion experience sources
CREATE TABLE companion_experience_sources (
    id BIGSERIAL PRIMARY KEY,
    source_key VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    experience_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Companion experience events
CREATE TABLE companion_experience_events (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    experience_source_id BIGINT NOT NULL REFERENCES companion_experience_sources(id),
    experience_gained BIGINT NOT NULL,
    event_data JSONB NOT NULL DEFAULT '{}', -- additional event context
    gained_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- STATS AND CUSTOMIZATION TABLES
-- =================================================================================================

-- Companion stats
CREATE TABLE companion_stats (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    stat_key VARCHAR(50) NOT NULL,
    base_value DECIMAL(10,2) NOT NULL DEFAULT 0,
    bonus_value DECIMAL(10,2) NOT NULL DEFAULT 0,
    multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    final_value DECIMAL(10,2) GENERATED ALWAYS AS (base_value + bonus_value) * multiplier STORED,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_companion_id, stat_key)
);

-- Companion appearance customization
CREATE TABLE companion_customizations (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    customization_type VARCHAR(50) NOT NULL, -- 'color', 'pattern', 'accessory', 'effect'
    customization_key VARCHAR(100) NOT NULL,
    customization_value JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_companion_id, customization_type, customization_key)
);

-- =================================================================================================
-- ABILITIES TABLES
-- =================================================================================================

-- Companion unlocked abilities
CREATE TABLE companion_unlocked_abilities (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    companion_ability_id BIGINT NOT NULL REFERENCES companion_abilities(id),
    unlocked_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_equipped BOOLEAN NOT NULL DEFAULT false,
    equipment_slot INTEGER, -- 1-4 for active abilities
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_companion_id, companion_ability_id),
    UNIQUE(character_companion_id, equipment_slot) DEFERRABLE INITIALLY DEFERRED
);

-- Companion ability usage tracking
CREATE TABLE companion_ability_usage (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    companion_ability_id BIGINT NOT NULL REFERENCES companion_abilities(id),
    used_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    target_data JSONB NOT NULL DEFAULT '{}', -- target information
    effect_data JSONB NOT NULL DEFAULT '{}', -- actual effects applied
    energy_consumed INTEGER NOT NULL DEFAULT 0
);

-- =================================================================================================
-- EFFECTS AND BUFFS TABLES
-- =================================================================================================

-- Active companion effects
CREATE TABLE companion_active_effects (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    effect_type VARCHAR(50) NOT NULL, -- 'buff', 'debuff', 'passive'
    effect_key VARCHAR(100) NOT NULL,
    source_type VARCHAR(50) NOT NULL, -- 'ability', 'item', 'environment'
    source_id BIGINT, -- reference to source
    effect_data JSONB NOT NULL DEFAULT '{}',
    duration_seconds INTEGER, -- NULL for permanent effects
    stacks INTEGER NOT NULL DEFAULT 1,
    applied_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE GENERATED ALWAYS AS (
        CASE WHEN duration_seconds IS NOT NULL
             THEN applied_at + INTERVAL '1 second' * duration_seconds
             ELSE NULL END
    ) STORED,
    UNIQUE(character_companion_id, effect_key, source_type, source_id)
);

-- =================================================================================================
-- TELEMETRY TABLES
-- =================================================================================================

-- Companion usage telemetry
CREATE TABLE companion_telemetry (
    id BIGSERIAL PRIMARY KEY,
    character_id BIGINT NOT NULL,
    character_companion_id BIGINT REFERENCES character_companions(id) ON DELETE SET NULL,
    event_type VARCHAR(50) NOT NULL, -- 'summon', 'dismiss', 'ability_used', 'level_up', 'death'
    event_data JSONB NOT NULL DEFAULT '{}',
    session_id VARCHAR(100), -- game session identifier
    location_data JSONB, -- position and context
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Companion performance metrics
CREATE TABLE companion_performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    character_companion_id BIGINT NOT NULL REFERENCES character_companions(id) ON DELETE CASCADE,
    metric_type VARCHAR(50) NOT NULL, -- 'damage_dealt', 'damage_taken', 'kills', 'deaths', 'support_actions'
    metric_value BIGINT NOT NULL DEFAULT 0,
    time_period_start TIMESTAMP WITH TIME ZONE NOT NULL,
    time_period_end TIMESTAMP WITH TIME ZONE NOT NULL,
    context_data JSONB NOT NULL DEFAULT '{}', -- combat, quest, etc.
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(character_companion_id, metric_type, time_period_start, time_period_end)
);

-- =================================================================================================
-- INDEXES
-- =================================================================================================

-- Companion types indexes
CREATE INDEX idx_companion_types_category ON companion_types(category);
CREATE INDEX idx_companion_types_rarity ON companion_types(rarity);
CREATE INDEX idx_companion_types_enabled ON companion_types(is_enabled);

-- Companion templates indexes
CREATE INDEX idx_companion_templates_type ON companion_templates(companion_type_id);
CREATE INDEX idx_companion_templates_default ON companion_templates(is_default);

-- Character companions indexes
CREATE INDEX idx_character_companions_character ON character_companions(character_id);
CREATE INDEX idx_character_companions_template ON character_companions(companion_template_id);
CREATE INDEX idx_character_companions_active ON character_companions(is_active);
CREATE INDEX idx_character_companions_level ON character_companions(current_level);

-- Companion stats indexes
CREATE INDEX idx_companion_stats_companion ON companion_stats(character_companion_id);
CREATE INDEX idx_companion_stats_key ON companion_stats(stat_key);

-- Companion abilities indexes
CREATE INDEX idx_companion_unlocked_abilities_companion ON companion_unlocked_abilities(character_companion_id);
CREATE INDEX idx_companion_unlocked_abilities_equipped ON companion_unlocked_abilities(is_equipped);

-- Companion effects indexes
CREATE INDEX idx_companion_active_effects_companion ON companion_active_effects(character_companion_id);
CREATE INDEX idx_companion_active_effects_expires ON companion_active_effects(expires_at);

-- Telemetry indexes
CREATE INDEX idx_companion_telemetry_character ON companion_telemetry(character_id);
CREATE INDEX idx_companion_telemetry_companion ON companion_telemetry(character_companion_id);
CREATE INDEX idx_companion_telemetry_event ON companion_telemetry(event_type);
CREATE INDEX idx_companion_telemetry_timestamp ON companion_telemetry(timestamp);

-- Performance metrics indexes
CREATE INDEX idx_companion_performance_metrics_companion ON companion_performance_metrics(character_companion_id);
CREATE INDEX idx_companion_performance_metrics_type ON companion_performance_metrics(metric_type);
CREATE INDEX idx_companion_performance_metrics_period ON companion_performance_metrics(time_period_start, time_period_end);

-- =================================================================================================
-- CONSTRAINTS
-- =================================================================================================

-- Check constraints
ALTER TABLE companion_types ADD CONSTRAINT chk_rarity CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary'));
ALTER TABLE companion_types ADD CONSTRAINT chk_currency CHECK (currency_type IN ('credits', 'premium', 'tokens'));
ALTER TABLE companion_abilities ADD CONSTRAINT chk_ability_type CHECK (type IN ('active', 'passive', 'toggle'));
ALTER TABLE companion_unlocked_abilities ADD CONSTRAINT chk_equipment_slot CHECK (equipment_slot BETWEEN 1 AND 4);

-- =================================================================================================
-- TRIGGERS
-- =================================================================================================

-- Function to update updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers to relevant tables
CREATE TRIGGER update_companion_types_updated_at BEFORE UPDATE ON companion_types FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_companion_templates_updated_at BEFORE UPDATE ON companion_templates FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_character_companions_updated_at BEFORE UPDATE ON character_companions FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_companion_stats_updated_at BEFORE UPDATE ON companion_stats FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_companion_unlocked_abilities_updated_at BEFORE UPDATE ON companion_unlocked_abilities FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
