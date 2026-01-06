# Weapon Modifiers Service

## Overview

Enterprise-grade Weapon Modifiers system providing extensive weapon customization for cyberpunk MMOFPS RPG gameplay. Delivers low-latency modifier calculations with comprehensive compatibility validation.

## Purpose

The Weapon Modifiers Service enables players to customize their weapons through three distinct modifier types: attachments, chips, and firmware. The system supports complex synergy effects, compatibility validation, and progression-based slot unlocking, allowing for deep tactical weapon customization.

## Functionality

### Core Features

#### **Modifier Types & Slots**
- **Attachments (4 slots)**: Physical weapon modifications (scopes, grips, barrels, etc.)
- **Chips (3 slots)**: Electronic enhancements (targeting, stabilization, energy management)
- **Firmware (1 slot)**: Software upgrades (AI assistance, predictive algorithms)

#### **Modifier Categories (30 total)**
- **Attachments**: Optics, Ergonomics, Barrels, Magazines, Underbarrels
- **Chips**: Targeting, Stability, Energy, Critical, Penetration
- **Firmware**: AI Assistance, Predictive, Adaptive, Overclock, Stealth

#### **Compatibility System**
- Weapon-type specific compatibility validation
- Cross-weapon modifier support with restrictions
- Real-time compatibility checking

#### **Synergy Effects**
- Combined modifier bonuses for specific combinations
- Dynamic effect calculations based on installed modifiers
- PvE/PvP-specific optimizations

#### **Progression Integration**
- Slot unlocking through player level advancement
- Reputation-based modifier access
- Prerequisite modifier requirements

### API Endpoints

#### **Modifier Management**
- `GET /modifiers` - List available modifiers with filtering
- `GET /modifiers/{id}` - Get detailed modifier information

#### **Weapon Integration**
- `GET /weapons/{id}/modifiers` - Get current weapon modifiers
- `POST /weapons/{id}/modifiers` - Apply modifier to weapon
- `DELETE /weapons/{id}/modifiers/{slot}` - Remove modifier from slot

#### **Compatibility & Effects**
- `GET /weapons/{id}/compatibility` - Check modifier compatibility
- `POST /effects/calculate` - Calculate combined modifier effects

## Structure

### Service Components

```
weapon-modifiers-service/
├── modifier-manager/     # Core modifier operations
├── slot-manager/         # Slot allocation and validation
├── compatibility-engine/ # Compatibility checking
├── effect-calculator/    # Effect computation and synergies
└── progression-tracker/  # Slot unlocking and requirements
```

### Dependencies

- **inventory-service**: Weapon ownership and storage
- **character-service**: Player progression and reputation
- **combat-service**: Real-time effect application
- **analytics-service**: Modifier usage statistics

## Performance Targets

### Latency Requirements
- **Modifier Queries**: <30ms P95 response time
- **Modifier Application**: <100ms P95 for single modifier
- **Bulk Operations**: <500ms P95 for full weapon modification
- **Effect Calculations**: <50ms P95 for synergy computations

### Scalability Metrics
- **Concurrent Operations**: Support for 10,000+ simultaneous modifier operations
- **Data Consistency**: 99.99% consistency across distributed operations
- **Cache Hit Rate**: >95% for frequently accessed modifier data

### Resource Optimization
- **Memory Usage**: <50MB per service instance
- **CPU Utilization**: <30% under normal load
- **Network Bandwidth**: <10Mbps per 1000 operations

## Usage

### Validation
```bash
# Validate OpenAPI specification
npx @redocly/cli lint main.yaml

# Generate documentation
npx @redocly/cli build-docs main.yaml -o docs/index.html
```

### Code Generation
```bash
# Generate Go client
npx @openapitools/openapi-generator-cli generate \
  -i main.yaml \
  -g go \
  -o ../services/weapon-modifiers-service-go/pkg/api

# Generate TypeScript client
npx @openapitools/openapi-generator-cli generate \
  -i main.yaml \
  -g typescript-fetch \
  -o ../client/UE5/Source/GameModule/Public/API
```

### Documentation
```bash
# Start local documentation server
npx @redocly/cli preview-docs main.yaml

# Build static documentation
npx @redocly/cli build-docs main.yaml -o docs/
```

## Development

### Environment Setup
```bash
# Install dependencies
npm install

# Run validation
npm run validate

# Generate code
npm run generate
```

### Testing
```bash
# Run unit tests
npm test

# Run integration tests
npm run test:integration

# Performance testing
npm run test:performance
```

## Deployment

### Docker Configuration
```yaml
# Dockerfile
FROM golang:1.21-alpine AS builder
WORKDIR /app
COPY . .
RUN go build -o weapon-modifiers-service ./cmd/server

FROM alpine:latest
RUN apk --no-cache add ca-certificates
WORKDIR /root/
COPY --from=builder /app/weapon-modifiers-service .
CMD ["./weapon-modifiers-service"]
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: weapon-modifiers-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: weapon-modifiers-service
        image: necpgame/weapon-modifiers-service:latest
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: url
        - name: REDIS_URL
          valueFrom:
            secretKeyRef:
              name: redis-secret
              key: url
```

## Monitoring

### Health Checks
- `/health` - Basic health status
- `/metrics` - Prometheus metrics
- `/ready` - Readiness probe

### Key Metrics
- `modifier_operations_total` - Total modifier operations
- `modifier_application_duration` - Application latency
- `compatibility_checks_total` - Compatibility validation count
- `effect_calculations_total` - Effect computation count

### Alerting
- Latency >100ms for modifier applications
- Error rate >1% for operations
- Queue depth >1000 pending operations

## Security

### Authentication
- JWT-based authentication for all endpoints
- Role-based access control (player, moderator, admin)
- API key validation for service-to-service communication

### Authorization
- Weapon ownership validation
- Modifier access based on player level/reputation
- Rate limiting per user and per endpoint

### Data Protection
- Encryption at rest for sensitive modifier data
- Audit logging for all modifier operations
- Input validation and sanitization

## Contributing

### Code Standards
- Follow Go coding standards and best practices
- Maintain 80%+ test coverage
- Use structured logging with zap
- Implement comprehensive error handling

### Documentation
- Update API documentation for new endpoints
- Maintain accurate OpenAPI specifications
- Document performance characteristics

### Testing
- Unit tests for all business logic
- Integration tests for API endpoints
- Performance tests for critical paths
- Load testing for scalability validation

