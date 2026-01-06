-- Battle Pass System Performance Optimizations
-- Version: V003
-- Description: Additional indexes, materialized views, and performance optimizations for Battle Pass System

-- =================================================================================================
-- ADDITIONAL PERFORMANCE INDEXES
-- =================================================================================================

-- Complex season queries
CREATE INDEX CONCURRENTLY idx_battle_pass_seasons_status_active ON battle_pass_seasons(status, is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_battle_pass_seasons_date_range ON battle_pass_seasons(start_date, end_date, status);

-- Player progression complex indexes
CREATE INDEX CONCURRENTLY idx_battle_pass_player_progress_level_xp ON battle_pass_player_progress(current_level, current_xp);
CREATE INDEX CONCURRENTLY idx_battle_pass_player_progress_enrollment_level ON battle_pass_player_progress(player_enrollment_id, current_level DESC);

-- XP transactions performance
CREATE INDEX CONCURRENTLY idx_battle_pass_xp_transactions_enrollment_time ON battle_pass_xp_transactions(player_enrollment_id, granted_at DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_xp_transactions_source_time ON battle_pass_xp_transactions(xp_source, granted_at DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_xp_transactions_recent ON battle_pass_xp_transactions(granted_at DESC) WHERE granted_at > NOW() - INTERVAL '30 days';

-- Rewards claiming performance
CREATE INDEX CONCURRENTLY idx_battle_pass_claimed_rewards_enrollment_time ON battle_pass_claimed_rewards(player_enrollment_id, claimed_at DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_claimed_rewards_delivery_status ON battle_pass_claimed_rewards(delivery_status, claimed_at DESC);

-- Challenges performance
CREATE INDEX CONCURRENTLY idx_battle_pass_player_challenges_enrollment_completed ON battle_pass_player_challenges(player_enrollment_id, is_completed, last_progress_update DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_challenges_type_category ON battle_pass_challenges(challenge_type, challenge_category, is_active);
CREATE INDEX CONCURRENTLY idx_battle_pass_challenges_date_range ON battle_pass_challenges(start_date, end_date) WHERE start_date IS NOT NULL AND end_date IS NOT NULL;

-- Premium subscriptions performance
CREATE INDEX CONCURRENTLY idx_battle_pass_player_subscriptions_player_season ON battle_pass_player_subscriptions(player_id, season_id, is_active);
CREATE INDEX CONCURRENTLY idx_battle_pass_player_subscriptions_expiring ON battle_pass_player_subscriptions(expiration_date) WHERE expiration_date IS NOT NULL AND expiration_date > NOW();

-- Analytics performance
CREATE INDEX CONCURRENTLY idx_battle_pass_analytics_events_player_season ON battle_pass_analytics_events(player_id, season_id, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_analytics_events_type_timestamp ON battle_pass_analytics_events(event_type, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_battle_pass_analytics_events_recent ON battle_pass_analytics_events(event_timestamp DESC) WHERE event_timestamp > NOW() - INTERVAL '7 days';

-- Notifications performance
CREATE INDEX CONCURRENTLY idx_battle_pass_scheduled_notifications_player_type ON battle_pass_scheduled_notifications(player_id, notification_type, scheduled_for);
CREATE INDEX CONCURRENTLY idx_battle_pass_scheduled_notifications_pending ON battle_pass_scheduled_notifications(delivery_status, scheduled_for) WHERE delivery_status = 'PENDING';

-- =================================================================================================
-- PARTIAL INDEXES FOR COMMON QUERIES
-- =================================================================================================

-- Active seasons only
CREATE INDEX CONCURRENTLY idx_battle_pass_seasons_current ON battle_pass_seasons(id, season_key, start_date, end_date) WHERE status = 'ACTIVE' AND is_active = true;

-- Active player enrollments
CREATE INDEX CONCURRENTLY idx_battle_pass_player_enrollment_active_season ON battle_pass_player_enrollment(player_id, season_id, track_id) WHERE is_active = true;

-- Unclaimed rewards
CREATE INDEX CONCURRENTLY idx_battle_pass_level_rewards_level_guaranteed ON battle_pass_level_rewards(level_id, reward_id, quantity) WHERE is_guaranteed = true;

-- Active challenges
CREATE INDEX CONCURRENTLY idx_battle_pass_season_challenges_active ON battle_pass_season_challenges(season_id, challenge_id, sort_order) WHERE is_required = true;

-- Active premium subscriptions
CREATE INDEX CONCURRENTLY idx_battle_pass_player_subscriptions_current ON battle_pass_player_subscriptions(player_id, premium_tier_id, expiration_date) WHERE is_active = true AND (expiration_date IS NULL OR expiration_date > NOW());

-- Pending notifications
CREATE INDEX CONCURRENTLY idx_battle_pass_scheduled_notifications_sendable ON battle_pass_scheduled_notifications(id, player_id, scheduled_for) WHERE delivery_status = 'PENDING' AND scheduled_for <= NOW();

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS
-- =================================================================================================

-- Player progression summary
CREATE MATERIALIZED VIEW battle_pass_player_summary AS
SELECT
    pe.player_id,
    pe.season_id,
    s.season_key,
    pe.track_id,
    t.track_type,
    pp.current_level,
    pp.current_xp,
    pp.total_xp_earned,
    pp.completed_levels,
    pe.enrolled_at,
    pe.purchase_date,
    CASE WHEN ps.id IS NOT NULL THEN true ELSE false END as is_premium,
    COUNT(cr.id) as claimed_rewards,
    COUNT(pc.id) FILTER (WHERE pc.is_completed = true) as completed_challenges
FROM battle_pass_player_enrollment pe
JOIN battle_pass_seasons s ON s.id = pe.season_id
JOIN battle_pass_tracks t ON t.id = pe.track_id
LEFT JOIN battle_pass_player_progress pp ON pp.player_enrollment_id = pe.id
LEFT JOIN battle_pass_player_subscriptions ps ON ps.player_id = pe.player_id AND (ps.season_id IS NULL OR ps.season_id = pe.season_id) AND ps.is_active = true
LEFT JOIN battle_pass_claimed_rewards cr ON cr.player_enrollment_id = pe.id
LEFT JOIN battle_pass_player_challenges pc ON pc.player_enrollment_id = pe.id
WHERE pe.is_active = true
GROUP BY pe.player_id, pe.season_id, s.season_key, pe.track_id, t.track_type, pp.current_level, pp.current_xp, pp.total_xp_earned, pp.completed_levels, pe.enrolled_at, pe.purchase_date, ps.id;

-- Season performance metrics
CREATE MATERIALIZED VIEW battle_pass_season_performance AS
SELECT
    s.id as season_id,
    s.season_key,
    s.status,
    COUNT(DISTINCT pe.player_id) as total_players,
    COUNT(DISTINCT pe.player_id) FILTER (WHERE t.track_type != 'FREE') as premium_players,
    AVG(pp.current_level) as avg_level,
    MAX(pp.current_level) as max_level,
    SUM(pp.total_xp_earned) as total_xp_earned,
    COUNT(cr.id) as total_rewards_claimed,
    COUNT(pc.id) FILTER (WHERE pc.is_completed = true) as total_challenges_completed,
    AVG(EXTRACT(EPOCH FROM (pp.last_progress_update - pe.enrolled_at))/86400) as avg_days_active
FROM battle_pass_seasons s
LEFT JOIN battle_pass_player_enrollment pe ON pe.season_id = s.id AND pe.is_active = true
LEFT JOIN battle_pass_tracks t ON t.id = pe.track_id
LEFT JOIN battle_pass_player_progress pp ON pp.player_enrollment_id = pe.id
LEFT JOIN battle_pass_claimed_rewards cr ON cr.player_enrollment_id = pe.id
LEFT JOIN battle_pass_player_challenges pc ON pc.player_enrollment_id = pe.id
GROUP BY s.id, s.season_key, s.status;

-- Challenge completion statistics
CREATE MATERIALIZED VIEW battle_pass_challenge_stats AS
SELECT
    c.id as challenge_id,
    c.challenge_key,
    c.name,
    c.challenge_type,
    c.challenge_category,
    COUNT(pc.id) as total_attempts,
    COUNT(pc.id) FILTER (WHERE pc.is_completed = true) as total_completions,
    ROUND(COUNT(pc.id) FILTER (WHERE pc.is_completed = true)::decimal / NULLIF(COUNT(pc.id), 0) * 100, 2) as completion_rate,
    AVG(pc.current_progress) as avg_progress,
    MAX(pc.times_completed) as max_completions,
    AVG(EXTRACT(EPOCH FROM (pc.completed_at - pc.created_at))/3600) as avg_completion_time_hours
FROM battle_pass_challenges c
LEFT JOIN battle_pass_player_challenges pc ON pc.challenge_id = c.id
WHERE c.is_active = true
GROUP BY c.id, c.challenge_key, c.name, c.challenge_type, c.challenge_category;

-- Revenue and monetization analytics
CREATE MATERIALIZED VIEW battle_pass_revenue_analytics AS
SELECT
    s.id as season_id,
    s.season_key,
    COUNT(DISTINCT ps.player_id) as total_premium_subscribers,
    SUM(pt.price_cents) FILTER (WHERE pt.currency = 'USD') as total_revenue_usd_cents,
    AVG(pt.price_cents) FILTER (WHERE pt.currency = 'USD') as avg_purchase_price_cents,
    COUNT(ps.id) FILTER (WHERE ps.auto_renew = true) as auto_renew_subscribers,
    AVG(EXTRACT(EPOCH FROM (ps.expiration_date - ps.purchase_date))/86400) as avg_subscription_length_days
FROM battle_pass_seasons s
LEFT JOIN battle_pass_player_subscriptions ps ON ps.season_id = s.id AND ps.is_active = true
LEFT JOIN battle_pass_premium_tiers pt ON pt.id = ps.premium_tier_id
GROUP BY s.id, s.season_key;

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_battle_pass_player_summary_player_season ON battle_pass_player_summary(player_id, season_id);
CREATE INDEX idx_battle_pass_player_summary_level ON battle_pass_player_summary(current_level DESC);
CREATE INDEX idx_battle_pass_player_summary_premium ON battle_pass_player_summary(is_premium) WHERE is_premium = true;

CREATE INDEX idx_battle_pass_season_performance_season ON battle_pass_season_performance(season_id);
CREATE INDEX idx_battle_pass_season_performance_status ON battle_pass_season_performance(status);

CREATE INDEX idx_battle_pass_challenge_stats_type ON battle_pass_challenge_stats(challenge_type, challenge_category);
CREATE INDEX idx_battle_pass_challenge_stats_completion ON battle_pass_challenge_stats(completion_rate DESC);

CREATE INDEX idx_battle_pass_revenue_analytics_season ON battle_pass_revenue_analytics(season_id);

-- =================================================================================================
-- PARTITIONING SETUP FOR HIGH-VOLUME TABLES
-- =================================================================================================

-- Partition XP transactions by month (for active seasons)
-- This assumes PostgreSQL 10+ with declarative partitioning

-- Create monthly partitions for current season (example for 2025)
-- Note: In production, these would be created dynamically

CREATE TABLE battle_pass_xp_transactions_2025_01 PARTITION OF battle_pass_xp_transactions
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE battle_pass_xp_transactions_2025_02 PARTITION OF battle_pass_xp_transactions
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Analytics events partitioning by month
CREATE TABLE battle_pass_analytics_events_2025_01 PARTITION OF battle_pass_analytics_events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE battle_pass_analytics_events_2025_02 PARTITION OF battle_pass_analytics_events
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- =================================================================================================
-- PERFORMANCE MONITORING FUNCTIONS
-- =================================================================================================

-- Function to get player battle pass status efficiently
CREATE OR REPLACE FUNCTION get_player_battle_pass_status(player_id_param BIGINT, season_id_param BIGINT DEFAULT NULL)
RETURNS TABLE (
    season_id BIGINT,
    season_name VARCHAR(255),
    track_type VARCHAR(20),
    current_level INTEGER,
    current_xp BIGINT,
    xp_to_next BIGINT,
    progress_percentage DECIMAL(5,2),
    is_premium BOOLEAN,
    claimed_rewards_count BIGINT,
    completed_challenges_count BIGINT
) AS $$
DECLARE
    target_season_id BIGINT;
BEGIN
    -- If no season specified, get current active season
    IF season_id_param IS NULL THEN
        SELECT id INTO target_season_id
        FROM battle_pass_seasons
        WHERE status = 'ACTIVE' AND is_active = true
        ORDER BY start_date DESC
        LIMIT 1;
    ELSE
        target_season_id := season_id_param;
    END IF;

    -- Return player status for the season
    RETURN QUERY
    SELECT
        s.id,
        s.name,
        t.track_type,
        COALESCE(pp.current_level, 1),
        COALESCE(pp.current_xp, 0),
        GREATEST(0, COALESCE(l.xp_required - pp.current_xp, s.base_xp_per_level)),
        CASE
            WHEN pp.current_xp >= COALESCE(l.xp_required, s.base_xp_per_level)
            THEN 100.0
            WHEN COALESCE(l.xp_required, s.base_xp_per_level) > 0
            THEN (pp.current_xp::DECIMAL / COALESCE(l.xp_required, s.base_xp_per_level)) * 100
            ELSE 0.0
        END,
        CASE WHEN ps.id IS NOT NULL THEN true ELSE false END,
        COALESCE(stats.claimed_count, 0),
        COALESCE(stats.completed_count, 0)
    FROM battle_pass_seasons s
    JOIN battle_pass_player_enrollment pe ON pe.season_id = s.id AND pe.player_id = player_id_param AND pe.is_active = true
    JOIN battle_pass_tracks t ON t.id = pe.track_id
    LEFT JOIN battle_pass_player_progress pp ON pp.player_enrollment_id = pe.id
    LEFT JOIN battle_pass_levels l ON l.season_id = s.id AND l.track_id = pe.track_id AND l.level = COALESCE(pp.current_level + 1, 2)
    LEFT JOIN battle_pass_player_subscriptions ps ON ps.player_id = player_id_param AND (ps.season_id IS NULL OR ps.season_id = s.id) AND ps.is_active = true
    LEFT JOIN (
        SELECT
            cr.player_enrollment_id,
            COUNT(*) as claimed_count
        FROM battle_pass_claimed_rewards cr
        GROUP BY cr.player_enrollment_id
    ) stats ON stats.player_enrollment_id = pe.id
    LEFT JOIN (
        SELECT
            pc.player_enrollment_id,
            COUNT(*) FILTER (WHERE pc.is_completed = true) as completed_count
        FROM battle_pass_player_challenges pc
        GROUP BY pc.player_enrollment_id
    ) challenge_stats ON challenge_stats.player_enrollment_id = pe.id
    WHERE s.id = target_season_id;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate level progression requirements
CREATE OR REPLACE FUNCTION get_level_progression_requirements(season_id_param BIGINT, track_id_param BIGINT, start_level INTEGER DEFAULT 1, end_level INTEGER DEFAULT 100)
RETURNS TABLE (
    level INTEGER,
    xp_required BIGINT,
    cumulative_xp BIGINT,
    reward_summary JSONB
) AS $$
BEGIN
    RETURN QUERY
    WITH level_data AS (
        SELECT
            l.level,
            l.xp_required,
            SUM(l2.xp_required) OVER (ORDER BY l2.level) as cumulative_xp,
            jsonb_build_object(
                'base_rewards', l.reward_data,
                'premium_rewards', l.bonus_reward_data,
                'reward_count', (
                    SELECT COUNT(*)
                    FROM battle_pass_level_rewards lr
                    WHERE lr.level_id = l.id
                )
            ) as reward_summary
        FROM battle_pass_levels l
        LEFT JOIN battle_pass_levels l2 ON l2.season_id = l.season_id AND l2.track_id = l.track_id AND l2.level <= l.level
        WHERE l.season_id = season_id_param
          AND l.track_id = track_id_param
          AND l.level BETWEEN start_level AND end_level
        GROUP BY l.id, l.level, l.xp_required, l.reward_data, l.bonus_reward_data
        ORDER BY l.level
    )
    SELECT * FROM level_data;
END;
$$ LANGUAGE plpgsql;

-- Function to get available rewards for player at specific level
CREATE OR REPLACE FUNCTION get_available_rewards_for_level(player_id_param BIGINT, season_id_param BIGINT, level_param INTEGER)
RETURNS TABLE (
    reward_id BIGINT,
    reward_key VARCHAR(100),
    reward_name VARCHAR(255),
    reward_type VARCHAR(20),
    quantity INTEGER,
    is_claimed BOOLEAN,
    can_claim BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        r.id,
        r.reward_key,
        r.name,
        r.reward_type,
        lr.quantity,
        CASE WHEN cr.id IS NOT NULL THEN true ELSE false END as is_claimed,
        CASE
            WHEN cr.id IS NOT NULL THEN false -- Already claimed
            WHEN pp.current_level < level_param THEN false -- Level not reached
            WHEN pe.track_id = 1 AND lr.level_id IN (
                SELECT l.id FROM battle_pass_levels l WHERE l.season_id = season_id_param AND l.is_premium_locked = true
            ) THEN false -- Free track can't claim premium-locked rewards
            ELSE true -- Can claim
        END as can_claim
    FROM battle_pass_player_enrollment pe
    JOIN battle_pass_player_progress pp ON pp.player_enrollment_id = pe.id
    JOIN battle_pass_levels l ON l.season_id = pe.season_id AND l.track_id = pe.track_id AND l.level = level_param
    JOIN battle_pass_level_rewards lr ON lr.level_id = l.id
    JOIN battle_pass_rewards r ON r.id = lr.reward_id
    LEFT JOIN battle_pass_claimed_rewards cr ON cr.player_enrollment_id = pe.id AND cr.level_id = l.id AND cr.reward_id = r.id
    WHERE pe.player_id = player_id_param
      AND pe.season_id = season_id_param
      AND pe.is_active = true;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- ANALYTICS REFRESH FUNCTION
-- =================================================================================================

-- Function to refresh all battle pass analytics views
CREATE OR REPLACE FUNCTION refresh_battle_pass_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY battle_pass_player_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY battle_pass_season_performance;
    REFRESH MATERIALIZED VIEW CONCURRENTLY battle_pass_challenge_stats;
    REFRESH MATERIALIZED VIEW CONCURRENTLY battle_pass_revenue_analytics;

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('battle-pass-system', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- MAINTENANCE FUNCTIONS
-- =================================================================================================

-- Function to clean up expired premium subscriptions
CREATE OR REPLACE FUNCTION cleanup_expired_battle_pass_subscriptions()
RETURNS INTEGER AS $$
DECLARE
    expired_count INTEGER;
BEGIN
    UPDATE battle_pass_player_subscriptions
    SET is_active = false
    WHERE expiration_date IS NOT NULL
      AND expiration_date < NOW()
      AND is_active = true;

    GET DIAGNOSTICS expired_count = ROW_COUNT;
    RETURN expired_count;
END;
$$ LANGUAGE plpgsql;

-- Function to archive old analytics data
CREATE OR REPLACE FUNCTION archive_old_battle_pass_analytics(days_old INTEGER DEFAULT 90)
RETURNS INTEGER AS $$
DECLARE
    archived_count INTEGER;
BEGIN
    -- Count records that would be archived (implement actual archiving logic as needed)
    SELECT COUNT(*) INTO archived_count
    FROM battle_pass_analytics_events
    WHERE event_timestamp < NOW() - INTERVAL '1 day' * days_old;

    -- In production, implement archiving to separate tables or external storage
    -- Example:
    -- INSERT INTO battle_pass_analytics_archive SELECT * FROM battle_pass_analytics_events WHERE ...
    -- DELETE FROM battle_pass_analytics_events WHERE ...

    RETURN archived_count;
END;
$$ LANGUAGE plpgsql;

-- Function to reset daily challenges for all players
CREATE OR REPLACE FUNCTION reset_daily_battle_pass_challenges()
RETURNS INTEGER AS $$
DECLARE
    reset_count INTEGER;
BEGIN
    -- Reset progress for daily challenges
    UPDATE battle_pass_player_challenges
    SET current_progress = 0,
        is_completed = false,
        completed_at = NULL,
        last_progress_update = NOW()
    WHERE challenge_id IN (
        SELECT id FROM battle_pass_challenges WHERE challenge_type = 'DAILY'
    ) AND is_completed = true;

    GET DIAGNOSTICS reset_count = ROW_COUNT;
    RETURN reset_count;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- PERFORMANCE MONITORING TABLES
-- =================================================================================================

-- Query performance monitoring for battle pass operations
CREATE TABLE battle_pass_query_performance (
    id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL, -- 'get_player_status', 'claim_reward', 'update_progress', etc.
    execution_time_ms INTEGER NOT NULL,
    player_id BIGINT,
    season_id BIGINT,
    parameters JSONB,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance monitoring
CREATE INDEX idx_battle_pass_query_performance_type_time ON battle_pass_query_performance(operation_type, executed_at DESC);
CREATE INDEX idx_battle_pass_query_performance_slow ON battle_pass_query_performance(execution_time_ms DESC) WHERE execution_time_ms > 500;
