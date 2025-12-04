--liquibase formatted sql

--changeset necpgame:V1_40_engram_creation_protection_tables
--comment: Create tables for engram creation and protection

CREATE SCHEMA IF NOT EXISTS economy;

CREATE TABLE IF NOT EXISTS economy.engram_creation_log (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  creation_id UUID NOT NULL UNIQUE,
  engram_id UUID NOT NULL,
  character_id UUID NOT NULL,
  target_person_id UUID,
  attitude_type VARCHAR(20) NOT NULL CHECK (attitude_type IN ('aggressive', 'neutral', 'friendly', 'subservient', 'custom')),
  creation_stage VARCHAR(20) NOT NULL DEFAULT 'preparation' CHECK (creation_stage IN ('preparation', 'scanning', 'creation', 'configuration', 'saving', 'completed', 'failed')),
  custom_attitude_settings JSONB,
  reputation_snapshot JSONB,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  completed_at TIMESTAMP WITH TIME ZONE,
  data_loss_percent DECIMAL(5,2) DEFAULT 0.0 CHECK (data_loss_percent >= 0 AND data_loss_percent <= 100),
  creation_cost DECIMAL(12,2) NOT NULL CHECK (creation_cost >= 0),
  chip_tier INTEGER NOT NULL CHECK (chip_tier >= 1 AND chip_tier <= 5),
  is_complete BOOLEAN NOT NULL DEFAULT true,
  CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_creation_log_character_id ON economy.engram_creation_log(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_creation_log_engram_id ON economy.engram_creation_log(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_creation_log_creation_id ON economy.engram_creation_log(creation_id);
CREATE INDEX IF NOT EXISTS idx_engram_creation_log_creation_stage ON economy.engram_creation_log(creation_stage);
CREATE INDEX IF NOT EXISTS idx_engram_creation_log_created_at ON economy.engram_creation_log(created_at DESC);

CREATE TABLE IF NOT EXISTS character.engram_protection (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  engram_id UUID NOT NULL UNIQUE,
  bound_character_id UUID,
  encoded_by UUID NOT NULL,
  protection_tier_name VARCHAR(20) NOT NULL CHECK (protection_tier_name IN ('basic', 'standard', 'advanced', 'corporate', 'military')),
  encoded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  protection_tier INTEGER NOT NULL CHECK (protection_tier >= 1 AND protection_tier <= 5),
  required_netrunner_level INTEGER NOT NULL DEFAULT 20 CHECK (required_netrunner_level >= 0 AND required_netrunner_level <= 100),
  copy_protection BOOLEAN NOT NULL DEFAULT false,
  hack_protection BOOLEAN NOT NULL DEFAULT false,
  install_protection BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT fk_engram FOREIGN KEY (engram_id) REFERENCES character.engrams(id) ON DELETE CASCADE,
  CONSTRAINT fk_encoded_by FOREIGN KEY (encoded_by) REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  CONSTRAINT fk_bound_character FOREIGN KEY (bound_character_id) REFERENCES mvp_core.character(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_engram_protection_engram_id ON character.engram_protection(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_protection_protection_tier ON character.engram_protection(protection_tier);
CREATE INDEX IF NOT EXISTS idx_engram_protection_encoded_by ON character.engram_protection(encoded_by);
CREATE INDEX IF NOT EXISTS idx_engram_protection_bound_character_id ON character.engram_protection(bound_character_id);

CREATE TABLE IF NOT EXISTS character.engrams (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  created_from_character_id UUID,
  name VARCHAR(255),
  attitude_type VARCHAR(20) NOT NULL CHECK (attitude_type IN ('aggressive', 'neutral', 'friendly', 'subservient', 'custom')),
  historical_person_name VARCHAR(255),
  custom_attitude_settings JSONB,
  reputation_snapshot JSONB,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  data_loss_percent DECIMAL(5,2) DEFAULT 0.0 CHECK (data_loss_percent >= 0 AND data_loss_percent <= 100),
  chip_tier INTEGER NOT NULL CHECK (chip_tier >= 1 AND chip_tier <= 5),
  is_historical BOOLEAN NOT NULL DEFAULT false,
  is_complete BOOLEAN NOT NULL DEFAULT true,
  CONSTRAINT fk_created_from_character FOREIGN KEY (created_from_character_id) REFERENCES mvp_core.character(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_engrams_created_from_character_id ON character.engrams(created_from_character_id);
CREATE INDEX IF NOT EXISTS idx_engrams_is_historical ON character.engrams(is_historical);
CREATE INDEX IF NOT EXISTS idx_engrams_chip_tier ON character.engrams(chip_tier);

CREATE OR REPLACE FUNCTION update_engram_creation_log_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_creation_log_updated_at
    BEFORE UPDATE ON economy.engram_creation_log
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_creation_log_updated_at();

CREATE OR REPLACE FUNCTION update_engram_protection_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_protection_updated_at
    BEFORE UPDATE ON character.engram_protection
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_protection_updated_at();

CREATE OR REPLACE FUNCTION update_engrams_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engrams_updated_at
    BEFORE UPDATE ON character.engrams
    FOR EACH ROW
    EXECUTE FUNCTION update_engrams_updated_at();

--rollback DROP TRIGGER IF EXISTS engrams_updated_at ON character.engrams;
--rollback DROP TRIGGER IF EXISTS engram_protection_updated_at ON character.engram_protection;
--rollback DROP TRIGGER IF EXISTS engram_creation_log_updated_at ON economy.engram_creation_log;
--rollback DROP FUNCTION IF EXISTS update_engrams_updated_at();
--rollback DROP FUNCTION IF EXISTS update_engram_protection_updated_at();
--rollback DROP FUNCTION IF EXISTS update_engram_creation_log_updated_at();
--rollback DROP TABLE IF EXISTS character.engrams;
--rollback DROP TABLE IF EXISTS character.engram_protection;
--rollback DROP TABLE IF EXISTS economy.engram_creation_log;

