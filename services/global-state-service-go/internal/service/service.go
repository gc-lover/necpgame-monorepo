package service

import (
	"context"
	"crypto/md5"
	"encoding/json"
	"fmt"
	"sync"
	"time"

	"global-state-service-go/internal/repository"
)

// Service handles business logic for global state management
type Service struct {
	repo            *repository.Repository
	stateCache      *StateCache
	eventBuffer     *EventBuffer
	memoryPools     *MemoryPools
	circuitBreaker  *CircuitBreaker
}

// StateCache provides multi-level caching for state data
type StateCache struct {
	l1Cache map[string]*repository.AggregateState // In-memory L1 cache
	l1Mutex sync.RWMutex
	// L2 and L3 would be Redis/PostgreSQL in production
}

// EventBuffer buffers events for batch processing
type EventBuffer struct {
	events []*repository.GameEvent
	mutex  sync.Mutex
	size   int
}

// MemoryPools provides zero-allocation memory pools
type MemoryPools struct {
	statePool   *sync.Pool
	eventPool   *sync.Pool
	bufferPool  *sync.Pool
}

// CircuitBreaker provides resilience against cascading failures
type CircuitBreaker struct {
	failures    int
	lastFailure time.Time
	threshold   int
	timeout     time.Duration
}

// NewService creates a new service instance with MMOFPS optimizations
func NewService(repo *repository.Repository) *Service {
	return &Service{
		repo: repo,
		stateCache: &StateCache{
			l1Cache: make(map[string]*repository.AggregateState),
		},
		eventBuffer: &EventBuffer{
			events: make([]*repository.GameEvent, 0, 1000),
			size:   1000,
		},
		memoryPools: &MemoryPools{
			statePool: &sync.Pool{
				New: func() interface{} {
					return &repository.AggregateState{
						Data: make(map[string]interface{}),
					}
				},
			},
			eventPool: &sync.Pool{
				New: func() interface{} {
					return &repository.GameEvent{
						EventData:    make(map[string]interface{}),
						Metadata:     make(map[string]interface{}),
						StateChanges: make(map[string]interface{}),
					}
				},
			},
			bufferPool: &sync.Pool{
				New: func() interface{} {
					return make([]*repository.GameEvent, 0, 100)
				},
			},
		},
		circuitBreaker: &CircuitBreaker{
			threshold: 5,
			timeout:   30 * time.Second,
		},
	}
}

// GetAggregateState retrieves current state with caching optimization
func (s *Service) GetAggregateState(ctx context.Context, aggregateType, aggregateID string, version *int64, includeEvents bool) (*repository.AggregateState, []*repository.GameEvent, error) {
	// Create timeout context for MMOFPS performance
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Check circuit breaker
	if !s.circuitBreaker.Allow() {
		return nil, nil, fmt.Errorf("circuit breaker open: service temporarily unavailable")
	}

	// Check L1 cache first
	cacheKey := fmt.Sprintf("%s:%s", aggregateType, aggregateID)
	s.stateCache.l1Mutex.RLock()
	if cached, exists := s.stateCache.l1Cache[cacheKey]; exists {
		s.stateCache.l1Mutex.RUnlock()
		var events []*repository.GameEvent
		if includeEvents {
			var err error
			events, err = s.repo.GetAggregateEvents(ctx, aggregateType, aggregateID, nil, nil, 100)
			if err != nil {
				s.recordFailure()
				return nil, nil, fmt.Errorf("failed to get events: %w", err)
			}
		}
		return cached, events, nil
	}
	s.stateCache.l1Mutex.RUnlock()

	// Get from repository
	state, err := s.repo.GetAggregateState(ctx, aggregateType, aggregateID, version)
	if err != nil {
		s.recordFailure()
		return nil, nil, fmt.Errorf("failed to get aggregate state: %w", err)
	}

	// Cache the result (L1)
	s.stateCache.l1Mutex.Lock()
	s.stateCache.l1Cache[cacheKey] = state
	s.stateCache.l1Mutex.Unlock()

	var events []*repository.GameEvent
	if includeEvents {
		events, err = s.repo.GetAggregateEvents(ctx, aggregateType, aggregateID, nil, nil, 100)
		if err != nil {
			s.recordFailure()
			return nil, nil, fmt.Errorf("failed to get events: %w", err)
		}
	}

	return state, events, nil
}

