# 🛠️ Backend Utilities Templates

**Шаблоны для utilities, cache, tests, metrics**

## 🔄 worker_pool.go

```go
// Issue: #123
package server

import "sync"

type WorkerPool struct {
    semaphore chan struct{}
    wg        sync.WaitGroup
}

func NewWorkerPool(size int) *WorkerPool {
    return &WorkerPool{
        semaphore: make(chan struct{}, size),
    }
}

func (p *WorkerPool) Submit(task func()) {
    p.semaphore <- struct{}{} // Acquire
    p.wg.Add(1)
    
    go func() {
        defer func() {
            <-p.semaphore // Release
            p.wg.Done()
        }()
        task()
    }()
}

func (p *WorkerPool) Wait() {
    p.wg.Wait()
}

func (p *WorkerPool) Close() {
    p.wg.Wait()
    close(p.semaphore)
}
```

## 💾 cache.go

```go
// Issue: #123
package server

import (
    "sync"
    "time"
)

type CacheEntry struct {
    Value     interface{}
    CreatedAt time.Time
}

type Cache struct {
    data sync.Map // ОПТИМИЗАЦИЯ: Lock-free reads
    ttl  time.Duration
    stop chan struct{}
}

func NewCache(ttl time.Duration) *Cache {
    cache := &Cache{
        ttl:  ttl,
        stop: make(chan struct{}),
    }
    
    // Cleanup goroutine
    go cache.cleanup()
    
    return cache
}

func (c *Cache) Get(key string) (interface{}, bool) {
    val, ok := c.data.Load(key)
    if !ok {
        return nil, false
    }
    
    entry := val.(*CacheEntry)
    if time.Since(entry.CreatedAt) > c.ttl {
        c.data.Delete(key)
        return nil, false
    }
    
    return entry.Value, true
}

func (c *Cache) Set(key string, value interface{}) {
    c.data.Store(key, &CacheEntry{
        Value:     value,
        CreatedAt: time.Now(),
    })
}

func (c *Cache) cleanup() {
    ticker := time.NewTicker(c.ttl / 2)
    defer ticker.Stop()
    
    for {
        select {
        case <-c.stop:
            return
        case <-ticker.C:
            now := time.Now()
            c.data.Range(func(key, value interface{}) bool {
                entry := value.(*CacheEntry)
                if now.Sub(entry.CreatedAt) > c.ttl {
                    c.data.Delete(key)
                }
                return true
            })
        }
    }
}

func (c *Cache) Close() {
    close(c.stop)
}
```

## 🧪 benchmarks_test.go

```go
// Issue: #123
package server

import (
    "context"
    "testing"
    "time"
    
    "go.uber.org/goleak"
)

// ОПТИМИЗАЦИЯ: Goroutine leak detection
func TestMain(m *testing.M) {
    goleak.VerifyTestMain(m)
}

// ОПТИМИЗАЦИЯ: Benchmark critical path
func BenchmarkListPlayers(b *testing.B) {
    service := setupTestService()
    ctx := context.Background()
    params := api.ListPlayersParams{
        Limit:  intPtr(100),
        Offset: intPtr(0),
    }
    
    b.ResetTimer()
    b.ReportAllocs() // ВАЖНО: показывай allocations
    
    for i := 0; i < b.N; i++ {
        _, err := service.ListPlayers(ctx, params)
        if err != nil {
            b.Fatal(err)
        }
    }
}

// ОПТИМИЗАЦИЯ: Performance budget test
func TestPerformanceBudget(t *testing.T) {
    result := testing.Benchmark(BenchmarkListPlayers)
    
    // Budget: max 1ms per op
    if result.NsPerOp() > 1000000 {
        t.Errorf("Performance regression: %d ns/op (budget: 1ms)", result.NsPerOp())
    }
    
    // Budget: max 5 allocs/op
    if result.AllocsPerOp() > 5 {
        t.Errorf("Allocation regression: %d allocs/op (budget: 5)", result.AllocsPerOp())
    }
}

// ОПТИМИЗАЦИЯ: No goroutine leaks
func TestNoGoroutineLeaks(t *testing.T) {
    defer goleak.VerifyNone(t)
    
    server := NewGameServer()
    server.Start()
    time.Sleep(1 * time.Second)
    server.Stop()
    
    // Goleak автоматически проверит leaks при defer
}

// ОПТИМИЗАЦИЯ: Load test
func TestLoadHandling(t *testing.T) {
    server := NewGameServer()
    server.Start()
    defer server.Stop()
    
    const concurrency = 1000
    const requestsPerWorker = 100
    
    var wg sync.WaitGroup
    wg.Add(concurrency)
    
    start := time.Now()
    
    for i := 0; i < concurrency; i++ {
        go func() {
            defer wg.Done()
            for j := 0; j < requestsPerWorker; j++ {
                server.ProcessRequest(Request{})
            }
        }()
    }
    
    wg.Wait()
    elapsed := time.Since(start)
    
    totalRequests := concurrency * requestsPerWorker
    rps := float64(totalRequests) / elapsed.Seconds()
    
    t.Logf("Throughput: %.0f requests/sec", rps)
    
    if rps < 10000 {
        t.Errorf("Low throughput: %.0f req/sec (target: >10k)", rps)
    }
}
```

