# Real-time Combat Service

## Issue: #2232

Enterprise-grade real-time combat service for NECPGAME MMOFPS RPG with WebSocket support, event-driven architecture, and high-performance optimizations.

## Features

### Core Functionality
- **Real-time Combat Sessions**: WebSocket-based live combat with 10k+ concurrent connections
- **Position Synchronization**: Anti-cheat validated position updates with lag compensation
- **Damage Calculation**: High-frequency damage events (2000+ RPS) with validation
- **Combat Actions**: Cooldown-managed combat actions with state validation
- **Spectator Mode**: Live tournament spectating with replay capabilities
- **Event Sourcing**: Complete combat replay and analytics via event sourcing

### Performance Optimizations
- **MMOFPS Standards**: P99 <50ms latency, 2000+ RPS hot paths
- **Memory Pooling**: Zero allocations in hot paths, <50MB per 1000 connections
- **Connection Pooling**: PostgreSQL (50 connections), Redis for caching
- **Circuit Breaker**: Resilience patterns for external service failures
- **Worker Pools**: Concurrent event processing with configurable workers

### Monitoring & Observability
- **Prometheus Metrics**: Throughput, latency, error rates, active connections
- **Structured Logging**: Zap with correlation IDs and request tracing
- **Health Checks**: `/health`, `/ready` endpoints with Kubernetes probes
- **Profiling**: pprof enabled on port 6060 for performance analysis

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   WebSocket     │    │   HTTP REST     │    │   Kafka         │
│   Connections   │◄──►│   API Gateway   │◄──►│   Events        │
│   (10k+ conn)   │    │   (Chi Router)  │    │   Streaming     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Combat        │    │   Business      │    │   Event Store   │
│   Handlers      │◄──►│   Logic         │◄──►│   (PostgreSQL)  │
│   (Optimized)   │    │   (Service)     │    │   + Redis Cache │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## API Endpoints

### Combat Sessions
- `GET /api/v1/combat/sessions` - List active combat sessions
- `POST /api/v1/combat/sessions` - Create new combat session
- `GET /api/v1/combat/sessions/{sessionId}` - Get session details
- `PUT /api/v1/combat/sessions/{sessionId}` - Update session
- `DELETE /api/v1/combat/sessions/{sessionId}` - End session

### Session Participation
- `POST /api/v1/combat/sessions/{sessionId}/join` - Join combat session
- `POST /api/v1/combat/sessions/{sessionId}/leave` - Leave session

### Real-time Combat
- `POST /api/v1/combat/sessions/{sessionId}/damage` - Apply damage (HOT PATH)
- `POST /api/v1/combat/sessions/{sessionId}/actions` - Execute combat action
- `POST /api/v1/combat/sessions/{sessionId}/spectate` - Start spectating
- `GET /api/v1/combat/sessions/{sessionId}/state` - Get combat state
- `POST /api/v1/combat/sessions/{sessionId}/position` - Update position

### Analytics & Replay
- `GET /api/v1/combat/sessions/{sessionId}/replay` - Combat replay events
- `GET /api/v1/combat/sessions/{sessionId}/stats` - Session statistics
- `GET /api/v1/combat/players/{playerId}/stats` - Player combat stats

### Health & Monitoring
- `GET /health` - Health check
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics
- `GET /debug/pprof/` - Profiling endpoints

## Configuration

### Environment Variables
```bash
# Server
PORT=8085

# Database
DATABASE_URL=postgres://user:password@localhost:5432/necpgame?sslmode=disable

# Cache
REDIS_URL=redis://localhost:6379

# Security
JWT_SECRET=your-secret-key

# Environment
ENVIRONMENT=development
LOG_LEVEL=info
```

### Database Schema

```sql
-- Combat sessions
CREATE TABLE gameplay.combat_sessions (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    max_players INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    map_id VARCHAR(255) NOT NULL,
    game_mode VARCHAR(255) NOT NULL
);

-- Combat events for replay and analytics
CREATE TABLE gameplay.combat_events (
    id VARCHAR(255) PRIMARY KEY,
    session_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    player_id VARCHAR(255) NOT NULL,
    data JSONB NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (session_id) REFERENCES gameplay.combat_sessions(id)
);

-- Indexes for performance
CREATE INDEX idx_combat_sessions_status ON gameplay.combat_sessions(status);
CREATE INDEX idx_combat_events_session ON gameplay.combat_events(session_id, timestamp);
CREATE INDEX idx_combat_events_player ON gameplay.combat_events(player_id);
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
./bin/realtime-combat-service
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
make docker-compose-up
```

## Performance Benchmarks

### Latency Targets
- **WebSocket position updates**: P99 <15ms
- **Damage events**: P99 <10ms
- **API endpoints**: P99 <50ms
- **Database queries**: P95 <5ms

### Throughput Targets
- **Concurrent connections**: 10,000+
- **Damage events/sec**: 2,000+
- **Position updates/sec**: 5,000+
- **API requests/sec**: 1,000+

### Resource Usage
- **Memory per connection**: ~5KB
- **CPU per 1000 connections**: <10%
- **Network bandwidth**: <1Mbps per connection
- **Database connections**: 50 max

## Monitoring

### Key Metrics
```
combat_sessions_created_total     - Session creation rate
combat_damage_events_total        - Damage event throughput
combat_request_duration_seconds   - Request latency histogram
combat_active_sessions            - Current active sessions
combat_errors_total               - Error rate tracking
```

### Health Checks
- **Readiness**: Database and Redis connectivity
- **Liveness**: Service responsiveness
- **Custom**: Combat session processing health

## Deployment

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: realtime-combat-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: realtime-combat-service
  template:
    metadata:
      labels:
        app: realtime-combat-service
    spec:
      containers:
      - name: realtime-combat-service
        image: realtime-combat-service:latest
        ports:
        - containerPort: 8085
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: REDIS_URL
          valueFrom:
            configMapKeyRef:
              name: redis-config
              key: url
        livenessProbe:
          httpGet:
            path: /health
            port: 8085
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8085
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Integration Points

### Event-Driven Architecture
- **Kafka Topics**: `game.combat.events`, `game.combat.damage.validation`
- **Event Types**: damage, position, action, session lifecycle
- **Consumer Groups**: combat-processor, damage-validator, analytics

### External Services
- **Database**: PostgreSQL for persistent storage
- **Cache**: Redis for hot data and session state
- **Message Queue**: Kafka for event streaming
- **Monitoring**: Prometheus for metrics collection

### API Gateway Integration
- **Authentication**: JWT token validation
- **Rate Limiting**: Request throttling per client
- **Load Balancing**: Round-robin across instances
- **Circuit Breaking**: Failure isolation

## Development

### Code Structure
```
internal/
├── config/        # Configuration management
├── handlers/      # HTTP request handlers
├── service/       # Business logic layer
├── repository/    # Data access layer
└── metrics/       # Monitoring and metrics

pkg/               # Shared packages (future)
cmd/               # CLI tools (future)
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Benchmarks
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

## Contributing

1. Follow Go best practices and SOLID principles
2. Write comprehensive tests for new features
3. Update documentation for API changes
4. Ensure performance benchmarks pass
5. Follow conventional commit messages

## License

Apache License 2.0
