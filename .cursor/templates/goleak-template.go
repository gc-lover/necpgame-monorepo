// Issue: #1585 - Goroutine leak detection template
// Copy this to server/leak_test.go in EVERY service
package server

import (
	"net/http"
	"sync"
	"testing"
	"time"

	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// This catches leaks from ANY test in the package
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines from stdlib
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		// Ignore database/sql background goroutines (expected to persist)
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
}

// TestServiceNoLeaks verifies service operations don't leak goroutines
func TestServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// TODO: Create your service
	// service := NewYourService()
	
	// TODO: Test typical operations
	// service.DoSomething()
	
	time.Sleep(100 * time.Millisecond)
	
	// If goroutines leaked, test FAILS here
}

// TestConcurrentNoLeaks verifies concurrent operations don't leak
func TestConcurrentNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
	)
	
	// TODO: Create service
	// service := NewYourService()
	
	// Simulate concurrent load (100 goroutines)
	done := make(chan struct{})
	for i := 0; i < 100; i++ {
		go func() {
			// TODO: Call service methods
			// service.Handle()
			done <- struct{}{}
		}()
	}
	
	// Wait for all
	for i := 0; i < 100; i++ {
		<-done
	}
	
	time.Sleep(100 * time.Millisecond)
	
	// No leaked goroutines
}

// Mock ResponseWriter (thread-safe!)
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

// Installation:
// 1. go get go.uber.org/goleak@latest
// 2. Copy this file to server/leak_test.go
// 3. Replace TODOs with actual service code
// 4. Run: go test -v ./server -run "^Test.*NoLeaks$"
//
// Expected result: PASS (0 goroutine leaks)

