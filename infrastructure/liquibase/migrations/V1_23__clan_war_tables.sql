-- Migration: Clan War System Tables
-- Description: Creates tables for clan wars, battles, and territories

CREATE SCHEMA IF NOT EXISTS pvp;

CREATE TABLE IF NOT EXISTS pvp.clan_wars (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  attacker_guild_id UUID NOT NULL,
  defender_guild_id UUID NOT NULL,
  territory_id UUID,
  winner_guild_id UUID,
  status VARCHAR(50) NOT NULL DEFAULT 'declared',
  phase VARCHAR(50) NOT NULL DEFAULT 'preparation',
  allies JSONB DEFAULT '[]'::jsonb,
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  end_time TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  attacker_score INTEGER NOT NULL DEFAULT 0,
  defender_score INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_status CHECK (status IN ('declared', 'ongoing', 'completed', 'cancelled')),
  CONSTRAINT chk_phase CHECK (phase IN ('preparation', 'active', 'completed', 'cancelled')),
  CONSTRAINT chk_different_guilds CHECK (attacker_guild_id != defender_guild_id)
);

CREATE TABLE IF NOT EXISTS pvp.war_battles (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  war_id UUID NOT NULL REFERENCES pvp.clan_wars(id) ON DELETE CASCADE,
  territory_id UUID,
  type VARCHAR(50) NOT NULL,
  status VARCHAR(50) NOT NULL DEFAULT 'scheduled',
  start_time TIMESTAMP WITH TIME ZONE NOT NULL,
  end_time TIMESTAMP WITH TIME ZONE,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  attacker_score INTEGER NOT NULL DEFAULT 0,
  defender_score INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT chk_battle_type CHECK (type IN ('territory', 'siege', 'open_world')),
  CONSTRAINT chk_battle_status CHECK (status IN ('scheduled', 'active', 'completed', 'cancelled'))
);

CREATE TABLE IF NOT EXISTS pvp.territories (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_guild_id UUID,
  name VARCHAR(255) NOT NULL,
  region VARCHAR(255) NOT NULL,
  resources JSONB DEFAULT '{}'::jsonb,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
  defense_level INTEGER NOT NULL DEFAULT 1,
  siege_difficulty INTEGER NOT NULL DEFAULT 1,
  CONSTRAINT chk_defense_level CHECK (defense_level >= 1 AND defense_level <= 10),
  CONSTRAINT chk_siege_difficulty CHECK (siege_difficulty >= 1 AND siege_difficulty <= 10)
);

CREATE INDEX IF NOT EXISTS idx_clan_wars_attacker ON pvp.clan_wars(attacker_guild_id);
CREATE INDEX IF NOT EXISTS idx_clan_wars_defender ON pvp.clan_wars(defender_guild_id);
CREATE INDEX IF NOT EXISTS idx_clan_wars_status ON pvp.clan_wars(status);
CREATE INDEX IF NOT EXISTS idx_clan_wars_phase ON pvp.clan_wars(phase);
CREATE INDEX IF NOT EXISTS idx_clan_wars_territory ON pvp.clan_wars(territory_id);
CREATE INDEX IF NOT EXISTS idx_clan_wars_start_time ON pvp.clan_wars(start_time);

CREATE INDEX IF NOT EXISTS idx_war_battles_war ON pvp.war_battles(war_id);
CREATE INDEX IF NOT EXISTS idx_war_battles_type ON pvp.war_battles(type);
CREATE INDEX IF NOT EXISTS idx_war_battles_status ON pvp.war_battles(status);
CREATE INDEX IF NOT EXISTS idx_war_battles_territory ON pvp.war_battles(territory_id);
CREATE INDEX IF NOT EXISTS idx_war_battles_start_time ON pvp.war_battles(start_time);

CREATE INDEX IF NOT EXISTS idx_territories_owner ON pvp.territories(owner_guild_id);
CREATE INDEX IF NOT EXISTS idx_territories_region ON pvp.territories(region);
CREATE INDEX IF NOT EXISTS idx_territories_name ON pvp.territories(name);

COMMENT ON TABLE pvp.clan_wars IS 'Clan wars between guilds with phases, scores, and territories';
COMMENT ON TABLE pvp.war_battles IS 'Individual battles within clan wars (territory, siege, open world)';
COMMENT ON TABLE pvp.territories IS 'Territories that can be contested in clan wars';

COMMENT ON COLUMN pvp.clan_wars.allies IS 'Array of guild IDs that are allies in this war';
COMMENT ON COLUMN pvp.clan_wars.status IS 'Current status: declared, ongoing, completed, cancelled';
COMMENT ON COLUMN pvp.clan_wars.phase IS 'Current phase: preparation (24h), active (7 days), completed, cancelled';
COMMENT ON COLUMN pvp.war_battles.type IS 'Type of battle: territory, siege, open_world';
COMMENT ON COLUMN pvp.territories.resources IS 'JSON object with resource types and amounts';

