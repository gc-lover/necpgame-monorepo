-- Realtime Gateway Network Database Schema
-- Version: V001
-- Description: Complete database schema for realtime-gateway network metrics, telemetry, and performance monitoring

-- =================================================================================================
-- NETWORK SESSIONS AND CONNECTIONS
-- =================================================================================================

-- Network sessions (active and historical connection tracking)
CREATE TABLE network_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id BIGINT NOT NULL,
    session_type VARCHAR(20) NOT NULL, -- 'udp_game', 'websocket_lobby', 'admin'

    -- Connection details
    ip_address INET NOT NULL,
    user_agent TEXT,
    client_version VARCHAR(50),

    -- UDP specific fields
    udp_port INTEGER,
    local_udp_port INTEGER,
    nat_type VARCHAR(20), -- 'open', 'moderate', 'strict', 'unknown'

    -- Timing
    session_start TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    session_end TIMESTAMP WITH TIME ZONE,
    last_activity TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Connection quality metrics
    connection_quality DECIMAL(3,2) DEFAULT 1.0, -- 0.0 to 1.0
    initial_ping_ms INTEGER,
    average_ping_ms INTEGER,
    max_ping_ms INTEGER,
    min_ping_ms INTEGER,

    -- Session statistics
    bytes_sent BIGINT DEFAULT 0,
    bytes_received BIGINT DEFAULT 0,
    packets_sent BIGINT DEFAULT 0,
    packets_received BIGINT DEFAULT 0,
    packets_lost BIGINT DEFAULT 0,

    -- Disconnect reason
    disconnect_reason VARCHAR(100),
    disconnect_initiated_by VARCHAR(20), -- 'client', 'server', 'timeout', 'error'

    -- Metadata
    session_metadata JSONB NOT NULL DEFAULT '{}',
    security_flags JSONB NOT NULL DEFAULT '[]', -- Array of security events/issues

    CONSTRAINT valid_session_type CHECK (session_type IN (
        'udp_game', 'websocket_lobby', 'websocket_admin', 'rest_api'
    )),
    CONSTRAINT valid_nat_type CHECK (nat_type IN (
        'open', 'moderate', 'strict', 'unknown'
    )),
    CONSTRAINT valid_disconnect_initiated_by CHECK (disconnect_initiated_by IN (
        'client', 'server', 'timeout', 'error', 'network', 'protocol_error'
    )),
    CONSTRAINT valid_connection_quality CHECK (connection_quality >= 0.0 AND connection_quality <= 1.0),
    CONSTRAINT session_end_after_start CHECK (
        session_end IS NULL OR session_end >= session_start
    )
);

-- =================================================================================================
-- SPATIAL PARTITIONING METRICS
-- =================================================================================================

-- Spatial grid cell metrics
CREATE TABLE spatial_cell_metrics (
    id BIGSERIAL PRIMARY KEY,
    metric_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Grid configuration
    grid_cell_x INTEGER NOT NULL,
    grid_cell_y INTEGER NOT NULL,
    cell_bounds JSONB NOT NULL, -- {"min_x": float, "max_x": float, "min_y": float, "max_y": float}

    -- Cell statistics
    active_players INTEGER NOT NULL DEFAULT 0,
    max_players INTEGER NOT NULL DEFAULT 0,
    player_capacity INTEGER NOT NULL DEFAULT 32,

    -- Performance metrics
    updates_per_second DECIMAL(6,2) DEFAULT 0,
    average_processing_time_us INTEGER DEFAULT 0,
    max_processing_time_us INTEGER DEFAULT 0,

    -- Network metrics for this cell
    packets_sent_per_second DECIMAL(6,2) DEFAULT 0,
    bytes_sent_per_second DECIMAL(8,2) DEFAULT 0,
    packets_received_per_second DECIMAL(6,2) DEFAULT 0,
    bytes_received_per_second DECIMAL(8,2) DEFAULT 0,

    -- Cell health
    cell_load_percentage DECIMAL(5,2) DEFAULT 0, -- 0.00 to 100.00
    migration_events INTEGER DEFAULT 0,
    boundary_crossings INTEGER DEFAULT 0,

    -- Metadata
    cell_metadata JSONB NOT NULL DEFAULT '{}',

    UNIQUE(metric_timestamp, grid_cell_x, grid_cell_y)
);

