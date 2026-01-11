//go:align 64
package service

import (
	"context"
	"fmt"
	"math"
	"math/rand"
	"sync"
	"time"

	"github.com/google/uuid"
	"github.com/prometheus/client_golang/prometheus"
	"github.com/prometheus/client_golang/prometheus/promauto"
	"github.com/redis/go-redis/v9"
	"golang.org/x/time/rate"
	"go.uber.org/zap"

	"github.com/NECPGAME/combat-system-service-go/internal/models"
	"github.com/NECPGAME/combat-system-service-go/internal/repository"
	"github.com/NECPGAME/combat-system-service-go/pkg/api"
)

//go:align 64
type Config struct {
	MaxAbilityNameLength     int           `yaml:"max_ability_name_length"`
	MaxAbilityDescription    int           `yaml:"max_ability_description"`
	DefaultCooldownMs        int           `yaml:"default_cooldown_ms"`
	DefaultCastTimeMs        int           `yaml:"default_cast_time_ms"`
	DamageCalculationTimeout time.Duration `yaml:"damage_calculation_timeout"`
	MaxConcurrentCalculations int          `yaml:"max_concurrent_calculations"`
}

//go:align 64
type Service struct {
	repo         repository.Repository
	logger       *zap.Logger
	config       Config
	rulesCache   *api.CombatSystemRules
	balanceCache *api.CombatBalanceConfig
	cacheMutex   sync.RWMutex
	semaphore    chan struct{}
	redis        *redis.Client
	rateLimiter  *rate.Limiter

	// Metrics
	damageCalculations    prometheus.Counter
	damageCalculationTime *prometheus.HistogramVec
	rulesUpdates          prometheus.Counter
	balanceUpdates        prometheus.Counter
	activeCalculations    prometheus.Gauge
	cacheHits             prometheus.Counter
	cacheMisses           prometheus.Counter
}

//go:align 64
func NewService(repo repository.Repository, logger *zap.Logger, config Config, redis *redis.Client) (*Service, error) {
	// Initialize semaphore for concurrent damage calculations
	semaphore := make(chan struct{}, config.MaxConcurrentCalculations)
	for i := 0; i < config.MaxConcurrentCalculations; i++ {
		semaphore <- struct{}{}
	}

	// Initialize metrics
	damageCalculations := promauto.NewCounter(prometheus.CounterOpts{
		Name: "damage_calculations_total",
		Help: "Total number of damage calculations performed",
	})

	damageCalculationTime := promauto.NewHistogramVec(prometheus.HistogramOpts{
		Name:    "damage_calculation_duration_seconds",
		Help:    "Duration of damage calculations",
		Buckets: prometheus.DefBuckets,
	}, []string{"result"})

	rulesUpdates := promauto.NewCounter(prometheus.CounterOpts{
		Name: "combat_rules_updates_total",
		Help: "Total number of combat rules updates",
	})

	balanceUpdates := promauto.NewCounter(prometheus.CounterOpts{
		Name: "combat_balance_updates_total",
		Help: "Total number of combat balance updates",
	})

	activeCalculations := promauto.NewGauge(prometheus.GaugeOpts{
		Name: "active_damage_calculations",
		Help: "Number of active damage calculations",
	})

	cacheHits := promauto.NewCounter(prometheus.CounterOpts{
		Name: "combat_cache_hits_total",
		Help: "Total number of cache hits",
	})

	cacheMisses := promauto.NewCounter(prometheus.CounterOpts{
		Name: "combat_cache_misses_total",
		Help: "Total number of cache misses",
	})

	// Initialize rate limiter (100 requests per second with burst of 50)
	rateLimiter := rate.NewLimiter(rate.Limit(100), 50)

	return &Service{
		repo:                  repo,
		logger:                logger,
		config:                config,
		semaphore:             semaphore,
		redis:                 redis,
		rateLimiter:           rateLimiter,
		damageCalculations:    damageCalculations,
		damageCalculationTime: damageCalculationTime,
		rulesUpdates:          rulesUpdates,
		balanceUpdates:        balanceUpdates,
		activeCalculations:    activeCalculations,
		cacheHits:             cacheHits,
		cacheMisses:           cacheMisses,
	}, nil
}

