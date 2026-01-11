// Memory Pool Library for Hot Path Optimization
// Issue: #1954
// PERFORMANCE: Object pooling to reduce GC pressure in high-throughput operations

package memory

import (
	"bytes"
	"strings"
	"sync"
)

// ResponsePool provides pooled Response objects for HTTP handlers
// PERFORMANCE: Reduces allocations in hot path HTTP handlers
var ResponsePool = sync.Pool{
	New: func() interface{} {
		return &Response{
			Data: make([]byte, 0, 1024), // Pre-allocate 1KB capacity
		}
	},
}

// Response represents a pooled HTTP response object
type Response struct {
	Status int
	Data   []byte
	Error  error
}

// Reset resets the response for reuse
func (r *Response) Reset() {
	r.Status = 0
	r.Data = r.Data[:0]
	r.Error = nil
}

// BufferPool provides pooled byte buffers for temporary operations
// PERFORMANCE: Reduces allocations for temporary buffers in hot paths
var BufferPool = sync.Pool{
	New: func() interface{} {
		return make([]byte, 0, 4096) // Pre-allocate 4KB capacity
	},
}

// GetBuffer gets a buffer from the pool and resets it
func GetBuffer() []byte {
	buf := BufferPool.Get().([]byte)
	return buf[:0] // Reset length, keep capacity
}

// PutBuffer returns a buffer to the pool
func PutBuffer(buf []byte) {
	if cap(buf) > 0 {
		BufferPool.Put(buf[:0]) // Reset before returning
	}
}

// BytesBufferPool provides pooled bytes.Buffer for JSON/text operations
// PERFORMANCE: Reduces allocations for JSON marshaling and text operations
var BytesBufferPool = sync.Pool{
	New: func() interface{} {
		return &bytes.Buffer{}
	},
}

// GetBytesBuffer gets a bytes.Buffer from the pool
func GetBytesBuffer() *bytes.Buffer {
	return BytesBufferPool.Get().(*bytes.Buffer)
}

// PutBytesBuffer returns a bytes.Buffer to the pool after resetting
func PutBytesBuffer(buf *bytes.Buffer) {
	if buf != nil {
		buf.Reset()
		BytesBufferPool.Put(buf)
	}
}

// StringBuilderPool provides pooled strings.Builder for string concatenation
// PERFORMANCE: Reduces allocations for string building operations
var StringBuilderPool = sync.Pool{
	New: func() interface{} {
		return &StringBuilder{
			builder: &strings.Builder{},
		}
	},
}

// StringBuilder wraps strings.Builder with pool management
type StringBuilder struct {
	builder *strings.Builder
}

// Reset resets the builder for reuse
func (sb *StringBuilder) Reset() {
	sb.builder.Reset()
}

// WriteString writes a string to the builder
func (sb *StringBuilder) WriteString(s string) {
	sb.builder.WriteString(s)
}

// String returns the built string
func (sb *StringBuilder) String() string {
	return sb.builder.String()
}

// GetStringBuilder gets a StringBuilder from the pool
func GetStringBuilder() *StringBuilder {
	sb := StringBuilderPool.Get().(*StringBuilder)
	sb.Reset()
	return sb
}

// PutStringBuilder returns a StringBuilder to the pool
func PutStringBuilder(sb *StringBuilder) {
	if sb != nil {
		sb.Reset()
		StringBuilderPool.Put(sb)
	}
}

// MapPool provides pooled maps for temporary key-value operations
// PERFORMANCE: Reduces allocations for temporary map operations
var MapPool = sync.Pool{
	New: func() interface{} {
		return make(map[string]interface{}, 10) // Pre-allocate 10 entries
	},
}

// GetMap gets a map from the pool
func GetMap() map[string]interface{} {
	return MapPool.Get().(map[string]interface{})
}

// PutMap returns a map to the pool after clearing
func PutMap(m map[string]interface{}) {
	if m != nil {
		// Clear map for reuse
		for k := range m {
			delete(m, k)
		}
		MapPool.Put(m)
	}
}
