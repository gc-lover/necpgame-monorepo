# 📖 Go Performance Bible - Part 2A

**Network Optimizations для MMOFPS**

---

# NETWORK OPTIMIZATIONS

## 🔴 CRITICAL: UDP for Game State

```go
// WebSocket/TCP - lobby, chat, inventory
ws.WriteJSON(data)

// UDP - position, shooting, game state
conn.WriteToUDP(packet, addr)
```

**Gains:** Latency ↓50-60%, Jitter ↓75-80%

---

## 🔴 CRITICAL: Spatial Partitioning

```go
type SpatialGrid struct {
    cellSize float32
    cells    sync.Map
}

func (g *SpatialGrid) GetNearbyPlayers(pos Vector3, radius float32) []*Player {
    cellID := g.GetCell(pos)
    nearby := make([]*Player, 0, 64)
    
    for x := cellID.X - radius; x <= cellID.X + radius; x++ {
        for y := cellID.Y - radius; y <= cellID.Y + radius; y++ {
            cell := g.GetCell(CellID{X: x, Y: y})
            nearby = append(nearby, cell.Players()...)
        }
    }
    return nearby
}
```

**Gains:** Network ↓80-90%, CPU ↓70%

---

## 🔴 CRITICAL: Delta Compression

```go
type PlayerUpdate struct {
    ID         uint32
    ChangeMask uint8
    Position   Vec3  `json:",omitempty"`
    Health     int16 `json:",omitempty"`
}

func (p *Player) GetDelta(prev *PlayerState) *PlayerUpdate {
    update := &PlayerUpdate{ID: p.ID}
    
    if p.Position != prev.Position {
        update.ChangeMask |= PositionChanged
        update.Position = p.Position
    }
    
    return update
}
```

**Gains:** Bandwidth ↓70-85%

---

## 🟡 HIGH: Batch Network Writes

```go
batch := make([]byte, 0, 64*1024)

for _, player := range players {
    batch = append(batch, player.Update()...)
    
    if len(batch) > 60000 {
        conn.WriteToUDP(batch, addr)
        batch = batch[:0]
    }
}
```

**Gains:** Syscalls ↓95%, CPU ↓60%

---

## 🟡 HIGH: Coordinate Quantization

```go
// ❌ float32: 12 bytes
type Position struct { X, Y, Z float32 }

// ✅ int16: 6 bytes
type QuantizedPos struct { X, Y, Z int16 }

func Quantize(pos Vec3) QuantizedPos {
    return QuantizedPos{
        X: int16(pos.X * 100), // 0.01m precision
        Y: int16(pos.Y * 100),
        Z: int16(pos.Z * 100),
    }
}
```

**Gains:** Bandwidth ↓50%

---

## 🟢 MEDIUM: UDP Buffer Pooling

```go
var udpBufferPool = sync.Pool{
    New: func() interface{} {
        buf := make([]byte, 1500)
        return &buf
    },
}

func HandleUDP(conn *net.UDPConn) {
    bufPtr := udpBufferPool.Get().(*[]byte)
    defer udpBufferPool.Put(bufPtr)
    
    n, addr, _ := conn.ReadFromUDP(*bufPtr)
    processPacket((*bufPtr)[:n], addr)
}
```

---

# SERIALIZATION

## 🟡 HIGH: Protocol Buffers

```go
// JSON: ~500ns encode, 15 allocs
data, _ := json.Marshal(player)

// Protobuf: ~150ns encode, 1 alloc
data, _ := proto.Marshal(player)
```

**Gains:** Encode ↓70%, Size ↓40-60%

---

## 🟢 MEDIUM: FlatBuffers

```go
builder := flatbuffers.NewBuilder(1024)
pos := CreatePosition(builder, x, y, z)
builder.Finish(pos)

conn.Write(builder.FinishedBytes())

// Decode without unmarshal
position := GetRootAsPosition(data, 0)
x := position.X()
```

**When:** Protobuf >5% CPU  
**Gains:** Decode ↓99%, 0 allocations

---

## 🟢 MEDIUM: Fast JSON

```go
import "github.com/bytedance/sonic"

data, _ := sonic.Marshal(obj) // 2-3x faster
```

---

**Next:** [Part 2B - Game Patterns](./02b-game-patterns.md)  
**See:** [Part 1](./01-memory-concurrency-db.md)

