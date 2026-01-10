package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/ability-service-go/config"
	"necpgame/services/ability-service-go/internal/repository"
	api "necpgame/services/ability-service-go/pkg/api"
)

// Handler implements the generated Handler interface
type Handler struct {
	logger *zap.Logger
	repo   *repository.Repository
	config *config.Config
}

// CombatAbilitiesHealthCheck implements combatAbilitiesHealthCheck operation.
func (h *Handler) CombatAbilitiesHealthCheck(ctx context.Context, params api.CombatAbilitiesHealthCheckParams) (api.CombatAbilitiesHealthCheckRes, error) {
	// PERFORMANCE: Add context timeout for health check
	timeoutCtx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	healthy := true
	if err := h.repo.HealthCheck(timeoutCtx); err != nil {
		healthy = false
		h.logger.Warn("Health check failed", zap.Error(err))
	}

	status := api.HealthResponseStatusHealthy
	if !healthy {
		status = api.HealthResponseStatusUnhealthy
	}

	return &api.HealthResponse{
		Status:    status,
		Domain:    api.NewOptString("ability-service"),
		Timestamp: time.Now(),
		Version:   api.NewOptString("1.0.0"),
	}, nil
}

// CombatAbilitiesListAbilities implements combatAbilitiesListAbilities operation.
func (h *Handler) CombatAbilitiesListAbilities(ctx context.Context, params api.CombatAbilitiesListAbilitiesParams) (*api.ListAbilitiesResponse, error) {
	// PERFORMANCE: Add context timeout for database operations (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	// Parse pagination parameters with defaults
	limit := 20 // default limit
	offset := 0 // default offset

	if params.Limit.IsSet() && params.Limit.Value > 0 {
		limit = params.Limit.Value
		// Enforce maximum limit for performance
		if limit > 100 {
			limit = 100
		}
	}

	if params.Offset.IsSet() && params.Offset.Value >= 0 {
		offset = params.Offset.Value
	}

	// Query abilities from database
	abilities, err := h.repo.ListAbilities(dbCtx, limit, offset)
	if err != nil {
		h.logger.Error("Failed to list abilities from database",
			zap.Error(err),
			zap.Int("limit", limit),
			zap.Int("offset", offset))
		return nil, fmt.Errorf("failed to retrieve abilities: %w", err)
	}

	// Convert repository models to API models
	apiAbilities := make([]api.Ability, len(abilities))
	for i, ability := range abilities {
		apiAbilities[i] = h.convertAbilityToAPI(ability)
	}

	// Get total count for pagination (simplified - could be optimized with separate COUNT query)
	totalCount := len(apiAbilities) // This is approximate, would need COUNT(*) for exact total

	h.logger.Info("Successfully retrieved abilities list",
		zap.Int("count", len(apiAbilities)),
		zap.Int("limit", limit),
		zap.Int("offset", offset))

	return &api.ListAbilitiesResponse{
		Abilities:  apiAbilities,
		TotalCount: totalCount,
	}, nil
}

// CombatAbilitiesActivateAbility implements combatAbilitiesActivateAbility operation.
func (h *Handler) CombatAbilitiesActivateAbility(ctx context.Context, req *api.ActivateAbilityRequest) (api.CombatAbilitiesActivateAbilityRes, error) {
	// PERFORMANCE: Add context timeout for ability activation
	timeoutCtx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	svc := &Service{logger: h.logger, repo: h.repo, config: h.config}

	err := svc.ActivateAbility(timeoutCtx, req.CharacterID.String(), req.AbilityID.String())
	if err != nil {
		h.logger.Error("Failed to activate ability",
			zap.String("character_id", req.CharacterID.String()),
			zap.String("ability_id", req.AbilityID.String()),
			zap.Error(err))
		return &api.ActivateAbilityBadRequest{
			Code:    "400",
			Message: err.Error(),
		}, nil
	}

	// Generate activation ID
	activationID := uuid.New()

	return &api.ActivateAbilityResponse{
		ActivationID: activationID,
		AbilityID:    api.NewOptUUID(req.AbilityID),
		CharacterID:  api.NewOptUUID(req.CharacterID),
	}, nil
}

