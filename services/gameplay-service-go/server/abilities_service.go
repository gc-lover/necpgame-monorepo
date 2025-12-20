// Package server Issue: #156
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/sirupsen/logrus"
)

type AbilityServiceInterface interface {
	GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) (*api.GetAbilityCatalogOK, error)
	GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error)
	GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error)
	UpdateLoadout(ctx context.Context, characterID uuid.UUID, req *api.AbilityLoadoutCreate) (*api.AbilityLoadout, error)
	GetCooldowns(ctx context.Context, characterID uuid.UUID) (*api.CooldownCheckResponse, error)
	GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) (*api.GetAvailableSynergiesOK, error)
	ActivateAbility(ctx context.Context, characterID uuid.UUID, req *api.AbilityActivationRequest) (*api.AbilityActivationResponse, error)
	ApplySynergy(ctx context.Context, characterID uuid.UUID, req *api.SynergyApplyRequest) (*api.SynergyApplyResponse, error)
	GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error)
	GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error)
}

type AbilityService struct {
	repo   AbilityRepositoryInterface
	logger *logrus.Logger
}

func NewAbilityService(db *pgxpool.Pool) *AbilityService {
	return &AbilityService{
		repo:   NewAbilityRepository(db),
		logger: logrus.New(),
	}
}

func (s *AbilityService) GetCatalog(ctx context.Context, abilityType *api.AbilityType, slot *api.AbilitySlot, source *api.AbilitySource, limit, offset int) (*api.GetAbilityCatalogOK, error) {
	abilities, total, err := s.repo.GetCatalog(ctx, abilityType, slot, source, limit, offset)
	if err != nil {
		return nil, err
	}

	return &api.GetAbilityCatalogOK{
		Abilities: abilities,
		Total:     total,
		Limit:     api.NewOptInt(limit),
		Offset:    api.NewOptInt(offset),
	}, nil
}

func (s *AbilityService) GetAbility(ctx context.Context, abilityID uuid.UUID) (*api.Ability, error) {
	return s.repo.GetAbility(ctx, abilityID)
}

func (s *AbilityService) GetLoadout(ctx context.Context, characterID uuid.UUID) (*api.AbilityLoadout, error) {
	return s.repo.GetLoadout(ctx, characterID)
}

func (s *AbilityService) UpdateLoadout(ctx context.Context, characterID uuid.UUID, req *api.AbilityLoadoutCreate) (*api.AbilityLoadout, error) {
	loadout, err := s.repo.GetLoadout(ctx, characterID)
	if err != nil {
		// Create new loadout if not exists
		loadout = &api.AbilityLoadout{
			ID:          uuid.New(),
			CharacterID: characterID,
			CreatedAt:   api.NewOptDateTime(time.Now()),
		}
	}

	if req.SlotQ.Set {
		loadout.SlotQ = req.SlotQ
	}
	if req.SlotE.Set {
		loadout.SlotE = req.SlotE
	}
	if req.SlotR.Set {
		loadout.SlotR = req.SlotR
	}
	if len(req.PassiveAbilities) > 0 {
		loadout.PassiveAbilities = req.PassiveAbilities
	}
	if len(req.HackingAbilities) > 0 {
		loadout.HackingAbilities = req.HackingAbilities
	}
	if req.AutoCastEnabled.Set {
		loadout.AutoCastEnabled = req.AutoCastEnabled
	}

	loadout.UpdatedAt = api.NewOptDateTime(time.Now())

	return s.repo.SaveLoadout(ctx, loadout)
}

func (s *AbilityService) GetCooldowns(ctx context.Context, characterID uuid.UUID) (*api.CooldownCheckResponse, error) {
	cooldowns, err := s.repo.GetCooldowns(ctx, characterID)
	if err != nil {
		return nil, err
	}

	return &api.CooldownCheckResponse{
		Cooldowns: cooldowns,
	}, nil
}

func (s *AbilityService) GetAvailableSynergies(ctx context.Context, characterID uuid.UUID, abilityID *uuid.UUID) (*api.GetAvailableSynergiesOK, error) {
	synergies, err := s.repo.GetAvailableSynergies(ctx, characterID, abilityID)
	if err != nil {
		return nil, err
	}

	return &api.GetAvailableSynergiesOK{
		Synergies: synergies,
	}, nil
}

