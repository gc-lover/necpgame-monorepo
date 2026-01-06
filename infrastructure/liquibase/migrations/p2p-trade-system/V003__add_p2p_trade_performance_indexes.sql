-- P2P Trade System Performance Optimizations
-- Version: V003
-- Description: Advanced indexes, materialized views, partitioning, and performance optimizations for P2P Trade System

-- =================================================================================================
-- ADDITIONAL PERFORMANCE INDEXES
-- =================================================================================================

-- Complex multi-column indexes for trade sessions
CREATE INDEX CONCURRENTLY idx_trade_sessions_complex_lookup ON trade_sessions(
    status, zone_id, created_at DESC, fraud_score DESC
) WHERE status NOT IN ('completed', 'cancelled', 'expired');

CREATE INDEX CONCURRENTLY idx_trade_sessions_active_players ON trade_sessions(
    LEAST(initiator_id, target_id),
    GREATEST(initiator_id, target_id),
    status
) WHERE status NOT IN ('completed', 'cancelled', 'expired');

CREATE INDEX CONCURRENTLY idx_trade_sessions_value_fraud ON trade_sessions(
    trade_value_estimate DESC, fraud_score DESC, created_at DESC
) WHERE status = 'confirmed';

-- Trade history performance indexes
CREATE INDEX CONCURRENTLY idx_trade_history_complex_search ON trade_history(
    player1_id, player2_id, completed_at DESC, total_value DESC
);

CREATE INDEX CONCURRENTLY idx_trade_history_value_analysis ON trade_history(
    total_value DESC, value_imbalance, completed_at DESC
) WHERE suspicious_flag = false;

CREATE INDEX CONCURRENTLY idx_trade_history_fraud_investigation ON trade_history(
    suspicious_flag, investigation_status, completed_at DESC, fraud_score DESC
) WHERE suspicious_flag = true OR investigation_status != 'none';

-- Trade events high-volume indexes
CREATE INDEX CONCURRENTLY idx_trade_events_session_sequence ON trade_events(
    session_id, event_sequence
);

CREATE INDEX CONCURRENTLY idx_trade_events_audit_trail ON trade_events(
    session_id, event_timestamp, event_type
);

CREATE INDEX CONCURRENTLY idx_trade_events_recent_activity ON trade_events(
    actor_id, event_timestamp DESC, event_type
) WHERE event_timestamp > NOW() - INTERVAL '24 hours';

-- Locked items performance
CREATE INDEX CONCURRENTLY idx_locked_items_expiring ON locked_items(
    expected_unlock_at, lock_status
) WHERE lock_status = 'active' AND expected_unlock_at IS NOT NULL;

CREATE INDEX CONCURRENTLY idx_locked_items_disputes ON locked_items(
    session_id, lock_status, locked_at DESC
) WHERE lock_status IN ('disputed', 'forfeited');

-- Escrow accounts performance
CREATE INDEX CONCURRENTLY idx_trade_escrow_pending ON trade_escrow_accounts(
    escrow_status, escrowed_at DESC
) WHERE escrow_status = 'held';

CREATE INDEX CONCURRENTLY idx_trade_escrow_disputed ON trade_escrow_accounts(
    escrow_status, escrowed_at DESC
) WHERE escrow_status = 'disputed';

-- Suspicious patterns analysis
CREATE INDEX CONCURRENTLY idx_trade_suspicious_patterns_recent ON trade_suspicious_patterns(
    detected_at DESC, severity_score DESC, status
) WHERE status IN ('detected', 'investigating');

CREATE INDEX CONCURRENTLY idx_trade_suspicious_patterns_related ON trade_suspicious_patterns
USING GIN (related_sessions);

-- Disputes performance
CREATE INDEX CONCURRENTLY idx_trade_disputes_open_priority ON trade_disputes(
    status, priority, created_at
) WHERE status IN ('open', 'investigating');

CREATE INDEX CONCURRENTLY idx_trade_disputes_participants ON trade_disputes(
    complainant_id, respondent_id, status, created_at DESC
);

