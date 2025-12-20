// Issue: #140895495
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockSubchannelRepository struct {
	mock.Mock
}

func (m *mockSubchannelRepository) CreateSubchannel(ctx context.Context, lobbyID uuid.UUID, req *models.CreateSubchannelRequest) (*models.Subchannel, error) {
	args := m.Called(ctx, lobbyID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Subchannel), args.Error(1)
}

func (m *mockSubchannelRepository) GetSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.Subchannel, error) {
	args := m.Called(ctx, lobbyID, subchannelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Subchannel), args.Error(1)
}

func (m *mockSubchannelRepository) ListSubchannels(ctx context.Context, lobbyID uuid.UUID) ([]models.Subchannel, error) {
	args := m.Called(ctx, lobbyID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Subchannel), args.Error(1)
}

func (m *mockSubchannelRepository) UpdateSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID, req *models.UpdateSubchannelRequest) (*models.Subchannel, error) {
	args := m.Called(ctx, lobbyID, subchannelID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Subchannel), args.Error(1)
}

func (m *mockSubchannelRepository) DeleteSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) error {
	args := m.Called(ctx, lobbyID, subchannelID)
	return args.Error(0)
}

func (m *mockSubchannelRepository) MoveParticipant(ctx context.Context, subchannelID, characterID uuid.UUID) error {
	args := m.Called(ctx, subchannelID, characterID)
	return args.Error(0)
}

func (m *mockSubchannelRepository) GetParticipants(ctx context.Context, subchannelID uuid.UUID) ([]models.SubchannelParticipant, error) {
	args := m.Called(ctx, subchannelID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.SubchannelParticipant), args.Error(1)
}

func setupTestSubchannelService() (*SubchannelService, *mockSubchannelRepository) {
	mockRepo := new(mockSubchannelRepository)
	service := NewSubchannelService(mockRepo)
	return service, mockRepo
}

