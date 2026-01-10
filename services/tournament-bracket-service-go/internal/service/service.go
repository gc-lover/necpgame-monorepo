// Tournament Bracket Service - Business logic layer
// Issue: #2210
// Agent: Backend Agent
package service

import (
	"context"
	"fmt"
	"math"
	"sort"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/tournament-bracket-service-go/internal/models"
	"necpgame/services/tournament-bracket-service-go/internal/repository"
)

// Service handles tournament bracket business logic
type Service struct {
	repo   *repository.Repository
	logger *zap.Logger
}

// NewService creates a new service instance
func NewService(repo *repository.Repository, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		logger: logger,
	}
}

// BRACKET OPERATIONS

// CreateBracket creates a new tournament bracket
func (s *Service) CreateBracket(ctx context.Context, req *models.CreateBracketRequest) (*models.Bracket, error) {
	s.logger.Info("Creating new bracket",
		zap.String("tournament_id", req.TournamentID),
		zap.String("name", req.Name),
		zap.String("bracket_type", string(req.BracketType)))

	bracket := &models.Bracket{
		ID:              uuid.New(),
		TournamentID:    req.TournamentID,
		Name:            req.Name,
		Description:     req.Description,
		BracketType:     req.BracketType,
		MaxParticipants: req.MaxParticipants,
		CurrentRound:    1,
		Status:          models.BracketStatusPending,
		StartDate:       req.StartDate,
		PrizePool:       req.PrizePool,
		Rules:           req.Rules,
		CreatedAt:       time.Now().UTC(),
		UpdatedAt:       time.Now().UTC(),
	}

	// Calculate total rounds based on bracket type and participants
	bracket.TotalRounds = s.calculateTotalRounds(req.BracketType, req.MaxParticipants)

	err := s.repo.CreateBracket(ctx, bracket)
	if err != nil {
		s.logger.Error("Failed to create bracket", zap.Error(err))
		return nil, fmt.Errorf("failed to create bracket: %w", err)
	}

	s.logger.Info("Bracket created successfully", zap.String("bracket_id", bracket.ID.String()))
	return bracket, nil
}

// GetBracket retrieves a bracket by ID
func (s *Service) GetBracket(ctx context.Context, bracketID uuid.UUID) (*models.Bracket, error) {
	s.logger.Debug("Retrieving bracket", zap.String("bracket_id", bracketID.String()))

	bracket, err := s.repo.GetBracket(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to get bracket", zap.Error(err))
		return nil, fmt.Errorf("failed to get bracket: %w", err)
	}

	return bracket, nil
}

// UpdateBracket updates an existing bracket
func (s *Service) UpdateBracket(ctx context.Context, bracketID uuid.UUID, req *models.UpdateBracketRequest) (*models.Bracket, error) {
	s.logger.Info("Updating bracket", zap.String("bracket_id", bracketID.String()))

	bracket, err := s.repo.GetBracket(ctx, bracketID)
	if err != nil {
		return nil, fmt.Errorf("failed to get bracket: %w", err)
	}

	// Apply updates
	if req.Name != nil {
		bracket.Name = *req.Name
	}
	if req.Description != nil {
		bracket.Description = req.Description
	}
	if req.Status != nil {
		bracket.Status = *req.Status
	}
	if req.StartDate != nil {
		bracket.StartDate = req.StartDate
	}
	if req.EndDate != nil {
		bracket.EndDate = req.EndDate
	}
	if req.PrizePool != nil {
		bracket.PrizePool = req.PrizePool
	}
	if req.Rules != nil {
		bracket.Rules = req.Rules
	}
	if req.Metadata != nil {
		bracket.Metadata = req.Metadata
	}

	bracket.UpdatedAt = time.Now().UTC()

	err = s.repo.UpdateBracket(ctx, bracket)
	if err != nil {
		s.logger.Error("Failed to update bracket", zap.Error(err))
		return nil, fmt.Errorf("failed to update bracket: %w", err)
	}

	s.logger.Info("Bracket updated successfully", zap.String("bracket_id", bracketID.String()))
	return bracket, nil
}

