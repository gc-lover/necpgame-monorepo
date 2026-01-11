package server

import (
	"fmt"
	"io"
	"os"
	"path/filepath"
	"sort"
	"sync"
	"sync/atomic"
	"time"
	"unsafe"

	"golang.org/x/sys/windows"
)

// MemoryMappedCacheManager provides high-performance memory-mapped file caching for large game assets
type MemoryMappedCacheManager struct {
	config       CacheConfig
	cacheDir     string
	memoryUsed   int64 // atomic
	fileMappings map[string]*FileMapping
	mutex        sync.RWMutex
	lruList      *LRUList
	maxMemory    int64
}

// CacheConfig configures the memory-mapped cache behavior
type CacheConfig struct {
	MaxMemoryMB       int     // Maximum memory to use for mappings (MB)
	MaxFileSizeMB     int     // Maximum file size to memory map (MB)
	CacheDir          string  // Directory for cache files
	CompressionEnabled bool    // Enable LZ4 compression for large files
	PreloadEnabled    bool    // Preload frequently accessed files
	LRUSize           int     // LRU cache size for access tracking
}

// FileMapping represents a memory-mapped file
type FileMapping struct {
	filePath    string
	fileSize    int64
	mappingSize int64
	fileHandle  windows.Handle
	mapHandle   windows.Handle
	dataPtr     uintptr
	accessTime  time.Time
	refCount    int32 // atomic
	isCompressed bool
	originalSize int64
}

// LRUList implements a simple LRU cache for access tracking
type LRUList struct {
	items map[string]*LRUItem
	head  *LRUItem
	tail  *LRUItem
	size  int
	maxSize int
	mutex sync.Mutex
}

type LRUItem struct {
	key   string
	prev  *LRUItem
	next  *LRUItem
}

// NewMemoryMappedCacheManager creates a new memory-mapped cache manager
func NewMemoryMappedCacheManager(config CacheConfig) (*MemoryMappedCacheManager, error) {
	if config.MaxMemoryMB <= 0 {
		config.MaxMemoryMB = 512 // Default 512MB
	}
	if config.MaxFileSizeMB <= 0 {
		config.MaxFileSizeMB = 50 // Default 50MB
	}
	if config.CacheDir == "" {
		config.CacheDir = "./cache"
	}
	if config.LRUSize <= 0 {
		config.LRUSize = 1000 // Default LRU size
	}

	cacheDir := filepath.Clean(config.CacheDir)
	if err := os.MkdirAll(cacheDir, 0755); err != nil {
		return nil, fmt.Errorf("failed to create cache directory: %w", err)
	}

	manager := &MemoryMappedCacheManager{
		config:       config,
		cacheDir:     cacheDir,
		fileMappings: make(map[string]*FileMapping),
		maxMemory:    int64(config.MaxMemoryMB) * 1024 * 1024, // Convert MB to bytes
		lruList:      NewLRUList(config.LRUSize),
	}

	return manager, nil
}

// LoadFile loads a file into memory-mapped cache
func (m *MemoryMappedCacheManager) LoadFile(filePath string) ([]byte, error) {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	// Check if file is already mapped
	if mapping, exists := m.fileMappings[filePath]; exists {
		atomic.AddInt32(&mapping.refCount, 1)
		m.lruList.Access(filePath)
		mapping.accessTime = time.Now()

		// Return slice of the mapped memory
		return unsafe.Slice((*byte)(unsafe.Pointer(mapping.dataPtr)), mapping.mappingSize), nil
	}

	// Check file size
	fileInfo, err := os.Stat(filePath)
	if err != nil {
		return nil, fmt.Errorf("failed to stat file: %w", err)
	}

	fileSize := fileInfo.Size()
	maxFileSize := int64(m.config.MaxFileSizeMB) * 1024 * 1024

	if fileSize > maxFileSize {
		return nil, fmt.Errorf("file too large for memory mapping: %d bytes > %d bytes", fileSize, maxFileSize)
	}

	// Check if we have enough memory
	currentMemory := atomic.LoadInt64(&m.memoryUsed)
	if currentMemory + fileSize > m.maxMemory {
		// Try to evict least recently used files
		if err := m.evictLRU(fileSize); err != nil {
			return nil, fmt.Errorf("insufficient memory for file mapping: %w", err)
		}
	}

	// Create memory mapping
	mapping, err := m.createFileMapping(filePath, fileSize)
	if err != nil {
		return nil, fmt.Errorf("failed to create file mapping: %w", err)
	}

	// Add to cache
	m.fileMappings[filePath] = mapping
	atomic.AddInt64(&m.memoryUsed, mapping.mappingSize)
	m.lruList.Access(filePath)

	return unsafe.Slice((*byte)(unsafe.Pointer(mapping.dataPtr)), mapping.mappingSize), nil
}