// CombatAbilitiesGetAbilityCooldown implements combatAbilitiesGetAbilityCooldown operation.
func (h *Handler) CombatAbilitiesGetAbilityCooldown(ctx context.Context, params api.CombatAbilitiesGetAbilityCooldownParams) (api.CombatAbilitiesGetAbilityCooldownRes, error) {
	// PERFORMANCE: Add context timeout for cooldown check
	timeoutCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	cooldown, err := h.repo.GetAbilityCooldown(timeoutCtx, params.PlayerId.String(), params.AbilityID.String())
	if err != nil {
		h.logger.Error("Failed to get ability cooldown",
			zap.String("player_id", params.PlayerId.String()),
			zap.String("ability_id", params.AbilityID.String()),
			zap.Error(err))
		return &api.GetAbilityCooldownNotFound{
			Code:    "500",
			Message: "Database error: " + err.Error(),
		}, nil
	}

	response := &api.AbilityCooldownResponse{
		AbilityID:    params.AbilityID,
		IsOnCooldown: cooldown != nil,
	}

	if cooldown != nil {
		// Calculate remaining time
		expiresAt, err := time.Parse(time.RFC3339, cooldown.ExpiresAt)
		if err == nil {
			remaining := time.Until(expiresAt)
			if remaining > 0 {
				response.RemainingTime = int32(remaining.Seconds())
			}
		}
	}

	return response, nil
}

// CombatAbilitiesValidateAbilityActivation implements combatAbilitiesValidateAbilityActivation operation.
func (h *Handler) CombatAbilitiesValidateAbilityActivation(ctx context.Context, req *api.ValidateAbilityRequest) (api.CombatAbilitiesValidateAbilityActivationRes, error) {
	// PERFORMANCE: Add context timeout for ability validation
	timeoutCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	svc := &Service{logger: h.logger, repo: h.repo, config: h.config}

	err := svc.ValidateAbilityActivation(timeoutCtx, req.CharacterID.String(), req.AbilityID.String())
	canActivate := err == nil

	response := &api.AbilityValidationResponse{
		CharacterID: req.CharacterID,
		AbilityID:   req.AbilityID,
		CanActivate: canActivate,
	}

	if err != nil {
		response.ErrorMessage = api.NewOptString(err.Error())
	}

	return response, nil
}

// CombatAbilitiesGetAbilitySynergies implements combatAbilitiesGetAbilitySynergies operation.
func (h *Handler) CombatAbilitiesGetAbilitySynergies(ctx context.Context, params api.CombatAbilitiesGetAbilitySynergiesParams) (*api.AbilitySynergiesResponse, error) {
	// PERFORMANCE: Add context timeout for synergy calculation (50ms P99 target)
	dbCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	abilityID := params.AbilityId.String()

	// Get the main ability to check its synergies
	mainAbility, err := h.repo.GetAbilityByID(dbCtx, abilityID)
	if err != nil {
		h.logger.Error("Failed to get ability for synergy calculation",
			zap.String("ability_id", abilityID),
			zap.Error(err))
		return nil, fmt.Errorf("failed to retrieve ability: %w", err)
	}

	if mainAbility == nil {
		return &api.AbilitySynergiesResponse{
			AbilityId:    abilityID,
			Synergies:    []api.AbilitySynergiesResponseSynergiesItem{},
			SynergyCount: 0,
		}, nil
	}

	// Calculate synergies based on the ability's synergy_abilities field
	synergies := []api.AbilitySynergiesResponseSynergiesItem{}

	for _, synergyID := range mainAbility.SynergyAbilities {
		// Get details of the synergistic ability
		synergyAbility, err := h.repo.GetAbilityByID(dbCtx, synergyID)
		if err != nil {
			h.logger.Warn("Failed to get synergistic ability",
				zap.String("main_ability", abilityID),
				zap.String("synergy_ability", synergyID),
				zap.Error(err))
			continue // Skip this synergy if we can't load it
		}

		if synergyAbility != nil {
			// Calculate synergy bonus (simplified - in production would be more complex)
			synergyBonus := h.calculateSynergyBonus(mainAbility, synergyAbility)

			synergyItem := api.AbilitySynergiesResponseSynergiesItem{
				AbilityId:   synergyID,
				AbilityName: synergyAbility.Name,
				Bonus:       synergyBonus,
				Type:        h.determineSynergyType(mainAbility, synergyAbility),
			}

			// Add optional description
			if synergyAbility.Description != nil {
				synergyItem.Description = api.NewOptString(fmt.Sprintf("Synergy with %s: %s",
					synergyAbility.Name, *synergyAbility.Description))
			}

			synergies = append(synergies, synergyItem)
		}
	}

	h.logger.Info("Calculated ability synergies",
		zap.String("ability_id", abilityID),
		zap.Int("synergy_count", len(synergies)))

	return &api.AbilitySynergiesResponse{
		AbilityId:    abilityID,
		Synergies:    synergies,
		SynergyCount: len(synergies),
	}, nil
}

