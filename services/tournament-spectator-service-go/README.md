# Tournament Spectator Service

**Enterprise-Grade Tournament Spectator API** for NECPGAME MMOFPS RPG.

## Overview

The Tournament Spectator Service provides real-time tournament spectating capabilities, including live camera controls, replay systems, spectator chat, and tournament statistics broadcasting.

## Features

- **Live Tournament Spectating**: Support for 1000+ concurrent spectators
- **Multiple Camera Modes**: Free, follow, overview, and cinematic camera controls
- **Real-time Replay System**: Variable speed replay with 60fps streaming
- **Spectator Chat**: Real-time messaging with moderation
- **Tournament Statistics**: Live leaderboards and event broadcasting
- **Anti-stream Sniping**: Protection against unfair advantages

## Architecture

### Tech Stack
- **Go 1.21** - High-performance backend
- **PostgreSQL** - Data persistence with pgx driver
- **Redis** - Caching and session management
- **WebSocket** - Real-time spectator feeds
- **Kafka** - Tournament event streaming
- **Chi Router** - HTTP routing with middleware

### Performance Targets
- WebSocket connections: 1000+ concurrent spectators
- Real-time updates: P99 <20ms latency
- Camera switches: <50ms transition time
- Replay streaming: 60fps with <100ms buffering
- Memory per spectator: <10KB active state
- Cache hit rate: >95% for tournament data

## API Endpoints

### Spectator Sessions
- `POST /api/v1/sessions` - Join/create spectator session
- `GET /api/v1/sessions` - List active sessions
- `GET /api/v1/sessions/{session_id}` - Get session details
- `DELETE /api/v1/sessions/{session_id}` - Leave session

### Camera Control
- `PUT /api/v1/sessions/{session_id}/camera` - Update camera settings

### Chat System
- `POST /api/v1/sessions/{session_id}/chat` - Send chat message
- `GET /api/v1/sessions/{session_id}/chat` - Get chat messages

### Tournament Stats
- `GET /api/v1/tournaments/{tournament_id}/stats` - Get live tournament statistics

### Health Check
- `GET /health` - Service health status

## Database Schema

### spectator_sessions
```sql
CREATE TABLE spectator_sessions (
    session_id UUID PRIMARY KEY,
    tournament_id UUID NOT NULL,
    spectator_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP NOT NULL,
    last_activity TIMESTAMP NOT NULL,
    stream_quality VARCHAR(10) DEFAULT 'high',
    nickname VARCHAR(50),
    ip_address INET,
    user_agent TEXT,
    camera_settings JSONB
);
```

### chat_messages
```sql
CREATE TABLE chat_messages (
    message_id UUID PRIMARY KEY,
    session_id UUID NOT NULL,
    sender_id UUID NOT NULL,
    sender_name VARCHAR(50),
    content TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    message_type VARCHAR(10) DEFAULT 'text',
    reply_to UUID,
    FOREIGN KEY (session_id) REFERENCES spectator_sessions(session_id)
);
```

## Configuration

Environment variables:
- `PORT` - Server port (default: 8090)
- `REDIS_ADDR` - Redis address (default: localhost:6379)
- `REDIS_PASSWORD` - Redis password
- `DATABASE_URL` - PostgreSQL connection string

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+

### Setup
```bash
# Install dependencies
go mod download

# Run tests
go test ./...

# Build
go build .

# Run
./tournament-spectator-service
```

### Docker
```bash
# Build
docker build -t tournament-spectator-service .

# Run
docker run -p 8090:8090 tournament-spectator-service
```

## Monitoring

### Health Checks
- HTTP endpoint: `GET /health`
- Returns JSON with service status, version, and timestamp

### Metrics
- Spectator session counts
- WebSocket connection metrics
- Chat message throughput
- Camera control latency

### Logging
- Structured logging with Zap
- Request/response logging
- Error tracking with context

## Security

- JWT authentication for all API endpoints
- Rate limiting on spectator session creation
- IP-based restrictions for suspicious activity
- Anti-stream sniping camera validation
- Chat message moderation and filtering

## Deployment

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tournament-spectator-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: tournament-spectator-service
  template:
    metadata:
      labels:
        app: tournament-spectator-service
    spec:
      containers:
      - name: tournament-spectator-service
        image: tournament-spectator-service:latest
        ports:
        - containerPort: 8090
        env:
        - name: PORT
          value: "8090"
        - name: REDIS_ADDR
          value: "redis-service:6379"
        livenessProbe:
          httpGet:
            path: /health
            port: 8090
          initialDelaySeconds: 30
          periodSeconds: 10
```

## Issue Tracking

- **Issue**: #140875800
- **Performance Requirements**: MMOFPS-grade real-time spectator system
- **API Specification**: `proto/openapi/specialized-domain/tournament-spectator-service.yaml`
