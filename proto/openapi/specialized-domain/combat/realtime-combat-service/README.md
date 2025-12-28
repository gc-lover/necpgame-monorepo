# Real-time Combat Service - Combat Sessions API

## Overview

The Real-time Combat Service provides comprehensive API endpoints for managing combat sessions in NECPGAME MMOFPS RPG. This service handles session lifecycle, player participation, spectator access, and real-time WebSocket connections.

## Core Features

### Session Management
- **Create Sessions**: Initialize new combat sessions with configurable parameters
- **Session Lifecycle**: Start, pause, end, and cancel combat sessions
- **Participant Management**: Join/leave sessions as players or spectators

### Real-time Communication
- **WebSocket Connections**: Real-time updates for players and spectators
- **Event Streaming**: Combat events, position updates, damage notifications
- **Anti-cheat Integration**: Position validation and lag compensation

### Spectator System
- **Multiple Camera Modes**: Free camera, player following, overview, cinematic
- **Read-only Access**: Spectators receive session events without affecting gameplay
- **Flexible Controls**: Dynamic camera switching and target following

## API Endpoints

### Session Operations

#### Create Session
```http
POST /sessions
Content-Type: application/json

{
  "game_mode": "team_deathmatch",
  "participants": [...],
  "max_rounds": 10,
  "time_limit_minutes": 15
}
```

#### Get Session Details
```http
GET /sessions/{session_id}
```

#### Update Session
```http
PUT /sessions/{session_id}
Content-Type: application/json

{
  "status": "paused",
  "current_round": 5
}
```

#### End Session
```http
DELETE /sessions/{session_id}
```

### Player Participation

#### Join as Player
```http
POST /sessions/{session_id}/join
Content-Type: application/json

{
  "player_id": "uuid",
  "team_id": "uuid"
}
```

#### Leave Session
```http
POST /sessions/{session_id}/leave
```

#### Join as Spectator
```http
POST /sessions/{session_id}/spectate
Content-Type: application/json

{
  "player_id": "uuid",
  "preferred_camera_mode": "free"
}
```

### Real-time Connections

#### Player WebSocket
```http
GET /ws/sessions/{session_id}?token=jwt&client_version=1.0.0
```

#### Spectator WebSocket
```http
GET /ws/sessions/{session_id}/spectate?token=jwt&camera_mode=follow_player&follow_target=player_uuid
```

## Data Schemas

### CombatSession
```json
{
  "id": "uuid",
  "status": "active",
  "participants": [...],
  "current_round": 3,
  "max_rounds": 10
}
```

### SessionParticipant
```json
{
  "player_id": "uuid",
  "team_id": "uuid",
  "joined_at": "2025-01-01T12:00:00Z",
  "position": {...},
  "health": 85.5
}
```

## WebSocket Message Format

### Client Messages
```json
{
  "type": "position_update",
  "data": {
    "x": 123.45,
    "y": 67.89,
    "z": 10.0
  },
  "timestamp": "2025-01-01T12:00:00Z"
}
```

### Server Messages
```json
{
  "type": "combat_event",
  "event": "damage_dealt",
  "data": {
    "attacker_id": "uuid",
    "target_id": "uuid",
    "damage": 45.5,
    "weapon": "assault_rifle"
  },
  "session_id": "uuid",
  "timestamp": "2025-01-01T12:00:00Z"
}
```

## Performance Requirements

- **Concurrent Sessions**: 500+ active sessions
- **WebSocket Connections**: 10k+ concurrent connections
- **Position Updates**: 1000+ updates/sec, P99 <10ms latency
- **Event Throughput**: 5000+ events/sec via Kafka
- **Memory per Connection**: ~5KB
- **Cache Hit Rate**: >98% (Redis for active sessions)

## Security Considerations

- JWT-based authentication for all endpoints
- Session ownership validation
- Anti-cheat position validation
- Rate limiting for connection attempts
- Spectator access controls

## Error Handling

All endpoints return standard HTTP status codes with detailed error messages:

```json
{
  "error": "SESSION_NOT_FOUND",
  "message": "Combat session with ID '123' not found",
  "details": {...}
}
```

## Implementation Notes

- **Backend**: Go microservice with ogen-generated handlers
- **Database**: PostgreSQL for session metadata
- **Cache**: Redis for active session state
- **Events**: Kafka for combat event streaming
- **WebSocket**: Gorilla WebSocket with connection pooling

## Related Services

- **Combat Damage Service**: Damage calculation and application
- **Combat Abilities Service**: Skill and ability management
- **Player Service**: Player authentication and profiles
- **Matchmaking Service**: Session creation and player matching

## Issue Reference

Issue: #2197 - [API Designer] Real-time Combat API Specification
