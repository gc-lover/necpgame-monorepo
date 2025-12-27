-- Achievement System Sample Data Migration
-- Inserts sample achievements for testing and development

-- Insert sample players
INSERT INTO players (id, username, email, created_at) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'cyber_runner', 'cyber@example.com', CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440001', 'neon_hacker', 'neon@example.com', CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440002', 'street_samurai', 'samurai@example.com', CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440003', 'data_thief', 'thief@example.com', CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440004', 'net_runner', 'runner@example.com', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insert sample achievements
INSERT INTO achievements (id, name, description, category, icon_url, points, rarity, is_hidden, is_active, created_at, updated_at) VALUES
    -- Combat achievements
    ('550e8400-e29b-41d4-a716-446655440010', 'First Blood', 'Win your first combat encounter', 'combat', '/icons/combat/first_blood.png', 10, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440011', 'Combat Veteran', 'Win 100 combat encounters', 'combat', '/icons/combat/veteran.png', 250, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440012', 'Legendary Warrior', 'Win 1000 combat encounters', 'combat', '/icons/combat/legendary.png', 1000, 'legendary', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Quest achievements
    ('550e8400-e29b-41d4-a716-446655440020', 'Quest Beginner', 'Complete your first quest', 'quest', '/icons/quest/beginner.png', 25, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440021', 'Quest Master', 'Complete 50 quests', 'quest', '/icons/quest/master.png', 500, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Level achievements
    ('550e8400-e29b-41d4-a716-446655440030', 'Level 10', 'Reach level 10', 'progression', '/icons/level/level10.png', 50, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440031', 'Level 25', 'Reach level 25', 'progression', '/icons/level/level25.png', 150, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440032', 'Level 50', 'Reach level 50', 'progression', '/icons/level/level50.png', 500, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440033', 'Max Level', 'Reach the maximum level', 'progression', '/icons/level/max.png', 2000, 'legendary', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Collection achievements
    ('550e8400-e29b-41d4-a716-446655440040', 'Item Collector', 'Collect 100 different items', 'collection', '/icons/collection/collector.png', 100, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440041', 'Rare Item Hunter', 'Collect 10 rare items', 'collection', '/icons/collection/rare_hunter.png', 300, 'rare', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Social achievements
    ('550e8400-e29b-41d4-a716-446655440050', 'Social Butterfly', 'Add 10 friends', 'social', '/icons/social/butterfly.png', 75, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440051', 'Guild Leader', 'Create or lead a guild', 'social', '/icons/social/guild_leader.png', 400, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Exploration achievements
    ('550e8400-e29b-41d4-a716-446655440060', 'Explorer', 'Discover 50 locations', 'exploration', '/icons/exploration/explorer.png', 125, 'uncommon', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440061', 'Hidden Secrets', 'Find 10 hidden locations', 'exploration', '/icons/exploration/secrets.png', 350, 'rare', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Cyberware achievements
    ('550e8400-e29b-41d4-a716-446655440070', 'Cybernetic', 'Install your first cyberware implant', 'cyberware', '/icons/cyberware/first_implant.png', 60, 'common', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440071', 'Full Borg', 'Install 10 different cyberware implants', 'cyberware', '/icons/cyberware/full_borg.png', 750, 'epic', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    -- Hidden achievements
    ('550e8400-e29b-41d4-a716-446655440080', 'Ghost in the Machine', '???', 'special', '/icons/special/ghost.png', 999, 'legendary', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440081', 'Speedrunner', 'Complete the game in under 10 hours', 'special', '/icons/special/speedrunner.png', 1500, 'legendary', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- Insert sample achievement progress
INSERT INTO achievement_progress (id, player_id, achievement_id, progress, max_progress, is_completed, completed_at, created_at, updated_at) VALUES
    -- Cyber runner progress
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440010', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440020', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440030', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '28 days', CURRENT_TIMESTAMP - INTERVAL '28 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440011', 75, 100, false, NULL, CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),

    -- Neon hacker progress
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440010', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440011', 100, 100, true, CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '15 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440031', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days'),

    -- Street samurai progress
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440010', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '35 days', CURRENT_TIMESTAMP - INTERVAL '35 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440020', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '34 days', CURRENT_TIMESTAMP - INTERVAL '34 days', CURRENT_TIMESTAMP - INTERVAL '34 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440030', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP - INTERVAL '33 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440031', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440032', 1, 1, true, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days')
ON CONFLICT (player_id, achievement_id) DO NOTHING;

-- Insert sample unlocked achievements
INSERT INTO player_achievements (id, player_id, achievement_id, unlocked_at, points_earned, rewards) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440010',
     CURRENT_TIMESTAMP - INTERVAL '30 days', 10,
     '[{"type": "currency", "id": "credits", "amount": 100}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440020',
     CURRENT_TIMESTAMP - INTERVAL '29 days', 25,
     '[{"type": "currency", "id": "credits", "amount": 250}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440030',
     CURRENT_TIMESTAMP - INTERVAL '28 days', 50,
     '[{"type": "currency", "id": "credits", "amount": 500}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440010',
     CURRENT_TIMESTAMP - INTERVAL '25 days', 10,
     '[{"type": "currency", "id": "credits", "amount": 100}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440011',
     CURRENT_TIMESTAMP - INTERVAL '15 days', 250,
     '[{"type": "currency", "id": "credits", "amount": 2500}, {"type": "item", "id": "rare_cosmetic", "amount": 1}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440031',
     CURRENT_TIMESTAMP - INTERVAL '10 days', 150,
     '[{"type": "currency", "id": "credits", "amount": 1500}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440010',
     CURRENT_TIMESTAMP - INTERVAL '35 days', 10,
     '[{"type": "currency", "id": "credits", "amount": 100}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440020',
     CURRENT_TIMESTAMP - INTERVAL '34 days', 25,
     '[{"type": "currency", "id": "credits", "amount": 250}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440030',
     CURRENT_TIMESTAMP - INTERVAL '33 days', 50,
     '[{"type": "currency", "id": "credits", "amount": 500}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440031',
     CURRENT_TIMESTAMP - INTERVAL '25 days', 150,
     '[{"type": "currency", "id": "credits", "amount": 1500}]'::jsonb),

    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440032',
     CURRENT_TIMESTAMP - INTERVAL '5 days', 500,
     '[{"type": "currency", "id": "credits", "amount": 5000}, {"type": "title", "id": "elite", "amount": 1}]'::jsonb)
ON CONFLICT (player_id, achievement_id) DO NOTHING;

-- Insert sample achievement events
INSERT INTO achievement_events (id, type, player_id, data, timestamp) VALUES
    (uuid_generate_v4(), 'combat_win', '550e8400-e29b-41d4-a716-446655440000',
     '{"enemy_type": "ganger", "damage_dealt": 150, "location": "downtown"}'::jsonb,
     CURRENT_TIMESTAMP - INTERVAL '2 hours'),

    (uuid_generate_v4(), 'quest_complete', '550e8400-e29b-41d4-a716-446655440000',
     '{"quest_id": "tutorial_01", "quest_name": "Welcome to Night City", "rewards": ["100 credits", "basic_weapon"]}'::jsonb,
     CURRENT_TIMESTAMP - INTERVAL '1 hour'),

    (uuid_generate_v4(), 'level_up', '550e8400-e29b-41d4-a716-446655440001',
     '{"old_level": 9, "new_level": 10, "xp_gained": 500}'::jsonb,
     CURRENT_TIMESTAMP - INTERVAL '30 minutes'),

    (uuid_generate_v4(), 'item_collect', '550e8400-e29b-41d4-a716-446655440002',
     '{"item_id": "rare_weapon_part", "item_name": "Plasma Capacitor", "rarity": "rare"}'::jsonb,
     CURRENT_TIMESTAMP - INTERVAL '15 minutes')
ON CONFLICT DO NOTHING;
