# 📖 Go Performance Bible - Part 1

**Memory, Concurrency & Database Optimizations**

**Версия:** 2.0 | **Дата:** 01.12.2025 | **Go:** 1.23+

---

# 1️⃣ MEMORY & GC OPTIMIZATIONS

## 🔴 CRITICAL: Memory Pooling (sync.Pool)

```go
var responsePool = sync.Pool{
    New: func() interface{} {
        return &Response{Data: make([]byte, 0, 4096)}
    },
}

func Handler(w http.ResponseWriter, r *http.Request) {
    resp := responsePool.Get().(*Response)
    defer func() {
        resp.Data = resp.Data[:0]
        responsePool.Put(resp)
    }()
}
```

**Gains:** Allocations ↓80-90%, GC pause ↓60-70%, Latency ↓20-30%

---

## 🔴 CRITICAL: Struct Field Alignment

```go
// ❌ Bad: 32 bytes
type Player struct {
    IsActive bool   // 1 + 7 padding
    ID       uint64 // 8
    Level    uint8  // 1 + 7 padding
}

// ✅ Good: 16 bytes (-50%)
type Player struct {
    ID       uint64 // 8
    Level    uint8  // 1
    IsActive bool   // 1
}
```

**Tool:** `fieldalignment ./...`  
**Gains:** Memory ↓30-50%, Cache hits ↑15-20%

---

## 🟡 HIGH: Preallocation

```go
// ❌ Bad: reallocations
items := []Item{}
for i := 0; i < 1000; i++ {
    items = append(items, Item{})
}

// ✅ Good: preallocate
items := make([]Item, 0, 1000)
```

**Gains:** Allocations ↓90%, CPU ↓15-20%

---

## 🟡 HIGH: String vs []byte

```go
// Hot path: use []byte
func Process(data []byte) { // Not string!
    bytes.ToUpper(data) // In-place
}
```

**Gains:** Allocations ↓50-70%

---

## 🟢 MEDIUM: Arena Allocator (Go 1.20+)

```go
import "arena"

func ProcessBatch() {
    mem := arena.NewArena()
    defer mem.Free()
    
    for i := 0; i < 10000; i++ {
        item := arena.New[Item](mem)
        process(item)
    }
}
```

**Status:** Experimental  
**Gains:** GC ↓90%, Latency ↓40-60% (batch processing)

---

## 🟡 HIGH: GC Tuning

```go
import "runtime/debug"

func init() {
    // Low-latency (game servers)
    debug.SetGCPercent(50) // More frequent, shorter pauses
    
    // High-throughput (batch)
    debug.SetGCPercent(200) // Less frequent, higher throughput
    
    // Memory limit (Go 1.19+)
    debug.SetMemoryLimit(2 * 1024 * 1024 * 1024) // 2GB
}
```

**Gains:** GC pause ↓40-60%

---

# 2️⃣ CONCURRENCY OPTIMIZATIONS

## 🔴 CRITICAL: Lock-Free Structures

```go
// ❌ Mutex: 50ns/op
type Counter struct {
    mu    sync.Mutex
    value int64
}

// ✅ Atomic: 5ns/op
type Counter struct {
    value atomic.Int64
}
```

**Gains:** Latency ↓90%, Contention -100%, Throughput ↑300-500%

---

## 🔴 CRITICAL: Worker Pool

```go
type WorkerPool struct {
    semaphore chan struct{}
}

func NewWorkerPool(size int) *WorkerPool {
    return &WorkerPool{semaphore: make(chan struct{}, size)}
}

func (p *WorkerPool) Submit(task func()) {
    p.semaphore <- struct{}{}
    go func() {
        defer func() { <-p.semaphore }()
        task()
    }()
}
```

**Gains:** Memory ↓90%, Scheduler overhead ↓80%

---

## 🟡 HIGH: SingleFlight Pattern (NEW 2024!)

```go
import "golang.org/x/sync/singleflight"

type Service struct {
    sf singleflight.Group
}

func (s *Service) GetPlayer(ctx context.Context, id string) (*Player, error) {
    // 100 parallel requests → 1 execution
    result, err, _ := s.sf.Do(id, func() (interface{}, error) {
        return s.repo.GetPlayer(ctx, id)
    })
    return result.(*Player), err
}
```

**Use cases:** Cache stampede, thundering herd  
**Gains:** DB queries ↓90-95% (burst), Latency ↓80%

---

## 🟡 HIGH: ErrGroup Pattern (NEW 2024!)

```go
import "golang.org/x/sync/errgroup"

func LoadData(ctx context.Context, id string) error {
    g, ctx := errgroup.WithContext(ctx)
    
    g.Go(func() error { return loadProfile(ctx, id) })
    g.Go(func() error { return loadInventory(ctx, id) })
    g.Go(func() error { return loadStats(ctx, id) })
    
    return g.Wait() // Parallel + error handling
}
```