-- Rate limits optimization
CREATE INDEX CONCURRENTLY idx_trade_rate_limits_expiring ON trade_rate_limits(
    rate_limit_until, is_rate_limited
) WHERE is_rate_limited = true;

-- =================================================================================================
-- PARTIAL INDEXES FOR SPECIFIC QUERIES
-- =================================================================================================

-- Active trade sessions only
CREATE INDEX CONCURRENTLY idx_trade_sessions_active_only ON trade_sessions(
    id, initiator_id, target_id, status, expires_at, fraud_score
) WHERE status NOT IN ('completed', 'cancelled', 'expired');

-- Recently completed trades
CREATE INDEX CONCURRENTLY idx_trade_history_recent ON trade_history(
    completed_at DESC, player1_id, player2_id, total_value
) WHERE completed_at > NOW() - INTERVAL '7 days';

-- High-value trades
CREATE INDEX CONCURRENTLY idx_trade_history_high_value ON trade_history(
    total_value DESC, completed_at DESC
) WHERE total_value > 10000;

-- Active locked items
CREATE INDEX CONCURRENTLY idx_locked_items_active_only ON locked_items(
    player_id, item_id, session_id, quantity
) WHERE lock_status = 'active';

-- Held escrow accounts
CREATE INDEX CONCURRENTLY idx_trade_escrow_held_only ON trade_escrow_accounts(
    player_id, currency_type, amount
) WHERE escrow_status = 'held';

-- Open disputes
CREATE INDEX CONCURRENTLY idx_trade_disputes_open_only ON trade_disputes(
    id, complainant_id, respondent_id, dispute_type, priority, created_at
) WHERE status IN ('open', 'investigating');

-- Current rate limits
CREATE INDEX CONCURRENTLY idx_trade_rate_limits_current ON trade_rate_limits(
    player_id, is_rate_limited, trades_this_hour, trades_today
) WHERE hour_start >= NOW() - INTERVAL '1 hour';

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS
-- =================================================================================================

-- Player trading activity summary
CREATE MATERIALIZED VIEW player_trading_summary AS
SELECT
    p.player_id,
    p.total_trades,
    p.completed_trades,
    p.cancelled_trades,
    p.total_trade_value,
    p.avg_trade_value,
    p.last_trade_at,
    p.favorite_trading_partner,
    p.most_traded_zone,
    p.suspicious_trade_count,
    p.active_disputes,
    p.current_rating,
    CASE
        WHEN p.total_trades = 0 THEN 'newcomer'
        WHEN p.total_trades < 10 THEN 'casual'
        WHEN p.total_trades < 50 THEN 'regular'
        WHEN p.total_trades < 200 THEN 'active'
        ELSE 'power_trader'
    END as trader_tier
FROM (
    SELECT
        ph.player_id,
        COUNT(*) as total_trades,
        COUNT(*) FILTER (WHERE th.suspicious_flag = false) as completed_trades,
        COUNT(*) FILTER (WHERE th.session_id IN (
            SELECT ts.id FROM trade_sessions ts WHERE ts.status = 'cancelled'
        )) as cancelled_trades,
        COALESCE(SUM(th.total_value), 0) as total_trade_value,
        ROUND(AVG(th.total_value), 2) as avg_trade_value,
        MAX(th.completed_at) as last_trade_at,
        MODE() WITHIN GROUP (ORDER BY
            CASE WHEN ph.player_id = th.player1_id THEN th.player2_id ELSE th.player1_id END
        ) as favorite_trading_partner,
        MODE() WITHIN GROUP (ORDER BY th.zone_id) as most_traded_zone,
        COUNT(*) FILTER (WHERE th.suspicious_flag = true) as suspicious_trade_count,
        0 as active_disputes, -- Will be updated by separate query
        5.0 as current_rating -- Placeholder for reputation system
    FROM (
        SELECT player1_id as player_id FROM trade_history
        UNION ALL
        SELECT player2_id as player_id FROM trade_history
    ) ph
    LEFT JOIN trade_history th ON (
        th.player1_id = ph.player_id OR th.player2_id = ph.player_id
    )
    GROUP BY ph.player_id
) p;

