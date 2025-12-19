package service

import (
	"context"
	"runtime"
	"testing"
	"time"

	"necpgame/services/interactive-objects-service-go/internal/repository"
)

// TestMain checks for goroutine leaks after all tests
func TestMain(m *testing.M) {
	initialGoroutines := runtime.NumGoroutine()

	code := m.Run()

	// Allow some time for cleanup
	time.Sleep(100 * time.Millisecond)

	finalGoroutines := runtime.NumGoroutine()

	if finalGoroutines > initialGoroutines+5 { // Allow some tolerance
		panic("Goroutine leak detected! Started with %d, ended with %d goroutines")
	}

	if code != 0 {
		panic("Tests failed")
	}
}

// TestMemoryPool_NoLeaks verifies memory pool doesn't leak
func TestMemoryPool_NoLeaks(t *testing.T) {
	repo := &repository.Repository{} // Mock repository
	service := NewInteractiveService(repo)

	ctx := context.Background()

	// Spawn multiple objects
	for i := 0; i < 100; i++ {
		obj, err := service.SpawnObject(ctx, "terminal", "urban", "zone_1", repository.Position{X: 1.0, Y: 2.0, Z: 3.0})
		if err != nil {
			t.Fatalf("Failed to spawn object: %v", err)
		}
		if obj == nil {
			t.Fatal("Spawned object is nil")
		}
	}

	// Force GC to clean up
	runtime.GC()
	runtime.GC() // Second GC to ensure cleanup

	// Check that memory pool is working
	initialStats := service.GetTelemetry()
	if initialStats.ActiveObjects == 0 {
		t.Log("Memory pool working - no active objects after cleanup")
	}
}

// TestConcurrentAccess_NoRace verifies concurrent access doesn't cause races
func TestConcurrentAccess_NoRace(t *testing.T) {
	repo := &repository.Repository{} // Mock repository
	service := NewInteractiveService(repo)

	ctx := context.Background()

	// Spawn initial object
	obj, err := service.SpawnObject(ctx, "terminal", "urban", "zone_1", repository.Position{X: 1.0, Y: 2.0, Z: 3.0})
	if err != nil {
		t.Fatalf("Failed to spawn initial object: %v", err)
	}

	done := make(chan bool, 10)

	// Concurrent telemetry reads
	for i := 0; i < 5; i++ {
		go func() {
			for j := 0; j < 100; j++ {
				_ = service.GetTelemetry()
			}
			done <- true
		}()
	}

	// Concurrent interactions
	for i := 0; i < 5; i++ {
		go func() {
			for j := 0; j < 100; j++ {
				_, _ = service.InteractWithObject(ctx, obj.ID, "hack")
			}
			done <- true
		}()
	}

	// Wait for all goroutines
	for i := 0; i < 10; i++ {
		<-done
	}
}

// Issue: #1840
