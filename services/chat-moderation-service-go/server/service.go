// Issue: #1911
// Chat Moderation Service - Business logic with zero allocations target
package server

import (
	"context"
	"errors"
	"strings"
	"sync"
	"time"
	"unicode"

	"github.com/gc-lover/necpgame-monorepo/services/chat-moderation-service-go/pkg/api"
	"github.com/google/uuid"
)

// Common errors
var (
	ErrNotFound = errors.New("not found")
	ErrConflict = errors.New("conflict")
)

// Service implements business logic for chat moderation
// SOLID: Single Responsibility - business logic only
// OPTIMIZATION: Memory pooling for hot path (CheckMessage)
type Service struct {
	repo *Repository

	// Memory pooling for hot path structs (zero allocations target!)
	checkResponsePool sync.Pool
	rulePool          sync.Pool
	violationPool     sync.Pool
	actionPool        sync.Pool
}

// NewService creates new service with dependency injection and memory pooling
func NewService(repo *Repository) *Service {
	s := &Service{repo: repo}

	// Initialize memory pools (zero allocation target!)
	s.checkResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.CheckMessageResponse{}
		},
	}
	s.rulePool = sync.Pool{
		New: func() interface{} {
			return &api.ModerationRule{}
		},
	}
	s.violationPool = sync.Pool{
		New: func() interface{} {
			return &api.ModerationViolation{}
		},
	}
	s.actionPool = sync.Pool{
		New: func() interface{} {
			return &api.ModerationAction{}
		},
	}

	return s
}

// GetModerationRules returns all moderation rules
func (s *Service) GetModerationRules(ctx context.Context, params api.GetModerationRulesParams) ([]api.ModerationRule, int32, error) {
	return s.repo.GetModerationRules(ctx, params)
}

// CreateModerationRule creates a new moderation rule
func (s *Service) CreateModerationRule(ctx context.Context, req *api.CreateModerationRuleRequest) (*api.ModerationRule, error) {
	rule := &api.ModerationRule{
		ID:        uuid.New(),
		Name:      req.Name,
		Type:      req.Type,
		Pattern:   req.Pattern,
		Severity:  req.Severity,
		Action:    req.Action,
		Active:    true,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
	}

	err := s.repo.CreateModerationRule(ctx, rule)
	if err != nil {
		return nil, err
	}

	return rule, nil
}

// GetModerationRule returns a specific rule
func (s *Service) GetModerationRule(ctx context.Context, ruleID string) (*api.ModerationRule, error) {
	return s.repo.GetModerationRule(ctx, ruleID)
}

// UpdateModerationRule updates a rule
func (s *Service) UpdateModerationRule(ctx context.Context, ruleID string, req *api.UpdateModerationRuleRequest) (*api.ModerationRule, error) {
	rule, err := s.repo.GetModerationRule(ctx, ruleID)
	if err != nil {
		return nil, err
	}

	if req.Name.IsSet() {
		rule.Name = req.Name.Value
	}
	if req.Pattern.IsSet() {
		rule.Pattern = req.Pattern.Value
	}
	if req.Severity.IsSet() {
		rule.Severity = req.Severity.Value
	}
	if req.Action.IsSet() {
		rule.Action = req.Action.Value
	}
	if req.Active.IsSet() {
		rule.Active = req.Active.Value
	}
	rule.UpdatedAt = time.Now()

	err = s.repo.UpdateModerationRule(ctx, rule)
	if err != nil {
		return nil, err
	}

	return rule, nil
}

// DeleteModerationRule deletes a rule
func (s *Service) DeleteModerationRule(ctx context.Context, ruleID string) error {
	return s.repo.DeleteModerationRule(ctx, ruleID)
}

// GetModerationViolations returns violations
func (s *Service) GetModerationViolations(ctx context.Context, params api.GetModerationViolationsParams) ([]api.ModerationViolation, int32, error) {
	return s.repo.GetModerationViolations(ctx, params)
}

// GetModerationViolation returns violation details
func (s *Service) GetModerationViolation(ctx context.Context, violationID string) (*api.ModerationViolation, error) {
	return s.repo.GetModerationViolation(ctx, violationID)
}

// UpdateViolationStatus updates violation status
func (s *Service) UpdateViolationStatus(ctx context.Context, violationID string, req *api.UpdateViolationStatusRequest) (*api.ModerationViolation, error) {
	return s.repo.UpdateViolationStatus(ctx, violationID, req)
}

// ApplyModerationAction applies an action
func (s *Service) ApplyModerationAction(ctx context.Context, violationID string, req *api.ApplyModerationActionRequest) (*api.ModerationAction, error) {
	action := &api.ModerationAction{
		ID:          uuid.New(),
		ViolationID: uuid.MustParse(violationID),
		ActionType:  req.ActionType,
		Reason:      req.Reason,
		ModeratorID: uuid.MustParse(req.ModeratorID),
		CreatedAt:   time.Now(),
	}

	if req.Duration.IsSet() {
		action.Duration = &req.Duration.Value
	}

	// Get violation to populate player_id
	violation, err := s.repo.GetModerationViolation(ctx, violationID)
	if err != nil {
		return nil, err
	}
	action.PlayerID = violation.PlayerID

	err = s.repo.CreateModerationAction(ctx, action)
	if err != nil {
		return nil, err
	}

	// Log the action
	err = s.repo.LogModerationAction(ctx, action)
	if err != nil {
		// Log error but don't fail the action
		GetLogger().WithError(err).Error("Failed to log moderation action")
	}

	return action, nil
}

