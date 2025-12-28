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

// SPECTATOR MODE METHODS
// Issue: #2213 - Tournament Spectator Mode Implementation

// JoinSpectatorMode allows a player to join tournament match as spectator
func (s *TournamentService) JoinSpectatorMode(ctx context.Context, matchID uuid.UUID, playerID uuid.UUID, playerName string, viewMode string, isVIP bool) (*repository.Spectator, error) {
	s.logger.Infof("Player %s joining spectator mode for match %s", playerName, matchID)

	// Verify match exists and is active
	match, err := s.repo.GetMatch(ctx, matchID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get match: %w", err)
	}

	if match.Status != "in_progress" {
		return nil, fmt.Errorf("match is not active for spectating")
	}

	// Check spectator capacity
	maxSpectators := 100 // Configurable
	if match.SpectatorCount >= maxSpectators && !isVIP {
		return nil, fmt.Errorf("spectator capacity reached")
	}

	// Create spectator record
	spectator := &repository.Spectator{
		ID:         uuid.New().String(),
		MatchID:    matchID.String(),
		PlayerID:   playerID.String(),
		PlayerName: playerName,
		JoinedAt:   time.Now(),
		ViewMode:   viewMode,
		Status:     "active",
		IsVIP:      isVIP,
		CameraPos:  map[string]interface{}{"x": 0, "y": 0, "z": 0},
		Metadata:   map[string]interface{}{"client_version": "1.0.0"},
		CreatedAt:  time.Now(),
		UpdatedAt:  time.Now(),
	}

	err = s.repo.CreateSpectator(ctx, spectator)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to create spectator: %w", err)
	}

	// Update match spectator count
	err = s.repo.UpdateMatchSpectatorCount(ctx, matchID, match.SpectatorCount+1)
	if err != nil {
		s.logger.Warnf("Failed to update spectator count for match %s: %v", matchID, err)
		// Don't fail the operation, just log the warning
	}

	s.logger.Infof("Player %s successfully joined spectator mode for match %s", playerName, matchID)
	return spectator, nil
}

// LeaveSpectatorMode allows a spectator to leave the match
func (s *TournamentService) LeaveSpectatorMode(ctx context.Context, spectatorID string) error {
	s.logger.Infof("Spectator %s leaving spectator mode", spectatorID)

	spectator, err := s.repo.GetSpectator(ctx, spectatorID)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to get spectator: %w", err)
	}

	// Update spectator record
	now := time.Now()
	spectator.LeftAt = &now
	spectator.Status = "inactive"
	spectator.UpdatedAt = now

	err = s.repo.UpdateSpectator(ctx, spectator)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update spectator: %w", err)
	}

	// Update match spectator count
	matchID, _ := uuid.Parse(spectator.MatchID)
	match, err := s.repo.GetMatch(ctx, matchID)
	if err == nil && match.SpectatorCount > 0 {
		err = s.repo.UpdateMatchSpectatorCount(ctx, matchID, match.SpectatorCount-1)
		if err != nil {
			s.logger.Warnf("Failed to update spectator count for match %s: %v", matchID, err)
		}
	}

	s.logger.Infof("Spectator %s successfully left spectator mode", spectatorID)
	return nil
}

// UpdateSpectatorView updates spectator camera position and view mode
func (s *TournamentService) UpdateSpectatorView(ctx context.Context, spectatorID string, viewMode string, followID string, cameraPos map[string]interface{}) error {
	s.logger.Debugf("Updating spectator %s view mode to %s", spectatorID, viewMode)

	spectator, err := s.repo.GetSpectator(ctx, spectatorID)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to get spectator: %w", err)
	}

	if spectator.Status != "active" {
		return fmt.Errorf("spectator is not active")
	}

	// Update spectator view settings
	spectator.ViewMode = viewMode
	spectator.FollowID = followID
	spectator.CameraPos = cameraPos
	spectator.UpdatedAt = time.Now()

	err = s.repo.UpdateSpectator(ctx, spectator)
	if err != nil {
		s.metrics.IncrementErrors()
		return fmt.Errorf("failed to update spectator view: %w", err)
	}

	return nil
}

// GetMatchSpectators gets all active spectators for a match
func (s *TournamentService) GetMatchSpectators(ctx context.Context, matchID uuid.UUID) ([]*repository.Spectator, error) {
	s.logger.Debugf("Getting spectators for match %s", matchID)

	spectators, err := s.repo.GetMatchSpectators(ctx, matchID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get match spectators: %w", err)
	}

	return spectators, nil
}

