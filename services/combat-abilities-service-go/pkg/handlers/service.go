package handlers

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"combat-abilities-service-go/pkg/cache"
	"combat-abilities-service-go/pkg/models"
	"combat-abilities-service-go/pkg/repository"
)

type Service struct {
	repo  repository.Repository
	cache cache.Cache
}

func NewService(repo repository.Repository, cache cache.Cache) *Service {
	return &Service{
		repo:  repo,
		cache: cache,
	}
}

// PERFORMANCE: Memory pools for response objects to reduce GC pressure in high-throughput combat service
var (
	abilityResponsePool = sync.Pool{
		New: func() interface{} {
			return &AbilityResponse{}
		},
	}
	activationResponsePool = sync.Pool{
		New: func() interface{} {
			return &ActivationResponse{}
		},
	}
	cooldownResponsePool = sync.Pool{
		New: func() interface{} {
			return &CooldownResponse{}
		},
	}
	synergyResponsePool = sync.Pool{
		New: func() interface{} {
			return &SynergyResponse{}
		},
	}
)

// Response structs for API
type AbilityResponse struct {
	ID               uuid.UUID         `json:"id"`
	Name             string            `json:"name"`
	Type             models.AbilityType `json:"type"`
	DamageType       models.DamageType `json:"damage_type"`
	CooldownMs       int               `json:"cooldown_ms"`
	ResourceCost     models.ResourceCost `json:"resource_cost"`
	Range            float64           `json:"range"`
	AreaOfEffect     float64           `json:"area_of_effect"`
	LevelRequirement int               `json:"level_requirement"`
	IsUnlocked       bool              `json:"is_unlocked"`
}

type ActivationResponse struct {
	ActivationID    uuid.UUID             `json:"activation_id"`
	AbilityID       uuid.UUID             `json:"ability_id"`
	CharacterID     uuid.UUID             `json:"character_id"`
	ResourceCost    models.ResourceCost   `json:"resource_cost"`
	SynergyBonus    float64               `json:"synergy_bonus"`
	ValidationToken string                `json:"validation_token"`
	CooldownEndTime int64                 `json:"cooldown_end_time"`
	Success         bool                  `json:"success"`
}

type CooldownResponse struct {
	AbilityID       uuid.UUID `json:"ability_id"`
	IsOnCooldown    bool      `json:"is_on_cooldown"`
	RemainingTime   int       `json:"remaining_time_ms"`
	TotalCooldown   int       `json:"total_cooldown_ms"`
	LastUsed        int64     `json:"last_used_timestamp"`
}

type SynergyResponse struct {
	AbilityID uuid.UUID    `json:"ability_id"`
	Synergies []SynergyInfo `json:"synergies"`
}

type SynergyInfo struct {
	PartnerAbilityID uuid.UUID `json:"partner_ability_id"`
	SynergyType      string    `json:"synergy_type"`
	BonusMultiplier  float64   `json:"bonus_multiplier"`
	Condition        string    `json:"condition"`
	IsActive         bool      `json:"is_active"`
}

// Core business logic methods

func (s *Service) GetCharacterAbilities(ctx context.Context, characterID uuid.UUID) ([]AbilityResponse, error) {
	// Try cache first
	cached, err := s.cache.GetCharacterAbilities(ctx, characterID)
	if err != nil {
		return nil, fmt.Errorf("cache error: %w", err)
	}

	if len(cached) > 0 {
		return s.convertCharacterAbilitiesToResponses(cached), nil
	}

	// Fallback to repository
	charAbilities, err := s.repo.GetCharacterAbilities(ctx, characterID)
	if err != nil {
		return nil, fmt.Errorf("repository error: %w", err)
	}

	// Cache results
	for _, ca := range charAbilities {
		s.cache.SetCharacterAbility(ctx, &ca)
	}

	return s.convertCharacterAbilitiesToResponses(charAbilities), nil
}