-- Spatial grid global metrics
CREATE TABLE spatial_grid_global_metrics (
    id BIGSERIAL PRIMARY KEY,
    metric_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Grid overview
    total_active_cells INTEGER NOT NULL DEFAULT 0,
    total_players INTEGER NOT NULL DEFAULT 0,
    average_players_per_cell DECIMAL(5,2) DEFAULT 0,

    -- Performance overview
    total_updates_per_second DECIMAL(8,2) DEFAULT 0,
    average_cell_processing_time_us INTEGER DEFAULT 0,
    max_cell_processing_time_us INTEGER DEFAULT 0,

    -- Network overview
    total_packets_sent_per_second DECIMAL(8,2) DEFAULT 0,
    total_bytes_sent_per_second DECIMAL(10,2) DEFAULT 0,
    total_packets_received_per_second DECIMAL(8,2) DEFAULT 0,
    total_bytes_received_per_second DECIMAL(10,2) DEFAULT 0,

    -- Load distribution
    cells_over_capacity INTEGER DEFAULT 0,
    cells_under_utilized INTEGER DEFAULT 0,
    load_balance_score DECIMAL(5,2) DEFAULT 0, -- 0.00 to 100.00

    -- Migration statistics
    total_migration_events INTEGER DEFAULT 0,
    migration_events_per_second DECIMAL(6,2) DEFAULT 0,
    average_migration_time_us INTEGER DEFAULT 0,

    -- Metadata
    grid_metadata JSONB NOT NULL DEFAULT '{}'
);

-- =================================================================================================
-- NETWORK TELEMETRY AND PERFORMANCE
-- =================================================================================================

-- Detailed network telemetry per player/session
CREATE TABLE network_telemetry (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID REFERENCES network_sessions(id),

    -- Player and timing
    player_id BIGINT NOT NULL,
    telemetry_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Core network metrics
    rtt_ms INTEGER, -- Round trip time
    jitter_ms INTEGER, -- Jitter in milliseconds
    packet_loss_percentage DECIMAL(5,2), -- 0.00 to 100.00
    out_of_order_packets INTEGER DEFAULT 0,

    -- Bandwidth metrics
    bandwidth_up_kbps INTEGER,
    bandwidth_down_kbps INTEGER,
    bandwidth_up_max_kbps INTEGER,
    bandwidth_down_max_kbps INTEGER,

    -- UDP specific metrics
    udp_packets_sent BIGINT DEFAULT 0,
    udp_packets_received BIGINT DEFAULT 0,
    udp_packets_lost BIGINT DEFAULT 0,
    udp_bytes_sent BIGINT DEFAULT 0,
    udp_bytes_received BIGINT DEFAULT 0,

    -- WebSocket specific metrics (if applicable)
    websocket_messages_sent BIGINT DEFAULT 0,
    websocket_messages_received BIGINT DEFAULT 0,
    websocket_bytes_sent BIGINT DEFAULT 0,
    websocket_bytes_received BIGINT DEFAULT 0,

    -- Quality indicators
    connection_stability_score DECIMAL(3,2), -- 0.0 to 1.0
    network_quality_score DECIMAL(3,2), -- 0.0 to 1.0

    -- Geographic and network info
    client_region VARCHAR(50),
    isp_name VARCHAR(100),
    connection_type VARCHAR(20), -- 'wired', 'wifi', 'mobile', 'unknown'

    -- Metadata
    telemetry_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_packet_loss CHECK (packet_loss_percentage >= 0.0 AND packet_loss_percentage <= 100.0),
    CONSTRAINT valid_connection_stability CHECK (connection_stability_score >= 0.0 AND connection_stability_score <= 1.0),
    CONSTRAINT valid_network_quality CHECK (network_quality_score >= 0.0 AND network_quality_score <= 1.0),
    CONSTRAINT valid_connection_type CHECK (connection_type IN (
        'wired', 'wifi', 'mobile_3g', 'mobile_4g', 'mobile_5g', 'satellite', 'unknown'
    ))
);

