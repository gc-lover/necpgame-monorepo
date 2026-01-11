-- Auction House Database Schema
-- Issue: #2175 - Dynamic Pricing Auction House mechanics

-- Create schema for auction house
CREATE SCHEMA IF NOT EXISTS auction_house;

-- Auction lots table - stores active auction listings
CREATE TABLE auction_house.auction_lots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id VARCHAR(100) NOT NULL,
    seller_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    current_price DECIMAL(15,4) NOT NULL CHECK (current_price >= 0),
    buyout_price DECIMAL(15,4) NULL CHECK (buyout_price >= 0),
    reserve_price DECIMAL(15,4) NULL CHECK (reserve_price >= 0),
    starting_price DECIMAL(15,4) NOT NULL CHECK (starting_price >= 0),
    end_time BIGINT NOT NULL, -- Unix timestamp
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'expired', 'cancelled')),
    bid_count INTEGER NOT NULL DEFAULT 0 CHECK (bid_count >= 0),
    time_remaining INTEGER NOT NULL DEFAULT 0 CHECK (time_remaining >= 0),
    priority INTEGER NOT NULL DEFAULT 5 CHECK (priority BETWEEN 1 AND 10),
    is_buyout_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    is_reserve_met BOOLEAN NOT NULL DEFAULT FALSE,
    item_type VARCHAR(50) NOT NULL,
    item_rarity VARCHAR(20) NOT NULL CHECK (item_rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    item_name VARCHAR(200) NOT NULL,
    region VARCHAR(50) NOT NULL DEFAULT 'global',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID NULL,
    updated_by UUID NULL
);

-- Bids table - stores all bids placed on auction lots
CREATE TABLE auction_house.bids (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    lot_id UUID NOT NULL REFERENCES auction_house.auction_lots(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL,
    amount DECIMAL(15,4) NOT NULL CHECK (amount > 0),
    bid_type VARCHAR(20) NOT NULL DEFAULT 'normal' CHECK (bid_type IN ('normal', 'buyout')),
    max_amount DECIMAL(15,4) NULL CHECK (max_amount >= amount), -- For auto-bidding
    is_winning BOOLEAN NOT NULL DEFAULT FALSE,
    placed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NULL,
    created_by UUID NULL
);

-- Trade records table - stores completed transactions
CREATE TABLE auction_house.trade_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    buyer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    item_id VARCHAR(100) NOT NULL,
    item_name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price DECIMAL(15,4) NOT NULL CHECK (price >= 0),
    fee DECIMAL(15,4) NOT NULL DEFAULT 0 CHECK (fee >= 0),
    tax DECIMAL(15,4) NOT NULL DEFAULT 0 CHECK (tax >= 0),
    trade_type VARCHAR(20) NOT NULL DEFAULT 'auction' CHECK (trade_type IN ('buy', 'sell', 'auction')),
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    executed_at_unix BIGINT NOT NULL,
    region VARCHAR(50) NOT NULL DEFAULT 'global',
    region_id INTEGER NOT NULL DEFAULT 1,
    counterparty_id UUID NULL, -- The other party in the trade
    created_by UUID NULL
);

-- Market prices table - stores current market pricing data
CREATE TABLE auction_house.market_prices (
    item_id VARCHAR(100) PRIMARY KEY,
    item_type VARCHAR(50) NOT NULL,
    region VARCHAR(50) NOT NULL DEFAULT 'global',
    current_price DECIMAL(15,4) NOT NULL CHECK (current_price >= 0),
    predicted_price DECIMAL(15,4) NULL CHECK (predicted_price >= 0),
    price_change_24h DECIMAL(7,4) NULL, -- Percentage change
    average_price_7d DECIMAL(15,4) NULL CHECK (average_price_7d >= 0),
    volume_24h BIGINT NOT NULL DEFAULT 0 CHECK (volume_24h >= 0),
    supply_score INTEGER NOT NULL DEFAULT 50 CHECK (supply_score BETWEEN 0 AND 100),
    demand_score INTEGER NOT NULL DEFAULT 50 CHECK (demand_score BETWEEN 0 AND 100),
    stability_score INTEGER NOT NULL DEFAULT 50 CHECK (stability_score BETWEEN 0 AND 100),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_by UUID NULL
);

