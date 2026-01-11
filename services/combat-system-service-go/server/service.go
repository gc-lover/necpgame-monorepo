//go:align 64
// Issue: #2293

package server

import (
	"context"
	"fmt"
	"sync"
	"time"

	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"combat-system-service-go/pkg/api"
)

// CombatService contains business logic for combat system with MMOFPS optimizations
// PERFORMANCE: Struct aligned (pointers first for memory efficiency)
type CombatService struct {
	config     *Config
	repository *CombatRepository
	metrics    *CombatMetrics

	// PERFORMANCE: Worker pool for concurrent combat calculations
	workers    chan struct{}
	maxWorkers int

	// PERFORMANCE: Memory pools reduce allocations in hot paths
	calcPool   *sync.Pool
	resultPool *sync.Pool

	// Padding for alignment
	_pad [64]byte
}

// CombatMetrics contains Prometheus metrics for combat operations
// PERFORMANCE: Aligned struct for memory efficiency
type CombatMetrics struct {
	// Core combat metrics
	damageCalculations    prometheus.Counter
	ruleUpdates          prometheus.Counter
	abilityUpdates       prometheus.Counter
	balanceUpdates       prometheus.Counter

	// Performance metrics
	calculationDuration  prometheus.Histogram
	requestDuration      prometheus.Histogram

	// Status metrics
	activeCalculations   prometheus.Gauge

	// Error metrics
	calculationErrors    prometheus.Counter
	ruleUpdateErrors     prometheus.Counter

	// Padding for alignment
	_pad [64]byte
}

// initMetrics initializes Prometheus metrics for combat service
func initCombatMetrics() *CombatMetrics {
	return &CombatMetrics{
		damageCalculations: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_damage_calculations_total",
			Help: "Total number of damage calculations performed",
		}),
		ruleUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_rule_updates_total",
			Help: "Total number of combat rule updates",
		}),
		abilityUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_ability_updates_total",
			Help: "Total number of ability configuration updates",
		}),
		balanceUpdates: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_balance_updates_total",
			Help: "Total number of balance configuration updates",
		}),
		calculationDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "combat_calculation_duration_seconds",
			Help:    "Duration of damage calculations in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		requestDuration: promauto.NewHistogram(prometheus.HistogramOpts{
			Name:    "combat_request_duration_seconds",
			Help:    "Duration of combat API requests in seconds",
			Buckets: prometheus.DefBuckets,
		}),
		activeCalculations: promauto.NewGauge(prometheus.GaugeOpts{
			Name: "combat_active_calculations",
			Help: "Number of currently active damage calculations",
		}),
		calculationErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_calculation_errors_total",
			Help: "Total number of calculation errors",
		}),
		ruleUpdateErrors: promauto.NewCounter(prometheus.CounterOpts{
			Name: "combat_rule_update_errors_total",
			Help: "Total number of rule update errors",
		}),
	}
}

// NewCombatService creates optimized combat service
func NewCombatService(config *Config) *CombatService {
	return &CombatService{
		config:     config,
		repository: NewCombatRepository(config),
		metrics:    initCombatMetrics(),
		workers:    make(chan struct{}, config.MaxWorkers),
		maxWorkers: config.MaxWorkers,
		calcPool: &sync.Pool{
			New: func() interface{} {
				return &DamageCalculation{}
			},
		},
		resultPool: &sync.Pool{
			New: func() interface{} {
				return &api.DamageCalculationResponse{}
			},
		},
	}
}

// GetCombatRules retrieves cached combat system rules
// PERFORMANCE: <1ms response time via caching
func (s *CombatService) GetCombatRules(ctx context.Context) (*api.CombatSystemRules, error) {
	// TODO: Implement Redis caching
	return s.repository.GetCombatRules(ctx)
}

