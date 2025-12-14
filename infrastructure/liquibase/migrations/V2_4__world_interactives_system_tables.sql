--liquibase formatted sql

--changeset necp:world_interactives_system_tables_v1 runOnChange:true
--comment: Create tables for world interactives system (blockposts, relays, medical stations, containers)

-- World interactives main table
CREATE TABLE IF NOT EXISTS world_interactives (
    id BIGSERIAL PRIMARY KEY,
    interactive_name VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('faction_control', 'communication', 'medical', 'logistics')),
    description TEXT,
    base_health INTEGER DEFAULT 100,
    is_destructible BOOLEAN DEFAULT true,
    respawn_time_seconds INTEGER DEFAULT 300,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_world_interactives_category (category),
    INDEX idx_world_interactives_name (interactive_name)
);

-- Interactive types and variants
CREATE TABLE IF NOT EXISTS interactive_types (
    id BIGSERIAL PRIMARY KEY,
    interactive_id BIGINT NOT NULL REFERENCES world_interactives(id) ON DELETE CASCADE,
    type_name VARCHAR(100) NOT NULL,
    variant_name VARCHAR(100),
    control_radius_meters INTEGER,
    price_modifier_percent INTEGER DEFAULT 0,
    access_requirement VARCHAR(200),

    -- Faction control specific
    controlling_faction VARCHAR(50),
    takeover_method VARCHAR(50) CHECK (takeover_method IN ('bribery', 'hacking', 'assault')),
    takeover_cost_eddies_min INTEGER,
    takeover_cost_eddies_max INTEGER,
    takeover_success_rate_min INTEGER,
    takeover_success_rate_max INTEGER,
    takeover_detection_risk_percent INTEGER DEFAULT 0,
    takeover_time_seconds INTEGER,
    takeover_alarm_probability_percent INTEGER DEFAULT 0,

    -- Communication relay specific
    signal_strength INTEGER,
    encryption_level VARCHAR(20) CHECK (encryption_level IN ('none', 'basic', 'advanced', 'military')),
    jamming_resistance INTEGER DEFAULT 0,
    bandwidth_mbps INTEGER,

    -- Medical station specific
    healing_rate_per_second INTEGER DEFAULT 10,
    cyberware_repair BOOLEAN DEFAULT false,
    trauma_team_available BOOLEAN DEFAULT false,

    -- Container specific
    storage_capacity INTEGER,
    security_level VARCHAR(20) CHECK (security_level IN ('none', 'basic', 'advanced', 'military')),
    loot_quality VARCHAR(20) CHECK (loot_quality IN ('trash', 'common', 'uncommon', 'rare', 'epic', 'legendary')),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Indexes
    INDEX idx_interactive_types_interactive_id (interactive_id),
    INDEX idx_interactive_types_faction (controlling_faction),
    INDEX idx_interactive_types_takeover_method (takeover_method),
    UNIQUE KEY uk_interactive_type_variant (interactive_id, type_name, COALESCE(variant_name, ''))
);

-- Interactive locations (for world placement)
CREATE TABLE IF NOT EXISTS interactive_locations (
    id BIGSERIAL PRIMARY KEY,
    interactive_type_id BIGINT NOT NULL REFERENCES interactive_types(id) ON DELETE CASCADE,
    world_location VARCHAR(100) NOT NULL, -- Format: "continent/city/district"
    coordinates_x DECIMAL(10,2),
    coordinates_y DECIMAL(10,2),
    coordinates_z DECIMAL(10,2),
    is_active BOOLEAN DEFAULT true,
    current_health INTEGER,
    last_interaction TIMESTAMP WITH TIME ZONE,
    controlled_by_faction VARCHAR(50),
    security_status VARCHAR(20) DEFAULT 'normal' CHECK (security_status IN ('normal', 'alert', 'lockdown', 'destroyed')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Indexes for spatial queries and faction control
    INDEX idx_interactive_locations_world (world_location),
    INDEX idx_interactive_locations_coordinates (coordinates_x, coordinates_y, coordinates_z),
    INDEX idx_interactive_locations_faction (controlled_by_faction),
    INDEX idx_interactive_locations_active (is_active),
    INDEX idx_interactive_locations_security (security_status)
);

-- Interactive interactions log (for telemetry)
CREATE TABLE IF NOT EXISTS interactive_interactions (
    id BIGSERIAL PRIMARY KEY,
    location_id BIGINT NOT NULL REFERENCES interactive_locations(id) ON DELETE CASCADE,
    player_id BIGINT NOT NULL,
    interaction_type VARCHAR(50) NOT NULL CHECK (interaction_type IN ('access', 'hack', 'destroy', 'repair', 'takeover', 'loot')),
    success BOOLEAN DEFAULT false,
    duration_seconds INTEGER,
    resources_used INTEGER DEFAULT 0,
    faction_impact VARCHAR(20) CHECK (faction_impact IN ('none', 'minor', 'major', 'critical')),
    telemetry_data JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Indexes for analytics
    INDEX idx_interactive_interactions_player (player_id),
    INDEX idx_interactive_interactions_type (interaction_type),
    INDEX idx_interactive_interactions_location (location_id),
    INDEX idx_interactive_interactions_time (created_at)
);

-- Partition the interactions table by month for performance
-- This will be handled by a separate migration for existing data

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers for updated_at
CREATE TRIGGER update_world_interactives_updated_at
    BEFORE UPDATE ON world_interactives
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_interactive_types_updated_at
    BEFORE UPDATE ON interactive_types
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_interactive_locations_updated_at
    BEFORE UPDATE ON interactive_locations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions (adjust as needed for your security model)
-- GRANT SELECT, INSERT, UPDATE ON world_interactives TO necp_user;
-- GRANT SELECT, INSERT, UPDATE ON interactive_types TO necp_user;
-- GRANT SELECT, INSERT, UPDATE ON interactive_locations TO necp_user;
-- GRANT SELECT, INSERT ON interactive_interactions TO necp_user;

--rollback DROP TABLE IF EXISTS interactive_interactions;
--rollback DROP TABLE IF EXISTS interactive_locations;
--rollback DROP TABLE IF EXISTS interactive_types;
--rollback DROP TABLE IF EXISTS world_interactives;
--rollback DROP FUNCTION IF EXISTS update_updated_at_column();