-- =================================================================================================
-- DELTA COMPRESSION AND OPTIMIZATION METRICS
-- =================================================================================================

-- Delta compression statistics
CREATE TABLE delta_compression_stats (
    id BIGSERIAL PRIMARY KEY,
    compression_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Compression performance
    original_bytes BIGINT NOT NULL,
    compressed_bytes BIGINT NOT NULL,
    compression_ratio DECIMAL(6,4), -- compressed/original
    compression_time_us INTEGER NOT NULL,

    -- Compression algorithm details
    algorithm_used VARCHAR(20) NOT NULL, -- 'coordinate_quantization', 'delta_encoding', 'bit_packing'
    quantization_precision DECIMAL(4,3), -- For coordinate quantization
    delta_fields_compressed INTEGER, -- Number of fields with deltas
    total_fields INTEGER, -- Total fields in update

    -- Change detection metrics
    position_changed BOOLEAN DEFAULT false,
    rotation_changed BOOLEAN DEFAULT false,
    velocity_changed BOOLEAN DEFAULT false,
    health_changed BOOLEAN DEFAULT false,
    weapon_changed BOOLEAN DEFAULT false,
    action_changed BOOLEAN DEFAULT false,

    -- Batch processing info
    batch_size INTEGER DEFAULT 1, -- How many updates batched together
    batch_compression_ratio DECIMAL(6,4),

    -- Context
    player_count_nearby INTEGER, -- Players in visibility range
    spatial_cell_x INTEGER,
    spatial_cell_y INTEGER,

    -- Metadata
    compression_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_compression_ratio CHECK (compression_ratio > 0),
    CONSTRAINT valid_batch_compression_ratio CHECK (batch_compression_ratio > 0),
    CONSTRAINT valid_algorithm CHECK (algorithm_used IN (
        'coordinate_quantization', 'delta_encoding', 'bit_packing',
        'huffman_coding', 'run_length_encoding', 'lz4_compression'
    )),
    CONSTRAINT valid_quantization_precision CHECK (quantization_precision > 0)
);

-- =================================================================================================
-- UDP AND WEBSOCKET PROTOCOL METRICS
-- =================================================================================================

-- UDP packet statistics
CREATE TABLE udp_packet_stats (
    id BIGSERIAL PRIMARY KEY,
    packet_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Packet identification
    packet_type VARCHAR(30) NOT NULL, -- 'player_update', 'combat_action', 'spatial_update', etc.
    sequence_number INTEGER,
    ack_sequence_number INTEGER,

    -- Packet metrics
    packet_size_bytes INTEGER NOT NULL,
    send_attempts INTEGER DEFAULT 1,
    delivery_confirmed BOOLEAN DEFAULT false,
    delivery_time_ms INTEGER,

    -- Error tracking
    packet_lost BOOLEAN DEFAULT false,
    corruption_detected BOOLEAN DEFAULT false,
    duplicate_detected BOOLEAN DEFAULT false,

    -- Routing info
    source_player_id BIGINT,
    target_player_count INTEGER DEFAULT 1,
    spatial_cell_x INTEGER,
    spatial_cell_y INTEGER,

    -- Quality metrics
    packet_priority VARCHAR(10) DEFAULT 'normal', -- 'low', 'normal', 'high', 'critical'
    reliability_required BOOLEAN DEFAULT false,

    -- Metadata
    packet_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_packet_type CHECK (packet_type IN (
        'player_update', 'combat_action', 'spatial_update', 'heartbeat',
        'bulk_update', 'critical_event', 'system_message'
    )),
    CONSTRAINT valid_packet_priority CHECK (packet_priority IN (
        'low', 'normal', 'high', 'critical'
    )),
    CONSTRAINT valid_packet_size CHECK (packet_size_bytes > 0 AND packet_size_bytes <= 1500)
);

