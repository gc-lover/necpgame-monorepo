-- Issue: #140876092
-- Loot Tables System Database Schema
-- Создание таблиц для системы Loot Tables:
-- - Таблицы лута (loot_tables)
-- - Записи в таблицах лута (loot_table_entries)
-- - Распределения лута (loot_distributions)
-- - Предметы в распределениях (loot_distribution_items)
-- - Roll сессии для групп (loot_rolls)
-- - Участники roll сессий (loot_roll_participants)
-- - Мировые дропы (world_drops)
-- - История лута игроков (player_loot_history)

-- Создание схемы economy, если её нет (уже создана в V1_15, но для безопасности)
CREATE SCHEMA IF NOT EXISTS economy;

-- Создание ENUM типов
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_source_type') THEN
        CREATE TYPE loot_source_type AS ENUM ('npc', 'container', 'quest', 'boss', 'world_event');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_distribution_mode') THEN
        CREATE TYPE loot_distribution_mode AS ENUM ('personal', 'party', 'shared', 'boss');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_distribution_status') THEN
        CREATE TYPE loot_distribution_status AS ENUM ('pending', 'distributed', 'expired');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_item_status') THEN
        CREATE TYPE loot_item_status AS ENUM ('pending', 'assigned', 'claimed');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_roll_status') THEN
        CREATE TYPE loot_roll_status AS ENUM ('active', 'completed', 'expired');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'loot_roll_choice') THEN
        CREATE TYPE loot_roll_choice AS ENUM ('need', 'greed', 'pass');
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'world_drop_status') THEN
        CREATE TYPE world_drop_status AS ENUM ('active', 'picked_up', 'expired');
    END IF;
END $$;