## 📊 metrics.go

```go
// Issue: #123
package server

import (
    "runtime"
    "time"
    
    "github.com/prometheus/client_golang/prometheus"
    "github.com/prometheus/client_golang/prometheus/promauto"
)

var (
    requestDuration = promauto.NewHistogramVec(
        prometheus.HistogramOpts{
            Name:    "http_request_duration_seconds",
            Help:    "HTTP request duration",
            Buckets: []float64{.001, .005, .01, .025, .05, .1, .5, 1},
        },
        []string{"handler", "method"},
    )
    
    gcPauseDuration = promauto.NewHistogram(
        prometheus.HistogramOpts{
            Name:    "go_gc_pause_duration_seconds",
            Help:    "GC pause duration",
            Buckets: prometheus.ExponentialBuckets(0.0001, 2, 10),
        },
    )
    
    goroutineCount = promauto.NewGauge(
        prometheus.GaugeOpts{
            Name: "go_goroutines",
            Help: "Number of goroutines",
        },
    )
    
    memoryUsage = promauto.NewGauge(
        prometheus.GaugeOpts{
            Name: "go_memory_usage_bytes",
            Help: "Memory usage in bytes",
        },
    )
)

// ОПТИМИЗАЦИЯ: Monitor GC, goroutines, memory
func StartMetricsCollector() {
    go func() {
        ticker := time.NewTicker(1 * time.Second)
        defer ticker.Stop()
        
        var stats runtime.MemStats
        for range ticker.C {
            runtime.ReadMemStats(&stats)
            
            // GC metrics
            gcPauseDuration.Observe(float64(stats.PauseNs[(stats.NumGC+255)%256]) / 1e9)
            
            // Goroutine count
            goroutineCount.Set(float64(runtime.NumGoroutine()))
            
            // Memory usage
            memoryUsage.Set(float64(stats.Alloc))
        }
    }()
}

// Middleware для request duration
func MetricsMiddleware(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        start := time.Now()
        
        next.ServeHTTP(w, r)
        
        duration := time.Since(start).Seconds()
        requestDuration.WithLabelValues(r.URL.Path, r.Method).Observe(duration)
    })
}
```

## 🔧 Использование

**Backend Agent должен:**

1. Копировать utilities из шаблонов
2. Интегрировать в свой сервис
3. Использовать в handlers/service/repository
4. Запустить `StartMetricsCollector()` в main.go

## 📚 Примеры интеграции

### В main.go:

```go
func main() {
    // Start metrics collector
    server.StartMetricsCollector()
    
    // Create worker pool
    workerPool := server.NewWorkerPool(1000)
    defer workerPool.Close()
    
    // Create cache
    cache := server.NewCache(5 * time.Minute)
    defer cache.Close()
    
    // ... rest of setup
}
```

### В handlers:

```go
func (h *Handlers) ProcessRequest(w http.ResponseWriter, r *http.Request) {
    // Use worker pool for heavy tasks
    h.workerPool.Submit(func() {
        result := heavyComputation()
        h.cache.Set("result", result)
    })
}
```

## 🆕 Новые техники (2025)

**Testing:**
- PGO (Profile-Guided Optimization): `.cursor/performance/03a-profiling-testing.md`
- Continuous profiling (Pyroscope): `.cursor/performance/03a-profiling-testing.md`

**Resilience:**
- TTL-based cleanup: `.cursor/performance/06-resilience-compression.md`
- Bounded map growth: `.cursor/performance/06-resilience-compression.md`

**Metrics:**
- Game-specific metrics: `.cursor/performance/06-resilience-compression.md`

## См. также

- `.cursor/templates/backend-api-templates.md` - API templates
- `.cursor/templates/backend-game-templates.md` - Game server templates
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - полный чек-лист
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - 150+ техник

