-- P2P Trade System Database Schema
-- Version: V001
-- Description: Complete database schema for P2P Trade System with sessions, history, security, and anti-fraud features

-- =================================================================================================
-- CORE TRADE TABLES
-- =================================================================================================

-- Trade sessions (active trading sessions between two players)
CREATE TABLE trade_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    initiator_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    zone_id VARCHAR(100),

    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    -- Statuses: pending, offering, confirmed, completed, cancelled, expired, disputed

    -- Trade offers (JSONB for flexible item/currency structure)
    initiator_offer JSONB NOT NULL DEFAULT '{}', -- {items: [{item_id, quantity, metadata}], currency: {eurodollars, cryptos}}
    target_offer JSONB NOT NULL DEFAULT '{}',

    -- Confirmation states
    initiator_confirmed BOOLEAN NOT NULL DEFAULT false,
    target_confirmed BOOLEAN NOT NULL DEFAULT false,

    -- Location and distance tracking
    initiator_position JSONB, -- {x, y, z} coordinates
    target_position JSONB,
    max_distance_allowed DECIMAL(6,2) NOT NULL DEFAULT 10.0, -- meters

    -- Timing
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    cancelled_at TIMESTAMP WITH TIME ZONE,

    -- Cancellation metadata
    cancelled_by BIGINT,
    cancel_reason VARCHAR(200),

    -- Security and fraud prevention
    initiator_ip VARCHAR(45), -- IPv6 support
    target_ip VARCHAR(45),
    initiator_device_fingerprint VARCHAR(255),
    target_device_fingerprint VARCHAR(255),

    -- Fraud detection flags
    fraud_score DECIMAL(3,2) DEFAULT 0.0, -- 0.0 to 1.0
    suspicious_flags JSONB NOT NULL DEFAULT '[]', -- Array of detected issues
    security_checks_passed JSONB NOT NULL DEFAULT '{}',

    -- Metadata
    trade_value_estimate BIGINT DEFAULT 0, -- Estimated trade value in eurodollars
    session_metadata JSONB NOT NULL DEFAULT '{}', -- Additional session data

    CONSTRAINT valid_status CHECK (status IN (
        'pending', 'offering', 'confirmed', 'completed', 'cancelled', 'expired', 'disputed'
    )),
    CONSTRAINT no_self_trade CHECK (initiator_id != target_id),
    CONSTRAINT valid_fraud_score CHECK (fraud_score >= 0.0 AND fraud_score <= 1.0),
    CONSTRAINT valid_distance CHECK (max_distance_allowed > 0 AND max_distance_allowed <= 100)
);

-- Trade history (completed trades audit trail)
CREATE TABLE trade_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id UUID NOT NULL REFERENCES trade_sessions(id),

    -- Participants
    player1_id BIGINT NOT NULL,
    player2_id BIGINT NOT NULL,

    -- What was traded (detailed snapshot)
    player1_items JSONB NOT NULL DEFAULT '[]', -- [{item_id, quantity, name, rarity, value}]
    player1_currency JSONB NOT NULL DEFAULT '{}', -- {eurodollars, cryptos, other_currencies}

    player2_items JSONB NOT NULL DEFAULT '[]',
    player2_currency JSONB NOT NULL DEFAULT '{}',

    -- Context
    zone_id VARCHAR(100),
    completed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Security audit data
    player1_ip VARCHAR(45),
    player2_ip VARCHAR(45),
    player1_device_fingerprint VARCHAR(255),
    player2_device_fingerprint VARCHAR(255),

    -- Fraud detection and analysis
    suspicious_flag BOOLEAN NOT NULL DEFAULT false,
    suspicious_reasons JSONB NOT NULL DEFAULT '[]', -- Array of reasons why flagged
    fraud_score DECIMAL(3,2) DEFAULT 0.0,
    investigation_status VARCHAR(20) DEFAULT 'none', -- none, pending, investigating, resolved, banned

    -- Economic analysis
    total_value BIGINT DEFAULT 0, -- Total trade value
    value_imbalance DECIMAL(5,2) DEFAULT 0.0, -- Percentage difference in trade values
    trade_type VARCHAR(30) DEFAULT 'balanced', -- balanced, donation, barter, sale

    -- Metadata
    trade_metadata JSONB NOT NULL DEFAULT '{}', -- Additional trade data for analytics

    CONSTRAINT valid_investigation_status CHECK (investigation_status IN (
        'none', 'pending', 'investigating', 'resolved', 'banned'
    )),
    CONSTRAINT valid_trade_type CHECK (trade_type IN (
        'balanced', 'donation', 'barter', 'sale', 'purchase'
    )),
    CONSTRAINT unique_session_history UNIQUE (session_id)
);

