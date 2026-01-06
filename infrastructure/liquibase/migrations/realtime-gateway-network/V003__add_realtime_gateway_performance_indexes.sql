-- Realtime Gateway Network Performance Optimizations
-- Version: V003
-- Description: Advanced indexes, materialized views, partitioning, and performance optimizations for realtime-gateway network metrics

-- =================================================================================================
-- HIGH-PERFORMANCE INDEXES FOR REAL-TIME QUERIES
-- =================================================================================================

-- Network sessions performance indexes
CREATE INDEX CONCURRENTLY idx_network_sessions_active_performance ON network_sessions(
    session_type, session_start DESC, connection_quality DESC
) WHERE session_end IS NULL;

CREATE INDEX CONCURRENTLY idx_network_sessions_player_recent ON network_sessions(
    player_id, session_start DESC, session_type
) WHERE session_start > NOW() - INTERVAL '24 hours';

CREATE INDEX CONCURRENTLY idx_network_sessions_ip_analysis ON network_sessions(
    ip_address, session_start DESC, connection_quality
) WHERE session_start > NOW() - INTERVAL '7 days';

-- Network telemetry high-frequency indexes
CREATE INDEX CONCURRENTLY idx_network_telemetry_real_time ON network_telemetry(
    player_id, telemetry_timestamp DESC, connection_stability_score DESC
) WHERE telemetry_timestamp > NOW() - INTERVAL '1 hour';

CREATE INDEX CONCURRENTLY idx_network_telemetry_session_current ON network_telemetry(
    session_id, telemetry_timestamp DESC
) WHERE telemetry_timestamp > NOW() - INTERVAL '5 minutes';

CREATE INDEX CONCURRENTLY idx_network_telemetry_quality_trends ON network_telemetry(
    telemetry_timestamp DESC, network_quality_score, rtt_ms
) WHERE telemetry_timestamp > NOW() - INTERVAL '24 hours';

-- Spatial metrics real-time monitoring
CREATE INDEX CONCURRENTLY idx_spatial_cell_metrics_real_time ON spatial_cell_metrics(
    metric_timestamp DESC, grid_cell_x, grid_cell_y, active_players DESC
) WHERE metric_timestamp > NOW() - INTERVAL '1 hour';

CREATE INDEX CONCURRENTLY idx_spatial_cell_metrics_load_monitoring ON spatial_cell_metrics(
    cell_load_percentage DESC, metric_timestamp DESC, active_players
) WHERE metric_timestamp > NOW() - INTERVAL '30 minutes';

-- Delta compression performance tracking
CREATE INDEX CONCURRENTLY idx_delta_compression_stats_real_time ON delta_compression_stats(
    compression_timestamp DESC, compression_ratio DESC, compression_time_us
) WHERE compression_timestamp > NOW() - INTERVAL '1 hour';

CREATE INDEX CONCURRENTLY idx_delta_compression_stats_efficiency ON delta_compression_stats(
    spatial_cell_x, spatial_cell_y, compression_timestamp DESC, compression_ratio
) WHERE compression_timestamp > NOW() - INTERVAL '6 hours';

-- UDP packet statistics real-time analysis
CREATE INDEX CONCURRENTLY idx_udp_packet_stats_real_time ON udp_packet_stats(
    packet_timestamp DESC, packet_type, packet_size_bytes
) WHERE packet_timestamp > NOW() - INTERVAL '5 minutes';

CREATE INDEX CONCURRENTLY idx_udp_packet_stats_delivery ON udp_packet_stats(
    delivery_confirmed, packet_timestamp DESC, packet_type
) WHERE packet_timestamp > NOW() - INTERVAL '1 hour' AND delivery_confirmed = false;

-- Performance metrics monitoring
CREATE INDEX CONCURRENTLY idx_network_performance_metrics_real_time ON network_performance_metrics(
    metric_timestamp DESC, active_udp_connections DESC, packet_loss_rate_percentage
) WHERE metric_timestamp > NOW() - INTERVAL '30 minutes';

CREATE INDEX CONCURRENTLY idx_network_performance_metrics_cpu_monitoring ON network_performance_metrics(
    cpu_usage_percentage DESC, metric_timestamp DESC
) WHERE metric_timestamp > NOW() - INTERVAL '1 hour';

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS AND MONITORING
-- =================================================================================================

-- Player network performance summary
CREATE MATERIALIZED VIEW player_network_performance AS
SELECT
    p.player_id,
    p.total_sessions,
    p.avg_session_duration_minutes,
    p.best_connection_quality,
    p.worst_connection_quality,
    p.avg_rtt_ms,
    p.avg_packet_loss,
    p.preferred_connection_type,
    p.network_stability_score,
    p.bandwidth_efficiency_score,
    CASE
        WHEN p.network_stability_score >= 0.9 THEN 'excellent'
        WHEN p.network_stability_score >= 0.7 THEN 'good'
        WHEN p.network_stability_score >= 0.5 THEN 'fair'
        ELSE 'poor'
    END as network_tier