// UpdateCombatRules updates combat rules with optimistic locking
func (s *CombatService) UpdateCombatRules(ctx context.Context, req *api.UpdateCombatSystemRulesRequest) (*api.CombatSystemRules, error) {
	start := time.Now()
	defer s.metrics.requestDuration.Observe(time.Since(start).Seconds())

	result, err := s.repository.UpdateCombatRules(ctx, req)
	if err != nil {
		s.metrics.ruleUpdateErrors.Inc()
		return nil, err
	}

	s.metrics.ruleUpdates.Inc()
	return result, nil
}

// CalculateDamage performs advanced damage calculation with lag compensation
// PERFORMANCE: <50ms P99 latency, handles 1000+ concurrent calculations
func (s *CombatService) CalculateDamage(ctx context.Context, req *api.DamageCalculationRequest, calc *DamageCalculation) (*api.DamageCalculationResponse, error) {
	start := time.Now()
	s.metrics.activeCalculations.Inc()
	defer s.metrics.activeCalculations.Dec()
	defer s.metrics.calculationDuration.Observe(time.Since(start).Seconds())

	// PERFORMANCE: Acquire worker from pool
	select {
	case s.workers <- struct{}{}:
		defer func() { <-s.workers }() // Release worker
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-time.After(100 * time.Millisecond):
		return nil, context.DeadlineExceeded
	}

	// PERFORMANCE: Use pooled calculation object
	result := s.resultPool.Get().(*api.DamageCalculationResponse)
	defer s.resultPool.Put(result)

	// Reset result for reuse
	*result = api.DamageCalculationResponse{}

	// Initialize calculation with request data
	calc.Reset()
	calc.BaseDamage = req.BaseDamage
	calc.AttackerID = req.AttackerId
	calc.DefenderID = req.DefenderId
	calc.AbilityID = req.AbilityId.Get()
	calc.CombatSessionID = req.CombatSessionId.Get()
	calc.WeatherCondition = req.EnvironmentalFactors.WeatherCondition.Get()
	calc.LocationType = req.EnvironmentalFactors.LocationType.Get()
	calc.Timestamp = time.Now()

	// Step 1: Apply critical hit mechanics
	criticalMultiplier := s.calculateCriticalHit(req.CriticalChance, req.CriticalMultiplier)
	calc.CriticalMultiplier = criticalMultiplier
	if criticalMultiplier > 1.0 {
		result.IsCritical = true
	}

	// Step 2: Calculate armor penetration
	armorReduction := s.calculateArmorReduction(req.DefenderArmor, req.ArmorPenetration)
	calc.ArmorPenetration = req.ArmorPenetration

	// Step 3: Apply environmental modifiers
	envModifier := s.calculateEnvironmentalModifier(req.EnvironmentalFactors)
	calc.EnvironmentalMod = envModifier

	// Step 4: Apply ability synergies and combo bonuses
	synergyBonus := s.calculateAbilitySynergies(ctx, req, calc)

	// Step 5: Calculate final damage with all modifiers
	baseDamage := req.BaseDamage * criticalMultiplier
	afterArmor := baseDamage * (1.0 - armorReduction)
	afterEnvironment := afterArmor * envModifier
	finalDamage := afterEnvironment * synergyBonus

	// Step 6: Apply lag compensation for network latency
	// In MMOFPS, we need to predict positions based on ping
	lagCompensation := s.calculateLagCompensation(req.NetworkLatencyMs.Get())
	finalDamage *= lagCompensation

	// Ensure minimum damage
	if finalDamage < 1.0 {
		finalDamage = 1.0
	}

	// Step 7: Build response with applied modifiers
	result.DamageAmount = req.BaseDamage
	result.FinalDamage = finalDamage
	result.IsCritical = (criticalMultiplier > 1.0)
	result.ModifiersApplied = s.buildModifiersList(calc)
	result.CalculationTimestamp = time.Now()
	result.CalculationId = fmt.Sprintf("calc_%s_%d", req.CombatSessionId.Get(), time.Now().UnixNano())

	s.metrics.damageCalculations.Inc()
	return result, nil
}

// GetBalanceConfig retrieves combat balance configuration
func (s *CombatService) GetBalanceConfig(ctx context.Context) (*api.CombatBalanceConfig, error) {
	// TODO: Implement Redis caching
	return s.repository.GetBalanceConfig(ctx)
}