**Gains:** Latency ↓70% (parallel vs sequential)

---

## 🟢 MEDIUM: Bounded Channels

```go
// ✅ Always bounded + backpressure
events := make(chan Event, 1000)

select {
case events <- event:
    // OK
case <-time.After(10 * time.Millisecond):
    return ErrOverloaded // Shed load
}
```

**Prevents:** Memory leaks, unbounded growth

---

## 🟢 MEDIUM: Context Timeouts

```go
const (
    DBTimeout    = 50 * time.Millisecond
    CacheTimeout = 10 * time.Millisecond
    ExtTimeout   = 200 * time.Millisecond
)

func (s *Service) Process(ctx context.Context) error {
    ctx, cancel := context.WithTimeout(ctx, DBTimeout)
    defer cancel()
    return s.repo.Query(ctx)
}
```

---

# 3️⃣ DATABASE OPTIMIZATIONS

## 🔴 CRITICAL: Batch Operations

```go
// ❌ N+1: 2000ms for 1000 IDs
for _, id := range ids {
    player := db.Get(id) // 1000 queries
}

// ✅ Batch: 5ms
players := db.GetBatch(ids) // 1 query
```

**Gains:** Queries ↓99%, Latency ↓99.7%, DB load ↓95%

---

## 🔴 CRITICAL: Connection Pooling

```go
db.SetMaxOpenConns(25)
db.SetMaxIdleConns(25)
db.SetConnMaxLifetime(5 * time.Minute)
db.SetConnMaxIdleTime(10 * time.Minute)
```

**Recommendations:**
- Game servers: 25 connections
- API services: 50 connections
- Analytics: 10 connections

---

## 🟡 HIGH: Prepared Statements

```go
type Repository struct {
    getPlayerStmt *sql.Stmt
}

func NewRepository(db *sql.DB) *Repository {
    stmt, _ := db.Prepare("SELECT * FROM players WHERE id = $1")
    return &Repository{getPlayerStmt: stmt}
}
```

**Gains:** Query planning ↓100%, Latency ↓10-15%

---

## 🟢 MEDIUM: Read Replicas

```go
type Repository struct {
    master  *sql.DB // Writes
    replica *sql.DB // Reads
}

func (r *Repository) GetPlayer(id string) (*Player, error) {
    return r.replica.QueryRow(...) // Read from replica
}
```

**Gains:** Master load ↓70-80%, Read throughput ↑300-500%

---

# 4️⃣ GOROUTINE MANAGEMENT

## 🔴 CRITICAL: Goroutine Leak Detection

```go
import "go.uber.org/goleak"

func TestMain(m *testing.M) {
    goleak.VerifyTestMain(m)
}

func TestNoLeaks(t *testing.T) {
    defer goleak.VerifyNone(t)
    
    svc := NewService()
    svc.Start()
    svc.Stop()
}
```

**Real case:** Uber - +34% speed, memory ↓9.2x after fixing leaks

---

## 🟡 HIGH: Runtime Tuning

```go
func init() {
    // Dedicated server
    runtime.GOMAXPROCS(runtime.NumCPU() - 1)
    
    // Or let K8s set it (respects CPU limits)
}
```

---

## 🟢 MEDIUM: Defer in Hot Path

```go
// ❌ Hot path: defer overhead
func HotPath() {
    mu.Lock()
    defer mu.Unlock() // 50ns overhead
}

// ✅ Manual unlock
func HotPath() {
    mu.Lock()
    mu.Unlock() // No overhead
}
```

**Nano-optimization:** Only for >100k ops/sec

---

# 5️⃣ ESCAPE ANALYSIS

## 🟡 HIGH: Keep Data on Stack

```bash
# Check escape analysis
go build -gcflags='-m' ./... 2>&1 | grep "escapes"
```

```go
// ✅ Stack (fast)
func process() {
    var buffer [1024]byte
    use(buffer[:])
}

// ❌ Heap (slow)
func process() *Buffer {
    buffer := make([]byte, 1024)
    return &buffer // Escapes!
}
```

---

# 📊 EXPECTED GAINS (Part 1)

## After implementing Part 1:

| Metric | Before | After | Gain |
|--------|--------|-------|------|
| Throughput | 2k/s | 8-10k/s | +400% |
| P99 Latency | 150ms | 30-50ms | -70% |
| Memory | 500MB | 200MB | -60% |
| GC Pause | 15ms | 4-6ms | -70% |
| DB Load | 100% | 20% | -80% |

---

**Next:** Part 2 - Network, Game, Advanced  
**See also:** `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`

