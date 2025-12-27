# WebRTC Signaling Service Go

Enterprise-Grade WebRTC Signaling Service for NECPGAME MMOFPS RPG.

**Domain:** specialized-domain
**Module:** webrtc-signaling-service-go

## Features

- **WebRTC Signaling Protocol** - Complete offer/answer/ICE candidate exchange
- **Real-time Voice Channels** - Guild parties, global chat, private rooms
- **NAT Traversal Support** - STUN/TURN server integration for connectivity
- **Voice Quality Monitoring** - Adaptive bitrate and quality optimization
- **Anti-cheat Integration** - Voice validation and spam detection
- **Scalable Architecture** - 5000+ concurrent voice connections
- **Enterprise-Grade Security** - JWT authentication and encrypted signaling

## Performance Targets

- **P99 Latency:** <25ms for signaling operations
- **Memory:** <50KB per active voice connection
- **Concurrent users:** 5000+ simultaneous voice sessions
- **Signaling throughput:** 10000+ messages/sec
- **Connection establishment:** <100ms average

## API Endpoints

### Health & Monitoring
- `GET /health` - Service health check
- `POST /health/batch` - Batch health check
- `GET /health/ws` - WebSocket health check
- `GET /metrics` - Prometheus metrics

### Voice Channels Management
- `GET /api/v1/voice-channels` - List voice channels
- `POST /api/v1/voice-channels` - Create voice channel
- `GET /api/v1/voice-channels/{channel_id}` - Get voice channel
- `PUT /api/v1/voice-channels/{channel_id}` - Update voice channel
- `DELETE /api/v1/voice-channels/{channel_id}` - Delete voice channel

### Voice Channel Operations
- `POST /api/v1/voice-channels/{channel_id}/join` - Join voice channel
- `POST /api/v1/voice-channels/{channel_id}/signal` - Exchange signaling messages
- `POST /api/v1/voice-channels/{channel_id}/leave` - Leave voice channel

### Voice Quality Monitoring
- `POST /api/v1/voice-quality/{channel_id}/report` - Report voice quality

## Architecture

### Core Components

- **Handlers Layer:** HTTP request/response handling with Chi router
- **Service Layer:** Business logic with WebRTC signaling orchestration
- **Repository Layer:** PostgreSQL + Redis data persistence
- **Configuration:** Environment-based configuration management
- **Logging:** Structured logging with Zap
- **Health Checks:** Comprehensive service monitoring

### Data Flow

```
Client Request → Chi Router → Handler → Service → Repository → PostgreSQL/Redis
                      ↓
                Middleware Stack (Auth, CORS, Logging, Metrics)
```

### Signaling Protocol

```json
{
  "type": "offer",
  "session_id": "session-123",
  "from_user_id": "user-456",
  "to_user_id": "user-789",
  "channel_id": "channel-101",
  "offer": {
    "type": "offer",
    "sdp": "v=0\r\no=- 12345..."
  }
}
```

## Configuration

### Environment Variables

```bash
# Server
SERVER_ADDR=:8080
SERVER_PORT=8080

# Database
DATABASE_URL=postgres://user:password@localhost:5432/webrtc_signaling?sslmode=disable

# Redis
REDIS_URL=redis://localhost:6379

# WebRTC
STUN_SERVER=stun:stun.l.google.com:19302
TURN_SERVER=turn:turn.example.com:3478

# Security
JWT_SECRET=your-secret-key
CORS_ORIGINS=*

# Monitoring
METRICS_ENABLED=true
LOG_LEVEL=info
```

## Building and Running

### Local Development

```bash
# Install dependencies
make deps

# Run with hot reload
make dev

# Run tests
make test

# Check health
make health
```

### Docker

```bash
# Build image
make docker-build

# Run container
make docker-run

# Docker compose
make docker-up
make docker-down
```

### Production

```bash
# Build optimized binary
make build

# Deploy to Kubernetes
make k8s-deploy
```

## Database Schema

### Core Tables