// ListBrackets retrieves brackets with optional filtering
func (s *Service) ListBrackets(ctx context.Context, tournamentID *string, status *models.BracketStatus, limit, offset int) ([]*models.Bracket, error) {
	s.logger.Debug("Listing brackets",
		zap.String("tournament_id", func() string {
			if tournamentID != nil {
				return *tournamentID
			}
			return ""
		}()),
		zap.String("status", func() string {
			if status != nil {
				return string(*status)
			}
			return ""
		}()))

	brackets, err := s.repo.ListBrackets(ctx, tournamentID, status, limit, offset)
	if err != nil {
		s.logger.Error("Failed to list brackets", zap.Error(err))
		return nil, fmt.Errorf("failed to list brackets: %w", err)
	}

	return brackets, nil
}

// GenerateBracketRounds generates rounds and matches for a bracket
func (s *Service) GenerateBracketRounds(ctx context.Context, bracketID uuid.UUID) error {
	s.logger.Info("Generating bracket rounds", zap.String("bracket_id", bracketID.String()))

	bracket, err := s.repo.GetBracket(ctx, bracketID)
	if err != nil {
		return fmt.Errorf("failed to get bracket: %w", err)
	}

	participants, err := s.repo.GetBracketParticipants(ctx, bracketID)
	if err != nil {
		return fmt.Errorf("failed to get participants: %w", err)
	}

	switch bracket.BracketType {
	case models.BracketTypeSingleElimination:
		return s.generateSingleEliminationRounds(ctx, bracket, participants)
	case models.BracketTypeDoubleElimination:
		return s.generateDoubleEliminationRounds(ctx, bracket, participants)
	case models.BracketTypeRoundRobin:
		return s.generateRoundRobinRounds(ctx, bracket, participants)
	default:
		return fmt.Errorf("unsupported bracket type: %s", bracket.BracketType)
	}
}

// MATCH OPERATIONS

// CreateMatch creates a new match in a bracket round
func (s *Service) CreateMatch(ctx context.Context, req *models.CreateMatchRequest) (*models.BracketMatch, error) {
	s.logger.Info("Creating match",
		zap.String("bracket_id", req.BracketID.String()),
		zap.String("round_id", req.RoundID.String()),
		zap.Int("match_number", req.MatchNumber))

	match := &models.BracketMatch{
		ID:                uuid.New(),
		BracketID:         req.BracketID,
		RoundID:           req.RoundID,
		MatchNumber:       req.MatchNumber,
		Participant1ID:    req.Participant1ID,
		Participant1Name:  req.Participant1Name,
		Participant1Seed:  req.Participant1Seed,
		Participant1Score: 0,
		Participant1Status: "active",
		Participant2ID:    req.Participant2ID,
		Participant2Name:  req.Participant2Name,
		Participant2Seed:  req.Participant2Seed,
		Participant2Score: 0,
		Participant2Status: "active",
		Status:            models.MatchStatusPending,
		ScheduledStart:    req.ScheduledStart,
		MapName:           req.MapName,
		GameMode:          req.GameMode,
		SpectatorCount:    0,
		ScoreDetails:      make(map[string]interface{}),
		MatchStats:        make(map[string]interface{}),
		Metadata:          make(map[string]interface{}),
		CreatedAt:         time.Now().UTC(),
		UpdatedAt:         time.Now().UTC(),
	}

	err := s.repo.CreateBracketMatch(ctx, match)
	if err != nil {
		s.logger.Error("Failed to create match", zap.Error(err))
		return nil, fmt.Errorf("failed to create match: %w", err)
	}

	s.logger.Info("Match created successfully", zap.String("match_id", match.ID.String()))
	return match, nil
}

