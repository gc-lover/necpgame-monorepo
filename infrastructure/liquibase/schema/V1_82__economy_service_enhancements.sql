-- Issue: #2237, #2232
--liquibase formatted sql

--changeset economy:service_enhancements runOnChange:true
--comment: Enhanced economy service with auctions, crafting, and market analytics tables

BEGIN;

-- Auction bids table
CREATE TABLE IF NOT EXISTS gameplay.auction_bids (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    auction_id UUID NOT NULL,
    bidder_id VARCHAR(255) NOT NULL,
    amount BIGINT NOT NULL CHECK (amount > 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_auction_bids_auction FOREIGN KEY (auction_id)
        REFERENCES gameplay.auctions(id) ON DELETE CASCADE
);

-- Add indexes for auction bids
CREATE INDEX IF NOT EXISTS idx_auction_bids_auction_id ON gameplay.auction_bids(auction_id);
CREATE INDEX IF NOT EXISTS idx_auction_bids_bidder_id ON gameplay.auction_bids(bidder_id);
CREATE INDEX IF NOT EXISTS idx_auction_bids_created_at ON gameplay.auction_bids(created_at);

-- Auctions table (if not exists)
CREATE TABLE IF NOT EXISTS gameplay.auctions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id VARCHAR(255) NOT NULL,
    seller_id VARCHAR(255) NOT NULL,
    current_bidder_id VARCHAR(255),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'sold', 'cancelled', 'expired')),
    currency VARCHAR(20) NOT NULL DEFAULT 'eurodollars',
    start_price BIGINT NOT NULL CHECK (start_price > 0),
    current_bid BIGINT NOT NULL DEFAULT 0,
    buyout_price BIGINT,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    bid_count INTEGER NOT NULL DEFAULT 0,
    sold_price BIGINT,
    sold_at TIMESTAMP WITH TIME ZONE,
    winning_bidder_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT check_buyout_price CHECK (buyout_price IS NULL OR buyout_price >= start_price),
    CONSTRAINT check_current_bid CHECK (current_bid >= start_price)
);

-- Add indexes for auctions
CREATE INDEX IF NOT EXISTS idx_auctions_seller_id ON gameplay.auctions(seller_id);
CREATE INDEX IF NOT EXISTS idx_auctions_status ON gameplay.auctions(status);
CREATE INDEX IF NOT EXISTS idx_auctions_expires_at ON gameplay.auctions(expires_at);
CREATE INDEX IF NOT EXISTS idx_auctions_current_bid ON gameplay.auctions(current_bid);

