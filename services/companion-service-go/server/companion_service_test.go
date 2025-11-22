package server

import (
	"context"
	"errors"
	"testing"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockCompanionRepository struct {
	mock.Mock
}

func (m *mockCompanionRepository) GetCompanionType(ctx context.Context, companionTypeID string) (*models.CompanionType, error) {
	args := m.Called(ctx, companionTypeID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CompanionType), args.Error(1)
}

func (m *mockCompanionRepository) ListCompanionTypes(ctx context.Context, category *models.CompanionCategory, limit, offset int) ([]models.CompanionType, error) {
	args := m.Called(ctx, category, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.CompanionType), args.Error(1)
}

func (m *mockCompanionRepository) CountCompanionTypes(ctx context.Context, category *models.CompanionCategory) (int, error) {
	args := m.Called(ctx, category)
	return args.Int(0), args.Error(1)
}

func (m *mockCompanionRepository) CreatePlayerCompanion(ctx context.Context, companion *models.PlayerCompanion) error {
	args := m.Called(ctx, companion)
	return args.Error(0)
}

func (m *mockCompanionRepository) GetPlayerCompanion(ctx context.Context, companionID uuid.UUID) (*models.PlayerCompanion, error) {
	args := m.Called(ctx, companionID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerCompanion), args.Error(1)
}

func (m *mockCompanionRepository) GetActiveCompanion(ctx context.Context, characterID uuid.UUID) (*models.PlayerCompanion, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerCompanion), args.Error(1)
}

func (m *mockCompanionRepository) UpdatePlayerCompanion(ctx context.Context, companion *models.PlayerCompanion) error {
	args := m.Called(ctx, companion)
	return args.Error(0)
}

func (m *mockCompanionRepository) ListPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus, limit, offset int) ([]models.PlayerCompanion, error) {
	args := m.Called(ctx, characterID, status, limit, offset)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.PlayerCompanion), args.Error(1)
}

func (m *mockCompanionRepository) CountPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus) (int, error) {
	args := m.Called(ctx, characterID, status)
	return args.Int(0), args.Error(1)
}

func (m *mockCompanionRepository) GetCompanionAbility(ctx context.Context, playerCompanionID uuid.UUID, abilityID string) (*models.CompanionAbility, error) {
	args := m.Called(ctx, playerCompanionID, abilityID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.CompanionAbility), args.Error(1)
}

func (m *mockCompanionRepository) CreateCompanionAbility(ctx context.Context, ability *models.CompanionAbility) error {
	args := m.Called(ctx, ability)
	return args.Error(0)
}

func (m *mockCompanionRepository) UpdateCompanionAbility(ctx context.Context, ability *models.CompanionAbility) error {
	args := m.Called(ctx, ability)
	return args.Error(0)
}

func (m *mockCompanionRepository) ListCompanionAbilities(ctx context.Context, playerCompanionID uuid.UUID) ([]models.CompanionAbility, error) {
	args := m.Called(ctx, playerCompanionID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.CompanionAbility), args.Error(1)
}

type mockEventBus struct {
	mock.Mock
}

func (m *mockEventBus) PublishEvent(ctx context.Context, eventType string, payload map[string]interface{}) error {
	args := m.Called(ctx, eventType, payload)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*CompanionService, *mockCompanionRepository, *mockEventBus, func()) {
	mockRepo := new(mockCompanionRepository)
	mockEventBus := new(mockEventBus)

	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})
	redisClient.FlushDB(context.Background())

	service := &CompanionService{
		repo:     mockRepo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: mockEventBus,
	}

	cleanup := func() {
		redisClient.Close()
	}

	return service, mockRepo, mockEventBus, cleanup
}

