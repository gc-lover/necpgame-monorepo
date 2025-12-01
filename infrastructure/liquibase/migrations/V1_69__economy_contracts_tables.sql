-- Issue: #140890166
-- Economy Contracts and Deals System Database Schema
-- Создание таблиц для системы контрактов и сделок:
-- - player_contracts (контракты игроков)
-- - contract_negotiations (переговоры по контрактам)
-- - escrows (эскроу для контрактов)
-- - collaterals (залоги для контрактов)
-- - contract_disputes (споры по контрактам)
-- - contract_execution_log (лог исполнения контрактов)

-- Создание схемы economy, если её нет
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'contract_type') THEN
        CREATE TYPE contract_type AS ENUM ('item_exchange', 'delivery', 'crafting', 'service');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'contract_status') THEN
        CREATE TYPE contract_status AS ENUM ('DRAFT', 'NEGOTIATION', 'ESCROW_PENDING', 'ACTIVE', 'COMPLETED', 'CANCELLED', 'DISPUTED');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'negotiation_status') THEN
        CREATE TYPE negotiation_status AS ENUM ('pending', 'accepted', 'rejected');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'escrow_status') THEN
        CREATE TYPE escrow_status AS ENUM ('locked', 'released', 'distributed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'collateral_status') THEN
        CREATE TYPE collateral_status AS ENUM ('locked', 'released', 'forfeited');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'dispute_decision') THEN
        CREATE TYPE dispute_decision AS ENUM ('pending', 'initiator_wins', 'counterparty_wins', 'partial');
    END IF;
END $$;

-- Таблица контрактов игроков
CREATE TABLE IF NOT EXISTS economy.player_contracts (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_type contract_type NOT NULL,
    initiator_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    counterparty_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    status contract_status NOT NULL DEFAULT 'DRAFT',
    terms JSONB NOT NULL DEFAULT '{}'::jsonb,
    initiator_assets JSONB NOT NULL DEFAULT '{}'::jsonb,
    counterparty_assets JSONB NOT NULL DEFAULT '{}'::jsonb,
    escrow_id UUID,
    initiator_collateral_id UUID,
    counterparty_collateral_id UUID,
    deadline TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    cancelled_at TIMESTAMP,
    dispute_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CHECK (initiator_id != counterparty_id)
);

-- Индексы для player_contracts
CREATE INDEX IF NOT EXISTS idx_player_contracts_initiator_status 
    ON economy.player_contracts(initiator_id, status);
CREATE INDEX IF NOT EXISTS idx_player_contracts_counterparty_status 
    ON economy.player_contracts(counterparty_id, status);
CREATE INDEX IF NOT EXISTS idx_player_contracts_type_status 
    ON economy.player_contracts(contract_type, status);
CREATE INDEX IF NOT EXISTS idx_player_contracts_status_deadline 
    ON economy.player_contracts(status, deadline) WHERE status IN ('ACTIVE', 'ESCROW_PENDING');

-- Таблица переговоров по контрактам
CREATE TABLE IF NOT EXISTS economy.contract_negotiations (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES economy.player_contracts(id) ON DELETE CASCADE,
    proposal JSONB NOT NULL DEFAULT '{}'::jsonb,
    proposer_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    status negotiation_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP
);

-- Индексы для contract_negotiations
CREATE INDEX IF NOT EXISTS idx_contract_negotiations_contract_status 
    ON economy.contract_negotiations(contract_id, status);
CREATE INDEX IF NOT EXISTS idx_contract_negotiations_proposer 
    ON economy.contract_negotiations(proposer_id);

-- Таблица эскроу для контрактов
CREATE TABLE IF NOT EXISTS economy.escrows (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL UNIQUE REFERENCES economy.player_contracts(id) ON DELETE CASCADE,
    initiator_items JSONB NOT NULL DEFAULT '[]'::jsonb,
    counterparty_items JSONB NOT NULL DEFAULT '[]'::jsonb,
    initiator_currency DECIMAL(20,2) NOT NULL DEFAULT 0.00,
    counterparty_currency DECIMAL(20,2) NOT NULL DEFAULT 0.00,
    status escrow_status NOT NULL DEFAULT 'locked',
    locked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at TIMESTAMP
);

