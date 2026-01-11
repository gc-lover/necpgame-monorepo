-- Quest Systems Database Schema
-- Enterprise-grade quest management for Night City MMOFPS RPG
-- Supports 10000+ quest instances with CQRS/Event Sourcing
-- Issue: #2302

-- Create quest schema if not exists
CREATE SCHEMA IF NOT EXISTS quest;
COMMENT ON SCHEMA quest IS 'Enterprise-grade quest management system for Night City MMOFPS RPG';

-- ===========================================
-- QUEST INSTANCES TABLE (CQRS COMMAND SIDE)
-- ===========================================
-- PERFORMANCE: Active quest state tracking with CQRS pattern
-- Supports <10ms query latency for quest state retrieval
CREATE TABLE quest.quest_instances (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,

    -- Core quest identity
    quest_template_id VARCHAR(100) NOT NULL,
    player_id UUID NOT NULL,
    zone_id UUID NOT NULL,

    -- Quest state (CQRS Command side)
    status VARCHAR(20) DEFAULT 'active' CHECK (status IN ('active', 'completed', 'failed', 'abandoned')),
    progress_data JSONB NOT NULL DEFAULT '{}',

    -- Objective tracking
    objectives_completed INTEGER DEFAULT 0,
    objectives_total INTEGER DEFAULT 0,
    current_objective_id VARCHAR(50),

    -- Time management
    started_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    completed_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,

    -- Rewards and consequences
    rewards_claimed BOOLEAN DEFAULT false,
    failure_consequences JSONB DEFAULT '{}',

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- Event sourcing metadata
    event_version BIGINT DEFAULT 0,

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_quest_instances_id_version UNIQUE (id, version),

    -- PERFORMANCE: Active quest index
    CONSTRAINT ck_quest_instances_active CHECK (
        (is_active = true AND status IN ('active', 'completed', 'failed')) OR
        (is_active = false)
    )
);

-- PERFORMANCE: Indexes for quest queries
CREATE INDEX idx_quest_instances_player_active ON quest.quest_instances (player_id, is_active) WHERE is_active = true;
CREATE INDEX idx_quest_instances_zone_active ON quest.quest_instances (zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_quest_instances_template ON quest.quest_instances (quest_template_id, status) WHERE is_active = true;
CREATE INDEX idx_quest_instances_expires ON quest.quest_instances (expires_at) WHERE expires_at IS NOT NULL AND is_active = true;

COMMENT ON TABLE quest.quest_instances IS 'Active quest instances with CQRS command-side state management';

-- ===========================================
-- QUEST EVENTS TABLE (EVENT SOURCING)
-- ===========================================
-- PERFORMANCE: Event sourcing for quest state changes
-- Supports full quest history reconstruction and analytics
CREATE TABLE quest.quest_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    quest_instance_id UUID NOT NULL REFERENCES quest.quest_instances(id) ON DELETE CASCADE,

    -- Event data
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('started', 'progressed', 'completed', 'failed', 'abandoned', 'objective_updated', 'reward_claimed')),
    event_version BIGINT NOT NULL,
    event_data JSONB NOT NULL,

    -- Metadata
    occurred_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    player_id UUID NOT NULL,
    zone_id UUID NOT NULL,

    -- PERFORMANCE: Event ordering constraint
    CONSTRAINT uk_quest_events_instance_version UNIQUE (quest_instance_id, event_version),
    CONSTRAINT fk_quest_events_instance FOREIGN KEY (quest_instance_id) REFERENCES quest.quest_instances(id)
);

-- PERFORMANCE: Indexes for event sourcing queries
CREATE INDEX idx_quest_events_instance_occurred ON quest.quest_events (quest_instance_id, occurred_at DESC);
CREATE INDEX idx_quest_events_player_occurred ON quest.quest_events (player_id, occurred_at DESC);
CREATE INDEX idx_quest_events_type_zone ON quest.quest_events (event_type, zone_id, occurred_at DESC);

COMMENT ON TABLE quest.quest_events IS 'Event sourcing for quest state changes and history tracking';

