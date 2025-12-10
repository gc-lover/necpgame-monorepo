// Issue: #140893532
package server

import (
	"context"
	"testing"
	"time"

	"github.com/alicebob/miniredis/v2"
	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/stretchr/testify/mock"
)

type mockCharacterRepository struct {
	mock.Mock
}

func (m *mockCharacterRepository) GetAccountByID(ctx context.Context, accountID uuid.UUID) (*models.PlayerAccount, error) {
	args := m.Called(ctx, accountID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerAccount), args.Error(1)
}

func (m *mockCharacterRepository) CreateAccount(ctx context.Context, req *models.CreateAccountRequest) (*models.PlayerAccount, error) {
	args := m.Called(ctx, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.PlayerAccount), args.Error(1)
}

func (m *mockCharacterRepository) GetCharacterByID(ctx context.Context, characterID uuid.UUID) (*models.Character, error) {
	args := m.Called(ctx, characterID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) GetCharactersByAccountID(ctx context.Context, accountID uuid.UUID) ([]models.Character, error) {
	args := m.Called(ctx, accountID)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).([]models.Character), args.Error(1)
}

func (m *mockCharacterRepository) CreateCharacter(ctx context.Context, req *models.CreateCharacterRequest) (*models.Character, error) {
	args := m.Called(ctx, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) UpdateCharacter(ctx context.Context, characterID uuid.UUID, req *models.UpdateCharacterRequest) (*models.Character, error) {
	args := m.Called(ctx, characterID, req)
	if args.Get(0) == nil {
		return nil, args.Error(1)
	}
	return args.Get(0).(*models.Character), args.Error(1)
}

func (m *mockCharacterRepository) DeleteCharacter(ctx context.Context, characterID uuid.UUID) error {
	args := m.Called(ctx, characterID)
	return args.Error(0)
}

func setupTestService(t *testing.T) (*CharacterService, *mockCharacterRepository, func()) {
	mr, err := miniredis.Run()
	if err != nil {
		t.Fatalf("failed to start miniredis: %v", err)
	}
	redisClient := redis.NewClient(&redis.Options{
		Addr:         mr.Addr(),
		DialTimeout:  50 * time.Millisecond,
		ReadTimeout:  50 * time.Millisecond,
		WriteTimeout: 50 * time.Millisecond,
	})

	mockRepo := new(mockCharacterRepository)
	service := &CharacterService{
		repo:           mockRepo,
		cache:          redisClient,
		characterCache: NewCharacterCache(redisClient, mockRepo),
		logger:         GetLogger(),
		keycloakURL:    "http://localhost:8080",
	}

	cleanup := func() {
		redisClient.Close()
		mr.Close()
	}

	return service, mockRepo, cleanup
}

