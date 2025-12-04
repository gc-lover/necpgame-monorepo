--liquibase formatted sql

--changeset necpgame:V1_38_engram_core_tables
--comment: Create tables for engram chips system - core functionality (slots, influence)

CREATE SCHEMA IF NOT EXISTS character;

CREATE TABLE IF NOT EXISTS character.engram_slots (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  engram_id UUID,
  installed_at TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  influence_level DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (influence_level >= 0 AND influence_level <= 100),
  slot_id INTEGER NOT NULL CHECK (slot_id >= 1 AND slot_id <= 3),
  usage_points INTEGER NOT NULL DEFAULT 0 CHECK (usage_points >= 0),
  is_active BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  CONSTRAINT uq_character_slot UNIQUE (character_id, slot_id),
  CONSTRAINT uq_engram_installed UNIQUE (engram_id) WHERE engram_id IS NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_engram_slots_character_id ON character.engram_slots(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_slots_slot_id ON character.engram_slots(character_id, slot_id);
CREATE INDEX IF NOT EXISTS idx_engram_slots_engram_id ON character.engram_slots(engram_id) WHERE engram_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_engram_slots_is_active ON character.engram_slots(character_id, is_active) WHERE is_active = true;

CREATE TABLE IF NOT EXISTS character.engram_influence_history (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  character_id UUID NOT NULL,
  engram_id UUID NOT NULL,
  change_reason VARCHAR(50) NOT NULL CHECK (change_reason IN ('skill_usage', 'decision_alignment', 'time_pass', 'blocker_effect', 'manual_adjust', 'conflict', 'removal')),
  action_data JSONB,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  influence_level_before DECIMAL(5,2) NOT NULL CHECK (influence_level_before >= 0 AND influence_level_before <= 100),
  influence_level_after DECIMAL(5,2) NOT NULL CHECK (influence_level_after >= 0 AND influence_level_after <= 100),
  change_amount DECIMAL(5,2) NOT NULL,
  slot_id INTEGER NOT NULL CHECK (slot_id >= 1 AND slot_id <= 3),
  CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_influence_history_character_id ON character.engram_influence_history(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_influence_history_engram_id ON character.engram_influence_history(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_influence_history_created_at ON character.engram_influence_history(created_at DESC);

CREATE OR REPLACE FUNCTION update_engram_slot_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_slot_updated_at
    BEFORE UPDATE ON character.engram_slots
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_slot_updated_at();

--rollback DROP TRIGGER IF EXISTS engram_slot_updated_at ON character.engram_slots;
--rollback DROP FUNCTION IF EXISTS update_engram_slot_updated_at();
--rollback DROP TABLE IF EXISTS character.engram_influence_history;
--rollback DROP TABLE IF EXISTS character.engram_slots;

