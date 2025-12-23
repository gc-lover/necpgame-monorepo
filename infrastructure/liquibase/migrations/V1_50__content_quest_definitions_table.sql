-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create gameplay.quest_definitions table for quest management

-- Table: gameplay.quest_definitions
CREATE TABLE IF NOT EXISTS gameplay.quest_definitions
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    description TEXT,
    title VARCHAR
(
    200
) NOT NULL,
    status VARCHAR
(
    20
) NOT NULL DEFAULT 'active' CHECK
(
    status
    IN
(
    'active',
    'completed',
    'archived'
)),
    rewards JSONB,
    objectives JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             level_min INTEGER NOT NULL CHECK (level_min >= 1 AND level_min <= 100),
    level_max INTEGER NOT NULL CHECK
(
    level_max
    >=
    1
    AND
    level_max
    <=
    100
)
    );

-- Indexes for performance (optimized for API queries)
CREATE INDEX IF NOT EXISTS idx_quest_definitions_status ON gameplay.quest_definitions(status);
CREATE INDEX IF NOT EXISTS idx_quest_definitions_level_range ON gameplay.quest_definitions(level_min, level_max);
CREATE INDEX IF NOT EXISTS idx_quest_definitions_created_at ON gameplay.quest_definitions(created_at DESC);

-- Partial index for active quests (most common query)
CREATE INDEX IF NOT EXISTS idx_quest_definitions_active ON gameplay.quest_definitions(level_min, level_max)
    WHERE status = 'active';

-- GIN index for JSONB rewards/objectives search
CREATE INDEX IF NOT EXISTS idx_quest_definitions_rewards_gin ON gameplay.quest_definitions USING GIN (rewards);
CREATE INDEX IF NOT EXISTS idx_quest_definitions_objectives_gin ON gameplay.quest_definitions USING GIN (objectives);

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_quest_definitions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_quest_definitions_updated_at
    BEFORE UPDATE
    ON gameplay.quest_definitions
    FOR EACH ROW
    EXECUTE FUNCTION update_quest_definitions_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE gameplay.quest_definitions IS 'Quest definitions for NECPGAME content management API';
COMMENT
ON COLUMN gameplay.quest_definitions.id IS 'Unique quest identifier (UUID)';
COMMENT
ON COLUMN gameplay.quest_definitions.title IS 'Quest title (max 200 chars)';
COMMENT
ON COLUMN gameplay.quest_definitions.description IS 'Detailed quest description';
COMMENT
ON COLUMN gameplay.quest_definitions.status IS 'Quest status: active, completed, archived';
COMMENT
ON COLUMN gameplay.quest_definitions.level_min IS 'Minimum player level required (1-100)';
COMMENT
ON COLUMN gameplay.quest_definitions.level_max IS 'Maximum player level allowed (1-100)';
COMMENT
ON COLUMN gameplay.quest_definitions.rewards IS 'JSONB array of quest rewards (experience, currency, items, reputation)';
COMMENT
ON COLUMN gameplay.quest_definitions.objectives IS 'JSONB array of quest objectives (kill, collect, deliver, explore, talk)';
COMMENT
ON COLUMN gameplay.quest_definitions.metadata IS 'Additional metadata for quest customization';
COMMENT
ON COLUMN gameplay.quest_definitions.created_at IS 'Quest creation timestamp';
COMMENT
ON COLUMN gameplay.quest_definitions.updated_at IS 'Quest last update timestamp';

-- BACKEND NOTE: Column order optimized for struct alignment (large → small types)
-- Expected memory per row: ~256 bytes
-- Hot queries: SELECT by status/level_range, JSONB searches
-- Cache strategy: Redis cache for active quests, TTL 1h
