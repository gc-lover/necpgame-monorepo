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
	repo                      CharacterRepositoryInterface
	engramRepo                EngramRepositoryInterface
	engramService             EngramServiceInterface
	engramSecurityRepo        EngramSecurityRepositoryInterface
	engramSecurityService     EngramSecurityServiceInterface
	engramCyberpsychosisRepo  EngramCyberpsychosisRepositoryInterface
	engramCyberpsychosisService EngramCyberpsychosisServiceInterface
	cache                     *redis.Client
	characterCache            *CharacterCache // Issue: #1609 - 3-tier cache
	logger                    *logrus.Logger
	keycloakURL               string
}

func NewCharacterService(dbURL, redisURL, keycloakURL string) (*CharacterService, error) {
	// Issue: #1605 - DB Connection Pool configuration
	config, err := pgxpool.ParseConfig(dbURL)
	if err != nil {
		return nil, err
	}
	config.MaxConns = 50
	config.MinConns = 10
	config.MaxConnLifetime = 5 * time.Minute
	config.MaxConnIdleTime = 1 * time.Minute
	
	dbPool, err := pgxpool.NewWithConfig(context.Background(), config)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewCharacterRepository(dbPool)
	engramRepo := NewEngramRepository(dbPool)
	engramService := NewEngramService(engramRepo, repo, redisClient)
	
	engramSecurityRepo := NewEngramSecurityRepository(dbPool)
	engramSecurityService := NewEngramSecurityService(engramSecurityRepo, redisClient)

	engramCyberpsychosisRepo := NewEngramCyberpsychosisRepository(dbPool)
	engramCyberpsychosisService := NewEngramCyberpsychosisService(engramCyberpsychosisRepo, engramService, redisClient)

	// Issue: #1609 - 3-tier cache (Memory → Redis → DB)
	characterCache := NewCharacterCache(redisClient, repo)

	return &CharacterService{
		repo:                      repo,
		engramRepo:                engramRepo,
		engramService:             engramService,
		engramSecurityRepo:        engramSecurityRepo,
		engramSecurityService:     engramSecurityService,
		engramCyberpsychosisRepo:  engramCyberpsychosisRepo,
		engramCyberpsychosisService: engramCyberpsychosisService,
		cache:                     redisClient,
		characterCache:            characterCache, // Issue: #1609
		logger:                    GetLogger(),
		keycloakURL:               keycloakURL,
	}, nil
}

func (s *CharacterService) GetEngramService() EngramServiceInterface {
	return s.engramService
}

func (s *CharacterService) GetEngramSecurityService() EngramSecurityServiceInterface {
	return s.engramSecurityService
}

func (s *CharacterService) GetEngramCyberpsychosisService() EngramCyberpsychosisServiceInterface {
	return s.engramCyberpsychosisService
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
	// Issue: #1609 - Use 3-tier cache (Memory → Redis → DB)
	characters, err := s.characterCache.GetByAccountID(ctx, accountID)
	if err != nil {
		return nil, err
	}

	SetCharactersCount(accountID.String(), float64(len(characters)))

	return characters, nil
}

func (s *CharacterService) GetCharacter(ctx context.Context, characterID uuid.UUID) (*models.Character, error) {
	// Issue: #1609 - Use 3-tier cache (Memory → Redis → DB)
	return s.characterCache.Get(ctx, characterID)
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
	// Issue: #1609 - Invalidate 3-tier cache
	s.characterCache.Invalidate(ctx, characterID)
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
	// Issue: #1609 - Invalidate 3-tier cache
	s.characterCache.InvalidateAccount(ctx, accountID)
	
	// Also invalidate account cache
	cacheKey := "account:" + accountID.String()
	s.cache.Del(ctx, cacheKey)
}
