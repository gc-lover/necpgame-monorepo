# Gameplay Restricted Modes Service

<!-- Issue: #1499 -->

## Overview

The Gameplay Restricted Modes Service implements the backend for Cyberpunk 2077's challenging gameplay modes system. This enterprise-grade microservice provides APIs for managing restricted gameplay modes like Ironman, Hardcore, Solo Challenge, and No-Death runs.

## Features

### Core Functionality
- **Ironman Mode**: Permanent death - character deletion on death
- **Hardcore Mode**: Limited resources and equipment access
- **Solo Challenge**: No group assistance required
- **No-Death Runs**: Complete content without dying
- **Session Management**: Track active restricted mode sessions
- **Progress Tracking**: Monitor player progress and statistics
- **Leaderboard System**: Rankings for different modes and timeframes

### Enterprise Features
- **JWT Authentication**: Secure API access with role-based permissions
- **PostgreSQL Integration**: Robust data persistence with connection pooling
- **Redis Caching**: Session management and performance optimization
- **Structured Logging**: ZeroLog for comprehensive observability
- **Prometheus Metrics**: Service monitoring and alerting
- **Graceful Shutdown**: Clean service termination
- **OpenAPI 3.0**: Auto-generated API documentation and client SDKs

## Architecture

### Tech Stack
- **Language**: Go 1.21+
- **Framework**: Chi Router v5
- **API Generation**: ogen (90% faster than oapi-codegen)
- **Database**: PostgreSQL with pgx driver
- **Cache**: Redis for sessions and performance
- **Logging**: ZeroLog for structured logging
- **Metrics**: Prometheus client
- **Security**: JWT, CSRF protection, security headers

### Service Structure
```
services/gameplay-restricted-modes-service-go/
├── main.go                    # Application entry point
├── go.mod                     # Dependencies
├── go.sum                     # Dependency checksums
├── Makefile                   # Build automation
├── Dockerfile                 # Container definition
├── openapi-bundled.yaml       # API specification
├── pkg/api/                   # Generated API code (ogen)
├── internal/
│   ├── auth/                  # JWT authentication
│   ├── config/                # Configuration management
│   ├── database/              # PostgreSQL + Redis operations
│   ├── handlers/              # HTTP request handlers
│   ├── monitoring/            # Logging and metrics
│   └── middleware/security/   # Security middleware
```

## Performance Targets

- **P99 Latency**: <50ms for all endpoints
- **Throughput**: 10k+ concurrent players
- **Memory**: <50KB per active session
- **Database**: <5ms query response time
- **Zero Allocations**: Hot paths optimized

## API Endpoints

### Restricted Modes Management
- `GET /gameplay/restricted-modes/available` - List available modes
- `GET /gameplay/restricted-modes/status` - Player's active modes
- `POST /gameplay/restricted-modes/select` - Activate a mode
- `POST /gameplay/restricted-modes/{sessionId}/complete` - Complete session
- `POST /gameplay/restricted-modes/{sessionId}/fail` - Fail session

### Leaderboards
- `GET /gameplay/restricted-modes/leaderboard` - Mode rankings

### Health & Monitoring
- `GET /health` - Health check endpoint
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics (basic auth required)

## Quick Start

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker (optional)

### Local Development

1. **Clone and setup**:
```bash
cd services/gameplay-restricted-modes-service-go
go mod tidy
```

2. **Environment variables**:
```bash
export ENVIRONMENT=development
export SERVER_PORT=8080
export DATABASE_URL="postgres://user:pass@localhost:5432/gameplay_db?sslmode=disable"
export REDIS_ADDR="localhost:6379"
export JWT_SECRET="your-super-secret-key"
export LOG_LEVEL=debug
```

3. **Run the service**:
```bash
make run
```

4. **Health check**:
```bash
curl http://localhost:8080/health
```

### Docker Development

```bash
# Build and run
make docker-run

# Or using docker-compose
make docker-compose-up
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `ENVIRONMENT` | `development` | Application environment |
| `SERVER_PORT` | `8080` | HTTP server port |
| `DATABASE_URL` | - | PostgreSQL connection string |
| `REDIS_ADDR` | `localhost:6379` | Redis server address |
| `JWT_SECRET` | - | JWT signing secret |
| `LOG_LEVEL` | `info` | Logging level (debug/info/warn/error) |
| `METRICS_USERNAME` | `metrics` | Metrics endpoint username |
| `METRICS_PASSWORD` | `secure-password` | Metrics endpoint password |

### Database Schema

The service requires these tables in the `gameplay` schema:

```sql
-- Player restricted modes status
CREATE TABLE gameplay.player_restricted_modes (
    player_id UUID NOT NULL,
    mode_type VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT FALSE,
    activated_at TIMESTAMP,
    deactivated_at TIMESTAMP,
    total_sessions INTEGER DEFAULT 0,
    successful_runs INTEGER DEFAULT 0,
    failed_runs INTEGER DEFAULT 0,
    best_score INTEGER,
    total_time_played INTERVAL DEFAULT '0s',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (player_id, mode_type)
);

