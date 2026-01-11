# Seasonal Challenges Service

Enterprise-grade seasonal challenges service for MMOFPS RPG with real-time WebSocket events.

## Features

- **Seasonal Challenges**: Time-limited competitive events with objectives and rewards
- **Real-time Updates**: WebSocket integration for live progress tracking
- **Leaderboards**: Competitive rankings with caching for performance
- **Reward System**: Seasonal currency and item rewards
- **MMOFPS Optimized**: Enterprise-grade performance for high-concurrency gaming

## API Endpoints

### REST API
- `GET /health` - Health checks
- `GET /api/v1/seasons` - List seasons
- `POST /api/v1/seasons` - Create season
- `GET /api/v1/seasons/{id}` - Get season details
- `PUT /api/v1/seasons/{id}` - Update season
- `POST /api/v1/challenges/{id}/progress` - Update challenge progress
- `GET /api/v1/leaderboards/{season_id}` - Get season leaderboard
- `POST /api/v1/rewards/{player_id}/claim` - Claim season rewards

### WebSocket
- `WS /ws/events` - Real-time event subscription
- `POST /events/{season_id}/broadcast` - Broadcast seasonal events

## WebSocket Events

### Outgoing Events
- `progress_update` - Challenge progress changes
- `challenge_unlocked` - New challenges become available
- `season_start` - Season begins
- `season_end` - Season concludes
- `leaderboard_update` - Rankings change
- `reward_available` - Rewards can be claimed
- `achievement_unlock` - Player achievements unlocked

### Incoming Messages
- `subscribe_season` - Subscribe to season events
- `unsubscribe_season` - Unsubscribe from season events
- `ping` - Connection health check

## Performance Targets

- **P99 Latency**: <50ms for all endpoints
- **Concurrent Users**: 10,000+ supported
- **WebSocket Connections**: 5,000+ active connections
- **Database Queries**: <5ms average response time
- **Memory Usage**: <50KB per instance baseline

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 15+
- Redis 7+

### Setup
```bash
# Clone repository
git clone <repository-url>
cd seasonal-challenges-service-go

# Install dependencies
go mod tidy

# Start development environment
docker-compose up -d postgres redis

# Run service
go run main.go
```

### Testing
```bash
# Run tests
go test ./...

# Run benchmarks
go test -bench=. -benchmem ./...

# Run with race detector
go test -race ./...
```

### Docker
```bash
# Build image
docker build -t seasonal-challenges-service .

# Run container
docker run -p 8080:8080 seasonal-challenges-service
```

## Architecture

### Components
- **HTTP Server**: REST API with enterprise middleware
- **WebSocket Server**: Real-time event broadcasting
- **Service Layer**: Business logic with optimistic locking
- **Repository Layer**: Database access with connection pooling
- **Health Checks**: Comprehensive monitoring endpoints

### Database Schema
- `seasons` - Seasonal events configuration
- `challenges` - Individual challenges and objectives
- `challenge_progress` - Player progress tracking
- `leaderboard` - Competitive rankings
- `seasonal_currency` - Special currency system
- `season_rewards` - Available and claimed rewards

## Monitoring

### Metrics
- Request count and duration
- WebSocket connection count
- Database connection pool status
- Memory and CPU usage
- Error rates by endpoint

### Health Checks
- `/health` - Basic service health
- `/health/detailed` - Comprehensive health with metrics
- `/health/batch` - Multi-service health check
- `/health/ws` - WebSocket connectivity health

## Security

- Bearer token authentication
- Rate limiting on all endpoints
- Input validation and sanitization
- SQL injection prevention
- XSS protection headers
- CORS configuration

## Issue: #1506