-- Trade events (complete audit trail for all trade actions)
CREATE TABLE trade_events (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID NOT NULL REFERENCES trade_sessions(id),

    event_type VARCHAR(50) NOT NULL,
    -- Types: session_created, offer_added, offer_updated, offer_removed,
    --        confirmed, unconfirmed, completed, cancelled, expired,
    --        fraud_detected, dispute_raised, item_locked, item_unlocked

    actor_id BIGINT NOT NULL, -- Who performed the action
    event_data JSONB NOT NULL DEFAULT '{}', -- Detailed event information

    -- Security tracking
    actor_ip VARCHAR(45),
    actor_device_fingerprint VARCHAR(255),

    -- Timing and sequencing
    event_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    event_sequence INTEGER NOT NULL, -- Sequential number within session

    -- Metadata
    event_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_event_type CHECK (event_type IN (
        'session_created', 'offer_added', 'offer_updated', 'offer_removed',
        'confirmed', 'unconfirmed', 'completed', 'cancelled', 'expired',
        'fraud_detected', 'dispute_raised', 'item_locked', 'item_unlocked',
        'distance_check', 'ownership_check', 'security_validation'
    ))
);

-- =================================================================================================
-- ITEM LOCKING AND ESCROW SYSTEM
-- =================================================================================================

-- Locked items during active trades (prevents double-trading)
CREATE TABLE locked_items (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID NOT NULL REFERENCES trade_sessions(id),
    player_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,

    quantity INTEGER NOT NULL DEFAULT 1,
    locked_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    expected_unlock_at TIMESTAMP WITH TIME ZONE,

    -- Item validation data (to detect changes during lock)
    item_checksum VARCHAR(255), -- Hash of item state
    item_value BIGINT, -- Cached item value
    item_metadata JSONB NOT NULL DEFAULT '{}',

    -- Lock status
    lock_status VARCHAR(20) NOT NULL DEFAULT 'active', -- active, released, expired, disputed
    unlock_reason VARCHAR(100),

    CONSTRAINT valid_lock_status CHECK (lock_status IN (
        'active', 'released', 'expired', 'disputed', 'forfeited'
    )),
    UNIQUE(session_id, player_id, item_id)
);

-- Escrow accounts for secure currency transfers
CREATE TABLE trade_escrow_accounts (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID NOT NULL REFERENCES trade_sessions(id),

    player_id BIGINT NOT NULL,
    currency_type VARCHAR(20) NOT NULL, -- eurodollars, cryptos, etc.
    amount BIGINT NOT NULL,

    escrowed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    released_at TIMESTAMP WITH TIME ZONE,
    forfeited_at TIMESTAMP WITH TIME ZONE,

    -- Status tracking
    escrow_status VARCHAR(20) NOT NULL DEFAULT 'held', -- held, released, forfeited, disputed
    release_reason VARCHAR(100),

    -- Security
    escrow_checksum VARCHAR(255), -- For integrity verification

    CONSTRAINT valid_escrow_status CHECK (escrow_status IN (
        'held', 'released', 'forfeited', 'disputed'
    )),
    CONSTRAINT positive_amount CHECK (amount > 0),
    UNIQUE(session_id, player_id, currency_type)
);

-- =================================================================================================
-- ANTI-FRAUD AND SECURITY TABLES
-- =================================================================================================

