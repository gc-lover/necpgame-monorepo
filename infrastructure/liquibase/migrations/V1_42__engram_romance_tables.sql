--liquibase formatted sql

--changeset necpgame:V1_42_engram_romance_tables
--comment: Create tables for engram romance integration

CREATE SCHEMA IF NOT EXISTS social;

CREATE TABLE IF NOT EXISTS social.engram_romance_comments (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  comment_id UUID NOT NULL UNIQUE,
  engram_id UUID NOT NULL,
  character_id UUID NOT NULL,
  partner_id UUID,
  comment_text TEXT NOT NULL,
  romance_event_type VARCHAR(20) NOT NULL CHECK (romance_event_type IN ('kiss', 'intimate', 'dialogue', 'conflict', 'breakup')),
  event_context JSONB,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  influence_level DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (influence_level >= 0 AND influence_level <= 100),
  CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_romance_comments_engram_id ON social.engram_romance_comments(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_romance_comments_character_id ON social.engram_romance_comments(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_romance_comments_romance_event_type ON social.engram_romance_comments(romance_event_type);
CREATE INDEX IF NOT EXISTS idx_engram_romance_comments_created_at ON social.engram_romance_comments(created_at DESC);

CREATE TABLE IF NOT EXISTS social.engram_romance_influence (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  engram_id UUID NOT NULL,
  character_id UUID NOT NULL,
  relationship_id UUID,
  special_events TEXT[],
  influence_category VARCHAR(20) NOT NULL CHECK (influence_category IN ('low', 'medium', 'high', 'critical')),
  engram_type VARCHAR(20) CHECK (engram_type IN ('friendly', 'aggressive', 'romantic', 'jealous')),
  last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  influence_level DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (influence_level >= 0 AND influence_level <= 100),
  impact_percentage DECIMAL(5,2) DEFAULT 0.0 CHECK (impact_percentage >= -100 AND impact_percentage <= 100),
  helps_relationship BOOLEAN NOT NULL DEFAULT false,
  interferes_relationship BOOLEAN NOT NULL DEFAULT false,
  CONSTRAINT fk_character FOREIGN KEY (character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  CONSTRAINT uq_engram_character_relationship UNIQUE (engram_id, character_id, relationship_id)
);

CREATE INDEX IF NOT EXISTS idx_engram_romance_influence_engram_id ON social.engram_romance_influence(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_romance_influence_character_id ON social.engram_romance_influence(character_id);
CREATE INDEX IF NOT EXISTS idx_engram_romance_influence_relationship_id ON social.engram_romance_influence(relationship_id);
CREATE INDEX IF NOT EXISTS idx_engram_romance_influence_category ON social.engram_romance_influence(influence_category);

--rollback DROP TABLE IF EXISTS social.engram_romance_influence;
--rollback DROP TABLE IF EXISTS social.engram_romance_comments;
--rollback DROP SCHEMA IF EXISTS social;



