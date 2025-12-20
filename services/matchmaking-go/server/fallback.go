// Package server Issue: #1588 - Fallback Strategy for graceful degradation
package server

import (
	"context"
	"errors"
	"log"
	"time"

	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/matchmaking-go/pkg/api"
)

var (
	_ = errors.New("all data sources failed, using fallback")
)

// FallbackStrategy implements fallback logic: primary → cache → default
type FallbackStrategy struct {
	repo  *Repository
	cache *CacheManager
}

// NewFallbackStrategy creates a new fallback strategy
func NewFallbackStrategy(repo *Repository, cache *CacheManager) *FallbackStrategy {
	return &FallbackStrategy{
		repo:  repo,
		cache: cache,
	}
}

// GetQueueStatusWithFallback tries primary DB, then cache, then default response
func (fs *FallbackStrategy) GetQueueStatusWithFallback(ctx context.Context, queueID uuid.UUID) (*api.QueueStatusResponse, error) {
	// Primary: Try DB query
	entry, err := fs.repo.GetQueueEntry(ctx, queueID)
	if err == nil && entry != nil {
		// Success: build response
		resp := &api.QueueStatusResponse{
			QueueId:     entry.ID,
			Status:      api.QueueStatusResponseStatus(entry.Status),
			TimeInQueue: int32(time.Since(entry.EnteredAt).Seconds()),
		}
		// Cache for future fallback
		fs.cache.CacheQueueStatus(ctx, queueID, resp, 5*time.Minute)
		return resp, nil
	}

	// Fallback 1: Try cache (stale OK)
	cached, err := fs.cache.GetQueueStatus(ctx, queueID)
	if err == nil && cached != nil {
		log.Printf("Using stale cache for queue %s (DB unavailable)", queueID)
		return cached, nil
	}

	// Fallback 2: Return default response (degraded mode)
	log.Printf("All sources failed for queue %s, returning default", queueID)
	return &api.QueueStatusResponse{
		QueueId:           queueID,
		Status:            api.QueueStatusResponseStatusWaiting,
		TimeInQueue:       0,
		EstimatedWaitTime: api.NewOptInt32(30), // Default 30s
		RatingRange:       []int32{1400, 1600}, // Default range
	}, nil
}

// GetPlayerRatingWithFallback tries primary DB, then cache, then default rating
func (fs *FallbackStrategy) GetPlayerRatingWithFallback(ctx context.Context, playerID uuid.UUID, activityType string) (int, error) {
	// Primary: Try DB query
	rating, err := fs.repo.GetPlayerRating(ctx, playerID, activityType)
	if err == nil {
		// Cache for future fallback
		fs.cache.CachePlayerRating(ctx, playerID, activityType, rating, 10*time.Minute)
		return rating, nil
	}

	// Fallback 1: Try cache (stale OK)
	cached, err := fs.cache.GetCachedPlayerRating(ctx, playerID, activityType)
	if err == nil {
		log.Printf("Using stale cache for player %s rating (DB unavailable)", playerID)
		return cached, nil
	}

	// Fallback 2: Return default rating (1500 MMR)
	log.Printf("All sources failed for player %s rating, using default 1500", playerID)
	return 1500, nil
}
