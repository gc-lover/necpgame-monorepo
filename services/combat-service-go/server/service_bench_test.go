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

// OPTIMIZATION: Issue #1936 - Benchmark tests for MMO performance validation
func BenchmarkCombatService_InitiateCombat(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Prepare request data
	reqData := InitiateCombatRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		CombatType: "PVP",
		Location:   "arena_01",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		service.InitiateCombat(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkCombatService_GetCombatStatus(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Pre-create a combat session
	reqData := InitiateCombatRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		CombatType: "PVP",
		Location:   "arena_01",
	}
	reqBody, _ := json.Marshal(reqData)

	req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
	w := httptest.NewRecorder()
	service.InitiateCombat(w, req)

	var resp InitiateCombatResponse
	json.Unmarshal(w.Body.Bytes(), &resp)
	combatID := resp.CombatID

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/combat/"+combatID+"/status", nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("combatId", combatID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.GetCombatStatus(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkCombatService_ExecuteCombatAction(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Pre-create a combat session
	reqData := InitiateCombatRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		CombatType: "PVP",
		Location:   "arena_01",
	}
	reqBody, _ := json.Marshal(reqData)

	req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
	w := httptest.NewRecorder()
	service.InitiateCombat(w, req)

	var resp InitiateCombatResponse
	json.Unmarshal(w.Body.Bytes(), &resp)
	combatID := resp.CombatID

	// Prepare action request
	actionReq := CombatActionRequest{
		ActionType: "ATTACK",
		ActionID:   "basic_attack",
		TargetID:   "enemy_456",
	}
	actionBody, _ := json.Marshal(actionReq)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/combat/"+combatID+"/action", bytes.NewReader(actionBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("combatId", combatID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.ExecuteCombatAction(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkCombatService_CalculateDamage(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Prepare damage calculation request
	damageReq := DamageCalculationRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		AttackType: "PHYSICAL",
		BaseDamage: 50,
		Modifiers: []*DamageModifier{
			{Type: "MULTIPLY", Value: 1.2, Source: "strength_bonus"},
			{Type: "ARMOR", Value: 10.0, Source: "defender_armor"},
		},
	}

	reqBody, _ := json.Marshal(damageReq)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/damage/calculate", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		service.CalculateDamage(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkCombatService_GetStatusEffects(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	characterID := "player_123"

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/status-effects/"+characterID, nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("characterId", characterID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.GetStatusEffects(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent load
func BenchmarkCombatService_ConcurrentLoad(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Prepare request data
	reqData := InitiateCombatRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		CombatType: "PVP",
		Location:   "arena_01",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()

			service.InitiateCombat(w, req)

			if w.Code != http.StatusOK {
				b.Fatalf("Expected status 200, got %d", w.Code)
			}
		}
	})
}

// Performance target validation
func TestCombatService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CombatMetrics{}
	service := NewCombatService(logger, metrics)

	// Test initiate combat performance
	reqData := InitiateCombatRequest{
		AttackerID: "player_123",
		DefenderID: "enemy_456",
		CombatType: "PVP",
		Location:   "arena_01",
	}

	reqBody, _ := json.Marshal(reqData)

	// Warm up
	for i := 0; i < 100; i++ {
		req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()
		service.InitiateCombat(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			req := httptest.NewRequest("POST", "/combat/initiate", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()
			service.InitiateCombat(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 1000 ops/sec for basic operations
	targetOpsPerSec := 1000.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("Performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 10 {
		t.Errorf("Too many allocations: %.2f allocs/op > 10 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}