-- Trade fraud analytics
CREATE MATERIALIZED VIEW trade_fraud_analytics AS
SELECT
    fa.date,
    fa.total_trades,
    fa.suspicious_trades,
    fa.fraud_score_avg,
    fa.top_fraud_patterns,
    fa.affected_players,
    fa.recovered_value,
    fa.false_positive_rate,
    CASE
        WHEN fa.suspicious_trades::float / NULLIF(fa.total_trades, 0) < 0.01 THEN 'low'
        WHEN fa.suspicious_trades::float / NULLIF(fa.total_trades, 0) < 0.05 THEN 'medium'
        WHEN fa.suspicious_trades::float / NULLIF(fa.total_trades, 0) < 0.10 THEN 'high'
        ELSE 'critical'
    END as risk_level
FROM (
    SELECT
        DATE(th.completed_at) as date,
        COUNT(*) as total_trades,
        COUNT(*) FILTER (WHERE th.suspicious_flag = true) as suspicious_trades,
        ROUND(AVG(th.fraud_score), 3) as fraud_score_avg,
        array_agg(DISTINCT tsp.pattern_type) FILTER (WHERE tsp.pattern_type IS NOT NULL) as top_fraud_patterns,
        COUNT(DISTINCT CASE WHEN th.suspicious_flag THEN th.player1_id END) +
        COUNT(DISTINCT CASE WHEN th.suspicious_flag THEN th.player2_id END) as affected_players,
        COALESCE(SUM(th.total_value) FILTER (WHERE th.suspicious_flag = true), 0) as recovered_value,
        ROUND(
            COUNT(*) FILTER (WHERE th.suspicious_flag = true AND th.investigation_status = 'false_positive')::float /
            NULLIF(COUNT(*) FILTER (WHERE th.suspicious_flag = true), 0), 3
        ) as false_positive_rate
    FROM trade_history th
    LEFT JOIN trade_suspicious_patterns tsp ON (
        tsp.related_sessions && ARRAY[th.session_id::text]
    )
    WHERE th.completed_at >= CURRENT_DATE - INTERVAL '30 days'
    GROUP BY DATE(th.completed_at)
) fa;

-- Zone trading activity
CREATE MATERIALIZED VIEW zone_trading_activity AS
SELECT
    zt.zone_id,
    zt.zone_type,
    zt.total_trades,
    zt.unique_traders,
    zt.total_value_traded,
    zt.avg_trade_value,
    zt.trade_success_rate,
    zt.popular_items,
    zt.peak_trading_hours,
    zt.fraud_incidents,
    CASE
        WHEN zt.total_trades < 100 THEN 'low_activity'
        WHEN zt.total_trades < 1000 THEN 'moderate_activity'
        WHEN zt.total_trades < 5000 THEN 'high_activity'
        ELSE 'trading_hub'
    END as activity_level
FROM (
    SELECT
        COALESCE(th.zone_id, ts.zone_id) as zone_id,
        'trading_zone' as zone_type, -- Would be populated from world service
        COUNT(DISTINCT COALESCE(th.session_id, ts.id)) as total_trades,
        COUNT(DISTINCT COALESCE(th.player1_id, ts.initiator_id)) +
        COUNT(DISTINCT COALESCE(th.player2_id, ts.target_id)) as unique_traders,
        COALESCE(SUM(th.total_value), 0) as total_value_traded,
        ROUND(AVG(th.total_value), 2) as avg_trade_value,
        ROUND(
            COUNT(*) FILTER (WHERE th.session_id IS NOT NULL)::float /
            NULLIF(COUNT(DISTINCT COALESCE(th.session_id, ts.id)), 0), 3
        ) as trade_success_rate,
        '[]'::jsonb as popular_items, -- Would be populated by analytics
        '[]'::jsonb as peak_trading_hours, -- Would be populated by analytics
        COUNT(*) FILTER (WHERE COALESCE(th.suspicious_flag, false)) as fraud_incidents
    FROM trade_sessions ts
    FULL OUTER JOIN trade_history th ON th.session_id = ts.id
    WHERE COALESCE(th.completed_at, ts.created_at) >= CURRENT_DATE - INTERVAL '30 days'
    GROUP BY COALESCE(th.zone_id, ts.zone_id)
) zt
WHERE zt.zone_id IS NOT NULL;

