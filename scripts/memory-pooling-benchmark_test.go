// Issue: #1867 - Memory pooling benchmark validation
// Benchmark tests for zero-allocations optimization validation
package main

import (
	"sync"
	"testing"
	"time"
)

// BenchmarkMemoryPooling validates memory pooling performance
func BenchmarkMemoryPooling(b *testing.B) {
	// Test sync.Pool performance vs regular allocations
	pool := sync.Pool{
		New: func() interface{} {
			return make([]byte, 1024)
		},
	}

	b.Run("WithPool", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			buf := pool.Get().([]byte)
			// Simulate work
			buf[0] = byte(i % 256)
			pool.Put(buf)
		}
	})

	b.Run("WithoutPool", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			buf := make([]byte, 1024)
			// Simulate work
			buf[0] = byte(i % 256)
			_ = buf // prevent optimization
		}
	})
}

// BenchmarkAtomicStats validates lock-free statistics performance
func BenchmarkAtomicStats(b *testing.B) {
	stats := NewAtomicStats()

	b.Run("AtomicOperations", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			stats.IncrementRequests()
			stats.AddLatency(time.Microsecond * time.Duration(i%1000))
			if i%10 == 0 {
				stats.IncrementErrors()
			}
		}
	})

	b.Run("StatsRetrieval", func(b *testing.B) {
		for i := 0; i < b.N; i++ {
			_ = stats.GetStats()
		}
	})
}

// BenchmarkStringBuilding validates string builder performance
func BenchmarkStringBuilding(b *testing.B) {
	testStrings := []string{"test", "string", "building", "performance"}

	b.Run("WithStringBuilderPool", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			sb := GetStringBuilder()
			for _, s := range testStrings {
				sb.WriteString(s)
			}
			_ = sb.String()
			ReleaseStringBuilder(sb)
		}
	})

	b.Run("WithRegularConcat", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			result := ""
			for _, s := range testStrings {
				result += s
			}
			_ = result
		}
	})
}

// BenchmarkConcurrentAccess validates concurrent memory pool access
func BenchmarkConcurrentAccess(b *testing.B) {
	pool := sync.Pool{
		New: func() interface{} {
			return &testStruct{data: make([]int, 10)}
		},
	}

	b.Run("ConcurrentPoolAccess", func(b *testing.B) {
		b.RunParallel(func(pb *testing.PB) {
			for pb.Next() {
				obj := pool.Get().(*testStruct)
				// Simulate work
				obj.data[0] = 42
				pool.Put(obj)
			}
		})
	})
}

type testStruct struct {
	data []int
}

// BenchmarkZeroAllocMap validates zero-allocation map performance
func BenchmarkZeroAllocMap(b *testing.B) {
	zam := NewZeroAllocMap()

	b.Run("ZeroAllocMapOperations", func(b *testing.B) {
		b.ReportAllocs()
		keys := make([]string, b.N)
		for i := range keys {
			keys[i] = string(rune('a' + i%26))
		}

		b.ResetTimer()
		for i := 0; i < b.N; i++ {
			key := keys[i%len(keys)]
			zam.Set(key, i)
			_, _ = zam.Get(key)
		}
	})
}

// BenchmarkServiceHandler simulates real service handler load
func BenchmarkServiceHandler(b *testing.B) {
	// This would simulate actual service handler calls
	// For now, just measure the overhead of our optimizations

	b.Run("HandlerOverhead", func(b *testing.B) {
		b.ReportAllocs()
		for i := 0; i < b.N; i++ {
			// Simulate handler operations
			buf := GetJSONBuffer()
			buf = append(buf, []byte("test data")...)
			ReleaseJSONBuffer(buf)

			slice := GetIntSlice()
			slice = append(slice, i)
			ReleaseIntSlice(slice)
		}
	})
}

// TestZeroAllocationsValidation runs allocation validation
func TestZeroAllocationsValidation(t *testing.T) {
	// Test that our zero-allocation helpers actually work
	allocs, ok := ValidateZeroAllocations(func() {
		// Test pooled operations
		buf := GetJSONBuffer()
		buf = append(buf, []byte("test")...)
		ReleaseJSONBuffer(buf)

		sb := GetStringBuilder()
		sb.WriteString("test")
		_ = sb.String()
		ReleaseStringBuilder(sb)
	})

	if !ok {
		t.Errorf("Zero allocation validation failed, allocations: %d", allocs)
	}
}