-- Suspicious trade patterns for fraud detection
CREATE TABLE trade_suspicious_patterns (
    id BIGSERIAL PRIMARY KEY,
    pattern_type VARCHAR(50) NOT NULL,
    -- Types: ip_sharing, rapid_trading, value_imbalance, item_laundering, bot_behavior

    player_id BIGINT NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- Pattern details
    pattern_data JSONB NOT NULL DEFAULT '{}',
    severity_score DECIMAL(3,2) NOT NULL, -- 0.0 to 1.0

    -- Associated trades
    related_sessions UUID[] NOT NULL DEFAULT '{}', -- Array of session IDs

    -- Resolution
    status VARCHAR(20) NOT NULL DEFAULT 'detected', -- detected, investigating, resolved, false_positive
    resolved_at TIMESTAMP WITH TIME ZONE,
    resolution_notes TEXT,

    -- Automated actions taken
    actions_taken JSONB NOT NULL DEFAULT '[]', -- Array of actions (warning, rate_limit, ban, etc.)

    CONSTRAINT valid_pattern_type CHECK (pattern_type IN (
        'ip_sharing', 'rapid_trading', 'value_imbalance', 'item_laundering',
        'bot_behavior', 'multi_account', 'market_manipulation', 'trade_farming'
    )),
    CONSTRAINT valid_status CHECK (status IN (
        'detected', 'investigating', 'resolved', 'false_positive'
    )),
    CONSTRAINT valid_severity CHECK (severity_score >= 0.0 AND severity_score <= 1.0)
);

-- Trade rate limiting
CREATE TABLE trade_rate_limits (
    id BIGSERIAL PRIMARY KEY,
    player_id BIGINT NOT NULL,

    -- Rate limit windows
    hourly_limit INTEGER NOT NULL DEFAULT 10,
    daily_limit INTEGER NOT NULL DEFAULT 50,
    trades_this_hour INTEGER NOT NULL DEFAULT 0,
    trades_today INTEGER NOT NULL DEFAULT 0,

    -- Time windows
    hour_start TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT DATE_TRUNC('hour', NOW()),
    day_start DATE NOT NULL DEFAULT CURRENT_DATE,

    -- Current status
    is_rate_limited BOOLEAN NOT NULL DEFAULT false,
    rate_limit_until TIMESTAMP WITH TIME ZONE,

    -- Metadata
    last_trade_at TIMESTAMP WITH TIME ZONE,
    rate_limit_reason VARCHAR(100),

    UNIQUE(player_id)
);

-- Trade disputes and arbitration
CREATE TABLE trade_disputes (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID REFERENCES trade_sessions(id), -- May be null for post-trade disputes
    history_id UUID REFERENCES trade_history(id), -- For completed trade disputes

    -- Involved parties
    complainant_id BIGINT NOT NULL,
    respondent_id BIGINT NOT NULL,

    -- Dispute details
    dispute_type VARCHAR(30) NOT NULL,
    -- Types: item_not_received, wrong_item, currency_not_received, fraud, harassment

    dispute_reason TEXT NOT NULL,
    evidence_provided JSONB NOT NULL DEFAULT '[]', -- Array of evidence links/IDs

    -- Resolution process
    status VARCHAR(20) NOT NULL DEFAULT 'open', -- open, investigating, resolved, escalated
    priority VARCHAR(10) NOT NULL DEFAULT 'normal', -- low, normal, high, critical

    -- Resolution
    resolved_by BIGINT, -- Admin/moderator who resolved
    resolution TEXT,
    resolution_actions JSONB NOT NULL DEFAULT '[]', -- Actions taken (refund, ban, warning)

    -- Timing
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    resolved_at TIMESTAMP WITH TIME ZONE,
    escalated_at TIMESTAMP WITH TIME ZONE,

    -- Metadata
    dispute_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_dispute_type CHECK (dispute_type IN (
        'item_not_received', 'wrong_item', 'currency_not_received',
        'fraud', 'harassment', 'scamming', 'system_error'
    )),
    CONSTRAINT valid_status CHECK (status IN (
        'open', 'investigating', 'resolved', 'escalated', 'appealed'
    )),
    CONSTRAINT valid_priority CHECK (priority IN (
        'low', 'normal', 'high', 'critical'
    )),
    CONSTRAINT dispute_target CHECK (
        (session_id IS NOT NULL AND history_id IS NULL) OR
        (session_id IS NULL AND history_id IS NOT NULL)
    )
);