-- Item trading popularity
CREATE MATERIALIZED VIEW item_trading_popularity AS
SELECT
    itp.item_id,
    itp.item_name,
    itp.item_type,
    itp.total_trades,
    itp.total_quantity_traded,
    itp.total_value_traded,
    itp.avg_price,
    itp.price_volatility,
    itp.trader_count,
    itp.popular_zones,
    itp.recent_price_trend,
    CASE
        WHEN itp.total_trades < 10 THEN 'rare'
        WHEN itp.total_trades < 100 THEN 'uncommon'
        WHEN itp.total_trades < 1000 THEN 'common'
        ELSE 'hot_item'
    END as rarity_tier
FROM (
    SELECT
        (elem->>'item_id')::bigint as item_id,
        elem->>'name' as item_name,
        elem->>'type' as item_type,
        COUNT(DISTINCT th.id) as total_trades,
        SUM((elem->>'quantity')::int) as total_quantity_traded,
        SUM((elem->>'value')::bigint) as total_value_traded,
        ROUND(AVG((elem->>'value')::bigint / NULLIF((elem->>'quantity')::int, 0)), 2) as avg_price,
        ROUND(STDDEV((elem->>'value')::bigint / NULLIF((elem->>'quantity')::int, 0)), 2) as price_volatility,
        COUNT(DISTINCT CASE WHEN th.player1_items::jsonb @> elem THEN th.player1_id END) +
        COUNT(DISTINCT CASE WHEN th.player2_items::jsonb @> elem THEN th.player2_id END) as trader_count,
        array_agg(DISTINCT th.zone_id) FILTER (WHERE th.zone_id IS NOT NULL) as popular_zones,
        'stable'::text as recent_price_trend -- Would be calculated from price history
    FROM trade_history th,
         jsonb_array_elements(th.player1_items || th.player2_items) elem
    WHERE th.completed_at >= CURRENT_DATE - INTERVAL '30 days'
      AND elem ? 'item_id'
    GROUP BY (elem->>'item_id')::bigint, elem->>'name', elem->>'type'
) itp;

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_player_trading_summary_player ON player_trading_summary(player_id);
CREATE INDEX idx_player_trading_summary_tier ON player_trading_summary(trader_tier, total_trades DESC);
CREATE INDEX idx_player_trading_summary_value ON player_trading_summary(total_trade_value DESC);

CREATE INDEX idx_trade_fraud_analytics_date ON trade_fraud_analytics(date DESC);
CREATE INDEX idx_trade_fraud_analytics_risk ON trade_fraud_analytics(risk_level, suspicious_trades DESC);

CREATE INDEX idx_zone_trading_activity_zone ON zone_trading_activity(zone_id);
CREATE INDEX idx_zone_trading_activity_level ON zone_trading_activity(activity_level, total_trades DESC);

CREATE INDEX idx_item_trading_popularity_item ON item_trading_popularity(item_id);
CREATE INDEX idx_item_trading_popularity_rarity ON item_trading_popularity(rarity_tier, total_trades DESC);
CREATE INDEX idx_item_trading_popularity_value ON item_trading_popularity(total_value_traded DESC);

-- =================================================================================================
-- PARTITIONING SETUP FOR HIGH-VOLUME TABLES
-- =================================================================================================

-- Partition trade_history by month
CREATE TABLE trade_history_2025_01 PARTITION OF trade_history
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE trade_history_2025_02 PARTITION OF trade_history
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition trade_events by month
CREATE TABLE trade_events_2025_01 PARTITION OF trade_events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE trade_events_2025_02 PARTITION OF trade_events
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition trade_analytics by year
CREATE TABLE trade_analytics_2024 PARTITION OF trade_analytics
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

CREATE TABLE trade_analytics_2025 PARTITION OF trade_analytics
    FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');

