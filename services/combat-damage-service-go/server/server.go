package server

import (
	"context"
	"fmt"
	"math"
	"net/http"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/jwtauth/v5"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// Server implements the api.Handler
type Server struct {
	logger         *zap.Logger
	db             *pgxpool.Pool
	tokenAuth      *jwtauth.JWTAuth
	damagePool     *sync.Pool // Memory pool for damage calculations
	effectsPool    *sync.Pool // Memory pool for effects operations
	validationPool *sync.Pool // Memory pool for validation operations
}

// NewServer creates a new server instance
func NewServer(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Server {
	return &Server{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
		damagePool: &sync.Pool{
			New: func() interface{} {
				return &api.DamageResult{}
			},
		},
		effectsPool: &sync.Pool{
			New: func() interface{} {
				return &api.EffectsResult{}
			},
		},
		validationPool: &sync.Pool{
			New: func() interface{} {
				return &api.DamageValidationResult{}
			},
		},
	}
}

// CalculateDamage implements api.Handler
func (s *Server) CalculateDamage(ctx context.Context, req *api.DamageRequest) (api.CalculateDamageRes, error) {
	// Set timeout for damage calculation (hot path - 50ms max)
	timeoutCtx, cancel := context.WithTimeout(ctx, 50*time.Millisecond)
	defer cancel()

	startTime := time.Now()

	// Validate request
	if req == nil {
		return &api.CalculateDamageBadRequest{
			Code:    "INVALID_REQUEST",
			Message: "Damage request cannot be nil",
		}, nil
	}

	// Get result from memory pool
	result := s.damagePool.Get().(*api.DamageResult)
	defer s.damagePool.Put(result)

	// Reset the result
	*result = api.DamageResult{}

	// Calculate damage with all modifiers
	calculatedResult, err := s.calculateDamageWithModifiers(timeoutCtx, req)
	if err != nil {
		return &api.CalculateDamageBadRequest{
			Code:    "CALCULATION_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Copy calculated result to pooled result
	*result = *calculatedResult

	// Add performance metrics
	processingTime := time.Since(startTime).Nanoseconds()
	result.ProcessingTimeNs.SetTo(processingTime)

	return result, nil
}

// ValidateDamage implements api.Handler
func (s *Server) ValidateDamage(ctx context.Context, req *api.DamageValidationRequest) (api.ValidateDamageRes, error) {
	// Set timeout for validation (100ms max for security checks)
	timeoutCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	startTime := time.Now()

	// Validate request
	if req == nil {
		return &api.Error{
			Code:    "INVALID_REQUEST",
			Message: "Validation request cannot be nil",
		}, nil
	}

	// Get result from memory pool
	result := s.validationPool.Get().(*api.DamageValidationResult)
	defer s.validationPool.Put(result)

	// Reset the result
	*result = api.DamageValidationResult{}

	// Perform validation
	validationResult, err := s.performDamageValidation(timeoutCtx, req)
	if err != nil {
		return &api.Error{
			Code:    "VALIDATION_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Copy validation result to pooled result
	*result = *validationResult

	// Add performance metrics
	processingTime := time.Since(startTime).Nanoseconds()
	result.ProcessingTimeNs.SetTo(processingTime)

	return result, nil
}

// ApplyEffects implements api.Handler
func (s *Server) ApplyEffects(ctx context.Context, req *api.EffectsRequest) (api.ApplyEffectsRes, error) {
	// Set timeout for effects application (200ms max for complex effect chains)
	timeoutCtx, cancel := context.WithTimeout(ctx, 200*time.Millisecond)
	defer cancel()

	startTime := time.Now()

	// Validate request
	if req == nil {
		return &api.Error{
			Code:    "INVALID_REQUEST",
			Message: "Effects request cannot be nil",
		}, nil
	}

	// Get result from memory pool
	result := s.effectsPool.Get().(*api.EffectsResult)
	defer s.effectsPool.Put(result)

	// Reset the result
	*result = api.EffectsResult{}

	// Apply effects
	effectsResult, err := s.applyCombatEffects(timeoutCtx, req)
	if err != nil {
		return &api.Error{
			Code:    "EFFECTS_APPLICATION_ERROR",
			Message: err.Error(),
		}, nil
	}

	// Copy effects result to pooled result
	*result = *effectsResult

	// Add performance metrics
	processingTime := time.Since(startTime).Nanoseconds()
	result.ProcessingTimeNs.SetTo(processingTime)

	return result, nil
}

// GetActiveEffects implements api.Handler
func (s *Server) GetActiveEffects(ctx context.Context, params api.GetActiveEffectsParams) (api.GetActiveEffectsRes, error) {
	// Set timeout for effects retrieval (150ms max for database queries)
	timeoutCtx, cancel := context.WithTimeout(ctx, 150*time.Millisecond)
	defer cancel()

	startTime := time.Now()

	// Get active effects for participant
	result, err := s.getActiveEffects(timeoutCtx, params.ParticipantID)
	if err != nil {
		return &api.Error{
			Code:    "PARTICIPANT_NOT_FOUND",
			Message: fmt.Sprintf("Participant %s not found or has no active effects", params.ParticipantID.String()),
		}, nil
	}

	// Add processing time (not included in response schema, but for consistency)
	_ = time.Since(startTime)

	return result, nil
}

// RemoveEffect implements api.Handler
func (s *Server) RemoveEffect(ctx context.Context, params api.RemoveEffectParams) (api.RemoveEffectRes, error) {
	// Set timeout for effect removal (100ms max for database operation)
	timeoutCtx, cancel := context.WithTimeout(ctx, 100*time.Millisecond)
	defer cancel()

	startTime := time.Now()

	// Attempt to remove the effect
	removed, err := s.removeCombatEffect(timeoutCtx, params.EffectID)
	if err != nil {
		return &api.Error{
			Code:    "EFFECT_NOT_FOUND",
			Message: fmt.Sprintf("Effect %s not found or already expired", params.EffectID.String()),
		}, nil
	}

	if !removed {
		return &api.Error{
			Code:    "EFFECT_NOT_FOUND",
			Message: fmt.Sprintf("Effect %s not found", params.EffectID.String()),
		}, nil
	}

	// Add processing time (not included in response schema, but for consistency)
	_ = time.Since(startTime)

	return &api.RemoveEffectNoContent{}, nil
}

// HealthCheck implements api.Handler
func (s *Server) HealthCheck(ctx context.Context) (api.HealthCheckRes, error) {
	// Check service status - always healthy for now
	status := api.HealthResponseStatusHealthy

	version := "1.0.0" // In real implementation, get from build info

	return &api.HealthResponse{
		Version:   api.OptString{Value: version, Set: true},
		Status:    status,
		Timestamp: time.Now(),
	}, nil
}

// CreateRouter creates Chi router with ogen handlers
func (s *Server) CreateRouter() *chi.Mux {
	r := chi.NewRouter()

	// Create ogen server
	server, err := api.NewServer(s, nil) // No security handler for now
	if err != nil {
		panic("Failed to create ogen server: " + err.Error())
	}

	// Mount ogen server
	r.Mount("/api/v1", http.HandlerFunc(server.ServeHTTP))

	return r
}

// calculateDamageWithModifiers calculates combat damage with all modifiers
func (s *Server) calculateDamageWithModifiers(ctx context.Context, req *api.DamageRequest) (*api.DamageResult, error) {
	calculationID := uuid.New()

	// Start with base damage
	damage := float64(req.BaseDamage)
	appliedModifiers := []api.DamageResultAppliedModifiersItem{}
	totalMultiplier := 1.0

	// Apply level scaling (higher level attackers do more damage)
	if req.AttackerLevel.IsSet() && req.TargetLevel.IsSet() {
		attackerLevel := float64(req.AttackerLevel.Value)
		targetLevel := float64(req.TargetLevel.Value)
		levelModifier := 1.0 + (attackerLevel-targetLevel)*0.02 // 2% per level difference
		damage *= levelModifier
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType("LEVEL_SCALING"),
			Value:  float32(levelModifier),
			Source: "level_difference",
		})
		totalMultiplier *= levelModifier
	}

	// Apply weapon modifiers
	for _, modifier := range req.WeaponModifiers {
		modValue := float64(modifier.Value)
		damage *= modValue
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType(modifier.Type),
			Value:  float32(modValue),
			Source: "weapon_modifier",
		})
		totalMultiplier *= modValue
	}

	// Apply implant synergies
	for _, synergy := range req.ImplantSynergies {
		synergyValue := float64(synergy.SynergyBonus)
		damage *= synergyValue
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType(synergy.ImplantType),
			Value:  float32(synergyValue),
			Source: "implant_synergy",
		})
		totalMultiplier *= synergyValue
	}

	// Apply environmental modifiers
	if req.EnvironmentalModifiers.IsSet() {
		envMods := req.EnvironmentalModifiers.Value
		if envMods.WeatherMultiplier.IsSet() {
			weatherMod := float64(envMods.WeatherMultiplier.Value)
			damage *= weatherMod
			appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
				Type:   api.DamageResultAppliedModifiersItemType("WEATHER"),
				Value:  float32(weatherMod),
				Source: "environmental",
			})
			totalMultiplier *= weatherMod
		}
		if envMods.TerrainCover.IsSet() {
			terrainMod := float64(envMods.TerrainCover.Value)
			damage *= terrainMod
			appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
				Type:   api.DamageResultAppliedModifiersItemType("TERRAIN"),
				Value:  float32(terrainMod),
				Source: "environmental",
			})
			totalMultiplier *= terrainMod
		}
	}

	// Apply critical hit
	isCritical := false
	criticalMultiplier := 1.0
	if req.IsCriticalHit.IsSet() && req.IsCriticalHit.Value {
		isCritical = true
		if req.CriticalMultiplier.IsSet() {
			criticalMultiplier = float64(req.CriticalMultiplier.Value)
		} else {
			criticalMultiplier = 2.0 // default critical multiplier
		}
		damage *= criticalMultiplier
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType("CRITICAL_HIT"),
			Value:  float32(criticalMultiplier),
			Source: "combat_mechanic",
		})
		totalMultiplier *= criticalMultiplier
	}

	// Apply weak spot bonus
	if req.IsWeakSpotHit.IsSet() && req.IsWeakSpotHit.Value {
		weakSpotMultiplier := 1.5 // 50% bonus for weak spots
		damage *= weakSpotMultiplier
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType("WEAK_SPOT"),
			Value:  float32(weakSpotMultiplier),
			Source: "combat_mechanic",
		})
		totalMultiplier *= weakSpotMultiplier
	}

	// Apply backstab bonus
	if req.IsBackstab.IsSet() && req.IsBackstab.Value {
		backstabMultiplier := 2.0 // 100% bonus for backstabs
		damage *= backstabMultiplier
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType("BACKSTAB"),
			Value:  float32(backstabMultiplier),
			Source: "combat_mechanic",
		})
		totalMultiplier *= backstabMultiplier
	}

	// Apply range modifier
	if req.RangeModifier.IsSet() {
		rangeMod := float64(req.RangeModifier.Value)
		damage *= rangeMod
		appliedModifiers = append(appliedModifiers, api.DamageResultAppliedModifiersItem{
			Type:   api.DamageResultAppliedModifiersItemType("RANGE"),
			Value:  float32(rangeMod),
			Source: "combat_mechanic",
		})
		totalMultiplier *= rangeMod
	}

	// Armor reduction (if not ignored)
	armorReduction := int32(0)
	if !(req.IgnoreArmor.IsSet() && req.IgnoreArmor.Value) {
		// Simulate armor calculation - in real implementation this would come from database
		armorValue := 50.0 // placeholder armor value
		armorReduction = int32(math.Min(damage*0.3, armorValue)) // 30% damage reduction, capped by armor
		damage -= float64(armorReduction)
	}

	// Ensure minimum damage
	if damage < 1 {
		damage = 1
	}

	finalDamage := int32(math.Round(damage))

	// Create damage breakdown
	damageBreakdown := []api.DamageResultDamageBreakdownItem{
		{
			Type:   api.DamageResultDamageBreakdownItemType("BASE"),
			Amount: req.BaseDamage,
		},
		{
			Type:   api.DamageResultDamageBreakdownItemType("MODIFIER"),
			Amount: int32(math.Round((totalMultiplier - 1.0) * 100)), // percentage
		},
		{
			Type:   api.DamageResultDamageBreakdownItemType("REDUCTION"),
			Amount: armorReduction,
		},
	}

	// Determine if target survives (simplified)
	targetAlive := finalDamage < 100 // placeholder logic

	timestamp := time.Now().UnixMilli()

	result := &api.DamageResult{
		AttackerID:       req.AttackerID,
		TargetID:         req.TargetID,
		CalculationID:    calculationID,
		AppliedModifiers: appliedModifiers,
		DamageBreakdown:  damageBreakdown,
		FinalDamage:      finalDamage,
		DamageType:       api.DamageResultDamageType(req.DamageType),
	}

	// Set optional fields
	result.BaseDamage.SetTo(req.BaseDamage)
	result.ArmorReduction.SetTo(armorReduction)
	result.Timestamp.SetTo(timestamp)

	if isCritical {
		critMult := float32(criticalMultiplier)
		result.CriticalMultiplier.SetTo(critMult)
	}

	totalMult := float32(totalMultiplier)
	result.TotalMultiplier.SetTo(totalMult)
	result.WasCritical.SetTo(isCritical)
	result.WasDodged.SetTo(false) // placeholder
	result.WasBlocked.SetTo(false) // placeholder
	result.TargetAlive.SetTo(targetAlive)

	return result, nil
}

