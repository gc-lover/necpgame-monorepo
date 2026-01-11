# Reputation Service - Dynamic Reputation System

## Overview

The Reputation Service implements advanced reputation decay and recovery mechanics with adaptive algorithms that respond to player behavior and game events.

## Key Features

- **Dynamic Decay**: Time-based reputation degradation with contextual factors
- **Adaptive Recovery**: Behavior-driven reputation restoration algorithms
- **Multi-dimensional Reputation**: Player, faction, and regional reputation tracking
- **Real-time Updates**: Event-driven reputation changes with immediate effects
- **Analytics Integration**: Reputation trends and player behavior insights
- **Performance Optimized**: MMOFPS-grade performance with <15ms P99 latency

## Domain Purpose

The Reputation Service manages the social fabric of NECPGAME, enabling dynamic player relationships that evolve based on actions, time, and social context.

## Performance Targets

- **P99 Latency**: <15ms for reputation queries, <25ms for updates
- **Memory**: <50KB per active reputation profile
- **Concurrent updates**: 100,000+ simultaneous reputation changes
- **Decay calculations**: Real-time (<5ms per player)

## Architecture

### Core Components

#### Reputation Engine
- **Decay Algorithm**: Time-based reputation degradation with activity modifiers
- **Recovery Algorithm**: Context-aware reputation restoration
- **Multi-dimensional Tracking**: Separate reputation scores for different contexts
- **Event-driven Updates**: Real-time reputation changes based on game events

#### Reputation Registry
- **Profile Management**: Comprehensive reputation profiles per player
- **Change Tracking**: Complete audit trail of reputation modifications
- **Statistics Aggregation**: Real-time analytics and trend analysis
- **Rule Management**: Configurable decay and recovery parameters

### Data Models

#### PlayerReputation
```go
type PlayerReputation struct {
    PlayerID       string  `json:"player_id"`
    ReputationType string  `json:"reputation_type"` // player, faction, region, global
    CurrentValue   float64 `json:"current_value"`
    BaseValue      float64 `json:"base_value"`
    DecayRate      float64 `json:"decay_rate"`
    NextDecayUnix  int64   `json:"next_decay_unix"`
    // ... optimized struct alignment
}
```

#### DecayRule
```go
type DecayRule struct {
    ReputationType     string  `json:"reputation_type"`
    BaseDecayRate      float64 `json:"base_decay_rate"`
    DecayIntervalHours int     `json:"decay_interval_hours"`
    ActivityMultiplier float64 `json:"activity_multiplier"`
    FactionModifier    float64 `json:"faction_modifier"`
}
```

## API Endpoints

### Reputation Management
- `GET /players/{id}/reputation` - Get player reputation profile
- `PUT /players/{id}/reputation` - Update reputation scores
- `GET /players/{id}/reputation/history` - Get reputation change history

### Decay & Recovery
- `POST /players/{id}/reputation/decay` - Trigger reputation decay
- `POST /players/{id}/reputation/recovery` - Trigger reputation recovery

### Configuration
- `GET /config/decay-rules` - Get decay configuration
- `PUT /config/decay-rules` - Update decay rules

### Analytics
- `GET /analytics/reputation-stats` - Get reputation system statistics

## Database Schema

### Key Tables
- `reputation.player_reputations` - Player reputation scores
- `reputation.reputation_changes` - Audit trail of changes
- `reputation.decay_rules` - Configurable decay parameters
- `reputation.recovery_events` - Recovery application tracking
- `reputation.player_stats` - Aggregated player statistics
- `reputation.global_stats` - System-wide analytics

### Performance Optimizations
- Composite indexes for query patterns
- Partial indexes for active records
- JSONB storage for flexible status tracking
- Time-based partitioning for historical data

## Algorithms

### Decay Algorithm
```go
// Base decay calculation
decayAmount = baseValue * decayRate * (intervalHours / 24)

// Activity modifier
if player.isActive() {
    decayAmount *= activityMultiplier // Usually < 1.0
} else {
    decayAmount *= 2.0 // Inactive players decay faster
}

// Faction modifier
decayAmount *= factionModifier // Ally < 1.0, Enemy > 1.0
```

