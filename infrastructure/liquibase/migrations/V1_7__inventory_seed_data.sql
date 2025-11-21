INSERT INTO mvp_core.item_templates (id, name, type, rarity, max_stack_size, weight, can_equip, equip_slot, requirements, stats, metadata) VALUES
('weapon_pistol_mk1', 'Pistol Mk1', 'weapon', 'common', 1, 1.5, true, 'primary', '{}', '{"damage": 20, "fire_rate": 300, "range": 50}'::jsonb, '{"description": "Standard issue pistol"}'::jsonb),
('weapon_rifle_mk1', 'Assault Rifle Mk1', 'weapon', 'common', 1, 3.2, true, 'primary', '{}', '{"damage": 35, "fire_rate": 600, "range": 100}'::jsonb, '{"description": "Standard assault rifle"}'::jsonb),
('ammo_pistol', 'Pistol Ammo', 'ammo', 'common', 200, 0.1, false, NULL, '{}', '{}', '{"description": "9mm ammunition"}'::jsonb),
('ammo_rifle', 'Rifle Ammo', 'ammo', 'common', 200, 0.15, false, NULL, '{}', '{}', '{"description": "5.56mm ammunition"}'::jsonb),
('health_pack', 'Health Pack', 'consumable', 'common', 10, 0.5, false, NULL, '{}', '{"heal_amount": 50}'::jsonb, '{"description": "Restores 50 HP"}'::jsonb),
('armor_vest_mk1', 'Armor Vest Mk1', 'armor', 'common', 1, 2.0, true, 'chest', '{}', '{"defense": 15}'::jsonb, '{"description": "Basic protection vest"}'::jsonb),
('helmet_mk1', 'Helmet Mk1', 'armor', 'common', 1, 0.8, true, 'head', '{}', '{"defense": 10}'::jsonb, '{"description": "Basic protection helmet"}'::jsonb)
ON CONFLICT (id) DO NOTHING;
