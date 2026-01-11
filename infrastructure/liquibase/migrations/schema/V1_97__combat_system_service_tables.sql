--liquibase formatted sql

--changeset combat-system:create-combat-system-rules-table
CREATE TABLE IF NOT EXISTS combat_system_rules (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    damage_rules JSONB NOT NULL,
    combat_mechanics JSONB NOT NULL,
    balance_parameters JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),

    -- Constraints
    CONSTRAINT chk_version_positive CHECK (version > 0),

    -- Indexes for performance
    INDEX idx_combat_system_rules_version (version DESC),
    INDEX idx_combat_system_rules_created_at (created_at DESC),
    INDEX idx_combat_system_rules_updated_at (updated_at DESC),

    -- GIN indexes for JSON fields
    INDEX idx_combat_system_rules_damage_rules_gin (damage_rules) USING GIN,
    INDEX idx_combat_system_rules_combat_mechanics_gin (combat_mechanics) USING GIN,
    INDEX idx_combat_system_rules_balance_parameters_gin (balance_parameters) USING GIN
);

--changeset combat-system:create-combat-balance-configs-table
CREATE TABLE IF NOT EXISTS combat_balance_configs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    global_multipliers JSONB NOT NULL,
    character_balance JSONB NOT NULL,
    environmental_balance JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT chk_balance_version_positive CHECK (version > 0),

    -- Indexes for performance
    INDEX idx_combat_balance_configs_version (version DESC),
    INDEX idx_combat_balance_configs_created_at (created_at DESC),
    INDEX idx_combat_balance_configs_updated_at (updated_at DESC),

    -- GIN indexes for JSON fields
    INDEX idx_combat_balance_configs_global_multipliers_gin (global_multipliers) USING GIN,
    INDEX idx_combat_balance_configs_character_balance_gin (character_balance) USING GIN,
    INDEX idx_combat_balance_configs_environmental_balance_gin (environmental_balance) USING GIN
);

--changeset combat-system:create-ability-configurations-table
CREATE TABLE IF NOT EXISTS ability_configurations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('offensive', 'defensive', 'utility', 'ultimate')),
    description TEXT,
    damage INTEGER DEFAULT 0 CHECK (damage >= 0),
    cooldown_ms INTEGER NOT NULL DEFAULT 1000 CHECK (cooldown_ms >= 0),
    mana_cost INTEGER DEFAULT 0 CHECK (mana_cost >= 0),
    range INTEGER DEFAULT 0 CHECK (range >= 0),
    cast_time_ms INTEGER DEFAULT 0 CHECK (cast_time_ms >= 0),
    balance_notes TEXT,
    stat_requirements JSONB DEFAULT '{}',
    effects JSONB DEFAULT '[]',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_ability_configurations_name (name),
    INDEX idx_ability_configurations_type (type),
    INDEX idx_ability_configurations_damage (damage),
    INDEX idx_ability_configurations_cooldown_ms (cooldown_ms),
    INDEX idx_ability_configurations_created_at (created_at DESC),
    INDEX idx_ability_configurations_updated_at (updated_at DESC),

    -- GIN indexes for JSON fields
    INDEX idx_ability_configurations_stat_requirements_gin (stat_requirements) USING GIN,
    INDEX idx_ability_configurations_effects_gin (effects) USING GIN
);

--changeset combat-system:create-combat-calculation-logs-table
CREATE TABLE IF NOT EXISTS combat_calculation_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    attacker_id UUID NOT NULL,
    defender_id UUID NOT NULL,
    ability_id UUID,
    base_damage INTEGER NOT NULL,
    final_damage INTEGER NOT NULL,
    damage_type VARCHAR(50) NOT NULL,
    critical_hit BOOLEAN NOT NULL DEFAULT FALSE,
    modifiers JSONB DEFAULT '[]',
    environmental_factors JSONB,
    calculation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Foreign key constraints (would reference character/user tables)
    -- CONSTRAINT fk_combat_logs_attacker FOREIGN KEY (attacker_id) REFERENCES characters(id),
    -- CONSTRAINT fk_combat_logs_defender FOREIGN KEY (defender_id) REFERENCES characters(id),
    -- CONSTRAINT fk_combat_logs_ability FOREIGN KEY (ability_id) REFERENCES ability_configurations(id),

    -- Indexes for performance
    INDEX idx_combat_calculation_logs_attacker_id (attacker_id),
    INDEX idx_combat_calculation_logs_defender_id (defender_id),
    INDEX idx_combat_calculation_logs_ability_id (ability_id),
    INDEX idx_combat_calculation_logs_damage_type (damage_type),
    INDEX idx_combat_calculation_logs_critical_hit (critical_hit),
    INDEX idx_combat_calculation_logs_calculation_timestamp (calculation_timestamp DESC),
    INDEX idx_combat_calculation_logs_final_damage (final_damage),

    -- GIN indexes for JSON fields
    INDEX idx_combat_calculation_logs_modifiers_gin (modifiers) USING GIN,
    INDEX idx_combat_calculation_logs_environmental_factors_gin (environmental_factors) USING GIN
);

