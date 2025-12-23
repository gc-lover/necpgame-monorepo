# Match Statistics Aggregator Service

<!-- Issue: #2214 -->
Real-time match statistics aggregation system for MMOFPS games with high-performance event processing, Redis caching, and REST API.

## Overview

The Match Statistics Aggregator Service provides real-time collection, processing, and aggregation of match statistics for competitive gaming. Optimized for MMOFPS workloads with 1000+ concurrent matches and P99 <50ms latency.

## Architecture

### Components

- **Event Collector**: High-throughput event ingestion with buffered channels and worker pools
- **Statistics Aggregator**: Real-time calculation with memory-optimized data structures
- **Redis Cache**: Multi-level caching with TTL and compression
- **REST API**: HTTP handlers optimized for low latency

### Performance Features

- **Memory Pooling**: Zero allocations for hot paths
- **Concurrent Processing**: Worker pools for parallel aggregation
- **Event Buffering**: Configurable buffer sizes for burst handling
- **Context Timeouts**: All operations have configurable timeouts
- **Circuit Breakers**: Resilience patterns for external dependencies

## Quick Start

### Prerequisites

- Go 1.21+
- Redis 6.0+
- PostgreSQL 13+ (optional, for persistence)

### Local Development

```bash
# Clone repository
git clone https://github.com/gc-lover/necpgame-monorepo.git
cd necpgame-monorepo/services/match-stats-aggregator

# Install dependencies
make deps

# Run tests
make test

# Start service (development mode)
make run

# Start with Redis
make run-redis
```

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run

# Check health
curl http://localhost:8080/health
```

## API Endpoints

### Match Statistics

```http
# Get match statistics
GET /api/v1/match-stats/matches/{matchID}

# Get active matches
GET /api/v1/match-stats/matches/active

# Get player statistics
GET /api/v1/match-stats/matches/{matchID}/players/{playerID}

# Get leaderboard
GET /api/v1/match-stats/matches/{matchID}/leaderboard/{metric}?limit=10
```

### System Information

```http
# System statistics
GET /api/v1/match-stats/system/stats

# Health check
GET /health

# Readiness check
GET /ready

# Prometheus metrics
GET /metrics
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `HTTP_ADDR` | `:8080` | HTTP server address |
| `REDIS_ADDR` | `localhost:6379` | Redis server address |
| `REDIS_PASSWORD` | `` | Redis password |
| `REDIS_DB` | `0` | Redis database |
| `LOG_LEVEL` | `info` | Log level (debug, info, warn, error) |
| `WORKERS` | `4` | Number of worker goroutines |
| `BUFFER_SIZE` | `10000` | Event buffer size |

### Command Line Flags

```bash
./match-stats-aggregator --help
```

## Event Types

The service processes the following event types:

- `player_join`: Player joins match
- `kill`: Player kill event
- `death`: Player death event
- `damage`: Damage dealt event
- `position`: Player position update
- `item_use`: Item usage event

### Event Format

```json
{
  "event_id": "uuid",
  "match_id": "match-uuid",
  "player_id": "player-uuid",
  "event_type": "kill",
  "timestamp": "2024-01-01T00:00:00Z",
  "event_data": {
    "killer_id": "player-uuid",
    "victim_id": "player-uuid",
    "weapon": "cyberpunk-rifle",
    "headshot": true
  }
}
```

## Performance Benchmarks

### Latency Targets

- **P99 Response Time**: <50ms
- **P95 Response Time**: <20ms
- **Average Response Time**: <10ms

### Throughput Targets

- **Event Processing**: 100k events/sec
- **Concurrent Matches**: 1000+ active matches
- **API Requests**: 10k RPS

### Memory Usage

- **Per Match**: <1KB base + <50KB per player
- **Cache Hit Rate**: >95%
- **Zero Allocations**: Hot paths optimized

## Monitoring

### Health Checks

- `/health`: Basic health check
- `/ready`: Readiness for traffic
- `/metrics`: Prometheus metrics

### Key Metrics

- `match_stats_events_received_total`: Total events received
- `match_stats_events_processed_total`: Total events processed
- `match_stats_matches_active`: Number of active matches
- `match_stats_cache_hit_ratio`: Cache hit ratio
- `match_stats_processing_latency`: Event processing latency

## Development

### Project Structure

```
services/match-stats-aggregator/
├── main.go                    # Service entry point
├── go.mod/go.sum             # Go dependencies
├── Makefile                  # Build and deployment
├── Dockerfile               # Container definition
├── README.md                # Documentation
├── internal/
│   ├── collector/           # Event collection
│   ├── aggregator/          # Statistics calculation
│   ├── cache/              # Redis caching
│   └── api/                # HTTP handlers
└── pkg/models/              # Data structures
```

### Testing

```bash
# Run tests
make test

# Run benchmarks
make bench

# Load testing
make load-test
```

### Code Quality

```bash
# Lint code
make lint

# Format code
make format

# CI pipeline
make ci
```

## Deployment

### Production Configuration

```yaml
# Kubernetes deployment example
apiVersion: apps/v1
kind: Deployment
metadata:
  name: match-stats-aggregator
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: match-stats-aggregator
        image: necpgame/match-stats-aggregator:latest
        env:
        - name: WORKERS
          value: "8"
        - name: BUFFER_SIZE
          value: "50000"
        - name: REDIS_ADDR
          value: "redis-cluster:6379"
        resources:
          requests:
            memory: "512Mi"
            cpu: "500m"
          limits:
            memory: "1Gi"
            cpu: "1000m"
```

### Scaling

- **Horizontal Scaling**: Increase replica count
- **Worker Scaling**: Adjust WORKERS environment variable
- **Cache Scaling**: Redis cluster for distributed caching
- **Database Scaling**: Read replicas for statistics queries

## Troubleshooting

### Common Issues

1. **High Latency**
   - Check Redis connectivity
   - Monitor worker queue depth
   - Verify buffer sizes

2. **Memory Issues**
   - Check for goroutine leaks
   - Monitor cache memory usage
   - Verify pool configurations

3. **Event Loss**
   - Check buffer overflow logs
   - Monitor worker health
   - Verify network connectivity

### Debug Mode

```bash
# Enable debug logging
export LOG_LEVEL=debug

# Enable profiling
go tool pprof http://localhost:8080/debug/pprof/profile
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Run `make ci` for validation
5. Submit pull request

## License

This project is part of the NECPGAME monorepo.

## Related Issues

- Issue #2214: Real-time Match Statistics Aggregation
- Related: Event-driven architecture, Redis caching, REST API design
