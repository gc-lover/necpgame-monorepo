-- Guild System Sample Data Migration
-- Inserts sample guild data for testing and development

-- Insert sample guilds
INSERT INTO guilds (id, name, description, motto, leader_id, level, experience, max_members, current_members, reputation, is_recruiting, emblem_url, banner_url) VALUES
    ('550e8400-e29b-41d4-a716-446655440500', 'Cyber Nomads', 'A wandering group of digital nomads exploring the net', 'Adapt or Die', '550e8400-e29b-41d4-a716-446655440000', 5, 25000, 100, 12, 1250, true, '/guilds/cyber-nomads/emblem.png', '/guilds/cyber-nomads/banner.png'),
    ('550e8400-e29b-41d4-a716-446655440501', 'Neon Reapers', 'Elite combat guild specializing in high-risk operations', 'Strike Fast, Strike Hard', '550e8400-e29b-41d4-a716-446655440001', 8, 75000, 75, 23, 2100, true, '/guilds/neon-reapers/emblem.png', '/guilds/neon-reapers/banner.png'),
    ('550e8400-e29b-41d4-a716-446655440502', 'Data Pirates', 'Information brokers and black market traders', 'Knowledge is Power', '550e8400-e29b-41d4-a716-446655440003', 6, 35000, 50, 18, 980, false, '/guilds/data-pirates/emblem.png', '/guilds/data-pirates/banner.png'),
    ('550e8400-e29b-41d4-a716-446655440503', 'Street Preachers', 'Charismatic leaders spreading the word on the streets', 'Spread the Truth', '550e8400-e29b-41d4-a716-446655440004', 3, 8000, 30, 7, 450, true, '/guilds/street-preachers/emblem.png', '/guilds/street-preachers/banner.png'),
    ('550e8400-e29b-41d4-a716-446655440504', 'Ghost Protocol', 'Stealth operatives who prefer to stay in the shadows', 'Silent but Deadly', '550e8400-e29b-41d4-a716-446655440002', 7, 50000, 40, 15, 1675, false, '/guilds/ghost-protocol/emblem.png', '/guilds/ghost-protocol/banner.png')
ON CONFLICT (id) DO NOTHING;

-- Insert guild members
INSERT INTO guild_members (id, guild_id, player_id, role, joined_at, last_active, contribution_points) VALUES
    -- Cyber Nomads
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440000', 'leader', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '2 hours', 5000),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440002', 'officer', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 3200),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440004', 'member', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '6 hours', 1800),

    -- Neon Reapers
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440001', 'leader', CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP - INTERVAL '30 minutes', 8500),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440000', 'officer', CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '4 hours', 6200),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440003', 'officer', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '8 hours', 5800),

    -- Data Pirates
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', '550e8400-e29b-41d4-a716-446655440003', 'leader', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '1 hour', 4200),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', '550e8400-e29b-41d4-a716-446655440001', 'officer', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '12 hours', 3100),

    -- Street Preachers
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440503', '550e8400-e29b-41d4-a716-446655440004', 'leader', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '3 hours', 1200),

    -- Ghost Protocol
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440504', '550e8400-e29b-41d4-a716-446655440002', 'leader', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '45 minutes', 3800)
ON CONFLICT (guild_id, player_id) DO NOTHING;

-- Insert guild ranks (default ranks are created by trigger, but we can add custom ones)
INSERT INTO guild_ranks (id, guild_id, rank_name, rank_level, min_experience, max_experience, benefits) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'Elite Reaper', 5, 20000, 49999, '{"permissions": ["chat", "deposit_vault", "invite_members", "lead_raids"], "bonus_xp": 10}'::jsonb),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'Reaper Lord', 6, 50000, NULL, '{"permissions": ["all"], "bonus_xp": 20, "title": "Reaper Lord"}'::jsonb),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', 'Data Baron', 5, 25000, NULL, '{"permissions": ["all"], "trading_fee_discount": 15}'::jsonb)
ON CONFLICT (guild_id, rank_level) DO NOTHING;

