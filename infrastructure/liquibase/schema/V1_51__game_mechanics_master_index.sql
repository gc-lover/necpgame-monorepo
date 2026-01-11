-- Issue: #2176 - Game Mechanics Systems Master Index
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create game_mechanics schema and tables for Game Mechanics Master Index

BEGIN;

-- Create schema for game mechanics
CREATE SCHEMA IF NOT EXISTS game_mechanics;

-- Grant permissions
GRANT USAGE ON SCHEMA game_mechanics TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA game_mechanics TO necpgame_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA game_mechanics TO necpgame_app;

-- Table: game_mechanics.mechanics
-- Stores all registered game mechanics
CREATE TABLE IF NOT EXISTS game_mechanics.mechanics
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('combat', 'economy', 'social', 'world', 'progression', 'ui')),
    category VARCHAR(20) NOT NULL DEFAULT 'core' CHECK (category IN ('core', 'optional', 'experimental')),
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deprecated')),
    version VARCHAR(20) DEFAULT '1.0.0',
    service_name VARCHAR(50) NOT NULL,
    endpoint VARCHAR(200),
    priority INTEGER NOT NULL DEFAULT 5 CHECK (priority >= 1 AND priority <= 10),
    is_required BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: game_mechanics.dependencies
-- Stores dependency relationships between mechanics
CREATE TABLE IF NOT EXISTS game_mechanics.dependencies
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mechanic_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    depends_on_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    dependency_type VARCHAR(20) NOT NULL DEFAULT 'required' CHECK (dependency_type IN ('required', 'optional', 'conflicts')),
    is_hard_dependency BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Prevent self-references and duplicate dependencies
    CONSTRAINT no_self_reference CHECK (mechanic_id != depends_on_id),
    UNIQUE(mechanic_id, depends_on_id)
);

