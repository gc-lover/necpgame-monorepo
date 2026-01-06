-- Companion System Performance Optimizations
-- Version: V003
-- Description: Additional indexes and performance optimizations for Companion System

-- =================================================================================================
-- ADDITIONAL INDEXES FOR PERFORMANCE
-- =================================================================================================

-- Complex query indexes for companion catalog
CREATE INDEX CONCURRENTLY idx_companion_types_category_rarity ON companion_types(category, rarity);
CREATE INDEX CONCURRENTLY idx_companion_types_purchase ON companion_types(is_purchasable, purchase_cost) WHERE is_purchasable = true;
CREATE INDEX CONCURRENTLY idx_companion_types_enabled_category ON companion_types(is_enabled, category) WHERE is_enabled = true;

-- Companion templates performance indexes
CREATE INDEX CONCURRENTLY idx_companion_templates_type_default ON companion_templates(companion_type_id, is_default);
CREATE INDEX CONCURRENTLY idx_companion_templates_active ON companion_templates(id) WHERE is_default = true;

-- Character companions complex indexes
CREATE INDEX CONCURRENTLY idx_character_companions_character_active ON character_companions(character_id, is_active) WHERE is_active = true;
CREATE INDEX CONCURRENTLY idx_character_companions_character_level ON character_companions(character_id, current_level DESC);
CREATE INDEX CONCURRENTLY idx_character_companions_template_character ON character_companions(companion_template_id, character_id);
CREATE INDEX CONCURRENTLY idx_character_companions_last_used ON character_companions(last_used_at DESC) WHERE last_used_at IS NOT NULL;

-- Companion stats performance indexes
CREATE INDEX CONCURRENTLY idx_companion_stats_companion_stat ON companion_stats(character_companion_id, stat_key);
CREATE INDEX CONCURRENTLY idx_companion_stats_stat_value ON companion_stats(stat_key, final_value DESC);

-- Abilities performance indexes
CREATE INDEX CONCURRENTLY idx_companion_unlocked_abilities_companion_equipped ON companion_unlocked_abilities(character_companion_id, is_equipped) WHERE is_equipped = true;
CREATE INDEX CONCURRENTLY idx_companion_unlocked_abilities_ability ON companion_unlocked_abilities(companion_ability_id);
CREATE INDEX CONCURRENTLY idx_companion_abilities_type_category ON companion_abilities(type, category);
CREATE INDEX CONCURRENTLY idx_companion_abilities_enabled ON companion_abilities(is_enabled) WHERE is_enabled = true;

-- Effects performance indexes
CREATE INDEX CONCURRENTLY idx_companion_active_effects_companion_type ON companion_active_effects(character_companion_id, effect_type);
CREATE INDEX CONCURRENTLY idx_companion_active_effects_expiring ON companion_active_effects(expires_at) WHERE expires_at IS NOT NULL;
CREATE INDEX CONCURRENTLY idx_companion_active_effects_source ON companion_active_effects(source_type, source_id);

-- Template abilities indexes
CREATE INDEX CONCURRENTLY idx_companion_template_abilities_template_level ON companion_template_abilities(companion_template_id, unlock_level);
CREATE INDEX CONCURRENTLY idx_companion_template_abilities_default ON companion_template_abilities(companion_template_id, is_default) WHERE is_default = true;

-- Progression indexes
CREATE INDEX CONCURRENTLY idx_companion_levels_template_level ON companion_levels(companion_template_id, level);
CREATE INDEX CONCURRENTLY idx_companion_experience_events_companion_time ON companion_experience_events(character_companion_id, gained_at DESC);

-- Inventory slots
CREATE INDEX CONCURRENTLY idx_companion_inventory_slots_character_unlocked ON companion_inventory_slots(character_id, is_unlocked) WHERE is_unlocked = true;

-- =================================================================================================
-- PARTIAL INDEXES FOR COMMON QUERIES
-- =================================================================================================

-- Active companions only
CREATE INDEX CONCURRENTLY idx_character_companions_active_only ON character_companions(id, character_id, companion_template_id, current_level) WHERE is_active = true;

-- Unlocked abilities only
CREATE INDEX CONCURRENTLY idx_companion_unlocked_abilities_active ON companion_unlocked_abilities(character_companion_id, companion_ability_id, equipment_slot) WHERE is_equipped = true;

-- Current active effects
CREATE INDEX CONCURRENTLY idx_companion_active_effects_current ON companion_active_effects(character_companion_id, effect_key, applied_at DESC) WHERE expires_at IS NULL OR expires_at > NOW();

