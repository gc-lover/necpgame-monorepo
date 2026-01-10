# Matchmaking Service

Enterprise-grade player matchmaking service for NECPGAME with high-performance queue management and real-time match finding.

## Features

- **High-Performance Queue Management**: Redis-backed queues with sub-millisecond operations
- **Multiple Game Modes**: PvP Ranked, PvP Casual, PvE Dungeon support
- **Real-Time Matchmaking**: WebSocket-based status updates
- **Anti-Cheat Integration**: Queue security and fraud detection
- **Enterprise Monitoring**: Comprehensive metrics and health checks
- **Horizontal Scaling**: Stateless design for easy scaling

## API Endpoints

### Health Check
```http
GET /health
```

Response:
```json
{
  "status": "healthy",
  "timestamp": "2024-01-10T20:00:00Z",
  "version": "1.0.0"
}
```

### Join Queue
```http
POST /matchmaking/queue/join
Content-Type: application/json

{
  "player_id": "uuid",
  "game_mode": "pvp_ranked"
}
```

Response:
```json
{
  "queue_position": 5,
  "estimated_wait_seconds": 45
}
```

### Leave Queue
```http
DELETE /matchmaking/queue/leave?player_id=uuid
```

### Get Queue Status
```http
GET /matchmaking/queue/status?player_id=uuid
```

Response:
```json
{
  "in_queue": true,
  "position": 3,
  "estimated_wait": 30,
  "game_mode": "pvp_ranked"
}
```

### Find Match
```http
POST /matchmaking/match/find
Content-Type: application/json

{
  "player_id": "uuid"
}
```

Response:
```json
{
  "match_id": "uuid",
  "status": "forming",
  "players": [
    {
      "player_id": "uuid",
      "team": "blue"
    }
  ],
  "game_mode": "pvp_ranked",
  "created_at": "2024-01-10T20:00:00Z"
}
```

## Architecture

```
┌─────────────────┐    ┌─────────────────┐
│   API Layer     │────│  Service Layer  │
│   (ogen-gen)    │    │ (Business Logic)│
└─────────────────┘    └─────────────────┘
          │                       │
          ▼                       ▼
┌─────────────────┐    ┌─────────────────┐
│  Redis Cache    │    │   PostgreSQL    │
│  (Queues)       │    │   (Persistence) │
└─────────────────┘    └─────────────────┘
```

## Configuration

Environment variables:

```bash
# Server
PORT=8082

# Database
DB_HOST=localhost
DB_PORT=5432
DB_USER=necpgame
DB_PASSWORD=password
DB_NAME=necpgame

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Matchmaking
MATCHMAKING_QUEUE_TIMEOUT=300
MATCHMAKING_MAX_PLAYERS_PER_MATCH=10
MATCHMAKING_MATCH_FORMATION_TIMEOUT=60
```

## Performance Metrics

- **Queue Join**: <10ms P95
- **Match Finding**: <50ms P95 for 10k concurrent players
- **Queue Status**: <5ms P95
- **Memory Usage**: <256MB per instance
- **Concurrent Players**: 50,000+ simultaneous queue operations

## Scaling Strategy

- **Horizontal Scaling**: Stateless design allows unlimited horizontal scaling
- **Redis Sharding**: Queue distribution across multiple Redis instances
- **Database Read Replicas**: Match history and statistics offloaded to replicas
- **Load Balancing**: API Gateway distributes requests across instances

## Monitoring

### Key Metrics
- `matchmaking_queue_size{game_mode}` - Current queue size per game mode
- `matchmaking_matches_created_total` - Total matches created
- `matchmaking_average_wait_time_seconds` - Average queue wait time
- `matchmaking_queue_join_rate` - Rate of players joining queues

### Health Checks
- Database connectivity
- Redis connectivity
- Queue processing health
- Match formation pipeline

## Development

### Local Setup
```bash
# Build
make build-matchmaking

# Run
./matchmaking-service

# Test
make test-matchmaking
```

### Docker
```bash
# Build image
docker build -t necpgame/matchmaking-service .

# Run with dependencies
docker-compose up matchmaking-service
```

## Security

- **Rate Limiting**: Prevents queue spam and abuse
- **Input Validation**: Strict validation of player IDs and game modes
- **Anti-Cheat**: Queue manipulation detection
- **Audit Logging**: All matchmaking operations logged
- **Token Validation**: Integration with auth service for player verification

## Future Enhancements

- **Skill-Based Matching**: Elo rating system integration
- **Geographic Matching**: Region-based player grouping
- **Tournament Queues**: Special queues for competitive events
- **Party Matching**: Group matchmaking support
- **Cross-Platform**: Multi-platform player matching