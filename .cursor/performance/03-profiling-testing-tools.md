# 📖 Go Performance Bible - Part 3

**Profiling, Testing, Tools & Summary**

**Версия:** 2.0 | **Дата:** 01.12.2025 | **Go:** 1.23+

---

# 1️⃣6️⃣ PROFILING & MONITORING

## 🔴 CRITICAL: pprof Endpoints

```go
import _ "net/http/pprof"

func main() {
    // Separate port for security
    go func() {
        http.ListenAndServe("localhost:6060", nil)
    }()
}
```

**Usage:**
```bash
# CPU profile (30 sec)
go tool pprof http://localhost:6060/debug/pprof/profile?seconds=30

# Memory profile
go tool pprof http://localhost:6060/debug/pprof/heap

# Goroutine profile
go tool pprof http://localhost:6060/debug/pprof/goroutine

# Allocations
go tool pprof http://localhost:6060/debug/pprof/allocs
```

---

## 🟡 HIGH: Continuous Profiling (NEW 2024!)

```go
import "github.com/grafana/pyroscope-go"

pyroscope.Start(pyroscope.Config{
    ApplicationName: "necpgame.matchmaking",
    ServerAddress:   "http://pyroscope:4040",
    
    ProfileTypes: []pyroscope.ProfileType{
        pyroscope.ProfileCPU,
        pyroscope.ProfileAllocObjects,
        pyroscope.ProfileAllocSpace,
        pyroscope.ProfileInuseObjects,
        pyroscope.ProfileInuseSpace,
    },
})
```

**Benefits:**
- Profile in production 24/7
- Detect regressions automatically
- Compare before/after deployments

**Tools:** Pyroscope, Grafana Phlare

---

## 🟡 HIGH: Execution Tracer

```go
import "runtime/trace"

func main() {
    f, _ := os.Create("trace.out")
    trace.Start(f)
    defer trace.Stop()
    
    // Run workload...
}

// Analysis: go tool trace trace.out
```

**Shows:**
- Goroutine scheduling
- GC events
- Network blocking
- Syscalls
- Lock contention

---

## 🟡 HIGH: Prometheus Metrics

```go
import "github.com/prometheus/client_golang/prometheus/promauto"

var (
    requestDuration = promauto.NewHistogramVec(
        prometheus.HistogramOpts{
            Name:    "http_request_duration_seconds",
            Buckets: []float64{.001, .005, .01, .025, .05, .1, .5, 1},
        },
        []string{"handler"},
    )
    
    gcPauseDuration = promauto.NewHistogram(
        prometheus.HistogramOpts{
            Name:    "go_gc_pause_duration_seconds",
            Buckets: prometheus.ExponentialBuckets(0.0001, 2, 10),
        },
    )
)

// Monitor GC
func monitorGC() {
    var stats runtime.MemStats
    for {
        runtime.ReadMemStats(&stats)
        gcPauseDuration.Observe(
            float64(stats.PauseNs[(stats.NumGC+255)%256]) / 1e9,
        )
        time.Sleep(1 * time.Second)
    }
}
```

**RED metrics (must have):**
- **R**ate (requests/sec)
- **E**rrors (error rate)
- **D**uration (latency)

---

# 1️⃣7️⃣ TESTING & VALIDATION

## 🔴 CRITICAL: Load Testing

```bash
# vegeta (HTTP)
echo "GET http://api:8080/players" | \
  vegeta attack -duration=60s -rate=5000/s | \
  vegeta report

# k6 (complex scenarios)
k6 run --vus 1000 --duration 60s load.js

# gatling (enterprise)
gatling.sh -s GameServerSimulation
```

**Check:**
- P99 latency < 50ms
- Error rate < 0.1%
- No memory leaks
- Stable throughput

---

## 🟡 HIGH: Benchmarks + CI

```go
func BenchmarkCriticalPath(b *testing.B) {
    b.ReportAllocs()
    b.ResetTimer()
    
    for i := 0; i < b.N; i++ {
        CriticalFunction()
    }
}

// Performance budget test
func TestPerformanceBudget(t *testing.T) {
    result := testing.Benchmark(BenchmarkCriticalPath)
    
    // Budget: 0 allocs/op
    if result.AllocsPerOp() > 0 {
        t.Fatalf("Allocation regression: %d allocs/op", 
            result.AllocsPerOp())
    }
    
    // Budget: <1ms per op
    if result.NsPerOp() > 1000000 {
        t.Fatalf("Latency regression: %dns/op", 
            result.NsPerOp())
    }
}
```

---

## 🟢 MEDIUM: Chaos Testing

