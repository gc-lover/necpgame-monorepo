-- Realtime Gateway Network Initial Data
-- Version: V002
-- Description: Initial configuration data for realtime-gateway network metrics and telemetry system

-- =================================================================================================
-- NETWORK SYSTEM CONFIGURATION
-- =================================================================================================

-- Create system configuration table for network settings
CREATE TABLE IF NOT EXISTS network_system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value JSONB NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Network system settings
INSERT INTO network_system_config (config_key, config_value, description) VALUES
('udp_server_enabled', 'true', 'Whether UDP server is enabled'),

('udp_server_port', '18080', 'UDP server port'),

('websocket_lobby_enabled', 'true', 'Whether WebSocket lobby is enabled'),

('max_udp_connections', '10000', 'Maximum concurrent UDP connections'),

('max_websocket_connections', '5000', 'Maximum concurrent WebSocket connections'),

('spatial_grid_enabled', 'true', 'Whether spatial partitioning is enabled'),

('spatial_cell_size_meters', '50', 'Size of spatial grid cells in meters'),

('max_players_per_cell', '32', 'Maximum players per spatial cell'),

('delta_compression_enabled', 'true', 'Whether delta compression is enabled'),

('coordinate_quantization_precision', '0.01', 'Coordinate quantization precision in meters'),

('adaptive_tick_rate_enabled', 'true', 'Whether adaptive tick rate is enabled'),

('tick_rate_player_thresholds', '{
    "0-50": 128,
    "51-200": 60,
    "201-500": 30,
    "500+": 20
}', 'Tick rate thresholds based on player count'),

('tick_rate_distance_multipliers', '{
    "close_range": 1.0,
    "medium_range": 0.6,
    "long_range": 0.3,
    "extended_range": 0.1
}', 'Tick rate multipliers based on distance'),

('network_buffer_pool_size', '10000', 'Network buffer pool size'),

('compression_buffer_pool_size', '5000', 'Compression buffer pool size'),

('packet_batch_size_max', '32', 'Maximum packets to batch together'),

('telemetry_collection_enabled', 'true', 'Whether telemetry collection is enabled'),

('telemetry_sampling_rate', '0.1', 'Telemetry sampling rate (0.0-1.0)'),

('performance_monitoring_enabled', 'true', 'Whether performance monitoring is enabled'),

('error_logging_level', '"info"', 'Error logging level'),

('connection_timeout_seconds', '30', 'Connection timeout in seconds'),

('heartbeat_interval_seconds', '10', 'Heartbeat interval for connections'),

('max_packet_size_bytes', '1500', 'Maximum UDP packet size'),

('network_quality_thresholds', '{
    "excellent": {"rtt_max": 25, "loss_max": 0.5},
    "good": {"rtt_max": 50, "loss_max": 2.0},
    "fair": {"rtt_max": 100, "loss_max": 5.0},
    "poor": {"rtt_max": 200, "loss_max": 10.0}
}', 'Network quality classification thresholds'),

('fraud_detection_enabled', 'true', 'Whether fraud detection is enabled in network layer'),

('rate_limiting_enabled', 'true', 'Whether rate limiting is enabled'),

('circuit_breaker_enabled', 'true', 'Whether circuit breaker pattern is enabled'),

('load_balancing_strategy', '"round_robin"', 'Load balancing strategy for network servers'),

('geographic_routing_enabled', 'false', 'Whether geographic routing is enabled'),

('compression_algorithms', '["coordinate_quantization", "delta_encoding", "bit_packing"]', 'Enabled compression algorithms'),

('security_features', '{
    "packet_validation": true,
    "sequence_number_validation": true,
    "ip_whitelisting": false,
    "rate_limiting": true,
    "fraud_detection": true
}', 'Enabled security features'),

('monitoring_endpoints', '{
    "prometheus_metrics": "/metrics",
    "health_check": "/health",
    "network_stats": "/network/stats",
    "performance_metrics": "/performance/metrics"
}', 'Monitoring and health check endpoints');

-- =================================================================================================
-- SPATIAL GRID CONFIGURATION
-- =================================================================================================

-- Create spatial grid configuration table
CREATE TABLE IF NOT EXISTS spatial_grid_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value JSONB NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Spatial grid settings
INSERT INTO spatial_grid_config (config_key, config_value, description) VALUES
('grid_cell_size', '{"width": 50, "height": 50}', 'Size of spatial grid cells in meters'),

