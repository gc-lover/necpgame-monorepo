-- P2P Trade System Initial Data
-- Version: V002
-- Description: Initial configuration data for P2P Trade System including rules, restrictions, and default settings

-- =================================================================================================
-- TRADE CONFIGURATION RULES
-- =================================================================================================

INSERT INTO trade_config_rules (
    rule_key, rule_type, rule_config, player_level_min, player_level_max, zone_restrictions
) VALUES
-- Distance limits
('default_distance_limit', 'distance_limit',
 '{"max_distance_meters": 10.0, "check_interval_seconds": 30}', NULL, NULL, '[]'),

('high_security_distance_limit', 'distance_limit',
 '{"max_distance_meters": 5.0, "strict_enforcement": true}', 1, 10, '[]'),

('vip_distance_limit', 'distance_limit',
 '{"max_distance_meters": 25.0, "allow_teleport_trading": true}', 50, NULL,
 '["vip_lounge", "executive_suite"]'),

-- Timeout settings
('default_trade_timeout', 'timeout',
 '{"session_timeout_minutes": 5, "offer_change_timeout_seconds": 30}', NULL, NULL, '[]'),

('quick_trade_timeout', 'timeout',
 '{"session_timeout_minutes": 2, "offer_change_timeout_seconds": 15}', NULL, NULL,
 '["combat_zones", "raid_areas"]'),

('extended_trade_timeout', 'timeout',
 '{"session_timeout_minutes": 15, "offer_change_timeout_seconds": 60}', NULL, NULL,
 '["safe_zones", "trading_posts"]'),

-- Rate limits
('default_rate_limit', 'rate_limit',
 '{"hourly_limit": 10, "daily_limit": 50, "concurrent_sessions": 3}', NULL, NULL, '[]'),

('premium_rate_limit', 'rate_limit',
 '{"hourly_limit": 25, "daily_limit": 100, "concurrent_sessions": 5}', 20, NULL, '[]'),

('restricted_rate_limit', 'rate_limit',
 '{"hourly_limit": 3, "daily_limit": 10, "concurrent_sessions": 1}', 1, 5, '[]'),

-- Value limits (to prevent money laundering)
('default_value_limit', 'value_limit',
 '{"max_trade_value": 50000, "max_daily_value": 200000}', NULL, NULL, '[]'),

('vip_value_limit', 'value_limit',
 '{"max_trade_value": 500000, "max_daily_value": 2000000}', 30, NULL, '[]'),

-- Security checks
('basic_security_checks', 'security_checks',
 '{"require_ip_verification": true, "require_device_fingerprint": true, "fraud_score_threshold": 0.3}', NULL, NULL, '[]'),

('strict_security_checks', 'security_checks',
 '{"require_ip_verification": true, "require_device_fingerprint": true, "require_biometric_verification": true, "fraud_score_threshold": 0.1}', 40, NULL, '[]'),

('combat_zone_security', 'security_checks',
 '{"allow_quick_trades": true, "skip_fraud_scoring": false, "fraud_score_threshold": 0.5}', NULL, NULL,
 '["combat_zones", "raid_areas", "battlegrounds"]');

-- =================================================================================================
-- TRADE ITEM RESTRICTIONS
-- =================================================================================================

INSERT INTO trade_item_restrictions (
    item_type, item_subtype, item_id, restriction_reason, restriction_config
) VALUES
-- Quest items
('quest_item', NULL, NULL, 'quest_item',
 '{"block_trade": true, "allow_donation": false, "destruction_on_trade_attempt": true}'),

-- Unique legendary items
('weapon', 'legendary', NULL, 'unique_item',
 '{"block_trade": true, "allow_donation": false, "soulbound_on_equip": true}'),

('armor', 'legendary', NULL, 'unique_item',
 '{"block_trade": true, "allow_donation": false, "soulbound_on_equip": true}'),

-- Bind on pickup items
('consumable', 'bind_on_pickup', NULL, 'bind_on_pickup',
 '{"block_trade": true, "allow_donation": false}'),

('material', 'bind_on_pickup', NULL, 'bind_on_pickup',
 '{"block_trade": true, "allow_donation": false}'),

-- Faction locked items
('weapon', 'corporation_locked', NULL, 'faction_locked',
 '{"block_trade": true, "allow_donation": false, "corporation_bound": true}'),

('armor', 'corporation_locked', NULL, 'faction_locked',
 '{"block_trade": true, "allow_donation": false, "corporation_bound": true}'),

-- Level locked items (too high level)
('weapon', NULL, NULL, 'level_locked',
 '{"min_trader_level": 50, "max_level_difference": 10}'),