-- =================================================================================================
-- ANALYTICS AND MONITORING TABLES
-- =================================================================================================

-- Trade analytics (aggregated data for dashboards)
CREATE TABLE trade_analytics (
    id BIGSERIAL PRIMARY KEY,
    date DATE NOT NULL,

    -- Volume metrics
    total_trades BIGINT NOT NULL DEFAULT 0,
    total_trade_value BIGINT NOT NULL DEFAULT 0,
    total_items_traded BIGINT NOT NULL DEFAULT 0,
    total_currency_traded BIGINT NOT NULL DEFAULT 0,

    -- Success metrics
    completed_trades BIGINT NOT NULL DEFAULT 0,
    cancelled_trades BIGINT NOT NULL DEFAULT 0,
    expired_trades BIGINT NOT NULL DEFAULT 0,
    disputed_trades BIGINT NOT NULL DEFAULT 0,

    -- Fraud metrics
    suspicious_trades BIGINT NOT NULL DEFAULT 0,
    fraud_score_avg DECIMAL(5,4) DEFAULT 0,

    -- Performance metrics
    avg_trade_duration INTERVAL,
    avg_items_per_trade DECIMAL(5,2) DEFAULT 0,
    avg_value_per_trade DECIMAL(10,2) DEFAULT 0,

    -- Regional data
    zone_trade_counts JSONB NOT NULL DEFAULT '{}', -- {"zone_id": count}

    -- Metadata
    analytics_metadata JSONB NOT NULL DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(date)
);

-- Trade performance monitoring
CREATE TABLE trade_performance_metrics (
    id BIGSERIAL PRIMARY KEY,
    metric_timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    -- API performance
    operation_type VARCHAR(30) NOT NULL, -- initiate, offer, confirm, complete, cancel
    operation_duration_ms INTEGER NOT NULL,
    operation_success BOOLEAN NOT NULL,

    -- Involved players/systems
    player_id BIGINT,
    session_id UUID,
    service_name VARCHAR(50), -- economy-service, inventory-service, etc.

    -- Additional context
    operation_metadata JSONB NOT NULL DEFAULT '{}',

    CONSTRAINT valid_operation CHECK (operation_type IN (
        'initiate', 'offer_add', 'offer_update', 'offer_remove',
        'confirm', 'unconfirm', 'complete', 'cancel', 'expire'
    ))
);

-- =================================================================================================
-- CONFIGURATION AND RULES TABLES
-- =================================================================================================

-- Trade configuration rules
CREATE TABLE trade_config_rules (
    id BIGSERIAL PRIMARY KEY,
    rule_key VARCHAR(100) UNIQUE NOT NULL,
    rule_type VARCHAR(30) NOT NULL,
    -- Types: distance_limit, timeout, rate_limit, value_limit, item_restrictions

    rule_config JSONB NOT NULL DEFAULT '{}',
    is_active BOOLEAN NOT NULL DEFAULT true,

    -- Applicability
    player_level_min INTEGER,
    player_level_max INTEGER,
    zone_restrictions JSONB NOT NULL DEFAULT '[]', -- Array of restricted zones

    -- Timing
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    activated_at TIMESTAMP WITH TIME ZONE,
    deactivated_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT valid_rule_type CHECK (rule_type IN (
        'distance_limit', 'timeout', 'rate_limit', 'value_limit',
        'item_restrictions', 'currency_limits', 'security_checks'
    ))
);

