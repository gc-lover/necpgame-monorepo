// Memory-Efficient Data Structures for Large-Scale Simulations
// Issue: #2095
// PERFORMANCE: Optimized for 10k+ entities in simulations with minimal allocations

package memory

import (
	"sync"
	"sync/atomic"
)

// SparseArray provides a memory-efficient array for sparse data
// Only allocates memory for non-zero elements
// PERFORMANCE: O(1) access, minimal memory footprint for sparse data
type SparseArray[T any] struct {
	data     map[int]T
	zero     T // Zero value for type T
	mu       sync.RWMutex
	size     atomic.Int64
	capacity int
}

// NewSparseArray creates a new sparse array
func NewSparseArray[T any](capacity int) *SparseArray[T] {
	return &SparseArray[T]{
		data:     make(map[int]T, capacity/10), // Pre-allocate for 10% fill rate
		capacity: capacity,
	}
}

// Set sets a value at index
func (sa *SparseArray[T]) Set(index int, value T) {
	if index < 0 || index >= sa.capacity {
		return
	}

	sa.mu.Lock()
	defer sa.mu.Unlock()

	// Check if value is zero (for removal)
	var zero T
	if value == zero {
		if _, exists := sa.data[index]; exists {
			delete(sa.data, index)
			sa.size.Add(-1)
		}
		return
	}

	if _, exists := sa.data[index]; !exists {
		sa.size.Add(1)
	}
	sa.data[index] = value
}

// Get gets a value at index
func (sa *SparseArray[T]) Get(index int) T {
	if index < 0 || index >= sa.capacity {
		return sa.zero
	}

	sa.mu.RLock()
	defer sa.mu.RUnlock()

	if value, exists := sa.data[index]; exists {
		return value
	}
	return sa.zero
}

// Has checks if index has a non-zero value
func (sa *SparseArray[T]) Has(index int) bool {
	if index < 0 || index >= sa.capacity {
		return false
	}

	sa.mu.RLock()
	defer sa.mu.RUnlock()

	_, exists := sa.data[index]
	return exists
}

// Size returns the number of non-zero elements
func (sa *SparseArray[T]) Size() int64 {
	return sa.size.Load()
}

// Clear removes all elements
func (sa *SparseArray[T]) Clear() {
	sa.mu.Lock()
	defer sa.mu.Unlock()

	sa.data = make(map[int]T, sa.capacity/10)
	sa.size.Store(0)
}

// RingBuffer provides a fixed-size circular buffer for temporal data
// PERFORMANCE: O(1) insert/read, no allocations after initialization
type RingBuffer[T any] struct {
	buffer   []T
	head     atomic.Int64
	tail     atomic.Int64
	size     atomic.Int64
	capacity int64
	mu       sync.RWMutex
}

// NewRingBuffer creates a new ring buffer
func NewRingBuffer[T any](capacity int) *RingBuffer[T] {
	return &RingBuffer[T]{
		buffer:   make([]T, capacity),
		capacity: int64(capacity),
	}
}

// Push adds an element to the buffer (overwrites oldest if full)
func (rb *RingBuffer[T]) Push(value T) {
	rb.mu.Lock()
	defer rb.mu.Unlock()

	head := rb.head.Load()
	rb.buffer[head%rb.capacity] = value

	newHead := (head + 1) % rb.capacity
	rb.head.Store(newHead)

	// Update size (don't exceed capacity)
	currentSize := rb.size.Load()
	if currentSize < rb.capacity {
		rb.size.Add(1)
	} else {
		// Buffer is full, advance tail
		rb.tail.Store((rb.tail.Load() + 1) % rb.capacity)
	}
}

// Pop removes and returns the oldest element
func (rb *RingBuffer[T]) Pop() (T, bool) {
	rb.mu.Lock()
	defer rb.mu.Unlock()

	size := rb.size.Load()
	if size == 0 {
		var zero T
		return zero, false
	}

	tail := rb.tail.Load()
	value := rb.buffer[tail%rb.capacity]

	rb.tail.Store((tail + 1) % rb.capacity)
	rb.size.Add(-1)

	return value, true
}

// Peek returns the oldest element without removing it
func (rb *RingBuffer[T]) Peek() (T, bool) {
	rb.mu.RLock()
	defer rb.mu.RUnlock()

	size := rb.size.Load()
	if size == 0 {
		var zero T
		return zero, false
	}

	tail := rb.tail.Load()
	return rb.buffer[tail%rb.capacity], true
}

// Size returns the number of elements in the buffer
func (rb *RingBuffer[T]) Size() int64 {
	return rb.size.Load()
}

// Capacity returns the buffer capacity
func (rb *RingBuffer[T]) Capacity() int64 {
	return rb.capacity
}

// Clear removes all elements
func (rb *RingBuffer[T]) Clear() {
	rb.mu.Lock()
	defer rb.mu.Unlock()

	rb.head.Store(0)
	rb.tail.Store(0)
	rb.size.Store(0)
}

// SpatialHashMap provides a 3D spatial hash map for efficient spatial queries
// PERFORMANCE: O(1) average case for insert/lookup, O(k) for range queries where k is entities in cell
type SpatialHashMap[T any] struct {
	cellSize float64
	cells    map[cellKey][]spatialEntry[T]
	mu       sync.RWMutex
}

