<!-- Issue: #265 -->

# Voice Chat System Architecture

## Overview

This document defines the complete technical architecture for the voice chat system in NECPGAME, supporting WebRTC-based
communication with party, guild, raid, and proximity channels for MMO FPS RPG gameplay.

## Performance Requirements

**Target Metrics:**

- P99 latency: <100ms for voice transmission
- Concurrent channels: 10,000+ simultaneous
- Max participants per channel: Party (5), Guild (100), Raid (25), Proximity (unlimited, spatial)
- Bandwidth optimization: Adaptive quality based on network conditions
- Scalability: Horizontal scaling with Redis session store

## System Components

### Core Microservices

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Voice Chat    │    │   Social         │    │   World         │
│   Service       │◄──►│   Service        │◄──►│   Service       │
│                 │    │                  │    │                 │
│ • Channel Mgmt  │    │ • Guild/Party    │    │ • Proximity     │
│ • Participant   │    │ • Permissions    │    │ • Coordinates   │
│ • Routing       │    │ • Events         │    │ • Spatial Audio │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                        │                        │
         └────────────────────────┼────────────────────────┘
                                  │
                    ┌─────────────────┐
                    │   Voice        │
                    │   Server       │
                    │   (WebRTC)     │
                    │                 │
                    │ • SFU Router   │
                    │ • Media Server │
                    │ • TURN/STUN    │
                    └─────────────────┘
```

### Infrastructure Components

**Voice Server (WebRTC):**

- **SFU (Selective Forwarding Unit):** Efficient multi-party routing
- **Media Server:** Audio processing, mixing, noise suppression
- **TURN/STUN Servers:** NAT traversal for P2P connections
- **Load Balancer:** Geographic distribution

**Supporting Services:**

- **Redis:** Session store, pub/sub for real-time events
- **PostgreSQL:** Persistent channel/participant data
- **Monitoring:** Prometheus metrics, Grafana dashboards

## Channel Types Architecture

### 1. Party Channels (2-5 players)

**Use Case:** Small team coordination, tactical communication
**Characteristics:**

- Low latency priority (<50ms)
- High quality audio (48kHz, stereo)
- Push-to-talk or voice activity detection
- Temporary channels (auto-cleanup when empty)

### 2. Guild Channels (up to 100 players)

**Use Case:** Large community communication
**Characteristics:**

- Medium latency tolerance (<100ms)
- Adaptive quality based on participant count
- Role-based permissions (officers, members)
- Persistent channels with moderation

### 3. Raid Channels (10-25 players with subchannels)

**Use Case:** Coordinated boss fights, complex encounters
**Characteristics:**

- Tactical subchannels (tank/heal/dps groups)
- Priority voice for raid leaders
- Automatic ducking for critical announcements
- Combat-aware muting

### 4. Proximity Channels (spatial, unlimited players)

**Use Case:** Immersive world interaction, ambient audio
**Characteristics:**

- 3D spatial audio with distance attenuation
- Dynamic participant management based on coordinates
- Environmental audio mixing
- Performance optimization (limited range)

## WebRTC Connection Architecture

### Connection Flow

```
Client Request ──► Voice Service ──► Token Generation
       │                   │
       │                   ▼
       └────────────► Voice Server ◄──── Redis Session
                       │   │
                       ▼   ▼
                  TURN/STUN   SFU Router
                       │
                       ▼
                  P2P/WebRTC Connection
```

### Connection States

```yaml
connection_states:
  - connecting: Initial WebRTC handshake
  - connected: Active audio stream
  - reconnecting: Temporary network issues
  - muted: Audio disabled locally
  - deafened: Audio disabled remotely
  - disconnected: Connection lost