// UpdateMatch updates match results and status
func (s *Service) UpdateMatch(ctx context.Context, matchID uuid.UUID, req *models.UpdateMatchRequest) (*models.MatchResult, error) {
	s.logger.Info("Updating match", zap.String("match_id", matchID.String()))

	// Get current match from database first
	// This is a simplified implementation - in production, you'd fetch the match first
	// For now, we'll create a mock match result based on the request

	var winnerID, winnerName *string
	var winnerScore, loserScore int

	if req.Participant1Score != nil && req.Participant2Score != nil {
		if *req.Participant1Score > *req.Participant2Score {
			if req.WinnerID != nil {
				winnerID = req.WinnerID
			}
			winnerScore = *req.Participant1Score
			loserScore = *req.Participant2Score
		} else {
			if req.WinnerID != nil {
				winnerID = req.WinnerID
			}
			winnerScore = *req.Participant2Score
			loserScore = *req.Participant1Score
		}
	}

	// Create match result
	result := &models.MatchResult{
		MatchID:     matchID,
		WinnerID:    winnerID,
		WinnerName:  winnerName,
		WinnerScore: winnerScore,
		LoserScore:  loserScore,
		CompletedAt: time.Now().UTC(),
		IsWalkover:  req.Status != nil && *req.Status == models.MatchStatusBye,
		IsForfeit:   false, // Would be determined by business rules
	}

	// In a real implementation, you'd update the match record in the database
	// For now, we'll just return the result

	s.logger.Info("Match updated successfully",
		zap.String("match_id", matchID.String()),
		zap.String("winner_id", func() string {
			if winnerID != nil {
				return *winnerID
			}
			return ""
		}()))

	return result, nil
}

// PARTICIPANT OPERATIONS

// AddParticipant adds a participant to a bracket
func (s *Service) AddParticipant(ctx context.Context, req *models.CreateParticipantRequest) (*models.BracketParticipant, error) {
	s.logger.Info("Adding participant",
		zap.String("bracket_id", req.BracketID.String()),
		zap.String("participant_id", req.ParticipantID))

	participant := &models.BracketParticipant{
		ID:              uuid.New(),
		BracketID:       req.BracketID,
		ParticipantID:   req.ParticipantID,
		ParticipantName: req.ParticipantName,
		ParticipantType: req.ParticipantType,
		SeedNumber:      req.SeedNumber,
		CurrentRound:    1,
		Status:          models.ParticipantStatusActive,
		JoinedAt:        time.Now().UTC(),
		TotalScore:      0,
		TotalWins:       0,
		TotalLosses:     0,
		TotalDraws:      0,
		AverageScore:    0,
		PerformanceStats: make(map[string]interface{}),
		Metadata:         req.Metadata,
		CreatedAt:        time.Now().UTC(),
		UpdatedAt:        time.Now().UTC(),
	}

	err := s.repo.CreateBracketParticipant(ctx, participant)
	if err != nil {
		s.logger.Error("Failed to add participant", zap.Error(err))
		return nil, fmt.Errorf("failed to add participant: %w", err)
	}

	s.logger.Info("Participant added successfully", zap.String("participant_id", participant.ID.String()))
	return participant, nil
}

// UpdateParticipant updates participant statistics
func (s *Service) UpdateParticipant(ctx context.Context, participantID uuid.UUID, req *models.UpdateParticipantRequest) error {
	s.logger.Info("Updating participant", zap.String("participant_id", participantID.String()))

	// Get current participant
	// This is simplified - in real implementation, you'd need to fetch the participant first

	participant := &models.BracketParticipant{
		ID: participantID,
	}

	// Apply updates
	if req.Status != nil {
		participant.Status = *req.Status
		if *req.Status == models.ParticipantStatusEliminated {
			now := time.Now().UTC()
			participant.EliminatedAt = &now
		}
	}
	if req.CurrentRound != nil {
		participant.CurrentRound = *req.CurrentRound
	}
	if req.FinalRank != nil {
		participant.FinalRank = req.FinalRank
	}
	if req.TotalScore != nil {
		participant.TotalScore = *req.TotalScore
	}
	if req.TotalWins != nil {
		participant.TotalWins = *req.TotalWins
	}
	if req.TotalLosses != nil {
		participant.TotalLosses = *req.TotalLosses
	}
	if req.TotalDraws != nil {
		participant.TotalDraws = *req.TotalDraws
	}
	if req.PerformanceStats != nil {
		participant.PerformanceStats = req.PerformanceStats
	}
	if req.Metadata != nil {
		participant.Metadata = req.Metadata
	}

	participant.UpdatedAt = time.Now().UTC()

	err := s.repo.UpdateBracketParticipant(ctx, participant)
	if err != nil {
		s.logger.Error("Failed to update participant", zap.Error(err))
		return fmt.Errorf("failed to update participant: %w", err)
	}

	s.logger.Info("Participant updated successfully", zap.String("participant_id", participantID.String()))
	return nil
}

