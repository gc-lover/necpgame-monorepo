# Combat Abilities Service Go

**Enterprise-Grade Combat Abilities Service** for NECPGAME MMOFPS RPG.

## Overview

This service manages combat abilities, cooldowns, synergies, and anti-cheat validation for the game's combat system. Built for high-performance MMOFPS gameplay with 1000+ RPS capability.

## Features

- **Real-time Ability Activation**: Hot path optimized for 1000+ RPS
- **Cooldown Management**: Efficient cooldown tracking and validation
- **Ability Synergies**: Combo and synergy calculations
- **Anti-Cheat Validation**: Client-side validation and cheat prevention
- **Memory Pooling**: Zero allocations in hot paths
- **Redis Caching**: High-performance caching layer
- **PostgreSQL Storage**: ACID-compliant data persistence

## Architecture

### Core Components

- **Handlers Layer**: HTTP API endpoints with performance optimizations
- **Service Layer**: Business logic with synergy calculations and validation
- **Repository Layer**: Data access with PostgreSQL integration
- **Cache Layer**: Redis caching with TTL management

### Performance Optimizations

- Memory pooling for response objects
- Context timeouts (<25ms P99 targets)
- Struct field alignment for optimal memory layout
- Zero allocations in hot paths
- 95%+ cache hit rates

## API Endpoints

### Health Check
```
GET /api/v1/health
```

### Ability Management
```
GET  /api/v1/combat/abilities              # List character abilities
POST /api/v1/combat/abilities              # Activate ability
GET  /api/v1/combat/abilities/{id}/cooldown # Get cooldown status
GET  /api/v1/combat/abilities/{id}/synergies # Get synergies
POST /api/v1/combat/abilities/validate      # Validate activation
```

## Configuration

### Environment Variables

- `REDIS_URL`: Redis connection URL
- `DATABASE_URL`: PostgreSQL connection URL
- `JWT_SECRET`: JWT signing secret
- `PORT`: Service port (default: 8084)

### Performance Tuning

- `GOGC`: GC threshold (recommended: 40 for low-latency)
- `GOMAXPROCS`: CPU cores for goroutines

## Development

### Prerequisites

- Go 1.25+
- PostgreSQL
- Redis
- Docker (optional)

### Quick Start

```bash
# Clone and navigate
cd services/combat-abilities-service-go

# Install dependencies
go mod download

# Run tests
make test

# Build
make build

# Run
make run
```

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run
```

## Testing

```bash
# Run unit tests
go test ./...

# Run with coverage
go test -cover ./...

# Run integration tests
go test -tags=integration ./...
```

## Monitoring

### Health Checks

- HTTP: `GET /api/v1/health`
- Metrics: Prometheus `/metrics` (future)
- Readiness: Database connectivity check

### Performance Metrics

- RPS: Target 1000+ for ability activation
- P99 Latency: <25ms for activation endpoints
- Cache Hit Rate: >95%
- Memory Usage: <100MB per instance

## Security

### Anti-Cheat Features

- Client hash validation
- Timestamp verification
- Resource consumption tracking
- Activation pattern analysis

### Authentication

- JWT Bearer token authentication
- Role-based access control
- Request rate limiting

## Deployment

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: combat-abilities-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: combat-abilities
        image: combat-abilities-service-go:latest
        ports:
        - containerPort: 8084
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
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

### Docker Compose

```yaml
version: '3.8'
services:
  combat-abilities:
    image: combat-abilities-service-go
    ports:
      - "8084:8084"
    environment:
      - DATABASE_URL=postgres://user:pass@localhost/db
      - REDIS_URL=redis://localhost:6379
    depends_on:
      - postgres
      - redis
```

## Contributing

1. Follow Go coding standards
2. Add tests for new features
3. Update documentation
4. Ensure performance targets are met

## License

Proprietary - NECPGAME