-- Table: game_mechanics.configurations
-- Stores configuration for each mechanic
CREATE TABLE IF NOT EXISTS game_mechanics.configurations
(
    mechanic_id UUID PRIMARY KEY REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    config_version VARCHAR(20) NOT NULL DEFAULT 'v1.0.0',
    settings JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: game_mechanics.status
-- Stores health status for each mechanic
CREATE TABLE IF NOT EXISTS game_mechanics.status
(
    mechanic_id UUID PRIMARY KEY REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    service_status VARCHAR(20) NOT NULL DEFAULT 'up' CHECK (service_status IN ('up', 'down', 'degraded')),
    response_time BIGINT DEFAULT 0 CHECK (response_time >= 0),
    error_count INTEGER NOT NULL DEFAULT 0 CHECK (error_count >= 0),
    last_checked TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_healthy BOOLEAN NOT NULL DEFAULT true
);

-- Indexes for performance optimization (MMOFPS requirements)

-- Mechanics table indexes
CREATE INDEX IF NOT EXISTS idx_mechanics_type ON game_mechanics.mechanics(type);
CREATE INDEX IF NOT EXISTS idx_mechanics_status ON game_mechanics.mechanics(status);
CREATE INDEX IF NOT EXISTS idx_mechanics_category ON game_mechanics.mechanics(category);
CREATE INDEX IF NOT EXISTS idx_mechanics_service_name ON game_mechanics.mechanics(service_name);
CREATE INDEX IF NOT EXISTS idx_mechanics_priority ON game_mechanics.mechanics(priority DESC);
CREATE INDEX IF NOT EXISTS idx_mechanics_required ON game_mechanics.mechanics(is_required) WHERE is_required = true;

-- Partial index for active mechanics (most common query)
CREATE INDEX IF NOT EXISTS idx_mechanics_active ON game_mechanics.mechanics(id, name, type, service_name, endpoint)
WHERE status = 'active';

-- Dependencies table indexes
CREATE INDEX IF NOT EXISTS idx_dependencies_mechanic_id ON game_mechanics.dependencies(mechanic_id);
CREATE INDEX IF NOT EXISTS idx_dependencies_depends_on_id ON game_mechanics.dependencies(depends_on_id);
CREATE INDEX IF NOT EXISTS idx_dependencies_type ON game_mechanics.dependencies(dependency_type);
CREATE INDEX IF NOT EXISTS idx_dependencies_hard ON game_mechanics.dependencies(is_hard_dependency);

-- Configurations table indexes
CREATE INDEX IF NOT EXISTS idx_configurations_active ON game_mechanics.configurations(mechanic_id) WHERE is_active = true;

-- Status table indexes
CREATE INDEX IF NOT EXISTS idx_status_healthy ON game_mechanics.status(is_healthy);
CREATE INDEX IF NOT EXISTS idx_status_service_status ON game_mechanics.status(service_status);
CREATE INDEX IF NOT EXISTS idx_status_last_checked ON game_mechanics.status(last_checked DESC);

-- Triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION game_mechanics.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_mechanics_updated_at
    BEFORE UPDATE ON game_mechanics.mechanics
    FOR EACH ROW EXECUTE FUNCTION game_mechanics.update_updated_at_column();

CREATE TRIGGER update_configurations_updated_at
    BEFORE UPDATE ON game_mechanics.configurations
    FOR EACH ROW EXECUTE FUNCTION game_mechanics.update_updated_at_column();

-- Row Level Security (RLS) policies
ALTER TABLE game_mechanics.mechanics ENABLE ROW LEVEL SECURITY;
ALTER TABLE game_mechanics.dependencies ENABLE ROW LEVEL SECURITY;
ALTER TABLE game_mechanics.configurations ENABLE ROW LEVEL SECURITY;
ALTER TABLE game_mechanics.status ENABLE ROW LEVEL SECURITY;

-- Policies for mechanics table
CREATE POLICY mechanics_read_policy ON game_mechanics.mechanics
    FOR SELECT USING (true);

CREATE POLICY mechanics_admin_policy ON game_mechanics.mechanics
    FOR ALL USING (
        current_setting('app.current_user_role', true) = 'admin' OR
        current_setting('app.current_user_role', true) = 'system'
    );

-- Policies for dependencies table
CREATE POLICY dependencies_read_policy ON game_mechanics.dependencies
    FOR SELECT USING (true);

CREATE POLICY dependencies_admin_policy ON game_mechanics.dependencies
    FOR ALL USING (
        current_setting('app.current_user_role', true) = 'admin' OR
        current_setting('app.current_user_role', true) = 'system'
    );

-- Policies for configurations table
CREATE POLICY configurations_read_policy ON game_mechanics.configurations
    FOR SELECT USING (true);

CREATE POLICY configurations_admin_policy ON game_mechanics.configurations
    FOR ALL USING (
        current_setting('app.current_user_role', true) = 'admin' OR
        current_setting('app.current_user_role', true) = 'system'
    );

-- Policies for status table
CREATE POLICY status_read_policy ON game_mechanics.status
    FOR SELECT USING (true);

CREATE POLICY status_admin_policy ON game_mechanics.status
    FOR ALL USING (
        current_setting('app.current_user_role', true) = 'admin' OR
        current_setting('app.current_user_role', true) = 'system'
    );

COMMIT;

--changeset author:necpgame dbms:postgresql
--comment: Insert initial game mechanics data for Game Mechanics Master Index

-- Insert core game mechanics
INSERT INTO game_mechanics.mechanics (id, name, type, category, status, version, service_name, endpoint, priority, is_required) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'Combat System', 'combat', 'core', 'active', '2.1.0', 'combat-service-go', '/api/v1/combat', 10, true),
    ('550e8400-e29b-41d4-a716-446655440001', 'Economy System', 'economy', 'core', 'active', '1.8.3', 'economy-service-go', '/api/v1/economy', 9, true),
    ('550e8400-e29b-41d4-a716-446655440002', 'Social System', 'social', 'core', 'active', '1.5.2', 'social-service-go', '/api/v1/social', 8, true),
    ('550e8400-e29b-41d4-a716-446655440003', 'World Simulation', 'world', 'core', 'active', '3.2.1', 'world-simulation-python', '/api/v1/world', 9, true),
    ('550e8400-e29b-41d4-a716-446655440004', 'Progression System', 'progression', 'core', 'active', '1.9.0', 'progression-service-go', '/api/v1/progression', 8, true),
    ('550e8400-e29b-41d4-a716-446655440005', 'Crafting System', 'economy', 'optional', 'active', '1.3.5', 'crafting-service-go', '/api/v1/crafting', 7, false),
    ('550e8400-e29b-41d4-a716-446655440006', 'Tournament System', 'social', 'optional', 'active', '1.1.2', 'tournament-service-go', '/api/v1/tournament', 6, false),
    ('550e8400-e29b-41d4-a716-446655440007', 'Achievement System', 'progression', 'optional', 'active', '1.4.1', 'achievement-service-go', '/api/v1/achievements', 5, false),
    ('550e8400-e29b-41d4-a716-446655440008', 'Quest System', 'world', 'core', 'active', '2.0.0', 'quest-service-go', '/api/v1/quests', 8, true),
    ('550e8400-e29b-41d4-a716-446655440009', 'Inventory System', 'ui', 'core', 'active', '1.7.4', 'inventory-service-go', '/api/v1/inventory', 7, true)
