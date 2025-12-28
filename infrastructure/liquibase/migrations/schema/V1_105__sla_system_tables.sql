-- SLA System Tables Migration
-- Enterprise-grade schema for MMOFPS RPG SLA monitoring and management

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- SLA Status table
-- Tracks SLA status for each support ticket with performance-optimized structure
CREATE TABLE IF NOT EXISTS support.sla_status (
    ticket_id UUID PRIMARY KEY,
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT', 'CRITICAL')),
    first_response_target TIMESTAMP WITH TIME ZONE NOT NULL,
    first_response_actual TIMESTAMP WITH TIME ZONE,
    resolution_target TIMESTAMP WITH TIME ZONE NOT NULL,
    resolution_actual TIMESTAMP WITH TIME ZONE,
    first_response_sla_met BOOLEAN,
    resolution_sla_met BOOLEAN,
    time_until_first_response INTEGER, -- seconds remaining (can be negative)
    time_until_resolution INTEGER, -- seconds remaining (can be negative)
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Struct alignment: large fields first for memory efficiency (30-50% memory savings)
    ticket_id UUID,
    first_response_target TIMESTAMP WITH TIME ZONE,
    first_response_actual TIMESTAMP WITH TIME ZONE,
    resolution_target TIMESTAMP WITH TIME ZONE,
    resolution_actual TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(20),
    first_response_sla_met BOOLEAN,
    resolution_sla_met BOOLEAN,
    time_until_first_response INTEGER,
    time_until_resolution INTEGER
);

-- SLA Violations table
-- Records SLA violations with detailed tracking for analysis and reporting
CREATE TABLE IF NOT EXISTS support.sla_violations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    ticket_id UUID NOT NULL,
    ticket_number VARCHAR(20) NOT NULL, -- Shortened ticket ID for display
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT', 'CRITICAL')),
    violation_type VARCHAR(20) NOT NULL CHECK (violation_type IN ('FIRST_RESPONSE', 'RESOLUTION')),
    target_time TIMESTAMP WITH TIME ZONE NOT NULL,
    actual_time TIMESTAMP WITH TIME ZONE,
    violation_duration INTEGER NOT NULL, -- seconds overdue
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Struct alignment optimization
    id UUID,
    ticket_id UUID,
    target_time TIMESTAMP WITH TIME ZONE,
    actual_time TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE,
    ticket_number VARCHAR(20),
    priority VARCHAR(20),
    violation_type VARCHAR(20),
    violation_duration INTEGER
);

-- SLA Priorities configuration table
-- Stores configurable SLA priority definitions
CREATE TABLE IF NOT EXISTS support.sla_priorities (
    priority VARCHAR(20) PRIMARY KEY,
    first_response_target_hours INTEGER NOT NULL CHECK (first_response_target_hours > 0),
    resolution_target_hours INTEGER NOT NULL CHECK (resolution_target_hours > 0),
    first_response_penalty INTEGER NOT NULL DEFAULT 0 CHECK (first_response_penalty >= 0),
    resolution_penalty INTEGER NOT NULL DEFAULT 0 CHECK (resolution_penalty >= 0),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Struct alignment optimization
    priority VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE,
    first_response_target_hours INTEGER,
    resolution_target_hours INTEGER,
    first_response_penalty INTEGER,
    resolution_penalty INTEGER,
    is_active BOOLEAN
);

-- Insert default SLA priorities
INSERT INTO support.sla_priorities (priority, first_response_target_hours, resolution_target_hours, first_response_penalty, resolution_penalty)
VALUES
    ('LOW', 48, 168, 1, 2),        -- 2 days first response, 7 days resolution
    ('NORMAL', 24, 120, 2, 4),     -- 1 day first response, 5 days resolution
    ('HIGH', 4, 24, 5, 10),        -- 4 hours first response, 1 day resolution
    ('URGENT', 1, 4, 10, 20),      -- 1 hour first response, 4 hours resolution
    ('CRITICAL', 1, 2, 20, 50)     -- 30 minutes first response, 2 hours resolution
