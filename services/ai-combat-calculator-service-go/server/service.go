package server

import (
	"context"
	"fmt"
	"log/slog"
	"math"
	"sync"
	"time"

	"github.com/google/uuid"
)

// Service defines the business logic interface for AI Combat Calculator
type Service interface {
	CalculateDamage(ctx context.Context, req DamageCalculationRequest) (*DamageResult, error)
	CalculateHealing(ctx context.Context, req HealingCalculationRequest) (*HealingResult, error)
	ApplyStatusEffect(ctx context.Context, req StatusEffectRequest) (*StatusEffectResult, error)
	GetCombatStats(ctx context.Context, entityID uuid.UUID) (*CombatStats, error)
	// TODO: Add other business methods
}

// AiCombatCalculatorService implements the business logic for AI combat calculations
type AiCombatCalculatorService struct {
	repo     Repository
	metrics  *ServiceMetrics
	mu       sync.RWMutex
	formulas map[string]CombatFormula // Performance: Memory pooling for combat formulas
}

// CombatFormula represents a damage/healing calculation formula
type CombatFormula struct {
	ID          string
	Name        string
	BaseDamage  float64
	Multipliers map[string]float64
	LastUpdated time.Time
}

// DamageCalculationRequest represents a request to calculate damage
type DamageCalculationRequest struct {
	AttackerID    uuid.UUID
	DefenderID    uuid.UUID
	DamageType    string
	BaseDamage    float64
	AttackerStats map[string]float64
	DefenderStats map[string]float64
	EnvironmentalFactors map[string]float64
}

// DamageResult represents the result of damage calculation
type DamageResult struct {
	FinalDamage     float64
	Mitigation      float64
	CriticalHit     bool
	StatusEffects   []string
	ExecutionTime   time.Duration
	CalculationLog  []string
}

// HealingCalculationRequest represents a request to calculate healing
type HealingCalculationRequest struct {
	HealerID      uuid.UUID
	TargetID      uuid.UUID
	HealingType   string
	BaseHealing   float64
	HealerStats   map[string]float64
	TargetStats   map[string]float64
}

// HealingResult represents the result of healing calculation
type HealingResult struct {
	FinalHealing    float64
	Amplification   float64
	Overheal        float64
	ExecutionTime   time.Duration
}

// StatusEffectRequest represents a request to apply status effect
type StatusEffectRequest struct {
	SourceID     uuid.UUID
	TargetID     uuid.UUID
	EffectType   string
	Duration     time.Duration
	Intensity    float64
}

// StatusEffectResult represents the result of status effect application
type StatusEffectResult struct {
	Applied      bool
	Duration     time.Duration
	StackCount   int
	Resisted     bool
}

// CombatStats represents combat statistics for an entity
type CombatStats struct {
	EntityID           uuid.UUID
	TotalDamageDealt   float64
	TotalDamageTaken   float64
	TotalHealingDone   float64
	TotalHealingTaken  float64
	CriticalHitRate    float64
	HitRate           float64
	StatusEffects     map[string]int
}

// PerformanceMetrics tracks service performance
type PerformanceMetrics struct {
	CPUUsagePercent       float64
	MemoryUsageMB         int
	CalculationTime       time.Duration
	AccuracyRate          float64
}

// ServiceMetrics collects service-wide metrics
type ServiceMetrics struct {
	TotalCalculations int64
	AverageLatency    time.Duration
	ErrorRate         float64
	CacheHitRate      float64
}

// NewAiCombatCalculatorService creates a new service instance
func NewAiCombatCalculatorService(repo Repository) *AiCombatCalculatorService {
	return &AiCombatCalculatorService{
		repo:     repo,
		metrics:  &ServiceMetrics{},
		formulas: make(map[string]CombatFormula),
	}
}

