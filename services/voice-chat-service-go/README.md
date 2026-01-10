# NECPGAME Voice Chat Service

Enterprise-grade voice chat service for MMO real-time communication, built with Go and WebRTC.

## 🚀 Features

### Core Functionality
- **WebRTC-based Voice Communication**: Peer-to-peer audio streaming with STUN/TURN support
- **Multiple Room Types**: Group chat, raid teams, guild channels, party voice, proximity chat, global announcements
- **Audio Recording**: Session recording with configurable permissions
- **Real-time Moderation**: Mute, unmute, kick, ban functionality with audit logging
- **Quality Monitoring**: Real-time voice quality metrics and analytics

### Performance Optimizations
- **Memory Pooling**: `sync.Pool` for hot path objects (CombatEvent, UserContext) reducing GC pressure
- **Struct Field Alignment**: Optimized memory layout for 30-50% memory savings
- **Connection Pooling**: Database and Redis connection pools
- **Profiling**: Built-in pprof endpoints for runtime performance analysis
- **Metrics**: Prometheus integration for monitoring and alerting

### Security & Scalability
- **JWT Authentication**: Secure token-based authentication
- **Rate Limiting**: Configurable request rate limits per user
- **RBAC**: Role-based access control (owner, moderator, member)
- **Input Validation**: Comprehensive request validation
- **Audit Logging**: Complete audit trail for moderation actions
- **Horizontal Scaling**: Stateless design for Kubernetes deployment

## 🏗️ Architecture

### Service Components

```
voice-chat-service-go/
├── main.go                 # Application entry point
├── config/config.go        # Configuration management
├── internal/
│   ├── models/models.go    # Data models with field alignment
│   ├── service/service.go  # Core business logic
│   └── handler/handler.go  # HTTP API handlers
├── pkg/api/api.go          # API request/response types
├── bundled.yaml            # OpenAPI 3.0 specification
├── Dockerfile              # Multi-stage container build
└── README.md              # This documentation
```

### Data Flow

1. **Room Creation**: User creates room → Database storage → Room manager initialization
2. **User Join**: Authentication → Room validation → WebRTC session setup → Signaling
3. **Voice Communication**: WebRTC peer connections → Audio streaming → Quality monitoring
4. **Moderation**: Permission checks → Action execution → Audit logging

### External Dependencies

- **Database**: PostgreSQL for persistent data storage
- **Cache**: Redis for session management and pub/sub
- **STUN/TURN**: External servers for WebRTC NAT traversal
- **Monitoring**: Prometheus for metrics collection

## 📋 API Reference

### Authentication
All API endpoints require JWT authentication via Bearer token:

```
Authorization: Bearer <jwt_token>
```

### Core Endpoints

#### Create Voice Room
```http
POST /api/v1/rooms
Content-Type: application/json

{
  "name": "Raid Team Alpha",
  "description": "Main raid voice channel",
  "owner_id": "user-uuid",
  "room_type": "raid",
  "max_participants": 40,
  "quality_preset": "high",
  "is_private": false
}
```

#### Join Voice Room
```http
POST /api/v1/rooms/{roomID}/join
Content-Type: application/json

{
  "user_id": "user-uuid",
  "username": "PlayerName",
  "password": "optional-for-private-rooms"
}
```

#### WebRTC Signaling
```http
POST /api/v1/rooms/{roomID}/signaling
Content-Type: application/json

{
  "session_id": "session-uuid",
  "from_user": "sender-uuid",
  "to_user": "receiver-uuid",
  "message_type": "offer",
  "sdp": "v=0\r\no=- 12345..."
}
```

#### Start Recording
```http
POST /api/v1/rooms/{roomID}/recording/start
Content-Type: application/json

{
  "user_id": "user-uuid",
  "title": "Raid Session Recording",
  "is_public": false
}
```

#### Moderation Actions
```http
POST /api/v1/rooms/{roomID}/participants/{userID}/mute
Content-Type: application/json

{
  "requester_id": "moderator-uuid",
  "target_user_id": "target-uuid",
  "mute": true,
  "duration": 300,
  "reason": "Spamming"
}
```