-- WebSocket session statistics
CREATE TABLE websocket_session_stats (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID REFERENCES network_sessions(id),

    -- Session info
    player_id BIGINT NOT NULL,
    session_start TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    session_end TIMESTAMP WITH TIME ZONE,

    -- Message statistics
    messages_sent BIGINT DEFAULT 0,
    messages_received BIGINT DEFAULT 0,
    bytes_sent BIGINT DEFAULT 0,
    bytes_received BIGINT DEFAULT 0,

    -- Connection quality
    connection_drops INTEGER DEFAULT 0,
    average_latency_ms INTEGER,
    max_latency_ms INTEGER,

    -- Message types breakdown
    chat_messages BIGINT DEFAULT 0,
    lobby_updates BIGINT DEFAULT 0,
    player_ready_signals BIGINT DEFAULT 0,
    system_notifications BIGINT DEFAULT 0,

    -- Compression stats
    compression_enabled BOOLEAN DEFAULT false,
    original_bytes BIGINT DEFAULT 0,
    compressed_bytes BIGINT DEFAULT 0,

    -- Metadata
    websocket_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT session_end_after_start CHECK (
        session_end IS NULL OR session_end >= session_start
    )
);

-- =================================================================================================
-- PERFORMANCE MONITORING AND ANALYTICS
-- =================================================================================================

-- Real-time performance metrics
CREATE TABLE network_performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    metric_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- System-wide metrics
    active_udp_connections INTEGER DEFAULT 0,
    active_websocket_connections INTEGER DEFAULT 0,
    total_active_players INTEGER DEFAULT 0,

    -- Processing performance
    average_packet_processing_time_us INTEGER,
    max_packet_processing_time_us INTEGER,
    packets_processed_per_second DECIMAL(8,2),

    -- Memory usage
    buffer_pool_utilization_percentage DECIMAL(5,2),
    compression_buffer_usage_percentage DECIMAL(5,2),

    -- CPU usage
    cpu_usage_percentage DECIMAL(5,2),
    network_thread_cpu_percentage DECIMAL(5,2),

    -- Network I/O
    network_bytes_in_per_second DECIMAL(10,2),
    network_bytes_out_per_second DECIMAL(10,2),
    network_packets_in_per_second DECIMAL(8,2),
    network_packets_out_per_second DECIMAL(8,2),

    -- Queue metrics
    packet_queue_length INTEGER DEFAULT 0,
    max_packet_queue_length INTEGER DEFAULT 0,
    queue_processing_time_us INTEGER,

    -- Error rates
    packet_loss_rate_percentage DECIMAL(5,2),
    connection_error_rate_percentage DECIMAL(5,2),
    timeout_rate_percentage DECIMAL(5,2),

    -- Tick rate metrics
    current_tick_rate_hz DECIMAL(5,2),
    adaptive_tick_rate_enabled BOOLEAN DEFAULT true,
    tick_rate_adjustments INTEGER DEFAULT 0,

    -- Metadata
    performance_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_buffer_pool_utilization CHECK (buffer_pool_utilization_percentage >= 0.0 AND buffer_pool_utilization_percentage <= 100.0),
    CONSTRAINT valid_compression_buffer_usage CHECK (compression_buffer_usage_percentage >= 0.0 AND compression_buffer_usage_percentage <= 100.0),
    CONSTRAINT valid_cpu_usage CHECK (cpu_usage_percentage >= 0.0 AND cpu_usage_percentage <= 100.0),
    CONSTRAINT valid_network_thread_cpu CHECK (network_thread_cpu_percentage >= 0.0 AND network_thread_cpu_percentage <= 100.0),
    CONSTRAINT valid_packet_loss_rate CHECK (packet_loss_rate_percentage >= 0.0 AND packet_loss_rate_percentage <= 100.0),
    CONSTRAINT valid_connection_error_rate CHECK (connection_error_rate_percentage >= 0.0 AND connection_error_rate_percentage <= 100.0),
    CONSTRAINT valid_timeout_rate CHECK (timeout_rate_percentage >= 0.0 AND timeout_rate_percentage <= 100.0)
);