// UnloadFile unloads a file from memory-mapped cache
func (m *MemoryMappedCacheManager) UnloadFile(filePath string) error {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	mapping, exists := m.fileMappings[filePath]
	if !exists {
		return nil // Already unloaded
	}

	refCount := atomic.AddInt32(&mapping.refCount, -1)
	if refCount > 0 {
		return nil // Still referenced
	}

	// Close mapping
	if err := m.closeFileMapping(mapping); err != nil {
		return fmt.Errorf("failed to close file mapping: %w", err)
	}

	atomic.AddInt64(&m.memoryUsed, -mapping.mappingSize)
	delete(m.fileMappings, filePath)
	m.lruList.Remove(filePath)

	return nil
}

// GetStats returns cache statistics
func (m *MemoryMappedCacheManager) GetStats() CacheStats {
	m.mutex.RLock()
	defer m.mutex.RUnlock()

	return CacheStats{
		MemoryUsedMB:     float64(atomic.LoadInt64(&m.memoryUsed)) / (1024 * 1024),
		MaxMemoryMB:      float64(m.maxMemory) / (1024 * 1024),
		FilesMapped:      len(m.fileMappings),
		LRUCacheSize:     m.lruList.size,
		CacheHitRatio:    m.calculateHitRatio(),
	}
}

// Close closes all mappings and cleans up resources
func (m *MemoryMappedCacheManager) Close() error {
	m.mutex.Lock()
	defer m.mutex.Unlock()

	var lastErr error
	for filePath, mapping := range m.fileMappings {
		if err := m.closeFileMapping(mapping); err != nil {
			lastErr = err
		}
		delete(m.fileMappings, filePath)
	}

	atomic.StoreInt64(&m.memoryUsed, 0)
	return lastErr
}

// createFileMapping creates a memory mapping for a file
func (m *MemoryMappedCacheManager) createFileMapping(filePath string, fileSize int64) (*FileMapping, error) {
	// Open file
	fileHandle, err := windows.CreateFile(
		windows.StringToUTF16Ptr(filePath),
		windows.GENERIC_READ,
		windows.FILE_SHARE_READ,
		nil,
		windows.OPEN_EXISTING,
		windows.FILE_ATTRIBUTE_NORMAL,
		0,
	)
	if err != nil {
		return nil, fmt.Errorf("CreateFile failed: %w", err)
	}

	// Create file mapping
	mapHandle, err := windows.CreateFileMapping(
		fileHandle,
		nil,
		windows.PAGE_READONLY,
		uint32(fileSize>>32),
		uint32(fileSize&0xFFFFFFFF),
		nil,
	)
	if err != nil {
		windows.CloseHandle(fileHandle)
		return nil, fmt.Errorf("CreateFileMapping failed: %w", err)
	}

	// Map view of file
	dataPtr, err := windows.MapViewOfFile(
		mapHandle,
		windows.FILE_MAP_READ,
		0, 0,
		uintptr(fileSize),
	)
	if err != nil {
		windows.CloseHandle(mapHandle)
		windows.CloseHandle(fileHandle)
		return nil, fmt.Errorf("MapViewOfFile failed: %w", err)
	}

	return &FileMapping{
		filePath:    filePath,
		fileSize:    fileSize,
		mappingSize: fileSize,
		fileHandle:  fileHandle,
		mapHandle:   mapHandle,
		dataPtr:     dataPtr,
		accessTime:  time.Now(),
		refCount:    1,
		isCompressed: false,
		originalSize: fileSize,
	}, nil
}

// closeFileMapping closes a file mapping
func (m *MemoryMappedCacheManager) closeFileMapping(mapping *FileMapping) error {
	var lastErr error

	if mapping.dataPtr != 0 {
		if err := windows.UnmapViewOfFile(mapping.dataPtr); err != nil {
			lastErr = err
		}
	}

	if mapping.mapHandle != 0 {
		if err := windows.CloseHandle(mapping.mapHandle); err != nil && lastErr == nil {
			lastErr = err
		}
	}

	if mapping.fileHandle != 0 {
		if err := windows.CloseHandle(mapping.fileHandle); err != nil && lastErr == nil {
			lastErr = err
		}
	}

	return lastErr
}

