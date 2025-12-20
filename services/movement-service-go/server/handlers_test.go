// Issue: #140890954
package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockMovementServiceForHandlers struct {
	mock.Mock
}

func (m *mockMovementServiceForHandlers) GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementServiceForHandlers) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementServiceForHandlers) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	args := m.Called(ctx, characterID, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PositionHistory), args.Error(1)
}

func TestNewHandlers(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	assert.NotNil(t, handlers)
	assert.Equal(t, mockService, handlers.service)
}

func TestHandlers_GetPosition_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("GetPosition", mock.Anything, characterID).Return(expectedPos, nil)

	res, err := handlers.GetPosition(context.Background(), api.GetPositionParams{
		CharacterId: characterID,
	})

	require.NoError(t, err)
	out, ok := res.(*api.CharacterPosition)
	require.True(t, ok)
	require.True(t, out.CharacterID.Set)
	assert.Equal(t, characterID, out.CharacterID.Value)
	mockService.AssertExpectations(t)
}

func TestHandlers_GetPosition_NotFound(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPosition", mock.Anything, characterID).Return(nil, nil)

	res, err := handlers.GetPosition(context.Background(), api.GetPositionParams{
		CharacterId: characterID,
	})

	require.NoError(t, err)
	_, ok := res.(*api.GetPositionNotFound)
	assert.True(t, ok)
	mockService.AssertExpectations(t)
}

func TestHandlers_GetPosition_Error(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	mockService.On("GetPosition", mock.Anything, characterID).Return(nil, errors.New("database error"))

	res, err := handlers.GetPosition(context.Background(), api.GetPositionParams{
		CharacterId: characterID,
	})

	require.Error(t, err)
	_, ok := res.(*api.GetPositionInternalServerError)
	assert.True(t, ok)
	mockService.AssertExpectations(t)
}

func TestHandlers_SavePosition_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	reqBody := &api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: api.NewOptNilFloat32(1.0),
		VelocityY: api.NewOptNilFloat32(0.0),
		VelocityZ: api.NewOptNilFloat32(0.0),
	}

	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	mockService.On("SavePosition", mock.Anything, characterID, mock.AnythingOfType("*models.SavePositionRequest")).Return(expectedPos, nil)

	res, err := handlers.SavePosition(context.Background(), reqBody, api.SavePositionParams{
		CharacterId: characterID,
	})

	require.NoError(t, err)
	out, ok := res.(*api.CharacterPosition)
	require.True(t, ok)
	require.True(t, out.CharacterID.Set)
	assert.Equal(t, characterID, out.CharacterID.Value)
	mockService.AssertExpectations(t)
}

func TestHandlers_SavePosition_ServiceError(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	reqBody := &api.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
	}

	mockService.On("SavePosition", mock.Anything, characterID, mock.AnythingOfType("*models.SavePositionRequest")).Return(nil, errors.New("database error"))

	res, err := handlers.SavePosition(context.Background(), reqBody, api.SavePositionParams{
		CharacterId: characterID,
	})

	require.Error(t, err)
	_, ok := res.(*api.SavePositionInternalServerError)
	assert.True(t, ok)
	mockService.AssertExpectations(t)
}

func TestHandlers_GetPositionHistory_Success(t *testing.T) {
	mockService := new(mockMovementServiceForHandlers)
	handlers := NewHandlers(mockService)

	characterID := uuid.New()
	history := []models.PositionHistory{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			PositionX:   10.5,
			PositionY:   20.3,
			PositionZ:   30.1,
			Yaw:         45.0,
			VelocityX:   1.0,
			VelocityY:   0.0,
			VelocityZ:   0.0,
			CreatedAt:   time.Now(),
		},
	}

	mockService.On("GetPositionHistory", mock.Anything, characterID, 10).Return(history, nil)

	res, err := handlers.GetPositionHistory(context.Background(), api.GetPositionHistoryParams{
		CharacterId: characterID,
		Limit:       api.OptInt{Value: 10, Set: true},
	})

	require.NoError(t, err)
	out, ok := res.(*api.GetPositionHistoryOKApplicationJSON)
	require.True(t, ok)
	assert.Len(t, *out, 1)
	mockService.AssertExpectations(t)
}