ON CONFLICT (id) DO NOTHING;

-- Insert dependency relationships
INSERT INTO game_mechanics.dependencies (mechanic_id, depends_on_id, dependency_type, is_hard_dependency) VALUES
    -- Combat depends on Inventory (for weapons)
    ('550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440009', 'required', true),
    -- Crafting depends on Inventory and Economy
    ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440009', 'required', true),
    ('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440001', 'required', true),
    -- Tournament depends on Social and Economy (for rewards)
    ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440002', 'required', true),
    ('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440001', 'required', false),
    -- Achievement depends on Progression
    ('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440004', 'required', true),
    -- Quest depends on World and Social
    ('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440003', 'required', true),
    ('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440002', 'required', false)
ON CONFLICT (mechanic_id, depends_on_id) DO NOTHING;

-- Insert default configurations
INSERT INTO game_mechanics.configurations (mechanic_id, config_version, settings, is_active) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'v2.1.0', '{"max_players_per_fight": 10, "damage_multiplier": 1.0, "health_regen_rate": 0.5}', true),
    ('550e8400-e29b-41d4-a716-446655440001', 'v1.8.3', '{"base_currency": "credits", "tax_rate": 0.05, "market_fee": 0.02}', true),
    ('550e8400-e29b-41d4-a716-446655440002', 'v1.5.2', '{"max_friends": 100, "guild_size_limit": 50, "chat_cooldown_ms": 1000}', true),
    ('550e8400-e29b-41d4-a716-446655440003', 'v3.2.1', '{"simulation_tick_ms": 100, "max_entities": 10000, "weather_enabled": true}', true),
    ('550e8400-e29b-41d4-a716-446655440004', 'v1.9.0', '{"xp_multiplier": 1.0, "level_cap": 100, "prestige_enabled": true}', true)
ON CONFLICT (mechanic_id) DO NOTHING;

-- Insert initial health status
INSERT INTO game_mechanics.status (mechanic_id, service_status, response_time, is_healthy) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'up', 15, true),
    ('550e8400-e29b-41d4-a716-446655440001', 'up', 12, true),
    ('550e8400-e29b-41d4-a716-446655440002', 'up', 8, true),
    ('550e8400-e29b-41d4-a716-446655440003', 'up', 25, true),
    ('550e8400-e29b-41d4-a716-446655440004', 'up', 10, true),
    ('550e8400-e29b-41d4-a716-446655440005', 'up', 18, true),
    ('550e8400-e29b-41d4-a716-446655440006', 'up', 22, true),
    ('550e8400-e29b-41d4-a716-446655440007', 'up', 14, true),
    ('550e8400-e29b-41d4-a716-446655440008', 'up', 16, true),
    ('550e8400-e29b-41d4-a716-446655440009', 'up', 9, true)
ON CONFLICT (mechanic_id) DO NOTHING;