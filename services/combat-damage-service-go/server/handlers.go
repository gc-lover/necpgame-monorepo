package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"sync"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/go-chi/jwtauth/v5"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"github.com/gc-lover/necpgame-monorepo/services/combat-damage-service-go/pkg/api"
)

// Handlers holds all HTTP handlers for the combat damage service
type Handlers struct {
	db        *pgxpool.Pool
	logger    *zap.Logger
	tokenAuth *jwtauth.JWTAuth

	// Performance optimization: sync.Pool for damage calculation objects
	damagePool sync.Pool
	effectPool sync.Pool
}

// NewHandlers creates a new handlers instance with optimized pools
func NewHandlers(db *pgxpool.Pool, logger *zap.Logger, tokenAuth *jwtauth.JWTAuth) *Handlers {
	h := &Handlers{
		db:        db,
		logger:    logger,
		tokenAuth: tokenAuth,
	}

	// Initialize object pools for zero allocations in hot paths
	h.damagePool.New = func() interface{} {
		return &api.DamageCalculationRequest{}
	}
	h.effectPool.New = func() interface{} {
		return &api.ApplyEffectsRequest{}
	}

	return h
}

// HealthCheck returns service health status
func (h *Handlers) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response := api.HealthResponse{
		Status:    "healthy",
		Version:   "1.0.0",
		Timestamp: time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

// ReadinessCheck returns service readiness status
func (h *Handlers) ReadinessCheck(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	// Check database connectivity
	if err := h.db.Ping(ctx); err != nil {
		h.logger.Error("Database readiness check failed", zap.Error(err))
		http.Error(w, "Service not ready: database unavailable", http.StatusServiceUnavailable)
		return
	}

	response := api.HealthResponse{
		Status:    "ready",
		Version:   "1.0.0",
		Timestamp: time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

// Metrics returns service metrics
func (h *Handlers) Metrics(w http.ResponseWriter, r *http.Request) {
	// TODO: Implement Prometheus metrics
	response := map[string]interface{}{
		"service": "combat-damage-service-go",
		"version": "1.0.0",
		"status":  "operational",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// CalculateDamage handles damage calculation requests
func (h *Handlers) CalculateDamage(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	// Get object from pool (zero allocation)
	req := h.damagePool.Get().(*api.DamageCalculationRequest)
	defer h.damagePool.Put(req)

	// Parse request
	if err := json.NewDecoder(r.Body).Decode(req); err != nil {
		h.logger.Error("Failed to decode damage calculation request", zap.Error(err))
		h.sendError(w, "Invalid request format", http.StatusBadRequest)
		return
	}

	// Validate request
	if err := h.validateDamageRequest(req); err != nil {
		h.logger.Warn("Invalid damage calculation request", zap.Error(err))
		h.sendError(w, err.Error(), http.StatusBadRequest)
		return
	}

	// Calculate damage (core business logic)
	result, err := h.calculateDamage(ctx, req)
	if err != nil {
		h.logger.Error("Damage calculation failed",
			zap.Error(err),
			zap.String("attacker_id", req.AttackerId.String()),
			zap.String("target_id", req.TargetId.String()))
		h.sendError(w, "Damage calculation failed", http.StatusInternalServerError)
		return
	}

	// Log performance metrics
	duration := time.Since(start)
	h.logger.Info("Damage calculation completed",
		zap.Duration("duration", duration),
		zap.Float64("damage", result.TotalDamage),
		zap.Bool("critical_hit", result.IsCriticalHit))

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(result)
}

// ValidateDamage handles damage validation for anti-cheat
func (h *Handlers) ValidateDamage(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	req := &api.DamageValidationRequest{}
	if err := json.NewDecoder(r.Body).Decode(req); err != nil {
		h.logger.Error("Failed to decode damage validation request", zap.Error(err))
		h.sendError(w, "Invalid request format", http.StatusBadRequest)
		return
	}

	// Validate damage calculation
	result, err := h.validateDamageCalculation(ctx, req)
	if err != nil {
		h.logger.Error("Damage validation failed", zap.Error(err))
		h.sendError(w, "Validation failed", http.StatusInternalServerError)
		return
	}

	duration := time.Since(start)
	h.logger.Info("Damage validation completed",
		zap.Duration("duration", duration),
		zap.Bool("is_valid", result.IsValid))

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// ApplyEffects handles combat effects application
func (h *Handlers) ApplyEffects(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	start := time.Now()

	// Get object from pool (zero allocation)
	req := h.effectPool.Get().(*api.ApplyEffectsRequest)
	defer h.effectPool.Put(req)

	if err := json.NewDecoder(r.Body).Decode(req); err != nil {
		h.logger.Error("Failed to decode apply effects request", zap.Error(err))
		h.sendError(w, "Invalid request format", http.StatusBadRequest)
		return
	}

	result, err := h.applyCombatEffects(ctx, req)
	if err != nil {
		h.logger.Error("Apply effects failed", zap.Error(err))
		h.sendError(w, "Effects application failed", http.StatusInternalServerError)
		return
	}

	duration := time.Since(start)
	h.logger.Info("Effects applied",
		zap.Duration("duration", duration),
		zap.Int("effects_count", len(result.AppliedEffects)))

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

// GetActiveEffects returns active effects for a participant
func (h *Handlers) GetActiveEffects(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	participantID := chi.URLParam(r, "participant_id")

	if participantID == "" {
		h.sendError(w, "Participant ID is required", http.StatusBadRequest)
		return
	}

	participantUUID, err := uuid.Parse(participantID)
	if err != nil {
		h.sendError(w, "Invalid participant ID format", http.StatusBadRequest)
		return
	}

	effects, err := h.getActiveEffects(ctx, participantUUID)
	if err != nil {
		h.logger.Error("Failed to get active effects",
			zap.Error(err),
			zap.String("participant_id", participantID))
		h.sendError(w, "Failed to retrieve effects", http.StatusInternalServerError)
		return
	}

	response := api.ActiveEffectsResponse{
		ParticipantId: participantUUID,
		Effects:       effects,
		Timestamp:     time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// RemoveEffect removes a specific combat effect
func (h *Handlers) RemoveEffect(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	effectID := chi.URLParam(r, "effect_id")

	if effectID == "" {
		h.sendError(w, "Effect ID is required", http.StatusBadRequest)
		return
	}

	effectUUID, err := uuid.Parse(effectID)
	if err != nil {
		h.sendError(w, "Invalid effect ID format", http.StatusBadRequest)
		return
	}

	err = h.removeCombatEffect(ctx, effectUUID)
	if err != nil {
		h.logger.Error("Failed to remove effect",
			zap.Error(err),
			zap.String("effect_id", effectID))
		h.sendError(w, "Failed to remove effect", http.StatusInternalServerError)
		return
	}

	h.logger.Info("Effect removed", zap.String("effect_id", effectID))
	w.WriteHeader(http.StatusNoContent)
}

// sendError sends a standardized error response
func (h *Handlers) sendError(w http.ResponseWriter, message string, statusCode int) {
	errorResponse := api.Error{
		Code:    strconv.Itoa(statusCode),
		Message: message,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	json.NewEncoder(w).Encode(errorResponse)
}

// validateDamageRequest validates damage calculation request
func (h *Handlers) validateDamageRequest(req *api.DamageCalculationRequest) error {
	if req.AttackerId == uuid.Nil {
		return fmt.Errorf("attacker_id is required")
	}
	if req.TargetId == uuid.Nil {
		return fmt.Errorf("target_id is required")
	}
	if req.BaseDamage <= 0 {
		return fmt.Errorf("base_damage must be positive")
	}
	if req.WeaponType == "" {
		return fmt.Errorf("weapon_type is required")
	}
	return nil
}

// calculateDamage implements the core damage calculation logic
func (h *Handlers) calculateDamage(ctx context.Context, req *api.DamageCalculationRequest) (*api.DamageCalculationResult, error) {
	// Base damage calculation
	totalDamage := req.BaseDamage

	// Apply weapon modifiers
	totalDamage *= h.getWeaponMultiplier(req.WeaponType)

	// Apply critical hit
	isCritical := h.calculateCriticalHit(req.CriticalChance)
	if isCritical {
		totalDamage *= req.CriticalMultiplier
	}

	// Apply armor reduction
	armorReduction := h.calculateArmorReduction(req.ArmorRating, req.Penetration)
	totalDamage *= (1.0 - armorReduction)

	// Apply environmental modifiers
	totalDamage *= h.getEnvironmentalModifier(req.EnvironmentType)

	// Apply implant synergies
	totalDamage *= h.calculateImplantSynergy(req.ImplantEffects)

	// Ensure minimum damage
	if totalDamage < 1.0 {
		totalDamage = 1.0
	}

	return &api.DamageCalculationResult{
		AttackerId:      req.AttackerId,
		TargetId:        req.TargetId,
		TotalDamage:     totalDamage,
		IsCriticalHit:   isCritical,
		ArmorReduction:  armorReduction,
		WeaponBonus:     h.getWeaponMultiplier(req.WeaponType),
		CalculatedAt:    time.Now(),
	}, nil
}

// validateDamageCalculation validates damage for anti-cheat
func (h *Handlers) validateDamageCalculation(ctx context.Context, req *api.DamageValidationRequest) (*api.DamageValidationResult, error) {
	// Recalculate expected damage
	expectedReq := &api.DamageCalculationRequest{
		AttackerId:       req.AttackerId,
		TargetId:         req.TargetId,
		BaseDamage:       req.BaseDamage,
		WeaponType:       req.WeaponType,
		CriticalChance:   req.CriticalChance,
		CriticalMultiplier: req.CriticalMultiplier,
		ArmorRating:      req.ArmorRating,
		Penetration:      req.Penetration,
		EnvironmentType:  req.EnvironmentType,
		ImplantEffects:   req.ImplantEffects,
	}

	expectedResult, err := h.calculateDamage(ctx, expectedReq)
	if err != nil {
		return nil, err
	}

	// Compare with reported damage
	tolerance := 0.01 // 1% tolerance for floating point
	isValid := abs(expectedResult.TotalDamage-req.ReportedDamage) <= tolerance

	return &api.DamageValidationResult{
		AttackerId:        req.AttackerId,
		TargetId:          req.TargetId,
		ReportedDamage:    req.ReportedDamage,
		ExpectedDamage:    expectedResult.TotalDamage,
		IsValid:           isValid,
		ValidationScore:   h.calculateValidationScore(expectedResult.TotalDamage, req.ReportedDamage),
		ValidatedAt:       time.Now(),
	}, nil
}

// applyCombatEffects applies combat effects to participants
func (h *Handlers) applyCombatEffects(ctx context.Context, req *api.ApplyEffectsRequest) (*api.ApplyEffectsResult, error) {
	appliedEffects := make([]api.CombatEffect, 0, len(req.Effects))

	for _, effect := range req.Effects {
		// Validate effect
		if err := h.validateEffect(effect); err != nil {
			h.logger.Warn("Invalid effect", zap.Error(err), zap.String("effect_type", effect.Type))
			continue
		}

		// Apply effect
		effect.Id = uuid.New()
		effect.AppliedAt = time.Now()
		effect.ExpiresAt = effect.AppliedAt.Add(time.Duration(effect.DurationMs) * time.Millisecond)

		appliedEffects = append(appliedEffects, effect)

		// Store in database
		if err := h.storeEffect(ctx, effect); err != nil {
			h.logger.Error("Failed to store effect", zap.Error(err), zap.String("effect_id", effect.Id.String()))
			continue
		}
	}

	return &api.ApplyEffectsResult{
		ParticipantId:  req.ParticipantId,
		AppliedEffects: appliedEffects,
		AppliedAt:      time.Now(),
	}, nil
}

// getActiveEffects retrieves active effects for a participant
func (h *Handlers) getActiveEffects(ctx context.Context, participantID uuid.UUID) ([]api.CombatEffect, error) {
	// Query database for active effects
	rows, err := h.db.Query(ctx, `
		SELECT id, type, value, duration_ms, applied_at, expires_at
		FROM combat_effects
		WHERE participant_id = $1 AND expires_at > NOW()
		ORDER BY applied_at DESC
	`, participantID)

	if err != nil {
		return nil, fmt.Errorf("failed to query active effects: %w", err)
	}
	defer rows.Close()

	var effects []api.CombatEffect
	for rows.Next() {
		var effect api.CombatEffect
		err := rows.Scan(&effect.Id, &effect.Type, &effect.Value, &effect.DurationMs, &effect.AppliedAt, &effect.ExpiresAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan effect: %w", err)
		}
		effects = append(effects, effect)
	}

	return effects, rows.Err()
}

// removeCombatEffect removes a specific effect
func (h *Handlers) removeCombatEffect(ctx context.Context, effectID uuid.UUID) error {
	_, err := h.db.Exec(ctx, `
		DELETE FROM combat_effects WHERE id = $1
	`, effectID)
	return err
}

// Helper methods for damage calculations

func (h *Handlers) getWeaponMultiplier(weaponType string) float64 {
	switch weaponType {
	case "pistol":
		return 1.0
	case "rifle":
		return 1.2
	case "shotgun":
		return 1.5
	case "sniper":
		return 2.0
	case "melee":
		return 0.8
	default:
		return 1.0
	}
}

func (h *Handlers) calculateCriticalHit(chance float64) bool {
	// Simple random check - in production would use crypto/rand
	return chance > 0.5 // Placeholder
}

func (h *Handlers) calculateArmorReduction(armorRating, penetration float64) float64 {
	reduction := armorRating * 0.01 // Convert to percentage
	reduction *= (1.0 - penetration) // Apply penetration
	if reduction > 0.9 { // Cap at 90%
		reduction = 0.9
	}
	return reduction
}

func (h *Handlers) getEnvironmentalModifier(envType string) float64 {
	switch envType {
	case "rain":
		return 0.9
	case "fog":
		return 0.8
	case "night":
		return 1.1
	default:
		return 1.0
	}
}

func (h *Handlers) calculateImplantSynergy(effects []string) float64 {
	bonus := 1.0
	for _, effect := range effects {
		switch effect {
		case "damage_boost":
			bonus += 0.1
		case "critical_boost":
			bonus += 0.05
		}
	}
	return bonus
}

func (h *Handlers) calculateValidationScore(expected, reported float64) float64 {
	if expected == 0 {
		return 0
	}
	diff := abs(expected - reported)
	return 1.0 - (diff / expected)
}

func (h *Handlers) validateEffect(effect api.CombatEffect) error {
	if effect.Type == "" {
		return fmt.Errorf("effect type is required")
	}
	if effect.DurationMs <= 0 {
		return fmt.Errorf("duration must be positive")
	}
	return nil
}

func (h *Handlers) storeEffect(ctx context.Context, effect api.CombatEffect) error {
	_, err := h.db.Exec(ctx, `
		INSERT INTO combat_effects (id, participant_id, type, value, duration_ms, applied_at, expires_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
	`, effect.Id, effect.ParticipantId, effect.Type, effect.Value, effect.DurationMs, effect.AppliedAt, effect.ExpiresAt)
	return err
}

func abs(x float64) float64 {
	if x < 0 {
		return -x
	}
	return x
}

// Issue: #2251
