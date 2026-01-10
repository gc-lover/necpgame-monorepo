package service

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/tournament-service-go/internal/config"
	"necpgame/services/tournament-service-go/internal/database"
	tournamentredis "necpgame/services/tournament-service-go/internal/redis"
)

// TournamentService implements enterprise-grade tournament management
type TournamentService struct {
	// Enterprise-grade components
	db         *database.Manager
	redis      *tournamentredis.Manager
	cache      *tournamentredis.TournamentCache
	logger     *zap.Logger
	config     *config.TournamentConfig

	// In-memory storage for high-performance operations (backed by Redis)
	tournaments map[string]*Tournament
	matches     map[string]*Match
	mu          sync.RWMutex
}

// Tournament represents a competitive tournament
type Tournament struct {
	ID            uuid.UUID `json:"id"`
	Name          string    `json:"name"`
	Description   string    `json:"description"`
	GameMode      string    `json:"gameMode"`
	MaxPlayers    int       `json:"maxPlayers"`
	CurrentPlayers int      `json:"currentPlayers"`
	Status        string    `json:"status"` // pending, active, completed, cancelled
	BracketType   string    `json:"bracketType"` // single_elimination, double_elimination, round_robin
	StartTime     time.Time `json:"startTime"`
	EndTime       time.Time `json:"endTime"`
	PrizePool     float64   `json:"prizePool"`
	CreatedAt     time.Time `json:"createdAt"`
	UpdatedAt     time.Time `json:"updatedAt"`
}

// Match represents a tournament match
type Match struct {
	ID            uuid.UUID   `json:"id"`
	TournamentID  uuid.UUID   `json:"tournamentId"`
	Round         int         `json:"round"`
	Players       []MatchPlayer `json:"players"`
	Status        string      `json:"status"` // pending, in_progress, completed
	Winner        *uuid.UUID  `json:"winner,omitempty"`
	Score         map[string]int `json:"score"`
	StartTime     time.Time   `json:"startTime"`
	EndTime       *time.Time  `json:"endTime,omitempty"`
	CreatedAt     time.Time   `json:"createdAt"`
}

// MatchPlayer represents a player in a match
type MatchPlayer struct {
	PlayerID uuid.UUID `json:"playerId"`
	Team     string    `json:"team"`
	Score    int       `json:"score"`
}

// PlayerQueue represents a player waiting for tournament matchmaking
type PlayerQueue struct {
	PlayerID       uuid.UUID `json:"playerId"`
	TournamentID   uuid.UUID `json:"tournamentId"`
	JoinedAt       time.Time `json:"joinedAt"`
	QueuePosition  int       `json:"queuePosition"`
	EstimatedWait  time.Duration `json:"estimatedWait"`
}

// LeaderboardEntry represents a player's leaderboard position
type LeaderboardEntry struct {
	PlayerID    uuid.UUID `json:"playerId"`
	PlayerName  string    `json:"playerName"`
	Score       int       `json:"score"`
	Rank        int       `json:"rank"`
	Wins        int       `json:"wins"`
	Losses      int       `json:"losses"`
	WinRate     float64   `json:"winRate"`
	LastUpdated time.Time `json:"lastUpdated"`
}

// TournamentStats represents comprehensive tournament statistics
type TournamentStats struct {
	TournamentID             uuid.UUID `json:"tournamentId"`
	TotalParticipants        int       `json:"totalParticipants"`
	ActiveParticipants       int       `json:"activeParticipants"`
	CompletedMatches         int       `json:"completedMatches"`
	AverageMatchDuration     time.Duration `json:"averageMatchDuration"`
	TotalPrizePool           float64   `json:"totalPrizePool"`
	SpectatorCount           int       `json:"spectatorCount"`
	LastUpdated              time.Time `json:"lastUpdated"`
}

// NewTournamentService creates a new tournament service with enterprise-grade components
func NewTournamentService(db *database.Manager, redis *tournamentredis.Manager, cfg *config.TournamentConfig, logger *zap.Logger) *TournamentService {
	cache := tournamentredis.NewTournamentCache(redis, logger)

	return &TournamentService{
		db:          db,
		redis:       redis,
		cache:       cache,
		logger:      logger,
		config:      cfg,
		tournaments: make(map[string]*Tournament),
		matches:     make(map[string]*Match),
	}
}