//go:align 64
func (s *Service) GetCombatRules(ctx context.Context) (*api.CombatSystemRules, error) {
	// Try memory cache first
	s.cacheMutex.RLock()
	if s.rulesCache != nil {
		s.cacheMutex.RUnlock()
		s.cacheHits.Inc()
		return s.rulesCache, nil
	}
	s.cacheMutex.RUnlock()

	// Try Redis cache
	cacheKey := "combat:rules:latest"
	if s.redis != nil {
		if cached, err := s.redis.Get(ctx, cacheKey).Result(); err == nil {
			var rules models.CombatSystemRules
			if err := json.Unmarshal([]byte(cached), &rules); err == nil {
				// Update memory cache
				s.cacheMutex.Lock()
				s.rulesCache = &rules
				s.cacheMutex.Unlock()
				s.cacheHits.Inc()
				return &rules, nil
			}
		}
	}

	s.cacheMutex.Lock()
	defer s.cacheMutex.Unlock()

	// Double-check after acquiring write lock
	if s.rulesCache != nil {
		s.cacheHits.Inc()
		return s.rulesCache, nil
	}

	rules, err := s.repo.GetCombatSystemRules(ctx)
	if err != nil {
		s.cacheMisses.Inc()
		return nil, fmt.Errorf("failed to get combat system rules: %w", err)
	}

	s.rulesCache = rules
	s.cacheMisses.Inc()

	// Cache in Redis
	if s.redis != nil {
		if data, err := json.Marshal(rules); err == nil {
			s.redis.Set(ctx, cacheKey, data, 30*time.Minute)
		}
	}

	return rules, nil
}

//go:align 64
func (s *Service) UpdateCombatRules(ctx context.Context, req *api.UpdateCombatSystemRulesRequest) (*api.CombatSystemRules, error) {
	// Get current rules
	rules, err := s.repo.GetCombatSystemRules(ctx)
	if err != nil {
		s.logger.Error("Failed to get current combat system rules", zap.Error(err))
		return nil, fmt.Errorf("failed to get current combat system rules: %w", err)
	}

	// Apply updates from request
	if req.DamageRules.IsSet() {
		rules.DamageRules = req.DamageRules.Value
	}
	if req.CombatMechanics.IsSet() {
		rules.CombatMechanics = req.CombatMechanics.Value
	}
	if req.BalanceParameters.IsSet() {
		rules.BalanceParameters = req.BalanceParameters.Value
	}

	// Validate rules
	if err := s.validateCombatSystemRules(rules); err != nil {
		return nil, fmt.Errorf("invalid combat system rules: %w", err)
	}

	// Increment version and update timestamp
	rules.Version++
	rules.UpdatedAt = time.Now()

	// Update in repository
	if err := s.repo.UpdateCombatSystemRules(ctx, rules); err != nil {
		s.logger.Error("Failed to update combat system rules", zap.Error(err))
		return nil, fmt.Errorf("failed to update combat system rules: %w", err)
	}

	// Update cache
	s.cacheMutex.Lock()
	rules.UpdatedAt = time.Now()
	s.rulesCache = rules
	s.cacheMutex.Unlock()

	// Invalidate Redis cache
	if s.redis != nil {
		s.redis.Del(ctx, "combat:rules:latest")
	}

	s.rulesUpdates.Inc()

	s.logger.Info("Combat system rules updated",
		zap.Int("version", rules.Version),
		zap.Time("updated_at", rules.UpdatedAt))

	return rules, nil
}