FROM (
    SELECT
        nt.player_id,
        COUNT(DISTINCT ns.id) as total_sessions,
        AVG(EXTRACT(EPOCH FROM (ns.session_end - ns.session_start))/60) as avg_session_duration_minutes,
        MAX(ns.connection_quality) as best_connection_quality,
        MIN(ns.connection_quality) as worst_connection_quality,
        AVG(nt.rtt_ms) as avg_rtt_ms,
        AVG(nt.packet_loss_percentage) as avg_packet_loss,
        MODE() WITHIN GROUP (ORDER BY nt.connection_type) as preferred_connection_type,
        AVG(nt.connection_stability_score) as network_stability_score,
        CASE
            WHEN AVG(nt.bandwidth_down_kbps) > 0
            THEN LEAST(AVG(nt.bandwidth_down_kbps) / 100000.0, 1.0)
            ELSE 0.5
        END as bandwidth_efficiency_score
    FROM network_sessions ns
    LEFT JOIN network_telemetry nt ON nt.session_id = ns.id
    WHERE ns.session_start >= CURRENT_DATE - INTERVAL '30 days'
    GROUP BY nt.player_id
) p
WHERE p.player_id IS NOT NULL;

-- Network region performance analysis
CREATE MATERIALIZED VIEW network_region_performance AS
SELECT
    nr.region_code,
    nr.region_name,
    nr.continent,
    rperf.total_players,
    rperf.avg_rtt_ms,
    rperf.avg_packet_loss,
    rperf.connection_success_rate,
    rperf.bandwidth_avg_down_kbps,
    rperf.bandwidth_avg_up_kbps,
    rperf.quality_score_avg,
    CASE
        WHEN rperf.quality_score_avg >= 0.8 THEN 'excellent'
        WHEN rperf.quality_score_avg >= 0.6 THEN 'good'
        WHEN rperf.quality_score_avg >= 0.4 THEN 'fair'
        ELSE 'poor'
    END as region_performance_tier
FROM network_regions nr
LEFT JOIN (
    SELECT
        -- Simple region detection based on IP patterns (would use GeoIP in production)
        CASE
            WHEN ns.ip_address <<= '192.168.0.0/16'::inet THEN 'NA-EAST'
            WHEN ns.ip_address <<= '10.0.0.0/8'::inet THEN 'EU-WEST'
            ELSE 'NA-EAST' -- Default
        END as region_code,
        COUNT(DISTINCT ns.player_id) as total_players,
        AVG(nt.rtt_ms) as avg_rtt_ms,
        AVG(nt.packet_loss_percentage) as avg_packet_loss,
        AVG(CASE WHEN ns.disconnect_reason IS NULL THEN 1.0 ELSE 0.0 END) as connection_success_rate,
        AVG(nt.bandwidth_down_kbps) as bandwidth_avg_down_kbps,
        AVG(nt.bandwidth_up_kbps) as bandwidth_avg_up_kbps,
        AVG(nt.network_quality_score) as quality_score_avg
    FROM network_sessions ns
    LEFT JOIN network_telemetry nt ON nt.session_id = ns.id
    WHERE ns.session_start >= CURRENT_DATE - INTERVAL '7 days'
    GROUP BY
        CASE
            WHEN ns.ip_address <<= '192.168.0.0/16'::inet THEN 'NA-EAST'
            WHEN ns.ip_address <<= '10.0.0.0/8'::inet THEN 'EU-WEST'
            ELSE 'NA-EAST'
        END
) rperf ON rperf.region_code = nr.region_code
WHERE nr.is_active = true;

-- Compression algorithm efficiency analysis
CREATE MATERIALIZED VIEW compression_algorithm_efficiency AS
SELECT
    dcs.compression_timestamp::date as date,
    dcs.algorithm_used,
    COUNT(*) as total_compressions,
    AVG(dcs.compression_ratio) as avg_compression_ratio,
    AVG(dcs.compression_time_us) as avg_compression_time_us,
    AVG(dcs.original_bytes) as avg_original_bytes,
    AVG(dcs.compressed_bytes) as avg_compressed_bytes,
    SUM(dcs.original_bytes) as total_original_bytes,
    SUM(dcs.compressed_bytes) as total_compressed_bytes,
    ROUND(
        (1 - SUM(dcs.compressed_bytes)::numeric / NULLIF(SUM(dcs.original_bytes), 0)) * 100, 2
    ) as overall_compression_efficiency_percentage,
    ROUND(AVG(dcs.compression_time_us), 2) as avg_processing_time_us,
    COUNT(*) FILTER (WHERE dcs.compression_ratio > 0.5) as high_efficiency_count,
    CASE
        WHEN AVG(dcs.compression_ratio) > 0.7 THEN 'excellent'
        WHEN AVG(dcs.compression_ratio) > 0.5 THEN 'good'
        WHEN AVG(dcs.compression_ratio) > 0.3 THEN 'fair'
        ELSE 'poor'
    END as efficiency_tier
