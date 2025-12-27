# Event Sourcing Aggregates Service

## Issue: #2217

Enterprise-grade event sourcing and CQRS aggregates service for NECPGAME microservice orchestration with real-time event processing, aggregate rebuilding, and read model projections.

## Features

### Core Functionality
- **Event Store**: Complete event sourcing implementation with PostgreSQL persistence
- **CQRS Architecture**: Command Query Responsibility Segregation with separate read/write models
- **Aggregate Management**: Automatic aggregate rebuilding from event streams
- **Snapshot Support**: Performance optimization with configurable aggregate snapshots
- **Real-time Processing**: Kafka-based event streaming with background workers
- **Read Model Projections**: Optimized query models for high-performance reads
- **Event Analytics**: Comprehensive event processing metrics and analytics

### Performance Optimizations
- **Event Sourcing**: Optimized for high-throughput event appends and streams
- **Snapshot Strategy**: Configurable snapshot intervals to reduce rebuild times
- **Kafka Streaming**: Real-time event distribution with guaranteed delivery
- **Redis Caching**: Hot data caching for frequently accessed aggregates
- **Background Processing**: Multi-worker event processing with error handling
- **Database Optimization**: Indexed event store for fast stream retrieval

### Enterprise Features
- **Event Versioning**: Backward-compatible event schema evolution
- **Correlation Tracking**: Distributed tracing with causation/correlation IDs
- **Processing Status**: Event processing state tracking with retry mechanisms
- **Aggregate Projections**: Automated read model updates from event streams
- **Event Replay**: Complete system rebuild capability from event history
- **Audit Trail**: Immutable event history for compliance and debugging

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   HTTP REST     │    │   Business      │    │   PostgreSQL    │
│   API (Chi)     │◄──►│   Logic         │◄──►│   Event Store    │
│   (CQRS)        │    │   (Aggregates)  │    │   + Snapshots    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Kafka Events  │    │   Redis Cache   │    │   Read Models   │
│   Streaming     │    │   (Hot Data)    │    │   Projections    │
│   (Real-time)   │    │   (Aggregates)  │    │   (Queries)      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Event Sourcing Concepts

### Events
Events are immutable facts that have occurred in the system:
```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440000",
  "aggregateId": "550e8400-e29b-41d4-a716-446655440001",
  "aggregateType": "player",
  "aggregateVersion": 5,
  "eventType": "PlayerLevelUp",
  "eventVersion": 1,
  "occurredAt": "2024-12-27T16:00:00Z",
  "payload": {
    "newLevel": 25,
    "experienceGained": 1500
  },
  "metadata": {
    "source": "game_server_01",
    "gameMode": "ranked"
  },
  "causationId": "550e8400-e29b-41d4-a716-446655440002",
  "correlationId": "550e8400-e29b-41d4-a716-446655440003"
}
```

### Aggregates
Aggregates are consistency boundaries that encapsulate state and behavior:
```json
{
  "aggregateId": "550e8400-e29b-41d4-a716-446655440001",
  "aggregateType": "player",
  "currentVersion": 15,
  "state": {
    "playerId": "player_001",
    "name": "CyberNinja",
    "level": 25,
    "experience": 12500,
    "inventory": ["sword_001", "armor_002"],
    "achievements": ["first_kill", "level_25"]
  }
}
```

### Read Models
Read models are optimized for queries and denormalized for performance:
```json
{
  "id": "player_001",
  "modelName": "player_summary",
  "data": {
    "name": "CyberNinja",
    "level": 25,
    "totalKills": 1500,
    "totalDeaths": 800,
    "kdRatio": 1.875,
    "favoriteWeapon": "cyber_blade",
    "lastSeen": "2024-12-27T16:00:00Z"
  },
  "version": 15,
  "updatedAt": "2024-12-27T16:00:00Z"
}
```

## API Endpoints

