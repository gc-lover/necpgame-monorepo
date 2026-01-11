-- Issue: #2175 - Dynamic Pricing Auction House mechanics
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create dynamic pricing auction system tables and schema

BEGIN;

-- Create schema for auction system
CREATE SCHEMA IF NOT EXISTS auction;

-- Grant permissions
GRANT USAGE ON SCHEMA auction TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA auction TO necpgame_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA auction TO necpgame_app;

-- Table: auction.items
-- Stores auction items with dynamic pricing data
CREATE TABLE IF NOT EXISTS auction.items
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    category VARCHAR(20) NOT NULL CHECK (category IN ('weapons', 'armor', 'consumables', 'materials', 'vehicles', 'cyberware')),
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    base_price DECIMAL(15,2) NOT NULL CHECK (base_price > 0),
    current_bid DECIMAL(15,2) NOT NULL DEFAULT 0 CHECK (current_bid >= 0),
    buyout_price DECIMAL(15,2) CHECK (buyout_price > base_price),
    seller_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'cancelled', 'expired')),
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Ensure end_time is in the future for active auctions
    CONSTRAINT valid_end_time CHECK (
        status != 'active' OR end_time > CURRENT_TIMESTAMP
    ),

    -- Ensure buyout price is higher than base price
    CONSTRAINT valid_buyout_price CHECK (
        buyout_price IS NULL OR buyout_price > base_price
    )
);

-- Table: auction.bids
-- Stores all bids placed on auctions
CREATE TABLE IF NOT EXISTS auction.bids
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id UUID NOT NULL REFERENCES auction.items(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_winning BOOLEAN NOT NULL DEFAULT false,

    -- Ensure bidder is not the seller
    CONSTRAINT bidder_not_seller CHECK (bidder_id != (SELECT seller_id FROM auction.items WHERE id = item_id))
);

