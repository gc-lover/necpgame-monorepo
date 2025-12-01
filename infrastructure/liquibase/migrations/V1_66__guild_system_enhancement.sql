-- Issue: #140888360
-- Guild System Database Schema Enhancement
-- Дополнение схемы системы гильдий:
-- - Обновление guilds (добавление bank_balance, emblem_url, изменение типов)
-- - Обновление guild_members (добавление rank_id, last_active_at, изменение типов)
-- - Обновление guild_bank_transactions (обновление структуры)
-- - Создание guild_bank_items (предметы в банке)
-- - Создание guild_wars (гильдейские войны)
-- - Создание guild_territories (территории гильдий)
-- - Создание guild_perks (перки гильдий)
-- Примечание: Базовые таблицы уже созданы в V1_14, V1_29, V1_30

-- Создание ENUM типов для соответствия архитектуре
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'guild_status') THEN
        CREATE TYPE guild_status AS ENUM ('active', 'disbanded', 'suspended');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'guild_war_status') THEN
        CREATE TYPE guild_war_status AS ENUM ('declared', 'active', 'ended', 'cancelled');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'bank_transaction_type') THEN
        CREATE TYPE bank_transaction_type AS ENUM ('deposit', 'withdraw');
    END IF;
END $$;

-- Обновление guilds для добавления недостающих полей
DO $$ 
BEGIN
    -- Добавляем bank_balance, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guilds' 
                   AND column_name = 'bank_balance') THEN
        ALTER TABLE social.guilds ADD COLUMN bank_balance DECIMAL(20, 2) NOT NULL DEFAULT 0;
    END IF;
    
    -- Добавляем emblem_url, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guilds' 
                   AND column_name = 'emblem_url') THEN
        ALTER TABLE social.guilds ADD COLUMN emblem_url VARCHAR(500);
    END IF;
    
    -- Изменяем experience на BIGINT, если это INTEGER
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'guilds' 
               AND column_name = 'experience' 
               AND data_type = 'integer') THEN
        ALTER TABLE social.guilds ALTER COLUMN experience TYPE BIGINT USING experience::BIGINT;
    END IF;
    
    -- Изменяем max_members на 50 по умолчанию, если это 20
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'guilds' 
               AND column_name = 'max_members' 
               AND column_default = '20') THEN
        ALTER TABLE social.guilds ALTER COLUMN max_members SET DEFAULT 50;
    END IF;
    
    -- Обновляем CHECK constraint для status
    IF EXISTS (SELECT 1 FROM information_schema.table_constraints 
               WHERE constraint_schema = 'social' 
               AND table_name = 'guilds' 
               AND constraint_name LIKE '%status%') THEN
        -- Удаляем старый constraint, если есть
        ALTER TABLE social.guilds DROP CONSTRAINT IF EXISTS guilds_status_check;
    END IF;
    -- Добавляем новый CHECK constraint
    ALTER TABLE social.guilds ADD CONSTRAINT guilds_status_check 
        CHECK (status IN ('active', 'disbanded', 'suspended'));
END $$;

-- Обновление guild_members для добавления недостающих полей
DO $$ 
BEGIN
    -- Добавляем rank_id, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_members' 
                   AND column_name = 'rank_id') THEN
        ALTER TABLE social.guild_members ADD COLUMN rank_id UUID REFERENCES social.guild_ranks(id) ON DELETE SET NULL;
    END IF;
    
    -- Добавляем last_active_at, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_members' 
                   AND column_name = 'last_active_at') THEN
        ALTER TABLE social.guild_members ADD COLUMN last_active_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
    END IF;
    
    -- Изменяем contribution на BIGINT, если это INTEGER
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'guild_members' 
               AND column_name = 'contribution' 
               AND data_type = 'integer') THEN
        ALTER TABLE social.guild_members ALTER COLUMN contribution TYPE BIGINT USING contribution::BIGINT;
    END IF;
END $$;

-- Обновление guild_bank_transactions для соответствия архитектуре
DO $$ 
BEGIN
    -- Добавляем amount, если его нет (вместо currency)
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_bank_transactions' 
                   AND column_name = 'amount') THEN
        ALTER TABLE social.guild_bank_transactions ADD COLUMN amount DECIMAL(20, 2);
    END IF;
    
    -- Добавляем currency_type, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_bank_transactions' 
                   AND column_name = 'currency_type') THEN
        ALTER TABLE social.guild_bank_transactions ADD COLUMN currency_type VARCHAR(50);
    END IF;
    
    -- Добавляем description, если его нет
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_bank_transactions' 
                   AND column_name = 'description') THEN
        ALTER TABLE social.guild_bank_transactions ADD COLUMN description TEXT;
    END IF;
    
    -- Обновляем performed_by (может быть account_id или character_id)
    IF EXISTS (SELECT 1 FROM information_schema.columns 
               WHERE table_schema = 'social' 
               AND table_name = 'guild_bank_transactions' 
               AND column_name = 'account_id') THEN
        ALTER TABLE social.guild_bank_transactions RENAME COLUMN account_id TO performed_by;
    END IF;
    
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                   WHERE table_schema = 'social' 
                   AND table_name = 'guild_bank_transactions' 
                   AND column_name = 'performed_by') THEN
        ALTER TABLE social.guild_bank_transactions ADD COLUMN performed_by UUID REFERENCES mvp_core.character(id) ON DELETE CASCADE;
    END IF;
