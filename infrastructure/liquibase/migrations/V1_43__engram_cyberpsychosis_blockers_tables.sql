--liquibase formatted sql

--changeset necpgame:V1_43_engram_cyberpsychosis_blockers_tables
--comment: Create tables for engram cyberpsychosis risk and blockers

CREATE TABLE IF NOT EXISTS character.engram_cyberpsychosis_risk (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id UUID NOT NULL UNIQUE,
    base_risk DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (base_risk >= 0 AND base_risk <= 100),
    engram_risk DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (engram_risk >= 0 AND engram_risk <= 100),
    total_risk DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (total_risk >= 0 AND total_risk <= 100),
    blocker_reduction DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (blocker_reduction >= 0 AND blocker_reduction <= 100),
    risk_factors JSONB,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_cyberpsychosis_risk_character_id ON character.engram_cyberpsychosis_risk(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_cyberpsychosis_risk_total_risk ON character.engram_cyberpsychosis_risk(total_risk);

CREATE TABLE IF NOT EXISTS character.engram_blockers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    blocker_id UUID NOT NULL UNIQUE,
    character_id UUID NOT NULL,
    tier INTEGER NOT NULL CHECK (tier >= 1 AND tier <= 5),
    risk_reduction DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (risk_reduction >= 0 AND risk_reduction <= 100),
    influence_reduction DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (influence_reduction >= 0 AND influence_reduction <= 100),
    duration_days INTEGER NOT NULL CHECK (duration_days >= 1),
    buffs JSONB,
    debuffs JSONB,
    installed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_blockers_character_id ON character.engram_blockers(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_blockers_is_active ON character.engram_blockers(character_id, is_active) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_engram_blockers_expires_at ON character.engram_blockers(expires_at);

CREATE OR REPLACE FUNCTION update_engram_blocker_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_blocker_updated_at
    BEFORE UPDATE ON character.engram_blockers
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_blocker_updated_at();

--rollback DROP TRIGGER IF EXISTS engram_blocker_updated_at ON character.engram_blockers;
--rollback DROP FUNCTION IF EXISTS update_engram_blocker_updated_at();
--rollback DROP TABLE IF EXISTS character.engram_blockers;
--rollback DROP TABLE IF EXISTS character.engram_cyberpsychosis_risk;