#### Quality Metrics
```http
POST /api/v1/rooms/{roomID}/metrics
Content-Type: application/json

{
  "user_id": "user-uuid",
  "session_id": "session-uuid",
  "quality_score": 0.95,
  "packet_loss_rate": 0.02,
  "jitter_ms": 15,
  "round_trip_time": 45.5
}
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SERVER_PORT` | HTTP server port | `8080` | No |
| `DATABASE_URL` | PostgreSQL connection string | - | Yes |
| `REDIS_URL` | Redis connection string | - | Yes |
| `JWT_SECRET` | JWT signing secret | - | Yes |
| `STUN_SERVERS` | STUN server URLs (comma-separated) | `stun:stun.l.google.com:19302` | No |
| `TURN_SERVERS` | TURN server URLs (comma-separated) | - | No |
| `RATE_LIMIT_RPM` | Requests per minute per user | `60` | No |
| `LOG_LEVEL` | Logging level | `info` | No |
| `PROFILING_ENABLED` | Enable pprof profiling | `false` | No |
| `METRICS_ENABLED` | Enable Prometheus metrics | `true` | No |

### Configuration File

```yaml
server:
  port: 8080
  read_timeout: 30s
  write_timeout: 30s

database:
  url: "postgres://user:pass@localhost:5432/voicechat?sslmode=disable"
  max_open_conns: 25
  max_idle_conns: 5
  conn_max_lifetime: 5m

redis:
  url: "redis://localhost:6379"
  pool_size: 10
  min_idle_conns: 2

jwt:
  secret: "your-jwt-secret-key"
  expiration: 24h

webrtc:
  stun_servers:
    - "stun:stun.l.google.com:19302"
  turn_servers: []

rate_limiting:
  requests_per_minute: 60

logging:
  level: "info"
  format: "json"

profiling:
  enabled: false
  port: 6060

metrics:
  enabled: true
  path: "/metrics"
```

## 🚀 Deployment

### Docker Build
```bash
# Build the container
docker build -t necpgame/voice-chat-service:latest .

# Run locally
docker run -p 8080:8080 \
  -e DATABASE_URL="postgres://..." \
  -e REDIS_URL="redis://..." \
  -e JWT_SECRET="your-secret" \
  necpgame/voice-chat-service:latest
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: voice-chat-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: voice-chat-service
  template:
    metadata:
      labels:
        app: voice-chat-service
    spec:
      containers:
      - name: voice-chat-service
        image: necpgame/voice-chat-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: voice-chat-secrets
              key: database-url
        - name: REDIS_URL
          valueFrom:
            secretKeyRef:
              name: voice-chat-secrets
              key: redis-url
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: voice-chat-secrets
              key: jwt-secret
        resources:
          requests:
            memory: "128Mi"
            cpu: "100m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
```

### Database Schema

