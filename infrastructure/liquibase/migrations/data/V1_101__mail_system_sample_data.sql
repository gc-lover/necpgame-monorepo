-- Mail System Sample Data Migration
-- Inserts sample data for testing the mail system

-- Insert sample mails
INSERT INTO mails (id, sender_id, recipient_id, subject, category, priority, sent_at, expires_at, folder, content) VALUES
-- System welcome mail
('550e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002',
 'Welcome to Night City!', 'system', 'high', NOW(), NOW() + INTERVAL '30 days', 'inbox',
 '{"text": "Welcome to Night City! Your journey begins now. Remember, the streets are always watching.", "html": "<h1>Welcome to Night City!</h1><p>Your journey begins now. Remember, the streets are always watching.</p>", "format": "html"}'),

-- Personal mail
('550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000',
 'About that job...', 'personal', 'normal', NOW() - INTERVAL '2 hours', NOW() + INTERVAL '7 days', 'inbox',
 '{"text": "Hey, about that job we discussed. I think we should meet at the Afterlife. 10 PM tonight.", "format": "plain"}'),

-- Trade mail with attachment
('550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440000',
 'Cyberware Delivery', 'trade', 'normal', NOW() - INTERVAL '1 hour', NOW() + INTERVAL '3 days', 'inbox',
 '{"text": "Your Kerenzikov boost cyberware is ready for pickup. Payment: 15,000 eddies. Location: Misty''s clinic.", "format": "plain"}'),

-- Guild mail
('550e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440000',
 'Guild Meeting Tonight', 'guild', 'high', NOW() - INTERVAL '30 minutes', NOW() + INTERVAL '1 day', 'inbox',
 '{"text": "All guild members: Emergency meeting at the Totentanz tonight at 9 PM. Important announcement regarding the Maelstrom attack.", "format": "plain"}'),

-- Event mail
('550e8400-e29b-41d4-a716-446655440006', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000',
 'Tournament Registration Open', 'event', 'normal', NOW(), NOW() + INTERVAL '14 days', 'inbox',
 '{"text": "Registration is now open for the Night City Combat Tournament! Prize pool: 50,000 eddies. Sign up before midnight.", "html": "<h2>Tournament Registration Open!</h2><p>Registration is now open for the <strong>Night City Combat Tournament</strong>!</p><p>Prize pool: <span style=\"color: gold;\">50,000 eddies</span></p><p>Sign up before midnight.</p>", "format": "html"}'),

-- Reward mail
('550e8400-e29b-41d4-a716-446655440007', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000',
 'Daily Login Bonus', 'reward', 'low', NOW(), NOW() + INTERVAL '24 hours', 'inbox',
 '{"text": "Thanks for logging in today! Here''s your daily bonus: 500 eddies and 1 mystery box.", "format": "plain"}'),

-- Read mail
('550e8400-e29b-41d4-a716-446655440008', '550e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440000',
 'Job completed successfully', 'personal', 'normal', NOW() - INTERVAL '1 day', NOW() + INTERVAL '6 days', 'inbox',
 '{"text": "The job is done. Payment transferred to your account. Let me know if you need more work.", "format": "plain"}'),

-- Archived mail
('550e8400-e29b-41d4-a716-446655440009', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000',
 'Old system update', 'system', 'low', NOW() - INTERVAL '7 days', NOW() - INTERVAL '1 day', 'archived',
 '{"text": "System maintenance completed. All services are back online.", "format": "plain"}'),

-- Sent mail (from current user)
('550e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440002',
 'Meeting confirmation', 'personal', 'normal', NOW() - INTERVAL '3 hours', NOW() + INTERVAL '4 days', 'sent',
 '{"text": "Confirming our meeting at Afterlife tonight. Don''t be late.", "format": "plain"}');

-- Mark some mails as read
UPDATE mails SET read_at = sent_at + INTERVAL '5 minutes' WHERE id IN (
    '550e8400-e29b-41d4-a716-446655440008',
    '550e8400-e29b-41d4-a716-446655440010'
);

-- Insert sample attachments
INSERT INTO mail_attachments (id, mail_id, filename, content_type, size_bytes, data) VALUES
('660e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440004',
 'cyberware_specs.pdf', 'application/pdf', 245760,
 decode('JVBERi0xLjQKMSAwIG9iago8PC9UeXBlIC9DYXRhbG9nCi9QYWdlcyAyIDAgUgo+PgplbmRvYmoKMiAwIG9iago8PC9UeXBlIC9QYWdlcwovS2lkcyBbMyAwIFJdCi9Db3VudCAxCj4+CmVuZG9iagozIDAgb2JqCjw8L1R5cGUgL1BhZ2UKL1BhcmVudCAyIDAgUgovTWVkaWFCb3ggWzAgMCA2MTIgNzkyXQovQ29udGVudHMgNCAwIFIKPj4KZW5kb2JqCjQgMCBvYmoKPDwvTGVuZ3RoIDQ0Pj4Kc3RyZWFtCkJUCi9GMSAxMiBUZgo3MiA3MjAgVGQoSGVsbG8gV29ybGQpIFRqCkVUCmVuZHN0cmVhbQplbmRvYmoKNSAwIG9iago8PC9UeXBlIC9Gb250Ci9TdWJ0eXBlIC9UeXBlMQovQmFzZUZvbnQgL0hlbHZldGljYT4+CmVuZG9iagp4cmVmCjAgNgowMDAwMDAwMDAwIDY1NTM1IGYgCjAwMDAwMDAwMTkgMDAwMDAgbiAKMDAwMDAwMDA3MyAwMDAwMCBuIAowMDAwMDAwMTIyIDAwMDAwIG4gCjAwMDAwMDAyMzIgMDAwMDAgbiAKMDAwMDAwMDI5OCAwMDAwMCBuIAp0cmFpbGVyCjw8L1NpemUgNgovUm9vdCAxIDAgUgo+PgpzdGFydHhyZWYKNDA0CiUlRU9GCg==', 'base64')),

('660e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440006',
 'tournament_poster.png', 'image/png', 512000,
 decode('iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChAI9jU77yQAAAABJRU5ErkJggg==', 'base64'));

-- Insert sample reports
INSERT INTO mail_reports (id, mail_id, reporter_id, reason, description, severity, status) VALUES
('770e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440003',
 '550e8400-e29b-41d4-a716-446655440000', 'spam', 'This looks like spam mail', 'low', 'submitted');


