// UpdateAggregateState updates state with optimistic locking and event publishing
func (s *Service) UpdateAggregateState(ctx context.Context, aggregateType, aggregateID string, changes map[string]interface{}, expectedVersion int64, userID string) (*repository.AggregateState, error) {
	// Create timeout context
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Check circuit breaker
	if !s.circuitBreaker.Allow() {
		return nil, fmt.Errorf("circuit breaker open: service temporarily unavailable")
	}

	// Get current state
	currentState, _, err := s.GetAggregateState(ctx, aggregateType, aggregateID, nil, false)
	if err != nil {
		return nil, fmt.Errorf("failed to get current state: %w", err)
	}

	// Check version for optimistic locking
	if currentState.Version != expectedVersion {
		return nil, fmt.Errorf("version conflict: expected %d, got %d", expectedVersion, currentState.Version)
	}

	// Apply changes
	for key, value := range changes {
		currentState.Data[key] = value
	}

	// Update version
	currentState.Version++
	currentState.LastModified = time.Now()

	// Calculate checksum
	checksum := s.calculateChecksum(currentState.Data)
	currentState.Checksum = checksum

	// Update in repository
	err = s.repo.UpdateAggregateState(ctx, currentState, expectedVersion)
	if err != nil {
		s.recordFailure()
		return nil, fmt.Errorf("failed to update state: %w", err)
	}

	// Publish state change event
	event := s.createStateChangeEvent(currentState, changes, userID)
	if err := s.PublishEvent(ctx, event); err != nil {
		// Log error but don't fail the operation
		fmt.Printf("Failed to publish event: %v\n", err)
	}

	// Update cache
	cacheKey := fmt.Sprintf("%s:%s", aggregateType, aggregateID)
	s.stateCache.l1Mutex.Lock()
	s.stateCache.l1Cache[cacheKey] = currentState
	s.stateCache.l1Mutex.Unlock()

	return currentState, nil
}

// PublishEvent publishes an event with buffering for performance
func (s *Service) PublishEvent(ctx context.Context, event *repository.GameEvent) error {
	// Create timeout context
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	// Check circuit breaker
	if !s.circuitBreaker.Allow() {
		return fmt.Errorf("circuit breaker open: service temporarily unavailable")
	}

	// Buffer events for batch processing
	s.eventBuffer.mutex.Lock()
	s.eventBuffer.events = append(s.eventBuffer.events, event)

	// Flush buffer if full
	shouldFlush := len(s.eventBuffer.events) >= s.eventBuffer.size
	s.eventBuffer.mutex.Unlock()

	if shouldFlush {
		return s.flushEventBuffer(ctx)
	}

	return nil
}

// flushEventBuffer processes buffered events in batch
func (s *Service) flushEventBuffer(ctx context.Context) error {
	s.eventBuffer.mutex.Lock()
	events := s.eventBuffer.events
	s.eventBuffer.events = s.memoryPools.bufferPool.Get().([]*repository.GameEvent)[:0]
	s.eventBuffer.mutex.Unlock()

	// Process events in batch
	for _, event := range events {
		if err := s.repo.PublishEvent(ctx, event); err != nil {
			s.recordFailure()
			return fmt.Errorf("failed to publish event %s: %w", event.EventID, err)
		}
	}

	// Return buffers to pool
	s.memoryPools.bufferPool.Put(events[:0])

	return nil
}

// GetAggregateEvents retrieves event history with pagination
func (s *Service) GetAggregateEvents(ctx context.Context, aggregateType, aggregateID string, fromVersion, toVersion *int64, limit int) ([]*repository.GameEvent, error) {
	// Create timeout context
	ctx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	events, err := s.repo.GetAggregateEvents(ctx, aggregateType, aggregateID, fromVersion, toVersion, limit)
	if err != nil {
		s.recordFailure()
		return nil, fmt.Errorf("failed to get aggregate events: %w", err)
	}

	return events, nil
}

// GetStateAnalytics provides analytics about state changes
func (s *Service) GetStateAnalytics(ctx context.Context, aggregateType *string, timeRange string, groupBy string) (map[string]interface{}, error) {
	// Create timeout context
	ctx, cancel := context.WithTimeout(ctx, 500*time.Millisecond)
	defer cancel()

	analytics, err := s.repo.GetStateAnalytics(ctx, aggregateType, timeRange, groupBy)
	if err != nil {
		s.recordFailure()
		return nil, fmt.Errorf("failed to get state analytics: %w", err)
	}

	return analytics, nil
}

// Helper methods

func (s *Service) calculateChecksum(data map[string]interface{}) string {
	dataBytes, _ := json.Marshal(data)
	return fmt.Sprintf("%x", md5.Sum(dataBytes))
}

func (s *Service) createStateChangeEvent(state *repository.AggregateState, changes map[string]interface{}, userID string) *repository.GameEvent {
	now := time.Now()
	return &repository.GameEvent{
		EventID:       generateUUID(),
		EventType:     "StateChanged",
		AggregateType: state.AggregateType,
		AggregateID:   state.AggregateID,
		EventVersion:  state.Version,
		EventData: map[string]interface{}{
			"changes": changes,
			"new_version": state.Version,
		},
		Metadata: map[string]interface{}{
			"user_id": userID,
			"checksum": state.Checksum,
		},
		ServerID:    "global-state-service",
		Timestamp:   now,
		ProcessedAt: &now,
		StateChanges: changes,
	}
}

func (s *Service) recordFailure() {
	s.circuitBreaker.recordFailure()
}

func (cb *CircuitBreaker) Allow() bool {
	if cb.failures >= cb.threshold {
		if time.Since(cb.lastFailure) < cb.timeout {
			return false
		}
		// Reset after timeout
		cb.failures = 0
	}
	return true
}

func (cb *CircuitBreaker) recordFailure() {
	cb.failures++
	cb.lastFailure = time.Now()
}

// generateUUID generates a simple UUID for events (in production, use proper UUID library)
func generateUUID() string {
	return fmt.Sprintf("%d", time.Now().UnixNano())
}