// performDamageValidation validates client-reported damage against server calculation
func (s *Server) performDamageValidation(ctx context.Context, req *api.DamageValidationRequest) (*api.DamageValidationResult, error) {
	validationIssues := []api.DamageValidationResultValidationIssuesItem{}
	validationScore := int32(100) // Start with perfect score
	isValid := true

	// Create a DamageRequest from validation request for recalculation
	damageReq := &api.DamageRequest{
		AttackerID: req.AttackerID,
		TargetID:   req.TargetID,
		BaseDamage: 100, // Placeholder - in real implementation would come from weapon data
		DamageType: api.DamageRequestDamageType(req.DamageType),
	}

	// Convert client modifiers to weapon modifiers
	for _, clientMod := range req.ClientModifiers {
		damageReq.WeaponModifiers = append(damageReq.WeaponModifiers, api.DamageRequestWeaponModifiersItem{
			Type:  api.DamageRequestWeaponModifiersItemType(clientMod.Type),
			Value: clientMod.Value,
		})
	}

	// Add critical hit if reported
	if req.WasCritical.IsSet() && req.WasCritical.Value {
		damageReq.IsCriticalHit.SetTo(true)
		damageReq.CriticalMultiplier.SetTo(2.0)
	}

	// Add headshot bonus if reported
	if req.WasHeadshot.IsSet() && req.WasHeadshot.Value {
		damageReq.IsWeakSpotHit.SetTo(true)
	}

	// Calculate server damage
	serverResult, err := s.calculateDamageWithModifiers(ctx, damageReq)
	if err != nil {
		return nil, fmt.Errorf("failed to calculate server damage: %w", err)
	}

	serverDamage := serverResult.FinalDamage
	reportedDamage := req.ReportedDamage

	// Validate damage amount
	damageDiff := math.Abs(float64(serverDamage - reportedDamage))
	damageDiffPercent := (damageDiff / float64(serverDamage)) * 100

	if damageDiffPercent > 10 { // More than 10% difference
		isValid = false
		validationScore -= 30
		validationIssues = append(validationIssues, api.DamageValidationResultValidationIssuesItem{
			IssueType:   api.DamageValidationResultValidationIssuesItemIssueType("DAMAGE_MISMATCH"),
			Severity:    api.DamageValidationResultValidationIssuesItemSeverity("HIGH"),
			Description: fmt.Sprintf("Damage mismatch: reported %d, calculated %d (%.1f%% difference)", reportedDamage, serverDamage, damageDiffPercent),
			EvidenceValue: api.OptFloat32{Value: float32(damageDiffPercent), Set: true},
		})
	}

	// Validate weapon hash (placeholder logic)
	if req.WeaponHash.IsSet() {
		// In real implementation, validate against known weapon hashes
		expectedHash := "valid_weapon_hash" // placeholder
		if req.WeaponHash.Value != expectedHash {
			isValid = false
			validationScore -= 20
			validationIssues = append(validationIssues, api.DamageValidationResultValidationIssuesItem{
				IssueType:   api.DamageValidationResultValidationIssuesItemIssueType("INVALID_WEAPON_HASH"),
				Severity:    api.DamageValidationResultValidationIssuesItemSeverity("MEDIUM"),
				Description: "Weapon hash validation failed",
			})
		}
	}

	// Validate position data (line-of-sight check)
	if req.PositionData.IsSet() {
		posData := req.PositionData.Value
		// Simplified line-of-sight validation
		distance := calculateDistance(posData.AttackerPos, posData.TargetPos)
		if distance > 100.0 { // Too far for valid shot
			isValid = false
			validationScore -= 25
			validationIssues = append(validationIssues, api.DamageValidationResultValidationIssuesItem{
				IssueType:   api.DamageValidationResultValidationIssuesItemIssueType("INVALID_DISTANCE"),
				Severity:    api.DamageValidationResultValidationIssuesItemSeverity("HIGH"),
				Description: fmt.Sprintf("Invalid shot distance: %.1f units", distance),
				EvidenceValue: api.OptFloat32{Value: float32(distance), Set: true},
			})
		}
	}

	// Check for suspicious patterns
	if len(req.ClientModifiers) > 10 { // Too many modifiers
		isValid = false
		validationScore -= 15
		validationIssues = append(validationIssues, api.DamageValidationResultValidationIssuesItem{
			IssueType:   api.DamageValidationResultValidationIssuesItemIssueType("TOO_MANY_MODIFIERS"),
			Severity:    api.DamageValidationResultValidationIssuesItemSeverity("MEDIUM"),
			Description: fmt.Sprintf("Too many client modifiers: %d", len(req.ClientModifiers)),
			EvidenceValue: api.OptFloat32{Value: float32(len(req.ClientModifiers)), Set: true},
		})
	}

	// Ensure score doesn't go below 0
	if validationScore < 0 {
		validationScore = 0
	}

	// Determine overall status
	var status api.DamageValidationResultValidationStatus
	if validationScore >= 90 {
		status = "VALID"
	} else if validationScore >= 70 {
		status = "WARNING"
	} else {
		status = "INVALID"
	}

	timestamp := time.Now().UnixMilli()

	result := &api.DamageValidationResult{
		SessionID:              req.SessionID,
		ValidationIssues:       validationIssues,
		ServerCalculatedDamage: serverDamage,
		ValidationScore:        validationScore,
		IsValid:                isValid,
	}

	// Set optional fields
	result.Timestamp.SetTo(timestamp)
	result.ValidationStatus.SetTo(status)
	result.RequiresServerCorrection.SetTo(!isValid)
	result.FlaggedForReview.SetTo(validationScore < 70)

	// Add corrected values if validation failed
	if !isValid {
		correctedValues := &api.DamageValidationResultCorrectedValues{
			CorrectedDamage: api.OptInt32{Value: serverDamage, Set: true},
			CorrectedModifiers: []api.DamageValidationResultCorrectedValuesCorrectedModifiersItem{
				{
					Type:        "SERVER_CALCULATED",
					CorrectValue: float32(serverDamage),
				},
			},
		}
		result.CorrectedValues.SetTo(*correctedValues)
	}

	return result, nil
}

