--liquibase formatted sql

--changeset necpgame:V1_29_guild_ranks_tables
--comment: Create table for guild ranks management

CREATE TABLE IF NOT EXISTS social.guild_ranks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    name VARCHAR(50) NOT NULL,
    permissions JSONB NOT NULL DEFAULT '[]',
    "order" INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_guild_rank_name UNIQUE (guild_id, name)
);

CREATE INDEX IF NOT EXISTS idx_guild_ranks_guild_id ON social.guild_ranks(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_ranks_order ON social.guild_ranks(guild_id, "order");




