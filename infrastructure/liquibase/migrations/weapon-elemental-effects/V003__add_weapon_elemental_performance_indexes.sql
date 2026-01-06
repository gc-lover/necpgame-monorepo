-- Weapon Elemental Effects Performance Optimizations
-- Version: V003
-- Description: Additional indexes, materialized views, and performance optimizations for Weapon Elemental Effects

-- =================================================================================================
-- ADDITIONAL PERFORMANCE INDEXES
-- =================================================================================================

-- Elemental effects complex queries
CREATE INDEX CONCURRENTLY idx_elemental_effects_element_type ON elemental_effects(element_id, effect_type, is_active);
CREATE INDEX CONCURRENTLY idx_elemental_effects_damage_type ON elemental_effects(damage_type, base_damage DESC);
CREATE INDEX CONCURRENTLY idx_elemental_effects_stacks ON elemental_effects(max_stacks, duration_seconds);

-- Effect modifiers performance
CREATE INDEX CONCURRENTLY idx_elemental_effect_modifiers_effect_type ON elemental_effect_modifiers(effect_id, modifier_type);
CREATE INDEX CONCURRENTLY idx_elemental_effect_modifiers_key ON elemental_effect_modifiers(modifier_key, modifier_type);

-- Interactions complex indexes
CREATE INDEX CONCURRENTLY idx_elemental_interactions_result ON elemental_interactions(result_element_id, interaction_type) WHERE result_element_id IS NOT NULL;
CREATE INDEX CONCURRENTLY idx_elemental_interactions_primary_secondary ON elemental_interactions(primary_element_id, secondary_element_id, interaction_type);

-- Weapon configurations performance
CREATE INDEX CONCURRENTLY idx_weapon_elemental_configs_weapon_element ON weapon_elemental_configs(weapon_type, element_id, is_active);
CREATE INDEX CONCURRENTLY idx_weapon_elemental_configs_performance ON weapon_elemental_configs(effect_chance, effect_damage_multiplier DESC, effect_duration_seconds DESC);

-- Character effects high-load indexes
CREATE INDEX CONCURRENTLY idx_character_elemental_effects_character_active ON character_elemental_effects(character_id, is_active, expires_at DESC);
CREATE INDEX CONCURRENTLY idx_character_elemental_effects_source ON character_elemental_effects(source_character_id, effect_id, applied_at DESC);
CREATE INDEX CONCURRENTLY idx_character_elemental_effects_stacks ON character_elemental_effects(current_stacks, max_stacks, remaining_duration_seconds DESC);

