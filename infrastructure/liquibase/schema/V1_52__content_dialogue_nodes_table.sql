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
ON TABLE narrative.dialogue_nodes IS 'Dialogue nodes for NECPGAME conversation system';
COMMENT
ON COLUMN narrative.dialogue_nodes.id IS 'Unique dialogue node identifier (UUID)';
COMMENT
ON COLUMN narrative.dialogue_nodes.npc_id IS 'Reference to NPC who speaks this dialogue';
COMMENT
ON COLUMN narrative.dialogue_nodes.quest_id IS 'Reference to quest this dialogue belongs to';
COMMENT
ON COLUMN narrative.dialogue_nodes.node_data IS 'JSONB dialogue content (text, choices, branches)';
COMMENT
ON COLUMN narrative.dialogue_nodes.conditions IS 'JSONB conditions for showing this dialogue';
COMMENT
ON COLUMN narrative.dialogue_nodes.actions IS 'JSONB actions triggered by this dialogue';
COMMENT
ON COLUMN narrative.dialogue_nodes.metadata IS 'Additional dialogue metadata';
COMMENT
ON COLUMN narrative.dialogue_nodes.created_at IS 'Dialogue creation timestamp';
COMMENT
ON COLUMN narrative.dialogue_nodes.updated_at IS 'Dialogue last update timestamp';

-- BACKEND NOTE: Core table for dialogue system
-- Expected memory per row: ~1KB
-- Hot queries: Dialogues by NPC/quest, condition matching