-- Connection quality statistics (aggregated)
CREATE TABLE connection_quality_stats (
    id BIGSERIAL PRIMARY KEY,
    stat_date DATE NOT NULL,
    player_id BIGINT NOT NULL,

    -- Daily aggregates
    sessions_count INTEGER DEFAULT 0,
    total_session_time_minutes INTEGER DEFAULT 0,
    average_connection_quality DECIMAL(3,2),

    -- Network performance aggregates
    average_rtt_ms INTEGER,
    average_jitter_ms INTEGER,
    average_packet_loss_percentage DECIMAL(5,2),
    average_bandwidth_down_kbps INTEGER,
    average_bandwidth_up_kbps INTEGER,

    -- Connection stability
    connection_drops INTEGER DEFAULT 0,
    successful_reconnects INTEGER DEFAULT 0,
    average_session_length_minutes INTEGER,

    -- Geographic and network context
    primary_region VARCHAR(50),
    primary_isp VARCHAR(100),
    primary_connection_type VARCHAR(20),

    -- Quality scores
    overall_quality_score DECIMAL(3,2), -- 0.0 to 1.0
    network_stability_score DECIMAL(3,2), -- 0.0 to 1.0
    performance_score DECIMAL(3,2), -- 0.0 to 1.0

    -- Trends
    quality_trend VARCHAR(10), -- 'improving', 'stable', 'degrading', 'unknown'
    performance_trend VARCHAR(10), -- 'improving', 'stable', 'degrading', 'unknown'

    -- Metadata
    quality_metadata JSONB NOT NULL DEFAULT '{}',

    UNIQUE(stat_date, player_id)
);

-- =================================================================================================
-- ADAPTIVE SYSTEMS METRICS
-- =================================================================================================

-- Tick rate adaptation metrics
CREATE TABLE tick_rate_adaptation_metrics (
    id BIGSERIAL PRIMARY KEY,
    adaptation_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Current state
    current_tick_rate_hz DECIMAL(5,2) NOT NULL,
    target_tick_rate_hz DECIMAL(5,2),
    adaptation_reason VARCHAR(50), -- 'player_count', 'load_balance', 'network_congestion'

    -- Trigger conditions
    active_players INTEGER NOT NULL,
    network_load_percentage DECIMAL(5,2),
    average_latency_ms INTEGER,

    -- Adaptation results
    adaptation_successful BOOLEAN NOT NULL,
    previous_tick_rate_hz DECIMAL(5,2),
    rate_change_percentage DECIMAL(6,2),

    -- Performance impact
    latency_change_ms INTEGER,
    packet_loss_change_percentage DECIMAL(5,2),
    bandwidth_change_kbps INTEGER,

    -- Metadata
    adaptation_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_adaptation_reason CHECK (adaptation_reason IN (
        'player_count', 'network_load', 'cpu_usage', 'memory_pressure',
        'bandwidth_limit', 'latency_spike', 'manual_override'
    )),
    CONSTRAINT valid_current_tick_rate CHECK (current_tick_rate_hz >= 1.0 AND current_tick_rate_hz <= 256.0),
    CONSTRAINT valid_target_tick_rate CHECK (target_tick_rate_hz >= 1.0 AND target_tick_rate_hz <= 256.0)
);

-- Spatial interest management metrics
CREATE TABLE spatial_interest_metrics (
    id BIGSERIAL PRIMARY KEY,
    metric_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Interest calculation metrics
    total_players INTEGER NOT NULL DEFAULT 0,
    average_visibility_radius_meters DECIMAL(6,2),
    max_visibility_radius_meters DECIMAL(6,2),

    -- Processing performance
    interest_calculation_time_us INTEGER,
    average_players_visible DECIMAL(5,2),
    max_players_visible INTEGER,

    -- Interest distribution
    players_with_full_visibility INTEGER DEFAULT 0, -- All nearby players visible
    players_with_limited_visibility INTEGER DEFAULT 0, -- Some players culled
    players_with_restricted_visibility INTEGER DEFAULT 0, -- Most players culled

    -- Distance-based metrics
    close_range_players_avg DECIMAL(5,2), -- <25m
    medium_range_players_avg DECIMAL(5,2), -- 25-75m
    long_range_players_avg DECIMAL(5,2), -- 75-150m

    -- Activity-based culling
    culled_inactive_players INTEGER DEFAULT 0,
    culled_background_players INTEGER DEFAULT 0,

    -- Metadata
    interest_metadata JSONB NOT NULL DEFAULT '{}'
);