// calculateSynergyBonus calculates the synergy bonus between two abilities
func (h *Handler) calculateSynergyBonus(mainAbility, synergyAbility *repository.Ability) string {
	// Simplified synergy calculation - in production would use complex game balance formulas
	bonus := "15% damage increase"

	// Example synergy rules (would be configurable in production)
	switch {
	case mainAbility.Type == "offensive" && synergyAbility.Type == "buff":
		bonus = "25% damage increase, 10% crit chance"
	case mainAbility.Type == "defensive" && synergyAbility.Type == "healing":
		bonus = "30% healing effectiveness, 20% damage reduction"
	case mainAbility.Type == synergyAbility.Type:
		bonus = "20% effectiveness increase"
	}

	return bonus
}

// determineSynergyType determines the type of synergy between abilities
func (h *Handler) determineSynergyType(mainAbility, synergyAbility *repository.Ability) string {
	// Determine synergy type based on ability types
	switch {
	case mainAbility.Type == "offensive" && synergyAbility.Type == "buff":
		return "damage_buff"
	case mainAbility.Type == "defensive" && synergyAbility.Type == "healing":
		return "defensive_heal"
	case mainAbility.Type == synergyAbility.Type:
		return "same_type_boost"
	case mainAbility.Type == "utility" || synergyAbility.Type == "utility":
		return "utility_enhancement"
	default:
		return "general_synergy"
	}
}

// convertAbilityToAPI converts repository Ability model to API Ability model
func (h *Handler) convertAbilityToAPI(ability *repository.Ability) api.Ability {
	apiAbility := api.Ability{
		Id:          ability.ID,
		Name:        ability.Name,
		Type:        ability.Type,
		Cooldown:    ability.Cooldown,
		CreatedAt:   ability.CreatedAt,
		UpdatedAt:   ability.UpdatedAt,
	}

	// Handle optional fields
	if ability.Description != nil {
		apiAbility.Description = api.NewOptString(*ability.Description)
	}

	if ability.ManaCost != nil {
		apiAbility.ManaCost = api.NewOptInt(*ability.ManaCost)
	}

	// Convert synergy abilities array
	if len(ability.SynergyAbilities) > 0 {
		synergies := make([]api.AbilitySynergiesItem, len(ability.SynergyAbilities))
		for i, synergyID := range ability.SynergyAbilities {
			synergies[i] = api.AbilitySynergiesItem{
				AbilityId: synergyID,
			}
		}
		apiAbility.SynergyAbilities = synergies
	} else {
		apiAbility.SynergyAbilities = []api.AbilitySynergiesItem{}
	}

	return apiAbility
}