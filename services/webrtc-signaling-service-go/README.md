# WebRTC Signaling Service

Enterprise-grade WebRTC signaling service for NECPGAME voice chat system.

## Overview

This service provides WebRTC signaling infrastructure for real-time peer-to-peer voice communication in MMOFPS games. It handles channel management, participant coordination, and signaling message exchange with enterprise-grade performance and reliability.

## Features

- **WebRTC Signaling Protocol**: Complete offer/answer/ICE candidate exchange
- **Voice Channel Management**: Guild parties, private rooms, global chat
- **Real-time Participant Tracking**: Join/leave notifications and state sync
- **Quality Monitoring**: Voice quality metrics and adaptive bitrate control
- **NAT Traversal**: STUN/TURN server integration for connectivity
- **Anti-cheat Integration**: Voice validation and spam detection
- **Scalable Architecture**: Support for 5000+ concurrent voice connections

## Performance Targets

- **Signaling Latency**: P99 <25ms for all operations
- **Concurrent Connections**: 5000+ active voice sessions
- **Throughput**: 10,000+ signaling messages/sec
- **Memory Usage**: <50KB per active connection
- **Uptime**: 99.9% availability

## API Endpoints

### Health & Monitoring
- `GET /health` - Service health check
- `POST /health/batch` - Batch health check for multiple services
- `GET /health/ws` - WebSocket health monitoring
- `GET /metrics` - Prometheus metrics

### Voice Channels
- `GET /api/v1/voice-channels` - List voice channels
- `POST /api/v1/voice-channels` - Create voice channel
- `GET /api/v1/voice-channels/{channel_id}` - Get channel details
- `PUT /api/v1/voice-channels/{channel_id}` - Update channel
- `DELETE /api/v1/voice-channels/{channel_id}` - Delete channel

### Voice Operations
- `POST /api/v1/voice-channels/{channel_id}/join` - Join voice channel
- `POST /api/v1/voice-channels/{channel_id}/signal` - WebRTC signaling
- `POST /api/v1/voice-channels/{channel_id}/leave` - Leave voice channel
- `POST /api/v1/voice-quality/{channel_id}/report` - Report voice quality

## Architecture

### Layered Design
- **Handlers Layer**: HTTP/WebSocket request processing
- **Service Layer**: Business logic and validation
- **Repository Layer**: Data persistence and caching
- **Infrastructure**: PostgreSQL + Redis with monitoring

### Technologies
- **Go 1.21+**: High-performance compiled language
- **PostgreSQL**: ACID-compliant data storage
- **Redis**: High-performance caching and pub/sub
- **Chi Router**: Lightweight HTTP router
- **Zap Logger**: Structured logging
- **Prometheus**: Metrics and monitoring

## Configuration

Environment variables:

```bash
# Server
SERVER_ADDR=:8080
ENVIRONMENT=production

# Database
DATABASE_URL=postgres://user:password@host:5432/webrtc_signaling?sslmode=require

# Redis
REDIS_URL=redis://host:6379

# Security
JWT_SECRET=your-256-bit-secret-key-here
```

## Development

### Prerequisites
- Go 1.21+
- PostgreSQL 13+
- Redis 6+
- Docker (optional)

### Setup
```bash
# Install dependencies
make mod-tidy

# Run locally
make run

# Run tests
make test

# Build Docker image
make docker-build

# Run in Docker
make docker-run
```

### Database Schema

The service requires the following PostgreSQL schema:

```sql
-- Voice channels
CREATE TABLE webrtc.voice_channels (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    max_participants INTEGER NOT NULL,
    current_participants INTEGER DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    is_active BOOLEAN DEFAULT true,
    quality_settings JSONB
);

-- Channel participants
CREATE TABLE webrtc.voice_channel_participants (
    channel_id UUID REFERENCES webrtc.voice_channels(id),
    user_id VARCHAR(255) NOT NULL,
    joined_at TIMESTAMP NOT NULL,
    is_muted BOOLEAN DEFAULT false,
    PRIMARY KEY (channel_id, user_id)
);

-- Signaling messages (for analytics)
CREATE TABLE webrtc.signaling_messages (
    id BIGSERIAL PRIMARY KEY,
    channel_id VARCHAR(255),
    sender_id VARCHAR(255) NOT NULL,
    target_id VARCHAR(255) NOT NULL,
    message_type VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Voice quality reports
CREATE TABLE webrtc.voice_quality_reports (
    id BIGSERIAL PRIMARY KEY,
    channel_id VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    latency_ms DECIMAL(8,2),
    packet_loss_percent DECIMAL(5,2),
    jitter_ms DECIMAL(8,2),
    bitrate_bps INTEGER,
    volume_level DECIMAL(3,2),
    timestamp TIMESTAMP NOT NULL
);
```

## Deployment

### Docker
```bash
docker build -t webrtc-signaling-service .
docker run -p 8080:8080 webrtc-signaling-service
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: webrtc-signaling-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: webrtc-signaling
  template:
    metadata:
      labels:
        app: webrtc-signaling
    spec:
      containers:
      - name: webrtc-signaling
        image: necpgame/webrtc-signaling-service:latest
        ports:
        - containerPort: 8080
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
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Monitoring

### Metrics
- `webrtc_signaling_requests_total` - Total requests by method/endpoint/status
- `webrtc_signaling_request_duration_seconds` - Request duration histogram
- `webrtc_signaling_active_connections` - Active voice connections gauge

### Health Checks
- `/health` - Basic health check
- `/ready` - Readiness for traffic
- `/metrics` - Prometheus metrics

### Logging
Structured JSON logs with correlation IDs for request tracing.

## Security

- JWT authentication for API access
- Input validation and sanitization
- CORS protection
- Rate limiting
- Anti-cheat signaling validation

## Integration

### Game Client
```javascript
// Join voice channel
const response = await fetch('/api/v1/voice-channels/guild-123/join', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    user_id: 'player_456',
    client_capabilities: {
      webrtc_support: true,
      dtls_support: true
    }
  })
});

// WebRTC signaling
const signalResponse = await fetch('/api/v1/voice-channels/guild-123/signal', {
  method: 'POST',
  body: JSON.stringify({
    type: 'offer',
    sender_id: 'player_456',
    target_id: 'player_789',
    sdp: offer.sdp
  })
});
```

### Backend Services
The service integrates with:
- **Authentication Service**: User validation
- **Guild Service**: Channel permissions
- **Analytics Service**: Voice usage metrics
- **Anti-cheat Service**: Voice validation

## Contributing

1. Follow Go coding standards
2. Add tests for new features
3. Update documentation
4. Run `make quality` before submitting

## License

MIT License - see LICENSE file for details.
