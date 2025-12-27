# Game Analytics Dashboard Service Go

Enterprise-Grade Analytics Dashboard for NECPGAME - Real-time Game Metrics, Player Analytics, and Performance Monitoring.

**Domain:** analytics-dashboard
**Module:** game-analytics-dashboard-service-go

## Features

- **Real-time Dashboard**: Live game metrics with 30-second updates
- **Player Analytics**: Comprehensive player behavior and engagement tracking
- **Combat Analytics**: Detailed combat statistics and performance metrics
- **Economic Analytics**: In-game economy trends and revenue tracking
- **Performance Monitoring**: System metrics and service health tracking
- **Custom Dashboards**: Configurable widgets and real-time data visualization
- **Advanced Queries**: Complex analytics queries with filtering and aggregation
- **Event Processing**: Real-time event ingestion and processing
- **Caching Layer**: Multi-level Redis caching for optimal performance

## Performance Targets

- **Dashboard Latency:** <1 second for real-time updates
- **Analytics Queries:** P99 <5 seconds for complex aggregations
- **Event Throughput:** 10,000+ events/second processing
- **Concurrent Users:** 1000+ simultaneous dashboard users
- **Data Freshness:** <30 seconds for real-time metrics
- **Memory Usage:** <200MB per service instance

## API Endpoints

### Dashboard Endpoints
- `GET /api/v1/dashboard/realtime` - Real-time dashboard data
- `GET /api/v1/dashboard/widgets` - Dashboard widgets configuration
- `PUT /api/v1/dashboard/widgets/{widgetId}` - Update widget configuration

### Analytics Endpoints
- `GET /api/v1/analytics/player/{playerId}` - Player analytics
- `GET /api/v1/analytics/game-metrics` - Game-wide metrics
- `GET /api/v1/analytics/combat` - Combat analytics
- `GET /api/v1/analytics/economic` - Economic analytics
- `GET /api/v1/analytics/social` - Social analytics
- `POST /api/v1/analytics/query` - Custom analytics queries

### Monitoring Endpoints
- `GET /health` - Service health check
- `GET /ready` - Readiness probe
- `GET /metrics` - Prometheus metrics
- `GET /api/v1/performance/metrics` - Performance metrics

### Event Processing
- `POST /api/v1/events` - Ingest analytics events

## Architecture

### Core Components

- **Handlers Layer:** HTTP request/response with middleware
- **Service Layer:** Business logic with data aggregation
- **Repository Layer:** PostgreSQL + Redis data access with caching
- **Event Processing:** Real-time event ingestion and background workers
- **Query Engine:** Advanced analytics queries with filtering

### Data Flow

```
External Events → Event Processor → Redis Cache → PostgreSQL
                      ↓
Dashboard Requests → Service Layer → Repository → Cached Response
```

### Analytics Types

#### Player Analytics
- Session tracking and engagement metrics
- Retention analysis (1-day, 7-day, 30-day)
- Churn prediction and risk scoring
- Skill progression and achievement tracking

#### Game Metrics
- Real-time player counts and concurrent users
- Revenue tracking and economic indicators
- Feature usage and engagement rates
- Geographic distribution and regional metrics

#### Combat Analytics
- Match statistics and win/loss ratios
- Weapon performance and popularity tracking
- Player skill ratings and matchmaking data
- Anti-cheat metrics and anomaly detection

#### Economic Analytics
- Transaction volumes and revenue trends
- Item popularity and trading patterns
- Currency circulation and market dynamics
- Player spending behavior analysis

## Configuration

### Environment Variables

```bash
# Server
SERVER_ADDR=:8080
SERVER_PORT=8080

# Database
DATABASE_URL=postgres://user:password@localhost:5432/game_analytics?sslmode=disable

# Redis
REDIS_URL=redis://localhost:6379

# Analytics
DATA_RETENTION_DAYS=90
CACHE_TTL=5m
BATCH_SIZE=1000
WORKER_POOL_SIZE=10
MAX_CONCURRENT_QUERIES=50

# Dashboard
REALTIME_UPDATE_INTERVAL=30s
DEFAULT_TIME_RANGE=24h
MAX_WIDGETS_PER_DASHBOARD=20

# External Services
COMBAT_STATS_URL=http://combat-stats-service:8080
ECONOMY_SERVICE_URL=http://economy-service:8080
SOCIAL_SERVICE_URL=http://social-service:8080
EVENT_BUS_URL=http://event-bus:8080

# Security
API_KEY=your-api-key

# Logging
LOG_LEVEL=info
```

## Analytics Queries

### Query Format

```json
{
  "start_time": "2024-12-27T00:00:00Z",
  "end_time": "2024-12-27T23:59:59Z",
  "granularity": "hour",
  "filters": {
    "region": "EU",
    "platform": "PC"
  },
  "group_by": ["hour", "region"],
  "metrics": ["player_count", "revenue"]
}
```

### Supported Metrics

- `player_analytics` - Player behavior and engagement
- `game_metrics` - Game-wide performance indicators
- `combat_stats` - Combat and matchmaking data
- `economic_data` - In-game economy metrics
- `performance_metrics` - System performance data

## Dashboard Widgets

### Widget Types

#### Metric Widget
```json
{
  "widget_id": "online-players",
  "widget_type": "metric",
  "title": "Online Players",
  "config": {
    "metric": "online_players",
    "format": "number"
  },
  "position": {
    "x": 0,
    "y": 0,
    "width": 3,
    "height": 2
  }
}
```

#### Chart Widget
```json
{
  "widget_id": "revenue-chart",
  "widget_type": "chart",
  "title": "Revenue Trends",
  "config": {
    "type": "line",
    "metric": "revenue",
    "period": "7d"
  }
}
```