FROM delta_compression_stats dcs
WHERE dcs.compression_timestamp >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY dcs.compression_timestamp::date, dcs.algorithm_used;

-- Spatial grid performance analysis
CREATE MATERIALIZED VIEW spatial_grid_performance AS
SELECT
    scm.metric_timestamp::date as date,
    scm.grid_cell_x,
    scm.grid_cell_y,
    COUNT(*) as measurements_count,
    AVG(scm.active_players) as avg_active_players,
    MAX(scm.active_players) as max_active_players,
    AVG(scm.cell_load_percentage) as avg_load_percentage,
    MAX(scm.cell_load_percentage) as max_load_percentage,
    AVG(scm.updates_per_second) as avg_updates_per_second,
    AVG(scm.processing_time_us) as avg_processing_time_us,
    SUM(scm.packets_sent_per_second) as total_packets_sent_per_second,
    SUM(scm.bytes_sent_per_second) as total_bytes_sent_per_second,
    COUNT(*) FILTER (WHERE scm.cell_load_percentage > 80) as overload_events,
    COUNT(*) FILTER (WHERE scm.cell_load_percentage < 20) as underutilized_events,
    CASE
        WHEN AVG(scm.cell_load_percentage) BETWEEN 60 AND 80 THEN 'optimal'
        WHEN AVG(scm.cell_load_percentage) > 80 THEN 'overloaded'
        WHEN AVG(scm.cell_load_percentage) < 40 THEN 'underutilized'
        ELSE 'balanced'
    END as utilization_status
FROM spatial_cell_metrics scm
WHERE scm.metric_timestamp >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY scm.metric_timestamp::date, scm.grid_cell_x, scm.grid_cell_y;

-- Error pattern analysis
CREATE MATERIALIZED VIEW network_error_patterns AS
SELECT
    DATE(ne.error_timestamp) as date,
    ne.error_type,
    ne.component_name,
    ne.severity_level,
    COUNT(*) as error_count,
    COUNT(DISTINCT ne.player_id) as affected_players,
    AVG(ne.recovery_time_ms) FILTER (WHERE ne.recovery_time_ms IS NOT NULL) as avg_recovery_time_ms,
    COUNT(*) FILTER (WHERE ne.recovery_successful = true) as successful_recoveries,
    COUNT(*) FILTER (WHERE ne.recovery_successful = false) as failed_recoveries,
    ROUND(
        COUNT(*) FILTER (WHERE ne.recovery_successful = true)::numeric /
        NULLIF(COUNT(*) FILTER (WHERE ne.recovery_attempted = true), 0) * 100, 2
    ) as recovery_success_rate,
    MODE() WITHIN GROUP (ORDER BY ne.error_message) as most_common_message,
    CASE
        WHEN COUNT(*) > 100 THEN 'critical'
        WHEN COUNT(*) > 50 THEN 'high'
        WHEN COUNT(*) > 20 THEN 'medium'
        ELSE 'low'
    END as frequency_severity
FROM network_error_logs ne
WHERE ne.error_timestamp >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY DATE(ne.error_timestamp), ne.error_type, ne.component_name, ne.severity_level;