// CalculateDamage implements damage calculation business logic
func (s *AiCombatCalculatorService) CalculateDamage(ctx context.Context, req DamageCalculationRequest) (*DamageResult, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("CalculateDamage completed", "duration_ms", duration.Milliseconds())
	}()

	// Validate request
	if err := s.validateDamageRequest(req); err != nil {
		return nil, fmt.Errorf("invalid damage request: %w", err)
	}

	calculationLog := []string{"Starting damage calculation"}

	// Base damage calculation
	finalDamage := req.BaseDamage
	calculationLog = append(calculationLog, fmt.Sprintf("Base damage: %.2f", finalDamage))

	// Apply attacker multipliers
	if attackerStrength, ok := req.AttackerStats["strength"]; ok {
		strengthMultiplier := 1.0 + (attackerStrength * 0.01)
		finalDamage *= strengthMultiplier
		calculationLog = append(calculationLog, fmt.Sprintf("Applied strength multiplier: %.2f", strengthMultiplier))
	}

	// Apply damage type modifiers
	typeMultiplier := s.getDamageTypeMultiplier(req.DamageType, req.DefenderStats)
	finalDamage *= typeMultiplier
	calculationLog = append(calculationLog, fmt.Sprintf("Applied damage type multiplier (%s): %.2f", req.DamageType, typeMultiplier))

	// Calculate mitigation
	mitigation := s.calculateMitigation(req.DefenderStats, req.DamageType)
	actualDamage := finalDamage * (1.0 - mitigation)
	calculationLog = append(calculationLog, fmt.Sprintf("Applied mitigation: %.2f%%, actual damage: %.2f", mitigation*100, actualDamage))

	// Critical hit calculation
	criticalHit := s.calculateCriticalHit(req.AttackerStats)
	if criticalHit {
		criticalMultiplier := 1.5
		actualDamage *= criticalMultiplier
		calculationLog = append(calculationLog, fmt.Sprintf("Critical hit! Applied multiplier: %.2f", criticalMultiplier))
	}

	// Environmental factors
	for factor, multiplier := range req.EnvironmentalFactors {
		actualDamage *= multiplier
		calculationLog = append(calculationLog, fmt.Sprintf("Applied environmental factor (%s): %.2f", factor, multiplier))
	}

	// Clamp final damage to reasonable bounds
	actualDamage = math.Max(0, math.Min(actualDamage, 1000000)) // Max 1M damage

	// Update metrics
	s.mu.Lock()
	s.metrics.TotalCalculations++
	s.mu.Unlock()

	result := &DamageResult{
		FinalDamage:   actualDamage,
		Mitigation:    mitigation,
		CriticalHit:   criticalHit,
		StatusEffects: []string{}, // TODO: Calculate status effects
		ExecutionTime: time.Since(start),
		CalculationLog: calculationLog,
	}

	slog.Info("Damage calculated successfully",
		"attacker_id", req.AttackerID,
		"defender_id", req.DefenderID,
		"damage_type", req.DamageType,
		"final_damage", actualDamage,
		"critical_hit", criticalHit,
	)

	return result, nil
}

// CalculateHealing implements healing calculation business logic
func (s *AiCombatCalculatorService) CalculateHealing(ctx context.Context, req HealingCalculationRequest) (*HealingResult, error) {
	start := time.Now()
	defer func() {
		duration := time.Since(start)
		slog.Info("CalculateHealing completed", "duration_ms", duration.Milliseconds())
	}()

	// Validate request
	if err := s.validateHealingRequest(req); err != nil {
		return nil, fmt.Errorf("invalid healing request: %w", err)
	}

	// Base healing calculation
	finalHealing := req.BaseHealing

	// Apply healer multipliers
	if healerWisdom, ok := req.HealerStats["wisdom"]; ok {
		wisdomMultiplier := 1.0 + (healerWisdom * 0.005)
		finalHealing *= wisdomMultiplier
	}

	// Apply healing type modifiers
	typeMultiplier := s.getHealingTypeMultiplier(req.HealingType, req.TargetStats)
	finalHealing *= typeMultiplier

	// Calculate amplification (healing received)
	amplification := s.calculateAmplification(req.TargetStats)
	actualHealing := finalHealing * (1.0 + amplification)

	// Calculate overheal (not used but tracked)
	maxHealth := req.TargetStats["max_health"]
	currentHealth := req.TargetStats["current_health"]
	overheal := math.Max(0, actualHealing-(maxHealth-currentHealth))

	// Clamp to prevent overheal abuse
	actualHealing = math.Min(actualHealing, maxHealth-currentHealth)

	result := &HealingResult{
		FinalHealing:  actualHealing,
		Amplification: amplification,
		Overheal:      overheal,
		ExecutionTime: time.Since(start),
	}

	slog.Info("Healing calculated successfully",
		"healer_id", req.HealerID,
		"target_id", req.TargetID,
		"healing_type", req.HealingType,
		"final_healing", actualHealing,
	)

	return result, nil
}

