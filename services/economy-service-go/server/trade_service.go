package server

import (
	"context"
	"encoding/json"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/economy-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type TradeRepositoryInterface interface {
	Create(ctx context.Context, session *models.TradeSession) error
	GetByID(ctx context.Context, id uuid.UUID) (*models.TradeSession, error)
	GetActiveByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error)
	Update(ctx context.Context, session *models.TradeSession) error
	UpdateStatus(ctx context.Context, id uuid.UUID, status models.TradeStatus) error
	CreateHistory(ctx context.Context, history *models.TradeHistory) error
	GetHistoryByCharacter(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.TradeHistory, error)
	CountHistoryByCharacter(ctx context.Context, characterID uuid.UUID) (int, error)
	CleanupExpired(ctx context.Context) error
}

type TradeService struct {
	repo     TradeRepositoryInterface
	cache    *redis.Client
	logger   *logrus.Logger
	eventBus EventBus
}

func NewTradeService(dbURL, redisURL string) (*TradeService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewTradeRepository(dbPool)
	eventBus := NewRedisEventBus(redisClient)

	return &TradeService{
		repo:     repo,
		cache:    redisClient,
		logger:   GetLogger(),
		eventBus: eventBus,
	}, nil
}

func (s *TradeService) CreateTrade(ctx context.Context, initiatorID uuid.UUID, req *models.CreateTradeRequest) (*models.TradeSession, error) {
	active, err := s.repo.GetActiveByCharacter(ctx, initiatorID)
	if err != nil {
		return nil, err
	}
	if len(active) > 0 {
		return nil, nil
	}

	active, err = s.repo.GetActiveByCharacter(ctx, req.RecipientID)
	if err != nil {
		return nil, err
	}
	if len(active) > 0 {
		return nil, nil
	}

	now := time.Now()
	session := &models.TradeSession{
		ID:                uuid.New(),
		InitiatorID:       initiatorID,
		RecipientID:       req.RecipientID,
		InitiatorOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		RecipientOffer:    models.TradeOffer{Items: []map[string]interface{}{}, Currency: make(map[string]int)},
		InitiatorConfirmed: false,
		RecipientConfirmed:  false,
		Status:            models.TradeStatusPending,
		ZoneID:            req.ZoneID,
		CreatedAt:         now,
		UpdatedAt:         now,
		ExpiresAt:         now.Add(5 * time.Minute),
	}

	err = s.repo.Create(ctx, session)
	if err != nil {
		return nil, err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"trade_id":     session.ID.String(),
			"initiator_id": initiatorID.String(),
			"recipient_id": req.RecipientID.String(),
			"zone_id":      nil,
			"timestamp":    session.CreatedAt.Format(time.RFC3339),
		}
		if session.ZoneID != nil {
			payload["zone_id"] = session.ZoneID.String()
		}
		s.eventBus.PublishEvent(ctx, "trade:started", payload)
	}

	RecordTrade(string(session.Status))
	s.invalidateTradeCache(ctx, initiatorID)
	s.invalidateTradeCache(ctx, req.RecipientID)

	return session, nil
}

func (s *TradeService) GetTrade(ctx context.Context, id uuid.UUID) (*models.TradeSession, error) {
	return s.repo.GetByID(ctx, id)
}

func (s *TradeService) GetActiveTrades(ctx context.Context, characterID uuid.UUID) ([]models.TradeSession, error) {
	cacheKey := "trades:active:" + characterID.String()

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var sessions []models.TradeSession
		if err := json.Unmarshal([]byte(cached), &sessions); err == nil {
			return sessions, nil
		} else {
			s.logger.WithError(err).Error("Failed to unmarshal cached sessions JSON")
		}
	}

	sessions, err := s.repo.GetActiveByCharacter(ctx, characterID)
	if err != nil {
		return nil, err
	}

	sessionsJSON, err := json.Marshal(sessions)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal sessions JSON")
	} else {
		s.cache.Set(ctx, cacheKey, sessionsJSON, 30*time.Second)
	}

	return sessions, nil
}

func (s *TradeService) UpdateOffer(ctx context.Context, sessionID, characterID uuid.UUID, req *models.UpdateTradeOfferRequest) (*models.TradeSession, error) {
	session, err := s.repo.GetByID(ctx, sessionID)
	if err != nil {
		return nil, err
	}
	if session == nil {
		return nil, nil
	}

	if session.Status != models.TradeStatusPending && session.Status != models.TradeStatusActive {
		return nil, nil
	}

	if session.InitiatorID == characterID {
		if req.Items != nil {
			session.InitiatorOffer.Items = req.Items
		}
		if req.Currency != nil {
			session.InitiatorOffer.Currency = req.Currency
		}
		session.InitiatorConfirmed = false
		session.Status = models.TradeStatusActive
	} else if session.RecipientID == characterID {
		if req.Items != nil {
			session.RecipientOffer.Items = req.Items
		}
		if req.Currency != nil {
			session.RecipientOffer.Currency = req.Currency
		}
		session.RecipientConfirmed = false
		session.Status = models.TradeStatusActive
	} else {
		return nil, nil
	}

	session.UpdatedAt = time.Now()
	err = s.repo.Update(ctx, session)
	if err != nil {
		return nil, err
	}

	RecordTrade(string(session.Status))
	s.invalidateTradeCache(ctx, session.InitiatorID)
	s.invalidateTradeCache(ctx, session.RecipientID)

	return session, nil
}

