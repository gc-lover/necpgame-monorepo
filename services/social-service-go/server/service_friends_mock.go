package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/friends"
)

type MockFriendsService struct{}

func NewMockFriendsService() *MockFriendsService {
	return &MockFriendsService{}
}

func (s *MockFriendsService) GetFriends(ctx context.Context, limit, offset int, onlineOnly bool) ([]friends.Friendship, int, error) {
	return []friends.Friendship{}, 0, nil
}

func (s *MockFriendsService) GetFriendsCount(ctx context.Context) (int, int, error) {
	return 0, 0, nil
}

func (s *MockFriendsService) GetFriend(ctx context.Context, friendID uuid.UUID) (*friends.Friendship, error) {
	return &friends.Friendship{}, nil
}

func (s *MockFriendsService) RemoveFriend(ctx context.Context, friendID uuid.UUID) error {
	return nil
}

