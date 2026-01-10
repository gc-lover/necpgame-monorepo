# Event Sourcing Aggregates Service

Enterprise-grade CQRS framework with Event Sourcing for NECPGAME microservices.

**Issue:** #2217  
**Status:** ✅ COMPLETED  
**Performance:** <5ms P99 event append, <50ms aggregate reconstruction

## Overview

This service implements a complete Event Sourcing framework with:

- **Aggregate Root Pattern** - Domain aggregates with uncommitted event handling
- **Event Store** - PostgreSQL-based event storage with optimistic concurrency
- **Snapshot Store** - Redis-based aggregate snapshots for performance
- **CQRS Architecture** - Command/Event buses with asynchronous processing
- **Saga Pattern** - Distributed transaction coordination
- **Read Model Projections** - Event-driven read model updates

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Command Bus     │────│ Aggregate Repo  │────│ Event Store     │
│ (CQRS)          │    │ (Repository)    │    │ (PostgreSQL)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
│                       │                       │
▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ Event Bus       │────│ Projections     │────│ Read Models     │
│ (Pub/Sub)       │    │ (Async)         │    │ (Redis/Cache)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## Performance Targets

- **Event Append:** <5ms P99 latency
- **Aggregate Load:** <50ms reconstruction time
- **Event Throughput:** 1000+ events/sec
- **Memory Usage:** <100KB per aggregate
- **Concurrent Users:** 200k+ simultaneous players

## Features

### Core Components

- **Player Aggregate** - Complete player domain logic with events
- **PostgreSQL Event Store** - ACID-compliant event storage
- **Redis Snapshot Store** - High-performance aggregate caching
- **Command/Event Buses** - Async processing pipelines
- **Saga Coordinator** - Distributed transaction management
- **Projection Manager** - Read model maintenance

### Enterprise Features

- Structured logging with correlation IDs
- Health checks and graceful shutdown
- Optimistic concurrency control
- Event versioning and migration support
- Comprehensive error handling
- Docker containerization ready

## Quick Start

### Prerequisites

- Go 1.24+
- PostgreSQL 15+
- Redis 7+ (optional, for snapshots)

### Build

```bash
make build
```

### Run Locally

```bash
# Set environment variables
export DATABASE_URL="postgres://user:password@localhost:5432/events?sslmode=disable"
export REDIS_URL="redis://localhost:6379"

# Run service
make run
```

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run
```

## API Endpoints

- `GET /health` - Service health check
- `GET /ready` - Readiness probe
- `POST /commands` - Execute commands (future)
- `GET /events` - Query events (future)

## Configuration

| Environment Variable | Description | Default |
|---------------------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection string | Required |
| `REDIS_URL` | Redis connection string | Optional |
| `PORT` | HTTP server port | `8080` |
| `GOGC` | Go GC percentage | `150` |

## Database Schema

### Events Table
```sql
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    event_id UUID UNIQUE NOT NULL,
    aggregate_id UUID NOT NULL,
    aggregate_type VARCHAR(50) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_version INTEGER NOT NULL,
    event_data JSONB NOT NULL,
    metadata JSONB,
    correlation_id UUID,
    causation_id UUID,
    server_id VARCHAR(100),
    player_id UUID,
    session_id UUID,
    event_timestamp TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    state_changes JSONB,
    affected_players JSONB,
    is_processed BOOLEAN DEFAULT FALSE,
    processing_error TEXT,
    retry_count INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Usage Examples

### Creating a Player Aggregate

```go
// Create aggregate
playerID := uuid.New()
player := aggregates.NewPlayerAggregate(playerID)

// Execute command
err := player.CreatePlayer("john_doe", "john@example.com")
if err != nil {
    log.Fatal(err)
}

// Save to repository
err = repo.Save(ctx, player)
if err != nil {
    log.Fatal(err)
}
```

### Loading from Event Store

```go
// Load aggregate with snapshot optimization
player, err := repo.LoadPlayer(ctx, playerID)
if err != nil {
    log.Fatal(err)
}

// Access current state
state := player.GetState()
fmt.Printf("Player level: %d\n", state.Level)
```

## Monitoring

### Health Checks

The service provides health endpoints:

```bash
# Health check
curl http://localhost:8080/health

# Readiness check
curl http://localhost:8080/ready
```

### Metrics

- Event processing latency
- Aggregate reconstruction time
- Command execution duration
- Saga completion rates
- Projection update lag

## Development

### Testing

```bash
# Run all tests
make test

# Run performance tests
make perf-test

# Run with coverage
go test -coverprofile=coverage.out ./...
go tool cover -html=coverage.out
```

### Code Quality

```bash
# Format code
make fmt

# Run linter
make lint

# Run vet
make vet
```

## Deployment

### Production Configuration

```yaml
# Kubernetes deployment example
apiVersion: apps/v1
kind: Deployment
metadata:
  name: event-sourcing-aggregates
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: event-sourcing-aggregates
        image: necpgame/event-sourcing-aggregates:latest
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: REDIS_URL
          value: "redis://redis-cluster:6379"
        resources:
          requests:
            memory: "256Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

### Scaling Considerations

- **Horizontal Scaling:** Event store partitioning by aggregate_id
- **Read Scaling:** Multiple projection instances with event bus
- **Cache Scaling:** Redis cluster for snapshot storage
- **Load Balancing:** Kubernetes service with session affinity

## Integration

### Kafka Integration

The service is designed to integrate with Kafka event streams:

```go
// Subscribe to world events
eventBus.Subscribe("world.tick.hourly", worldEventHandler)

// Publish domain events
eventBus.Publish(ctx, playerLevelGainedEvent)
```

### Service Dependencies

- **PostgreSQL** - Event storage and read models
- **Redis** - Snapshot caching and session storage
- **Kafka** - Event streaming and inter-service communication

## Security

### Event Data Protection

- Event data encryption at rest
- TLS for all external communications
- API key authentication for event access
- Audit logging for all event operations

### Access Control

- Aggregate-level authorization
- Event type-based permissions
- Player data isolation
- GDPR-compliant data handling

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   ```bash
   # Check DATABASE_URL format
   echo $DATABASE_URL
   # Verify PostgreSQL is running
   pg_isready -h localhost -p 5432
   ```

2. **Redis Connection Failed**
   ```bash
   # Redis snapshots are optional
   # Service will work without Redis but with reduced performance
   ```

3. **High Memory Usage**
   ```bash
   # Adjust GOGC environment variable
   export GOGC=200
   ```

### Logs

```bash
# View structured logs
docker logs event-sourcing-aggregates

# Filter by level
docker logs event-sourcing-aggregates 2>&1 | grep "level=error"
```

## Contributing

1. Follow Go coding standards
2. Add tests for new features
3. Update documentation
4. Run full test suite before PR

## License

Internal NECPGAME project - proprietary license.

---

**Built with enterprise-grade Event Sourcing patterns for MMOFPS RPG scalability.**