-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create knowledge.enemies table for enemy management

-- Table: knowledge.enemies
CREATE TABLE IF NOT EXISTS knowledge.enemies
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    enemy_id VARCHAR
(
    100
) NOT NULL UNIQUE,
    name VARCHAR
(
    200
) NOT NULL,
    description TEXT,
    category VARCHAR
(
    50
) NOT NULL CHECK
(
    category
    IN
(
    'human',
    'cyberpsycho',
    'machine',
    'mutant',
    'supernatural',
    'boss'
)),
    faction VARCHAR
(
    50
),
    level_min INTEGER DEFAULT 1 CHECK (level_min >= 1),
    level_max INTEGER DEFAULT 1 CHECK (level_max >= 1),
    location VARCHAR
(
    100
),
    behavior VARCHAR
(
    50
) DEFAULT 'aggressive' CHECK
(
    behavior
    IN
(
    'passive',
    'neutral',
    'aggressive',
    'territorial'
)),
    stats JSONB,
    abilities JSONB,
    loot_table JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_enemies_category ON knowledge.enemies(category);
CREATE INDEX IF NOT EXISTS idx_enemies_faction ON knowledge.enemies(faction);
CREATE INDEX IF NOT EXISTS idx_enemies_location ON knowledge.enemies(location);
CREATE INDEX IF NOT EXISTS idx_enemies_level_range ON knowledge.enemies(level_min, level_max);
CREATE INDEX IF NOT EXISTS idx_enemies_enemy_id ON knowledge.enemies(enemy_id);
CREATE INDEX IF NOT EXISTS idx_enemies_created_at ON knowledge.enemies(created_at DESC);

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_enemies_stats_gin ON knowledge.enemies USING GIN (stats);
CREATE INDEX IF NOT EXISTS idx_enemies_abilities_gin ON knowledge.enemies USING GIN (abilities);
CREATE INDEX IF NOT EXISTS idx_enemies_loot_table_gin ON knowledge.enemies USING GIN (loot_table);

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_enemies_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_enemies_updated_at
    BEFORE UPDATE
    ON knowledge.enemies
    FOR EACH ROW
    EXECUTE FUNCTION update_enemies_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE knowledge.enemies IS 'Enemy definitions for NECPGAME combat system';
COMMENT
ON COLUMN knowledge.enemies.id IS 'Unique enemy identifier (UUID)';
COMMENT
ON COLUMN knowledge.enemies.enemy_id IS 'Human-readable enemy identifier';
COMMENT
ON COLUMN knowledge.enemies.name IS 'Enemy display name (max 200 chars)';
COMMENT
ON COLUMN knowledge.enemies.description IS 'Detailed enemy description';
COMMENT
ON COLUMN knowledge.enemies.category IS 'Enemy type classification';
COMMENT
ON COLUMN knowledge.enemies.faction IS 'Enemy faction affiliation';
COMMENT
ON COLUMN knowledge.enemies.level_min IS 'Minimum enemy level';
COMMENT
ON COLUMN knowledge.enemies.level_max IS 'Maximum enemy level';
COMMENT
ON COLUMN knowledge.enemies.location IS 'Enemy spawn location';
COMMENT
ON COLUMN knowledge.enemies.behavior IS 'Enemy behavior pattern';
COMMENT
ON COLUMN knowledge.enemies.stats IS 'JSONB enemy stats (health, damage, etc.)';
COMMENT
ON COLUMN knowledge.enemies.abilities IS 'JSONB enemy abilities and attacks';
COMMENT
ON COLUMN knowledge.enemies.loot_table IS 'JSONB loot drop definitions';
COMMENT
ON COLUMN knowledge.enemies.metadata IS 'Additional enemy metadata';

-- BACKEND NOTE: Core table for combat and AI systems
-- Expected memory per row: ~1KB
-- Hot queries: Enemies by location/level, faction filtering
