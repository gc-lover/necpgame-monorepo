-- Migration: Housing System Tables
-- Description: Creates tables for apartments, furniture items, placed furniture, and visits

CREATE SCHEMA IF NOT EXISTS housing;

CREATE TABLE IF NOT EXISTS housing.apartments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id UUID NOT NULL,
  owner_type VARCHAR(50) NOT NULL DEFAULT 'character',
  apartment_type VARCHAR(50) NOT NULL,
  location VARCHAR(255) NOT NULL,
  guests JSONB DEFAULT '[]'::jsonb,
  settings JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  price BIGINT NOT NULL,
  furniture_slots INTEGER NOT NULL DEFAULT 20,
  prestige_score INTEGER NOT NULL DEFAULT 0,
  is_public BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT chk_apartment_type CHECK (apartment_type IN ('studio', 'standard', 'penthouse', 'guild_hall')),
  CONSTRAINT chk_owner_type CHECK (owner_type IN ('character', 'guild')),
  CONSTRAINT chk_furniture_slots CHECK (furniture_slots >= 0)
);

CREATE TABLE IF NOT EXISTS housing.furniture_items (
  id VARCHAR(255) PRIMARY KEY,
  description TEXT,
  category VARCHAR(50) NOT NULL,
  name VARCHAR(255) NOT NULL,
  function_bonus JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  price BIGINT NOT NULL DEFAULT 0,
  prestige_value INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_furniture_category CHECK (category IN ('decorative', 'functional', 'comfort', 'storage'))
);

CREATE TABLE IF NOT EXISTS housing.placed_furniture (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID NOT NULL REFERENCES housing.apartments(id) ON DELETE CASCADE,
    furniture_item_id VARCHAR(255) NOT NULL REFERENCES housing.furniture_items(id),
    position JSONB NOT NULL DEFAULT '{}'::jsonb,
    rotation JSONB NOT NULL DEFAULT '{}'::jsonb,
    scale JSONB NOT NULL DEFAULT '{}'::jsonb,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS housing.apartment_visits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    apartment_id UUID NOT NULL REFERENCES housing.apartments(id) ON DELETE CASCADE,
    visitor_id UUID NOT NULL,
    visited_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_apartments_owner ON housing.apartments(owner_id, owner_type);
CREATE INDEX IF NOT EXISTS idx_apartments_type ON housing.apartments(apartment_type);
CREATE INDEX IF NOT EXISTS idx_apartments_public ON housing.apartments(is_public);
CREATE INDEX IF NOT EXISTS idx_apartments_prestige ON housing.apartments(prestige_score DESC);
CREATE INDEX IF NOT EXISTS idx_apartments_location ON housing.apartments(location);

CREATE INDEX IF NOT EXISTS idx_furniture_items_category ON housing.furniture_items(category);
CREATE INDEX IF NOT EXISTS idx_furniture_items_name ON housing.furniture_items(name);

CREATE INDEX IF NOT EXISTS idx_placed_furniture_apartment ON housing.placed_furniture(apartment_id);
CREATE INDEX IF NOT EXISTS idx_placed_furniture_item ON housing.placed_furniture(furniture_item_id);

CREATE INDEX IF NOT EXISTS idx_apartment_visits_apartment ON housing.apartment_visits(apartment_id);
CREATE INDEX IF NOT EXISTS idx_apartment_visits_visitor ON housing.apartment_visits(visitor_id);
CREATE INDEX IF NOT EXISTS idx_apartment_visits_visited_at ON housing.apartment_visits(visited_at DESC);

COMMENT ON TABLE housing.apartments IS 'Player and guild apartments with customization options';
COMMENT ON TABLE housing.furniture_items IS 'Catalog of furniture items available for purchase';
COMMENT ON TABLE housing.placed_furniture IS 'Furniture items placed in apartments with 3D transforms';
COMMENT ON TABLE housing.apartment_visits IS 'Records of visits to apartments';

COMMENT ON COLUMN housing.apartments.owner_type IS 'Type of owner: character or guild';
COMMENT ON COLUMN housing.apartments.guests IS 'Array of character IDs with guest access';
COMMENT ON COLUMN housing.apartments.settings IS 'JSON object with apartment settings';
COMMENT ON COLUMN housing.furniture_items.function_bonus IS 'JSON object with functional bonuses (craft_speed, storage_slots, etc.)';
COMMENT ON COLUMN housing.placed_furniture.position IS 'JSON object with 3D position {x, y, z}';
COMMENT ON COLUMN housing.placed_furniture.rotation IS 'JSON object with 3D rotation {x, y, z}';
COMMENT ON COLUMN housing.placed_furniture.scale IS 'JSON object with 3D scale {x, y, z}';

