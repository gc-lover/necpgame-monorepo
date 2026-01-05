-- Liquibase formatted SQL
-- Changeset: ai-companions-table
-- Issue: #2266 - AI Companion Service implementation

-- Create AI companions table for storing AI personality and interaction data
CREATE TABLE IF NOT EXISTS ai.companions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    personality JSONB NOT NULL DEFAULT '{}',
    memory JSONB NOT NULL DEFAULT '{}',
    settings JSONB NOT NULL DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_ai_companions_status (status),
    INDEX idx_ai_companions_created_at (created_at DESC),

    -- Constraints
    CONSTRAINT chk_ai_name_not_empty CHECK (LENGTH(TRIM(name)) > 0),
    CONSTRAINT chk_ai_personality_valid CHECK (jsonb_typeof(personality) = 'object'),
    CONSTRAINT chk_ai_memory_valid CHECK (jsonb_typeof(memory) = 'object'),
    CONSTRAINT chk_ai_settings_valid CHECK (jsonb_typeof(settings) = 'object')
);

-- Comments for documentation
COMMENT ON TABLE ai.companions IS 'Stores AI companion personalities, memories, and interaction settings';
COMMENT ON COLUMN ai.companions.name IS 'Human-readable name for the AI companion';
COMMENT ON COLUMN ai.companions.personality IS 'Personality traits and characteristics as JSON';
COMMENT ON COLUMN ai.companions.memory IS 'Short-term and long-term memory data as JSON';
COMMENT ON COLUMN ai.companions.settings IS 'Interaction settings and preferences as JSON';
COMMENT ON COLUMN ai.companions.status IS 'Companion status: active or inactive';

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_ai_companions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_ai_companions_updated_at
    BEFORE UPDATE ON ai.companions
    FOR EACH ROW
    EXECUTE FUNCTION update_ai_companions_updated_at();

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS ai;
COMMENT ON SCHEMA ai IS 'Schema for AI companion services and data';

-- Performance optimization: Create GIN indexes for JSONB columns
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ai_companions_personality_gin ON ai.companions USING GIN (personality);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ai_companions_memory_gin ON ai.companions USING GIN (memory);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_ai_companions_settings_gin ON ai.companions USING GIN (settings);