-- Real-time performance dashboard
CREATE MATERIALIZED VIEW network_performance_dashboard AS
SELECT
    CURRENT_TIMESTAMP as generated_at,
    -- Connection metrics
    COUNT(DISTINCT ns.id) FILTER (WHERE ns.session_end IS NULL) as active_connections,
    COUNT(DISTINCT ns.id) FILTER (WHERE ns.session_type = 'udp_game' AND ns.session_end IS NULL) as active_udp_connections,
    COUNT(DISTINCT ns.id) FILTER (WHERE ns.session_type = 'websocket_lobby' AND ns.session_end IS NULL) as active_websocket_connections,

    -- Performance metrics (last 5 minutes)
    AVG(nt.rtt_ms) FILTER (WHERE nt.telemetry_timestamp > NOW() - INTERVAL '5 minutes') as avg_rtt_last_5min,
    AVG(nt.packet_loss_percentage) FILTER (WHERE nt.telemetry_timestamp > NOW() - INTERVAL '5 minutes') as avg_loss_last_5min,
    AVG(npm.cpu_usage_percentage) FILTER (WHERE npm.metric_timestamp > NOW() - INTERVAL '5 minutes') as avg_cpu_last_5min,
    AVG(npm.active_udp_connections) FILTER (WHERE npm.metric_timestamp > NOW() - INTERVAL '5 minutes') as avg_udp_connections_last_5min,

    -- Spatial metrics
    COUNT(*) FILTER (WHERE scm.metric_timestamp > NOW() - INTERVAL '5 minutes' AND scm.cell_load_percentage > 80) as overloaded_cells,
    AVG(scm.cell_load_percentage) FILTER (WHERE scm.metric_timestamp > NOW() - INTERVAL '5 minutes') as avg_cell_load,

    -- Compression metrics
    AVG(dcs.compression_ratio) FILTER (WHERE dcs.compression_timestamp > NOW() - INTERVAL '5 minutes') as avg_compression_ratio,
    AVG(dcs.compression_time_us) FILTER (WHERE dcs.compression_timestamp > NOW() - INTERVAL '5 minutes') as avg_compression_time,

    -- Error metrics
    COUNT(*) FILTER (WHERE ne.error_timestamp > NOW() - INTERVAL '5 minutes' AND ne.severity_level = 'high') as high_severity_errors_last_5min,
    COUNT(*) FILTER (WHERE ne.error_timestamp > NOW() - INTERVAL '5 minutes' AND ne.severity_level = 'critical') as critical_errors_last_5min,

    -- Overall health score (0-100)
    ROUND(
        (
            -- Connection stability (30%)
            LEAST(COUNT(DISTINCT ns.id) FILTER (WHERE ns.session_end IS NULL AND ns.connection_quality > 0.7)::numeric / NULLIF(COUNT(DISTINCT ns.id) FILTER (WHERE ns.session_end IS NULL), 0), 1) * 30 +
            -- Performance (30%)
            GREATEST(0, 1 - AVG(nt.rtt_ms) FILTER (WHERE nt.telemetry_timestamp > NOW() - INTERVAL '5 minutes') / 100.0) * 30 +
            -- Resource usage (20%)
            GREATEST(0, 1 - AVG(npm.cpu_usage_percentage) FILTER (WHERE npm.metric_timestamp > NOW() - INTERVAL '5 minutes') / 100.0) * 20 +
            -- Error rate (20%)
            GREATEST(0, 1 - COUNT(*) FILTER (WHERE ne.error_timestamp > NOW() - INTERVAL '5 minutes')::numeric / 100.0) * 20
        ), 1
    ) as overall_health_score
FROM network_sessions ns
LEFT JOIN network_telemetry nt ON nt.session_id = ns.id
LEFT JOIN network_performance_metrics npm ON true -- Cross join for latest metrics
LEFT JOIN spatial_cell_metrics scm ON scm.metric_timestamp > NOW() - INTERVAL '5 minutes'
LEFT JOIN delta_compression_stats dcs ON dcs.compression_timestamp > NOW() - INTERVAL '5 minutes'
LEFT JOIN network_error_logs ne ON ne.error_timestamp > NOW() - INTERVAL '5 minutes'
WHERE ns.session_start > NOW() - INTERVAL '1 hour'; -- Include recent sessions

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_player_network_performance_player ON player_network_performance(player_id);
CREATE INDEX idx_player_network_performance_tier ON player_network_performance(network_tier);

CREATE INDEX idx_network_region_performance_region ON network_region_performance(region_code);
CREATE INDEX idx_network_region_performance_tier ON network_region_performance(region_performance_tier);

CREATE INDEX idx_compression_algorithm_efficiency_date ON compression_algorithm_efficiency(date DESC);
CREATE INDEX idx_compression_algorithm_efficiency_algorithm ON compression_algorithm_efficiency(algorithm_used, date DESC);

CREATE INDEX idx_spatial_grid_performance_date_cell ON spatial_grid_performance(date DESC, grid_cell_x, grid_cell_y);
CREATE INDEX idx_spatial_grid_performance_status ON spatial_grid_performance(utilization_status, date DESC);

CREATE INDEX idx_network_error_patterns_date_type ON network_error_patterns(date DESC, error_type);
CREATE INDEX idx_network_error_patterns_severity ON network_error_patterns(frequency_severity, date DESC);

CREATE INDEX idx_network_performance_dashboard_generated ON network_performance_dashboard(generated_at DESC);

-- =================================================================================================
-- PARTITIONING FOR HIGH-VOLUME TABLES
-- =================================================================================================

-- Partition network_telemetry by day (very high volume)
CREATE TABLE network_telemetry_2025_01_01 PARTITION OF network_telemetry
    FOR VALUES FROM ('2025-01-01') TO ('2025-01-02');

CREATE TABLE network_telemetry_2025_01_02 PARTITION OF network_telemetry
    FOR VALUES FROM ('2025-01-02') TO ('2025-01-03');

-- Partition delta_compression_stats by day
CREATE TABLE delta_compression_stats_2025_01_01 PARTITION OF delta_compression_stats
    FOR VALUES FROM ('2025-01-01') TO ('2025-01-02');

CREATE TABLE delta_compression_stats_2025_01_02 PARTITION OF delta_compression_stats
    FOR VALUES FROM ('2025-01-02') TO ('2025-01-03');