-- Trade item restrictions (what can't be traded)
CREATE TABLE trade_item_restrictions (
    id BIGSERIAL PRIMARY KEY,
    item_type VARCHAR(50) NOT NULL, -- weapon, armor, consumable, etc.
    item_subtype VARCHAR(50), -- assault_rifle, heavy_armor, etc.
    item_id BIGINT, -- Specific item ID, null for type-wide restrictions

    restriction_reason VARCHAR(100) NOT NULL,
    -- Reasons: bind_on_pickup, quest_item, unique_item, faction_locked, level_locked

    restriction_config JSONB NOT NULL DEFAULT '{}', -- Additional restriction data
    is_active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),

    UNIQUE(item_type, item_subtype, item_id)
);

-- =================================================================================================
-- INDEXES
-- =================================================================================================

-- Trade sessions indexes
CREATE INDEX idx_trade_sessions_initiator_status ON trade_sessions(initiator_id, status, created_at DESC);
CREATE INDEX idx_trade_sessions_target_status ON trade_sessions(target_id, status, created_at DESC);
CREATE INDEX idx_trade_sessions_status_created ON trade_sessions(status, created_at DESC);
CREATE INDEX idx_trade_sessions_expires ON trade_sessions(expires_at) WHERE status NOT IN ('completed', 'cancelled', 'expired');
CREATE INDEX idx_trade_sessions_fraud_score ON trade_sessions(fraud_score DESC) WHERE fraud_score > 0.5;
CREATE INDEX idx_trade_sessions_zone ON trade_sessions(zone_id, created_at DESC);

-- Trade history indexes
CREATE INDEX idx_trade_history_player1_completed ON trade_history(player1_id, completed_at DESC);
CREATE INDEX idx_trade_history_player2_completed ON trade_history(player2_id, completed_at DESC);
CREATE INDEX idx_trade_history_completed_at ON trade_history(completed_at DESC);
CREATE INDEX idx_trade_history_suspicious ON trade_history(suspicious_flag, completed_at DESC) WHERE suspicious_flag = true;
CREATE INDEX idx_trade_history_value ON trade_history(total_value DESC, completed_at DESC);
CREATE INDEX idx_trade_history_zone ON trade_history(zone_id, completed_at DESC);

-- Trade events indexes
CREATE INDEX idx_trade_events_session_timestamp ON trade_events(session_id, event_timestamp);
CREATE INDEX idx_trade_events_type_timestamp ON trade_events(event_type, event_timestamp DESC);
CREATE INDEX idx_trade_events_actor ON trade_events(actor_id, event_timestamp DESC);

-- Locked items indexes
CREATE INDEX idx_locked_items_session_player ON locked_items(session_id, player_id);
CREATE INDEX idx_locked_items_player_item ON locked_items(player_id, item_id);
CREATE INDEX idx_locked_items_status ON locked_items(lock_status, locked_at DESC);

-- Escrow accounts indexes
CREATE INDEX idx_trade_escrow_session_player ON trade_escrow_accounts(session_id, player_id);
CREATE INDEX idx_trade_escrow_status ON trade_escrow_accounts(escrow_status, escrowed_at DESC);

-- Suspicious patterns indexes
CREATE INDEX idx_trade_suspicious_patterns_player ON trade_suspicious_patterns(player_id, detected_at DESC);
CREATE INDEX idx_trade_suspicious_patterns_type ON trade_suspicious_patterns(pattern_type, severity_score DESC);
CREATE INDEX idx_trade_suspicious_patterns_status ON trade_suspicious_patterns(status, detected_at DESC);

-- Rate limits indexes
CREATE INDEX idx_trade_rate_limits_player ON trade_rate_limits(player_id);
CREATE INDEX idx_trade_rate_limits_limited ON trade_rate_limits(is_rate_limited, rate_limit_until);

-- Disputes indexes
CREATE INDEX idx_trade_disputes_status ON trade_disputes(status, created_at DESC);
CREATE INDEX idx_trade_disputes_complainant ON trade_disputes(complainant_id, created_at DESC);
CREATE INDEX idx_trade_disputes_respondent ON trade_disputes(respondent_id, created_at DESC);
CREATE INDEX idx_trade_disputes_priority ON trade_disputes(priority, created_at DESC) WHERE status = 'open';

