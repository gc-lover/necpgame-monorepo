# Notification Service

## Overview

**Notification Service API** - Enterprise-grade domain service managing all forms of notifications, messages, and
real-time communication within the NECPGAME ecosystem. This service provides comprehensive messaging infrastructure
including chat systems, event-driven messaging, message queues, and Kafka-based event streaming.

## Domain Purpose

The Notification Service serves as the central communication hub for NECPGAME, handling:

- **Real-time Chat Systems**: Multi-channel messaging with global, guild, and private conversations
- **Event Streaming**: Kafka-based event publishing for player activities and system events
- **Message Queues**: High-throughput message queuing with priority and TTL support
- **Push Notifications**: Reliable delivery of in-game alerts and system notifications
- **Social Communication**: Community messaging with moderation and formatting capabilities

## Performance Targets

- **Message Delivery**: <20ms P99 latency for real-time messages
- **Throughput**: 100,000+ messages per second sustained
- **Concurrent Connections**: 500,000+ WebSocket connections
- **Queue Processing**: <5ms average dequeue time
- **Event Streaming**: Sub-millisecond publish latency

## Structure

```
notification-service/
├── main.yaml                 # Main OpenAPI specification
├── README.md                 # This documentation
└── (future) schemas/         # Domain-specific schemas
```

## Dependencies

- **Common Schemas**: `../common-service/schemas/health.yaml`, `../common-service/schemas/error.yaml`
- **Common Responses**: `../common-service/responses/error.yaml`, `../common-service/responses/success.yaml`
- **Common Security**: `../common-service/security/security.yaml`

## Usage

### Health Monitoring

```bash
# Service health check
GET /health

# Batch health check for multiple services
POST /health/batch

# WebSocket health monitoring
GET /health/ws
```

### Chat Messaging

```bash
# Get chat messages with filtering
GET /chat/messages?channel_type=GUILD&limit=50

# Send a chat message
POST /chat/messages

# Create a new chat channel
POST /chat/channels

# List available channels
GET /chat/channels?type=PUBLIC
```

### Message Queues

```bash
# Enqueue a message
POST /queues/messages

# Dequeue messages for processing
GET /queues/messages?queue=game-events&max_messages=10

# Acknowledge message processing
PUT /queues/messages/{messageId}/ack

# Create a new queue
POST /queues
```

### Event Streaming

```bash
# Publish an event to Kafka
POST /events/publish

# List available event topics
GET /events/topics
```

### Notifications

```bash
# Get user notifications
GET /notifications?status=unread&type=achievement

# Send a notification to a user
POST /notifications
```

## Validation

### Redocly Lint Check

```bash
npx @redocly/cli lint proto/openapi/notification-service/main.yaml
```

### Go Code Generation

```bash
ogen proto/openapi/notification-service/main.yaml \
  --package notification \
  --generate server,client,models \
  --output services/notification-service-go/
```

## Mandatory Elements

### OpenAPI Header

- OpenAPI 3.0.3 specification
- Enterprise-grade info with version, description, contact
- License and terms of service
- External documentation links

### Servers Configuration

- Production: `https://api.necpgame.com/v1/notification`
- Staging: `https://staging-api.necpgame.com/v1/notification`
- Local: `http://localhost:8080/api/v1/notification`

### Security Schemes

- BearerAuth (JWT tokens)
- Service-to-service authentication
- WebSocket authentication

### Health Endpoints

- `/health` - Basic health check
- `/health/batch` - Batch health check for services
- `/health/ws` - WebSocket real-time health monitoring

### Common Schemas

- `HealthResponse` from `../common-service/schemas/health.yaml`
- `Error` from `../common-service/schemas/error.yaml`
- `WebSocketHealthMessage` for real-time updates

## Backend Optimization Hints

### Memory Alignment