('armor', NULL, NULL, 'level_locked',
 '{"min_trader_level": 50, "max_level_difference": 10}'),

-- Illegal/contraband items
('contraband', NULL, NULL, 'illegal_item',
 '{"block_trade": true, "report_on_attempt": true, "security_flag": true}'),

-- Temporary items
('consumable', 'temporary_buff', NULL, 'temporary_item',
 '{"max_duration_hours": 1, "block_trade": true}'),

('consumable', 'event_item', NULL, 'event_item',
 '{"block_trade": true, "allow_donation": false, "event_bound": true}'),

-- Account bound items
('cosmetic', 'account_bound', NULL, 'account_bound',
 '{"block_trade": true, "allow_donation": false, "account_locked": true}'),

('mount', 'account_bound', NULL, 'account_bound',
 '{"block_trade": true, "allow_donation": false, "account_locked": true}'),

-- Tournament rewards
('trophy', 'tournament', NULL, 'tournament_item',
 '{"block_trade": true, "allow_donation": false, "tournament_bound": true}'),

('title', 'tournament', NULL, 'tournament_item',
 '{"block_trade": true, "allow_donation": false, "tournament_bound": true}'),

-- Prototype items
('weapon', 'prototype', NULL, 'prototype_item',
 '{"block_trade": true, "allow_donation": false, "testing_phase": true}'),

('armor', 'prototype', NULL, 'prototype_item',
 '{"block_trade": true, "allow_donation": false, "testing_phase": true}');

-- =================================================================================================
-- DEFAULT RATE LIMITS
-- =================================================================================================

-- These will be created automatically via triggers, but we can set some defaults
-- The trade_rate_limits table will be populated as players start trading

-- =================================================================================================
-- ANALYTICS INITIALIZATION
-- =================================================================================================

-- Initialize analytics for today (will be updated by background jobs)
INSERT INTO trade_analytics (date, analytics_metadata) VALUES
(CURRENT_DATE, '{"initialized": true, "version": "1.0.0"}');

-- =================================================================================================
-- SYSTEM CONFIGURATION DATA
-- =================================================================================================

-- Create a configuration table for system-wide settings
CREATE TABLE IF NOT EXISTS trade_system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value JSONB NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- System-wide trade settings
INSERT INTO trade_system_config (config_key, config_value, description) VALUES
('system_enabled', 'true', 'Whether the P2P trade system is enabled globally'),

('default_session_timeout', '{"minutes": 5}', 'Default timeout for trade sessions'),

('max_concurrent_sessions', '{"per_player": 3, "global_limit": 10000}', 'Concurrent session limits'),

('fraud_detection_enabled', 'true', 'Whether fraud detection is active'),

('fraud_score_threshold', '{"warning": 0.3, "block": 0.7}', 'Fraud score thresholds'),

('analytics_enabled', 'true', 'Whether trade analytics collection is enabled'),

('dispute_system_enabled', 'true', 'Whether the dispute resolution system is active'),

('escrow_enabled', 'true', 'Whether escrow accounts are used for currency transfers'),

('rate_limiting_enabled', 'true', 'Whether rate limiting is enforced'),

('audit_trail_enabled', 'true', 'Whether complete audit trails are maintained'),

('maintenance_mode', 'false', 'Whether the system is in maintenance mode'),

('emergency_stop', 'false', 'Emergency stop flag for critical issues'),

('feature_flags', '{
    "quick_trades": true,
    "bulk_trades": false,
    "cross_faction_trades": true,
    "anonymous_trades": false,
    "premium_features": true,
    "beta_testing": false
}', 'Feature flags for trade system capabilities'),

('regional_settings', '{
    "default": {
        "max_distance": 10.0,
        "timeout_minutes": 5,
        "currency_multiplier": 1.0
    },
    "combat_zones": {
        "max_distance": 5.0,
        "timeout_minutes": 2,
        "currency_multiplier": 0.8
    },
    "safe_zones": {
        "max_distance": 15.0,
        "timeout_minutes": 10,
        "currency_multiplier": 1.2
    }
}', 'Regional-specific trade settings'),

('notification_settings', '{
    "trade_initiated": {"email": false, "in_game": true, "push": false},
    "trade_completed": {"email": false, "in_game": true, "push": false},
    "trade_cancelled": {"email": false, "in_game": true, "push": false},
    "fraud_alert": {"email": true, "in_game": true, "push": true},
    "dispute_raised": {"email": true, "in_game": true, "push": true}
}', 'Notification settings for different trade events');

-- =================================================================================================
-- SECURITY PATTERNS REFERENCE DATA
-- =================================================================================================

