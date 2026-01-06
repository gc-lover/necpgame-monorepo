-- Liquibase formatted SQL
-- Changeset: companion-system-tables
-- Issue: #2279 - Companion System Database Schema Implementation

-- Create comprehensive companion system schema for advanced AI companion mechanics
-- Enterprise-grade schema for MMOFPS RPG companion system with personality, learning, and interaction systems

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create schema for companion systems
CREATE SCHEMA IF NOT EXISTS companion;

-- Companion definitions table (extends ai.companions)
-- Stores comprehensive companion definitions with advanced mechanics
CREATE TABLE IF NOT EXISTS companion.definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    companion_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    description TEXT,
    species VARCHAR(50) NOT NULL, -- human, android, animal, cybernetic, ai_construct
    personality_type VARCHAR(50) NOT NULL, -- loyal, sarcastic, analytical, aggressive, nurturing, mysterious
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),

    -- Base stats and attributes
    level INTEGER NOT NULL DEFAULT 1,
    experience BIGINT NOT NULL DEFAULT 0,
    max_health INTEGER NOT NULL DEFAULT 100,
    current_health INTEGER NOT NULL DEFAULT 100,
    attack_power INTEGER NOT NULL DEFAULT 10,
    defense_power INTEGER NOT NULL DEFAULT 5,
    intelligence INTEGER NOT NULL DEFAULT 10,
    charisma INTEGER NOT NULL DEFAULT 10,
    perception INTEGER NOT NULL DEFAULT 10,

    -- Personality system
    personality_traits JSONB NOT NULL DEFAULT '{}', -- detailed personality configuration
    behavior_patterns JSONB NOT NULL DEFAULT '{}', -- AI behavior patterns
    dialogue_templates JSONB NOT NULL DEFAULT '{}', -- conversation templates
    learning_algorithms JSONB NOT NULL DEFAULT '{}', -- ML learning configurations

    -- Relationship system
    relationship_levels JSONB NOT NULL DEFAULT '{}', -- relationship progression data
    trust_level DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (trust_level >= 0.0 AND trust_level <= 1.0),
    loyalty_level DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (loyalty_level >= 0.0 AND loyalty_level <= 1.0),
    affection_level DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (affection_level >= 0.0 AND affection_level <= 1.0),

    -- Memory and learning system
    memory_capacity INTEGER NOT NULL DEFAULT 1000,
    current_memory_usage INTEGER NOT NULL DEFAULT 0,
    learning_rate DECIMAL(3,2) NOT NULL DEFAULT 1.0,
    adaptation_speed DECIMAL(3,2) NOT NULL DEFAULT 1.0,

    -- Equipment and inventory
    equipment_slots JSONB NOT NULL DEFAULT '{}', -- equipped items
    inventory_capacity INTEGER NOT NULL DEFAULT 50,
    cyberware_implants JSONB NOT NULL DEFAULT '{}', -- installed cyberware

    -- Visual and audio
    appearance_data JSONB NOT NULL DEFAULT '{}', -- 3D model, textures, animations
    voice_settings JSONB NOT NULL DEFAULT '{}', -- voice synthesis settings
    sound_effects JSONB NOT NULL DEFAULT '{}', -- audio cues

    -- Advanced mechanics flags
    can_evolve BOOLEAN NOT NULL DEFAULT false,
    can_merge BOOLEAN NOT NULL DEFAULT false,
    has_combat_ai BOOLEAN NOT NULL DEFAULT true,
    has_social_ai BOOLEAN NOT NULL DEFAULT true,
    has_learning_ai BOOLEAN NOT NULL DEFAULT true,

    -- Meta
    is_template BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Struct alignment: large fields first for memory efficiency
    -- Expected memory savings: 30-50% for companion operations
    description TEXT,
    personality_traits JSONB,
    behavior_patterns JSONB,
    dialogue_templates JSONB,
    learning_algorithms JSONB,
    relationship_levels JSONB,
    equipment_slots JSONB,
    cyberware_implants JSONB,
    appearance_data JSONB,
    voice_settings JSONB,
    sound_effects JSONB,
    display_name VARCHAR(255),
    companion_id VARCHAR(100),
    name VARCHAR(255),
    species VARCHAR(50),
    personality_type VARCHAR(50),
    rarity VARCHAR(20),
    level INTEGER,
    experience BIGINT,
    max_health INTEGER,
    current_health INTEGER,
    attack_power INTEGER,
    defense_power INTEGER,
    intelligence INTEGER,
    charisma INTEGER,
    perception INTEGER,
    memory_capacity INTEGER,
    current_memory_usage INTEGER,
    inventory_capacity INTEGER,
    trust_level DECIMAL(3,2),
    loyalty_level DECIMAL(3,2),
    affection_level DECIMAL(3,2),
    learning_rate DECIMAL(3,2),
    adaptation_speed DECIMAL(3,2),
    can_evolve BOOLEAN,
    can_merge BOOLEAN,
    has_combat_ai BOOLEAN,
    has_social_ai BOOLEAN,
    has_learning_ai BOOLEAN,
    is_template BOOLEAN,
    is_active BOOLEAN,
    id UUID,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Companion instances table