-- Analytics indexes
CREATE INDEX idx_trade_analytics_date ON trade_analytics(date DESC);
CREATE INDEX idx_trade_analytics_value ON trade_analytics(total_trade_value DESC);

-- Performance metrics indexes
CREATE INDEX idx_trade_performance_metrics_timestamp ON trade_performance_metrics(metric_timestamp DESC);
CREATE INDEX idx_trade_performance_metrics_operation ON trade_performance_metrics(operation_type, operation_duration_ms DESC);

-- =================================================================================================
-- CONSTRAINTS AND TRIGGERS
-- =================================================================================================

-- Additional check constraints
ALTER TABLE trade_sessions ADD CONSTRAINT expires_after_created CHECK (expires_at > created_at);
ALTER TABLE trade_sessions ADD CONSTRAINT confirmed_before_completed CHECK (
    (completed_at IS NULL) OR (confirmed_at IS NOT NULL AND confirmed_at <= completed_at)
);

-- Function to update trade value estimates
CREATE OR REPLACE FUNCTION update_trade_value_estimate()
RETURNS TRIGGER AS $$
DECLARE
    item_value BIGINT;
    currency_value BIGINT;
    total_value BIGINT := 0;
BEGIN
    -- Calculate from initiator offer
    IF NEW.initiator_offer IS NOT NULL AND NEW.initiator_offer != '{}'::jsonb THEN
        -- Sum item values (simplified - would need actual item pricing logic)
        SELECT COALESCE(SUM((elem->>'quantity')::int * (elem->>'estimated_value')::int), 0)
        INTO item_value
        FROM jsonb_array_elements(NEW.initiator_offer->'items') elem;

        -- Sum currency values
        currency_value := COALESCE((NEW.initiator_offer->>'eurodollars')::bigint, 0);

        total_value := total_value + item_value + currency_value;
    END IF;

    -- Add target offer value similarly
    IF NEW.target_offer IS NOT NULL AND NEW.target_offer != '{}'::jsonb THEN
        SELECT COALESCE(SUM((elem->>'quantity')::int * (elem->>'estimated_value')::int), 0)
        INTO item_value
        FROM jsonb_array_elements(NEW.target_offer->'items') elem;

        currency_value := COALESCE((NEW.target_offer->>'eurodollars')::bigint, 0);

        total_value := total_value + item_value + currency_value;
    END IF;

    NEW.trade_value_estimate := total_value;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to update trade value on offer changes
CREATE TRIGGER update_trade_value_trigger
    BEFORE INSERT OR UPDATE OF initiator_offer, target_offer
    ON trade_sessions
    FOR EACH ROW
    EXECUTE FUNCTION update_trade_value_estimate();

-- Function to validate trade session state transitions
CREATE OR REPLACE FUNCTION validate_trade_session_transition()
RETURNS TRIGGER AS $$
BEGIN
    -- Prevent invalid status transitions
    IF OLD.status = 'completed' AND NEW.status != 'completed' THEN
        RAISE EXCEPTION 'Cannot change status from completed';
    END IF;

    IF OLD.status = 'cancelled' AND NEW.status != 'cancelled' THEN
        RAISE EXCEPTION 'Cannot change status from cancelled';
    END IF;

    IF OLD.status = 'expired' AND NEW.status != 'expired' THEN
        RAISE EXCEPTION 'Cannot change status from expired';
    END IF;

    -- Set timestamps based on status changes
    IF NEW.status = 'confirmed' AND OLD.status != 'confirmed' THEN
        NEW.confirmed_at := NOW();
    END IF;

    IF NEW.status = 'completed' AND OLD.status != 'completed' THEN
        NEW.completed_at := NOW();
    END IF;

    IF NEW.status = 'cancelled' AND OLD.status != 'cancelled' THEN
        NEW.cancelled_at := NOW();
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger for trade session state validation
CREATE TRIGGER validate_trade_session_transition_trigger
    BEFORE UPDATE ON trade_sessions
    FOR EACH ROW
    EXECUTE FUNCTION validate_trade_session_transition();
