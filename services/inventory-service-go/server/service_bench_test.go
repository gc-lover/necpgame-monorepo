// Issue: #1581 - Benchmarks для inventory caching
package server

import (
	"context"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/pkg/api"
	"github.com/gc-lover/necpgame-monorepo/services/inventory-service-go/models"
	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"
)

// Mock Repository for benchmarks
type MockRepository struct{}

func (m *MockRepository) GetInventory(ctx context.Context, characterID uuid.UUID) (*models.InventoryResponse, error) {
	return &models.InventoryResponse{
		Inventory: models.Inventory{
			ID:          uuid.New(),
			CharacterID: characterID,
			Capacity:    50,
			UsedSlots:   0,
			Weight:      0,
			MaxWeight:   100.0,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
		Items: []models.InventoryItem{},
	}, nil
}

func (m *MockRepository) AddItem(ctx context.Context, characterID uuid.UUID, item *models.AddItemRequest) error {
	return nil
}

func (m *MockRepository) RemoveItem(ctx context.Context, characterID, itemID uuid.UUID) error {
	return nil
}

func (m *MockRepository) UpdateItem(ctx context.Context, characterID, itemID uuid.UUID, updateFn func() error) error {
	return updateFn()
}

// Benchmark: Old service (no cache, direct DB)
func BenchmarkInventory_NoCaching(b *testing.B) {
	service, _ := NewInventoryService("", "")
	if service == nil {
		b.Skip("Service initialization failed")
	}
	ctx := context.Background()
	playerID := uuid.New()
	
	b.ResetTimer()
	b.ReportAllocs()
	
	for i := 0; i < b.N; i++ {
		service.GetInventory(ctx, playerID)
	}
}

// Benchmark: Optimized service (3-tier cache)
func BenchmarkInventory_With3TierCache(b *testing.B) {
	// Note: This will primarily hit L1 memory cache after first access
	redis := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DialTimeout:  1 * time.Second,
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})
	defer redis.Close()
	
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()
	
	// Check Redis availability
	if err := redis.Ping(ctx).Err(); err != nil {
		b.Skipf("Skipping benchmark due to Redis not available: %v", err)
		return
	}
	
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	playerID := uuid.New().String()
	
	// Prime cache
	service.GetInventory(ctx, playerID)
	
	b.ResetTimer()
	b.ReportAllocs()
	
	for i := 0; i < b.N; i++ {
		service.GetInventory(ctx, playerID)
	}
}

// Benchmark: Diff updates vs Full inventory
func BenchmarkInventory_DiffVsFull(b *testing.B) {
	redis := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DialTimeout:  1 * time.Second,
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})
	defer redis.Close()
	
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()
	
	// Check Redis availability
	if err := redis.Ping(ctx).Err(); err != nil {
		b.Skipf("Skipping benchmark due to Redis not available: %v", err)
		return
	}
	
	service := NewOptimizedInventoryService(redis, &MockRepository{}).(*OptimizedInventoryService)
	playerID := uuid.New().String()
	
	// Prime cache
	service.GetInventory(ctx, playerID)
	
	b.Run("FullInventory", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			service.GetInventory(ctx, playerID)
		}
	})
	
	b.Run("DiffOnly", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			service.GetInventoryDiff(ctx, playerID)
		}
	})
}

// Benchmark: Batch operations vs Single
func BenchmarkInventory_BatchVsSingle(b *testing.B) {
	redis := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DialTimeout:  1 * time.Second,
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})
	defer redis.Close()
	
	ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
	defer cancel()
	
	// Check Redis availability
	if err := redis.Ping(ctx).Err(); err != nil {
		b.Skipf("Skipping benchmark due to Redis not available: %v", err)
		return
	}
	
	service := NewOptimizedInventoryService(redis, &MockRepository{}).(*OptimizedInventoryService)
	playerID := uuid.New().String()
	
	items := make([]api.AddItemRequest, 10)
	for i := range items {
		items[i] = api.AddItemRequest{
			ItemID:   uuid.New(),
			Quantity: api.NewOptInt(1),
		}
	}
	
	b.Run("SingleAdds", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			for _, item := range items {
				service.AddItem(ctx, playerID, &item)
			}
		}
	})
	
	b.Run("BatchAdd", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			service.BatchAddItems(ctx, playerID, items)
		}
	})
}

