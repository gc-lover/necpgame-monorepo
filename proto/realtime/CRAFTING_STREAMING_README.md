# Crafting Streaming Protocol

**Issue:** #2204 - Network components for crafting system
**Agent:** Network

## Overview

gRPC streaming protocol for multi-step crafting workflows in NECPGAME. Provides real-time progress updates, queue monitoring, and multi-step workflow management.

## Protocol Definition

See `crafting-streaming.proto` for complete protocol buffer definitions.

## Services

### CraftingStreamingService

#### StreamCraftingProgress
Streams real-time crafting progress updates for active crafting sessions.

**Request:**
- `session_id`: UUID of crafting session
- `player_id`: UUID of player
- `token`: Authentication token

**Response Stream:**
- `progress_percent`: 0-100
- `elapsed_seconds`: Time elapsed
- `remaining_seconds`: Estimated time remaining
- `current_step`: Current crafting step name
- `step_progress`: Progress within current step (0-100)
- `steps`: Array of step statuses
- `timestamp_ms`: Server timestamp

#### StreamCraftingQueue
Streams queue position and status updates for crafting requests.

**Request:**
- `player_id`: UUID of player
- `token`: Authentication token

**Response Stream:**
- `request_id`: UUID of crafting request
- `queue_position`: Current position (0 = processing)
- `estimated_wait_seconds`: Estimated wait time
- `status`: "queued", "processing", "completed", "failed"
- `recipe_id`: UUID of recipe
- `timestamp_ms`: Server timestamp

#### StreamMultiStepWorkflow
Streams progress for multi-step crafting workflows.

**Request:**
- `workflow_id`: UUID of multi-step workflow
- `player_id`: UUID of player
- `token`: Authentication token

**Response Stream:**
- `overall_progress`: 0-100
- `current_step_index`: Current step number
- `total_steps`: Total number of steps
- `current_step_name`: Name of current step
- `steps`: Array of workflow step statuses
- `status`: "active", "paused", "completed", "failed"
- `timestamp_ms`: Server timestamp

## Code Generation

```bash
# Generate Go code from proto
protoc --go_out=. --go_opt=paths=source_relative \
  --go-grpc_out=. --go-grpc_opt=paths=source_relative \
  proto/realtime/crafting-streaming.proto
```

## Performance Targets

- **Latency:** P99 <10ms for progress updates
- **Throughput:** 10,000 concurrent streams
- **Message Size:** ~128 bytes per update
- **Compression:** gRPC built-in compression

## Integration

### Backend Service Integration

```go
import (
    "google.golang.org/grpc"
    pb "necpgame/services/crafting-network-service-go/pkg/proto"
)

// Implement CraftingStreamingService
type CraftingStreamingServer struct {
    pb.UnimplementedCraftingStreamingServiceServer
}

func (s *CraftingStreamingServer) StreamCraftingProgress(
    req *pb.StreamCraftingProgressRequest,
    stream pb.CraftingStreamingService_StreamCraftingProgressServer,
) error {
    // Stream progress updates
    for {
        update := &pb.CraftingProgressUpdate{
            SessionId: req.SessionId,
            ProgressPercent: calculateProgress(),
            // ... other fields
        }
        if err := stream.Send(update); err != nil {
            return err
        }
        time.Sleep(100 * time.Millisecond) // 10 updates/sec
    }
}
```

### Client Integration

```go
conn, _ := grpc.Dial("crafting-network-service:9090", grpc.WithInsecure())
client := pb.NewCraftingStreamingServiceClient(conn)

stream, _ := client.StreamCraftingProgress(ctx, &pb.StreamCraftingProgressRequest{
    SessionId: sessionID,
    PlayerId:  playerID,
    Token:     token,
})

for {
    update, err := stream.Recv()
    if err != nil {
        break
    }
    // Handle progress update
    handleProgress(update)
}
```

## Security

- **Authentication:** Bearer token required
- **Authorization:** Player must own the session
- **Rate Limiting:** 10 streams per player max
- **Timeout:** 30 seconds idle timeout

## Monitoring

- **Metrics:** Stream count, message rate, latency
- **Alerts:** Stream failures > 1%, latency > 50ms
- **Tracing:** Distributed tracing with correlation IDs
