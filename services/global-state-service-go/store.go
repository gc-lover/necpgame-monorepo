// Issue: #53
package main

import (
	"context"
	"errors"
	"sync"
	"time"
)

var errConflict = errors.New("version conflict")

type stateStore interface {
	get(ctx context.Context, key string) (stateEntry, bool, error)
	list(ctx context.Context, categories []string) ([]stateEntry, error)
	upsert(ctx context.Context, req stateMutationRequest) (stateEntry, error)
	upsertBatch(ctx context.Context, reqs []stateMutationRequest) ([]stateEntry, error)
}

type inMemoryStateStore struct {
	mu    sync.RWMutex
	items map[string]stateEntry
}

func newInMemoryStateStore() *inMemoryStateStore {
	return &inMemoryStateStore{items: make(map[string]stateEntry)}
}

func (s *inMemoryStateStore) get(ctx context.Context, key string) (stateEntry, bool, error) {
	select {
	case <-ctx.Done():
		return stateEntry{}, false, ctx.Err()
	default:
	}
	s.mu.RLock()
	defer s.mu.RUnlock()
	entry, ok := s.items[key]
	return entry, ok, nil
}

func (s *inMemoryStateStore) list(ctx context.Context, categories []string) ([]stateEntry, error) {
	select {
	case <-ctx.Done():
		return nil, ctx.Err()
	default:
	}
	s.mu.RLock()
	defer s.mu.RUnlock()
	if len(categories) == 0 {
		result := make([]stateEntry, 0, len(s.items))
		for _, v := range s.items {
			result = append(result, v)
		}
		return result, nil
	}
	filter := make(map[string]struct{}, len(categories))
	for _, c := range categories {
		filter[c] = struct{}{}
	}
	var result []stateEntry
	for _, v := range s.items {
		if _, ok := filter[v.Category]; ok {
			result = append(result, v)
		}
	}
	return result, nil
}

func (s *inMemoryStateStore) upsert(ctx context.Context, req stateMutationRequest) (stateEntry, error) {
	select {
	case <-ctx.Done():
		return stateEntry{}, ctx.Err()
	default:
	}
	now := time.Now().UTC()
	s.mu.Lock()
	defer s.mu.Unlock()

	current, ok := s.items[req.Key]
	if req.ExpectedVersion != nil {
		actual := uint64(0)
		if ok {
			actual = current.Version
		}
		if actual != *req.ExpectedVersion {
			return stateEntry{}, errConflict
		}
	}
	nextVersion := uint64(1)
	if ok {
		nextVersion = current.Version + 1
	}
	clone := make([]byte, len(req.Value))
	copy(clone, req.Value)
	entry := stateEntry{
		Key:       req.Key,
		Category:  req.Category,
		Value:     clone,
		Version:   nextVersion,
		UpdatedAt: now,
	}
	s.items[req.Key] = entry
	return entry, nil
}

func (s *inMemoryStateStore) upsertBatch(ctx context.Context, reqs []stateMutationRequest) ([]stateEntry, error) {
	select {
	case <-ctx.Done():
		return nil, ctx.Err()
	default:
	}
	now := time.Now().UTC()
	s.mu.Lock()
	defer s.mu.Unlock()

	results := make([]stateEntry, 0, len(reqs))
	for _, req := range reqs {
		current, ok := s.items[req.Key]
		if req.ExpectedVersion != nil {
			actual := uint64(0)
			if ok {
				actual = current.Version
			}
			if actual != *req.ExpectedVersion {
				return nil, errConflict
			}
		}
		nextVersion := uint64(1)
		if ok {
			nextVersion = current.Version + 1
		}
		clone := make([]byte, len(req.Value))
		copy(clone, req.Value)
		entry := stateEntry{
			Key:       req.Key,
			Category:  req.Category,
			Value:     clone,
			Version:   nextVersion,
			UpdatedAt: now,
		}
		results = append(results, entry)
	}
	for _, entry := range results {
		s.items[entry.Key] = entry
	}
	return results, nil
}

