('grid_bounds', '{"min_x": -10000, "max_x": 10000, "min_y": -10000, "max_y": 10000}', 'World bounds for spatial grid'),

('max_players_per_cell', '32', 'Maximum players allowed per cell before load balancing'),

('neighbor_search_radius', '3', 'Number of cells to search in each direction (3x3 grid)'),

('cell_migration_threshold', '0.8', 'Cell load percentage that triggers migration'),

('visibility_radii', '{
    "close_range": 25,
    "medium_range": 75,
    "long_range": 150,
    "extended_range": 300
}', 'Visibility ranges in meters'),

('interest_priority_levels', '{
    "high": ["allies", "targets", "combat_participants"],
    "medium": ["visible_players", "nearby_objects"],
    "low": ["background_players", "distant_objects"],
    "none": ["out_of_range"]
}', 'Interest management priority levels'),

('load_balancing_strategies', '["player_redistribution", "cell_expansion", "server_scaling"]', 'Available load balancing strategies'),

('cell_performance_targets', '{
    "max_updates_per_second": 1000,
    "max_processing_time_us": 1000,
    "target_load_percentage": 70
}', 'Performance targets for spatial cells');

-- =================================================================================================
-- NETWORK QUALITY BASELINES
-- =================================================================================================

-- Create network quality baseline table
CREATE TABLE IF NOT EXISTS network_quality_baselines (
    id BIGSERIAL PRIMARY KEY,
    baseline_key VARCHAR(100) UNIQUE NOT NULL,
    baseline_value JSONB NOT NULL,
    description TEXT,
    region_filter VARCHAR(50), -- NULL for global, region name for regional
    isp_filter VARCHAR(100), -- NULL for all ISPs
    connection_type_filter VARCHAR(20), -- NULL for all types
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Global network quality baselines
INSERT INTO network_quality_baselines (baseline_key, baseline_value, description) VALUES
('global_average_rtt', '{"value": 45, "unit": "ms", "acceptable_range": [20, 100]}', 'Global average round trip time'),

('global_average_jitter', '{"value": 5, "unit": "ms", "acceptable_range": [1, 15]}', 'Global average jitter'),

('global_average_packet_loss', '{"value": 1.5, "unit": "percentage", "acceptable_range": [0.0, 5.0]}', 'Global average packet loss'),

('global_average_bandwidth_down', '{"value": 50000, "unit": "kbps", "acceptable_range": [10000, 100000]}', 'Global average download bandwidth'),

('global_average_bandwidth_up', '{"value": 10000, "unit": "kbps", "acceptable_range": [1000, 50000]}', 'Global average upload bandwidth'),

-- Regional baselines (sample)
('north_america_rtt', '{"value": 35, "unit": "ms", "acceptable_range": [15, 80]}', 'North America average RTT'),

('europe_rtt', '{"value": 40, "unit": "ms", "acceptable_range": [20, 90]}', 'Europe average RTT'),

('asia_pacific_rtt', '{"value": 60, "unit": "ms", "acceptable_range": [30, 150]}', 'Asia Pacific average RTT'),

-- Connection type baselines
('wired_connection_rtt', '{"value": 25, "unit": "ms", "acceptable_range": [10, 50]}', 'Wired connection RTT baseline'),

('wifi_connection_rtt', '{"value": 35, "unit": "ms", "acceptable_range": [15, 70]}', 'WiFi connection RTT baseline'),

('mobile_4g_rtt', '{"value": 55, "unit": "ms", "acceptable_range": [25, 120]}', '4G mobile RTT baseline'),

('mobile_5g_rtt', '{"value": 35, "unit": "ms", "acceptable_range": [15, 80]}', '5G mobile RTT baseline');

-- =================================================================================================
-- COMPRESSION ALGORITHM CONFIGURATION
-- =================================================================================================

-- Create compression configuration table
CREATE TABLE IF NOT EXISTS compression_algorithm_config (
    id BIGSERIAL PRIMARY KEY,
    algorithm_name VARCHAR(50) UNIQUE NOT NULL,
    algorithm_config JSONB NOT NULL,
    description TEXT,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    priority INTEGER NOT NULL DEFAULT 0, -- Higher priority = preferred
    performance_baseline JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Compression algorithm configurations
INSERT INTO compression_algorithm_config (
    algorithm_name, algorithm_config, description, priority, performance_baseline
) VALUES
('coordinate_quantization', '{
    "float32_to_int16": true,
    "precision_meters": 0.01,
    "range_min": -32768,
    "range_max": 32767,
    "compression_ratio_target": 0.5
}', 'Coordinate quantization from float32 to int16', 100, '{
    "avg_compression_ratio": 0.5,
    "avg_compression_time_us": 5,
    "max_error_meters": 0.01
}'),

('delta_encoding', '{
    "track_fields": ["position", "rotation", "velocity", "health", "weapon_state"],
    "absolute_threshold": 10,
    "delta_threshold": 0.1,
    "prediction_enabled": true
}', 'Delta encoding for changed fields only', 90, '{
    "avg_compression_ratio": 0.3,
    "avg_compression_time_us": 15,
    "fields_compressed_avg": 3
}'),

('bit_packing', '{
    "variable_length_encoding": true,
    "huffman_coding": false,
    "run_length_encoding": true,
    "max_run_length": 255
}', 'Bit-level packing optimizations', 80, '{
    "avg_compression_ratio": 0.7,
    "avg_compression_time_us": 8,
    "efficiency_gain": 0.2
}'),

('lz4_compression', '{
    "compression_level": 1,
    "block_size_kb": 64,
    "content_type": "binary_packet"
}', 'LZ4 compression for large packets', 60, '{
    "avg_compression_ratio": 0.6,
    "avg_compression_time_us": 50,
    "throughput_mbps": 500
}'),

('huffman_coding', '{
    "dynamic_tree": true,
    "tree_update_frequency": 1000,
    "symbol_weights": {"position": 0.4, "rotation": 0.2, "health": 0.1, "actions": 0.3}
}', 'Huffman coding for frequent symbols', 70, '{
    "avg_compression_ratio": 0.65,
    "avg_compression_time_us": 25,
    "entropy_reduction": 0.3
}');

-- =================================================================================================
-- NETWORK MONITORING THRESHOLDS
-- =================================================================================================

-- Create monitoring threshold table
CREATE TABLE IF NOT EXISTS network_monitoring_thresholds (
    id BIGSERIAL PRIMARY KEY,
    metric_name VARCHAR(100) UNIQUE NOT NULL,
    warning_threshold JSONB NOT NULL,
    critical_threshold JSONB NOT NULL,
    description TEXT,
    auto_action_enabled BOOLEAN NOT NULL DEFAULT false,
    auto_action_config JSONB,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Network monitoring thresholds
INSERT INTO network_monitoring_thresholds (
    metric_name, warning_threshold, critical_threshold, description, auto_action_enabled, auto_action_config
) VALUES
('packet_loss_rate', '{"percentage": 2.0, "duration_minutes": 5}', '{"percentage": 5.0, "duration_minutes": 2}', 'UDP packet loss rate monitoring', true, '{"action": "reduce_tick_rate", "reduction_percentage": 20}'),

('average_latency', '{"rtt_ms": 75, "duration_minutes": 10}', '{"rtt_ms": 150, "duration_minutes": 5}', 'Average network latency monitoring', true, '{"action": "increase_batch_size", "batch_multiplier": 1.5}'),

('cpu_usage', '{"percentage": 70, "duration_minutes": 5}', '{"percentage": 85, "duration_minutes": 2}', 'Network server CPU usage monitoring', true, '{"action": "scale_up_servers", "scale_factor": 1.2}'),

('memory_usage', '{"percentage": 75, "duration_minutes": 5}', '{"percentage": 90, "duration_minutes": 2}', 'Network server memory usage monitoring', true, '{"action": "garbage_collect", "force_gc": true}'),

('active_connections', '{"count": 8000, "percentage_of_max": 80}', '{"count": 9000, "percentage_of_max": 90}', 'Active connections monitoring', true, '{"action": "enable_rate_limiting", "limit_multiplier": 0.8}'),

('queue_length', '{"length": 1000, "duration_seconds": 30}', '{"length": 2000, "duration_seconds": 10}', 'Network processing queue length', true, '{"action": "increase_worker_threads", "thread_increment": 2}'),

('tick_rate_deviation', '{"deviation_percentage": 15, "duration_minutes": 5}', '{"deviation_percentage": 30, "duration_minutes": 2}', 'Tick rate deviation from target', true, '{"action": "adjust_tick_rate", "correction_factor": 0.9}'),

('compression_ratio_drop', '{"ratio_drop_percentage": 20, "duration_minutes": 10}', '{"ratio_drop_percentage": 40, "duration_minutes": 5}', 'Compression efficiency drop', false, '{"action": "switch_algorithm", "fallback_algorithm": "coordinate_quantization"}'),

('spatial_cell_overload', '{"overload_percentage": 25, "duration_minutes": 5}', '{"overload_percentage": 50, "duration_minutes": 2}', 'Spatial cell overload detection', true, '{"action": "redistribute_players", "max_redistribution": 8}'),

('websocket_connection_drops', '{"drops_per_minute": 10, "duration_minutes": 5}', '{"drops_per_minute": 25, "duration_minutes": 2}', 'WebSocket connection drop rate', true, '{"action": "adjust_heartbeat_interval", "new_interval_seconds": 5}');

-- =================================================================================================
-- ERROR CODE DEFINITIONS
-- =================================================================================================

-- Create error code reference table
CREATE TABLE IF NOT EXISTS network_error_codes (
    id BIGSERIAL PRIMARY KEY,
    error_code VARCHAR(20) UNIQUE NOT NULL,
    error_name VARCHAR(100) NOT NULL,
    error_description TEXT,
    severity_level VARCHAR(10) NOT NULL, -- 'low', 'medium', 'high', 'critical'
    recovery_action VARCHAR(100),
    user_visible BOOLEAN NOT NULL DEFAULT false,
    user_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Network error code definitions
INSERT INTO network_error_codes (
    error_code, error_name, error_description, severity_level, recovery_action, user_visible, user_message
) VALUES
('NET_001', 'Connection Timeout', 'Network connection timed out', 'medium', 'retry_connection', true, 'Connection lost. Attempting to reconnect...'),

('NET_002', 'Packet Corruption', 'Received corrupted network packet', 'low', 'request_retransmission', false, NULL),

('NET_003', 'Invalid Packet Format', 'Received packet with invalid format', 'medium', 'log_and_drop', false, NULL),

('NET_004', 'Sequence Number Mismatch', 'Packet sequence number out of order', 'low', 'reorder_packets', false, NULL),

('NET_005', 'Rate Limit Exceeded', 'Too many packets received in time window', 'medium', 'apply_backoff', true, 'Connection rate limited. Please wait before retrying.'),

('NET_006', 'Buffer Overflow', 'Network buffer capacity exceeded', 'high', 'increase_buffer_size', false, NULL),

('NET_007', 'Spatial Calculation Error', 'Error in spatial partitioning calculations', 'high', 'recalculate_spatial_grid', false, NULL),

('NET_008', 'Compression Failure', 'Failed to compress/decompress packet', 'medium', 'fallback_to_uncompressed', false, NULL),

('NET_009', 'Authentication Failed', 'Network authentication verification failed', 'critical', 'disconnect_client', true, 'Authentication failed. Please reconnect.'),

('NET_010', 'Protocol Version Mismatch', 'Client/server protocol version incompatibility', 'high', 'request_client_update', true, 'Client version outdated. Please update your game client.'),

('NET_011', 'Spatial Grid Overload', 'Spatial partitioning system overloaded', 'high', 'redistribute_load', false, NULL),

('NET_012', 'Tick Rate Instability', 'Adaptive tick rate system unstable', 'medium', 'reset_tick_rate', false, NULL),

('NET_013', 'WebSocket Upgrade Failed', 'Failed to upgrade HTTP to WebSocket', 'low', 'fallback_to_http', false, NULL),

('NET_014', 'NAT Traversal Failed', 'UDP hole punching failed for NAT traversal', 'medium', 'retry_nat_traversal', true, 'Connection issue. Retrying...'),

('NET_015', 'Certificate Validation Failed', 'SSL/TLS certificate validation error', 'critical', 'reject_connection', true, 'Security certificate error. Please check your connection.');

-- =================================================================================================
-- PERFORMANCE BASELINES FOR MONITORING
-- =================================================================================================

-- Create performance baseline table
CREATE TABLE IF NOT EXISTS network_performance_baselines (
    id BIGSERIAL PRIMARY KEY,
    metric_name VARCHAR(100) UNIQUE NOT NULL,
    baseline_value DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    description TEXT,
    acceptable_range JSONB, -- {"min": value, "max": value}
    warning_threshold JSONB, -- {"min": value, "max": value}
    critical_threshold JSONB, -- {"min": value, "max": value}
    measurement_period VARCHAR(20) DEFAULT '5_minutes', -- '1_minute', '5_minutes', '1_hour', etc.
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Network performance baselines
INSERT INTO network_performance_baselines (
    metric_name, baseline_value, unit, description, acceptable_range, warning_threshold, critical_threshold
) VALUES
('packet_processing_time', 50.0, 'microseconds', 'Average UDP packet processing time', '{"min": 10, "max": 200}', '{"min": 5, "max": 500}', '{"min": 1, "max": 1000}'),

('compression_time', 15.0, 'microseconds', 'Average packet compression time', '{"min": 5, "max": 50}', '{"min": 2, "max": 100}', '{"min": 1, "max": 200}'),

('spatial_lookup_time', 5.0, 'microseconds', 'Spatial grid cell lookup time', '{"min": 1, "max": 20}', '{"min": 0.5, "max": 50}', '{"min": 0.1, "max": 100}'),

('tick_rate_stability', 95.0, 'percentage', 'Tick rate stability percentage', '{"min": 90, "max": 100}', '{"min": 80, "max": 105}', '{"min": 70, "max": 110}'),

('connection_success_rate', 98.5, 'percentage', 'UDP connection success rate', '{"min": 95, "max": 100}', '{"min": 90, "max": 100}', '{"min": 85, "max": 100}'),

('websocket_upgrade_success_rate', 99.0, 'percentage', 'WebSocket upgrade success rate', '{"min": 95, "max": 100}', '{"min": 90, "max": 100}', '{"min": 85, "max": 100}'),

('buffer_pool_utilization', 60.0, 'percentage', 'Network buffer pool utilization', '{"min": 10, "max": 80}', '{"min": 5, "max": 90}', '{"min": 1, "max": 95}'),

('compression_efficiency', 65.0, 'percentage', 'Average compression efficiency', '{"min": 50, "max": 80}', '{"min": 40, "max": 85}', '{"min": 30, "max": 90}'),

('spatial_cell_balance', 75.0, 'percentage', 'Spatial grid load balance score', '{"min": 60, "max": 90}', '{"min": 50, "max": 95}', '{"min": 40, "max": 100}'),

('queue_processing_efficiency', 95.0, 'percentage', 'Network queue processing efficiency', '{"min": 90, "max": 100}', '{"min": 80, "max": 100}', '{"min": 70, "max": 100}');

-- =================================================================================================
-- GEOGRAPHIC REGION CONFIGURATION
-- =================================================================================================

-- Create geographic region configuration
CREATE TABLE IF NOT EXISTS network_regions (
    id BIGSERIAL PRIMARY KEY,
    region_code VARCHAR(10) UNIQUE NOT NULL,
    region_name VARCHAR(100) NOT NULL,
    continent VARCHAR(20),
    coordinate_bounds JSONB, -- {"min_lat": float, "max_lat": float, "min_lon": float, "max_lon": float}
    expected_network_quality JSONB, -- {"avg_rtt": int, "avg_loss": float, "avg_bandwidth_down": int}
    server_endpoints JSONB, -- Array of server endpoints for this region
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Geographic region configurations
INSERT INTO network_regions (
    region_code, region_name, continent, coordinate_bounds, expected_network_quality, server_endpoints
) VALUES
('NA-EAST', 'North America East', 'North America',
 '{"min_lat": 24, "max_lat": 50, "min_lon": -100, "max_lon": -60}',
 '{"avg_rtt": 35, "avg_loss": 1.0, "avg_bandwidth_down": 75000}',
 '["us-east-1.necp.game:18080", "us-east-2.necp.game:18080"]'),

('NA-WEST', 'North America West', 'North America',
 '{"min_lat": 30, "max_lat": 50, "min_lon": -130, "max_lon": -100}',
 '{"avg_rtt": 40, "avg_loss": 1.2, "avg_bandwidth_down": 80000}',
 '["us-west-1.necp.game:18080", "us-west-2.necp.game:18080"]'),

('EU-WEST', 'Europe West', 'Europe',
 '{"min_lat": 35, "max_lat": 60, "min_lon": -10, "max_lon": 20}',
 '{"avg_rtt": 45, "avg_loss": 0.8, "avg_bandwidth_down": 60000}',
 '["eu-west-1.necp.game:18080", "eu-central-1.necp.game:18080"]'),

('EU-EAST', 'Europe East', 'Europe',
 '{"min_lat": 40, "max_lat": 60, "min_lon": 20, "max_lon": 60}',
 '{"avg_rtt": 55, "avg_loss": 1.5, "avg_bandwidth_down": 45000}',
 '["eu-east-1.necp.game:18080"]'),

('ASIA-EAST', 'Asia East', 'Asia',
 '{"min_lat": 20, "max_lat": 50, "min_lon": 100, "max_lon": 150}',
 '{"avg_rtt": 120, "avg_loss": 2.0, "avg_bandwidth_down": 35000}',
 '["ap-east-1.necp.game:18080", "ap-northeast-1.necp.game:18080"]'),

('ASIA-SOUTHEAST', 'Asia Southeast', 'Asia',
 '{"min_lat": -10, "max_lat": 20, "min_lon": 90, "max_lon": 150}',
 '{"avg_rtt": 150, "avg_loss": 2.5, "avg_bandwidth_down": 25000}',
 '["ap-southeast-1.necp.game:18080", "ap-southeast-2.necp.game:18080"]'),

('OCEANIA', 'Oceania', 'Oceania',
 '{"min_lat": -50, "max_lat": -10, "min_lon": 110, "max_lon": 180}',
 '{"avg_rtt": 180, "avg_loss": 3.0, "avg_bandwidth_down": 30000}',
 '["ap-southeast-2.necp.game:18080"]'),

('SA-EAST', 'South America East', 'South America',
 '{"min_lat": -35, "max_lat": 10, "min_lon": -70, "max_lon": -30}',
 '{"avg_rtt": 100, "avg_loss": 2.0, "avg_bandwidth_down": 20000}',
 '["sa-east-1.necp.game:18080"]');

-- =================================================================================================
-- CLIENT VERSION COMPATIBILITY MATRIX
-- =================================================================================================

-- Create client version compatibility table
CREATE TABLE IF NOT EXISTS network_client_compatibility (
    id BIGSERIAL PRIMARY KEY,
    client_version VARCHAR(20) NOT NULL,
    protocol_version VARCHAR(10) NOT NULL,
    supported_features JSONB NOT NULL, -- Array of supported network features
    known_issues JSONB NOT NULL DEFAULT '[]', -- Array of known compatibility issues
    recommended BOOLEAN NOT NULL DEFAULT true,
    deprecated BOOLEAN NOT NULL DEFAULT false,
    end_of_support DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(client_version)
);

-- Client version compatibility matrix
INSERT INTO network_client_compatibility (
    client_version, protocol_version, supported_features, known_issues, recommended, deprecated, end_of_support
) VALUES
('1.0.0', '1.0', '["udp_gamestate", "websocket_lobby", "spatial_partitioning", "delta_compression"]', '[]', true, false, NULL),

('1.1.0', '1.1', '["udp_gamestate", "websocket_lobby", "spatial_partitioning", "delta_compression", "adaptive_tick_rate"]', '[]', true, false, NULL),

('1.2.0', '1.2', '["udp_gamestate", "websocket_lobby", "spatial_partitioning", "delta_compression", "adaptive_tick_rate", "batch_compression"]', '[]', true, false, NULL),

('0.9.5', '0.9', '["udp_gamestate", "websocket_lobby", "basic_compression"]', '[{"issue": "no_spatial_partitioning", "severity": "medium"}]', false, true, '2025-06-01'),

('0.8.2', '0.8', '["udp_gamestate", "websocket_lobby"]', '[{"issue": "no_compression", "severity": "high"}, {"issue": "no_spatial_partitioning", "severity": "high"}]', false, true, '2025-03-01');

-- =================================================================================================
-- FUNCTIONS FOR INITIALIZATION
-- =================================================================================================

-- Function to get regional network settings
CREATE OR REPLACE FUNCTION get_regional_network_settings(client_ip INET)
RETURNS TABLE (
    region_code VARCHAR(10),
    region_name VARCHAR(100),
    expected_rtt_ms INTEGER,
    expected_loss_percentage DECIMAL,
    recommended_servers JSONB
) AS $$
DECLARE
    client_region VARCHAR(10);
BEGIN
    -- Simple IP-based region detection (would be enhanced with GeoIP database)
    -- This is a simplified version - in production use proper GeoIP service
    client_region := CASE
        WHEN client_ip >>= '192.168.0.0/16'::inet THEN 'NA-EAST'  -- Example: local network
        WHEN client_ip >>= '10.0.0.0/8'::inet THEN 'EU-WEST'     -- Example: VPN
        ELSE 'NA-EAST'  -- Default fallback
    END;

    RETURN QUERY
    SELECT
        nr.region_code,
        nr.region_name,
        (nr.expected_network_quality->>'avg_rtt')::INTEGER as expected_rtt_ms,
        (nr.expected_network_quality->>'avg_loss')::DECIMAL as expected_loss_percentage,
        nr.server_endpoints as recommended_servers
    FROM network_regions nr
    WHERE nr.region_code = client_region
      AND nr.is_active = true;
END;
$$ LANGUAGE plpgsql;

-- Function to validate network configuration
CREATE OR REPLACE FUNCTION validate_network_config()
RETURNS TABLE (
    config_issue VARCHAR(100),
    severity VARCHAR(10),
    description TEXT
) AS $$
BEGIN
    -- Check for missing critical configuration
    RETURN QUERY
    SELECT
        'missing_udp_port'::VARCHAR(100),
        'CRITICAL'::VARCHAR(10),
        'UDP server port not configured'::TEXT
    WHERE NOT EXISTS (
        SELECT 1 FROM network_system_config
        WHERE config_key = 'udp_server_port' AND config_value::text != ''
    );

    -- Check for invalid spatial grid configuration
    RETURN QUERY
    SELECT
        'invalid_spatial_grid'::VARCHAR(100),
        'HIGH'::VARCHAR(10),
        'Spatial grid cell size invalid'::TEXT
    WHERE EXISTS (
        SELECT 1 FROM network_system_config
        WHERE config_key = 'spatial_cell_size_meters'
        AND (config_value::integer <= 0 OR config_value::integer > 1000)
    );

    -- Check for disabled critical features
    RETURN QUERY
    SELECT
        'compression_disabled'::VARCHAR(100),
        'MEDIUM'::VARCHAR(10),
        'Delta compression is disabled, will increase bandwidth usage'::TEXT
    WHERE EXISTS (
        SELECT 1 FROM network_system_config
        WHERE config_key = 'delta_compression_enabled'
        AND config_value::boolean = false
    );

    -- Check for missing monitoring thresholds
    RETURN QUERY
    SELECT
        'missing_monitoring_thresholds'::VARCHAR(100),
        'LOW'::VARCHAR(10),
        'Some network monitoring thresholds not configured'::TEXT
    WHERE (SELECT COUNT(*) FROM network_monitoring_thresholds) < 5;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- INDEXES FOR NEW TABLES
-- =================================================================================================

-- Network system config indexes
CREATE INDEX IF NOT EXISTS idx_network_system_config_key ON network_system_config(config_key) WHERE is_active = true;

-- Network quality baselines indexes
CREATE INDEX IF NOT EXISTS idx_network_quality_baselines_key ON network_quality_baselines(baseline_key);
CREATE INDEX IF NOT EXISTS idx_network_quality_baselines_region ON network_quality_baselines(region_filter);

-- Compression algorithm config indexes
CREATE INDEX IF NOT EXISTS idx_compression_algorithm_config_name ON compression_algorithm_config(algorithm_name) WHERE is_enabled = true;
CREATE INDEX IF NOT EXISTS idx_compression_algorithm_config_priority ON compression_algorithm_config(priority DESC) WHERE is_enabled = true;

-- Network monitoring thresholds indexes
CREATE INDEX IF NOT EXISTS idx_network_monitoring_thresholds_name ON network_monitoring_thresholds(metric_name) WHERE auto_action_enabled = true;

-- Network error codes indexes
CREATE INDEX IF NOT EXISTS idx_network_error_codes_code ON network_error_codes(error_code);
CREATE INDEX IF NOT EXISTS idx_network_error_codes_severity ON network_error_codes(severity_level);

-- Network performance baselines indexes
CREATE INDEX IF NOT EXISTS idx_network_performance_baselines_name ON network_performance_baselines(metric_name);

-- Network regions indexes
CREATE INDEX IF NOT EXISTS idx_network_regions_code ON network_regions(region_code) WHERE is_active = true;

-- Client compatibility indexes
CREATE INDEX IF NOT EXISTS idx_network_client_compatibility_version ON network_client_compatibility(client_version) WHERE recommended = true;

-- =================================================================================================
-- VALIDATION
-- =================================================================================================

-- Run validation to ensure proper setup
SELECT * FROM validate_network_config();