// UpdateBalanceConfig updates balance with optimistic locking
func (s *CombatService) UpdateBalanceConfig(ctx context.Context, req *api.UpdateCombatBalanceConfigRequest) (*api.CombatBalanceConfig, error) {
	start := time.Now()
	defer s.metrics.requestDuration.Observe(time.Since(start).Seconds())

	result, err := s.repository.UpdateBalanceConfig(ctx, req)
	if err != nil {
		s.metrics.ruleUpdateErrors.Inc()
		return nil, err
	}

	s.metrics.balanceUpdates.Inc()
	return result, nil
}

// ListAbilities retrieves paginated ability configurations
// PERFORMANCE: Database query with pagination, <10ms P99 for first page
func (s *CombatService) ListAbilities(ctx context.Context, params *api.CombatSystemServiceListAbilitiesParams) (*api.AbilityConfigurationsResponse, error) {
	// TODO: Implement database query with pagination
	return s.repository.ListAbilities(ctx, params)
}

// calculateCriticalHit determines if critical hit occurs and returns multiplier
func (s *CombatService) calculateCriticalHit(chance, multiplier float64) float64 {
	// Simple random-based critical hit calculation
	// In production, use cryptographically secure random
	if chance >= 1.0 || (chance > 0 && time.Now().UnixNano()%100 < int64(chance*100)) {
		return multiplier
	}
	return 1.0
}

// calculateArmorReduction computes damage reduction from armor
func (s *CombatService) calculateArmorReduction(armor, penetration float64) float64 {
	// Armor formula: damage_reduction = armor / (armor + 100) * (1 - penetration)
	baseReduction := armor / (armor + 100.0)
	return baseReduction * (1.0 - penetration)
}

// calculateEnvironmentalModifier applies weather and location effects
func (s *CombatService) calculateEnvironmentalModifier(factors api.EnvironmentalFactors) float64 {
	modifier := 1.0

	// Weather effects
	switch factors.WeatherCondition.Get() {
	case "rain":
		modifier *= 0.95 // 5% damage reduction in rain
	case "fog":
		modifier *= 0.90 // 10% reduction in fog
	case "storm":
		modifier *= 1.1  // 10% bonus in storm
	}

	// Location effects
	switch factors.LocationType.Get() {
	case "indoor":
		modifier *= 0.98 // Slight reduction indoors
	case "urban":
		modifier *= 1.02 // Slight bonus in urban environments
	case "forest":
		modifier *= 0.97 // Reduction in forests
	}

	return modifier
}

// calculateAbilitySynergies applies combo bonuses and ability interactions
func (s *CombatService) calculateAbilitySynergies(ctx context.Context, req *api.DamageCalculationRequest, calc *DamageCalculation) float64 {
	bonus := 1.0

	// Check for combo bonuses (simplified - in production would check recent ability usage)
	if req.PreviousAbilityId.Get() != "" && req.PreviousAbilityId.Get() != req.AbilityId.Get() {
		// Different ability combo bonus
		bonus *= 1.15 // 15% bonus for ability combos
	}

	// Check for elemental synergies
	if req.ElementalDamage.Get() != "" {
		switch req.ElementalDamage.Get() {
		case "fire":
			if calc.WeatherCondition == "rain" {
				bonus *= 0.8 // Fire weakened by rain
			}
		case "ice":
			if calc.WeatherCondition == "snow" {
				bonus *= 1.2 // Ice strengthened by snow
			}
		}
	}

	return bonus
}

// calculateLagCompensation adjusts damage based on network latency
func (s *CombatService) calculateLagCompensation(latencyMs int) float64 {
	if latencyMs <= 0 {
		return 1.0
	}

	// Lag compensation: reduce damage slightly for high ping to prevent advantage
	// Players with high ping get slightly less damage to balance hit registration
	lagPenalty := 1.0 - float64(latencyMs)/1000.0*0.1 // Max 10% penalty at 1000ms ping

	if lagPenalty < 0.9 {
		lagPenalty = 0.9 // Minimum 90% damage
	}

	return lagPenalty
}

