-- Issue: #content-management-api
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create knowledge.lore_entries table for lore management

-- Table: knowledge.lore_entries
CREATE TABLE IF NOT EXISTS knowledge.lore_entries
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    gen_random_uuid
(
),
    related_entities UUID[],
    content TEXT NOT NULL,
    tags TEXT[],
    title VARCHAR
(
    200
) NOT NULL,
    category VARCHAR
(
    50
) NOT NULL CHECK
(
    category
    IN
(
    'characters',
    'factions',
    'locations',
    'events',
    'technology'
)),
    metadata JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
                             );

-- Indexes for performance (optimized for API queries)
CREATE INDEX IF NOT EXISTS idx_lore_entries_category ON knowledge.lore_entries(category);
CREATE INDEX IF NOT EXISTS idx_lore_entries_title ON knowledge.lore_entries(title);
CREATE INDEX IF NOT EXISTS idx_lore_entries_created_at ON knowledge.lore_entries(created_at DESC);

-- GIN indexes for array and JSONB searches
CREATE INDEX IF NOT EXISTS idx_lore_entries_related_entities_gin ON knowledge.lore_entries USING GIN (related_entities);
CREATE INDEX IF NOT EXISTS idx_lore_entries_tags_gin ON knowledge.lore_entries USING GIN (tags);
CREATE INDEX IF NOT EXISTS idx_lore_entries_metadata_gin ON knowledge.lore_entries USING GIN (metadata);

-- Full-text search index for content
CREATE INDEX IF NOT EXISTS idx_lore_entries_content_fts ON knowledge.lore_entries USING GIN (to_tsvector('english', content));

-- Function to update updated_at timestamp
CREATE
OR REPLACE FUNCTION update_lore_entries_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Trigger to automatically update updated_at
CREATE TRIGGER trigger_lore_entries_updated_at
    BEFORE UPDATE
    ON knowledge.lore_entries
    FOR EACH ROW
    EXECUTE FUNCTION update_lore_entries_updated_at();

-- Comments for API documentation
COMMENT
ON TABLE knowledge.lore_entries IS 'Lore entries for NECPGAME knowledge base';
COMMENT
ON COLUMN knowledge.lore_entries.id IS 'Unique lore entry identifier (UUID)';
COMMENT
ON COLUMN knowledge.lore_entries.title IS 'Lore entry title (max 200 chars)';
COMMENT
ON COLUMN knowledge.lore_entries.content IS 'Full lore content text';
COMMENT
ON COLUMN knowledge.lore_entries.category IS 'Lore category: characters, factions, locations, events, technology';
COMMENT
ON COLUMN knowledge.lore_entries.tags IS 'Text array of lore tags for categorization';
COMMENT
ON COLUMN knowledge.lore_entries.related_entities IS 'UUID array of related entities';
COMMENT
ON COLUMN knowledge.lore_entries.metadata IS 'Additional lore metadata';
COMMENT
ON COLUMN knowledge.lore_entries.created_at IS 'Lore creation timestamp';
COMMENT
ON COLUMN knowledge.lore_entries.updated_at IS 'Lore last update timestamp';

-- BACKEND NOTE: Full-text search optimized table
-- Expected memory per row: ~2KB
-- Hot queries: Category filtering, full-text search, tag matching