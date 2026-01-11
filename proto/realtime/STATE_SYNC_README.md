# Real-Time State Synchronization Protocol

## Overview

Protocol Buffers definition for real-time state synchronization with conflict resolution and latency compensation. Designed for MMOFPS games requiring <20ms state sync with automatic conflict handling.

## Issue: #2146

## Features

### 1. State Synchronization
- Optimistic locking with version numbers
- Vector clocks for causal consistency
- Delta updates for bandwidth efficiency
- Full state snapshots when needed

### 2. Conflict Resolution
- Automatic conflict detection
- Multiple resolution strategies:
  - Last Write Wins (default)
  - Merge states
  - Keep old state
  - Manual resolution
- Conflict types:
  - Version mismatch
  - Vector clock divergence
  - Concurrent updates
  - Data inconsistency

### 3. Latency Compensation
- Client-side prediction
- Server-side reconciliation
- Rewind time calculation
- Prediction correction

### 4. Performance Optimizations
- Compression support (GZIP, LZ4, ZSTD)
- Delta updates (only changed state)
- Batch updates via streaming
- Rate limiting support

## Message Types

### StateSyncRequest
Client sends state update request with:
- Player ID and client tick
- State category and key
- New state data
- Expected version (optimistic locking)
- Vector clock
- Estimated latency

### StateSyncResponse
Server responds with:
- Sync result (success/conflict/error)
- Current version
- Conflict information (if detected)
- Latency compensation applied
- Updated state and vector clock

### ConflictInfo
Detailed conflict information:
- Conflict ID and type
- Old and new states
- Resolution strategy
- Resolved state (if auto-resolved)

### LatencyCompensation
Latency compensation details:
- Measured latency
- Compensation factor
- Rewind time for reconciliation
- Prediction correction delta

## Service Methods

1. **SyncState** - Single state update
2. **GetStateSnapshot** - Full state snapshot
3. **ResolveConflict** - Manual conflict resolution
4. **StreamStateUpdates** - Bidirectional streaming for real-time updates

## Usage Example

```go
// Client sends state update
req := &StateSyncRequest{
    PlayerId: playerID,
    ClientTick: clientTick,
    Category: "position",
    Key: "player_position",
    StateData: positionData,
    ExpectedVersion: currentVersion,
    EstimatedLatencyMs: latency,
}

// Server responds
resp, err := client.SyncState(ctx, req)
if resp.Result == SyncResult_CONFLICT {
    // Handle conflict
    resolution := &ConflictResolutionRequest{
        ConflictId: resp.Conflict.ConflictId,
        Strategy: ResolutionStrategy_LAST_WRITE_WINS,
    }
    client.ResolveConflict(ctx, resolution)
}
```

## Performance Targets

- **Latency**: <20ms for state sync
- **Throughput**: >1000 updates/sec per player
- **Bandwidth**: 50-70% reduction with delta compression
- **Conflict Resolution**: <5ms for automatic resolution

## Integration

This protocol integrates with:
- `data-synchronization-service-go` - Conflict resolution backend
- `realtime-gateway-service-go` - Real-time state distribution
- `combat-system-service-go` - Latency compensation for combat
