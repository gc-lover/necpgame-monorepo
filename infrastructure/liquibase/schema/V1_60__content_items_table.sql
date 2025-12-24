-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create gameplay.items table for item management

-- Table: gameplay.items
CREATE TABLE IF NOT EXISTS gameplay.items
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    item_id VARCHAR
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
    'weapon',
    'armor',
    'consumable',
    'material',
    'currency',
    'key_item'
)),
    rarity VARCHAR
(
    20
) DEFAULT 'common' CHECK
(
    rarity
    IN
(
    'common',
    'uncommon',
    'rare',
    'epic',
    'legendary',
    'unique'
)),
    value INTEGER DEFAULT 0 CHECK (value >= 0),
    weight DECIMAL(5,2) DEFAULT 0.0 CHECK (weight >= 0),
    stackable BOOLEAN DEFAULT true,
    max_stack INTEGER DEFAULT 1 CHECK (max_stack > 0),
    properties JSONB,
    requirements JSONB,
    effects JSONB,
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_items_category ON gameplay.items(category);
CREATE INDEX IF NOT EXISTS idx_items_rarity ON gameplay.items(rarity);
CREATE INDEX IF NOT EXISTS idx_items_value ON gameplay.items(value);
CREATE INDEX IF NOT EXISTS idx_items_item_id ON gameplay.items(item_id);
CREATE INDEX IF NOT EXISTS idx_items_created_at ON gameplay.items(created_at DESC);

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_items_properties_gin ON gameplay.items USING GIN (properties);
CREATE INDEX IF NOT EXISTS idx_items_requirements_gin ON gameplay.items USING GIN (requirements);
CREATE INDEX IF NOT EXISTS idx_items_effects_gin ON gameplay.items USING GIN (effects);

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_items_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_items_updated_at
    BEFORE UPDATE
    ON gameplay.items
    FOR EACH ROW
    EXECUTE FUNCTION update_items_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE gameplay.items IS 'Item definitions for NECPGAME inventory system';
COMMENT
ON COLUMN gameplay.items.id IS 'Unique item identifier (UUID)';
COMMENT
ON COLUMN gameplay.items.item_id IS 'Human-readable item identifier';
COMMENT
ON COLUMN gameplay.items.name IS 'Item display name (max 200 chars)';
COMMENT
ON COLUMN gameplay.items.description IS 'Detailed item description';
COMMENT
ON COLUMN gameplay.items.category IS 'Item category classification';
COMMENT
ON COLUMN gameplay.items.rarity IS 'Item rarity tier';
COMMENT
ON COLUMN gameplay.items.value IS 'Base monetary value';
COMMENT
ON COLUMN gameplay.items.weight IS 'Item weight in kg';
COMMENT
ON COLUMN gameplay.items.properties IS 'JSONB item properties (damage, defense, etc.)';
COMMENT
ON COLUMN gameplay.items.requirements IS 'JSONB usage requirements (level, skills)';
COMMENT
ON COLUMN gameplay.items.effects IS 'JSONB item effects (buffs, debuffs)';
COMMENT
ON COLUMN gameplay.items.metadata IS 'Additional item metadata';

-- BACKEND NOTE: Core table for inventory and economy systems
-- Expected memory per row: ~1KB
-- Hot queries: Items by category/rarity, property searches
