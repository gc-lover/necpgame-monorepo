--liquibase formatted sql

--changeset necpgame:20250110_001_create_market_states_table
--comment: Create market_states table for BazaarBot simulation state persistence

-- Market state table for BazaarBot simulation
CREATE TABLE IF NOT EXISTS gameplay.market_states (
    id BIGSERIAL PRIMARY KEY,
    commodity VARCHAR(50) NOT NULL,
    tick_id VARCHAR(255) NOT NULL,
    game_hour INTEGER,
    game_day INTEGER,
    price DECIMAL(15,4) NOT NULL,
    volume INTEGER NOT NULL DEFAULT 0,
    market_efficiency DECIMAL(5,2), -- 0.00 to 1.00
    agent_count INTEGER DEFAULT 0,
    bid_count INTEGER DEFAULT 0,
    ask_count INTEGER DEFAULT 0,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),

    -- Constraints
    CONSTRAINT check_price_positive CHECK (price > 0),
    CONSTRAINT check_volume_non_negative CHECK (volume >= 0),
    CONSTRAINT check_efficiency_range CHECK (market_efficiency >= 0 AND market_efficiency <= 1),
    CONSTRAINT check_agent_count_non_negative CHECK (agent_count >= 0),
    CONSTRAINT check_bid_count_non_negative CHECK (bid_count >= 0),
    CONSTRAINT check_ask_count_non_negative CHECK (ask_count >= 0),
    CONSTRAINT check_game_hour_range CHECK (game_hour IS NULL OR (game_hour >= 0 AND game_hour <= 23)),
    CONSTRAINT check_game_day_positive CHECK (game_day IS NULL OR game_day > 0),

    -- Unique constraint for commodity + tick_id (prevents duplicate market clears per tick)
    CONSTRAINT uk_market_states_commodity_tick UNIQUE (commodity, tick_id)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_market_states_commodity ON gameplay.market_states(commodity);
CREATE INDEX IF NOT EXISTS idx_market_states_tick_id ON gameplay.market_states(tick_id);
CREATE INDEX IF NOT EXISTS idx_market_states_recorded_at ON gameplay.market_states(recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_states_game_hour ON gameplay.market_states(game_hour);
CREATE INDEX IF NOT EXISTS idx_market_states_game_day ON gameplay.market_states(game_day);
CREATE INDEX IF NOT EXISTS idx_market_states_price ON gameplay.market_states(price);

-- Composite indexes for common queries
CREATE INDEX IF NOT EXISTS idx_market_states_commodity_recorded ON gameplay.market_states(commodity, recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_market_states_commodity_hour ON gameplay.market_states(commodity, game_hour);

-- Table comment
COMMENT ON TABLE gameplay.market_states IS 'Stores BazaarBot market clearing results and simulation state for each commodity per tick';

-- Column comments
COMMENT ON COLUMN gameplay.market_states.commodity IS 'BazaarBot commodity type (Food, Wood, Metal, Weapon, Crystal)';
COMMENT ON COLUMN gameplay.market_states.tick_id IS 'Unique simulation tick identifier from Kafka event';
COMMENT ON COLUMN gameplay.market_states.game_hour IS 'Game hour (0-23) when market was cleared';
COMMENT ON COLUMN gameplay.market_states.game_day IS 'Game day number when market was cleared';
COMMENT ON COLUMN gameplay.market_states.price IS 'Final market clearing price per unit';
COMMENT ON COLUMN gameplay.market_states.volume IS 'Total volume traded in this market clearing';
COMMENT ON COLUMN gameplay.market_states.market_efficiency IS 'Market efficiency score (0.00-1.00) from BazaarBot simulation';
COMMENT ON COLUMN gameplay.market_states.agent_count IS 'Number of active trading agents in this market';
COMMENT ON COLUMN gameplay.market_states.bid_count IS 'Number of buy orders in order book';
COMMENT ON COLUMN gameplay.market_states.ask_count IS 'Number of sell orders in order book';
COMMENT ON COLUMN gameplay.market_states.recorded_at IS 'Timestamp when market state was recorded';

-- Permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.market_states TO necpgame_app;
GRANT USAGE, SELECT ON SEQUENCE gameplay.market_states_id_seq TO necpgame_app;

--rollback DROP TABLE IF EXISTS gameplay.market_states;