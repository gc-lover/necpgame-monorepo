# Guild Service Go

## Overview

Enterprise-grade guild management service for the Night City MMOFPS RPG. Provides comprehensive guild functionality including member management, social features, and competitive elements.

## Features

- **Guild Management**: Creation, configuration, and administration
- **Member System**: Role-based permissions and contribution tracking
- **Social Features**: Guild events, communication, and coordination
- **Economy**: Shared guild bank and resource management
- **Competitive**: Guild rankings and inter-guild relationships
- **Performance**: MMOFPS-grade performance with <20ms P99 latency

## Architecture

### Core Components

- **Handlers**: HTTP request handling with object pooling
- **Service**: Business logic with caching and rate limiting
- **Repository**: Data access layer with PostgreSQL and Redis
- **Models**: Internal data structures optimized for performance

### Performance Optimizations

- **Struct Alignment**: Memory-efficient data structures
- **Object Pooling**: Reduced GC pressure for high-frequency operations
- **Two-Level Caching**: Memory + Redis for optimal performance
- **Rate Limiting**: Protection against abuse
- **Concurrent Operations**: Semaphore-based operation limiting

## API Endpoints

### Guild Management
- `GET /guilds/{guildId}` - Get guild details
- `POST /guilds` - Create new guild
- `PUT /guilds/{guildId}` - Update guild
- `DELETE /guilds/{guildId}` - Delete guild
- `GET /guilds` - List guilds with pagination

### Member Management
- `GET /guilds/{guildId}/members` - Get guild members
- `POST /guilds/{guildId}/members` - Add member to guild
- `DELETE /guilds/{guildId}/members/{playerId}` - Remove member

### Guild Bank
- `GET /guilds/{guildId}/bank` - Get guild bank balance

### Guild Events
- `POST /guilds/events` - Create guild event
- `GET /guilds/{guildId}/events` - Get guild events

## Database Schema

The service uses PostgreSQL with the following key tables:
- `guilds.guilds` - Guild definitions with versioning
- `guilds.guild_members` - Member relationships and permissions
- `guilds.guild_ranks` - Competitive rankings
- `guilds.guild_bank` - Shared resources
- `guilds.guild_events` - Scheduled events
- `guilds.guild_relationships` - Inter-guild diplomacy

## Configuration

### Environment Variables

- `SERVER_ADDR` - Server bind address (default: ":8080")
- `DATABASE_URL` - PostgreSQL connection string
- `REDIS_URL` - Redis connection string
- `LOG_LEVEL` - Logging level (default: "info")
- `GOGC` - Go GC percentage (default: 50 for reduced pressure)

### Service Configuration

```yaml
maxGuildNameLength: 50
maxGuildDescription: 500
defaultMaxMembers: 50
guildOperationTimeout: 30s
maxConcurrentOps: 50
```

## Development

### Prerequisites

- Go 1.21+
- PostgreSQL 15+
- Redis 7+
- Docker & Docker Compose

### Running Locally

1. **Start dependencies:**
   ```bash
   docker-compose up -d guild-db guild-redis
   ```

2. **Run migrations:**
   ```bash
   # Migrations are applied automatically via Docker Compose
   # Or run manually with Liquibase
   ```

3. **Start service:**
   ```bash
   go run main.go
   ```

4. **Test health check:**
   ```bash
   curl http://localhost:8080/health
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

### Docker

```bash
# Build image
docker build -t guild-service .

# Run with dependencies
docker-compose up
```

### Kubernetes

The service is designed for Kubernetes deployment with:
- Horizontal Pod Autoscaling (HPA)
- Pod Disruption Budget (PDB)
- ConfigMaps and Secrets for configuration
- Readiness and liveness probes

## Monitoring

### Metrics

- **Prometheus**: Operation latency, throughput, error rates
- **Guild Operations**: Creation, updates, member changes
- **Cache Performance**: Hit/miss ratios
- **Resource Usage**: Memory, CPU, database connections

### Health Checks

- **Readiness**: Database and Redis connectivity
- **Liveness**: HTTP endpoint responsiveness
- **Custom**: Guild operation functionality

## Security

- **Authentication**: Bearer token validation
- **Authorization**: Role-based permissions
- **Rate Limiting**: Per-operation rate limits
- **Input Validation**: Comprehensive request validation
- **SQL Injection**: Parameterized queries
- **XSS Protection**: Input sanitization

## Performance Targets

- **P99 Latency**: <20ms for guild operations
- **Memory Usage**: <12KB per active guild member
- **Concurrent Guilds**: 50,000+ active guilds
- **Real-time Updates**: <50ms propagation time
- **Guild Chat**: 1000+ concurrent users

## API Documentation

Complete OpenAPI 3.0 specification available in `proto/openapi/guild-service/main.yaml`.

## Related Issues

- Issue: #2295 - Implement guild-service-go with enterprise-grade social features