// applyCombatEffects applies combat effects to a participant
func (s *Server) applyCombatEffects(ctx context.Context, req *api.EffectsRequest) (*api.EffectsResult, error) {
	appliedEffects := []api.EffectsResultAppliedEffectsItem{}
	rejectedEffects := []api.EffectsResultRejectedEffectsItem{}
	success := true

	// In a real implementation, this would check existing effects from database/cache
	// For now, we'll use a simple in-memory approach
	existingEffects := make(map[string]api.EffectsRequestEffectsItem)

	// Check stacking and override settings
	allowStacking := true
	if req.StackEffects.IsSet() {
		allowStacking = req.StackEffects.Value
	}

	allowOverride := false
	if req.OverrideExisting.IsSet() {
		allowOverride = req.OverrideExisting.Value
	}

	// Process each effect
	for _, effect := range req.Effects {
		effectID := uuid.New()
		effectType := string(effect.EffectType)

		// Check for conflicts with existing effects
		if _, exists := existingEffects[effectType]; exists {
			if !allowOverride && !allowStacking {
				// Reject effect due to conflict
				rejectedEffects = append(rejectedEffects, api.EffectsResultRejectedEffectsItem{
					EffectType: effectType,
					Reason:     api.EffectsResultRejectedEffectsItemReason("CONFLICT"),
				})
				continue
			}
			if allowOverride {
				// Override existing effect (simplified - just mark as existing)
				existingEffects[effectType] = effect // Store the effect itself for override logic
			}
		} else {
			// Add new effect
			existingEffects[effectType] = effect
		}

		// Apply effect (in real implementation, this would persist to database)
		appliedEffects = append(appliedEffects, api.EffectsResultAppliedEffectsItem{
			EffectID:   effectID,
			EffectType: effectType,
			DurationMs: effect.DurationMs,
			Stacks:     api.OptInt32{Value: 1, Set: true}, // Simplified - no stacking logic yet
		})
	}

	// Determine overall status
	var status api.EffectsResultStatus
	totalEffects := len(appliedEffects) + len(rejectedEffects)
	if len(rejectedEffects) == 0 {
		status = "SUCCESS"
	} else if len(appliedEffects) > 0 {
		status = "PARTIAL"
		success = false
	} else {
		status = "FAILED"
		success = false
	}

	// Create summary
	summary := &api.EffectsResultSummary{
		TotalRequested:      api.OptInt32{Value: int32(totalEffects), Set: true},
		SuccessfullyApplied: api.OptInt32{Value: int32(len(appliedEffects)), Set: true},
		RejectedCount:       api.OptInt32{Value: int32(len(rejectedEffects)), Set: true},
	}

	timestamp := time.Now().UnixMilli()

	result := &api.EffectsResult{
		ParticipantID:    req.ParticipantID,
		AppliedEffects:   appliedEffects,
		RejectedEffects:  rejectedEffects,
		Timestamp:        api.OptInt64{Value: timestamp, Set: true},
		Status:           api.OptEffectsResultStatus{Value: status, Set: true},
		Summary:          api.OptEffectsResultSummary{Value: *summary, Set: true},
		Success:          success,
	}

	return result, nil
}

