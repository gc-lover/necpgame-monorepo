-- Issue: #2215
-- liquibase formatted sql

--changeset author:necpgame dbms:postgresql
--comment: Create event store schema for microservice orchestration with CQRS and event sourcing

BEGIN;

-- Create event store schema for microservices
CREATE SCHEMA IF NOT EXISTS event_store;

-- Table: event_store.events
-- Core event store for all domain events in the system
CREATE TABLE IF NOT EXISTS event_store.events
(
    -- Primary identifier for the event
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Aggregate information for event sourcing
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL, -- e.g., 'player', 'quest', 'combat_session'
    aggregate_version BIGINT NOT NULL,

    -- Event metadata
    event_type VARCHAR(200) NOT NULL, -- e.g., 'PlayerCreated', 'QuestCompleted'
    event_version INTEGER NOT NULL DEFAULT 1,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Event payload (JSON)
    payload JSONB NOT NULL,

    -- Metadata for event processing
    metadata JSONB DEFAULT '{}',

    -- Causation and correlation IDs for distributed tracing
    causation_id UUID,
    correlation_id UUID,

    -- Processing status
    processed_at TIMESTAMP WITH TIME ZONE,
    processing_status VARCHAR(20) DEFAULT 'pending' CHECK (processing_status IN ('pending', 'processing', 'processed', 'failed')),
    processing_error TEXT,

    -- Audit fields
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance (optimized for event sourcing queries)
CREATE INDEX IF NOT EXISTS idx_events_aggregate ON event_store.events(aggregate_id, aggregate_version);
CREATE INDEX IF NOT EXISTS idx_events_type ON event_store.events(event_type);
CREATE INDEX IF NOT EXISTS idx_events_occurred_at ON event_store.events(occurred_at DESC);
CREATE INDEX IF NOT EXISTS idx_events_correlation ON event_store.events(correlation_id);
CREATE INDEX IF NOT EXISTS idx_events_processing_status ON event_store.events(processing_status);

-- Partial index for unprocessed events (most common query)
CREATE INDEX IF NOT EXISTS idx_events_pending ON event_store.events(occurred_at)
    WHERE processing_status = 'pending';

-- GIN indexes for JSON payload searches
CREATE INDEX IF NOT EXISTS idx_events_payload_gin ON event_store.events USING GIN (payload);
CREATE INDEX IF NOT EXISTS idx_events_metadata_gin ON event_store.events USING GIN (metadata);

-- Table: event_store.snapshots
-- Snapshots for performance optimization in event sourcing
CREATE TABLE IF NOT EXISTS event_store.snapshots
(
    snapshot_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_version BIGINT NOT NULL,
    snapshot_data JSONB NOT NULL,
    taken_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Uniqueness constraint: only one snapshot per aggregate version
    UNIQUE(aggregate_id, aggregate_version)
);

-- Indexes for snapshots
CREATE INDEX IF NOT EXISTS idx_snapshots_aggregate ON event_store.snapshots(aggregate_id, aggregate_version DESC);
CREATE INDEX IF NOT EXISTS idx_snapshots_taken_at ON event_store.snapshots(taken_at DESC);

-- Table: event_store.sagas
-- Saga instances for complex business processes spanning multiple aggregates
CREATE TABLE IF NOT EXISTS event_store.sagas
(
    saga_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    saga_type VARCHAR(100) NOT NULL, -- e.g., 'PlayerRegistration', 'QuestCompletion'
    correlation_id UUID NOT NULL,
    current_step VARCHAR(100) NOT NULL,
    saga_data JSONB NOT NULL DEFAULT '{}',
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'completed', 'failed', 'compensating')),

    -- Timeout handling
    started_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    timeout_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,

    -- Error handling
    error_message TEXT,
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3
);

-- Indexes for sagas
CREATE INDEX IF NOT EXISTS idx_sagas_type_status ON event_store.sagas(saga_type, status);
CREATE INDEX IF NOT EXISTS idx_sagas_correlation ON event_store.sagas(correlation_id);
CREATE INDEX IF NOT EXISTS idx_sagas_timeout ON event_store.sagas(timeout_at) WHERE status = 'active';

-- Table: event_store.event_handlers
-- Registry of event handlers for processing events
CREATE TABLE IF NOT EXISTS event_store.event_handlers
(
    handler_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    handler_name VARCHAR(200) NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    service_name VARCHAR(100) NOT NULL,
    handler_status VARCHAR(20) DEFAULT 'active' CHECK (handler_status IN ('active', 'inactive', 'failed')),

    -- Processing statistics
    last_processed_at TIMESTAMP WITH TIME ZONE,
    total_processed BIGINT DEFAULT 0,
    total_failed BIGINT DEFAULT 0,

    -- Configuration
    retry_policy JSONB DEFAULT '{"max_retries": 3, "backoff_seconds": 5}',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE(handler_name, event_type)
);

