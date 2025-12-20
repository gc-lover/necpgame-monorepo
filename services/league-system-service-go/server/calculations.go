// Package server Issue: #??? - Calculation methods split from service.go
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/league-system-service-go/models"
	"github.com/google/uuid"
	"go.uber.org/zap"
)

// calculateLeagueCountdown calculates time remaining in current league
func (s *LeagueService) calculateLeagueCountdown(ctx context.Context) (*models.LeagueCountdown, error) {
	league, err := s.repo.GetCurrentLeague(ctx)
	if err != nil {
		return nil, err
	}

	now := time.Now()
	timeLeft := league.EndDate.Sub(now)

	if timeLeft < 0 {
		return &models.LeagueCountdown{
			TotalSeconds: 0,
			Days:         0,
			Hours:        0,
			Minutes:      0,
			Seconds:      0,
		}, nil
	}

	totalSeconds := int(timeLeft.Seconds())
	days := totalSeconds / 86400
	hours := (totalSeconds % 86400) / 3600
	minutes := (totalSeconds % 3600) / 60
	seconds := totalSeconds % 60

	// Calculate phase end time
	var phaseEndSeconds int
	var nextPhase string

	switch league.Phase.Name {
	case "START":
		nextPhase = "RISE"
	case "RISE":
		nextPhase = "CRISIS"
	case "CRISIS":
		nextPhase = "ENDGAME"
	case "ENDGAME":
		nextPhase = "FINALE"
	case "FINALE":
		nextPhase = "RESET"
	}

	if nextPhase != "" {
		// Calculate time to next phase (simplified)
		phaseEndSeconds = totalSeconds / 4 // Rough estimate
	}

	return &models.LeagueCountdown{
		TotalSeconds:    totalSeconds,
		Days:            days,
		Hours:           hours,
		Minutes:         minutes,
		Seconds:         seconds,
		PhaseEndSeconds: phaseEndSeconds,
		NextPhase:       nextPhase,
	}, nil
}

// getLeaguePhases returns all phases for the current league
func (s *LeagueService) getLeaguePhases(ctx context.Context) (*models.LeaguePhases, error) {
	league, err := s.repo.GetCurrentLeague(ctx)
	if err != nil {
		return nil, err
	}

	// Generate phases based on league dates
	totalDuration := league.EndDate.Sub(league.StartDate)
	phaseDuration := totalDuration / 5 // 5 phases

	phases := []models.LeaguePhase{
		{
			Name:             "START",
			StartDate:        league.StartDate,
			EndDate:          league.StartDate.Add(phaseDuration),
			Description:      "Character creation and early game",
			TimeAcceleration: 15.0,
		},
		{
			Name:             "RISE",
			StartDate:        league.StartDate.Add(phaseDuration),
			EndDate:          league.StartDate.Add(2 * phaseDuration),
			Description:      "Corporate wars and empire building",
			TimeAcceleration: 20.0,
		},
		{
			Name:             "CRISIS",
			StartDate:        league.StartDate.Add(2 * phaseDuration),
			EndDate:          league.StartDate.Add(3 * phaseDuration),
			Description:      "Major conflicts and Blackwall events",
			TimeAcceleration: 25.0,
		},
		{
			Name:             "ENDGAME",
			StartDate:        league.StartDate.Add(3 * phaseDuration),
			EndDate:          league.StartDate.Add(4 * phaseDuration),
			Description:      "Final conflicts and story climax",
			TimeAcceleration: 30.0,
		},
		{
			Name:             "FINALE",
			StartDate:        league.StartDate.Add(4 * phaseDuration),
			EndDate:          league.EndDate,
			Description:      "Final concert and league conclusion",
			TimeAcceleration: 1.0, // Real-time for finale
		},
	}

	return &models.LeaguePhases{
		CurrentPhase:     league.Phase,
		Phases:           phases,
		TimeAcceleration: league.TimeAcceleration,
	}, nil
}