```go
// Test failure scenarios:
// - Random pod kills
// - Network delays/packet loss
// - DB failures
// - CPU throttling

// Service must survive and recover!
```

**Tools:** chaos-mesh, LitmusChaos

---

# 1️⃣8️⃣ INSTRUMENTATION

## 🟡 HIGH: Distributed Tracing

```go
import "go.opentelemetry.io/otel"

func (s *Service) ProcessRequest(ctx context.Context) error {
    ctx, span := otel.Tracer("service").Start(ctx, "ProcessRequest")
    defer span.End()
    
    // DB call with trace
    dbCtx, dbSpan := otel.Tracer("service").Start(ctx, "DB.GetPlayer")
    player, err := s.repo.GetPlayer(dbCtx, id)
    dbSpan.End()
    
    return err
}
```

**Shows:**
- Where time is spent
- Cross-service calls
- Bottlenecks in microservices

**Tools:** Jaeger, Tempo, Zipkin

---

## 🟢 MEDIUM: Structured Logging

```go
import "go.uber.org/zap"

logger, _ := zap.NewProduction()

logger.Info("player joined",
    zap.String("player_id", id),
    zap.Int("level", level),
    zap.Duration("load_time", duration),
)
```

**NOT:**
```go
fmt.Println("Player", id, "joined") // ❌
log.Printf("Player %s joined", id)  // ❌
```

---

# 🛠️ TOOLS & LIBRARIES (2024-2025)

## Must-Have Libraries:

```go
// Leak detection
"go.uber.org/goleak"

// Concurrency patterns
"golang.org/x/sync/singleflight"
"golang.org/x/sync/errgroup"

// Metrics
"github.com/prometheus/client_golang/prometheus"

// Tracing
"go.opentelemetry.io/otel"

// Logging
"go.uber.org/zap"

// Rate limiting
"golang.org/x/time/rate"
```

## Performance Libraries:

```go
// Fast JSON
"github.com/bytedance/sonic"
"github.com/json-iterator/go"

// Protobuf
"google.golang.org/protobuf"

// FlatBuffers
"github.com/google/flatbuffers"

// Circuit breaker
"github.com/sony/gobreaker"
```

## Profiling Tools:

- **Built-in:** `net/http/pprof`, `runtime/trace`
- **Continuous:** Pyroscope, Grafana Phlare
- **Load testing:** vegeta, k6, Gatling
- **Tracing:** Jaeger, Tempo
- **Metrics:** Prometheus, VictoriaMetrics

---

# 📊 PRIORITY MATRIX

## 🔴 P0 - CRITICAL (implement FIRST):

| Optimization | Impact | Effort | When |
|--------------|--------|--------|------|
| Batch DB ops | ⭐⭐⭐⭐⭐ | 🟢 Low | ALWAYS |
| Spatial partitioning | ⭐⭐⭐⭐⭐ | 🟡 Med | >100 players |
| Memory pooling | ⭐⭐⭐⭐ | 🟢 Low | Hot path |
| Lock-free | ⭐⭐⭐⭐ | 🟢 Low | Counters |
| Context timeouts | ⭐⭐⭐⭐ | 🟢 Low | ALWAYS |
| pprof endpoints | ⭐⭐⭐⭐ | 🟢 Low | ALWAYS |
| Goroutine leak tests | ⭐⭐⭐⭐ | 🟢 Low | ALWAYS |

## 🟡 P1 - HIGH (strong impact):

| Optimization | Impact | Effort | When |
|--------------|--------|--------|------|
| Struct alignment | ⭐⭐⭐ | 🟢 Low | Start of project |
| SingleFlight | ⭐⭐⭐⭐ | 🟢 Low | Cache stampede |
| UDP protocol | ⭐⭐⭐⭐⭐ | 🟡 Med | Game state |
| Delta compression | ⭐⭐⭐⭐ | 🟡 Med | Network heavy |
| Worker pools | ⭐⭐⭐ | 🟢 Low | Many goroutines |
| ErrGroup | ⭐⭐⭐ | 🟢 Low | Parallel ops |

## 🟢 P2 - MEDIUM (good to have):

| Optimization | Impact | Effort | When |
|--------------|--------|--------|------|
| PGO | ⭐⭐⭐ | 🟢 Low | Production |
| Ring buffer | ⭐⭐⭐ | 🟡 Med | Event processing |
| Multi-level cache | ⭐⭐⭐ | 🟡 Med | Read-heavy |
| Flyweight pattern | ⭐⭐⭐ | 🟡 Med | Shared objects |

## ⚪ P3 - LOW (edge cases):

