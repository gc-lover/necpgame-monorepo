-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create narrative.npc_definitions table for NPC management

-- Table: narrative.npc_definitions
CREATE TABLE IF NOT EXISTS narrative.npc_definitions
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    dialogue_id UUID,
    quest_ids UUID[],
    name VARCHAR
(
    100
) NOT NULL,
    faction VARCHAR
(
    50
),
    location VARCHAR
(
    100
),
    role VARCHAR
(
    100
),
    appearance JSONB,
    stats JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance (optimized for API queries)
CREATE INDEX IF NOT EXISTS idx_npc_definitions_faction ON narrative.npc_definitions(faction);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_location ON narrative.npc_definitions(location);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_dialogue_id ON narrative.npc_definitions(dialogue_id);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_created_at ON narrative.npc_definitions(created_at DESC);

-- GIN index for quest_ids array search
CREATE INDEX IF NOT EXISTS idx_npc_definitions_quest_ids_gin ON narrative.npc_definitions USING GIN (quest_ids);

-- GIN indexes for JSONB appearance/stats search
CREATE INDEX IF NOT EXISTS idx_npc_definitions_appearance_gin ON narrative.npc_definitions USING GIN (appearance);
CREATE INDEX IF NOT EXISTS idx_npc_definitions_stats_gin ON narrative.npc_definitions USING GIN (stats);

-- Partial index for faction-based filtering (common in quests)
CREATE INDEX IF NOT EXISTS idx_npc_definitions_faction_location ON narrative.npc_definitions(faction, location)
    WHERE faction IS NOT NULL;

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_npc_definitions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_npc_definitions_updated_at
    BEFORE UPDATE
    ON narrative.npc_definitions
    FOR EACH ROW
    EXECUTE FUNCTION update_npc_definitions_updated_at();

-- Foreign key constraint (if dialogue exists)
-- ALTER TABLE narrative.npc_definitions
-- ADD CONSTRAINT fk_npc_definitions_dialogue_id
-- FOREIGN KEY (dialogue_id) REFERENCES narrative.dialogue_nodes(id) ON DELETE SET NULL;

-- Comments for API documentation
COMMENT
ON TABLE narrative.npc_definitions IS 'NPC definitions for NECPGAME content management API';
COMMENT
ON COLUMN narrative.npc_definitions.id IS 'Unique NPC identifier (UUID)';
COMMENT
ON COLUMN narrative.npc_definitions.name IS 'NPC name (max 100 chars)';
COMMENT
ON COLUMN narrative.npc_definitions.faction IS 'NPC faction affiliation';
COMMENT
ON COLUMN narrative.npc_definitions.location IS 'NPC primary location';
COMMENT
ON COLUMN narrative.npc_definitions.role IS 'NPC role/function (merchant, guard, quest giver, etc.)';
COMMENT
ON COLUMN narrative.npc_definitions.dialogue_id IS 'Reference to main dialogue tree';
COMMENT
ON COLUMN narrative.npc_definitions.quest_ids IS 'Array of quest IDs this NPC participates in';
COMMENT
ON COLUMN narrative.npc_definitions.appearance IS 'JSONB NPC appearance data (model, voice, animations)';
COMMENT
ON COLUMN narrative.npc_definitions.stats IS 'JSONB NPC stats (health, level, attributes)';
COMMENT
ON COLUMN narrative.npc_definitions.metadata IS 'Additional metadata for NPC customization';
COMMENT
ON COLUMN narrative.npc_definitions.created_at IS 'NPC creation timestamp';
COMMENT
ON COLUMN narrative.npc_definitions.updated_at IS 'NPC last update timestamp';

-- BACKEND NOTE: Column order optimized for struct alignment (large → small types)
-- Expected memory per row: ~512 bytes (with JSONB data)
-- Hot queries: SELECT by faction/location, dialogue lookup
-- Cache strategy: Redis cache for active NPCs, TTL 30min