-- Таблица таблиц лута
CREATE TABLE IF NOT EXISTS economy.loot_tables (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    source_type loot_source_type NOT NULL,
    source_id UUID NOT NULL,
    min_items INTEGER NOT NULL DEFAULT 1 CHECK (min_items >= 0),
    max_items INTEGER NOT NULL DEFAULT 1 CHECK (max_items >= min_items),
    currency_min DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (currency_min >= 0),
    currency_max DECIMAL(20,2) NOT NULL DEFAULT 0.0 CHECK (currency_max >= currency_min),
    luck_modifier DECIMAL(5,2) NOT NULL DEFAULT 0.0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для loot_tables
CREATE INDEX IF NOT EXISTS idx_loot_tables_source ON economy.loot_tables(source_type, source_id);
CREATE INDEX IF NOT EXISTS idx_loot_tables_is_active ON economy.loot_tables(is_active) WHERE is_active = true;

-- Таблица записей в таблицах лута
CREATE TABLE IF NOT EXISTS economy.loot_table_entries (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    loot_table_id UUID NOT NULL REFERENCES economy.loot_tables(id) ON DELETE CASCADE,
    item_template_id UUID NOT NULL,
    weight INTEGER NOT NULL DEFAULT 1 CHECK (weight > 0),
    min_quantity INTEGER NOT NULL DEFAULT 1 CHECK (min_quantity > 0),
    max_quantity INTEGER NOT NULL DEFAULT 1 CHECK (max_quantity >= min_quantity),
    drop_chance DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (drop_chance >= 0 AND drop_chance <= 100),
    is_guaranteed BOOLEAN NOT NULL DEFAULT false,
    min_level INTEGER CHECK (min_level IS NULL OR min_level >= 1),
    max_level INTEGER CHECK (max_level IS NULL OR max_level >= min_level),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для loot_table_entries
CREATE INDEX IF NOT EXISTS idx_loot_table_entries_loot_table_id ON economy.loot_table_entries(loot_table_id);
CREATE INDEX IF NOT EXISTS idx_loot_table_entries_item_template_id ON economy.loot_table_entries(item_template_id);

-- Таблица распределений лута
CREATE TABLE IF NOT EXISTS economy.loot_distributions (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    source_type loot_source_type NOT NULL,
    source_id UUID NOT NULL,
    distribution_mode loot_distribution_mode NOT NULL,
    party_id UUID,
    status loot_distribution_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    distributed_at TIMESTAMP
);

-- Индексы для loot_distributions
CREATE INDEX IF NOT EXISTS idx_loot_distributions_source ON economy.loot_distributions(source_type, source_id);
CREATE INDEX IF NOT EXISTS idx_loot_distributions_party_status ON economy.loot_distributions(party_id, status) WHERE party_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_loot_distributions_status ON economy.loot_distributions(status, created_at);

-- Таблица предметов в распределениях
CREATE TABLE IF NOT EXISTS economy.loot_distribution_items (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    distribution_id UUID NOT NULL REFERENCES economy.loot_distributions(id) ON DELETE CASCADE,
    item_template_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    assigned_to UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    status loot_item_status NOT NULL DEFAULT 'pending',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для loot_distribution_items
CREATE INDEX IF NOT EXISTS idx_loot_distribution_items_distribution_id ON economy.loot_distribution_items(distribution_id);
CREATE INDEX IF NOT EXISTS idx_loot_distribution_items_assigned_to ON economy.loot_distribution_items(assigned_to, status) WHERE assigned_to IS NOT NULL;

-- Таблица roll сессий для групп
CREATE TABLE IF NOT EXISTS economy.loot_rolls (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    distribution_id UUID NOT NULL REFERENCES economy.loot_distributions(id) ON DELETE CASCADE,
    item_id UUID NOT NULL REFERENCES economy.loot_distribution_items(id) ON DELETE CASCADE,
    party_id UUID NOT NULL,
    status loot_roll_status NOT NULL DEFAULT 'active',
    expires_at TIMESTAMP NOT NULL,
    winner_id UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Индексы для loot_rolls
CREATE INDEX IF NOT EXISTS idx_loot_rolls_distribution_id ON economy.loot_rolls(distribution_id);
CREATE INDEX IF NOT EXISTS idx_loot_rolls_party_status ON economy.loot_rolls(party_id, status);
CREATE INDEX IF NOT EXISTS idx_loot_rolls_expires_at ON economy.loot_rolls(expires_at) WHERE status = 'active';

-- Таблица участников roll сессий
CREATE TABLE IF NOT EXISTS economy.loot_roll_participants (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    roll_id UUID NOT NULL REFERENCES economy.loot_rolls(id) ON DELETE CASCADE,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    choice loot_roll_choice NOT NULL,
    roll_value INTEGER CHECK (roll_value IS NULL OR (roll_value >= 1 AND roll_value <= 100)),
    rolled_at TIMESTAMP,
    UNIQUE(roll_id, character_id)
);

-- Индексы для loot_roll_participants
CREATE INDEX IF NOT EXISTS idx_loot_roll_participants_roll_id ON economy.loot_roll_participants(roll_id, choice);
CREATE INDEX IF NOT EXISTS idx_loot_roll_participants_character_id ON economy.loot_roll_participants(character_id);

-- Таблица мировых дропов
CREATE TABLE IF NOT EXISTS economy.world_drops (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    item_template_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    location_x DECIMAL(10,2) NOT NULL,
    location_y DECIMAL(10,2) NOT NULL,
    location_z DECIMAL(10,2) NOT NULL,
    world_id UUID NOT NULL,
    status world_drop_status NOT NULL DEFAULT 'active',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    picked_up_by UUID REFERENCES mvp_core.character(id) ON DELETE SET NULL,
    picked_up_at TIMESTAMP
);

-- Индексы для world_drops
CREATE INDEX IF NOT EXISTS idx_world_drops_world_location ON economy.world_drops(world_id, location_x, location_y);
CREATE INDEX IF NOT EXISTS idx_world_drops_status_expires ON economy.world_drops(status, expires_at) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_world_drops_status_created ON economy.world_drops(status, created_at);

-- Таблица истории лута игроков (для smart loot)
CREATE TABLE IF NOT EXISTS economy.player_loot_history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    character_id UUID NOT NULL REFERENCES mvp_core.character(id) ON DELETE CASCADE,
    item_template_id UUID NOT NULL,
    source_type loot_source_type NOT NULL,
    obtained_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    luck_value DECIMAL(5,2) NOT NULL DEFAULT 0.0
);

-- Индексы для player_loot_history
CREATE INDEX IF NOT EXISTS idx_player_loot_history_character ON economy.player_loot_history(character_id, obtained_at DESC);
CREATE INDEX IF NOT EXISTS idx_player_loot_history_item ON economy.player_loot_history(item_template_id, character_id);

-- Комментарии к таблицам
COMMENT ON TABLE economy.loot_tables IS 'Таблицы лута (определения дропов)';
COMMENT ON TABLE economy.loot_table_entries IS 'Записи в таблицах лута (предметы с весами и шансами)';
COMMENT ON TABLE economy.loot_distributions IS 'Распределения лута (сгенерированные дропы)';
COMMENT ON TABLE economy.loot_distribution_items IS 'Предметы в распределениях (конкретные предметы для выдачи)';
COMMENT ON TABLE economy.loot_rolls IS 'Roll сессии для групп (Need/Greed система)';
COMMENT ON TABLE economy.loot_roll_participants IS 'Участники roll сессий (выборы игроков)';
COMMENT ON TABLE economy.world_drops IS 'Мировые дропы (предметы, выпавшие в мире)';
COMMENT ON TABLE economy.player_loot_history IS 'История лута игроков (для smart loot)';

-- Комментарии к колонкам
COMMENT ON COLUMN economy.loot_tables.source_type IS 'Тип источника: npc, container, quest, boss, world_event';
COMMENT ON COLUMN economy.loot_tables.min_items IS 'Минимальное количество предметов';
COMMENT ON COLUMN economy.loot_tables.max_items IS 'Максимальное количество предметов';
COMMENT ON COLUMN economy.loot_tables.currency_min IS 'Минимальная валюта';
COMMENT ON COLUMN economy.loot_tables.currency_max IS 'Максимальная валюта';
COMMENT ON COLUMN economy.loot_tables.luck_modifier IS 'Модификатор удачи';
COMMENT ON COLUMN economy.loot_table_entries.weight IS 'Вес предмета в таблице (для вероятности)';
COMMENT ON COLUMN economy.loot_table_entries.drop_chance IS 'Шанс дропа в процентах (0-100)';
COMMENT ON COLUMN economy.loot_table_entries.is_guaranteed IS 'Гарантированный дроп';
COMMENT ON COLUMN economy.loot_distributions.distribution_mode IS 'Режим распределения: personal, party, shared, boss';
COMMENT ON COLUMN economy.loot_distributions.party_id IS 'ID группы (nullable для personal)';
COMMENT ON COLUMN economy.loot_rolls.expires_at IS 'Время истечения roll сессии';
COMMENT ON COLUMN economy.loot_rolls.winner_id IS 'ID победителя roll (nullable до завершения)';
COMMENT ON COLUMN economy.loot_roll_participants.choice IS 'Выбор игрока: need, greed, pass';
COMMENT ON COLUMN economy.loot_roll_participants.roll_value IS 'Значение roll (1-100, nullable для pass)';
COMMENT ON COLUMN economy.world_drops.expires_at IS 'Время истечения мирового дропа';
COMMENT ON COLUMN economy.world_drops.picked_up_by IS 'ID персонажа, поднявшего дроп (nullable)';
COMMENT ON COLUMN economy.player_loot_history.luck_value IS 'Значение удачи при получении предмета';


