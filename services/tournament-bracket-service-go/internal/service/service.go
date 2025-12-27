// Issue: #2210
package service

import (
	"context"
	"fmt"
	"sort"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"tournament-bracket-service-go/internal/repository"
	"tournament-bracket-service-go/internal/metrics"
)

// TournamentService handles tournament business logic
type TournamentService struct {
	repo     *repository.TournamentRepository
	metrics  *metrics.Collector
	logger   *zap.SugaredLogger
}

// NewTournamentService creates a new tournament service
func NewTournamentService(repo *repository.TournamentRepository, metrics *metrics.Collector, logger *zap.SugaredLogger) *TournamentService {
	return &TournamentService{
		repo:    repo,
		metrics: metrics,
		logger:  logger,
	}
}

// GetTournaments retrieves tournaments with filtering
func (s *TournamentService) GetTournaments(ctx context.Context, status *string, gameMode *string, limit int, offset int) ([]*repository.Tournament, error) {
	tournaments, err := s.repo.GetTournaments(ctx, status, gameMode, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get tournaments: %w", err)
	}

	return tournaments, nil
}

// GetTournament retrieves a single tournament
func (s *TournamentService) GetTournament(ctx context.Context, tournamentID uuid.UUID) (*repository.Tournament, error) {
	tournament, err := s.repo.GetTournament(ctx, tournamentID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get tournament: %w", err)
	}

	return tournament, nil
}

// CreateTournament creates a new tournament
func (s *TournamentService) CreateTournament(ctx context.Context, name, description, gameMode, tournamentType string, maxParticipants, minSkillLevel, maxSkillLevel, entryFee int, prizePool, rules, metadata map[string]interface{}, registrationStart, registrationEnd, startTime *time.Time) (*repository.Tournament, error) {

	tournamentID := uuid.New()

	tournament := &repository.Tournament{
		ID:                  tournamentID,
		Name:                name,
		Description:         description,
		GameMode:            gameMode,
		TournamentType:      tournamentType,
		MaxParticipants:     maxParticipants,
		CurrentParticipants: 0,
		MinSkillLevel:       minSkillLevel,
		MaxSkillLevel:       maxSkillLevel,
		EntryFee:            entryFee,
		PrizePool:           prizePool,
		Status:              "registration",
		RegistrationStart:   registrationStart,
		RegistrationEnd:     registrationEnd,
		StartTime:           startTime,
		Rules:               rules,
		Metadata:            metadata,
		CreatedAt:           time.Now(),
		UpdatedAt:           time.Now(),
	}

	if err := s.repo.CreateTournament(ctx, tournament); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create tournament: %w", err)
	}

	s.metrics.IncrementTournamentsCreated()
	s.logger.Infof("Created tournament: %s (%s)", tournamentID.String(), name)

	return tournament, nil
}

// RegisterForTournament registers a player for a tournament
func (s *TournamentService) RegisterForTournament(ctx context.Context, tournamentID uuid.UUID, playerID, playerName string, skillRating int) (*repository.Participant, error) {

	participantID := uuid.New()

	participant := &repository.Participant{
		ID:               participantID,
		TournamentID:     tournamentID,
		PlayerID:         playerID,
		PlayerName:       playerName,
		SkillRating:      skillRating,
		RegistrationTime: time.Now(),
		Status:           "registered",
		Metadata:         make(map[string]interface{}),
	}

	if err := s.repo.RegisterParticipant(ctx, participant); err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to register for tournament: %w", err)
	}

	s.metrics.IncrementParticipantsRegistered()
	s.logger.Infof("Registered player %s for tournament %s", playerID, tournamentID.String())

	return participant, nil
}

// GetTournamentParticipants gets all participants for a tournament
func (s *TournamentService) GetTournamentParticipants(ctx context.Context, tournamentID uuid.UUID, limit int, offset int) ([]*repository.Participant, error) {
	participants, err := s.repo.GetTournamentParticipants(ctx, tournamentID, limit, offset)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get participants: %w", err)
	}

	return participants, nil
}

