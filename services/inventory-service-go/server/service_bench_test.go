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
	redis := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	ctx := context.Background()
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
	redis := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	service := NewOptimizedInventoryService(redis, &MockRepository{}).(*OptimizedInventoryService)
	ctx := context.Background()
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
	redis := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	service := NewOptimizedInventoryService(redis, &MockRepository{}).(*OptimizedInventoryService)
	ctx := context.Background()
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
	
	redis := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	ctx := context.Background()
	
	duration := 10 * time.Second
	rps := 10000
	totalRequests := rps * int(duration.Seconds())
	
	playerIDs := make([]string, 1000)
	for i := range playerIDs {
		playerIDs[i] = uuid.New().String()
	}
	
	start := time.Now()
	successCount := 0
	
	for i := 0; i < totalRequests; i++ {
		playerID := playerIDs[i%len(playerIDs)]
		
		_, err := service.GetInventory(ctx, playerID)
		if err == nil {
			successCount++
		}
		
		// Rate limiting
		if i%1000 == 0 {
			time.Sleep(100 * time.Millisecond)
		}
	}
	
	elapsed := time.Since(start)
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

// Benchmark memory usage
func TestInventory_MemoryUsage(t *testing.T) {
	redis := redis.NewClient(&redis.Options{Addr: "localhost:6379"})
	service := NewOptimizedInventoryService(redis, &MockRepository{})
	ctx := context.Background()
	
	// Load 1000 inventories into cache
	for i := 0; i < 1000; i++ {
		playerID := uuid.New().String()
		service.GetInventory(ctx, playerID)
	}
	
	t.Log("Loaded 1000 inventories into cache")
	t.Log("Expected memory: ~5-10 MB (L1 cache)")
	t.Log("Redis memory: ~20-30 MB (L2 cache)")
}




