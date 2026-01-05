# Performance Monitoring Service

## Overview

**Performance Monitoring Service API** - Enterprise-grade domain service providing comprehensive monitoring, metrics
collection, health checks, and observability for the NECPGAME platform. This service implements SOLID/DRY principles
with domain separation, ensuring reliable performance tracking across all game systems.

## Domain Purpose

The Performance Monitoring Service serves as the central observability hub for the entire game platform, providing:

- **Real-time Health Monitoring**: Continuous health checks for all services and infrastructure components
- **Performance Metrics**: Detailed performance analytics for game mechanics, AI systems, and user interactions
- **Alert Management**: Proactive alerting system for performance degradation and system issues
- **Distributed Tracing**: End-to-end request tracing across microservices
- **Analytics Dashboard**: Comprehensive analytics for operational insights

## Performance Targets

- **Latency**: < 50ms for health checks, < 200ms for metrics queries
- **Memory**: < 512MB baseline, < 1GB under load
- **RPS**: 1000+ sustained requests per second
- **Uptime**: 99.9% availability SLA

## Structure

```
performance-monitoring-service/
├── main.yaml                 # Main OpenAPI specification
├── README.md                 # This documentation
└── (future) schemas/         # Domain-specific schemas
```

## Dependencies

- **Common Schemas**: `../common-service/schemas/health.yaml`, `../common-service/schemas/error.yaml`
- **Common Responses**: `../common-service/responses/error.yaml`, `../common-service/responses/success.yaml`
- **Common Operations**: `../common-service/operations/health.yaml`, `../common-service/operations/batch-health.yaml`

## Usage

### Health Monitoring

```bash
# Service health check
GET /health

# Batch health check for multiple services
GET /health/batch?service_filter=all

# WebSocket health monitoring
GET /health/ws
```

### Performance Metrics

```bash
# Get service metrics
GET /metrics/{service_name}?time_range=1h&metric_types=cpu,memory

# Get alerts with filtering
GET /alerts?severity=critical&status=active&limit=50

# Acknowledge alert
POST /alerts/{alert_id}/acknowledge
```

### Analytics

```bash
# Anti-cheat monitoring
GET /anti-cheat/monitoring/stats

# Economy analytics
GET /economy/analytics/overview?time_range=24h

# AI combat analytics
GET /ai-combat/analytics/overview
```

## Validation

### Redocly Lint Check

```bash
npx @redocly/cli lint proto/openapi/performance-monitoring-service/main.yaml
```

### Go Code Generation

```bash
ogen proto/openapi/performance-monitoring-service/main.yaml \
  --package performance_monitoring \
  --generate server,client,models \
  --output services/performance-monitoring-service-go/
```

## Mandatory Elements

### OpenAPI Header

- OpenAPI 3.0.3 specification
- Enterprise-grade info with version, description, contact
- License and terms of service
- External documentation links

### Servers Configuration

- Production: `https://api.necpgame.com/v1/performance-monitoring`
- Staging: `https://staging-api.necpgame.com/v1/performance-monitoring`
- Local: `http://localhost:8080/api/v1/performance-monitoring`

### Security Schemes

- BearerAuth (JWT tokens)
- APIKeyAuth (service-to-service)
- Mutual TLS for internal communications

### Health Endpoints

- `/health` - Basic health check
- `/health/batch` - Batch health check for services
- `/health/ws` - WebSocket real-time health monitoring

### Common Schemas

- `HealthResponse` from `../common-service/schemas/health.yaml`
- `Error` from `../common-service/schemas/error.yaml`
- `WebSocketHealthMessage` for real-time updates

## Backend Optimization Hints

### Memory Alignment

```go
// Struct field alignment for 30-50% memory savings
type PerformanceMetrics struct {
    Timestamp   time.Time `json:"timestamp"`   // 8 bytes
    ServiceName string    `json:"service_name"` // 16 bytes
    CPUUsage    float64   `json:"cpu_usage"`   // 8 bytes
    MemoryUsage uint64    `json:"memory_usage"` // 8 bytes
    // Total: 40 bytes, perfectly aligned
}
```

### Connection Pooling

```go
// Database connection pool configuration
db.SetMaxOpenConns(100)
db.SetMaxIdleConns(10)
db.SetConnMaxLifetime(time.Hour)

// Redis connection pool
redisPool := &redis.Pool{
    MaxActive: 100,
    MaxIdle:   10,
    Wait:      true,
}
```

### Context Timeouts

```go
// All operations must have context timeouts
ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
defer cancel()

result, err := service.GetMetrics(ctx, request)
```

## How to Use the Template

1. **Copy Template**: Start from the enterprise template
2. **Replace Placeholders**: Update service name, description, version
3. **Add Real Operations**: Implement domain-specific endpoints
4. **Optimize Schemas**: Apply memory alignment and performance hints
5. **Validate**: Run Redocly lint and Ogen generation
6. **Test**: Ensure all endpoints work correctly

## Performance Benchmarks

### Health Check Performance

- Single service: < 10ms response time
- Batch check (10 services): < 50ms response time
- WebSocket connection: < 100ms establishment

### Metrics Query Performance

- 1-hour range: < 200ms
- 24-hour range: < 500ms
- 7-day range: < 2s

### Alert Processing

- Alert detection: < 1s from event
- Alert notification: < 500ms delivery
- Alert escalation: < 30s for critical alerts

## Related Documents

- `REORGANIZATION_INSTRUCTION.md` - Migration guidelines
- `MIGRATION_GUIDE.md` - Step-by-step migration process
- `.cursor/rules/agent-backend.mdc` - Backend implementation rules
- `.cursor/rules/agent-performance.mdc` - Performance optimization guidelines

## Next Steps

1. **Implement Backend**: Create Go service in `services/performance-monitoring-service-go/`
2. **Database Setup**: Configure PostgreSQL/Liquibase migrations
3. **Monitoring Setup**: Configure Prometheus/Grafana dashboards
4. **Testing**: Implement comprehensive test suite
5. **Deployment**: Set up Kubernetes manifests
6. **Documentation**: Generate API documentation with Redoc

## Important Remarks

- **Security First**: All endpoints require authentication
- **Rate Limiting**: Implemented at gateway level
- **Audit Logging**: All operations are logged for compliance
- **Scalability**: Horizontal scaling with Redis clustering
- **Observability**: Full metrics collection for all operations
- **Compliance**: GDPR compliant data handling
- **Performance**: Optimized for high-throughput scenarios

## Issue Tracking

Related Issues:

- #2266 - Refactor system-domain - AI, monitoring, networking services
- Performance monitoring implementation tasks
- Health check and alerting system requirements