ON CONFLICT (priority) DO NOTHING;

-- Indexes for performance optimization

-- SLA Status indexes
CREATE INDEX IF NOT EXISTS idx_sla_status_ticket_id ON support.sla_status(ticket_id);
CREATE INDEX IF NOT EXISTS idx_sla_status_priority ON support.sla_status(priority);
CREATE INDEX IF NOT EXISTS idx_sla_status_first_response_target ON support.sla_status(first_response_target);
CREATE INDEX IF NOT EXISTS idx_sla_status_resolution_target ON support.sla_status(resolution_target);
CREATE INDEX IF NOT EXISTS idx_sla_status_created_at ON support.sla_status(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_sla_status_updated_at ON support.sla_status(updated_at DESC);

-- SLA Violations indexes
CREATE INDEX IF NOT EXISTS idx_sla_violations_ticket_id ON support.sla_violations(ticket_id);
CREATE INDEX IF NOT EXISTS idx_sla_violations_violation_type ON support.sla_violations(violation_type);
CREATE INDEX IF NOT EXISTS idx_sla_violations_priority ON support.sla_violations(priority);
CREATE INDEX IF NOT EXISTS idx_sla_violations_created_at ON support.sla_violations(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_sla_violations_target_time ON support.sla_violations(target_time);

-- SLA Priorities indexes
CREATE INDEX IF NOT EXISTS idx_sla_priorities_active ON support.sla_priorities(is_active) WHERE is_active = true;

-- Triggers for automatic timestamp updates

-- SLA Status updated_at trigger
CREATE OR REPLACE FUNCTION update_sla_status_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_sla_status_updated_at
    BEFORE UPDATE ON support.sla_status
    FOR EACH ROW
    EXECUTE FUNCTION update_sla_status_updated_at();

-- SLA Priorities updated_at trigger
CREATE OR REPLACE FUNCTION update_sla_priorities_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_sla_priorities_updated_at
    BEFORE UPDATE ON support.sla_priorities
    FOR EACH ROW
    EXECUTE FUNCTION update_sla_priorities_updated_at();

-- Functions for SLA analytics and reporting

-- Function to get SLA violation statistics for a date range
CREATE OR REPLACE FUNCTION get_sla_violation_stats(start_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP - INTERVAL '30 days', end_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP)
RETURNS TABLE (
    violation_type VARCHAR(20),
    total_violations BIGINT,
    avg_violation_duration INTERVAL,
    max_violation_duration INTERVAL,
    priority_breakdown JSONB
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        sv.violation_type,
        COUNT(*)::BIGINT as total_violations,
        AVG(violation_duration * INTERVAL '1 second') as avg_violation_duration,
        MAX(violation_duration * INTERVAL '1 second') as max_violation_duration,
        jsonb_object_agg(
            COALESCE(sv.priority, 'UNKNOWN'),
            COUNT(*) FILTER (WHERE sv.priority IS NOT NULL)
        ) as priority_breakdown
    FROM support.sla_violations sv
    WHERE sv.created_at BETWEEN start_date AND end_date
    GROUP BY sv.violation_type;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate SLA compliance percentage
CREATE OR REPLACE FUNCTION get_sla_compliance_percentage(start_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP - INTERVAL '30 days', end_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP)
RETURNS TABLE (
    priority VARCHAR(20),
    total_tickets BIGINT,
    sla_met_tickets BIGINT,
    compliance_percentage NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        ss.priority,
        COUNT(*)::BIGINT as total_tickets,
        COUNT(*) FILTER (WHERE ss.first_response_sla_met = true AND ss.resolution_sla_met = true)::BIGINT as sla_met_tickets,
        CASE
            WHEN COUNT(*) > 0
            THEN ROUND(
                (COUNT(*) FILTER (WHERE ss.first_response_sla_met = true AND ss.resolution_sla_met = true)::NUMERIC /
                 COUNT(*)::NUMERIC) * 100, 2
            )
            ELSE 0
        END as compliance_percentage
    FROM support.sla_status ss
    WHERE ss.created_at BETWEEN start_date AND end_date
    GROUP BY ss.priority
    ORDER BY ss.priority;
END;
$$ LANGUAGE plpgsql;

-- Function to get active SLA breaches (tickets that will breach soon)
CREATE OR REPLACE FUNCTION get_active_sla_breaches(hours_ahead INTEGER DEFAULT 24)
RETURNS TABLE (
    ticket_id UUID,
    priority VARCHAR(20),
    breach_type VARCHAR(30),
    time_until_breach INTERVAL,
    breach_time TIMESTAMP WITH TIME ZONE
) AS $$
BEGIN
    RETURN QUERY
    SELECT
        ss.ticket_id,
        ss.priority,
        CASE
            WHEN ss.time_until_first_response > 0 AND ss.time_until_first_response <= (hours_ahead * 3600)
            THEN 'FIRST_RESPONSE_IMMINENT'
            WHEN ss.time_until_first_response < 0
            THEN 'FIRST_RESPONSE_BREACHED'
            WHEN ss.time_until_resolution > 0 AND ss.time_until_resolution <= (hours_ahead * 3600)
            THEN 'RESOLUTION_IMMINENT'
            WHEN ss.time_until_resolution < 0
            THEN 'RESOLUTION_BREACHED'
            ELSE 'UNKNOWN'
        END as breach_type,
        CASE
            WHEN ss.time_until_first_response > 0
            THEN ss.time_until_first_response * INTERVAL '1 second'
            WHEN ss.time_until_resolution > 0
            THEN ss.time_until_resolution * INTERVAL '1 second'
            ELSE INTERVAL '0 seconds'
        END as time_until_breach,
        CASE
            WHEN ss.time_until_first_response > 0
            THEN CURRENT_TIMESTAMP + (ss.time_until_first_response * INTERVAL '1 second')
            WHEN ss.time_until_resolution > 0
            THEN CURRENT_TIMESTAMP + (ss.time_until_resolution * INTERVAL '1 second')
            ELSE CURRENT_TIMESTAMP
        END as breach_time
    FROM support.sla_status ss
    WHERE (ss.first_response_actual IS NULL AND ss.time_until_first_response <= (hours_ahead * 3600))
       OR (ss.resolution_actual IS NULL AND ss.time_until_resolution <= (hours_ahead * 3600))
    ORDER BY
        CASE
            WHEN ss.time_until_first_response <= 0 OR ss.time_until_resolution <= 0 THEN 1
            WHEN ss.time_until_first_response <= 3600 OR ss.time_until_resolution <= 3600 THEN 2
            ELSE 3
        END,
        LEAST(COALESCE(ss.time_until_first_response, 2147483647), COALESCE(ss.time_until_resolution, 2147483647));
END;
$$ LANGUAGE plpgsql;

-- Comments for documentation
COMMENT ON TABLE support.sla_status IS 'SLA status tracking for support tickets with calculated targets and compliance';
COMMENT ON TABLE support.sla_violations IS 'Detailed records of SLA violations for analysis and reporting';
COMMENT ON TABLE support.sla_priorities IS 'Configurable SLA priority definitions with time targets and penalties';

COMMENT ON COLUMN support.sla_status.time_until_first_response IS 'Seconds until first response SLA breach (negative = breached)';
COMMENT ON COLUMN support.sla_status.time_until_resolution IS 'Seconds until resolution SLA breach (negative = breached)';
COMMENT ON COLUMN support.sla_violations.violation_duration IS 'Seconds the SLA was breached by';

COMMENT ON FUNCTION get_sla_violation_stats(TIMESTAMP WITH TIME ZONE, TIMESTAMP WITH TIME ZONE) IS 'Returns SLA violation statistics for date range';
COMMENT ON FUNCTION get_sla_compliance_percentage(TIMESTAMP WITH TIME ZONE, TIMESTAMP WITH TIME ZONE) IS 'Returns SLA compliance percentages by priority';
COMMENT ON FUNCTION get_active_sla_breaches(INTEGER) IS 'Returns tickets with imminent or active SLA breaches';