-- Supply demand history table - stores historical market data for analytics
CREATE TABLE auction_house.supply_demand_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id VARCHAR(100) NOT NULL,
    region VARCHAR(50) NOT NULL DEFAULT 'global',
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    supply BIGINT NOT NULL DEFAULT 0 CHECK (supply >= 0),
    demand BIGINT NOT NULL DEFAULT 0 CHECK (demand >= 0),
    clearing_price DECIMAL(15,4) NULL CHECK (clearing_price >= 0),
    volume BIGINT NOT NULL DEFAULT 0 CHECK (volume >= 0),
    created_by UUID NULL
);

-- Price beliefs table - stores BazaarBot algorithm state
CREATE TABLE auction_house.price_beliefs (
    item_id VARCHAR(100) PRIMARY KEY,
    current_belief DECIMAL(15,4) NOT NULL CHECK (current_belief >= 0),
    belief_variance DECIMAL(15,4) NOT NULL CHECK (belief_variance >= 0),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    trades_count BIGINT NOT NULL DEFAULT 0 CHECK (trades_count >= 0),
    learning_rate DECIMAL(5,4) NOT NULL DEFAULT 0.05 CHECK (learning_rate BETWEEN 0.001 AND 0.5),
    confidence DECIMAL(5,4) NOT NULL DEFAULT 0.1 CHECK (confidence BETWEEN 0 AND 1),
    updated_by UUID NULL
);

-- Auction house statistics table - stores aggregated statistics
CREATE TABLE auction_house.auction_stats (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    stat_date DATE NOT NULL,
    region VARCHAR(50) NOT NULL DEFAULT 'global',
    total_volume BIGINT NOT NULL DEFAULT 0 CHECK (total_volume >= 0),
    total_value DECIMAL(20,4) NOT NULL DEFAULT 0 CHECK (total_value >= 0),
    active_lots INTEGER NOT NULL DEFAULT 0 CHECK (active_lots >= 0),
    completed_trades INTEGER NOT NULL DEFAULT 0 CHECK (completed_trades >= 0),
    unique_traders INTEGER NOT NULL DEFAULT 0 CHECK (unique_traders >= 0),
    average_price_change DECIMAL(7,4) NULL, -- Percentage
    market_efficiency DECIMAL(5,4) NULL CHECK (market_efficiency BETWEEN 0 AND 1),
    response_time_ms INTEGER NULL CHECK (response_time_ms >= 0),
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_by UUID NULL,
    UNIQUE(stat_date, region)
);

-- Player trading history table - for reputation and analytics
CREATE TABLE auction_house.player_trading_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    trade_id UUID NOT NULL REFERENCES auction_house.trade_records(id) ON DELETE CASCADE,
    trade_type VARCHAR(20) NOT NULL CHECK (trade_type IN ('buyer', 'seller')),
    item_id VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price DECIMAL(15,4) NOT NULL CHECK (price >= 0),
    profit_loss DECIMAL(15,4) NULL, -- Calculated profit/loss
    reputation_impact DECIMAL(5,4) NULL, -- Impact on player reputation
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID NULL
);

-- Pricing algorithms table - stores algorithm configurations
CREATE TABLE auction_house.pricing_algorithms (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    algorithm VARCHAR(50) NOT NULL CHECK (algorithm IN ('bazaarbot', 'double_auction', 'simple')),
    parameters JSONB NOT NULL DEFAULT '{}',
    supported_item_types TEXT[] NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    priority INTEGER NOT NULL DEFAULT 1 CHECK (priority >= 1),
    max_items INTEGER NOT NULL DEFAULT 1000 CHECK (max_items > 0),
    version INTEGER NOT NULL DEFAULT 1 CHECK (version >= 1),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID NULL,
    updated_by UUID NULL
);

-- Indexes for performance optimization
CREATE INDEX CONCURRENTLY idx_auction_lots_status ON auction_house.auction_lots(status);
CREATE INDEX CONCURRENTLY idx_auction_lots_seller ON auction_house.auction_lots(seller_id);
CREATE INDEX CONCURRENTLY idx_auction_lots_item_type ON auction_house.auction_lots(item_type);
CREATE INDEX CONCURRENTLY idx_auction_lots_region ON auction_house.auction_lots(region);
CREATE INDEX CONCURRENTLY idx_auction_lots_end_time ON auction_house.auction_lots(end_time);
CREATE INDEX CONCURRENTLY idx_auction_lots_created_at ON auction_house.auction_lots(created_at DESC);

CREATE INDEX CONCURRENTLY idx_bids_lot_id ON auction_house.bids(lot_id);
CREATE INDEX CONCURRENTLY idx_bids_bidder ON auction_house.bids(bidder_id);
CREATE INDEX CONCURRENTLY idx_bids_placed_at ON auction_house.bids(placed_at DESC);

