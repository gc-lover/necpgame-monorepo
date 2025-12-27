-- Economy System Tables Migration
-- Enterprise-grade schema for MMOFPS RPG economic operations

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Player wallets table
-- Stores player currency balances with performance optimizations
CREATE TABLE IF NOT EXISTS player_wallets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    currency_type VARCHAR(50) NOT NULL DEFAULT 'credits',
    balance DECIMAL(20,2) NOT NULL DEFAULT 0 CHECK (balance >= 0),
    reserved_balance DECIMAL(20,2) NOT NULL DEFAULT 0 CHECK (reserved_balance >= 0),
    last_transaction_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Composite unique constraint for efficient wallet lookups
    UNIQUE(player_id, currency_type),

    -- Index for performance
    CONSTRAINT fk_player_wallets_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);

-- Economic transactions table
-- Audit trail for all economic operations with high-throughput support
CREATE TABLE IF NOT EXISTS economic_transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    transaction_id UUID NOT NULL, -- Groups related operations
    player_id UUID NOT NULL,
    transaction_type VARCHAR(50) NOT NULL, -- purchase, sale, transfer, reward, penalty, etc.
    currency_type VARCHAR(50) NOT NULL DEFAULT 'credits',
    amount DECIMAL(20,2) NOT NULL,
    balance_before DECIMAL(20,2) NOT NULL,
    balance_after DECIMAL(20,2) NOT NULL,
    description TEXT,
    metadata JSONB, -- Additional transaction data
    processed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Indexes for high-performance queries
    INDEX idx_economic_transactions_player_time (player_id, processed_at DESC),
    INDEX idx_economic_transactions_type (transaction_type),
    INDEX idx_economic_transactions_transaction_id (transaction_id)
);

-- Market listings table
-- Player-to-player marketplace listings
CREATE TABLE IF NOT EXISTS market_listings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL,
    item_id UUID NOT NULL,
    item_instance_id UUID, -- For unique items
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    price_per_unit DECIMAL(20,2) NOT NULL CHECK (price_per_unit > 0),
    currency_type VARCHAR(50) NOT NULL DEFAULT 'credits',
    listing_type VARCHAR(20) NOT NULL DEFAULT 'sell' CHECK (listing_type IN ('sell', 'buy')),
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'cancelled', 'expired')),
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_market_listings_seller FOREIGN KEY (seller_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_market_listings_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

-- Trade orders table
-- Advanced trading system for complex economic operations
CREATE TABLE IF NOT EXISTS trade_orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    player_id UUID NOT NULL,
    order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('buy', 'sell')),
    item_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price_per_unit DECIMAL(20,2) NOT NULL CHECK (price_per_unit > 0),
    currency_type VARCHAR(50) NOT NULL DEFAULT 'credits',
    order_status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (order_status IN ('open', 'partial', 'filled', 'cancelled')),
    filled_quantity INTEGER NOT NULL DEFAULT 0 CHECK (filled_quantity >= 0),
    expires_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints and indexes
    CONSTRAINT fk_trade_orders_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_trade_orders_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CHECK (filled_quantity <= quantity)
);

-- Auction houses table
-- Timed auctions for rare items
CREATE TABLE IF NOT EXISTS auctions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    seller_id UUID NOT NULL,
    item_id UUID NOT NULL,
    item_instance_id UUID,
    starting_price DECIMAL(20,2) NOT NULL CHECK (starting_price > 0),
    current_bid DECIMAL(20,2),
    buyout_price DECIMAL(20,2),
    currency_type VARCHAR(50) NOT NULL DEFAULT 'credits',
    current_bidder_id UUID,
    bid_count INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'ended', 'cancelled')),
    ends_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT fk_auctions_seller FOREIGN KEY (seller_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_auctions_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    CONSTRAINT fk_auctions_bidder FOREIGN KEY (current_bidder_id) REFERENCES players(id) ON DELETE SET NULL
);

-- Auction bids table
-- Bid history for auctions
CREATE TABLE IF NOT EXISTS auction_bids (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auction_id UUID NOT NULL REFERENCES auctions(id) ON DELETE CASCADE,
    bidder_id UUID NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    bid_amount DECIMAL(20,2) NOT NULL CHECK (bid_amount > 0),
    bid_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Index for bid history queries
    INDEX idx_auction_bids_auction_time (auction_id, bid_time DESC)
);

-- Economic events table
-- Event-driven economic system updates
CREATE TABLE IF NOT EXISTS economic_events (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(50) NOT NULL,
    player_id UUID,
    entity_type VARCHAR(50), -- wallet, market, auction, etc.
    entity_id UUID,
    event_data JSONB NOT NULL DEFAULT '{}'::jsonb,
    processed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Indexes for event processing
    INDEX idx_economic_events_type (event_type),
    INDEX idx_economic_events_processed (processed),
    INDEX idx_economic_events_created (created_at)
);

