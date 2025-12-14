--liquibase formatted sql

--changeset necp:interactive_objects_system_tables_v1 runOnChange:true
--comment: Create tables for interactive objects system (terminals, security scanners, medical stations, cargo containers)

-- Interactive objects main table
CREATE TABLE IF NOT EXISTS interactive_objects (
    id VARCHAR(100) PRIMARY KEY,
    object_type VARCHAR(50) NOT NULL CHECK (object_type IN ('terminal', 'security_scanner', 'ammo_depot', 'medical_station', 'data_node', 'black_market', 'security_door', 'elevator', 'cargo_container', 'drone_station')),
    position JSONB NOT NULL,
    zone_type VARCHAR(50) NOT NULL CHECK (zone_type IN ('urban', 'industrial', 'corporate', 'underground', 'airport', 'military_base', 'motel', 'laboratory')),
    zone_id VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'depleted', 'destroyed', 'locked', 'removed')),
    data JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_used TIMESTAMP WITH TIME ZONE,

    INDEX idx_interactive_objects_zone_status (zone_id, status),
    INDEX idx_interactive_objects_type (object_type),
    INDEX idx_interactive_objects_zone_type (zone_type),
    INDEX idx_interactive_objects_created_at (created_at DESC)
);

-- Terminal objects (hacking terminals)
CREATE TABLE IF NOT EXISTS terminal_objects (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    security_level INTEGER NOT NULL DEFAULT 1 CHECK (security_level >= 1 AND security_level <= 5),
    available_charges INTEGER NOT NULL DEFAULT 10,
    hack_success_rate DECIMAL(3,2) DEFAULT 0.7,
    data_types_available JSONB,
    alarm_probability DECIMAL(3,2) DEFAULT 0.2,
    connected_systems JSONB,

    INDEX idx_terminal_objects_security (security_level)
);

-- Security scanners
CREATE TABLE IF NOT EXISTS security_scanners (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    scanner_type VARCHAR(50) NOT NULL DEFAULT 'motion' CHECK (scanner_type IN ('motion', 'thermal', 'cybernetic', 'facial_recognition')),
    detection_range_meters INTEGER DEFAULT 20,
    false_positive_rate DECIMAL(3,2) DEFAULT 0.05,
    bypass_methods JSONB,
    alert_response_time_seconds INTEGER DEFAULT 30,

    INDEX idx_security_scanners_type (scanner_type)
);

-- Ammo depots
CREATE TABLE IF NOT EXISTS ammo_depots (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    ammo_types JSONB,
    total_capacity INTEGER NOT NULL DEFAULT 1000,
    current_stock INTEGER NOT NULL DEFAULT 1000,
    restock_cooldown_hours INTEGER DEFAULT 24,
    access_requirements JSONB,

    INDEX idx_ammo_depots_stock (current_stock)
);

-- Medical stations
CREATE TABLE IF NOT EXISTS medical_stations (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    medical_type VARCHAR(50) NOT NULL DEFAULT 'standard' CHECK (medical_type IN ('standard', 'trauma', 'cybernetic', 'experimental')),
    available_charges INTEGER NOT NULL DEFAULT 5,
    heal_amount INTEGER NOT NULL DEFAULT 50,
    cooldown_seconds INTEGER DEFAULT 300,
    side_effects JSONB,

    INDEX idx_medical_stations_type (medical_type)
);

-- Data nodes
CREATE TABLE IF NOT EXISTS data_nodes (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    data_type VARCHAR(50) NOT NULL DEFAULT 'corporate' CHECK (data_type IN ('corporate', 'personal', 'government', 'black_market')),
    data_size_gb DECIMAL(5,2) DEFAULT 1.0,
    encryption_level VARCHAR(20) DEFAULT 'basic' CHECK (encryption_level IN ('none', 'basic', 'advanced', 'military')),
    extraction_time_seconds INTEGER DEFAULT 60,
    corruption_risk DECIMAL(3,2) DEFAULT 0.1,

    INDEX idx_data_nodes_type (data_type)
);

-- Black market hubs
CREATE TABLE IF NOT EXISTS black_market_hubs (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    merchant_types JSONB,
    inventory_refresh_hours INTEGER DEFAULT 24,
    reputation_requirement INTEGER DEFAULT 0,
    bust_probability DECIMAL(3,2) DEFAULT 0.05,
    special_offers JSONB,

    INDEX idx_black_market_hubs_reputation (reputation_requirement)
);