//go:align 64
func (s *Service) CalculateDamage(ctx context.Context, request *api.DamageCalculationRequest) (*api.DamageCalculationResponse, error) {
	timer := prometheus.NewTimer(s.damageCalculationTime.WithLabelValues("success"))
	defer timer.ObserveDuration()

	// Rate limiting check
	if !s.rateLimiter.Allow() {
		return nil, fmt.Errorf("rate limit exceeded")
	}

	// Acquire semaphore slot
	select {
	case <-ctx.Done():
		return nil, ctx.Err()
	case <-s.semaphore:
		defer func() { s.semaphore <- struct{}{} }()
	}

	s.activeCalculations.Inc()
	defer s.activeCalculations.Dec()

	// Get combat rules (with caching)
	rules, err := s.GetCombatSystemRules(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get combat rules: %w", err)
	}

	// Get balance config (with caching)
	balance, err := s.getCombatBalanceConfig(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get balance config: %w", err)
	}

	// Perform damage calculation
	response, err := s.performDamageCalculation(request, rules, balance)
	if err != nil {
		s.damageCalculationTime.WithLabelValues("error").Observe(time.Since(time.Now()).Seconds())
		return nil, fmt.Errorf("damage calculation failed: %w", err)
	}

	s.damageCalculations.Inc()

	s.logger.Debug("Damage calculated",
		zap.String("attacker_id", request.AttackerID.String()),
		zap.String("defender_id", request.DefenderID.String()),
		zap.Int("final_damage", response.FinalDamage),
		zap.Bool("critical_hit", response.CriticalHit))

	return response, nil
}

//go:align 64
func (s *Service) performDamageCalculation(request *models.DamageCalculationRequest, rules *models.CombatSystemRules, balance *models.CombatBalanceConfig) (*models.DamageCalculationResponse, error) {
	response := &models.DamageCalculationResponse{
		FinalDamage: request.BaseDamage,
		DamageType:  request.DamageType,
		Modifiers:   []models.DamageModifier{},
		CalculationLog: []string{},
	}

	// Apply global multipliers
	response.FinalDamage = int(float64(response.FinalDamage) * balance.GlobalMultipliers.DamageMultiplier)
	response.Modifiers = append(response.Modifiers, models.DamageModifier{
		Type:        "global_multiplier",
		Value:       balance.GlobalMultipliers.DamageMultiplier,
		Description: "Global damage multiplier",
	})

	// Apply base damage multiplier from rules
	response.FinalDamage = int(float64(response.FinalDamage) * rules.DamageRules.BaseDamageMultiplier)
	response.Modifiers = append(response.Modifiers, models.DamageModifier{
		Type:        "base_damage_multiplier",
		Value:       rules.DamageRules.BaseDamageMultiplier,
		Description: "Base damage multiplier from combat rules",
	})

	// Apply environmental modifiers
	if request.EnvironmentalFactors.Weather != "" {
		if modifier, exists := balance.EnvironmentalBalance.WeatherEffects[request.EnvironmentalFactors.Weather]; exists {
			response.FinalDamage = int(float64(response.FinalDamage) * modifier)
			response.Modifiers = append(response.Modifiers, models.DamageModifier{
				Type:        "weather_modifier",
				Value:       modifier,
				Description: fmt.Sprintf("Weather modifier: %s", request.EnvironmentalFactors.Weather),
			})
		}
	}

	// Apply time of day modifier
	if request.EnvironmentalFactors.TimeOfDay != "" {
		if modifier, exists := balance.EnvironmentalBalance.TimeOfDayEffects[request.EnvironmentalFactors.TimeOfDay]; exists {
			response.FinalDamage = int(float64(response.FinalDamage) * modifier)
			response.Modifiers = append(response.Modifiers, models.DamageModifier{
				Type:        "time_modifier",
				Value:       modifier,
				Description: fmt.Sprintf("Time of day modifier: %s", request.EnvironmentalFactors.TimeOfDay),
			})
		}
	}

	// Check for critical hit
	criticalChance := request.AttackerStats.CriticalChance
	if rand.Float64() < criticalChance {
		response.CriticalHit = true
		response.FinalDamage = int(float64(response.FinalDamage) * rules.DamageRules.CriticalHitMultiplier)
		response.Modifiers = append(response.Modifiers, models.DamageModifier{
			Type:        "critical_hit",
			Value:       rules.DamageRules.CriticalHitMultiplier,
			Description: "Critical hit multiplier applied",
		})
	}

	// Apply armor reduction
	if request.DefenderStats.Armor > 0 {
		armorReduction := float64(request.DefenderStats.Armor) * rules.DamageRules.ArmorReductionFactor
		damageReduction := int(math.Min(armorReduction, float64(response.FinalDamage)*0.9)) // Max 90% reduction
		response.FinalDamage -= damageReduction
		response.Modifiers = append(response.Modifiers, models.DamageModifier{
			Type:        "armor_reduction",
			Value:       -float64(damageReduction),
			Description: fmt.Sprintf("Armor reduction: %d damage blocked", damageReduction),
		})
	}

	// Ensure minimum damage of 1
	if response.FinalDamage < 1 {
		response.FinalDamage = 1
		response.Modifiers = append(response.Modifiers, models.DamageModifier{
			Type:        "minimum_damage",
			Value:       1,
			Description: "Minimum damage applied",
		})
	}

	// Create calculation log
	response.CalculationLog = []string{
		fmt.Sprintf("Base damage: %d", request.BaseDamage),
		fmt.Sprintf("Global multiplier: %.2f", balance.GlobalMultipliers.DamageMultiplier),
		fmt.Sprintf("Base damage multiplier: %.2f", rules.DamageRules.BaseDamageMultiplier),
		fmt.Sprintf("Critical hit: %t", response.CriticalHit),
		fmt.Sprintf("Final damage: %d", response.FinalDamage),
	}

	return response, nil
}

