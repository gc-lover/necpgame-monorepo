// Issue: #150 + #1585 - Goroutine leak detection for matchmaking-go
package server

import (
	"context"
	"net/http"
	"sync"
	"testing"
	"time"

	"go.uber.org/goleak"
	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

func TestServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// Create service components
	repo, err := NewRepository("postgres://test")
	if err == nil && repo != nil && repo.db != nil {
		defer repo.Close()
	}
	
	cache := NewCacheManager("localhost:6379")
	if cache != nil {
		defer cache.Close()
	}
	
	_ = NewService(repo, cache)
	
	time.Sleep(100 * time.Millisecond)
}
func TestHandlersNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	repo, _ := NewRepository("postgres://test")
	if repo != nil && repo.db != nil {
		defer repo.Close()
	}
	
	cache := NewCacheManager("localhost:6379")
	if cache != nil {
		defer cache.Close()
	}
	
	service := NewService(repo, cache)
	handlers := NewHandlers(service)
	
	ctx := context.Background()
	
	// Call handlers (will error due to no DB, but tests goroutine cleanup)
	_, _ = handlers.EnterQueue(ctx, &api.EnterQueueRequest{
		ActivityType: api.EnterQueueRequestActivityTypePvp5v5,
	})
	
	time.Sleep(50 * time.Millisecond)
}

func TestConcurrentNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	done := make(chan struct{})
	for i := 0; i < 100; i++ {
		go func() {
			// Simulate concurrent operations
			time.Sleep(10 * time.Millisecond)
			done <- struct{}{}
		}()
	}
	
	for i := 0; i < 100; i++ {
		<-done
	}
	
	time.Sleep(100 * time.Millisecond)
}

type mockResponseWriter struct {
	mu      sync.Mutex
	headers http.Header
	body    []byte
	status  int
}

func (m *mockResponseWriter) Header() http.Header {
	if m.headers == nil {
		m.headers = make(http.Header)
	}
	return m.headers
}

func (m *mockResponseWriter) Write(data []byte) (int, error) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.body = append(m.body, data...)
	return len(data), nil
}

func (m *mockResponseWriter) WriteHeader(statusCode int) {
	m.mu.Lock()
	defer m.mu.Unlock()
	m.status = statusCode
}


