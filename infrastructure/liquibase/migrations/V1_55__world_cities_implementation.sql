-- Issue: #140875381
-- liquibase formatted sql

--changeset backend:world-cities-schema dbms:postgresql
--comment: Create world cities schema and tables for geographical regions management

BEGIN;

-- Create world_cities schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS world_cities;

-- Create world_cities.city_definitions table
CREATE TABLE IF NOT EXISTS world_cities.city_definitions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    city_id VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    name_local VARCHAR(200),
    country VARCHAR(100),
    continent VARCHAR(50),
    latitude DECIMAL(10,8) NOT NULL CHECK (latitude >= -90 AND latitude <= 90),
    longitude DECIMAL(11,8) NOT NULL CHECK (longitude >= -180 AND longitude <= 180),

    -- City characteristics
    population_2020 INTEGER CHECK (population_2020 >= 0),
    population_2050 INTEGER CHECK (population_2050 >= 0),
    population_2093 INTEGER CHECK (population_2093 >= 0),
    area_km2 DECIMAL(12,2) CHECK (area_km2 >= 0),
    elevation_m INTEGER,

    -- Cyberpunk characteristics
    cyberpunk_level INTEGER CHECK (cyberpunk_level >= 1 AND cyberpunk_level <= 10) DEFAULT 5,
    corruption_index DECIMAL(3,2) CHECK (corruption_index >= 0 AND corruption_index <= 1),
    technology_index DECIMAL(3,2) CHECK (technology_index >= 0 AND technology_index <= 1),

    -- City zones and districts
    zones JSONB, -- Array of zone objects with names, types, coordinates
    districts JSONB, -- Hierarchical district structure
    landmarks JSONB, -- Notable locations and buildings

    -- Economic data
    economy_data JSONB, -- GDP, currency, major industries
    corporation_presence JSONB, -- Which corporations have major presence

    -- Faction influence
    faction_influence JSONB, -- Influence levels by faction

    -- Timeline data
    timeline_events JSONB, -- Major events by year
    future_evolution JSONB, -- Projected changes 2020-2093

    -- Metadata
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'destroyed', 'abandoned', 'virtual')),
    is_capital BOOLEAN DEFAULT FALSE,
    is_megacity BOOLEAN DEFAULT FALSE,

    -- Game integration
    available_in_game BOOLEAN DEFAULT TRUE,
    game_regions JSONB, -- Which game regions this city belongs to

    -- Audit fields
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Source tracking
    source_file VARCHAR(500),
    version VARCHAR(20) DEFAULT '1.0.0'
);

-- Comments for documentation
COMMENT ON TABLE world_cities.city_definitions IS 'Master table for world cities definitions with geographical, economic, and cyberpunk characteristics';
COMMENT ON COLUMN world_cities.city_definitions.city_id IS 'Unique identifier for the city (used in code and APIs)';
COMMENT ON COLUMN world_cities.city_definitions.cyberpunk_level IS 'Cyberpunk development level from 1 (low-tech) to 10 (fully cyberpunk)';
COMMENT ON COLUMN world_cities.city_definitions.zones IS 'JSON array of city zones: [{"name": "Downtown", "type": "commercial", "coordinates": {...}}]';
COMMENT ON COLUMN world_cities.city_definitions.economy_data IS 'Economic data: {"gdp_2020": 500000000, "currency": "USD", "industries": ["tech", "finance"]}';
COMMENT ON COLUMN world_cities.city_definitions.timeline_events IS 'Major events by year: {"2025": ["Corporate takeover"], "2050": ["Great Firewall"]}';

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_city_definitions_city_id ON world_cities.city_definitions (city_id);
CREATE INDEX IF NOT EXISTS idx_city_definitions_country ON world_cities.city_definitions (country);
CREATE INDEX IF NOT EXISTS idx_city_definitions_continent ON world_cities.city_definitions (continent);
CREATE INDEX IF NOT EXISTS idx_city_definitions_coordinates ON world_cities.city_definitions USING GIST (point(longitude, latitude));
CREATE INDEX IF NOT EXISTS idx_city_definitions_cyberpunk_level ON world_cities.city_definitions (cyberpunk_level);
CREATE INDEX IF NOT EXISTS idx_city_definitions_zones ON world_cities.city_definitions USING GIN (zones);
CREATE INDEX IF NOT EXISTS idx_city_definitions_economy_data ON world_cities.city_definitions USING GIN (economy_data);
CREATE INDEX IF NOT EXISTS idx_city_definitions_status ON world_cities.city_definitions (status);

