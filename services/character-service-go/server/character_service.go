package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/character-service-go/models"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type CharacterRepositoryInterface interface {
	GetAccountByID(ctx context.Context, accountID uuid.UUID) (*models.PlayerAccount, error)
	CreateAccount(ctx context.Context, req *models.CreateAccountRequest) (*models.PlayerAccount, error)
	GetCharacterByID(ctx context.Context, characterID uuid.UUID) (*models.Character, error)
	GetCharactersByAccountID(ctx context.Context, accountID uuid.UUID) ([]models.Character, error)
	CreateCharacter(ctx context.Context, req *models.CreateCharacterRequest) (*models.Character, error)
	UpdateCharacter(ctx context.Context, characterID uuid.UUID, req *models.UpdateCharacterRequest) (*models.Character, error)
	DeleteCharacter(ctx context.Context, characterID uuid.UUID) error
}

type CharacterService struct {
	repo  CharacterRepositoryInterface
	cache *redis.Client
	logger *logrus.Logger
	keycloakURL string
}

func NewCharacterService(dbURL, redisURL, keycloakURL string) (*CharacterService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewCharacterRepository(dbPool)

	return &CharacterService{
		repo:  repo,
		cache: redisClient,
		logger: GetLogger(),
		keycloakURL: keycloakURL,
	}, nil
}

func (s *CharacterService) GetAccount(ctx context.Context, accountID uuid.UUID) (*models.PlayerAccount, error) {
	cacheKey := "account:" + accountID.String()
	
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var account models.PlayerAccount
		if err := json.Unmarshal([]byte(cached), &account); err == nil {
			return &account, nil
		}
	}

	account, err := s.repo.GetAccountByID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	if account != nil {
		accountJSON, _ := json.Marshal(account)
		s.cache.Set(ctx, cacheKey, accountJSON, 5*time.Minute)
	}

	return account, nil
}

func (s *CharacterService) CreateAccount(ctx context.Context, req *models.CreateAccountRequest) (*models.PlayerAccount, error) {
	account, err := s.repo.CreateAccount(ctx, req)
	if err != nil {
		return nil, err
	}

	cacheKey := "account:" + account.ID.String()
	accountJSON, _ := json.Marshal(account)
	s.cache.Set(ctx, cacheKey, accountJSON, 5*time.Minute)

	return account, nil
}

func (s *CharacterService) GetCharactersByAccountID(ctx context.Context, accountID uuid.UUID) ([]models.Character, error) {
	cacheKey := "characters:account:" + accountID.String()
	
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var characters []models.Character
		if err := json.Unmarshal([]byte(cached), &characters); err == nil {
			return characters, nil
		}
	}

	characters, err := s.repo.GetCharactersByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	charactersJSON, _ := json.Marshal(characters)
	s.cache.Set(ctx, cacheKey, charactersJSON, 5*time.Minute)

	SetCharactersCount(accountID.String(), float64(len(characters)))

	return characters, nil
}

func (s *CharacterService) GetCharacter(ctx context.Context, characterID uuid.UUID) (*models.Character, error) {
	cacheKey := "character:" + characterID.String()
	
	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var char models.Character
		if err := json.Unmarshal([]byte(cached), &char); err == nil {
			return &char, nil
		}
	}

	char, err := s.repo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	if char != nil {
		charJSON, _ := json.Marshal(char)
		s.cache.Set(ctx, cacheKey, charJSON, 5*time.Minute)
	}

	return char, nil
}

func (s *CharacterService) CreateCharacter(ctx context.Context, req *models.CreateCharacterRequest) (*models.Character, error) {
	char, err := s.repo.CreateCharacter(ctx, req)
	if err != nil {
		return nil, err
	}

	s.invalidateAccountCache(ctx, req.AccountID)

	return char, nil
}

func (s *CharacterService) UpdateCharacter(ctx context.Context, characterID uuid.UUID, req *models.UpdateCharacterRequest) (*models.Character, error) {
	char, err := s.repo.UpdateCharacter(ctx, characterID, req)
	if err != nil {
		return nil, err
	}

	s.invalidateCharacterCache(ctx, characterID)
	if char != nil {
		s.invalidateAccountCache(ctx, char.AccountID)
	}

	return char, nil
}

func (s *CharacterService) DeleteCharacter(ctx context.Context, characterID uuid.UUID) error {
	char, err := s.repo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return err
	}

	err = s.repo.DeleteCharacter(ctx, characterID)
	if err != nil {
		return err
	}

	s.invalidateCharacterCache(ctx, characterID)
	if char != nil {
		s.invalidateAccountCache(ctx, char.AccountID)
	}

	return nil
}

func (s *CharacterService) ValidateCharacter(ctx context.Context, characterID uuid.UUID) (bool, error) {
	char, err := s.repo.GetCharacterByID(ctx, characterID)
	if err != nil {
		return false, err
	}

	return char != nil, nil
}

func (s *CharacterService) invalidateCharacterCache(ctx context.Context, characterID uuid.UUID) {
	cacheKey := "character:" + characterID.String()
	s.cache.Del(ctx, cacheKey)
}

func (s *CharacterService) SwitchCharacter(ctx context.Context, accountID, characterID uuid.UUID) (*models.SwitchCharacterResponse, error) {
	characters, err := s.repo.GetCharactersByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	var targetCharacter *models.Character
	for i := range characters {
		if characters[i].ID == characterID {
			targetCharacter = &characters[i]
			break
		}
	}

	if targetCharacter == nil {
		return &models.SwitchCharacterResponse{
			Success: false,
		}, nil
	}

	var previousCharacterID *uuid.UUID
	for i := range characters {
		if characters[i].ID != characterID {
			previousCharacterID = &characters[i].ID
			break
		}
	}

	s.invalidateAccountCache(ctx, accountID)
	s.invalidateCharacterCache(ctx, characterID)

	return &models.SwitchCharacterResponse{
		PreviousCharacterID: previousCharacterID,
		CurrentCharacter:    targetCharacter,
		Success:             true,
	}, nil
}

func (s *CharacterService) invalidateAccountCache(ctx context.Context, accountID uuid.UUID) {
	cacheKey := "account:" + accountID.String()
	s.cache.Del(ctx, cacheKey)
	
	charactersCacheKey := "characters:account:" + accountID.String()
	s.cache.Del(ctx, charactersCacheKey)
}
