# Combat Stats Service

## Issue: #2250

Enterprise-grade combat statistics and analytics service for NECPGAME MMOFPS RPG with real-time battle metrics, performance tracking, and anti-cheat validation.

## Features

### Core Functionality
- **Real-time Statistics**: Live combat metrics aggregation with sub-second latency
- **Player Performance**: Individual player analytics with skill ratings and improvement tracking
- **Weapon Analytics**: Weapon effectiveness, popularity, and meta-relevance analysis
- **Leaderboards**: Dynamic rankings by kills, score, and weapon usage
- **Match Analytics**: Detailed match statistics and performance insights
- **Anti-cheat Validation**: Statistical anomaly detection for fair play

### Performance Optimizations
- **MMOFPS Standards**: P99 <50ms for hot paths, <200ms for analytics
- **Memory Pooling**: Zero allocations in hot combat processing paths
- **Event Streaming**: Kafka integration for real-time event processing
- **Redis Caching**: Hot data caching with TTL-based expiration
- **Connection Pooling**: PostgreSQL (100 connections) + Redis optimized pools
- **Circuit Breaker**: Resilience patterns for external service failures

### Enterprise Features
- **Event-Driven Architecture**: CQRS pattern with event sourcing
- **Analytics Pipeline**: Real-time data processing and aggregation
- **Monitoring & Alerting**: Prometheus metrics with custom dashboards
- **Scalable Architecture**: Horizontal scaling with stateless design
- **Data Consistency**: Event-sourced with guaranteed delivery
- **Security**: JWT authentication with role-based access control

## Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   HTTP REST     │    │   Business      │    │   PostgreSQL    │
│   API (Chi)     │◄──►│   Logic         │◄──►│   + Redis Cache  │
│   (JWT Auth)    │    │   (Service)     │    │   (Hot Stats)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Kafka Events  │    │   Prometheus    │    │   Event Store   │
│   Streaming     │    │   Metrics       │    │   (CQRS)        │
│   (Real-time)   │    │   (Monitoring)  │    │   (Audit Trail)  │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## API Endpoints

### Player Statistics
- `GET /api/v1/combat-stats/player/{playerId}` - Get player combat stats
- `GET /api/v1/combat-stats/performance/player/{playerId}` - Get player performance metrics

### Weapon Analytics
- `GET /api/v1/combat-stats/weapon/{weaponId}` - Get weapon statistics
- `GET /api/v1/combat-stats/leaderboards/weapon/{weaponId}` - Get weapon leaderboard
- `GET /api/v1/combat-stats/performance/weapon/{weaponId}` - Get weapon performance

### Match Analytics
- `GET /api/v1/combat-stats/match/{matchId}` - Get match statistics
- `GET /api/v1/combat-stats/performance/match/{matchId}` - Get match performance

### Leaderboards
- `GET /api/v1/combat-stats/leaderboards/kills` - Kill leaderboard
- `GET /api/v1/combat-stats/leaderboards/score` - Score leaderboard

### Analytics
- `GET /api/v1/combat-stats/analytics/damage` - Damage analytics
- `GET /api/v1/combat-stats/analytics/kill-death` - K/D analytics
- `GET /api/v1/combat-stats/analytics/playtime` - Playtime analytics

### Event Recording
- `POST /api/v1/combat-stats/events` - Record combat event (kill, death, damage)

### Health & Monitoring
- `GET /health` - Health check
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics

## Configuration

### Environment Variables
```bash
# Server
PORT=8080

# Database
DATABASE_URL=postgres://user:password@localhost:5432/necpgame?sslmode=disable

# Cache
REDIS_URL=redis://localhost:6379

# Security
JWT_SECRET=your-secret-key

# Performance
GOGC=75                    # Higher GC threshold for stats workloads
CACHE_SIZE=10000           # Redis cache size
STATS_TTL=24h             # Statistics cache TTL

# Environment
ENVIRONMENT=development
LOG_LEVEL=info
```