-- =================================================================================================
-- PERFORMANCE MONITORING FUNCTIONS
-- =================================================================================================

-- Function to get player trading statistics efficiently
CREATE OR REPLACE FUNCTION get_player_trading_stats(player_id_param BIGINT, days_back INTEGER DEFAULT 30)
RETURNS TABLE (
    total_trades BIGINT,
    completed_trades BIGINT,
    cancelled_trades BIGINT,
    total_value BIGINT,
    avg_trade_value DECIMAL,
    success_rate DECIMAL,
    suspicious_trades BIGINT,
    active_disputes BIGINT,
    current_limits JSONB
) AS $$
DECLARE
    cutoff_date TIMESTAMP WITH TIME ZONE;
BEGIN
    cutoff_date := NOW() - INTERVAL '1 day' * days_back;

    RETURN QUERY
    WITH player_trades AS (
        SELECT
            th.id,
            th.total_value,
            th.suspicious_flag,
            CASE WHEN th.player1_id = player_id_param THEN th.player1_id ELSE th.player2_id END as trader_id,
            th.completed_at
        FROM trade_history th
        WHERE (th.player1_id = player_id_param OR th.player2_id = player_id_param)
          AND th.completed_at >= cutoff_date
    ),
    player_sessions AS (
        SELECT
            ts.id,
            ts.status,
            CASE WHEN ts.initiator_id = player_id_param THEN ts.initiator_id ELSE ts.target_id END as trader_id
        FROM trade_sessions ts
        WHERE (ts.initiator_id = player_id_param OR ts.target_id = player_id_param)
          AND ts.created_at >= cutoff_date
    )
    SELECT
        (SELECT COUNT(*) FROM player_trades) + (SELECT COUNT(*) FROM player_sessions WHERE status IN ('pending', 'offering', 'confirmed')) as total_trades,
        (SELECT COUNT(*) FROM player_trades WHERE suspicious_flag = false) as completed_trades,
        (SELECT COUNT(*) FROM player_sessions WHERE status = 'cancelled') as cancelled_trades,
        COALESCE((SELECT SUM(total_value) FROM player_trades), 0) as total_value,
        ROUND(COALESCE((SELECT AVG(total_value) FROM player_trades), 0), 2) as avg_trade_value,
        ROUND(
            (SELECT COUNT(*) FROM player_trades WHERE suspicious_flag = false)::decimal /
            NULLIF((SELECT COUNT(*) FROM player_trades), 0) * 100, 2
        ) as success_rate,
        (SELECT COUNT(*) FROM player_trades WHERE suspicious_flag = true) as suspicious_trades,
        COALESCE((SELECT COUNT(*) FROM trade_disputes WHERE complainant_id = player_id_param AND status IN ('open', 'investigating')), 0) as active_disputes,
        COALESCE((
            SELECT jsonb_build_object(
                'hourly_used', trl.trades_this_hour,
                'hourly_limit', trl.hourly_limit,
                'daily_used', trl.trades_today,
                'daily_limit', trl.daily_limit,
                'is_limited', trl.is_rate_limited
            ) FROM trade_rate_limits trl WHERE trl.player_id = player_id_param
        ), '{}'::jsonb) as current_limits;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate trade zone statistics
CREATE OR REPLACE FUNCTION get_zone_trading_stats(zone_id_param VARCHAR(100), hours_back INTEGER DEFAULT 24)
RETURNS TABLE (
    zone_id VARCHAR(100),
    active_sessions BIGINT,
    completed_trades BIGINT,
    total_value BIGINT,
    unique_traders BIGINT,
    avg_trade_value DECIMAL,
    fraud_rate DECIMAL,
    popular_items JSONB
) AS $$
DECLARE
    cutoff_time TIMESTAMP WITH TIME ZONE;
