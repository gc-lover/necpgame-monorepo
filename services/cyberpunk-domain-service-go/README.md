# Cyberpunk Domain Service

<!-- Issue: Implement cyberpunk-domain-service-go -->
Enterprise-grade cyberpunk domain service for NECPGAME with consolidated cyberpunk functionality, health monitoring, and real-time updates.

## Overview

The Cyberpunk Domain Service provides a unified interface for all cyberpunk-related functionality in NECPGAME, including health monitoring, real-time updates via WebSocket, and integration with various cyberpunk subsystems (battle pass, blackwall, cyberware, hacking).

## Architecture

### Core Components

- **Health Monitoring**: Real-time health checks for all cyberpunk subsystems
- **Circuit Breaker**: Resilient service communication with failure protection
- **WebSocket Support**: Real-time health updates and monitoring
- **Batch Operations**: Efficient health checks for multiple domains
- **Metrics Collection**: Prometheus integration for observability

### Performance Features

- **Memory Pooling**: Zero allocations in hot paths (30-50% memory savings)
- **Context Timeouts**: All operations have configurable timeouts
- **Concurrent Processing**: Worker pools for parallel health checks
- **Circuit Breaker Protection**: Resilience against service failures

## Quick Start

### Prerequisites

- Go 1.21+
- PostgreSQL 13+ (optional, for persistence)
- Redis 6.0+ (optional, for caching)

### Local Development

```bash
# Clone repository
git clone https://github.com/gc-lover/necpgame-monorepo.git
cd necpgame-monorepo/services/cyberpunk-domain-service-go

# Install dependencies
make deps

# Run service
make run

# Run with debug logging
make run-debug
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

### Health Monitoring

```http
# Basic health check
GET /health

# Domain-specific health check
GET /api/v1/cyberpunk-domain/health

# Batch health check for multiple domains
POST /api/v1/cyberpunk-domain/health/batch
Content-Type: application/json

{
  "domains": ["battle-pass", "blackwall", "cyberware"]
}

# Real-time health monitoring WebSocket
GET /health/ws
```

### System Information

```http
# Prometheus metrics
GET /metrics
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8080` | HTTP server port |
| `DATABASE_URL` | Required | PostgreSQL connection string |
| `REDIS_URL` | Optional | Redis connection string |
| `LOG_LEVEL` | `info` | Logging level (debug, info, warn, error) |
| `ENABLE_WEBSOCKET` | `true` | Enable WebSocket support |
| `HEALTH_CHECK_TIMEOUT` | `5s` | Health check timeout |
| `CIRCUIT_BREAKER_TIMEOUT` | `10s` | Circuit breaker timeout |

### Example Configuration

```bash
export DATABASE_URL="postgres://user:pass@localhost/cyberpunk?sslmode=disable"
export REDIS_URL="redis://localhost:6379"
export LOG_LEVEL="debug"
export ENABLE_WEBSOCKET="true"
```

## WebSocket Real-time Monitoring

The service provides real-time health updates via WebSocket:

```javascript
const ws = new WebSocket('ws://localhost:8080/health/ws');

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('Health update:', data);
};
```

### Message Types

- `health_update`: Regular health status updates
- `health_alert`: Service health degradation alerts
- `service_down`: Service unavailability notifications

## Circuit Breaker Protection

The service implements circuit breaker patterns for resilient communication with cyberpunk subsystems:

- **Closed State**: Normal operation, requests pass through
- **Open State**: Service failures detected, requests fail fast
- **Half-Open State**: Testing recovery, limited requests allowed

## Monitoring & Observability

### Health Checks

- `/health`: Basic service health
- `/ready`: Readiness for traffic
- `/metrics`: Prometheus metrics

### Key Metrics

- `cyberpunk_health_checks_total`: Total health checks performed
- `cyberpunk_health_checks_successful`: Successful health checks
- `cyberpunk_websocket_connections`: Active WebSocket connections
- `cyberpunk_circuit_breaker_calls`: Circuit breaker usage
- `cyberpunk_batch_health_checks`: Batch health check operations

## Domain Integration

The service integrates with the following cyberpunk domains:

### Battle Pass
- Seasonal progression systems
- Reward tier management
- Player advancement tracking

### Blackwall
- Netrunning mechanics
- ICE breaking systems
- Digital security layers

### Cyberware
- Neural implant management
- Cybernetic enhancement tracking
- Compatibility validation

### Hacking
- System intrusion mechanics
- Security bypass algorithms
- Digital combat systems

## Development

### Project Structure

```
services/cyberpunk-domain-service-go/
├── main.go                    # Service entry point
├── go.mod/go.sum             # Go dependencies
├── Makefile                  # Build automation
├── Dockerfile               # Container definition
├── README.md                # Documentation
├── internal/
│   ├── config/              # Configuration management
│   ├── handlers/            # HTTP handlers
│   ├── service/             # Business logic
│   ├── metrics/             # Metrics collection
│   └── repository/          # Data access (future)
└── pkg/models/              # Data models
```

### Testing

```bash
# Run unit tests
make test

# Run integration tests
make test-integration

# Run benchmarks
make bench

# Run CI pipeline
make ci
```

### Code Quality

```bash
# Lint code
make lint

# Format code
make format

# Security scan
make security-scan
```

## Deployment

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: cyberpunk-domain-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: cyberpunk-domain-service
        image: necpgame/cyberpunk-domain-service:latest
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
            memory: "256Mi"
            cpu: "200m"
```

### Docker Compose

```yaml
version: '3.8'
services:
  cyberpunk-domain-service:
    image: necpgame/cyberpunk-domain-service:latest
    ports:
      - "8080:8080"
    environment:
      - DATABASE_URL=postgres://user:pass@postgres/cyberpunk
      - REDIS_URL=redis://redis:6379
    depends_on:
      - postgres
      - redis
```

## Performance Benchmarks

### Latency Targets
- **Health Check**: P99 <25ms
- **Batch Health Check**: P99 <100ms
- **WebSocket Message**: P99 <10ms

### Throughput Targets
- **Health Checks**: 2000+ checks/second
- **WebSocket Messages**: 5000+ messages/second
- **Concurrent Connections**: 10,000+ WebSocket clients

### Resource Usage
- **Memory**: <50KB per active connection
- **CPU**: <5% under normal load
- **Network**: <2Mbps per 1000 concurrent users

## Troubleshooting

### Common Issues

#### High Latency
- Check database connection pool settings
- Verify Redis connectivity for caching
- Monitor circuit breaker state

#### WebSocket Connection Issues
- Ensure WebSocket support is enabled
- Check firewall settings for WebSocket ports
- Verify client connection limits

#### Circuit Breaker Tripped
- Investigate downstream service health
- Check network connectivity
- Review service timeout configurations

### Debug Mode

```bash
# Enable debug logging
export LOG_LEVEL=debug

# Enable profiling
go tool pprof http://localhost:8080/debug/pprof/profile
```

## Related Issues

- Issue #2232: Real-time Combat API Specification
- Issue #2231: Combat System API Specification
- Issue #2229: Crafting Service Implementation
- Issue #2214: Real-time Match Statistics Aggregation

## License

This project is part of the NECPGAME monorepo.