-- Partition udp_packet_stats by hour (extremely high volume)
CREATE TABLE udp_packet_stats_2025_01_01_00 PARTITION OF udp_packet_stats
    FOR VALUES FROM ('2025-01-01 00:00:00') TO ('2025-01-01 01:00:00');

CREATE TABLE udp_packet_stats_2025_01_01_01 PARTITION OF udp_packet_stats
    FOR VALUES FROM ('2025-01-01 01:00:00') TO ('2025-01-01 02:00:00');

-- =================================================================================================
-- PERFORMANCE MONITORING FUNCTIONS
-- =================================================================================================

-- Function to get real-time network health metrics
CREATE OR REPLACE FUNCTION get_network_health_metrics(observation_window_minutes INTEGER DEFAULT 5)
RETURNS TABLE (
    metric_name VARCHAR(50),
    current_value DECIMAL(10,2),
    status VARCHAR(20),
    trend VARCHAR(10),
    details JSONB
) AS $$
DECLARE
    window_start TIMESTAMP WITH TIME ZONE;
BEGIN
    window_start := NOW() - INTERVAL '1 minute' * observation_window_minutes;

    RETURN QUERY
    -- Active connections
    SELECT
        'active_connections'::VARCHAR(50),
        COUNT(DISTINCT ns.id)::DECIMAL(10,2),
        CASE
            WHEN COUNT(DISTINCT ns.id) > 9000 THEN 'critical'
            WHEN COUNT(DISTINCT ns.id) > 7000 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'udp_connections', COUNT(*) FILTER (WHERE ns.session_type = 'udp_game'),
            'websocket_connections', COUNT(*) FILTER (WHERE ns.session_type = 'websocket_lobby')
        )
    FROM network_sessions ns
    WHERE ns.session_end IS NULL

    UNION ALL

    -- Average latency
    SELECT
        'average_latency'::VARCHAR(50),
        ROUND(AVG(nt.rtt_ms), 2),
        CASE
            WHEN AVG(nt.rtt_ms) > 150 THEN 'critical'
            WHEN AVG(nt.rtt_ms) > 75 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'samples', COUNT(*),
            'min_rtt', MIN(nt.rtt_ms),
            'max_rtt', MAX(nt.rtt_ms)
        )
    FROM network_telemetry nt
    WHERE nt.telemetry_timestamp > window_start

    UNION ALL

    -- Packet loss rate
    SELECT
        'packet_loss_rate'::VARCHAR(50),
        ROUND(AVG(nt.packet_loss_percentage), 2),
        CASE
            WHEN AVG(nt.packet_loss_percentage) > 5.0 THEN 'critical'
            WHEN AVG(nt.packet_loss_percentage) > 2.0 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'samples', COUNT(*),
            'loss_trend', 'stable'
        )
    FROM network_telemetry nt
    WHERE nt.telemetry_timestamp > window_start

    UNION ALL

    -- CPU usage
    SELECT
        'cpu_usage'::VARCHAR(50),
        ROUND(AVG(npm.cpu_usage_percentage), 2),
        CASE
            WHEN AVG(npm.cpu_usage_percentage) > 85 THEN 'critical'
            WHEN AVG(npm.cpu_usage_percentage) > 70 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'network_thread_cpu', ROUND(AVG(npm.network_thread_cpu_percentage), 2),
            'samples', COUNT(*)
        )
    FROM network_performance_metrics npm
    WHERE npm.metric_timestamp > window_start

    UNION ALL

    -- Spatial grid overload
    SELECT
        'spatial_overload'::VARCHAR(50),
        COUNT(*)::DECIMAL(10,2),
        CASE
            WHEN COUNT(*) > 10 THEN 'critical'
            WHEN COUNT(*) > 5 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'overloaded_cells', COUNT(*),
            'total_cells', (SELECT COUNT(*) FROM spatial_cell_metrics scm WHERE scm.metric_timestamp > window_start)
        )
    FROM spatial_cell_metrics scm
    WHERE scm.metric_timestamp > window_start
      AND scm.cell_load_percentage > 80

    UNION ALL

    -- Error rate
    SELECT
        'error_rate'::VARCHAR(50),
        ROUND(COUNT(*)::DECIMAL(10,2) / NULLIF(EXTRACT(EPOCH FROM (NOW() - window_start)) / 60, 0), 2),
        CASE
            WHEN COUNT(*)::DECIMAL / NULLIF(EXTRACT(EPOCH FROM (NOW() - window_start)) / 60, 0) > 10 THEN 'critical'
            WHEN COUNT(*)::DECIMAL / NULLIF(EXTRACT(EPOCH FROM (NOW() - window_start)) / 60, 0) > 5 THEN 'warning'
            ELSE 'normal'
        END::VARCHAR(20),
        'stable'::VARCHAR(10),
        jsonb_build_object(
            'total_errors', COUNT(*),
            'high_severity', COUNT(*) FILTER (WHERE nel.severity_level IN ('high', 'critical'))
        )
    FROM network_error_logs nel
    WHERE nel.error_timestamp > window_start;
