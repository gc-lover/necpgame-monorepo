-- Game Mechanics Master Index Database Schema
-- Version: V001
-- Description: Complete database schema for Game Mechanics Master Index with mechanic definitions, dependencies, configurations, and health monitoring
-- Issue: #2176 - Game Mechanics Systems Master Index
-- PERFORMANCE: Optimized for MMOFPS with proper indexing and partitioning

-- =================================================================================================
-- SCHEMA CREATION
-- =================================================================================================

CREATE SCHEMA IF NOT EXISTS game_mechanics;
GRANT USAGE ON SCHEMA game_mechanics TO necpgame_app;
GRANT ALL ON SCHEMA game_mechanics TO necpgame_admin;

-- =================================================================================================
-- MECHANICS REGISTRY TABLES
-- =================================================================================================

-- Core mechanics registry table
-- PERFORMANCE: Partitioned by status for better query performance
CREATE TABLE game_mechanics.mechanics (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL DEFAULT '1.0.0',
    endpoint TEXT NOT NULL,
    mechanic_type VARCHAR(50) NOT NULL, -- combat, economy, social, world, progression, quest, vehicle, crafting, multiplayer, ui, balance, performance, security, analytics, ai
    category VARCHAR(50) NOT NULL DEFAULT 'core', -- core, optional, experimental
    status VARCHAR(20) NOT NULL DEFAULT 'active', -- active, inactive, deprecated
    priority INTEGER NOT NULL DEFAULT 5 CHECK (priority >= 1 AND priority <= 10),
    is_required BOOLEAN NOT NULL DEFAULT false,

    -- Metadata
    description TEXT,
    tags TEXT[] DEFAULT '{}',
    config_schema JSONB DEFAULT '{}',
    health_check_url TEXT,

    -- Timestamps
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
) PARTITION BY LIST (status);

-- Partitions for mechanics table
CREATE TABLE game_mechanics.mechanics_active PARTITION OF game_mechanics.mechanics
    FOR VALUES IN ('active');
CREATE TABLE game_mechanics.mechanics_inactive PARTITION OF game_mechanics.mechanics
    FOR VALUES IN ('inactive');
CREATE TABLE game_mechanics.mechanics_deprecated PARTITION OF game_mechanics.mechanics
    FOR VALUES IN ('deprecated');

-- Mechanic dependencies table
CREATE TABLE game_mechanics.dependencies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mechanic_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    depends_on_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    dependency_type VARCHAR(20) NOT NULL DEFAULT 'required', -- required, optional, conflicts
    is_hard_dependency BOOLEAN NOT NULL DEFAULT true,

    -- Metadata
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Constraints
    UNIQUE(mechanic_id, depends_on_id),
    CHECK (mechanic_id != depends_on_id) -- Prevent self-dependencies
);

-- Mechanic configurations table
-- PERFORMANCE: Partitioned by version for historical tracking
CREATE TABLE game_mechanics.configurations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mechanic_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    config_version VARCHAR(20) NOT NULL DEFAULT '1.0.0',
    settings JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,

    -- Metadata
    description TEXT,
    applied_by VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Constraints
    UNIQUE(mechanic_id, config_version)
) PARTITION BY RANGE (created_at);

-- Partitions for configurations (monthly partitions for better performance)
CREATE TABLE game_mechanics.configurations_2025_01 PARTITION OF game_mechanics.configurations
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
CREATE TABLE game_mechanics.configurations_2025_02 PARTITION OF game_mechanics.configurations
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');
CREATE TABLE game_mechanics.configurations_future PARTITION OF game_mechanics.configurations
    FOR VALUES FROM ('2025-03-01') TO ('2030-01-01');