func (s *Service) ActivateAbility(ctx context.Context, req *ActivateAbilityRequest) (*ActivationResponse, error) {
	// Validate ability activation
	if err := s.repo.ValidateAbilityActivation(ctx, req.CharacterID, req.AbilityID); err != nil {
		return nil, fmt.Errorf("validation failed: %w", err)
	}

	// Check cooldown
	cooldown, err := s.cache.GetCooldown(ctx, req.CharacterID, req.AbilityID)
	if err != nil {
		return nil, fmt.Errorf("cooldown check failed: %w", err)
	}
	if cooldown > 0 {
		return nil, fmt.Errorf("ability on cooldown")
	}

	// Get ability details
	ability, err := s.repo.GetAbility(ctx, req.AbilityID)
	if err != nil {
		return nil, fmt.Errorf("ability lookup failed: %w", err)
	}
	if ability == nil {
		return nil, fmt.Errorf("ability not found")
	}

	// Calculate synergy bonus
	synergyBonus := s.calculateSynergyBonus(ctx, req.CharacterID, req.AbilityID, req.SynergyAbilities)

	// Create activation record
	activation := &models.AbilityActivation{
		AbilityID:        req.AbilityID,
		CharacterID:      req.CharacterID,
		TargetEntityID:   req.TargetEntityID,
		TargetPosition:   models.Vector3{X: req.TargetPosition.X, Y: req.TargetPosition.Y, Z: req.TargetPosition.Z},
		SynergyAbilities: req.SynergyAbilities,
		Status:           models.ActivationStatusActive,
		ResourceCost:     ability.ResourceCost,
		SynergyBonus:     synergyBonus,
		ValidationToken:  s.generateValidationToken(),
		ClientTimestamp:  req.ClientTimestamp,
		ServerTimestamp:  time.Now().UnixMilli(),
		CooldownEndTime:  time.Now().Add(time.Duration(ability.CooldownMs) * time.Millisecond).UnixMilli(),
	}

	if err := s.repo.CreateActivation(ctx, activation); err != nil {
		return nil, fmt.Errorf("activation creation failed: %w", err)
	}

	// Set cooldown in cache
	cooldownDuration := time.Duration(ability.CooldownMs) * time.Millisecond
	s.cache.SetCooldown(ctx, req.CharacterID, req.AbilityID, ability.CooldownMs, cooldownDuration)

	// Cache activation
	s.cache.SetActivation(ctx, activation)

	// Prepare response
	resp := &ActivationResponse{
		ActivationID:    activation.ID,
		AbilityID:       activation.AbilityID,
		CharacterID:     activation.CharacterID,
		ResourceCost:    activation.ResourceCost,
		SynergyBonus:    activation.SynergyBonus,
		ValidationToken: activation.ValidationToken,
		CooldownEndTime: activation.CooldownEndTime,
		Success:         true,
	}

	return resp, nil
}

func (s *Service) GetAbilityCooldown(ctx context.Context, characterID, abilityID uuid.UUID) (*CooldownResponse, error) {
	// Try cache first
	remaining, err := s.cache.GetCooldown(ctx, characterID, abilityID)
	if err != nil {
		return nil, fmt.Errorf("cache error: %w", err)
	}

	// Get ability details for total cooldown
	ability, err := s.repo.GetAbility(ctx, abilityID)
	if err != nil {
		return nil, fmt.Errorf("ability lookup failed: %w", err)
	}

	resp := &CooldownResponse{
		AbilityID:     abilityID,
		IsOnCooldown:  remaining > 0,
		RemainingTime: remaining,
		TotalCooldown: ability.CooldownMs,
		LastUsed:      time.Now().Add(-time.Duration(ability.CooldownMs-remaining) * time.Millisecond).UnixMilli(),
	}

	return resp, nil
}

func (s *Service) GetAbilitySynergies(ctx context.Context, abilityID uuid.UUID) (*SynergyResponse, error) {
	ability, err := s.repo.GetAbility(ctx, abilityID)
	if err != nil {
		return nil, fmt.Errorf("ability lookup failed: %w", err)
	}
	if ability == nil {
		return nil, fmt.Errorf("ability not found")
	}

	var synergies []SynergyInfo
	for _, synergy := range ability.Synergies {
		synergies = append(synergies, SynergyInfo{
			PartnerAbilityID: synergy.PartnerAbilityID,
			SynergyType:      synergy.Type,
			BonusMultiplier:  synergy.BonusMultiplier,
			Condition:        synergy.Condition,
			IsActive:         true, // TODO: Implement synergy activation logic
		})
	}

	resp := &SynergyResponse{
		AbilityID: abilityID,
		Synergies: synergies,
	}

	return resp, nil
}