### Database Schema

```sql
-- Player combat statistics with real-time updates
CREATE TABLE combat.player_stats (
    player_id VARCHAR(255) PRIMARY KEY,
    total_kills BIGINT NOT NULL DEFAULT 0,
    total_deaths BIGINT NOT NULL DEFAULT 0,
    total_score BIGINT NOT NULL DEFAULT 0,
    total_playtime BIGINT NOT NULL DEFAULT 0,
    weapon_stats JSONB,
    match_history JSONB,
    accuracy DECIMAL(3,2) DEFAULT 0.00,
    headshot_rate DECIMAL(3,2) DEFAULT 0.00,
    avg_damage_per_kill DECIMAL(6,2) DEFAULT 0.00,
    last_updated TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Weapon performance statistics
CREATE TABLE combat.weapon_stats (
    weapon_id VARCHAR(255) PRIMARY KEY,
    total_kills BIGINT NOT NULL DEFAULT 0,
    total_shots BIGINT NOT NULL DEFAULT 0,
    total_hits BIGINT NOT NULL DEFAULT 0,
    accuracy DECIMAL(5,4) DEFAULT 0.0000,
    avg_damage DECIMAL(6,2) DEFAULT 0.00,
    last_used TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Match statistics with player performance
CREATE TABLE combat.match_stats (
    id BIGSERIAL PRIMARY KEY,
    match_id VARCHAR(255) NOT NULL,
    player_id VARCHAR(255) NOT NULL,
    kills INTEGER NOT NULL DEFAULT 0,
    deaths INTEGER NOT NULL DEFAULT 0,
    score INTEGER NOT NULL DEFAULT 0,
    playtime INTEGER NOT NULL DEFAULT 0,
    weapon_usage JSONB,
    damage_dealt INTEGER NOT NULL DEFAULT 0,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    metadata JSONB
);

-- Performance indexes
CREATE INDEX idx_player_stats_kills ON combat.player_stats(total_kills DESC);
CREATE INDEX idx_player_stats_score ON combat.player_stats(total_score DESC);
CREATE INDEX idx_weapon_stats_kills ON combat.weapon_stats(total_kills DESC);
CREATE INDEX idx_match_stats_match ON combat.match_stats(match_id);
CREATE INDEX idx_match_stats_player ON combat.match_stats(player_id, start_time DESC);

-- Partial indexes for active players
CREATE INDEX idx_player_stats_active ON combat.player_stats(last_updated DESC)
    WHERE last_updated > CURRENT_TIMESTAMP - INTERVAL '30 days';
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
./bin/combat-stats-service
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
- **Player Stats**: P99 <25ms (cached), <100ms (database)
- **Weapon Stats**: P99 <15ms (cached), <50ms (database)
- **Leaderboards**: P99 <50ms (top 100), <200ms (top 1000)
- **Event Recording**: P99 <10ms (async processing)
- **Analytics**: P99 <100ms (real-time), <500ms (historical)

### Throughput Targets
- **Concurrent Users**: 10,000+ simultaneous stat tracking
- **Events/sec**: 5000+ combat events processed
- **Queries/sec**: 2000+ statistics queries sustained
- **Cache Hit Rate**: 95%+ for hot player/weapon data
- **Database Load**: <50% CPU utilization under peak load

### Resource Usage
- **Memory per Player**: <50KB active stats in memory
- **Cache Memory**: <500MB for 10k active players
- **Database Connections**: 100 max optimized pool
- **Network Bandwidth**: <2Mbps per 1000 concurrent users

## Statistics System Design

### Player Statistics
- **Kills/Deaths/Score**: Core combat metrics
- **Accuracy/Headshot Rate**: Precision metrics
- **Playtime**: Engagement tracking
- **Weapon Usage**: Loadout optimization
- **Match History**: Performance trends

### Weapon Analytics
- **Kill Efficiency**: Kills per shot/minute
- **Popularity**: Usage frequency across playerbase
- **Meta Relevance**: Performance vs average
- **Balance Metrics**: Statistical weapon balancing

### Match Analytics
- **Intensity Score**: Combat frequency metric
- **Balance Rating**: Team fairness assessment
- **Completion Rate**: Match finish percentage
- **Engagement Score**: Player participation metric

### Leaderboards
- **Kill Leaderboard**: Top players by eliminations
- **Score Leaderboard**: Top players by points
- **Weapon Leaderboards**: Top weapons by various metrics
- **Regional Rankings**: Geographic performance tracking

## Event Processing

### Combat Events
```json
{
  "eventId": "evt_123456",
  "eventType": "kill",
  "playerId": "player_001",
  "targetId": "player_002",
  "weaponId": "weapon_ak47",
  "damage": 85,
  "matchId": "match_abc123",
  "position": {"x": 123.45, "y": 67.89, "z": 10.0},
  "timestamp": "2024-12-27T16:00:00Z"
}
```

### Event Types
- `kill` - Player elimination
- `death` - Player death
- `damage` - Damage dealt
- `assist` - Kill assistance
- `objective` - Objective completion
- `match_start` - Match begin
- `match_end` - Match completion

### Real-time Processing
- **Kafka Streaming**: Event ingestion and distribution
- **Redis Queue**: Real-time event processing
- **Batch Updates**: Periodic statistics aggregation
- **Cache Invalidation**: Hot data refresh

## Monitoring

### Key Metrics
```
combat_stats_retrieved_total         - Statistics retrieval rate
combat_stats_updated_total           - Statistics update rate
combat_events_recorded_total         - Combat event processing rate
combat_stats_request_duration_seconds - API response latency
combat_stats_active_connections       - Active user connections
combat_stats_errors_total            - Error rate tracking
```

### Health Checks
- **Readiness**: Database and Redis connectivity
- **Liveness**: Event processing queue health
- **Custom**: Statistics calculation accuracy
- **Performance**: Cache hit rates and latency

## Anti-Cheat Integration

### Statistical Analysis
- **Anomaly Detection**: Unusual kill/death ratios
- **Pattern Recognition**: Aim-bot detection via accuracy patterns
- **Timing Analysis**: Human vs automated reaction times
- **Consistency Checks**: Performance stability validation

### Validation Metrics
- **Accuracy Thresholds**: Maximum allowed accuracy percentages
- **Kill Speed Limits**: Minimum time between eliminations
- **Position Validation**: Teleportation and speed hack detection
- **Weapon Balance**: Statistical weapon effectiveness monitoring

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

# Performance benchmarks
go test -bench=. ./...

# Load testing
go test -tags=load ./...
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

## Integration Points

### Event-Driven Architecture
- **Kafka Topics**: `combat.events`, `stats.updates`, `leaderboard.changes`
- **Event Types**: Player stats updated, weapon stats changed, match completed
- **Consumer Groups**: stats-aggregator, leaderboard-updater, analytics-processor

### External Services
- **Database**: PostgreSQL for persistent statistics storage
- **Cache**: Redis for hot statistics and leaderboards
- **Message Queue**: Kafka for real-time event streaming
- **Monitoring**: Prometheus for metrics collection
- **Authentication**: JWT token validation

### Game Integration
- **Match Service**: Real-time combat event streaming
- **Player Service**: Statistics updates and achievements
- **Inventory Service**: Weapon usage tracking
- **Achievement Service**: Statistics-based unlocks
- **Ranking Service**: Leaderboard calculations

## Contributing

1. Follow Go best practices and SOLID principles
2. Write comprehensive tests for statistics calculations
3. Update API documentation for endpoint changes
4. Ensure performance benchmarks pass for new features
5. Implement proper error handling and logging

## License

Apache License 2.0
