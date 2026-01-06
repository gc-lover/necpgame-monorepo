-- World Regions Tables Migration
-- Creates all necessary tables for the world regions service

-- Create regions table
CREATE TABLE IF NOT EXISTS regions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    region_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('continent', 'subregion', 'country', 'state', 'province')),
    area_km2 DECIMAL(15, 2),
    population_2020 BIGINT,
    population_2050 BIGINT,
    population_2093 BIGINT,

    -- Political data
    sovereign_states INTEGER,
    major_powers JSONB,
    conflict_zones JSONB,
    stability_index DECIMAL(3, 2) CHECK (stability_index >= 0 AND stability_index <= 1),

    -- Economic data
    gdp_total DECIMAL(20, 2),
    dominant_sectors JSONB,
    trade_hubs JSONB,
    currency_zones JSONB,

    -- Social data
    class_structure JSONB,
    cultural_diversity VARCHAR(20),
    education_level VARCHAR(20),
    healthcare_access VARCHAR(20),
    migration_patterns JSONB,

    -- Technology data
    cybernetics_adoption DECIMAL(3, 2),
    ai_integration VARCHAR(20),
    network_infrastructure VARCHAR(50),
    megacities JSONB,
    research_centers JSONB,

    -- Environmental data
    climate_zones JSONB,
    natural_resources JSONB,
    environmental_issues JSONB,
    protected_areas JSONB,

    -- Military data
    major_factions JSONB,
    conflict_types JSONB,
    strategic_resources JSONB,

    -- Game integration
    cities JSONB,
    timeline_events JSONB,
    subregions JSONB,
    game_regions JSONB,

    -- Metadata
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deprecated')),
    source_file VARCHAR(500),
    version VARCHAR(20) NOT NULL DEFAULT '1.0',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Constraints
    CONSTRAINT check_cybernetics_adoption CHECK (cybernetics_adoption >= 0 AND cybernetics_adoption <= 1)
);

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_regions_region_id ON regions(region_id);
CREATE INDEX IF NOT EXISTS idx_regions_name ON regions(name);
CREATE INDEX IF NOT EXISTS idx_regions_type ON regions(type);
CREATE INDEX IF NOT EXISTS idx_regions_status ON regions(status);
CREATE INDEX IF NOT EXISTS idx_regions_population_2020 ON regions(population_2020);
CREATE INDEX IF NOT EXISTS idx_regions_population_2050 ON regions(population_2050);
CREATE INDEX IF NOT EXISTS idx_regions_population_2093 ON regions(population_2093);
CREATE INDEX IF NOT EXISTS idx_regions_stability_index ON regions(stability_index);
CREATE INDEX IF NOT EXISTS idx_regions_cybernetics_adoption ON regions(cybernetics_adoption);

-- Create partial indexes for specific queries
CREATE INDEX IF NOT EXISTS idx_regions_active_continents ON regions(type, name) WHERE status = 'active' AND type = 'continent';
CREATE INDEX IF NOT EXISTS idx_regions_high_population ON regions(population_2020) WHERE population_2020 > 1000000000;

-- Add comments for documentation
COMMENT ON TABLE regions IS 'World regions with geographical, political, economic, and game-specific data';
COMMENT ON COLUMN regions.id IS 'Unique identifier for the region';
COMMENT ON COLUMN regions.region_id IS 'Human-readable region identifier';
COMMENT ON COLUMN regions.name IS 'Region name';
COMMENT ON COLUMN regions.type IS 'Region type (continent, subregion, country, etc.)';
COMMENT ON COLUMN regions.area_km2 IS 'Area in square kilometers';
COMMENT ON COLUMN regions.population_2020 IS 'Population in 2020';
COMMENT ON COLUMN regions.population_2050 IS 'Projected population in 2050';
COMMENT ON COLUMN regions.population_2093 IS 'Projected population in 2093';
COMMENT ON COLUMN regions.sovereign_states IS 'Number of sovereign states in the region';
COMMENT ON COLUMN regions.major_powers IS 'Major political/economic powers in the region';
COMMENT ON COLUMN regions.conflict_zones IS 'Areas of active conflict';
COMMENT ON COLUMN regions.stability_index IS 'Political stability index (0-1)';
COMMENT ON COLUMN regions.gdp_total IS 'Total GDP of the region';
COMMENT ON COLUMN regions.dominant_sectors IS 'Dominant economic sectors';
COMMENT ON COLUMN regions.trade_hubs IS 'Major trade hubs';
COMMENT ON COLUMN regions.currency_zones IS 'Currency zones used in the region';
COMMENT ON COLUMN regions.class_structure IS 'Social class structure';
COMMENT ON COLUMN regions.cultural_diversity IS 'Level of cultural diversity';
COMMENT ON COLUMN regions.education_level IS 'General education level';
COMMENT ON COLUMN regions.healthcare_access IS 'Healthcare access level';
COMMENT ON COLUMN regions.migration_patterns IS 'Migration patterns in the region';
COMMENT ON COLUMN regions.cybernetics_adoption IS 'Cybernetics adoption rate (0-1)';
COMMENT ON COLUMN regions.ai_integration IS 'Level of AI integration';
COMMENT ON COLUMN regions.network_infrastructure IS 'Network infrastructure type';
COMMENT ON COLUMN regions.megacities IS 'Major megacities in the region';
COMMENT ON COLUMN regions.research_centers IS 'Major research centers';
COMMENT ON COLUMN regions.climate_zones IS 'Climate zones in the region';
COMMENT ON COLUMN regions.natural_resources IS 'Natural resources available';
COMMENT ON COLUMN regions.environmental_issues IS 'Major environmental issues';
COMMENT ON COLUMN regions.protected_areas IS 'Protected natural areas';
COMMENT ON COLUMN regions.major_factions IS 'Major military/political factions';
COMMENT ON COLUMN regions.conflict_types IS 'Types of conflicts in the region';
COMMENT ON COLUMN regions.strategic_resources IS 'Strategic resources and locations';
COMMENT ON COLUMN regions.cities IS 'Major cities in the region';
COMMENT ON COLUMN regions.timeline_events IS 'Historical timeline events';
COMMENT ON COLUMN regions.subregions IS 'Subregions within this region';
COMMENT ON COLUMN regions.game_regions IS 'Game regions available in this area';
COMMENT ON COLUMN regions.status IS 'Region status (active, inactive, deprecated)';
COMMENT ON COLUMN regions.source_file IS 'Source file for this region data';
COMMENT ON COLUMN regions.version IS 'Data version';
COMMENT ON COLUMN regions.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN regions.updated_at IS 'Record last update timestamp';

-- Create trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_regions_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_regions_updated_at
    BEFORE UPDATE ON regions
    FOR EACH ROW
    EXECUTE FUNCTION update_regions_updated_at_column();

















