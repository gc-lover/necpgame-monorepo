--liquibase formatted sql

--changeset necpgame:V1_32_party_tables
--comment: Create tables for party system

CREATE TABLE IF NOT EXISTS social.parties (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leader_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    max_size INTEGER NOT NULL DEFAULT 5 CHECK (max_size >= 2 AND max_size <= 5),
    loot_mode VARCHAR(50) NOT NULL DEFAULT 'free_for_all' CHECK (loot_mode IN ('free_for_all', 'round_robin', 'need_before_greed', 'master_looter')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_parties_leader_id ON social.parties(leader_id);
CREATE INDEX IF NOT EXISTS idx_parties_created_at ON social.parties(created_at DESC);

CREATE TABLE IF NOT EXISTS social.party_members (
    party_id UUID NOT NULL REFERENCES social.parties(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('leader', 'member')),
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (party_id, character_id)
);

CREATE INDEX IF NOT EXISTS idx_party_members_party_id ON social.party_members(party_id);
CREATE INDEX IF NOT EXISTS idx_party_members_character_id ON social.party_members(character_id);
CREATE INDEX IF NOT EXISTS idx_party_members_joined_at ON social.party_members(joined_at ASC);

