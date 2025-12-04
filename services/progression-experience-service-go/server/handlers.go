// Issue: #1604, #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/necpgame/progression-experience-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers)
type Handlers struct{}

func NewHandlers() *Handlers {
	return &Handlers{}
}

// AddExperience - TYPED response!
func (h *Handlers) AddExperience(ctx context.Context, req *api.AddExperienceRequest) (api.AddExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	progression := &api.CharacterProgression{
		CharacterID:              api.NewOptUUID(req.PlayerID),
		Level:                    api.NewOptInt(1),
		Experience:               api.NewOptInt(req.ExperienceAmount),
		ExperienceToNextLevel:    api.NewOptInt(1000),
		AvailableAttributePoints: api.NewOptInt(5),
		AvailableSkillPoints:     api.NewOptInt(3),
	}

	return progression, nil
}

// CalculateExperience - TYPED response!
func (h *Handlers) CalculateExperience(ctx context.Context, req *api.CalculateExperienceRequest) (api.CalculateExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	response := &api.ExperienceCalculationResponse{
		BaseXp:    api.NewOptInt(100),
		FinalXp:   api.NewOptInt(120),
		Modifiers: api.NewOptExperienceCalculationResponseModifiers(map[string]float32{}),
	}

	return response, nil
}

// CheckLevelUp - TYPED response!
func (h *Handlers) CheckLevelUp(ctx context.Context, req *api.CheckLevelUpRequest) (api.CheckLevelUpRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	response := &api.LevelUpCheckResponse{
		PlayerID:              api.NewOptUUID(req.PlayerID),
		LevelUpAvailable:      api.NewOptBool(false),
		CurrentLevel:          api.NewOptInt(1),
		NewLevel:              api.OptNilInt{},
		AttributePointsGained: api.OptNilInt{},
		SkillPointsGained:     api.OptNilInt{},
	}

	return response, nil
}

// GetLevelRequirements - TYPED response!
func (h *Handlers) GetLevelRequirements(ctx context.Context, params api.GetLevelRequirementsParams) (api.GetLevelRequirementsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	response := &api.LevelRequirementsResponse{
		Level:              api.NewOptInt(params.Level),
		ExperienceRequired: api.NewOptInt(1000),
		AttributePointsReward: api.NewOptInt(5),
		SkillPointsReward:     api.NewOptInt(3),
	}

	return response, nil
}

// GetPlayerLevel - TYPED response!
func (h *Handlers) GetPlayerLevel(ctx context.Context, params api.GetPlayerLevelParams) (api.GetPlayerLevelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	// TODO: Implement business logic
	response := &api.PlayerLevelResponse{
		PlayerID:              api.NewOptUUID(params.PlayerID),
		Level:                 api.NewOptInt(1),
		Experience:            api.NewOptInt(0),
		ExperienceToNextLevel: api.NewOptInt(1000),
	}

	return response, nil
}