-- Damage history performance
CREATE INDEX CONCURRENTLY idx_elemental_effect_damage_effect_instance ON elemental_effect_damage(effect_instance_id, damage_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_elemental_effect_damage_target ON elemental_effect_damage(target_character_id, damage_type, damage_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_elemental_effect_damage_critical ON elemental_effect_damage(is_critical, damage_amount DESC) WHERE is_critical = true;

-- Effect interactions performance
CREATE INDEX CONCURRENTLY idx_elemental_effect_interactions_character ON elemental_effect_interactions(character_id, interaction_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_elemental_effect_interactions_primary ON elemental_effect_interactions(primary_effect_id, secondary_effect_id);

-- Environmental zones spatial queries
CREATE INDEX CONCURRENTLY idx_environmental_elemental_zones_bounds ON environmental_elemental_zones USING GIN (zone_bounds) WHERE zone_bounds IS NOT NULL;
CREATE INDEX CONCURRENTLY idx_environmental_elemental_zones_center ON environmental_elemental_zones USING GIN (zone_center) WHERE zone_center IS NOT NULL;
CREATE INDEX CONCURRENTLY idx_environmental_elemental_zones_radius ON environmental_elemental_zones(zone_radius, element_id) WHERE zone_radius IS NOT NULL;

-- Zone effects performance
CREATE INDEX CONCURRENTLY idx_environmental_zone_effects_zone_character ON environmental_zone_effects(zone_id, character_id, is_still_in_zone);
CREATE INDEX CONCURRENTLY idx_environmental_zone_effects_active ON environmental_zone_effects(is_still_in_zone, last_effect_applied_at DESC) WHERE is_still_in_zone = true;

-- Analytics performance
CREATE INDEX CONCURRENTLY idx_elemental_effects_stats_date_element ON elemental_effects_stats(date DESC, element_id, total_applications DESC);
CREATE INDEX CONCURRENTLY idx_elemental_effects_stats_weapon ON elemental_effects_stats(weapon_type, element_id, date DESC);

-- Telemetry high-volume indexes
CREATE INDEX CONCURRENTLY idx_elemental_telemetry_events_character_session ON elemental_telemetry_events(character_id, session_id, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_elemental_telemetry_events_match ON elemental_telemetry_events(match_id, event_type, event_timestamp DESC);
CREATE INDEX CONCURRENTLY idx_elemental_telemetry_events_damage ON elemental_telemetry_events(damage_amount DESC, event_type) WHERE damage_amount IS NOT NULL AND event_type = 'DAMAGE_DEALT';

-- Weapon performance metrics
CREATE INDEX CONCURRENTLY idx_weapon_elemental_performance_weapon_element ON weapon_elemental_performance(weapon_type, element_id, measured_at DESC);
CREATE INDEX CONCURRENTLY idx_weapon_elemental_performance_accuracy ON weapon_elemental_performance(effect_accuracy DESC, average_damage_per_effect DESC);

-- =================================================================================================
-- PARTIAL INDEXES FOR SPECIFIC QUERIES
-- =================================================================================================

-- Active elemental effects only
CREATE INDEX CONCURRENTLY idx_character_elemental_effects_active_only ON character_elemental_effects(id, character_id, effect_id, current_stacks, remaining_duration_seconds) WHERE is_active = true;

-- Expiring effects (for cleanup)
CREATE INDEX CONCURRENTLY idx_character_elemental_effects_expiring_soon ON character_elemental_effects(id, expires_at) WHERE expires_at IS NOT NULL AND expires_at <= NOW() + INTERVAL '5 minutes';

-- High-damage effects
CREATE INDEX CONCURRENTLY idx_elemental_effect_damage_high_damage ON elemental_effect_damage(damage_amount DESC, damage_timestamp DESC) WHERE damage_amount > 50;

-- Critical hits only
CREATE INDEX CONCURRENTLY idx_elemental_effect_damage_critical_only ON elemental_effect_damage(target_character_id, damage_amount DESC, damage_timestamp DESC) WHERE is_critical = true;

-- Active environmental zones
CREATE INDEX CONCURRENTLY idx_environmental_elemental_zones_active_spatial ON environmental_elemental_zones(id, element_id, zone_center, zone_radius) WHERE is_active = true;

-- Recent telemetry (last hour)
CREATE INDEX CONCURRENTLY idx_elemental_telemetry_events_recent ON elemental_telemetry_events(event_timestamp DESC, event_type) WHERE event_timestamp > NOW() - INTERVAL '1 hour';

-- High-performance weapon stats
CREATE INDEX CONCURRENTLY idx_weapon_elemental_performance_high_accuracy ON weapon_elemental_performance(weapon_type, effect_accuracy DESC) WHERE effect_accuracy > 0.5;

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS
-- =================================================================================================

-- Character elemental effect summary
CREATE MATERIALIZED VIEW character_elemental_summary AS
SELECT
    cee.character_id,
    COUNT(*) as total_active_effects,
    COUNT(*) FILTER (WHERE cee.current_stacks = cee.max_stacks) as max_stacks_effects,
    AVG(cee.remaining_duration_seconds) as avg_effect_duration,
    SUM(cee.total_damage_dealt) as total_damage_from_effects,
    array_agg(DISTINCT et.element_key) as active_elements,
    MAX(cee.applied_at) as last_effect_applied,
    COUNT(DISTINCT cee.source_character_id) as unique_attackers
FROM character_elemental_effects cee
JOIN elemental_effects ee ON ee.id = cee.effect_id
JOIN elemental_types et ON et.id = ee.element_id
WHERE cee.is_active = true
GROUP BY cee.character_id;

-- Elemental effectiveness by weapon type
CREATE MATERIALIZED VIEW weapon_elemental_effectiveness AS
SELECT
    wep.weapon_type,
    et.element_key,
    COUNT(DISTINCT eed.id) as total_damage_events,
    SUM(eed.damage_amount) as total_damage_dealt,
    AVG(eed.damage_amount) as avg_damage_per_hit,
    COUNT(*) FILTER (WHERE eed.is_critical = true) as critical_hits,
    ROUND(
        COUNT(*) FILTER (WHERE eed.is_critical = true)::decimal /
        NULLIF(COUNT(*), 0) * 100, 2
    ) as critical_hit_rate,
    array_agg(DISTINCT eed.damage_type) as damage_types_used
FROM weapon_elemental_performance wep
JOIN elemental_types et ON et.id = wep.element_id
LEFT JOIN elemental_telemetry_events ete ON ete.weapon_type = wep.weapon_type
    AND ete.element_id = wep.element_id
    AND ete.event_type = 'DAMAGE_DEALT'
LEFT JOIN elemental_effect_damage eed ON eed.damage_timestamp >= wep.measured_at - INTERVAL '1 hour'
    AND eed.damage_timestamp <= wep.measured_at
GROUP BY wep.weapon_type, et.element_key, wep.element_id;

-- Elemental interaction frequency
CREATE MATERIALIZED VIEW elemental_interaction_frequency AS
SELECT
    ei.primary_element_id,
    et1.element_key as primary_element,
    ei.secondary_element_id,
    et2.element_key as secondary_element,
    ei.interaction_type,
    COUNT(eei.id) as total_interactions,
    SUM(eei.result_damage) as total_result_damage,
    AVG(EXTRACT(EPOCH FROM (eei.interaction_timestamp - (
        SELECT MIN(applied_at)
        FROM character_elemental_effects cee1
        WHERE cee1.id = eei.primary_effect_id
    )))) as avg_time_to_interaction,
    array_agg(DISTINCT eei.interaction_data) FILTER (WHERE eei.interaction_data IS NOT NULL) as interaction_variations
FROM elemental_interactions ei
JOIN elemental_types et1 ON et1.id = ei.primary_element_id
JOIN elemental_types et2 ON et2.id = ei.secondary_element_id
LEFT JOIN elemental_effect_interactions eei ON eei.interaction_id = ei.id
    AND eei.interaction_timestamp >= CURRENT_DATE - INTERVAL '7 days'
GROUP BY ei.primary_element_id, et1.element_key, ei.secondary_element_id, et2.element_key, ei.interaction_type, ei.id;

-- Environmental zone effectiveness
CREATE MATERIALIZED VIEW environmental_zone_effectiveness AS
SELECT
    eez.zone_key,
    eez.zone_type,
    et.element_key,
    COUNT(eze.id) as total_exposures,
    COUNT(DISTINCT eze.character_id) as unique_characters_affected,
    AVG(EXTRACT(EPOCH FROM (eze.exited_at - eze.entered_at))) FILTER (WHERE eze.exited_at IS NOT NULL) as avg_exposure_time,
    SUM(eze.total_effects_applied) as total_effects_applied,
    AVG(eze.total_effects_applied) as avg_effects_per_exposure,
    COUNT(*) FILTER (WHERE eze.is_still_in_zone = true) as currently_exposed
FROM environmental_elemental_zones eez
JOIN elemental_types et ON et.id = eez.element_id
LEFT JOIN environmental_zone_effects eze ON eze.zone_id = eez.id
    AND eze.entered_at >= CURRENT_DATE - INTERVAL '30 days'
WHERE eez.is_active = true
GROUP BY eez.zone_key, eez.zone_type, et.element_key, eez.id;

-- Daily elemental combat statistics
CREATE MATERIALIZED VIEW daily_elemental_combat_stats AS
SELECT
    DATE(ete.event_timestamp) as combat_date,
    ete.element_id,
    et.element_key,
    ete.weapon_type,
    COUNT(*) FILTER (WHERE ete.event_type = 'EFFECT_APPLIED') as effects_applied,
    COUNT(*) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as damage_events,
    SUM(ete.damage_amount) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as total_damage,
    AVG(ete.damage_amount) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as avg_damage,
    COUNT(DISTINCT ete.character_id) as unique_characters,
    COUNT(DISTINCT ete.target_character_id) as unique_targets,
    COUNT(*) FILTER (WHERE ete.event_type = 'EFFECT_INTERACTION') as interactions_triggered
FROM elemental_telemetry_events ete
JOIN elemental_types et ON et.id = ete.element_id
WHERE ete.event_timestamp >= CURRENT_DATE - INTERVAL '30 days'
GROUP BY DATE(ete.event_timestamp), ete.element_id, et.element_key, ete.weapon_type;

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_character_elemental_summary_character ON character_elemental_summary(character_id);
CREATE INDEX idx_character_elemental_summary_damage ON character_elemental_summary(total_damage_from_effects DESC);

CREATE INDEX idx_weapon_elemental_effectiveness_weapon ON weapon_elemental_effectiveness(weapon_type, element_key);
CREATE INDEX idx_weapon_elemental_effectiveness_damage ON weapon_elemental_effectiveness(total_damage_dealt DESC);

CREATE INDEX idx_elemental_interaction_frequency_elements ON elemental_interaction_frequency(primary_element, secondary_element);
CREATE INDEX idx_elemental_interaction_frequency_type ON elemental_interaction_frequency(interaction_type, total_interactions DESC);

CREATE INDEX idx_environmental_zone_effectiveness_zone ON environmental_zone_effectiveness(zone_key);
CREATE INDEX idx_environmental_zone_effectiveness_exposure ON environmental_zone_effectiveness(total_exposures DESC);

CREATE INDEX idx_daily_elemental_combat_stats_date ON daily_elemental_combat_stats(combat_date DESC);
CREATE INDEX idx_daily_elemental_combat_stats_element ON daily_elemental_combat_stats(element_key, combat_date DESC);

-- =================================================================================================
-- PARTITIONING SETUP FOR HIGH-VOLUME TABLES
-- =================================================================================================

-- Partition character elemental effects by month
CREATE TABLE character_elemental_effects_2025_01 PARTITION OF character_elemental_effects
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE character_elemental_effects_2025_02 PARTITION OF character_elemental_effects
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition elemental effect damage by month
CREATE TABLE elemental_effect_damage_2025_01 PARTITION OF elemental_effect_damage
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE elemental_effect_damage_2025_02 PARTITION OF elemental_effect_damage
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Partition telemetry events by month
CREATE TABLE elemental_telemetry_events_2025_01 PARTITION OF elemental_telemetry_events
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE elemental_telemetry_events_2025_02 PARTITION OF elemental_telemetry_events
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- =================================================================================================
-- PERFORMANCE MONITORING FUNCTIONS
-- =================================================================================================

-- Function to get character elemental status efficiently
CREATE OR REPLACE FUNCTION get_character_elemental_status(character_id_param BIGINT)
RETURNS TABLE (
    element_key VARCHAR(50),
    total_effects INTEGER,
    active_effects INTEGER,
    total_damage_dealt BIGINT,
    max_stacks_reached INTEGER,
    longest_duration INTERVAL,
    most_damage_effect VARCHAR(100)
) AS $$
BEGIN
    RETURN QUERY
    WITH effect_stats AS (
        SELECT
            et.element_key,
            COUNT(cee.id) as total_effects_count,
            COUNT(cee.id) FILTER (WHERE cee.is_active = true) as active_effects_count,
            COALESCE(SUM(cee.total_damage_dealt), 0) as total_damage,
            COUNT(cee.id) FILTER (WHERE cee.current_stacks = cee.max_stacks) as max_stacks_count,
            MAX(cee.remaining_duration_seconds) as max_duration_seconds,
            (
                SELECT ee.effect_key
                FROM character_elemental_effects cee2
                JOIN elemental_effects ee ON ee.id = cee2.effect_id
                WHERE cee2.character_id = character_id_param
                AND cee2.total_damage_dealt > 0
                ORDER BY cee2.total_damage_dealt DESC
                LIMIT 1
            ) as top_damage_effect
        FROM elemental_types et
        LEFT JOIN elemental_effects ee ON ee.element_id = et.id
        LEFT JOIN character_elemental_effects cee ON cee.effect_id = ee.id AND cee.character_id = character_id_param
        GROUP BY et.element_key
    )
    SELECT
        es.element_key,
        es.total_effects_count,
        es.active_effects_count,
        es.total_damage,
        es.max_stacks_count,
        (es.max_duration_seconds || ' seconds')::INTERVAL as longest_duration,
        es.top_damage_effect
    FROM effect_stats es
    WHERE es.total_effects_count > 0
    ORDER BY es.total_damage DESC;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate weapon elemental performance
CREATE OR REPLACE FUNCTION calculate_weapon_elemental_performance(
    weapon_type_param VARCHAR(50),
    element_id_param BIGINT,
    time_window_hours INTEGER DEFAULT 24
)
RETURNS TABLE (
    weapon_type VARCHAR(50),
    element_key VARCHAR(50),
    shots_fired BIGINT,
    effects_applied BIGINT,
    effect_accuracy DECIMAL(5,2),
    total_damage BIGINT,
    avg_damage_per_effect DECIMAL(8,2),
    critical_hit_rate DECIMAL(5,2),
    kill_count BIGINT
) AS $$
DECLARE
    time_cutoff TIMESTAMP WITH TIME ZONE;
BEGIN
    time_cutoff := NOW() - INTERVAL '1 hour' * time_window_hours;

    RETURN QUERY
    WITH weapon_stats AS (
        SELECT
            ete.weapon_type,
            et.element_key,
            COUNT(*) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as shots_fired,
            COUNT(*) FILTER (WHERE ete.event_type = 'EFFECT_APPLIED') as effects_applied,
            SUM(ete.damage_amount) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as total_damage,
            AVG(ete.damage_amount) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as avg_damage,
            COUNT(*) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT' AND ete.damage_amount > 100) as critical_hits,
            COUNT(DISTINCT ete.target_character_id) FILTER (WHERE ete.event_type = 'DAMAGE_DEALT') as unique_targets
        FROM elemental_telemetry_events ete
        JOIN elemental_types et ON et.id = ete.element_id
        WHERE ete.weapon_type = weapon_type_param
          AND ete.element_id = element_id_param
          AND ete.event_timestamp >= time_cutoff
        GROUP BY ete.weapon_type, et.element_key
    )
    SELECT
        ws.weapon_type,
        ws.element_key,
        ws.shots_fired,
        ws.effects_applied,
        CASE
            WHEN ws.shots_fired > 0
            THEN ROUND((ws.effects_applied::DECIMAL / ws.shots_fired) * 100, 2)
            ELSE 0.0
        END as effect_accuracy,
        COALESCE(ws.total_damage, 0) as total_damage,
        COALESCE(ws.avg_damage, 0) as avg_damage_per_effect,
        CASE
            WHEN ws.shots_fired > 0
            THEN ROUND((ws.critical_hits::DECIMAL / ws.shots_fired) * 100, 2)
            ELSE 0.0
        END as critical_hit_rate,
        ws.unique_targets as kill_count
    FROM weapon_stats ws;
END;
$$ LANGUAGE plpgsql;

-- Function to get active environmental effects for character
CREATE OR REPLACE FUNCTION get_character_environmental_effects(character_id_param BIGINT)
RETURNS TABLE (
    zone_key VARCHAR(100),
    element_key VARCHAR(50),
    effect_key VARCHAR(100),
    exposure_time INTERVAL,
    effects_applied INTEGER,
    total_damage BIGINT,
    is_still_active BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        eez.zone_key,
        et.element_key,
        ee.effect_key,
        CASE
            WHEN eze.exited_at IS NOT NULL
            THEN eze.exited_at - eze.entered_at
            ELSE NOW() - eze.entered_at
        END as exposure_time,
        eze.total_effects_applied,
        COALESCE(SUM(eed.damage_amount), 0) as total_damage,
        eze.is_still_in_zone
    FROM environmental_zone_effects eze
    JOIN environmental_elemental_zones eez ON eez.id = eze.zone_id
    JOIN elemental_types et ON et.id = eez.element_id
    JOIN elemental_effects ee ON ee.id = eez.effect_id
    LEFT JOIN elemental_effect_damage eed ON eed.effect_instance_id = eze.effect_instance_id
    WHERE eze.character_id = character_id_param
      AND eze.entered_at >= CURRENT_DATE - INTERVAL '7 days'
    GROUP BY eze.id, eez.zone_key, et.element_key, ee.effect_key, eze.entered_at, eze.exited_at, eze.total_effects_applied, eze.is_still_in_zone
    ORDER BY eze.entered_at DESC;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- ANALYTICS REFRESH FUNCTION
-- =================================================================================================

-- Function to refresh all weapon elemental analytics views
CREATE OR REPLACE FUNCTION refresh_weapon_elemental_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY character_elemental_summary;
    REFRESH MATERIALIZED VIEW CONCURRENTLY weapon_elemental_effectiveness;
    REFRESH MATERIALIZED VIEW CONCURRENTLY elemental_interaction_frequency;
    REFRESH MATERIALIZED VIEW CONCURRENTLY environmental_zone_effectiveness;
    REFRESH MATERIALIZED VIEW CONCURRENTLY daily_elemental_combat_stats;

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('weapon-elemental-effects', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- MAINTENANCE FUNCTIONS
-- =================================================================================================

-- Function to clean up expired elemental effects
CREATE OR REPLACE FUNCTION cleanup_expired_elemental_effects()
RETURNS INTEGER AS $$
DECLARE
    expired_count INTEGER;
BEGIN
    -- Mark effects as inactive
    UPDATE character_elemental_effects
    SET is_active = false,
        updated_at = NOW()
    WHERE is_active = true
      AND (expires_at IS NOT NULL AND expires_at < NOW());

    GET DIAGNOSTICS expired_count = ROW_COUNT;
    RETURN expired_count;
END;
$$ LANGUAGE plpgsql;

-- Function to clean up old elemental telemetry
CREATE OR REPLACE FUNCTION cleanup_old_elemental_telemetry(days_to_keep INTEGER DEFAULT 90)
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM elemental_telemetry_events
    WHERE event_timestamp < CURRENT_DATE - INTERVAL '1 day' * days_to_keep;

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to update environmental zone effects
CREATE OR REPLACE FUNCTION update_environmental_zone_effects()
RETURNS INTEGER AS $$
DECLARE
    updated_count INTEGER;
BEGIN
    -- Update effects for characters still in zones
    UPDATE environmental_zone_effects
    SET last_effect_applied_at = NOW(),
        total_effects_applied = total_effects_applied + 1
    WHERE is_still_in_zone = true
      AND last_effect_applied_at <= NOW() - INTERVAL '1 second' * (
          SELECT effect_interval_seconds
          FROM environmental_elemental_zones eez
          WHERE eez.id = environmental_zone_effects.zone_id
      );

    GET DIAGNOSTICS updated_count = ROW_COUNT;
    RETURN updated_count;
END;
$$ LANGUAGE plpgsql;

-- Function to validate elemental effect data integrity
CREATE OR REPLACE FUNCTION validate_elemental_effects_integrity()
RETURNS TABLE (
    issue_type VARCHAR(50),
    issue_description TEXT,
    affected_records BIGINT
) AS $$
BEGIN
    -- Check for effects with invalid stacks
    RETURN QUERY
    SELECT
        'invalid_stacks'::VARCHAR(50) as issue_type,
        'Effects with current_stacks > max_stacks'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM character_elemental_effects cee
    WHERE cee.current_stacks > cee.max_stacks;

    -- Check for effects without valid source
    RETURN QUERY
    SELECT
        'missing_source'::VARCHAR(50) as issue_type,
        'Effects without valid source_weapon_id or source_character_id'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM character_elemental_effects cee
    WHERE cee.source_weapon_id IS NULL AND cee.source_character_id IS NULL;

    -- Check for orphaned damage records
    RETURN QUERY
    SELECT
        'orphaned_damage'::VARCHAR(50) as issue_type,
        'Damage records without corresponding effect instance'::TEXT as issue_description,
        COUNT(*) as affected_records
    FROM elemental_effect_damage eed
    LEFT JOIN character_elemental_effects cee ON cee.id = eed.effect_instance_id
    WHERE cee.id IS NULL;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- PERFORMANCE MONITORING TABLES
-- =================================================================================================

-- Query performance monitoring for elemental operations
CREATE TABLE weapon_elemental_query_performance (
    id BIGSERIAL PRIMARY KEY,
    operation_type VARCHAR(50) NOT NULL, -- 'apply_effect', 'calculate_interaction', 'get_character_status', 'update_environmental'
    execution_time_ms INTEGER NOT NULL,
    character_id BIGINT,
    weapon_type VARCHAR(50),
    element_id BIGINT,
    parameters JSONB,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance monitoring
CREATE INDEX idx_weapon_elemental_query_performance_type_time ON weapon_elemental_query_performance(operation_type, executed_at DESC);
CREATE INDEX idx_weapon_elemental_query_performance_slow ON weapon_elemental_query_performance(execution_time_ms DESC) WHERE execution_time_ms > 500;
