# Crafting Domain Kafka Events

**Issue:** #2204 - Network components for crafting system
**Agent:** Network

## Overview

Kafka event schemas for crafting domain operations. Supports real-time crafting session management, progress tracking, and event-driven architecture.

## Event Types

### Crafting Session Events

- `crafting.session.start` - Crafting session started
- `crafting.session.complete` - Crafting session completed successfully
- `crafting.session.fail` - Crafting session failed
- `crafting.session.cancel` - Crafting session cancelled
- `crafting.session.pause` - Crafting session paused
- `crafting.session.resume` - Crafting session resumed

## Topics

### game.crafting.events
- **Partitions:** 16
- **Retention:** 7 days
- **Compression:** snappy
- **Description:** Crafting session and operation events

### game.crafting.progress
- **Partitions:** 24
- **Retention:** 1 hour (real-time progress updates)
- **Compression:** lz4
- **Description:** Real-time crafting progress updates for WebSocket clients

## Performance Targets

- **Throughput:** 5,000 events/sec (crafting.events)
- **Throughput:** 10,000 events/sec (crafting.progress)
- **Latency:** P99 <50ms (crafting.events)
- **Latency:** P99 <10ms (crafting.progress)
- **Durability:** 5 9's (crafting.events), 4 9's (crafting.progress)

## Consumer Groups

### crafting_processor
- **Topics:** game.crafting.events
- **Partitions per consumer:** 4
- **Description:** Crafting service consumers for crafting session events

### crafting_progress_consumer
- **Topics:** game.crafting.progress
- **Partitions per consumer:** 6
- **Description:** Real-time progress consumers for WebSocket broadcasting

## Examples

See `proto/kafka/examples/crafting-session-start.json` for example event.

## Integration

### Publishing Events

```go
import (
    "github.com/IBM/sarama"
)

producer, _ := sarama.NewSyncProducer(brokers, config)
defer producer.Close()

event := &CraftingSessionStartEvent{
    EventID:   uuid.New().String(),
    EventType: "crafting.session.start",
    // ... other fields
}

message := &sarama.ProducerMessage{
    Topic: "game.crafting.events",
    Value: sarama.ByteEncoder(json.Marshal(event)),
}

producer.SendMessage(message)
```

### Consuming Events

```go
consumer, _ := sarama.NewConsumer(brokers, config)
defer consumer.Close()

partitionConsumer, _ := consumer.ConsumePartition("game.crafting.events", 0, sarama.OffsetNewest)
defer partitionConsumer.Close()

for message := range partitionConsumer.Messages() {
    var event CraftingSessionEvent
    json.Unmarshal(message.Value, &event)
    // Handle event
}
```
