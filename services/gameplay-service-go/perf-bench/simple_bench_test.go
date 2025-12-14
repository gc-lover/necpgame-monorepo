package main

import (
	"bytes"
	"encoding/json"
	"strconv"
	"strings"
	"sync"
	"sync/atomic"
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

// Optimized benchmarks with memory pooling
var optimizedPool = sync.Pool{
	New: func() interface{} {
		return &TestStruct{}
	},
}

func BenchmarkOptimizedMemoryPooling(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		obj := optimizedPool.Get().(*TestStruct)
		// Reset fields to avoid keeping references
		obj.ID = uuid.New()
		obj.Type = "test"
		obj.Status = "active"
		obj.CreatedAt = time.Now()
		obj.Data = obj.Data[:0] // Reset slice without reallocation
		obj.Data = append(obj.Data, "item1", "item2")
		optimizedPool.Put(obj)
	}
}

// Benchmark optimized JSON marshaling with buffer reuse
var jsonBufferPoolBench = sync.Pool{
	New: func() interface{} {
		return &bytes.Buffer{}
	},
}

func BenchmarkOptimizedJSONMarshal(b *testing.B) {
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
		buf := jsonBufferPoolBench.Get().(*bytes.Buffer)
		buf.Reset()
		encoder := json.NewEncoder(buf)
		_ = encoder.Encode(obj)
		_ = buf.Bytes()
		jsonBufferPoolBench.Put(buf)
	}
}

// Benchmark GC pressure simulation (memory allocations over time)
func BenchmarkGCPressure(b *testing.B) {
	b.ReportAllocs()

	// Simulate high-frequency allocations like in real server
	for i := 0; i < b.N; i++ {
		// Simulate JSON processing (common in APIs)
		data := map[string]interface{}{
			"id":       uuid.New(),
			"type":     "combat_session",
			"status":   "active",
			"players":  []string{"p1", "p2", "p3", "p4", "p5"},
			"metadata": map[string]int{"level": 10, "score": 1000},
		}
		_, _ = json.Marshal(data)

		// Simulate string operations (query building)
		query := "SELECT id, type, status FROM sessions WHERE status = 'active' AND type = 'pvp'"
		_ = query + " ORDER BY created_at DESC LIMIT 20"

		// Simulate slice operations
		slice := make([]string, 0, 10)
		for j := 0; j < 5; j++ {
			slice = append(slice, "item"+strconv.Itoa(j))
		}
		_ = slice
	}
}

// Benchmark lock-free operations (atomic counters, cache)
var (
	atomicCounter int64
	cacheMap      = make(map[string]*TestStruct)
	cacheMutex    sync.RWMutex
)

func BenchmarkAtomicOperations(b *testing.B) {
	b.ReportAllocs()
	b.ResetTimer()

	for i := 0; i < b.N; i++ {
		// Simulate atomic counter updates
		atomic.AddInt64(&atomicCounter, 1)
		_ = atomic.LoadInt64(&atomicCounter)

		// Simulate lock-free cache access pattern
		key := "session_" + strconv.Itoa(i%10)
		cacheMutex.RLock()
		_, _ = cacheMap[key]
		cacheMutex.RUnlock()
	}
}