BEGIN
    cutoff_time := NOW() - INTERVAL '1 hour' * hours_back;

    RETURN QUERY
    SELECT
        zone_id_param as zone_id,
        COUNT(*) FILTER (WHERE ts.status NOT IN ('completed', 'cancelled', 'expired')) as active_sessions,
        COUNT(DISTINCT th.id) as completed_trades,
        COALESCE(SUM(th.total_value), 0) as total_value,
        COUNT(DISTINCT CASE WHEN ts.initiator_id IS NOT NULL THEN ts.initiator_id END) +
        COUNT(DISTINCT CASE WHEN ts.target_id IS NOT NULL THEN ts.target_id END) as unique_traders,
        ROUND(AVG(th.total_value), 2) as avg_trade_value,
        ROUND(
            COUNT(*) FILTER (WHERE th.suspicious_flag = true)::decimal /
            NULLIF(COUNT(th.id), 0) * 100, 2
        ) as fraud_rate,
        '[]'::jsonb as popular_items -- Would be calculated from item popularity analysis
    FROM trade_sessions ts
    LEFT JOIN trade_history th ON th.session_id = ts.id AND th.completed_at >= cutoff_time
    WHERE ts.zone_id = zone_id_param
      AND ts.created_at >= cutoff_time;
END;
$$ LANGUAGE plpgsql;

-- Function to detect fraud patterns for a player
CREATE OR REPLACE FUNCTION detect_player_fraud_patterns(player_id_param BIGINT, days_back INTEGER DEFAULT 7)
RETURNS TABLE (
    pattern_type VARCHAR(50),
    severity_score DECIMAL,
    detected_at TIMESTAMP WITH TIME ZONE,
    evidence_count INTEGER,
    risk_level VARCHAR(10)
) AS $$
DECLARE
    cutoff_date TIMESTAMP WITH TIME ZONE;
BEGIN
    cutoff_date := NOW() - INTERVAL '1 day' * days_back;

    RETURN QUERY
    WITH player_activity AS (
        SELECT
            th.player1_id,
            th.player2_id,
            th.completed_at,
            th.total_value,
            th.zone_id,
            th.player1_ip,
            th.player2_ip
        FROM trade_history th
        WHERE (th.player1_id = player_id_param OR th.player2_id = player_id_param)
          AND th.completed_at >= cutoff_date
    )
    SELECT
        tsp.pattern_type,
        tsp.severity_score,
        tsp.detected_at,
        array_length(tsp.related_sessions, 1) as evidence_count,
        CASE
            WHEN tsp.severity_score >= 0.8 THEN 'CRITICAL'
            WHEN tsp.severity_score >= 0.6 THEN 'HIGH'
            WHEN tsp.severity_score >= 0.4 THEN 'MEDIUM'
            WHEN tsp.severity_score >= 0.2 THEN 'LOW'
            ELSE 'MINIMAL'
        END as risk_level
    FROM trade_suspicious_patterns tsp
    WHERE tsp.player_id = player_id_param
      AND tsp.detected_at >= cutoff_date
      AND tsp.status IN ('detected', 'investigating')
    ORDER BY tsp.severity_score DESC, tsp.detected_at DESC;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- AUTOMATED MAINTENANCE FUNCTIONS
-- =================================================================================================

-- Function to clean up expired trade sessions
CREATE OR REPLACE FUNCTION cleanup_expired_trade_sessions()
RETURNS INTEGER AS $$
DECLARE
    expired_count INTEGER;
BEGIN
    -- Cancel expired sessions
    UPDATE trade_sessions
    SET status = 'expired',
        cancelled_at = NOW(),
        cancel_reason = 'session_expired'
    WHERE status NOT IN ('completed', 'cancelled', 'expired')
      AND expires_at < NOW();

    GET DIAGNOSTICS expired_count = ROW_COUNT;

    -- Unlock items from expired sessions
    UPDATE locked_items
    SET lock_status = 'expired',
        unlock_reason = 'session_expired'
    WHERE session_id IN (
        SELECT id FROM trade_sessions WHERE status = 'expired'
    ) AND lock_status = 'active';

    -- Release escrow from expired sessions
    UPDATE trade_escrow_accounts
    SET escrow_status = 'forfeited',
        forfeited_at = NOW(),
        release_reason = 'session_expired'
    WHERE session_id IN (
        SELECT id FROM trade_sessions WHERE status = 'expired'
    ) AND escrow_status = 'held';

    RETURN expired_count;