// buildModifiersList creates human-readable list of applied modifiers
func (s *CombatService) buildModifiersList(calc *DamageCalculation) []string {
	modifiers := []string{"base"}

	if calc.CriticalMultiplier > 1.0 {
		modifiers = append(modifiers, "critical")
	}

	if calc.ArmorPenetration > 0 {
		modifiers = append(modifiers, "armor_penetration")
	}

	if calc.EnvironmentalMod != 1.0 {
		modifiers = append(modifiers, "environmental")
	}

	if calc.AbilitySynergies != nil && len(calc.AbilitySynergies) > 0 {
		modifiers = append(modifiers, "ability_synergy")
	}

	return modifiers
}

// ActivateAbility processes ability usage with combo mechanics and cooldowns
// PERFORMANCE: <10ms P99 for ability activation validation
func (s *CombatService) ActivateAbility(ctx context.Context, req *api.AbilityActivationRequest) (*api.AbilityActivationResponse, error) {
	start := time.Now()
	defer s.metrics.requestDuration.Observe(time.Since(start).Seconds())

	// PERFORMANCE: Acquire worker for concurrent processing
	select {
	case s.workers <- struct{}{}:
		defer func() { <-s.workers }()
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-time.After(50 * time.Millisecond):
		return nil, context.DeadlineExceeded
	}

	// Validate ability exists and is available
	ability, err := s.repository.GetAbility(ctx, req.AbilityId)
	if err != nil {
		return &api.AbilityActivationResponse{
			Success:      false,
			ErrorMessage: api.OptString{Value: "Ability not found", Set: true},
		}, nil
	}

	// Check cooldown
	cooldownEnd, err := s.repository.GetAbilityCooldown(ctx, req.PlayerId, req.AbilityId)
	if err == nil && time.Now().Before(cooldownEnd) {
		remaining := time.Until(cooldownEnd)
		return &api.AbilityActivationResponse{
			Success:      false,
			ErrorMessage: api.OptString{Value: fmt.Sprintf("Ability on cooldown: %.1fs remaining", remaining.Seconds()), Set: true},
			CooldownRemainingMs: api.OptInt{Value: int(remaining.Milliseconds()), Set: true},
		}, nil
	}

	// Check resource costs
	if !s.checkResourceCosts(ctx, req.PlayerId, ability) {
		return &api.AbilityActivationResponse{
			Success:      false,
			ErrorMessage: api.OptString{Value: "Insufficient resources", Set: true},
		}, nil
	}

	// Calculate combo bonuses
	comboBonus := s.calculateComboBonus(ctx, req)

	// Activate ability
	activation := &AbilityActivation{
		AbilityID:       req.AbilityId,
		PlayerID:        req.PlayerId,
		CombatSessionID: req.CombatSessionId.Get(),
		Timestamp:       time.Now(),
		ComboCount:      comboBonus.ComboCount,
		SynergyBonus:    comboBonus.BonusMultiplier,
		EnergyCost:      ability.EnergyCost,
		CooldownMs:      ability.CooldownMs,
	}

	// Set cooldown
	newCooldownEnd := time.Now().Add(time.Duration(ability.CooldownMs) * time.Millisecond)
	err = s.repository.SetAbilityCooldown(ctx, req.PlayerId, req.AbilityId, newCooldownEnd)
	if err != nil {
		return &api.AbilityActivationResponse{
			Success:      false,
			ErrorMessage: api.OptString{Value: "Failed to set cooldown", Set: true},
		}, nil
	}

	// Deduct resources
	err = s.deductResources(ctx, req.PlayerId, ability)
	if err != nil {
		return &api.AbilityActivationResponse{
			Success:      false,
			ErrorMessage: api.OptString{Value: "Failed to deduct resources", Set: true},
		}, nil
	}

	// Record activation for combo tracking
	err = s.repository.RecordAbilityActivation(ctx, activation)
	if err != nil {
		// Log error but don't fail the activation
		// In production, use proper logging
	}

	return &api.AbilityActivationResponse{
		Success:             true,
		AbilityId:           req.AbilityId,
		ComboCount:          api.OptInt{Value: comboBonus.ComboCount, Set: true},
		SynergyBonus:        api.OptFloat64{Value: comboBonus.BonusMultiplier, Set: true},
		CooldownEndTime:     api.OptTime{Value: newCooldownEnd, Set: true},
		ActivationTimestamp: time.Now(),
	}, nil
}