func (s *AbilityService) ActivateAbility(ctx context.Context, characterID uuid.UUID, req *api.AbilityActivationRequest) (*api.AbilityActivationResponse, error) {
	// Check if ability exists
	ability, err := s.repo.GetAbility(ctx, req.AbilityID)
	if err != nil {
		return nil, errors.New("ability not found")
	}

	// Check cooldown
	cooldowns, err := s.repo.GetCooldowns(ctx, characterID)
	if err == nil {
		for _, cd := range cooldowns {
			if cd.AbilityID == req.AbilityID && cd.IsOnCooldown {
				// Check if cooldown expired
				if cd.ExpiresAt.Set && cd.ExpiresAt.Value.After(time.Now()) {
					return nil, errors.New("ability is on cooldown")
				}
			}
		}
	}

	// Start cooldown
	cooldownDuration := 30 // Default, should come from ability
	if ability.CooldownBase.Set {
		cooldownDuration = ability.CooldownBase.Value
	}

	err = s.repo.StartCooldown(ctx, characterID, req.AbilityID, time.Duration(cooldownDuration)*time.Second)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to start cooldown")
	}

	// Record activation
	var targetID *uuid.UUID
	if req.TargetID.Set {
		targetID = &req.TargetID.Value
	}
	err = s.repo.RecordActivation(ctx, characterID, req.AbilityID, targetID)
	if err != nil {
		s.logger.WithError(err).Warn("Failed to record activation")
	}

	// Check for synergies
	synergyTriggered := false
	synergies, err := s.repo.GetAvailableSynergies(ctx, characterID, &req.AbilityID)
	if err == nil && len(synergies) > 0 {
		synergyTriggered = true
	}

	// Update cyberpsychosis (basic implementation)
	cyberpsychosisUpdated := false
	if ability.CyberpsychosisImpact.Set && ability.CyberpsychosisImpact.Value > 0 {
		err = s.repo.UpdateCyberpsychosis(ctx, characterID, ability.CyberpsychosisImpact.Value)
		if err == nil {
			cyberpsychosisUpdated = true
		}
	}

	return &api.AbilityActivationResponse{
		AbilityID:             req.AbilityID,
		Success:               true,
		Message:               api.NewOptNilString("Ability activated successfully"),
		CooldownStarted:       api.NewOptBool(true),
		SynergyTriggered:      api.NewOptBool(synergyTriggered),
		CyberpsychosisUpdated: api.NewOptBool(cyberpsychosisUpdated),
	}, nil
}

// ApplySynergy applies a synergy to abilities
// Issue: #156
func (s *AbilityService) ApplySynergy(ctx context.Context, characterID uuid.UUID, req *api.SynergyApplyRequest) (*api.SynergyApplyResponse, error) {
	// Check if synergy exists
	synergy, err := s.repo.GetSynergy(ctx, req.SynergyID)
	if err != nil {
		return nil, errors.New("synergy not found")
	}

	// Check requirements
	requirementsMet, err := s.repo.CheckSynergyRequirements(ctx, characterID, synergy)
	if err != nil {
		return nil, err
	}
	if !requirementsMet {
		return nil, errors.New("synergy requirements not met")
	}

	// Apply synergy
	err = s.repo.ApplySynergy(ctx, characterID, req.SynergyID, synergy)
	if err != nil {
		return nil, err
	}

	bonusesApplied := api.SynergyApplyResponseBonusesApplied{
		DamageMultiplier:  synergy.Bonuses.DamageMultiplier,
		CooldownReduction: synergy.Bonuses.CooldownReduction,
	}

	return &api.SynergyApplyResponse{
		SynergyID:      req.SynergyID,
		Success:        true,
		Message:        api.NewOptNilString("Synergy applied successfully"),
		BonusesApplied: api.NewOptSynergyApplyResponseBonusesApplied(bonusesApplied),
	}, nil
}

// GetCyberpsychosisState returns current cyberpsychosis state
// Issue: #156
func (s *AbilityService) GetCyberpsychosisState(ctx context.Context, characterID uuid.UUID) (*api.CyberpsychosisState, error) {
	state, err := s.repo.GetCyberpsychosisState(ctx, characterID)
	if err != nil {
		return nil, err
	}
	return state, nil
}

// GetAbilityMetrics returns ability usage metrics
// Issue: #156
func (s *AbilityService) GetAbilityMetrics(ctx context.Context, characterID uuid.UUID, abilityID api.OptUUID, periodStart api.OptDateTime, periodEnd api.OptDateTime) (*api.AbilityMetrics, error) {
	metrics, err := s.repo.GetAbilityMetrics(ctx, characterID, abilityID, periodStart, periodEnd)
	if err != nil {
		return nil, err
	}
	return metrics, nil
}