END;
$$ LANGUAGE plpgsql;

-- Function to analyze player connection quality trends
CREATE OR REPLACE FUNCTION analyze_player_connection_trends(player_id_param BIGINT, days_back INTEGER DEFAULT 7)
RETURNS TABLE (
    date DATE,
    avg_rtt_ms INTEGER,
    avg_packet_loss DECIMAL(5,2),
    avg_connection_quality DECIMAL(3,2),
    sessions_count INTEGER,
    trend_rtt VARCHAR(10),
    trend_loss VARCHAR(10),
    quality_grade VARCHAR(10)
) AS $$
DECLARE
    cutoff_date DATE;
BEGIN
    cutoff_date := CURRENT_DATE - days_back;

    RETURN QUERY
    WITH daily_stats AS (
        SELECT
            DATE(ns.session_start) as session_date,
            AVG(nt.rtt_ms) as avg_rtt,
            AVG(nt.packet_loss_percentage) as avg_loss,
            AVG(ns.connection_quality) as avg_quality,
            COUNT(DISTINCT ns.id) as sessions
        FROM network_sessions ns
        LEFT JOIN network_telemetry nt ON nt.session_id = ns.id
        WHERE ns.player_id = player_id_param
          AND DATE(ns.session_start) >= cutoff_date
        GROUP BY DATE(ns.session_start)
    ),
    trend_analysis AS (
        SELECT
            session_date,
            avg_rtt,
            avg_loss,
            avg_quality,
            sessions,
            -- RTT trend (compared to previous day)
            CASE
                WHEN LAG(avg_rtt) OVER (ORDER BY session_date) IS NULL THEN 'unknown'
                WHEN avg_rtt < LAG(avg_rtt) OVER (ORDER BY session_date) * 0.9 THEN 'improving'
                WHEN avg_rtt > LAG(avg_rtt) OVER (ORDER BY session_date) * 1.1 THEN 'worsening'
                ELSE 'stable'
            END as rtt_trend,
            -- Loss trend
            CASE
                WHEN LAG(avg_loss) OVER (ORDER BY session_date) IS NULL THEN 'unknown'
                WHEN avg_loss < LAG(avg_loss) OVER (ORDER BY session_date) * 0.8 THEN 'improving'
                WHEN avg_loss > LAG(avg_loss) OVER (ORDER BY session_date) * 1.2 THEN 'worsening'
                ELSE 'stable'
            END as loss_trend,
            -- Quality grade
            CASE
                WHEN avg_quality >= 0.9 THEN 'excellent'
                WHEN avg_quality >= 0.7 THEN 'good'
                WHEN avg_quality >= 0.5 THEN 'fair'
                ELSE 'poor'
            END as quality_grade
        FROM daily_stats
    )
    SELECT
        ta.session_date::DATE,
        ta.avg_rtt::INTEGER,
        ROUND(ta.avg_loss, 2)::DECIMAL(5,2),
        ROUND(ta.avg_quality, 2)::DECIMAL(3,2),
        ta.sessions::INTEGER,
        ta.rtt_trend::VARCHAR(10),
        ta.loss_trend::VARCHAR(10),
        ta.quality_grade::VARCHAR(10)
    FROM trend_analysis ta
    ORDER BY ta.session_date DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to get spatial grid optimization recommendations