-- ===========================================
-- GUILD WARS TABLE
-- ===========================================
-- PERFORMANCE: Large-scale PvP coordination (1000+ concurrent wars)
-- Supports real-time war state management with high concurrency
CREATE TABLE quest.guild_wars (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,

    -- War identity
    war_name VARCHAR(200) NOT NULL,
    attacking_guild_id UUID NOT NULL,
    defending_guild_id UUID NOT NULL,
    zone_id UUID NOT NULL,

    -- War state
    status VARCHAR(20) DEFAULT 'preparing' CHECK (status IN ('preparing', 'active', 'completed', 'cancelled')),
    war_type VARCHAR(30) DEFAULT 'territory' CHECK (war_type IN ('territory', 'resource', 'revenge', 'alliance')),

    -- Territory control
    territory_control JSONB DEFAULT '{}', -- Zone-based control percentages
    strategic_points JSONB DEFAULT '{}', -- Key locations and their status

    -- Combat statistics
    total_participants INTEGER DEFAULT 0,
    casualties_attacker INTEGER DEFAULT 0,
    casualties_defender INTEGER DEFAULT 0,
    resources_looted JSONB DEFAULT '{}',

    -- Time management
    started_at TIMESTAMP WITH TIME ZONE,
    ended_at TIMESTAMP WITH TIME ZONE,
    preparation_ends_at TIMESTAMP WITH TIME ZONE,

    -- Victory conditions
    victory_conditions JSONB NOT NULL,
    winner_guild_id UUID,

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- Event sourcing metadata
    event_version BIGINT DEFAULT 0,

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_guild_wars_id_version UNIQUE (id, version),

    -- PERFORMANCE: Prevent self-wars
    CONSTRAINT ck_guild_wars_no_self_war CHECK (attacking_guild_id != defending_guild_id)
);

-- PERFORMANCE: Indexes for war queries
CREATE INDEX idx_guild_wars_attackers_active ON quest.guild_wars (attacking_guild_id, is_active) WHERE is_active = true;
CREATE INDEX idx_guild_wars_defenders_active ON quest.guild_wars (defending_guild_id, is_active) WHERE is_active = true;
CREATE INDEX idx_guild_wars_zone_active ON quest.guild_wars (zone_id, status) WHERE is_active = true;
CREATE INDEX idx_guild_wars_status_time ON quest.guild_wars (status, started_at DESC) WHERE is_active = true;

COMMENT ON TABLE quest.guild_wars IS 'Large-scale guild warfare coordination and state management';

-- ===========================================
-- PLAYER QUEST PROGRESS TABLE
-- ===========================================
-- PERFORMANCE: Cross-system progress tracking
-- Supports quest chains and prerequisite management
CREATE TABLE quest.player_quest_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    quest_template_id VARCHAR(100) NOT NULL,

    -- Progress tracking
    completion_count INTEGER DEFAULT 0,
    best_completion_time INTERVAL,
    average_completion_time INTERVAL,

    -- Statistics
    total_attempts INTEGER DEFAULT 0,
    successful_attempts INTEGER DEFAULT 0,
    failed_attempts INTEGER DEFAULT 0,
    abandoned_count INTEGER DEFAULT 0,

    -- Rewards and achievements
    total_rewards_claimed JSONB DEFAULT '{}',
    achievements_unlocked VARCHAR(100)[] DEFAULT '{}',

    -- Time tracking
    first_attempted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_completed_at TIMESTAMP WITH TIME ZONE,
    last_attempted_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Performance data
    average_difficulty_rating REAL DEFAULT 0.0 CHECK (average_difficulty_rating >= 0.0 AND average_difficulty_rating <= 10.0),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Unique constraint for player-quest tracking
    CONSTRAINT uk_player_quest_progress_player_template UNIQUE (player_id, quest_template_id)
);

-- PERFORMANCE: Indexes for progress queries
CREATE INDEX idx_player_quest_progress_player ON quest.player_quest_progress (player_id, last_completed_at DESC);
CREATE INDEX idx_player_quest_progress_template ON quest.player_quest_progress (quest_template_id, completion_count DESC);
CREATE INDEX idx_player_quest_progress_attempts ON quest.player_quest_progress (player_id, total_attempts DESC);

