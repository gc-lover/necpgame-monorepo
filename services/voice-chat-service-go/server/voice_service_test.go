// Issue: #140895495
package server

import (
	"context"
	"errors"
	"testing"

	"github.com/google/uuid"
	"github.com/necpgame/voice-chat-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockVoiceRepository struct {
	mock.Mock
}

func (m *mockVoiceRepository) CreateChannel(ctx context.Context, channel *models.VoiceChannel) error {
	args := m.Called(ctx, channel)
	return args.Error(0)
}

func (m *mockVoiceRepository) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error) {
	args := m.Called(ctx, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.VoiceChannel), args.Error(1)
}

func (m *mockVoiceRepository) ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) ([]models.VoiceChannel, error) {
	args := m.Called(ctx, channelType, ownerID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.VoiceChannel), args.Error(1)
}

func (m *mockVoiceRepository) AddParticipant(ctx context.Context, participant *models.VoiceParticipant) error {
	args := m.Called(ctx, participant)
	return args.Error(0)
}

func (m *mockVoiceRepository) RemoveParticipant(ctx context.Context, channelID, characterID uuid.UUID) error {
	args := m.Called(ctx, channelID, characterID)
	return args.Error(0)
}

func (m *mockVoiceRepository) GetParticipant(ctx context.Context, channelID, characterID uuid.UUID) (*models.VoiceParticipant, error) {
	args := m.Called(ctx, channelID, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.VoiceParticipant), args.Error(1)
}

func (m *mockVoiceRepository) ListParticipants(ctx context.Context, channelID uuid.UUID) ([]models.VoiceParticipant, error) {
	args := m.Called(ctx, channelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.VoiceParticipant), args.Error(1)
}

func (m *mockVoiceRepository) UpdateParticipantStatus(ctx context.Context, channelID, characterID uuid.UUID, status models.ParticipantStatus) error {
	args := m.Called(ctx, channelID, characterID, status)
	return args.Error(0)
}

func (m *mockVoiceRepository) UpdateParticipantPosition(ctx context.Context, channelID, characterID uuid.UUID, position map[string]interface{}) error {
	args := m.Called(ctx, channelID, characterID, position)
	return args.Error(0)
}

func (m *mockVoiceRepository) CountParticipants(ctx context.Context, channelID uuid.UUID) (int, error) {
	args := m.Called(ctx, channelID)
	return args.Int(0), args.Error(1)
}

func setupTestService(t *testing.T) (*VoiceService, *mockVoiceRepository, func()) {
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return nil, nil, nil
	}
	redisClient := redis.NewClient(redisOpts)

	mockRepo := new(mockVoiceRepository)
	service := &VoiceService{
		repo:      mockRepo,
		cache:     redisClient,
		logger:    GetLogger(),
		webrtcURL: "wss://webrtc.example.com",
		webrtcKey: "test-key",
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, cleanup
}

func TestVoiceService_CreateChannel_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.CreateChannelRequest{
		CharacterID:   characterID,
		Type:          models.VoiceChannelTypeParty,
		Name:          "Test Party",
		MaxMembers:    5,
		QualityPreset: "standard",
		Settings:      make(map[string]interface{}),
	}

	mockRepo.On("CreateChannel", mock.Anything, mock.AnythingOfType("*models.VoiceChannel")).Return(nil)

	ctx := context.Background()
	channel, err := service.CreateChannel(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, channel)
	assert.Equal(t, characterID, channel.OwnerID)
	assert.Equal(t, models.VoiceChannelTypeParty, channel.Type)
	assert.Equal(t, "Test Party", channel.Name)
	assert.Equal(t, 5, channel.MaxMembers)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_CreateChannel_DefaultMaxMembers(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.CreateChannelRequest{
		CharacterID: characterID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  0,
		Settings:    make(map[string]interface{}),
	}

	mockRepo.On("CreateChannel", mock.Anything, mock.AnythingOfType("*models.VoiceChannel")).Return(nil).Run(func(args mock.Arguments) {
		channel := args.Get(1).(*models.VoiceChannel)
		assert.Equal(t, 5, channel.MaxMembers)
	})

	ctx := context.Background()
	channel, err := service.CreateChannel(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, channel)
	assert.Equal(t, 5, channel.MaxMembers)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_GetChannel_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		OwnerID:     uuid.New(),
		Name:        "Test Party",
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)

	ctx := context.Background()
	result, err := service.GetChannel(ctx, channelID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, channelID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_ListChannels_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channels := []models.VoiceChannel{
		{
			ID:          uuid.New(),
			Type:        models.VoiceChannelTypeParty,
			Name:        "Party 1",
			MaxMembers:  5,
		},
	}

	mockRepo.On("ListChannels", mock.Anything, (*models.VoiceChannelType)(nil), (*uuid.UUID)(nil), 10, 0).Return(channels, nil)

	ctx := context.Background()
	result, err := service.ListChannels(ctx, nil, nil, 10, 0)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Channels, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_JoinChannel_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}

	req := &models.JoinChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
		Position:    make(map[string]interface{}),
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)
	mockRepo.On("CountParticipants", mock.Anything, channelID).Return(0, nil)
	mockRepo.On("GetParticipant", mock.Anything, channelID, characterID).Return(nil, nil)
	mockRepo.On("AddParticipant", mock.Anything, mock.AnythingOfType("*models.VoiceParticipant")).Return(nil)

	ctx := context.Background()
	tokenResp, err := service.JoinChannel(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, tokenResp)
	assert.NotEmpty(t, tokenResp.Token)
	assert.Equal(t, service.webrtcURL, tokenResp.ServerURL)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_JoinChannel_ChannelFull(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}

	req := &models.JoinChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
		Position:    make(map[string]interface{}),
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)
	mockRepo.On("CountParticipants", mock.Anything, channelID).Return(5, nil)

	ctx := context.Background()
	tokenResp, err := service.JoinChannel(ctx, req)

	assert.Error(t, err)
	assert.Nil(t, tokenResp)
	assert.Contains(t, err.Error(), "channel is full")
	mockRepo.AssertNotCalled(t, "AddParticipant", mock.Anything, mock.Anything)
}