-- Индексы для escrows
CREATE INDEX IF NOT EXISTS idx_escrows_contract_id 
    ON economy.escrows(contract_id);
CREATE INDEX IF NOT EXISTS idx_escrows_status 
    ON economy.escrows(status);

-- Обновление player_contracts для добавления FK к escrows
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_schema = 'economy' 
                   AND table_name = 'player_contracts' 
                   AND constraint_name = 'fk_player_contracts_escrow_id') THEN
        ALTER TABLE economy.player_contracts 
        ADD CONSTRAINT fk_player_contracts_escrow_id 
        FOREIGN KEY (escrow_id) REFERENCES economy.escrows(id) ON DELETE SET NULL;
    END IF;
END $$;

-- Таблица залогов для контрактов
CREATE TABLE IF NOT EXISTS economy.collaterals (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES economy.player_contracts(id) ON DELETE CASCADE,
    player_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    amount DECIMAL(20,2) NOT NULL DEFAULT 0.00,
    forfeited_amount DECIMAL(20,2) NOT NULL DEFAULT 0.00,
    status collateral_status NOT NULL DEFAULT 'locked',
    locked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    released_at TIMESTAMP
);

-- Индексы для collaterals
CREATE INDEX IF NOT EXISTS idx_collaterals_contract_id 
    ON economy.collaterals(contract_id);
CREATE INDEX IF NOT EXISTS idx_collaterals_player_status 
    ON economy.collaterals(player_id, status);

-- Обновление player_contracts для добавления FK к collaterals
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_schema = 'economy' 
                   AND table_name = 'player_contracts' 
                   AND constraint_name = 'fk_player_contracts_initiator_collateral_id') THEN
        ALTER TABLE economy.player_contracts 
        ADD CONSTRAINT fk_player_contracts_initiator_collateral_id 
        FOREIGN KEY (initiator_collateral_id) REFERENCES economy.collaterals(id) ON DELETE SET NULL;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_schema = 'economy' 
                   AND table_name = 'player_contracts' 
                   AND constraint_name = 'fk_player_contracts_counterparty_collateral_id') THEN
        ALTER TABLE economy.player_contracts 
        ADD CONSTRAINT fk_player_contracts_counterparty_collateral_id 
        FOREIGN KEY (counterparty_collateral_id) REFERENCES economy.collaterals(id) ON DELETE SET NULL;
    END IF;
END $$;

-- Таблица споров по контрактам
CREATE TABLE IF NOT EXISTS economy.contract_disputes (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL UNIQUE REFERENCES economy.player_contracts(id) ON DELETE CASCADE,
    initiator_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    reason TEXT NOT NULL,
    evidence JSONB NOT NULL DEFAULT '{}'::jsonb,
    ai_moderation_result JSONB,
    decision dispute_decision NOT NULL DEFAULT 'pending',
    escrow_distribution JSONB,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для contract_disputes
CREATE INDEX IF NOT EXISTS idx_contract_disputes_contract_id 
    ON economy.contract_disputes(contract_id);
CREATE INDEX IF NOT EXISTS idx_contract_disputes_initiator 
    ON economy.contract_disputes(initiator_id);
CREATE INDEX IF NOT EXISTS idx_contract_disputes_decision_resolved 
    ON economy.contract_disputes(decision, resolved_at) WHERE decision != 'pending';

-- Обновление player_contracts для добавления FK к contract_disputes
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints 
                   WHERE constraint_schema = 'economy' 
                   AND table_name = 'player_contracts' 
                   AND constraint_name = 'fk_player_contracts_dispute_id') THEN
        ALTER TABLE economy.player_contracts 
        ADD CONSTRAINT fk_player_contracts_dispute_id 
        FOREIGN KEY (dispute_id) REFERENCES economy.contract_disputes(id) ON DELETE SET NULL;
    END IF;
END $$;