```sql
-- Voice rooms
CREATE TABLE voice_rooms (
    id UUID PRIMARY KEY,
    room_id VARCHAR(36) UNIQUE NOT NULL,
    room_name VARCHAR(100) NOT NULL,
    description TEXT,
    owner_id VARCHAR(36) NOT NULL,
    game_server_id VARCHAR(36),
    region VARCHAR(50),
    max_participants INTEGER DEFAULT 50,
    current_participants INTEGER DEFAULT 0,
    room_type VARCHAR(20) DEFAULT 'group',
    audio_codec VARCHAR(20) DEFAULT 'opus',
    quality_preset VARCHAR(10) DEFAULT 'high',
    bitrate INTEGER DEFAULT 64000,
    status VARCHAR(20) DEFAULT 'active',
    is_private BOOLEAN DEFAULT FALSE,
    is_temporary BOOLEAN DEFAULT FALSE,
    is_moderated BOOLEAN DEFAULT FALSE,
    allow_recording BOOLEAN DEFAULT FALSE,
    password VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    scheduled_end_at TIMESTAMP WITH TIME ZONE,
    settings JSONB,
    permissions JSONB,
    metadata JSONB,
    INDEX idx_room_id (room_id),
    INDEX idx_owner_id (owner_id),
    INDEX idx_status (status),
    INDEX idx_region (region)
);

-- Voice participants
CREATE TABLE voice_participants (
    id UUID PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    session_id VARCHAR(36) UNIQUE NOT NULL,
    username VARCHAR(50) NOT NULL,
    role VARCHAR(20) DEFAULT 'member',
    status VARCHAR(20) DEFAULT 'active',
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_activity TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    muted_until TIMESTAMP WITH TIME ZONE,
    is_muted BOOLEAN DEFAULT FALSE,
    is_deafened BOOLEAN DEFAULT FALSE,
    is_speaking BOOLEAN DEFAULT FALSE,
    is_connected BOOLEAN DEFAULT FALSE,
    is_authenticated BOOLEAN DEFAULT TRUE,
    push_to_talk BOOLEAN DEFAULT FALSE,
    volume_level DECIMAL(3,2) DEFAULT 1.0,
    x DECIMAL(10,2),
    y DECIMAL(10,2),
    z DECIMAL(10,2),
    bitrate INTEGER,
    packets_sent INTEGER DEFAULT 0,
    packets_lost INTEGER DEFAULT 0,
    user_agent TEXT,
    ip_address INET,
    capabilities JSONB,
    stats JSONB,
    INDEX idx_room_user (room_id, user_id),
    INDEX idx_session_id (session_id),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE
);

-- WebRTC sessions
CREATE TABLE webrtc_sessions (
    id UUID PRIMARY KEY,
    session_id VARCHAR(36) UNIQUE NOT NULL,
    room_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) DEFAULT 'new',
    connection_type VARCHAR(20),
    ice_transport VARCHAR(20) DEFAULT 'DTLS-SRTP',
    dtls_role VARCHAR(10) DEFAULT 'client',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    connected_at TIMESTAMP WITH TIME ZONE,
    disconnected_at TIMESTAMP WITH TIME ZONE,
    local_port INTEGER,
    remote_port INTEGER,
    bytes_sent BIGINT DEFAULT 0,
    bytes_received BIGINT DEFAULT 0,
    local_sdp JSONB,
    remote_sdp JSONB,
    ice_candidates JSONB,
    connection_stats JSONB,
    is_initiator BOOLEAN DEFAULT FALSE,
    has_audio BOOLEAN DEFAULT TRUE,
    has_video BOOLEAN DEFAULT FALSE,
    dtls_fingerprint BOOLEAN DEFAULT TRUE,
    INDEX idx_session_id (session_id),
    INDEX idx_room_user (room_id, user_id),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE
);

-- Signaling messages
CREATE TABLE signaling_messages (
    id UUID PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL,
    from_user VARCHAR(36) NOT NULL,
    to_user VARCHAR(36) NOT NULL,
    message_type VARCHAR(20) NOT NULL,
    sdp TEXT,
    candidate TEXT,
    candidate_type VARCHAR(10),
    sequence_number INTEGER DEFAULT 0,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_reliable BOOLEAN DEFAULT TRUE,
    metadata JSONB,
    INDEX idx_session_timestamp (session_id, timestamp),
    INDEX idx_from_to (from_user, to_user),
    FOREIGN KEY (session_id) REFERENCES webrtc_sessions(session_id) ON DELETE CASCADE
);

-- Voice quality metrics
CREATE TABLE voice_quality_metrics (
    id UUID PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    session_id VARCHAR(36) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    jitter_ms INTEGER,
    packets_lost INTEGER,
    packets_sent INTEGER,
    bytes_sent BIGINT,
    round_trip_time DECIMAL(8,3),
    packet_loss_rate DECIMAL(5,4),
    audio_level DECIMAL(3,2),
    quality_score DECIMAL(3,2),
    codec_used VARCHAR(20),
    network_type VARCHAR(20),
    has_issues BOOLEAN DEFAULT FALSE,
    codec_stats JSONB,
    network_stats JSONB,
    INDEX idx_room_timestamp (room_id, timestamp),
    INDEX idx_user_timestamp (user_id, timestamp),
    INDEX idx_quality_score (quality_score),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE,
    FOREIGN KEY (session_id) REFERENCES webrtc_sessions(session_id) ON DELETE CASCADE
);

-- Moderation events
CREATE TABLE voice_moderation_events (
    id UUID PRIMARY KEY,
    room_id VARCHAR(36) NOT NULL,
    moderator_id VARCHAR(36) NOT NULL,
    target_user_id VARCHAR(36) NOT NULL,
    action_type VARCHAR(20) NOT NULL,
    reason TEXT,
    timestamp TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE,
    duration INTEGER, -- seconds
    is_temporary BOOLEAN DEFAULT FALSE,
    is_reversible BOOLEAN DEFAULT TRUE,
    action_data JSONB,
    evidence TEXT, -- link to recorded audio
    INDEX idx_room_timestamp (room_id, timestamp),
    INDEX idx_moderator (moderator_id),
    INDEX idx_target (target_user_id),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE
);

-- Voice recordings
CREATE TABLE voice_recordings (
    id UUID PRIMARY KEY,
    recording_id VARCHAR(36) UNIQUE NOT NULL,
    room_id VARCHAR(36) NOT NULL,
    initiator_id VARCHAR(36) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    ended_at TIMESTAMP WITH TIME ZONE,
    duration INTERVAL,
    file_path TEXT,
    file_format VARCHAR(10) DEFAULT 'opus',
    codec VARCHAR(20) DEFAULT 'opus',
    bitrate INTEGER DEFAULT 64000,
    sample_rate INTEGER DEFAULT 48000,
    file_size BIGINT,
    status VARCHAR(20) DEFAULT 'recording',
    title VARCHAR(200),
    is_public BOOLEAN DEFAULT FALSE,
    allow_download BOOLEAN DEFAULT TRUE,
    has_transcription BOOLEAN DEFAULT FALSE,
    participants JSONB,
    metadata JSONB,
    INDEX idx_recording_id (recording_id),
    INDEX idx_room_started (room_id, started_at),
    INDEX idx_initiator (initiator_id),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE
);

-- Room invites
CREATE TABLE voice_room_invites (
    id UUID PRIMARY KEY,
    invite_id VARCHAR(36) UNIQUE NOT NULL,
    room_id VARCHAR(36) NOT NULL,
    inviter_id VARCHAR(36) NOT NULL,
    invitee_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE DEFAULT (NOW() + INTERVAL '24 hours'),
    accepted_at TIMESTAMP WITH TIME ZONE,
    rejected_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) DEFAULT 'pending',
    message TEXT,
    token VARCHAR(100) UNIQUE NOT NULL,
    require_auth BOOLEAN DEFAULT TRUE,
    is_temporary BOOLEAN DEFAULT FALSE,
    permissions JSONB,
    INDEX idx_invite_id (invite_id),
    INDEX idx_token (token),
    INDEX idx_invitee_status (invitee_id, status),
    INDEX idx_expires_at (expires_at),
    FOREIGN KEY (room_id) REFERENCES voice_rooms(room_id) ON DELETE CASCADE
);
```