-- Health monitoring table
-- PERFORMANCE: Partitioned by date for time-series optimization
CREATE TABLE game_mechanics.health_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mechanic_id UUID NOT NULL REFERENCES game_mechanics.mechanics(id) ON DELETE CASCADE,
    service_status VARCHAR(20) NOT NULL DEFAULT 'up', -- up, down, degraded
    response_time INTEGER, -- milliseconds
    error_count INTEGER NOT NULL DEFAULT 0,
    last_checked TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_healthy BOOLEAN NOT NULL DEFAULT true,

    -- Health metrics
    memory_usage BIGINT, -- bytes
    cpu_usage DECIMAL(5,2), -- percentage
    active_connections INTEGER,
    error_rate DECIMAL(5,2), -- percentage

    -- Metadata
    check_duration INTEGER, -- milliseconds
    error_message TEXT,

    -- Partition key
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
) PARTITION BY RANGE (created_at);

-- Partitions for health status (daily partitions for MMOFPS performance)
CREATE TABLE game_mechanics.health_status_2025_01_01 PARTITION OF game_mechanics.health_status
    FOR VALUES FROM ('2025-01-01') TO ('2025-01-02');
CREATE TABLE game_mechanics.health_status_2025_01_02 PARTITION OF game_mechanics.health_status
    FOR VALUES FROM ('2025-01-02') TO ('2025-01-03');
CREATE TABLE game_mechanics.health_status_future PARTITION OF game_mechanics.health_status
    FOR VALUES FROM ('2025-01-03') TO ('2030-01-01');