| Optimization | Impact | Effort | When |
|--------------|--------|--------|------|
| FlatBuffers | ⭐⭐⭐⭐ | 🔴 High | Protobuf bottleneck |
| ogen generator | ⭐⭐⭐⭐⭐ | 🔴 High | JSON bottleneck |
| Arena allocator | ⭐⭐⭐ | 🟡 Med | Experimental |

---

# 🎯 IMPLEMENTATION ROADMAP

## Week 1: Foundation (P0 basics)
- [ ] Struct field alignment
- [ ] Context timeouts
- [ ] DB connection pooling
- [ ] Structured logging
- [ ] pprof endpoints
- [ ] Prometheus metrics

**Expected:** +100-150% throughput

---

## Week 2-3: Hot Path (P0 + P1)
- [ ] Memory pooling (sync.Pool)
- [ ] Batch DB operations
- [ ] Lock-free counters
- [ ] Preallocation
- [ ] Goleak tests
- [ ] SingleFlight pattern

**Expected:** +400-500% throughput

---

## Week 4-6: Game Optimizations (P1 + P2)
- [ ] Spatial partitioning
- [ ] UDP protocol
- [ ] Delta compression
- [ ] Worker pools
- [ ] Adaptive tick rate
- [ ] Interest management

**Expected:** 50 → 500+ players per server

---

## Month 2+: Advanced (P2 + P3 if needed)
- [ ] PGO compilation
- [ ] Ring buffers
- [ ] Continuous profiling
- [ ] Multi-level cache
- [ ] FlatBuffers (if Protobuf bottleneck)
- [ ] Wrapper types (if allocations critical)

**Expected:** Production-grade stability

---

# 💰 ROI CALCULATION

## Single Service:

| Phase | Dev Time | Monthly Savings | Payback |
|-------|----------|-----------------|---------|
| Week 1 | 2-3 days | $200-400 | 1 week |
| Week 2-3 | 1-2 weeks | $500-1000 | 2-3 weeks |
| Week 4-6 | 2-3 weeks | $1000-2000 | 1 month |
| Month 2+ | 1 month | $1500-3000 | 1.5 months |

## For 20 Services:

- **Total dev time:** 6-12 months (parallel work)
- **Monthly savings:** $10k-40k
- **Annual savings:** $120k-480k
- **Payback:** 2-3 months

---

# 📈 CUMULATIVE GAINS

## CRUD API Service:

| Metric | Baseline | Week 1 | Week 3 | Week 6 | Month 2+ |
|--------|----------|--------|--------|--------|----------|
| **Throughput** | 2k/s | 4k/s | 12k/s | 15k/s | 18k/s |
| **P99 Latency** | 150ms | 80ms | 15ms | 10ms | 8ms |
| **Memory** | 500MB | 350MB | 150MB | 120MB | 100MB |
| **GC Pause** | 15ms | 8ms | 3ms | 2ms | 1ms |
| **Allocations** | 15/op | 8/op | 3/op | 1/op | 0/op |

## Game Server:

| Metric | Baseline | Week 1 | Week 3 | Week 6 | Month 2+ |
|--------|----------|--------|--------|--------|----------|
| **Max Players** | 50 | 100 | 150 | 500 | 1000+ |
| **Network** | 10GB/s | 5GB/s | 1GB/s | 300MB/s | 200MB/s |
| **Tick Jitter** | ±10ms | ±5ms | ±2ms | ±1ms | ±0.5ms |
| **CPU Usage** | 80% | 60% | 50% | 40% | 35% |
| **Latency** | 80ms | 50ms | 30ms | 20ms | 15ms |

---

# OK VALIDATION CHECKLIST

## Before Handoff to Network/QA:

### Automatic Checks:
```bash
# Struct alignment
fieldalignment ./...

# Goroutine leaks
go test -v ./... -run TestMain

# Benchmarks
go test -bench=. -benchmem | grep "allocs/op"

# Profiling
curl http://localhost:6060/debug/pprof/allocs > allocs.prof
go tool pprof -top allocs.prof
```

### Code Patterns:
```bash
# Memory pooling
grep -r "sync.Pool" server/

# Batch operations
grep -r "Batch" server/repository.go

# Context timeouts
grep -r "context.WithTimeout" server/

# Atomic operations
grep -r "atomic\." server/
```

### Manual Review:
- [ ] All external calls have timeouts
- [ ] DB pool configured (25-50 connections)
- [ ] Hot path has 0 allocs/op
- [ ] No fmt.Println (structured logging only)
- [ ] Profiling endpoints enabled
- [ ] Benchmarks pass performance budgets

---

# 🎯 QUICK START GUIDE

## For Backend Agent:

### 1. Use Templates:
```
.cursor/templates/backend-api-templates.md     - handlers, service, repo
.cursor/templates/backend-game-templates.md    - game server, spatial grid
.cursor/templates/backend-utils-templates.md   - cache, workers, tests
```

