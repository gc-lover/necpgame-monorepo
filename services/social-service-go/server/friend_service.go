package server

import (
	"context"
	"errors"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
)

type FriendService struct {
	friendRepo        FriendRepositoryInterface
	notificationRepo NotificationRepositoryInterface
	eventBus          EventBus
}

func NewFriendService(
	friendRepo FriendRepositoryInterface,
	notificationRepo NotificationRepositoryInterface,
	eventBus EventBus,
) *FriendService {
	return &FriendService{
		friendRepo:        friendRepo,
		notificationRepo: notificationRepo,
		eventBus:          eventBus,
	}
}

func (s *FriendService) SendFriendRequest(ctx context.Context, fromCharacterID uuid.UUID, req *models.SendFriendRequestRequest) (*models.Friendship, error) {
	if fromCharacterID == req.ToCharacterID {
		return nil, errors.New("cannot send friend request to yourself")
	}

	existing, err := s.friendRepo.GetFriendship(ctx, fromCharacterID, req.ToCharacterID)
	if err != nil {
		return nil, err
	}
	if existing != nil {
		if existing.Status == models.FriendshipStatusAccepted {
			return nil, errors.New("already friends")
		}
		if existing.Status == models.FriendshipStatusPending {
			return nil, errors.New("friend request already pending")
		}
		if existing.Status == models.FriendshipStatusBlocked {
			return nil, errors.New("cannot send friend request to blocked user")
		}
	}

	friendship, err := s.friendRepo.CreateRequest(ctx, fromCharacterID, req.ToCharacterID)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"friendship_id":    friendship.ID.String(),
			"from_character_id": fromCharacterID.String(),
			"to_character_id":   req.ToCharacterID.String(),
			"status":           string(friendship.Status),
			"timestamp":        time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "friend:request-sent", payload)
	}

	if s.notificationRepo != nil {
		notificationReq := &models.CreateNotificationRequest{
			AccountID: req.ToCharacterID,
			Type:      models.NotificationTypeFriend,
			Priority:  models.NotificationPriorityMedium,
			Title:     "New Friend Request",
			Content:   "You have received a friend request",
			Data: map[string]interface{}{
				"friendship_id":     friendship.ID.String(),
				"from_character_id": fromCharacterID.String(),
			},
			Channels: []models.DeliveryChannel{models.DeliveryChannelInGame, models.DeliveryChannelWebSocket},
		}
		s.notificationRepo.Create(ctx, &models.Notification{
			ID:        uuid.New(),
			AccountID: notificationReq.AccountID,
			Type:      notificationReq.Type,
			Priority:  notificationReq.Priority,
			Title:     notificationReq.Title,
			Content:   notificationReq.Content,
			Data:      notificationReq.Data,
			Status:    models.NotificationStatusUnread,
			Channels:  notificationReq.Channels,
			CreatedAt: time.Now(),
		})
	}

	return friendship, nil
}

func (s *FriendService) AcceptFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) (*models.Friendship, error) {
	friendship, err := s.friendRepo.GetByID(ctx, requestID)
	if err != nil {
		return nil, err
	}
	if friendship == nil {
		return nil, errors.New("friend request not found")
	}

	if friendship.CharacterAID != characterID && friendship.CharacterBID != characterID {
		return nil, errors.New("not authorized to accept this request")
	}

	if friendship.Status != models.FriendshipStatusPending {
		return nil, errors.New("friend request is not pending")
	}

	accepted, err := s.friendRepo.AcceptRequest(ctx, requestID)
	if err != nil {
		return nil, err
	}
	if accepted == nil {
		return nil, errors.New("failed to accept friend request")
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"friendship_id":   accepted.ID.String(),
			"character_a_id":  accepted.CharacterAID.String(),
			"character_b_id":  accepted.CharacterBID.String(),
			"initiator_id":    accepted.InitiatorID.String(),
			"status":          string(accepted.Status),
			"timestamp":       time.Now().Format(time.RFC3339),
		}
		s.eventBus.PublishEvent(ctx, "friend:request-accepted", payload)
	}

	return accepted, nil
}

func (s *FriendService) RejectFriendRequest(ctx context.Context, characterID uuid.UUID, requestID uuid.UUID) error {
	friendship, err := s.friendRepo.GetByID(ctx, requestID)
	if err != nil {
		return err
	}
	if friendship == nil {
		return errors.New("friend request not found")
	}

	if friendship.CharacterAID != characterID && friendship.CharacterBID != characterID {
		return errors.New("not authorized to reject this request")
	}

	return s.friendRepo.Delete(ctx, requestID)
}

func (s *FriendService) RemoveFriend(ctx context.Context, characterID uuid.UUID, friendID uuid.UUID) error {
	friendship, err := s.friendRepo.GetFriendship(ctx, characterID, friendID)
	if err != nil {
		return err
	}
	if friendship == nil {
		return errors.New("friendship not found")
	}

	if friendship.Status != models.FriendshipStatusAccepted {
		return errors.New("not friends")
	}

	return s.friendRepo.Delete(ctx, friendship.ID)
}

func (s *FriendService) BlockFriend(ctx context.Context, characterID uuid.UUID, targetID uuid.UUID) (*models.Friendship, error) {
	if characterID == targetID {
		return nil, errors.New("cannot block yourself")
	}

	friendship, err := s.friendRepo.GetFriendship(ctx, characterID, targetID)
	if err != nil {
		return nil, err
	}

	if friendship == nil {
		friendship, err = s.friendRepo.CreateRequest(ctx, characterID, targetID)
		if err != nil {
			return nil, err
		}
	}

	blocked, err := s.friendRepo.Block(ctx, friendship.ID)
	if err != nil {
		return nil, err
	}
	if blocked == nil {
		return nil, errors.New("failed to block friend")
	}

	return blocked, nil
}

func (s *FriendService) GetFriends(ctx context.Context, characterID uuid.UUID) (*models.FriendListResponse, error) {
	friendships, err := s.friendRepo.GetByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return &models.FriendListResponse{
		Friends: friendships,
		Total:   len(friendships),
	}, nil
}

func (s *FriendService) GetFriendRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	return s.friendRepo.GetPendingRequests(ctx, characterID)
}