```

### Quality Adaptation

**Adaptive Bitrate:**

- Network condition monitoring
- Automatic quality reduction under poor connectivity
- Priority-based audio allocation

**Codec Selection:**

- Primary: Opus (variable bitrate)
- Fallback: PCMU/PCMA for compatibility
- Spatial: Custom 3D audio processing

## Database Schema Design

### Core Tables

**voice_channels:**

```sql
CREATE TABLE voice_channels (
    id UUID PRIMARY KEY,
    type VARCHAR(20) NOT NULL, -- party, guild, raid, proximity
    owner_id UUID REFERENCES players(id),
    guild_id UUID REFERENCES guilds(id), -- NULL for non-guild channels
    raid_id UUID REFERENCES raids(id), -- NULL for non-raid channels
    name VARCHAR(100),
    max_participants INTEGER,
    is_persistent BOOLEAN DEFAULT false,
    settings JSONB, -- codec, quality, permissions
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Optimized indexes
CREATE INDEX idx_voice_channels_type ON voice_channels(type);
CREATE INDEX idx_voice_channels_guild ON voice_channels(guild_id);
CREATE INDEX idx_voice_channels_active ON voice_channels(updated_at) WHERE updated_at > NOW() - INTERVAL '1 hour';
```

**voice_participants:**

```sql
CREATE TABLE voice_participants (
    id UUID PRIMARY KEY,
    channel_id UUID REFERENCES voice_channels(id) ON DELETE CASCADE,
    player_id UUID REFERENCES players(id) ON DELETE CASCADE,
    session_token VARCHAR(255) UNIQUE,
    state JSONB, -- {muted: boolean, deafened: boolean, speaking: boolean}
    joined_at TIMESTAMP WITH TIME ZONE,
    last_activity TIMESTAMP WITH TIME ZONE,
    coordinates JSONB, -- For proximity channels {x, y, z}
    permissions JSONB, -- Channel-specific permissions
    UNIQUE(channel_id, player_id)
);

-- Performance indexes
CREATE INDEX idx_voice_participants_channel ON voice_participants(channel_id);
CREATE INDEX idx_voice_participants_player ON voice_participants(player_id);
CREATE INDEX idx_voice_participants_active ON voice_participants(last_activity) WHERE last_activity > NOW() - INTERVAL '5 minutes';
```

**voice_channel_permissions:**

```sql
CREATE TABLE voice_channel_permissions (
    id UUID PRIMARY KEY,
    channel_id UUID REFERENCES voice_channels(id) ON DELETE CASCADE,
    player_id UUID REFERENCES players(id) ON DELETE CASCADE,
    permission VARCHAR(50), -- speak, mute_others, kick, etc.
    granted_by UUID REFERENCES players(id),
    granted_at TIMESTAMP WITH TIME ZONE,
    UNIQUE(channel_id, player_id, permission)
);
```

### Partitioning Strategy

**Time-based partitioning for high-volume tables:**

```sql
-- Partition voice_participants by month for historical data
CREATE TABLE voice_participants_y2025m01 PARTITION OF voice_participants
    FOR VALUES FROM ('2025-01-01') TO ('2025-02-01');
```

## API Design (High Level)

### Channel Management API

```
POST   /api/v1/voice/channels              # Create channel
GET    /api/v1/voice/channels/{id}         # Get channel info
PUT    /api/v1/voice/channels/{id}         # Update channel settings
DELETE /api/v1/voice/channels/{id}         # Delete channel

POST   /api/v1/voice/channels/{id}/join    # Join channel
POST   /api/v1/voice/channels/{id}/leave   # Leave channel
GET    /api/v1/voice/channels/{id}/participants # List participants
```

### Participant Management API

```
PUT    /api/v1/voice/channels/{id}/participants/{playerId}/mute
PUT    /api/v1/voice/channels/{id}/participants/{playerId}/deafen
PUT    /api/v1/voice/channels/{id}/participants/{playerId}/volume
POST   /api/v1/voice/channels/{id}/participants/{playerId}/kick
```

### WebRTC Connection API

```
POST   /api/v1/voice/connection/token       # Get WebRTC token
GET    /api/v1/voice/connection/ice         # Get ICE servers
POST   /api/v1/voice/connection/quality     # Report connection quality
```

## Spatial Audio System

### 3D Audio Processing

**Distance-based attenuation:**

```
attenuation = 1.0 / (1.0 + distance * falloff_factor)
where:
- distance: Euclidean distance between players
- falloff_factor: Configurable per environment (0.1 for indoor, 0.05 for outdoor)
```

**Directional audio:**

- Head-related transfer function (HRTF) for realistic 3D positioning
- Doppler effect for moving sound sources
- Occlusion processing for walls/obstacles

### Proximity Channel Management

**Dynamic participant updates:**

1. Player moves → World service publishes coordinate update
2. Voice service receives update → Calculates audible players
3. SFU router updates audio streams
4. Client receives new participant list

**Performance optimization:**

- Spatial partitioning (octree/grid)
- Distance culling (max audible range: 50 meters)
- Priority-based streaming (closer players first)

## Integration Patterns

### Event-Driven Communication

**Redis Pub/Sub channels:**

- `voice:channel:{id}:events` - Channel state changes
- `voice:player:{id}:updates` - Player status updates
- `voice:proximity:{zone}:updates` - Spatial audio updates

### Service Mesh Integration

**Envoy configuration for service-to-service:**

```yaml
routes:
  - match:
      prefix: "/api/v1/voice"
    route:
      cluster: voice-chat-service
      timeout: 30s
      retry_policy:
        retry_on: "5xx"
        num_retries: 3
```

## Monitoring and Observability

### Key Metrics

**Voice Quality:**

- Audio latency (P50, P95, P99)
- Packet loss percentage
- Jitter buffer size
- Connection success rate

**System Performance:**

- Active channels count
- Concurrent participants
- CPU usage per SFU
- Memory usage per channel type

**Business Metrics:**

- Average session duration
- Channel type distribution
- User engagement (voice time per session)

### Alerting Rules

```
ALERT VoiceHighLatency
  IF voice_latency_p95 > 200ms FOR 5m
  LABELS { severity = "warning" }

ALERT VoiceConnectionFailure
  IF voice_connection_success_rate < 95% FOR 10m
  LABELS { severity = "critical" }
```

## Security Considerations

### Authentication & Authorization

**Token-based access:**

- JWT tokens with channel permissions
- Short-lived tokens (15 minutes)
- Channel-specific access control

### Anti-Abuse Measures

**Rate limiting:**

- Connection attempts per IP
- Channel creation per user
- API calls per minute

**Content moderation:**

- Voice activity detection
- Automatic muting for detected abuse
- Report system integration

## Deployment Architecture

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: voice-chat-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: voice-service
        image: necpgame/voice-chat-service:v1.0.0
        resources:
          requests:
            cpu: 500m
            memory: 1Gi
          limits:
            cpu: 2000m
            memory: 4Gi
      - name: voice-server
        image: necpgame/voice-server:v1.0.0
        ports:
        - containerPort: 3478  # STUN
        - containerPort: 5349  # TURN
        - containerPort: 8080  # WebRTC
```

### Scaling Strategy

**Horizontal Pod Autoscaling:**

```yaml
metrics:
- type: Resource
  resource:
    name: cpu
    target:
      type: Utilization
      averageUtilization: 70
- type: Pods
  pods:
    metric:
      name: voice_active_channels
    target:
      type: AverageValue
      averageValue: "100"
```

## Migration Strategy

### Phase 1: Core Infrastructure

1. Deploy voice service microservice
2. Set up WebRTC infrastructure
3. Implement basic party channels

### Phase 2: Extended Features

1. Add guild and raid channels
2. Implement proximity audio
3. Add spatial processing

### Phase 3: Advanced Features

1. Custom audio processing
2. Advanced moderation tools
3. Analytics and monitoring

## Success Criteria

- [ ] All channel types functional (party, guild, raid, proximity)
- [ ] WebRTC connections stable under load
- [ ] Spatial audio working in proximity channels
- [ ] P99 latency <100ms for voice transmission
- [ ] Support for 10,000+ concurrent channels
- [ ] Integration with social and world services complete
- [ ] Comprehensive monitoring and alerting in place

## Risk Assessment

### High Risk

- **WebRTC complexity:** Mitigated by using proven SFU implementation
- **Audio quality consistency:** Mitigated by adaptive bitrate
- **Scalability at MMO scale:** Mitigated by horizontal scaling design

### Medium Risk

- **Network NAT traversal:** Mitigated by TURN/STUN servers
- **Audio synchronization:** Mitigated by timestamp-based sync
- **Cross-platform compatibility:** Mitigated by WebRTC standards

## Next Steps

### Immediate Tasks

1. **API Designer:** Create OpenAPI specification for voice endpoints
2. **Database:** Implement channel and participant tables
3. **Backend:** Implement voice service handlers
4. **Network:** Configure WebRTC infrastructure
5. **DevOps:** Set up Kubernetes deployment

### Subsystem Breakdown

- Voice Service (Go microservice)
- Voice Server (WebRTC SFU)
- Database schema and migrations
- Client integration (UE5)
- Monitoring and alerting setup

This architecture provides a scalable, high-performance voice chat system suitable for MMO FPS RPG gameplay with support
for all required channel types and spatial audio features.