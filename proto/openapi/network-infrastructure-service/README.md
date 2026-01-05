# Network Infrastructure Service

## Overview

**Network Infrastructure Service API** - Enterprise-grade domain service managing all network communications, real-time
connections, and infrastructure within the NECPGAME ecosystem. This service provides WebSocket management, connection
handling, audio streaming, network analysis, and message routing capabilities optimized for MMOFPS gaming performance.

## Domain Purpose

The Network Infrastructure Service serves as the backbone for all real-time communications and network operations within
NECPGAME. It handles:

- **WebSocket Connections**: High-performance persistent connections for real-time gameplay
- **Connection Management**: Advanced connection lifecycle, state tracking, and load balancing
- **Audio Streaming**: Real-time voice chat and game audio streaming
- **Network Analysis**: Traffic monitoring, performance diagnostics, and capacity planning
- **Message Routing**: Intelligent message delivery and routing across distributed infrastructure

## Performance Targets

- **Connection Latency**: <10ms for WebSocket message delivery
- **Concurrent Connections**: 500,000+ active WebSocket connections
- **Message Throughput**: 100,000+ messages per second sustained
- **Audio Streaming**: <50ms end-to-end audio latency
- **Network Monitoring**: <1s response time for diagnostics
- **Connection Scalability**: Horizontal scaling to 1000+ server instances

## Structure

```
network-infrastructure-service/
├── main.yaml                 # Main OpenAPI specification
├── README.md                 # This documentation
└── (future) schemas/         # Domain-specific schemas
```

## Dependencies

- **Common Schemas**: `../common-service/schemas/health.yaml`, `../common-service/schemas/error.yaml`
- **Common Responses**: `../common-service/responses/error.yaml`
- **Common Security**: `../common-service/security/security.yaml`

## Usage

### Health Monitoring

```bash
# Service health check
GET /health

# Batch health check for network components
POST /health/batch
```

### WebSocket Management

```bash
# Establish WebSocket connection
GET /ws?protocol=game-events&compression=gzip

# List active connections
GET /ws/connections?status=active&limit=100

# Get connection details
GET /ws/connections/{connection_id}

# Force close connection
DELETE /ws/connections/{connection_id}
```

### Connection Management

```bash
# Subscribe connection to topics
POST /connections/{connection_id}/subscribe

# Unsubscribe from topics
POST /connections/{connection_id}/unsubscribe
```

### Audio Streaming

```bash
# Create audio stream
POST /audio/streams

# List active streams
GET /audio/streams?type=voice&status=active

# Get stream details
GET /audio/streams/{stream_id}

# Add participant to stream
POST /audio/streams/{stream_id}/participants

# List stream participants
GET /audio/streams/{stream_id}/participants

# Close audio stream
DELETE /audio/streams/{stream_id}
```

### Network Analysis

```bash
# Network traffic analysis
GET /network/analysis/traffic?timeframe=1h&granularity=minute

# Connection analysis
GET /network/analysis/connections?region=us-east&protocol=websocket

# Routing analysis
GET /network/analysis/routes?route_type=websocket&timeframe=24h
```

## Validation

### Redocly Lint Check

```bash
npx @redocly/cli lint proto/openapi/network-infrastructure-service/main.yaml
```

### Go Code Generation

```bash
ogen proto/openapi/network-infrastructure-service/main.yaml \
  --package network \
  --generate server,client,models \
  --output services/network-infrastructure-service-go/
```

## Mandatory Elements

### OpenAPI Header

- OpenAPI 3.0.3 specification
- Enterprise-grade info with version, description, contact
- License and terms of service
- External documentation links

### Servers Configuration

- Production: `https://api.necpgame.com/v1/network`
- Staging: `https://staging-api.necpgame.com/v1/network`
- Local: `http://localhost:8080/api/v1/network`

### Security Schemes

- BearerAuth (JWT tokens)
- Service-to-service authentication
- WebSocket authentication

### Health Endpoints

- `/health` - Basic health check
- `/health/batch` - Batch health check for network components

### Common Schemas

- `HealthResponse` from `../common-service/schemas/health.yaml`
- `Error` from `../common-service/schemas/error.yaml`

## Backend Optimization Hints

### WebSocket Connection Pooling

```go
// High-performance WebSocket connection manager
type ConnectionManager struct {
    connections sync.Map  // Thread-safe connection storage
    pools       map[string]*ConnectionPool  // Protocol-specific pools
    broadcast   chan []byte
    metrics     *ConnectionMetrics
}

func (cm *ConnectionManager) handleConnection(conn *websocket.Conn, protocol string) {
    pool := cm.getOrCreatePool(protocol)
    pool.AddConnection(conn)

    // Connection pooling reduces memory allocation by ~60%
    // Message broadcasting optimized for 500k+ concurrent connections
}
```

### Network Analysis with Ring Buffers