func (s *TradeService) ConfirmTrade(ctx context.Context, sessionID, characterID uuid.UUID) (*models.TradeSession, error) {
	session, err := s.repo.GetByID(ctx, sessionID)
	if err != nil {
		return nil, err
	}
	if session == nil {
		return nil, nil
	}

	if session.Status != models.TradeStatusActive && session.Status != models.TradeStatusConfirmed {
		return nil, nil
	}

	if session.InitiatorID == characterID {
		session.InitiatorConfirmed = true
	} else if session.RecipientID == characterID {
		session.RecipientConfirmed = true
	} else {
		return nil, nil
	}

	if session.InitiatorConfirmed && session.RecipientConfirmed {
		session.Status = models.TradeStatusConfirmed
	} else {
		session.Status = models.TradeStatusActive
	}

	session.UpdatedAt = time.Now()
	err = s.repo.Update(ctx, session)
	if err != nil {
		return nil, err
	}

	RecordTrade(string(session.Status))
	s.invalidateTradeCache(ctx, session.InitiatorID)
	s.invalidateTradeCache(ctx, session.RecipientID)

	return session, nil
}

func (s *TradeService) CompleteTrade(ctx context.Context, sessionID uuid.UUID) error {
	session, err := s.repo.GetByID(ctx, sessionID)
	if err != nil {
		return err
	}
	if session == nil {
		return nil
	}

	if session.Status != models.TradeStatusConfirmed {
		return nil
	}

	now := time.Now()
	session.Status = models.TradeStatusCompleted
	session.CompletedAt = &now
	session.UpdatedAt = now

	err = s.repo.Update(ctx, session)
	if err != nil {
		return err
	}

	history := &models.TradeHistory{
		ID:             uuid.New(),
		TradeSessionID: session.ID,
		InitiatorID:    session.InitiatorID,
		RecipientID:    session.RecipientID,
		InitiatorOffer: session.InitiatorOffer,
		RecipientOffer: session.RecipientOffer,
		Status:         models.TradeStatusCompleted,
		ZoneID:         session.ZoneID,
		CreatedAt:      session.CreatedAt,
		CompletedAt:    now,
	}

	err = s.repo.CreateHistory(ctx, history)
	if err != nil {
		return err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"trade_id":     session.ID.String(),
			"initiator_id": session.InitiatorID.String(),
			"recipient_id": session.RecipientID.String(),
			"zone_id":      nil,
			"timestamp":    now.Format(time.RFC3339),
		}
		if session.ZoneID != nil {
			payload["zone_id"] = session.ZoneID.String()
		}
		s.eventBus.PublishEvent(ctx, "trade:completed", payload)
	}

	RecordTradeCompleted()
	s.invalidateTradeCache(ctx, session.InitiatorID)
	s.invalidateTradeCache(ctx, session.RecipientID)

	return nil
}

func (s *TradeService) CancelTrade(ctx context.Context, sessionID, characterID uuid.UUID) error {
	session, err := s.repo.GetByID(ctx, sessionID)
	if err != nil {
		return err
	}
	if session == nil {
		return nil
	}

	if session.InitiatorID != characterID && session.RecipientID != characterID {
		return nil
	}

	if session.Status == models.TradeStatusCompleted || session.Status == models.TradeStatusCancelled {
		return nil
	}

	session.Status = models.TradeStatusCancelled
	session.UpdatedAt = time.Now()

	err = s.repo.UpdateStatus(ctx, sessionID, models.TradeStatusCancelled)
	if err != nil {
		return err
	}

	if s.eventBus != nil {
		payload := map[string]interface{}{
			"trade_id":     sessionID.String(),
			"initiator_id": session.InitiatorID.String(),
			"recipient_id": session.RecipientID.String(),
			"cancelled_by": characterID.String(),
			"zone_id":      nil,
			"timestamp":    session.UpdatedAt.Format(time.RFC3339),
		}
		if session.ZoneID != nil {
			payload["zone_id"] = session.ZoneID.String()
		}
		s.eventBus.PublishEvent(ctx, "trade:cancelled", payload)
	}

	RecordTrade(string(session.Status))
	s.invalidateTradeCache(ctx, session.InitiatorID)
	s.invalidateTradeCache(ctx, session.RecipientID)

	return nil
}

func (s *TradeService) GetTradeHistory(ctx context.Context, characterID uuid.UUID, limit, offset int) (*models.TradeHistoryListResponse, error) {
	cacheKey := "trade_history:" + characterID.String() + ":limit:" + strconv.Itoa(limit) + ":offset:" + strconv.Itoa(offset)

	cached, err := s.cache.Get(ctx, cacheKey).Result()
	if err == nil && cached != "" {
		var response models.TradeHistoryListResponse
		if err := json.Unmarshal([]byte(cached), &response); err == nil {
			return &response, nil
		} else {
			s.logger.WithError(err).Error("Failed to unmarshal cached trade history JSON")
		}
	}

	history, err := s.repo.GetHistoryByCharacter(ctx, characterID, limit, offset)
	if err != nil {
		return nil, err
	}

	total, err := s.repo.CountHistoryByCharacter(ctx, characterID)
	if err != nil {
		return nil, err
	}

	response := &models.TradeHistoryListResponse{
		History: history,
		Total:   total,
	}

	responseJSON, err := json.Marshal(response)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal trade history response JSON")
	} else {
		s.cache.Set(ctx, cacheKey, responseJSON, 5*time.Minute)
	}

	return response, nil
}

func (s *TradeService) invalidateTradeCache(ctx context.Context, characterID uuid.UUID) {
	pattern := "trades:active:" + characterID.String()
	s.cache.Del(ctx, pattern)
	pattern = "trade_history:" + characterID.String() + ":*"
	keys, _ := s.cache.Keys(ctx, pattern).Result()
	if len(keys) > 0 {
		s.cache.Del(ctx, keys...)
	}
}

