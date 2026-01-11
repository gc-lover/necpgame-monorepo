package server

import (
	"context"
	"crypto/rand"
	"encoding/json"
	"fmt"
	"log/slog"
	"math"
	"math/big"
	"strings"
	"sync"
	"time"

	"github.com/google/uuid"
)

// FeatureFlagService provides feature flag and A/B testing capabilities
type FeatureFlagService struct {
	repo         Repository
	flagCache    map[string]*FeatureFlag
	experimentCache map[string]*Experiment
	mu           sync.RWMutex
}

// FeatureFlag represents a feature flag configuration
type FeatureFlag struct {
	ID          string            `json:"id"`
	Name        string            `json:"name"`
	Description string            `json:"description"`
	Enabled     bool              `json:"enabled"`
	Rollout     RolloutConfig     `json:"rollout"`
	Conditions  []Condition       `json:"conditions"`
	Tags        []string          `json:"tags"`
	CreatedAt   time.Time         `json:"created_at"`
	UpdatedAt   time.Time         `json:"updated_at"`
}

// RolloutConfig defines how to roll out a feature
type RolloutConfig struct {
	Strategy    string  `json:"strategy"`    // "percentage", "user_list", "rule_based"
	Percentage  float64 `json:"percentage"`  // 0-100 for percentage rollout
	UserIDs     []string `json:"user_ids"`   // Specific user IDs for user_list strategy
	Rules       []Rule   `json:"rules"`      // Rules for rule_based strategy
}

// Condition defines when a feature flag should be active
type Condition struct {
	Type      string      `json:"type"`      // "time", "user_property", "environment"
	Property  string      `json:"property"`  // Property name to check
	Operator  string      `json:"operator"`  // "eq", "ne", "gt", "lt", "contains", "regex"
	Value     interface{} `json:"value"`     // Value to compare against
}

// Rule defines a targeting rule
type Rule struct {
	Attribute string      `json:"attribute"` // User attribute (e.g., "country", "subscription_tier")
	Operator  string      `json:"operator"`  // Comparison operator
	Value     interface{} `json:"value"`     // Target value
}

// Experiment represents an A/B testing experiment
type Experiment struct {
	ID            string                 `json:"id"`
	Name          string                 `json:"name"`
	Description   string                 `json:"description"`
	Status        string                 `json:"status"`        // "draft", "running", "completed", "stopped"
	FeatureFlagID string                 `json:"feature_flag_id"`
	Variants      []ExperimentVariant    `json:"variants"`
	Targeting     ExperimentTargeting    `json:"targeting"`
	Metrics       []ExperimentMetric     `json:"metrics"`
	StartDate     *time.Time             `json:"start_date"`
	EndDate       *time.Time             `json:"end_date"`
	CreatedAt     time.Time              `json:"created_at"`
	UpdatedAt     time.Time              `json:"updated_at"`
}

// ExperimentVariant represents a variant in an A/B test
type ExperimentVariant struct {
	ID         string  `json:"id"`
	Name       string  `json:"name"`
	Percentage float64 `json:"percentage"` // Percentage of users in this variant
	Payload    interface{} `json:"payload"`    // Feature flag value for this variant
}

// ExperimentTargeting defines who should be included in the experiment
type ExperimentTargeting struct {
	Percentage float64    `json:"percentage"` // Overall experiment participation %
	Filters    []Rule     `json:"filters"`    // User targeting filters
}

// ExperimentMetric defines what to measure in the experiment
type ExperimentMetric struct {
	Name        string `json:"name"`
	Type        string `json:"type"`        // "conversion", "revenue", "engagement"
	EventName   string `json:"event_name"`  // Event to track
	Aggregation string `json:"aggregation"` // "count", "sum", "avg", "rate"
}

// UserContext provides context about a user for feature evaluation
type UserContext struct {
	UserID       string                 `json:"user_id"`
	Properties   map[string]interface{} `json:"properties"`
	Environment  string                 `json:"environment"`
	Timestamp    time.Time              `json:"timestamp"`
}