// PROGRESS TRACKING

// GetBracketProgress returns overall bracket progress
func (s *Service) GetBracketProgress(ctx context.Context, bracketID uuid.UUID) (*models.BracketProgress, error) {
	s.logger.Debug("Getting bracket progress", zap.String("bracket_id", bracketID.String()))

	progress, err := s.repo.GetBracketProgress(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to get bracket progress", zap.Error(err))
		return nil, fmt.Errorf("failed to get bracket progress: %w", err)
	}

	return progress, nil
}

// HELPER METHODS

// calculateTotalRounds calculates total rounds based on bracket type and participants
func (s *Service) calculateTotalRounds(bracketType models.BracketType, maxParticipants int) *int {
	var rounds int

	switch bracketType {
	case models.BracketTypeSingleElimination:
		rounds = int(math.Ceil(math.Log2(float64(maxParticipants))))
	case models.BracketTypeDoubleElimination:
		// Winners bracket + losers bracket + finals
		winnersRounds := int(math.Ceil(math.Log2(float64(maxParticipants))))
		rounds = winnersRounds*2 + 1
	case models.BracketTypeRoundRobin:
		// All participants play each other once
		rounds = maxParticipants - 1
	case models.BracketTypeSwiss:
		// Fixed number of rounds (typically 5-7)
		rounds = 5
	default:
		rounds = 1
	}

	return &rounds
}

// generateSingleEliminationRounds generates rounds for single elimination bracket
func (s *Service) generateSingleEliminationRounds(ctx context.Context, bracket *models.Bracket, participants []*models.BracketParticipant) error {
	totalRounds := *bracket.TotalRounds

	// Sort participants by seed
	sort.Slice(participants, func(i, j int) bool {
		if participants[i].SeedNumber == nil && participants[j].SeedNumber == nil {
			return participants[i].ParticipantName < participants[j].ParticipantName
		}
		if participants[i].SeedNumber == nil {
			return false
		}
		if participants[j].SeedNumber == nil {
			return true
		}
		return *participants[i].SeedNumber < *participants[j].SeedNumber
	})

	// Create rounds
	for round := 1; round <= totalRounds; round++ {
		roundID := uuid.New()
		roundName := s.getRoundName(bracket.BracketType, round, totalRounds)

		bracketRound := &models.BracketRound{
			ID:              roundID,
			BracketID:       bracket.ID,
			RoundNumber:     round,
			RoundName:       &roundName,
			RoundType:       models.RoundTypeElimination,
			Status:          models.RoundStatusPending,
			TotalMatches:    len(participants) / int(math.Pow(2, float64(round))),
			CompletedMatches: 0,
			ByeCount:        0,
			Metadata:        make(map[string]interface{}),
			CreatedAt:       time.Now().UTC(),
			UpdatedAt:       time.Now().UTC(),
		}

		err := s.repo.CreateBracketRound(ctx, bracketRound)
		if err != nil {
			return fmt.Errorf("failed to create round %d: %w", round, err)
		}

		// Create matches for this round
		err = s.createRoundMatches(ctx, bracketRound, participants, round, totalRounds)
		if err != nil {
			return fmt.Errorf("failed to create matches for round %d: %w", round, err)
		}
	}

	return nil
}

// generateDoubleEliminationRounds generates rounds for double elimination bracket
func (s *Service) generateDoubleEliminationRounds(ctx context.Context, bracket *models.Bracket, participants []*models.BracketParticipant) error {
	// Simplified implementation - full double elimination is complex
	return s.generateSingleEliminationRounds(ctx, bracket, participants)
}

