// Memory Pooling and Object Reuse Library
// Issue: #2153
// PERFORMANCE: Redis cluster, memory pooling, object reuse
// Enterprise-grade memory optimization for all Go services

package memory

import (
	"sync"
)

// Pool provides a generic object pool for memory-efficient object reuse
// PERFORMANCE: Reduces GC pressure by reusing objects instead of allocating new ones
type Pool[T any] struct {
	pool sync.Pool
	reset func(*T)
}

// NewPool creates a new object pool
func NewPool[T any](factory func() *T, reset func(*T)) *Pool[T] {
	return &Pool[T]{
		pool: sync.Pool{
			New: func() interface{} {
				return factory()
			},
		},
		reset: reset,
	}
}

// Get retrieves an object from the pool
func (p *Pool[T]) Get() *T {
	return p.pool.Get().(*T)
}

// Put returns an object to the pool
func (p *Pool[T]) Put(obj *T) {
	if obj == nil {
		return
	}
	
	if p.reset != nil {
		p.reset(obj)
	}
	
	p.pool.Put(obj)
}

// BufferPool provides a pool for byte buffers
type BufferPool struct {
	pool sync.Pool
	size int
}

// NewBufferPool creates a new buffer pool with specified size
func NewBufferPool(size int) *BufferPool {
	return &BufferPool{
		pool: sync.Pool{
			New: func() interface{} {
				return make([]byte, 0, size)
			},
		},
		size: size,
	}
}

// Get retrieves a buffer from the pool
func (p *BufferPool) Get() []byte {
	return p.pool.Get().([]byte)
}

// Put returns a buffer to the pool
func (p *BufferPool) Put(buf []byte) {
	if buf == nil {
		return
	}
	
	// Only return buffers that are not too large
	if cap(buf) > p.size*2 {
		return
	}
	
	// Reset buffer
	buf = buf[:0]
	p.pool.Put(buf)
}

// SlicePool provides a pool for slices of a specific type
type SlicePool[T any] struct {
	pool sync.Pool
	capacity int
}

// NewSlicePool creates a new slice pool with specified capacity
func NewSlicePool[T any](capacity int) *SlicePool[T] {
	return &SlicePool[T]{
		pool: sync.Pool{
			New: func() interface{} {
				return make([]T, 0, capacity)
			},
		},
		capacity: capacity,
	}
}

// Get retrieves a slice from the pool
func (p *SlicePool[T]) Get() []T {
	return p.pool.Get().([]T)
}

// Put returns a slice to the pool
func (p *SlicePool[T]) Put(slice []T) {
	if slice == nil {
		return
	}
	
	// Only return slices that are not too large
	if cap(slice) > p.capacity*2 {
		return
	}
	
	// Reset slice
	slice = slice[:0]
	p.pool.Put(slice)
}

// MapPool provides a pool for maps
type MapPool[K comparable, V any] struct {
	pool sync.Pool
	capacity int
}

// NewMapPool creates a new map pool with specified capacity
func NewMapPool[K comparable, V any](capacity int) *MapPool[K, V] {
	return &MapPool[K, V]{
		pool: sync.Pool{
			New: func() interface{} {
				return make(map[K]V, capacity)
			},
		},
		capacity: capacity,
	}
}

// Get retrieves a map from the pool
func (p *MapPool[K, V]) Get() map[K]V {
	return p.pool.Get().(map[K]V)
}

// Put returns a map to the pool
func (p *MapPool[K, V]) Put(m map[K]V) {
	if m == nil {
		return
	}
	
	// Clear map
	for k := range m {
		delete(m, k)
	}
	
	p.pool.Put(m)
}