// EvaluationResult represents the result of feature flag evaluation
type EvaluationResult struct {
	FlagName     string      `json:"flag_name"`
	Value        interface{} `json:"value"`
	RuleID       string      `json:"rule_id,omitempty"`
	ExperimentID string      `json:"experiment_id,omitempty"`
	VariantID    string      `json:"variant_id,omitempty"`
	Timestamp    time.Time   `json:"timestamp"`
}

// NewFeatureFlagService creates a new feature flag service
func NewFeatureFlagService(repo Repository) *FeatureFlagService {
	return &FeatureFlagService{
		repo:            repo,
		flagCache:       make(map[string]*FeatureFlag),
		experimentCache: make(map[string]*Experiment),
	}
}

// EvaluateFeature evaluates a feature flag for a given user context
func (s *FeatureFlagService) EvaluateFeature(ctx context.Context, flagName string, userCtx *UserContext) (*EvaluationResult, error) {
	flag, err := s.getFeatureFlag(ctx, flagName)
	if err != nil {
		return nil, fmt.Errorf("failed to get feature flag: %w", err)
	}

	if !flag.Enabled {
		return &EvaluationResult{
			FlagName:  flagName,
			Value:     false,
			Timestamp: time.Now(),
		}, nil
	}

	// Check if user is part of an experiment
	experiment, variant, err := s.getUserExperimentVariant(ctx, flagName, userCtx)
	if err != nil {
		slog.Warn("Failed to get experiment variant", "flag_name", flagName, "user_id", userCtx.UserID, "error", err)
	}

	if experiment != nil && variant != nil {
		return &EvaluationResult{
			FlagName:     flagName,
			Value:        variant.Payload,
			ExperimentID: experiment.ID,
			VariantID:    variant.ID,
			Timestamp:    time.Now(),
		}, nil
	}

	// Check conditions
	if !s.evaluateConditions(flag.Conditions, userCtx) {
		return &EvaluationResult{
			FlagName:  flagName,
			Value:     false,
			Timestamp: time.Now(),
		}, nil
	}

	// Evaluate rollout
	enabled := s.evaluateRollout(flag.Rollout, userCtx)
	return &EvaluationResult{
		FlagName:  flagName,
		Value:     enabled,
		Timestamp: time.Now(),
	}, nil
}

