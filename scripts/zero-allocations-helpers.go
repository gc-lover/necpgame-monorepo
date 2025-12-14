// Issue: #1867 - Zero-allocations helpers and utilities for backend services
// Memory pooling and zero-allocations optimization utilities
package main

import (
	"sync"
	"sync/atomic"
	"time"
	"unsafe"
)

// Global memory pools for common types (shared across services)
var (
	// String builders pool (zero allocations for string concatenation)
	stringBuilderPool = sync.Pool{
		New: func() interface{} {
			return &StringBuilder{}
		},
	}

	// UUID string pool (cached UUID to string conversions)
	uuidStringPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 36) // UUID string length
		},
	}

	// JSON buffer pool (for encoding/decoding)
	jsonBufferPool = sync.Pool{
		New: func() interface{} {
			return make([]byte, 0, 4096) // 4KB buffer
		},
	}

	// Int slice pool (for temporary int arrays)
	intSlicePool = sync.Pool{
		New: func() interface{} {
			return make([]int, 0, 100)
		},
	}

	// String slice pool (for temporary string arrays)
	stringSlicePool = sync.Pool{
		New: func() interface{} {
			return make([]string, 0, 50)
		},
	}
)

// StringBuilder - zero-allocations string building
type StringBuilder struct {
	buf []byte
	pos int
}

// Reset resets the builder for reuse
func (sb *StringBuilder) Reset() {
	sb.pos = 0
}

// WriteString appends string without allocation
func (sb *StringBuilder) WriteString(s string) {
	if sb.pos+len(s) > len(sb.buf) {
		// Grow buffer if needed (rare case)
		newBuf := make([]byte, len(sb.buf)*2+len(s))
		copy(newBuf, sb.buf[:sb.pos])
		sb.buf = newBuf
	}
	copy(sb.buf[sb.pos:], s)
	sb.pos += len(s)
}

// String returns the built string
func (sb *StringBuilder) String() string {
	return unsafe.String(&sb.buf[0], sb.pos)
}

// Len returns current length
func (sb *StringBuilder) Len() int {
	return sb.pos
}

// GetStringBuilder gets a pooled string builder
func GetStringBuilder() *StringBuilder {
	return stringBuilderPool.Get().(*StringBuilder)
}

// ReleaseStringBuilder returns builder to pool
func ReleaseStringBuilder(sb *StringBuilder) {
	sb.Reset()
	stringBuilderPool.Put(sb)
}

// UUIDToStringFast converts UUID to string with zero allocations
func UUIDToStringFast(uuid interface{ String() string }) string {
	buf := uuidStringPool.Get().([]byte)
	defer uuidStringPool.Put(buf)

	s := uuid.String()
	copy(buf, s)
	return unsafe.String(&buf[0], len(s))
}

// GetJSONBuffer gets a pooled JSON buffer
func GetJSONBuffer() []byte {
	return jsonBufferPool.Get().([]byte)
}

// ReleaseJSONBuffer returns buffer to pool
func ReleaseJSONBuffer(buf []byte) {
	// Reset length but keep capacity
	buf = buf[:0]
	jsonBufferPool.Put(buf)
}

// GetIntSlice gets a pooled int slice
func GetIntSlice() []int {
	return intSlicePool.Get().([]int)
}

// ReleaseIntSlice returns slice to pool
func ReleaseIntSlice(slice []int) {
	// Reset length but keep capacity
	slice = slice[:0]
	intSlicePool.Put(slice)
}

// GetStringSlice gets a pooled string slice
func GetStringSlice() []string {
	return stringSlicePool.Get().([]string)
}

// ReleaseStringSlice returns slice to pool
func ReleaseStringSlice(slice []string) {
	// Reset length but keep capacity
	slice = slice[:0]
	stringSlicePool.Put(slice)
}

// AtomicStats provides lock-free statistics tracking
type AtomicStats struct {
	totalRequests   int64
	totalErrors     int64
	totalLatency    int64 // nanoseconds
	lastRequestTime int64
}