-- Таблица лога исполнения контрактов
CREATE TABLE IF NOT EXISTS economy.contract_execution_log (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES economy.player_contracts(id) ON DELETE CASCADE,
    action VARCHAR(255) NOT NULL,
    details JSONB NOT NULL DEFAULT '{}'::jsonb,
    executed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для contract_execution_log
CREATE INDEX IF NOT EXISTS idx_contract_execution_log_contract_executed 
    ON economy.contract_execution_log(contract_id, executed_at DESC);
CREATE INDEX IF NOT EXISTS idx_contract_execution_log_executed_at 
    ON economy.contract_execution_log(executed_at DESC);

-- Комментарии к таблицам
COMMENT ON TABLE economy.player_contracts IS 'Контракты игроков (обмен, доставка, крафт, сервисы)';
COMMENT ON TABLE economy.contract_negotiations IS 'Переговоры по контрактам (предложения, принятие/отклонение)';
COMMENT ON TABLE economy.escrows IS 'Эскроу для контрактов (блокировка активов)';
COMMENT ON TABLE economy.collaterals IS 'Залоги для контрактов (гарантии выполнения)';
COMMENT ON TABLE economy.contract_disputes IS 'Споры по контрактам (арбитраж, AI-модерация)';
COMMENT ON TABLE economy.contract_execution_log IS 'Лог исполнения контрактов (история действий)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.player_contracts.contract_type IS 'Тип контракта: item_exchange, delivery, crafting, service';
COMMENT ON COLUMN economy.player_contracts.status IS 'Статус контракта: DRAFT, NEGOTIATION, ESCROW_PENDING, ACTIVE, COMPLETED, CANCELLED, DISPUTED';
COMMENT ON COLUMN economy.player_contracts.terms IS 'Условия контракта в JSONB';
COMMENT ON COLUMN economy.player_contracts.initiator_assets IS 'Активы инициатора (предметы, валюта) в JSONB';
COMMENT ON COLUMN economy.player_contracts.counterparty_assets IS 'Активы контрагента (предметы, валюта) в JSONB';
COMMENT ON COLUMN economy.player_contracts.escrow_id IS 'ID эскроу для контракта (nullable)';
COMMENT ON COLUMN economy.player_contracts.initiator_collateral_id IS 'ID залога инициатора (nullable)';
COMMENT ON COLUMN economy.player_contracts.counterparty_collateral_id IS 'ID залога контрагента (nullable)';
COMMENT ON COLUMN economy.player_contracts.deadline IS 'Срок выполнения контракта';
COMMENT ON COLUMN economy.player_contracts.dispute_id IS 'ID спора по контракту (nullable)';
COMMENT ON COLUMN economy.contract_negotiations.proposal IS 'Предложение по контракту в JSONB';
COMMENT ON COLUMN economy.contract_negotiations.status IS 'Статус предложения: pending, accepted, rejected';
COMMENT ON COLUMN economy.escrows.initiator_items IS 'Предметы инициатора в эскроу (JSONB массив)';
COMMENT ON COLUMN economy.escrows.counterparty_items IS 'Предметы контрагента в эскроу (JSONB массив)';
COMMENT ON COLUMN economy.escrows.status IS 'Статус эскроу: locked, released, distributed';
COMMENT ON COLUMN economy.collaterals.amount IS 'Сумма залога';
COMMENT ON COLUMN economy.collaterals.forfeited_amount IS 'Сумма удержанного залога';
COMMENT ON COLUMN economy.collaterals.status IS 'Статус залога: locked, released, forfeited';
COMMENT ON COLUMN economy.contract_disputes.reason IS 'Причина спора';
COMMENT ON COLUMN economy.contract_disputes.evidence IS 'Доказательства спора в JSONB';
COMMENT ON COLUMN economy.contract_disputes.ai_moderation_result IS 'Результат AI-модерации спора в JSONB (nullable)';
COMMENT ON COLUMN economy.contract_disputes.decision IS 'Решение по спору: pending, initiator_wins, counterparty_wins, partial';
COMMENT ON COLUMN economy.contract_disputes.escrow_distribution IS 'Распределение эскроу согласно решению в JSONB (nullable)';
COMMENT ON COLUMN economy.contract_execution_log.action IS 'Действие в логе (например, "item_exchanged", "currency_transferred")';
COMMENT ON COLUMN economy.contract_execution_log.details IS 'Детали действия в JSONB';


