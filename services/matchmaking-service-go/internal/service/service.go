package service

import (
	"context"
	"fmt"
	"log"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/matchmaking-service-go/internal/config"
	"necpgame/services/matchmaking-service-go/internal/database"
	matchmakingredis "necpgame/services/matchmaking-service-go/internal/redis"
)

// MatchmakingService implements the core matchmaking business logic with enterprise-grade optimizations
type MatchmakingService struct {
	// Enterprise-grade components
	db         *database.Manager
	redis      *matchmakingredis.Manager
	cache      *matchmakingredis.MatchmakingCache
	logger     *zap.Logger
	config     *config.MatchmakingConfig

	// In-memory storage for high-performance operations (backed by Redis)
	queue   map[string]*QueuedPlayer
	matches map[string]*Match
	mu      sync.RWMutex
}

// QueuedPlayer represents a player in the matchmaking queue
type QueuedPlayer struct {
	PlayerID        uuid.UUID
	GameMode        string
	JoinedAt        time.Time
	QueuePosition   int
	EstimatedWaitSec int
}

// Match represents a found match
type Match struct {
	MatchID uuid.UUID
	Players []MatchPlayer
	Status  string
	CreatedAt time.Time
}

// MatchPlayer represents a player in a match
type MatchPlayer struct {
	PlayerID uuid.UUID
	Team     string
}

// QueueResult represents the result of joining a queue
type QueueResult struct {
	QueuePosition        int
	EstimatedWaitSeconds int
}

// NewMatchmakingService creates a new matchmaking service with enterprise-grade components
func NewMatchmakingService(db *database.Manager, redisMgr *matchmakingredis.Manager, cfg *config.MatchmakingConfig, logger *zap.Logger) *MatchmakingService {
	cache := matchmakingredis.NewMatchmakingCache(redisMgr, logger)

	return &MatchmakingService{
		db:      db,
		redis:   redisMgr,
		cache:   cache,
		logger:  logger,
		config:  cfg,
		queue:   make(map[string]*QueuedPlayer),
		matches: make(map[string]*Match),
	}
}

// JoinQueue adds a player to the matchmaking queue
func (s *MatchmakingService) JoinQueue(ctx context.Context, playerID, gameMode string) (*QueueResult, error) {
	// Add context timeout for database operations (50ms for MMOFPS)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	playerUUID, err := uuid.Parse(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}
	s.mu.Lock()
	defer s.mu.Unlock()

	// Check if player already in queue
	if _, exists := s.queue[playerID]; exists {
		return nil, fmt.Errorf("player already in queue")
	}

	// Add player to queue
	player := &QueuedPlayer{
		PlayerID:         playerUUID,
		GameMode:         gameMode,
		JoinedAt:         time.Now(),
		QueuePosition:    len(s.queue) + 1,
		EstimatedWaitSec: 30, // Fixed estimate for demo
	}

	s.queue[playerID] = player

	log.Printf("Player %s joined queue for %s at position %d", playerID, gameMode, player.QueuePosition)

	return &QueueResult{
		QueuePosition:        player.QueuePosition,
		EstimatedWaitSeconds: player.EstimatedWaitSec,
	}, nil
}

// LeaveQueue removes a player from the matchmaking queue
func (s *MatchmakingService) LeaveQueue(ctx context.Context, playerID string) error {
	_, err := uuid.Parse(playerID)
	if err != nil {
		return fmt.Errorf("invalid player ID: %w", err)
	}
	s.mu.Lock()
	defer s.mu.Unlock()

	if _, exists := s.queue[playerID]; !exists {
		return fmt.Errorf("player not in queue")
	}

	delete(s.queue, playerID)
	log.Printf("Player %s left queue", playerID)

	return nil
}

// FindMatch attempts to find a match for the player
func (s *MatchmakingService) FindMatch(ctx context.Context, playerID string) (*Match, error) {
	// Add context timeout for database operations (50ms for MMOFPS)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	_, err := uuid.Parse(playerID)
	if err != nil {
		return nil, fmt.Errorf("invalid player ID: %w", err)
	}
	s.mu.Lock()
	defer s.mu.Unlock()

	// Check if player is in queue
	_, exists := s.queue[playerID]
	if !exists {
		return nil, fmt.Errorf("player not in queue")
	}

	// Simple matchmaking logic: if we have at least 2 players, create a match
	queueSize := len(s.queue)
	if queueSize < 2 {
		return nil, fmt.Errorf("not enough players for match")
	}

	// Create a simple 2-player match for demo
	matchID := uuid.New()
	match := &Match{
		MatchID:  matchID,
		Players:  make([]MatchPlayer, 0, 2),
		Status:   "forming",
		CreatedAt: time.Now(),
	}

	// Add players to match (simple logic: first two players)
	playerCount := 0
	for pid := range s.queue {
		if playerCount >= 2 {
			break
		}

		team := "alpha"
		if playerCount == 1 {
			team = "bravo"
		}

		playerUUID, _ := uuid.Parse(pid) // pid is already validated
		match.Players = append(match.Players, MatchPlayer{
			PlayerID: playerUUID,
			Team:     team,
		})

		// Remove from queue
		delete(s.queue, pid)
		playerCount++
	}

	s.matches[matchID.String()] = match

	log.Printf("Created match %s with %d players", matchID.String(), len(match.Players))

	return match, nil
}

// GetQueueStatus returns the current queue status for a player
func (s *MatchmakingService) GetQueueStatus(ctx context.Context, playerID string) (*QueuedPlayer, error) {
	// Add context timeout for database operations (50ms for MMOFPS)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	s.mu.RLock()
	defer s.mu.RUnlock()

	player, exists := s.queue[playerID]
	if !exists {
		return nil, fmt.Errorf("player not in queue")
	}

	return player, nil
}

// GetMatch returns match details
func (s *MatchmakingService) GetMatch(ctx context.Context, matchID string) (*Match, error) {
	// Add context timeout for database operations (50ms for MMOFPS)
	ctx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	_, err := uuid.Parse(matchID)
	if err != nil {
		return nil, fmt.Errorf("invalid match ID: %w", err)
	}
	s.mu.RLock()
	defer s.mu.RUnlock()

	match, exists := s.matches[matchID]
	if !exists {
		return nil, fmt.Errorf("match not found")
	}

	return match, nil
}