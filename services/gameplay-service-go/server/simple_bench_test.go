package server

import (
	"encoding/json"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

// Simple benchmark to test memory pooling performance
func BenchmarkMemoryPooling(b *testing.B) {
	logger := logrus.New()
	handlers := NewHandlers(logger, nil)

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Test memory pool usage
		response := handlers.sessionListResponsePool.Get().(*api.SessionListResponse)
		handlers.sessionListResponsePool.Put(response)
	}
}

// Benchmark JSON marshaling performance
func BenchmarkJSONMarshalCombatSession(b *testing.B) {
	session := &api.CombatSessionResponse{
		ID:           api.NewOptUUID(uuid.New()),
		SessionType:  api.SessionTypePvpArena,
		Status:       api.SessionStatusActive,
		CreatedAt:    api.NewOptDateTime(time.Now()),
		Participants: []api.Participant{},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = json.Marshal(session)
	}
}

// Benchmark string operations that might cause allocations
func BenchmarkStringOperations(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Simulate query building that might cause allocations
		query := "SELECT id FROM combat_sessions WHERE status = 'active' AND session_type = 'pvp'"
		_ = query + " ORDER BY created_at DESC LIMIT 20"
	}
}