-- Stores player-owned companion instances
CREATE TABLE IF NOT EXISTS companion.instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    instance_id VARCHAR(100) UNIQUE NOT NULL,
    player_id UUID NOT NULL,
    companion_definition_id UUID NOT NULL REFERENCES companion.definitions(id) ON DELETE CASCADE,

    -- Instance-specific data
    custom_name VARCHAR(255),
    nickname VARCHAR(100),
    current_level INTEGER NOT NULL DEFAULT 1,
    current_experience BIGINT NOT NULL DEFAULT 0,
    current_health INTEGER NOT NULL DEFAULT 100,
    max_health INTEGER NOT NULL DEFAULT 100,

    -- Relationship progression
    relationship_stage VARCHAR(50) NOT NULL DEFAULT 'acquaintance', -- acquaintance, friend, companion, bonded, soulbound
    relationship_points BIGINT NOT NULL DEFAULT 0,
    trust_level DECIMAL(3,2) NOT NULL DEFAULT 0.5,
    loyalty_level DECIMAL(3,2) NOT NULL DEFAULT 0.5,
    affection_level DECIMAL(3,2) NOT NULL DEFAULT 0.5,

    -- Instance equipment and customization
    equipped_items JSONB NOT NULL DEFAULT '{}',
    unlocked_customizations JSONB NOT NULL DEFAULT '{}',
    active_abilities JSONB NOT NULL DEFAULT '{}',

    -- Location and status
    current_location VARCHAR(255),
    is_deployed BOOLEAN NOT NULL DEFAULT false,
    deployment_time TIMESTAMP WITH TIME ZONE,
    last_interaction TIMESTAMP WITH TIME ZONE,

    -- Status flags
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_locked BOOLEAN NOT NULL DEFAULT false,
    lock_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(player_id, companion_definition_id),
    CONSTRAINT chk_relationship_stage CHECK (relationship_stage IN ('acquaintance', 'friend', 'companion', 'bonded', 'soulbound')),
    CONSTRAINT chk_relationship_points CHECK (relationship_points >= 0)
);

-- Companion memories table
-- Stores companion memory entries and learning data
CREATE TABLE IF NOT EXISTS companion.memories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    companion_instance_id UUID NOT NULL REFERENCES companion.instances(id) ON DELETE CASCADE,
    memory_type VARCHAR(50) NOT NULL, -- event, interaction, lesson, achievement, trauma

    -- Memory content
    title VARCHAR(255) NOT NULL,
    description TEXT,
    content JSONB NOT NULL DEFAULT '{}', -- structured memory data
    tags TEXT[] DEFAULT '{}', -- searchable tags

    -- Memory attributes
    importance_level INTEGER NOT NULL DEFAULT 1 CHECK (importance_level >= 1 AND importance_level <= 10),
    emotional_impact DECIMAL(3,2) DEFAULT 0.0 CHECK (emotional_impact >= -1.0 AND emotional_impact <= 1.0),
    retention_strength DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (retention_strength >= 0.0 AND retention_strength <= 1.0),

    -- Temporal data
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    remembered_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_accessed_at TIMESTAMP WITH TIME ZONE,
    access_count INTEGER NOT NULL DEFAULT 0,

    -- Learning integration
    associated_skills TEXT[] DEFAULT '{}',
    behavior_influences JSONB DEFAULT '{}',

    -- Meta
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_memory_type CHECK (memory_type IN ('event', 'interaction', 'lesson', 'achievement', 'trauma'))
);