func TestSubchannelService_CreateSubchannel_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(10),
	}

	subchannel := &models.Subchannel{
		ID:                  uuid.New(),
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("CreateSubchannel", mock.Anything, lobbyID, req).Return(subchannel, nil)

	ctx := context.Background()
	result, err := service.CreateSubchannel(ctx, lobbyID, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "Test Subchannel", result.Name)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_CreateSubchannel_InvalidName(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "A",
		MaxParticipants: intPtr(10),
	}

	ctx := context.Background()
	result, err := service.CreateSubchannel(ctx, lobbyID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "name must be between 2 and 32 characters")
	mockRepo.AssertNotCalled(t, "CreateSubchannel", mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_CreateSubchannel_InvalidMaxParticipants(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(0),
	}

	ctx := context.Background()
	result, err := service.CreateSubchannel(ctx, lobbyID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "max_participants must be between 1 and 100")
	mockRepo.AssertNotCalled(t, "CreateSubchannel", mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_GetSubchannel_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)

	ctx := context.Background()
	result, err := service.GetSubchannel(ctx, lobbyID, subchannelID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, subchannelID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_GetSubchannel_NotFound(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(nil, nil)

	ctx := context.Background()
	result, err := service.GetSubchannel(ctx, lobbyID, subchannelID)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Equal(t, pgx.ErrNoRows, err)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_ListSubchannels_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannels := []models.Subchannel{
		{
			ID:                  uuid.New(),
			LobbyID:             lobbyID,
			Name:                "Test Subchannel",
			SubchannelType:      models.SubchannelTypeCustom,
			MaxParticipants:     intPtr(10),
			IsLocked:            false,
			CurrentParticipants: 0,
			CreatedAt:           time.Now(),
			UpdatedAt:           time.Now(),
		},
	}

	mockRepo.On("ListSubchannels", mock.Anything, lobbyID).Return(subchannels, nil)

	ctx := context.Background()
	result, err := service.ListSubchannels(ctx, lobbyID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, lobbyID, result.LobbyID)
	assert.Len(t, result.Subchannels, 1)
	assert.Equal(t, 1, result.TotalCount)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_UpdateSubchannel_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	req := &models.UpdateSubchannelRequest{
		Name:            stringPtr("Updated Subchannel"),
		MaxParticipants: intPtr(20),
	}

	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Updated Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(20),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("UpdateSubchannel", mock.Anything, lobbyID, subchannelID, req).Return(subchannel, nil)

	ctx := context.Background()
	result, err := service.UpdateSubchannel(ctx, lobbyID, subchannelID, req)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, "Updated Subchannel", result.Name)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_UpdateSubchannel_InvalidName(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	req := &models.UpdateSubchannelRequest{
		Name:            stringPtr("A"),
		MaxParticipants: intPtr(20),
	}

	ctx := context.Background()
	result, err := service.UpdateSubchannel(ctx, lobbyID, subchannelID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "name must be between 2 and 32 characters")
	mockRepo.AssertNotCalled(t, "UpdateSubchannel", mock.Anything, mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_UpdateSubchannel_NotFound(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	req := &models.UpdateSubchannelRequest{
		Name:            stringPtr("Updated Subchannel"),
		MaxParticipants: intPtr(20),
	}

	mockRepo.On("UpdateSubchannel", mock.Anything, lobbyID, subchannelID, req).Return(nil, nil)

	ctx := context.Background()
	result, err := service.UpdateSubchannel(ctx, lobbyID, subchannelID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Equal(t, pgx.ErrNoRows, err)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_DeleteSubchannel_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)
	mockRepo.On("DeleteSubchannel", mock.Anything, lobbyID, subchannelID).Return(nil)

	ctx := context.Background()
	err := service.DeleteSubchannel(ctx, lobbyID, subchannelID)

	require.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_DeleteSubchannel_MainSubchannel(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Main Subchannel",
		SubchannelType:      models.SubchannelTypeMain,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)

	ctx := context.Background()
	err := service.DeleteSubchannel(ctx, lobbyID, subchannelID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "cannot delete main subchannel")
	mockRepo.AssertNotCalled(t, "DeleteSubchannel", mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_MoveToSubchannel_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	characterID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 5,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)
	mockRepo.On("MoveParticipant", mock.Anything, subchannelID, characterID).Return(nil)

	ctx := context.Background()
	result, err := service.MoveToSubchannel(ctx, lobbyID, subchannelID, characterID, false)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, subchannelID, result.SubchannelID)
	assert.Equal(t, characterID, result.CharacterID)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_MoveToSubchannel_Locked(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	characterID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            true,
		CurrentParticipants: 5,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)

	ctx := context.Background()
	result, err := service.MoveToSubchannel(ctx, lobbyID, subchannelID, characterID, false)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "subchannel is locked")
	mockRepo.AssertNotCalled(t, "MoveParticipant", mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_MoveToSubchannel_Full(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	characterID := uuid.New()
	maxParticipants := 10
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     &maxParticipants,
		IsLocked:            false,
		CurrentParticipants: 10,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)

	ctx := context.Background()
	result, err := service.MoveToSubchannel(ctx, lobbyID, subchannelID, characterID, false)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "subchannel is full")
	mockRepo.AssertNotCalled(t, "MoveParticipant", mock.Anything, mock.Anything, mock.Anything)
}

func TestSubchannelService_GetSubchannelParticipants_Success(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	subchannelID := uuid.New()
	subchannel := &models.Subchannel{
		ID:                  subchannelID,
		LobbyID:             lobbyID,
		Name:                "Test Subchannel",
		SubchannelType:      models.SubchannelTypeCustom,
		MaxParticipants:     intPtr(10),
		IsLocked:            false,
		CurrentParticipants: 0,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	participants := []models.SubchannelParticipant{
		{
			CharacterID: uuid.New(),
			JoinedAt:    time.Now(),
		},
	}

	mockRepo.On("GetSubchannel", mock.Anything, lobbyID, subchannelID).Return(subchannel, nil)
	mockRepo.On("GetParticipants", mock.Anything, subchannelID).Return(participants, nil)

	ctx := context.Background()
	result, err := service.GetSubchannelParticipants(ctx, lobbyID, subchannelID)

	require.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, subchannelID, result.SubchannelID)
	assert.Len(t, result.Participants, 1)
	assert.Equal(t, 1, result.TotalCount)
	mockRepo.AssertExpectations(t)
}

func TestSubchannelService_CreateSubchannel_DatabaseError(t *testing.T) {
	service, mockRepo := setupTestSubchannelService()

	lobbyID := uuid.New()
	req := &models.CreateSubchannelRequest{
		Name:            "Test Subchannel",
		MaxParticipants: intPtr(10),
	}

	expectedErr := errors.New("database error")
	mockRepo.On("CreateSubchannel", mock.Anything, lobbyID, req).Return(nil, expectedErr)

	ctx := context.Background()
	result, err := service.CreateSubchannel(ctx, lobbyID, req)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}
