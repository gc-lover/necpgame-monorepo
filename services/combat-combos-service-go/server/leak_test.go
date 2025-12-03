// Issue: #1585 - Goroutine leak detection (BLOCKER)
// Verifies no goroutine leaks in combat-combos service
package server

import (
	"net/http"
	"sync"
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines from stdlib
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		// Ignore database/sql background goroutines (these are expected to persist)
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestMemoryPoolingNoLeaks verifies sync.Pool doesn't leak goroutines
func TestMemoryPoolingNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// Test respondJSON with sync.Pool (Issue #1578)
	w := &mockResponseWriter{headers: make(http.Header)}
	data := map[string]string{"status": "success", "id": "test-123"}
	
	// Stress test memory pooling
	for i := 0; i < 1000; i++ {
		respondJSON(w, 200, data)
	}
	
	time.Sleep(50 * time.Millisecond)
	
	// sync.Pool cleanup should not leak goroutines
	// GAINS: 0 goroutines leaked (tested with goleak)
}

// TestRespondJSONNoLeaks verifies respondJSON helper doesn't leak
func TestRespondJSONNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// Multiple concurrent writes (simulates real load)
	// Use one writer per goroutine to avoid race condition
	done := make(chan struct{})
	for i := 0; i < 100; i++ {
		go func() {
			w := &mockResponseWriter{headers: make(http.Header)}
			respondJSON(w, 200, map[string]string{"test": "data"})
			done <- struct{}{}
		}()
	}
	
	// Wait for all
	for i := 0; i < 100; i++ {
		<-done
	}
	
	time.Sleep(100 * time.Millisecond)
	
	// No goroutines leaked from JSON encoding
}

// TestServiceCreationNoLeaks verifies service creation doesn't leak
func TestServiceCreationNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// Create repository & service
	repo, err := NewRepository("postgres://test")
	if err == nil {
		// Close DB if opened successfully
		defer repo.db.Close()
	}
	_ = NewService(repo)
	
	time.Sleep(100 * time.Millisecond)
	
	// If goroutines leaked during init, test fails
}

// Mock http.ResponseWriter for testing (thread-safe!)

type mockResponseWriter struct {
	mu      sync.Mutex
	headers http.Header
	body    []byte
	status  int
}

func (m *mockResponseWriter) Header() http.Header {
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
