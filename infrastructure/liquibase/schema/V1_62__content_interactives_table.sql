-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create knowledge.interactives table for interactive object management

-- Table: knowledge.interactives
CREATE TABLE IF NOT EXISTS knowledge.interactives
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    interactive_id VARCHAR
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
    'container',
    'door',
    'terminal',
    'vehicle',
    'device',
    'furniture',
    'decoration'
)),
    location VARCHAR
(
    100
),
    interactable BOOLEAN DEFAULT true,
    reusable BOOLEAN DEFAULT true,
    requires_key BOOLEAN DEFAULT false,
    key_item_id UUID,
    interaction_type VARCHAR
(
    50
) DEFAULT 'examine' CHECK
(
    interaction_type
    IN
(
    'examine',
    'open',
    'use',
    'hack',
    'search',
    'pickup'
)),
    properties JSONB,
    requirements JSONB,
    rewards JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_interactives_category ON knowledge.interactives(category);
CREATE INDEX IF NOT EXISTS idx_interactives_location ON knowledge.interactives(location);
CREATE INDEX IF NOT EXISTS idx_interactives_interaction_type ON knowledge.interactives(interaction_type);
CREATE INDEX IF NOT EXISTS idx_interactives_interactive_id ON knowledge.interactives(interactive_id);
CREATE INDEX IF NOT EXISTS idx_interactives_key_item_id ON knowledge.interactives(key_item_id);
CREATE INDEX IF NOT EXISTS idx_interactives_created_at ON knowledge.interactives(created_at DESC);

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_interactives_properties_gin ON knowledge.interactives USING GIN (properties);
CREATE INDEX IF NOT EXISTS idx_interactives_requirements_gin ON knowledge.interactives USING GIN (requirements);
CREATE INDEX IF NOT EXISTS idx_interactives_rewards_gin ON knowledge.interactives USING GIN (rewards);

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_interactives_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_interactives_updated_at
    BEFORE UPDATE
    ON knowledge.interactives
    FOR EACH ROW
    EXECUTE FUNCTION update_interactives_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE knowledge.interactives IS 'Interactive object definitions for NECPGAME world';
COMMENT
ON COLUMN knowledge.interactives.id IS 'Unique interactive identifier (UUID)';
COMMENT
ON COLUMN knowledge.interactives.interactive_id IS 'Human-readable interactive identifier';
COMMENT
ON COLUMN knowledge.interactives.name IS 'Interactive display name (max 200 chars)';
COMMENT
ON COLUMN knowledge.interactives.description IS 'Detailed interactive description';
COMMENT
ON COLUMN knowledge.interactives.category IS 'Interactive type classification';
COMMENT
ON COLUMN knowledge.interactives.location IS 'Interactive spawn location';
COMMENT
ON COLUMN knowledge.interactives.interaction_type IS 'Type of interaction available';
COMMENT
ON COLUMN knowledge.interactives.properties IS 'JSONB interactive properties';
COMMENT
ON COLUMN knowledge.interactives.requirements IS 'JSONB interaction requirements';
COMMENT
ON COLUMN knowledge.interactives.rewards IS 'JSONB interaction rewards';
COMMENT
ON COLUMN knowledge.interactives.metadata IS 'Additional interactive metadata';

-- BACKEND NOTE: World-building table for interactive elements
-- Expected memory per row: ~1KB
-- Hot queries: Interactives by location/category, requirement checks