-- Currency exchange rates table
-- Dynamic currency conversion rates
CREATE TABLE IF NOT EXISTS currency_rates (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    from_currency VARCHAR(50) NOT NULL,
    to_currency VARCHAR(50) NOT NULL,
    exchange_rate DECIMAL(10,6) NOT NULL CHECK (exchange_rate > 0),
    effective_from TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    effective_until TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Unique constraint for active rates
    UNIQUE(from_currency, to_currency, effective_from)
);

-- Economic statistics table
-- Aggregated economic data for analytics
CREATE TABLE IF NOT EXISTS economic_statistics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    stat_type VARCHAR(50) NOT NULL, -- daily_volume, player_wealth, etc.
    stat_key VARCHAR(100) NOT NULL, -- Specific identifier (player_id, item_id, etc.)
    stat_value DECIMAL(20,2) NOT NULL,
    stat_date DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Composite index for efficient queries
    UNIQUE(stat_type, stat_key, stat_date)
);

-- Indexes for performance optimization

-- Player wallets indexes
CREATE INDEX IF NOT EXISTS idx_player_wallets_player ON player_wallets(player_id);
CREATE INDEX IF NOT EXISTS idx_player_wallets_currency ON player_wallets(currency_type);
CREATE INDEX IF NOT EXISTS idx_player_wallets_updated ON player_wallets(updated_at DESC);

-- Economic transactions indexes (additional to those defined inline)
CREATE INDEX IF NOT EXISTS idx_economic_transactions_currency ON economic_transactions(currency_type);
CREATE INDEX IF NOT EXISTS idx_economic_transactions_amount ON economic_transactions(amount);