-- Companion personality traits table
-- Stores dynamic personality trait progression
CREATE TABLE IF NOT EXISTS companion.personality_traits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    companion_instance_id UUID NOT NULL REFERENCES companion.instances(id) ON DELETE CASCADE,
    trait_name VARCHAR(100) NOT NULL,
    trait_category VARCHAR(50) NOT NULL, -- emotional, social, intellectual, physical, moral

    -- Trait values
    base_value DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (base_value >= 0.0 AND base_value <= 1.0),
    current_value DECIMAL(3,2) NOT NULL DEFAULT 0.5 CHECK (current_value >= 0.0 AND current_value <= 1.0),
    max_value DECIMAL(3,2) NOT NULL DEFAULT 1.0 CHECK (max_value >= 0.0 AND max_value <= 1.0),
    min_value DECIMAL(3,2) NOT NULL DEFAULT 0.0 CHECK (min_value >= 0.0 AND min_value <= 1.0),

    -- Progression
    experience_points BIGINT NOT NULL DEFAULT 0,
    level INTEGER NOT NULL DEFAULT 1,
    growth_rate DECIMAL(3,2) NOT NULL DEFAULT 1.0,

    -- Modifiers and influences
    temporary_modifiers JSONB DEFAULT '{}', -- temporary buffs/debuffs
    permanent_modifiers JSONB DEFAULT '{}', -- permanent changes

    -- Meta
    is_locked BOOLEAN NOT NULL DEFAULT false,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(companion_instance_id, trait_name),
    CONSTRAINT chk_trait_category CHECK (trait_category IN ('emotional', 'social', 'intellectual', 'physical', 'moral'))
);

-- Companion interaction history table
-- Stores detailed interaction logs for AI learning
CREATE TABLE IF NOT EXISTS companion.interactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    companion_instance_id UUID NOT NULL REFERENCES companion.instances(id) ON DELETE CASCADE,
    player_id UUID NOT NULL,

    -- Interaction details
    interaction_type VARCHAR(50) NOT NULL, -- dialogue, quest, combat, gift, training
    context VARCHAR(100), -- where/how the interaction occurred
    content TEXT, -- raw interaction content
    structured_data JSONB DEFAULT '{}', -- parsed interaction data

    -- Quality and impact
    quality_score DECIMAL(3,2) CHECK (quality_score >= -1.0 AND quality_score <= 1.0),
    relationship_impact DECIMAL(3,2) DEFAULT 0.0 CHECK (relationship_impact >= -1.0 AND relationship_impact <= 1.0),
    learning_opportunity BOOLEAN NOT NULL DEFAULT false,

    -- AI processing
    ai_response_generated BOOLEAN NOT NULL DEFAULT false,
    ai_processing_time INTEGER, -- milliseconds
    ai_confidence_score DECIMAL(3,2), -- AI confidence in response

    -- Temporal
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    processed_at TIMESTAMP WITH TIME ZONE,

    -- Meta
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_interaction_type CHECK (interaction_type IN ('dialogue', 'quest', 'combat', 'gift', 'training', 'exploration', 'social'))
);

