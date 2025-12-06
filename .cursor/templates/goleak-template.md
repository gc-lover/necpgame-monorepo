# Goroutine Leak Detection Template

**Issue:** #1585

## Добавление в go.mod

```go
require (
    go.uber.org/goleak v1.3.0
)
```

## Создание leak_test.go

**Файл:** `server/leak_test.go`

**Скопируй код из:** `.cursor/templates/goleak-template.go`

**Или используй из:** `.cursor/templates/backend-utils-templates.md` (раздел benchmarks_test.go)

## Runtime Goroutine Monitoring

**Добавление в main.go:**

```go
import (
	"runtime"
	"time"
)

// GoroutineMonitor monitors goroutine count
type GoroutineMonitor struct {
	maxGoroutines int
	logger        *logrus.Logger
}

func NewGoroutineMonitor(max int, logger *logrus.Logger) *GoroutineMonitor {
	return &GoroutineMonitor{
		maxGoroutines: max,
		logger:        logger,
	}
}

func (gm *GoroutineMonitor) Start() {
	ticker := time.NewTicker(10 * time.Second)
	defer ticker.Stop()
	
	for range ticker.C {
		count := runtime.NumGoroutine()
		
		if count > gm.maxGoroutines {
			gm.logger.WithFields(logrus.Fields{
				"count": count,
				"max":   gm.maxGoroutines,
			}).Error("Goroutine leak detected")
			
			// Dump goroutine stack traces
			buf := make([]byte, 1<<20) // 1MB
			n := runtime.Stack(buf, true)
			gm.logger.WithField("stack", string(buf[:n])).Error("Goroutine dump")
		}
		
		// Prometheus metric (if available)
		// goroutineCount.Set(float64(count))
	}
}

// В main():
monitor := NewGoroutineMonitor(1000, logger)
go monitor.Start()
```

## Prometheus Alert

**Добавление в prometheus alerts:**

```yaml
- alert: GoroutineLeak
  expr: go_goroutines > 10000
  for: 5m
  labels:
    severity: critical
  annotations:
    summary: "Goroutine leak detected in {{ $labels.job }}"
    description: "{{ $labels.job }} has {{ $value }} goroutines"
```

