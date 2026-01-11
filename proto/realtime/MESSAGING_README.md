# Real-Time Messaging System Protocol

## Overview

Protocol Buffers definition for real-time messaging system with WebSocket clusters, message routing, and presence tracking. Designed for MMOFPS games requiring <50ms message delivery with 100k+ concurrent connections.

## Issue: #1978

## Features

### 1. WebSocket Clusters
- Multi-node cluster support
- Automatic node discovery
- Load balancing across nodes
- Failover and health monitoring
- Cluster configuration management

### 2. Message Routing
- Direct routing to target node
- Broadcast to all nodes
- Cluster-based routing
- Proximity-based routing
- Priority-based delivery

### 3. Presence System
- Real-time player presence tracking
- Status updates (online, away, busy, offline, in-game, in-menu)
- Zone-based presence
- Proximity-based presence queries
- Activity tracking

### 4. Message Types
- Channel messages (guild, party, world)
- Direct messages (player-to-player)
- System messages
- Announcements

## Architecture

### Cluster Structure
```
Cluster
├── Node 1 (WebSocket Server)
│   ├── 10k connections
│   └── Load: 60%
├── Node 2 (WebSocket Server)
│   ├── 12k connections
│   └── Load: 70%
└── Node 3 (WebSocket Server)
    ├── 8k connections
    └── Load: 50%
```

### Message Routing Flow
```
Client A (Node 1)          Cluster Router          Client B (Node 2)
   │                            │                        │
   ├── Send Message ───────────>│                        │
   │                            ├── Route to Node 2 ───>│
   │                            │                        │
   │                            │<── Delivery ACK ───────┤
   │<── Delivery Confirmation ──┤                        │
```

### Presence Tracking
- Redis for presence state
- Real-time updates via WebSocket
- Proximity-based queries
- Zone-based filtering

## Performance

- **Message Delivery**: <50ms P99
- **Concurrent Connections**: 100k+ per cluster
- **Throughput**: 1M+ messages/sec per cluster
- **Presence Updates**: <100ms propagation time

## Integration

This protocol integrates with:
- `realtime-gateway-service-go` - WebSocket server
- Redis - Presence state and message routing
- Kafka - Message replication (optional)

## Use Cases

1. **Guild Chat**: Channel messages routed to guild members
2. **Direct Messages**: Player-to-player communication
3. **System Announcements**: Broadcast to all online players
4. **Presence Tracking**: Show online friends, nearby players
5. **Proximity Chat**: Chat with nearby players
