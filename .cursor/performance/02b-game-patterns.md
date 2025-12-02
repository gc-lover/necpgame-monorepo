# 📖 Go Performance Bible - Part 2B

**Game-Specific Patterns & Advanced Techniques**

---

# GAME-SPECIFIC PATTERNS

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

**Gains:** CPU stable, Players: 50 → 500+

---

## 🟡 HIGH: Interest Management

```go
type InterestLevel int

const (
    HighDetail   InterestLevel = iota // <50m
    MediumDetail                       // 50-150m
    LowDetail                          // 150-300m
    NoInterest                         // >300m
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
    
    if time.Now().Before(deadline) {
        s.ProcessNonCritical()
    }
    
    time.Sleep(time.Until(deadline))
}
```

**Gains:** Tick stability: ±10ms → ±1ms

---

# ADVANCED PATTERNS

## 🟡 HIGH: Ring Buffer

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
        return false
    }
    
    rb.buffer[write] = event
    rb.writePos.Store(next)
    return true
}
```

**Gains:** Throughput ↑10x vs channel

---

## 🟢 MEDIUM: Copy-On-Write

```go
type GameState struct {
    state atomic.Value
}

func (g *GameState) Get() *StateSnapshot {
    return g.state.Load().(*StateSnapshot)
}

func (g *GameState) Update(fn func(*StateSnapshot)) {
    old := g.Get()
    new := old.Clone()
    fn(new)
    g.state.Store(new)
}
```

**Gains:** Read throughput ↑100x

---

## 🟢 MEDIUM: Flyweight Pattern

```go
type WeaponTemplate struct {
    Name    string
    Model3D []byte // 10KB
    Damage  int
}

type WeaponInstance struct {
    ID       uint64
    Template *WeaponTemplate
    OwnerID  uint64
}

var templates = map[string]*WeaponTemplate{
    "AK47": {Model3D: loadModel("ak47.obj")},
}
```

**For 1000 AK-47:** 10MB → 10KB

---

# WRAPPER TYPES

## 🟡 HIGH: Optimized Wrappers

```go
// types.gen.go (generated):
type Player struct {
    Id     string
    Name   string
    Level  int32
    Health int32
}

// player_internal.go (optimized):
type PlayerInternal struct {
    ID       uint64   // Parsed
    Health   int32
    Level    int32
    Name     [32]byte // Fixed
    IsActive bool
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
```

**When:** >1000 ops/sec, allocations critical  
**Gains:** Allocations → 0, Memory ↓40-60%

---

# GO 1.23+ FEATURES

## 🟡 HIGH: PGO (Profile-Guided Optimization)

```bash
# 1. Collect production profile
curl http://prod:6060/debug/pprof/profile?seconds=30 > default.pgo

# 2. Place in project root
mv default.pgo .

# 3. Build (automatically uses default.pgo)
go build

# 4. Deploy optimized binary
```

**Gains:** CPU ↓2-14%, Latency ↓1-10%  
**FREE optimization!**

---

## 🟢 MEDIUM: Improved sync.Map (Go 1.23)

```go
var cache sync.Map
cache.Store(key, value) // Faster in 1.23
```

**Gains:** Write ↑20-30% vs 1.22

---

## 🟢 MEDIUM: Range-over-Func (Go 1.23)

```go
func (s *SpatialGrid) Players(yield func(*Player) bool) {
    for _, cell := range s.cells {
        for _, player := range cell.players {
            if !yield(player) {
                return
            }
        }
    }
}

for player := range s.grid.Players {
    process(player) // No allocation!
}
```

---

# CACHING STRATEGIES

## 🔴 CRITICAL: Multi-Level Cache

```go
type CacheService struct {
    l1 sync.Map      // 1ms
    l2 *redis.Client // 5-10ms
    l3 *sql.DB       // 20-50ms
}

func (c *CacheService) Get(key string) (interface{}, error) {
    if val, ok := c.l1.Load(key); ok {
        return val, nil // 95% hits
    }
    
    if val, err := c.l2.Get(ctx, key).Result(); err == nil {
        c.l1.Store(key, val)
        return val, nil // 4% hits
    }
    
    val, err := c.l3.Query(...)
    if err == nil {
        c.l2.Set(ctx, key, val, TTL)
        c.l1.Store(key, val)
    }
    return val, err // 1% misses
}
```

**Average:** 2-3ms (vs 30ms без cache)

---

# SECURITY & STABILITY

## 🔴 CRITICAL: Rate Limiting

```go
import "golang.org/x/time/rate"

type RateLimiter struct {
    limiters sync.Map
}

func (rl *RateLimiter) Allow(userID string) bool {
    limiter, _ := rl.limiters.LoadOrStore(userID,
        rate.NewLimiter(100, 200))
    
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

**Next:** [Part 3A - Profiling](./03a-profiling-testing.md)  
**Previous:** [Part 1](./01-memory-concurrency-db.md)