-- Create index for efficient bid queries
CREATE INDEX IF NOT EXISTS idx_bids_item_timestamp ON auction.bids(item_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_bids_bidder ON auction.bids(bidder_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_bids_winning ON auction.bids(item_id, is_winning) WHERE is_winning = true;

-- Table: auction.market_data
-- Stores market analysis data for each category
CREATE TABLE IF NOT EXISTS auction.market_data
(
    category VARCHAR(20) PRIMARY KEY CHECK (category IN ('weapons', 'armor', 'consumables', 'materials', 'vehicles', 'cyberware')),
    total_volume BIGINT NOT NULL DEFAULT 0 CHECK (total_volume >= 0),
    average_price DECIMAL(15,2) NOT NULL DEFAULT 0 CHECK (average_price >= 0),
    median_price DECIMAL(15,2) NOT NULL DEFAULT 0 CHECK (median_price >= 0),
    price_std_dev DECIMAL(15,2) NOT NULL DEFAULT 0 CHECK (price_std_dev >= 0),
    supply_velocity DECIMAL(10,4) NOT NULL DEFAULT 0,
    demand_velocity DECIMAL(10,4) NOT NULL DEFAULT 0,
    price_elasticity DECIMAL(5,4) DEFAULT 0,
    market_saturation DECIMAL(3,2) NOT NULL DEFAULT 0 CHECK (market_saturation >= 0 AND market_saturation <= 1),
    last_update TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: auction.price_history
-- Stores historical price data for market analysis
CREATE TABLE IF NOT EXISTS auction.price_history
(
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(20) NOT NULL CHECK (category IN ('weapons', 'armor', 'consumables', 'materials', 'vehicles', 'cyberware')),
    item_id UUID REFERENCES auction.items(id) ON DELETE SET NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    price DECIMAL(15,2) NOT NULL CHECK (price >= 0),
    volume INTEGER NOT NULL DEFAULT 1 CHECK (volume > 0),
    type VARCHAR(20) NOT NULL DEFAULT 'bid' CHECK (type IN ('bid', 'market', 'algorithm', 'manual')),

    -- Composite index for efficient time-series queries
    CONSTRAINT unique_price_point UNIQUE (category, timestamp, item_id, type)
);

-- Create indexes for price history (optimized for time-series queries)
CREATE INDEX IF NOT EXISTS idx_price_history_category_time ON auction.price_history(category, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_price_history_item_time ON auction.price_history(item_id, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_price_history_type ON auction.price_history(type, timestamp DESC);

-- Partition price_history by month for better performance (if needed in production)
-- CREATE TABLE auction.price_history_y2024m01 PARTITION OF auction.price_history FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');

-- Table: auction.auction_results
-- Stores completed auction results for analysis
CREATE TABLE IF NOT EXISTS auction.auction_results
(
    item_id UUID PRIMARY KEY REFERENCES auction.items(id) ON DELETE CASCADE,
    final_price DECIMAL(15,2) NOT NULL CHECK (final_price >= 0),
    winner_id UUID,
    seller_id UUID NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    duration INTERVAL NOT NULL,
    total_bids INTEGER NOT NULL DEFAULT 0 CHECK (total_bids >= 0),
    price_efficiency DECIMAL(5,4) CHECK (price_efficiency >= 0 AND price_efficiency <= 2), -- Efficiency ratio
    market_impact DECIMAL(5,4) DEFAULT 0, -- Market impact score
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table: auction.algorithm_performance
-- Stores performance metrics for pricing algorithms
CREATE TABLE IF NOT EXISTS auction.algorithm_performance
(
    id BIGSERIAL PRIMARY KEY,
    algorithm_type VARCHAR(20) NOT NULL CHECK (algorithm_type IN ('linear', 'exponential', 'adaptive')),
    category VARCHAR(20) NOT NULL CHECK (category IN ('weapons', 'armor', 'consumables', 'materials', 'vehicles', 'cyberware')),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accuracy_score DECIMAL(5,4) CHECK (accuracy_score >= 0 AND accuracy_score <= 1),
    prediction_error DECIMAL(15,2) CHECK (prediction_error >= 0),
    response_time_ms INTEGER CHECK (response_time_ms >= 0),
    auctions_processed INTEGER NOT NULL DEFAULT 1 CHECK (auctions_processed > 0)
);

-- Create indexes for algorithm performance
CREATE INDEX IF NOT EXISTS idx_algorithm_performance_type_time ON auction.algorithm_performance(algorithm_type, timestamp DESC);
CREATE INDEX IF NOT EXISTS idx_algorithm_performance_category ON auction.algorithm_performance(category, timestamp DESC);

-- Triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION auction.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_updated_at
    BEFORE UPDATE ON auction.items
    FOR EACH ROW EXECUTE FUNCTION auction.update_updated_at_column();

-- Function to update market data when bids are placed
CREATE OR REPLACE FUNCTION auction.update_market_data_on_bid()
RETURNS TRIGGER AS $$
DECLARE
    item_category VARCHAR(20);
    new_avg_price DECIMAL(15,2);
    new_median_price DECIMAL(15,2);
    new_std_dev DECIMAL(15,2);
    total_volume BIGINT;
BEGIN
    -- Get item category
    SELECT category INTO item_category
    FROM auction.items
    WHERE id = NEW.item_id;

    -- Calculate new market statistics (simplified version)
    SELECT
        COUNT(*) + COALESCE((SELECT total_volume FROM auction.market_data WHERE category = item_category), 0),
        AVG(amount) FILTER (WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours'),
        STDDEV(amount) FILTER (WHERE timestamp >= CURRENT_TIMESTAMP - INTERVAL '24 hours')
    INTO total_volume, new_avg_price, new_std_dev
    FROM auction.bids
    WHERE item_id IN (
        SELECT id FROM auction.items
        WHERE category = item_category
        AND created_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
    );

    -- Calculate median (simplified)
    SELECT percentile_cont(0.5) WITHIN GROUP (ORDER BY amount)
    INTO new_median_price
    FROM auction.bids
    WHERE item_id IN (
        SELECT id FROM auction.items
        WHERE category = item_category
        AND created_at >= CURRENT_TIMESTAMP - INTERVAL '24 hours'
    );

    -- Update market data
    INSERT INTO auction.market_data (
        category, total_volume, average_price, median_price, price_std_dev, last_update
    ) VALUES (
        item_category, total_volume,
        COALESCE(new_avg_price, 0),
        COALESCE(new_median_price, 0),
        COALESCE(new_std_dev, 0),
        CURRENT_TIMESTAMP
    )
    ON CONFLICT (category) DO UPDATE SET
        total_volume = EXCLUDED.total_volume,
        average_price = EXCLUDED.average_price,
        median_price = EXCLUDED.median_price,
        price_std_dev = EXCLUDED.price_std_dev,
        last_update = EXCLUDED.last_update;

    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_update_market_data_on_bid
    AFTER INSERT ON auction.bids
    FOR EACH ROW EXECUTE FUNCTION auction.update_market_data_on_bid();

-- Function to mark winning bids
CREATE OR REPLACE FUNCTION auction.update_winning_bids()
RETURNS TRIGGER AS $$
BEGIN
    -- Mark previous winning bid as not winning
    UPDATE auction.bids
    SET is_winning = false
    WHERE item_id = NEW.item_id AND id != NEW.id;

    -- Mark new bid as winning
    NEW.is_winning := true;

    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_update_winning_bids
    BEFORE INSERT ON auction.bids
    FOR EACH ROW EXECUTE FUNCTION auction.update_winning_bids();

-- Row Level Security (RLS) policies
ALTER TABLE auction.items ENABLE ROW LEVEL SECURITY;
ALTER TABLE auction.bids ENABLE ROW LEVEL SECURITY;
ALTER TABLE auction.market_data ENABLE ROW LEVEL SECURITY;
ALTER TABLE auction.price_history ENABLE ROW LEVEL SECURITY;
ALTER TABLE auction.auction_results ENABLE ROW LEVEL SECURITY;
ALTER TABLE auction.algorithm_performance ENABLE ROW LEVEL SECURITY;

-- RLS Policies for items table
CREATE POLICY items_read_policy ON auction.items
    FOR SELECT USING (true);

CREATE POLICY items_insert_policy ON auction.items
    FOR INSERT WITH CHECK (seller_id = current_setting('app.current_user_id', true)::uuid);

CREATE POLICY items_update_policy ON auction.items
    FOR UPDATE USING (
        seller_id = current_setting('app.current_user_id', true)::uuid OR
        current_setting('app.current_user_role', true) = 'admin'
    );

-- RLS Policies for bids table
CREATE POLICY bids_read_policy ON auction.bids
    FOR SELECT USING (true);

CREATE POLICY bids_insert_policy ON auction.bids
    FOR INSERT WITH CHECK (bidder_id = current_setting('app.current_user_id', true)::uuid);

-- RLS Policies for market data (read-only for all)
CREATE POLICY market_data_read_policy ON auction.market_data
    FOR SELECT USING (true);

-- Insert initial market data for all categories
INSERT INTO auction.market_data (category, average_price, median_price) VALUES
    ('weapons', 1000.00, 950.00),
    ('armor', 800.00, 750.00),
    ('consumables', 50.00, 45.00),
    ('materials', 25.00, 20.00),
    ('vehicles', 5000.00, 4500.00),
    ('cyberware', 2000.00, 1800.00)
ON CONFLICT (category) DO NOTHING;

COMMIT;

--changeset author:necpgame dbms:postgresql
--comment: Create indexes and constraints for auction system performance

-- Performance indexes for auction queries
CREATE INDEX IF NOT EXISTS idx_items_category_status ON auction.items(category, status);
CREATE INDEX IF NOT EXISTS idx_items_seller_status ON auction.items(seller_id, status);
CREATE INDEX IF NOT EXISTS idx_items_end_time ON auction.items(end_time) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_items_created_at ON auction.items(created_at DESC);

-- Partial indexes for common queries
CREATE INDEX IF NOT EXISTS idx_items_active_category ON auction.items(category) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_items_expired ON auction.items(end_time) WHERE end_time < CURRENT_TIMESTAMP AND status = 'active';

-- Performance indexes for auction results
CREATE INDEX IF NOT EXISTS idx_auction_results_end_time ON auction.auction_results(end_time DESC);
CREATE INDEX IF NOT EXISTS idx_auction_results_seller ON auction.auction_results(seller_id, end_time DESC);
CREATE INDEX IF NOT EXISTS idx_auction_results_winner ON auction.auction_results(winner_id, end_time DESC);

-- Function to automatically expire auctions
CREATE OR REPLACE FUNCTION auction.expire_auctions()
RETURNS void AS $$
BEGIN
    UPDATE auction.items
    SET status = 'expired', updated_at = CURRENT_TIMESTAMP
    WHERE status = 'active' AND end_time < CURRENT_TIMESTAMP;
END;
$$ language 'plpgsql';

-- Function to finalize auction results
CREATE OR REPLACE FUNCTION auction.finalize_auction(p_item_id UUID)
RETURNS void AS $$
DECLARE
    winning_bid RECORD;
    item_record RECORD;
BEGIN
    -- Get item details
    SELECT * INTO item_record FROM auction.items WHERE id = p_item_id;

    IF item_record.status != 'active' THEN
        RETURN;
    END IF;

    -- Get winning bid
    SELECT * INTO winning_bid
    FROM auction.bids
    WHERE item_id = p_item_id AND is_winning = true
    ORDER BY amount DESC, timestamp ASC
    LIMIT 1;

    -- Calculate duration
    DECLARE
        auction_duration INTERVAL := item_record.end_time - item_record.created_at;
        final_price DECIMAL(15,2) := COALESCE(winning_bid.amount, 0);
        total_bids_count INTEGER := (SELECT COUNT(*) FROM auction.bids WHERE item_id = p_item_id);
    BEGIN
        -- Insert auction result
        INSERT INTO auction.auction_results (
            item_id, final_price, winner_id, seller_id, end_time, duration, total_bids
        ) VALUES (
            p_item_id, final_price, winning_bid.bidder_id, item_record.seller_id,
            item_record.end_time, auction_duration, total_bids_count
        );

        -- Update item status
        UPDATE auction.items
        SET status = CASE WHEN winning_bid.bidder_id IS NOT NULL THEN 'sold' ELSE 'expired' END,
            current_bid = final_price,
            updated_at = CURRENT_TIMESTAMP
        WHERE id = p_item_id;
    END;
END;
$$ language 'plpgsql';