func TestCompanionService_GetCompanionType_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	companionTypeID := "combat_drone_001"
	expectedType := &models.CompanionType{
		ID:          companionTypeID,
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Drone",
		Description: "A combat drone",
		Stats:       map[string]interface{}{"health": 100.0, "damage": 50.0},
		Abilities:   []string{"attack", "defend"},
		Cost:        1000,
		CreatedAt:   time.Now(),
	}

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(expectedType, nil)

	result, err := service.GetCompanionType(context.Background(), companionTypeID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, companionTypeID, result.ID)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_GetCompanionType_NotFound(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	companionTypeID := "nonexistent"

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(nil, nil)

	result, err := service.GetCompanionType(context.Background(), companionTypeID)

	assert.NoError(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_ListCompanionTypes_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	types := []models.CompanionType{
		{
			ID:          "type_001",
			Category:    models.CompanionCategoryCombat,
			Name:        "Combat Drone",
			Description: "A combat drone",
			Cost:        1000,
		},
	}

	mockRepo.On("ListCompanionTypes", context.Background(), (*models.CompanionCategory)(nil), 10, 0).Return(types, nil)
	mockRepo.On("CountCompanionTypes", context.Background(), (*models.CompanionCategory)(nil)).Return(1, nil)

	result, err := service.ListCompanionTypes(context.Background(), nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Types, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_PurchaseCompanion_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionTypeID := "combat_drone_001"
	companionType := &models.CompanionType{
		ID:          companionTypeID,
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Drone",
		Description: "A combat drone",
		Stats:       map[string]interface{}{"health": 100.0, "damage": 50.0},
		Abilities:   []string{"attack"},
		Cost:        1000,
		CreatedAt:   time.Now(),
	}

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(companionType, nil)
	mockRepo.On("GetPlayerCompanion", context.Background(), uuid.Nil).Return(nil, nil)
	mockRepo.On("ListPlayerCompanions", context.Background(), characterID, (*models.CompanionStatus)(nil), 100, 0).Return([]models.PlayerCompanion{}, nil)
	mockRepo.On("CreatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "companion:purchased", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	result, err := service.PurchaseCompanion(context.Background(), characterID, companionTypeID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, characterID, result.CharacterID)
	assert.Equal(t, companionTypeID, result.CompanionTypeID)
	assert.Equal(t, models.CompanionStatusOwned, result.Status)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestCompanionService_PurchaseCompanion_TypeNotFound(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionTypeID := "nonexistent"

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(nil, nil)

	result, err := service.PurchaseCompanion(context.Background(), characterID, companionTypeID)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "companion type not found")
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_PurchaseCompanion_AlreadyOwned(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionTypeID := "combat_drone_001"
	companionType := &models.CompanionType{
		ID:          companionTypeID,
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Drone",
		Description: "A combat drone",
		Cost:        1000,
	}
	existingCompanion := models.PlayerCompanion{
		ID:            uuid.New(),
		CharacterID:   characterID,
		CompanionTypeID: companionTypeID,
		Status:        models.CompanionStatusOwned,
	}

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(companionType, nil)
	mockRepo.On("GetPlayerCompanion", context.Background(), uuid.Nil).Return(nil, nil)
	mockRepo.On("ListPlayerCompanions", context.Background(), characterID, (*models.CompanionStatus)(nil), 100, 0).Return([]models.PlayerCompanion{existingCompanion}, nil)

	result, err := service.PurchaseCompanion(context.Background(), characterID, companionTypeID)

	assert.Error(t, err)
	assert.Nil(t, result)
	assert.Contains(t, err.Error(), "companion already owned")
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_GetCompanionDetail_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	companionID := uuid.New()
	companionTypeID := "combat_drone_001"
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   uuid.New(),
		CompanionTypeID: companionTypeID,
		Level:         5,
		Experience:    1000,
		Status:        models.CompanionStatusOwned,
	}
	companionType := &models.CompanionType{
		ID:          companionTypeID,
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Drone",
		Description: "A combat drone",
	}
	abilities := []models.CompanionAbility{
		{
			ID:              uuid.New(),
			PlayerCompanionID: companionID,
			AbilityID:       "attack",
			IsActive:        true,
		},
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(companionType, nil)
	mockRepo.On("ListCompanionAbilities", context.Background(), companionID).Return(abilities, nil)

	result, err := service.GetCompanionDetail(context.Background(), companionID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Equal(t, companion, result.Companion)
	assert.Equal(t, companionType, result.Type)
	assert.Len(t, result.Abilities, 1)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_SummonCompanion_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusOwned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("GetActiveCompanion", context.Background(), characterID).Return(nil, nil)
	mockRepo.On("UpdatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "companion:summoned", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	err := service.SummonCompanion(context.Background(), characterID, companionID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestCompanionService_SummonCompanion_AlreadySummoned(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusSummoned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)

	err := service.SummonCompanion(context.Background(), characterID, companionID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "companion already summoned")
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_DismissCompanion_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusSummoned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("UpdatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "companion:dismissed", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	err := service.DismissCompanion(context.Background(), characterID, companionID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestCompanionService_DismissCompanion_NotSummoned(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusOwned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)

	err := service.DismissCompanion(context.Background(), characterID, companionID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "companion is not summoned")
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_RenameCompanion_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	customName := "My Drone"
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusOwned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("UpdatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)

	err := service.RenameCompanion(context.Background(), characterID, companionID, customName)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_AddExperience_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Level:         1,
		Experience:    0,
		Status:        models.CompanionStatusOwned,
		Stats:         map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("UpdatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)

	err := service.AddExperience(context.Background(), characterID, companionID, 100, "combat")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_AddExperience_LevelUp(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Level:         1,
		Experience:    0,
		Status:        models.CompanionStatusOwned,
		Stats:         map[string]interface{}{"health": 100.0, "damage": 50.0},
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("UpdatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "companion:level-up", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	err := service.AddExperience(context.Background(), characterID, companionID, 10000, "combat")

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestCompanionService_UseAbility_Success(t *testing.T) {
	service, mockRepo, mockEventBus, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	abilityID := "attack"
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusSummoned,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("GetCompanionAbility", context.Background(), companionID, abilityID).Return(nil, nil)
	mockRepo.On("CreateCompanionAbility", context.Background(), mock.AnythingOfType("*models.CompanionAbility")).Return(nil)
	mockEventBus.On("PublishEvent", context.Background(), "companion:ability-used", mock.AnythingOfType("map[string]interface {}")).Return(nil)

	err := service.UseAbility(context.Background(), characterID, companionID, abilityID)

	assert.NoError(t, err)
	mockRepo.AssertExpectations(t)
	mockEventBus.AssertExpectations(t)
}

func TestCompanionService_UseAbility_OnCooldown(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionID := uuid.New()
	abilityID := "attack"
	companion := &models.PlayerCompanion{
		ID:            companionID,
		CharacterID:   characterID,
		CompanionTypeID: "combat_drone_001",
		Status:        models.CompanionStatusSummoned,
	}
	cooldownUntil := time.Now().Add(10 * time.Second)
	ability := &models.CompanionAbility{
		ID:              uuid.New(),
		PlayerCompanionID: companionID,
		AbilityID:       abilityID,
		CooldownUntil:   &cooldownUntil,
	}

	mockRepo.On("GetPlayerCompanion", context.Background(), companionID).Return(companion, nil)
	mockRepo.On("GetCompanionAbility", context.Background(), companionID, abilityID).Return(ability, nil)

	err := service.UseAbility(context.Background(), characterID, companionID, abilityID)

	assert.Error(t, err)
	assert.Contains(t, err.Error(), "ability is on cooldown")
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_ListPlayerCompanions_Success(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companions := []models.PlayerCompanion{
		{
			ID:            uuid.New(),
			CharacterID:   characterID,
			CompanionTypeID: "combat_drone_001",
			Status:        models.CompanionStatusOwned,
		},
	}

	mockRepo.On("ListPlayerCompanions", context.Background(), characterID, (*models.CompanionStatus)(nil), 10, 0).Return(companions, nil)
	mockRepo.On("CountPlayerCompanions", context.Background(), characterID, (*models.CompanionStatus)(nil)).Return(1, nil)

	result, err := service.ListPlayerCompanions(context.Background(), characterID, nil, 10, 0)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	assert.Len(t, result.Companions, 1)
	assert.Equal(t, 1, result.Total)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_GetCompanionType_RepositoryError(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	companionTypeID := "combat_drone_001"
	expectedErr := errors.New("database error")

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(nil, expectedErr)

	result, err := service.GetCompanionType(context.Background(), companionTypeID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

func TestCompanionService_PurchaseCompanion_RepositoryError(t *testing.T) {
	service, mockRepo, _, cleanup := setupTestService(t)
	defer cleanup()

	characterID := uuid.New()
	companionTypeID := "combat_drone_001"
	companionType := &models.CompanionType{
		ID:          companionTypeID,
		Category:    models.CompanionCategoryCombat,
		Name:        "Combat Drone",
		Description: "A combat drone",
		Cost:        1000,
	}
	expectedErr := errors.New("database error")

	mockRepo.On("GetCompanionType", context.Background(), companionTypeID).Return(companionType, nil)
	mockRepo.On("GetPlayerCompanion", context.Background(), uuid.Nil).Return(nil, nil)
	mockRepo.On("ListPlayerCompanions", context.Background(), characterID, (*models.CompanionStatus)(nil), 100, 0).Return([]models.PlayerCompanion{}, nil)
	mockRepo.On("CreatePlayerCompanion", context.Background(), mock.AnythingOfType("*models.PlayerCompanion")).Return(expectedErr)

	result, err := service.PurchaseCompanion(context.Background(), characterID, companionTypeID)

	assert.Error(t, err)
	assert.Nil(t, result)
	mockRepo.AssertExpectations(t)
}