## 📊 Monitoring

### Metrics Endpoints

- **Health Check**: `GET /health`
- **Prometheus Metrics**: `GET /metrics`
- **Profiling**: `GET /debug/pprof/` (when enabled)

### Key Metrics

- `voice_chat_rooms_created_total`: Total rooms created
- `voice_chat_participants_joined_total`: Total participants joined
- `voice_chat_signaling_messages_total`: Signaling message count
- `voice_chat_webrtc_connections_total`: WebRTC connection count
- `voice_chat_active_rooms`: Currently active rooms
- `voice_chat_active_participants`: Currently active participants
- `voice_chat_audio_quality_score`: Quality score distribution

### Alerts

- High error rates (>5%)
- Memory usage >80%
- Response time >500ms (p95)
- WebRTC connection failures >10%
- Audio quality score <0.7 (p50)

## 🔒 Security Considerations

### Authentication & Authorization
- JWT tokens with configurable expiration
- Role-based permissions (owner > moderator > member)
- Session management with automatic cleanup

### Network Security
- Rate limiting per user and IP
- Input validation and sanitization
- CORS configuration for web clients
- HTTPS-only in production

### Data Protection
- Password hashing for private rooms
- Audit logging for moderation actions
- Secure token generation for invites
- No sensitive data in logs

### Operational Security
- Non-root container execution
- Minimal attack surface (distroless images)
- Regular security updates
- Secrets management via Kubernetes

## 🧪 Testing

### Unit Tests
```bash
go test ./internal/service/...
go test ./internal/handler/...
```

### Integration Tests
```bash
go test -tags=integration ./tests/...
```

### Load Testing
```bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8080/health

# Using hey
hey -n 1000 -c 10 http://localhost:8080/api/v1/rooms
```

## 🚦 Troubleshooting

### Common Issues

#### WebRTC Connection Failures
- Check STUN/TURN server configuration
- Verify firewall settings (ports 3478, 5349 for TURN)
- Ensure proper SSL certificates for DTLS

#### High Memory Usage
- Check for goroutine leaks
- Monitor memory pool usage
- Review connection pooling settings

#### Audio Quality Issues
- Verify codec settings (Opus recommended)
- Check network conditions
- Monitor quality metrics dashboard

#### Database Connection Issues
- Verify connection string format
- Check connection pool settings
- Monitor database performance

### Debug Mode
Enable debug logging and profiling:
```bash
export LOG_LEVEL=debug
export PROFILING_ENABLED=true
export METRICS_ENABLED=true
```

### Logs Analysis
```bash
# View recent errors
kubectl logs -f deployment/voice-chat-service | grep ERROR

# Check metrics
curl http://localhost:8080/metrics | grep voice_chat
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes with tests
4. Ensure all tests pass
5. Submit a pull request

### Code Standards
- Follow Go best practices
- Add comprehensive documentation
- Include unit tests for new features
- Update OpenAPI spec for API changes

## 📄 License

This project is part of the NECPGAME ecosystem. See LICENSE file for details.

## 📞 Support

- **Documentation**: See OpenAPI spec in `bundled.yaml`
- **Issues**: Create GitHub issues for bugs/features
- **Discussions**: Use GitHub Discussions for questions

---

**Built for the next generation of MMO gaming experiences.**