### Event Store Operations
- `GET /api/v1/events/stream/{aggregateId}` - Get event stream for aggregate
- `GET /api/v1/events/aggregates/{aggregateType}` - Get aggregates by type
- `GET /api/v1/events/aggregates/{aggregateType}/{aggregateId}` - Get aggregate state
- `POST /api/v1/events/events` - Append new event to store
- `GET /api/v1/events/events` - Query events with filters

### Aggregate Operations
- `POST /api/v1/events/aggregates/{aggregateType}/{aggregateId}/rebuild` - Rebuild aggregate
- `GET /api/v1/events/aggregates/{aggregateType}/{aggregateId}/snapshot` - Get aggregate snapshot
- `POST /api/v1/events/aggregates/{aggregateType}/{aggregateId}/snapshot` - Create snapshot

### Read Model Operations
- `GET /api/v1/events/read-models/{modelName}` - Get read models by type
- `GET /api/v1/events/read-models/{modelName}/{id}` - Get specific read model

### Processing & Analytics
- `GET /api/v1/events/processing/status` - Get processing status
- `POST /api/v1/events/processing/retry/{eventId}` - Retry failed event
- `GET /api/v1/events/analytics/events-per-day` - Events per day analytics
- `GET /api/v1/events/analytics/processing-latency` - Processing latency analytics
- `GET /api/v1/events/analytics/aggregate-sizes` - Aggregate sizes analytics

### Health & Monitoring
- `GET /health` - Health check
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics

## Event Store Schema