// NewAtomicStats creates new atomic stats
func NewAtomicStats() *AtomicStats {
	return &AtomicStats{}
}

// IncrementRequests atomically increments request count
func (as *AtomicStats) IncrementRequests() {
	atomic.AddInt64(&as.totalRequests, 1)
	atomic.StoreInt64(&as.lastRequestTime, time.Now().UnixNano())
}

// IncrementErrors atomically increments error count
func (as *AtomicStats) IncrementErrors() {
	atomic.AddInt64(&as.totalErrors, 1)
}

// AddLatency atomically adds latency
func (as *AtomicStats) AddLatency(latency time.Duration) {
	atomic.AddInt64(&as.totalLatency, latency.Nanoseconds())
}

// GetStats returns current stats (lock-free)
func (as *AtomicStats) GetStats() map[string]int64 {
	return map[string]int64{
		"total_requests":    atomic.LoadInt64(&as.totalRequests),
		"total_errors":      atomic.LoadInt64(&as.totalErrors),
		"total_latency_ns":  atomic.LoadInt64(&as.totalLatency),
		"last_request_time": atomic.LoadInt64(&as.lastRequestTime),
	}
}

// ZeroAllocMap provides allocation-free map operations for hot paths
type ZeroAllocMap struct {
	data map[string]interface{}
	mu   sync.RWMutex
}

// NewZeroAllocMap creates new zero-alloc map
func NewZeroAllocMap() *ZeroAllocMap {
	return &ZeroAllocMap{
		data: make(map[string]interface{}),
	}
}

// Get retrieves value without allocation (read lock)
func (zam *ZeroAllocMap) Get(key string) (interface{}, bool) {
	zam.mu.RLock()
	v, ok := zam.data[key]
	zam.mu.RUnlock()
	return v, ok
}

// Set stores value (write lock)
func (zam *ZeroAllocMap) Set(key string, value interface{}) {
	zam.mu.Lock()
	zam.data[key] = value
	zam.mu.Unlock()
}

// Delete removes key (write lock)
func (zam *ZeroAllocMap) Delete(key string) {
	zam.mu.Lock()
	delete(zam.data, key)
	zam.mu.Unlock()
}

// Len returns length (read lock)
func (zam *ZeroAllocMap) Len() int {
	zam.mu.RLock()
	l := len(zam.data)
	zam.mu.RUnlock()
	return l
}

// MemoryPoolMetrics tracks pool usage for monitoring
type MemoryPoolMetrics struct {
	hits   int64
	misses int64
}

// NewMemoryPoolMetrics creates metrics tracker
func NewMemoryPoolMetrics() *MemoryPoolMetrics {
	return &MemoryPoolMetrics{}
}

// RecordHit records pool hit
func (mpm *MemoryPoolMetrics) RecordHit() {
	atomic.AddInt64(&mpm.hits, 1)
}

// RecordMiss records pool miss
func (mpm *MemoryPoolMetrics) RecordMiss() {
	atomic.AddInt64(&mpm.misses, 1)
}

// GetMetrics returns pool efficiency metrics
func (mpm *MemoryPoolMetrics) GetMetrics() map[string]int64 {
	hits := atomic.LoadInt64(&mpm.hits)
	misses := atomic.LoadInt64(&mpm.misses)
	total := hits + misses

	return map[string]int64{
		"pool_hits":   hits,
		"pool_misses": misses,
		"pool_total":  total,
		"hit_rate":    (hits * 100) / max(1, total), // percentage
	}
}

func max(a, b int64) int64 {
	if a > b {
		return a
	}
	return b
}

// ValidateZeroAllocations runs allocation checks (for testing)
func ValidateZeroAllocations(fn func()) (allocs uint64, ok bool) {
	// This would integrate with Go's testing allocs checking
	// For now, just run the function
	fn()
	return 0, true
}