END;
$$ LANGUAGE plpgsql;

-- Function to update trade analytics
CREATE OR REPLACE FUNCTION update_trade_analytics(target_date DATE DEFAULT CURRENT_DATE)
RETURNS VOID AS $$
BEGIN
    INSERT INTO trade_analytics (
        date, total_trades, total_trade_value, total_items_traded,
        completed_trades, cancelled_trades, expired_trades, disputed_trades,
        suspicious_trades, fraud_score_avg, avg_trade_duration, avg_items_per_trade,
        zone_trade_counts, analytics_metadata
    )
    SELECT
        target_date,
        COUNT(DISTINCT ts.id) as total_trades,
        COALESCE(SUM(th.total_value), 0) as total_trade_value,
        COALESCE(SUM(jsonb_array_length(th.player1_items) + jsonb_array_length(th.player2_items)), 0) as total_items_traded,
        COUNT(DISTINCT th.id) as completed_trades,
        COUNT(*) FILTER (WHERE ts.status = 'cancelled') as cancelled_trades,
        COUNT(*) FILTER (WHERE ts.status = 'expired') as expired_trades,
        COUNT(DISTINCT td.id) as disputed_trades,
        COUNT(*) FILTER (WHERE COALESCE(th.suspicious_flag, false)) as suspicious_trades,
        ROUND(AVG(COALESCE(th.fraud_score, ts.fraud_score)), 3) as fraud_score_avg,
        AVG(EXTRACT(EPOCH FROM (th.completed_at - ts.created_at))) as avg_trade_duration,
        ROUND(AVG(jsonb_array_length(th.player1_items) + jsonb_array_length(th.player2_items)), 2) as avg_items_per_trade,
        jsonb_object_agg(COALESCE(ts.zone_id, 'unknown'), COUNT(*)) FILTER (WHERE ts.zone_id IS NOT NULL) as zone_trade_counts,
        jsonb_build_object('updated_at', NOW(), 'source', 'automated_update')
    FROM trade_sessions ts
    LEFT JOIN trade_history th ON th.session_id = ts.id AND DATE(th.completed_at) = target_date
    LEFT JOIN trade_disputes td ON (td.session_id = ts.id OR td.history_id = th.id) AND DATE(td.created_at) = target_date
    WHERE DATE(ts.created_at) = target_date
    GROUP BY target_date
    ON CONFLICT (date) DO UPDATE SET
        total_trades = EXCLUDED.total_trades,
        total_trade_value = EXCLUDED.total_trade_value,
        total_items_traded = EXCLUDED.total_items_traded,
        completed_trades = EXCLUDED.completed_trades,
        cancelled_trades = EXCLUDED.cancelled_trades,
        expired_trades = EXCLUDED.expired_trades,
        disputed_trades = EXCLUDED.disputed_trades,
        suspicious_trades = EXCLUDED.suspicious_trades,
        fraud_score_avg = EXCLUDED.fraud_score_avg,
        avg_trade_duration = EXCLUDED.avg_trade_duration,
        avg_items_per_trade = EXCLUDED.avg_items_per_trade,
        zone_trade_counts = EXCLUDED.zone_trade_counts,
        analytics_metadata = EXCLUDED.analytics_metadata;
END;
$$ LANGUAGE plpgsql;

