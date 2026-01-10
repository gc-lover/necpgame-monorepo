# Message Queue Service

Enterprise-grade message queue service for NECPGAME, providing high-throughput inter-service communication with Kafka backend.

## Features

- **High-Performance**: Optimized for MMOFPS scale with sub-millisecond latency
- **Kafka Integration**: Full Kafka protocol support with partitioning and replication
- **Enterprise Security**: JWT authentication, rate limiting, RBAC
- **Monitoring**: Comprehensive Prometheus metrics and health checks
- **Reliability**: Message persistence, dead letter queues, retry logic
- **Scalability**: Consumer groups, load balancing, horizontal scaling

## Architecture

### Components

- **Producer API**: HTTP REST API for message production
- **Consumer Management**: Consumer group coordination and load balancing
- **Topic Management**: Dynamic topic creation and configuration
- **Metrics & Monitoring**: Real-time performance monitoring
- **Health Checks**: Service availability and dependency monitoring

### Data Flow

```
Producers → HTTP API → Kafka Topics → Consumer Groups → Services
     ↓           ↓           ↓           ↓           ↓
  Auth/Rate    Validation  Partitioning  Load Balance Processing
   Limiting                              Balancing
```

## API Endpoints

### Message Operations

#### Produce Message
```http
POST /api/v1/messages
Content-Type: application/json

{
  "topic": "user-events",
  "key": "user-123",
  "content": "{\"event\":\"login\",\"user_id\":\"123\"}",
  "headers": {"source": "auth-service"},
  "priority": 1,
  "ttl": "3600000000000"
}
```

#### Consume Messages
```http
POST /api/v1/messages/consume
Content-Type: application/json

{
  "topic": "user-events",
  "group_id": "user-processor",
  "consumer_id": "consumer-1",
  "max_messages": 10,
  "timeout": "30000000000"
}
```

#### Acknowledge Message
```http
POST /api/v1/messages/acknowledge
Content-Type: application/json

{
  "message_id": "msg-12345",
  "topic": "user-events",
  "success": true,
  "group_id": "user-processor"
}
```

### Topic Management

#### Create Topic
```http
POST /api/v1/topics
Content-Type: application/json

{
  "name": "user-events",
  "partitions": 3,
  "replication": 3,
  "retention": "604800000000000",
  "max_size": "1073741824"
}
```

#### Get Topic Info
```http
GET /api/v1/topics/{name}
```

### Consumer Management

#### Register Consumer
```http
POST /api/v1/consumers
Content-Type: application/json

{
  "consumer_id": "consumer-1",
  "group_id": "user-processor",
  "topics": ["user-events", "order-events"],
  "max_concurrency": 5,
  "rate_limit": 100
}
```

### Monitoring

#### Get Queue Metrics
```http
GET /api/v1/metrics?start_time=2024-01-01T00:00:00Z&end_time=2024-01-02T00:00:00Z&topic=user-events
```

#### Health Check
```http
GET /health
```

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `MESSAGE_QUEUE_PORT` | `8087` | HTTP server port |
| `METRICS_ADDR` | `:9097` | Metrics server address |
| `PPROF_ADDR` | `:6067` | Profiling server address |
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_USER` | `postgres` | PostgreSQL user |
| `DB_PASSWORD` | `postgres` | PostgreSQL password |
| `DB_NAME` | `necpgame` | PostgreSQL database |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `KAFKA_BROKERS` | `localhost:9092` | Kafka brokers |
| `MESSAGE_QUEUE_MAX_SIZE` | `1048576` | Max message size in bytes |
| `MESSAGE_QUEUE_TTL` | `24h` | Default message TTL |

### Kafka Configuration

The service supports full Kafka configuration including:

- Multiple brokers
- SASL authentication
- TLS encryption
- Custom serializers
- Partition strategies
- Consumer group management

## Performance Optimization

### Memory Management
- **Memory Pools**: `sync.Pool` for hot path objects (messages, consumers, batches)
- **Struct Alignment**: 30-50% memory savings through optimal field ordering
- **Zero Allocations**: Critical paths avoid heap allocations

### Network Optimization
- **Connection Pooling**: Persistent Kafka connections
- **Batch Operations**: Efficient message batching
- **Compression**: LZ4 compression for message payloads

### Monitoring
- **P99 Latency**: <15ms target for message operations
- **Throughput**: 10,000+ messages/second
- **Memory Usage**: <100MB baseline
- **GC Pressure**: <1ms pause time

## Security

### Authentication
- JWT token validation
- Configurable token expiration
- Role-based access control

### Authorization
- Topic-level permissions
- Consumer group restrictions
- Rate limiting per user/service

### Data Protection
- Message encryption at rest
- TLS transport encryption
- Audit logging for all operations

## Deployment

### Docker Build
```bash
docker build -t necpgame/message-queue-service:latest .
```

### Kubernetes Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: message-queue-service
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: message-queue
        image: necpgame/message-queue-service:latest
        ports:
        - containerPort: 8087
        - containerPort: 9097
        env:
        - name: KAFKA_BROKERS
          value: "kafka-cluster:9092"
        - name: DB_HOST
          value: "postgres-service"
        livenessProbe:
          httpGet:
            path: /health
            port: 8087
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8087
          initialDelaySeconds: 5
          periodSeconds: 5
```

## Monitoring

### Metrics
- `message_queue_messages_produced_total`: Total messages produced
- `message_queue_messages_consumed_total`: Total messages consumed
- `message_queue_messages_failed_total`: Total failed messages
- `message_queue_active_consumers`: Number of active consumers
- `message_queue_processing_latency_seconds`: Message processing latency

### Health Checks
- Service availability
- Database connectivity
- Kafka broker reachability
- Redis connectivity
- Consumer group health

## Development

### Local Setup
```bash
# Start dependencies
docker-compose up -d kafka postgres redis

# Run service
go run main.go
```

### Testing
```bash
# Unit tests
go test ./...

# Integration tests
go test -tags=integration ./...

# Benchmarks
go test -bench=. -benchmem ./...
```

### API Testing
```bash
# Produce test message
curl -X POST http://localhost:8087/api/v1/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <jwt-token>" \
  -d '{"topic":"test","content":"Hello World"}'

# Check health
curl http://localhost:8087/health
```

## Troubleshooting

### Common Issues

1. **Connection Refused**: Check Kafka/PostgreSQL connectivity
2. **Authentication Failed**: Verify JWT tokens and permissions
3. **High Latency**: Check network configuration and load balancing
4. **Memory Leaks**: Enable profiling and check memory pools

### Logs
```bash
# Structured JSON logs
{"level":"info","ts":"2024-01-01T12:00:00Z","msg":"Message produced","message_id":"msg-123","topic":"user-events"}
```

### Profiling
```bash
# CPU profiling
go tool pprof http://localhost:6067/debug/pprof/profile

# Memory profiling
go tool pprof http://localhost:6067/debug/pprof/heap
```

## Contributing

1. Follow Go coding standards
2. Add comprehensive tests
3. Update documentation
4. Ensure performance benchmarks pass
5. Run security scans before PR

## License

Proprietary - NECPGAME Internal Use Only