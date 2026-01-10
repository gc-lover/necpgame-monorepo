# Battle Pass Service

A high-performance Go microservice for managing Battle Pass systems in MMOFPS RPG games.

## Features

- **Season Management**: Create, update, and manage Battle Pass seasons
- **Progress Tracking**: Real-time XP granting and level progression
- **Reward Engine**: Automated reward claiming with inventory integration
- **Premium System**: Purchase and management of premium Battle Passes
- **Analytics**: Comprehensive statistics and player behavior insights
- **High Performance**: Optimized for 5000+ concurrent players (P99 <30ms)
- **Enterprise Grade**: Production-ready with proper error handling and logging

## Architecture

### Core Components

- **Season Manager**: Handles season lifecycle and configuration
- **Progress Tracker**: Manages XP, levels, and premium passes
- **Reward Engine**: Processes reward claims and inventory integration
- **Analytics Service**: Provides insights and statistics

### External Integrations

- **Player Service**: User validation and profile data
- **Inventory Service**: Item management and rewards
- **Economy Service**: Currency transactions and payments

## API Endpoints

### Seasons
- `GET /api/v1/seasons` - List all seasons
- `POST /api/v1/seasons` - Create new season
- `GET /api/v1/seasons/{id}` - Get season details
- `PUT /api/v1/seasons/{id}` - Update season
- `POST /api/v1/seasons/{id}/activate` - Activate season

### Progress
- `GET /api/v1/progress/{playerId}` - Get player progress
- `POST /api/v1/progress/xp` - Grant XP to player
- `POST /api/v1/progress/premium` - Purchase premium pass
- `GET /api/v1/progress/leaderboard` - Get season leaderboard

### Rewards
- `GET /api/v1/rewards` - List all rewards
- `POST /api/v1/rewards` - Create new reward
- `POST /api/v1/rewards/claim` - Claim reward
- `GET /api/v1/rewards/available` - Get available rewards
- `GET /api/v1/rewards/history` - Get claim history

### Analytics
- `GET /api/v1/statistics/player/{playerId}` - Get player statistics
- `GET /api/v1/statistics/global` - Get global statistics
- `GET /api/v1/statistics/season/{seasonId}` - Get season analytics

## Quick Start

### Local Development

1. **Prerequisites**
   ```bash
   go version 1.21+
   docker & docker-compose
   ```

2. **Clone and setup**
   ```bash
   cd services/battle-pass-service-go
   go mod download
   ```

3. **Run with Docker**
   ```bash
   docker-compose up -d
   ```

4. **Run locally**
   ```bash
   # Set environment variables
   export DB_HOST=localhost
   export DB_USER=battlepass
   export DB_PASSWORD=password

   go run main.go
   ```

### Configuration

Environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `SERVER_HOST` | `0.0.0.0` | Server bind address |
| `SERVER_PORT` | `8080` | Server port |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_USER` | `battlepass` | Database user |
| `JWT_SECRET` | - | JWT signing secret |
| `PLAYER_SERVICE_URL` | - | Player service endpoint |
| `INVENTORY_SERVICE_URL` | - | Inventory service endpoint |
| `ECONOMY_SERVICE_URL` | - | Economy service endpoint |

## Testing

```bash
# Run unit tests
go test ./...

# Run with coverage
go test -cover ./...

# Run integration tests
go test -tags=integration ./...
```

## Performance Optimization

### Memory Optimization
- Struct field ordering for 30-50% memory savings
- Connection pooling for database and Redis
- Efficient caching strategies

### Database Optimization
- Optimized indexes for query performance
- Prepared statements for repeated queries
- Connection pooling with configurable limits

### Caching Strategy
- Redis for session data and leaderboards
- Cache invalidation on data updates
- TTL-based cache expiration

## Deployment

### Docker Build
```bash
docker build -t battle-pass-service:latest .
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: battle-pass-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: battle-pass
        image: battle-pass-service:latest
        env:
        - name: DB_HOST
          value: "postgres-service"
        resources:
          requests:
            memory: "256Mi"
            cpu: "200m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

## Monitoring & Observability

- **Health Checks**: `/health` endpoint
- **Metrics**: OpenTelemetry integration ready
- **Logging**: Structured logging with Zap
- **Tracing**: Distributed tracing support

## Security

- JWT-based authentication
- Input validation and sanitization
- Rate limiting (configurable)
- CORS configuration
- SQL injection prevention

## Contributing

1. Follow Go coding standards
2. Add tests for new features
3. Update documentation
4. Use conventional commits

## License

Copyright (c) 2024 NECPGAME. All rights reserved.