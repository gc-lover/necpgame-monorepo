package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/movement-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
	"github.com/stretchr/testify/require"
)

type mockMovementRepository struct {
	mock.Mock
}

func (m *mockMovementRepository) GetPositionByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementRepository) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	args := m.Called(ctx, characterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterPosition), args.Error(1)
}

func (m *mockMovementRepository) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	args := m.Called(ctx, characterID, limit)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PositionHistory), args.Error(1)
}

func (m *mockMovementRepository) SavePositionHistory(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) error {
	args := m.Called(ctx, characterID, req)
	return args.Error(0)
}

func TestNewMovementService(t *testing.T) {
	service, err := NewMovementService(
		"postgres://user:pass@localhost:5432/test",
		"redis://localhost:6379",
		"ws://localhost:8080/gateway",
		1*time.Second,
	)

	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}

	assert.NotNil(t, service)
	assert.NotNil(t, service.repo)
	assert.NotNil(t, service.cache)
	assert.NotNil(t, service.positions)
	assert.Equal(t, 1*time.Second, service.updateInterval)

	service.Shutdown()
}

func TestMovementService_GetPosition(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:   mockRepo,
		logger: GetLogger(),
	}

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
	}

	mockRepo.On("GetPositionByCharacterID", mock.Anything, characterID).Return(expectedPos, nil)

	ctx := context.Background()
	pos, err := service.GetPosition(ctx, characterID)

	require.NoError(t, err)
	assert.NotNil(t, pos)
	assert.Equal(t, expectedPos.CharacterID, pos.CharacterID)
	assert.Equal(t, expectedPos.PositionX, pos.PositionX)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_GetPosition_NotFound(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:   mockRepo,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	mockRepo.On("GetPositionByCharacterID", mock.Anything, characterID).Return(nil, errors.New("not found"))

	ctx := context.Background()
	pos, err := service.GetPosition(ctx, characterID)

	assert.Error(t, err)
	assert.Nil(t, pos)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_SavePosition(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return
	}
	redisClient := redis.NewClient(redisOpts)

	service := &MovementService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
		VelocityX: 1.0,
		VelocityY: 0.0,
		VelocityZ: 0.0,
	}

	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   req.PositionX,
		PositionY:   req.PositionY,
		PositionZ:   req.PositionZ,
		Yaw:         req.Yaw,
		VelocityX:   req.VelocityX,
		VelocityY:   req.VelocityY,
		VelocityZ:   req.VelocityZ,
	}

	mockRepo.On("SavePosition", mock.Anything, characterID, req).Return(expectedPos, nil)

	ctx := context.Background()
	pos, err := service.SavePosition(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, pos)
	assert.Equal(t, expectedPos.CharacterID, pos.CharacterID)
	assert.Equal(t, expectedPos.PositionX, pos.PositionX)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_SavePosition_InvalidCoordinates(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return
	}
	redisClient := redis.NewClient(redisOpts)

	service := &MovementService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 1e10,
		PositionY: -1e10,
		PositionZ: 0.0,
		Yaw:       0.0,
	}

	expectedPos := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID,
		PositionX:   req.PositionX,
		PositionY:   req.PositionY,
		PositionZ:   req.PositionZ,
		Yaw:         req.Yaw,
	}

	mockRepo.On("SavePosition", mock.Anything, characterID, req).Return(expectedPos, nil)

	ctx := context.Background()
	pos, err := service.SavePosition(ctx, characterID, req)

	require.NoError(t, err)
	assert.NotNil(t, pos)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_SavePosition_DatabaseError(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return
	}
	redisClient := redis.NewClient(redisOpts)

	service := &MovementService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	req := &models.SavePositionRequest{
		PositionX: 10.5,
		PositionY: 20.3,
		PositionZ: 30.1,
		Yaw:       45.0,
	}

	mockRepo.On("SavePosition", mock.Anything, characterID, req).Return(nil, errors.New("database error"))

	ctx := context.Background()
	pos, err := service.SavePosition(ctx, characterID, req)

	assert.Error(t, err)
	assert.Nil(t, pos)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_GetPositionHistory(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:   mockRepo,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	expectedHistory := []models.PositionHistory{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			PositionX:   10.5,
			PositionY:   20.3,
			PositionZ:   30.1,
			Yaw:         45.0,
		},
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			PositionX:   11.5,
			PositionY:   21.3,
			PositionZ:   31.1,
			Yaw:         46.0,
		},
	}

	mockRepo.On("GetPositionHistory", mock.Anything, characterID, 10).Return(expectedHistory, nil)

	ctx := context.Background()
	history, err := service.GetPositionHistory(ctx, characterID, 10)

	require.NoError(t, err)
	assert.NotNil(t, history)
	assert.Len(t, history, 2)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_GetPositionHistory_Empty(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:   mockRepo,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	mockRepo.On("GetPositionHistory", mock.Anything, characterID, 10).Return([]models.PositionHistory{}, nil)

	ctx := context.Background()
	history, err := service.GetPositionHistory(ctx, characterID, 10)

	require.NoError(t, err)
	assert.NotNil(t, history)
	assert.Len(t, history, 0)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_GetPositionHistory_DatabaseError(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:   mockRepo,
		logger: GetLogger(),
	}

	characterID := uuid.New()
	mockRepo.On("GetPositionHistory", mock.Anything, characterID, 10).Return(nil, errors.New("database error"))

	ctx := context.Background()
	history, err := service.GetPositionHistory(ctx, characterID, 10)

	assert.Error(t, err)
	assert.Nil(t, history)
	mockRepo.AssertExpectations(t)
}