-- Purchasable companions
CREATE INDEX CONCURRENTLY idx_companion_types_purchasable_cost ON companion_types(id, type_key, purchase_cost, currency_type) WHERE is_purchasable = true AND is_enabled = true;

-- =================================================================================================
-- MATERIALIZED VIEWS FOR ANALYTICS
-- =================================================================================================

-- Companion usage statistics
CREATE MATERIALIZED VIEW companion_usage_stats AS
SELECT
    ct.type_key,
    ct.category,
    ct.rarity,
    COUNT(cc.id) as total_owned,
    COUNT(cc.id) FILTER (WHERE cc.is_active = true) as currently_active,
    AVG(cc.current_level) as avg_level,
    MAX(cc.current_level) as max_level,
    AVG(cc.current_experience) as avg_experience,
    COUNT(DISTINCT cc.character_id) as unique_owners
FROM companion_types ct
LEFT JOIN companion_templates cmp_t ON cmp_t.companion_type_id = ct.id
LEFT JOIN character_companions cc ON cc.companion_template_id = cmp_t.id
GROUP BY ct.id, ct.type_key, ct.category, ct.rarity;

-- Companion ability usage statistics
CREATE MATERIALIZED VIEW companion_ability_usage_stats AS
SELECT
    ca.ability_key,
    ca.name,
    ca.type,
    ca.category,
    COUNT(cau.id) as total_uses,
    COUNT(DISTINCT cau.character_companion_id) as unique_users,
    AVG(EXTRACT(EPOCH FROM (cau.used_at - LAG(cau.used_at) OVER (PARTITION BY cau.character_companion_id, cau.companion_ability_id ORDER BY cau.used_at)))) as avg_cooldown_usage,
    MAX(cau.used_at) as last_used
FROM companion_abilities ca
LEFT JOIN companion_ability_usage cau ON cau.companion_ability_id = ca.id
GROUP BY ca.id, ca.ability_key, ca.name, ca.type, ca.category;

-- Character companion summary
CREATE MATERIALIZED VIEW character_companion_summary AS
SELECT
    cc.character_id,
    COUNT(*) as total_companions,
    COUNT(*) FILTER (WHERE cc.is_active = true) as active_companions,
    AVG(cc.current_level) as avg_companion_level,
    MAX(cc.current_level) as max_companion_level,
    SUM(cc.current_experience) as total_experience,
    COUNT(DISTINCT cta.category) as unique_categories,
    COUNT(DISTINCT cta.rarity) as unique_rarities
FROM character_companions cc
JOIN companion_templates ct ON ct.id = cc.companion_template_id
JOIN companion_types cta ON cta.id = ct.companion_type_id
GROUP BY cc.character_id;

-- =================================================================================================
-- INDEXES FOR MATERIALIZED VIEWS
-- =================================================================================================

CREATE INDEX idx_companion_usage_stats_type ON companion_usage_stats(type_key);
CREATE INDEX idx_companion_usage_stats_category ON companion_usage_stats(category);
CREATE INDEX idx_companion_usage_stats_rarity ON companion_usage_stats(rarity);

CREATE INDEX idx_companion_ability_usage_stats_ability ON companion_ability_usage_stats(ability_key);
CREATE INDEX idx_companion_ability_usage_stats_category ON companion_ability_usage_stats(category);

CREATE INDEX idx_character_companion_summary_character ON character_companion_summary(character_id);
CREATE INDEX idx_character_companion_summary_level ON character_companion_summary(avg_companion_level);

-- =================================================================================================
-- PARTITIONING SETUP
-- =================================================================================================

-- Partition companion telemetry by month for better performance
-- Note: This requires PostgreSQL 10+ with declarative partitioning

-- Create partitioned table for telemetry
CREATE TABLE companion_telemetry_y2025m01 PARTITION OF companion_telemetry
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');

CREATE TABLE companion_telemetry_y2025m02 PARTITION OF companion_telemetry
    FOR VALUES FROM ('2025-02-01') TO ('2025-03-01');

-- Future partitions can be added as needed
-- This is a template for monthly partitioning

-- =================================================================================================
-- FUNCTIONS FOR COMMON QUERIES
-- =================================================================================================