func TestVoiceService_JoinChannel_AlreadyParticipant(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}

	existingParticipant := &models.VoiceParticipant{
		ID:          uuid.New(),
		ChannelID:   channelID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusConnected,
		WebRTCToken: "existing-token",
	}

	req := &models.JoinChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
		Position:    make(map[string]interface{}),
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)
	mockRepo.On("CountParticipants", mock.Anything, channelID).Return(1, nil)
	mockRepo.On("GetParticipant", mock.Anything, channelID, characterID).Return(existingParticipant, nil)

	ctx := context.Background()
	tokenResp, err := service.JoinChannel(ctx, req)

	require.NoError(t, err)
	assert.NotNil(t, tokenResp)
	assert.Equal(t, "existing-token", tokenResp.Token)
	mockRepo.AssertNotCalled(t, "AddParticipant", mock.Anything, mock.Anything)
}

func TestVoiceService_LeaveChannel_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		Settings:    make(map[string]interface{}),
	}

	req := &models.LeaveChannelRequest{
		CharacterID: characterID,
		ChannelID:   channelID,
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)
	mockRepo.On("RemoveParticipant", mock.Anything, channelID, characterID).Return(nil)
	mockRepo.On("CountParticipants", mock.Anything, channelID).Return(0, nil)

	ctx := context.Background()
	err := service.LeaveChannel(ctx, req)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_GetChannelParticipants_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	participants := []models.VoiceParticipant{
		{
			ID:          uuid.New(),
			ChannelID:   channelID,
			CharacterID: uuid.New(),
			Status:      models.ParticipantStatusConnected,
		},
	}

	mockRepo.On("ListParticipants", mock.Anything, channelID).Return(participants, nil)

	ctx := context.Background()
	result, err := service.GetChannelParticipants(ctx, channelID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Participants, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_GetChannelDetail_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	channel := &models.VoiceChannel{
		ID:          channelID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}

	participants := []models.VoiceParticipant{
		{
			ID:          uuid.New(),
			ChannelID:   channelID,
			CharacterID: uuid.New(),
			Status:      models.ParticipantStatusConnected,
		},
	}

	mockRepo.On("GetChannel", mock.Anything, channelID).Return(channel, nil)
	mockRepo.On("ListParticipants", mock.Anything, channelID).Return(participants, nil)

	ctx := context.Background()
	result, err := service.GetChannelDetail(ctx, channelID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, channelID, result.Channel.ID)
	assert.Len(t, result.Participants, 1)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_UpdateParticipantStatus_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	req := &models.UpdateParticipantStatusRequest{
		ChannelID:   channelID,
		CharacterID: characterID,
		Status:      models.ParticipantStatusMuted,
	}

	mockRepo.On("UpdateParticipantStatus", mock.Anything, channelID, characterID, models.ParticipantStatusMuted).Return(nil)

	ctx := context.Background()
	err := service.UpdateParticipantStatus(ctx, req)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_UpdateParticipantPosition_Success(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	channelID := uuid.New()
	characterID := uuid.New()
	position := map[string]interface{}{
		"x": 10.0,
		"y": 20.0,
		"z": 30.0,
	}
	req := &models.UpdateParticipantPositionRequest{
		ChannelID:   channelID,
		CharacterID: characterID,
		Position:    position,
	}

	mockRepo.On("UpdateParticipantPosition", mock.Anything, channelID, characterID, position).Return(nil)

	ctx := context.Background()
	err := service.UpdateParticipantPosition(ctx, req)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestVoiceService_CreateChannel_DatabaseError(t *testing.T) {
	service, mockRepo, cleanup := setupTestService(t)
	if service == nil {
		return
	}
	defer cleanup()

	characterID := uuid.New()
	req := &models.CreateChannelRequest{
		CharacterID: characterID,
		Type:        models.VoiceChannelTypeParty,
		Name:        "Test Party",
		MaxMembers:  5,
		Settings:    make(map[string]interface{}),
	}
	expectedErr := errors.New("database error")

	mockRepo.On("CreateChannel", mock.Anything, mock.AnythingOfType("*models.VoiceChannel")).Return(expectedErr)

	ctx := context.Background()
	channel, err := service.CreateChannel(ctx, req)

	assert.Error(t, err)
	assert.Nil(t, channel)
	mockRepo.AssertExpectations(t)
}