//go:align 64
func (s *Service) GetCombatBalanceConfig(ctx context.Context) (*models.CombatBalanceConfig, error) {
	return s.getCombatBalanceConfig(ctx)
}

//go:align 64
func (s *Service) getCombatBalanceConfig(ctx context.Context) (*models.CombatBalanceConfig, error) {
	s.cacheMutex.RLock()
	if s.balanceCache != nil {
		s.cacheMutex.RUnlock()
		s.cacheHits.Inc()
		return s.balanceCache, nil
	}
	s.cacheMutex.RUnlock()

	s.cacheMutex.Lock()
	defer s.cacheMutex.Unlock()

	// Double-check after acquiring write lock
	if s.balanceCache != nil {
		s.cacheHits.Inc()
		return s.balanceCache, nil
	}

	config, err := s.repo.GetCombatBalanceConfig(ctx)
	if err != nil {
		s.cacheMisses.Inc()
		return nil, fmt.Errorf("failed to get combat balance config: %w", err)
	}

	s.balanceCache = config
	s.cacheMisses.Inc()
	return config, nil
}

//go:align 64
func (s *Service) UpdateCombatBalanceConfig(ctx context.Context, req *api.UpdateCombatBalanceConfigRequest) (*models.CombatBalanceConfig, error) {
	// Get current config first
	currentConfig, err := s.getCombatBalanceConfig(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get current balance config: %w", err)
	}

	// Apply updates from request
	if req.GlobalMultipliers.IsSet() {
		currentConfig.GlobalMultipliers = req.GlobalMultipliers.Value
	}
	if req.CharacterBalance.IsSet() {
		currentConfig.CharacterBalance = req.CharacterBalance.Value
	}
	if req.EnvironmentBalance.IsSet() {
		currentConfig.EnvironmentalBalance = req.EnvironmentBalance.Value
	}

	// Validate config
	if err := s.validateCombatBalanceConfig(currentConfig); err != nil {
		return nil, fmt.Errorf("invalid combat balance config: %w", err)
	}

	// Increment version
	currentConfig.Version++
	currentConfig.UpdatedAt = time.Now()

	if err := s.repo.UpdateCombatBalanceConfig(ctx, currentConfig); err != nil {
		s.logger.Error("Failed to update combat balance config", zap.Error(err))
		return nil, fmt.Errorf("failed to update combat balance config: %w", err)
	}

	// Update cache
	s.cacheMutex.Lock()
	s.balanceCache = currentConfig
	s.cacheMutex.Unlock()

	// Invalidate Redis cache
	if s.redis != nil {
		s.redis.Del(ctx, "combat:balance:latest")
	}

	s.balanceUpdates.Inc()

	s.logger.Info("Combat balance config updated",
		zap.Int("version", currentConfig.Version),
		zap.Time("updated_at", currentConfig.UpdatedAt))

	return currentConfig, nil
}