-- Create reference data for fraud detection patterns
CREATE TABLE IF NOT EXISTS fraud_pattern_definitions (
    id BIGSERIAL PRIMARY KEY,
    pattern_key VARCHAR(50) UNIQUE NOT NULL,
    pattern_name VARCHAR(100) NOT NULL,
    description TEXT,
    detection_logic JSONB NOT NULL, -- How to detect this pattern
    severity_weight DECIMAL(3,2) NOT NULL, -- 0.0 to 1.0
    false_positive_rate DECIMAL(5,4), -- Estimated false positive rate
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Common fraud patterns
INSERT INTO fraud_pattern_definitions (
    pattern_key, pattern_name, description, detection_logic, severity_weight, false_positive_rate
) VALUES
('ip_sharing', 'IP Address Sharing',
 'Multiple accounts trading from same IP address',
 '{"check_type": "ip_similarity", "threshold": 0.95, "time_window_hours": 24}', 0.6, 0.15),

('rapid_trading', 'Rapid Trading Pattern',
 'Unusually high frequency of trades in short time',
 '{"check_type": "trade_frequency", "max_trades_per_hour": 20, "time_window_minutes": 60}', 0.4, 0.05),

('value_imbalance', 'Value Imbalance',
 'Trades with significant value differences between parties',
 '{"check_type": "value_ratio", "max_ratio": 10.0, "min_trade_value": 1000}', 0.3, 0.25),

('item_laundering', 'Item Laundering',
 'Trading same item through multiple accounts',
 '{"check_type": "item_tracking", "max_hops": 3, "time_window_hours": 168}', 0.8, 0.02),

('bot_behavior', 'Bot-like Behavior',
 'Automated trading patterns without human variation',
 '{"check_type": "timing_analysis", "min_interval_variance": 0.1, "pattern_length": 10}', 0.7, 0.10),

('multi_account', 'Multi-Account Trading',
 'Coordinated trading between related accounts',
 '{"check_type": "account_relationships", "max_related_accounts": 3, "coordination_threshold": 0.8}', 0.9, 0.01),

('market_manipulation', 'Market Manipulation',
 'Artificial price inflation through coordinated trades',
 '{"check_type": "price_analysis", "volatility_threshold": 2.0, "volume_threshold": 1000}', 0.5, 0.30),

('trade_farming', 'Trade Farming',
 'Exploiting trade mechanics for illegitimate gains',
 '{"check_type": "pattern_recognition", "known_farming_patterns": ["circular_trades", "value_loop"]}', 0.6, 0.20);

-- =================================================================================================
-- DISPUTE REASON CATEGORIES
-- =================================================================================================

-- Create reference data for dispute categories
CREATE TABLE IF NOT EXISTS dispute_categories (
    id BIGSERIAL PRIMARY KEY,
    category_key VARCHAR(30) UNIQUE NOT NULL,
    category_name VARCHAR(100) NOT NULL,
    description TEXT,
    requires_evidence BOOLEAN NOT NULL DEFAULT true,
    auto_resolution_possible BOOLEAN NOT NULL DEFAULT false,
    priority_boost INTEGER NOT NULL DEFAULT 0, -- Priority increase for this category
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Dispute categories
INSERT INTO dispute_categories (
    category_key, category_name, description, requires_evidence, auto_resolution_possible, priority_boost
) VALUES
('item_not_received', 'Item Not Received',
 'Buyer did not receive the promised item after trade completion', true, true, 1),

('wrong_item', 'Wrong Item Received',
 'Received item does not match what was offered in trade', true, true, 1),

('currency_not_received', 'Currency Not Received',
 'Seller did not receive payment after trade completion', true, true, 2),

('fraud', 'Fraudulent Trade',
 'Trade was conducted under false pretenses or with malicious intent', true, false, 5),

('harassment', 'Harassment/Threats',
 'Trader used harassing language or threats during trade', true, false, 3),

('scamming', 'Scamming Attempt',
 'Attempted to scam or deceive trading partner', true, false, 4),

('system_error', 'System Error',
 'Trade failed due to technical issues or bugs', false, true, 0),

('account_compromise', 'Account Compromise',
 'Account was compromised and trades made without permission', true, false, 4),

('dispute_abuse', 'Dispute Abuse',
 'Dispute filed in bad faith or without valid reason', true, false, -1);

-- =================================================================================================
-- PERFORMANCE BASELINES
-- =================================================================================================

-- Create baseline performance metrics (will be updated by monitoring)
CREATE TABLE IF NOT EXISTS trade_performance_baselines (
    id BIGSERIAL PRIMARY KEY,
    metric_name VARCHAR(50) UNIQUE NOT NULL,
    baseline_value DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL, -- 'ms', 'percentage', 'count', etc.
    description TEXT,
    warning_threshold DECIMAL(10,2), -- When to alert
    critical_threshold DECIMAL(10,2), -- When to escalate
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_by VARCHAR(50)
);

-- Performance baselines
INSERT INTO trade_performance_baselines (
    metric_name, baseline_value, unit, description, warning_threshold, critical_threshold, updated_by
) VALUES
('trade_initiation_time', 150.0, 'ms', 'Average time to initiate trade session', 300.0, 1000.0, 'system'),

('offer_update_time', 100.0, 'ms', 'Average time to update trade offer', 250.0, 750.0, 'system'),

('trade_completion_time', 200.0, 'ms', 'Average time to complete trade', 500.0, 1500.0, 'system'),

('fraud_check_time', 50.0, 'ms', 'Average time for fraud detection', 150.0, 500.0, 'system'),

('success_rate', 98.5, 'percentage', 'Trade success rate', 95.0, 90.0, 'system'),

('dispute_rate', 1.5, 'percentage', 'Trades resulting in disputes', 3.0, 5.0, 'system'),

('concurrent_sessions', 100.0, 'count', 'Average concurrent trade sessions', 200.0, 500.0, 'system');

-- =================================================================================================
-- FUNCTIONS FOR INITIALIZATION
-- =================================================================================================

-- Function to initialize player rate limits
CREATE OR REPLACE FUNCTION initialize_player_rate_limits(player_id_param BIGINT)
RETURNS VOID AS $$
BEGIN
    INSERT INTO trade_rate_limits (
        player_id, hourly_limit, daily_limit, hour_start, day_start
    ) VALUES (
        player_id_param,
        10, -- Default hourly limit
        50, -- Default daily limit
        DATE_TRUNC('hour', NOW()),
        CURRENT_DATE
    )
    ON CONFLICT (player_id) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

-- Function to validate system configuration
CREATE OR REPLACE FUNCTION validate_trade_system_config()
RETURNS TABLE (
    config_issue VARCHAR(100),
    severity VARCHAR(10),
    description TEXT
) AS $$
BEGIN
    -- Check for missing critical configuration
    RETURN QUERY
    SELECT
        'missing_system_enabled'::VARCHAR(100),
        'CRITICAL'::VARCHAR(10),
        'System enabled flag not set'::TEXT
    WHERE NOT EXISTS (
        SELECT 1 FROM trade_system_config
        WHERE config_key = 'system_enabled' AND config_value::boolean = true
    );

    -- Check for invalid fraud thresholds
    RETURN QUERY
    SELECT
        'invalid_fraud_threshold'::VARCHAR(100),
        'WARNING'::VARCHAR(10),
        'Fraud score threshold configuration invalid'::TEXT
    WHERE EXISTS (
        SELECT 1 FROM trade_system_config
        WHERE config_key = 'fraud_score_threshold'
        AND NOT (config_value ? 'warning' AND config_value ? 'block')
    );

    -- Check for missing feature flags
    RETURN QUERY
    SELECT
        'missing_feature_flags'::VARCHAR(100),
        'INFO'::VARCHAR(10),
        'Feature flags not configured'::TEXT
    WHERE NOT EXISTS (
        SELECT 1 FROM trade_system_config
        WHERE config_key = 'feature_flags'
    );
END;
$$ LANGUAGE plpgsql;

-- =================================================================================================
-- INDEXES FOR NEW TABLES
-- =================================================================================================

-- Trade system config indexes
CREATE INDEX IF NOT EXISTS idx_trade_system_config_key ON trade_system_config(config_key) WHERE is_active = true;

-- Fraud pattern definitions indexes
CREATE INDEX IF NOT EXISTS idx_fraud_pattern_definitions_key ON fraud_pattern_definitions(pattern_key) WHERE is_active = true;
CREATE INDEX IF NOT EXISTS idx_fraud_pattern_definitions_severity ON fraud_pattern_definitions(severity_weight DESC);

-- Dispute categories indexes
CREATE INDEX IF NOT EXISTS idx_dispute_categories_key ON dispute_categories(category_key) WHERE is_active = true;

-- Performance baselines indexes
CREATE INDEX IF NOT EXISTS idx_trade_performance_baselines_name ON trade_performance_baselines(metric_name);

-- =================================================================================================
-- VALIDATION
-- =================================================================================================

-- Run validation to ensure proper setup
SELECT * FROM validate_trade_system_config();