// CreateFeatureFlag creates a new feature flag
func (s *FeatureFlagService) CreateFeatureFlag(ctx context.Context, flag *FeatureFlag) (*FeatureFlag, error) {
	flag.ID = uuid.New().String()
	flag.CreatedAt = time.Now()
	flag.UpdatedAt = time.Now()

	if err := s.repo.StoreFeatureFlag(ctx, flag); err != nil {
		return nil, fmt.Errorf("failed to store feature flag: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.flagCache[flag.Name] = flag
	s.mu.Unlock()

	slog.Info("Feature flag created", "flag_name", flag.Name, "flag_id", flag.ID)
	return flag, nil
}

// UpdateFeatureFlag updates an existing feature flag
func (s *FeatureFlagService) UpdateFeatureFlag(ctx context.Context, flagName string, updates *FeatureFlag) (*FeatureFlag, error) {
	flag, err := s.getFeatureFlag(ctx, flagName)
	if err != nil {
		return nil, err
	}

	// Update fields
	flag.Description = updates.Description
	flag.Enabled = updates.Enabled
	flag.Rollout = updates.Rollout
	flag.Conditions = updates.Conditions
	flag.Tags = updates.Tags
	flag.UpdatedAt = time.Now()

	if err := s.repo.UpdateFeatureFlag(ctx, flag); err != nil {
		return nil, fmt.Errorf("failed to update feature flag: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.flagCache[flagName] = flag
	s.mu.Unlock()

	slog.Info("Feature flag updated", "flag_name", flagName)
	return flag, nil
}

// CreateExperiment creates a new A/B testing experiment
func (s *FeatureFlagService) CreateExperiment(ctx context.Context, experiment *Experiment) (*Experiment, error) {
	experiment.ID = uuid.New().String()
	experiment.Status = "draft"
	experiment.CreatedAt = time.Now()
	experiment.UpdatedAt = time.Now()

	// Validate variant percentages sum to 100%
	totalPercentage := 0.0
	for _, variant := range experiment.Variants {
		totalPercentage += variant.Percentage
	}
	if math.Abs(totalPercentage-100.0) > 0.01 {
		return nil, fmt.Errorf("variant percentages must sum to 100%%, got %.2f%%", totalPercentage)
	}

	if err := s.repo.StoreExperiment(ctx, experiment); err != nil {
		return nil, fmt.Errorf("failed to store experiment: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.experimentCache[experiment.ID] = experiment
	s.mu.Unlock()

	slog.Info("Experiment created", "experiment_name", experiment.Name, "experiment_id", experiment.ID)
	return experiment, nil
}

// StartExperiment starts an A/B testing experiment
func (s *FeatureFlagService) StartExperiment(ctx context.Context, experimentID string) error {
	experiment, err := s.getExperiment(ctx, experimentID)
	if err != nil {
		return err
	}

	if experiment.Status != "draft" {
		return fmt.Errorf("experiment is not in draft status")
	}

	now := time.Now()
	experiment.Status = "running"
	experiment.StartDate = &now
	experiment.UpdatedAt = now

	if err := s.repo.UpdateExperiment(ctx, experiment); err != nil {
		return fmt.Errorf("failed to start experiment: %w", err)
	}

	// Update cache
	s.mu.Lock()
	s.experimentCache[experimentID] = experiment
	s.mu.Unlock()

	slog.Info("Experiment started", "experiment_id", experimentID)
	return nil
}

// GetExperimentResults gets results for an A/B testing experiment
func (s *FeatureFlagService) GetExperimentResults(ctx context.Context, experimentID string) (*ExperimentResults, error) {
	experiment, err := s.getExperiment(ctx, experimentID)
	if err != nil {
		return nil, err
	}

	results, err := s.repo.GetExperimentResults(ctx, experimentID)
	if err != nil {
		return nil, fmt.Errorf("failed to get experiment results: %w", err)
	}

	return &ExperimentResults{
		Experiment: *experiment,
		Results:    *results,
	}, nil
}

// Helper functions

func (s *FeatureFlagService) getFeatureFlag(ctx context.Context, flagName string) (*FeatureFlag, error) {
	s.mu.RLock()
	flag, exists := s.flagCache[flagName]
	s.mu.RUnlock()

	if exists {
		return flag, nil
	}

	flag, err := s.repo.GetFeatureFlag(ctx, flagName)
	if err != nil {
		return nil, err
	}

	// Update cache
	s.mu.Lock()
	s.flagCache[flagName] = flag
	s.mu.Unlock()

	return flag, nil
}

func (s *FeatureFlagService) getExperiment(ctx context.Context, experimentID string) (*Experiment, error) {
	s.mu.RLock()
	experiment, exists := s.experimentCache[experimentID]
	s.mu.RUnlock()

	if exists {
		return experiment, nil
	}

	experiment, err := s.repo.GetExperiment(ctx, experimentID)
	if err != nil {
		return nil, err
	}

	// Update cache
	s.mu.Lock()
	s.experimentCache[experimentID] = experiment
	s.mu.Unlock()

	return experiment, nil
}

func (s *FeatureFlagService) getUserExperimentVariant(ctx context.Context, flagName string, userCtx *UserContext) (*Experiment, *ExperimentVariant, error) {
	experiments, err := s.repo.GetActiveExperimentsForFlag(ctx, flagName)
	if err != nil {
		return nil, nil, err
	}

	for _, experiment := range experiments {
		// Check if user should be included in experiment
		if !s.shouldIncludeUserInExperiment(experiment, userCtx) {
			continue
		}

		// Determine variant for user
		variant := s.getVariantForUser(experiment, userCtx)
		if variant != nil {
			return &experiment, variant, nil
		}
	}

	return nil, nil, nil
}

func (s *FeatureFlagService) shouldIncludeUserInExperiment(experiment Experiment, userCtx *UserContext) bool {
	// Check overall participation percentage
	if experiment.Targeting.Percentage < 100.0 {
		userHash := s.hashUserID(userCtx.UserID, experiment.ID)
		if userHash > experiment.Targeting.Percentage {
			return false
		}
	}

	// Check targeting filters
	for _, filter := range experiment.Targeting.Filters {
		if !s.evaluateRule(filter, userCtx) {
			return false
		}
	}

	return true
}

func (s *FeatureFlagService) getVariantForUser(experiment Experiment, userCtx *UserContext) *ExperimentVariant {
	userHash := s.hashUserID(userCtx.UserID, experiment.ID)

	cumulativePercentage := 0.0
	for _, variant := range experiment.Variants {
		cumulativePercentage += variant.Percentage
		if userHash <= cumulativePercentage {
			return &variant
		}
	}

	return nil
}

func (s *FeatureFlagService) hashUserID(userID, experimentID string) float64 {
	// Simple hash function to distribute users consistently
	hashInput := userID + experimentID
	hash := 0
	for _, char := range hashInput {
		hash = (hash*31 + int(char)) % 1000
	}
	return float64(hash) / 10.0 // Convert to percentage (0-100)
}

func (s *FeatureFlagService) evaluateConditions(conditions []Condition, userCtx *UserContext) bool {
	for _, condition := range conditions {
		if !s.evaluateCondition(condition, userCtx) {
			return false
		}
	}
	return true
}

func (s *FeatureFlagService) evaluateCondition(condition Condition, userCtx *UserContext) bool {
	var userValue interface{}

	switch condition.Property {
	case "user_id":
		userValue = userCtx.UserID
	case "environment":
		userValue = userCtx.Environment
	case "timestamp":
		userValue = userCtx.Timestamp
	default:
		// Check user properties
		if val, exists := userCtx.Properties[condition.Property]; exists {
			userValue = val
		} else {
			return false
		}
	}

	return s.compareValues(userValue, condition.Operator, condition.Value)
}

func (s *FeatureFlagService) evaluateRule(rule Rule, userCtx *UserContext) bool {
	var userValue interface{}

	if val, exists := userCtx.Properties[rule.Attribute]; exists {
		userValue = val
	} else {
		return false
	}

	return s.compareValues(userValue, rule.Operator, rule.Value)
}

func (s *FeatureFlagService) compareValues(userValue interface{}, operator string, targetValue interface{}) bool {
	// Simple comparison logic - extend as needed
	switch operator {
	case "eq":
		return fmt.Sprintf("%v", userValue) == fmt.Sprintf("%v", targetValue)
	case "ne":
		return fmt.Sprintf("%v", userValue) != fmt.Sprintf("%v", targetValue)
	case "contains":
		userStr := fmt.Sprintf("%v", userValue)
		targetStr := fmt.Sprintf("%v", targetValue)
		return strings.Contains(userStr, targetStr)
	default:
		return false
	}
}

func (s *FeatureFlagService) evaluateRollout(rollout RolloutConfig, userCtx *UserContext) bool {
	switch rollout.Strategy {
	case "percentage":
		userHash := s.hashUserID(userCtx.UserID, "rollout")
		return userHash <= rollout.Percentage
	case "user_list":
		for _, userID := range rollout.UserIDs {
			if userID == userCtx.UserID {
				return true
			}
		}
		return false
	case "rule_based":
		for _, rule := range rollout.Rules {
			if s.evaluateRule(rule, userCtx) {
				return true
			}
		}
		return false
	default:
		return false
	}
}

// ExperimentResults represents experiment results
type ExperimentResults struct {
	Experiment Experiment             `json:"experiment"`
	Results    ExperimentResultData  `json:"results"`
}

// ExperimentResultData contains the actual experiment metrics
type ExperimentResultData struct {
	VariantResults []VariantResult `json:"variant_results"`
	Winner         string          `json:"winner,omitempty"`
	Confidence     float64         `json:"confidence"`
}

// VariantResult contains metrics for a specific variant
type VariantResult struct {
	VariantID    string             `json:"variant_id"`
	SampleSize   int64              `json:"sample_size"`
	MetricValues map[string]float64 `json:"metric_values"`
}