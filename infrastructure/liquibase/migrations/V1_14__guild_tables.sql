CREATE SCHEMA IF NOT EXISTS social;

CREATE TABLE IF NOT EXISTS social.guilds (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  leader_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  description TEXT,
  name VARCHAR(100) NOT NULL UNIQUE,
  tag VARCHAR(10) NOT NULL UNIQUE,
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  level INT NOT NULL DEFAULT 1,
  experience INT NOT NULL DEFAULT 0,
  max_members INT NOT NULL DEFAULT 20
);

CREATE TABLE IF NOT EXISTS social.guild_members (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  rank VARCHAR(20) NOT NULL DEFAULT 'recruit',
  status VARCHAR(20) NOT NULL DEFAULT 'active',
  joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  contribution INT NOT NULL DEFAULT 0,
  UNIQUE(guild_id, character_id)
);

CREATE TABLE IF NOT EXISTS social.guild_invitations (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
  character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  invited_by UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
  message TEXT,
  status VARCHAR(20) NOT NULL DEFAULT 'pending',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  expires_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS social.guild_banks (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  guild_id UUID NOT NULL UNIQUE REFERENCES social.guilds(id) ON DELETE CASCADE,
  currency JSONB NOT NULL DEFAULT '{}',
  items JSONB NOT NULL DEFAULT '[]',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_guilds_name ON social.guilds(name);
CREATE INDEX IF NOT EXISTS idx_guilds_tag ON social.guilds(tag);
CREATE INDEX IF NOT EXISTS idx_guilds_leader ON social.guilds(leader_id);
CREATE INDEX IF NOT EXISTS idx_guilds_status ON social.guilds(status);

CREATE INDEX IF NOT EXISTS idx_guild_members_guild ON social.guild_members(guild_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_members_character ON social.guild_members(character_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_members_rank ON social.guild_members(guild_id, rank);

CREATE INDEX IF NOT EXISTS idx_guild_invitations_character ON social.guild_invitations(character_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_invitations_guild ON social.guild_invitations(guild_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_invitations_expires ON social.guild_invitations(expires_at) WHERE status = 'pending';

CREATE INDEX IF NOT EXISTS idx_guild_banks_guild ON social.guild_banks(guild_id);

