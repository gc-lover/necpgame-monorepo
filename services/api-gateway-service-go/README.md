# API Gateway Service

Enterprise-grade reverse proxy and API gateway for NECPGAME microservices architecture.

## Overview

The API Gateway serves as the single entry point for all client requests to NECPGAME backend services. It provides:

- **Reverse Proxy**: Routes requests to appropriate microservices
- **Load Balancing**: Distributes traffic across service instances
- **Authentication**: JWT token validation and authorization
- **Rate Limiting**: Prevents abuse and ensures fair usage
- **Monitoring**: Comprehensive metrics and health checks
- **Security**: CORS, security headers, and request validation

## Architecture

```
┌─────────────┐    ┌─────────────┐    ┌─────────────────┐
│   Clients   │────│ API Gateway │────│ Microservices   │
│             │    │             │    │                 │
│ - Web Apps  │    │ - Routing   │    │ - Analytics     │
│ - Mobile    │    │ - Auth      │    │ - Guild         │
│ - UE5 Client│    │ - Rate Lim. │    │ - Combat        │
└─────────────┘    │ - Monitoring│    │ - Matchmaking   │
                   └─────────────┘    └─────────────────┘
```

## Supported Services

| Service | Path | Description |
|---------|------|-------------|
| Analytics | `/api/v1/analytics/*` | Game analytics and metrics |
| Guild | `/api/v1/guilds/*` | Guild management and social features |
| Combat | `/api/v1/combat/*` | Combat statistics and real-time data |
| Combat Realtime | `/api/v1/combat-realtime/*` | Real-time combat sessions |
| World Events | `/api/v1/world-events/*` | Dynamic world events |
| Matchmaking | `/api/v1/matchmaking/*` | Player matchmaking |
| Cyberspace | `/api/v1/cyberspace/*` | Cyberspace navigation and easter eggs |
| WebRTC | `/api/v1/webrtc/*` | Voice communication signaling |
| Trading | `/api/v1/trading/*` | Economic trading systems |
| Inventory | `/api/v1/inventory/*` | Player inventory management |
| Economy | `/api/v1/economy/*` | Economic systems and markets |
| Auth | `/api/v1/auth/*` | Authentication and authorization |
| Character | `/api/v1/character/*` | Character management |
| Crafting | `/api/v1/crafting/*` | Item crafting systems |
| Housing | `/api/v1/housing/*` | Player housing |
| Achievement | `/api/v1/achievement/*` | Achievement systems |
| Social | `/api/v1/social/*` | Social features |
| Voice Chat | `/api/v1/voice-chat/*` | Voice chat services |
| WS Lobby | `/api/v1/ws-lobby/*` | WebSocket lobby services |

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8080` | Server port |
| `LOG_LEVEL` | `info` | Logging level |

### Service URLs

Services are configured with environment variables or Kubernetes service discovery:

```bash
# Example service URLs (configured in Kubernetes)
ANALYTICS_URL=http://analytics-dashboard-service-go:8080
GUILD_URL=http://guild-service-go:8080
COMBAT_URL=http://combat-stats-service-go:8080
# ... etc
```

## Health Checks

### Gateway Health
```bash
GET /health
```

Response:
```json
{
  "status": "healthy",
  "service": "api-gateway",
  "timestamp": "2025-12-28T11:30:00Z",
  "version": "1.0.0",
  "services": 20
}
```

### Readiness Probe
```bash
GET /ready
```

Response:
```json
{
  "status": "ready",
  "service": "api-gateway",
  "timestamp": "2025-12-28T11:30:00Z",
  "available": 18,
  "total": 20
}
```

### Metrics
```bash
GET /metrics
```

Provides Prometheus metrics for monitoring.

## Development

### Prerequisites

- Go 1.21+
- Docker (optional)

### Setup

```bash
# Install dependencies
make deps

# Run in development mode
make run

# Run tests
make test

# Build Docker image
make docker-build
```

### Testing

```bash
# Unit tests
make test

# Integration tests
cd ../../tests/integration
go test -run TestAPIGatewayRouting
```

## Deployment

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: api-gateway
spec:
  replicas: 3
  selector:
    matchLabels:
      app: api-gateway
  template:
    metadata:
      labels:
        app: api-gateway
    spec:
      containers:
      - name: api-gateway
        image: necpgame/api-gateway-service-go:latest
        ports:
        - containerPort: 8080
        env:
        - name: PORT
          value: "8080"
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

### Metrics

- Request count and latency by service
- Error rates and status codes
- Circuit breaker status
- Service availability

### Logs

Structured JSON logs with correlation IDs:

```json
{
  "level": "info",
  "timestamp": "2025-12-28T11:30:00Z",
  "service": "api-gateway",
  "request_id": "abc123",
  "service": "guild",
  "path": "/api/v1/guilds/123",
  "method": "GET",
  "status": 200,
  "duration_ms": 45
}
```

## Security

- JWT token validation
- CORS protection
- Rate limiting
- Request sanitization
- Security headers
- Audit logging

## Performance

### Benchmarks

- **Throughput**: 10,000+ RPS
- **Latency**: P99 <50ms
- **Memory**: <100MB per instance
- **Concurrent Connections**: 1000+

### Optimizations

- Connection pooling
- Request/response caching
- Circuit breaker patterns
- Graceful degradation
- Horizontal scaling support

## Troubleshooting

### Common Issues

1. **404 Errors**: Check service registration and URL configuration
2. **502 Errors**: Backend service unavailable
3. **429 Errors**: Rate limiting triggered
4. **503 Errors**: Circuit breaker activated

### Debug Mode

Enable debug logging:

```bash
export LOG_LEVEL=debug
make run
```

### Service Discovery

Check service health:

```bash
curl http://localhost:8080/health
```

Test specific service routing:

```bash
curl http://localhost:8080/api/v1/analytics/health
```
