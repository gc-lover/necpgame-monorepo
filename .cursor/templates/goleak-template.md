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

```go
// Issue: #1585 - Goroutine leak detection
package server

import (
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestServiceNoLeaks verifies service lifecycle doesn't leak
func TestServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// Test service Start/Stop
	svc := NewService()
	svc.Start()
	time.Sleep(100 * time.Millisecond)
	svc.Stop()
	
	// If goroutines leaked, test FAILS
}

// TestHandlerNoLeaks verifies HTTP handlers don't leak
func TestHandlerNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	handler := NewHandler()
	req := httptest.NewRequest("GET", "/api/test", nil)
	w := httptest.NewRecorder()
	
	handler.ServeHTTP(w, req)
	
	// If goroutines leaked, test FAILS
}
```

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

