// Issue: #1585 - Goroutine leak detection (CRITICAL - Admin Service!)
// admin-service is HIGH RISK for leaks (admin operations, concurrent management)
package server

import (
	"context"
	"sync"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
	"github.com/stretchr/testify/mock"
	"go.uber.org/goleak"
)

// TestMain verifies no goroutine leaks across ALL tests
// CRITICAL for admin service - each operation might spawn goroutines
func TestMain(m *testing.M) {
	goleak.VerifyTestMain(m,
		// Ignore known persistent goroutines
		goleak.IgnoreTopFunction("internal/poll.runtime_pollWait"),
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.(*ConnPool).reaper"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)
}

// TestAdminServiceNoLeaks verifies admin service operations don't leak goroutines
func TestAdminServiceNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.(*ConnPool).reaper"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)

	service, mockRepo, mockEventBus, _ := setupTestService()

	ctx := context.Background()
	adminID := uuid.New()
	characterID := uuid.New()

	// Test BanPlayer operation
	mockRepo.On("CreateAuditLog", mock.Anything, mock.Anything).Return(nil)
	mockEventBus.On("PublishEvent", mock.Anything, mock.Anything, mock.Anything).Return(nil)

	banReq := &models.BanPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test ban",
		Permanent:   false,
	}

	_, err := service.BanPlayer(ctx, adminID, banReq, "127.0.0.1", "test-agent")
	if err != nil {
		t.Logf("BanPlayer error (expected with mocks): %v", err)
	}

	// Test KickPlayer operation
	kickReq := &models.KickPlayerRequest{
		CharacterID: characterID,
		Reason:      "Test kick",
	}

	_, err = service.KickPlayer(ctx, adminID, kickReq, "127.0.0.1", "test-agent")
	if err != nil {
		t.Logf("KickPlayer error (expected with mocks): %v", err)
	}

	// Test MutePlayer operation
	muteReq := &models.MutePlayerRequest{
		CharacterID: characterID,
		Reason:      "Test mute",
		Duration:    3600,
	}

	_, err = service.MutePlayer(ctx, adminID, muteReq, "127.0.0.1", "test-agent")
	if err != nil {
		t.Logf("MutePlayer error (expected with mocks): %v", err)
	}

	// Wait for any async operations to complete
	time.Sleep(200 * time.Millisecond)

	// If goroutines leaked from admin operations, test FAILS
}

// TestGoroutineMonitorNoLeaks verifies GoroutineMonitor doesn't leak
func TestGoroutineMonitorNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.(*ConnPool).reaper"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)

	monitor := NewGoroutineMonitor(100)

	// Start monitor in goroutine
	done := make(chan struct{})
	go func() {
		monitor.Start()
		close(done)
	}()

	// Let it run for a bit
	time.Sleep(50 * time.Millisecond)

	// Stop monitor
	monitor.Stop()

	// Wait for goroutine to exit
	select {
	case <-done:
		// OK
	case <-time.After(1 * time.Second):
		t.Error("GoroutineMonitor goroutine did not exit")
	}

	// If goroutines leaked, test FAILS
}

// TestConcurrentAdminOperationsNoLeaks verifies concurrent operations don't leak
func TestConcurrentAdminOperationsNoLeaks(t *testing.T) {
	defer goleak.VerifyNone(t,
		goleak.IgnoreTopFunction("database/sql.(*DB).connectionOpener"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.(*ConnPool).reaper"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/internal/pool.startGlobalTimeCache.func1"),
		goleak.IgnoreTopFunction("github.com/redis/go-redis/v9/maintnotifications.(*CircuitBreakerManager).cleanupLoop"),
	)

	service, mockRepo, mockEventBus, _ := setupTestService()

	ctx := context.Background()
	adminID := uuid.New()
	characterID := uuid.New()

	mockRepo.On("CreateAuditLog", mock.Anything, mock.Anything).Return(nil)
	mockEventBus.On("PublishEvent", mock.Anything, mock.Anything, mock.Anything).Return(nil)

	// Simulate concurrent admin operations (100 goroutines)
	var wg sync.WaitGroup
	done := make(chan struct{})

	for i := 0; i < 100; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			banReq := &models.BanPlayerRequest{
				CharacterID: characterID,
				Reason:      "Concurrent test",
				Permanent:   false,
			}
			service.BanPlayer(ctx, adminID, banReq, "127.0.0.1", "test-agent")
		}()
	}

	// Wait for all goroutines
	go func() {
		wg.Wait()
		close(done)
	}()

	select {
	case <-done:
		// All goroutines completed
	case <-time.After(5 * time.Second):
		t.Fatal("Timeout waiting for goroutines")
	}

	// Wait for cleanup
	time.Sleep(200 * time.Millisecond)

	// No leaked goroutines
}