### 2. Implement Optimizations:

**Level 1 (ALL services):**
- Context timeouts
- DB pool config
- Struct alignment
- Goleak tests

**Level 2 (Hot path, >100 RPS):**
- Memory pooling
- Batch operations
- Lock-free counters

**Level 3 (Game servers):**
- Spatial partitioning
- UDP protocol
- Adaptive tick rate

### 3. Validate:
```bash
./scripts/validate-backend-optimizations.sh services/{service}-go
```

### 4. Benchmark:
```bash
go test -bench=. -benchmem -cpuprofile=cpu.prof
go tool pprof -http=:8080 cpu.prof
```

---

# 📚 REFERENCES & RESOURCES

## Official Go Resources:
- Go 1.23 Release Notes: https://go.dev/doc/go1.23
- Go Performance Tips: https://go.dev/wiki/Performance
- Go Memory Model: https://go.dev/ref/mem

## Community Resources:
- Dave Cheney Performance Workshop: https://dave.cheney.net/high-performance-go-workshop
- Uber Go Style Guide: https://github.com/uber-go/guide
- Google Go Guide: https://google.github.io/styleguide/go/

## Tools Documentation:
- pprof: https://github.com/google/pprof
- Pyroscope: https://pyroscope.io/docs
- vegeta: https://github.com/tsenart/vegeta
- k6: https://k6.io/docs

## Libraries:
- goleak: https://github.com/uber-go/goleak
- singleflight: https://pkg.go.dev/golang.org/x/sync/singleflight
- errgroup: https://pkg.go.dev/golang.org/x/sync/errgroup

---

# 🎓 LEARNING PATH

## Beginner (Week 1-2):
1. Read: `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`
2. Implement: P0 optimizations
3. Learn: pprof basics
4. Practice: Simple benchmarks

## Intermediate (Week 3-6):
1. Implement: P1 optimizations
2. Learn: Advanced profiling (trace, allocs)
3. Practice: Load testing
4. Study: Game-specific patterns

## Advanced (Month 2+):
1. Implement: P2-P3 optimizations
2. Learn: PGO, continuous profiling
3. Experiment: FlatBuffers, ogen
4. Master: Production debugging

---

# 🏆 SUCCESS METRICS

## Service Health:

| Metric | Target | Alert If |
|--------|--------|----------|
| P99 Latency | <10ms | >50ms |
| Throughput | >10k/s | <5k/s |
| Error Rate | <0.1% | >1% |
| GC Pause | <1ms | >5ms |
| Goroutines | Stable | Growing |
| Memory | Stable | Leaking |

## Business Impact:

- **Infrastructure cost:** ↓60-80%
- **Player experience:** Smooth, no lag
- **Concurrent players:** 10x more per server
- **Reliability:** 99.9%+ uptime

---

# 🎯 FINAL SUMMARY

## Total Optimizations: 75+ techniques

**Categories:**
1. Memory & GC (6)
2. Concurrency (6)
3. Database (4)
4. Network (6)
5. Goroutine (3)
6. Serialization (3)
7. Game-Specific (3)
8. Advanced Memory (2)
9. Caching (2)
10. Go 1.23+ Features (3)
11. Wrapper Types (1)
12. Security (3)
13. Profiling (4)
14. Testing (3)
15. Protocol Patterns (2)
16. Instrumentation (2)

**Priority Breakdown:**
- 🔴 P0 (Critical): 10 techniques
- 🟡 P1 (High): 15 techniques
- 🟢 P2 (Medium): 12 techniques
- ⚪ P3 (Low): 3 techniques

---

## Expected Overall Gains:

### CRUD API:
- Throughput: 2k → **18k req/sec** (+800%)
- Latency: 150ms → **8ms P99** (-95%)
- Memory: 500MB → **100MB** (-80%)
- Infrastructure cost: **-70%**

### Game Server:
- Capacity: 50 → **1000+ players** (+2000%)
- Network: 10GB/s → **200MB/s** (-98%)
- Tick stability: ±10ms → **±0.5ms** (-95%)
- Player experience: **Production-ready**

---

## 🚀 START HERE:

1. Read all 3 parts
2. Check `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`
3. Use templates from `.cursor/templates/backend-*.md`
4. Run `/backend-validate-optimizations #123`
5. Deploy and measure!

---

**Parts:**
- Part 1: Memory, Concurrency, DB (this file's siblings)
- Part 2: Network, Game, Advanced
- Part 3: Profiling, Testing, Tools (you are here!)

**Last updated:** 01.12.2025  
**Next review:** Every 6 months (new Go releases)