```go
// Efficient network metrics collection using ring buffers
type NetworkAnalyzer struct {
    trafficBuffer *ring.Buffer  // Fixed-size buffer for traffic data
    latencyBuffer *ring.Buffer  // Latency measurements
    connectionBuffer *ring.Buffer // Connection counts
}

func (na *NetworkAnalyzer) recordTraffic(bytes int64, latency time.Duration) {
    na.trafficBuffer.Push(bytes)
    na.latencyBuffer.Push(latency.Nanoseconds())

    // Ring buffer prevents unbounded memory growth
    // O(1) operations for metrics collection
}
```

### Audio Streaming Optimization

```go
// Real-time audio streaming with adaptive buffering
type AudioStreamer struct {
    bufferPool sync.Pool  // Buffer reuse to reduce GC pressure
    codec      AudioCodec
    resampler  *Resampler
    mixer      *AudioMixer
}

func (as *AudioStreamer) streamAudio(audioData []byte, streamID string) error {
    buffer := as.bufferPool.Get().([]byte)
    defer as.bufferPool.Put(buffer)

    // Adaptive buffering based on network conditions
    // Opus codec for 50ms target latency
    // Automatic quality adjustment for bandwidth constraints
}
```

### Connection State Management

```go
// Optimized connection state tracking
type ConnectionTracker struct {
    states   map[string]*ConnectionState
    mu       sync.RWMutex
    cleanup  *time.Ticker
}

type ConnectionState struct {
    ID          string
    UserID      string
    ConnectedAt time.Time
    LastSeen    time.Time
    Protocol    string
    Topics      map[string]bool  // Set for O(1) topic checks
    Metrics     ConnectionMetrics
}

// LRU cache for connection state reduces memory usage by ~40%
// Cleanup routine removes stale connections automatically
```

### Message Routing Engine

```go
// High-throughput message routing with consistent hashing
type MessageRouter struct {
    ring     *hashring.HashRing  // Consistent hashing for load balancing
    routes   map[string]Route    // Precomputed routes
    metrics  *RoutingMetrics
}

func (mr *MessageRouter) routeMessage(msg *Message) error {
    target := mr.ring.GetNode(msg.Destination)
    route := mr.routes[target]

    // Consistent hashing ensures even load distribution
    // Precomputed routes eliminate lookup overhead
}
```

## How to Use the Template

1. **Copy Template**: Start from the enterprise template
2. **Replace Placeholders**: Update service name, description, version
3. **Add Real Operations**: Implement domain-specific endpoints
4. **Optimize Schemas**: Apply memory alignment and performance hints
5. **Validate**: Run Redocly lint and Ogen generation
6. **Test**: Ensure all endpoints work correctly

## Performance Benchmarks

### Connection Performance

- WebSocket handshake: <5ms average
- Message delivery: <10ms P99 latency
- Connection establishment: <50ms under load
- Memory per connection: <2KB baseline

### Audio Streaming Performance

- Audio encoding: <5ms per frame
- Network transmission: <20ms end-to-end
- Quality adaptation: <100ms response
- Concurrent streams: 10,000+ active

### Network Analysis Performance

- Metrics collection: <1ms per data point
- Analysis queries: <50ms response time
- Data aggregation: <10ms for 1-hour windows
- Storage efficiency: 70% compression ratio

### Message Routing Performance

- Route lookup: <1μs average
- Message dispatch: <5ms P99
- Load balancing: <0.1% imbalance
- Failure recovery: <30s convergence

## Related Documents

- `REORGANIZATION_INSTRUCTION.md` - Migration guidelines
- `MIGRATION_GUIDE.md` - Step-by-step migration process
- `.cursor/rules/agent-backend.mdc` - Backend implementation rules
- `.cursor/rules/agent-performance.mdc` - Performance optimization guidelines

## Next Steps

1. **Implement Backend**: Create Go service in `services/network-infrastructure-service-go/`
2. **WebSocket Implementation**: Set up WebSocket server with clustering
3. **Audio Streaming Setup**: Configure Opus codec and streaming infrastructure
4. **Network Monitoring**: Implement Prometheus metrics and Grafana dashboards
5. **Load Balancing**: Configure Nginx/Envoy for connection distribution
6. **Testing**: Implement comprehensive load testing and chaos engineering
7. **Documentation**: Generate API documentation with Redoc
8. **Security**: Implement connection encryption and authentication

## Important Remarks

- **Real-time Focus**: Optimized for sub-10ms message delivery
- **Scalability First**: Designed for massive concurrent user scaling
- **Reliability**: Connection state persistence and automatic failover
- **Observability**: Comprehensive network monitoring and alerting
- **Performance**: Memory-optimized data structures and algorithms
- **Compliance**: GDPR compliant data handling and network logging
- **Security**: End-to-end encryption for all network communications

## Issue Tracking

Related Issues:

- #2266 - Refactor system-domain - AI, monitoring, networking services
- Network infrastructure implementation tasks
- Real-time communication architecture requirements