END $$;

-- Таблица предметов в банке гильдии
CREATE TABLE IF NOT EXISTS social.guild_bank_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    item_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    deposited_by UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    deposited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для guild_bank_items
CREATE INDEX IF NOT EXISTS idx_guild_bank_items_guild_id ON social.guild_bank_items(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_bank_items_item_id ON social.guild_bank_items(item_id);

-- Таблица гильдейских войн
CREATE TABLE IF NOT EXISTS social.guild_wars (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    attacker_guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    defender_guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'declared' CHECK (status IN ('declared', 'active', 'ended', 'cancelled')),
    declared_by UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    winner_guild_id UUID REFERENCES social.guilds(id) ON DELETE SET NULL,
    CHECK (attacker_guild_id != defender_guild_id)
);

-- Индексы для guild_wars
CREATE INDEX IF NOT EXISTS idx_guild_wars_attacker_status ON social.guild_wars(attacker_guild_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_wars_defender_status ON social.guild_wars(defender_guild_id, status);
CREATE INDEX IF NOT EXISTS idx_guild_wars_status ON social.guild_wars(status);

-- Таблица территорий гильдий
CREATE TABLE IF NOT EXISTS social.guild_territories (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    territory_id UUID NOT NULL,
    captured_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tax_rate DECIMAL(5, 2) NOT NULL DEFAULT 0 CHECK (tax_rate >= 0 AND tax_rate <= 100),
    resources JSONB DEFAULT '{}'::jsonb
);

-- Индексы для guild_territories
CREATE INDEX IF NOT EXISTS idx_guild_territories_guild_id ON social.guild_territories(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_territories_territory_id ON social.guild_territories(territory_id);

-- Таблица перков гильдий
CREATE TABLE IF NOT EXISTS social.guild_perks (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES social.guilds(id) ON DELETE CASCADE,
    perk_id UUID NOT NULL,
    unlocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(guild_id, perk_id)
);

-- Индексы для guild_perks
CREATE INDEX IF NOT EXISTS idx_guild_perks_guild_id ON social.guild_perks(guild_id);
CREATE INDEX IF NOT EXISTS idx_guild_perks_perk_id ON social.guild_perks(perk_id);

-- Обновление индексов для guild_members
CREATE INDEX IF NOT EXISTS idx_guild_members_rank_id ON social.guild_members(guild_id, rank_id) WHERE rank_id IS NOT NULL;

-- Комментарии к таблицам
COMMENT ON TABLE social.guilds IS 'Гильдии игроков';
COMMENT ON TABLE social.guild_members IS 'Участники гильдий';
COMMENT ON TABLE social.guild_ranks IS 'Ранги в гильдиях';
COMMENT ON TABLE social.guild_bank_items IS 'Предметы в банке гильдии';
COMMENT ON TABLE social.guild_bank_transactions IS 'Транзакции банка гильдии';
COMMENT ON TABLE social.guild_wars IS 'Гильдейские войны';
COMMENT ON TABLE social.guild_territories IS 'Территории гильдий';
COMMENT ON TABLE social.guild_perks IS 'Перки гильдий';

-- Комментарии к колонкам
COMMENT ON COLUMN social.guilds.bank_balance IS 'Баланс банка гильдии';
COMMENT ON COLUMN social.guilds.emblem_url IS 'URL эмблемы гильдии';
COMMENT ON COLUMN social.guilds.experience IS 'Опыт гильдии (BIGINT)';
COMMENT ON COLUMN social.guilds.max_members IS 'Максимальное количество участников (default: 50)';
COMMENT ON COLUMN social.guilds.status IS 'Статус гильдии: active, disbanded, suspended';
COMMENT ON COLUMN social.guild_members.rank_id IS 'ID ранга в гильдии (FK к guild_ranks)';
COMMENT ON COLUMN social.guild_members.last_active_at IS 'Время последней активности участника';
COMMENT ON COLUMN social.guild_members.contribution IS 'Вклад участника (BIGINT)';
COMMENT ON COLUMN social.guild_bank_transactions.amount IS 'Сумма транзакции (DECIMAL)';
COMMENT ON COLUMN social.guild_bank_transactions.currency_type IS 'Тип валюты';
COMMENT ON COLUMN social.guild_bank_transactions.description IS 'Описание транзакции';
COMMENT ON COLUMN social.guild_bank_transactions.performed_by IS 'ID игрока, выполнившего транзакцию';
COMMENT ON COLUMN social.guild_wars.status IS 'Статус войны: declared, active, ended, cancelled';
COMMENT ON COLUMN social.guild_wars.winner_guild_id IS 'ID победившей гильдии (nullable)';
COMMENT ON COLUMN social.guild_territories.tax_rate IS 'Налоговая ставка с территории (0-100%)';
COMMENT ON COLUMN social.guild_territories.resources IS 'Ресурсы территории (JSONB)';


