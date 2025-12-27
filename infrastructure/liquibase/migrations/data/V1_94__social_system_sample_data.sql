-- Social System Sample Data Migration
-- Inserts sample social data for testing and development

-- Insert friendships
INSERT INTO friendships (id, requester_id, addressee_id, status, requested_at, accepted_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', 'accepted', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002', 'accepted', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '24 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', 'accepted', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP - INTERVAL '19 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 'accepted', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '14 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440004', 'pending', CURRENT_TIMESTAMP - INTERVAL '5 days', NULL),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'blocked', CURRENT_TIMESTAMP - INTERVAL '10 days', NULL)
ON CONFLICT (requester_id, addressee_id) DO NOTHING;

-- Insert player relationships
INSERT INTO player_relationships (id, player_id, target_player_id, relationship_type, trust_level, reputation_score, interaction_count, last_interaction, notes) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', 'friend', 85, 150, 25, CURRENT_TIMESTAMP - INTERVAL '2 hours', 'Reliable raid partner'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002', 'ally', 60, 75, 12, CURRENT_TIMESTAMP - INTERVAL '1 day', 'Good for trading'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003', 'mentor', 90, 200, 40, CURRENT_TIMESTAMP - INTERVAL '6 hours', 'Excellent teacher'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440003', 'rival', -30, -50, 8, CURRENT_TIMESTAMP - INTERVAL '3 days', 'Competitive but fair'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000', 'mentee', 70, 100, 15, CURRENT_TIMESTAMP - INTERVAL '12 hours', 'Quick learner'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'enemy', -80, -120, 3, CURRENT_TIMESTAMP - INTERVAL '1 week', 'Untrustworthy')
ON CONFLICT (player_id, target_player_id, relationship_type) DO NOTHING;

-- Insert social circles
INSERT INTO social_circles (id, name, description, creator_id, circle_type, max_members, is_private, join_code) VALUES
    (uuid_generate_v4(), 'Night City Explorers', 'For players who love exploring the neon-lit streets of Night City', '550e8400-e29b-41d4-a716-446655440000', 'interest', 20, false, NULL),
    (uuid_generate_v4(), 'Raid Veterans', 'Experienced raiders sharing tactics and strategies', '550e8400-e29b-41d4-a716-446655440001', 'activity', 15, true, 'RAID2024'),
    (uuid_generate_v4(), 'Market Traders', 'Discuss trading strategies and market trends', '550e8400-e29b-41d4-a716-446655440003', 'interest', 25, false, NULL),
    (uuid_generate_v4(), 'New Player Hub', 'Help and guidance for newcomers to the city', '550e8400-e29b-41d4-a716-446655440004', 'activity', 30, false, NULL)
ON CONFLICT DO NOTHING;

-- Insert social circle members
INSERT INTO social_circle_members (id, circle_id, player_id, role, joined_at) VALUES
    -- Night City Explorers
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Night City Explorers' LIMIT 1), '550e8400-e29b-41d4-a716-446655440000', 'creator', CURRENT_TIMESTAMP - INTERVAL '20 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Night City Explorers' LIMIT 1), '550e8400-e29b-41d4-a716-446655440002', 'member', CURRENT_TIMESTAMP - INTERVAL '18 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Night City Explorers' LIMIT 1), '550e8400-e29b-41d4-a716-446655440004', 'member', CURRENT_TIMESTAMP - INTERVAL '15 days'),

    -- Raid Veterans
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Raid Veterans' LIMIT 1), '550e8400-e29b-41d4-a716-446655440001', 'creator', CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Raid Veterans' LIMIT 1), '550e8400-e29b-41d4-a716-446655440000', 'moderator', CURRENT_TIMESTAMP - INTERVAL '24 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Raid Veterans' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 'member', CURRENT_TIMESTAMP - INTERVAL '22 days'),

    -- Market Traders
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Market Traders' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 'creator', CURRENT_TIMESTAMP - INTERVAL '15 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'Market Traders' LIMIT 1), '550e8400-e29b-41d4-a716-446655440001', 'member', CURRENT_TIMESTAMP - INTERVAL '12 days'),

    -- New Player Hub
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'New Player Hub' LIMIT 1), '550e8400-e29b-41d4-a716-446655440004', 'creator', CURRENT_TIMESTAMP - INTERVAL '10 days'),
    (uuid_generate_v4(), (SELECT id FROM social_circles WHERE name = 'New Player Hub' LIMIT 1), '550e8400-e29b-41d4-a716-446655440002', 'moderator', CURRENT_TIMESTAMP - INTERVAL '8 days')
ON CONFLICT (circle_id, player_id) DO NOTHING;