-- Security doors
CREATE TABLE IF NOT EXISTS security_doors (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    access_level VARCHAR(20) NOT NULL DEFAULT 'restricted' CHECK (access_level IN ('public', 'restricted', 'confidential', 'classified')),
    lock_type VARCHAR(50) NOT NULL DEFAULT 'electronic' CHECK (lock_type IN ('electronic', 'biometric', 'keycard', 'manual')),
    bypass_difficulty INTEGER DEFAULT 3 CHECK (bypass_difficulty >= 1 AND bypass_difficulty <= 10),
    alarm_trigger_probability DECIMAL(3,2) DEFAULT 0.8,
    connected_zones JSONB,

    INDEX idx_security_doors_access (access_level)
);

-- Cargo containers
CREATE TABLE IF NOT EXISTS cargo_containers (
    object_id VARCHAR(100) PRIMARY KEY REFERENCES interactive_objects(id) ON DELETE CASCADE,
    container_type VARCHAR(50) NOT NULL DEFAULT 'standard' CHECK (container_type IN ('standard', 'secure', 'hazardous', 'valuable')),
    loot_table JSONB,
    trap_probability DECIMAL(3,2) DEFAULT 0.3,
    trap_types JSONB,
    access_method VARCHAR(50) DEFAULT 'forced_entry' CHECK (access_method IN ('key', 'code', 'forced_entry', 'hacking')),

    INDEX idx_cargo_containers_type (container_type)
);

-- Interactive object telemetry
CREATE TABLE IF NOT EXISTS interactive_object_telemetry (
    id BIGSERIAL PRIMARY KEY,
    object_id VARCHAR(100) NOT NULL REFERENCES interactive_objects(id) ON DELETE CASCADE,
    player_id VARCHAR(100) NOT NULL,
    interaction_type VARCHAR(50) NOT NULL,
    interaction_result VARCHAR(20) NOT NULL CHECK (interaction_result IN ('success', 'failure', 'partial', 'interrupted')),
    rewards_granted JSONB,
    damage_taken INTEGER DEFAULT 0,
    alarm_triggered BOOLEAN DEFAULT false,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_interactive_telemetry_object (object_id),
    INDEX idx_interactive_telemetry_player (player_id),
    INDEX idx_interactive_telemetry_result (interaction_result),
    INDEX idx_interactive_telemetry_timestamp (timestamp DESC)
);

-- Zone interaction density
CREATE TABLE IF NOT EXISTS zone_interaction_density (
    zone_id VARCHAR(100) PRIMARY KEY,
    zone_type VARCHAR(50) NOT NULL,
    total_objects INTEGER DEFAULT 0,
    active_objects INTEGER DEFAULT 0,
    interactions_last_hour INTEGER DEFAULT 0,
    popular_object_types JSONB,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_zone_density_type (zone_type),
    INDEX idx_zone_density_updated (last_updated DESC)
);

-- Insert sample interactive objects for different zones
INSERT INTO interactive_objects (id, object_type, position, zone_type, zone_id, data) VALUES
('urban_terminal_001', 'terminal', '{"x": 1234.5, "y": 5678.9, "z": 0.0}', 'urban', 'night_city_district_1', '{"charges": 10, "security_level": 2}'),
('corp_security_door_001', 'security_door', '{"x": 9876.5, "y": 4321.0, "z": 0.0}', 'corporate', 'arasaka_tower_1', '{"access_level": "confidential", "bypass_difficulty": 4}'),
('industrial_ammo_depot_001', 'ammo_depot', '{"x": 5555.5, "y": 6666.6, "z": 0.0}', 'industrial', 'abandoned_factory_1', '{"ammo_types": ["7.62mm", "5.56mm"], "current_stock": 800}'),
('airport_data_node_001', 'data_node', '{"x": 1111.1, "y": 2222.2, "z": 0.0}', 'airport', 'international_terminal_1', '{"data_type": "government", "encryption_level": "advanced"}'),
('military_medical_station_001', 'medical_station', '{"x": 7777.7, "y": 8888.8, "z": 0.0}', 'military_base', 'outpost_alpha', '{"medical_type": "trauma", "available_charges": 3}')
ON CONFLICT (id) DO NOTHING;

-- Issue: #1861