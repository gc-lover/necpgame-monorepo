# Event-Driven Simulation Tick Infrastructure

**Issue: #2281**  
**Agent: Network/Backend/DevOps**  
**Status: Implemented**

## Overview

This document describes the event-driven simulation tick infrastructure that powers world simulation updates in NECPGAME. The system uses Kafka topics to drive periodic simulation updates for economy service (hourly ticks) and world simulation (daily ticks).

## Architecture

### Components

1. **Simulation Ticker Service** (`services/simulation-ticker-service-go/`)
   - Generates periodic tick events
   - Supports hourly and daily tick types
   - Publishes events to Kafka topics

2. **Economy Service** (`services/economy-service-go/`)
   - Consumes `world.tick.hourly` events
   - Triggers market clearing (`Market.Clear()`)
   - Publishes market results to `simulation.event` topic

3. **World Simulation Service** (`services/world-simulation-python/`)
   - Consumes `world.tick.daily` events
   - Triggers diplomacy evaluation (`Diplomacy.Evaluate()`)
   - Runs crowd simulation (Mesa agents)
   - Publishes simulation outputs to `simulation.event` topic

### Kafka Topics

#### Input Topics (Ticks)

- **`world.tick.hourly`**
  - Partitions: 3
  - Replication Factor: 3
  - Retention: 1 day
  - Description: Hourly simulation tick for Economy service
  - Schema: `proto/kafka/schemas/world/world-tick-events.json`
  - Consumers: `economy-service-go`, `analytics-service`

- **`world.tick.daily`**
  - Partitions: 1
  - Replication Factor: 3
  - Retention: 7 days
  - Description: Daily simulation tick for Diplomacy engine
  - Schema: `proto/kafka/schemas/world/world-tick-events.json`
  - Consumers: `world-simulation-python`, `social-service`, `analytics-service`

#### Output Topic (Simulation Events)

- **`simulation.event`**
  - Partitions: 8
  - Replication Factor: 3
  - Retention: 7 days
  - Description: Output events from various simulations (market clearing, crowd signals, diplomacy updates)
  - Schema: `proto/kafka/schemas/world/simulation-events.json`
  - Producers: `economy-service-go`, `world-simulation-python`
  - Consumers: `analytics-service`, `gameplay-service`, `notification-service`

### Topic Configuration

Full topic configuration is defined in `proto/kafka/topics/topic-config.yaml`:

```yaml
# Simulation Ticks - Issue #2281
- name: world.tick.hourly
  partitions: 3
  replication_factor: 3
  retention_ms: 86400000 # 1 day
  segment_ms: 3600000 # 1 hour
  cleanup_policy: delete
  compression_type: snappy
  max_message_bytes: 131072 # 128KB
  description: Hourly simulation tick for Economy service

- name: world.tick.daily
  partitions: 1
  replication_factor: 3
  retention_ms: 604800000 # 7 days
  segment_ms: 86400000 # 1 day
  cleanup_policy: delete
  compression_type: gzip
  max_message_bytes: 262144 # 256KB
  description: Daily simulation tick for Diplomacy engine

- name: simulation.event
  partitions: 8
  replication_factor: 3
  retention_ms: 604800000 # 7 days
  segment_ms: 86400000 # 1 day
  cleanup_policy: delete
  compression_type: lz4
  max_message_bytes: 524288 # 512KB
  description: Output events from various simulations
```

## Event Schemas

### Tick Events (`world-tick-events.json`)

Tick events follow the base-event.json schema and include tick-specific data:

```json
{
  "event_id": "uuid",
  "event_type": "world.tick.hourly" | "world.tick.daily",
  "timestamp": "RFC3339",
  "version": "1.0.0",
  "source": "simulation.ticker",
  "data": {
    "tick_id": "uuid",
    "tick_type": "hourly" | "daily",
    "game_hour": 0-23,  // For hourly ticks
    "game_day": 1+,     // For daily ticks
    "game_time": "RFC3339",
    "tick_timestamp": "RFC3339",
    "triggered_by": "scheduler",
    "consumers": ["service-names"]
  }
}
```

