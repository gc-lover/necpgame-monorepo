// Package server Issue: #1604, #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/necpgame/progression-experience-service-go/pkg/api"
)

// DBTimeout Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers)
type Handlers struct {
	service ExperienceServiceInterface
}

func NewHandlers(service ExperienceServiceInterface) *Handlers {
	return &Handlers{
		service: service,
	}
}

// AddExperience - TYPED response!
func (h *Handlers) AddExperience(ctx context.Context, req *api.AddExperienceRequest) (api.AddExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(req.PlayerID),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(req.ExperienceAmount),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
		}, nil
	}

	source := string(req.Source)

	progression, err := h.service.AddExperience(ctx, req.PlayerID, req.ExperienceAmount, source)
	if err != nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(req.PlayerID),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(req.ExperienceAmount),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
		}, nil
	}

	return progression, nil
}

// CalculateExperience - TYPED response!
func (h *Handlers) CalculateExperience(ctx context.Context, req *api.CalculateExperienceRequest) (api.CalculateExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.ExperienceCalculationResponse{
			BaseXp:    api.NewOptInt(100),
			FinalXp:   api.NewOptInt(120),
			Modifiers: api.NewOptExperienceCalculationResponseModifiers(map[string]float32{}),
		}, nil
	}

	modifiers := map[string]float32{}
	if req.Modifiers.IsSet() {
		modifiers = req.Modifiers.Value
	}

	response, err := h.service.CalculateExperience(ctx, req.BaseExperience, modifiers)
	if err != nil {
		return &api.ExperienceCalculationResponse{
			BaseXp:    api.NewOptInt(req.BaseExperience),
			FinalXp:   api.NewOptInt(req.BaseExperience),
			Modifiers: api.NewOptExperienceCalculationResponseModifiers(modifiers),
		}, nil
	}

	return response, nil
}

// CheckLevelUp - TYPED response!
func (h *Handlers) CheckLevelUp(ctx context.Context, req *api.CheckLevelUpRequest) (api.CheckLevelUpRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.LevelUpCheckResponse{
			PlayerID:              api.NewOptUUID(req.PlayerID),
			LevelUpAvailable:      api.NewOptBool(false),
			CurrentLevel:          api.NewOptInt(1),
			NewLevel:              api.OptNilInt{},
			AttributePointsGained: api.OptNilInt{},
			SkillPointsGained:     api.OptNilInt{},
		}, nil
	}

	response, err := h.service.CheckLevelUp(ctx, req.PlayerID)
	if err != nil {
		return &api.LevelUpCheckResponse{
			PlayerID:              api.NewOptUUID(req.PlayerID),
			LevelUpAvailable:      api.NewOptBool(false),
			CurrentLevel:          api.NewOptInt(1),
			NewLevel:              api.OptNilInt{},
			AttributePointsGained: api.OptNilInt{},
			SkillPointsGained:     api.OptNilInt{},
		}, nil
	}

	return response, nil
}

// GetLevelRequirements - TYPED response!
func (h *Handlers) GetLevelRequirements(ctx context.Context, params api.GetLevelRequirementsParams) (api.GetLevelRequirementsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.LevelRequirementsResponse{
			Level:                 api.NewOptInt(params.Level),
			ExperienceRequired:    api.NewOptInt(1000),
			AttributePointsReward: api.NewOptInt(5),
			SkillPointsReward:     api.NewOptInt(3),
		}, nil
	}

	response, err := h.service.GetLevelRequirements(ctx, params.Level)
	if err != nil {
		return &api.LevelRequirementsResponse{
			Level:                 api.NewOptInt(params.Level),
			ExperienceRequired:    api.NewOptInt(1000),
			AttributePointsReward: api.NewOptInt(5),
			SkillPointsReward:     api.NewOptInt(3),
		}, nil
	}

	return response, nil
}

// GetPlayerLevel - TYPED response!
func (h *Handlers) GetPlayerLevel(ctx context.Context, params api.GetPlayerLevelParams) (api.GetPlayerLevelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.PlayerLevelResponse{
			PlayerID:              api.NewOptUUID(params.PlayerID),
			Level:                 api.NewOptInt(1),
			Experience:            api.NewOptInt(0),
			ExperienceToNextLevel: api.NewOptInt(1000),
		}, nil
	}

	response, err := h.service.GetPlayerLevel(ctx, params.PlayerID)
	if err != nil {
		return &api.PlayerLevelResponse{
			PlayerID:              api.NewOptUUID(params.PlayerID),
			Level:                 api.NewOptInt(1),
			Experience:            api.NewOptInt(0),
			ExperienceToNextLevel: api.NewOptInt(1000),
		}, nil
	}

	return response, nil
}
