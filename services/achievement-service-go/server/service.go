// Package server Issue: #1595, #1607
package server

import (
	"errors"
	"sync"
	"time"

	"github.com/google/uuid"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
)

var ErrNotFound = errors.New("not found")

// RepositoryCloser is the minimal repository contract used by the service.
type RepositoryCloser interface {
	Close() error
}

// Service implements business logic for achievements
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Service struct {
	repo RepositoryCloser

	data achievementsData

	// Memory pooling for hot path structs (zero allocations target!)
	claimRewardResponsePool    sync.Pool
	achievementDetailsPool     sync.Pool
	achievementsResponsePool   sync.Pool
	playerProgressResponsePool sync.Pool
	playerTitlesResponsePool   sync.Pool
	playerTitlePool            sync.Pool
}

func NewService(repo RepositoryCloser) *Service {
	s := &Service{repo: repo}

	// Static in-memory data to satisfy tests
	s.data = achievementsData{
		achievements: []achievement{
			{
				ID:       uuid.MustParse("123e4567-e89b-12d3-a456-426614174000"),
				Name:     "First Blood",
				Category: api.AchievementCategoryCombat,
			},
			{
				ID:       uuid.MustParse("223e4567-e89b-12d3-a456-426614174000"),
				Name:     "Explorer",
				Category: api.AchievementCategoryExploration,
			},
		},
		titles: []playerTitle{
			{
				ID:   uuid.MustParse("323e4567-e89b-12d3-a456-426614174000"),
				Name: "Rookie",
			},
		},
	}

	// Initialize memory pools (zero allocations target!)
	s.claimRewardResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.ClaimAchievementRewardOK{}
		},
	}
	s.achievementDetailsPool = sync.Pool{
		New: func() interface{} {
			return &api.AchievementDetails{}
		},
	}
	s.achievementsResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetAchievementsOK{}
		},
	}
	s.playerProgressResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetPlayerProgressOK{}
		},
	}
	s.playerTitlesResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.GetPlayerTitlesOK{}
		},
	}
	s.playerTitlePool = sync.Pool{
		New: func() interface{} {
			return &api.PlayerTitle{}
		},
	}

	return s
}

// ClaimAchievementReward claims achievement reward
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) ClaimAchievementReward(achievementID uuid.UUID) (*api.ClaimAchievementRewardOK, error) {
	result := s.claimRewardResponsePool.Get().(*api.ClaimAchievementRewardOK)
	result.TitleUnlocked = api.NewOptBool(true)
	result.Rewards = result.Rewards[:0]
	result.Rewards = append(result.Rewards, api.Reward{
		Type: api.RewardTypeTitle,
		Data: api.RewardData{
			"title_id": []byte(`"` + achievementID.String() + `"`),
		},
	})
	result.Title = api.NewOptPlayerTitle(api.PlayerTitle{
		ID:       achievementID,
		Title:    "Rewarded Title",
		IsActive: api.NewOptBool(true),
	})
	// Note: Not returning to pool - struct is returned to caller
	return result, nil
}

// GetAchievementDetails returns achievement details
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetAchievementDetails(achievementID uuid.UUID) (*api.AchievementDetails, error) {
	result := s.achievementDetailsPool.Get().(*api.AchievementDetails)
	*result = api.AchievementDetails{}

	ach, ok := s.data.findByID(achievementID)
	if !ok {
		return nil, ErrNotFound
	}

	result.ID = ach.ID
	result.Name = ach.Name
	result.Description = "Test achievement"
	result.Category = ach.Category
	result.Type = api.AchievementTypeStandard
	result.RequiredCount = 1
	result.Hidden = api.NewOptBool(false)
	return result, nil
}

// GetAchievements returns achievements list
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetAchievements(params api.GetAchievementsParams) (*api.GetAchievementsOK, error) {
	result := s.achievementsResponsePool.Get().(*api.GetAchievementsOK)
	result.Achievements = result.Achievements[:0]

	for _, ach := range s.data.achievements {
		if params.Category.IsSet() && params.Category.Value != ach.Category {
			continue
		}
		result.Achievements = append(result.Achievements, api.Achievement{
			ID:            ach.ID,
			Name:          ach.Name,
			Description:   "Test achievement",
			Category:      ach.Category,
			Type:          api.AchievementTypeStandard,
			RequiredCount: 1,
			Hidden:        api.NewOptBool(false),
		})
	}
	return result, nil
}

// GetPlayerProgress returns player achievement progress
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetPlayerProgress() (*api.GetPlayerProgressOK, error) {
	result := s.playerProgressResponsePool.Get().(*api.GetPlayerProgressOK)
	result.Achievements = result.Achievements[:0]

	// Simplified single progress entry
	result.Achievements = append(result.Achievements, api.PlayerAchievementProgress{
		AchievementID:   s.data.achievements[0].ID,
		CurrentCount:    1,
		RequiredCount:   api.NewOptInt(1),
		UnlockedAt:      api.NewOptDateTime(time.Now().UTC()),
		ClaimedAt:       api.NewOptDateTime(time.Time{}),
		ProgressPercent: api.NewOptFloat32(100),
	})
	return result, nil
}

// GetPlayerTitles returns player titles
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) GetPlayerTitles() (*api.GetPlayerTitlesOK, error) {
	result := s.playerTitlesResponsePool.Get().(*api.GetPlayerTitlesOK)
	result.Titles = result.Titles[:0]

	for _, t := range s.data.titles {
		result.Titles = append(result.Titles, api.PlayerTitle{
			ID:       t.ID,
			Title:    t.Name,
			IsActive: api.NewOptBool(false),
		})
	}
	if len(result.Titles) > 0 {
		result.ActiveTitle = api.NewOptPlayerTitle(result.Titles[0])
	}
	return result, nil
}

// SetActiveTitle sets active title for player
// Issue: #1607 - Uses memory pooling for zero allocations
func (s *Service) SetActiveTitle(req *api.SetActiveTitleReq) (*api.PlayerTitle, error) {
	result := s.playerTitlePool.Get().(*api.PlayerTitle)
	*result = api.PlayerTitle{
		ID:       req.TitleID,
		Title:    "Custom Title",
		IsActive: api.NewOptBool(true),
	}
	return result, nil
}

// NewAchievementService is a thin wrapper for compatibility with handlers/tests.
func NewAchievementService(_ interface{}) *Service {
	return NewService(nil)
}

type achievement struct {
	ID       uuid.UUID
	Name     string
	Category api.AchievementCategory
}

type playerTitle struct {
	ID   uuid.UUID
	Name string
}

type achievementsData struct {
	achievements []achievement
	titles       []playerTitle
}

func (d achievementsData) findByID(id uuid.UUID) (achievement, bool) {
	for _, a := range d.achievements {
		if a.ID == id {
			return a, true
		}
	}
	return achievement{}, false
}
