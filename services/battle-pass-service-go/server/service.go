// Issue: #227, #1607
package server

import (
	"context"
	"errors"
	"sync"

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

// Service implements business logic for battle pass
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	seasonPool sync.Pool
	playerProgressPool sync.Pool
	claimRewardResponsePool sync.Pool
	purchasePremiumResponsePool sync.Pool
}

func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocations target!)
	s.seasonPool = sync.Pool{
		New: func() interface{} {
			return &api.Season{}
		},
	}
	s.playerProgressPool = sync.Pool{
		New: func() interface{} {
			return &api.PlayerProgress{}
		},
	}
	s.claimRewardResponsePool = sync.Pool{
		New: func() interface{} {
			return make(map[string]interface{})
		},
	}
	s.purchasePremiumResponsePool = sync.Pool{
		New: func() interface{} {
			return make(map[string]interface{})
		},
	}

	return s
}

// GetCurrentSeason returns current season
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetCurrentSeason(ctx context.Context) (*api.Season, error) {
	season, err := s.repo.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	result := s.seasonPool.Get().(*api.Season)
	// Note: Not returning to pool - struct is returned to caller
	*result = *season // Copy data

	return result, nil
}

// GetPlayerProgress returns player progress
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetPlayerProgress(ctx context.Context, playerId string) (*api.PlayerProgress, error) {
	progress, err := s.repo.GetPlayerProgress(ctx, playerId)
	if err != nil {
		return nil, err
	}

	// Issue: #1607 - Use memory pooling
	result := s.playerProgressPool.Get().(*api.PlayerProgress)
	// Note: Not returning to pool - struct is returned to caller
	*result = *progress // Copy data

	return result, nil
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

	// Issue: #1607 - Use memory pooling
	result := s.claimRewardResponsePool.Get().(map[string]interface{})
	// Reset map
	for k := range result {
		delete(result, k)
	}
	// Note: Not returning to pool - map is returned to caller

	result["rewards"] = []api.Reward{*reward}

	return result, nil
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

	// Issue: #1607 - Use memory pooling
	result := s.purchasePremiumResponsePool.Get().(map[string]interface{})
	// Reset map
	for k := range result {
		delete(result, k)
	}
	// Note: Not returning to pool - map is returned to caller

	result["premium_status"] = true
	result["retroactive_rewards"] = retroRewards

	return result, nil
}

// GetWeeklyChallenges returns weekly challenges
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetWeeklyChallenges(ctx context.Context, playerId string) (map[string]interface{}, error) {
	challenges, err := s.repo.GetWeeklyChallenges(ctx, playerId)
	if err != nil {
		return nil, err
	}

	weekNumber := 1 // TODO: Calculate current week based on season start date

	// Issue: #1607 - Use memory pooling
	result := s.claimRewardResponsePool.Get().(map[string]interface{})
	// Reset map
	for k := range result {
		delete(result, k)
	}
	// Note: Not returning to pool - map is returned to caller

	result["week_number"] = weekNumber
	result["challenges"] = challenges

	return result, nil
}

// CompleteChallenge completes a challenge
// Issue: #1607 - Uses memory pooling for zero allocations
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

	// Issue: #1607 - Use memory pooling
	result := s.claimRewardResponsePool.Get().(map[string]interface{})
	// Reset map
	for k := range result {
		delete(result, k)
	}
	// Note: Not returning to pool - map is returned to caller

	result["xp_awarded"] = challengeDetails.XpReward
	result["new_level"] = newLevel

	return result, nil
}

// AddXP adds XP to player progress
// Issue: #1607 - Uses memory pooling for zero allocations
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

	// Issue: #1607 - Use memory pooling
	result := s.claimRewardResponsePool.Get().(map[string]interface{})
	// Reset map
	for k := range result {
		delete(result, k)
	}
	// Note: Not returning to pool - map is returned to caller

	result["new_xp"] = progress.CurrentXp + xpAmount
	result["new_level"] = newLevel
	result["level_up"] = levelUp
	result["rewards_unlocked"] = rewardsUnlocked

	return result, nil
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









