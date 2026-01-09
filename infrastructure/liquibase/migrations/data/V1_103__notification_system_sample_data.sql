-- Notification System Sample Data Migration
-- Inserts sample data for testing the notification system

-- Insert sample notifications for testing
INSERT INTO notifications (id, player_id, type, title, message, data, is_read, created_at, expires_at, priority) VALUES
-- System welcome notification
('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000',
 'system', 'Welcome to Night City!', 'Welcome to Night City! Your journey begins now. Remember, the streets are always watching.',
 '{"welcome_bonus": 1000}', false, NOW(), NOW() + INTERVAL '30 days', 'high'),

-- Achievement notification
('550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000',
 'achievement', 'First Steps Unlocked!', 'Congratulations! You have unlocked the "First Steps" achievement.',
 '{"achievement_id": "first_steps", "points": 10}', false, NOW() - INTERVAL '2 hours', NULL, 'normal'),

-- Quest notification
('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000',
 'quest', 'New Quest Available', 'A new quest "Street Justice" is now available in your quest log.',
 '{"quest_id": "street_justice", "location": "Watson"}', false, NOW() - INTERVAL '1 hour', NULL, 'normal'),

-- Social notification
('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000',
 'social', 'Friend Request', 'Player "GhostRunner" has sent you a friend request.',
 '{"sender_id": "550e8400-e29b-41d4-a716-446655440010", "sender_name": "GhostRunner"}', false, NOW() - INTERVAL '30 minutes', NULL, 'normal'),

-- Combat notification
('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440000',
 'combat', 'Combat Achievement', 'You have defeated 10 enemies in a single session!',
 '{"enemies_defeated": 10, "session_id": "combat_001"}', true, NOW() - INTERVAL '4 hours', NULL, 'normal'),

-- Economy notification
('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440000',
 'economy', 'Market Update', 'The price of Arasaka shares has increased by 15%.',
 '{"stock_symbol": "ARASAKA", "price_change": 15.0}', false, NOW() - INTERVAL '15 minutes', NULL, 'low'),

-- Event notification with expiration
('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440000',
 'event', 'Limited Time Event', 'Join the "Night City Grand Prix" event! Limited time only.',
 '{"event_id": "grand_prix", "rewards": ["rare_car", "cyberware_parts"]}', false, NOW(), NOW() + INTERVAL '7 days', 'urgent'),

-- Already read notification
('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440000',
 'system', 'Daily Login Bonus', 'You have received your daily login bonus of 500 eddies.',
 '{"bonus_amount": 500, "currency": "eddies"}', true, NOW() - INTERVAL '1 day', NULL, 'low'),

-- Expired notification (for testing cleanup)
('550e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440000',
 'event', 'Expired Event', 'This event has expired.',
 '{"event_id": "expired_test"}', false, NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 'normal');

-- Insert notifications for another player
INSERT INTO notifications (id, player_id, type, title, message, data, is_read, created_at, priority) VALUES
('550e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440010',
 'system', 'Welcome to Night City!', 'Welcome to Night City! Your journey begins now.',
 '{"welcome_bonus": 1000}', false, NOW(), 'high'),

('550e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440010',
 'achievement', 'First Kill', 'You have made your first kill in Night City.',
 '{"achievement_id": "first_kill", "points": 25}', false, NOW() - INTERVAL '3 hours', 'high');






















