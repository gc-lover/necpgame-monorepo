-- Issue: #142109960
-- Database Schema for Hybrid Network System
-- Network Engineer - Database schema for protocol sessions, zones, spatial partitioning

-- Zone Protocol Configuration
-- Конфигурация протоколов для зон
CREATE TABLE zone_protocol_config (
    id BIGSERIAL PRIMARY KEY,
    zone_id VARCHAR(255) NOT NULL UNIQUE,
    zone_type VARCHAR(50) NOT NULL, -- safe, open_world, dedicated_pvp, gvg, massive_war
    default_protocol VARCHAR(20) NOT NULL, -- websocket, udp
    supports_switching BOOLEAN NOT NULL DEFAULT FALSE,
    tickrate_base INTEGER NOT NULL,
    tickrate_min INTEGER NOT NULL,
    tickrate_max INTEGER NOT NULL,
    adaptive_tickrate BOOLEAN NOT NULL DEFAULT TRUE,
    latency_target_ms INTEGER NOT NULL,
    max_players INTEGER NOT NULL,
    network_bandwidth_per_player_kbps INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_zone_protocol_config_zone_id ON zone_protocol_config(zone_id);
CREATE INDEX idx_zone_protocol_config_zone_type ON zone_protocol_config(zone_type);

-- Protocol Sessions
-- Сессии протоколов для отслеживания соединений
CREATE TABLE protocol_sessions (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    zone_id VARCHAR(255) NOT NULL,
    current_protocol VARCHAR(20) NOT NULL, -- websocket, udp
    session_state VARCHAR(50) NOT NULL, -- websocket_only, udp_only, switching, dual
    websocket_connection_id VARCHAR(255),
    udp_connection_id VARCHAR(255),
    websocket_endpoint VARCHAR(255),
    udp_endpoint VARCHAR(255),
    session_token VARCHAR(512) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    FOREIGN KEY (zone_id) REFERENCES zone_protocol_config(zone_id) ON DELETE CASCADE
);

CREATE INDEX idx_protocol_sessions_session_id ON protocol_sessions(session_id);
CREATE INDEX idx_protocol_sessions_user_id ON protocol_sessions(user_id);
CREATE INDEX idx_protocol_sessions_zone_id ON protocol_sessions(zone_id);
CREATE INDEX idx_protocol_sessions_current_protocol ON protocol_sessions(current_protocol);
CREATE INDEX idx_protocol_sessions_expires_at ON protocol_sessions(expires_at);

-- Protocol Switch History
-- История переключений протоколов
CREATE TABLE protocol_switch_history (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    from_protocol VARCHAR(20) NOT NULL,
    to_protocol VARCHAR(20) NOT NULL,
    switch_state VARCHAR(50) NOT NULL, -- preparing, synchronizing, overlapping, switching, completed, failed, rollback
    switch_reason VARCHAR(255),
    preparation_duration_ms INTEGER,
    synchronization_duration_ms INTEGER,
    overlap_duration_ms INTEGER,
    total_duration_ms INTEGER,
    success BOOLEAN NOT NULL DEFAULT FALSE,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES protocol_sessions(session_id) ON DELETE CASCADE
);

CREATE INDEX idx_protocol_switch_history_session_id ON protocol_switch_history(session_id);
CREATE INDEX idx_protocol_switch_history_created_at ON protocol_switch_history(created_at);
CREATE INDEX idx_protocol_switch_history_success ON protocol_switch_history(success);

-- Spatial Cells
-- Ячейки spatial partitioning для оптимизации сетевого трафика
CREATE TABLE spatial_cells (
    id BIGSERIAL PRIMARY KEY,
    zone_id VARCHAR(255) NOT NULL,
    cell_x INTEGER NOT NULL,
    cell_y INTEGER NOT NULL,
    cell_id VARCHAR(255) NOT NULL, -- Generated: "{zone_id}:{cell_x}:{cell_y}"
    player_count INTEGER NOT NULL DEFAULT 0,
    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(zone_id, cell_x, cell_y),
    FOREIGN KEY (zone_id) REFERENCES zone_protocol_config(zone_id) ON DELETE CASCADE
);

CREATE INDEX idx_spatial_cells_zone_id ON spatial_cells(zone_id);
CREATE INDEX idx_spatial_cells_cell_id ON spatial_cells(cell_id);
CREATE INDEX idx_spatial_cells_coordinates ON spatial_cells(zone_id, cell_x, cell_y);
CREATE INDEX idx_spatial_cells_player_count ON spatial_cells(player_count);

-- Spatial Cell Players
-- Привязка игроков к ячейкам spatial partitioning
CREATE TABLE spatial_cell_players (
    id BIGSERIAL PRIMARY KEY,
    cell_id VARCHAR(255) NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    player_id BIGINT NOT NULL,
    position_x DOUBLE PRECISION NOT NULL,
    position_y DOUBLE PRECISION NOT NULL,
    position_z DOUBLE PRECISION NOT NULL,
    subscribed_cells TEXT[], -- Array of cell_ids player is subscribed to
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES protocol_sessions(session_id) ON DELETE CASCADE,
    FOREIGN KEY (cell_id) REFERENCES spatial_cells(cell_id) ON DELETE CASCADE
);

CREATE INDEX idx_spatial_cell_players_cell_id ON spatial_cell_players(cell_id);
CREATE INDEX idx_spatial_cell_players_session_id ON spatial_cell_players(session_id);
CREATE INDEX idx_spatial_cell_players_player_id ON spatial_cell_players(player_id);

-- Spatial Shards
-- Shards для spatial sharding (большие битвы 400+ игроков)
CREATE TABLE spatial_shards (
    id BIGSERIAL PRIMARY KEY,
    zone_id VARCHAR(255) NOT NULL,
    shard_id VARCHAR(255) NOT NULL UNIQUE,
    shard_index INTEGER NOT NULL,
    server_instance_id VARCHAR(255) NOT NULL,
    bounds_min_x DOUBLE PRECISION NOT NULL,
    bounds_min_y DOUBLE PRECISION NOT NULL,
    bounds_max_x DOUBLE PRECISION NOT NULL,
    bounds_max_y DOUBLE PRECISION NOT NULL,
    player_count INTEGER NOT NULL DEFAULT 0,
    max_players INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, full, maintenance
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES zone_protocol_config(zone_id) ON DELETE CASCADE
);

CREATE INDEX idx_spatial_shards_zone_id ON spatial_shards(zone_id);
CREATE INDEX idx_spatial_shards_shard_id ON spatial_shards(shard_id);
CREATE INDEX idx_spatial_shards_status ON spatial_shards(status);

-- Spatial Shard Players
-- Привязка игроков к shards
CREATE TABLE spatial_shard_players (
    id BIGSERIAL PRIMARY KEY,
    shard_id VARCHAR(255) NOT NULL,
    session_id VARCHAR(255) NOT NULL,
    player_id BIGINT NOT NULL,
    position_x DOUBLE PRECISION NOT NULL,
    position_y DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES protocol_sessions(session_id) ON DELETE CASCADE,
    FOREIGN KEY (shard_id) REFERENCES spatial_shards(shard_id) ON DELETE CASCADE
);

CREATE INDEX idx_spatial_shard_players_shard_id ON spatial_shard_players(shard_id);
CREATE INDEX idx_spatial_shard_players_session_id ON spatial_shard_players(session_id);
CREATE INDEX idx_spatial_shard_players_player_id ON spatial_shard_players(player_id);

-- Cluster Servers
-- Серверы кластера для massive war (2000+ игроков)
CREATE TABLE cluster_servers (
    id BIGSERIAL PRIMARY KEY,
    cluster_id VARCHAR(255) NOT NULL,
    server_instance_id VARCHAR(255) NOT NULL UNIQUE,
    server_role VARCHAR(50) NOT NULL, -- coordinator, game_server, edge_server
    zone_id VARCHAR(255) NOT NULL,
    host VARCHAR(255) NOT NULL,
    port INTEGER NOT NULL,
    protocol VARCHAR(20) NOT NULL, -- websocket, udp
    max_capacity INTEGER NOT NULL,
    current_load INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'active', -- active, maintenance, overloaded, down
    last_heartbeat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES zone_protocol_config(zone_id) ON DELETE CASCADE
);

CREATE INDEX idx_cluster_servers_cluster_id ON cluster_servers(cluster_id);
CREATE INDEX idx_cluster_servers_zone_id ON cluster_servers(zone_id);
CREATE INDEX idx_cluster_servers_status ON cluster_servers(status);
CREATE INDEX idx_cluster_servers_server_role ON cluster_servers(server_role);

-- Network Metrics
-- Метрики сети для мониторинга и оптимизации
CREATE TABLE network_metrics (
    id BIGSERIAL PRIMARY KEY,
    session_id VARCHAR(255),
    zone_id VARCHAR(255),
    protocol VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    packets_sent BIGINT NOT NULL DEFAULT 0,
    packets_received BIGINT NOT NULL DEFAULT 0,
    packets_lost BIGINT NOT NULL DEFAULT 0,
    latency_p50_ms INTEGER,
    latency_p95_ms INTEGER,
    latency_p99_ms INTEGER,
    rtt_ms INTEGER,
    bandwidth_used_kbps INTEGER,
    jitter_ms INTEGER,
    FOREIGN KEY (session_id) REFERENCES protocol_sessions(session_id) ON DELETE SET NULL,
    FOREIGN KEY (zone_id) REFERENCES zone_protocol_config(zone_id) ON DELETE SET NULL
);

CREATE INDEX idx_network_metrics_session_id ON network_metrics(session_id);
CREATE INDEX idx_network_metrics_zone_id ON network_metrics(zone_id);
CREATE INDEX idx_network_metrics_timestamp ON network_metrics(timestamp);
CREATE INDEX idx_network_metrics_protocol ON network_metrics(protocol);

-- UDP Connection State
-- Состояние UDP соединений для надежности
CREATE TABLE udp_connection_state (
    id BIGSERIAL PRIMARY KEY,
    connection_id VARCHAR(255) NOT NULL UNIQUE,
    session_id VARCHAR(255) NOT NULL,
    last_sequence_number BIGINT NOT NULL DEFAULT 0,
    last_ack_number BIGINT NOT NULL DEFAULT 0,
    last_heartbeat TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    connection_state VARCHAR(50) NOT NULL DEFAULT 'connecting', -- connecting, authenticating, connected, disconnecting
    packets_sent BIGINT NOT NULL DEFAULT 0,
    packets_received BIGINT NOT NULL DEFAULT 0,
    packets_retransmitted BIGINT NOT NULL DEFAULT 0,
    packets_lost BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES protocol_sessions(session_id) ON DELETE CASCADE
);

CREATE INDEX idx_udp_connection_state_connection_id ON udp_connection_state(connection_id);
CREATE INDEX idx_udp_connection_state_session_id ON udp_connection_state(session_id);
CREATE INDEX idx_udp_connection_state_last_heartbeat ON udp_connection_state(last_heartbeat);

-- UDP Packet Sequence
-- История последовательности пакетов для обработки дубликатов и повторных передач
CREATE TABLE udp_packet_sequence (
    id BIGSERIAL PRIMARY KEY,
    connection_id VARCHAR(255) NOT NULL,
    sequence_number BIGINT NOT NULL,
    packet_type VARCHAR(50) NOT NULL,
    payload_size INTEGER NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_at TIMESTAMP,
    retransmission_count INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (connection_id) REFERENCES udp_connection_state(connection_id) ON DELETE CASCADE,
    UNIQUE(connection_id, sequence_number)
);

CREATE INDEX idx_udp_packet_sequence_connection_id ON udp_packet_sequence(connection_id);
CREATE INDEX idx_udp_packet_sequence_sequence_number ON udp_packet_sequence(connection_id, sequence_number);
CREATE INDEX idx_udp_packet_sequence_acknowledged ON udp_packet_sequence(acknowledged);
CREATE INDEX idx_udp_packet_sequence_sent_at ON udp_packet_sequence(sent_at);

-- Triggers для обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_zone_protocol_config_updated_at BEFORE UPDATE ON zone_protocol_config
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_protocol_sessions_updated_at BEFORE UPDATE ON protocol_sessions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_spatial_cell_players_updated_at BEFORE UPDATE ON spatial_cell_players
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_spatial_shards_updated_at BEFORE UPDATE ON spatial_shards
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_spatial_shard_players_updated_at BEFORE UPDATE ON spatial_shard_players
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cluster_servers_updated_at BEFORE UPDATE ON cluster_servers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_udp_connection_state_updated_at BEFORE UPDATE ON udp_connection_state
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Views для аналитики

-- View: Zone Statistics
CREATE OR REPLACE VIEW zone_statistics AS
SELECT
    zpc.zone_id,
    zpc.zone_type,
    zpc.default_protocol,
    zpc.tickrate_base,
    zpc.max_players,
    COUNT(DISTINCT ps.session_id) AS current_players,
    COUNT(DISTINCT CASE WHEN ps.current_protocol = 'websocket' THEN ps.session_id END) AS websocket_players,
    COUNT(DISTINCT CASE WHEN ps.current_protocol = 'udp' THEN ps.session_id END) AS udp_players,
    COUNT(DISTINCT sc.cell_id) AS active_cells,
    COUNT(DISTINCT ss.shard_id) AS active_shards
FROM zone_protocol_config zpc
LEFT JOIN protocol_sessions ps ON ps.zone_id = zpc.zone_id AND ps.expires_at > CURRENT_TIMESTAMP
LEFT JOIN spatial_cells sc ON sc.zone_id = zpc.zone_id
LEFT JOIN spatial_shards ss ON ss.zone_id = zpc.zone_id AND ss.status = 'active'
GROUP BY zpc.zone_id, zpc.zone_type, zpc.default_protocol, zpc.tickrate_base, zpc.max_players;

-- View: Protocol Switch Statistics
CREATE OR REPLACE VIEW protocol_switch_statistics AS
SELECT
    DATE_TRUNC('hour', created_at) AS hour,
    from_protocol,
    to_protocol,
    COUNT(*) AS total_switches,
    COUNT(*) FILTER (WHERE success = true) AS successful_switches,
    COUNT(*) FILTER (WHERE success = false) AS failed_switches,
    AVG(total_duration_ms) AS avg_duration_ms,
    PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY total_duration_ms) AS p50_duration_ms,
    PERCENTILE_CONT(0.95) WITHIN GROUP (ORDER BY total_duration_ms) AS p95_duration_ms
FROM protocol_switch_history
WHERE created_at > CURRENT_TIMESTAMP - INTERVAL '24 hours'
GROUP BY DATE_TRUNC('hour', created_at), from_protocol, to_protocol;

-- View: Network Performance Metrics
CREATE OR REPLACE VIEW network_performance_metrics AS
SELECT
    DATE_TRUNC('minute', timestamp) AS minute,
    protocol,
    zone_id,
    AVG(latency_p50_ms) AS avg_latency_p50_ms,
    AVG(latency_p95_ms) AS avg_latency_p95_ms,
    AVG(packets_lost) AS avg_packets_lost,
    AVG(bandwidth_used_kbps) AS avg_bandwidth_kbps,
    SUM(packets_sent) AS total_packets_sent,
    SUM(packets_received) AS total_packets_received,
    SUM(packets_lost) AS total_packets_lost
FROM network_metrics
WHERE timestamp > CURRENT_TIMESTAMP - INTERVAL '1 hour'
GROUP BY DATE_TRUNC('minute', timestamp), protocol, zone_id;

