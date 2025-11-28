package server

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/movement-service-go/models"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type MovementRepositoryInterface interface {
	GetPositionByCharacterID(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error)
	SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error)
	GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error)
}

type MovementService struct {
	repo           MovementRepositoryInterface
	cache          *redis.Client
	logger         *logrus.Logger
	gatewayURL     string
	updateInterval time.Duration
	positions      map[string]*models.EntityState
	positionsMu    sync.RWMutex
	gatewayConn    *websocket.Conn
	gatewayConnMu  sync.RWMutex
}

func NewMovementService(dbURL, redisURL, gatewayURL string, updateInterval time.Duration) (*MovementService, error) {
	dbPool, err := pgxpool.New(context.Background(), dbURL)
	if err != nil {
		return nil, err
	}

	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	repo := NewMovementRepository(dbPool)

	return &MovementService{
		repo:           repo,
		cache:          redisClient,
		logger:         GetLogger(),
		gatewayURL:     gatewayURL,
		updateInterval: updateInterval,
		positions:      make(map[string]*models.EntityState),
	}, nil
}

func (s *MovementService) GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	pos, err := s.repo.GetPositionByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return pos, nil
}

func (s *MovementService) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	pos, err := s.repo.SavePosition(ctx, characterID, req)
	if err != nil {
		return nil, err
	}

	cacheKey := "position:" + characterID.String()
	posJSON, err := json.Marshal(pos)
	if err != nil {
		s.logger.WithError(err).Error("Failed to marshal position JSON")
		return nil, err
	}
	if err := s.cache.Set(ctx, cacheKey, posJSON, 5*time.Minute).Err(); err != nil {
		s.logger.WithError(err).Error("Failed to cache position")
	}

	RecordPositionSaved()

	return pos, nil
}

func (s *MovementService) GetPositionHistory(ctx context.Context, characterID uuid.UUID, limit int) ([]models.PositionHistory, error) {
	history, err := s.repo.GetPositionHistory(ctx, characterID, limit)
	if err != nil {
		return nil, err
	}

	return history, nil
}

func (s *MovementService) StartGatewayConnection(ctx context.Context) error {
	dialer := websocket.Dialer{
		HandshakeTimeout: 10 * time.Second,
	}

	conn, _, err := dialer.Dial(s.gatewayURL, nil)
	if err != nil {
		s.logger.WithError(err).Error("Failed to connect to gateway")
		return err
	}

	s.gatewayConnMu.Lock()
	s.gatewayConn = conn
	s.gatewayConnMu.Unlock()

	s.logger.Info("Connected to gateway")

	go s.readGameStateFromGateway(ctx, conn)

	return nil
}

func (s *MovementService) readGameStateFromGateway(ctx context.Context, conn *websocket.Conn) {
	defer conn.Close()

	for {
		select {
		case <-ctx.Done():
			s.logger.Info("Context cancelled, closing gateway connection")
			return
		default:
		}

		_, data, err := conn.ReadMessage()
		if err != nil {
			s.logger.WithError(err).Error("Failed to read message from gateway")
			return
		}

		var gameState struct {
			GameState struct {
				Snapshot struct {
					Entities []models.EntityState `json:"entities"`
				} `json:"snapshot"`
				Delta struct {
					Changed []models.EntityState `json:"changed"`
				} `json:"delta"`
			} `json:"game_state"`
		}

		if err := json.Unmarshal(data, &gameState); err != nil {
			s.logger.WithError(err).Debug("Failed to parse GameState message")
			continue
		}

		var entities []models.EntityState
		if len(gameState.GameState.Snapshot.Entities) > 0 {
			entities = gameState.GameState.Snapshot.Entities
		} else if len(gameState.GameState.Delta.Changed) > 0 {
			entities = gameState.GameState.Delta.Changed
		}

		s.positionsMu.Lock()
		for _, entity := range entities {
			s.positions[entity.ID] = &entity
		}
		s.positionsMu.Unlock()

		RecordPositionReceivedFromGateway()
	}
}

func (s *MovementService) StartPositionSaver(ctx context.Context) error {
	ticker := time.NewTicker(s.updateInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			s.logger.Info("Context cancelled, stopping position saver")
			return nil
		case <-ticker.C:
			s.saveAllPositions(ctx)
		}
	}
}

func (s *MovementService) saveAllPositions(ctx context.Context) {
	s.positionsMu.RLock()
	positionsCopy := make(map[string]*models.EntityState, len(s.positions))
	for id, pos := range s.positions {
		positionsCopy[id] = pos
	}
	s.positionsMu.RUnlock()

	for entityID, entityState := range positionsCopy {
		characterID, err := uuid.Parse(entityID)
		if err != nil {
			s.logger.WithError(err).WithField("entity_id", entityID).Warn("Invalid entity ID, skipping")
			continue
		}

		req := &models.SavePositionRequest{
			PositionX: float64(entityState.X),
			PositionY: float64(entityState.Y),
			PositionZ: float64(entityState.Z),
			Yaw:       float64(entityState.Yaw),
			VelocityX: float64(entityState.VX),
			VelocityY: float64(entityState.VY),
			VelocityZ: float64(entityState.VZ),
		}

		_, err = s.repo.SavePosition(ctx, characterID, req)
		if err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to save position")
			RecordError("save_position_failed")
			continue
		}

		cacheKey := "position:" + characterID.String()
		posJSON, err := json.Marshal(req)
		if err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to marshal position JSON")
			RecordError("marshal_position_failed")
			continue
		}
		if err := s.cache.Set(ctx, cacheKey, posJSON, 5*time.Minute).Err(); err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to cache position")
			RecordError("cache_position_failed")
		}
	}

	s.logger.WithField("count", len(positionsCopy)).Debug("Saved positions to database")
}

func (s *MovementService) Shutdown() {
	s.gatewayConnMu.Lock()
	if s.gatewayConn != nil {
		s.gatewayConn.Close()
	}
	s.gatewayConnMu.Unlock()
}