**Hourly Tick Example:**
```json
{
  "event_id": "550e8400-e29b-41d4-a716-446655440000",
  "event_type": "world.tick.hourly",
  "timestamp": "2024-01-15T14:00:00Z",
  "version": "1.0.0",
  "source": "simulation.ticker",
  "data": {
    "tick_id": "660e8400-e29b-41d4-a716-446655440001",
    "tick_type": "hourly",
    "game_hour": 14,
    "game_time": "2024-01-15T14:00:00Z",
    "tick_timestamp": "2024-01-15T14:00:00Z",
    "triggered_by": "scheduler",
    "consumers": ["economy-service", "analytics-service"]
  }
}
```

**Daily Tick Example:**
```json
{
  "event_id": "770e8400-e29b-41d4-a716-446655440002",
  "event_type": "world.tick.daily",
  "timestamp": "2024-01-15T00:00:00Z",
  "version": "1.0.0",
  "source": "simulation.ticker",
  "data": {
    "tick_id": "880e8400-e29b-41d4-a716-446655440003",
    "tick_type": "daily",
    "game_day": 1461,
    "game_time": "2024-01-15T00:00:00Z",
    "tick_timestamp": "2024-01-15T00:00:00Z",
    "triggered_by": "scheduler",
    "consumers": ["world-simulation-python", "social-service", "analytics-service"],
    "metadata": {
      "simulation_day": 1461,
      "simulation_week": 208,
      "simulation_month": 48,
      "simulation_year": 2024
    }
  }
}
```

### Simulation Events (`simulation-events.json`)

Simulation events represent outputs from various simulations:

**Market Cleared Event:**
```json
{
  "event_type": "simulation.event.market_cleared",
  "commodity": "Food",
  "price": 10.50,
  "volume": 150,
  "timestamp": "2024-01-15T14:05:00Z",
  "market_id": "market-Food",
  "period": "hourly",
  "tick_id": "660e8400-e29b-41d4-a716-446655440001",
  "game_hour": 14
}
```

**Crowd Signal Event:**
```json
{
  "event_type": "simulation.event.crowd_signal",
  "signal_type": "food_demand_rising",
  "location": "night_city_sector_4",
  "magnitude": 75.5,
  "change_percent": 15.2,
  "agent_count": 150,
  "aggregation_timestamp": "2024-01-15T00:05:00Z",
  "time_window_minutes": 60,
  "predicted_duration_minutes": 120
}
```

**Diplomacy Update Event:**
```json
{
  "event_type": "simulation.event.diplomacy_update",
  "faction_a_id": "uuid",
  "faction_b_id": "uuid",
  "new_relation_state": "WAR" | "PEACE" | "ALLIANCE" | "COLD_WAR",
  "score_change": -15.5,
  "reason": "Territorial dispute escalation"
}
```

Full schema definitions:
- `proto/kafka/schemas/world/world-tick-events.json`
- `proto/kafka/schemas/world/simulation-events.json`
- `proto/kafka/schemas/core/base-event.json` (inherited base schema)

## Deployment

### Simulation Ticker Service

The ticker service runs as Kubernetes CronJobs:

**Hourly CronJob** (`infrastructure/kafka/simulation-ticker-service.yaml`):
```yaml
schedule: "0 * * * *"  # Run every hour at minute 0
command: ["/simulation-ticker", "--type", "hourly"]
```

**Daily CronJob**:
```yaml
schedule: "0 0 * * *"  # Run daily at midnight
command: ["/simulation-ticker", "--type", "daily"]
```

### Economy Service Consumer

Economy service automatically starts hourly tick consumer on startup:

```go
// Start Kafka consumer for hourly ticks (#2281)
go startHourlyTickConsumer()
```

The consumer:
1. Reads from `world.tick.hourly` topic
2. Parses tick event according to schema
3. Triggers `Market.Clear()` for all commodities
4. Publishes market results to `simulation.event` topic
5. Persists market state to database

### World Simulation Service Consumer

World simulation service (Python) consumes daily ticks:

```python
# Initialize consumer for daily ticks
self.consumer = Consumer({
    'bootstrap.servers': self.kafka_bootstrap,
    'group.id': 'world-simulation-group',
    'auto.offset.reset': 'latest'
})
self.consumer.subscribe(['world.tick.daily'])
```

The consumer:
1. Reads from `world.tick.daily` topic
2. Triggers `Diplomacy.Evaluate()` for all faction pairs
3. Runs crowd simulation (Mesa agents)
4. Publishes simulation outputs to `simulation.event` topic

