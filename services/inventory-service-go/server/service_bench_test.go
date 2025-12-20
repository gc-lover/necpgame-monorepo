package server

import (
	"bytes"
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

// OPTIMIZATION: Issue #1950 - Benchmark tests for MMO performance validation
func BenchmarkInventoryService_GetInventory(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	characterID := "player_123"

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/inventory/"+characterID, nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("characterId", characterID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.GetInventory(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkInventoryService_ListInventoryItems(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	characterID := "player_123"

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/inventory/"+characterID+"/items", nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("characterId", characterID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.ListInventoryItems(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkInventoryService_MoveItem(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	characterID := "player_123"

	reqData := MoveItemRequest{
		InventoryItemID: "item_123",
		FromContainer:   "main",
		ToContainer:     "main",
		ToSlotX:         1,
		ToSlotY:         2,
		Quantity:        1,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/inventory/"+characterID+"/move", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("characterId", characterID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.MoveItem(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkInventoryService_EquipItem(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	characterID := "player_123"

	reqData := EquipItemRequest{
		InventoryItemID: "item_123",
		SlotType:        "MAIN_HAND",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/inventory/"+characterID+"/equip", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("characterId", characterID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.EquipItem(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkInventoryService_GetItem(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	itemID := "sword_001"

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/items/"+itemID, nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("itemId", itemID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.GetItem(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkInventoryService_SearchItems(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	reqData := SearchItemsRequest{
		Query: "sword",
		Limit: 50,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/items/search", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		service.SearchItems(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent load
func BenchmarkInventoryService_ConcurrentLoad(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	reqData := MoveItemRequest{
		InventoryItemID: "item_123",
		FromContainer:   "main",
		ToContainer:     "main",
		ToSlotX:         1,
		ToSlotY:         2,
		Quantity:        1,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			req := httptest.NewRequest("POST", "/inventory/player_123/move", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()

			rctx := chi.NewRouteContext()
			rctx.URLParams.Add("characterId", "player_123")
			req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

			service.MoveItem(w, req)

			if w.Code != http.StatusOK {
				b.Fatalf("Expected status 200, got %d", w.Code)
			}
		}
	})
}

// Performance target validation
func TestInventoryService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &InventoryMetrics{}
	service := NewInventoryService(logger, metrics)

	characterID := "player_123"

	// Test get inventory performance
	req := httptest.NewRequest("GET", "/inventory/"+characterID, nil)
	rctx := chi.NewRouteContext()
	rctx.URLParams.Add("characterId", characterID)
	req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

	// Warm up
	for i := 0; i < 100; i++ {
		w := httptest.NewRecorder()
		service.GetInventory(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			w := httptest.NewRecorder()
			service.GetInventory(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 2000 ops/sec for inventory operations
	targetOpsPerSec := 2000.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 5 {
		t.Errorf("Too many allocations: %.2f allocs/op > 5 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}