// GetSpectatorStats gets spectator statistics for a tournament
func (s *TournamentService) GetSpectatorStats(ctx context.Context, tournamentID uuid.UUID) (map[string]interface{}, error) {
	s.logger.Debugf("Getting spectator stats for tournament %s", tournamentID)

	// Get all matches for the tournament
	matches, err := s.repo.GetTournamentMatches(ctx, tournamentID)
	if err != nil {
		s.metrics.IncrementErrors()
		return nil, fmt.Errorf("failed to get tournament matches: %w", err)
	}

	totalSpectators := 0
	activeSpectators := 0
	vipSpectators := 0
	peakSpectators := 0

	for _, match := range matches {
		totalSpectators += match.SpectatorCount
		if match.Status == "in_progress" {
			activeSpectators += match.SpectatorCount
		}
		if match.SpectatorCount > peakSpectators {
			peakSpectators = match.SpectatorCount
		}
	}

	// Get VIP spectator count
	for _, match := range matches {
		spectators, err := s.repo.GetMatchSpectators(ctx, uuid.MustParse(match.ID))
		if err == nil {
			for _, spectator := range spectators {
				if spectator.IsVIP {
					vipSpectators++
				}
			}
		}
	}

	stats := map[string]interface{}{
		"tournament_id":     tournamentID.String(),
		"total_spectators":  totalSpectators,
		"active_spectators": activeSpectators,
		"vip_spectators":    vipSpectators,
		"peak_spectators":   peakSpectators,
		"match_count":       len(matches),
	}

	return stats, nil
}

// Tournament Bracket Schema business logic

// GetBracketSchema gets the bracket schema for a tournament type
func (s *TournamentService) GetBracketSchema(ctx context.Context, tournamentType string, playerCount int) (map[string]interface{}, error) {
	schema, err := s.repo.GetBracketSchema(ctx, tournamentType, playerCount)
	if err != nil {
		return nil, err
	}

	s.logger.Infof("Retrieved bracket schema for %s tournament with %d players", tournamentType, playerCount)
	return schema, nil
}

// ValidateBracket validates a tournament bracket structure
func (s *TournamentService) ValidateBracket(ctx context.Context, bracket map[string]interface{}) (bool, []string) {
	errors := s.repo.ValidateBracketStructure(ctx, bracket)

	isValid := len(errors) == 0
	if isValid {
		s.logger.Info("Bracket validation successful")
	} else {
		s.logger.Warnf("Bracket validation failed with %d errors", len(errors))
	}

	return isValid, errors
}

// GenerateBracket generates a tournament bracket from player list
func (s *TournamentService) GenerateBracket(ctx context.Context, request map[string]interface{}) (map[string]interface{}, error) {
	bracket, err := s.repo.GenerateBracketFromPlayers(ctx, request)
	if err != nil {
		return nil, err
	}

	s.logger.Infof("Generated tournament bracket with %d players", len(request["players"].([]interface{})))
	return bracket, nil
}

// UpdateBracketMatch updates a match result in the bracket
func (s *TournamentService) UpdateBracketMatch(ctx context.Context, matchUpdate map[string]interface{}) (map[string]interface{}, error) {
	updatedBracket, err := s.repo.UpdateBracketMatchResult(ctx, matchUpdate)
	if err != nil {
		return nil, err
	}

	s.logger.Infof("Updated bracket match result for match %v", matchUpdate["match_id"])
	return updatedBracket, nil
}

// GetBracketProgress gets tournament bracket progress
func (s *TournamentService) GetBracketProgress(ctx context.Context, tournamentID uuid.UUID) (map[string]interface{}, error) {
	progress, err := s.repo.GetBracketProgress(ctx, tournamentID)
	if err != nil {
		return nil, err
	}

	return progress, nil
}

// PredictBracketOutcomes predicts possible bracket outcomes
func (s *TournamentService) PredictBracketOutcomes(ctx context.Context, tournamentID uuid.UUID, maxDepth int) (map[string]interface{}, error) {
	predictions, err := s.repo.PredictBracketOutcomes(ctx, tournamentID, maxDepth)
	if err != nil {
		return nil, err
	}

	s.logger.Infof("Generated bracket outcome predictions with max depth %d", maxDepth)
	return predictions, nil
}