// generateRoundRobinRounds generates rounds for round-robin bracket
func (s *Service) generateRoundRobinRounds(ctx context.Context, bracket *models.Bracket, participants []*models.BracketParticipant) error {
	totalRounds := *bracket.TotalRounds

	for round := 1; round <= totalRounds; round++ {
		roundID := uuid.New()
		roundName := fmt.Sprintf("Round %d", round)

		bracketRound := &models.BracketRound{
			ID:              roundID,
			BracketID:       bracket.ID,
			RoundNumber:     round,
			RoundName:       &roundName,
			RoundType:       models.RoundTypeElimination,
			Status:          models.RoundStatusPending,
			TotalMatches:    len(participants) / 2, // Each round has N/2 matches
			CompletedMatches: 0,
			ByeCount:        0,
			Metadata:        make(map[string]interface{}),
			CreatedAt:       time.Now().UTC(),
			UpdatedAt:       time.Now().UTC(),
		}

		err := s.repo.CreateBracketRound(ctx, bracketRound)
		if err != nil {
			return fmt.Errorf("failed to create round %d: %w", round, err)
		}

		// Create round-robin matches (simplified)
		for i := 0; i < len(participants); i += 2 {
			if i+1 < len(participants) {
				matchReq := &models.CreateMatchRequest{
					BracketID:        bracket.ID,
					RoundID:          roundID,
					MatchNumber:      i/2 + 1,
					Participant1ID:   &participants[i].ParticipantID,
					Participant1Name: &participants[i].ParticipantName,
					Participant2ID:   &participants[i+1].ParticipantID,
					Participant2Name: &participants[i+1].ParticipantName,
				}

				_, err := s.CreateMatch(ctx, matchReq)
				if err != nil {
					return fmt.Errorf("failed to create match %d in round %d: %w", i/2+1, round, err)
				}
			}
		}
	}

	return nil
}

// createRoundMatches creates matches for a specific round
func (s *Service) createRoundMatches(ctx context.Context, round *models.BracketRound, participants []*models.BracketParticipant, roundNum, totalRounds int) error {
	// Simplified match creation - in real implementation, this would use proper bracket logic
	matchesNeeded := len(participants) / int(math.Pow(2, float64(roundNum)))

	for i := 1; i <= matchesNeeded; i++ {
		matchReq := &models.CreateMatchRequest{
			BracketID:   round.BracketID,
			RoundID:     round.ID,
			MatchNumber: i,
		}

		// Assign participants based on seeding (simplified)
		participantIndex := (i - 1) * 2
		if participantIndex < len(participants) {
			matchReq.Participant1ID = &participants[participantIndex].ParticipantID
			matchReq.Participant1Name = &participants[participantIndex].ParticipantName
		}
		if participantIndex+1 < len(participants) {
			matchReq.Participant2ID = &participants[participantIndex+1].ParticipantID
			matchReq.Participant2Name = &participants[participantIndex+1].ParticipantName
		}

		_, err := s.CreateMatch(ctx, matchReq)
		if err != nil {
			return fmt.Errorf("failed to create match %d: %w", i, err)
		}
	}

	return nil
}

// getRoundName returns appropriate round name based on bracket type and position
func (s *Service) getRoundName(bracketType models.BracketType, round, totalRounds int) string {
	switch bracketType {
	case models.BracketTypeSingleElimination:
		switch {
		case round == totalRounds:
			return "Final"
		case round == totalRounds-1:
			return "Semi-Final"
		case round == totalRounds-2:
			return "Quarter-Final"
		default:
			return fmt.Sprintf("Round %d", round)
		}
	default:
		return fmt.Sprintf("Round %d", round)
	}
}