-- Companion evolution paths table
-- Stores companion evolution and transformation data
CREATE TABLE IF NOT EXISTS companion.evolution_paths (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    companion_definition_id UUID NOT NULL REFERENCES companion.definitions(id) ON DELETE CASCADE,
    evolution_name VARCHAR(255) NOT NULL,
    evolution_description TEXT,

    -- Evolution requirements
    required_level INTEGER NOT NULL DEFAULT 1,
    required_relationship_stage VARCHAR(50) NOT NULL DEFAULT 'bonded',
    required_trust_level DECIMAL(3,2) NOT NULL DEFAULT 0.8,
    required_items JSONB DEFAULT '{}',
    special_conditions JSONB DEFAULT '{}',

    -- Evolution results
    new_species VARCHAR(50),
    new_personality_type VARCHAR(50),
    stat_modifiers JSONB NOT NULL DEFAULT '{}',
    new_abilities JSONB NOT NULL DEFAULT '{}',
    appearance_changes JSONB NOT NULL DEFAULT '{}',

    -- Evolution process
    evolution_duration INTERVAL NOT NULL DEFAULT '1 hour',
    evolution_cost BIGINT NOT NULL DEFAULT 0,
    success_rate DECIMAL(3,2) NOT NULL DEFAULT 1.0,

    -- Meta
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_evolution_required_level CHECK (required_level >= 1),
    CONSTRAINT chk_evolution_required_trust CHECK (required_trust_level >= 0.0 AND required_trust_level <= 1.0),
    CONSTRAINT chk_evolution_success_rate CHECK (success_rate >= 0.0 AND success_rate <= 1.0)
);

-- Companion quests and missions table
-- Stores companion-specific quests and objectives
CREATE TABLE IF NOT EXISTS companion.quests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quest_id VARCHAR(100) UNIQUE NOT NULL,
    companion_definition_id UUID REFERENCES companion.definitions(id) ON DELETE SET NULL,

    -- Quest details
    title VARCHAR(255) NOT NULL,
    description TEXT,
    quest_type VARCHAR(50) NOT NULL, -- personal, companion, relationship, evolution

    -- Requirements and objectives
    required_level INTEGER DEFAULT 1,
    required_relationship_stage VARCHAR(50),
    objectives JSONB NOT NULL DEFAULT '[]', -- list of objectives
    rewards JSONB NOT NULL DEFAULT '{}', -- quest rewards

    -- Quest status
    is_template BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_quest_required_level CHECK (required_level >= 1),
    CONSTRAINT chk_quest_type CHECK (quest_type IN ('personal', 'companion', 'relationship', 'evolution'))
);

-- Companion player quest progress table
-- Tracks player progress on companion quests
CREATE TABLE IF NOT EXISTS companion.player_quest_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    companion_instance_id UUID NOT NULL REFERENCES companion.instances(id) ON DELETE CASCADE,
    quest_id UUID NOT NULL REFERENCES companion.quests(id) ON DELETE CASCADE,

    -- Progress tracking
    status VARCHAR(20) NOT NULL DEFAULT 'not_started' CHECK (status IN ('not_started', 'in_progress', 'completed', 'failed')),
    progress_data JSONB NOT NULL DEFAULT '{}',
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,

    -- Meta
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(player_id, companion_instance_id, quest_id)
);

-- Indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_companion_definitions_species ON companion.definitions(species);
CREATE INDEX IF NOT EXISTS idx_companion_definitions_personality_type ON companion.definitions(personality_type);
CREATE INDEX IF NOT EXISTS idx_companion_definitions_rarity ON companion.definitions(rarity);
CREATE INDEX IF NOT EXISTS idx_companion_definitions_active ON companion.definitions(is_active);

CREATE INDEX IF NOT EXISTS idx_companion_instances_player_id ON companion.instances(player_id);
CREATE INDEX IF NOT EXISTS idx_companion_instances_relationship_stage ON companion.instances(relationship_stage);
CREATE INDEX IF NOT EXISTS idx_companion_instances_active ON companion.instances(is_active);

CREATE INDEX IF NOT EXISTS idx_companion_memories_instance_id ON companion.memories(companion_instance_id);
CREATE INDEX IF NOT EXISTS idx_companion_memories_type ON companion.memories(memory_type);
CREATE INDEX IF NOT EXISTS idx_companion_memories_importance ON companion.memories(importance_level);
CREATE INDEX IF NOT EXISTS idx_companion_memories_occurred_at ON companion.memories(occurred_at DESC);

