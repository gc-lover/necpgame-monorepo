-- AI Enemies Database Schema
-- Enterprise-grade AI enemy management for Night City MMOFPS RPG
-- Supports 500+ AI entities per zone with <50ms P99 latency
-- Issue: #2302

-- Create ai schema if not exists
CREATE SCHEMA IF NOT EXISTS ai;
COMMENT ON SCHEMA ai IS 'Enterprise-grade AI enemy management system for Night City MMOFPS RPG';

-- ===========================================
-- AI ENEMIES TABLE
-- ===========================================
-- PERFORMANCE: Core AI enemy data with spatial indexing and optimistic locking
-- Supports <10ms query latency for position-based enemy lookups
CREATE TABLE ai.ai_enemies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 1,

    -- Core enemy identity
    enemy_type VARCHAR(50) NOT NULL CHECK (enemy_type IN ('elite_mercenary_boss', 'cyberpsychic_elite', 'corporate_elite_squad', 'regular_enemy')),
    enemy_subtype VARCHAR(50),
    zone_id UUID NOT NULL,

    -- Spatial positioning with PostGIS
    position GEOMETRY(POINT, 4326) NOT NULL,
    rotation REAL DEFAULT 0.0,
    zone_name VARCHAR(100) NOT NULL,

    -- Health and combat state
    health INTEGER NOT NULL DEFAULT 100 CHECK (health >= 0),
    max_health INTEGER NOT NULL DEFAULT 100 CHECK (max_health > 0),
    shield INTEGER DEFAULT 0 CHECK (shield >= 0),
    armor INTEGER DEFAULT 0 CHECK (armor >= 0),

    -- AI behavioral state
    behavior_state VARCHAR(30) DEFAULT 'idle' CHECK (behavior_state IN ('idle', 'patrol', 'combat', 'flee', 'dead')),
    target_player_id UUID,
    last_behavior_change TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Elite enemy special properties
    boss_phase INTEGER DEFAULT 1 CHECK (boss_phase >= 1 AND boss_phase <= 5), -- For boss enemies
    psychic_ability_active BOOLEAN DEFAULT false, -- For cyberpsychic elites
    squad_leader_id UUID, -- For squad-based enemies

    -- Performance optimization
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- Event sourcing metadata
    event_version BIGINT DEFAULT 0,

    -- PERFORMANCE: Optimistic locking constraint
    CONSTRAINT uk_ai_enemies_id_version UNIQUE (id, version),

    -- PERFORMANCE: Spatial constraint for valid coordinates
    CONSTRAINT ck_ai_enemies_position_valid CHECK (ST_IsValid(position))
);