### Events Table
```sql
-- Core event store for all domain events
CREATE TABLE event_store.events (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_version BIGINT NOT NULL,
    event_type VARCHAR(200) NOT NULL,
    event_version INTEGER NOT NULL DEFAULT 1,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    payload JSONB NOT NULL,
    metadata JSONB DEFAULT '{}',
    causation_id UUID,
    correlation_id UUID,
    processed_at TIMESTAMP WITH TIME ZONE,
    processing_status VARCHAR(20) DEFAULT 'pending',
    processing_error TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Aggregate Snapshots Table
```sql
-- Performance optimization for aggregate rebuilding
CREATE TABLE event_store.aggregate_snapshots (
    aggregate_id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    version BIGINT NOT NULL,
    state JSONB NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Read Models Table
```sql
-- CQRS read models for optimized queries
CREATE TABLE event_store.read_models (
    id VARCHAR(255) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    data JSONB NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (model_name, id)
);
```

### Processing Status Table
```sql
-- Event processing status tracking
CREATE TABLE event_store.processing_status (
    event_id UUID PRIMARY KEY REFERENCES event_store.events(event_id),
    status VARCHAR(20) NOT NULL,
    attempts INTEGER DEFAULT 0,
    last_attempt TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    next_retry TIMESTAMP WITH TIME ZONE
);
```

### Performance Indexes
```sql
-- Event stream queries
CREATE INDEX idx_events_aggregate ON event_store.events(aggregate_id, aggregate_version);
CREATE INDEX idx_events_type_time ON event_store.events(event_type, occurred_at DESC);
CREATE INDEX idx_events_processing ON event_store.events(processing_status, occurred_at);

-- Aggregate queries
CREATE INDEX idx_snapshots_type ON event_store.aggregate_snapshots(aggregate_type, version DESC);

-- Read model queries
CREATE INDEX idx_read_models_name ON event_store.read_models(model_name, updated_at DESC);

-- GIN indexes for JSONB
CREATE INDEX idx_events_payload_gin ON event_store.events USING GIN (payload);
CREATE INDEX idx_events_metadata_gin ON event_store.events USING GIN (metadata);
CREATE INDEX idx_snapshots_state_gin ON event_store.aggregate_snapshots USING GIN (state);
CREATE INDEX idx_read_models_data_gin ON event_store.read_models USING GIN (data);
```

## Configuration

### Environment Variables
```bash
# Server
PORT=8082

# Database
DATABASE_URL=postgres://user:password@localhost:5432/necpgame?sslmode=disable

# Cache
REDIS_URL=redis://localhost:6379

# Message Queue
KAFKA_BROKERS=localhost:9092
CONSUMER_GROUP=event-sourcing-aggregates

# Security
JWT_SECRET=your-secret-key

# Event Store
EVENT_STORE_SCHEMA=event_store
SNAPSHOT_INTERVAL=100         # Events between snapshots
PROCESSING_WORKERS=10         # Background workers

# Environment
ENVIRONMENT=development
LOG_LEVEL=info
```

### Kafka Topics
```
events.player           - Player aggregate events
events.quest           - Quest aggregate events
events.combat_session  - Combat session events
events.tournament      - Tournament events
events.guild           - Guild aggregate events
events.economy         - Economy events
events.inventory       - Inventory events
```

## Event Processing Pipeline

### Event Append Flow
```
1. Receive Event via REST API
2. Validate Event Schema
3. Assign Version Number
4. Store in PostgreSQL
5. Publish to Kafka Topic
6. Return Success Response
```

### Background Processing Flow
```
1. Kafka Consumer Reads Event
2. Deserialize Event Payload
3. Find Event Processor
4. Process Event (Update Aggregates)
5. Update Read Models
6. Mark Event as Processed
7. Handle Errors with Retry Logic
```

### Aggregate Rebuild Flow
```
1. Receive Rebuild Request
2. Load All Events for Aggregate
3. Apply Events in Order
4. Build Current State
5. Optionally Create Snapshot
6. Return Rebuilt State
```

## Snapshot Strategy

### Automatic Snapshots
- **Interval-based**: Every N events (configurable)
- **Size-based**: When event count exceeds threshold
- **Time-based**: Periodic snapshots for active aggregates

### Manual Snapshots
- **On-demand**: API-triggered snapshot creation
- **Maintenance**: Administrative snapshot operations
- **Migration**: During system upgrades

### Snapshot Optimization
- **Compression**: JSONB compression for large states
- **Retention**: Configurable snapshot history
- **Cleanup**: Automatic old snapshot removal

## Read Model Projections

### Player Summary Projection
```json
{
  "id": "player_001",
  "modelName": "player_summary",
  "data": {
    "name": "CyberNinja",
    "level": 25,
    "experience": 12500,
    "totalKills": 1500,
    "totalDeaths": 800,
    "kdRatio": 1.875,
    "rank": "Diamond",
    "favoriteWeapon": "cyber_blade"
  }
}
```

### Quest Progress Projection
```json
{
  "id": "quest_001",
  "modelName": "quest_progress",
  "data": {
    "title": "Corporate Espionage",
    "status": "in_progress",
    "progress": 0.75,
    "objectives": [
      {"id": "obj_1", "description": "Hack terminal", "completed": true},
      {"id": "obj_2", "description": "Steal data", "completed": false}
    ],
    "participants": ["player_001", "player_002"]
  }
}
```

## Monitoring

### Key Metrics
```
event_sourcing_events_appended_total          - Event append rate
event_sourcing_events_processed_total          - Event processing rate
event_sourcing_processing_errors_total         - Processing error rate
event_sourcing_snapshots_created_total         - Snapshot creation rate
event_sourcing_read_models_updated_total       - Read model update rate
event_sourcing_active_aggregates               - Active aggregates count
event_sourcing_pending_events                  - Pending events queue
event_sourcing_processing_duration_seconds     - Event processing latency
event_sourcing_event_append_latency_seconds    - Event append latency
event_sourcing_snapshot_creation_duration_seconds - Snapshot creation time
```

### Health Checks
- **Database Connectivity**: PostgreSQL connection validation
- **Redis Availability**: Cache system health checks
- **Kafka Connectivity**: Message queue health validation
- **Processing Workers**: Background worker status monitoring
- **Event Store Integrity**: Schema and data consistency checks

## Performance Benchmarks

### Event Processing
- **Append Latency**: P99 <50ms for event storage
- **Processing Latency**: P99 <100ms for event handling
- **Throughput**: 5000+ events/second sustained
- **Kafka Lag**: <1 second average processing delay

### Aggregate Operations
- **Stream Retrieval**: P99 <200ms for event streams
- **Rebuild Time**: <500ms for typical aggregates (<100 events)
- **Snapshot Load**: P99 <10ms for cached snapshots
- **State Size**: <50KB average aggregate state

### Read Model Queries
- **Query Latency**: P99 <25ms for read model queries
- **Cache Hit Rate**: 95%+ for hot read models
- **Update Latency**: P99 <50ms for projection updates
- **Consistency Delay**: <2 seconds for eventual consistency

## Error Handling

### Retry Mechanisms
- **Exponential Backoff**: Failed event processing retries
- **Circuit Breaker**: External service failure protection
- **Dead Letter Queue**: Unprocessable event handling
- **Manual Intervention**: Administrative retry capabilities

### Eventual Consistency
- **Read Model Updates**: Asynchronous projection updates
- **Cache Invalidation**: Automatic cache refresh on updates
- **Conflict Resolution**: Aggregate version conflict handling
- **Compensation Events**: Error compensation through new events

## Development

### Code Structure
```
internal/
├── config/        # Configuration management
├── handlers/      # HTTP request handlers
├── service/       # Business logic layer
├── repository/    # Data access layer
├── metrics/       # Monitoring and metrics

pkg/               # Shared packages (future)
cmd/               # CLI tools (future)
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Event sourcing tests
go test -tags=eventsourcing ./...

# Performance benchmarks
go test -bench=. ./...
```

### Code Quality
```bash
# Format code
make fmt

# Lint code
make lint

# Full CI pipeline
make all
```

## Quick Start

### Local Development
```bash
# Install dependencies
go mod tidy

# Run locally
make run

# Or build and run
make build
./bin/event-sourcing-aggregates
```

### Docker
```bash
# Build image
make docker-build

# Run container
make docker-run
```

### Docker Compose
```bash
# Start all services
make docker-compose up
```

## Example Usage

### Append Event
```bash
curl -X POST http://localhost:8082/api/v1/events/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-jwt-token" \
  -d '{
    "aggregateId": "550e8400-e29b-41d4-a716-446655440001",
    "aggregateType": "player",
    "eventType": "PlayerLevelUp",
    "payload": {
      "newLevel": 25,
      "experienceGained": 1500
    },
    "metadata": {
      "source": "game_server_01"
    }
  }'
```

### Get Aggregate State
```bash
curl -X GET http://localhost:8082/api/v1/events/aggregates/player/550e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer your-jwt-token"
```

### Get Read Model
```bash
curl -X GET http://localhost:8082/api/v1/events/read-models/player_summary/player_001 \
  -H "Authorization: Bearer your-jwt-token"
```

## Integration Points

### Game Services
- **Player Service**: Player aggregate events and state management
- **Quest Service**: Quest progress events and completion tracking
- **Combat Service**: Combat session events and statistics aggregation
- **Tournament Service**: Tournament events and bracket management

### Infrastructure Services
- **Kafka**: Event streaming and distribution
- **Redis**: Aggregate caching and session storage
- **PostgreSQL**: Event store persistence and read models
- **Prometheus**: Metrics collection and alerting

### External Systems
- **Analytics Platform**: Event data for player behavior analysis
- **Data Warehouse**: Event history for business intelligence
- **Audit Systems**: Immutable event trail for compliance
- **Monitoring Systems**: Real-time event processing metrics

## Contributing

1. Follow Go best practices and SOLID principles
2. Write comprehensive tests for event processing logic
3. Update API documentation for event sourcing operations
4. Ensure performance benchmarks pass for event throughput
5. Implement proper error handling and eventual consistency

## License

Apache License 2.0