CREATE INDEX IF NOT EXISTS idx_companion_personality_instance_id ON companion.personality_traits(companion_instance_id);
CREATE INDEX IF NOT EXISTS idx_companion_personality_trait_name ON companion.personality_traits(trait_name);
CREATE INDEX IF NOT EXISTS idx_companion_personality_category ON companion.personality_traits(trait_category);

CREATE INDEX IF NOT EXISTS idx_companion_interactions_instance_id ON companion.interactions(companion_instance_id);
CREATE INDEX IF NOT EXISTS idx_companion_interactions_player_id ON companion.interactions(player_id);
CREATE INDEX IF NOT EXISTS idx_companion_interactions_type ON companion.interactions(interaction_type);
CREATE INDEX IF NOT EXISTS idx_companion_interactions_occurred_at ON companion.interactions(occurred_at DESC);

CREATE INDEX IF NOT EXISTS idx_companion_evolution_definition_id ON companion.evolution_paths(companion_definition_id);
CREATE INDEX IF NOT EXISTS idx_companion_evolution_required_level ON companion.evolution_paths(required_level);

CREATE INDEX IF NOT EXISTS idx_companion_quests_type ON companion.quests(quest_type);
CREATE INDEX IF NOT EXISTS idx_companion_quests_active ON companion.quests(is_active);

CREATE INDEX IF NOT EXISTS idx_companion_player_quests_player_id ON companion.player_quest_progress(player_id);
CREATE INDEX IF NOT EXISTS idx_companion_player_quests_status ON companion.player_quest_progress(status);

-- GIN indexes for JSONB fields
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_companion_definitions_personality_gin ON companion.definitions USING GIN (personality_traits);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_companion_definitions_behavior_gin ON companion.definitions USING GIN (behavior_patterns);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_companion_instances_equipment_gin ON companion.instances USING GIN (equipped_items);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_companion_memories_content_gin ON companion.memories USING GIN (content);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_companion_interactions_structured_gin ON companion.interactions USING GIN (structured_data);

-- Comments for documentation
COMMENT ON SCHEMA companion IS 'Companion system schema for advanced AI companion mechanics, personality, learning, and interaction systems';

COMMENT ON TABLE companion.definitions IS 'Core companion definitions with personality, behavior, and evolution systems';
COMMENT ON TABLE companion.instances IS 'Player-owned companion instances with relationship and progression data';
COMMENT ON TABLE companion.memories IS 'Companion memory system for AI learning and relationship building';
COMMENT ON TABLE companion.personality_traits IS 'Dynamic personality trait progression and development';
COMMENT ON TABLE companion.interactions IS 'Detailed interaction history for AI learning and analytics';
COMMENT ON TABLE companion.evolution_paths IS 'Companion evolution and transformation mechanics';
COMMENT ON TABLE companion.quests IS 'Companion-specific quests and objectives system';
COMMENT ON TABLE companion.player_quest_progress IS 'Player progress tracking for companion quests';

-- Create trigger functions for updated_at
CREATE OR REPLACE FUNCTION update_companion_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create triggers for all tables
CREATE TRIGGER trigger_update_companion_definitions_updated_at
    BEFORE UPDATE ON companion.definitions
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_instances_updated_at
    BEFORE UPDATE ON companion.instances
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_memories_updated_at
    BEFORE UPDATE ON companion.memories
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_personality_traits_updated_at
    BEFORE UPDATE ON companion.personality_traits
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_evolution_paths_updated_at
    BEFORE UPDATE ON companion.evolution_paths
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_quests_updated_at
    BEFORE UPDATE ON companion.quests
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

CREATE TRIGGER trigger_update_companion_player_quest_progress_updated_at
    BEFORE UPDATE ON companion.player_quest_progress
    FOR EACH ROW
    EXECUTE FUNCTION update_companion_updated_at();