CREATE OR REPLACE FUNCTION get_spatial_optimization_recommendations()
RETURNS TABLE (
    recommendation_type VARCHAR(50),
    priority VARCHAR(10),
    description TEXT,
    affected_cells INTEGER,
    estimated_impact VARCHAR(20),
    implementation_complexity VARCHAR(10)
) AS $$
BEGIN
    RETURN QUERY
    -- Overloaded cells
    SELECT
        'cell_redistribution'::VARCHAR(50),
        'high'::VARCHAR(10),
        'Redistribute players from overloaded cells to underutilized ones'::TEXT,
        COUNT(*)::INTEGER,
        'high_impact'::VARCHAR(20),
        'medium'::VARCHAR(10)
    FROM spatial_cell_metrics
    WHERE metric_timestamp > NOW() - INTERVAL '1 hour'
      AND cell_load_percentage > 85

    UNION ALL

    -- Underutilized cells
    SELECT
        'cell_consolidation'::VARCHAR(50),
        'medium'::VARCHAR(10),
        'Consolidate players from underutilized cells'::TEXT,
        COUNT(*)::INTEGER,
        'medium_impact'::VARCHAR(20),
        'low'::VARCHAR(10)
    FROM spatial_cell_metrics
    WHERE metric_timestamp > NOW() - INTERVAL '1 hour'
      AND cell_load_percentage < 30

    UNION ALL

    -- High migration frequency
    SELECT
        'migration_optimization'::VARCHAR(50),
        'medium'::VARCHAR(10),
        'Optimize cell boundaries to reduce player migration'::TEXT,
        COUNT(DISTINCT grid_cell_x || ',' || grid_cell_y)::INTEGER,
        'medium_impact'::VARCHAR(20),
        'high'::VARCHAR(10)
    FROM spatial_cell_metrics
    WHERE metric_timestamp > NOW() - INTERVAL '1 hour'
      AND migration_events > 10

    UNION ALL

    -- Network congestion in cells
    SELECT
        'network_optimization'::VARCHAR(50),
        'high'::VARCHAR(10),
        'Implement packet batching for high-traffic cells'::TEXT,
        COUNT(*)::INTEGER,
        'high_impact'::VARCHAR(20),
        'low'::VARCHAR(10)
    FROM spatial_cell_metrics
    WHERE metric_timestamp > NOW() - INTERVAL '30 minutes'
      AND packets_sent_per_second > 2000

    UNION ALL

    -- Visibility radius optimization
    SELECT
        'visibility_tuning'::VARCHAR(50),
        'low'::VARCHAR(10),
        'Adjust visibility radii based on cell density'::TEXT,
        1::INTEGER,
        'low_impact'::VARCHAR(20),
        'low'::VARCHAR(10)
    WHERE EXISTS (
        SELECT 1 FROM spatial_cell_metrics
        WHERE metric_timestamp > NOW() - INTERVAL '1 hour'
          AND active_players > 0
    );
END;
$$ LANGUAGE plpgsql;

