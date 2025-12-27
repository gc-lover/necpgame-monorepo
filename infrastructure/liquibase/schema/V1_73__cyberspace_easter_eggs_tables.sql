-- Issue: #2262 - Cyberspace Easter Eggs Database Schema
-- liquibase formatted sql

--changeset backend:cyberspace-easter-eggs-tables dbms:postgresql
--comment: Create cyberspace easter eggs tables for player discoveries and rewards

BEGIN;

-- Table: gameplay.easter_egg_definitions
-- Stores all cyberspace easter eggs with discovery mechanics and rewards
CREATE TABLE IF NOT EXISTS gameplay.easter_egg_definitions
(
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL CHECK (category IN ('technology', 'cultural', 'historical', 'humorous')),
    difficulty VARCHAR(20) NOT NULL DEFAULT 'medium' CHECK (difficulty IN ('easy', 'medium', 'hard', 'legendary')),
    description TEXT,
    content TEXT,
    location JSONB, -- network_type, specific_areas
    discovery_method JSONB, -- type, description, requirements
    rewards JSONB, -- array of reward objects
    lore_connections JSONB, -- array of lore connection IDs
    hints JSONB, -- array of hint objects
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deprecated')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: gameplay.player_easter_egg_discoveries
-- Tracks which easter eggs each player has discovered
CREATE TABLE IF NOT EXISTS gameplay.player_easter_egg_discoveries
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    easter_egg_id VARCHAR(100) NOT NULL REFERENCES gameplay.easter_egg_definitions(id) ON DELETE CASCADE,
    discovered_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rewards_claimed BOOLEAN NOT NULL DEFAULT FALSE,
    discovery_context JSONB, -- optional context about how/where discovered
    UNIQUE(player_id, easter_egg_id)
);

-- Indexes for performance (optimized for MMOFPS queries)
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_category ON gameplay.easter_egg_definitions(category);
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_difficulty ON gameplay.easter_egg_definitions(difficulty);
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_status ON gameplay.easter_egg_definitions(status);

-- Partial index for active easter eggs (most common query)
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_active ON gameplay.easter_egg_definitions(category, difficulty)
    WHERE status = 'active';

-- GIN indexes for JSONB searches
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_location_gin ON gameplay.easter_egg_definitions USING GIN (location);
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_discovery_method_gin ON gameplay.easter_egg_definitions USING GIN (discovery_method);
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_rewards_gin ON gameplay.easter_egg_definitions USING GIN (rewards);
CREATE INDEX IF NOT EXISTS idx_easter_egg_definitions_lore_connections_gin ON gameplay.easter_egg_definitions USING GIN (lore_connections);

-- Player discovery indexes
CREATE INDEX IF NOT EXISTS idx_player_easter_egg_discoveries_player ON gameplay.player_easter_egg_discoveries(player_id);
CREATE INDEX IF NOT EXISTS idx_player_easter_egg_discoveries_egg ON gameplay.player_easter_egg_discoveries(easter_egg_id);
CREATE INDEX IF NOT EXISTS idx_player_easter_egg_discoveries_discovered_at ON gameplay.player_easter_egg_discoveries(discovered_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_easter_egg_discoveries_rewards_claimed ON gameplay.player_easter_egg_discoveries(rewards_claimed)
    WHERE rewards_claimed = FALSE;

-- Function to update updated_at timestamp for easter eggs
CREATE OR REPLACE FUNCTION update_easter_egg_definitions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at for easter eggs
CREATE TRIGGER trigger_easter_egg_definitions_updated_at
    BEFORE UPDATE ON gameplay.easter_egg_definitions
    FOR EACH ROW
    EXECUTE FUNCTION update_easter_egg_definitions_updated_at();

-- Comments for API documentation
COMMENT ON TABLE gameplay.easter_egg_definitions IS 'Cyberspace easter eggs definitions for discovery mechanics and rewards';
COMMENT ON TABLE gameplay.player_easter_egg_discoveries IS 'Player easter egg discoveries tracking for achievements and rewards';

COMMENT ON COLUMN gameplay.easter_egg_definitions.id IS 'Unique easter egg identifier';
COMMENT ON COLUMN gameplay.easter_egg_definitions.name IS 'Easter egg display name';
COMMENT ON COLUMN gameplay.easter_egg_definitions.category IS 'Category: technology, cultural, historical, humorous';
COMMENT ON COLUMN gameplay.easter_egg_definitions.difficulty IS 'Discovery difficulty: easy, medium, hard, legendary';
COMMENT ON COLUMN gameplay.easter_egg_definitions.location IS 'JSONB location data (network_type, specific_areas)';
COMMENT ON COLUMN gameplay.easter_egg_definitions.discovery_method IS 'JSONB discovery method (type, description, requirements)';
COMMENT ON COLUMN gameplay.easter_egg_definitions.rewards IS 'JSONB array of reward objects';
COMMENT ON COLUMN gameplay.easter_egg_definitions.lore_connections IS 'JSONB array of lore connection IDs';

COMMENT ON COLUMN gameplay.player_easter_egg_discoveries.player_id IS 'Player who discovered the easter egg';
COMMENT ON COLUMN gameplay.player_easter_egg_discoveries.easter_egg_id IS 'Easter egg that was discovered';
COMMENT ON COLUMN gameplay.player_easter_egg_discoveries.discovery_context IS 'Optional context about discovery circumstances';

-- BACKEND NOTE: Column order optimized for struct alignment (large → small types)
-- Expected memory per easter_egg_definitions row: ~512 bytes (JSONB heavy)
-- Expected memory per player_discovery row: ~128 bytes
-- Hot queries: Active easter eggs by category/difficulty, player discoveries
-- Cache strategy: Redis cache for active easter eggs, TTL 6h; player discoveries cached per session

COMMIT;
