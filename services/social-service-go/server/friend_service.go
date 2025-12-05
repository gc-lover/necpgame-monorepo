// Issue: Social Service ogen Migration - Friends Service
package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type FriendServiceInterface interface {
	GetFriends(ctx context.Context, characterID uuid.UUID, onlineOnly bool, limit, offset int) (*models.FriendListResponse, error)
	GetFriend(ctx context.Context, characterID, friendID uuid.UUID) (*models.Friendship, error)
	GetFriendsCount(ctx context.Context, characterID uuid.UUID) (int, error)
	GetOnlineFriends(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.FriendListResponse, error)
	RemoveFriend(ctx context.Context, characterID, friendID uuid.UUID) error
}

type FriendService struct {
	repo   FriendRepositoryInterface
	logger *logrus.Logger
}

func NewFriendService(db *pgxpool.Pool, logger *logrus.Logger) *FriendService {
	return &FriendService{
		repo:   NewFriendRepository(db),
		logger: logger,
	}
}

// GetFriends returns list of friends with caching support
// Hot path: 2k RPS - requires 3-tier caching (memory → Redis → DB)
func (s *FriendService) GetFriends(ctx context.Context, characterID uuid.UUID, onlineOnly bool, limit, offset int) (*models.FriendListResponse, error) {
	// TODO: 3-tier caching:
	// 1. Memory cache (sync.Map) - TTL: 5s
	// 2. Redis cache - TTL: 5min
	// 3. DB query (current implementation)

	// Validate pagination
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	friendships, err := s.repo.GetFriends(ctx, characterID, onlineOnly, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get friends: %w", err)
	}

	// TODO: Filter online friends using Redis ZSET if onlineOnly=true
	// For now, repository returns all friends

	total, err := s.repo.GetFriendsCount(ctx, characterID)
	if err != nil {
		return nil, fmt.Errorf("failed to get friends count: %w", err)
	}

	return &models.FriendListResponse{
		Friends: friendships,
		Total:   total,
	}, nil
}

// GetFriend returns specific friendship
func (s *FriendService) GetFriend(ctx context.Context, characterID, friendID uuid.UUID) (*models.Friendship, error) {
	// TODO: Check cache first
	friendship, err := s.repo.GetFriend(ctx, characterID, friendID)
	if err != nil {
		return nil, fmt.Errorf("failed to get friend: %w", err)
	}

	if friendship == nil {
		return nil, nil
	}

	// Verify friendship belongs to character
	if friendship.CharacterAID != characterID && friendship.CharacterBID != characterID {
		return nil, fmt.Errorf("friendship not found")
	}

	return friendship, nil
}

// GetFriendsCount returns total count of accepted friends
// TODO: Cache count in Redis (5 min TTL), update on friend add/remove
func (s *FriendService) GetFriendsCount(ctx context.Context, characterID uuid.UUID) (int, error) {
	count, err := s.repo.GetFriendsCount(ctx, characterID)
	if err != nil {
		return 0, fmt.Errorf("failed to get friends count: %w", err)
	}
	return count, nil
}

// GetOnlineFriends returns online friends
// TODO: Redis ZSET for online users (sorted by last_seen)
// No DB queries for online check - all from Redis
func (s *FriendService) GetOnlineFriends(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.FriendListResponse, error) {
	// Validate pagination
	if limit <= 0 {
		limit = 50
	}
	if limit > 100 {
		limit = 100
	}
	if offset < 0 {
		offset = 0
	}

	friendships, err := s.repo.GetOnlineFriends(ctx, characterID, limit, offset)
	if err != nil {
		return nil, fmt.Errorf("failed to get online friends: %w", err)
	}

	// TODO: Filter by online status from Redis
	// For now, return all friends (will be filtered when Redis integration is added)

	total, err := s.repo.GetFriendsCount(ctx, characterID)
	if err != nil {
		return nil, fmt.Errorf("failed to get friends count: %w", err)
	}

	return &models.FriendListResponse{
		Friends: friendships,
		Total:   total,
	}, nil
}

// RemoveFriend removes friendship (soft delete)
// TODO: Transaction for consistency, invalidate cache, notify via WebSocket
func (s *FriendService) RemoveFriend(ctx context.Context, characterID, friendID uuid.UUID) error {
	if characterID == friendID {
		return fmt.Errorf("cannot remove self as friend")
	}

	err := s.repo.RemoveFriend(ctx, characterID, friendID)
	if err != nil {
		return fmt.Errorf("failed to remove friend: %w", err)
	}

	// TODO: Invalidate cache
	// TODO: Notify other player via WebSocket

	return nil
}