-- =================================================================================================
-- ERROR TRACKING AND DIAGNOSTICS
-- =================================================================================================

-- Network error tracking
CREATE TABLE network_error_logs (
    id BIGSERIAL PRIMARY KEY,
    error_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Error classification
    error_type VARCHAR(50) NOT NULL,
    error_severity VARCHAR(10) NOT NULL, -- 'low', 'medium', 'high', 'critical'

    -- Context
    player_id BIGINT,
    session_id UUID REFERENCES network_sessions(id),
    component_name VARCHAR(50), -- 'udp_server', 'spatial_grid', 'delta_compression', etc.

    -- Error details
    error_message TEXT NOT NULL,
    error_code VARCHAR(20),
    stack_trace TEXT,

    -- Network context
    ip_address INET,
    user_agent TEXT,
    client_version VARCHAR(50),

    -- Recovery information
    recovery_attempted BOOLEAN DEFAULT false,
    recovery_successful BOOLEAN DEFAULT false,
    recovery_time_ms INTEGER,

    -- Metadata
    error_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_error_type CHECK (error_type IN (
        'connection_failed', 'packet_corruption', 'timeout', 'buffer_overflow',
        'protocol_error', 'spatial_calculation_error', 'compression_error',
        'authentication_failed', 'rate_limit_exceeded', 'system_resource_error'
    )),
    CONSTRAINT valid_error_severity CHECK (error_severity IN (
        'low', 'medium', 'high', 'critical'
    )),
    CONSTRAINT valid_component_name CHECK (component_name IN (
        'udp_server', 'websocket_server', 'spatial_grid_manager',
        'delta_compression_engine', 'adaptive_tick_controller',
        'network_buffer_pool', 'coordinate_quantization',
        'batch_network_writer', 'protobuf_handler', 'session_manager'
    ))
);

-- =================================================================================================
-- INDEXES
-- =================================================================================================

-- Network sessions indexes
CREATE INDEX idx_network_sessions_player_type ON network_sessions(player_id, session_type, session_start DESC);
CREATE INDEX idx_network_sessions_active ON network_sessions(session_end) WHERE session_end IS NULL;
CREATE INDEX idx_network_sessions_ip ON network_sessions(ip_address, session_start DESC);
CREATE INDEX idx_network_sessions_quality ON network_sessions(connection_quality DESC, session_start DESC);

-- Spatial cell metrics indexes
CREATE INDEX idx_spatial_cell_metrics_timestamp_cell ON spatial_cell_metrics(metric_timestamp DESC, grid_cell_x, grid_cell_y);
CREATE INDEX idx_spatial_cell_metrics_load ON spatial_cell_metrics(cell_load_percentage DESC, metric_timestamp DESC);

-- Spatial grid global metrics indexes
CREATE INDEX idx_spatial_grid_global_metrics_timestamp ON spatial_grid_global_metrics(metric_timestamp DESC);
CREATE INDEX idx_spatial_grid_global_metrics_load_balance ON spatial_grid_global_metrics(load_balance_score, metric_timestamp DESC);

-- Network telemetry indexes
CREATE INDEX idx_network_telemetry_player_timestamp ON network_telemetry(player_id, telemetry_timestamp DESC);
CREATE INDEX idx_network_telemetry_session ON network_telemetry(session_id, telemetry_timestamp DESC);
CREATE INDEX idx_network_telemetry_quality ON network_telemetry(network_quality_score, telemetry_timestamp DESC);

-- Delta compression stats indexes
CREATE INDEX idx_delta_compression_stats_timestamp ON delta_compression_stats(compression_timestamp DESC);
CREATE INDEX idx_delta_compression_stats_ratio ON delta_compression_stats(compression_ratio, compression_timestamp DESC);
CREATE INDEX idx_delta_compression_stats_algorithm ON delta_compression_stats(algorithm_used, compression_timestamp DESC);