-- Function to refresh all trade analytics views
CREATE OR REPLACE FUNCTION refresh_trade_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY player_trading_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY trade_fraud_analytics;
    REFRESH MATERIALIZED VIEW CONCURRENTLY zone_trading_activity;
    REFRESH MATERIALIZED VIEW CONCURRENTLY item_trading_popularity;

    -- Update daily analytics
    PERFORM update_trade_analytics(CURRENT_DATE);
    PERFORM update_trade_analytics(CURRENT_DATE - INTERVAL '1 day');

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('p2p-trade-system', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- DATA INTEGRITY VALIDATION
-- =================================================================================================

-- Function to validate trade data integrity
CREATE OR REPLACE FUNCTION validate_trade_data_integrity()
RETURNS TABLE (
    integrity_issue VARCHAR(100),
    severity VARCHAR(10),
    affected_records BIGINT,
    description TEXT
) AS $$
BEGIN
    -- Check for sessions without valid participants
    RETURN QUERY
    SELECT
        'invalid_session_participants'::VARCHAR(100),
        'CRITICAL'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Trade sessions with invalid or missing participants'::TEXT
    FROM trade_sessions
    WHERE initiator_id IS NULL OR target_id IS NULL OR initiator_id = target_id;

    -- Check for completed trades without history
    RETURN QUERY
    SELECT
        'missing_trade_history'::VARCHAR(100),
        'HIGH'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Completed trade sessions without corresponding history records'::TEXT
    FROM trade_sessions ts
    WHERE ts.status = 'completed'
      AND NOT EXISTS (SELECT 1 FROM trade_history th WHERE th.session_id = ts.id);

    -- Check for locked items without active sessions
    RETURN QUERY
    SELECT
        'orphaned_locked_items'::VARCHAR(100),
        'MEDIUM'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Locked items for completed/cancelled sessions'::TEXT
    FROM locked_items li
    JOIN trade_sessions ts ON ts.id = li.session_id
    WHERE li.lock_status = 'active'
      AND ts.status IN ('completed', 'cancelled', 'expired');

    -- Check for inconsistent confirmation states
    RETURN QUERY
    SELECT
        'inconsistent_confirmations'::VARCHAR(100),
        'LOW'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Sessions with inconsistent confirmation states'::TEXT
    FROM trade_sessions
    WHERE (initiator_confirmed = true OR target_confirmed = true)
      AND status NOT IN ('confirmed', 'completed');

    -- Check for high-value trades without fraud scoring
    RETURN QUERY
    SELECT
        'unscored_high_value_trades'::VARCHAR(100),
        'MEDIUM'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'High-value trades without fraud scoring'::TEXT
    FROM trade_sessions
    WHERE trade_value_estimate > 50000
      AND (fraud_score IS NULL OR fraud_score = 0);
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- PERFORMANCE MONITORING TABLES
-- =================================================================================================

-- Detailed performance metrics for trade operations
CREATE TABLE trade_detailed_metrics (
    id BIGSERIAL PRIMARY KEY,
    operation_id VARCHAR(50) NOT NULL, -- UUID for tracking operation chain
    operation_type VARCHAR(30) NOT NULL,
    player_id BIGINT,
    session_id UUID,
    step_name VARCHAR(50) NOT NULL, -- 'validation', 'locking', 'transfer', etc.
    step_duration_ms INTEGER NOT NULL,
    step_success BOOLEAN NOT NULL,
    step_error TEXT,
    metadata JSONB NOT NULL DEFAULT '{}',
    recorded_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Index for detailed metrics
CREATE INDEX CONCURRENTLY idx_trade_detailed_metrics_operation ON trade_detailed_metrics(operation_id, recorded_at DESC);
CREATE INDEX CONCURRENTLY idx_trade_detailed_metrics_type ON trade_detailed_metrics(operation_type, step_name, step_duration_ms DESC);

-- Performance baselines specific to P2P trading
CREATE TABLE trade_operation_baselines (
    id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(30) UNIQUE NOT NULL,
    baseline_p50_ms INTEGER NOT NULL, -- Median response time
    baseline_p95_ms INTEGER NOT NULL, -- 95th percentile
    baseline_p99_ms INTEGER NOT NULL, -- 99th percentile
    success_rate_baseline DECIMAL(5,4) NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(50) DEFAULT 'system'
);

-- Insert initial baselines
INSERT INTO trade_operation_baselines (
    operation_type, baseline_p50_ms, baseline_p95_ms, baseline_p99_ms, success_rate_baseline
) VALUES
('initiate_trade', 150, 300, 750, 0.995),
('add_offer', 100, 250, 600, 0.998),
('confirm_trade', 80, 200, 500, 0.999),
('complete_trade', 200, 500, 1200, 0.990),
('cancel_trade', 50, 120, 300, 0.999);