### Recovery Algorithm
```go
// Base recovery amount
recoveryAmount = baseRecovery * typeModifier

// Activity bonus
if player.isActive() {
    recoveryAmount *= 1.2
}

// Context modifiers
recoveryAmount *= difficultyModifier
recoveryAmount *= streakModifier

// Apply to lowest reputations first
```

## Configuration

### Environment Variables
```bash
REPUTATION_DB_HOST=localhost
REPUTATION_DB_PORT=5432
REPUTATION_DB_NAME=necpgame
REPUTATION_REDIS_URL=redis://localhost:6379
REPUTATION_METRICS_PORT=9090
```

### Decay Configuration
```yaml
decay:
  defaultRate: 0.05  # 5% per day
  intervalHours: 24  # Check every 24 hours
  maxPerDay: 50.0    # Maximum decay per day
  minReputation: -1000.0
  maxReputation: 1000.0

recovery:
  cooldownHours: 6
  maxPerDay: 100.0
  baseAmount: 25.0

modifiers:
  activityActive: 0.5     # Active players decay slower
  activityInactive: 2.0   # Inactive players decay faster
  factionAlly: 0.5        # Decay slower with allies
  factionEnemy: 2.0       # Decay faster with enemies
```

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker & Docker Compose

### Local Setup
```bash
# Clone and setup
cd services/reputation-service-go
go mod download

# Run database migrations
liquibase update

# Start service
go run cmd/server/main.go
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Performance tests
go test -bench=. -benchmem ./...
```

## Deployment

### Docker Build
```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o reputation ./cmd/server

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from:builder /app/reputation .
CMD ["./reputation"]
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: reputation
spec:
  replicas: 3
  selector:
    matchLabels:
      app: reputation
  template:
    metadata:
      labels:
        app: reputation
    spec:
      containers:
      - name: reputation
        image: necpgame/reputation:latest
        ports:
        - containerPort: 8080
        env:
        - name: REPUTATION_DB_HOST
          value: "postgres-service"
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "256Mi"
            cpu: "200m"
```

## Monitoring

### Key Metrics
- **Decay success rate**: >99%
- **Recovery cooldown compliance**: >95%
- **Average reputation change**: Tracked per player type
- **System performance**: P99 latency <15ms

### Health Checks
- Database connectivity
- Redis availability
- Decay processing status
- Recovery queue length

## Security

### Anti-cheat Measures
- Reputation manipulation detection
- Abnormal change rate monitoring
- Player behavior pattern analysis
- Rate limiting and validation

### Data Protection
- Encrypted sensitive reputation data
- Audit logging for all changes
- GDPR compliance for player data
- Secure API authentication

## Related Services

### Dependencies
- **Player Service** - Player identification and authentication
- **Game Events Service** - Reputation change event triggers
- **Quest Service** - Reputation rewards and penalties
- **Faction Service** - Inter-faction reputation management

### Integration Points
- **Quest System** - Reputation-based quest availability
- **Trading System** - Reputation modifiers on pricing
- **Social System** - Reputation-based social interactions
- **Anti-cheat System** - Reputation anomaly detection

## Roadmap

### Phase 1 (Current)
- Basic decay and recovery mechanics
- Multi-dimensional reputation tracking
- Real-time reputation updates

### Phase 2 (Next)
- Advanced analytics dashboard
- AI-driven decay rate adjustment
- Cross-service reputation sharing

### Phase 3 (Future)
- Reputation marketplace
- Dynamic reputation currencies
- Machine learning-based predictions

## Contributing

### Code Standards
- Struct alignment optimization for memory efficiency
- Comprehensive error handling and logging
- Extensive unit and integration test coverage
- Performance benchmarking for all operations

### Review Process
- Architecture review for algorithm changes
- Performance impact assessment
- Security audit for reputation manipulation
- Database migration review for schema changes

## License

MIT License - see LICENSE file for details.