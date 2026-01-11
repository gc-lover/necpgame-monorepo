# Real-time Combat Service API

## Overview

**Enterprise-grade Real-time Combat API for NECPGAME MMOFPS RPG**

Complete real-time combat service for NECPGAME with WebSocket-based live combat sessions, position synchronization, damage calculations, and spectator systems. Handles 10k+ concurrent connections with enterprise-grade performance optimizations.

## Key Features

- **Enterprise-grade architecture** with proper domain separation - **Real-time WebSocket support** for 10k+ concurrent connections - **Backend optimization hints** for struct alignment and memory savings - **Event-driven microservice** with Kafka integration - **Anti-cheat validation** and lag compensation algorithms

## Domain Purpose

Manages real-time combat components including session lifecycle, live player synchronization, damage calculations, combat analytics, and spectator systems.

## Performance Targets

- WebSocket connections: 10k+ concurrent, <50MB memory per 1000 connections
- Position updates: 1000+ updates/sec, P99 <10ms latency
- Damage events: 2000+ events/sec, <5ms processing time
- Combat sessions: 500+ active sessions, <20ms state sync
- Event throughput: 5000+ events/sec via Kafka
- Cache hit rate: >98% (Redis for active combat states)

## Architecture

### Domain Separation
- **Session Management**: `schemas/session-schemas.yaml` - Combat session lifecycle
- **Position Synchronization**: `schemas/position-schemas.yaml` - Real-time position updates
- **Damage Calculations**: `schemas/damage-schemas.yaml` - Damage processing and effects
- **Combat Actions**: `schemas/action-schemas.yaml` - Abilities and combat mechanics
- **Spectator Mode**: `schemas/spectator-schemas.yaml` - Spectator functionality
- **Statistics**: `schemas/stats-schemas.yaml` - Combat performance metrics
- **Replay System**: `schemas/replay-schemas.yaml` - Combat replay and analysis
- **Tournament Integration**: `schemas/tournament-schemas.yaml` - Tournament support
- **Leaderboards**: `schemas/leaderboard-schemas.yaml` - Real-time rankings
- **Analytics**: `schemas/analytics-schemas.yaml` - Advanced combat analytics

### Key Features

#### Real-time Combat Sessions
- **WebSocket Connections**: Long-lived connections with heartbeat validation
- **Session Management**: Create, join, leave, and manage combat sessions
- **State Synchronization**: Real-time position, health, and combat state updates
- **Spectator Support**: Advanced spectator modes with camera controls

#### Advanced Damage System
- **Damage Calculations**: Environmental factors, modifiers, and critical hits
- **Status Effects**: Burning, poisoned, stunned, slowed, bleeding effects
- **Anti-cheat Validation**: Server-side damage verification and lag compensation

#### Combat Analytics
- **Performance Metrics**: Real-time statistics and performance monitoring
- **Heatmaps**: Position heatmaps for tactical analysis
- **Replay System**: Event-sourced combat replays for review and analysis

#### Tournament Integration
- **Tournament Sessions**: Bracket-based tournament match management
- **Real-time Leaderboards**: Live ranking updates during tournaments
- **Spectator Features**: Tournament-specific spectator modes

## API Endpoints

### Health Monitoring
- `GET /health` - Service health check

### Combat Session Management
- `POST /sessions` - Create new combat session
- `GET /sessions/{session_id}` - Get session details
- `PUT /sessions/{session_id}` - Update session parameters
- `DELETE /sessions/{session_id}` - End combat session
- `POST /sessions/{session_id}/join` - Join combat session
- `POST /sessions/{session_id}/leave` - Leave combat session
- `POST /sessions/{session_id}/spectate` - Spectate combat session

### Real-time Communication
- `GET /ws/sessions/{session_id}` - WebSocket connection for combat updates
- `GET /ws/sessions/{session_id}/spectate` - Spectator WebSocket connection

### Position Synchronization
- `POST /sessions/{session_id}/positions` - Update player position

### Combat Mechanics
- `POST /sessions/{session_id}/damage` - Apply damage to target
- `POST /sessions/{session_id}/actions` - Execute combat action

### Statistics & Analytics
- `GET /sessions/{session_id}/stats` - Get combat session statistics
- `GET /sessions/{session_id}/leaderboard` - Get session leaderboard
- `GET /analytics/combat/{session_id}` - Get detailed combat analytics

### Tournament Support
- `GET /tournaments/{tournament_id}/sessions` - Get tournament sessions
- `GET /tournaments/{tournament_id}/leaderboard` - Get tournament leaderboard
- `POST /tournaments/{tournament_id}/spectate` - Join tournament as spectator

### Replay System
- `GET /sessions/{session_id}/replay` - Get combat session replay data

## Security & Authentication

- **JWT Bearer Authentication**: RS256 signed tokens with configurable expiration
- **WebSocket Security**: Token-based authentication for real-time connections
- **Rate Limiting**: Configurable limits per user role and endpoint type
- **Anti-cheat Integration**: Server-side validation for all combat actions

## Integration Points

### Service Dependencies
- **Infrastructure Service**: Entity management and audit logging
- **Game Entities Service**: Player and ability data
- **Common Services**: Health checks, error responses, pagination

### Event Streaming
- **Kafka Topics**: Combat events, position updates, damage notifications
- **WebSocket Broadcasting**: Real-time updates to connected clients
- **Redis Caching**: Active session state and player data caching

## Performance Optimizations

### Backend Optimizations
- **P99 Latency**: <10ms for configuration reads, <50ms for damage calculations
- **Memory Usage**: <50KB per active connection, struct alignment savings: 30-50%
- **Concurrent Operations**: 10k+ WebSocket connections supported
- **Data Consistency**: ACID compliance for critical combat state updates

### Scalability Features
- **Horizontal Scaling**: Stateless services with Redis session storage
- **Load Balancing**: WebSocket-aware load balancing for session affinity
- **Event Sourcing**: Kafka-based event sourcing for combat replay
- **Caching Strategy**: Multi-level caching (Redis + application cache)

## Development Guidelines

### Code Generation
```bash
# Generate Go client/server code with ogen
ogen --target ../../generated/realtime-combat-service \
     --package realtime_combat \
     --clean main.yaml
```

### Validation
```bash
# Lint with redocly
npx redocly lint main.yaml

# Bundle for distribution
npx redocly bundle main.yaml --output main-bundled.yaml
```

## Related Services

- **combat-service** - Core combat mechanics and rules
- **combat-system-service** - Combat system configuration management
- **gameplay-service** - General gameplay mechanics
- **tournament-service** - Tournament management
- **analytics-service** - Advanced analytics and reporting

## Issue Tracking

- **Primary Issue:** #2232 - Real-time Combat API Specification
- **Domain:** Game/Combat/Realtime
- **Priority:** High
- **Complexity:** Enterprise

---

*This specification follows NECPGAME enterprise API design patterns and is optimized for MMOFPS real-time performance requirements.*