//go:align 64
func (s *Service) ListAbilities(ctx context.Context, params api.CombatSystemServiceListAbilitiesParams) (*api.AbilityConfigurationsResponse, error) {
	limit := 50 // default
	if params.Limit != nil {
		limit = *params.Limit
	}
	offset := 0 // default
	if params.Offset != nil {
		offset = *params.Offset
	}

	var abilityType *string
	if params.Type != nil {
		abilityType = params.Type
	}

	abilities, total, err := s.repo.ListAbilityConfigurations(ctx, limit, offset, abilityType)
	if err != nil {
		s.logger.Error("Failed to list ability configurations", zap.Error(err))
		return nil, fmt.Errorf("failed to list ability configurations: %w", err)
	}

	// Convert to API response format
	abilityConfigs := make([]api.AbilityConfiguration, len(abilities))
	for i, ability := range abilities {
		effects := make([]api.AbilityEffect, len(ability.Effects))
		for j, effect := range ability.Effects {
			effects[j] = api.AbilityEffect{
				Type:       effect.Type,
				DurationMs: api.OptInt{Value: effect.DurationMs, Set: true},
				Power:      api.OptInt{Value: effect.Power, Set: true},
				Target:     api.OptString{Value: effect.Target, Set: true},
			}
		}

		abilityConfigs[i] = api.AbilityConfiguration{
			Id:               ability.ID,
			Name:             ability.Name,
			Type:             ability.Type,
			Description:      api.OptString{Value: ability.Description, Set: true},
			Damage:           api.OptInt{Value: ability.Damage, Set: true},
			CooldownMs:       ability.CooldownMs,
			ManaCost:         api.OptInt{Value: ability.ManaCost, Set: true},
			Range:            api.OptInt{Value: ability.Range, Set: true},
			CastTimeMs:       api.OptInt{Value: ability.CastTimeMs, Set: true},
			BalanceNotes:     api.OptString{Value: ability.BalanceNotes, Set: true},
			StatRequirements: ability.StatRequirements,
			Effects:          effects,
			CreatedAt:        ability.CreatedAt,
			UpdatedAt:        ability.UpdatedAt,
		}
	}

	return &api.AbilityConfigurationsResponse{
		Abilities: abilityConfigs,
		Total:     total,
		Page:      (offset / limit) + 1,
		Limit:     limit,
	}, nil
}

//go:align 64
func (s *Service) GetAbilityConfiguration(ctx context.Context, abilityID uuid.UUID) (*models.AbilityConfiguration, error) {
	ability, err := s.repo.GetAbilityConfiguration(ctx, abilityID)
	if err != nil {
		s.logger.Error("Failed to get ability configuration", zap.Error(err), zap.String("ability_id", abilityID.String()))
		return nil, fmt.Errorf("failed to get ability configuration: %w", err)
	}

	return ability, nil
}

//go:align 64
func (s *Service) CreateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error {
	// Validate ability
	if err := s.validateAbilityConfiguration(ability); err != nil {
		return fmt.Errorf("invalid ability configuration: %w", err)
	}

	ability.ID = uuid.New()
	ability.CreatedAt = time.Now()
	ability.UpdatedAt = time.Now()

	if err := s.repo.CreateAbilityConfiguration(ctx, ability); err != nil {
		s.logger.Error("Failed to create ability configuration", zap.Error(err))
		return fmt.Errorf("failed to create ability configuration: %w", err)
	}

	s.logger.Info("Ability configuration created",
		zap.String("ability_id", ability.ID.String()),
		zap.String("name", ability.Name),
		zap.String("type", ability.Type))

	return nil
}