-- Function to refresh all network analytics views
CREATE OR REPLACE FUNCTION refresh_network_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY player_network_performance;
    REFRESH MATERIALIZED VIEW CONCURRENTLY network_region_performance;
    REFRESH MATERIALIZED VIEW CONCURRENTLY compression_algorithm_efficiency;
    REFRESH MATERIALIZED VIEW CONCURRENTLY spatial_grid_performance;
    REFRESH MATERIALIZED VIEW CONCURRENTLY network_error_patterns;
    REFRESH MATERIALIZED VIEW network_performance_dashboard; -- Not concurrent due to real-time data

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('realtime-gateway-network', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- AUTOMATED MAINTENANCE PROCEDURES
-- =================================================================================================

-- Function to clean up old network telemetry data (keep 90 days)
CREATE OR REPLACE FUNCTION cleanup_old_network_telemetry()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM network_telemetry
    WHERE telemetry_timestamp < CURRENT_DATE - INTERVAL '90 days';

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to archive old compression stats (keep 60 days)
CREATE OR REPLACE FUNCTION archive_old_compression_stats()
RETURNS INTEGER AS $$
DECLARE
    archived_count INTEGER;
BEGIN
    -- Move old compression stats to archive table (would create archive table in production)
    -- For now, just delete
    DELETE FROM delta_compression_stats
    WHERE compression_timestamp < CURRENT_DATE - INTERVAL '60 days';

    GET DIAGNOSTICS archived_count = ROW_COUNT;
    RETURN archived_count;
END;
$$ LANGUAGE plpgsql;

-- Function to aggregate daily network statistics
CREATE OR REPLACE FUNCTION aggregate_daily_network_stats(target_date DATE DEFAULT CURRENT_DATE - 1)
RETURNS VOID AS $$
BEGIN
    -- Update connection quality stats
    INSERT INTO connection_quality_stats (
        stat_date, player_id, sessions_count, total_session_time_minutes,
        average_connection_quality, average_rtt_ms, average_packet_loss_percentage
    )
    SELECT
        target_date,
        ns.player_id,
        COUNT(DISTINCT ns.id),
        SUM(EXTRACT(EPOCH FROM (
            COALESCE(ns.session_end, NOW()) - ns.session_start
        )) / 60),
        AVG(ns.connection_quality),
        AVG(nt.rtt_ms),
        AVG(nt.packet_loss_percentage)
    FROM network_sessions ns
    LEFT JOIN network_telemetry nt ON nt.session_id = ns.id
    WHERE DATE(ns.session_start) = target_date
    GROUP BY ns.player_id
    ON CONFLICT (stat_date, player_id) DO UPDATE SET
        sessions_count = EXCLUDED.sessions_count,
        total_session_time_minutes = EXCLUDED.total_session_time_minutes,
        average_connection_quality = EXCLUDED.average_connection_quality,
        average_rtt_ms = EXCLUDED.average_rtt_ms,
        average_packet_loss_percentage = EXCLUDED.average_packet_loss_percentage;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- DATA INTEGRITY VALIDATION
-- =================================================================================================

-- Function to validate network data integrity
CREATE OR REPLACE FUNCTION validate_network_data_integrity()
RETURNS TABLE (
    integrity_issue VARCHAR(100),
    severity VARCHAR(10),
    affected_records BIGINT,
    description TEXT
) AS $$
BEGIN
    -- Check for sessions without telemetry
    RETURN QUERY
    SELECT
        'sessions_without_telemetry'::VARCHAR(100),
        'LOW'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Active sessions without recent telemetry data'::TEXT
    FROM network_sessions ns
    WHERE ns.session_end IS NULL
      AND NOT EXISTS (
          SELECT 1 FROM network_telemetry nt
          WHERE nt.session_id = ns.id
            AND nt.telemetry_timestamp > NOW() - INTERVAL '10 minutes'
      );

    -- Check for orphaned telemetry records
    RETURN QUERY
    SELECT
        'orphaned_telemetry'::VARCHAR(100),
        'MEDIUM'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Telemetry records for non-existent sessions'::TEXT
    FROM network_telemetry nt
    WHERE NOT EXISTS (
        SELECT 1 FROM network_sessions ns WHERE ns.id = nt.session_id
    );

    -- Check for invalid spatial coordinates
    RETURN QUERY
    SELECT
        'invalid_spatial_coordinates'::VARCHAR(100),
        'HIGH'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Spatial metrics with invalid coordinates'::TEXT
    FROM spatial_cell_metrics scm
    WHERE scm.grid_cell_x < -1000 OR scm.grid_cell_x > 1000
       OR scm.grid_cell_y < -1000 OR scm.grid_cell_y > 1000;

    -- Check for negative performance metrics
    RETURN QUERY
    SELECT
        'negative_performance_metrics'::VARCHAR(100),
        'MEDIUM'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Performance metrics with negative values'::TEXT
    FROM network_performance_metrics npm
    WHERE npm.cpu_usage_percentage < 0
       OR npm.memory_usage_percentage < 0
       OR npm.network_bytes_in_per_second < 0;

    -- Check for compression ratios > 1.0
    RETURN QUERY
    SELECT
        'invalid_compression_ratios'::VARCHAR(100),
        'LOW'::VARCHAR(10),
        COUNT(*)::BIGINT,
        'Compression ratios greater than 1.0 (impossible)'::TEXT
    FROM delta_compression_stats dcs
    WHERE dcs.compression_ratio > 1.0;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- SCHEDULED MAINTENANCE SETUP
-- =================================================================================================

-- Create maintenance schedule (would be called by cron/external scheduler)
CREATE OR REPLACE FUNCTION perform_network_maintenance()
RETURNS TABLE (
    maintenance_task VARCHAR(50),
    records_processed INTEGER,
    execution_time_ms INTEGER,
    status VARCHAR(20)
) AS $$
DECLARE
    start_time TIMESTAMP WITH TIME ZONE;
    end_time TIMESTAMP WITH TIME ZONE;
    processed_count INTEGER;
BEGIN
    -- Cleanup old telemetry
    start_time := NOW();
    SELECT cleanup_old_network_telemetry() INTO processed_count;
    end_time := NOW();

    RETURN QUERY SELECT
        'cleanup_telemetry'::VARCHAR(50),
        processed_count,
        EXTRACT(EPOCH FROM (end_time - start_time) * 1000)::INTEGER,
        'completed'::VARCHAR(20);

    -- Archive old compression stats
    start_time := NOW();
    SELECT archive_old_compression_stats() INTO processed_count;
    end_time := NOW();

    RETURN QUERY SELECT
        'archive_compression'::VARCHAR(50),
        processed_count,
        EXTRACT(EPOCH FROM (end_time - start_time) * 1000)::INTEGER,
        'completed'::VARCHAR(20);

    -- Aggregate daily stats
    start_time := NOW();
    PERFORM aggregate_daily_network_stats();
    end_time := NOW();

    RETURN QUERY SELECT
        'aggregate_daily_stats'::VARCHAR(50),
        1, -- Dummy count
        EXTRACT(EPOCH FROM (end_time - start_time) * 1000)::INTEGER,
        'completed'::VARCHAR(20);

    -- Refresh analytics views
    start_time := NOW();
    PERFORM refresh_network_analytics();
    end_time := NOW();

    RETURN QUERY SELECT
        'refresh_analytics'::VARCHAR(50),
        1, -- Dummy count
        EXTRACT(EPOCH FROM (end_time - start_time) * 1000)::INTEGER,
        'completed'::VARCHAR(20);

    -- Validate data integrity
    start_time := NOW();
    PERFORM validate_network_data_integrity();
    end_time := NOW();

    RETURN QUERY SELECT
        'validate_integrity'::VARCHAR(50),
        1, -- Dummy count
        EXTRACT(EPOCH FROM (end_time - start_time) * 1000)::INTEGER,
        'completed'::VARCHAR(20);
END;
$$ LANGUAGE plpgsql;