-- Insert social events
INSERT INTO social_events (id, title, description, organizer_id, event_type, location_type, location_details, max_participants, starts_at, ends_at, is_public) VALUES
    (uuid_generate_v4(), 'Night City Downtown Tour', 'Explore the neon-lit streets and hidden gems of downtown Night City', '550e8400-e29b-41d4-a716-446655440000', 'casual', 'virtual', '{"coordinates": {"x": 1234, "y": 5678}, "district": "downtown"}'::jsonb, 10, CURRENT_TIMESTAMP + INTERVAL '2 days', CURRENT_TIMESTAMP + INTERVAL '2 days 3 hours', true),
    (uuid_generate_v4(), 'Arasaka Tower Raid', 'Coordinated assault on Arasaka Tower - experienced raiders only', '550e8400-e29b-41d4-a716-446655440001', 'competitive', 'virtual', '{"coordinates": {"x": 8901, "y": 2345}, "district": "corporate_plaza"}'::jsonb, 8, CURRENT_TIMESTAMP + INTERVAL '5 days', CURRENT_TIMESTAMP + INTERVAL '5 days 4 hours', false),
    (uuid_generate_v4(), 'Market Trading Meetup', 'Discuss current market trends and trading strategies', '550e8400-e29b-41d4-a716-446655440003', 'educational', 'virtual', '{"platform": "market_hall", "channel": "trading_discussion"}'::jsonb, 20, CURRENT_TIMESTAMP + INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '1 day 2 hours', true),
    (uuid_generate_v4(), 'New Player Welcome Session', 'Help newcomers get started in Night City', '550e8400-e29b-41d4-a716-446655440004', 'educational', 'virtual', '{"platform": "community_center", "channel": "newcomers"}'::jsonb, 15, CURRENT_TIMESTAMP + INTERVAL '3 days', CURRENT_TIMESTAMP + INTERVAL '3 days 1 hour', true)
ON CONFLICT DO NOTHING;

-- Insert social event participants
INSERT INTO social_event_participants (id, event_id, player_id, status, registered_at) VALUES
    -- Night City Downtown Tour
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Night City Downtown Tour' LIMIT 1), '550e8400-e29b-41d4-a716-446655440000', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Night City Downtown Tour' LIMIT 1), '550e8400-e29b-41d4-a716-446655440002', 'registered', CURRENT_TIMESTAMP - INTERVAL '12 hours'),

    -- Arasaka Tower Raid
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Arasaka Tower Raid' LIMIT 1), '550e8400-e29b-41d4-a716-446655440001', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Arasaka Tower Raid' LIMIT 1), '550e8400-e29b-41d4-a716-446655440000', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Arasaka Tower Raid' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 'registered', CURRENT_TIMESTAMP - INTERVAL '1 day'),

    -- Market Trading Meetup
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Market Trading Meetup' LIMIT 1), '550e8400-e29b-41d4-a716-446655440003', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'Market Trading Meetup' LIMIT 1), '550e8400-e29b-41d4-a716-446655440001', 'registered', CURRENT_TIMESTAMP - INTERVAL '3 hours'),

    -- New Player Welcome Session
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'New Player Welcome Session' LIMIT 1), '550e8400-e29b-41d4-a716-446655440004', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '18 hours'),
    (uuid_generate_v4(), (SELECT id FROM social_events WHERE title = 'New Player Welcome Session' LIMIT 1), '550e8400-e29b-41d4-a716-446655440002', 'confirmed', CURRENT_TIMESTAMP - INTERVAL '12 hours')
ON CONFLICT (event_id, player_id) DO NOTHING;

-- Insert social messages
INSERT INTO social_messages (id, sender_id, recipient_id, message_type, content, is_read, sent_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440001', 'text', 'Hey, ready for the raid tomorrow?', true, CURRENT_TIMESTAMP - INTERVAL '4 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', 'text', 'Absolutely! I''ve got the new Katana ready.', true, CURRENT_TIMESTAMP - INTERVAL '3 hours 45 minutes'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440001', 'text', 'Market prices are volatile today. Watch your investments!', false, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440004', 'text', 'Thanks for the help getting started! Really appreciate it.', true, CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440002', 'text', 'No problem! Come join our newcomer session tomorrow.', false, CURRENT_TIMESTAMP - INTERVAL '30 minutes'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440004', 'invitation', 'You are invited to join the Night City Explorers circle!', false, CURRENT_TIMESTAMP - INTERVAL '5 days')
ON CONFLICT DO NOTHING;

-- Insert player reputation
INSERT INTO player_reputation (id, player_id, reputation_type, reputation_score) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'combat', 150),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'trading', 80),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'social', 120),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'combat', 200),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'leadership', 175),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'social', 90),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'exploration', 100),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'social', 85),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'trading', 180),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'combat', -50),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', 'social', 95),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', 'teaching', 110)
ON CONFLICT (player_id, reputation_type) DO NOTHING;

-- Insert social achievements
INSERT INTO social_achievements (id, player_id, achievement_type, achievement_data, unlocked_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'first_friend', '{"friend_count": 1}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'social_butterfly', '{"friend_count": 10}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '15 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'event_organizer', '{"events_created": 5}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '20 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'raid_leader', '{"successful_raids": 25}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '10 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'master_trader', '{"successful_trades": 100}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', 'community_helper', '{"players_helped": 15}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '12 days')
ON CONFLICT DO NOTHING;

-- Insert social activity log
INSERT INTO social_activity_log (id, player_id, activity_type, activity_data, created_at) VALUES
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'friend_request_sent', '{"target_player": "neon_hacker"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440000', 'friend_request_accepted', '{"target_player": "neon_hacker"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440001', 'event_created', '{"event_title": "Arasaka Tower Raid"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440003', 'message_sent', '{"recipient": "cyber_runner", "message_length": 45}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440004', 'circle_created', '{"circle_name": "New Player Hub"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '10 days'),
    (uuid_generate_v4(), '550e8400-e29b-41d4-a716-446655440002', 'circle_joined', '{"circle_name": "Night City Explorers"}'::jsonb, CURRENT_TIMESTAMP - INTERVAL '18 days')
ON CONFLICT DO NOTHING;
