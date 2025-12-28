package handlers

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"fmt"
	"math"
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
		// Check if synergy partner ability exists and condition is met
		isActive := false
		if synergy.Condition != "" {
			// Basic synergy activation logic:
			// - "always": always active
			// - "partner_exists": active if partner ability exists
			// - "both_equipped": would require checking if both abilities are equipped (not implemented yet)
			switch synergy.Condition {
			case "always":
				isActive = true
			case "partner_exists":
				// Check if partner ability exists in database
				partnerAbility, err := s.repo.GetAbility(ctx, synergy.PartnerAbilityID)
				isActive = err == nil && partnerAbility != nil
			default:
				// Unknown condition, default to inactive
				isActive = false
			}
		}

		synergies = append(synergies, SynergyInfo{
			PartnerAbilityID: synergy.PartnerAbilityID,
			SynergyType:      synergy.Type,
			BonusMultiplier:  synergy.BonusMultiplier,
			Condition:        synergy.Condition,
			IsActive:         isActive,
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

// ADVANCED COMBAT SYSTEM METHODS
// Issue: #2219 - Combat System Enhancement - Advanced Combos & Synergies

// ExecuteComboChain executes a combo chain and calculates effects
func (s *Service) ExecuteComboChain(ctx context.Context, playerID uuid.UUID, comboID uuid.UUID, executionData *ComboExecutionData) (*ComboResult, error) {
	// Get combo chain
	combo, err := s.repo.GetComboChain(ctx, comboID)
	if err != nil {
		return nil, fmt.Errorf("failed to get combo chain: %w", err)
	}

	// Validate combo execution
	validation := s.validateComboExecution(combo, executionData)
	if !validation.IsValid {
		return &ComboResult{
			Success:    false,
			Violations: validation.Violations,
		}, nil
	}

	// Calculate combo effects
	effects := s.calculateComboEffects(combo, executionData, playerID)

	// Update combo statistics
	go s.updateComboStats(comboID, true, executionData.TotalTime)

	result := &ComboResult{
		Success:         true,
		ComboID:         comboID,
		ExecutionTime:   executionData.TotalTime,
		BonusEffects:    effects,
		MasteryProgress: s.calculateMasteryProgress(combo, executionData),
	}

	return result, nil
}

// validateComboExecution validates if combo execution meets requirements
func (s *Service) validateComboExecution(combo *models.ComboChain, executionData *ComboExecutionData) *ComboValidation {
	validation := &ComboValidation{IsValid: true}

	// Check ability sequence
	for i, step := range combo.Abilities {
		if i >= len(executionData.AbilitySequence) {
			validation.IsValid = false
			validation.Violations = append(validation.Violations, "incomplete combo sequence")
			break
		}

		executedAbility := executionData.AbilitySequence[i]
		if executedAbility.AbilityID != step.AbilityID {
			validation.IsValid = false
			validation.Violations = append(validation.Violations, fmt.Sprintf("wrong ability at step %d", i+1))
		}

		// Check timing
		if executedAbility.ExecutionTime > step.TimeWindow {
			validation.IsValid = false
			validation.Violations = append(validation.Violations, fmt.Sprintf("step %d executed too slowly", i+1))
		}

		// Check position requirements
		if !s.validatePositionRequirements(step.PositionReq, executedAbility.PositionData) {
			validation.IsValid = false
			validation.Violations = append(validation.Violations, fmt.Sprintf("position requirements not met at step %d", i+1))
		}
	}

	// Check total time
	totalTime := 0
	for _, ability := range executionData.AbilitySequence {
		totalTime += ability.ExecutionTime
	}
	if totalTime > combo.MaxTimeGap*len(combo.Abilities) {
		validation.IsValid = false
		validation.Violations = append(validation.Violations, "combo executed too slowly")
	}

	return validation
}

// validatePositionRequirements checks if position requirements are met
func (s *Service) validatePositionRequirements(req models.ComboPositionReq, positionData *PositionData) bool {
	if req.DistanceToTarget != nil {
		if positionData.DistanceToTarget < req.DistanceToTarget.Min || positionData.DistanceToTarget > req.DistanceToTarget.Max {
			return false
		}
	}

	if req.AngleToTarget != nil {
		if positionData.AngleToTarget < req.AngleToTarget.Min || positionData.AngleToTarget > req.AngleToTarget.Max {
			return false
		}
	}

	if req.MovementSpeed != nil {
		if positionData.MovementSpeed < req.MovementSpeed.Min || positionData.MovementSpeed > req.MovementSpeed.Max {
			return false
		}
	}

	if len(req.TerrainType) > 0 {
		terrainMatch := false
		for _, terrain := range req.TerrainType {
			if terrain == positionData.TerrainType {
				terrainMatch = true
				break
			}
		}
		if !terrainMatch {
			return false
		}
	}

	return true
}

// calculateComboEffects calculates bonus effects from completed combo
func (s *Service) calculateComboEffects(combo *models.ComboChain, executionData *ComboExecutionData, playerID uuid.UUID) []models.ComboEffect {
	effects := make([]models.ComboEffect, len(combo.BonusEffects))

	for i, baseEffect := range combo.BonusEffects {
		effect := baseEffect

		// Apply scaling
		scaling := effect.Scaling
		multiplier := 1.0

		// Level scaling (assuming level 50 for demo)
		playerLevel := 50
		multiplier *= 1.0 + scaling.LevelMultiplier*float64(playerLevel)/100.0

		// Combo length bonus
		multiplier *= 1.0 + scaling.ComboLengthBonus*float64(len(combo.Abilities))

		// Time bonus (faster = better)
		timeRatio := float64(executionData.TotalTime) / float64(combo.MaxTimeGap*len(combo.Abilities))
		if timeRatio < 1.0 {
			multiplier *= 1.0 + scaling.TimeBonus*(1.0-timeRatio)
		}

		// Apply multiplier to effect value
		if damage, ok := effect.Value.(int); ok {
			effect.Value = int(float64(damage) * multiplier)
		} else if damageFloat, ok := effect.Value.(float64); ok {
			effect.Value = damageFloat * multiplier
		}

		effects[i] = effect
	}

	return effects
}

// calculateMasteryProgress calculates progress toward combo mastery
func (s *Service) calculateMasteryProgress(combo *models.ComboChain, executionData *ComboExecutionData) float64 {
	// Simple mastery calculation based on execution time vs optimal time
	optimalTime := combo.MaxTimeGap * len(combo.Abilities) / 2 // Assume optimal is half max time
	timeRatio := float64(executionData.TotalTime) / float64(optimalTime)

	if timeRatio <= 1.0 {
		return 1.0 // Perfect execution
	} else if timeRatio <= 2.0 {
		return 2.0 - timeRatio // Linear degradation
	} else {
		return 0.0 // Too slow
	}
}

// updateComboStats updates combo usage statistics
func (s *Service) updateComboStats(comboID uuid.UUID, success bool, executionTime int) {
	ctx := context.Background()
	key := fmt.Sprintf("combo_stats:%s", comboID.String())

	// In real implementation, this would update database
	// For now, just cache the stats
	stats := map[string]interface{}{
		"last_used":       time.Now(),
		"success":         success,
		"execution_time":  executionTime,
		"usage_count":     1, // Would increment in real implementation
	}

	s.cache.Set(ctx, key, stats, time.Hour)
}

// DiscoverDynamicCombo discovers a new combo pattern from player behavior
func (s *Service) DiscoverDynamicCombo(ctx context.Context, playerID uuid.UUID, abilitySequence []uuid.UUID, executionTimes []int) (*models.DynamicCombo, error) {
	// Analyze ability sequence for patterns
	pattern := s.analyzeAbilityPattern(abilitySequence, executionTimes)

	if pattern.IsValidPattern {
		combo := &models.DynamicCombo{
			ID:            uuid.New(),
			PlayerID:      playerID,
			Name:          fmt.Sprintf("Dynamic Combo %s", generateRandomSuffix()),
			Description:   "AI-discovered combo pattern",
			Steps:         pattern.Steps,
			DiscoveredAt:  time.Now(),
			LastUsedAt:    time.Now(),
			UsageCount:    0,
			SuccessCount:  0,
			AverageTime:   pattern.AverageTime,
			BestTime:      pattern.BestTime,
			MasteryLevel:  1,
			IsOptimized:   true,
			OptimizationScore: pattern.OptimizationScore,
			NeuralPattern: pattern.NeuralPattern,
		}

		// Save to repository
		err := s.repo.SaveDynamicCombo(ctx, combo)
		if err != nil {
			return nil, fmt.Errorf("failed to save dynamic combo: %w", err)
		}

		return combo, nil
	}

	return nil, fmt.Errorf("no valid combo pattern detected")
}

// analyzeAbilityPattern analyzes ability sequence for combo patterns
func (s *Service) analyzeAbilityPattern(abilitySequence []uuid.UUID, executionTimes []int) *PatternAnalysis {
	analysis := &PatternAnalysis{
		IsValidPattern: false,
	}

	if len(abilitySequence) < 3 {
		return analysis // Need at least 3 abilities for a combo
	}

	// Calculate timing statistics
	totalTime := 0
	minTime := executionTimes[0]
	maxTime := executionTimes[0]

	for _, time := range executionTimes {
		totalTime += time
		if time < minTime {
			minTime = time
		}
		if time > maxTime {
			maxTime = time
		}
	}

	averageTime := totalTime / len(executionTimes)

	// Check for rhythmic patterns (similar timing between abilities)
	timeVariance := 0.0
	for i := 1; i < len(executionTimes); i++ {
		diff := float64(executionTimes[i] - executionTimes[i-1])
		timeVariance += diff * diff
	}
	timeVariance /= float64(len(executionTimes) - 1)
	timeVariance = math.Sqrt(timeVariance)

	// Low variance indicates rhythmic execution
	rhythmScore := 1.0 - (timeVariance / float64(averageTime))
	if rhythmScore < 0 {
		rhythmScore = 0
	}

	// Check for ability type alternation (offensive -> defensive -> mobility, etc.)
	typeAlternation := s.calculateTypeAlternation(abilitySequence)

	// Calculate optimization score
	optimizationScore := (rhythmScore * 0.6) + (typeAlternation * 0.4)

	if optimizationScore > 0.7 { // Threshold for valid combo
		analysis.IsValidPattern = true
		analysis.AverageTime = averageTime
		analysis.BestTime = minTime
		analysis.OptimizationScore = optimizationScore
		analysis.NeuralPattern = fmt.Sprintf("rhythm_%.2f_alt_%.2f", rhythmScore, typeAlternation)

		// Create combo steps
		for i, abilityID := range abilitySequence {
			step := models.ComboStep{
				AbilityID:   abilityID,
				Order:       i,
				TimeWindow:  executionTimes[i] + 200, // Add some tolerance
			}
			analysis.Steps = append(analysis.Steps, step)
		}
	}

	return analysis
}

// calculateTypeAlternation calculates how well ability types alternate
func (s *Service) calculateTypeAlternation(abilitySequence []uuid.UUID) float64 {
	if len(abilitySequence) < 2 {
		return 0.0
	}

	alternationCount := 0
	for i := 1; i < len(abilitySequence); i++ {
		// In real implementation, get ability types from repository
		// For demo, assume alternating types
		alternationCount++
	}

	return float64(alternationCount) / float64(len(abilitySequence)-1)
}

// ActivateAdvancedSynergy activates a complex multi-ability synergy
func (s *Service) ActivateAdvancedSynergy(ctx context.Context, playerID uuid.UUID, synergyID uuid.UUID, activationData *SynergyActivationData) (*SynergyResult, error) {
	// Get synergy
	synergy, err := s.repo.GetAdvancedSynergy(ctx, synergyID)
	if err != nil {
		return nil, fmt.Errorf("failed to get synergy: %w", err)
	}

	// Validate activation requirements
	validation := s.validateSynergyActivation(synergy, activationData)
	if !validation.IsValid {
		return &SynergyResult{
			Success:    false,
			Violations: validation.Violations,
		}, nil
	}

	// Calculate synergy effects
	effects := s.calculateSynergyEffects(synergy, activationData, playerID)

	// Update synergy statistics
	synergy.ActivationCount++
	synergy.LastActivatedAt = &time.Now()

	go s.repo.UpdateAdvancedSynergy(ctx, synergy)

	result := &SynergyResult{
		Success:         true,
		SynergyID:       synergyID,
		Effects:         effects,
		Duration:        synergy.DurationMs,
		Cooldown:        synergy.CooldownMs,
		EnergyConsumed:  synergy.EnergyCost,
	}

	return result, nil
}

// validateSynergyActivation validates synergy activation requirements
func (s *Service) validateSynergyActivation(synergy *models.AdvancedSynergy, activationData *SynergyActivationData) *SynergyValidation {
	validation := &SynergyValidation{IsValid: true}

	// Check ability sequence
	if len(synergy.ActivationReq.AbilitySequence) != len(activationData.AbilitySequence) {
		validation.IsValid = false
		validation.Violations = append(validation.Violations, "incorrect ability sequence length")
		return validation
	}

	for i, requiredAbility := range synergy.ActivationReq.AbilitySequence {
		if i >= len(activationData.AbilitySequence) || activationData.AbilitySequence[i] != requiredAbility {
			validation.IsValid = false
			validation.Violations = append(validation.Violations, fmt.Sprintf("wrong ability at position %d", i))
		}
	}

	// Check time window
	totalTime := 0
	for _, time := range activationData.ExecutionTimes {
		totalTime += time
	}
	if totalTime > synergy.ActivationReq.TimeWindow {
		validation.IsValid = false
		validation.Violations = append(validation.Violations, "activation took too long")
	}

	// Check position requirements
	if !s.validatePositionRequirements(synergy.ActivationReq.PositionReq, activationData.PositionData) {
		validation.IsValid = false
		validation.Violations = append(validation.Violations, "position requirements not met")
	}

	// Check state requirements
	if !s.validateStateRequirements(synergy.ActivationReq.StateReq, activationData.StateData) {
		validation.IsValid = false
		validation.Violations = append(validation.Violations, "state requirements not met")
	}

	return validation
}

// validateStateRequirements checks if state requirements are met
func (s *Service) validateStateRequirements(req models.ComboStateReq, stateData *StateData) bool {
	if req.HealthPercent != nil {
		if stateData.HealthPercent < req.HealthPercent.Min || stateData.HealthPercent > req.HealthPercent.Max {
			return false
		}
	}

	if req.EnergyPercent != nil {
		if stateData.EnergyPercent < req.EnergyPercent.Min || stateData.EnergyPercent > req.EnergyPercent.Max {
			return false
		}
	}

	// Check required status effects
	for _, requiredEffect := range req.StatusEffects {
		hasEffect := false
		for _, currentEffect := range stateData.StatusEffects {
			if currentEffect == requiredEffect {
				hasEffect = true
				break
			}
		}
		if !hasEffect {
			return false
		}
	}

	// Check forbidden status effects
	for _, forbiddenEffect := range req.ForbiddenEffects {
		for _, currentEffect := range stateData.StatusEffects {
			if currentEffect == forbiddenEffect {
				return false
			}
		}
	}

	return true
}

// calculateSynergyEffects calculates effects from activated synergy
func (s *Service) calculateSynergyEffects(synergy *models.AdvancedSynergy, activationData *SynergyActivationData, playerID uuid.UUID) []models.AdvancedSynergyEffect {
	effects := make([]models.AdvancedSynergyEffect, len(synergy.Effects))

	for i, baseEffect := range synergy.Effects {
		effect := baseEffect

		// Apply advanced scaling
		scaling := effect.Scaling

		// Apply base scaling
		baseMultiplier := 1.0
		playerLevel := 50 // Would get from player data
		baseMultiplier *= 1.0 + scaling.BaseScaling.LevelMultiplier*float64(playerLevel)/100.0

		// Apply exponential bonuses
		for metric, exponent := range scaling.ExponentialBonus {
			value := s.getMetricValue(metric, activationData)
			if value > 0 {
				baseMultiplier *= math.Pow(value, exponent)
			}
		}

		// Apply diminishing bonuses
		for metric, factor := range scaling.DiminishingBonus {
			value := s.getMetricValue(metric, activationData)
			if value > 0 {
				baseMultiplier *= 1.0 + factor/(1.0+value)
			}
		}

		// Apply threshold bonuses
		for _, threshold := range scaling.ThresholdBonuses {
			value := s.getMetricValue(threshold.Metric, activationData)
			if value >= threshold.Threshold {
				// Apply threshold effect scaling
				threshold.BaseScaling = effect.PrimaryEffect.Scaling
				thresholdEffect := s.calculateComboEffects(&models.ComboChain{BonusEffects: []models.ComboEffect{threshold.BonusEffect}}, &ComboExecutionData{}, playerID)
				if len(thresholdEffect) > 0 {
					// Merge threshold effect with main effect
					baseMultiplier *= 1.2 // 20% bonus for threshold
				}
			}
		}

		// Apply scaling to primary effect
		if damage, ok := effect.PrimaryEffect.Value.(int); ok {
			effect.PrimaryEffect.Value = int(float64(damage) * baseMultiplier)
		} else if damageFloat, ok := effect.PrimaryEffect.Value.(float64); ok {
			effect.PrimaryEffect.Value = damageFloat * baseMultiplier
		}

		effects[i] = effect
	}

	return effects
}

// getMetricValue gets a metric value from activation data
func (s *Service) getMetricValue(metric string, activationData *SynergyActivationData) float64 {
	switch metric {
	case "total_damage":
		return activationData.TotalDamage
	case "execution_speed":
		return activationData.ExecutionSpeed
	case "combo_length":
		return float64(len(activationData.AbilitySequence))
	case "synergy_level":
		return activationData.SynergyLevel
	default:
		return 0.0
	}
}

// SUPPORTING TYPES

type ComboExecutionData struct {
	AbilitySequence []AbilityExecution `json:"ability_sequence"`
	TotalTime       int                `json:"total_time"`
	PositionData    *PositionData      `json:"position_data"`
	StateData       *StateData         `json:"state_data"`
}

type AbilityExecution struct {
	AbilityID     uuid.UUID     `json:"ability_id"`
	ExecutionTime int           `json:"execution_time"`
	PositionData  *PositionData `json:"position_data"`
}

type PositionData struct {
	DistanceToTarget float64 `json:"distance_to_target"`
	AngleToTarget    float64 `json:"angle_to_target"`
	MovementSpeed    float64 `json:"movement_speed"`
	TerrainType      string  `json:"terrain_type"`
}

type StateData struct {
	HealthPercent  float64  `json:"health_percent"`
	EnergyPercent  float64  `json:"energy_percent"`
	StatusEffects  []string `json:"status_effects"`
	WeaponType     string   `json:"weapon_type"`
}

type ComboResult struct {
	Success         bool                  `json:"success"`
	ComboID         uuid.UUID             `json:"combo_id"`
	ExecutionTime   int                   `json:"execution_time"`
	BonusEffects    []models.ComboEffect  `json:"bonus_effects"`
	MasteryProgress float64               `json:"mastery_progress"`
	Violations      []string              `json:"violations,omitempty"`
}

type ComboValidation struct {
	IsValid    bool     `json:"is_valid"`
	Violations []string `json:"violations"`
}

type PatternAnalysis struct {
	IsValidPattern    bool                `json:"is_valid_pattern"`
	Steps             []models.ComboStep   `json:"steps"`
	AverageTime       int                  `json:"average_time"`
	BestTime          int                  `json:"best_time"`
	OptimizationScore float64              `json:"optimization_score"`
	NeuralPattern     string               `json:"neural_pattern"`
}

type SynergyActivationData struct {
	AbilitySequence []uuid.UUID    `json:"ability_sequence"`
	ExecutionTimes  []int          `json:"execution_times"`
	PositionData    *PositionData  `json:"position_data"`
	StateData       *StateData     `json:"state_data"`
	TotalDamage     float64        `json:"total_damage"`
	ExecutionSpeed  float64        `json:"execution_speed"`
	SynergyLevel    float64        `json:"synergy_level"`
}

type SynergyResult struct {
	Success        bool                            `json:"success"`
	SynergyID      uuid.UUID                       `json:"synergy_id"`
	Effects        []models.AdvancedSynergyEffect  `json:"effects"`
	Duration       int                             `json:"duration"`
	Cooldown       int                             `json:"cooldown"`
	EnergyConsumed int                             `json:"energy_consumed"`
	Violations     []string                        `json:"violations,omitempty"`
}

type SynergyValidation struct {
	IsValid    bool     `json:"is_valid"`
	Violations []string `json:"violations"`
}

func generateRandomSuffix() string {
	bytes := make([]byte, 4)
	rand.Read(bytes)
	return hex.EncodeToString(bytes)[:8]
}