CREATE INDEX CONCURRENTLY idx_trade_records_buyer ON auction_house.trade_records(buyer_id);
CREATE INDEX CONCURRENTLY idx_trade_records_seller ON auction_house.trade_records(seller_id);
CREATE INDEX CONCURRENTLY idx_trade_records_item ON auction_house.trade_records(item_id);
CREATE INDEX CONCURRENTLY idx_trade_records_executed_at ON auction_house.trade_records(executed_at DESC);
CREATE INDEX CONCURRENTLY idx_trade_records_region ON auction_house.trade_records(region);

CREATE INDEX CONCURRENTLY idx_market_prices_item_type ON auction_house.market_prices(item_type);
CREATE INDEX CONCURRENTLY idx_market_prices_region ON auction_house.market_prices(region);
CREATE INDEX CONCURRENTLY idx_market_prices_last_updated ON auction_house.market_prices(last_updated DESC);

CREATE INDEX CONCURRENTLY idx_supply_demand_history_item ON auction_house.supply_demand_history(item_id);
CREATE INDEX CONCURRENTLY idx_supply_demand_history_timestamp ON auction_house.supply_demand_history(timestamp DESC);
CREATE INDEX CONCURRENTLY idx_supply_demand_history_region ON auction_house.supply_demand_history(region);

CREATE INDEX CONCURRENTLY idx_auction_stats_date_region ON auction_house.auction_stats(stat_date, region);
CREATE INDEX CONCURRENTLY idx_auction_stats_last_updated ON auction_house.auction_stats(last_updated DESC);

CREATE INDEX CONCURRENTLY idx_player_trading_history_player ON auction_house.player_trading_history(player_id);
CREATE INDEX CONCURRENTLY idx_player_trading_history_trade ON auction_house.player_trading_history(trade_id);

-- Partial indexes for active records
CREATE INDEX CONCURRENTLY idx_active_lots ON auction_house.auction_lots(status, end_time) WHERE status = 'active';
CREATE INDEX CONCURRENTLY idx_active_bids ON auction_house.bids(is_winning, placed_at) WHERE is_winning = TRUE;

-- JSONB indexes for flexible querying
CREATE INDEX CONCURRENTLY idx_pricing_algorithms_params ON auction_house.pricing_algorithms USING GIN(parameters);
CREATE INDEX CONCURRENTLY idx_pricing_algorithms_item_types ON auction_house.pricing_algorithms USING GIN(supported_item_types);

-- Triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION auction_house.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_auction_lots_updated_at
    BEFORE UPDATE ON auction_house.auction_lots
    FOR EACH ROW EXECUTE FUNCTION auction_house.update_updated_at_column();

CREATE TRIGGER update_pricing_algorithms_updated_at
    BEFORE UPDATE ON auction_house.pricing_algorithms
    FOR EACH ROW EXECUTE FUNCTION auction_house.update_updated_at_column();

-- Insert default pricing algorithms
INSERT INTO auction_house.pricing_algorithms (id, name, description, algorithm, parameters, supported_item_types, priority, max_items) VALUES
('bazaarbot_standard', 'BazaarBot Standard', 'Standard BazaarBot algorithm with adaptive learning', 'bazaarbot',
 '{"learning_rate": 0.05, "initial_variance": 0.5, "confidence_threshold": 0.8}',
 ARRAY['weapons', 'armor', 'resources', 'consumables', 'crafting_materials'], 1, 10000),

('double_auction_economy', 'Double Auction Economy', 'Double auction for high-volume economic items', 'double_auction',
 '{"min_order_size": 1, "max_spread": 0.1, "clearing_mechanism": "midpoint"}',
 ARRAY['resources', 'crafting_materials'], 2, 50000),

('simple_market', 'Simple Market Pricing', 'Simple supply/demand based pricing for basic items', 'simple',
 '{"base_price_multiplier": 1.0, "volatility_factor": 0.1}',
 ARRAY['consumables'], 3, 25000);

-- Insert default market prices for common items
INSERT INTO auction_house.market_prices (item_id, item_type, region, current_price, volume_24h, supply_score, demand_score, stability_score) VALUES
('sword_basic', 'weapons', 'global', 100.00, 150, 60, 70, 80),
('armor_light', 'armor', 'global', 250.00, 89, 55, 65, 75),
('ore_iron', 'resources', 'global', 15.50, 1200, 80, 85, 90),
('potion_health', 'consumables', 'global', 45.00, 450, 70, 75, 85),
('leather_common', 'crafting_materials', 'global', 8.75, 780, 75, 80, 88);