#### Table Widget
```json
{
  "widget_id": "combat-stats",
  "widget_type": "table",
  "title": "Combat Statistics",
  "config": {
    "columns": ["matches", "win_rate", "avg_duration"],
    "limit": 10
  }
}
```

## Event Processing

### Event Ingestion

```json
POST /api/v1/events
{
  "type": "player_login",
  "data": {
    "player_id": "player_123",
    "session_id": "session_456",
    "platform": "PC",
    "region": "EU"
  }
}
```

### Supported Event Types

- `player_login` - Player authentication events
- `player_logout` - Player session end
- `match_start` - Combat match initiation
- `match_end` - Combat match completion
- `purchase` - In-game purchase events
- `achievement_unlock` - Achievement progression
- `guild_join` - Social guild membership
- `error` - Application error events

## Database Schema

### Core Tables

```sql
-- Player analytics with engagement metrics
CREATE TABLE player_analytics (
    player_id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255),
    total_play_time BIGINT DEFAULT 0,
    sessions_count INTEGER DEFAULT 0,
    last_seen TIMESTAMP,
    average_session_time DECIMAL(10,2),
    retention_rate DECIMAL(5,4),
    churn_risk VARCHAR(20),
    engagement_score DECIMAL(5,2),
    time_range VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Game metrics with time-series data
CREATE TABLE game_metrics (
    id BIGSERIAL PRIMARY KEY,
    total_players BIGINT,
    active_players BIGINT,
    new_registrations BIGINT,
    concurrent_users BIGINT,
    peak_concurrent BIGINT,
    average_session_time DECIMAL(10,2),
    revenue DECIMAL(15,2),
    time_range VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Combat analytics with performance data
CREATE TABLE combat_analytics (
    id BIGSERIAL PRIMARY KEY,
    total_matches BIGINT,
    average_match_time DECIMAL(10,2),
    win_rate DECIMAL(5,4),
    popular_weapons JSONB,
    kill_death_ratio DECIMAL(5,4),
    headshot_rate DECIMAL(5,4),
    time_range VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Economic analytics for in-game economy
CREATE TABLE economic_analytics (
    id BIGSERIAL PRIMARY KEY,
    total_transactions BIGINT,
    total_revenue DECIMAL(15,2),
    average_transaction DECIMAL(10,2),
    popular_items JSONB,
    currency_circulation DECIMAL(15,2),
    trade_volume BIGINT,
    time_range VARCHAR(10),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Event processing queue
CREATE TABLE analytics_events (
    id BIGSERIAL PRIMARY KEY,
    event_type VARCHAR(100),
    event_data JSONB,
    processed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP
);
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

## Integration Examples

### Dashboard Client Integration

```javascript
// Fetch real-time dashboard
const dashboard = await fetch('/api/v1/dashboard/realtime')
  .then(r => r.json());

// Query player analytics
const playerStats = await fetch('/api/v1/analytics/player/player_123?time_range=24h')
  .then(r => r.json());

// Execute custom analytics query
const queryResult = await fetch('/api/v1/analytics/query', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    start_time: '2024-12-27T00:00:00Z',
    end_time: '2024-12-27T23:59:59Z',
    granularity: 'hour',
    metrics: ['player_count', 'revenue'],
    group_by: ['hour']
  })
}).then(r => r.json());
```

### Event Ingestion

```javascript
// Send player login event
await fetch('/api/v1/events', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    type: 'player_login',
    data: {
      player_id: 'player_123',
      platform: 'PC',
      region: 'EU'
    }
  })
});

// Send match completion event
await fetch('/api/v1/events', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    type: 'match_end',
    data: {
      match_id: 'match_456',
      winner: 'player_123',
      duration: 1800,
      score: 2500
    }
  })
});
```

## Monitoring and Alerting

### Prometheus Metrics

- `analytics_dashboard_requests_total` - Total API requests
- `analytics_dashboard_request_duration` - Request duration histogram
- `analytics_dashboard_cache_hit_ratio` - Cache performance
- `analytics_dashboard_active_connections` - Active connections
- `analytics_dashboard_event_processing_rate` - Event processing throughput

### Health Checks

- **Database:** PostgreSQL connection and query performance
- **Redis:** Cache connectivity and performance
- **External Services:** Integration health for combat/economy/social services
- **Event Processing:** Queue backlog and processing latency

## Security Features

- **API Key Authentication:** Service-to-service authentication
- **Input Validation:** Comprehensive request sanitization
- **Rate Limiting:** Request throttling to prevent abuse
- **Audit Logging:** Complete event logging for compliance
- **Data Encryption:** Sensitive data protection at rest and in transit

## Performance Optimizations

### Memory Management
- **Object Pooling:** Reuse of analytics data structures
- **Memory Limits:** Configurable memory usage caps
- **GC Optimization:** Reduced garbage collection pressure

### Caching Strategy
- **Multi-level Caching:** Redis with configurable TTL
- **Cache Warming:** Pre-population of frequently accessed data
- **Cache Invalidation:** Smart invalidation based on data freshness

### Query Optimization
- **Database Indexing:** Optimized indexes for analytics queries
- **Query Batching:** Batch processing for bulk operations
- **Result Pagination:** Efficient handling of large result sets

## BACKEND OPTIMIZATION NOTES

- **Struct field alignment:** Optimized for analytics data structures
- **Expected memory savings:** 30-50% for complex analytics objects
- **Concurrent processing:** Worker pools for high-throughput event processing
- **Real-time capabilities:** Sub-second dashboard updates with caching

## Contributing

1. Follow Go coding standards and performance guidelines
2. Add comprehensive tests for new analytics features
3. Update API documentation for new endpoints
4. Ensure performance targets are maintained
5. Test integrations with external services

## License

Proprietary - NECPGAME Internal Use Only
