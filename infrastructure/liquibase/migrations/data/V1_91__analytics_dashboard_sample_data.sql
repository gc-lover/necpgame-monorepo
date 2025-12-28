-- Issue: #2264 - Analytics Dashboard Sample Data
-- liquibase formatted sql
-- changeset backend:analytics-dashboard-sample-data dbms:postgresql
-- comment: Insert sample analytics data for testing and demonstration

BEGIN;

-- Insert sample player sessions
INSERT INTO analytics.player_sessions (
    player_id, session_start, session_end, duration_seconds,
    game_mode, region, device_type, events_count
) VALUES
(
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    '2025-12-28 10:00:00+00',
    '2025-12-28 11:30:00+00',
    5400,
    'deathmatch',
    'NA',
    'desktop',
    156
),
(
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    '2025-12-28 09:15:00+00',
    '2025-12-28 10:45:00+00',
    5400,
    'team_deathmatch',
    'EU',
    'mobile',
    203
),
(
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    '2025-12-28 08:00:00+00',
    '2025-12-28 09:30:00+00',
    5400,
    'battle_royale',
    'ASIA',
    'console',
    89
);

-- Insert sample player events
INSERT INTO analytics.player_events (
    player_id, event_type, event_data, game_mode, region
) VALUES
(
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    'player_login',
    '{"login_method": "steam", "device_info": {"os": "windows", "version": "11"}}',
    'deathmatch',
    'NA'
),
(
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    'combat_kill',
    '{"weapon": "cyberpunk_pistol", "damage": 85.5, "headshot": true}',
    'deathmatch',
    'NA'
),
(
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    'item_purchase',
    '{"item_id": "health_boost", "price": 25.99, "currency": "eddies"}',
    'team_deathmatch',
    'EU'
),
(
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    'guild_join',
    '{"guild_id": "cyber_nomads", "guild_role": "member"}',
    'battle_royale',
    'ASIA'
);

-- Insert sample economic transactions
INSERT INTO analytics.economic_transactions (
    player_id, transaction_type, currency_type, amount, item_id, transaction_data
) VALUES
(
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    'purchase',
    'eddies',
    49.99,
    'premium_weapon_pack',
    '{"payment_method": "credit_card", "discount_applied": 0.1}'
),
(
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    'sale',
    'crypto',
    1250.00,
    'rare_cyberware',
    '{"marketplace_fee": 25.00, "buyer_id": "550e8400-e29b-41d4-a716-446655440003"}'
),
(
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    'reward',
    'eddies',
    100.00,
    NULL,
    '{"reward_type": "daily_login", "streak_days": 7}'
);

-- Insert sample combat matches
INSERT INTO analytics.combat_matches (
    match_id, game_mode, start_time, end_time, duration_seconds,
    winner_team, total_players, region, server_id, match_data
) VALUES
(
    'match_20251228_001',
    'deathmatch',
    '2025-12-28 10:05:00+00',
    '2025-12-28 10:15:00+00',
    600,
    'team_alpha',
    8,
    'NA',
    'server-01',
    '{"map": "night_city_district", "weather": "rainy", "difficulty": "normal"}'
),
(
    'match_20251228_002',
    'team_deathmatch',
    '2025-12-28 09:20:00+00',
    '2025-12-28 09:35:00+00',
    900,
    'team_beta',
    12,
    'EU',
    'server-02',
    '{"map": "corporate_tower", "weather": "foggy", "difficulty": "hard"}'
);

-- Insert sample player combat stats
INSERT INTO analytics.player_combat_stats (
    player_id, match_id, kills, deaths, assists, score,
    damage_dealt, damage_taken, accuracy
) VALUES
(
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    'match_20251228_001',
    12, 3, 5, 2850,
    15420.50, 8750.25, 0.78
),
(
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    'match_20251228_002',
    8, 5, 7, 1920,
    12890.75, 9650.00, 0.65
),
(
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    'match_20251228_001',
    5, 8, 3, 1240,
    9870.25, 12300.50, 0.52
);

