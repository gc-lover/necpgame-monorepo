package buffer

import (
	"bytes"
	"sync"

	"go.uber.org/zap"
)

// Config holds buffer pool configuration
type Config struct {
	InitialSize int
	MaxSize     int
	Logger      *zap.Logger
}

// Pool manages a pool of reusable buffers
type Pool struct {
	config Config
	logger *zap.Logger

	// Buffer pool
	pool sync.Pool

	// Metrics
	activeBuffers int64
	totalBuffers  int64
	mu            sync.RWMutex
}

// NewPool creates a new buffer pool
func NewPool(config Config) *Pool {
	pool := &Pool{
		config: config,
		logger: config.Logger,
	}

	pool.pool = sync.Pool{
		New: func() interface{} {
			return bytes.NewBuffer(make([]byte, 0, config.InitialSize))
		},
	}

	pool.logger.Info("buffer pool created",
		zap.Int("initial_size", config.InitialSize),
		zap.Int("max_size", config.MaxSize))

	return pool
}

// Get retrieves a buffer from the pool
func (p *Pool) Get() *bytes.Buffer {
	buf := p.pool.Get().(*bytes.Buffer)
	buf.Reset()

	p.mu.Lock()
	p.activeBuffers++
	p.totalBuffers++
	p.mu.Unlock()

	p.logger.Debug("buffer retrieved from pool", zap.Int64("active", p.activeBuffers))
	return buf
}

// Put returns a buffer to the pool
func (p *Pool) Put(buf *bytes.Buffer) {
	if buf == nil {
		return
	}

	// Reset buffer
	buf.Reset()

	// Check if buffer is too large, don't return to pool if it exceeds max size
	if buf.Cap() > p.config.MaxSize {
		p.logger.Debug("buffer too large, not returning to pool",
			zap.Int("capacity", buf.Cap()),
			zap.Int("max_size", p.config.MaxSize))
		return
	}

	p.pool.Put(buf)

	p.mu.Lock()
	p.activeBuffers--
	p.mu.Unlock()

	p.logger.Debug("buffer returned to pool", zap.Int64("active", p.activeBuffers))
}

// GetActiveBuffers returns the number of active buffers
func (p *Pool) GetActiveBuffers() int64 {
	p.mu.RLock()
	defer p.mu.RUnlock()
	return p.activeBuffers
}

// GetTotalBuffers returns the total number of buffers created
func (p *Pool) GetTotalBuffers() int64 {
	p.mu.RLock()
	defer p.mu.RUnlock()
	return p.totalBuffers
}
