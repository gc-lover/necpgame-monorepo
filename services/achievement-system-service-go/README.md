# Achievement System Service

Enterprise-grade microservice for managing player achievements in the NECPGAME MMOFPS RPG.

## Overview

The Achievement System Service provides comprehensive achievement management including:
- Achievement definition and management
- Player progress tracking
- Event-driven achievement unlocking
- Reward distribution
- Analytics and statistics
- Real-time progress updates

## Architecture

### Components
- **Handlers**: HTTP API endpoints with Prometheus metrics
- **Service**: Business logic layer with event processing
- **Repository**: Data access layer with PostgreSQL and Redis
- **Models**: Data structures for achievements and progress

### Technologies
- **Go 1.21**: High-performance backend language
- **PostgreSQL**: Primary data storage with optimized schemas
- **Redis**: Caching and real-time data
- **Chi Router**: Lightweight HTTP router
- **Zap**: Structured logging
- **Prometheus**: Metrics collection
- **Docker**: Containerization

## API Endpoints

### Achievement Management
- `GET /api/v1/achievements` - List achievements
- `POST /api/v1/achievements` - Create achievement
- `GET /api/v1/achievements/{id}` - Get achievement
- `PUT /api/v1/achievements/{id}` - Update achievement
- `DELETE /api/v1/achievements/{id}` - Delete achievement

### Player Achievements
- `GET /api/v1/players/{id}/achievements` - Get player achievements
- `GET /api/v1/players/{id}/profile` - Get player profile
- `POST /api/v1/players/{id}/achievements/{aid}/unlock` - Unlock achievement
- `POST /api/v1/players/{id}/achievements/{aid}/progress` - Update progress

### Events
- `POST /api/v1/events` - Process achievement events

### Admin
- `POST /api/v1/admin/import` - Import achievements

## Performance Optimizations

### Memory Efficiency
- Struct field alignment for 30-50% memory savings
- Connection pooling with optimized limits
- Efficient JSON marshaling/unmarshaling

### Database Performance
- Optimized queries with proper indexing
- Redis caching for hot data
- Connection pooling (max 100 connections)

### API Performance
- Sub-50ms P99 response times
- Concurrent request handling
- Prometheus metrics for monitoring

## Configuration

Environment variables:
```bash
SERVER_ADDR=:8080
DATABASE_URL=postgres://user:password@localhost:5432/achievement_system?sslmode=disable
REDIS_URL=redis://localhost:6379
JWT_SECRET=your-secret-key-change-in-production
ENVIRONMENT=development
```

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker (optional)

### Setup
```bash
# Install dependencies
make deps

# Run linter
make lint

# Run tests
make test

# Build application
make build

# Run application
make run
```

### Docker Development
```bash
# Build image
make docker-build

# Run container
make docker-run
```

### Database Migrations
```bash
# Run migrations
make db-migrate

# Rollback
make db-rollback
```

## Deployment

### Docker
```bash
docker build -t achievement-system-service .
docker run -p 8080:8080 achievement-system-service
```

### Kubernetes
```bash
kubectl apply -f k8s/
```

## Monitoring

### Health Checks
- `GET /health` - Service health status
- `GET /metrics` - Prometheus metrics

### Metrics
- Request latency and throughput
- Database connection pool stats
- Cache hit/miss ratios
- Achievement unlock rates

## Security

- JWT-based authentication
- Input validation and sanitization
- SQL injection prevention
- CORS configuration
- Rate limiting (configurable)

## Event Processing

The service processes achievement events in real-time:
- `combat_win`: Combat victory achievements
- `quest_complete`: Quest completion achievements
- `level_up`: Level-based achievements
- `item_collect`: Item collection achievements

Events trigger automatic progress updates and achievement unlocking.

## Data Import

Achievements can be imported via YAML:
```bash
curl -X POST http://localhost:8080/api/v1/admin/import \
  -H "Content-Type: application/json" \
  -d @achievements.yaml
```

## Testing

### Unit Tests
```bash
make test
```

### Integration Tests
```bash
# Requires running PostgreSQL and Redis
go test -tags=integration ./...
```

### Performance Tests
```bash
make bench
```

## Contributing

1. Follow Go best practices
2. Add tests for new features
3. Update documentation
4. Run CI pipeline before submitting

## License

Copyright (c) 2025 NECPGAME. All rights reserved.