type cellKey struct {
	x, y, z int32
}

type spatialEntry[T any] struct {
	value    T
	position [3]float64
}

// NewSpatialHashMap creates a new spatial hash map
func NewSpatialHashMap[T any](cellSize float64) *SpatialHashMap[T] {
	return &SpatialHashMap[T]{
		cellSize: cellSize,
		cells:    make(map[cellKey][]spatialEntry[T], 1024),
	}
}

// Insert inserts a value at a 3D position
func (shm *SpatialHashMap[T]) Insert(x, y, z float64, value T) {
	key := cellKey{
		x: int32(x / shm.cellSize),
		y: int32(y / shm.cellSize),
		z: int32(z / shm.cellSize),
	}

	shm.mu.Lock()
	defer shm.mu.Unlock()

	shm.cells[key] = append(shm.cells[key], spatialEntry[T]{
		value:    value,
		position: [3]float64{x, y, z},
	})
}

// QueryRange returns all values within a bounding box
func (shm *SpatialHashMap[T]) QueryRange(minX, minY, minZ, maxX, maxY, maxZ float64) []T {
	minKey := cellKey{
		x: int32(minX / shm.cellSize),
		y: int32(minY / shm.cellSize),
		z: int32(minZ / shm.cellSize),
	}
	maxKey := cellKey{
		x: int32(maxX / shm.cellSize),
		y: int32(maxY / shm.cellSize),
		z: int32(maxZ / shm.cellSize),
	}

	shm.mu.RLock()
	defer shm.mu.RUnlock()

	var results []T
	for x := minKey.x; x <= maxKey.x; x++ {
		for y := minKey.y; y <= maxKey.y; y++ {
			for z := minKey.z; z <= maxKey.z; z++ {
				key := cellKey{x: x, y: y, z: z}
				if entries, exists := shm.cells[key]; exists {
					for _, entry := range entries {
						px, py, pz := entry.position[0], entry.position[1], entry.position[2]
						if px >= minX && px <= maxX && py >= minY && py <= maxY && pz >= minZ && pz <= maxZ {
							results = append(results, entry.value)
						}
					}
				}
			}
		}
	}
	return results
}

// Clear removes all entries
func (shm *SpatialHashMap[T]) Clear() {
	shm.mu.Lock()
	defer shm.mu.Unlock()

	shm.cells = make(map[cellKey][]spatialEntry[T], 1024)
}

// CompactMap provides a memory-efficient map for small key sets
// Uses array for small sets, switches to map for large sets
// PERFORMANCE: O(1) for small sets, O(log n) for large sets
type CompactMap[K comparable, V any] struct {
	small    []compactEntry[K, V] // Array for small sets (<16 elements)
	large    map[K]V              // Map for large sets
	useLarge bool
	mu       sync.RWMutex
}

type compactEntry[K comparable, V any] struct {
	key   K
	value V
}

// NewCompactMap creates a new compact map
func NewCompactMap[K comparable, V any]() *CompactMap[K, V] {
	return &CompactMap[K, V]{
		small: make([]compactEntry[K, V], 0, 16),
	}
}

// Set sets a key-value pair
func (cm *CompactMap[K, V]) Set(key K, value V) {
	cm.mu.Lock()
	defer cm.mu.Unlock()

	if !cm.useLarge {
		// Check if key exists in small array
		for i := range cm.small {
			if cm.small[i].key == key {
				cm.small[i].value = value
				return
			}
		}

		// Add to small array
		cm.small = append(cm.small, compactEntry[K, V]{key: key, value: value})

		// Switch to large map if threshold exceeded
		if len(cm.small) >= 16 {
			cm.large = make(map[K]V, len(cm.small)*2)
			for _, entry := range cm.small {
				cm.large[entry.key] = entry.value
			}
			cm.small = nil
			cm.useLarge = true
		}
	} else {
		cm.large[key] = value
	}
}

// Get gets a value by key
func (cm *CompactMap[K, V]) Get(key K) (V, bool) {
	cm.mu.RLock()
	defer cm.mu.RUnlock()

	if !cm.useLarge {
		for _, entry := range cm.small {
			if entry.key == key {
				return entry.value, true
			}
		}
		var zero V
		return zero, false
	}

	value, exists := cm.large[key]
	return value, exists
}

// Delete removes a key-value pair
func (cm *CompactMap[K, V]) Delete(key K) {
	cm.mu.Lock()
	defer cm.mu.Unlock()

	if !cm.useLarge {
		for i := range cm.small {
			if cm.small[i].key == key {
				cm.small = append(cm.small[:i], cm.small[i+1:]...)
				return
			}
		}
	} else {
		delete(cm.large, key)
	}
}

// Size returns the number of elements
func (cm *CompactMap[K, V]) Size() int {
	cm.mu.RLock()
	defer cm.mu.RUnlock()

	if !cm.useLarge {
		return len(cm.small)
	}
	return len(cm.large)
}

// Clear removes all elements
func (cm *CompactMap[K, V]) Clear() {
	cm.mu.Lock()
	defer cm.mu.Unlock()

	cm.small = cm.small[:0]
	cm.large = nil
	cm.useLarge = false
}
