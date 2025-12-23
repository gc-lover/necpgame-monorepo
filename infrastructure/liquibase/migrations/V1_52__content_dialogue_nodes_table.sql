-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create narrative.dialogue_nodes table for dialogue management

-- Table: narrative.dialogue_nodes
CREATE TABLE IF NOT EXISTS narrative.dialogue_nodes
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    npc_id UUID,
    quest_id UUID,
    node_data JSONB NOT NULL,
    conditions JSONB,
    actions JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance (optimized for API queries)
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_npc_id ON narrative.dialogue_nodes(npc_id);
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_quest_id ON narrative.dialogue_nodes(quest_id);
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_created_at ON narrative.dialogue_nodes(created_at DESC);

-- Composite index for NPC + Quest dialogues
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_npc_quest ON narrative.dialogue_nodes(npc_id, quest_id);

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_node_data_gin ON narrative.dialogue_nodes USING GIN (node_data);
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_conditions_gin ON narrative.dialogue_nodes USING GIN (conditions);
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_actions_gin ON narrative.dialogue_nodes USING GIN (actions);

-- Partial index for quest-specific dialogues
CREATE INDEX IF NOT EXISTS idx_dialogue_nodes_quest_only ON narrative.dialogue_nodes(quest_id)
    WHERE quest_id IS NOT NULL;

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_dialogue_nodes_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_dialogue_nodes_updated_at
    BEFORE UPDATE
    ON narrative.dialogue_nodes
    FOR EACH ROW
    EXECUTE FUNCTION update_dialogue_nodes_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE narrative.dialogue_nodes IS 'Dialogue nodes for NECPGAME content management API';
COMMENT
ON COLUMN narrative.dialogue_nodes.id IS 'Unique dialogue node identifier (UUID)';
COMMENT
ON COLUMN narrative.dialogue_nodes.npc_id IS 'Reference to NPC who owns this dialogue';
COMMENT
ON COLUMN narrative.dialogue_nodes.quest_id IS 'Reference to quest this dialogue belongs to (optional)';
COMMENT
ON COLUMN narrative.dialogue_nodes.node_data IS 'JSONB dialogue node data (text, responses, next_node_id)';
COMMENT
ON COLUMN narrative.dialogue_nodes.conditions IS 'JSONB conditions for showing this dialogue (quest_completed, item_owned, etc.)';
COMMENT
ON COLUMN narrative.dialogue_nodes.actions IS 'JSONB actions to perform (start_quest, give_item, change_reputation)';
COMMENT
ON COLUMN narrative.dialogue_nodes.metadata IS 'Additional metadata for dialogue customization';
COMMENT
ON COLUMN narrative.dialogue_nodes.created_at IS 'Dialogue creation timestamp';
COMMENT
ON COLUMN narrative.dialogue_nodes.updated_at IS 'Dialogue last update timestamp';

-- BACKEND NOTE: Column order optimized for struct alignment (large → small types)
-- Expected memory per row: ~1KB (with JSONB dialogue data)
-- Hot queries: SELECT by npc_id, npc_id+quest_id combinations
-- Cache strategy: Redis cache for active dialogue trees, TTL 1h