--changeset combat-system:create-combat-rules-audit-table
CREATE TABLE IF NOT EXISTS combat_rules_audit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    rules_id UUID NOT NULL,
    version INTEGER NOT NULL,
    change_type VARCHAR(20) NOT NULL CHECK (change_type IN ('created', 'updated', 'deleted')),
    old_values JSONB,
    new_values JSONB,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Foreign key constraints
    CONSTRAINT fk_combat_rules_audit_rules FOREIGN KEY (rules_id) REFERENCES combat_system_rules(id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_combat_rules_audit_rules_id (rules_id),
    INDEX idx_combat_rules_audit_version (version),
    INDEX idx_combat_rules_audit_change_type (change_type),
    INDEX idx_combat_rules_audit_changed_at (changed_at DESC),
    INDEX idx_combat_rules_audit_changed_by (changed_by),

    -- GIN indexes for JSON fields
    INDEX idx_combat_rules_audit_old_values_gin (old_values) USING GIN,
    INDEX idx_combat_rules_audit_new_values_gin (new_values) USING GIN
);

--changeset combat-system:create-balance-config-audit-table
CREATE TABLE IF NOT EXISTS balance_config_audit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    config_id UUID NOT NULL,
    version INTEGER NOT NULL,
    change_type VARCHAR(20) NOT NULL CHECK (change_type IN ('created', 'updated', 'deleted')),
    old_values JSONB,
    new_values JSONB,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Foreign key constraints
    CONSTRAINT fk_balance_config_audit_config FOREIGN KEY (config_id) REFERENCES combat_balance_configs(id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_balance_config_audit_config_id (config_id),
    INDEX idx_balance_config_audit_version (version),
    INDEX idx_balance_config_audit_change_type (change_type),
    INDEX idx_balance_config_audit_changed_at (changed_at DESC),
    INDEX idx_balance_config_audit_changed_by (changed_by),

    -- GIN indexes for JSON fields
    INDEX idx_balance_config_audit_old_values_gin (old_values) USING GIN,
    INDEX idx_balance_config_audit_new_values_gin (new_values) USING GIN
);

--changeset combat-system:create-ability-config-audit-table
CREATE TABLE IF NOT EXISTS ability_config_audit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    ability_id UUID NOT NULL,
    change_type VARCHAR(20) NOT NULL CHECK (change_type IN ('created', 'updated', 'deleted')),
    old_values JSONB,
    new_values JSONB,
    changed_by VARCHAR(255),
    changed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Foreign key constraints
    CONSTRAINT fk_ability_config_audit_ability FOREIGN KEY (ability_id) REFERENCES ability_configurations(id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_ability_config_audit_ability_id (ability_id),
    INDEX idx_ability_config_audit_change_type (change_type),
    INDEX idx_ability_config_audit_changed_at (changed_at DESC),
    INDEX idx_ability_config_audit_changed_by (changed_by),

    -- GIN indexes for JSON fields
    INDEX idx_ability_config_audit_old_values_gin (old_values) USING GIN,
    INDEX idx_ability_config_audit_new_values_gin (new_values) USING GIN
);

--changeset combat-system:add-triggers
-- Function to update updated_at timestamp for combat_system_rules
CREATE OR REPLACE FUNCTION update_combat_system_rules_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for combat_system_rules table
CREATE TRIGGER trg_update_combat_system_rules_updated_at
    BEFORE UPDATE ON combat_system_rules
    FOR EACH ROW
    EXECUTE FUNCTION update_combat_system_rules_updated_at();

-- Function to update updated_at timestamp for combat_balance_configs
CREATE OR REPLACE FUNCTION update_combat_balance_configs_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for combat_balance_configs table
CREATE TRIGGER trg_update_combat_balance_configs_updated_at
    BEFORE UPDATE ON combat_balance_configs
    FOR EACH ROW
    EXECUTE FUNCTION update_combat_balance_configs_updated_at();

-- Function to update updated_at timestamp for ability_configurations
CREATE OR REPLACE FUNCTION update_ability_configurations_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for ability_configurations table
CREATE TRIGGER trg_update_ability_configurations_updated_at
    BEFORE UPDATE ON ability_configurations
    FOR EACH ROW
    EXECUTE FUNCTION update_ability_configurations_updated_at();

