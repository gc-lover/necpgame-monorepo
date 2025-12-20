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

// OPTIMIZATION: Issue #1968 - Benchmark tests for AI performance validation
func BenchmarkAIService_GetNPCBehavior(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	npcID := "npc_123"

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("GET", "/ai/"+npcID+"/behavior", nil)
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("npcId", npcID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.GetNPCBehavior(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkAIService_CalculatePath(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	npcID := "npc_123"

	reqData := CalculatePathRequest{
		StartPosition: Vector3{X: 0, Y: 0, Z: 0},
		EndPosition:   Vector3{X: 10, Y: 10, Z: 0},
		PathType:      "DIRECT",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/ai/"+npcID+"/pathfind", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("npcId", npcID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.CalculatePath(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkAIService_MakeDecision(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	npcID := "npc_123"

	reqData := MakeDecisionRequest{
		DecisionContext: &DecisionContext{
			NPCID: npcID,
			CurrentState: &AIState{
				NPCID:          npcID,
				CurrentBehavior: "idle",
			},
			TimePressure:  50,
			RiskTolerance: 50,
		},
		AvailableActions: []*DecisionAction{
			{
				ActionID:        "attack_001",
				ActionType:      "ATTACK",
				PriorityScore:   80,
				RiskLevel:       40,
				SuccessProbability: 0.7,
			},
			{
				ActionID:        "flee_001",
				ActionType:      "FLEE",
				PriorityScore:   60,
				RiskLevel:       20,
				SuccessProbability: 0.9,
			},
		},
		TimeLimitMs: 100,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/ai/"+npcID+"/decision", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("npcId", npcID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.MakeDecision(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

func BenchmarkAIService_ExecuteBehaviorTree(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	treeID := "combat_tree"

	reqData := ExecuteBehaviorTreeRequest{
		NPCID:           "npc_123",
		ContextVariables: map[string]interface{}{
			"health": 75,
			"threat_detected": true,
		},
		MaxDepth:  5,
		TimeoutMs: 1000,
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	for i := 0; i < b.N; i++ {
		req := httptest.NewRequest("POST", "/ai/behavior-trees/"+treeID+"/execute", bytes.NewReader(reqBody))
		w := httptest.NewRecorder()

		rctx := chi.NewRouteContext()
		rctx.URLParams.Add("treeId", treeID)
		req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

		service.ExecuteBehaviorTree(w, req)

		if w.Code != http.StatusOK {
			b.Fatalf("Expected status 200, got %d", w.Code)
		}
	}
}

// Memory allocation benchmark for concurrent AI load
func BenchmarkAIService_ConcurrentAILoad(b *testing.B) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	reqData := CalculatePathRequest{
		StartPosition: Vector3{X: 0, Y: 0, Z: 0},
		EndPosition:   Vector3{X: 10, Y: 10, Z: 0},
		PathType:      "DIRECT",
	}

	reqBody, _ := json.Marshal(reqData)

	b.ResetTimer()
	b.ReportAllocs()

	b.RunParallel(func(pb *testing.PB) {
		npcCounter := 0
		for pb.Next() {
			npcID := fmt.Sprintf("npc_%d", npcCounter%100) // Cycle through 100 NPCs
			npcCounter++

			req := httptest.NewRequest("POST", "/ai/"+npcID+"/pathfind", bytes.NewReader(reqBody))
			w := httptest.NewRecorder()

			rctx := chi.NewRouteContext()
			rctx.URLParams.Add("npcId", npcID)
			req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

			service.CalculatePath(w, req)

			if w.Code != http.StatusOK {
				b.Fatalf("Expected status 200, got %d", w.Code)
			}
		}
	})
}

// Performance target validation for AI
func TestAIService_PerformanceTargets(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	npcID := "npc_123"

	// Test decision making performance
	reqData := MakeDecisionRequest{
		DecisionContext: &DecisionContext{
			NPCID: npcID,
			CurrentState: &AIState{
				NPCID:          npcID,
				CurrentBehavior: "idle",
			},
			TimePressure:  50,
			RiskTolerance: 50,
		},
		AvailableActions: []*DecisionAction{
			{
				ActionID:        "attack_001",
				ActionType:      "ATTACK",
				PriorityScore:   80,
				RiskLevel:       40,
				SuccessProbability: 0.7,
			},
		},
		TimeLimitMs: 100,
	}

	reqBody, _ := json.Marshal(reqData)
	req := httptest.NewRequest("POST", "/ai/"+npcID+"/decision", bytes.NewReader(reqBody))
	rctx := chi.NewRouteContext()
	rctx.URLParams.Add("npcId", npcID)
	req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

	// Warm up
	for i := 0; i < 100; i++ {
		w := httptest.NewRecorder()
		service.MakeDecision(w, req)
	}

	// Benchmark for 1 second
	result := testing.Benchmark(func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			w := httptest.NewRecorder()
			service.MakeDecision(w, req)
		}
	})

	// Calculate operations per second
	opsPerSec := float64(result.N) / result.T.Seconds()

	// Target: at least 1000 ops/sec for AI decision making
	targetOpsPerSec := 1000.0

	if opsPerSec < targetOpsPerSec {
		t.Errorf("AI performance target not met: %.2f ops/sec < %.2f ops/sec target", opsPerSec, targetOpsPerSec)
	}

	// Check memory allocations (should be low with pooling)
	if result.AllocsPerOp() > 10 {
		t.Errorf("Too many allocations: %.2f allocs/op > 10 allocs/op target", result.AllocsPerOp())
	}

	t.Logf("AI Performance: %.2f ops/sec, %.2f allocs/op", opsPerSec, result.AllocsPerOp())
}

// Test concurrent AI processing
func TestAIService_ConcurrentProcessing(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &AIMetrics{}
	service := NewAIService(logger, metrics, 1000)

	// Test with multiple NPCs making decisions simultaneously
	done := make(chan bool, 10)

	for i := 0; i < 10; i++ {
		go func(npcIndex int) {
			npcID := fmt.Sprintf("npc_%d", npcIndex)

			reqData := MakeDecisionRequest{
				DecisionContext: &DecisionContext{
					NPCID: npcID,
					CurrentState: &AIState{
						NPCID:          npcID,
						CurrentBehavior: "idle",
					},
				},
				AvailableActions: []*DecisionAction{
					{
						ActionID:        "idle_001",
						ActionType:      "IDLE",
						PriorityScore:   50,
						RiskLevel:       0,
						SuccessProbability: 1.0,
					},
				},
			}

			reqBody, _ := json.Marshal(reqData)
			req := httptest.NewRequest("POST", "/ai/"+npcID+"/decision", bytes.NewReader(reqBody))
			rctx := chi.NewRouteContext()
			rctx.URLParams.Add("npcId", npcID)
			req = req.WithContext(chi.NewRouteContext().WithRouteContext(req.Context(), rctx))

			w := httptest.NewRecorder()
			service.MakeDecision(w, req)

			if w.Code != http.StatusOK {
				t.Errorf("Expected status 200 for NPC %s, got %d", npcID, w.Code)
			}

			done <- true
		}(i)
	}

	// Wait for all goroutines to complete
	for i := 0; i < 10; i++ {
		<-done
	}

	t.Log("Concurrent AI processing test passed")
}
