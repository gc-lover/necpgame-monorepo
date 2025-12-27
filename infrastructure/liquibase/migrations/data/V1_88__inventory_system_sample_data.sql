-- Inventory System Sample Data Migration
-- Inserts sample items, equipment, and inventory data for testing

-- Insert equipment slots
INSERT INTO equipment_slots (id, name, display_name, category, is_main_hand, is_off_hand, is_two_handed, is_active) VALUES
    ('550e8400-e29b-41d4-a716-446655440100', 'head', 'Head', 'armor', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440101', 'chest', 'Chest', 'armor', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440102', 'legs', 'Legs', 'armor', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440103', 'feet', 'Feet', 'armor', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440104', 'main_hand', 'Main Hand', 'weapon', true, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440105', 'off_hand', 'Off Hand', 'weapon', false, true, false, true),
    ('550e8400-e29b-41d4-a716-446655440106', 'necklace', 'Necklace', 'accessory', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440107', 'ring1', 'Ring 1', 'accessory', false, false, false, true),
    ('550e8400-e29b-41d4-a716-446655440108', 'ring2', 'Ring 2', 'accessory', false, false, false, true)
ON CONFLICT (id) DO NOTHING;

-- Insert sample items
INSERT INTO items (id, name, description, category, item_type, rarity, icon_url, max_stack, is_tradeable, is_sellable, base_price, durability, max_durability, is_active, created_at, updated_at) VALUES
    -- Weapons
    ('550e8400-e29b-41d4-a716-446655440200', 'Militech M-10AF Lexington', 'High-end Militech assault rifle with excellent damage output', 'weapon', 'rifle', 'rare', '/icons/weapons/militech-lexington.png', 1, true, true, 25000, 100, 100, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440201', 'Katana "Goliath"', 'Legendary monofilament katana with razor-sharp edge', 'weapon', 'melee', 'legendary', '/icons/weapons/katana-goliath.png', 1, true, true, 100000, 200, 200, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440202', 'Pistol "Malorian Arms 3516"', 'Reliable sidearm for close-quarters combat', 'weapon', 'pistol', 'uncommon', '/icons/weapons/malorian-3516.png', 1, true, true, 8000, 80, 80, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Armor
    ('550e8400-e29b-41d4-a716-446655440210', 'Judy Alvarez Jacket', 'Stylish jacket with built-in armor plating', 'armor', 'chest', 'rare', '/icons/armor/judy-jacket.png', 1, true, true, 15000, 120, 120, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440211', 'Panther Helmet', 'High-tech helmet with HUD integration', 'armor', 'head', 'epic', '/icons/armor/panther-helmet.png', 1, true, true, 35000, 150, 150, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440212', 'Nomad Boots', 'Durable boots for harsh terrain', 'armor', 'feet', 'common', '/icons/armor/nomad-boots.png', 1, true, true, 2000, 100, 100, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Consumables
    ('550e8400-e29b-41d4-a716-446655440220', 'Health Booster', 'Restores 50 HP instantly', 'consumable', 'health', 'common', '/icons/consumables/health-booster.png', 10, true, true, 500, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440221', 'Stamina Booster', 'Restores 30 stamina instantly', 'consumable', 'stamina', 'common', '/icons/consumables/stamina-booster.png', 10, true, true, 300, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440222', 'Rare Health Injector', 'Restores 150 HP and grants temporary damage resistance', 'consumable', 'health', 'rare', '/icons/consumables/rare-health-injector.png', 5, true, true, 2500, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Materials
    ('550e8400-e29b-41d4-a716-446655440230', 'Circuit Board', 'Common electronic component', 'material', 'electronic', 'common', '/icons/materials/circuit-board.png', 50, true, true, 50, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440231', 'Rare Earth Metal', 'Valuable material for crafting', 'material', 'metal', 'rare', '/icons/materials/rare-earth.png', 25, true, true, 800, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Quest Items
    ('550e8400-e29b-41d4-a716-446655440240', 'Encrypted Datashard', 'Contains sensitive information', 'quest', 'datashard', 'epic', '/icons/quest/datashard.png', 1, false, false, 0, NULL, NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insert item effects
INSERT INTO item_effects (id, item_id, effect_type, effect_target, effect_value, effect_duration, is_percentage, is_active, created_at) VALUES
    -- Militech Lexington effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440200', 'stat_boost', 'damage', 25, NULL, true, true, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440200', 'stat_boost', 'accuracy', 15, NULL, false, true, CURRENT_TIMESTAMP),

    -- Katana Goliath effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440201', 'stat_boost', 'damage', 50, NULL, true, true, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440201', 'stat_boost', 'critical_chance', 20, NULL, false, true, CURRENT_TIMESTAMP),

    -- Judy Alvarez Jacket effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440210', 'stat_boost', 'armor', 30, NULL, false, true, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440210', 'stat_boost', 'charisma', 10, NULL, false, true, CURRENT_TIMESTAMP),

    -- Panther Helmet effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440211', 'stat_boost', 'armor', 40, NULL, false, true, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440211', 'ability_grant', 'scanner', 1, NULL, false, true, CURRENT_TIMESTAMP),

    -- Health Booster effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440220', 'health_restore', 'health', 50, NULL, false, true, CURRENT_TIMESTAMP),

    -- Rare Health Injector effects
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440222', 'health_restore', 'health', 150, NULL, false, true, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440222', 'stat_boost', 'damage_resistance', 25, 300, true, true, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert sample inventory data for test players
-- Using existing players from achievement system sample data
INSERT INTO player_inventories (id, player_id, item_id, quantity, slot_position, durability, is_equipped, acquired_at, updated_at) VALUES
    -- Cyber runner inventory
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440200', 1, 0, 95, true, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440210', 1, 1, 110, true, CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440220', 5, 2, NULL, false, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440221', 3, 3, NULL, false, CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440230', 25, 4, NULL, false, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP),

    -- Neon hacker inventory
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440201', 1, 0, 185, true, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440211', 1, 1, 140, true, CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440222', 2, 2, NULL, false, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440231', 10, 3, NULL, false, CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP),

    -- Street samurai inventory
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440202', 1, 0, 75, true, CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440212', 1, 1, 90, true, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440220', 8, 2, NULL, false, CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440240', 1, 3, NULL, false, CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert player equipment data
INSERT INTO player_equipment (id, player_id, inventory_item_id, equipment_slot_id, equipped_at) VALUES
    -- Cyber runner equipment
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440000' AND item_id = '550e8400-e29b-41d4-a716-446655440200' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440104', CURRENT_TIMESTAMP - INTERVAL '10 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440000' AND item_id = '550e8400-e29b-41d4-a716-446655440210' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440101', CURRENT_TIMESTAMP - INTERVAL '9 days'),

    -- Neon hacker equipment
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440001' AND item_id = '550e8400-e29b-41d4-a716-446655440201' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440104', CURRENT_TIMESTAMP - INTERVAL '12 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440001' AND item_id = '550e8400-e29b-41d4-a716-446655440211' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440100', CURRENT_TIMESTAMP - INTERVAL '11 days'),

    -- Street samurai equipment
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440002' AND item_id = '550e8400-e29b-41d4-a716-446655440202' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440104', CURRENT_TIMESTAMP - INTERVAL '15 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002',
     (SELECT id FROM player_inventories WHERE player_id = '550e8400-e29b-41d4-a716-446655440002' AND item_id = '550e8400-e29b-41d4-a716-446655440212' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440103', CURRENT_TIMESTAMP - INTERVAL '14 days')
ON CONFLICT DO NOTHING;

-- Insert some sample inventory transactions
INSERT INTO inventory_transactions (id, player_id, transaction_type, item_id, quantity, from_slot, to_slot, transaction_data, created_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'add_item', '550e8400-e29b-41d4-a716-446655440200', 1, NULL, 0,
     '{"source": "loot", "location": "downtown_combat"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '10 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'equip_item', '550e8400-e29b-41d4-a716-446655440200', 1, 0, NULL,
     '{"equipment_slot": "main_hand"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '10 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'add_item', '550e8400-e29b-41d4-a716-446655440220', 5, NULL, 2,
     '{"source": "vendor_purchase", "vendor": "ripperdoc"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '8 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'add_item', '550e8400-e29b-41d4-a716-446655440201', 1, NULL, 0,
     '{"source": "quest_reward", "quest": "phantom_liberty_main"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '12 days'),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'add_item', '550e8400-e29b-41d4-a716-446655440240', 1, NULL, 3,
     '{"source": "quest_item", "quest": "information_warfare"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '12 days')
ON CONFLICT DO NOTHING;