-- Indexes for event handlers
CREATE INDEX IF NOT EXISTS idx_event_handlers_event_type ON event_store.event_handlers(event_type);
CREATE INDEX IF NOT EXISTS idx_event_handlers_service ON event_store.event_handlers(service_name);

-- Table: event_store.dead_letter_queue
-- Dead letter queue for failed event processing
CREATE TABLE IF NOT EXISTS event_store.dead_letter_queue
(
    dlq_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL REFERENCES event_store.events(event_id),
    handler_id UUID REFERENCES event_store.event_handlers(handler_id),

    -- Failure information
    failure_reason TEXT NOT NULL,
    failure_count INTEGER DEFAULT 1,
    first_failed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_failed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Original event data for replay
    original_payload JSONB NOT NULL,
    original_metadata JSONB,

    -- Retry and resolution status
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'retrying', 'resolved', 'unresolvable')),
    resolved_at TIMESTAMP WITH TIME ZONE,
    resolved_by VARCHAR(100)
);

-- Indexes for dead letter queue
CREATE INDEX IF NOT EXISTS idx_dlq_event_id ON event_store.dead_letter_queue(event_id);
CREATE INDEX IF NOT EXISTS idx_dlq_status ON event_store.dead_letter_queue(status);
CREATE INDEX IF NOT EXISTS idx_dlq_last_failed ON event_store.dead_letter_queue(last_failed_at DESC);

-- Functions for event store operations

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION event_store.update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Function to get next aggregate version
CREATE OR REPLACE FUNCTION event_store.get_next_aggregate_version(p_aggregate_id UUID)
RETURNS BIGINT AS $$
DECLARE
    next_version BIGINT;
BEGIN
    SELECT COALESCE(MAX(aggregate_version), 0) + 1
    INTO next_version
    FROM event_store.events
    WHERE aggregate_id = p_aggregate_id;

    RETURN next_version;
END;
$$ LANGUAGE plpgsql;

-- Function to validate event order
CREATE OR REPLACE FUNCTION event_store.validate_event_order(
    p_aggregate_id UUID,
    p_expected_version BIGINT
) RETURNS BOOLEAN AS $$
DECLARE
    current_version BIGINT;
BEGIN
    SELECT COALESCE(MAX(aggregate_version), 0)
    INTO current_version
    FROM event_store.events
    WHERE aggregate_id = p_aggregate_id;

    RETURN current_version + 1 = p_expected_version;
END;
$$ LANGUAGE plpgsql;

-- Triggers for automatic timestamp updates
CREATE TRIGGER trigger_events_updated_at
    BEFORE UPDATE ON event_store.events
    FOR EACH ROW EXECUTE FUNCTION event_store.update_updated_at();

-- Comments for documentation
COMMENT ON SCHEMA event_store IS 'Event store schema for CQRS and event sourcing microservice orchestration';

COMMENT ON TABLE event_store.events IS 'Core event store table containing all domain events';
COMMENT ON COLUMN event_store.events.event_id IS 'Unique event identifier';
COMMENT ON COLUMN event_store.events.aggregate_id IS 'ID of the aggregate this event belongs to';
COMMENT ON COLUMN event_store.events.aggregate_type IS 'Type of aggregate (player, quest, etc.)';
COMMENT ON COLUMN event_store.events.aggregate_version IS 'Version of the aggregate after this event';
COMMENT ON COLUMN event_store.events.event_type IS 'Type of event (PlayerCreated, etc.)';
COMMENT ON COLUMN event_store.events.payload IS 'Event payload data in JSON format';
COMMENT ON COLUMN event_store.events.metadata IS 'Additional metadata for event processing';

COMMENT ON TABLE event_store.snapshots IS 'Snapshots for performance optimization in event sourcing';
COMMENT ON TABLE event_store.sagas IS 'Saga instances for complex business processes';
COMMENT ON TABLE event_store.event_handlers IS 'Registry of event handlers for processing';
COMMENT ON TABLE event_store.dead_letter_queue IS 'Dead letter queue for failed event processing';

-- BACKEND NOTE: Event store optimized for high-throughput event processing
-- Expected load: 5000+ events/sec across all microservices
-- Performance targets: P99 <50ms for event append, <200ms for aggregate reconstruction
-- Retention policy: Events kept for 7+ years, snapshots for 1+ years

COMMIT;