//go:align 64
func (s *Service) UpdateAbilityConfiguration(ctx context.Context, ability *models.AbilityConfiguration) error {
	// Validate ability
	if err := s.validateAbilityConfiguration(ability); err != nil {
		return fmt.Errorf("invalid ability configuration: %w", err)
	}

	ability.UpdatedAt = time.Now()

	if err := s.repo.UpdateAbilityConfiguration(ctx, ability); err != nil {
		s.logger.Error("Failed to update ability configuration", zap.Error(err))
		return fmt.Errorf("failed to update ability configuration: %w", err)
	}

	s.logger.Info("Ability configuration updated",
		zap.String("ability_id", ability.ID.String()),
		zap.String("name", ability.Name))

	return nil
}

//go:align 64
func (s *Service) HealthCheck(ctx context.Context) error {
	health, err := s.repo.GetSystemHealth(ctx)
	if err != nil {
		s.logger.Error("Failed to get system health", zap.Error(err))
		return fmt.Errorf("failed to get system health: %w", err)
	}

	// Basic health check - ensure we can access the database
	if health.TotalCombatCalculations < 0 {
		return fmt.Errorf("invalid health data")
	}

	return nil
}

//go:align 64
func (s *Service) validateCombatSystemRules(rules *models.CombatSystemRules) error {
	if rules.DamageRules.BaseDamageMultiplier < 0.1 || rules.DamageRules.BaseDamageMultiplier > 5.0 {
		return fmt.Errorf("base damage multiplier must be between 0.1 and 5.0")
	}

	if rules.DamageRules.CriticalHitMultiplier < 1.0 || rules.DamageRules.CriticalHitMultiplier > 3.0 {
		return fmt.Errorf("critical hit multiplier must be between 1.0 and 3.0")
	}

	if rules.DamageRules.ArmorReductionFactor < 0.0 || rules.DamageRules.ArmorReductionFactor > 1.0 {
		return fmt.Errorf("armor reduction factor must be between 0.0 and 1.0")
	}

	return nil
}

//go:align 64
func (s *Service) validateCombatBalanceConfig(config *models.CombatBalanceConfig) error {
	if config.GlobalMultipliers.DamageMultiplier <= 0 {
		return fmt.Errorf("damage multiplier must be positive")
	}

	if config.GlobalMultipliers.HealingMultiplier <= 0 {
		return fmt.Errorf("healing multiplier must be positive")
	}

	if config.GlobalMultipliers.CooldownMultiplier <= 0 {
		return fmt.Errorf("cooldown multiplier must be positive")
	}

	return nil
}

//go:align 64
func (s *Service) validateAbilityConfiguration(ability *models.AbilityConfiguration) error {
	if len(ability.Name) < 1 || len(ability.Name) > s.config.MaxAbilityNameLength {
		return fmt.Errorf("ability name must be between 1 and %d characters", s.config.MaxAbilityNameLength)
	}

	if len(ability.Description) > s.config.MaxAbilityDescription {
		return fmt.Errorf("ability description must be less than %d characters", s.config.MaxAbilityDescription)
	}

	validTypes := map[string]bool{
		"offensive": true, "defensive": true, "utility": true, "ultimate": true,
	}
	if !validTypes[ability.Type] {
		return fmt.Errorf("invalid ability type: %s", ability.Type)
	}

	if ability.CooldownMs < 0 {
		return fmt.Errorf("cooldown must be non-negative")
	}

	if ability.Damage < 0 {
		return fmt.Errorf("damage must be non-negative")
	}

	if ability.ManaCost < 0 {
		return fmt.Errorf("mana cost must be non-negative")
	}

	if ability.Range < 0 {
		return fmt.Errorf("range must be non-negative")
	}

	if ability.CastTimeMs < 0 {
		return fmt.Errorf("cast time must be non-negative")
	}

	return nil
}