-- Active restricted mode sessions
CREATE TABLE gameplay.restricted_mode_sessions (
    session_id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    character_id UUID NOT NULL,
    mode_type VARCHAR(50) NOT NULL,
    content_type VARCHAR(50),
    difficulty VARCHAR(20),
    started_at TIMESTAMP DEFAULT NOW(),
    progress DECIMAL(3,2) DEFAULT 0.0,
    is_active BOOLEAN DEFAULT TRUE,
    current_score INTEGER DEFAULT 0,
    time_elapsed INTERVAL DEFAULT '0s',
    restrictions TEXT[],
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Completed sessions for history
CREATE TABLE gameplay.completed_restricted_mode_sessions (
    session_id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    mode_type VARCHAR(50) NOT NULL,
    completed_at TIMESTAMP DEFAULT NOW(),
    success BOOLEAN DEFAULT FALSE,
    completion_time INTERVAL,
    final_score INTEGER,
    rank_achieved INTEGER,
    rewards TEXT[],
    created_at TIMESTAMP DEFAULT NOW()
);
```

## Development

### Building
```bash
# Build for current platform
make build

# Build for Linux
make build-linux

# Run tests
make test

# Run linter
make lint

# Security scan
make security-scan
```

### Code Generation
```bash
# Regenerate API code after OpenAPI changes
make generate
```

### Testing
```bash
# Unit tests
make test-unit

# Integration tests
make test-integration

# Benchmarks
make bench
```

### Profiling
```bash
# Start profiling server
make profile

# Then use pprof
go tool pprof http://localhost:6555/debug/pprof/profile
```

## Deployment

### Docker Production
```bash
# Multi-platform build
make docker-build-multi

# Production run
docker run -d \
  --name gameplay-restricted-modes \
  -p 8080:8080 \
  --env-file production.env \
  --restart unless-stopped \
  gameplay-restricted-modes-service:latest
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gameplay-restricted-modes
spec:
  replicas: 3
  selector:
    matchLabels:
      app: gameplay-restricted-modes
  template:
    metadata:
      labels:
        app: gameplay-restricted-modes
    spec:
      containers:
      - name: gameplay-restricted-modes
        image: gameplay-restricted-modes-service:latest
        ports:
        - containerPort: 8080
        envFrom:
        - configMapRef:
            name: gameplay-restricted-modes-config
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Monitoring

### Health Checks
- **Health**: `GET /health` - Database connectivity
- **Readiness**: `GET /ready` - Full service readiness
- **Metrics**: `GET /metrics` - Prometheus format (basic auth)

### Logging
Structured JSON logs with service context:
```json
{
  "level": "info",
  "service": "gameplay-restricted-modes",
  "timestamp": "2024-12-24T08:00:00Z",
  "message": "Player activated Ironman mode",
  "player_id": "uuid",
  "mode_type": "ironman"
}
```

### Metrics
- Request count, duration, error rate
- Database connection pool stats
- Cache hit/miss ratios
- Active sessions count

## Security

### Authentication
- JWT-based authentication
- Role-based access control
- Token refresh mechanism

### Authorization
- Permission-based endpoint access
- User context propagation
- Session validation

### Security Headers
- Content-Type-Options: nosniff
- X-Frame-Options: DENY
- X-XSS-Protection: 1; mode=block
- Strict-Transport-Security
- Content Security Policy

### CSRF Protection
- CSRF token validation for state-changing operations
- Same-site cookie handling

## API Documentation

### OpenAPI Specification
The service provides auto-generated OpenAPI documentation at runtime. Access the bundled specification:

```bash
curl http://localhost:8080/openapi.yaml
```

### Client SDK Generation
Generate client SDKs using the OpenAPI spec:

```bash
# TypeScript
npx openapi-typescript openapi-bundled.yaml -o client.ts

# Go client
ogen --target client/go --package client --clean openapi-bundled.yaml
```

## Contributing

### Code Standards
- Go 1.21+ required
- `go fmt` for formatting
- `go vet` for static analysis
- `golangci-lint` for comprehensive linting
- Tests required for new features
- Documentation required for public APIs

### Commit Messages
```
[backend] feat: implement restricted modes activation

- Add mode selection endpoint
- Implement eligibility checking
- Add session creation logic
- Update player statistics

Related Issue: #1499
```

## Troubleshooting

### Common Issues

1. **Database connection failed**
   - Check DATABASE_URL format
   - Verify PostgreSQL is running
   - Check network connectivity

2. **Redis connection failed**
   - Verify REDIS_ADDR format
   - Check Redis server status
   - Confirm authentication settings

3. **JWT token invalid**
   - Verify JWT_SECRET is set
   - Check token expiration
   - Validate token format

4. **High latency**
   - Check database query performance
   - Monitor Redis cache hit rate
   - Review connection pool settings

### Logs Analysis
```bash
# View recent errors
docker logs gameplay-restricted-modes 2>&1 | grep ERROR

# Monitor performance
docker logs gameplay-restricted-modes 2>&1 | grep "duration"
```

## License

MIT License - see LICENSE file for details.

## Contact

**Team**: Gameplay Development Team
**Email**: gameplay@necpgame.com
**Slack**: #gameplay-backend