func TestMovementService_SaveAllPositions(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	redisOpts, err := redis.ParseURL("redis://localhost:6379")
	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return
	}
	redisClient := redis.NewClient(redisOpts)

	service := &MovementService{
		repo:      mockRepo,
		cache:     redisClient,
		logger:    GetLogger(),
		positions: make(map[string]*models.EntityState),
	}

	characterID1 := uuid.New()
	characterID2 := uuid.New()

	service.positionsMu.Lock()
	service.positions[characterID1.String()] = &models.EntityState{
		ID:  characterID1.String(),
		X:   10.5,
		Y:   20.3,
		Z:   30.1,
		Yaw: 45.0,
		VX:  1.0,
		VY:  0.0,
		VZ:  0.0,
	}
	service.positions[characterID2.String()] = &models.EntityState{
		ID:  characterID2.String(),
		X:   11.5,
		Y:   21.3,
		Z:   31.1,
		Yaw: 46.0,
		VX:  1.0,
		VY:  0.0,
		VZ:  0.0,
	}
	service.positionsMu.Unlock()

	expectedPos1 := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID1,
		PositionX:   10.5,
		PositionY:   20.3,
		PositionZ:   30.1,
		Yaw:         45.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
	}

	expectedPos2 := &models.CharacterPosition{
		ID:          uuid.New(),
		CharacterID: characterID2,
		PositionX:   11.5,
		PositionY:   21.3,
		PositionZ:   31.1,
		Yaw:         46.0,
		VelocityX:   1.0,
		VelocityY:   0.0,
		VelocityZ:   0.0,
	}

	mockRepo.On("SavePosition", mock.Anything, characterID1, mock.Anything).Return(expectedPos1, nil)
	mockRepo.On("SavePosition", mock.Anything, characterID2, mock.Anything).Return(expectedPos2, nil)

	ctx := context.Background()
	service.saveAllPositions(ctx)

	mockRepo.AssertExpectations(t)
}

func TestMovementService_SaveAllPositions_InvalidEntityID(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:      mockRepo,
		logger:    GetLogger(),
		positions: make(map[string]*models.EntityState),
	}

	service.positionsMu.Lock()
	service.positions["invalid-uuid"] = &models.EntityState{
		ID:  "invalid-uuid",
		X:   10.5,
		Y:   20.3,
		Z:   30.1,
		Yaw: 45.0,
	}
	service.positionsMu.Unlock()

	ctx := context.Background()
	service.saveAllPositions(ctx)

	mockRepo.AssertNotCalled(t, "SavePosition")
}

func TestMovementService_SaveAllPositions_EmptyPositions(t *testing.T) {
	mockRepo := new(mockMovementRepository)
	service := &MovementService{
		repo:      mockRepo,
		logger:    GetLogger(),
		positions: make(map[string]*models.EntityState),
	}

	ctx := context.Background()
	service.saveAllPositions(ctx)

	mockRepo.AssertNotCalled(t, "SavePosition")
}

func TestMovementService_Shutdown(t *testing.T) {
	service := &MovementService{
		gatewayConn: nil,
	}

	service.Shutdown()

	assert.Nil(t, service.gatewayConn)
}

