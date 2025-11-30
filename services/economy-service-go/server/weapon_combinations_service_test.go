// Issue: #140894175
package server

import (
	"context"
	"testing"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/mock"
)

type mockWeaponCombinationsRepository struct {
	mock.Mock
}

func (m *mockWeaponCombinationsRepository) SaveWeaponCombination(ctx context.Context, weaponID uuid.UUID, combinationData map[string]interface{}) error {
	args := m.Called(ctx, weaponID, combinationData)
	return args.Error(0)
}

func (m *mockWeaponCombinationsRepository) GetWeaponCombination(ctx context.Context, weaponID uuid.UUID) (map[string]interface{}, error) {
	args := m.Called(ctx, weaponID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(map[string]interface{}), args.Error(1)
}

func (m *mockWeaponCombinationsRepository) SaveWeaponModifier(ctx context.Context, weaponID, modifierID uuid.UUID, modifierData map[string]interface{}) error {
	args := m.Called(ctx, weaponID, modifierID, modifierData)
	return args.Error(0)
}

func (m *mockWeaponCombinationsRepository) GetWeaponModifiers(ctx context.Context) ([]map[string]interface{}, error) {
	args := m.Called(ctx)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]map[string]interface{}), args.Error(1)
}

func setupTestWeaponCombinationsService() (*WeaponCombinationsService, *mockWeaponCombinationsRepository, *redis.Client) {
	mockRepo := new(mockWeaponCombinationsRepository)
	redisClient := redis.NewClient(&redis.Options{
		Addr: "localhost:6379",
		DB:   1,
	})

	service := &WeaponCombinationsService{
		repo:   mockRepo,
		cache:  redisClient,
		logger: GetLogger(),
	}

	return service, mockRepo, redisClient
}

func TestNewWeaponCombinationsService(t *testing.T) {
	dbURL := "postgres://user:pass@localhost:5432/test"
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		t.Skipf("Skipping test due to database connection: %v", err)
		return
	}
	defer dbPool.Close()

	redisURL := "redis://localhost:6379/1"
	service, err := NewWeaponCombinationsService(dbPool, redisURL)

	if err != nil {
		t.Skipf("Skipping test due to Redis connection: %v", err)
		return
	}

	assert.NotNil(t, service)
	assert.NotNil(t, service.repo)
	assert.NotNil(t, service.cache)
	assert.NotNil(t, service.logger)
}

func TestWeaponCombinationsService_GenerateWeaponCombination(t *testing.T) {
	service, mockRepo, redisClient := setupTestWeaponCombinationsService()
	defer redisClient.Close()

	ctx := context.Background()
	baseWeaponType := uuid.New()
	brandID := uuid.New()
	rarity := "legendary"
	seed := stringPtr("test_seed")
	playerLevel := intPtr(50)

	weaponID, combination, err := service.GenerateWeaponCombination(ctx, baseWeaponType, brandID, rarity, seed, playerLevel)

	assert.NoError(t, err)
	assert.NotEqual(t, uuid.Nil, weaponID)
	assert.NotNil(t, combination)
	mockRepo.AssertNotCalled(t, "SaveWeaponCombination")
}

func TestWeaponCombinationsService_GetWeaponCombinationMatrix(t *testing.T) {
	service, _, redisClient := setupTestWeaponCombinationsService()
	defer redisClient.Close()

	ctx := context.Background()

	matrix, err := service.GetWeaponCombinationMatrix(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, matrix)
}

func TestWeaponCombinationsService_GetWeaponModifiers(t *testing.T) {
	service, mockRepo, redisClient := setupTestWeaponCombinationsService()
	defer redisClient.Close()

	ctx := context.Background()
	expectedModifiers := []map[string]interface{}{
		{"id": "mod1", "type": "scope", "damage_bonus": 10},
		{"id": "mod2", "type": "grip", "accuracy": 5},
	}

	mockRepo.On("GetWeaponModifiers", ctx).Return(expectedModifiers, nil)

	modifiers, err := service.GetWeaponModifiers(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, modifiers)
	mockRepo.AssertExpectations(t)
}

func TestWeaponCombinationsService_ApplyWeaponModifier(t *testing.T) {
	service, mockRepo, redisClient := setupTestWeaponCombinationsService()
	defer redisClient.Close()

	ctx := context.Background()
	weaponID := uuid.New()
	modifierID := uuid.New()
	modifierType := "scope"
	characterID := uuidPtr(uuid.New())

	result, err := service.ApplyWeaponModifier(ctx, weaponID, modifierID, modifierType, characterID)

	assert.NoError(t, err)
	assert.NotNil(t, result)
	mockRepo.AssertNotCalled(t, "SaveWeaponModifier")
}

func TestWeaponCombinationsService_GetCorporations(t *testing.T) {
	service, _, redisClient := setupTestWeaponCombinationsService()
	defer redisClient.Close()

	ctx := context.Background()

	corporations, err := service.GetCorporations(ctx)

	assert.NoError(t, err)
	assert.NotNil(t, corporations)
	assert.IsType(t, []map[string]interface{}{}, corporations)
}