// CreateTournament creates a new tournament with enterprise-grade validation
func (s *TournamentService) CreateTournament(ctx context.Context, name, description, gameMode string, maxPlayers int, bracketType string, startTime time.Time) (*Tournament, error) {
	// Validate context timeout
	if deadline, ok := ctx.Deadline(); ok && time.Until(deadline) < 5*time.Second {
		return nil, fmt.Errorf("insufficient time for operation")
	}

	// Validate input parameters
	if name == "" {
		return nil, fmt.Errorf("tournament name is required")
	}
	if gameMode == "" {
		return nil, fmt.Errorf("game mode is required")
	}
	if maxPlayers <= 0 || maxPlayers > s.config.MaxPlayersPerTournament {
		return nil, fmt.Errorf("invalid max players: must be between 1 and %d", s.config.MaxPlayersPerTournament)
	}
	if bracketType == "" {
		bracketType = "single_elimination" // default
	}

	// Check concurrent tournament limit
	s.mu.RLock()
	activeCount := 0
	for _, t := range s.tournaments {
		if t.Status == "active" || t.Status == "pending" {
			activeCount++
		}
	}
	s.mu.RUnlock()

	if activeCount >= s.config.MaxConcurrentTournaments {
		return nil, fmt.Errorf("maximum concurrent tournaments limit reached: %d", s.config.MaxConcurrentTournaments)
	}

	// Create tournament
	tournamentID := uuid.New()
	tournament := &Tournament{
		ID:             tournamentID,
		Name:           name,
		Description:    description,
		GameMode:       gameMode,
		MaxPlayers:     maxPlayers,
		CurrentPlayers: 0,
		Status:         "pending",
		BracketType:    bracketType,
		StartTime:      startTime,
		EndTime:        startTime.Add(2 * time.Hour), // default 2 hours
		PrizePool:      0,
		CreatedAt:      time.Now(),
		UpdatedAt:      time.Now(),
	}

	// Store in memory
	s.mu.Lock()
	s.tournaments[tournamentID.String()] = tournament
	s.mu.Unlock()

	// Cache tournament data
	cacheCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	if err := s.cache.SetTournament(cacheCtx, tournamentID.String(), tournament); err != nil {
		s.logger.Warn("Failed to cache tournament, continuing with in-memory only",
			zap.String("tournamentID", tournamentID.String()), zap.Error(err))
	}

	s.logger.Info("Tournament created",
		zap.String("tournamentID", tournamentID.String()),
		zap.String("name", name),
		zap.String("gameMode", gameMode),
		zap.Int("maxPlayers", maxPlayers),
		zap.String("bracketType", bracketType))

	return tournament, nil
}

// GetTournament retrieves tournament information with caching
func (s *TournamentService) GetTournament(ctx context.Context, tournamentID string) (*Tournament, error) {
	// Validate UUID
	if _, err := uuid.Parse(tournamentID); err != nil {
		return nil, fmt.Errorf("invalid tournament ID: %w", err)
	}

	// Try cache first
	var cachedTournament Tournament
	err := s.cache.GetTournament(ctx, tournamentID, &cachedTournament)
	if err == nil {
		s.logger.Debug("Tournament retrieved from cache", zap.String("tournamentID", tournamentID))
		return &cachedTournament, nil
	}

	// Check in-memory storage
	s.mu.RLock()
	tournament, exists := s.tournaments[tournamentID]
	s.mu.RUnlock()

	if exists {
		// Cache for future requests
		cacheCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
		defer cancel()

		if cacheErr := s.cache.SetTournament(cacheCtx, tournamentID, tournament); cacheErr != nil {
			s.logger.Warn("Failed to cache tournament",
				zap.String("tournamentID", tournamentID), zap.Error(cacheErr))
		}

		return tournament, nil
	}

	return nil, fmt.Errorf("tournament not found")
}