// performLeagueReset executes the league reset process
func (s *LeagueService) performLeagueReset(ctx context.Context, leagueID uuid.UUID) {
	s.logger.Info("Performing league reset", zap.String("league_id", leagueID.String()))

	// 1. Collect final statistics
	stats, err := s.repo.GetLeagueStatistics(ctx, leagueID)
	if err != nil {
		s.logger.Error("Failed to collect league statistics", zap.Error(err), zap.String("league_id", leagueID.String()))
		return
	}

	// 2. Update Hall of Fame with final results
	if s.features.enableHallOfFame {
		for _, topPlayer := range stats.TopPlayers {
			hofEntry := &models.HallOfFameEntry{
				PlayerID:       topPlayer.PlayerID,
				PlayerName:     topPlayer.PlayerName,
				Category:       topPlayer.Category,
				Achievement:    fmt.Sprintf("Top %d in League", topPlayer.Rank),
				Date:           time.Now(),
				Rank:           topPlayer.Rank,
				RewardCosmetic: s.calculateRewardCosmetic(topPlayer.Rank),
			}

			if err := s.repo.AddHallOfFameEntry(ctx, leagueID, hofEntry); err != nil {
				s.logger.Error("Failed to add Hall of Fame entry", zap.Error(err), zap.String("player_id", topPlayer.PlayerID.String()))
			}
		}
	}

	// 3. Update player legacy progression
	if err := s.updatePlayerLegacyProgress(ctx, stats); err != nil {
		s.logger.Error("Failed to update player legacy progress", zap.Error(err))
	}

	// 4. Mark league as completed
	if err := s.repo.UpdateLeagueStatus(ctx, leagueID, models.LeagueStatusCompleted); err != nil {
		s.logger.Error("Failed to mark league as completed", zap.Error(err))
	}

	// 5. Create new league (simplified - in production this would be more complex)
	if err := s.createNextLeague(ctx); err != nil {
		s.logger.Error("Failed to create next league", zap.Error(err))
	}

	s.logger.Info("League reset completed successfully", zap.String("league_id", leagueID.String()))
}

// calculateRewardCosmetic determines cosmetic reward based on rank
func (s *LeagueService) calculateRewardCosmetic(rank int) string {
	switch {
	case rank == 1:
		return "Legendary Champion Crown"
	case rank <= 3:
		return "Epic Victory Wreath"
	case rank <= 10:
		return "Rare League Badge"
	case rank <= 50:
		return "Uncommon League Emblem"
	default:
		return "Common League Token"
	}
}

// updatePlayerLegacyProgress updates legacy progress for all players
func (s *LeagueService) updatePlayerLegacyProgress(ctx context.Context, stats *models.LeagueStatistics) error {
	// Simplified implementation - in production this would be more sophisticated
	for _, topPlayer := range stats.TopPlayers {
		// Add legacy points based on rank
		legacyPoints := s.calculateLegacyPoints(topPlayer.Rank)

		if err := s.repo.UpdatePlayerLegacyProgress(ctx, topPlayer.PlayerID, legacyPoints); err != nil {
			s.logger.Error("Failed to update legacy progress", zap.Error(err), zap.String("player_id", topPlayer.PlayerID.String()))
			continue
		}

		// Award titles based on achievements
		if topPlayer.Rank == 1 {
			title := &models.Title{
				ID:          uuid.New(),
				Name:        "League Champion",
				Description: "First place in a completed league",
				Rarity:      models.RarityLegendary,
				UnlockedAt:  time.Now(),
			}
			if err := s.repo.AddPlayerTitle(ctx, topPlayer.PlayerID, title); err != nil {
				s.logger.Warn("Failed to award champion title", zap.Error(err))
			}
		}
	}

	return nil
}

// calculateLegacyPoints calculates legacy points based on rank
func (s *LeagueService) calculateLegacyPoints(rank int) int {
	switch {
	case rank == 1:
		return 1000
	case rank <= 3:
		return 500
	case rank <= 10:
		return 250
	case rank <= 50:
		return 100
	case rank <= 100:
		return 50
	default:
		return 10
	}
}

// createNextLeague creates the next league in the cycle
func (s *LeagueService) createNextLeague(ctx context.Context) error {
	// Simplified - create a new league with accelerated time
	newLeague := &models.League{
		ID:               uuid.New(),
		Name:             fmt.Sprintf("League Season %d", time.Now().Year()*12+int(time.Now().Month())),
		StartDate:        time.Now().Add(24 * time.Hour),                  // Start tomorrow
		EndDate:          time.Now().Add(24*7*time.Hour + 30*time.Minute), // 7 days accelerated
		Status:           models.LeagueStatusPlanned,
		Seed:             time.Now().Unix(),
		TimeAcceleration: 168.0, // 7 days real time = 30 minutes game time
	}

	return s.repo.CreateLeague(ctx, newLeague)
}
