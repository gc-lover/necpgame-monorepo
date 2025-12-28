-- World Cities Tables Migration
-- Creates all necessary tables for the world cities service

-- Create cities table
CREATE TABLE IF NOT EXISTS cities (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    city_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    name_local VARCHAR(255),
    country VARCHAR(100) NOT NULL,
    continent VARCHAR(50) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    population_2020 INTEGER,
    population_2050 INTEGER,
    population_2093 INTEGER,
    area_km2 DECIMAL(12, 2),
    elevation_m INTEGER,
    cyberpunk_level INTEGER CHECK (cyberpunk_level >= 1 AND cyberpunk_level <= 10),
    corruption_index DECIMAL(3, 2) CHECK (corruption_index >= 0 AND corruption_index <= 1),
    technology_index DECIMAL(3, 2) CHECK (technology_index >= 0 AND technology_index <= 1),
    zones JSONB,
    districts JSONB,
    landmarks JSONB,
    economy_data JSONB,
    corporation_presence JSONB,
    faction_influence JSONB,
    timeline_events JSONB,
    future_evolution JSONB,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deprecated')),
    is_capital BOOLEAN NOT NULL DEFAULT FALSE,
    is_megacity BOOLEAN NOT NULL DEFAULT FALSE,
    available_in_game BOOLEAN NOT NULL DEFAULT TRUE,
    game_regions JSONB,
    source_file VARCHAR(500),
    version VARCHAR(20) NOT NULL DEFAULT '1.0',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Spatial index for geographical queries
    CONSTRAINT check_latitude CHECK (latitude >= -90 AND latitude <= 90),
    CONSTRAINT check_longitude CHECK (longitude >= -180 AND longitude <= 180)
);

-- Create spatial index for location-based queries
CREATE INDEX IF NOT EXISTS idx_cities_location ON cities USING gist (point(longitude, latitude));

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_cities_city_id ON cities(city_id);
CREATE INDEX IF NOT EXISTS idx_cities_name ON cities(name);
CREATE INDEX IF NOT EXISTS idx_cities_country ON cities(country);
CREATE INDEX IF NOT EXISTS idx_cities_continent ON cities(continent);
CREATE INDEX IF NOT EXISTS idx_cities_cyberpunk_level ON cities(cyberpunk_level);
CREATE INDEX IF NOT EXISTS idx_cities_is_megacity ON cities(is_megacity);
CREATE INDEX IF NOT EXISTS idx_cities_is_capital ON cities(is_capital);
CREATE INDEX IF NOT EXISTS idx_cities_status ON cities(status);
CREATE INDEX IF NOT EXISTS idx_cities_available_in_game ON cities(available_in_game);
CREATE INDEX IF NOT EXISTS idx_cities_population_2020 ON cities(population_2020);
CREATE INDEX IF NOT EXISTS idx_cities_population_2050 ON cities(population_2050);
CREATE INDEX IF NOT EXISTS idx_cities_population_2093 ON cities(population_2093);

-- Create partial indexes for specific queries
CREATE INDEX IF NOT EXISTS idx_cities_active_megacities ON cities(is_megacity, name) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_cities_available_game_cities ON cities(available_in_game, continent) WHERE available_in_game = true;

-- Add comments for documentation
COMMENT ON TABLE cities IS 'World cities with geographical, cyberpunk, and game-specific data';
COMMENT ON COLUMN cities.id IS 'Unique identifier for the city';
COMMENT ON COLUMN cities.city_id IS 'Human-readable city identifier (e.g., "night_city")';
COMMENT ON COLUMN cities.name IS 'City name in English';
COMMENT ON COLUMN cities.name_local IS 'City name in local language';
COMMENT ON COLUMN cities.country IS 'Country where the city is located';
COMMENT ON COLUMN cities.continent IS 'Continent where the city is located';
COMMENT ON COLUMN cities.latitude IS 'Latitude coordinate';
COMMENT ON COLUMN cities.longitude IS 'Longitude coordinate';
COMMENT ON COLUMN cities.population_2020 IS 'Population in 2020';
COMMENT ON COLUMN cities.population_2050 IS 'Projected population in 2050';
COMMENT ON COLUMN cities.population_2093 IS 'Projected population in 2093';
COMMENT ON COLUMN cities.area_km2 IS 'Area in square kilometers';
COMMENT ON COLUMN cities.elevation_m IS 'Elevation above sea level in meters';
COMMENT ON COLUMN cities.cyberpunk_level IS 'Cyberpunk development level (1-10)';
COMMENT ON COLUMN cities.corruption_index IS 'Corruption index (0-1)';
COMMENT ON COLUMN cities.technology_index IS 'Technology adoption index (0-1)';
COMMENT ON COLUMN cities.zones IS 'City zones and districts data';
COMMENT ON COLUMN cities.districts IS 'Detailed district information';
COMMENT ON COLUMN cities.landmarks IS 'Important landmarks and locations';
COMMENT ON COLUMN cities.economy_data IS 'Economic indicators and data';
COMMENT ON COLUMN cities.corporation_presence IS 'Corporation presence and influence';
COMMENT ON COLUMN cities.faction_influence IS 'Faction influence and control';
COMMENT ON COLUMN cities.timeline_events IS 'Historical and future timeline events';
COMMENT ON COLUMN cities.future_evolution IS 'Future development plans and scenarios';
COMMENT ON COLUMN cities.status IS 'City status (active, inactive, deprecated)';
COMMENT ON COLUMN cities.is_capital IS 'Whether this city is a capital';
COMMENT ON COLUMN cities.is_megacity IS 'Whether this city is a megacity';
COMMENT ON COLUMN cities.available_in_game IS 'Whether the city is available in the game';
COMMENT ON COLUMN cities.game_regions IS 'Game regions where this city appears';
COMMENT ON COLUMN cities.source_file IS 'Source file for this city data';
COMMENT ON COLUMN cities.version IS 'Data version';
COMMENT ON COLUMN cities.created_at IS 'Record creation timestamp';
COMMENT ON COLUMN cities.updated_at IS 'Record last update timestamp';

-- Create trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_cities_updated_at
    BEFORE UPDATE ON cities
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

