package bench

import (
	"encoding/json"
	"strings"
	"sync"
	"testing"
	"time"

	"github.com/google/uuid"
)

// TestStruct represents a typical API response structure
type TestStruct struct {
	ID        uuid.UUID `json:"id"`
	Type      string    `json:"type"`
	Status    string    `json:"status"`
	CreatedAt time.Time `json:"created_at"`
	Data      []string  `json:"data"`
}

// Memory pool for zero allocations testing
var testStructPool = sync.Pool{
	New: func() interface{} {
		return &TestStruct{}
	},
}

// Benchmark memory pooling performance
func BenchmarkMemoryPooling(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		obj := testStructPool.Get().(*TestStruct)
		// Simulate usage
		obj.ID = uuid.New()
		obj.Type = "test"
		obj.Status = "active"
		obj.CreatedAt = time.Now()
		obj.Data = []string{"item1", "item2"}
		testStructPool.Put(obj)
	}
}

// Benchmark JSON marshaling performance (common source of allocations)
func BenchmarkJSONMarshal(b *testing.B) {
	obj := &TestStruct{
		ID:        uuid.New(),
		Type:      "combat_session",
		Status:    "active",
		CreatedAt: time.Now(),
		Data:      []string{"player1", "player2", "player3"},
	}

	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		_, _ = json.Marshal(obj)
	}
}

// Benchmark string concatenation (another common allocation source)
func BenchmarkStringConcatenation(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		query := "SELECT id, type, status FROM combat_sessions"
		query += " WHERE status = 'active'"
		query += " AND type = 'pvp'"
		query += " ORDER BY created_at DESC"
		query += " LIMIT 20"
		_ = query
	}
}

// Benchmark string building with strings.Builder (optimized version)
func BenchmarkStringBuilder(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		var builder strings.Builder
		builder.WriteString("SELECT id, type, status FROM combat_sessions")
		builder.WriteString(" WHERE status = 'active'")
		builder.WriteString(" AND type = 'pvp'")
		builder.WriteString(" ORDER BY created_at DESC")
		builder.WriteString(" LIMIT 20")
		_ = builder.String()
	}
}

// Benchmark map operations (common in request processing)
func BenchmarkMapOperations(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		m := make(map[string]interface{})
		m["id"] = uuid.New()
		m["type"] = "combat_session"
		m["status"] = "active"
		m["participants"] = []string{"player1", "player2"}
		_ = m
	}
}