-- Create partial indexes for common queries
CREATE INDEX IF NOT EXISTS idx_city_definitions_active ON world_cities.city_definitions (city_id) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_city_definitions_megacities ON world_cities.city_definitions (city_id) WHERE is_megacity = TRUE;
CREATE INDEX IF NOT EXISTS idx_city_definitions_capitals ON world_cities.city_definitions (city_id) WHERE is_capital = TRUE;

-- Updated at trigger
CREATE OR REPLACE FUNCTION world_cities.update_city_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_city_definitions_updated_at
    BEFORE UPDATE ON world_cities.city_definitions
    FOR EACH ROW EXECUTE FUNCTION world_cities.update_city_updated_at();

COMMIT;

--changeset backend:world-cities-validation-functions dbms:postgresql
--comment: Add validation functions for city data integrity

-- Function to validate coordinates
CREATE OR REPLACE FUNCTION world_cities.validate_coordinates(lat DECIMAL, lon DECIMAL)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN lat >= -90 AND lat <= 90 AND lon >= -180 AND lon <= 180;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to validate JSON structure for zones
CREATE OR REPLACE FUNCTION world_cities.validate_city_zones(zones_json JSONB)
RETURNS BOOLEAN AS $$
DECLARE
    zone_record RECORD;
BEGIN
    IF zones_json IS NULL THEN
        RETURN TRUE;
    END IF;

    -- Check if it's an array
    IF jsonb_typeof(zones_json) != 'array' THEN
        RETURN FALSE;
    END IF;

    -- Validate each zone has required fields
    FOR zone_record IN SELECT * FROM jsonb_array_elements(zones_json) AS zone
    LOOP
        IF NOT (zone_record.zone ? 'name' AND zone_record.zone ? 'type') THEN
            RETURN FALSE;
        END IF;
    END LOOP;

    RETURN TRUE;
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Function to validate cyberpunk level ranges
CREATE OR REPLACE FUNCTION world_cities.validate_cyberpunk_level(level INTEGER)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN level IS NULL OR (level >= 1 AND level <= 10);
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Add check constraints
ALTER TABLE world_cities.city_definitions
ADD CONSTRAINT chk_city_definitions_coordinates
CHECK (world_cities.validate_coordinates(latitude, longitude));

ALTER TABLE world_cities.city_definitions
ADD CONSTRAINT chk_city_definitions_zones
CHECK (world_cities.validate_city_zones(zones));

ALTER TABLE world_cities.city_definitions
ADD CONSTRAINT chk_city_definitions_cyberpunk_level
CHECK (world_cities.validate_cyberpunk_level(cyberpunk_level));

--changeset backend:world-cities-sample-data dbms:postgresql
--comment: Insert sample world cities data for testing