```go
// Struct field alignment for messaging performance
type ChatMessage struct {
    MessageID   string    `json:"message_id"`   // 16 bytes (UUID)
    Timestamp   int64     `json:"timestamp"`   // 8 bytes
    SenderID    string    `json:"sender_id"`   // 16 bytes
    ChannelID   string    `json:"channel_id"`  // 16 bytes
    Content     string    `json:"content"`     // 16 bytes (string header)
    // Total: 72 bytes, perfectly aligned for 64-bit systems
}
```

### Message Queue Optimization

```go
// High-throughput message queue with Redis
queue := redis.NewClient(&redis.Options{
    Addr:     "localhost:6379",
    PoolSize: 100,  // Connection pool for concurrent access
})

// Priority queue implementation
type PriorityQueue struct {
    highPriority   *list.List
    normalPriority *list.List
    lowPriority    *list.List
}
```

### WebSocket Connection Pooling

```go
// WebSocket connection manager for chat
type ConnectionManager struct {
    connections sync.Map  // Thread-safe connection storage
    broadcast   chan []byte
    register    chan *WebSocketConnection
    unregister  chan *WebSocketConnection
}

func (cm *ConnectionManager) run() {
    for {
        select {
        case conn := <-cm.register:
            cm.connections.Store(conn.ID, conn)
        case conn := <-cm.unregister:
            cm.connections.Delete(conn.ID)
        case message := <-cm.broadcast:
            cm.connections.Range(func(key, value interface{}) bool {
                conn := value.(*WebSocketConnection)
                conn.Send(message)
                return true
            })
        }
    }
}
```

### Kafka Event Streaming

```go
// High-performance Kafka producer
producer, err := kafka.NewProducer(&kafka.ConfigMap{
    "bootstrap.servers": "localhost:9092",
    "acks":             "all",  // Ensure delivery
    "compression.type": "lz4", // Compression for throughput
})

// Event publishing with correlation ID
event := BaseEvent{
    EventID:       uuid.New(),
    CorrelationID: getCorrelationID(),
    EventType:     "player_login",
    Source:        "notification-service",
    Timestamp:     time.Now(),
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

### Message Delivery Performance

- Chat messages: <20ms P99 delivery time
- Queue messages: <5ms dequeue time
- Push notifications: <100ms delivery time
- Event publishing: <1ms publish latency

### Throughput Benchmarks

- Chat messages: 50,000+ messages/second
- Queue operations: 100,000+ operations/second
- Event streaming: 200,000+ events/second
- WebSocket connections: 500,000+ concurrent

### Scalability Metrics

- Horizontal scaling: 10+ service instances
- Database connections: 100+ concurrent
- Redis connections: 500+ pooled
- Kafka partitions: 50+ per topic

## Related Documents

- `REORGANIZATION_INSTRUCTION.md` - Migration guidelines
- `MIGRATION_GUIDE.md` - Step-by-step migration process
- `.cursor/rules/agent-backend.mdc` - Backend implementation rules
- `.cursor/rules/agent-performance.mdc` - Performance optimization guidelines

## Next Steps

1. **Implement Backend**: Create Go service in `services/notification-service-go/`
2. **Database Setup**: Configure PostgreSQL/Liquibase migrations
3. **Kafka Setup**: Configure Kafka topics and consumers
4. **Redis Setup**: Configure Redis for message queues and caching
5. **WebSocket Implementation**: Implement real-time messaging
6. **Testing**: Implement comprehensive test suite
7. **Monitoring**: Set up comprehensive monitoring and alerting
8. **Documentation**: Generate API documentation with Redoc

## Important Remarks

- **Real-time Focus**: Optimized for real-time communication patterns
- **Scalability First**: Designed for massive concurrent user base
- **Reliability**: Message delivery guarantees and error recovery
- **Security**: End-to-end encryption for sensitive communications
- **Moderation**: Built-in content moderation and spam protection
- **Compliance**: GDPR compliant data handling and retention
- **Monitoring**: Comprehensive observability for all operations

## Issue Tracking

Related Issues:

- #2266 - Refactor system-domain - AI, monitoring, networking services
- Notification system implementation tasks
- Real-time messaging architecture requirements