-- UDP packet stats indexes
CREATE INDEX idx_udp_packet_stats_timestamp_type ON udp_packet_stats(packet_timestamp DESC, packet_type);
CREATE INDEX idx_udp_packet_stats_player ON udp_packet_stats(source_player_id, packet_timestamp DESC);
CREATE INDEX idx_udp_packet_stats_size ON udp_packet_stats(packet_size_bytes, packet_timestamp DESC);

-- WebSocket session stats indexes
CREATE INDEX idx_websocket_session_stats_session ON websocket_session_stats(session_id, session_start DESC);
CREATE INDEX idx_websocket_session_stats_player ON websocket_session_stats(player_id, session_start DESC);

-- Network performance metrics indexes
CREATE INDEX idx_network_performance_metrics_timestamp ON network_performance_metrics(metric_timestamp DESC);
CREATE INDEX idx_network_performance_metrics_cpu ON network_performance_metrics(cpu_usage_percentage DESC, metric_timestamp DESC);

-- Connection quality stats indexes
CREATE INDEX idx_connection_quality_stats_date_player ON connection_quality_stats(stat_date DESC, player_id);
CREATE INDEX idx_connection_quality_stats_quality ON connection_quality_stats(overall_quality_score DESC, stat_date DESC);

-- Tick rate adaptation indexes
CREATE INDEX idx_tick_rate_adaptation_timestamp ON tick_rate_adaptation_metrics(adaptation_timestamp DESC);
CREATE INDEX idx_tick_rate_adaptation_reason ON tick_rate_adaptation_metrics(adaptation_reason, adaptation_timestamp DESC);

-- Spatial interest metrics indexes
CREATE INDEX idx_spatial_interest_metrics_timestamp ON spatial_interest_metrics(metric_timestamp DESC);

-- Network error logs indexes
CREATE INDEX idx_network_error_logs_timestamp_type ON network_error_logs(error_timestamp DESC, error_type);
CREATE INDEX idx_network_error_logs_severity ON network_error_logs(error_severity, error_timestamp DESC);
CREATE INDEX idx_network_error_logs_player ON network_error_logs(player_id, error_timestamp DESC);

-- =================================================================================================
-- PARTITIONING SETUP
-- =================================================================================================

-- Partition network_telemetry by month (high volume table)
CREATE TABLE network_telemetry_2025_01 PARTITION OF network_telemetry
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE network_telemetry_2025_02 PARTITION OF network_telemetry
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition delta_compression_stats by month
CREATE TABLE delta_compression_stats_2025_01 PARTITION OF delta_compression_stats
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE delta_compression_stats_2025_02 PARTITION OF delta_compression_stats
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition udp_packet_stats by day (very high volume)
CREATE TABLE udp_packet_stats_2025_01_01 PARTITION OF udp_packet_stats
    FOR VALUES FROM ('2025-01-01') TO ('2025-01-02');

CREATE TABLE udp_packet_stats_2025_01_02 PARTITION OF udp_packet_stats
    FOR VALUES FROM ('2025-01-02') TO ('2025-01-03');

-- =================================================================================================
-- CONSTRAINTS AND TRIGGERS
-- =================================================================================================

-- Function to update session statistics on telemetry insert
CREATE OR REPLACE FUNCTION update_session_telemetry_stats()
RETURNS TRIGGER AS $$
BEGIN
    -- Update session statistics with latest telemetry data
    UPDATE network_sessions
    SET
        last_activity = NEW.telemetry_timestamp,
        connection_quality = COALESCE(NEW.network_quality_score, connection_quality),
        average_ping_ms = COALESCE(NEW.rtt_ms, average_ping_ms),
        max_ping_ms = GREATEST(COALESCE(max_ping_ms, 0), COALESCE(NEW.rtt_ms, 0)),
        min_ping_ms = LEAST(COALESCE(min_ping_ms, 999999), COALESCE(NEW.rtt_ms, 999999))
    WHERE id = NEW.session_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for session telemetry updates
CREATE TRIGGER update_session_telemetry_trigger
    AFTER INSERT ON network_telemetry
    FOR EACH ROW
    EXECUTE FUNCTION update_session_telemetry_stats();

