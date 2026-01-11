# Combat System Service API

## Overview

The Combat System Service provides enterprise-grade combat system management for NECPGAME. This service handles core combat mechanics, balance configurations, damage calculations, and ability management with high-performance optimizations for MMOFPS gameplay.

## Architecture

### Domain Separation
- **Combat Rules Schemas**: `schemas/combat-rules-schemas.yaml` - Core combat system rules and mechanics
- **Damage Calculation Schemas**: `schemas/damage-calculation-schemas.yaml` - Damage calculation and resolution
- **Balance Config Schemas**: `schemas/balance-config-schemas.yaml` - Combat balance and configuration management
- **Ability System Schemas**: `schemas/ability-system-schemas.yaml` - Combat abilities and special moves
- **Main API**: `main.yaml` - Complete OpenAPI specification

### Key Features

#### Combat Rules Management
- **Damage Calculation Rules**: Configurable damage multipliers, critical hits, armor reduction
- **Combat Mechanics**: Turn-based vs real-time, action points, interruption rules
- **Balance Parameters**: Difficulty scaling, player advantages, NPC modifiers

#### Advanced Damage System
- **Environmental Factors**: Terrain, weather, time-of-day modifiers
- **Modifier Stacking**: Additive, multiplicative, percentage, and fixed modifiers
- **Critical Hit System**: Configurable chance and multipliers
- **Armor Calculations**: Reduction factors with penetration mechanics

#### Balance Configuration
- **Global Multipliers**: Faction and region-based modifiers
- **Character Balance**: Class-specific adjustments and stat modifiers
- **Environment Balance**: Terrain and cover effectiveness modifiers

#### Ability System
- **Bulk Balance Updates**: High-performance batch operations for balance passes
- **Power Level Management**: Ability classification and balancing tiers
- **Stat Requirements**: Dynamic ability access based on character stats
- **Cooldown Management**: Configurable ability timing and recovery

## Performance Targets

### Backend Optimizations
- **P99 Latency**: <10ms for configuration reads, <50ms for damage calculations
- **Memory Usage**: <50KB per active configuration, struct alignment savings: 30-50%
- **Concurrent Operations**: 1000+ simultaneous combat calculations supported
- **Data Consistency**: ACID compliance for balance updates with optimistic locking

### Scalability Features
- **Bulk Operations**: Up to 100 ability balance updates in single request
- **Pagination Support**: Efficient handling of large ability configuration lists
- **Caching Strategy**: Redis-backed configuration caching with TTL
- **Event-Driven**: Kafka integration for real-time balance updates

## API Endpoints

### Combat Rules Management
- `GET /combat-system/rules` - Retrieve current combat system rules
- `PUT /combat-system/rules` - Update combat system rules with optimistic locking

### Damage Calculation
- `POST /combat-system/damage/calculator` - Calculate damage with all modifiers

### Balance Configuration
- `GET /combat-system/balance` - Get current balance configuration
- `PUT /combat-system/balance` - Update balance parameters

### Ability Management
- `GET /combat-system/abilities` - List ability configurations with pagination
- `PUT /combat-system/abilities/{ability_id}` - Update individual ability configuration
- `POST /combat-system/abilities/balance/bulk` - Bulk update ability balances

## Security & Authentication

- **JWT Bearer Authentication**: RS256 signed tokens with configurable expiration
- **Role-Based Access**: Admin, Game Designer, and Analyst permission levels
- **Audit Logging**: Complete change tracking for balance modifications
- **Rate Limiting**: Configurable limits per user role and endpoint

## Integration Points

### Service Dependencies
- **Infrastructure Service**: Entity management and audit logging
- **Game Entities Service**: Ability and character data
- **Common Services**: Health checks, error responses, pagination

### Event Streaming
- **Kafka Topics**: Balance change notifications, damage calculation events
- **WebSocket Support**: Real-time configuration updates for game clients
- **Webhook Integration**: External balance monitoring and alerting

## Development Guidelines

### Code Generation
- **Ogen Compatibility**: Full OpenAPI 3.0 support with code generation
- **Struct Alignment**: Backend fields ordered for optimal memory layout
- **Validation Tags**: Comprehensive input validation with custom error messages

### Testing Strategy
- **Unit Tests**: Individual damage calculations and modifier applications
- **Integration Tests**: Full combat scenarios with multiple participants
- **Performance Tests**: Load testing with 1000+ concurrent calculations
- **Balance Tests**: Automated verification of game balance constraints

## Deployment Considerations

### Infrastructure Requirements
- **Database**: PostgreSQL with connection pooling (min 10, max 50 connections)
- **Cache**: Redis cluster with persistence and replication
- **Message Queue**: Kafka with topic partitioning and consumer groups

### Monitoring & Observability
- **Metrics**: Prometheus integration with custom combat metrics
- **Logging**: Structured logging with correlation IDs
- **Tracing**: Distributed tracing for damage calculation chains
- **Health Checks**: Multi-level health verification (database, cache, external services)

## Migration Notes

This specification implements domain separation from the original monolithic combat system API. Legacy endpoints remain available during transition, but new implementations should use the domain-separated schemas for better maintainability and performance.