-- Insert sample guild activity
INSERT INTO analytics.guild_activity (
    guild_id, activity_type, player_id, activity_data
) VALUES
(
    '660e8400-e29b-41d4-a716-446655440010'::uuid,
    'guild_join',
    '550e8400-e29b-41d4-a716-446655440000'::uuid,
    '{"guild_name": "Cyber Nomads", "join_method": "invitation"}'
),
(
    '660e8400-e29b-41d4-a716-446655440010'::uuid,
    'guild_event_participate',
    '550e8400-e29b-41d4-a716-446655440001'::uuid,
    '{"event_type": "raid", "event_id": "raid_20251228_001", "contribution_score": 850}'
),
(
    '660e8400-e29b-41d4-a716-446655440011'::uuid,
    'guild_leave',
    '550e8400-e29b-41d4-a716-446655440002'::uuid,
    '{"leave_reason": "inactivity", "membership_duration_days": 45}'
);

-- Insert sample system metrics
INSERT INTO analytics.system_metrics (
    metric_type, metric_name, metric_value, unit, server_id, region, metadata
) VALUES
(
    'performance',
    'cpu_usage_percent',
    67.5,
    'percent',
    'game-server-01',
    'NA',
    '{"cores_used": 8, "total_cores": 16, "load_average": [2.1, 1.8, 1.5]}'
),
(
    'performance',
    'memory_usage_mb',
    8192.0,
    'MB',
    'game-server-01',
    'NA',
    '{"total_memory": 32768, "available_memory": 24576, "swap_used": 1024}'
),
(
    'network',
    'latency_ms',
    45.2,
    'ms',
    'game-server-01',
    'NA',
    '{"ping_count": 100, "packet_loss": 0.1, "jitter": 2.5}'
),
(
    'performance',
    'active_connections',
    1250.0,
    'count',
    'game-server-01',
    'NA',
    '{"peak_connections": 1450, "idle_connections": 200}'
);

-- Insert sample alerts
INSERT INTO analytics.alerts (
    alert_type, severity, title, description, alert_data
) VALUES
(
    'performance',
    'high',
    'High Server Latency Detected',
    'Game server latency has exceeded 50ms threshold for 5 minutes',
    '{"server_id": "game-server-01", "current_latency": 67.8, "threshold": 50, "duration_minutes": 5}'
),
(
    'economic',
    'medium',
    'Currency Inflation Alert',
    'Eddies circulation has increased by 15% in the last 24 hours',
    '{"currency_type": "eddies", "increase_percent": 15, "time_period": "24h", "current_circulation": 15420000}'
),
(
    'player',
    'low',
    'High Player Churn Rate',
    'Player retention rate has dropped below 85% for new users',
    '{"retention_rate": 82.5, "threshold": 85, "affected_segment": "new_users", "time_period": "7d"}'
);

-- Insert sample dashboard configuration
INSERT INTO analytics.dashboards (
    name, description, config, created_by, is_public, tags
) VALUES
(
    'Executive Overview',
    'High-level KPIs for executive decision making',
    '{
        "widgets": [
            {"type": "metric", "metric": "active_users", "title": "Active Users"},
            {"type": "chart", "chart_type": "line", "metric": "revenue", "period": "30d"},
            {"type": "metric", "metric": "server_health", "title": "Server Health Score"}
        ],
        "layout": "grid",
        "refresh_interval": 300
    }'::jsonb,
    '770e8400-e29b-41d4-a716-446655440020'::uuid,
    true,
    ARRAY['executive', 'kpi', 'overview']
),
(
    'Player Analytics Deep Dive',
    'Detailed player behavior and engagement analytics',
    '{
        "widgets": [
            {"type": "segmentation", "metric": "player_segments", "filters": {"region": "all"}},
            {"type": "retention", "cohort": "weekly", "period": "90d"},
            {"type": "behavior_flow", "start_event": "login", "depth": 5}
        ],
        "layout": "tabs",
        "refresh_interval": 600
    }'::jsonb,
    '770e8400-e29b-41d4-a716-446655440021'::uuid,
    false,
    ARRAY['players', 'behavior', 'retention']
);

COMMIT;