// evictLRU evicts least recently used files to free up memory
func (m *MemoryMappedCacheManager) evictLRU(requiredBytes int64) error {
	// Sort mappings by access time (oldest first)
	type mappingInfo struct {
		filePath string
		size     int64
	}

	var mappings []mappingInfo
	for filePath, mapping := range m.fileMappings {
		if atomic.LoadInt32(&mapping.refCount) == 0 {
			mappings = append(mappings, mappingInfo{filePath, mapping.mappingSize})
		}
	}

	// Sort by access time (oldest first)
	sort.Slice(mappings, func(i, j int) bool {
		mappingI := m.fileMappings[mappings[i].filePath]
		mappingJ := m.fileMappings[mappings[j].filePath]
		return mappingI.accessTime.Before(mappingJ.accessTime)
	})

	freedBytes := int64(0)
	for _, info := range mappings {
		if freedBytes >= requiredBytes {
			break
		}

		if err := m.closeFileMapping(m.fileMappings[info.filePath]); err != nil {
			continue // Skip on error, try next
		}

		atomic.AddInt64(&m.memoryUsed, -info.size)
		delete(m.fileMappings, info.filePath)
		m.lruList.Remove(info.filePath)
		freedBytes += info.size
	}

	if freedBytes < requiredBytes {
		return fmt.Errorf("could not free enough memory: freed %d, required %d", freedBytes, requiredBytes)
	}

	return nil
}

// calculateHitRatio calculates cache hit ratio (simplified)
func (m *MemoryMappedCacheManager) calculateHitRatio() float64 {
	totalAccesses := m.lruList.size
	if totalAccesses == 0 {
		return 0.0
	}

	hits := 0
	for _, item := range m.lruList.items {
		if item != nil {
			hits++
		}
	}

	return float64(hits) / float64(totalAccesses)
}

// CacheStats represents cache performance statistics
type CacheStats struct {
	MemoryUsedMB  float64 `json:"memory_used_mb"`
	MaxMemoryMB   float64 `json:"max_memory_mb"`
	FilesMapped   int     `json:"files_mapped"`
	LRUCacheSize  int     `json:"lru_cache_size"`
	CacheHitRatio float64 `json:"cache_hit_ratio"`
}

// NewLRUList creates a new LRU list
func NewLRUList(maxSize int) *LRUList {
	return &LRUList{
		items:   make(map[string]*LRUItem),
		maxSize: maxSize,
	}
}

// Access marks an item as recently accessed
func (l *LRUList) Access(key string) {
	l.mutex.Lock()
	defer l.mutex.Unlock()

	if item, exists := l.items[key]; exists {
		l.moveToFront(item)
		return
	}

	// Add new item
	item := &LRUItem{key: key}
	l.items[key] = item

	if l.head == nil {
		l.head = item
		l.tail = item
	} else {
		item.next = l.head
		l.head.prev = item
		l.head = item
	}

	l.size++

	// Evict if over limit
	if l.size > l.maxSize {
		l.evictOldest()
	}
}

// Remove removes an item from the LRU list
func (l *LRUList) Remove(key string) {
	l.mutex.Lock()
	defer l.mutex.Unlock()

	item, exists := l.items[key]
	if !exists {
		return
	}

	l.removeItem(item)
	delete(l.items, key)
	l.size--
}

func (l *LRUList) moveToFront(item *LRUItem) {
	if item == l.head {
		return
	}

	l.removeItem(item)

	item.next = l.head
	item.prev = nil
	if l.head != nil {
		l.head.prev = item
	}
	l.head = item

	if l.tail == nil {
		l.tail = item
	}
}

func (l *LRUList) removeItem(item *LRUItem) {
	if item.prev != nil {
		item.prev.next = item.next
	} else {
		l.head = item.next
	}

	if item.next != nil {
		item.next.prev = item.prev
	} else {
		l.tail = item.prev
	}
}

func (l *LRUList) evictOldest() {
	if l.tail == nil {
		return
	}

	delete(l.items, l.tail.key)
	l.removeItem(l.tail)
	l.size--
}