-- Market listings indexes
CREATE INDEX IF NOT EXISTS idx_market_listings_seller ON market_listings(seller_id);
CREATE INDEX IF NOT EXISTS idx_market_listings_item ON market_listings(item_id);
CREATE INDEX IF NOT EXISTS idx_market_listings_status ON market_listings(status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_market_listings_price ON market_listings(price_per_unit);
CREATE INDEX IF NOT EXISTS idx_market_listings_expires ON market_listings(expires_at) WHERE expires_at IS NOT NULL;

-- Trade orders indexes
CREATE INDEX IF NOT EXISTS idx_trade_orders_player ON trade_orders(player_id);
CREATE INDEX IF NOT EXISTS idx_trade_orders_item ON trade_orders(item_id);
CREATE INDEX IF NOT EXISTS idx_trade_orders_status ON trade_orders(order_status) WHERE order_status = 'open';
CREATE INDEX IF NOT EXISTS idx_trade_orders_price ON trade_orders(price_per_unit);

-- Auctions indexes
CREATE INDEX IF NOT EXISTS idx_auctions_seller ON auctions(seller_id);
CREATE INDEX IF NOT EXISTS idx_auctions_status ON auctions(status) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auctions_ends ON auctions(ends_at) WHERE status = 'active';
CREATE INDEX IF NOT EXISTS idx_auctions_current_bid ON auctions(current_bid);

-- Currency rates indexes
CREATE INDEX IF NOT EXISTS idx_currency_rates_from_to ON currency_rates(from_currency, to_currency);
CREATE INDEX IF NOT EXISTS idx_currency_rates_effective ON currency_rates(effective_from, effective_until);

-- Triggers for automatic timestamp updates

-- Player wallets updated_at trigger
CREATE OR REPLACE FUNCTION update_player_wallets_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_player_wallets_updated_at
    BEFORE UPDATE ON player_wallets
    FOR EACH ROW
    EXECUTE FUNCTION update_player_wallets_updated_at();

-- Market listings updated_at trigger
CREATE OR REPLACE FUNCTION update_market_listings_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_market_listings_updated_at
    BEFORE UPDATE ON market_listings
    FOR EACH ROW
    EXECUTE FUNCTION update_market_listings_updated_at();

-- Trade orders updated_at trigger
CREATE OR REPLACE FUNCTION update_trade_orders_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_trade_orders_updated_at
    BEFORE UPDATE ON trade_orders
    FOR EACH ROW
    EXECUTE FUNCTION update_trade_orders_updated_at();

-- Auctions updated_at trigger
CREATE OR REPLACE FUNCTION update_auctions_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_auctions_updated_at
    BEFORE UPDATE ON auctions
    FOR EACH ROW
    EXECUTE FUNCTION update_auctions_updated_at();

-- Function to automatically log economic transactions
CREATE OR REPLACE FUNCTION log_economic_transaction()
RETURNS TRIGGER AS $$
DECLARE
    trans_id UUID;
BEGIN
    -- Generate transaction ID for grouping related operations
    trans_id := COALESCE(NEW.transaction_id, uuid_generate_v4());

    -- Update transaction_id if it was null
    IF NEW.transaction_id IS NULL THEN
        NEW.transaction_id := trans_id;
    END IF;

    -- Insert audit record (actual logic would be more complex)
    -- This is a simplified version for the trigger

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_economic_transaction
    BEFORE INSERT ON economic_transactions
    FOR EACH ROW
    EXECUTE FUNCTION log_economic_transaction();

-- Function to update wallet balance and create transaction
CREATE OR REPLACE FUNCTION update_wallet_balance(
    p_player_id UUID,
    p_currency_type VARCHAR(50),
    p_amount DECIMAL(20,2),
    p_transaction_type VARCHAR(50),
    p_description TEXT DEFAULT NULL,
    p_metadata JSONB DEFAULT NULL
)
RETURNS BOOLEAN AS $$
DECLARE
    current_balance DECIMAL(20,2);
    transaction_id UUID := uuid_generate_v4();
BEGIN
    -- Get current balance
    SELECT balance INTO current_balance
    FROM player_wallets
    WHERE player_id = p_player_id AND currency_type = p_currency_type;

    IF NOT FOUND THEN
        -- Create wallet if it doesn't exist
        INSERT INTO player_wallets (player_id, currency_type, balance)
        VALUES (p_player_id, p_currency_type, 0);
        current_balance := 0;
    END IF;

    -- Check for sufficient funds on debit
    IF p_amount < 0 AND current_balance + p_amount < 0 THEN
        RAISE EXCEPTION 'Insufficient funds: balance=%, required=%', current_balance, -p_amount;
    END IF;

    -- Update balance
    UPDATE player_wallets
    SET balance = balance + p_amount,
        last_transaction_at = CURRENT_TIMESTAMP,
        updated_at = CURRENT_TIMESTAMP
    WHERE player_id = p_player_id AND currency_type = p_currency_type;

    -- Create transaction record
    INSERT INTO economic_transactions (
        transaction_id, player_id, transaction_type, currency_type,
        amount, balance_before, balance_after, description, metadata
    ) VALUES (
        transaction_id, p_player_id, p_transaction_type, p_currency_type,
        p_amount, current_balance, current_balance + p_amount, p_description, p_metadata
    );

    RETURN true;
END;
$$ LANGUAGE plpgsql;

-- Function to process market listing expiration
CREATE OR REPLACE FUNCTION expire_market_listings()
RETURNS INTEGER AS $$
DECLARE
    expired_count INTEGER;
BEGIN
    UPDATE market_listings
    SET status = 'expired', updated_at = CURRENT_TIMESTAMP
    WHERE status = 'active' AND expires_at < CURRENT_TIMESTAMP;

    GET DIAGNOSTICS expired_count = ROW_COUNT;
    RETURN expired_count;
END;
$$ LANGUAGE plpgsql;

-- Function to process auction expiration
CREATE OR REPLACE FUNCTION expire_auctions()
RETURNS INTEGER AS $$
DECLARE
    expired_count INTEGER;
BEGIN
    -- Mark auctions as ended
    UPDATE auctions
    SET status = 'ended', updated_at = CURRENT_TIMESTAMP
    WHERE status = 'active' AND ends_at < CURRENT_TIMESTAMP;

    GET DIAGNOSTICS expired_count = ROW_COUNT;
    RETURN expired_count;
END;
$$ LANGUAGE plpgsql;

-- Function to get player economic summary
CREATE OR REPLACE FUNCTION get_player_economic_summary(player_uuid UUID)
RETURNS TABLE (
    total_balance DECIMAL(20,2),
    active_listings INTEGER,
    open_orders INTEGER,
    active_auctions INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(pw.balance), 0)::DECIMAL(20,2) as total_balance,
        COUNT(ml.id)::INTEGER as active_listings,
        COUNT(to.id)::INTEGER as open_orders,
        COUNT(a.id)::INTEGER as active_auctions
    FROM players p
    LEFT JOIN player_wallets pw ON p.id = pw.player_id
    LEFT JOIN market_listings ml ON p.id = ml.seller_id AND ml.status = 'active'
    LEFT JOIN trade_orders to ON p.id = to.player_id AND to.order_status = 'open'
    LEFT JOIN auctions a ON p.id = a.seller_id AND a.status = 'active'
    WHERE p.id = player_uuid;
END;
$$ LANGUAGE plpgsql;

-- Comments for documentation
COMMENT ON TABLE player_wallets IS 'Player currency balances with reservation support';
COMMENT ON TABLE economic_transactions IS 'Complete audit trail for all economic operations';
COMMENT ON TABLE market_listings IS 'Player-to-player marketplace listings';
COMMENT ON TABLE trade_orders IS 'Advanced trading system orders';
COMMENT ON TABLE auctions IS 'Timed auctions for rare items';
COMMENT ON TABLE auction_bids IS 'Bid history for auctions';
COMMENT ON TABLE economic_events IS 'Event-driven economic system updates';
COMMENT ON TABLE currency_rates IS 'Dynamic currency conversion rates';
COMMENT ON TABLE economic_statistics IS 'Aggregated economic data for analytics';

COMMENT ON FUNCTION update_wallet_balance(UUID, VARCHAR, DECIMAL, VARCHAR, TEXT, JSONB) IS 'Atomic wallet balance update with transaction logging';
COMMENT ON FUNCTION expire_market_listings() IS 'Process expired market listings';
COMMENT ON FUNCTION expire_auctions() IS 'Process expired auctions';
COMMENT ON FUNCTION get_player_economic_summary(UUID) IS 'Returns comprehensive economic summary for a player';