-- Crafting recipes table
CREATE TABLE IF NOT EXISTS gameplay.crafting_recipes (
    recipe_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    result_item_id VARCHAR(255) NOT NULL,
    result_quantity INTEGER NOT NULL DEFAULT 1 CHECK (result_quantity > 0),
    crafting_time INTEGER NOT NULL DEFAULT 30 CHECK (crafting_time > 0), -- seconds
    skill_required VARCHAR(50) NOT NULL DEFAULT 'basic',
    difficulty VARCHAR(20) NOT NULL DEFAULT 'easy' CHECK (difficulty IN ('easy', 'medium', 'hard', 'expert')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Crafting recipe ingredients table
CREATE TABLE IF NOT EXISTS gameplay.crafting_recipe_ingredients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    recipe_id UUID NOT NULL,
    ingredient_item_id VARCHAR(255) NOT NULL,
    quantity_required INTEGER NOT NULL CHECK (quantity_required > 0),

    CONSTRAINT fk_recipe_ingredients_recipe FOREIGN KEY (recipe_id)
        REFERENCES gameplay.crafting_recipes(recipe_id) ON DELETE CASCADE
);

-- Add indexes for crafting
CREATE INDEX IF NOT EXISTS idx_crafting_recipes_skill ON gameplay.crafting_recipes(skill_required);
CREATE INDEX IF NOT EXISTS idx_crafting_recipes_difficulty ON gameplay.crafting_recipes(difficulty);
CREATE INDEX IF NOT EXISTS idx_recipe_ingredients_recipe_id ON gameplay.crafting_recipe_ingredients(recipe_id);

-- Transaction history table (enhanced)
CREATE TABLE IF NOT EXISTS gameplay.transaction_history (
    transaction_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id VARCHAR(255) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL CHECK (transaction_type IN ('purchase', 'sale', 'crafting', 'auction_win', 'auction_bid', 'reward', 'penalty')),
    amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    currency_type VARCHAR(20) NOT NULL DEFAULT 'eurodollars' CHECK (currency_type IN ('eurodollars', 'cryptocurrency', 'reputation')),
    description TEXT,
    related_trade_id UUID,
    related_auction_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_trade FOREIGN KEY (related_trade_id)
        REFERENCES gameplay.active_trades(trade_id) ON DELETE SET NULL,
    CONSTRAINT fk_transaction_auction FOREIGN KEY (related_auction_id)
        REFERENCES gameplay.auctions(id) ON DELETE SET NULL
);

-- Add indexes for transaction history
CREATE INDEX IF NOT EXISTS idx_transaction_history_player_id ON gameplay.transaction_history(player_id);
CREATE INDEX IF NOT EXISTS idx_transaction_history_type ON gameplay.transaction_history(transaction_type);
CREATE INDEX IF NOT EXISTS idx_transaction_history_created_at ON gameplay.transaction_history(created_at);

-- Market price history table
CREATE TABLE IF NOT EXISTS gameplay.market_price_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    item_id VARCHAR(255) NOT NULL,
    average_price DECIMAL(15,2) NOT NULL,
    min_price DECIMAL(15,2) NOT NULL,
    max_price DECIMAL(15,2) NOT NULL,
    trade_count INTEGER NOT NULL,
    time_window VARCHAR(10) NOT NULL, -- '1h', '6h', '24h', '7d', '30d'
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Add indexes for market price history
CREATE INDEX IF NOT EXISTS idx_market_price_item_id ON gameplay.market_price_history(item_id);
CREATE INDEX IF NOT EXISTS idx_market_price_time_window ON gameplay.market_price_history(time_window);
CREATE INDEX IF NOT EXISTS idx_market_price_recorded_at ON gameplay.market_price_history(recorded_at);

-- Player wallets table (if not exists)
CREATE TABLE IF NOT EXISTS gameplay.player_wallets (
    player_id VARCHAR(255) PRIMARY KEY,
    eurodollars DECIMAL(15,2) NOT NULL DEFAULT 1000.00 CHECK (eurodollars >= 0),
    cryptocurrency DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (cryptocurrency >= 0),
    reputation_points INTEGER NOT NULL DEFAULT 0 CHECK (reputation_points >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Character inventory table
CREATE TABLE IF NOT EXISTS gameplay.character_inventory (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id VARCHAR(255) NOT NULL,
    item_id VARCHAR(255) NOT NULL,
    name VARCHAR(200) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity >= 0),
    item_type VARCHAR(50) NOT NULL DEFAULT 'consumable',
    rarity VARCHAR(20) NOT NULL DEFAULT 'common' CHECK (rarity IN ('common', 'uncommon', 'rare', 'epic', 'legendary')),
    value DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(character_id, item_id)
);

-- Add indexes for character inventory
CREATE INDEX IF NOT EXISTS idx_character_inventory_character_id ON gameplay.character_inventory(character_id);
CREATE INDEX IF NOT EXISTS idx_character_inventory_item_type ON gameplay.character_inventory(item_type);
CREATE INDEX IF NOT EXISTS idx_character_inventory_rarity ON gameplay.character_inventory(rarity);

-- Triggers for updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER IF NOT EXISTS update_auctions_updated_at
    BEFORE UPDATE ON gameplay.auctions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER IF NOT EXISTS update_player_wallets_updated_at
    BEFORE UPDATE ON gameplay.player_wallets
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER IF NOT EXISTS update_character_inventory_updated_at
    BEFORE UPDATE ON gameplay.character_inventory
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.auction_bids TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.auctions TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.crafting_recipes TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.crafting_recipe_ingredients TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.transaction_history TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.market_price_history TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.player_wallets TO necpgame_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON gameplay.character_inventory TO necpgame_app;

-- Insert some sample crafting recipes
INSERT INTO gameplay.crafting_recipes (recipe_id, name, description, result_item_id, result_quantity, crafting_time, skill_required, difficulty) VALUES
    (gen_random_uuid(), 'Basic Health Potion', 'A simple healing potion', 'health_potion_basic', 1, 30, 'alchemy', 'easy'),
    (gen_random_uuid(), 'Iron Sword', 'A sturdy iron sword', 'sword_iron', 1, 120, 'blacksmith', 'medium'),
    (gen_random_uuid(), 'Mana Crystal', 'A crystal that restores mana', 'mana_crystal', 1, 45, 'enchanting', 'easy'),
    (gen_random_uuid(), 'Leather Armor', 'Basic leather protection', 'armor_leather', 1, 90, 'tailoring', 'medium')
ON CONFLICT DO NOTHING;

-- Insert sample recipe ingredients
INSERT INTO gameplay.crafting_recipe_ingredients (recipe_id, ingredient_item_id, quantity_required)
SELECT r.recipe_id, 'herb_green', 2 FROM gameplay.crafting_recipes r WHERE r.name = 'Basic Health Potion'
UNION ALL
SELECT r.recipe_id, 'iron_ore', 3 FROM gameplay.crafting_recipes r WHERE r.name = 'Iron Sword'
UNION ALL
SELECT r.recipe_id, 'crystal_shard', 1 FROM gameplay.crafting_recipes r WHERE r.name = 'Mana Crystal'
UNION ALL
SELECT r.recipe_id, 'leather_hide', 4 FROM gameplay.crafting_recipes r WHERE r.name = 'Leather Armor'
ON CONFLICT DO NOTHING;

COMMIT;

--rollback DROP TABLE IF EXISTS gameplay.auction_bids;
--rollback DROP TABLE IF EXISTS gameplay.auctions;
--rollback DROP TABLE IF EXISTS gameplay.crafting_recipes;
--rollback DROP TABLE IF EXISTS gameplay.crafting_recipe_ingredients;
--rollback DROP TABLE IF EXISTS gameplay.transaction_history;
--rollback DROP TABLE IF EXISTS gameplay.market_price_history;
--rollback DROP TABLE IF EXISTS gameplay.player_wallets;
--rollback DROP TABLE IF EXISTS gameplay.character_inventory;