-- Function to audit combat system rules changes
CREATE OR REPLACE FUNCTION audit_combat_system_rules_changes()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO combat_rules_audit (rules_id, version, change_type, new_values, changed_by)
        VALUES (NEW.id, NEW.version, 'created', row_to_json(NEW), NEW.created_by);
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO combat_rules_audit (rules_id, version, change_type, old_values, new_values, changed_by)
        VALUES (NEW.id, NEW.version, 'updated', row_to_json(OLD), row_to_json(NEW), NEW.created_by);
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO combat_rules_audit (rules_id, version, change_type, old_values, changed_by)
        VALUES (OLD.id, OLD.version, 'deleted', row_to_json(OLD), OLD.created_by);
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- Trigger for combat system rules audit
CREATE TRIGGER trg_audit_combat_system_rules
    AFTER INSERT OR UPDATE OR DELETE ON combat_system_rules
    FOR EACH ROW
    EXECUTE FUNCTION audit_combat_system_rules_changes();

--changeset combat-system:insert-default-data
-- Insert default combat system rules
INSERT INTO combat_system_rules (
    id, version, damage_rules, combat_mechanics, balance_parameters, created_by
) VALUES (
    gen_random_uuid(),
    1,
    '{
        "base_damage_multiplier": 1.0,
        "critical_hit_multiplier": 1.5,
        "armor_reduction_factor": 0.7,
        "environmental_modifiers": {
            "weather_damage_modifier": 1.0,
            "time_of_day_modifier": 1.0
        }
    }'::jsonb,
    '{
        "turn_based_enabled": false,
        "action_points_system": {
            "max_action_points": 3,
            "points_per_turn": 3
        },
        "interruption_rules": {
            "interruption_enabled": true,
            "interruption_chance": 0.1
        },
        "cooldown_system": {
            "global_cooldown_ms": 1000,
            "cooldown_reduction": 0.0
        },
        "targeting_rules": {
            "max_targets": 1,
            "area_of_effect": false,
            "line_of_sight": true
        }
    }'::jsonb,
    '{
        "difficulty_scaling": {
            "easy_multiplier": 0.8,
            "normal_multiplier": 1.0,
            "hard_multiplier": 1.2
        },
        "class_balance": {
            "dps_balance": 1.0,
            "tank_balance": 1.0,
            "healer_balance": 1.0,
            "support_balance": 1.0
        },
        "level_scaling": {
            "min_level": 1,
            "max_level": 100,
            "scaling_factor": 1.0
        }
    }'::jsonb,
    'system'
);

-- Insert default combat balance config
INSERT INTO combat_balance_configs (
    id, version, global_multipliers, character_balance, environmental_balance
) VALUES (
    gen_random_uuid(),
    1,
    '{
        "damage_multiplier": 1.0,
        "healing_multiplier": 1.0,
        "cooldown_multiplier": 1.0
    }'::jsonb,
    '{
        "level_scaling_enabled": true,
        "class_multipliers": {
            "warrior": 1.0,
            "mage": 1.0,
            "rogue": 1.0,
            "priest": 1.0
        },
        "stat_weights": {
            "strength": 1.0,
            "agility": 1.0,
            "intelligence": 1.0,
            "wisdom": 1.0
        }
    }'::jsonb,
    '{
        "weather_effects": {
            "rain": 0.9,
            "storm": 0.8,
            "sunny": 1.1
        },
        "terrain_modifiers": {
            "forest": 1.0,
            "mountain": 0.9,
            "desert": 1.1
        },
        "time_of_day_effects": {
            "day": 1.0,
            "night": 1.2,
            "dawn": 0.9,
            "dusk": 1.1
        }
    }'::jsonb
);

--changeset combat-system:add-comments
COMMENT ON TABLE combat_system_rules IS 'Core combat system rules with damage calculations, mechanics, and balance parameters';
COMMENT ON TABLE combat_balance_configs IS 'Combat balance configurations with global multipliers and character scaling';
COMMENT ON TABLE ability_configurations IS 'Combat ability configurations with stats, effects, and balance notes';
COMMENT ON TABLE combat_calculation_logs IS 'Audit log of all damage calculations for analysis and debugging';
COMMENT ON TABLE combat_rules_audit IS 'Audit trail for combat system rules changes';
COMMENT ON TABLE balance_config_audit IS 'Audit trail for balance configuration changes';
COMMENT ON TABLE ability_config_audit IS 'Audit trail for ability configuration changes';