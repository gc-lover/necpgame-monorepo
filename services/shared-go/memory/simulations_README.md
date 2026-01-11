# Memory-Efficient Data Structures for Large-Scale Simulations

## Issue: #2095

## Overview

Enterprise-grade memory-efficient data structures optimized for large-scale game simulations with 10,000+ entities. These structures minimize allocations, reduce GC pressure, and provide O(1) or O(log n) operations for common simulation patterns.

## Data Structures

### 1. SparseArray

Memory-efficient array for sparse data. Only allocates memory for non-zero elements.

**Use Cases:**
- Sparse game state (only active entities stored)
- Event queues with gaps
- Sparse matrices for physics calculations

**Performance:**
- O(1) access time
- Minimal memory footprint (only stores non-zero elements)
- Thread-safe with RWMutex

**Example:**
```go
import "necpgame/services/shared-go/memory"

// Create sparse array for 100k entities (only active ones stored)
sparse := memory.NewSparseArray[EntityState](100000)

// Set entity at index 5000
sparse.Set(5000, EntityState{Health: 100, Position: Vec3{0, 0, 0}})

// Get entity
state := sparse.Get(5000)

// Check if exists
if sparse.Has(5000) {
    // Entity is active
}
```

### 2. RingBuffer

Fixed-size circular buffer for temporal data. No allocations after initialization.

**Use Cases:**
- Recent event history
- Time-series data (last N samples)
- Command history for replay
- Frame buffers for rendering

**Performance:**
- O(1) insert/read
- Zero allocations after initialization
- Fixed memory footprint

**Example:**
```go
// Create ring buffer for last 1000 events
buffer := memory.NewRingBuffer[GameEvent](1000)

// Push events (overwrites oldest when full)
buffer.Push(GameEvent{Type: "player_move", Data: ...})

// Pop oldest event
event, ok := buffer.Pop()

// Peek without removing
event, ok := buffer.Peek()
```

### 3. SpatialHashMap

3D spatial hash map for efficient spatial queries. O(1) average case for insert/lookup.

**Use Cases:**
- Spatial queries (find entities in region)
- Collision detection
- Interest management (nearby players)
- Physics simulation (particle systems)

**Performance:**
- O(1) average case for insert/lookup
- O(k) for range queries where k is entities in cells
- Efficient for sparse 3D data

**Example:**
```go
// Create spatial hash map with 10-unit cells
spatial := memory.NewSpatialHashMap[Entity](10.0)

// Insert entity at position
spatial.Insert(100.5, 200.3, 50.1, entity)

// Query entities in bounding box
entities := spatial.QueryRange(90, 190, 40, 110, 210, 60)
```

### 4. CompactMap

Memory-efficient map that uses array for small sets, switches to map for large sets.

**Use Cases:**
- Small key-value sets (<16 elements) - uses array
- Large key-value sets (>=16 elements) - uses map
- Component lookups in ECS
- Attribute maps

**Performance:**
- O(1) for small sets (array lookup)
- O(log n) for large sets (map lookup)
- Automatic optimization based on size

**Example:**
```go
// Create compact map
compact := memory.NewCompactMap[string, int]()

// Set values (uses array for <16 elements)
compact.Set("health", 100)
compact.Set("level", 50)

// Get value
value, ok := compact.Get("health")

// Delete key
compact.Delete("health")
```

## Integration with Existing Libraries

### Memory Pooling
These structures work seamlessly with the memory pooling library:
```go
import (
    "necpgame/services/shared-go/memory"
)

// Use with pools
sparsePool := memory.NewPool(
    func() *memory.SparseArray[Entity] {
        return memory.NewSparseArray[Entity](10000)
    },
    func(sa *memory.SparseArray[Entity]) {
        sa.Clear()
    },
)
```

### ECS Integration
Use with Entity Component System:
```go
import (
    "necpgame/services/shared-go/ecs"
    "necpgame/services/shared-go/memory"
)

// Spatial hash map for entity queries
spatialEntities := memory.NewSpatialHashMap[ecs.EntityID](10.0)

// Add entities to spatial map
for _, entity := range world.GetEntitiesWithComponents([]ecs.ComponentID{ecs.ComponentIDPosition}) {
    pos, _ := world.GetComponent(entity.ID, ecs.ComponentIDPosition).(*ecs.Position)
    spatialEntities.Insert(pos.X, pos.Y, pos.Z, entity.ID)
}
```

## Performance Characteristics

### Memory Usage
- **SparseArray**: ~16 bytes per non-zero element (vs 8 bytes per element in dense array)
- **RingBuffer**: Fixed size (capacity * sizeof(T))
- **SpatialHashMap**: ~24 bytes per entry + cell overhead
- **CompactMap**: Array for <16 elements, map for >=16 elements

### Time Complexity
- **SparseArray**: O(1) get/set, O(n) iteration
- **RingBuffer**: O(1) push/pop/peek
- **SpatialHashMap**: O(1) insert, O(k) range query (k = entities in cells)
- **CompactMap**: O(1) for small sets, O(log n) for large sets

## Best Practices

### 1. Choose the Right Structure
- **Sparse data**: Use SparseArray
- **Temporal data**: Use RingBuffer
- **Spatial queries**: Use SpatialHashMap
- **Small key-value sets**: Use CompactMap

### 2. Pre-allocate Capacity
```go
// Pre-allocate for expected size
sparse := memory.NewSparseArray[Entity](100000) // 100k capacity
buffer := memory.NewRingBuffer[Event](1000)     // 1k capacity
```

### 3. Reuse Structures
```go
// Clear and reuse instead of creating new
sparse.Clear()
buffer.Clear()
spatial.Clear()
```

### 4. Thread Safety
All structures are thread-safe with RWMutex. Use read locks for concurrent reads:
```go
// Multiple readers can access simultaneously
sparse.Get(index) // Uses RLock
sparse.Set(index, value) // Uses Lock
```

## Example: Large-Scale Simulation

```go
package simulation

import (
    "necpgame/services/shared-go/memory"
)

type Simulation struct {
    entities    *memory.SparseArray[Entity]
    spatial     *memory.SpatialHashMap[EntityID]
    eventBuffer *memory.RingBuffer[Event]
    attributes  *memory.CompactMap[string, float64]
}

func NewSimulation() *Simulation {
    return &Simulation{
        entities:    memory.NewSparseArray[Entity](100000),
        spatial:     memory.NewSpatialHashMap[EntityID](10.0),
        eventBuffer: memory.NewRingBuffer[Event](1000),
        attributes:  memory.NewCompactMap[string, float64](),
    }
}

func (s *Simulation) Update() {
    // Query nearby entities
    nearby := s.spatial.QueryRange(0, 0, 0, 100, 100, 100)
    
    // Process entities
    for _, entityID := range nearby {
        entity := s.entities.Get(int(entityID))
        // Update entity...
    }
    
    // Store events
    s.eventBuffer.Push(Event{Type: "simulation_tick"})
}
```

## Statistics

For 10,000 active entities in a 1000x1000x1000 world:
- **SparseArray**: ~160 KB (only active entities)
- **SpatialHashMap**: ~240 KB (entities + spatial cells)
- **RingBuffer**: ~8 KB (1000 events)
- **Total**: ~408 KB (vs ~800 KB for dense structures)

**Memory Savings**: ~50% for sparse simulations
