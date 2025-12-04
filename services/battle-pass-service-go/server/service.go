// Issue: #227
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/battle-pass-service-go/pkg/api"
)

var (
	ErrNotFound          = errors.New("not found")
	ErrAlreadyClaimed    = errors.New("already claimed")
	ErrAlreadyCompleted  = errors.New("already completed")
	ErrAlreadyPremium    = errors.New("already premium")
	ErrPremiumRequired   = errors.New("premium required")
	ErrLevelNotReached   = errors.New("level not reached")
)

const XPPerLevel = 1000 // XP required per level

type Service struct {
	repo *Repository
}

func NewService(repo *Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) GetCurrentSeason(ctx context.Context) (*api.Season, error) {
	return s.repo.GetCurrentSeason(ctx)
}

func (s *Service) GetPlayerProgress(ctx context.Context, playerId string) (*api.PlayerProgress, error) {
	return s.repo.GetPlayerProgress(ctx, playerId)
}

func (s *Service) ClaimReward(ctx context.Context, playerId string, level int, track api.RewardTrack) (map[string]interface{}, error) {
	// Get player progress
	progress, err := s.repo.GetPlayerProgress(ctx, playerId)
	if err != nil {
		return nil, err
	}

	// Check if level is reached
	if level > progress.CurrentLevel {
		return nil, ErrLevelNotReached
	}

	// Check if premium required
	if track == "premium" && !progress.HasPremium {
		return nil, ErrPremiumRequired
	}

	// Check if already claimed
	claimed := false
	if track == "free" {
		for _, l := range progress.ClaimedLevelsFree {
			if l == level {
				claimed = true
				break
			}
		}
	} else {
		for _, l := range progress.ClaimedLevelsPremium {
			if l == level {
				claimed = true
				break
			}
		}
	}

	if claimed {
		return nil, ErrAlreadyClaimed
	}

	// Get reward
	reward, err := s.repo.GetReward(ctx, progress.SeasonId, level, track)
	if err != nil {
		return nil, err
	}

	// Mark as claimed
	if err := s.repo.MarkRewardClaimed(ctx, playerId, level, track); err != nil {
		return nil, err
	}

	// TODO: Distribute rewards to player (integrate with economy service)

	return map[string]interface{}{
		"rewards": []api.Reward{*reward},
	}, nil
}

func (s *Service) PurchasePremium(ctx context.Context, playerId string) (map[string]interface{}, error) {
	// Get player progress
	progress, err := s.repo.GetPlayerProgress(ctx, playerId)
	if err != nil {
		return nil, err
	}

	if progress.HasPremium {
		return nil, ErrAlreadyPremium
	}

	// TODO: Check player currency (integrate with economy service)

	// Activate premium
	if err := s.repo.ActivatePremium(ctx, playerId); err != nil {
		return nil, err
	}

	// Get retroactive rewards
	retroRewards, err := s.repo.GetRetroactiveRewards(ctx, progress.SeasonId, progress.CurrentLevel)
	if err != nil {
		return nil, err
	}

	// TODO: Distribute retroactive rewards

	return map[string]interface{}{
		"premium_status":      true,
		"retroactive_rewards": retroRewards,
	}, nil
}

func (s *Service) GetWeeklyChallenges(ctx context.Context, playerId string) (map[string]interface{}, error) {
	challenges, err := s.repo.GetWeeklyChallenges(ctx, playerId)
	if err != nil {
		return nil, err
	}

	weekNumber := 1 // TODO: Calculate current week based on season start date

	return map[string]interface{}{
		"week_number": weekNumber,
		"challenges":  challenges,
	}, nil
}

func (s *Service) CompleteChallenge(ctx context.Context, playerId, challengeId string) (map[string]interface{}, error) {
	// Check if challenge exists and not completed
	challenge, err := s.repo.GetPlayerChallenge(ctx, playerId, challengeId)
	if err != nil {
		return nil, err
	}

	if challenge.CompletedAt != nil {
		return nil, ErrAlreadyCompleted
	}

	// Get challenge details for XP reward
	challengeDetails, err := s.repo.GetChallengeDetails(ctx, challengeId)
	if err != nil {
		return nil, err
	}

	// Mark challenge as completed
	if err := s.repo.MarkChallengeCompleted(ctx, playerId, challengeId); err != nil {
		return nil, err
	}

	// Add XP
	newLevel, err := s.addXPInternal(ctx, playerId, challengeDetails.XpReward, "weekly_challenge")
	if err != nil {
		return nil, err
	}

	return map[string]interface{}{
		"xp_awarded": challengeDetails.XpReward,
		"new_level":  newLevel,
	}, nil
}

func (s *Service) AddXP(ctx context.Context, playerId string, xpAmount int, source string) (map[string]interface{}, error) {
	newLevel, err := s.addXPInternal(ctx, playerId, xpAmount, source)
	if err != nil {
		return nil, err
	}

	progress, err := s.repo.GetPlayerProgress(ctx, playerId)
	if err != nil {
		return nil, err
	}

	// Check for level up and new rewards
	levelUp := newLevel > progress.CurrentLevel
	var rewardsUnlocked []api.Reward

	if levelUp {
		// Get unclaimed rewards for new levels
		for level := progress.CurrentLevel + 1; level <= newLevel; level++ {
			// Free track
			reward, err := s.repo.GetReward(ctx, progress.SeasonId, level, "free")
			if err == nil {
				rewardsUnlocked = append(rewardsUnlocked, *reward)
			}

			// Premium track (if has premium)
			if progress.HasPremium {
				reward, err := s.repo.GetReward(ctx, progress.SeasonId, level, "premium")
				if err == nil {
					rewardsUnlocked = append(rewardsUnlocked, *reward)
				}
			}
		}
	}

	return map[string]interface{}{
		"new_xp":           progress.CurrentXp + xpAmount,
		"new_level":        newLevel,
		"level_up":         levelUp,
		"rewards_unlocked": rewardsUnlocked,
	}, nil
}

func (s *Service) addXPInternal(ctx context.Context, playerId string, xpAmount int, source string) (int, error) {
	progress, err := s.repo.GetPlayerProgress(ctx, playerId)
	if err != nil {
		return 0, err
	}

	newXP := progress.CurrentXp + xpAmount
	newLevel := (newXP / XPPerLevel) + 1
	if newLevel > 100 {
		newLevel = 100
	}

	if err := s.repo.UpdateProgress(ctx, playerId, newLevel, newXP); err != nil {
		return 0, err
	}

	return newLevel, nil
}

func respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func respondError(w http.ResponseWriter, status int, message string) {
	respondJSON(w, status, map[string]string{"error": message})
}






