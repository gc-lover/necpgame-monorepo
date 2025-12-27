-- Economy System Sample Data Migration
-- Inserts sample economic data for testing and development

-- Insert player wallets for existing players
INSERT INTO player_wallets (id, player_id, currency_type, balance, reserved_balance, last_transaction_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'credits', 50000.00, 1000.00, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'premium_currency', 250.00, 0.00, CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'credits', 75000.00, 5000.00, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'premium_currency', 500.00, 50.00, CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'credits', 30000.00, 0.00, CURRENT_TIMESTAMP - INTERVAL '30 minutes'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'premium_currency', 100.00, 0.00, CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'credits', 100000.00, 25000.00, CURRENT_TIMESTAMP - INTERVAL '15 minutes'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', 'credits', 25000.00, 1000.00, CURRENT_TIMESTAMP - INTERVAL '45 minutes')
ON CONFLICT (player_id, currency_type) DO NOTHING;

-- Insert sample economic transactions
INSERT INTO economic_transactions (id, transaction_id, player_id, transaction_type, currency_type, amount, balance_before, balance_after, description, metadata, processed_at) VALUES
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'reward', 'credits', 10000.00, 40000.00, 50000.00, 'Quest completion reward', '{"quest_id": "tutorial_01", "source": "quest_system"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'purchase', 'credits', -2500.00, 50000.00, 47500.00, 'Item purchase: Health Injector', '{"item_id": "health_injector", "quantity": 5}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'sale', 'credits', 1500.00, 47500.00, 49000.00, 'Item sale: Circuit Board', '{"item_id": "circuit_board", "quantity": 10}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '45 minutes'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'reward', 'premium_currency', 100.00, 400.00, 500.00, 'Achievement unlocked', '{"achievement": "First Blood"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'transfer', 'credits', -5000.00, 80000.00, 75000.00, 'Guild contribution', '{"guild_id": "cyber_nomads", "recipient": "guild_bank"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'purchase', 'premium_currency', -25.00, 125.00, 100.00, 'Cosmetic purchase', '{"item_id": "neon_jacket_skin"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (uuid_generate_v4(), uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'auction_win', 'credits', -75000.00, 175000.00, 100000.00, 'Auction won: Legendary Katana', '{"auction_id": "legendary_katana_001", "item_id": "katana_goliath"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '15 minutes')
ON CONFLICT DO NOTHING;

-- Insert market listings
INSERT INTO market_listings (id, seller_id, item_id, quantity, price_per_unit, currency_type, listing_type, status, expires_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440231', 5, 1000.00, 'credits', 'sell', 'active', CURRENT_TIMESTAMP + INTERVAL '24 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440222', 2, 3000.00, 'credits', 'sell', 'active', CURRENT_TIMESTAMP + INTERVAL '12 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440230', 20, 60.00, 'credits', 'sell', 'active', CURRENT_TIMESTAMP + INTERVAL '48 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440202', 1, 12000.00, 'credits', 'sell', 'active', CURRENT_TIMESTAMP + INTERVAL '6 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440212', 1, 3000.00, 'credits', 'sell', 'active', CURRENT_TIMESTAMP + INTERVAL '36 hours')
ON CONFLICT DO NOTHING;

-- Insert trade orders
INSERT INTO trade_orders (id, player_id, order_type, item_id, quantity, price_per_unit, currency_type, order_status, filled_quantity, expires_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'buy', '550e8400-e29b-41d4-a716-446655440231', 10, 800.00, 'credits', 'open', 0, CURRENT_TIMESTAMP + INTERVAL '24 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'sell', '550e8400-e29b-41d4-a716-446655440222', 5, 2500.00, 'credits', 'partial', 2, CURRENT_TIMESTAMP + INTERVAL '18 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'buy', '550e8400-e29b-41d4-a716-446655440230', 50, 50.00, 'credits', 'open', 0, CURRENT_TIMESTAMP + INTERVAL '72 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'sell', '550e8400-e29b-41d4-a716-446655440201', 1, 150000.00, 'credits', 'open', 0, CURRENT_TIMESTAMP + INTERVAL '168 hours')
ON CONFLICT DO NOTHING;

-- Insert auctions
INSERT INTO auctions (id, seller_id, item_id, starting_price, current_bid, buyout_price, currency_type, current_bidder_id, bid_count, status, ends_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440211', 25000.00, 35000.00, 50000.00, 'credits', '550e8400-e29b-41d4-a716-446655440003', 3, 'active', CURRENT_TIMESTAMP + INTERVAL '2 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440240', 5000.00, NULL, 10000.00, 'credits', NULL, 0, 'active', CURRENT_TIMESTAMP + INTERVAL '6 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440202', 15000.00, 18000.00, 25000.00, 'credits', '550e8400-e29b-41d4-a716-446655440000', 2, 'active', CURRENT_TIMESTAMP + INTERVAL '4 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440231', 1500.00, 1800.00, 2500.00, 'credits', '550e8400-e29b-41d4-a716-446655440001', 1, 'active', CURRENT_TIMESTAMP + INTERVAL '8 hours')
ON CONFLICT DO NOTHING;

-- Insert auction bids
INSERT INTO auction_bids (id, auction_id, bidder_id, bid_amount, bid_time) VALUES
    (uuid_generate_v4(), (SELECT id FROM auctions WHERE seller_id = '550e8400-e29b-41d4-a716-446655440001' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 30000.00, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), (SELECT id FROM auctions WHERE seller_id = '550e8400-e29b-41d4-a716-446655440001' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 35000.00, CURRENT_TIMESTAMP - INTERVAL '30 minutes'),
    (uuid_generate_v4(), (SELECT id FROM auctions WHERE seller_id = '550e8400-e29b-41d4-a716-446655440003' LIMIT 1), '550e8400-e29b-41d4-a716-446655440000', 18000.00, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    (uuid_generate_v4(), (SELECT id FROM auctions WHERE seller_id = '550e8400-e29b-41d4-a716-446655440004' LIMIT 1), '550e8400-e29b-41d4-a716-446655440001', 1800.00, CURRENT_TIMESTAMP - INTERVAL '30 minutes')
ON CONFLICT DO NOTHING;

-- Insert currency exchange rates
INSERT INTO currency_rates (id, from_currency, to_currency, exchange_rate, effective_from, effective_until) VALUES
    (uuid_generate_v4(), 'credits', 'premium_currency', 200.00, CURRENT_TIMESTAMP - INTERVAL '30 days', NULL),
    (uuid_generate_v4(), 'premium_currency', 'credits', 0.005, CURRENT_TIMESTAMP - INTERVAL '30 days', NULL),
    (uuid_generate_v4(), 'credits', 'eurodollars', 0.01, CURRENT_TIMESTAMP - INTERVAL '30 days', NULL),
    (uuid_generate_v4(), 'eurodollars', 'credits', 100.00, CURRENT_TIMESTAMP - INTERVAL '30 days', NULL)
ON CONFLICT DO NOTHING;

-- Insert economic events
INSERT INTO economic_events (id, event_type, player_id, entity_type, entity_id, event_data, processed) VALUES
    (uuid_generate_v4(), 'wallet_updated', '550e8400-e29b-41d4-a716-446655440000', 'wallet', (SELECT id FROM player_wallets WHERE player_id = '550e8400-e29b-41d4-a716-446655440000' AND currency_type = 'credits' LIMIT 1), '{"amount": 10000, "reason": "quest_reward"}'::jsonb, true),
    (uuid_generate_v4(), 'listing_created', '550e8400-e29b-41d4-a716-446655440000', 'market_listing', (SELECT id FROM market_listings WHERE seller_id = '550e8400-e29b-41d4-a716-446655440000' LIMIT 1), '{"item_id": "rare_earth_metal", "price": 1000}'::jsonb, true),
    (uuid_generate_v4(), 'auction_bid', '550e8400-e29b-41d4-a716-446655440003', 'auction', (SELECT id FROM auctions WHERE seller_id = '550e8400-e29b-41d4-a716-446655440001' LIMIT 1), '{"bid_amount": 35000, "previous_bid": 30000}'::jsonb, true),
    (uuid_generate_v4(), 'market_price_update', NULL, 'market', NULL, '{"item_category": "weapons", "average_price_change": 5.2}'::jsonb, false)
ON CONFLICT DO NOTHING;

-- Insert economic statistics
INSERT INTO economic_statistics (id, stat_type, stat_key, stat_value, stat_date) VALUES
    (uuid_generate_v4(), 'daily_volume', 'credits', 5000000.00, CURRENT_DATE),
    (uuid_generate_v4(), 'player_wealth', '550e8400-e29b-41d4-a716-446655440000', 50000.00, CURRENT_DATE),
    (uuid_generate_v4(), 'player_wealth', '550e8400-e29b-41d4-a716-446655440001', 75000.00, CURRENT_DATE),
    (uuid_generate_v4(), 'player_wealth', '550e8400-e29b-41d4-a716-446655440003', 100000.00, CURRENT_DATE),
    (uuid_generate_v4(), 'market_activity', 'active_listings', 25.00, CURRENT_DATE),
    (uuid_generate_v4(), 'market_activity', 'active_auctions', 8.00, CURRENT_DATE)
ON CONFLICT (stat_type, stat_key, stat_date) DO NOTHING;
