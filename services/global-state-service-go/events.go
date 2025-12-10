// Issue: #53
package main

import (
	"context"
	"sync"
)

type eventStore interface {
	save(ctx context.Context, evt stateEvent) error
	list(ctx context.Context, limit int) ([]stateEvent, error)
}

type inMemoryEventStore struct {
	max int
	mu  sync.RWMutex
	buf []stateEvent
}

func newInMemoryEventStore(max int) *inMemoryEventStore {
	return &inMemoryEventStore{max: max, buf: make([]stateEvent, 0, max)}
}

func (s *inMemoryEventStore) save(ctx context.Context, evt stateEvent) error {
	select {
	case <-ctx.Done():
		return ctx.Err()
	default:
	}
	s.mu.Lock()
	defer s.mu.Unlock()
	s.buf = append([]stateEvent{evt}, s.buf...)
	if len(s.buf) > s.max {
		s.buf = s.buf[:s.max]
	}
	return nil
}

func (s *inMemoryEventStore) list(ctx context.Context, limit int) ([]stateEvent, error) {
	select {
	case <-ctx.Done():
		return nil, ctx.Err()
	default:
	}
	if limit <= 0 {
		return []stateEvent{}, nil
	}
	s.mu.RLock()
	defer s.mu.RUnlock()
	if limit > len(s.buf) {
		limit = len(s.buf)
	}
	result := make([]stateEvent, limit)
	copy(result, s.buf[:limit])
	return result, nil
}