```sql
-- Voice channels
CREATE TABLE voice_channels (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    guild_id UUID,
    owner_id UUID NOT NULL,
    max_users INTEGER NOT NULL DEFAULT 50,
    current_users INTEGER NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Voice participants
CREATE TABLE voice_participants (
    id UUID PRIMARY KEY,
    channel_id UUID NOT NULL REFERENCES voice_channels(id),
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'member',
    is_muted BOOLEAN NOT NULL DEFAULT false,
    is_deafened BOOLEAN NOT NULL DEFAULT false,
    joined_at TIMESTAMP NOT NULL
);

-- Voice quality reports
CREATE TABLE voice_quality_reports (
    id UUID PRIMARY KEY,
    channel_id UUID NOT NULL REFERENCES voice_channels(id),
    user_id UUID NOT NULL,
    bitrate INTEGER NOT NULL,
    packet_loss DECIMAL(5,4) NOT NULL,
    jitter DECIMAL(5,4) NOT NULL,
    latency DECIMAL(5,4) NOT NULL,
    quality VARCHAR(20) NOT NULL,
    reported_at TIMESTAMP NOT NULL
);
```

## Monitoring

### Metrics

- `webrtc_active_connections` - Current active WebSocket connections
- `webrtc_signaling_messages_total` - Total signaling messages processed
- `webrtc_channel_operations_total` - Voice channel operations
- `webrtc_voice_quality_reports_total` - Voice quality reports

### Health Checks

- **Database:** PostgreSQL connection health
- **Redis:** Redis connection health
- **WebSocket:** Active connection count
- **Memory:** Memory usage monitoring

## Security Features

- **JWT Authentication:** Bearer token validation
- **CORS Protection:** Configurable cross-origin policies
- **Rate Limiting:** DDoS protection (configurable)
- **Input Validation:** Comprehensive request sanitization
- **Audit Logging:** All signaling operations logged

## Performance Optimizations

### Memory Management
- **Object Pooling:** Reuse of frequently allocated objects
- **Connection Pooling:** PostgreSQL (25 connections) + Redis optimization
- **Context Timeouts:** All operations have strict timeouts
- **Structured Allocation:** Memory-aligned data structures

### Concurrent Processing
- **Worker Pools:** Configurable concurrent operation processing
- **Non-blocking I/O:** Asynchronous database and Redis operations
- **Circuit Breakers:** Resilience for external service failures
- **Graceful Degradation:** Service continues under load

## Integration Points

### Game Client Integration
```javascript
// Join voice channel
const response = await fetch('/api/v1/voice-channels/channel-123/join', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ user_id: 'user-456' })
});

// WebRTC signaling
const signalingResponse = await fetch('/api/v1/voice-channels/channel-123/signal', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        type: 'offer',
        from_user_id: 'user-456',
        to_user_id: 'user-789',
        channel_id: 'channel-123',
        offer: { type: 'offer', sdp: localDescription.sdp }
    })
});
```

### Backend Service Integration
- **Player Service:** User authentication and profile data
- **Guild Service:** Guild-based voice channel permissions
- **Analytics Service:** Voice quality and usage analytics
- **Notification Service:** Voice channel event notifications

## BACKEND OPTIMIZATION NOTES

- **Struct field alignment:** Large fields first for memory efficiency
- **Expected memory savings:** 30-50% for WebRTC signaling structures
- **Concurrent connections:** Optimized for 5000+ simultaneous users
- **Signaling performance:** <25ms P99 latency for critical paths

## Development Guidelines

### Code Style
- SOLID principles throughout all layers
- Comprehensive error handling and logging
- Unit tests for all business logic
- Integration tests for API endpoints

### Performance Testing
```bash
# Load testing
ab -n 10000 -c 100 http://localhost:8080/health

# Memory profiling
make profile-mem

# CPU profiling
make profile-cpu
```

### Deployment Checklist
- [ ] Environment variables configured
- [ ] Database migrations applied
- [ ] Redis connection established
- [ ] SSL certificates installed
- [ ] Monitoring dashboards configured
- [ ] Load balancer configured
- [ ] Backup strategy implemented

## Contributing

1. Follow Go coding standards
2. Add tests for new features
3. Update documentation
4. Ensure performance targets are met
5. Run full test suite before submitting

## License

Proprietary - NECPGAME Internal Use Only