-- Function to get companion stats efficiently
CREATE OR REPLACE FUNCTION get_companion_stats(companion_id BIGINT)
RETURNS TABLE (
    stat_key VARCHAR(50),
    base_value DECIMAL(10,2),
    bonus_value DECIMAL(10,2),
    multiplier DECIMAL(5,2),
    final_value DECIMAL(10,2)
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cs.stat_key,
        cs.base_value,
        cs.bonus_value,
        cs.multiplier,
        cs.final_value
    FROM companion_stats cs
    WHERE cs.character_companion_id = companion_id
    ORDER BY cs.stat_key;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate companion level progression
CREATE OR REPLACE FUNCTION calculate_companion_level_progress(companion_id BIGINT)
RETURNS TABLE (
    current_level INTEGER,
    current_experience BIGINT,
    experience_to_next BIGINT,
    progress_percentage DECIMAL(5,2)
) AS $$
DECLARE
    companion_record RECORD;
    next_level_exp BIGINT;
BEGIN
    -- Get companion data
    SELECT cc.current_level, cc.current_experience, cc.companion_template_id
    INTO companion_record
    FROM character_companions cc
    WHERE cc.id = companion_id;

    IF NOT FOUND THEN
        RETURN;
    END IF;

    -- Get experience required for next level
    SELECT cl.experience_required
    INTO next_level_exp
    FROM companion_levels cl
    WHERE cl.companion_template_id = companion_record.companion_template_id
      AND cl.level = companion_record.current_level + 1;

    -- Return progression data
    RETURN QUERY
    SELECT
        companion_record.current_level,
        companion_record.current_experience,
        COALESCE(next_level_exp - companion_record.current_experience, 0)::BIGINT,
        CASE
            WHEN next_level_exp IS NOT NULL AND next_level_exp > 0
            THEN (companion_record.current_experience::DECIMAL / next_level_exp) * 100
            ELSE 100.0
        END;
END;
$$ LANGUAGE plpgsql;

-- Function to get active companion effects
CREATE OR REPLACE FUNCTION get_active_companion_effects(companion_id BIGINT)
RETURNS TABLE (
    effect_type VARCHAR(50),
    effect_key VARCHAR(100),
    effect_data JSONB,
    stacks INTEGER,
    expires_at TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        cae.effect_type,
        cae.effect_key,
        cae.effect_data,
        cae.stacks,
        cae.expires_at
    FROM companion_active_effects cae
    WHERE cae.character_companion_id = companion_id
      AND (cae.expires_at IS NULL OR cae.expires_at > NOW())
    ORDER BY cae.applied_at DESC;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- REFRESH MATERIALIZED VIEWS FUNCTION
-- =================================================================================================

-- Function to refresh all companion analytics views
CREATE OR REPLACE FUNCTION refresh_companion_analytics()
RETURNS VOID AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY companion_usage_stats;
    REFRESH MATERIALIZED VIEW CONCURRENTLY companion_ability_usage_stats;
    REFRESH MATERIALIZED VIEW CONCURRENTLY character_companion_summary;

    -- Log refresh completion
    INSERT INTO system_logs (component, event, details, logged_at)
    VALUES ('companion-system', 'analytics_refresh', '{"status": "completed"}', NOW());
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- MAINTENANCE FUNCTIONS
-- =================================================================================================

-- Function to clean up expired effects
CREATE OR REPLACE FUNCTION cleanup_expired_companion_effects()
RETURNS INTEGER AS $$
DECLARE
    deleted_count INTEGER;
BEGIN
    DELETE FROM companion_active_effects
    WHERE expires_at IS NOT NULL AND expires_at < NOW();

    GET DIAGNOSTICS deleted_count = ROW_COUNT;
    RETURN deleted_count;
END;
$$ LANGUAGE plpgsql;

-- Function to archive old telemetry data
CREATE OR REPLACE FUNCTION archive_old_companion_telemetry(days_old INTEGER DEFAULT 90)
RETURNS INTEGER AS $$
DECLARE
    archived_count INTEGER;
BEGIN
    -- This would typically move data to an archive table
    -- For now, just count what would be archived
    SELECT COUNT(*) INTO archived_count
    FROM companion_telemetry
    WHERE timestamp < NOW() - INTERVAL '1 day' * days_old;

    -- In production, you would:
    -- INSERT INTO companion_telemetry_archive SELECT * FROM companion_telemetry WHERE ...
    -- DELETE FROM companion_telemetry WHERE ...

    RETURN archived_count;
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- PERFORMANCE MONITORING SETUP
-- =================================================================================================

-- Create a table to track slow queries (for monitoring)
CREATE TABLE companion_query_performance (
    id BIGSERIAL PRIMARY KEY,
    query_type VARCHAR(100) NOT NULL,
    execution_time_ms INTEGER NOT NULL,
    rows_affected INTEGER,
    query_params JSONB,
    executed_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Index for performance monitoring
CREATE INDEX idx_companion_query_performance_type_time ON companion_query_performance(query_type, executed_at DESC);
CREATE INDEX idx_companion_query_performance_slow ON companion_query_performance(execution_time_ms DESC) WHERE execution_time_ms > 1000;