// getActiveEffects retrieves all active effects for a combat participant
func (s *Server) getActiveEffects(ctx context.Context, participantID uuid.UUID) (*api.ActiveEffects, error) {
	// In a real implementation, this would query the database for active effects
	// For now, we'll return mock data based on participant ID hash for consistency

	// Generate some mock effects based on participant ID hash
	hash := int(participantID[0]) + int(participantID[1]) + int(participantID[2])
	effectCount := hash % 5 // 0-4 effects

	effects := []api.ActiveEffectsEffectsItem{}
	hasCriticalEffects := false

	// Generate mock effects
	for i := 0; i < effectCount; i++ {
		var effectType string
		var duration int32

		switch i % 4 {
		case 0:
			effectType = "DAMAGE_BOOST"
			duration = 30000 // 30 seconds
		case 1:
			effectType = "SPEED_INCREASE"
			duration = 15000 // 15 seconds
		case 2:
			effectType = "ARMOR_REDUCTION"
			duration = 20000 // 20 seconds
			hasCriticalEffects = true // debuffs are critical
		case 3:
			effectType = "REGENERATION"
			duration = 45000 // 45 seconds
		}

		effects = append(effects, api.ActiveEffectsEffectsItem{
			EffectID:    uuid.New(),
			EffectType:  api.ActiveEffectsEffectsItemEffectType(effectType),
			RemainingMs: duration - int32(hash%int(duration)), // mock remaining time
			SourceID:    uuid.New(), // mock source ID
			Stacks:      api.OptInt32{Value: int32((hash+i)%3 + 1), Set: true}, // 1-3 stacks
			AppliedAt:   api.OptInt64{Value: time.Now().UnixMilli(), Set: true},
		})
	}

	// Create summary
	buffCount := 0
	debuffCount := 0
	statusCount := 0

	for _, effect := range effects {
		switch effect.EffectType {
		case "DAMAGE_BOOST", "SPEED_INCREASE", "REGENERATION":
			buffCount++
		case "ARMOR_REDUCTION":
			debuffCount++
		default:
			statusCount++
		}
	}

	summary := &api.ActiveEffectsSummary{
		Buffs:         api.OptInt32{Value: int32(buffCount), Set: true},
		Debuffs:       api.OptInt32{Value: int32(debuffCount), Set: true},
		StatusEffects: api.OptInt32{Value: int32(statusCount), Set: true},
	}

	return &api.ActiveEffects{
		ParticipantID:      participantID,
		Effects:           effects,
		Timestamp:         time.Now().UnixMilli(),
		TotalEffects:      api.OptInt32{Value: int32(len(effects)), Set: true},
		Summary:           api.OptActiveEffectsSummary{Value: *summary, Set: true},
		HasCriticalEffects: api.OptBool{Value: hasCriticalEffects, Set: true},
	}, nil
}

// removeCombatEffect removes a specific combat effect by ID
func (s *Server) removeCombatEffect(ctx context.Context, effectID uuid.UUID) (bool, error) {
	// In a real implementation, this would:
	// 1. Query the database to find the effect
	// 2. Check if the effect exists and belongs to the requesting participant
	// 3. Remove the effect from the database
	// 4. Update any dependent systems (caches, etc.)

	// For this implementation, we'll simulate effect removal
	// In practice, this would be a database operation
	return true, nil // Assume effect was found and removed
}

// calculateDistance calculates 3D distance between two points
func calculateDistance(attackerPos api.DamageValidationRequestPositionDataAttackerPos, targetPos api.DamageValidationRequestPositionDataTargetPos) float64 {
	dx := float64(attackerPos.X - targetPos.X)
	dy := float64(attackerPos.Y - targetPos.Y)
	dz := float64(attackerPos.Z - targetPos.Z)
	return math.Sqrt(dx*dx + dy*dy + dz*dz)
}

// Issue: #2251