func (s *Service) ValidateAbilityActivation(ctx context.Context, req *ValidateAbilityRequest) (*ValidateAbilityResponse, error) {
	// Perform comprehensive validation
	violations := []string{}

	// Check if character has the ability
	charAbilities, err := s.repo.GetCharacterAbilities(ctx, req.CharacterID)
	if err != nil {
		return nil, fmt.Errorf("character abilities lookup failed: %w", err)
	}

	hasAbility := false
	for _, ca := range charAbilities {
		if ca.AbilityID == req.AbilityID && ca.IsUnlocked {
			hasAbility = true
			break
		}
	}
	if !hasAbility {
		violations = append(violations, "ability_not_available")
	}

	// Check cooldown
	cooldown, err := s.cache.GetCooldown(ctx, req.CharacterID, req.AbilityID)
	if err != nil {
		return nil, fmt.Errorf("cooldown check failed: %w", err)
	}
	if cooldown > 0 {
		violations = append(violations, "ability_on_cooldown")
	}

	// Calculate confidence score based on violations
	confidence := 1.0
	if len(violations) > 0 {
		confidence = 0.0
	}

	resp := &ValidateAbilityResponse{
		IsValid:        len(violations) == 0,
		Violations:     violations,
		ConfidenceScore: confidence,
	}

	return resp, nil
}

// Helper methods

func (s *Service) convertCharacterAbilitiesToResponses(charAbilities []models.CharacterAbility) []AbilityResponse {
	responses := make([]AbilityResponse, 0, len(charAbilities))

	for _, ca := range charAbilities {
		// Get ability details (simplified - in real implementation would batch load)
		ability, _ := s.repo.GetAbility(context.Background(), ca.AbilityID)
		if ability == nil {
			continue
		}

		response := AbilityResponse{
			ID:               ability.ID,
			Name:             ability.Name,
			Type:             ability.Type,
			DamageType:       ability.DamageType,
			CooldownMs:       ability.CooldownMs,
			ResourceCost:     ability.ResourceCost,
			Range:            ability.Range,
			AreaOfEffect:     ability.AreaOfEffect,
			LevelRequirement: ability.LevelRequirement,
			IsUnlocked:       ca.IsUnlocked,
		}

		responses = append(responses, response)
	}

	return responses
}

func (s *Service) calculateSynergyBonus(ctx context.Context, characterID, abilityID uuid.UUID, synergyAbilities []uuid.UUID) float64 {
	if len(synergyAbilities) == 0 {
		return 0.0
	}

	ability, err := s.repo.GetAbility(ctx, abilityID)
	if err != nil || ability == nil {
		return 0.0
	}

	bonus := 0.0
	for _, synergyAbilityID := range synergyAbilities {
		for _, synergy := range ability.Synergies {
			if synergy.PartnerAbilityID == synergyAbilityID {
				bonus += synergy.BonusMultiplier
			}
		}
	}

	return bonus
}

func (s *Service) generateValidationToken() string {
	bytes := make([]byte, 16)
	rand.Read(bytes)
	return hex.EncodeToString(bytes)
}

// Request/Response structs for API
type ActivateAbilityRequest struct {
	AbilityID        uuid.UUID   `json:"ability_id"`
	CharacterID      uuid.UUID   `json:"character_id"`
	TargetEntityID   *uuid.UUID  `json:"target_entity_id,omitempty"`
	TargetPosition   Vector3     `json:"target_position"`
	SynergyAbilities []uuid.UUID `json:"synergy_abilities"`
	ClientTimestamp  int64       `json:"client_timestamp"`
	ClientVersion    string      `json:"client_version"`
}

type Vector3 struct {
	X float64 `json:"x"`
	Y float64 `json:"y"`
	Z float64 `json:"z"`
}

type ValidateAbilityRequest struct {
	AbilityID     uuid.UUID   `json:"ability_id"`
	CharacterID   uuid.UUID   `json:"character_id"`
	ClientHash    string      `json:"client_hash"`
	ActivationData ActivationData `json:"activation_data"`
}

type ActivationData struct {
	Timestamp int64      `json:"timestamp"`
	Position  Vector3     `json:"position"`
	Target    *uuid.UUID  `json:"target,omitempty"`
}

type ValidateAbilityResponse struct {
	IsValid         bool     `json:"is_valid"`
	Violations      []string `json:"violations"`
	ConfidenceScore float64  `json:"confidence_score"`
}