-- Function to validate spatial cell bounds
CREATE OR REPLACE FUNCTION validate_spatial_cell_bounds()
RETURNS TRIGGER AS $$
DECLARE
    bounds JSONB;
    min_x DECIMAL;
    max_x DECIMAL;
    min_y DECIMAL;
    max_y DECIMAL;
BEGIN
    bounds := NEW.cell_bounds;

    IF bounds IS NULL OR NOT (bounds ? 'min_x' AND bounds ? 'max_x' AND bounds ? 'min_y' AND bounds ? 'max_y') THEN
        RAISE EXCEPTION 'Invalid cell bounds: must contain min_x, max_x, min_y, max_y';
    END IF;

    min_x := (bounds->>'min_x')::DECIMAL;
    max_x := (bounds->>'max_x')::DECIMAL;
    min_y := (bounds->>'min_y')::DECIMAL;
    max_y := (bounds->>'max_y')::DECIMAL;

    IF min_x >= max_x OR min_y >= max_y THEN
        RAISE EXCEPTION 'Invalid cell bounds: min must be less than max';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for spatial cell bounds validation
CREATE TRIGGER validate_spatial_cell_bounds_trigger
    BEFORE INSERT OR UPDATE ON spatial_cell_metrics
    FOR EACH ROW
    EXECUTE FUNCTION validate_spatial_cell_bounds();

-- Function to maintain connection quality stats
CREATE OR REPLACE FUNCTION maintain_connection_quality_stats()
RETURNS TRIGGER AS $$
BEGIN
    -- Update or insert daily connection quality stats
    INSERT INTO connection_quality_stats (
        stat_date, player_id, sessions_count, total_session_time_minutes,
        average_connection_quality, average_rtt_ms, average_jitter_ms,
        average_packet_loss_percentage, connection_drops
    )
    SELECT
        CURRENT_DATE,
        NEW.player_id,
        1,
        EXTRACT(EPOCH FROM (NEW.session_end - NEW.session_start))/60,
        NEW.connection_quality,
        NEW.average_ping_ms,
        0, -- jitter not tracked in session
        CASE WHEN NEW.packets_sent > 0
             THEN (NEW.packets_lost::DECIMAL / NEW.packets_sent) * 100
             ELSE 0 END,
        CASE WHEN NEW.disconnect_reason IN ('timeout', 'network', 'error') THEN 1 ELSE 0 END
    ON CONFLICT (stat_date, player_id) DO UPDATE SET
        sessions_count = connection_quality_stats.sessions_count + 1,
        total_session_time_minutes = connection_quality_stats.total_session_time_minutes +
            EXTRACT(EPOCH FROM (NEW.session_end - NEW.session_start))/60,
        average_connection_quality = (
            connection_quality_stats.average_connection_quality * connection_quality_stats.sessions_count +
            NEW.connection_quality
        ) / (connection_quality_stats.sessions_count + 1),
        average_rtt_ms = COALESCE(
            (connection_quality_stats.average_rtt_ms * connection_quality_stats.sessions_count +
             NEW.average_ping_ms) / (connection_quality_stats.sessions_count + 1),
            NEW.average_ping_ms
        ),
        average_packet_loss_percentage = (
            connection_quality_stats.average_packet_loss_percentage * connection_quality_stats.sessions_count +
            CASE WHEN NEW.packets_sent > 0
                 THEN (NEW.packets_lost::DECIMAL / NEW.packets_sent) * 100
                 ELSE 0 END
        ) / (connection_quality_stats.sessions_count + 1),
        connection_drops = connection_quality_stats.connection_drops +
            CASE WHEN NEW.disconnect_reason IN ('timeout', 'network', 'error') THEN 1 ELSE 0 END;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for connection quality stats maintenance
CREATE TRIGGER maintain_connection_quality_stats_trigger
    AFTER UPDATE ON network_sessions
    FOR EACH ROW
    WHEN (OLD.session_end IS NULL AND NEW.session_end IS NOT NULL)
    EXECUTE FUNCTION maintain_connection_quality_stats();