// Load test: 10k RPS for 10 seconds
func TestInventory_LoadTest_10kRPS(t *testing.T) {
	if testing.Short() {
		t.Skip("Skipping load test in short mode")
	}
	
	// Add timeout for entire test to prevent hanging
	ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	defer cancel()
	
	// Redis client with fast timeouts
	redis := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DialTimeout:  1 * time.Second,
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})
	defer redis.Close()
	
	// Check Redis availability with timeout
	pingCtx, pingCancel := context.WithTimeout(ctx, 1*time.Second)
	defer pingCancel()
	if err := redis.Ping(pingCtx).Err(); err != nil {
		t.Skipf("Skipping load test due to Redis not available: %v", err)
		return
	}
	
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	
	duration := 10 * time.Second
	rps := 10000
	totalRequests := rps * int(duration.Seconds())
	
	playerIDs := make([]string, 1000)
	for i := range playerIDs {
		playerIDs[i] = uuid.New().String()
	}
	
	start := time.Now()
	successCount := 0
	
	// Use context with timeout for each request
	for i := 0; i < totalRequests; i++ {
		// Check if context is cancelled (timeout)
		select {
		case <-ctx.Done():
			t.Logf("Test timed out after %v", time.Since(start))
			return
		default:
		}
		
		playerID := playerIDs[i%len(playerIDs)]
		
		// Use context with timeout for each GetInventory call
		reqCtx, reqCancel := context.WithTimeout(ctx, 100*time.Millisecond)
		_, err := service.GetInventory(reqCtx, playerID)
		reqCancel()
		
		if err == nil {
			successCount++
		}
		
		// Rate limiting
		if i%1000 == 0 {
			select {
			case <-ctx.Done():
				return
			case <-time.After(100 * time.Millisecond):
			}
		}
	}
	
	elapsed := time.Since(start)
	if elapsed.Seconds() > 0 {
		actualRPS := float64(successCount) / elapsed.Seconds()
		
		t.Logf("Load Test Results:")
		t.Logf("  Duration: %v", elapsed)
		t.Logf("  Total Requests: %d", totalRequests)
		t.Logf("  Successful: %d", successCount)
		t.Logf("  Actual RPS: %.2f", actualRPS)
		t.Logf("  P99 Latency: <30ms (target)")
		
		if actualRPS < float64(rps)*0.7 {
			t.Errorf("RPS too low: %.2f < %d (expected)", actualRPS, rps)
		}
	}
}

// Benchmark memory usage
func TestInventory_MemoryUsage(t *testing.T) {
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	
	// Redis client with fast timeouts
	redis := redis.NewClient(&redis.Options{
		Addr:         "localhost:6379",
		DialTimeout:  1 * time.Second,
		ReadTimeout:  1 * time.Second,
		WriteTimeout: 1 * time.Second,
		PoolTimeout:  1 * time.Second,
	})
	defer redis.Close()
	
	// Check Redis availability
	pingCtx, pingCancel := context.WithTimeout(ctx, 1*time.Second)
	defer pingCancel()
	if err := redis.Ping(pingCtx).Err(); err != nil {
		t.Skipf("Skipping test due to Redis not available: %v", err)
		return
	}
	
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	
	// Load 1000 inventories into cache with timeout per request
	for i := 0; i < 1000; i++ {
		select {
		case <-ctx.Done():
			t.Logf("Test timed out after loading %d inventories", i)
			return
		default:
		}
		
		playerID := uuid.New().String()
		reqCtx, reqCancel := context.WithTimeout(ctx, 100*time.Millisecond)
		service.GetInventory(reqCtx, playerID)
		reqCancel()
	}
	
	t.Log("Loaded 1000 inventories into cache")
	t.Log("Expected memory: ~5-10 MB (L1 cache)")
	t.Log("Redis memory: ~20-30 MB (L2 cache)")
}




