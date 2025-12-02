# 📖 Go Performance Bible - Part 2

**Network, Game-Specific & Advanced Optimizations**

**Версия:** 2.0 | **Дата:** 01.12.2025 | **Go:** 1.23+

---

# 6️⃣ NETWORK OPTIMIZATIONS

## 🔴 CRITICAL: UDP for Game State

```go
// WebSocket/TCP - for lobby, chat, inventory
ws.WriteJSON(data)

// UDP - for position, shooting, game state
conn.WriteToUDP(packet, addr)
```

**Gains:** Latency ↓50-60%, Jitter ↓75-80%, Overhead ↓80%

---

## 🔴 CRITICAL: Spatial Partitioning

```go
type SpatialGrid struct {
    cellSize float32
    cells    sync.Map
}

func (g *SpatialGrid) GetNearbyPlayers(pos Vector3, radius float32) []*Player {
    // Check only neighboring cells, not all 2000 players!
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

**Gains:** Network ↓80-90%, CPU ↓70%, Scale: 50 → 500+ players

---

## 🔴 CRITICAL: Delta Compression

```go
type PlayerUpdate struct {
    ID         uint32
    ChangeMask uint8 // Bit flags: what changed
    Position   Vec3  `json:",omitempty"`
    Rotation   Quat  `json:",omitempty"`
    Health     int16 `json:",omitempty"`
}

func (p *Player) GetDelta(prev *PlayerState) *PlayerUpdate {
    update := &PlayerUpdate{ID: p.ID}
    
    if p.Position != prev.Position {
        update.ChangeMask |= PositionChanged
        update.Position = p.Position
    }
    
    if p.Health != prev.Health {
        update.ChangeMask |= HealthChanged
        update.Health = p.Health
    }
    
    return update
}
```

**Gains:** Bandwidth ↓70-85%, Cost ↓$1000s/month

---

## 🟡 HIGH: Batch Network Writes

```go
batch := make([]byte, 0, 64*1024) // 64KB buffer

