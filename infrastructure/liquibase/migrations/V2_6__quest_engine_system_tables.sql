--liquibase formatted sql

--changeset necp:quest_engine_system_tables_v1 runOnChange:true
--comment: Create tables for quest engine system (guild wars, cyber missions, social intrigue, reputation contracts)

-- Quests main table
CREATE TABLE IF NOT EXISTS quests (
    id VARCHAR(100) PRIMARY KEY,
    type VARCHAR(50) NOT NULL CHECK (type IN ('guild_war', 'cyber_space_mission', 'social_intrigue', 'reputation_contract', 'dynamic_generated')),
    player_id VARCHAR(100) NOT NULL,
    template_id VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'completed', 'failed', 'abandoned')),
    objectives JSONB,
    rewards JSONB,
    progress JSONB DEFAULT '{}',
    metadata JSONB DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    completed_at TIMESTAMP WITH TIME ZONE,

    INDEX idx_quests_player_status (player_id, status),
    INDEX idx_quests_type (type),
    INDEX idx_quests_created_at (created_at DESC)
);

-- Guild war quests
CREATE TABLE IF NOT EXISTS guild_war_quests (
    quest_id VARCHAR(100) PRIMARY KEY REFERENCES quests(id) ON DELETE CASCADE,
    guild_id VARCHAR(100) NOT NULL,
    opposing_guild_id VARCHAR(100) NOT NULL,
    territory_control JSONB,
    war_duration_hours INTEGER DEFAULT 24,
    victory_conditions JSONB,
    current_phase VARCHAR(50) DEFAULT 'preparation' CHECK (current_phase IN ('preparation', 'active', 'resolution', 'completed')),

    INDEX idx_guild_war_quests_guilds (guild_id, opposing_guild_id),
    INDEX idx_guild_war_quests_phase (current_phase)
);

-- Cyber space mission quests
CREATE TABLE IF NOT EXISTS cyber_space_quests (
    quest_id VARCHAR(100) PRIMARY KEY REFERENCES quests(id) ON DELETE CASCADE,
    netrunner_id VARCHAR(100) NOT NULL,
    target_system VARCHAR(100) NOT NULL,
    ice_encountered JSONB,
    data_extracted JSONB,
    corruption_level INTEGER DEFAULT 0 CHECK (corruption_level >= 0 AND corruption_level <= 100),
    reality_anchor_strength INTEGER DEFAULT 100,

    INDEX idx_cyber_space_quests_netrunner (netrunner_id),
    INDEX idx_cyber_space_quests_system (target_system)
);

-- Social intrigue quests
CREATE TABLE IF NOT EXISTS social_intrigue_quests (
    quest_id VARCHAR(100) PRIMARY KEY REFERENCES quests(id) ON DELETE CASCADE,
    relationship_graph JSONB,
    involved_npcs JSONB,
    intrigue_level VARCHAR(20) DEFAULT 'low' CHECK (intrigue_level IN ('low', 'medium', 'high', 'critical')),
    reputation_impacts JSONB,
    long_term_consequences JSONB,

    INDEX idx_social_intrigue_quests_level (intrigue_level)
);

-- Reputation contract quests
CREATE TABLE IF NOT EXISTS reputation_contract_quests (
    quest_id VARCHAR(100) PRIMARY KEY REFERENCES quests(id) ON DELETE CASCADE,
    contractor_id VARCHAR(100) NOT NULL,
    target_reputation INTEGER NOT NULL,
    current_reputation INTEGER DEFAULT 0,
    contract_terms JSONB,
    deadline TIMESTAMP WITH TIME ZONE,
    penalties JSONB,

    INDEX idx_reputation_contract_quests_contractor (contractor_id),
    INDEX idx_reputation_contract_quests_deadline (deadline)
);

-- Quest templates
CREATE TABLE IF NOT EXISTS quest_templates (
    id VARCHAR(100) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    base_objectives JSONB,
    base_rewards JSONB,
    difficulty_level VARCHAR(20) DEFAULT 'normal' CHECK (difficulty_level IN ('easy', 'normal', 'hard', 'expert')),
    estimated_duration_minutes INTEGER,
    prerequisites JSONB,
    tags JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_quest_templates_type_difficulty (type, difficulty_level),
    INDEX idx_quest_templates_tags (tags)
);

-- Dynamic quest generation rules
CREATE TABLE IF NOT EXISTS quest_generation_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL UNIQUE,
    trigger_conditions JSONB,
    quest_template_ids JSONB,
    generation_probability DECIMAL(3,3) DEFAULT 0.5,
    cooldown_minutes INTEGER DEFAULT 60,
    player_level_requirements JSONB,
    active BOOLEAN DEFAULT true,

    INDEX idx_quest_generation_rules_active (active)
);

-- Quest events for event sourcing
CREATE TABLE IF NOT EXISTS quest_events (
    id BIGSERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL REFERENCES quests(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL,
    event_data JSONB,
    player_id VARCHAR(100),
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_quest_events_quest (quest_id),
    INDEX idx_quest_events_type (event_type),
    INDEX idx_quest_events_timestamp (timestamp DESC)
);

-- Quest telemetry
CREATE TABLE IF NOT EXISTS quest_telemetry (
    id BIGSERIAL PRIMARY KEY,
    quest_id VARCHAR(100) NOT NULL REFERENCES quests(id) ON DELETE CASCADE,
    player_id VARCHAR(100) NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    action_data JSONB,
    session_duration_seconds INTEGER,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    INDEX idx_quest_telemetry_quest (quest_id),
    INDEX idx_quest_telemetry_player (player_id),
    INDEX idx_quest_telemetry_timestamp (timestamp DESC)
);

-- Insert sample quest templates
INSERT INTO quest_templates (id, type, name, description, base_objectives, base_rewards, difficulty_level, estimated_duration_minutes) VALUES
('guild_war_territory_capture', 'guild_war', 'Territory Capture', 'Capture and hold enemy territory in guild warfare', '{"capture_points": 5, "hold_time_minutes": 30}', '{"eddies": 50000, "reputation": 100}', 'hard', 45),
('cyber_data_extraction', 'cyber_space_mission', 'Data Extraction', 'Infiltrate corporate network and extract sensitive data', '{"ice_breached": 3, "data_collected_gb": 10}', '{"eddies": 25000, "black_ice_fragments": 5}', 'expert', 30),
('social_blackmail', 'social_intrigue', 'Corporate Blackmail', 'Gather compromising information on corporate executive', '{"evidence_collected": 3, "discretion_maintained": true}', '{"eddies": 75000, "contacts": ["corporate_insider"]}', 'medium', 60),
('reputation_hitman', 'reputation_contract', 'Hitman Contract', 'Eliminate target and maintain reputation threshold', '{"target_eliminated": true, "reputation_maintained": 80}', '{"eddies": 100000, "reputation_bonus": 50}', 'hard', 90)
ON CONFLICT (id) DO NOTHING;

-- Issue: #1861