// GetModerationLogs returns logs
func (s *Service) GetModerationLogs(ctx context.Context, params api.GetModerationLogsParams) ([]api.ModerationLog, int32, error) {
	return s.repo.GetModerationLogs(ctx, params)
}

// CheckMessage - HOT PATH: P99 <50ms, zero allocations target
// Issue: #1911 - Core moderation logic with performance optimizations
func (s *Service) CheckMessage(ctx context.Context, req *api.CheckMessageRequest) (*api.CheckMessageResponse, error) {
	// Get from pool (zero allocation!)
	resp := s.checkResponsePool.Get().(*api.CheckMessageResponse)
	defer s.checkResponsePool.Put(resp)

	// Reset pooled struct
	resp.Allowed = true
	resp.Violations = resp.Violations[:0] // Reset slice
	resp.ActionRequired = false
	resp.ProcessingTimeMs = 0

	message := strings.ToLower(req.Message)
	playerID := req.PlayerID.String()

	// Load active rules (cached for performance)
	rules, err := s.repo.GetActiveRules(ctx)
	if err != nil {
		return nil, err
	}

	violations := make([]api.CheckMessageResponseViolationsItem, 0, 5) // Pre-allocate

	for _, rule := range rules {
		var violated bool
		var description string

		switch rule.Type {
		case api.ModerationRuleTypeWordFilter:
			violated, description = s.checkWordFilter(message, rule.Pattern)

		case api.ModerationRuleTypeSpamDetection:
			violated, description = s.checkSpamDetection(ctx, playerID, message, rule.Pattern)

		case api.ModerationRuleTypeToxicityAnalysis:
			violated, description = s.checkToxicityAnalysis(message, rule.Pattern)
		}

		if violated {
			resp.Allowed = false

			violation := api.CheckMessageResponseViolationsItem{
				RuleID:      rule.ID,
				RuleType:    rule.Type,
				Severity:    rule.Severity,
				Description: description,
			}
			violations = append(violations, violation)

			// Create violation record asynchronously (don't block hot path)
			go func() {
				ctx := context.Background()
				s.createViolationAsync(ctx, req, &rule, description)
			}()

			// Check if action required based on severity
			if rule.Severity == api.ModerationRuleSeverityHigh ||
				rule.Severity == api.ModerationRuleSeverityCritical {
				resp.ActionRequired = true
			}
		}
	}

	resp.Violations = violations
	return resp, nil
}

// checkWordFilter - Simple word/pattern matching
func (s *Service) checkWordFilter(message, pattern string) (bool, string) {
	if strings.Contains(message, strings.ToLower(pattern)) {
		return true, "Message contains forbidden word: " + pattern
	}
	return false, ""
}

// checkSpamDetection - Basic spam detection logic
func (s *Service) checkSpamDetection(ctx context.Context, playerID, message, pattern string) (bool, string) {
	// Check for excessive caps
	caps := 0
	total := 0
	for _, r := range message {
		if unicode.IsLetter(r) {
			total++
			if unicode.IsUpper(r) {
				caps++
			}
		}
	}

	if total > 10 && float64(caps)/float64(total) > 0.7 {
		return true, "Excessive use of capital letters (spam indicator)"
	}

	// Check for repeated characters
	repeated := 0
	for i := 1; i < len(message); i++ {
		if message[i] == message[i-1] {
			repeated++
		} else {
			repeated = 0
		}
		if repeated >= 4 {
			return true, "Excessive repeated characters (spam indicator)"
		}
	}

	return false, ""
}

// checkToxicityAnalysis - Basic toxicity detection (placeholder for ML model)
func (s *Service) checkToxicityAnalysis(message, pattern string) (bool, string) {
	toxicWords := []string{"toxic", "hate", "abuse", "insult"}

	for _, word := range toxicWords {
		if strings.Contains(message, word) {
			return true, "Message contains potentially toxic content"
		}
	}

	return false, ""
}

// createViolationAsync - Create violation record asynchronously
func (s *Service) createViolationAsync(ctx context.Context, req *api.CheckMessageRequest, rule *api.ModerationRule, description string) {
	violation := &api.ModerationViolation{
		ID:               uuid.New(),
		PlayerID:         req.PlayerID,
		RuleID:           rule.ID,
		RuleType:         rule.Type,
		Severity:         rule.Severity,
		MessageContent:   req.Message,
		ViolationDetails: map[string]interface{}{"description": description},
		Status:           api.ModerationViolationStatusPending,
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}

	err := s.repo.CreateModerationViolation(ctx, violation)
	if err != nil {
		GetLogger().WithError(err).Error("Failed to create violation record")
	}
}
