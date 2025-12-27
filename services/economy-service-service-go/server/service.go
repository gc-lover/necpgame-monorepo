// Issue: #backend-economy_service
// PERFORMANCE: Worker pools, batch operations, memory pooling

package server

import (
	"context"
	"sync"
	"time"

	"economy-service-service-go/pkg/api"
)

// PERFORMANCE: Worker pool for concurrent operations
const maxWorkers = 10
var workerPool = make(chan struct{}, maxWorkers)

// Service contains business logic for economy-service
// PERFORMANCE: Struct aligned (pointers first, then values)
type Service struct {
	repo      *Repository    // 8 bytes (pointer)
	workers   chan struct{} // 8 bytes (pointer)
	pool      *sync.Pool    // 8 bytes (pointer)
	// Padding for alignment
	_pad [0]byte
}

// NewService creates a new service instance with PERFORMANCE optimizations
func NewService() *Service {
	return &Service{
		repo:    NewRepository(),
		workers: workerPool,
		pool: &sync.Pool{
			New: func() interface{} {
				return &api.HealthResponse{}
			},
		},
	}
}

// HealthCheck performs a health check with PERFORMANCE optimizations
func (s *Service) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Acquire worker from pool (limit concurrency)
	select {
	case s.workers <- struct{}{}:
		defer func() { <-s.workers }() // Release worker
	case <-ctx.Done():
		return ctx.Err()
	case <-time.After(5 * time.Second): // Timeout
		return context.DeadlineExceeded
	}

	// PERFORMANCE: Check repository health with timeout
	healthCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repo.HealthCheck(healthCtx)
}