COMMENT ON TABLE quest.player_quest_progress IS 'Cross-system quest progress and statistics tracking';

-- ===========================================
-- RELATIONSHIP GRAPHS TABLE
-- ===========================================
-- PERFORMANCE: Social intrigue NPC relationships with graph database features
-- Supports complex relationship networks and conspiracy mechanics
CREATE TABLE quest.relationship_graphs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Relationship identity
    source_entity_id UUID NOT NULL, -- NPC or player ID
    target_entity_id UUID NOT NULL,
    relationship_type VARCHAR(30) NOT NULL CHECK (relationship_type IN ('alliance', 'rivalry', 'romantic', 'familial', 'professional', 'hostile', 'neutral')),

    -- Relationship strength and dynamics
    strength INTEGER DEFAULT 0 CHECK (strength >= -100 AND strength <= 100), -- -100 to +100
    trust_level INTEGER DEFAULT 50 CHECK (trust_level >= 0 AND trust_level <= 100),
    last_interaction_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Relationship metadata
    established_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    relationship_data JSONB DEFAULT '{}', -- Custom relationship properties

    -- Context and zone
    zone_id UUID NOT NULL,
    context_tags VARCHAR(50)[] DEFAULT '{}', -- Tags for filtering (e.g., 'business', 'personal', 'criminal')

    -- Time-based decay
    decay_rate REAL DEFAULT 0.01 CHECK (decay_rate >= 0.0 AND decay_rate <= 1.0),
    last_decay_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Prevent self-relationships
    CONSTRAINT ck_relationship_graphs_no_self CHECK (source_entity_id != target_entity_id),

    -- PERFORMANCE: Unique relationship constraint (bidirectional)
    CONSTRAINT uk_relationship_graphs_entities UNIQUE (
        LEAST(source_entity_id, target_entity_id),
        GREATEST(source_entity_id, target_entity_id),
        relationship_type
    )
);

-- PERFORMANCE: Indexes for relationship queries
CREATE INDEX idx_relationship_graphs_source ON quest.relationship_graphs (source_entity_id, strength DESC);
CREATE INDEX idx_relationship_graphs_target ON quest.relationship_graphs (target_entity_id, strength DESC);
CREATE INDEX idx_relationship_graphs_zone_type ON quest.relationship_graphs (zone_id, relationship_type);
CREATE INDEX idx_relationship_graphs_strength ON quest.relationship_graphs (strength DESC) WHERE strength > 0;
CREATE INDEX idx_relationship_graphs_interaction ON quest.relationship_graphs (last_interaction_at DESC);

COMMENT ON TABLE quest.relationship_graphs IS 'Social intrigue NPC relationship networks and conspiracy mechanics';

-- ===========================================
-- QUEST REPUTATION CONTRACTS TABLE
-- ===========================================
-- PERFORMANCE: Dynamic contract generation for reputation system
-- Supports procedural quest creation and competition mechanics
CREATE TABLE quest.reputation_contracts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,

    -- Contract identity
    contract_type VARCHAR(30) NOT NULL CHECK (contract_type IN ('bounty', 'escort', 'delivery', 'investigation', 'elimination')),
    issuer_entity_id UUID NOT NULL,
    target_entity_id UUID,

    -- Contract details
    title VARCHAR(200) NOT NULL,
    description TEXT,
    requirements JSONB NOT NULL,
    rewards JSONB NOT NULL,

    -- Time and difficulty
    difficulty_level INTEGER DEFAULT 1 CHECK (difficulty_level >= 1 AND difficulty_level <= 10),
    time_limit INTERVAL,
    expires_at TIMESTAMP WITH TIME ZONE,

    -- Competition system
    max_acceptors INTEGER DEFAULT 1,
    current_acceptors INTEGER DEFAULT 0,
    competition_enabled BOOLEAN DEFAULT false,

    -- Status tracking
    status VARCHAR(20) DEFAULT 'available' CHECK (status IN ('available', 'accepted', 'completed', 'expired', 'cancelled')),
    accepted_by UUID,
    completed_by UUID,
    completed_at TIMESTAMP WITH TIME ZONE,

    -- Zone and context
    zone_id UUID NOT NULL,
    contract_tags VARCHAR(50)[] DEFAULT '{}',

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_reputation_contracts_id_version UNIQUE (id, version)
);

