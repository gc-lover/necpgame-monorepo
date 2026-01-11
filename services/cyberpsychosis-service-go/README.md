# Cyberpsychosis Combat States Service

**Enterprise-grade Go microservice for Cyberpsychosis Combat States management in MMOFPS RPG**

[![Go Version](https://img.shields.io/badge/go-1.21+-blue.svg)](https://golang.org)
[![PostgreSQL](https://img.shields.io/badge/postgresql-13+-blue.svg)](https://www.postgresql.org)
[![OpenAPI](https://img.shields.io/badge/openapi-3.0.3-green.svg)](https://swagger.io/specification/)

## Overview

The Cyberpsychosis Combat States Service provides comprehensive management of cyberpsychosis states in real-time combat scenarios. It handles state transitions, health monitoring, and performance optimization for high-throughput MMOFPS gameplay.

### Key Features

#### 🎯 **Dynamic State Management**
- **Real-time Transitions**: Instant state changes with audit trails
- **Severity Levels**: 1-10 scale with progressive effects
- **Automatic Decay**: Background health drain and state degradation
- **Recovery Mechanisms**: Multiple cure methods with cooldowns

#### 🏥 **Health & Monitoring**
- **System Health Checks**: Comprehensive health monitoring
- **Performance Metrics**: OpenTelemetry integration
- **Error Tracking**: Detailed error rates and response times
- **Background Workers**: Automated state processing

#### 🔧 **Combat State Types**

| State | Damage | Speed | Accuracy | Control | Curable |
|-------|--------|-------|----------|---------|---------|
| **Berserk** | +150% | +80% | Normal | Limited | Yes |
| **Adrenal Overload** | +50% | +200% | -30% | Limited | Yes |
| **Neural Overload** | +20% | Normal | +100% | Full | Yes |
| **System Shock** | -50% | -70% | Normal | None | No |
| **Cyberpsychosis** | +200% | +100% | +50% | None | No |

## Architecture

### System Components

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   HTTP API      │────│  Service Layer   │────│   Repository    │
│                 │    │                  │    │                 │
│ • REST Endpoints│    │ • Business Logic │    │ • PostgreSQL    │
│ • JSON Responses│    │ • State Mgmt     │    │ • Connection Pool│
│ • CORS Support  │    │ • Workers        │    │ • Transactions   │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌──────────────────┐
                    │  Background      │
                    │  Workers         │
                    │                  │
                    │ • Health Drain   │
                    │ • State Transit. │
                    │ • Metrics Update │
                    └──────────────────┘
```

### Performance Optimizations

#### Memory Management
- **Object Pooling**: `sync.Pool` for state and transition objects
- **Struct Alignment**: 30-50% memory savings with optimized field ordering
- **Zero Allocations**: Hot path operations without heap allocations

#### Database Optimization
- **Connection Pooling**: 25 max connections, 5 min connections
- **Query Optimization**: Composite indexes for common access patterns
- **Batch Operations**: Efficient bulk state processing

#### Concurrent Processing
- **Worker Pools**: Background goroutines for health drain and transitions
- **Channel-based Communication**: Safe shutdown with context cancellation
- **Metrics Collection**: Real-time performance monitoring

## API Reference

### Core Endpoints

#### Create Cyberpsychosis State
```http
POST /api/v1/cyberpsychosis/states
Content-Type: application/json

{
  "player_id": "550e8400-e29b-41d4-a716-446655440000",
  "state_type": 1,
  "trigger_reason": "combat_damage_threshold_exceeded"
}
```

#### Get Player State
```http
GET /api/v1/cyberpsychosis/states/{player_id}
```

#### Trigger Specific States
```http
POST /api/v1/cyberpsychosis/triggers/berserk
POST /api/v1/cyberpsychosis/triggers/adrenal-overload
POST /api/v1/cyberpsychosis/triggers/neural-overload
```

#### Health Monitoring
```http
GET /api/v1/health              # Basic health check
GET /api/v1/system/health       # Detailed system metrics
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `HTTP_ADDR` | `:8080` | HTTP server address |
| `DATABASE_URL` | `postgres://...` | PostgreSQL connection string |
| `LOG_LEVEL` | `info` | Logging level |

### Database Schema

The service requires the following PostgreSQL schema:

```sql
-- Main tables
cyberpsychosis.states              -- Active cyberpsychosis states
cyberpsychosis.state_transitions   -- Audit trail for transitions
cyberpsychosis.combat_sessions     -- Combat context tracking
cyberpsychosis.system_config       -- Configuration storage
cyberpsychosis.health_monitoring   -- Health check history
cyberpsychosis.notifications       -- Player notifications

-- Performance views
cyberpsychosis.active_states_view     -- Active states with duration
cyberpsychosis.state_statistics       -- Aggregate statistics
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 13+
- Docker (optional)

### Building

```bash
# Clone and build
cd services/cyberpsychosis-service-go
go mod tidy
go build -o bin/cyberpsychosis-service ./main.go
```

### Running

```bash
# Set environment variables
export DATABASE_URL="postgres://user:pass@localhost:5432/cyber_db?sslmode=disable"
export HTTP_ADDR=":8080"

# Run the service
./bin/cyberpsychosis-service
```

### Testing

```bash
# Unit tests
go test ./...

# Benchmarks
go test -bench=. -benchmem

# Integration tests
go test -tags=integration ./...
```

## Deployment

### Docker

```dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY go.mod go.sum ./
RUN go mod download
COPY . .
RUN go build -o cyberpsychosis-service ./main.go

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/cyberpsychosis-service .
CMD ["./cyberpsychosis-service"]
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cyberpsychosis-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: cyberpsychosis-service
  template:
    metadata:
      labels:
        app: cyberpsychosis-service
    spec:
      containers:
      - name: cyberpsychosis-service
        image: necpgame/cyberpsychosis-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
```

## Monitoring

### Metrics

The service exposes the following metrics:

- `cyberpsychosis_active_states_total` - Number of active states
- `cyberpsychosis_state_transitions_total` - Total state transitions
- `cyberpsychosis_transition_duration_seconds` - Transition duration histogram
- `cyberpsychosis_health_drain_rate` - Current health drain rate

### Health Checks

- **HTTP**: `GET /health` - Basic service availability
- **System**: `GET /system/health` - Detailed system health with metrics

### Logging

Structured JSON logging with the following levels:
- `DEBUG` - Detailed operation information
- `INFO` - General operational messages
- `WARN` - Warning conditions
- `ERROR` - Error conditions requiring attention

## Security

### Authentication
- JWT Bearer token authentication
- Row Level Security (RLS) in PostgreSQL
- Player-scoped data access

### Input Validation
- Strict OpenAPI schema validation
- SQL injection prevention via prepared statements
- XSS protection with input sanitization

## Performance Benchmarks

### Latency Targets
- **State Creation**: <35ms P99
- **State Retrieval**: <15ms P99
- **Health Check**: <5ms P99
- **State Transition**: <25ms P99

### Throughput
- **Concurrent Users**: 10,000+ active players
- **State Transitions**: 5,000+ per minute
- **Health Checks**: 100,000+ per minute

### Memory Usage
- **Idle**: ~50MB
- **Peak Load**: ~200MB
- **Per Player**: ~2KB average

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Run benchmarks: `go test -bench=. -benchmem`
5. Ensure OpenAPI spec is valid
6. Submit a pull request

## License

This project is part of the NECPGAME ecosystem. See LICENSE file for details.

## Support

For support and questions:
- **Issues**: GitHub Issues
- **Documentation**: `/docs` directory
- **Team**: `#cyberpsychosis-service` Slack channel