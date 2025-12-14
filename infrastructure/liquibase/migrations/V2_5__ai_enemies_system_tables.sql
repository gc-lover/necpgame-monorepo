--liquibase formatted sql

--changeset necp:ai_enemies_system_tables_v1 runOnChange:true
--comment: Create tables for AI enemies system (elite mercenaries, cyberpsychics, corporate squads)

-- AI enemies main table
CREATE TABLE IF NOT EXISTS ai_enemies (
    id VARCHAR(100) PRIMARY KEY,
    enemy_type VARCHAR(50) NOT NULL CHECK (enemy_type IN ('elite_mercenary_boss', 'cyberpsychic_elite', 'corporate_elite_squad', 'standard_enemy')),
    position JSONB NOT NULL,
    health JSONB NOT NULL,
    squad_id VARCHAR(100),
    zone_id VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'destroyed', 'inactive')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_ai_enemies_zone_status (zone_id, status),
    INDEX idx_ai_enemies_type (enemy_type),
    INDEX idx_ai_enemies_squad (squad_id),
    INDEX idx_ai_enemies_created_at (created_at DESC)
);

-- Elite mercenary boss types
CREATE TABLE IF NOT EXISTS elite_mercenary_bosses (
    enemy_id VARCHAR(100) PRIMARY KEY REFERENCES ai_enemies(id) ON DELETE CASCADE,
    boss_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(50) NOT NULL CHECK (specialization IN ('teleportation', 'drone_swarm', 'environmental_control')),
    threat_level INTEGER NOT NULL DEFAULT 5 CHECK (threat_level >= 1 AND threat_level <= 10),
    unique_abilities JSONB,
    loot_table JSONB,

    UNIQUE(boss_name)
);

-- Cyberpsychic elite types
CREATE TABLE IF NOT EXISTS cyberpsychic_elites (
    enemy_id VARCHAR(100) PRIMARY KEY REFERENCES ai_enemies(id) ON DELETE CASCADE,
    psychic_name VARCHAR(100) NOT NULL,
    corruption_level INTEGER NOT NULL DEFAULT 1 CHECK (corruption_level >= 1 AND corruption_level <= 5),
    illusion_types JSONB,
    mind_control_range INTEGER DEFAULT 50,
    reality_distortion_effects JSONB,

    UNIQUE(psychic_name)
);

-- Corporate elite squad types
CREATE TABLE IF NOT EXISTS corporate_elite_squads (
    enemy_id VARCHAR(100) PRIMARY KEY REFERENCES ai_enemies(id) ON DELETE CASCADE,
    corporation VARCHAR(50) NOT NULL CHECK (corporation IN ('arasaka', 'militech', 'trauma_team', 'biotechnica')),
    squad_size INTEGER NOT NULL DEFAULT 4 CHECK (squad_size >= 2 AND squad_size <= 8),
    formation_type VARCHAR(50) NOT NULL DEFAULT 'phalanx' CHECK (formation_type IN ('phalanx', 'wedge', 'circle', 'line')),
    reinforcement_cooldown_seconds INTEGER DEFAULT 300,
    tactical_abilities JSONB,

    INDEX idx_corporate_elite_squads_corp (corporation)
);

-- AI behavior telemetry
CREATE TABLE IF NOT EXISTS ai_behavior_telemetry (
    id BIGSERIAL PRIMARY KEY,
    enemy_id VARCHAR(100) NOT NULL REFERENCES ai_enemies(id) ON DELETE CASCADE,
    behavior_type VARCHAR(50) NOT NULL,
    decision_made VARCHAR(100),
    target_player_id VARCHAR(100),
    position_before JSONB,
    position_after JSONB,
    damage_dealt INTEGER DEFAULT 0,
    damage_taken INTEGER DEFAULT 0,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_ai_behavior_telemetry_enemy (enemy_id),
    INDEX idx_ai_behavior_telemetry_timestamp (timestamp DESC),
    INDEX idx_ai_behavior_telemetry_type (behavior_type)
);

-- AI spawn zones configuration
CREATE TABLE IF NOT EXISTS ai_spawn_zones (
    zone_id VARCHAR(100) PRIMARY KEY,
    zone_type VARCHAR(50) NOT NULL,
    max_enemies INTEGER NOT NULL DEFAULT 20,
    spawn_cooldown_seconds INTEGER DEFAULT 60,
    enemy_type_distribution JSONB,
    threat_level_range JSONB,
    active_since TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_ai_spawn_zones_type (zone_type)
);

-- AI difficulty scaling
CREATE TABLE IF NOT EXISTS ai_difficulty_scaling (
    id BIGSERIAL PRIMARY KEY,
    player_count INTEGER NOT NULL,
    zone_threat_modifier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    enemy_health_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    enemy_damage_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    spawn_rate_multiplier DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_ai_difficulty_scaling_active (active),
    UNIQUE(player_count, active)
);

-- Insert default difficulty scaling
INSERT INTO ai_difficulty_scaling (player_count, zone_threat_modifier, enemy_health_multiplier, enemy_damage_multiplier, spawn_rate_multiplier) VALUES
(1, 0.8, 0.9, 0.8, 0.7),
(2, 1.0, 1.0, 1.0, 1.0),
(4, 1.3, 1.2, 1.3, 1.4),
(8, 1.6, 1.4, 1.6, 1.8),
(16, 2.0, 1.6, 2.0, 2.2)
ON CONFLICT (player_count, active) DO NOTHING;

-- Issue: #1861