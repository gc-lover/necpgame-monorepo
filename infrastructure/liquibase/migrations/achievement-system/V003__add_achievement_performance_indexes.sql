-- Achievement System Performance Optimizations
-- Version: V003
-- Description: Additional indexes, materialized views, and performance optimizations for Achievement System

-- =================================================================================================
-- ADDITIONAL PERFORMANCE INDEXES
-- =================================================================================================

-- Complex achievement queries
CREATE INDEX CONCURRENTLY idx_achievement_definitions_category_difficulty ON achievement_definitions(category, difficulty, is_active);
CREATE INDEX CONCURRENTLY idx_achievement_definitions_type_active ON achievement_definitions(achievement_type, is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_achievement_definitions_hidden ON achievement_definitions(is_hidden, category) WHERE is_hidden = false;

-- Player achievement complex indexes
CREATE INDEX CONCURRENTLY idx_player_achievements_player_status_date ON player_achievements(player_id, status, completed_at DESC);
CREATE INDEX CONCURRENTLY idx_player_achievements_achievement_status ON player_achievements(achievement_id, status);
CREATE INDEX CONCURRENTLY idx_player_achievements_completed_date ON player_achievements(completed_at DESC) WHERE completed_at IS NOT NULL;
CREATE INDEX CONCURRENTLY idx_player_achievements_unlocked_recent ON player_achievements(unlocked_at DESC) WHERE unlocked_at > NOW() - INTERVAL '7 days';

-- Achievement progress performance
CREATE INDEX CONCURRENTLY idx_achievement_progress_player_achievement_completed ON achievement_progress(player_achievement_id, is_completed, last_updated DESC);
CREATE INDEX CONCURRENTLY idx_achievement_progress_key_value ON achievement_progress(progress_key, current_value, target_value);
CREATE INDEX CONCURRENTLY idx_achievement_progress_incomplete ON achievement_progress(is_completed, last_updated DESC) WHERE is_completed = false;

-- Progress events performance
CREATE INDEX CONCURRENTLY idx_achievement_progress_events_player_achievement ON achievement_progress_events(player_achievement_id, recorded_at DESC);
CREATE INDEX CONCURRENTLY idx_achievement_progress_events_recent ON achievement_progress_events(recorded_at DESC) WHERE recorded_at > NOW() - INTERVAL '24 hours';

-- Reward claiming performance
CREATE INDEX CONCURRENTLY idx_achievement_claimed_rewards_player_delivery ON achievement_claimed_rewards(player_achievement_id, delivery_status, claimed_at DESC);
CREATE INDEX CONCURRENTLY idx_achievement_claimed_rewards_delivery_date ON achievement_claimed_rewards(delivery_status, claimed_at DESC);
CREATE INDEX CONCURRENTLY idx_achievement_claimed_rewards_pending ON achievement_claimed_rewards(delivery_status, claimed_at) WHERE delivery_status = 'PENDING';

-- Analytics performance
CREATE INDEX CONCURRENTLY idx_achievement_events_player_type_timestamp ON achievement_events(player_id, event_type, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_achievement_events_achievement_timestamp ON achievement_events(achievement_id, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_achievement_events_recent_events ON achievement_events(event_timestamp DESC) WHERE event_timestamp > NOW() - INTERVAL '3 days';
CREATE INDEX CONCURRENTLY idx_achievement_events_type_timestamp ON achievement_events(event_type, event_timestamp DESC);

-- Chain and seasonal performance
CREATE INDEX CONCURRENTLY idx_achievement_chain_members_chain_position ON achievement_chain_members(chain_id, position);
CREATE INDEX CONCURRENTLY idx_achievement_season_members_season_featured ON achievement_season_members(season_id, is_featured) WHERE is_featured = true;

-- Guild achievements performance
CREATE INDEX CONCURRENTLY idx_guild_achievements_guild_status ON guild_achievements(guild_id, status, completed_at DESC);

-- Notification performance
CREATE INDEX CONCURRENTLY idx_achievement_scheduled_notifications_player_type_scheduled ON achievement_scheduled_notifications(player_id, notification_type, scheduled_for);
CREATE INDEX CONCURRENTLY idx_achievement_scheduled_notifications_sendable ON achievement_scheduled_notifications(delivery_status, scheduled_for) WHERE delivery_status = 'PENDING' AND scheduled_for <= NOW();

-- =================================================================================================
-- PARTIAL INDEXES FOR COMMON QUERIES
-- =================================================================================================

-- Active achievements only
CREATE INDEX CONCURRENTLY idx_achievement_definitions_active_category ON achievement_definitions(id, code, category, difficulty) WHERE is_active = true;

-- Unlocked player achievements
CREATE INDEX CONCURRENTLY idx_player_achievements_unlocked ON player_achievements(id, player_id, achievement_id, status) WHERE status IN ('UNLOCKED', 'IN_PROGRESS', 'COMPLETED');

-- Completed achievements for rewards
CREATE INDEX CONCURRENTLY idx_player_achievements_completed_unclaimed ON player_achievements(id, player_id, achievement_id) WHERE status = 'COMPLETED' AND claimed_at IS NULL;

-- Current season achievements
CREATE INDEX CONCURRENTLY idx_achievement_season_members_current_season ON achievement_season_members(achievement_id, bonus_multiplier) WHERE season_id = (SELECT id FROM achievement_seasons WHERE is_active = true LIMIT 1);

-- High-value achievements (for leaderboards)
CREATE INDEX CONCURRENTLY idx_achievement_definitions_high_value ON achievement_definitions(id, code, difficulty) WHERE difficulty IN ('HARD', 'LEGENDARY');

-- Active progress tracking
CREATE INDEX CONCURRENTLY idx_achievement_progress_active_tracking ON achievement_progress(id, player_achievement_id, progress_key, current_value, target_value) WHERE is_completed = false;

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS
-- =================================================================================================

-- Player achievement statistics
CREATE MATERIALIZED VIEW achievement_player_stats AS
SELECT
    pa.player_id,
    COUNT(*) as total_achievements,
    COUNT(*) FILTER (WHERE pa.status = 'COMPLETED') as completed_achievements,
    COUNT(*) FILTER (WHERE pa.status = 'IN_PROGRESS') as in_progress_achievements,
    COUNT(*) FILTER (WHERE pa.claimed_at IS NOT NULL) as claimed_rewards,
    AVG(EXTRACT(EPOCH FROM (pa.completed_at - pa.unlocked_at))/3600) FILTER (WHERE pa.completed_at IS NOT NULL) as avg_completion_time_hours,
    MAX(pa.completed_at) as last_completion_date,
    MIN(pa.unlocked_at) as first_achievement_date,
    array_agg(ad.category) FILTER (WHERE pa.status = 'COMPLETED') as completed_categories
FROM player_achievements pa
JOIN achievement_definitions ad ON ad.id = pa.achievement_id
GROUP BY pa.player_id;

-- Achievement popularity and completion rates
CREATE MATERIALIZED VIEW achievement_popularity_stats AS
SELECT
    ad.id as achievement_id,
    ad.code,
    ad.title,
    ad.category,
    ad.difficulty,
    COUNT(pa.id) as total_unlocked,
    COUNT(pa.id) FILTER (WHERE pa.status = 'COMPLETED') as total_completed,
    ROUND(
        COUNT(pa.id) FILTER (WHERE pa.status = 'COMPLETED')::decimal /
        NULLIF(COUNT(pa.id), 0) * 100, 2
    ) as completion_rate,
    COUNT(pa.id) FILTER (WHERE pa.claimed_at IS NOT NULL) as total_claimed,
    AVG(EXTRACT(EPOCH FROM (pa.completed_at - pa.unlocked_at))/3600) FILTER (WHERE pa.completed_at IS NOT NULL) as avg_completion_time_hours,
    MAX(pa.completed_at) as last_completed_at
FROM achievement_definitions ad
LEFT JOIN player_achievements pa ON pa.achievement_id = ad.id
WHERE ad.is_active = true
GROUP BY ad.id, ad.code, ad.title, ad.category, ad.difficulty;

-- Daily achievement activity
CREATE MATERIALIZED VIEW achievement_daily_activity AS
SELECT
    DATE(ae.event_timestamp) as activity_date,
    ae.event_type,
    COUNT(*) as total_events,
    COUNT(DISTINCT ae.player_id) as unique_players,
    COUNT(DISTINCT ae.achievement_id) as unique_achievements,
    array_agg(DISTINCT ad.category) FILTER (WHERE ad.category IS NOT NULL) as categories_active
FROM achievement_events ae
LEFT JOIN achievement_definitions ad ON ad.id = ae.achievement_id
WHERE ae.event_timestamp >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY DATE(ae.event_timestamp), ae.event_type
ORDER BY activity_date DESC, total_events DESC;

-- Achievement chains progress
CREATE MATERIALIZED VIEW achievement_chain_progress AS
SELECT
    ac.id as chain_id,
    ac.chain_key,
    ac.name,
    COUNT(DISTINCT pa.player_id) as total_players_attempting,
    COUNT(DISTINCT pa.player_id) FILTER (WHERE acm.position = (
        SELECT MAX(position) FROM achievement_chain_members WHERE chain_id = ac.id
    ) AND pa.status = 'COMPLETED') as total_players_completed,
    ROUND(
        COUNT(DISTINCT pa.player_id) FILTER (WHERE acm.position = (
            SELECT MAX(position) FROM achievement_chain_members WHERE chain_id = ac.id
        ) AND pa.status = 'COMPLETED')::decimal /
        NULLIF(COUNT(DISTINCT pa.player_id), 0) * 100, 2
    ) as completion_rate,
    AVG(acm.position) as avg_progress_position
FROM achievement_chains ac
LEFT JOIN achievement_chain_members acm ON acm.chain_id = ac.id
LEFT JOIN player_achievements pa ON pa.achievement_id = acm.achievement_id
WHERE ac.is_active = true
GROUP BY ac.id, ac.chain_key, ac.name;

-- Seasonal achievement performance
CREATE MATERIALIZED VIEW achievement_seasonal_performance AS
SELECT
    as2.id as season_id,
    as2.season_key,
    as2.name,
    COUNT(DISTINCT pa.player_id) as total_participants,
    COUNT(pa.id) FILTER (WHERE pa.status = 'COMPLETED') as total_completions,
    COUNT(asm.id) as total_seasonal_achievements,
    COUNT(pa.id) FILTER (WHERE pa.status = 'COMPLETED' AND asm.is_featured = true) as featured_completions,
    AVG(asm.bonus_multiplier) as avg_bonus_multiplier,
    SUM(pa.completion_count) as total_completion_count
FROM achievement_seasons as2
LEFT JOIN achievement_season_members asm ON asm.season_id = as2.id
LEFT JOIN player_achievements pa ON pa.achievement_id = asm.achievement_id
WHERE as2.is_active = true
GROUP BY as2.id, as2.season_key, as2.name;

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_achievement_player_stats_player ON achievement_player_stats(player_id);
CREATE INDEX idx_achievement_player_stats_completion_rate ON achievement_player_stats(completed_achievements DESC);

CREATE INDEX idx_achievement_popularity_stats_achievement ON achievement_popularity_stats(achievement_id);
CREATE INDEX idx_achievement_popularity_stats_completion_rate ON achievement_popularity_stats(completion_rate DESC);
CREATE INDEX idx_achievement_popularity_stats_category ON achievement_popularity_stats(category, difficulty);

CREATE INDEX idx_achievement_daily_activity_date ON achievement_daily_activity(activity_date DESC);
CREATE INDEX idx_achievement_daily_activity_type ON achievement_daily_activity(event_type, activity_date DESC);

CREATE INDEX idx_achievement_chain_progress_chain ON achievement_chain_progress(chain_id);
CREATE INDEX idx_achievement_chain_progress_completion_rate ON achievement_chain_progress(completion_rate DESC);

CREATE INDEX idx_achievement_seasonal_performance_season ON achievement_seasonal_performance(season_id);

-- =================================================================================================
-- PARTITIONING SETUP FOR HIGH-VOLUME TABLES
-- =================================================================================================

-- Partition achievement events by month (for analytics)
-- Note: PostgreSQL 10+ required for declarative partitioning

CREATE TABLE achievement_events_2025_01 PARTITION OF achievement_events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE achievement_events_2025_02 PARTITION OF achievement_events
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition progress events by month
CREATE TABLE achievement_progress_events_2025_01 PARTITION OF achievement_progress_events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE achievement_progress_events_2025_02 PARTITION OF achievement_progress_events
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- =================================================================================================
-- PERFORMANCE MONITORING FUNCTIONS
-- =================================================================================================

-- Function to get player achievement summary efficiently
CREATE OR REPLACE FUNCTION get_player_achievement_summary(player_id_param BIGINT)
RETURNS TABLE (
    total_achievements BIGINT,
    completed_achievements BIGINT,
    in_progress_achievements BIGINT,
    claimed_rewards BIGINT,
    completion_percentage DECIMAL(5,2),
    favorite_category VARCHAR(30),
    avg_completion_time INTERVAL,
    recent_completions JSONB
) AS $$
DECLARE
    stats_record RECORD;
BEGIN
    -- Get basic stats
    SELECT
        COALESCE(aps.total_achievements, 0) as total_achievements,
        COALESCE(aps.completed_achievements, 0) as completed_achievements,
        COALESCE(aps.in_progress_achievements, 0) as in_progress_achievements,
        COALESCE(aps.claimed_rewards, 0) as claimed_rewards,
        CASE
            WHEN aps.total_achievements > 0
            THEN ROUND((aps.completed_achievements::DECIMAL / aps.total_achievements) * 100, 2)
            ELSE 0.0
        END as completion_percentage,
        (SELECT unnest(aps.completed_categories) GROUP BY 1 ORDER BY COUNT(*) DESC LIMIT 1) as favorite_category,
        aps.avg_completion_time_hours * INTERVAL '1 hour' as avg_completion_time
    INTO stats_record
    FROM achievement_player_stats aps
    WHERE aps.player_id = player_id_param;

    -- Get recent completions (last 5)
    WITH recent_completions AS (
        SELECT
            ad.title,
            pa.completed_at,
            ROW_NUMBER() OVER (ORDER BY pa.completed_at DESC) as rn
        FROM player_achievements pa
        JOIN achievement_definitions ad ON ad.id = pa.achievement_id
        WHERE pa.player_id = player_id_param
          AND pa.status = 'COMPLETED'
        ORDER BY pa.completed_at DESC
        LIMIT 5
    )
    SELECT jsonb_agg(
        jsonb_build_object(
            'title', rc.title,
            'completed_at', rc.completed_at
        )
    ) INTO stats_record.recent_completions
    FROM recent_completions rc;

    -- Return the summary
    RETURN QUERY
    SELECT
        stats_record.total_achievements,
        stats_record.completed_achievements,
        stats_record.in_progress_achievements,
        stats_record.claimed_rewards,
        stats_record.completion_percentage,
        stats_record.favorite_category,
        stats_record.avg_completion_time,
        COALESCE(stats_record.recent_completions, '[]'::jsonb);
END;
$$ LANGUAGE plpgsql;

-- Function to calculate achievement completion statistics
CREATE OR REPLACE FUNCTION get_achievement_completion_stats(achievement_id_param BIGINT, days_back INTEGER DEFAULT 30)
RETURNS TABLE (
    achievement_title JSONB,
    total_unlocked BIGINT,
    total_completed BIGINT,
    completion_rate DECIMAL(5,2),
    avg_completion_time INTERVAL,
    completions_by_day JSONB
) AS $$
DECLARE
    achievement_record RECORD;
BEGIN
    -- Get achievement info
    SELECT ad.title INTO achievement_record
    FROM achievement_definitions ad
    WHERE ad.id = achievement_id_param;

    -- Calculate statistics
    WITH daily_completions AS (
        SELECT
            DATE(pa.completed_at) as completion_date,
            COUNT(*) as completions
        FROM player_achievements pa
        WHERE pa.achievement_id = achievement_id_param
          AND pa.status = 'COMPLETED'
          AND pa.completed_at >= CURRENT_DATE - INTERVAL '1 day' * days_back
        GROUP BY DATE(pa.completed_at)
        ORDER BY completion_date
    ),
    stats AS (
        SELECT
            COUNT(DISTINCT pa.id) FILTER (WHERE pa.status IN ('UNLOCKED', 'IN_PROGRESS', 'COMPLETED')) as total_unlocked,
            COUNT(DISTINCT pa.id) FILTER (WHERE pa.status = 'COMPLETED') as total_completed,
            AVG(EXTRACT(EPOCH FROM (pa.completed_at - pa.unlocked_at))/3600) FILTER (WHERE pa.completed_at IS NOT NULL) as avg_completion_hours
        FROM player_achievements pa
        WHERE pa.achievement_id = achievement_id_param
    )
    SELECT
        achievement_record.title,
        stats.total_unlocked,
        stats.total_completed,
        CASE
            WHEN stats.total_unlocked > 0
            THEN ROUND((stats.total_completed::DECIMAL / stats.total_unlocked) * 100, 2)
            ELSE 0.0
        END as completion_rate,
        (stats.avg_completion_hours * INTERVAL '1 hour')::INTERVAL as avg_completion_time,
        COALESCE(
            jsonb_object_agg(
                dc.completion_date::text,
                dc.completions
            ) FILTER (WHERE dc.completion_date IS NOT NULL),
            '{}'::jsonb
        ) as completions_by_day
    INTO achievement_record
    FROM stats
    LEFT JOIN daily_completions dc ON true
    GROUP BY stats.total_unlocked, stats.total_completed, stats.avg_completion_hours;

    -- Return the statistics
    RETURN QUERY
    SELECT
        achievement_record.title,
        achievement_record.total_unlocked,
        achievement_record.total_completed,
        achievement_record.completion_rate,
        achievement_record.avg_completion_time,
        achievement_record.completions_by_day;
END;
$$ LANGUAGE plpgsql;

-- Function to get available achievements for player
CREATE OR REPLACE FUNCTION get_available_achievements_for_player(player_id_param BIGINT, category_filter VARCHAR(30) DEFAULT NULL, limit_param INTEGER DEFAULT 50)
RETURNS TABLE (
    achievement_id BIGINT,
    code VARCHAR(100),
    title JSONB,
    description JSONB,
    category VARCHAR(30),
    difficulty VARCHAR(15),
    is_hidden BOOLEAN,
    is_repeatable BOOLEAN,
    status VARCHAR(20),
    progress_percentage DECIMAL(5,2),
    can_claim BOOLEAN,
    rewards JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        ad.id,
        ad.code,
        ad.title,
        ad.description,
        ad.category,
        ad.difficulty,
        ad.is_hidden,
        ad.is_repeatable,
        COALESCE(pa.status, 'LOCKED') as status,
        COALESCE(ap.progress_percentage, 0.0) as progress_percentage,
        CASE
            WHEN pa.status = 'COMPLETED' AND pa.claimed_at IS NULL THEN true
            ELSE false
        END as can_claim,
        ad.rewards
    FROM achievement_definitions ad
    LEFT JOIN player_achievements pa ON pa.achievement_id = ad.id AND pa.player_id = player_id_param
    LEFT JOIN (
        SELECT
            apa.player_achievement_id,
            MAX(apa.progress_percentage) as progress_percentage
        FROM achievement_progress apa
        GROUP BY apa.player_achievement_id
    ) ap ON ap.player_achievement_id = pa.id
    WHERE ad.is_active = true
      AND (category_filter IS NULL OR ad.category = category_filter)
      AND (ad.is_hidden = false OR pa.status IS NOT NULL) -- Show hidden only if unlocked
    ORDER BY
        CASE
            WHEN pa.status = 'COMPLETED' THEN 1
            WHEN pa.status IN ('IN_PROGRESS', 'UNLOCKED') THEN 2
            WHEN pa.status IS NULL THEN 3
        END,
        ad.sort_order,
        ad.difficulty DESC
    LIMIT limit_param;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- ANALYTICS REFRESH FUNCTION
-- =================================================================================================

-- Function to refresh all achievement analytics views
CREATE OR REPLACE FUNCTION refresh_achievement_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY achievement_player_stats;
    REFRESH MATERIALIZED VIEW CONCURRENTLY achievement_popularity_stats;
    REFRESH MATERIALIZED VIEW CONCURRENTLY achievement_daily_activity;
    REFRESH MATERIALIZED VIEW CONCURRENTLY achievement_chain_progress;
    REFRESH MATERIALIZED VIEW CONCURRENTLY achievement_seasonal_performance;

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('achievement-system', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- MAINTENANCE FUNCTIONS
-- =================================================================================================

-- Function to clean up old achievement events (keep last 90 days)
CREATE OR REPLACE FUNCTION cleanup_old_achievement_events(days_to_keep INTEGER DEFAULT 90)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM achievement_events
    WHERE event_timestamp < CURRENT_DATE - INTERVAL '1 day' * days_to_keep;

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to clean up old progress events (keep last 30 days)
CREATE OR REPLACE FUNCTION cleanup_old_achievement_progress_events(days_to_keep INTEGER DEFAULT 30)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM achievement_progress_events
    WHERE recorded_at < CURRENT_DATE - INTERVAL '1 day' * days_to_keep;

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to validate achievement progress integrity
CREATE OR REPLACE FUNCTION validate_achievement_progress_integrity()
RETURNS TABLE (
    issue_type VARCHAR(50),
    issue_description TEXT,
    affected_records BIGINT
) AS $$
BEGIN
    -- Check for achievements marked as completed but progress not at 100%
    RETURN QUERY
    SELECT
        'incomplete_progress'::VARCHAR(50) as issue_type,
        'Achievements marked as completed but progress not at 100%'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM player_achievements pa
    JOIN achievement_progress ap ON ap.player_achievement_id = pa.id
    WHERE pa.status = 'COMPLETED'
      AND ap.progress_percentage < 100;

    -- Check for orphaned progress records
    RETURN QUERY
    SELECT
        'orphaned_progress'::VARCHAR(50) as issue_type,
        'Progress records without corresponding player achievement'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM achievement_progress ap
    LEFT JOIN player_achievements pa ON pa.id = ap.player_achievement_id
    WHERE pa.id IS NULL;

    -- Check for achievements with invalid status transitions
    RETURN QUERY
    SELECT
        'invalid_status'::VARCHAR(50) as issue_type,
        'Achievements with invalid status transitions'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM player_achievements pa
    WHERE (pa.status = 'COMPLETED' AND pa.completed_at IS NULL)
       OR (pa.status = 'CLAIMED' AND pa.claimed_at IS NULL)
       OR (pa.status IN ('IN_PROGRESS', 'COMPLETED', 'CLAIMED') AND pa.unlocked_at IS NULL);
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- PERFORMANCE MONITORING TABLES
-- =================================================================================================

-- Query performance monitoring for achievement operations
CREATE TABLE achievement_query_performance (
    id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL, -- 'get_player_summary', 'update_progress', 'claim_reward', 'unlock_achievement'
    execution_time_ms INTEGER NOT NULL,
    player_id BIGINT,
    achievement_id BIGINT,
    parameters JSONB,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance monitoring
CREATE INDEX idx_achievement_query_performance_type_time ON achievement_query_performance(operation_type, executed_at DESC);
CREATE INDEX idx_achievement_query_performance_slow ON achievement_query_performance(execution_time_ms DESC) WHERE execution_time_ms > 500;
