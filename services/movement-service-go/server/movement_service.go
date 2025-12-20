package server

import (
	"context"
	"encoding/json"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/movement-service-go/models"
	"github.com/google/uuid"
	"github.com/gorilla/websocket"
	"github.com/jackc/pgx/v5/pgxpool"
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

	// Memory pooling for hot path structs (Issue #1607)
	positionPool sync.Pool
}

func NewMovementService(dbURL, redisURL, gatewayURL string, updateInterval time.Duration) (*MovementService, error) {
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

	repo := NewMovementRepository(dbPool)

	s := &MovementService{
		repo:           repo,
		cache:          redisClient,
		logger:         GetLogger(),
		gatewayURL:     gatewayURL,
		updateInterval: updateInterval,
		positions:      make(map[string]*models.EntityState),
	}

	// Initialize memory pool (zero allocations target!)
	s.positionPool = sync.Pool{
		New: func() interface{} {
			return &models.CharacterPosition{}
		},
	}

	return s, nil
}

func (s *MovementService) GetPosition(ctx context.Context, characterID uuid.UUID) (*models.CharacterPosition, error) {
	pos, err := s.repo.GetPositionByCharacterID(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return pos, nil
}

// SavePosition Issue: #1431
func (s *MovementService) SavePosition(ctx context.Context, characterID uuid.UUID, req *models.SavePositionRequest) (*models.CharacterPosition, error) {
	pos, err := s.repo.SavePosition(ctx, characterID, req)
	if err != nil {
		return nil, err
	}

	cacheKey := "position:" + characterID.String()
	posJSON, err := json.Marshal(pos)
	if err != nil {
		s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to marshal position for cache")
		RecordError("json_marshal_failed")
		return nil, err
	}

	if err := s.cache.Set(ctx, cacheKey, posJSON, 5*time.Minute).Err(); err != nil {
		s.logger.WithError(err).WithField("character_id", characterID).Warn("Failed to set position in cache")
		RecordError("cache_set_failed")
		// Не возвращаем ошибку, так как данные уже сохранены в БД
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

		pos, err := s.repo.SavePosition(ctx, characterID, req)
		if err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to save position")
			RecordError("save_position_failed")
			continue
		}

		cacheKey := "position:" + characterID.String()
		posJSON, err := json.Marshal(pos)
		if err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Error("Failed to marshal position for cache")
			RecordError("json_marshal_failed")
			// Продолжаем для других позиций, так как данные уже сохранены в БД
			continue
		}

		if err := s.cache.Set(ctx, cacheKey, posJSON, 5*time.Minute).Err(); err != nil {
			s.logger.WithError(err).WithField("character_id", characterID).Warn("Failed to set position in cache")
			RecordError("cache_set_failed")
			// Продолжаем для других позиций, так как данные уже сохранены в БД
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