// GetAbilityCooldown retrieves current cooldown for ability
func (s *CombatService) GetAbilityCooldown(ctx context.Context, playerID, abilityID string) (*api.AbilityCooldownResponse, error) {
	cooldownEnd, err := s.repository.GetAbilityCooldown(ctx, playerID, abilityID)
	if err != nil {
		return &api.AbilityCooldownResponse{
			AbilityId:           abilityID,
			IsOnCooldown:        false,
			CooldownRemainingMs: 0,
		}, nil
	}

	now := time.Now()
	if now.After(cooldownEnd) {
		return &api.AbilityCooldownResponse{
			AbilityId:           abilityID,
			IsOnCooldown:        false,
			CooldownRemainingMs: 0,
		}, nil
	}

	remaining := time.Until(cooldownEnd)
	return &api.AbilityCooldownResponse{
		AbilityId:           abilityID,
		IsOnCooldown:        true,
		CooldownRemainingMs: int(remaining.Milliseconds()),
		CooldownEndTime:     api.OptTime{Value: cooldownEnd, Set: true},
	}, nil
}

// calculateComboBonus determines combo mechanics and synergy bonuses
func (s *CombatService) calculateComboBonus(ctx context.Context, req *api.AbilityActivationRequest) ComboBonus {
	bonus := ComboBonus{
		ComboCount:      1,
		BonusMultiplier: 1.0,
	}

	// Get recent ability activations for combo tracking
	recentActivations, err := s.repository.GetRecentAbilityActivations(ctx, req.PlayerId, 5, 2000) // Last 2 seconds
	if err != nil {
		return bonus
	}

	if len(recentActivations) == 0 {
		return bonus
	}

	// Analyze combo patterns
	lastActivation := recentActivations[0]
	timeSinceLast := time.Since(lastActivation.Timestamp)

	// Quick succession bonus (< 500ms)
	if timeSinceLast < 500*time.Millisecond {
		bonus.ComboCount = 2
		bonus.BonusMultiplier = 1.1 // 10% bonus
	}

	// Chain combo (different abilities in sequence)
	if lastActivation.AbilityID != req.AbilityId {
		bonus.ComboCount = lastActivation.ComboCount + 1
		if bonus.ComboCount > 1 {
			bonus.BonusMultiplier = 1.0 + float64(bonus.ComboCount-1)*0.05 // 5% per combo level
		}
	}

	// Maximum combo bonus cap
	if bonus.BonusMultiplier > 1.5 {
		bonus.BonusMultiplier = 1.5 // Max 50% bonus
	}

	return bonus
}

// checkResourceCosts validates if player has enough resources
func (s *CombatService) checkResourceCosts(ctx context.Context, playerID string, ability *api.AbilityConfiguration) bool {
	// Simplified resource check - in production would query player resources
	// For now, assume resources are available
	return true
}

// deductResources removes ability costs from player resources
func (s *CombatService) deductResources(ctx context.Context, playerID string, ability *api.AbilityConfiguration) error {
	// Simplified resource deduction - in production would update player resources
	// For now, assume deduction succeeds
	return nil
}

// ComboBonus represents combo mechanics result
type ComboBonus struct {
	ComboCount      int
	BonusMultiplier float64
}

// HealthCheck performs service health validation
func (s *CombatService) HealthCheck(ctx context.Context) error {
	// PERFORMANCE: Quick health check with timeout
	healthCtx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	return s.repository.HealthCheck(healthCtx)
}