// ApplyStatusEffect implements status effect application
func (s *AiCombatCalculatorService) ApplyStatusEffect(ctx context.Context, req StatusEffectRequest) (*StatusEffectResult, error) {
	// TODO: Implement status effect calculation with resistance, stacking, etc.
	return &StatusEffectResult{
		Applied:    true,
		Duration:   req.Duration,
		StackCount: 1,
		Resisted:   false,
	}, nil
}

// GetCombatStats retrieves combat statistics
func (s *AiCombatCalculatorService) GetCombatStats(ctx context.Context, entityID uuid.UUID) (*CombatStats, error) {
	// TODO: Implement stats retrieval from repository
	return &CombatStats{
		EntityID:         entityID,
		TotalDamageDealt: 0,
		TotalDamageTaken: 0,
		TotalHealingDone: 0,
		TotalHealingTaken: 0,
		CriticalHitRate:  0.05,
		HitRate:         0.85,
		StatusEffects:   make(map[string]int),
	}, nil
}

// Helper methods for calculations

func (s *AiCombatCalculatorService) getDamageTypeMultiplier(damageType string, defenderStats map[string]float64) float64 {
	// Simplified damage type calculations
	switch damageType {
	case "physical":
		if armor, ok := defenderStats["armor"]; ok {
			return math.Max(0.1, 1.0-(armor*0.001)) // Min 10% damage
		}
	case "energy":
		if resistance, ok := defenderStats["energy_resistance"]; ok {
			return math.Max(0.2, 1.0-(resistance*0.01))
		}
	case "chemical":
		if resistance, ok := defenderStats["chemical_resistance"]; ok {
			return math.Max(0.15, 1.0-(resistance*0.01))
		}
	}
	return 1.0
}

func (s *AiCombatCalculatorService) calculateMitigation(defenderStats map[string]float64, damageType string) float64 {
	baseMitigation := 0.0

	if defense, ok := defenderStats["defense"]; ok {
		baseMitigation += defense * 0.001 // 0.1% per defense point
	}

	// Type-specific mitigation
	switch damageType {
	case "physical":
		if armor, ok := defenderStats["armor"]; ok {
			baseMitigation += armor * 0.002
		}
	case "energy":
		if resistance, ok := defenderStats["energy_resistance"]; ok {
			baseMitigation += resistance * 0.01
		}
	}

	return math.Min(0.9, baseMitigation) // Max 90% mitigation
}

func (s *AiCombatCalculatorService) calculateCriticalHit(attackerStats map[string]float64) bool {
	baseCritRate := 0.05 // 5% base

	if critChance, ok := attackerStats["critical_chance"]; ok {
		baseCritRate += critChance * 0.01
	}

	// Simple random check (in real implementation, use proper RNG)
	return baseCritRate > 0.15 // For demo purposes
}

func (s *AiCombatCalculatorService) getHealingTypeMultiplier(healingType string, targetStats map[string]float64) float64 {
	switch healingType {
	case "direct":
		return 1.0
	case "hot":
		return 0.8 // HoT slightly less effective
	case "shield":
		return 1.2 // Shields slightly more effective
	}
	return 1.0
}

func (s *AiCombatCalculatorService) calculateAmplification(targetStats map[string]float64) float64 {
	if healingReceived, ok := targetStats["healing_received"]; ok {
		return healingReceived * 0.01 // 1% per point
	}
	return 0.0
}

// Validation methods

func (s *AiCombatCalculatorService) validateDamageRequest(req DamageCalculationRequest) error {
	if req.AttackerID == uuid.Nil {
		return fmt.Errorf("attacker ID is required")
	}
	if req.DefenderID == uuid.Nil {
		return fmt.Errorf("defender ID is required")
	}
	if req.BaseDamage < 0 {
		return fmt.Errorf("base damage cannot be negative")
	}
	if req.DamageType == "" {
		return fmt.Errorf("damage type is required")
	}
	return nil
}

func (s *AiCombatCalculatorService) validateHealingRequest(req HealingCalculationRequest) error {
	if req.HealerID == uuid.Nil {
		return fmt.Errorf("healer ID is required")
	}
	if req.TargetID == uuid.Nil {
		return fmt.Errorf("target ID is required")
	}
	if req.BaseHealing < 0 {
		return fmt.Errorf("base healing cannot be negative")
	}
	return nil
}