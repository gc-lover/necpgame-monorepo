package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/friends"
)

type FriendsServiceInterface interface {
	GetFriends(ctx context.Context, limit, offset int, onlineOnly bool) ([]friends.Friendship, int, error)
	GetFriendsCount(ctx context.Context) (int, int, error)
	GetFriend(ctx context.Context, friendID uuid.UUID) (*friends.Friendship, error)
	RemoveFriend(ctx context.Context, friendID uuid.UUID) error
}

