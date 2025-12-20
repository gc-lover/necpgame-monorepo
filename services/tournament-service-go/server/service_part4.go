	for range ticker.C {
		s.logger.Debug("running ranking updater")
		s.updateAllPlayerRankings(context.Background())
		s.metrics.RankingUpdates.Inc()
	}
}

func (s *TournamentService) leagueManager() {
	ticker := time.NewTicker(s.config.LeagueUpdateInterval)
	defer ticker.Stop()

	for range ticker.C {
		s.logger.Debug("running league manager")
		s.leagues.Range(func(key, value interface{}) bool {
			league := value.(*League)
			now := time.Now()

			// Transition league status (season end)
			if league.Status == "active" && now.After(league.SeasonEndTime) {
				league.Status = "completed"
				s.logger.WithField("league_id", league.LeagueID).Info("league season ended")
				s.saveLeagueToRedis(context.Background(), league)
				s.distributeLeagueRewards(context.Background(), league)
			}
			return true
		})
	}
}

// Helper methods
func (s *TournamentService) validateTournamentRequest(req *CreateTournamentRequest) error {
	if req.Name == "" {
		return fmt.Errorf("tournament name is required")
	}
	if len(req.Name) < 2 || len(req.Name) > 255 {
		return fmt.Errorf("tournament name must be between 2 and 255 characters")
	}
	if req.GameMode == "" {
		return fmt.Errorf("game mode is required")
	}
	if req.MaxParticipants < 2 || req.MaxParticipants > 10000 {
		return fmt.Errorf("max participants must be between 2 and 10000")
	}
	return nil
}

func (s *TournamentService) saveTournamentToRedis(ctx context.Context, tournament *Tournament) {
	key := fmt.Sprintf("tournament:%s", tournament.TournamentID)

	data, err := json.Marshal(tournament)
	if err != nil {
		s.logger.WithError(err).WithField("tournament_id", tournament.TournamentID).Error("failed to marshal tournament for Redis")
		return
	}

	if err := s.redisClient.Set(ctx, key, data, 24*time.Hour).Err(); err != nil {
		s.logger.WithError(err).WithField("tournament_id", tournament.TournamentID).Error("failed to save tournament to Redis")
	}
}

func (s *TournamentService) saveMatchToRedis(ctx context.Context, match *Match) {
	key := fmt.Sprintf("match:%s", match.MatchID)

	data, err := json.Marshal(match)
	if err != nil {
		s.logger.WithError(err).WithField("match_id", match.MatchID).Error("failed to marshal match for Redis")
		return
	}

	if err := s.redisClient.Set(ctx, key, data, 24*time.Hour).Err(); err != nil {
		s.logger.WithError(err).WithField("match_id", match.MatchID).Error("failed to save match to Redis")
	}
}

func (s *TournamentService) saveLeagueToRedis(ctx context.Context, league *League) {
	key := fmt.Sprintf("league:%s", league.LeagueID)

	data, err := json.Marshal(league)
	if err != nil {
		s.logger.WithError(err).WithField("league_id", league.LeagueID).Error("failed to marshal league for Redis")
		return
	}

	if err := s.redisClient.Set(ctx, key, data, 24*time.Hour).Err(); err != nil {
		s.logger.WithError(err).WithField("league_id", league.LeagueID).Error("failed to save league to Redis")
	}
}

func (s *TournamentService) generateBracket(tournament *Tournament) []Match {
	// Simple single-elimination bracket generation
	var matches []Match
	var participants []string

	s.registrations.Range(func(key, value interface{}) bool {
		reg := value.(*Registration)
		if reg.TournamentID == tournament.TournamentID && reg.Status == "confirmed" {
			participants = append(participants, reg.PlayerID)
		}
		return true
	})

	// Generate matches for round 1
	for i := 0; i < len(participants); i += 2 {
		if i+1 < len(participants) {
			match := Match{
				MatchID:       uuid.New().String(),
				TournamentID:  tournament.TournamentID,
				Round:         1,
				Position:      i/2 + 1,
				Player1ID:     participants[i],
				Player2ID:     participants[i+1],
				Status:        "scheduled",
				ScheduledTime: time.Now().Add(time.Duration(i) * time.Minute), // Stagger start times
				CreatedAt:     time.Now(),
			}
			matches = append(matches, match)
			s.matches.Store(match.MatchID, &match)
			s.metrics.ActiveMatches.Inc()
		}
	}

	return matches
}

func (s *TournamentService) calculateTotalRounds(participants int) int {
	if participants <= 1 {
		return 0
	}
	return int(math.Ceil(math.Log2(float64(participants))))
}

func (s *TournamentService) isTournamentCompleted(tournament *Tournament) bool {
	// Check if all matches in current round are completed
	completedMatches := 0
	totalMatches := 0

	s.matches.Range(func(key, value interface{}) bool {
		match := value.(*Match)
		if match.TournamentID == tournament.TournamentID && match.Round == tournament.CurrentRound {
			totalMatches++
			if match.Status == "completed" {
				completedMatches++
			}
		}
		return true
	})

	return completedMatches == totalMatches && totalMatches > 0
}

func (s *TournamentService) updateTournamentProgress(ctx context.Context, tournamentID string) {
	// Update tournament progress logic
}

func (s *TournamentService) updatePlayerRankings(ctx context.Context, player1ID, player2ID, winnerID string) {
	// Update ELO rankings logic
	s.metrics.RankingUpdates.Inc()
}

func (s *TournamentService) updateAllPlayerRankings(ctx context.Context) {
	// Recalculate all player rankings
}

func (s *TournamentService) distributePrizes(ctx context.Context, tournament *Tournament) {
	// Distribute prizes to winners
	s.metrics.RewardClaims.Add(float64(len(tournament.Prizes)))
}

func (s *TournamentService) distributeLeagueRewards(ctx context.Context, league *League) {
	// Distribute league season rewards
}

// Missing imports and rate limiter
var rateLimiters sync.Map

// Add missing imports
import (
	"math"
	"golang.org/x/time/rate"
)