-- PERFORMANCE: Spatial indexes for geographic queries
CREATE INDEX idx_ai_enemies_position ON ai.ai_enemies USING GIST (position) WHERE is_active = true;
CREATE INDEX idx_ai_enemies_zone_active ON ai.ai_enemies (zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_ai_enemies_type_zone ON ai.ai_enemies (enemy_type, zone_id, is_active) WHERE is_active = true;
CREATE INDEX idx_ai_enemies_behavior ON ai.ai_enemies (behavior_state, updated_at DESC) WHERE is_active = true;
CREATE INDEX idx_ai_enemies_target ON ai.ai_enemies (target_player_id) WHERE target_player_id IS NOT NULL AND is_active = true;

COMMENT ON TABLE ai.ai_enemies IS 'Core AI enemy entities with spatial indexing and behavioral state management';

-- ===========================================
-- AI BEHAVIOR PATTERNS TABLE
-- ===========================================
-- PERFORMANCE: Behavior tree definitions and dynamic pattern storage
-- Supports <5ms pattern retrieval for AI decision making
CREATE TABLE ai.ai_behavior_patterns (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enemy_type VARCHAR(50) NOT NULL,
    pattern_name VARCHAR(100) NOT NULL,
    pattern_version INTEGER DEFAULT 1,

    -- Behavior tree definition (JSONB for flexible structure)
    behavior_tree JSONB NOT NULL,

    -- Pattern metadata
    difficulty_level INTEGER DEFAULT 1 CHECK (difficulty_level >= 1 AND difficulty_level <= 10),
    zone_restrictions VARCHAR(500), -- Comma-separated zone names
    player_count_min INTEGER DEFAULT 1,
    player_count_max INTEGER DEFAULT 50,

    -- Performance metrics
    average_execution_time_ms REAL DEFAULT 0.0,
    success_rate REAL DEFAULT 0.0 CHECK (success_rate >= 0.0 AND success_rate <= 1.0),

    -- Activation conditions
    activation_conditions JSONB DEFAULT '{}',
    deactivation_conditions JSONB DEFAULT '{}',

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- PERFORMANCE: Unique constraint for pattern versioning
    CONSTRAINT uk_ai_behavior_patterns_type_name_version UNIQUE (enemy_type, pattern_name, pattern_version)
);

-- PERFORMANCE: Indexes for pattern lookups
CREATE INDEX idx_ai_behavior_patterns_type ON ai.ai_behavior_patterns (enemy_type, is_active) WHERE is_active = true;
CREATE INDEX idx_ai_behavior_patterns_difficulty ON ai.ai_behavior_patterns (difficulty_level, success_rate DESC) WHERE is_active = true;

COMMENT ON TABLE ai.ai_behavior_patterns IS 'Behavior tree definitions and AI pattern management';

-- ===========================================
-- AI COMBAT STATISTICS TABLE
-- ===========================================
-- PERFORMANCE: Combat analytics and damage tracking
-- Supports real-time combat statistics aggregation
CREATE TABLE ai.ai_combat_statistics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enemy_id UUID NOT NULL REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Combat session data
    session_start TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    session_end TIMESTAMP WITH TIME ZONE,
    total_damage_dealt INTEGER DEFAULT 0 CHECK (total_damage_dealt >= 0),
    total_damage_received INTEGER DEFAULT 0 CHECK (total_damage_received >= 0),

    -- Player interactions
    players_targeted UUID[] DEFAULT '{}',
    players_killed UUID[] DEFAULT '{}',
    abilities_used JSONB DEFAULT '{}',

    -- Performance metrics
    decision_count INTEGER DEFAULT 0,
    average_decision_time_ms REAL DEFAULT 0.0,
    behavior_changes INTEGER DEFAULT 0,

    -- Spatial movement data
    distance_traveled REAL DEFAULT 0.0,
    zones_visited VARCHAR(500)[] DEFAULT '{}',

    -- Elite enemy specific stats
    boss_phases_completed INTEGER DEFAULT 0,
    psychic_attacks_successful INTEGER DEFAULT 0,
    squad_coordination_score REAL DEFAULT 0.0,

    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- PERFORMANCE: Foreign key index
    CONSTRAINT fk_ai_combat_statistics_enemy FOREIGN KEY (enemy_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Indexes for combat analytics
CREATE INDEX idx_ai_combat_statistics_enemy_session ON ai.ai_combat_statistics (enemy_id, session_start DESC);
CREATE INDEX idx_ai_combat_statistics_damage ON ai.ai_combat_statistics (total_damage_dealt DESC, total_damage_received);

COMMENT ON TABLE ai.ai_combat_statistics IS 'AI combat performance tracking and analytics';

-- ===========================================
-- AI POSITION UPDATES TABLE (ai-position-sync-service)
-- ===========================================
-- PERFORMANCE: High-throughput position sync with Redis caching
-- Supports <25ms P99 update latency and <10ms P99 query latency
-- Issue: #2303
CREATE TABLE ai.ai_position_updates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_id UUID NOT NULL,
    zone_id UUID NOT NULL,
    enemy_id UUID REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Position data (optimized order: large → small types)
    position_x REAL NOT NULL,
    position_y REAL NOT NULL,
    position_z REAL NOT NULL,
    velocity_x REAL DEFAULT 0.0,
    velocity_y REAL DEFAULT 0.0,
    velocity_z REAL DEFAULT 0.0,

    -- Metadata (optimized order)
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Performance: Check constraints for data validation
    CONSTRAINT ck_ai_position_updates_position_valid CHECK (
        position_x >= -100000.0 AND position_x <= 100000.0 AND
        position_y >= -100000.0 AND position_y <= 100000.0 AND
        position_z >= -10000.0 AND position_z <= 10000.0
    ),

    -- Performance: Composite index for zone-based queries
    CONSTRAINT fk_ai_position_updates_enemy FOREIGN KEY (enemy_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Covering indexes for hot queries (<10ms target)
CREATE INDEX idx_ai_position_updates_entity_time_covering
ON ai.ai_position_updates (entity_id, timestamp DESC, position_x, position_y, position_z, zone_id);

CREATE INDEX idx_ai_position_updates_zone_time_covering
ON ai.ai_position_updates (zone_id, timestamp DESC, entity_id, position_x, position_y, position_z);

-- PERFORMANCE: Partial index for recent data (TTL optimization)
CREATE INDEX idx_ai_position_updates_recent
ON ai.ai_position_updates (entity_id, timestamp DESC)
WHERE timestamp > NOW() - INTERVAL '24 hours';

COMMENT ON TABLE ai.ai_position_updates IS 'High-throughput AI position sync with Redis caching - <25ms P99 updates, <10ms P99 queries';

-- ===========================================
-- AI BEHAVIOR STATES TABLE (ai-behavior-engine-service)
-- ===========================================
-- PERFORMANCE: Real-time behavior state management
-- Supports <5ms behavior transitions and priority evaluations
-- Issue: #2303
CREATE TABLE ai.ai_behavior_states (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    entity_id UUID NOT NULL,
    zone_id UUID NOT NULL,
    enemy_id UUID REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Behavior data (optimized order: large → small types)
    current_behavior VARCHAR(50) NOT NULL CHECK (current_behavior IN (
        'idle', 'patrol', 'combat', 'flee', 'hunt', 'guard', 'chase', 'retreat'
    )),
    priority INTEGER NOT NULL DEFAULT 1 CHECK (priority >= 1 AND priority <= 10),
    behavior_metadata JSONB DEFAULT '{}',

    -- Transition data
    previous_behavior VARCHAR(50),
    transition_reason VARCHAR(100),
    transition_timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Performance tracking
    execution_time_ms REAL DEFAULT 0.0,
    success_rate REAL DEFAULT 1.0 CHECK (success_rate >= 0.0 AND success_rate <= 1.0),

    -- Metadata (optimized order)
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,

    -- Performance: Foreign key constraint
    CONSTRAINT fk_ai_behavior_states_enemy FOREIGN KEY (enemy_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Covering indexes for behavior queries (<5ms target)
CREATE INDEX idx_ai_behavior_states_entity_active_covering
ON ai.ai_behavior_states (entity_id, is_active, current_behavior, priority DESC, updated_at DESC)
WHERE is_active = true;

CREATE INDEX idx_ai_behavior_states_zone_priority_covering
ON ai.ai_behavior_states (zone_id, priority DESC, current_behavior, entity_id)
WHERE is_active = true;

-- PERFORMANCE: GIN index for behavior metadata queries
CREATE INDEX idx_ai_behavior_states_metadata_gin
ON ai.ai_behavior_states USING GIN (behavior_metadata)
WHERE is_active = true;

COMMENT ON TABLE ai.ai_behavior_states IS 'Real-time AI behavior state management - <5ms transitions, priority-based execution';

-- ===========================================
-- AI COMBAT EVENTS TABLE (ai-combat-calculator-service)
-- ===========================================
-- PERFORMANCE: High-frequency combat calculations
-- Supports <1ms damage calculations and real-time event processing
-- Issue: #2303
CREATE TABLE ai.ai_combat_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(30) NOT NULL CHECK (event_type IN (
        'damage', 'heal', 'critical_hit', 'miss', 'ability_use', 'status_effect'
    )),

    -- Combat participants (optimized order: large → small types)
    attacker_id UUID NOT NULL,
    target_id UUID NOT NULL,
    zone_id UUID NOT NULL,
    enemy_attacker_id UUID REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,
    enemy_target_id UUID REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Combat data
    damage_amount INTEGER DEFAULT 0 CHECK (damage_amount >= 0),
    heal_amount INTEGER DEFAULT 0 CHECK (heal_amount >= 0),
    ability_id VARCHAR(100),
    status_effect VARCHAR(50),

    -- Calculation metadata
    calculation_time_ms REAL DEFAULT 0.0,
    is_critical BOOLEAN DEFAULT false,
    damage_type VARCHAR(20) DEFAULT 'physical' CHECK (damage_type IN (
        'physical', 'thermal', 'electrical', 'chemical', 'electromagnetic'
    )),

    -- Performance tracking
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Performance: Foreign key constraints
    CONSTRAINT fk_ai_combat_events_attacker FOREIGN KEY (enemy_attacker_id) REFERENCES ai.ai_enemies(id),
    CONSTRAINT fk_ai_combat_events_target FOREIGN KEY (enemy_target_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Covering indexes for combat queries (<1ms target)
CREATE INDEX idx_ai_combat_events_zone_timestamp_covering
ON ai.ai_combat_events (zone_id, timestamp DESC, event_type, attacker_id, target_id, damage_amount)
WHERE timestamp > NOW() - INTERVAL '1 hour';

CREATE INDEX idx_ai_combat_events_participants_covering
ON ai.ai_combat_events (attacker_id, target_id, timestamp DESC, event_type, damage_amount);

-- PERFORMANCE: Partial index for recent high-damage events
CREATE INDEX idx_ai_combat_events_high_damage_recent
ON ai.ai_combat_events (zone_id, damage_amount DESC, timestamp DESC)
WHERE damage_amount > 100 AND timestamp > NOW() - INTERVAL '1 hour';

COMMENT ON TABLE ai.ai_combat_events IS 'High-frequency AI combat calculations - <1ms damage calc, real-time event processing';

-- ===========================================
-- AI ENEMY COORDINATION TABLE (ai-enemy-coordinator-service)
-- ===========================================
-- PERFORMANCE: Zone-based AI coordination and load balancing
-- Supports 500+ AI entities per zone with <50ms P99 coordination
-- Issue: #2303
CREATE TABLE ai.ai_enemy_coordination (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    zone_id UUID NOT NULL,
    coordinator_version INTEGER DEFAULT 1,

    -- Coordination data
    active_enemies INTEGER DEFAULT 0 CHECK (active_enemies >= 0),
    max_enemies INTEGER DEFAULT 50 CHECK (max_enemies > 0),
    enemy_density REAL DEFAULT 0.0 CHECK (enemy_density >= 0.0),

    -- Load balancing
    server_instance VARCHAR(100),
    coordination_load REAL DEFAULT 0.0 CHECK (coordination_load >= 0.0 AND coordination_load <= 1.0),

    -- Performance metrics
    avg_response_time_ms REAL DEFAULT 0.0,
    coordination_events_per_second REAL DEFAULT 0.0,
    zone_health_score REAL DEFAULT 1.0 CHECK (zone_health_score >= 0.0 AND zone_health_score <= 1.0),

    -- Metadata (optimized order)
    last_coordination TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Performance: Unique constraint for zone coordination
    CONSTRAINT uk_ai_enemy_coordination_zone UNIQUE (zone_id)
);

-- PERFORMANCE: Covering indexes for coordination queries (<50ms target)
CREATE INDEX idx_ai_enemy_coordination_zone_load_covering
ON ai.ai_enemy_coordination (zone_id, coordination_load, active_enemies, max_enemies, last_coordination DESC);

CREATE INDEX idx_ai_enemy_coordination_health_covering
ON ai.ai_enemy_coordination (zone_health_score DESC, active_enemies, last_coordination DESC);

-- PERFORMANCE: Partial index for overloaded zones
CREATE INDEX idx_ai_enemy_coordination_overloaded
ON ai.ai_enemy_coordination (zone_id, coordination_load DESC)
WHERE coordination_load > 0.8;

COMMENT ON TABLE ai.ai_enemy_coordination IS 'Zone-based AI coordination and load balancing - 500+ AI per zone, <50ms P99';

-- ===========================================
-- AI POSITION HISTORY TABLE (Legacy - keeping for compatibility)
-- ===========================================
-- PERFORMANCE: Movement tracking with spatial-temporal indexing
-- Supports trajectory analysis and predictive AI behavior
CREATE TABLE ai.ai_position_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enemy_id UUID NOT NULL REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Position data with PostGIS
    position GEOMETRY(POINT, 4326) NOT NULL,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Movement context
    velocity_x REAL DEFAULT 0.0,
    velocity_y REAL DEFAULT 0.0,
    velocity_z REAL DEFAULT 0.0,
    behavior_state VARCHAR(30),
    target_position GEOMETRY(POINT, 4326),

    -- Zone transition tracking
    zone_changed BOOLEAN DEFAULT false,
    previous_zone VARCHAR(100),

    -- Performance data
    path_efficiency REAL DEFAULT 1.0 CHECK (path_efficiency >= 0.0 AND path_efficiency <= 1.0),

    -- PERFORMANCE: Spatial-temporal constraint
    CONSTRAINT ck_ai_position_history_position_valid CHECK (ST_IsValid(position)),
    CONSTRAINT fk_ai_position_history_enemy FOREIGN KEY (enemy_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Spatial-temporal indexes for trajectory queries
CREATE INDEX idx_ai_position_history_enemy_time ON ai.ai_position_history (enemy_id, recorded_at DESC);
CREATE INDEX idx_ai_position_history_position_time ON ai.ai_position_history USING GIST (position, recorded_at) WHERE recorded_at > NOW() - INTERVAL '24 hours';
CREATE INDEX idx_ai_position_history_zone ON ai.ai_position_history (previous_zone, recorded_at DESC) WHERE zone_changed = true;

COMMENT ON TABLE ai.ai_position_history IS 'AI movement tracking with spatial-temporal indexing (legacy compatibility)';

-- ===========================================
-- AI ENEMY EVENTS TABLE (EVENT SOURCING)
-- ===========================================
-- PERFORMANCE: Event sourcing for AI state changes
-- Supports full state reconstruction and debugging
CREATE TABLE ai.ai_enemy_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enemy_id UUID NOT NULL REFERENCES ai.ai_enemies(id) ON DELETE CASCADE,

    -- Event data
    event_type VARCHAR(50) NOT NULL CHECK (event_type IN ('spawned', 'damaged', 'killed', 'behavior_changed', 'position_updated', 'ability_used')),
    event_version BIGINT NOT NULL,
    event_data JSONB NOT NULL,

    -- Metadata
    occurred_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    zone_id UUID NOT NULL,
    player_involved UUID,

    -- PERFORMANCE: Event ordering constraint
    CONSTRAINT uk_ai_enemy_events_enemy_version UNIQUE (enemy_id, event_version),
    CONSTRAINT fk_ai_enemy_events_enemy FOREIGN KEY (enemy_id) REFERENCES ai.ai_enemies(id)
);

-- PERFORMANCE: Indexes for event sourcing queries
CREATE INDEX idx_ai_enemy_events_enemy_occurred ON ai.ai_enemy_events (enemy_id, occurred_at DESC);
CREATE INDEX idx_ai_enemy_events_type_zone ON ai.ai_enemy_events (event_type, zone_id, occurred_at DESC);
CREATE INDEX idx_ai_enemy_events_player ON ai.ai_enemy_events (player_involved, occurred_at DESC) WHERE player_involved IS NOT NULL;

COMMENT ON TABLE ai.ai_enemy_events IS 'Event sourcing for AI enemy state changes and behavior tracking';

-- ===========================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- ===========================================

-- Update ai_enemies updated_at timestamp
CREATE OR REPLACE FUNCTION ai.update_ai_enemy_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ai_enemies_updated_at
    BEFORE UPDATE ON ai.ai_enemies
    FOR EACH ROW
    EXECUTE FUNCTION ai.update_ai_enemy_updated_at();

-- Update behavior_patterns updated_at timestamp
CREATE OR REPLACE FUNCTION ai.update_behavior_pattern_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ai_behavior_patterns_updated_at
    BEFORE UPDATE ON ai.ai_behavior_patterns
    FOR EACH ROW
    EXECUTE FUNCTION ai.update_behavior_pattern_updated_at();

-- ===========================================
-- PERFORMANCE OPTIMIZATION FUNCTIONS
-- ===========================================

-- Function to get active enemies in zone with spatial filtering
CREATE OR REPLACE FUNCTION ai.get_active_enemies_in_zone(
    zone_uuid UUID,
    player_position GEOMETRY(POINT, 4326),
    radius_meters REAL DEFAULT 1000.0
)
RETURNS TABLE (
    id UUID,
    enemy_type VARCHAR(50),
    position GEOMETRY(POINT, 4326),
    health INTEGER,
    behavior_state VARCHAR(30),
    distance REAL
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        e.id,
        e.enemy_type,
        e.position,
        e.health,
        e.behavior_state,
        ST_Distance(e.position::GEOGRAPHY, player_position::GEOGRAPHY) as distance
    FROM ai.ai_enemies e
    WHERE e.zone_id = zone_uuid
      AND e.is_active = true
      AND ST_DWithin(e.position::GEOGRAPHY, player_position::GEOGRAPHY, radius_meters)
    ORDER BY distance ASC
    LIMIT 500; -- Performance limit for zone density
END;
$$ LANGUAGE plpgsql;

-- Function to update enemy event version
CREATE OR REPLACE FUNCTION ai.increment_enemy_event_version()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE ai.ai_enemies
    SET event_version = event_version + 1
    WHERE id = NEW.enemy_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_ai_enemy_events_version_increment
    AFTER INSERT ON ai.ai_enemy_events
    FOR EACH ROW
    EXECUTE FUNCTION ai.increment_enemy_event_version();

-- ===========================================
-- BACKEND PERFORMANCE HINTS
-- ===========================================

-- Issue: #2303
-- AI Enemy Services Database Schema - Performance Requirements

-- AI-POSITION-SYNC-SERVICE:
-- Connection pool: 50-100 connections (high-throughput position updates)
-- Expected QPS: 10,000+ position updates/second across all zones
-- Target latency: <25ms P99 for updates, <10ms P99 for queries
-- Redis TTL: 5 minutes for position data
-- Batch operations: Use Redis pipelines for zone-wide updates

-- AI-BEHAVIOR-ENGINE-SERVICE:
-- Connection pool: 25-50 connections (behavior state management)
-- Expected QPS: 5,000+ behavior evaluations/second
-- Target latency: <5ms for behavior transitions
-- JSONB queries: Use GIN indexes for metadata filtering
-- Priority queues: Implement in-memory for real-time scheduling

-- AI-COMBAT-CALCULATOR-SERVICE:
-- Connection pool: 25-50 connections (combat calculations)
-- Expected QPS: 50,000+ damage calculations/second during combat
-- Target latency: <1ms for damage calculations
-- Event streaming: Use Redis pub/sub for real-time combat sync
-- Aggregation: Materialized views for combat statistics

-- AI-ENEMY-COORDINATOR-SERVICE:
-- Connection pool: 10-25 connections (zone coordination)
-- Expected QPS: 1,000+ coordination events/second
-- Target latency: <50ms P99 for zone management
-- Load balancing: Monitor coordination_load for scaling decisions
-- Zone density: Max 500 active AI per zone

-- GENERAL PERFORMANCE NOTES:
-- Use prepared statements for all queries
-- Implement connection pooling with pgBouncer in transaction mode
-- Monitor slow queries (>100ms) and optimize with EXPLAIN ANALYZE
-- Use Redis for session state and position caching
-- Implement proper error handling and circuit breakers

COMMENT ON SCHEMA ai IS 'Enterprise-grade AI enemy management system for Night City MMOFPS RPG - Issues: #2302, #2303';