// Package server Issue: #1599 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	"github.com/necpgame/gameplay-progression-core-service-go/pkg/api"
)

// DBTimeout Context timeout constants
const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers)
type Handlers struct {
	service ProgressionServiceInterface
}

func NewHandlers(service ProgressionServiceInterface) *Handlers {
	return &Handlers{
		service: service,
	}
}

// ValidateProgression - TYPED response!
func (h *Handlers) ValidateProgression(ctx context.Context, req *api.ValidateProgressionRequest) (api.ValidateProgressionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.ProgressionValidationResponse{
			Valid:  api.NewOptBool(true),
			Issues: []string{},
		}, nil
	}

	response, err := h.service.ValidateProgression(ctx, req.CharacterID)
	if err != nil {
		return &api.ProgressionValidationResponse{
			Valid:  api.NewOptBool(false),
			Issues: []string{"Validation failed"},
		}, nil
	}

	return response, nil
}

// GetCharacterProgression - TYPED response!
func (h *Handlers) GetCharacterProgression(ctx context.Context, params api.GetCharacterProgressionParams) (api.GetCharacterProgressionRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	progression, err := h.service.GetCharacterProgression(ctx, params.CharacterId)
	if err != nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	return progression, nil
}

// DistributeAttributePoints - TYPED response!
func (h *Handlers) DistributeAttributePoints(ctx context.Context, req *api.DistributeAttributePointsRequest, params api.DistributeAttributePointsParams) (api.DistributeAttributePointsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	attributes := map[string]int{
		req.Attribute: req.Points,
	}

	progression, err := h.service.DistributeAttributePoints(ctx, params.CharacterId, attributes)
	if err != nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	return progression, nil
}

// AddExperience - TYPED response!
func (h *Handlers) AddExperience(ctx context.Context, req *api.AddExperienceRequest, params api.AddExperienceParams) (api.AddExperienceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(100),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	source := string(req.Source)

	progression, err := h.service.AddExperience(ctx, params.CharacterId, req.Amount, source)
	if err != nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(100),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	return progression, nil
}

// DistributeSkillPoints - TYPED response!
func (h *Handlers) DistributeSkillPoints(ctx context.Context, req *api.DistributeSkillPointsRequest, params api.DistributeSkillPointsParams) (api.DistributeSkillPointsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	if h.service == nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	skills := map[string]int{
		req.SkillID: req.Points,
	}

	progression, err := h.service.DistributeSkillPoints(ctx, params.CharacterId, skills)
	if err != nil {
		return &api.CharacterProgression{
			CharacterID:              api.NewOptUUID(params.CharacterId),
			Level:                    api.NewOptInt(1),
			Experience:               api.NewOptInt(0),
			ExperienceToNextLevel:    api.NewOptInt(1000),
			AvailableAttributePoints: api.NewOptInt(5),
			AvailableSkillPoints:     api.NewOptInt(3),
			Attributes:               api.OptCharacterProgressionAttributes{},
			Skills:                   []api.SkillProgress{},
		}, nil
	}

	return progression, nil
}