-- Insert guild applications
INSERT INTO guild_applications (id, guild_id, applicant_id, application_text, status, created_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440001', 'I want to join your nomadic lifestyle and explore the net together.', 'pending', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440004', 'Looking to prove myself in combat. Ready for high-risk operations.', 'approved', CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', '550e8400-e29b-41d4-a716-446655440002', 'I have valuable information networks that could benefit your operations.', 'rejected', CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT (guild_id, applicant_id) DO NOTHING;

-- Insert guild events
INSERT INTO guild_events (id, guild_id, event_type, event_data, created_by, created_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', 'guild_created', '{"founder": "cyber_runner"}'::jsonb, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', 'member_joined', '{"member": "street_samurai", "role": "officer"}'::jsonb, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', 'level_up', '{"old_level": 4, "new_level": 5, "experience_gained": 5000}'::jsonb, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '20 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'raid_completed', '{"raid_name": "Arasaka Tower", "difficulty": "hard", "participants": 8}'::jsonb, '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '3 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', 'trade_completed', '{"value": 50000, "items_traded": 12}'::jsonb, '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '6 hours')
ON CONFLICT DO NOTHING;

-- Insert guild achievements
INSERT INTO guild_achievements (id, guild_id, achievement_id, unlocked_by, unlocked_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '40 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '35 days')
ON CONFLICT (guild_id, achievement_id) DO NOTHING;

-- Insert guild vaults
INSERT INTO guild_vaults (id, guild_id, vault_name, vault_level, max_slots) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', 'Main Vault', 3, 150),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'Combat Supplies', 5, 200),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'Rare Equipment', 4, 100),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', 'Information Cache', 2, 75)
ON CONFLICT (guild_id, vault_name) DO NOTHING;

-- Insert guild vault items
INSERT INTO guild_vault_items (id, vault_id, item_id, quantity, deposited_by, deposited_at) VALUES
    -- Cyber Nomads Main Vault
    (uuid_generate_v4(), (SELECT id FROM guild_vaults WHERE guild_id = '550e8400-e29b-41d4-a716-446655440500' AND vault_name = 'Main Vault' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440220', 25, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '10 days'),

    -- Neon Reapers Combat Supplies
    (uuid_generate_v4(), (SELECT id FROM guild_vaults WHERE guild_id = '550e8400-e29b-41d4-a716-446655440501' AND vault_name = 'Combat Supplies' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440222', 10, '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (uuid_generate_v4(), (SELECT id FROM guild_vaults WHERE guild_id = '550e8400-e29b-41d4-a716-446655440501' AND vault_name = 'Combat Supplies' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440231', 50, '550e8400-e29b-41d4-a716-446655440000', CURRENT_TIMESTAMP - INTERVAL '3 days'),

    -- Neon Reapers Rare Equipment
    (uuid_generate_v4(), (SELECT id FROM guild_vaults WHERE guild_id = '550e8400-e29b-41d4-a716-446655440501' AND vault_name = 'Rare Equipment' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440201', 1, '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '7 days'),

    -- Data Pirates Information Cache
    (uuid_generate_v4(), (SELECT id FROM guild_vaults WHERE guild_id = '550e8400-e29b-41d4-a716-446655440502' AND vault_name = 'Information Cache' LIMIT 1),
     '550e8400-e29b-41d4-a716-446655440240', 3, '550e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP - INTERVAL '4 days')
ON CONFLICT (vault_id, item_id) DO NOTHING;

-- Insert guild messages
INSERT INTO guild_messages (id, guild_id, sender_id, message_type, message_text, is_pinned, created_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440000', 'announcement', 'Welcome to Cyber Nomads! Remember to always adapt to the changing net.', true, CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440500', '550e8400-e29b-41d4-a716-446655440002', 'chat', 'Found some rare cyberware in the badlands. Anyone interested?', false, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440001', 'announcement', 'Raid on Arasaka Tower scheduled for tomorrow 20:00 UTC. All officers mandatory.', true, CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440000', 'chat', 'Great work on the last raid everyone! The loot distribution will be posted soon.', false, CURRENT_TIMESTAMP - INTERVAL '4 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', '550e8400-e29b-41d4-a716-446655440003', 'system', 'Guild achievement unlocked: Social Butterfly', false, CURRENT_TIMESTAMP - INTERVAL '25 days')
ON CONFLICT DO NOTHING;

-- Insert guild permissions (custom permissions beyond defaults)
INSERT INTO guild_permissions (id, guild_id, role, permission) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'officer', 'lead_raids'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440501', 'officer', 'manage_guild_bank'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', 'officer', 'trade_guild_items'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440502', 'member', 'access_information_cache')
ON CONFLICT (guild_id, role, permission) DO NOTHING;
