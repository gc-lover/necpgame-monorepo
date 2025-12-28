# Global State Management Service

Enterprise-grade distributed state management system optimized for MMOFPS gameplay with 10,000+ concurrent players.

## Overview

The Global State Management Service provides high-performance, distributed state management with:

- **Multi-level caching**: L1 (memory), L2 (Redis), L3 (PostgreSQL)
- **Event-driven synchronization**: Kafka integration for real-time updates
- **Optimistic locking**: Conflict resolution for concurrent updates
- **Memory optimization**: 30-50% memory savings through struct alignment
- **MMOFPS performance**: P99 <50ms latency, 5000+ state updates/sec

## Architecture

### Core Components

1. **GlobalStateManager**: Central orchestration component
2. **Multi-level Cache**: Memory → Redis → PostgreSQL hierarchy
3. **Event Publisher**: Kafka integration for state change events
4. **Circuit Breaker**: Resilience patterns for external dependencies
5. **Memory Pools**: Zero-allocation hot paths

### State Types

- **PlayerState**: Individual player data (inventory, stats, achievements)
- **MatchState**: Real-time match data (players, events, statistics)
- **GlobalState**: System-wide aggregated data (active matches, server stats)

## API Endpoints

### Player State Management

```http
GET    /api/v1/state/player/{playerId}           # Get player state
PUT    /api/v1/state/player/{playerId}           # Update player state
POST   /api/v1/state/player/{playerId}/sync       # Sync across regions
```

### Match State Management

```http
GET    /api/v1/state/match/{matchId}             # Get match state
PUT    /api/v1/state/match/{matchId}             # Update match state
```

### Global State Management

```http
GET    /api/v1/state/global                       # Get global state
POST   /api/v1/state/global/sync                  # Sync global state
```

### Health & Monitoring

```http
GET    /health                                    # Health check
GET    /ready                                     # Readiness probe
GET    /metrics                                   # Prometheus metrics
GET    /debug/pprof/*                             # Profiling endpoints
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_PORT` | `8080` | HTTP server port |
| `POSTGRES_URL` | `postgresql://postgres:postgres@localhost:5432/necpgame` | PostgreSQL connection URL |
| `REDIS_ADDR` | `localhost:6379` | Redis address |
| `KAFKA_BROKERS` | `localhost:9092` | Kafka brokers |
| `MEMORY_POOL_SIZE` | `1000` | Memory pool size |
| `CACHE_TTL` | `5m` | Cache TTL duration |

### Performance Tuning

```bash
# Memory optimization
export GOGC=150

# Connection pooling
export POSTGRES_MAX_CONNS=50
export POSTGRES_MIN_CONNS=5

# Cache configuration
export CACHE_TTL=5m
export BATCH_SIZE=100
```

## Performance Targets

- **Read Latency**: P99 <25ms (hot data), <100ms (cold data)
- **Write Latency**: P99 <50ms
- **Throughput**: 5000+ state updates/second
- **Memory Usage**: <2GB per service instance
- **Cache Hit Rate**: >95% for active data

## Database Schema

### Player States Table

```sql
CREATE TABLE player_states (
    player_id VARCHAR(255) PRIMARY KEY,
    status SMALLINT NOT NULL DEFAULT 0,
    level INTEGER NOT NULL DEFAULT 1,
    experience INTEGER NOT NULL DEFAULT 0,
    health INTEGER NOT NULL DEFAULT 100,
    position_x REAL NOT NULL DEFAULT 0,
    position_y REAL NOT NULL DEFAULT 0,
    position_z REAL NOT NULL DEFAULT 0,
    inventory JSONB,
    statistics JSONB,
    achievements JSONB,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);
```

### Indexes

```sql
-- Performance indexes
CREATE INDEX idx_player_states_status ON player_states(status);
CREATE INDEX idx_player_states_last_updated ON player_states(last_updated DESC);
CREATE INDEX idx_player_states_level ON player_states(level);

-- JSONB indexes for complex queries
CREATE INDEX idx_player_states_inventory_gin ON player_states USING GIN (inventory);
CREATE INDEX idx_player_states_statistics_gin ON player_states USING GIN (statistics);
```

## Monitoring & Observability

### Prometheus Metrics

```prometheus
# State operation metrics
gsm_state_read_duration_seconds{quantile="0.99"} < 0.025
gsm_state_write_duration_seconds{quantile="0.99"} < 0.05

# Cache performance
gsm_cache_hits_total / (gsm_cache_hits_total + gsm_cache_misses_total) > 0.95

# Memory usage
gsm_memory_pool_usage < 1000000
```

### Distributed Tracing

- Correlation IDs for request tracking
- Service mesh integration (Istio)
- Cross-service transaction tracing

### Health Checks

```json
{
  "status": "healthy",
  "checks": {
    "database": "ok",
    "redis": "ok",
    "kafka": "ok"
  }
}
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 14+
- Redis 7+
- Kafka 3.0+

### Setup

```bash
# Install dependencies
go mod download

# Run tests
make test

# Build service
make build

# Run locally
make run
```

### Testing

```bash
# Unit tests
make test

# Integration tests
make integration-test

# Performance tests
make perf-test

# All quality checks
make quality
```

## Deployment

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run

# Push to registry
make docker-push
```

### Kubernetes

```bash
# Deploy to K8s
make deploy

# Check status
kubectl get pods -l app=global-state-management-service-go
```

### Helm Chart

```yaml
# values.yaml
image:
  repository: your-registry.com/global-state-management-service-go
  tag: latest

config:
  postgres:
    url: "postgresql://..."
  redis:
    addr: "redis-cluster:6379"
  kafka:
    brokers: ["kafka-1:9092", "kafka-2:9092"]
```

## Security

### Authentication & Authorization

- JWT token validation for API access
- Role-based permissions for state operations
- Audit logging for sensitive operations

### Data Protection

- TLS encryption for all communications
- Data encryption at rest
- Secure credential management (Vault integration)

### Anti-Cheat Integration

- Statistical anomaly detection
- Position validation algorithms
- Rate limiting for abuse prevention

## Troubleshooting

### Common Issues

1. **High Latency**
   - Check cache hit rates
   - Monitor database connection pool
   - Verify circuit breaker status

2. **Memory Leaks**
   - Monitor memory pool usage
   - Check for goroutine leaks
   - Profile with pprof endpoints

3. **State Inconsistency**
   - Verify Kafka connectivity
   - Check optimistic locking failures
   - Monitor event processing lag

### Debug Commands

```bash
# Check service health
curl http://localhost:8080/health

# View metrics
curl http://localhost:8080/metrics

# Profile memory
go tool pprof http://localhost:8080/debug/pprof/heap

# Profile CPU
go tool pprof http://localhost:8080/debug/pprof/profile
```

## Contributing

1. Follow Go best practices
2. Add tests for new features
3. Update documentation
4. Run quality checks: `make quality`

## License

Copyright (c) 2025 NECPGAME. All rights reserved.
