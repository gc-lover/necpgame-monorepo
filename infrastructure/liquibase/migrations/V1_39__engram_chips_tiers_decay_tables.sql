--liquibase formatted sql

--changeset necpgame:V1_39_engram_chips_tiers_decay_tables
--comment: Create tables for engram chips tiers and decay tracking

CREATE SCHEMA IF NOT EXISTS inventory;

CREATE TABLE IF NOT EXISTS inventory.engram_chip_tiers (
  tier INTEGER PRIMARY KEY CHECK (tier >= 1 AND tier <= 5),
  tier_name VARCHAR(50) NOT NULL UNIQUE CHECK (tier_name IN ('prototype', 'standard', 'advanced', 'corporate', 'legendary')),
  stability_level VARCHAR(20) NOT NULL CHECK (stability_level IN ('low', 'medium', 'high', 'very_high', 'maximum')),
  corruption_risk VARCHAR(20) NOT NULL CHECK (corruption_risk IN ('high', 'medium', 'low', 'very_low', 'minimal')),
  protection_level VARCHAR(20) NOT NULL CHECK (protection_level IN ('limited', 'standard', 'advanced', 'corporate', 'military')),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  corruption_risk_percent DECIMAL(5,2) NOT NULL CHECK (corruption_risk_percent >= 0 AND corruption_risk_percent <= 100),
  creation_cost_min DECIMAL(12,2) NOT NULL CHECK (creation_cost_min >= 0),
  creation_cost_max DECIMAL(12,2) NOT NULL CHECK (creation_cost_max >= creation_cost_min),
  lifespan_years_min INTEGER NOT NULL CHECK (lifespan_years_min > 0),
  lifespan_years_max INTEGER NOT NULL CHECK (lifespan_years_max >= lifespan_years_min),
  available_from_year INTEGER NOT NULL CHECK (available_from_year >= 2020 AND available_from_year <= 2093)
);

INSERT INTO inventory.engram_chip_tiers (tier, tier_name, stability_level, lifespan_years_min, lifespan_years_max, corruption_risk, corruption_risk_percent, protection_level, creation_cost_min, creation_cost_max, available_from_year) VALUES
(1, 'prototype', 'low', 1, 2, 'high', 40.0, 'limited', 150000.0, 250000.0, 2070),
(2, 'standard', 'medium', 3, 5, 'medium', 25.0, 'standard', 300000.0, 500000.0, 2075),
(3, 'advanced', 'high', 10, 15, 'low', 15.0, 'advanced', 750000.0, 1200000.0, 2080),
(4, 'corporate', 'very_high', 20, 30, 'very_low', 8.0, 'corporate', 1500000.0, 2500000.0, 2085),
(5, 'legendary', 'maximum', 50, 100, 'minimal', 2.0, 'military', 3000000.0, 5000000.0, 2085)
ON CONFLICT (tier) DO NOTHING;

CREATE TABLE IF NOT EXISTS inventory.engram_chip_decay (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  chip_id UUID NOT NULL,
  decay_risk VARCHAR(20) NOT NULL DEFAULT 'none' CHECK (decay_risk IN ('none', 'low', 'medium', 'high', 'critical')),
  storage_temperature VARCHAR(20) NOT NULL DEFAULT 'optimal' CHECK (storage_temperature IN ('optimal', 'acceptable', 'poor', 'critical')),
  storage_humidity VARCHAR(20) NOT NULL DEFAULT 'optimal' CHECK (storage_humidity IN ('optimal', 'acceptable', 'poor', 'critical')),
  decay_effects JSONB DEFAULT '[]'::jsonb,
  last_checked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  decay_percent DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (decay_percent >= 0 AND decay_percent <= 100),
  tier INTEGER NOT NULL REFERENCES inventory.engram_chip_tiers(tier),
  storage_time_outside_hours INTEGER NOT NULL DEFAULT 0 CHECK (storage_time_outside_hours >= 0),
  time_until_critical_hours INTEGER,
  electromagnetic_shield BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT uq_chip_decay UNIQUE (chip_id)
);

CREATE INDEX IF NOT EXISTS idx_engram_chip_decay_chip_id ON inventory.engram_chip_decay(chip_id);
CREATE INDEX IF NOT EXISTS idx_engram_chip_decay_tier ON inventory.engram_chip_decay(tier);
CREATE INDEX IF NOT EXISTS idx_engram_chip_decay_decay_risk ON inventory.engram_chip_decay(decay_risk);
CREATE INDEX IF NOT EXISTS idx_engram_chip_tiers_available_from_year ON inventory.engram_chip_tiers(available_from_year);

CREATE OR REPLACE FUNCTION update_engram_chip_decay_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_chip_decay_updated_at
    BEFORE UPDATE ON inventory.engram_chip_decay
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_chip_decay_updated_at();

CREATE OR REPLACE FUNCTION update_engram_chip_tiers_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_chip_tiers_updated_at
    BEFORE UPDATE ON inventory.engram_chip_tiers
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_chip_tiers_updated_at();

--rollback DROP TRIGGER IF EXISTS engram_chip_tiers_updated_at ON inventory.engram_chip_tiers;
--rollback DROP TRIGGER IF EXISTS engram_chip_decay_updated_at ON inventory.engram_chip_decay;
--rollback DROP FUNCTION IF EXISTS update_engram_chip_tiers_updated_at();
--rollback DROP FUNCTION IF EXISTS update_engram_chip_decay_updated_at();
--rollback DROP TABLE IF EXISTS inventory.engram_chip_decay;
--rollback DROP TABLE IF EXISTS inventory.engram_chip_tiers;
--rollback DROP SCHEMA IF EXISTS inventory;

