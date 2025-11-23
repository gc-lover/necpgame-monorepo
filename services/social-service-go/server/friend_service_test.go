package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockFriendRepository struct {
	mock.Mock
}

func (m *mockFriendRepository) CreateRequest(ctx context.Context, fromCharacterID, toCharacterID uuid.UUID) (*models.Friendship, error) {
	args := m.Called(ctx, fromCharacterID, toCharacterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) GetByCharacterID(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	args := m.Called(ctx, characterID)
	return args.Get(0).([]models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) GetPendingRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	args := m.Called(ctx, characterID)
	return args.Get(0).([]models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) GetFriendship(ctx context.Context, characterAID, characterBID uuid.UUID) (*models.Friendship, error) {
	args := m.Called(ctx, characterAID, characterBID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) AcceptRequest(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) Block(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	args := m.Called(ctx, id)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Friendship), args.Error(1)
}

func (m *mockFriendRepository) Delete(ctx context.Context, id uuid.UUID) error {
	args := m.Called(ctx, id)
	return args.Error(0)
}

func TestSocialService_SendFriendRequest_Success(t *testing.T) {
	mockFriendRepo := new(mockFriendRepository)
	mockNotificationRepo := new(mockNotificationRepository)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{})
	service := &SocialService{
		friendRepo:      mockFriendRepo,
		notificationRepo: mockNotificationRepo,
		eventBus:        mockEventBus,
		cache:           redisClient,
		logger:          GetLogger(),
	}

	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	req := &models.SendFriendRequestRequest{
		ToCharacterID: toCharacterID,
	}

	friendship := &models.Friendship{
		ID:          uuid.New(),
		CharacterAID: fromCharacterID,
		CharacterBID: toCharacterID,
		Status:      models.FriendshipStatusPending,
		InitiatorID: fromCharacterID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockFriendRepo.On("GetFriendship", mock.Anything, fromCharacterID, toCharacterID).Return(nil, nil)
	mockFriendRepo.On("CreateRequest", mock.Anything, fromCharacterID, toCharacterID).Return(friendship, nil)
	mockEventBus.On("PublishEvent", mock.Anything, "friend:request-sent", mock.Anything).Return(nil)
	mockNotificationRepo.On("Create", mock.Anything, mock.AnythingOfType("*models.Notification")).Return(&models.Notification{}, nil)

	ctx := context.Background()
	result, err := service.SendFriendRequest(ctx, fromCharacterID, req)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, friendship.ID, result.ID)
	mockFriendRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestSocialService_SendFriendRequest_AlreadyFriends(t *testing.T) {
	mockFriendRepo := new(mockFriendRepository)
	redisClient := redis.NewClient(&redis.Options{})
	service := &SocialService{
		friendRepo: mockFriendRepo,
		cache:      redisClient,
		logger:     GetLogger(),
	}

	fromCharacterID := uuid.New()
	toCharacterID := uuid.New()

	req := &models.SendFriendRequestRequest{
		ToCharacterID: toCharacterID,
	}

	existingFriendship := &models.Friendship{
		ID:          uuid.New(),
		CharacterAID: fromCharacterID,
		CharacterBID: toCharacterID,
		Status:      models.FriendshipStatusAccepted,
		InitiatorID: fromCharacterID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockFriendRepo.On("GetFriendship", mock.Anything, fromCharacterID, toCharacterID).Return(existingFriendship, nil)

	ctx := context.Background()
	result, err := service.SendFriendRequest(ctx, fromCharacterID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "already friends")
	mockFriendRepo.AssertExpectations(t)
}

func TestSocialService_AcceptFriendRequest_Success(t *testing.T) {
	mockFriendRepo := new(mockFriendRepository)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{})
	service := &SocialService{
		friendRepo: mockFriendRepo,
		eventBus:   mockEventBus,
		cache:      redisClient,
		logger:     GetLogger(),
	}

	characterID := uuid.New()
	requestID := uuid.New()
	fromCharacterID := uuid.New()

	pendingFriendship := &models.Friendship{
		ID:          requestID,
		CharacterAID: characterID,
		CharacterBID: fromCharacterID,
		Status:      models.FriendshipStatusPending,
		InitiatorID: fromCharacterID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	acceptedFriendship := &models.Friendship{
		ID:          requestID,
		CharacterAID: characterID,
		CharacterBID: fromCharacterID,
		Status:      models.FriendshipStatusAccepted,
		InitiatorID: fromCharacterID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockFriendRepo.On("GetByID", mock.Anything, requestID).Return(pendingFriendship, nil)
	mockFriendRepo.On("AcceptRequest", mock.Anything, requestID).Return(acceptedFriendship, nil)
	mockEventBus.On("PublishEvent", mock.Anything, "friend:request-accepted", mock.Anything).Return(nil)

	ctx := context.Background()
	result, err := service.AcceptFriendRequest(ctx, characterID, requestID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, models.FriendshipStatusAccepted, result.Status)
	mockFriendRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestSocialService_GetFriends_Success(t *testing.T) {
	mockFriendRepo := new(mockFriendRepository)

	redisClient := redis.NewClient(&redis.Options{})
	service := &SocialService{
		friendRepo: mockFriendRepo,
		cache:      redisClient,
		logger:     GetLogger(),
	}

	characterID := uuid.New()
	friendships := []models.Friendship{
		{
			ID:          uuid.New(),
			CharacterAID: characterID,
			CharacterBID: uuid.New(),
			Status:      models.FriendshipStatusAccepted,
			InitiatorID: characterID,
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockFriendRepo.On("GetByCharacterID", mock.Anything, characterID).Return(friendships, nil)

	ctx := context.Background()
	result, err := service.GetFriends(ctx, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, 1, result.Total)
	assert.Equal(t, 1, len(result.Friends))
	mockFriendRepo.AssertExpectations(t)
}

func TestSocialService_GetFriendRequests_Success(t *testing.T) {
	mockFriendRepo := new(mockFriendRepository)

	redisClient := redis.NewClient(&redis.Options{})
	service := &SocialService{
		friendRepo: mockFriendRepo,
		cache:      redisClient,
		logger:     GetLogger(),
	}

	characterID := uuid.New()
	requests := []models.Friendship{
		{
			ID:          uuid.New(),
			CharacterAID: characterID,
			CharacterBID: uuid.New(),
			Status:      models.FriendshipStatusPending,
			InitiatorID: uuid.New(),
			CreatedAt:   time.Now(),
			UpdatedAt:   time.Now(),
		},
	}

	mockFriendRepo.On("GetPendingRequests", mock.Anything, characterID).Return(requests, nil)

	ctx := context.Background()
	result, err := service.GetFriendRequests(ctx, characterID)

	assert.NoError(t, err)
	assert.Equal(t, 1, len(result))
	mockFriendRepo.AssertExpectations(t)
}