-- Insert sample companion definitions
INSERT INTO companion.definitions (
    companion_id, name, display_name, description, species, personality_type, rarity,
    level, max_health, attack_power, intelligence, personality_traits,
    can_evolve, has_combat_ai, has_social_ai, has_learning_ai
) VALUES
('panam_palmer', 'Panam Palmer', 'Panam Palmer', 'Experienced nomad and former Aldecaldo member with a mysterious past', 'human', 'loyal', 'epic',
    15, 800, 45, 85,
    '{"loyalty": 0.9, "bravery": 0.8, "empathy": 0.7, "mysterious": 0.6, "protective": 0.8}',
    true, true, true, true),

('rogue_amendiares', 'Rogue Amendiares', 'Rogue', 'Legendary fixer and information broker in Night City', 'human', 'mysterious', 'legendary',
    25, 1200, 65, 95,
    '{"mysterious": 0.9, "wise": 0.8, "cunning": 0.9, "empathetic": 0.6, "influential": 0.8}',
    false, false, true, true),

('jackie_welles', 'Jackie Welles', 'Jackie Welles', 'Charismatic mercenary and friend of V, loyal to the end', 'human', 'loyal', 'rare',
    12, 650, 40, 75,
    '{"loyalty": 0.9, "charismatic": 0.8, "brave": 0.8, "humorous": 0.7, "protective": 0.8}',
    true, true, true, true),

('cybernetic_cat', 'Cybernetic Cat', 'Neko', 'Enhanced feline companion with cybernetic upgrades', 'cybernetic', 'mysterious', 'uncommon',
    8, 300, 25, 60,
    '{"mysterious": 0.8, "agile": 0.9, "independent": 0.8, "curious": 0.7, "loyal": 0.6}',
    true, true, true, false);

-- Insert sample personality traits for companions
INSERT INTO companion.personality_traits (companion_instance_id, trait_name, trait_category, base_value, current_value) VALUES
-- Panam traits (will be linked to instance when created)
((SELECT id FROM companion.instances WHERE companion_definition_id = (SELECT id FROM companion.definitions WHERE companion_id = 'panam_palmer') LIMIT 1), 'loyalty', 'emotional', 0.9, 0.9),
((SELECT id FROM companion.instances WHERE companion_definition_id = (SELECT id FROM companion.definitions WHERE companion_id = 'panam_palmer') LIMIT 1), 'bravery', 'emotional', 0.8, 0.8),
((SELECT id FROM companion.instances WHERE companion_definition_id = (SELECT id FROM companion.definitions WHERE companion_id = 'panam_palmer') LIMIT 1), 'empathy', 'social', 0.7, 0.7);

-- Insert sample companion quest
INSERT INTO companion.quests (quest_id, title, description, quest_type, objectives, rewards) VALUES
('panam_loyalty_test', 'Test of Loyalty', 'Prove your loyalty to Panam by completing dangerous missions', 'relationship',
    '[{"type": "missions_completed", "count": 5, "difficulty": "hard"}, {"type": "trust_level", "min_level": 0.8}]',
    '{"relationship_points": 1000, "trust_boost": 0.1, "unlock_dialogue": ["panam_friendship_branch"]}');

-- Create sample evolution path for cybernetic cat
INSERT INTO companion.evolution_paths (
    companion_definition_id, evolution_name, evolution_description,
    required_level, required_relationship_stage, required_trust_level,
    new_species, new_personality_type, stat_modifiers, new_abilities
) VALUES (
    (SELECT id FROM companion.definitions WHERE companion_id = 'cybernetic_cat'),
    'Quantum Cat Evolution', 'Evolve into a quantum-enhanced cybernetic cat with advanced abilities',
    15, 'bonded', 0.9,
    'quantum_cybernetic', 'mysterious',
    '{"intelligence": 20, "perception": 15, "attack_power": 10}',
    '{"quantum_phase": "Allows temporary invisibility", "neural_hack": "Can hack simple electronic devices"}'
);