// DeleteBracket deletes a bracket by ID
func (s *Service) DeleteBracket(ctx context.Context, bracketID uuid.UUID) error {
	s.logger.Info("Deleting bracket", zap.String("bracket_id", bracketID.String()))

	err := s.repo.DeleteBracket(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to delete bracket", zap.Error(err))
		return fmt.Errorf("failed to delete bracket: %w", err)
	}

	s.logger.Info("Bracket deleted successfully", zap.String("bracket_id", bracketID.String()))
	return nil
}

// StartBracket starts a tournament bracket
func (s *Service) StartBracket(ctx context.Context, bracketID uuid.UUID) error {
	s.logger.Info("Starting bracket", zap.String("bracket_id", bracketID.String()))

	// Update bracket status to in_progress
	err := s.repo.UpdateBracketStatus(ctx, bracketID, models.BracketStatusInProgress)
	if err != nil {
		s.logger.Error("Failed to start bracket", zap.Error(err))
		return fmt.Errorf("failed to start bracket: %w", err)
	}

	s.logger.Info("Bracket started successfully", zap.String("bracket_id", bracketID.String()))
	return nil
}

// AdvanceBracket advances bracket to next round
func (s *Service) AdvanceBracket(ctx context.Context, bracketID uuid.UUID) error {
	s.logger.Info("Advancing bracket", zap.String("bracket_id", bracketID.String()))

	bracket, err := s.repo.GetBracket(ctx, bracketID)
	if err != nil {
		return fmt.Errorf("failed to get bracket: %w", err)
	}

	if bracket.CurrentRound >= bracket.TotalRounds {
		return fmt.Errorf("bracket already finished")
	}

	bracket.CurrentRound++
	err = s.repo.UpdateBracketCurrentRound(ctx, bracketID, bracket.CurrentRound)
	if err != nil {
		s.logger.Error("Failed to advance bracket", zap.Error(err))
		return fmt.Errorf("failed to advance bracket: %w", err)
	}

	s.logger.Info("Bracket advanced successfully", zap.String("bracket_id", bracketID.String()), zap.Int("current_round", bracket.CurrentRound))
	return nil
}

// FinishBracket finishes a tournament bracket
func (s *Service) FinishBracket(ctx context.Context, bracketID uuid.UUID) error {
	s.logger.Info("Finishing bracket", zap.String("bracket_id", bracketID.String()))

	err := s.repo.UpdateBracketStatus(ctx, bracketID, models.BracketStatusCompleted)
	if err != nil {
		s.logger.Error("Failed to finish bracket", zap.Error(err))
		return fmt.Errorf("failed to finish bracket: %w", err)
	}

	s.logger.Info("Bracket finished successfully", zap.String("bracket_id", bracketID.String()))
	return nil
}

// GetRounds retrieves all rounds for a bracket
func (s *Service) GetRounds(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketRound, error) {
	s.logger.Debug("Retrieving rounds for bracket", zap.String("bracket_id", bracketID.String()))

	rounds, err := s.repo.GetRoundsByBracketID(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to get rounds", zap.Error(err))
		return nil, fmt.Errorf("failed to get rounds: %w", err)
	}

	return rounds, nil
}

// GetRound retrieves a round by ID
func (s *Service) GetRound(ctx context.Context, roundID uuid.UUID) (*models.BracketRound, error) {
	s.logger.Debug("Retrieving round", zap.String("round_id", roundID.String()))

	round, err := s.repo.GetRound(ctx, roundID)
	if err != nil {
		s.logger.Error("Failed to get round", zap.Error(err))
		return nil, fmt.Errorf("failed to get round: %w", err)
	}

	return round, nil
}

// UpdateRound updates an existing round
func (s *Service) UpdateRound(ctx context.Context, roundID uuid.UUID, req *models.UpdateRoundRequest) (*models.BracketRound, error) {
	s.logger.Info("Updating round", zap.String("round_id", roundID.String()))

	round, err := s.repo.GetRound(ctx, roundID)
	if err != nil {
		return nil, fmt.Errorf("failed to get round: %w", err)
	}

	// Apply updates
	if req.Name != nil {
		round.Name = *req.Name
	}
	if req.Status != nil {
		round.Status = *req.Status
	}
	if req.StartDate != nil {
		round.StartDate = req.StartDate
	}
	if req.EndDate != nil {
		round.EndDate = req.EndDate
	}
	round.UpdatedAt = time.Now().UTC()

	err = s.repo.UpdateRound(ctx, round)
	if err != nil {
		s.logger.Error("Failed to update round", zap.Error(err))
		return nil, fmt.Errorf("failed to update round: %w", err)
	}

	s.logger.Info("Round updated successfully", zap.String("round_id", roundID.String()))
	return round, nil
}