-- PERFORMANCE: Indexes for contract queries
CREATE INDEX idx_reputation_contracts_zone_active ON quest.reputation_contracts (zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_reputation_contracts_type_difficulty ON quest.reputation_contracts (contract_type, difficulty_level) WHERE is_active = true;
CREATE INDEX idx_reputation_contracts_expires ON quest.reputation_contracts (expires_at) WHERE expires_at IS NOT NULL AND is_active = true;
CREATE INDEX idx_reputation_contracts_issuer ON quest.reputation_contracts (issuer_entity_id, status) WHERE is_active = true;

COMMENT ON TABLE quest.reputation_contracts IS 'Dynamic reputation contracts and procedural quest generation';

-- ===========================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- ===========================================

-- Update quest_instances updated_at timestamp
CREATE OR REPLACE FUNCTION quest.update_quest_instance_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_quest_instances_updated_at
    BEFORE UPDATE ON quest.quest_instances
    FOR EACH ROW
    EXECUTE FUNCTION quest.update_quest_instance_updated_at();

-- Update guild_wars updated_at timestamp
CREATE OR REPLACE FUNCTION quest.update_guild_war_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_guild_wars_updated_at
    BEFORE UPDATE ON quest.guild_wars
    FOR EACH ROW
    EXECUTE FUNCTION quest.update_guild_war_updated_at();

-- Update reputation_contracts updated_at timestamp
CREATE OR REPLACE FUNCTION quest.update_reputation_contract_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_reputation_contracts_updated_at
    BEFORE UPDATE ON quest.reputation_contracts
    FOR EACH ROW
    EXECUTE FUNCTION quest.update_reputation_contract_updated_at();

-- ===========================================
-- PERFORMANCE OPTIMIZATION FUNCTIONS
-- ===========================================

-- Function to get active quests for player
CREATE OR REPLACE FUNCTION quest.get_active_player_quests(player_uuid UUID)
RETURNS TABLE (
    id UUID,
    quest_template_id VARCHAR(100),
    status VARCHAR(20),
    progress_data JSONB,
    objectives_completed INTEGER,
    objectives_total INTEGER,
    expires_at TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        qi.id,
        qi.quest_template_id,
        qi.status,
        qi.progress_data,
        qi.objectives_completed,
        qi.objectives_total,
        qi.expires_at
    FROM quest.quest_instances qi
    WHERE qi.player_id = player_uuid
      AND qi.is_active = true
      AND qi.status = 'active'
    ORDER BY qi.created_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to get active guild wars in zone
CREATE OR REPLACE FUNCTION quest.get_active_guild_wars_in_zone(zone_uuid UUID)
RETURNS TABLE (
    id UUID,
    war_name VARCHAR(200),
    attacking_guild_id UUID,
    defending_guild_id UUID,
    status VARCHAR(20),
    started_at TIMESTAMP WITH TIME ZONE,
    territory_control JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        gw.id,
        gw.war_name,
        gw.attacking_guild_id,
        gw.defending_guild_id,
        gw.status,
        gw.started_at,
        gw.territory_control
    FROM quest.guild_wars gw
    WHERE gw.zone_id = zone_uuid
      AND gw.is_active = true
      AND gw.status IN ('preparing', 'active')
    ORDER BY gw.started_at DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to update quest event version
CREATE OR REPLACE FUNCTION quest.increment_quest_event_version()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE quest.quest_instances
    SET event_version = event_version + 1
    WHERE id = NEW.quest_instance_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_quest_events_version_increment
    AFTER INSERT ON quest.quest_events
    FOR EACH ROW
    EXECUTE FUNCTION quest.increment_quest_event_version();

-- Function to update guild war event version
CREATE OR REPLACE FUNCTION quest.increment_guild_war_event_version()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE quest.guild_wars
    SET event_version = event_version + 1
    WHERE id = NEW.guild_war_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON SCHEMA quest IS 'Enterprise-grade quest management system for Night City MMOFPS RPG - Issue: #2302';