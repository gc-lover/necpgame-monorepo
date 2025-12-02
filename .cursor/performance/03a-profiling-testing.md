# 📖 Go Performance Bible - Part 3A

**Profiling, Monitoring & Testing**

---

# PROFILING & MONITORING

## 🔴 CRITICAL: pprof Endpoints

```go
import _ "net/http/pprof"

func main() {
    go func() {
        http.ListenAndServe("localhost:6060", nil)
    }()
}
```

**Usage:**
```bash
# CPU (30 sec)
go tool pprof http://localhost:6060/debug/pprof/profile?seconds=30

# Memory
go tool pprof http://localhost:6060/debug/pprof/heap

# Goroutines
go tool pprof http://localhost:6060/debug/pprof/goroutine

# Allocations
go tool pprof http://localhost:6060/debug/pprof/allocs
```

---

## 🟡 HIGH: Continuous Profiling (NEW!)

```go
import "github.com/grafana/pyroscope-go"

pyroscope.Start(pyroscope.Config{
    ApplicationName: "necpgame.matchmaking",
    ServerAddress:   "http://pyroscope:4040",
    
    ProfileTypes: []pyroscope.ProfileType{
        pyroscope.ProfileCPU,
        pyroscope.ProfileAllocObjects,
        pyroscope.ProfileInuseSpace,
    },
})
```

**Benefits:** 24/7 profiling, regression detection

---

## 🟡 HIGH: Execution Tracer

```go
import "runtime/trace"

func main() {
    f, _ := os.Create("trace.out")
    trace.Start(f)
    defer trace.Stop()
}

// Analysis: go tool trace trace.out
```

**Shows:** Goroutine scheduling, GC, blocking, syscalls

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
)

func monitorGC() {
    var stats runtime.MemStats
    for {
        runtime.ReadMemStats(&stats)
        gcPause.Observe(
            float64(stats.PauseNs[(stats.NumGC+255)%256]) / 1e9,
        )
        time.Sleep(1 * time.Second)
    }
}
```

**RED metrics:** Rate, Errors, Duration

---

# TESTING & VALIDATION

## 🔴 CRITICAL: Load Testing

```bash
# vegeta
echo "GET http://api:8080/players" | \
  vegeta attack -duration=60s -rate=5000/s | \
  vegeta report

# k6
k6 run --vus 1000 --duration 60s load.js
```

**Check:** P99 <50ms, Error rate <0.1%

---

## 🟡 HIGH: Benchmarks + Budgets

```go
func BenchmarkCritical(b *testing.B) {
    b.ReportAllocs()
    b.ResetTimer()
    
    for i := 0; i < b.N; i++ {
        CriticalFunction()
    }
}

func TestPerformanceBudget(t *testing.T) {
    result := testing.Benchmark(BenchmarkCritical)
    
    if result.AllocsPerOp() > 0 {
        t.Fatal("Allocation regression")
    }
    
    if result.NsPerOp() > 1000000 {
        t.Fatal("Latency regression")
    }
}
```

---

## 🟢 MEDIUM: Chaos Testing

**Tools:** chaos-mesh, LitmusChaos

**Test scenarios:**
- Random pod kills
- Network delays
- DB failures
- CPU throttling

---

# INSTRUMENTATION

## 🟡 HIGH: Distributed Tracing

```go
import "go.opentelemetry.io/otel"

func (s *Service) Process(ctx context.Context) error {
    ctx, span := otel.Tracer("service").Start(ctx, "Process")
    defer span.End()
    
    dbCtx, dbSpan := otel.Tracer("service").Start(ctx, "DB.Get")
    data, err := s.repo.Get(dbCtx, id)
    dbSpan.End()
    
    return err
}
```

**Tools:** Jaeger, Tempo, Zipkin

---

## 🟢 MEDIUM: Structured Logging

```go
import "go.uber.org/zap"

logger, _ := zap.NewProduction()

logger.Info("player_joined",
    zap.String("player_id", id),
    zap.Int("level", level),
    zap.Duration("load_time", dur),
)
```

**NOT:** `fmt.Println`, `log.Printf`

---

**Next:** [Part 3B - Tools & Summary](./03b-tools-summary.md)  
**Previous:** [Part 2A - Network](./02a-network-optimizations.md)