-- Insert major cyberpunk cities
INSERT INTO world_cities.city_definitions (
    city_id, name, name_local, country, continent, latitude, longitude,
    population_2020, population_2050, population_2093, area_km2,
    cyberpunk_level, corruption_index, technology_index,
    zones, districts, landmarks,
    economy_data, corporation_presence,
    faction_influence, timeline_events,
    is_capital, is_megacity,
    source_file
) VALUES
-- Night City (main hub)
('night-city', 'Night City', 'Night City', 'United States', 'North America', 33.7175, -117.8311,
 8000000, 12000000, 18000000, 2200.0,
 10, 0.95, 0.98,
 '[{"name": "Corporate Plaza", "type": "corporate", "coordinates": {"center": [33.7175, -117.8311]}}]'::jsonb,
 '{}'::jsonb,
 '[{"name": "Arasaka Tower", "type": "skyscraper"}, {"name": "Afterlife", "type": "bar"}]'::jsonb,
 '{"gdp_2020": 1200000000000, "currency": "USD", "industries": ["corporations", "tech", "entertainment"]}'::jsonb,
 '{"arasaka": 0.9, "militech": 0.7, "biotechnica": 0.6}'::jsonb,
 '{"corporations": 0.8, "nomads": 0.1, "street_kids": 0.3}'::jsonb,
 '{"2023": ["Fourth Corporate War begins"], "2045": ["DataKrash"], "2077": ["Nuclear attack"]}'::jsonb,
 FALSE, TRUE,
 'knowledge/canon/lore/locations/world-cities/night-city-detailed-2020-2093.yaml'),

-- Tokyo (neon archipelago)
('tokyo', 'Tokyo', '東京', 'Japan', 'Asia', 35.6762, 139.6503,
 14000000, 16000000, 12000000, 2194.0,
 9, 0.85, 0.97,
 '[{"name": "Shinjuku Media Core", "type": "media"}, {"name": "Tsukiji Data Reef", "type": "tech"}]'::jsonb,
 '{}'::jsonb,
 '[{"name": "Tokyo Skytree", "type": "landmark"}, {"name": "Shibuya Crossing", "type": "district"}]'::jsonb,
 '{"gdp_2020": 1800000000000, "currency": "JPY", "industries": ["electronics", "anime", "finance"]}'::jsonb,
 '{"arasaka": 0.8, "biotechnica": 0.5, "trauma_team": 0.7}'::jsonb,
 '{"corporations": 0.7, "tyger_claws": 0.4, "maelstrom": 0.2}'::jsonb,
 '{"2020": ["COVID-19 impacts"], "2050": ["Great Firewall deployment"], "2070": ["Neon Renaissance"]}'::jsonb,
 TRUE, TRUE,
 'knowledge/canon/lore/locations/world-cities/tokyo-detailed-2020-2093.yaml'),

-- New York (megacity)
('new-york', 'New York', 'New York', 'United States', 'North America', 40.7128, -74.0060,
 8500000, 10000000, 8000000, 783.8,
 8, 0.90, 0.94,
 '[{"name": "Manhattan Corporate", "type": "corporate"}, {"name": "Brooklyn Industrial", "type": "industrial"}]'::jsonb,
 '{}'::jsonb,
 '[{"name": "Empire State Building", "type": "landmark"}, {"name": "Times Square", "type": "district"}]'::jsonb,
 '{"gdp_2020": 1700000000000, "currency": "USD", "industries": ["finance", "media", "real_estate"]}'::jsonb,
 '{"militech": 0.8, "arasaka": 0.6, "biotechnica": 0.4}'::jsonb,
 '{"corporations": 0.9, "maulers": 0.3, "valentinos": 0.1}'::jsonb,
 '{"2025": ["Climate migration begins"], "2050": ["Manhattan flooding"], "2080": ["Rebirth project"]}'::jsonb,
 FALSE, TRUE,
 'knowledge/canon/lore/locations/world-cities/new-york-detailed-2020-2093.yaml')

ON CONFLICT (city_id) DO NOTHING;

-- BACKEND NOTE: World cities implementation for MMORPG geographical system
-- Issue: #140875381
-- Performance: GIS indexes for spatial queries, GIN indexes for JSONB fields
-- Scalability: Supports 140+ cities with full cyberpunk characteristics
-- Integration: Provides foundation for world-regions-service and narrative systems