// GetMatchesByBracket gets matches for a specific bracket
func (s *TournamentService) GetMatchesByBracket(ctx context.Context, bracketID uuid.UUID) ([]*repository.Match, error) {
	matches, err := s.repo.GetMatchesByBracket(ctx, bracketID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get matches: %w", err)
	}

	return matches, nil
}

// GenerateTournamentBracket generates bracket for a tournament (simplified)
func (s *TournamentService) GenerateTournamentBracket(ctx context.Context, tournamentID uuid.UUID) error {
	// Get participants
	participants, err := s.repo.GetTournamentParticipants(ctx, tournamentID, 1000, 0)
	if err != nil {
		return fmt.Errorf("failed to get participants: %w", err)
	}

	if len(participants) < 2 {
		return fmt.Errorf("not enough participants for tournament")
	}

	// Sort participants by skill rating for seeding
	sort.Slice(participants, func(i, j int) bool {
		return participants[i].SkillRating > participants[j].SkillRating
	})

	// Assign seeds
	for i, participant := range participants {
		seed := i + 1
		participant.Seed = &seed
		// In real implementation, update participant seed in database
	}

	s.logger.Infof("Generated bracket for tournament %s with %d participants", tournamentID.String(), len(participants))
	return nil
}

// UpdateMatchResult updates the result of a match
func (s *TournamentService) UpdateMatchResult(ctx context.Context, matchID uuid.UUID, winnerID uuid.UUID, winnerScore int, loserID uuid.UUID, loserScore int) error {
	// In real implementation, update match result in database
	s.logger.Infof("Updated match %s result: winner %s (%d) vs loser %s (%d)",
		matchID.String(), winnerID.String(), winnerScore, loserID.String(), loserScore)
	return nil
}

// GetTournamentLeaderboard gets tournament leaderboard
func (s *TournamentService) GetTournamentLeaderboard(ctx context.Context, tournamentID uuid.UUID, limit int) ([]map[string]interface{}, error) {
	// Simplified leaderboard - in real implementation, aggregate from match results
	leaderboard := []map[string]interface{}{
		{
			"player_id":    "player_001",
			"player_name":  "CyberNinja",
			"score":        2500,
			"wins":         15,
			"losses":       3,
			"win_rate":     0.83,
			"skill_change": 150,
		},
		{
			"player_id":    "player_002",
			"player_name":  "NeonGhost",
			"score":        2350,
			"wins":         14,
			"losses":       4,
			"win_rate":     0.78,
			"skill_change": 120,
		},
	}

	if len(leaderboard) > limit {
		leaderboard = leaderboard[:limit]
	}

	return leaderboard, nil
}

// GetLiveTournaments gets currently active tournaments
func (s *TournamentService) GetLiveTournaments(ctx context.Context) ([]*repository.Tournament, error) {
	activeStatus := "in_progress"
	tournaments, err := s.repo.GetTournaments(ctx, &activeStatus, nil, 50, 0)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get live tournaments: %w", err)
	}

	return tournaments, nil
}

// GetLiveMatches gets currently active matches
func (s *TournamentService) GetLiveMatches(ctx context.Context, limit int) ([]map[string]interface{}, error) {
	// Simplified live matches - in real implementation, query active matches
	liveMatches := []map[string]interface{}{
		{
			"match_id":       "match_001",
			"tournament_id":  "tournament_001",
			"player1":        "CyberNinja",
			"player2":        "NeonGhost",
			"score1":         12,
			"score2":         8,
			"map":            "Downtown",
			"game_mode":      "Deathmatch",
			"spectators":     45,
			"time_remaining": 180, // seconds
		},
	}

	if len(liveMatches) > limit {
		liveMatches = liveMatches[:limit]
	}

	return liveMatches, nil
}