-- System-wide health metrics table
CREATE TABLE game_mechanics.system_health (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_mechanics INTEGER NOT NULL DEFAULT 0,
    active_mechanics INTEGER NOT NULL DEFAULT 0,
    inactive_mechanics INTEGER NOT NULL DEFAULT 0,
    degraded_mechanics INTEGER NOT NULL DEFAULT 0,
    health_score DECIMAL(5,2) NOT NULL DEFAULT 0.00, -- 0-100 percentage

    -- Performance metrics
    avg_response_time INTEGER, -- milliseconds
    total_errors INTEGER NOT NULL DEFAULT 0,
    system_load DECIMAL(5,2), -- percentage

    -- Metadata
    last_health_check TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    check_duration INTEGER, -- milliseconds
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- =================================================================================================
-- INDEXES FOR MMOFPS PERFORMANCE
-- =================================================================================================

-- Core mechanics indexes
CREATE INDEX CONCURRENTLY idx_mechanics_type ON game_mechanics.mechanics(mechanic_type);
CREATE INDEX CONCURRENTLY idx_mechanics_service ON game_mechanics.mechanics(service_name);
CREATE INDEX CONCURRENTLY idx_mechanics_status ON game_mechanics.mechanics(status);
CREATE INDEX CONCURRENTLY idx_mechanics_priority ON game_mechanics.mechanics(priority DESC);
CREATE INDEX CONCURRENTLY idx_mechanics_required ON game_mechanics.mechanics(is_required) WHERE is_required = true;

-- Partial indexes for active mechanics (most common queries)
CREATE INDEX CONCURRENTLY idx_mechanics_active_type ON game_mechanics.mechanics(mechanic_type)
    WHERE status = 'active';
CREATE INDEX CONCURRENTLY idx_mechanics_active_service ON game_mechanics.mechanics(service_name)
    WHERE status = 'active';

-- Dependencies indexes
CREATE INDEX CONCURRENTLY idx_dependencies_mechanic ON game_mechanics.dependencies(mechanic_id);
CREATE INDEX CONCURRENTLY idx_dependencies_depends_on ON game_mechanics.dependencies(depends_on_id);
CREATE INDEX CONCURRENTLY idx_dependencies_type ON game_mechanics.dependencies(dependency_type);

-- Configurations indexes
CREATE INDEX CONCURRENTLY idx_configurations_mechanic ON game_mechanics.configurations(mechanic_id);
CREATE INDEX CONCURRENTLY idx_configurations_active ON game_mechanics.configurations(mechanic_id, is_active)
    WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_configurations_version ON game_mechanics.configurations(config_version);

-- Health status indexes (optimized for time-series queries)
CREATE INDEX CONCURRENTLY idx_health_mechanic_time ON game_mechanics.health_status(mechanic_id, created_at DESC);
CREATE INDEX CONCURRENTLY idx_health_status ON game_mechanics.health_status(service_status);
CREATE INDEX CONCURRENTLY idx_health_healthy ON game_mechanics.health_status(is_healthy)
    WHERE is_healthy = false; -- Index only unhealthy services for alerts

-- JSONB indexes for complex queries
CREATE INDEX CONCURRENTLY idx_mechanics_tags ON game_mechanics.mechanics USING GIN (tags);
CREATE INDEX CONCURRENTLY idx_mechanics_config_schema ON game_mechanics.mechanics USING GIN (config_schema);
CREATE INDEX CONCURRENTLY idx_configurations_settings ON game_mechanics.configurations USING GIN (settings);

-- Composite indexes for common query patterns
CREATE INDEX CONCURRENTLY idx_mechanics_type_status ON game_mechanics.mechanics(mechanic_type, status);
CREATE INDEX CONCURRENTLY idx_dependencies_mechanic_type ON game_mechanics.dependencies(mechanic_id, dependency_type);

-- =================================================================================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- =================================================================================================

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION game_mechanics.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers for updated_at
CREATE TRIGGER update_mechanics_updated_at
    BEFORE UPDATE ON game_mechanics.mechanics
    FOR EACH ROW EXECUTE FUNCTION game_mechanics.update_updated_at_column();

CREATE TRIGGER update_configurations_updated_at
    BEFORE UPDATE ON game_mechanics.configurations
    FOR EACH ROW EXECUTE FUNCTION game_mechanics.update_updated_at_column();

-- =================================================================================================
-- INITIAL DATA SEEDING
-- =================================================================================================

-- Insert initial system mechanics
INSERT INTO game_mechanics.mechanics (id, name, service_name, version, endpoint, mechanic_type, category, status, priority, is_required, description, tags, health_check_url) VALUES
-- Core Combat Mechanics
(gen_random_uuid(), 'Weapon Systems', 'combat-service-go', '1.0.0', '/api/v1/combat/weapons', 'combat', 'core', 'active', 10, true, 'Core weapon mechanics including firearms, melee, and weapon mods', ARRAY['combat', 'weapons', 'core'], '/health'),
(gen_random_uuid(), 'Damage Calculation', 'combat-service-go', '1.0.0', '/api/v1/combat/damage', 'combat', 'core', 'active', 10, true, 'Damage calculation and health management system', ARRAY['combat', 'damage', 'health'], '/health'),
(gen_random_uuid(), 'Quickhacks', 'combat-service-go', '1.0.0', '/api/v1/combat/quickhacks', 'combat', 'core', 'active', 9, true, 'Cyberpunk quickhack abilities system', ARRAY['combat', 'abilities', 'cyberpunk'], '/health'),

-- Core Economy Mechanics
(gen_random_uuid(), 'Currency Systems', 'economy-service-go', '1.0.0', '/api/v1/economy/currency', 'economy', 'core', 'active', 9, true, 'Multi-currency economic system (Eddies, Crypto, Barter)', ARRAY['economy', 'currency', 'trading'], '/health'),
(gen_random_uuid(), 'Trading Systems', 'trading-service-go', '1.0.0', '/api/v1/trading', 'economy', 'core', 'active', 8, true, 'Player-to-player and NPC trading systems', ARRAY['economy', 'trading', 'marketplace'], '/health'),
(gen_random_uuid(), 'Crafting System', 'crafting-service-go', '1.0.0', '/api/v1/crafting', 'economy', 'core', 'active', 8, true, 'Item crafting and production mechanics', ARRAY['economy', 'crafting', 'production'], '/health'),

-- Core Social Mechanics
(gen_random_uuid(), 'Relationship Systems', 'social-service-go', '1.0.0', '/api/v1/social/relationships', 'social', 'core', 'active', 7, true, 'NPC and player relationship management', ARRAY['social', 'relationships', 'npc'], '/health'),
(gen_random_uuid(), 'Guild Systems', 'guild-service-go', '1.0.0', '/api/v1/guilds', 'social', 'core', 'active', 7, true, 'Player guilds and social organizations', ARRAY['social', 'guilds', 'community'], '/health'),

-- World Mechanics
(gen_random_uuid(), 'Weather Effects', 'environment-service-go', '1.0.0', '/api/v1/world/weather', 'world', 'core', 'active', 6, true, 'Dynamic weather and environmental effects', ARRAY['world', 'environment', 'weather'], '/health'),
(gen_random_uuid(), 'Fast Travel', 'location-service-go', '1.0.0', '/api/v1/world/travel', 'world', 'core', 'active', 6, true, 'Fast travel and location management', ARRAY['world', 'travel', 'locations'], '/health'),

-- Progression Systems
(gen_random_uuid(), 'Skill Trees', 'progression-service-go', '1.0.0', '/api/v1/progression/skills', 'progression', 'core', 'active', 8, true, 'Character skill progression system', ARRAY['progression', 'skills', 'character'], '/health'),
(gen_random_uuid(), 'Experience Gain', 'experience-service-go', '1.0.0', '/api/v1/progression/experience', 'progression', 'core', 'active', 8, true, 'XP gain and leveling mechanics', ARRAY['progression', 'experience', 'leveling'], '/health'),

-- Quest Systems
(gen_random_uuid(), 'Main Story Quests', 'quest-service-go', '1.0.0', '/api/v1/quests/main', 'quest', 'core', 'active', 9, true, 'Main storyline quest management', ARRAY['quests', 'storyline', 'narrative'], '/health'),
(gen_random_uuid(), 'Dynamic Quests', 'quest-service-go', '1.0.0', '/api/v1/quests/dynamic', 'quest', 'core', 'active', 7, true, 'Procedurally generated quests', ARRAY['quests', 'dynamic', 'procedural'], '/health'),

-- Multiplayer Systems
(gen_random_uuid(), 'Matchmaking', 'matchmaking-service-go', '1.0.0', '/api/v1/multiplayer/matchmaking', 'multiplayer', 'core', 'active', 10, true, 'Player matchmaking and lobby system', ARRAY['multiplayer', 'matchmaking', 'lobby'], '/health'),
(gen_random_uuid(), 'Arena Combat', 'pvp-arena-service-go', '1.0.0', '/api/v1/multiplayer/arena', 'multiplayer', 'core', 'active', 8, true, 'PvP arena combat system', ARRAY['multiplayer', 'pvp', 'arena'], '/health'),

-- System Health Monitoring
(gen_random_uuid(), 'System Health', 'game-mechanics-master-index-service-go', '1.0.0', '/api/v1/system/health', 'performance', 'core', 'active', 10, true, 'Overall system health monitoring', ARRAY['system', 'health', 'monitoring'], '/health');

-- =================================================================================================
-- PERMISSIONS
-- =================================================================================================

-- Grant permissions to application user
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA game_mechanics TO necpgame_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA game_mechanics TO necpgame_app;

-- Grant read permissions to reporting/analytics users
GRANT SELECT ON ALL TABLES IN SCHEMA game_mechanics TO necpgame_readonly;

-- =================================================================================================
-- PARTITION MAINTENANCE
-- =================================================================================================

-- Function to create monthly partitions for configurations
CREATE OR REPLACE FUNCTION game_mechanics.create_monthly_config_partition(target_month DATE)
RETURNS VOID AS $$
DECLARE
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    start_date := date_trunc('month', target_month);
    end_date := start_date + INTERVAL '1 month';
    partition_name := 'configurations_' || to_char(start_date, 'YYYY_MM');

    -- Check if partition already exists
    IF NOT EXISTS (
        SELECT 1 FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = partition_name AND n.nspname = 'game_mechanics'
    ) THEN
        EXECUTE format('CREATE TABLE game_mechanics.%I PARTITION OF game_mechanics.configurations FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Function to create daily partitions for health status
CREATE OR REPLACE FUNCTION game_mechanics.create_daily_health_partition(target_date DATE)
RETURNS VOID AS $$
DECLARE
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    start_date := target_date;
    end_date := start_date + INTERVAL '1 day';
    partition_name := 'health_status_' || to_char(start_date, 'YYYY_MM_DD');

    -- Check if partition already exists
    IF NOT EXISTS (
        SELECT 1 FROM pg_class c
        JOIN pg_namespace n ON n.oid = c.relnamespace
        WHERE c.relname = partition_name AND n.nspname = 'game_mechanics'
    ) THEN
        EXECUTE format('CREATE TABLE game_mechanics.%I PARTITION OF game_mechanics.health_status FOR VALUES FROM (%L) TO (%L)',
            partition_name, start_date, end_date);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- VIEWS FOR COMMON QUERIES
-- =================================================================================================

-- View for active mechanics with health status
CREATE VIEW game_mechanics.active_mechanics_health AS
SELECT
    m.id,
    m.name,
    m.service_name,
    m.mechanic_type,
    m.endpoint,
    h.service_status,
    h.response_time,
    h.is_healthy,
    h.last_checked
FROM game_mechanics.mechanics m
LEFT JOIN game_mechanics.health_status h ON m.id = h.mechanic_id
    AND h.created_at = (
        SELECT MAX(created_at)
        FROM game_mechanics.health_status
        WHERE mechanic_id = m.id
    )
WHERE m.status = 'active';

-- View for mechanic dependencies with names
CREATE VIEW game_mechanics.mechanic_dependencies_detailed AS
SELECT
    d.id,
    d.mechanic_id,
    m1.name as mechanic_name,
    d.depends_on_id,
    m2.name as depends_on_name,
    d.dependency_type,
    d.is_hard_dependency,
    d.description
FROM game_mechanics.dependencies d
JOIN game_mechanics.mechanics m1 ON d.mechanic_id = m1.id
JOIN game_mechanics.mechanics m2 ON d.depends_on_id = m2.id;

-- =================================================================================================
-- MONITORING AND ALERTS
-- =================================================================================================

-- Function to calculate system health score
CREATE OR REPLACE FUNCTION game_mechanics.calculate_system_health()
RETURNS DECIMAL(5,2) AS $$
DECLARE
    total_count INTEGER;
    healthy_count INTEGER;
    health_percentage DECIMAL(5,2);
BEGIN
    SELECT COUNT(*) INTO total_count
    FROM game_mechanics.active_mechanics_health;

    SELECT COUNT(*) INTO healthy_count
    FROM game_mechanics.active_mechanics_health
    WHERE is_healthy = true;

    IF total_count = 0 THEN
        RETURN 0.00;
    END IF;

    health_percentage := (healthy_count::DECIMAL / total_count::DECIMAL) * 100.00;

    -- Insert into system_health table
    INSERT INTO game_mechanics.system_health (
        total_mechanics,
        active_mechanics,
        healthy_mechanics,
        health_score,
        last_health_check
    )
    SELECT
        total_count,
        total_count,
        healthy_count,
        health_percentage,
        NOW();

    RETURN health_percentage;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- MIGRATION METADATA
-- =================================================================================================

-- Insert migration metadata
INSERT INTO infrastructure.liquibase_migration_metadata (
    version,
    description,
    applied_at,
    checksum,
    success
) VALUES (
    'V001',
    'Create Game Mechanics Master Index tables with partitioning and performance optimizations',
    NOW(),
    'game-mechanics-master-index-v001',
    true
) ON CONFLICT (version) DO NOTHING;