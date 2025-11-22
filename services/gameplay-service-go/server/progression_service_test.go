package server

import (
	"context"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/gameplay-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockProgressionRepository struct {
	mock.Mock
}

func (m *mockProgressionRepository) GetProgression(ctx context.Context, characterID uuid.UUID) (*models.CharacterProgression, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CharacterProgression), args.Error(1)
}

func (m *mockProgressionRepository) CreateProgression(ctx context.Context, progression *models.CharacterProgression) error {
	args := m.Called(ctx, progression)
	return args.Error(0)
}

func (m *mockProgressionRepository) UpdateProgression(ctx context.Context, progression *models.CharacterProgression) error {
	args := m.Called(ctx, progression)
	return args.Error(0)
}

func (m *mockProgressionRepository) GetSkillExperience(ctx context.Context, characterID uuid.UUID, skillID string) (*models.SkillExperience, error) {
	args := m.Called(ctx, characterID, skillID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.SkillExperience), args.Error(1)
}

func (m *mockProgressionRepository) CreateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error {
	args := m.Called(ctx, skillExp)
	return args.Error(0)
}

func (m *mockProgressionRepository) UpdateSkillExperience(ctx context.Context, skillExp *models.SkillExperience) error {
	args := m.Called(ctx, skillExp)
	return args.Error(0)
}

func (m *mockProgressionRepository) ListSkillExperience(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.SkillExperience, error) {
	args := m.Called(ctx, characterID, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.SkillExperience), args.Error(1)
}

func (m *mockProgressionRepository) CountSkillExperience(ctx context.Context, characterID uuid.UUID) (int, error) {
	args := m.Called(ctx, characterID)
	return args.Int(0), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func setupTestProgressionService(t *testing.T) (*ProgressionService, *mockProgressionRepository, *mockEventBus, func()) {
	mockRepo := new(mockProgressionRepository)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	dbPool, _ := pgxpool.New(context.Background(), "postgres://user:pass@localhost:5432/test")

	service := &ProgressionService{
		repo:     mockRepo,
		db:       dbPool,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: mockEventBus,
	}

	cleanup := func() {
		redisClient.Close()
		if dbPool != nil {
			dbPool.Close()
		}
	}

	return service, mockRepo, mockEventBus, cleanup
}

func TestProgressionService_GetProgression_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	expectedProgression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  10,
		SkillPoints:      5,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(expectedProgression, nil)

	result, err := service.GetProgression(context.Background(), characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, 5, result.Level)
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_GetProgression_CreateNew(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()

	mockRepo.On("GetProgression", context.Background(), characterID).Return(nil, nil)
	mockRepo.On("CreateProgression", context.Background(), mock.AnythingOfType("*models.CharacterProgression")).Return(nil)

	result, err := service.GetProgression(context.Background(), characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, 1, result.Level)
	assert.Equal(t, int64(0), result.Experience)
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_AddExperience_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            1,
		Experience:       0,
		ExperienceToNext: 100,
		AttributePoints:  0,
		SkillPoints:      0,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)
	mockRepo.On("UpdateProgression", context.Background(), mock.AnythingOfType("*models.CharacterProgression")).Return(nil)

	err := service.AddExperience(context.Background(), characterID, 50, "combat")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_AddExperience_LevelUp(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            1,
		Experience:       90,
		ExperienceToNext: 100,
		AttributePoints:  0,
		SkillPoints:      0,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)
	mockRepo.On("UpdateProgression", context.Background(), mock.AnythingOfType("*models.CharacterProgression")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "character:level-up", mock.Anything).Return(nil)

	err := service.AddExperience(context.Background(), characterID, 20, "combat")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestProgressionService_AddSkillExperience_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	skillID := "combat"
	skillExp := &models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     skillID,
		Level:       1,
		Experience:  0,
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(skillExp, nil)
	mockRepo.On("UpdateSkillExperience", context.Background(), mock.AnythingOfType("*models.SkillExperience")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "character:skill-leveled", mock.Anything).Return(nil)

	err := service.AddSkillExperience(context.Background(), characterID, skillID, 50)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestProgressionService_AddSkillExperience_CreateNew(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	skillID := "combat"

	mockRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(nil, nil)
	mockRepo.On("CreateSkillExperience", context.Background(), mock.AnythingOfType("*models.SkillExperience")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "character:skill-leveled", mock.Anything).Return(nil)

	err := service.AddSkillExperience(context.Background(), characterID, skillID, 50)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestProgressionService_AllocateAttributePoint_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  5,
		SkillPoints:      3,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)
	mockRepo.On("UpdateProgression", context.Background(), mock.AnythingOfType("*models.CharacterProgression")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "character:attribute-increased", mock.Anything).Return(nil)

	err := service.AllocateAttributePoint(context.Background(), characterID, "strength")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestProgressionService_AllocateAttributePoint_NotEnoughPoints(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  0,
		SkillPoints:      3,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)

	err := service.AllocateAttributePoint(context.Background(), characterID, "strength")

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not enough attribute points")
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_AllocateAttributePoint_MaximumReached(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  5,
		SkillPoints:      3,
		Attributes:       map[string]int{"strength": 25},
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)

	err := service.AllocateAttributePoint(context.Background(), characterID, "strength")

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "at maximum")
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_AllocateSkillPoint_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	skillID := "combat"
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  5,
		SkillPoints:      3,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}
	skillExp := &models.SkillExperience{
		ID:          uuid.New(),
		CharacterID: characterID,
		SkillID:     skillID,
		Level:       5,
		Experience:  1000,
		UpdatedAt:   time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)
	mockRepo.On("GetSkillExperience", context.Background(), characterID, skillID).Return(skillExp, nil)
	mockRepo.On("UpdateProgression", context.Background(), mock.AnythingOfType("*models.CharacterProgression")).Return(nil)
	mockRepo.On("UpdateSkillExperience", context.Background(), mock.AnythingOfType("*models.SkillExperience")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "character:skill-leveled", mock.Anything).Return(nil)

	err := service.AllocateSkillPoint(context.Background(), characterID, skillID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestProgressionService_AllocateSkillPoint_NotEnoughPoints(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	skillID := "combat"
	progression := &models.CharacterProgression{
		CharacterID:      characterID,
		Level:            5,
		Experience:       1000,
		ExperienceToNext: 2000,
		AttributePoints:  5,
		SkillPoints:      0,
		Attributes:       make(map[string]int),
		UpdatedAt:        time.Now(),
	}

	mockRepo.On("GetProgression", context.Background(), characterID).Return(progression, nil)

	err := service.AllocateSkillPoint(context.Background(), characterID, skillID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "not enough skill points")
	mockRepo.AssertExpectations(t)
}

func TestProgressionService_GetSkillProgression_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestProgressionService(t)
	defer cleanup()

	characterID := uuid.New()
	skills := []models.SkillExperience{
		{
			ID:          uuid.New(),
			CharacterID: characterID,
			SkillID:     "combat",
			Level:       5,
			Experience:  1000,
			UpdatedAt:   time.Now(),
		},
	}

	mockRepo.On("ListSkillExperience", context.Background(), characterID, 10, 0).Return(skills, nil)
	mockRepo.On("CountSkillExperience", context.Background(), characterID).Return(1, nil)

	result, err := service.GetSkillProgression(context.Background(), characterID, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Skills, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