## Performance Considerations

### Backend Optimization (Go Services)

**Context Timeouts:**
- Kafka consumer read timeout: 30 seconds
- Kafka producer write timeout: 10 seconds
- Database operations: 5 seconds

**Connection Pooling:**
- Kafka reader/writer: Reused connections
- Database: Connection pool (TODO: implement)

**Memory Management:**
- Preallocated slices for market orders
- Limited trade history (last 10 trades per agent)
- Batch message processing

### Python Service Optimization

**Async Processing:**
- Asyncio-based event loop
- Non-blocking Kafka operations
- Parallel diplomacy and crowd simulation

**Resource Limits:**
- Agent count: Configurable via `CROWD_NUM_AGENTS`
- Grid size: Configurable via `CROWD_GRID_WIDTH`/`HEIGHT`
- Simulation can be disabled: `CROWD_SIMULATION_ENABLED=false`

## Testing

### Manual Testing

**Send Hourly Tick:**
```bash
docker exec -it necpgame-kafka-1 kafka-console-producer \
  --broker-list localhost:9092 \
  --topic world.tick.hourly
# Paste hourly tick JSON
```

**Send Daily Tick:**
```bash
docker exec -it necpgame-kafka-1 kafka-console-producer \
  --broker-list localhost:9092 \
  --topic world.tick.daily
# Paste daily tick JSON
```

**Consume Simulation Events:**
```bash
docker exec -it necpgame-kafka-1 kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic simulation.event \
  --from-beginning
```

### Integration Testing

1. Start Kafka cluster
2. Start Economy Service (consumes hourly ticks)
3. Start World Simulation Service (consumes daily ticks)
4. Manually trigger ticker service or send test tick events
5. Verify market clearing occurs (check logs)
6. Verify simulation events are published (consume from topic)
7. Verify database persistence (check market_state table)

## Monitoring

### Metrics to Track

1. **Tick Generation:**
   - Tick events sent per hour/day
   - Ticker service execution time
   - Failed tick generations

2. **Consumer Lag:**
   - Economy service lag on `world.tick.hourly`
   - World simulation lag on `world.tick.daily`

3. **Market Clearing:**
   - Markets cleared per tick
   - Average clearing time
   - Market efficiency scores

4. **Simulation Events:**
   - Events published per tick
   - Event size distribution
   - Consumer lag on `simulation.event`

### Logging

All services use structured logging with context:

**Economy Service:**
```
Processing hourly tick: event_id=..., tick_id=..., game_hour=14
Market cleared - Commodity: Food, Price: 10.50, Volume: 150
Published market event: commodity=Food, price=10.50, volume=150, tick_id=...
```

**World Simulation Service:**
```
Processing daily tick - running diplomacy evaluation and crowd simulation
Generated diplomacy evaluation: 21 relations, 3 state changes
Published 5 simulation events (2 diplomacy, 3 crowd)
```

## Future Enhancements

1. **Database Persistence:**
   - Implement proper connection pooling
   - Add market_state table with proper indexes
   - Store tick history for analytics

2. **Agent-Based Market Clearing:**
   - Load agents from database
   - Use `Market.Clear(agents)` instead of legacy method
   - Track agent wealth and beliefs over time

3. **Advanced Simulation Events:**
   - Demand signals for economy optimization
   - Population movement tracking
   - Social trend detection

4. **Monitoring Dashboard:**
   - Real-time tick status
   - Market clearing metrics
   - Simulation event throughput

## Related Documentation

- Kafka Topic Configuration: `proto/kafka/topics/topic-config.yaml`
- Event Schemas: `proto/kafka/schemas/world/`
- Economy Service: `services/economy-service-go/cmd/api/main.go`
- World Simulation Service: `services/world-simulation-python/app.py`
- Ticker Service: `services/simulation-ticker-service-go/main.go`
- Kubernetes CronJobs: `infrastructure/kafka/simulation-ticker-service.yaml`

## Issue Reference

- **Issue #2281**: Event-Driven Simulation Tick Infrastructure
- **Related Issues**: 
  - #2278: BazaarBot Market Simulation (market clearing logic)
  - Future: Database persistence for market states
  - Future: Agent-based market clearing