// JoinTournament adds a player to a tournament
func (s *TournamentService) JoinTournament(ctx context.Context, tournamentID, playerID string) error {
	// Validate UUIDs
	_, err := uuid.Parse(tournamentID)
	if err != nil {
		return fmt.Errorf("invalid tournament ID: %w", err)
	}

	_, err = uuid.Parse(playerID)
	if err != nil {
		return fmt.Errorf("invalid player ID: %w", err)
	}

	s.mu.Lock()
	defer s.mu.Unlock()

	tournament, exists := s.tournaments[tournamentID]
	if !exists {
		return fmt.Errorf("tournament not found")
	}

	if tournament.Status != "pending" {
		return fmt.Errorf("tournament is not accepting new players")
	}

	if tournament.CurrentPlayers >= tournament.MaxPlayers {
		return fmt.Errorf("tournament is full")
	}

	tournament.CurrentPlayers++
	tournament.UpdatedAt = time.Now()

	// Invalidate cache
	cacheCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	if err := s.cache.DeleteTournament(cacheCtx, tournamentID); err != nil {
		s.logger.Warn("Failed to invalidate tournament cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
	}

	s.logger.Info("Player joined tournament",
		zap.String("tournamentID", tournamentID),
		zap.String("playerID", playerID),
		zap.Int("currentPlayers", tournament.CurrentPlayers))

	return nil
}

// StartTournament begins tournament competition
func (s *TournamentService) StartTournament(ctx context.Context, tournamentID string) error {
	_, err := uuid.Parse(tournamentID)
	if err != nil {
		return fmt.Errorf("invalid tournament ID: %w", err)
	}

	s.mu.Lock()
	defer s.mu.Unlock()

	tournament, exists := s.tournaments[tournamentID]
	if !exists {
		return fmt.Errorf("tournament not found")
	}

	if tournament.Status != "pending" {
		return fmt.Errorf("tournament cannot be started")
	}

	if tournament.CurrentPlayers < 2 {
		return fmt.Errorf("not enough players to start tournament")
	}

	tournament.Status = "active"
	tournament.UpdatedAt = time.Now()

	// Generate initial matches based on bracket type
	if err := s.generateInitialMatches(tournament); err != nil {
		return fmt.Errorf("failed to generate initial matches: %w", err)
	}

	// Invalidate cache
	cacheCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	if err := s.cache.DeleteTournament(cacheCtx, tournamentID); err != nil {
		s.logger.Warn("Failed to invalidate tournament cache",
			zap.String("tournamentID", tournamentID), zap.Error(err))
	}

	s.logger.Info("Tournament started",
		zap.String("tournamentID", tournamentID),
		zap.Int("playerCount", tournament.CurrentPlayers))

	return nil
}

// generateInitialMatches creates initial tournament matches
func (s *TournamentService) generateInitialMatches(tournament *Tournament) error {
	// Simple implementation - create matches for all players
	// In a real implementation, this would follow bracket logic

	playerCount := tournament.CurrentPlayers
	if playerCount%2 != 0 {
		playerCount-- // Handle odd number of players
	}

	for i := 0; i < playerCount; i += 2 {
		matchID := uuid.New()
		match := &Match{
			ID:           matchID,
			TournamentID: tournament.ID,
			Round:        1,
			Players:      make([]MatchPlayer, 2),
			Status:       "pending",
			Score:        make(map[string]int),
			StartTime:    time.Now(),
			CreatedAt:    time.Now(),
		}

		// Add placeholder players (in real implementation, would assign actual players)
		for j := 0; j < 2; j++ {
			match.Players[j] = MatchPlayer{
				PlayerID: uuid.New(), // Placeholder
				Team:     fmt.Sprintf("team_%d", j+1),
				Score:    0,
			}
		}

		s.matches[matchID.String()] = match
	}

	return nil
}

// GetTournamentLeaderboard returns tournament leaderboard
func (s *TournamentService) GetTournamentLeaderboard(ctx context.Context, tournamentID string, limit int) ([]LeaderboardEntry, error) {
	if _, err := uuid.Parse(tournamentID); err != nil {
		return nil, fmt.Errorf("invalid tournament ID: %w", err)
	}

	// Try cache first
	var cachedLeaderboard []LeaderboardEntry
	err := s.cache.GetLeaderboard(ctx, tournamentID, &cachedLeaderboard)
	if err == nil && len(cachedLeaderboard) > 0 {
		s.logger.Debug("Leaderboard retrieved from cache", zap.String("tournamentID", tournamentID))
		return cachedLeaderboard, nil
	}

	// Generate mock leaderboard (in real implementation, would query database)
	leaderboard := make([]LeaderboardEntry, 0, limit)
	for i := 1; i <= limit && i <= 10; i++ {
		entry := LeaderboardEntry{
			PlayerID:    uuid.New(),
			PlayerName:  fmt.Sprintf("Player_%d", i),
			Score:       1000 - (i-1)*50,
			Rank:        i,
			Wins:        10 - (i-1),
			Losses:      i - 1,
			WinRate:     float64(10-(i-1)) / float64(10) * 100,
			LastUpdated: time.Now(),
		}
		leaderboard = append(leaderboard, entry)
	}

	// Cache leaderboard
	cacheCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	if err := s.cache.SetLeaderboard(cacheCtx, tournamentID, leaderboard); err != nil {
		s.logger.Warn("Failed to cache leaderboard",
			zap.String("tournamentID", tournamentID), zap.Error(err))
	}

	return leaderboard, nil
}

// GetTournamentStats returns comprehensive tournament statistics
func (s *TournamentService) GetTournamentStats(ctx context.Context, tournamentID string) (*TournamentStats, error) {
	if _, err := uuid.Parse(tournamentID); err != nil {
		return nil, fmt.Errorf("invalid tournament ID: %w", err)
	}

	s.mu.RLock()
	tournament, exists := s.tournaments[tournamentID]
	s.mu.RUnlock()

	if !exists {
		return nil, fmt.Errorf("tournament not found")
	}

	// Calculate stats
	stats := &TournamentStats{
		TournamentID:         tournament.ID,
		TotalParticipants:    tournament.CurrentPlayers,
		ActiveParticipants:   tournament.CurrentPlayers, // Simplified
		CompletedMatches:     0,                         // Would calculate from matches
		AverageMatchDuration: 15 * time.Minute,          // Mock data
		TotalPrizePool:       tournament.PrizePool,
		SpectatorCount:       0, // Would track spectators
		LastUpdated:          time.Now(),
	}

	return stats, nil
}

// HealthCheck performs service health validation
func (s *TournamentService) HealthCheck(ctx context.Context) error {
	// Test database connection
	if err := s.db.HealthCheck(ctx); err != nil {
		return fmt.Errorf("database health check failed: %w", err)
	}

	// Test Redis connection
	if err := s.redis.HealthCheck(ctx); err != nil {
		return fmt.Errorf("redis health check failed: %w", err)
	}

	s.logger.Debug("Tournament service health check passed")
	return nil
}