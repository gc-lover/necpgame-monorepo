# Reputation Service API

## Overview

The Reputation Service provides enterprise-grade reputation management with natural decay mechanics and multiple recovery paths for realistic social interactions in NECPGAME.

## Architecture

### Domain Separation
- **Core Schemas**: `schemas/reputation-schemas.yaml` - Core reputation entities
- **Request Schemas**: `schemas/request-schemas.yaml` - API request structures
- **Response Schemas**: `schemas/response-schemas.yaml` - API response structures
- **Main API**: `main.yaml` - Complete OpenAPI specification

### Key Features

#### Reputation Decay Mechanics
- **Natural Decay**: Configurable decay rates based on relationship type and strength
- **Context Modifiers**: Time, location, and activity-based decay adjustments
- **Grace Periods**: Protection for inactive players

#### Recovery Systems
- **Multiple Action Types**: Quests, gifts, events, sacrifices, and more
- **Context Awareness**: Recovery effectiveness varies by location and time
- **Cooldown Management**: Prevents abuse with intelligent cooldowns

#### Performance Optimizations
- **Redis Caching**: High cache hit rates for active relationships
- **Bulk Operations**: Atomic batch updates for events and quests
- **Event Sourcing**: Complete audit trail for reputation changes

## API Endpoints

### Core Operations
- `GET /entities/{entity_id}/reputation` - Get reputation relationships
- `POST /reputation` - Update reputation
- `POST /reputation/bulk` - Bulk reputation updates

### Recovery System
- `POST /reputation/recovery` - Perform recovery actions
- `GET /entities/{entity_id}/recovery-options` - Get available recovery options

### Analytics & Simulation
- `POST /reputation/simulate-decay` - Simulate reputation decay
- Health monitoring endpoints

## Integration Points

### Event-Driven Architecture
- **Kafka Topics**: Reputation change events, decay calculations
- **Event Types**: `reputation.updated`, `reputation.decayed`, `recovery.performed`

### Database Schema
- **Tables**: `reputation_relationships`, `decay_configurations`, `recovery_actions`
- **Indexes**: Optimized for entity-target queries and time-based operations

### Caching Strategy
- **Redis Keys**: `reputation:{entity_id}:{target_id}`, `decay:config:{type}`
- **TTL**: 24 hours for active relationships, 7 days for inactive

## Configuration

### Decay Rates (Configurable)
```yaml
faction_reputation:
  base_decay: 1.5%  # per day
  grace_period: 7   # days
  strength_modifier:
    weak: 1.2
    strong: 0.8

personal_relationships:
  base_decay: 1.0%
  activity_modifier:
    inactive: 1.5
    active: 1.0
```

### Recovery Actions
```yaml
quest_completion:
  base_recovery: 15.0
  cooldown_hours: 24
  location_modifier:
    home_territory: 1.5
    enemy_territory: 0.7
```

## Performance Targets

- **Single Updates**: P99 <50ms
- **Bulk Operations**: 1000+ updates/sec
- **Decay Processing**: <10 minutes for 1M entities
- **Cache Hit Rate**: >95%
- **Storage**: <50 bytes per relationship

## Monitoring & Observability

### Metrics
- `reputation_updates_total` - Total reputation changes
- `decay_calculations_duration` - Decay processing time
- `recovery_actions_success_rate` - Recovery success percentage

### Health Checks
- Database connectivity
- Redis availability
- Kafka producer/consumer health
- Queue depths and processing rates

## Security Considerations

### Authentication
- JWT token validation for all operations
- Entity ownership verification
- Anti-cheat reputation validation

### Authorization
- Reputation modification permissions
- Bulk operation restrictions
- Simulation access controls

### Data Protection
- PII minimization in reputation data
- Encryption at rest and in transit
- Audit logging for sensitive operations

## Testing Strategy

### Unit Tests
- Decay calculation algorithms
- Recovery action validation
- Schema validation and serialization

### Integration Tests
- End-to-end reputation workflows
- Event-driven reputation updates
- Cross-service reputation synchronization

### Performance Tests
- Bulk operation throughput
- Cache performance under load
- Database query optimization

## Deployment

### Kubernetes Resources
- Deployment with horizontal pod autoscaling
- ConfigMap for decay/recovery configurations
- Secret for database and Redis credentials

### Environment Variables
```bash
REPUTATION_DB_HOST=postgres-service
REPUTATION_REDIS_HOST=redis-service
REPUTATION_KAFKA_BROKERS=kafka-service:9092
REPUTATION_JWT_SECRET=jwt-secret-key
```

## Related Systems

- **Quest Service**: Triggers reputation recovery actions
- **Event Service**: Bulk reputation updates for world events
- **Social Service**: Relationship strength calculations
- **Analytics Service**: Reputation trend analysis

## Future Enhancements

- **AI-Powered Recovery**: Machine learning for personalized recovery suggestions
- **Dynamic Decay**: Adaptive decay rates based on player behavior patterns
- **Reputation Networks**: Graph-based relationship analysis
- **Cross-Game Integration**: Reputation portability across game instances