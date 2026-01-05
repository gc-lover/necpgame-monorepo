-- Liquibase formatted SQL
-- Changeset: procedural-generators-table
-- Issue: #1495 - Procedural Generation Service implementation

-- Create procedural generators table for storing algorithm configurations
CREATE TABLE IF NOT EXISTS procedural.generators (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    seed BIGINT NOT NULL,
    algorithm VARCHAR(100) NOT NULL DEFAULT 'perlin_noise',
    parameters JSONB NOT NULL DEFAULT '{}',
    status VARCHAR(50) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Indexes for performance
    INDEX idx_procedural_generators_status (status),
    INDEX idx_procedural_generators_algorithm (algorithm),
    INDEX idx_procedural_generators_created_at (created_at DESC),

    -- Constraints
    CONSTRAINT chk_seed_positive CHECK (seed > 0),
    CONSTRAINT chk_name_not_empty CHECK (LENGTH(TRIM(name)) > 0)
);

-- Comments for documentation
COMMENT ON TABLE procedural.generators IS 'Stores procedural generation algorithm configurations and parameters';
COMMENT ON COLUMN procedural.generators.name IS 'Human-readable name for the generator';
COMMENT ON COLUMN procedural.generators.seed IS 'Random seed for reproducible generation';
COMMENT ON COLUMN procedural.generators.algorithm IS 'Algorithm type (perlin_noise, simplex, etc.)';
COMMENT ON COLUMN procedural.generators.parameters IS 'Algorithm-specific parameters as JSON';
COMMENT ON COLUMN procedural.generators.status IS 'Generator status: active or inactive';

-- Create trigger for updated_at
CREATE OR REPLACE FUNCTION update_procedural_generators_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_procedural_generators_updated_at
    BEFORE UPDATE ON procedural.generators
    FOR EACH ROW
    EXECUTE FUNCTION update_procedural_generators_updated_at();

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS procedural;
COMMENT ON SCHEMA procedural IS 'Schema for procedural generation services and data';