for _, player := range players {
    batch = append(batch, player.Update()...)
    
    if len(batch) > 60000 { // Before MTU
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
type Position struct {
    X, Y, Z float32
}

// OK int16: 6 bytes (-50%)
type QuantizedPos struct {
    X, Y, Z int16 // 0.01m precision
}

func Quantize(pos Vec3) QuantizedPos {
    return QuantizedPos{
        X: int16(pos.X * 100),
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
        buf := make([]byte, 1500) // MTU
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

# 7️⃣ SERIALIZATION

## 🟡 HIGH: Protocol Buffers

```go
// JSON: ~500ns encode, 15 allocs
data, _ := json.Marshal(player)

// Protobuf: ~150ns encode, 1 alloc
data, _ := proto.Marshal(player)
```

**Gains:** Encode/decode ↓70%, Size ↓40-60%

---

## 🟢 MEDIUM: FlatBuffers (ultra-low latency)

```go
builder := flatbuffers.NewBuilder(1024)
pos := CreatePosition(builder, x, y, z)
builder.Finish(pos)

conn.Write(builder.FinishedBytes()) // Zero-copy send

// Decode without unmarshal
position := GetRootAsPosition(data, 0)
x := position.X() // Direct access!
```

**When:** Protobuf >5% CPU in profiler  
**Gains:** Decode ↓99% (500ns → 5ns), 0 allocations

---

## 🟢 MEDIUM: Fast JSON (sonic/json-iterator)

```go
import "github.com/bytedance/sonic"
// Or: import jsoniter "github.com/json-iterator/go"

data, _ := sonic.Marshal(obj) // 2-3x faster than stdlib
```

---

# 8️⃣ GAME-SPECIFIC PATTERNS

## 🔴 CRITICAL: Adaptive Tick Rate

```go
func (s *GameServer) AdaptTickRate() {
    count := s.PlayerCount()
    
    switch {
    case count < 50:
        s.tickRate = 8 * time.Millisecond   // 128 Hz
    case count < 200:
        s.tickRate = 16 * time.Millisecond  // 60 Hz
    case count < 500:
        s.tickRate = 33 * time.Millisecond  // 30 Hz
    default:
        s.tickRate = 50 * time.Millisecond  // 20 Hz
    }
}
```

**Gains:** CPU stable, Players: 50 → 500+ per server

---

## 🟡 HIGH: Interest Management

```go
type InterestLevel int

const (
    HighDetail   InterestLevel = iota // <50m: full updates
    MediumDetail                       // 50-150m: position only
    LowDetail                          // 150-300m: every 5 ticks
    NoInterest                         // >300m: nothing
)

func (s *Server) GetUpdateDetail(dist float32) InterestLevel {
    switch {
    case dist < 50:  return HighDetail
    case dist < 150: return MediumDetail
    case dist < 300: return LowDetail
    default:         return NoInterest
    }
}
```

**Gains:** Network ↓60-80%, CPU ↓50%

---

## 🟡 HIGH: Tick Budget Management

```go
func (s *GameServer) GameTick() {
    deadline := time.Now().Add(s.tickRate)
    
    s.ProcessPhysics(deadline)
    s.ProcessAI(deadline)
    s.ProcessNetwork(deadline)
    
    // Optional tasks if time remains
    if time.Now().Before(deadline) {
        s.ProcessNonCritical()
    }
    
    time.Sleep(time.Until(deadline))
}

func (s *GameServer) ProcessPhysics(deadline time.Time) {
    for _, entity := range s.entities {
        if time.Now().After(deadline) {
            break // Budget exceeded
        }
        entity.UpdatePhysics()
    }
}
```

**Gains:** Tick stability: ±10ms → ±1ms

---

# 9️⃣ ADVANCED PATTERNS

## 🟡 HIGH: Ring Buffer (lock-free)

```go
type RingBuffer struct {
    buffer   []Event
    size     int64
    writePos atomic.Int64
    readPos  atomic.Int64
}

func (rb *RingBuffer) Push(event Event) bool {
    write := rb.writePos.Load()
    next := (write + 1) % rb.size
    
    if next == rb.readPos.Load() {
        return false // Full
    }
    
    rb.buffer[write] = event
    rb.writePos.Store(next)
    return true
}
```

**Gains:** Lock contention -100%, Throughput ↑10x vs channel

---

## 🟢 MEDIUM: Copy-On-Write State

```go
type GameState struct {
    state atomic.Value // *StateSnapshot
}

// Read (lock-free, fast)
func (g *GameState) Get() *StateSnapshot {
    return g.state.Load().(*StateSnapshot)
}

// Write (copy entire state)
func (g *GameState) Update(fn func(*StateSnapshot)) {
    old := g.Get()
    new := old.Clone()
    fn(new)
    g.state.Store(new)
}
```

**Use cases:** Game configs, leaderboards  
**Gains:** Read throughput ↑100x

---

## 🟢 MEDIUM: Flyweight Pattern

```go
// Shared template
type WeaponTemplate struct {
    Name    string
    Model3D []byte // 10KB
    Damage  int
}

// Unique instance
type WeaponInstance struct {
    ID       uint64
    Template *WeaponTemplate // Pointer to shared
    OwnerID  uint64
}

var templates = map[string]*WeaponTemplate{
    "AK47": {Model3D: loadModel("ak47.obj")},
}
```

**For 1000 AK-47:** Memory: 10MB → 10KB (1000x savings!)

---

## 🟢 MEDIUM: Object Pooling (game objects)

```go
type BulletPool struct {
    pool sync.Pool
}

func (p *BulletPool) Get() *Bullet {
    bullet := p.pool.Get().(*Bullet)
    bullet.Reset()
    return bullet
}

func (p *BulletPool) Put(bullet *Bullet) {
    bullet.Active = false
    p.pool.Put(bullet)
}
```

**Use for:** Bullets, particles, VFX, temporary objects

---

# 🔟 GO 1.23+ FEATURES (NEW!)

## 🟡 HIGH: Profile-Guided Optimization (PGO)

```bash
# 1. Collect profile in production
curl http://prod:6060/debug/pprof/profile?seconds=30 > default.pgo

# 2. Put in project root
mv default.pgo .

# 3. Build with PGO (automatic if default.pgo exists)
go build

# 4. Deploy optimized binary
```

**Gains (Google data):** CPU ↓2-14%, Latency ↓1-10%  
**Available:** Go 1.20+, stable in 1.21+  
**FREE optimization!**

---

## 🟢 MEDIUM: Improved sync.Map (Go 1.23)

```go
// Go 1.23 improved sync.Map performance
var cache sync.Map

cache.Store(key, value) // Faster in 1.23
```

**Gains:** Write perf ↑20-30% (vs Go 1.22)

---

## 🟢 MEDIUM: Range-over-Func (Go 1.23)

```go
// Custom iterator (zero-allocation)
func (s *SpatialGrid) Players(yield func(*Player) bool) {
    for _, cell := range s.cells {
        for _, player := range cell.players {
            if !yield(player) {
                return
            }
        }
    }
}

// Usage
for player := range s.grid.Players {
    process(player) // No slice allocation!
}
```

---

# 1️⃣1️⃣ CACHING STRATEGIES

## 🔴 CRITICAL: Multi-Level Cache

```go
type CacheService struct {
    l1 sync.Map      // In-memory (1ms)
    l2 *redis.Client // Redis (5-10ms)
    l3 *sql.DB       // Database (20-50ms)
}

func (c *CacheService) Get(key string) (interface{}, error) {
    // L1
    if val, ok := c.l1.Load(key); ok {
        return val, nil // 95% hits
    }
    
    // L2
    if val, err := c.l2.Get(ctx, key).Result(); err == nil {
        c.l1.Store(key, val)
        return val, nil // 4% hits
    }
    
    // L3
    val, err := c.l3.Query(...)
    if err == nil {
        c.l2.Set(ctx, key, val, TTL)
        c.l1.Store(key, val)
    }
    return val, err // 1% misses
}
```

**Average latency:** 2-3ms (vs 30ms without cache)

---

## 🟡 HIGH: Cache Invalidation Strategies

```go
// Write-Through (consistency first)
func (s *Service) Update(player *Player) error {
    if err := s.db.Update(player); err != nil {
        return err
    }
    s.cache.Set(player.ID, player)
    return nil
}

// Write-Behind (speed first)
func (s *Service) Update(player *Player) error {
    s.cache.Set(player.ID, player)
    go s.db.Update(player) // Async
    return nil
}
```

---

# 1️⃣2️⃣ ADVANCED MEMORY

## 🟡 HIGH: Escape Analysis Optimization

```go
// OK Stays on stack
func process() {
    var buffer [1024]byte // Fixed size
    use(buffer[:])
}

// ❌ Escapes to heap
func process() *[]byte {
    buffer := make([]byte, 1024)
    return &buffer
}
```

**Check:** `go build -gcflags='-m' ./...`

---

## 🟢 MEDIUM: Cache Line Padding

```go
type CacheLinePadded struct {
    value uint64
    _     [56]byte // Pad to 64 bytes (cache line)
}
```

**When:** False sharing in hot structures

---

# 1️⃣3️⃣ WRAPPER TYPES (Hot Path)

## 🟡 HIGH: Optimized Wrappers

```go
// types.gen.go (generated - don't touch):
type Player struct {
    Id     string `json:"id"`
    Name   string `json:"name"`
    Level  int32  `json:"level"`
    Health int32  `json:"health"`
}

// player_internal.go (our optimization):
type PlayerInternal struct {
    ID       uint64   // 8 bytes (parsed from string)
    Health   int32    // 4 bytes
    Level    int32    // 4 bytes
    Name     [32]byte // 32 bytes (fixed, no alloc)
    IsActive bool     // 1 byte
}

var playerPool = sync.Pool{
    New: func() interface{} {
        return &PlayerInternal{}
    },
}

func ToInternal(p api.Player) *PlayerInternal {
    internal := playerPool.Get().(*PlayerInternal)
    internal.ID, _ = strconv.ParseUint(p.Id, 10, 64)
    internal.Health = p.Health
    internal.Level = p.Level
    copy(internal.Name[:], p.Name)
    return internal
}

// Use in hot path
func (s *GameServer) ProcessPlayers(apiPlayers []api.Player) {
    for _, p := range apiPlayers {
        internal := ToInternal(p)
        defer playerPool.Put(internal)
        
        s.updateGameState(internal) // Zero allocations!
    }
}
```

**When:** Game servers, >1000 ops/sec, after profiling shows allocations  
**Gains:** Allocations → 0, Memory ↓40-60%

---

# 1️⃣4️⃣ SECURITY & STABILITY

## 🔴 CRITICAL: Rate Limiting

```go
import "golang.org/x/time/rate"

type RateLimiter struct {
    limiters sync.Map
}

func (rl *RateLimiter) Allow(userID string) bool {
    limiter, _ := rl.limiters.LoadOrStore(userID,
        rate.NewLimiter(100, 200)) // 100/sec, burst 200
    
    return limiter.(*rate.Limiter).Allow()
}
```

---

## 🟡 HIGH: Circuit Breaker

```go
import "github.com/sony/gobreaker"

var cb = gobreaker.NewCircuitBreaker(gobreaker.Settings{
    MaxRequests: 3,
    Interval:    10 * time.Second,
    Timeout:     30 * time.Second,
})

func CallExternal() error {
    _, err := cb.Execute(func() (interface{}, error) {
        return externalService.Call()
    })
    return err
}
```

**Prevents:** Cascading failures

---

## 🟢 MEDIUM: Graceful Shutdown

```go
func (s *Server) Run() error {
    ctx, stop := signal.NotifyContext(context.Background(),
        os.Interrupt, syscall.SIGTERM)
    defer stop()
    
    go s.httpServer.ListenAndServe()
    
    <-ctx.Done()
    
    shutdownCtx, cancel := context.WithTimeout(
        context.Background(), 30*time.Second)
    defer cancel()
    
    return s.httpServer.Shutdown(shutdownCtx)
}
```

---

# 1️⃣5️⃣ PROTOCOL PATTERNS

## 🟡 HIGH: Protocol Versioning

```go
type PacketHeader struct {
    Version    uint8  // Protocol version
    PacketType uint8
    Sequence   uint16
}

func (s *Server) HandlePacket(data []byte) {
    if data[0] != CurrentProtocolVersion {
        s.HandleLegacyPacket(data) // Backward compat
        return
    }
    s.HandleCurrentPacket(data)
}
```

---

## 🟢 MEDIUM: Jitter Buffer

```go
type JitterBuffer struct {
    packets map[uint16]*Packet
    delay   time.Duration // ~100ms
}

func (jb *JitterBuffer) Add(seq uint16, packet *Packet) {
    jb.packets[seq] = packet
    
    time.AfterFunc(jb.delay, func() {
        jb.PlaySequence(seq)
    })
}
```

**For:** Smooth gameplay despite network jitter

---

# 📊 EXPECTED GAINS (Part 2)

## After implementing Part 1 + Part 2:

| Metric | Baseline | Part 1 | Part 1+2 | Total Gain |
|--------|----------|--------|----------|------------|
| Throughput | 2k/s | 10k/s | 15k/s | **+650%** |
| P99 Latency | 150ms | 40ms | 10ms | **-93%** |
| Network | 10GB/s | 5GB/s | 300MB/s | **-97%** |
| Max Players | 50 | 100 | 500 | **+900%** |
| Tick Jitter | ±10ms | ±5ms | ±1ms | **-90%** |

---

**Next:** Part 3 - Profiling, Testing, Tools  
**See also:** `.cursor/performance/01-memory-concurrency-db.md`

