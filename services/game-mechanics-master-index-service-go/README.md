# Game Mechanics Master Index Service

**Enterprise-Grade Game Mechanics Registry API for NECPGAME**

[![Go Version](https://img.shields.io/badge/go-1.25.3-blue.svg)](https://golang.org)
[![PostgreSQL](https://img.shields.io/badge/postgres-15+-blue.svg)](https://www.postgresql.org)
[![Redis](https://img.shields.io/badge/redis-7+-red.svg)](https://redis.io)

## Overview

The Game Mechanics Master Index Service serves as the central nervous system for NECPGAME, providing comprehensive management of all game mechanics in the ecosystem. This service enables dynamic mechanic discovery, dependency management, health monitoring, and configuration management.

## Key Features

### 🎯 **Mechanic Registry**
- Centralized registration and discovery of game mechanics
- Real-time status tracking and health monitoring
- Dynamic configuration management
- Service endpoint resolution

### 🔗 **Dependency Management**
- Automatic resolution of mechanic interdependencies
- Validation of circular dependencies
- Hard and soft dependency types
- Dependency graph analysis

### 📊 **Health Monitoring**
- Real-time health status for all mechanics
- Performance metrics collection
- Automated health checks
- System-wide health scoring

### ⚙️ **Configuration Management**
- Dynamic configuration for all mechanics
- Versioned configuration history
- Runtime configuration updates
- Configuration validation

## Performance Targets

- **P99 Latency**: <10ms for registry operations
- **Memory**: <50KB per active mechanic registry
- **Concurrent Connections**: 100,000+ simultaneous connections
- **Registry Operations**: <1ms response time

## Architecture

### Components

```
game-mechanics-master-index-service-go/
├── internal/
│   ├── handlers/          # HTTP API handlers
│   ├── models/           # Data models and types
│   ├── repository/       # Database abstraction layer
│   └── service/          # Business logic layer
├── pkg/api/              # Generated OpenAPI client/server
├── proto/openapi/        # OpenAPI specifications
└── main.go              # Application entry point
```

### Data Flow

```
Client Request → HTTP Handler → Service Layer → Repository → PostgreSQL
                                    ↓
                              Health Monitoring
                                    ↓
                              Metrics Collection
```

## Database Schema

### Schemas and Tables

- **`game_mechanics.mechanics`**: Core mechanic definitions
- **`game_mechanics.dependencies`**: Mechanic dependency relationships
- **`game_mechanics.configurations`**: Mechanic configurations
- **`game_mechanics.status`**: Health and status information

### Indexes

Optimized indexes for MMOFPS performance:
- Partial indexes for active mechanics
- Composite indexes for common query patterns
- JSONB indexes for configuration queries

## API Endpoints

### Core Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health` | Service health check |
| GET | `/api/v1/mechanics` | List all mechanics |
| POST | `/api/v1/mechanics` | Register new mechanic |
| GET | `/api/v1/mechanics/{id}` | Get mechanic by ID |
| PUT | `/api/v1/mechanics/{id}` | Update mechanic |
| DELETE | `/api/v1/mechanics/{id}` | Delete mechanic |

### Advanced Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/mechanics/{id}/dependencies` | Get mechanic dependencies |
| POST | `/api/v1/mechanics/{id}/dependencies` | Add dependency |
| GET | `/api/v1/mechanics/{id}/config` | Get mechanic configuration |
| PUT | `/api/v1/mechanics/{id}/config` | Update configuration |
| GET | `/api/v1/system/health` | Get system health |
| GET | `/api/v1/system/status` | Get system status |

## Configuration

### Environment Variables

```bash
# Database
DATABASE_URL=postgres://user:pass@localhost:5432/necpgame?sslmode=disable

# Redis (optional)
REDIS_URL=redis://localhost:6379

# HTTP Server
HTTP_ADDR=:8080

# Logging
LOG_LEVEL=info
```

### Database Setup

Run the Liquibase migration:

```bash
liquibase --changeLogFile=infrastructure/liquibase/schema/V1_51__game_mechanics_master_index.sql update
```

## Development

### Prerequisites

- Go 1.25.3+
- PostgreSQL 15+
- Redis 7+ (optional)
- Protocol Buffers compiler

### Building

```bash
cd services/game-mechanics-master-index-service-go
go mod download
go build -o bin/game-mechanics-master-index .
```

### Running

```bash
# With default configuration
./bin/game-mechanics-master-index

# With custom config
DATABASE_URL="postgres://..." REDIS_URL="redis://..." ./bin/game-mechanics-master-index
```

### Testing

```bash
# Run unit tests
go test ./...

# Run integration tests
go test -tags=integration ./...

# Run with race detector
go test -race ./...
```

## Deployment

### Docker

```dockerfile
FROM golang:1.25.3-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o main .

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/main .
CMD ["./main"]
```

### Kubernetes

See `k8s/` directory for deployment manifests.

## Monitoring

### Metrics

The service exposes Prometheus metrics at `/metrics`:

- `game_mechanics_total`: Total registered mechanics
- `game_mechanics_active`: Active mechanics count
- `game_mechanics_registry_size`: In-memory registry size
- `game_mechanics_request_duration`: Request duration histogram
- `game_mechanics_cache_hit_rate`: Cache hit rate

### Health Checks

- **HTTP**: `/health` - Basic health check
- **System**: `/api/v1/system/health` - Detailed system health
- **Status**: `/api/v1/system/status` - Quick status overview

## Security

### Authentication

- JWT Bearer token authentication
- Admin role required for write operations
- Service-to-service authentication support

### Authorization

- Row Level Security (RLS) policies
- Role-based access control
- Audit logging for sensitive operations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Run `go test ./...`
5. Submit a pull request

## License

MIT License - see LICENSE file for details.

## Related Services

- **combat-service-go**: Combat mechanics implementation
- **economy-service-go**: Economy mechanics implementation
- **social-service-go**: Social mechanics implementation
- **quest-service-go**: Quest mechanics implementation
- **world-simulation-python**: World simulation mechanics

## Issue Tracking

- **GitHub Issues**: Bug reports and feature requests
- **Project Board**: Task tracking and workflow management
- **Issue**: #2176 - Game Mechanics Systems Master Index