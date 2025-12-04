--liquibase formatted sql

--changeset necpgame:V1_30_guild_bank_transactions_tables
--comment: Create table for guild bank transactions

CREATE TABLE IF NOT EXISTS social.guild_bank_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    account_id UUID NOT NULL REFERENCES character.characters(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('deposit', 'withdraw')),
    currency INTEGER NOT NULL DEFAULT 0,
    items JSONB NOT NULL DEFAULT '[]',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_guild_bank_transactions_guild_id ON social.guild_bank_transactions(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_bank_transactions_account_id ON social.guild_bank_transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_guild_bank_transactions_created_at ON social.guild_bank_transactions(created_at DESC);

