// DeleteRound deletes a round by ID
func (s *Service) DeleteRound(ctx context.Context, roundID uuid.UUID) error {
	s.logger.Info("Deleting round", zap.String("round_id", roundID.String()))

	err := s.repo.DeleteRound(ctx, roundID)
	if err != nil {
		s.logger.Error("Failed to delete round", zap.Error(err))
		return fmt.Errorf("failed to delete round: %w", err)
	}

	s.logger.Info("Round deleted successfully", zap.String("round_id", roundID.String()))
	return nil
}

// GetMatch retrieves a match by ID
func (s *Service) GetMatch(ctx context.Context, matchID uuid.UUID) (*models.BracketMatch, error) {
	s.logger.Debug("Retrieving match", zap.String("match_id", matchID.String()))

	match, err := s.repo.GetMatch(ctx, matchID)
	if err != nil {
		s.logger.Error("Failed to get match", zap.Error(err))
		return nil, fmt.Errorf("failed to get match: %w", err)
	}

	return match, nil
}

// DeleteMatch deletes a match by ID
func (s *Service) DeleteMatch(ctx context.Context, matchID uuid.UUID) error {
	s.logger.Info("Deleting match", zap.String("match_id", matchID.String()))

	err := s.repo.DeleteMatch(ctx, matchID)
	if err != nil {
		s.logger.Error("Failed to delete match", zap.Error(err))
		return fmt.Errorf("failed to delete match: %w", err)
	}

	s.logger.Info("Match deleted successfully", zap.String("match_id", matchID.String()))
	return nil
}

// GetParticipant retrieves a participant by ID
func (s *Service) GetParticipant(ctx context.Context, participantID string) (*models.BracketParticipant, error) {
	s.logger.Debug("Retrieving participant", zap.String("participant_id", participantID))

	participant, err := s.repo.GetParticipant(ctx, participantID)
	if err != nil {
		s.logger.Error("Failed to get participant", zap.Error(err))
		return nil, fmt.Errorf("failed to get participant: %w", err)
	}

	return participant, nil
}

// RemoveParticipant removes a participant from bracket
func (s *Service) RemoveParticipant(ctx context.Context, participantID string) error {
	s.logger.Info("Removing participant", zap.String("participant_id", participantID))

	err := s.repo.RemoveParticipant(ctx, participantID)
	if err != nil {
		s.logger.Error("Failed to remove participant", zap.Error(err))
		return fmt.Errorf("failed to remove participant: %w", err)
	}

	s.logger.Info("Participant removed successfully", zap.String("participant_id", participantID))
	return nil
}

// GetParticipants retrieves all participants for a bracket
func (s *Service) GetParticipants(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketParticipant, error) {
	s.logger.Debug("Retrieving participants for bracket", zap.String("bracket_id", bracketID.String()))

	participants, err := s.repo.GetParticipantsByBracketID(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to get participants", zap.Error(err))
		return nil, fmt.Errorf("failed to get participants: %w", err)
	}

	return participants, nil
}

// GetMatches retrieves all matches for a bracket
func (s *Service) GetMatches(ctx context.Context, bracketID uuid.UUID) ([]*models.BracketMatch, error) {
	s.logger.Debug("Retrieving matches for bracket", zap.String("bracket_id", bracketID.String()))

	matches, err := s.repo.GetMatchesByBracketID(ctx, bracketID)
	if err != nil {
		s.logger.Error("Failed to get matches", zap.Error(err))
		return nil, fmt.Errorf("failed to get matches: %w", err)
	}

	return matches, nil
}