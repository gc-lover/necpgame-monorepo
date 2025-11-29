--liquibase formatted sql

--changeset necpgame:V1_41_engram_transfer_tables
--comment: Create tables for engram transfers between players

CREATE TABLE IF NOT EXISTS economy.engram_transfers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transfer_id UUID NOT NULL UNIQUE,
    engram_id UUID NOT NULL,
    from_character_id UUID NOT NULL,
    to_character_id UUID NOT NULL,
    transfer_type VARCHAR(20) NOT NULL CHECK (transfer_type IN ('voluntary', 'cooperative', 'forced', 'trade', 'loan', 'extract')),
    is_copy BOOLEAN NOT NULL DEFAULT false,
    new_attitude_type VARCHAR(20),
    transfer_price DECIMAL(12,2),
    status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (status IN ('pending', 'completed', 'failed', 'cancelled', 'returned')),
    loan_return_date TIMESTAMP WITH TIME ZONE,
    extraction_risk_percent DECIMAL(5,2),
    engram_damaged BOOLEAN NOT NULL DEFAULT false,
    damage_percent DECIMAL(5,2),
    target_character_died BOOLEAN NOT NULL DEFAULT false,
    new_engram_id UUID,
    transferred_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_from_character FOREIGN KEY (from_character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    CONSTRAINT fk_to_character FOREIGN KEY (to_character_id) REFERENCES mvp_core.character(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_engram_transfers_engram_id ON economy.engram_transfers(engram_id);
CREATE INDEX IF NOT EXISTS idx_engram_transfers_from_character_id ON economy.engram_transfers(from_character_id);
CREATE INDEX IF NOT EXISTS idx_engram_transfers_to_character_id ON economy.engram_transfers(to_character_id);
CREATE INDEX IF NOT EXISTS idx_engram_transfers_transfer_type ON economy.engram_transfers(transfer_type);
CREATE INDEX IF NOT EXISTS idx_engram_transfers_status ON economy.engram_transfers(status);
CREATE INDEX IF NOT EXISTS idx_engram_transfers_loan_return_date ON economy.engram_transfers(loan_return_date) WHERE loan_return_date IS NOT NULL;

CREATE OR REPLACE FUNCTION update_engram_transfer_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER engram_transfer_updated_at
    BEFORE UPDATE ON economy.engram_transfers
    FOR EACH ROW
    EXECUTE FUNCTION update_engram_transfer_updated_at();

--rollback DROP TRIGGER IF EXISTS engram_transfer_updated_at ON economy.engram_transfers;
--rollback DROP FUNCTION IF EXISTS update_engram_transfer_updated_at();
--rollback DROP TABLE IF EXISTS economy.engram_transfers;



