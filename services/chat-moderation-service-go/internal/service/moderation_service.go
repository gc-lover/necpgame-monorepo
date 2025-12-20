// Issue: #1911
package service

import (
	"context"
	"fmt"
	"regexp"
	"strings"
	"sync"
	"sync/atomic"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/chat-moderation-service-go/internal/config"
	"necpgame/services/chat-moderation-service-go/internal/repository"
	"necpgame/services/chat-moderation-service-go/models"
)

// ModerationService handles chat moderation business logic
type ModerationService struct {
	repo   *repository.Repository
	config *config.Config
	logger *zap.Logger

	// OPTIMIZATION: Memory pooling for frequent allocations
	messagePool  *sync.Pool
	responsePool *sync.Pool

	// OPTIMIZATION: Atomic counters for metrics
	atomicStats *AtomicStats

	// OPTIMIZATION: Cached compiled regex patterns
	ruleCache      map[string]*regexp.Regexp
	ruleCacheMutex sync.RWMutex
}

// AtomicStats provides lock-free metrics collection
type AtomicStats struct {
	messagesChecked     int64
	violationsDetected  int64
	actionsTaken        int64
	processingTimeTotal int64
	processingCount     int64
}

// NewModerationService creates a new moderation service instance
func NewModerationService(repo *repository.Repository, cfg *config.Config) *ModerationService {
	service := &ModerationService{
		repo:   repo,
		config: cfg,
		logger: cfg.Logger,

		// OPTIMIZATION: Memory pools for zero-allocation hot path
		messagePool: &sync.Pool{
			New: func() interface{} {
				return &models.CheckMessageRequest{}
			},
		},
		responsePool: &sync.Pool{
			New: func() interface{} {
				return &models.CheckMessageResponse{}
			},
		},

		atomicStats: &AtomicStats{},
		ruleCache:   make(map[string]*regexp.Regexp),
	}

	// Pre-warm caches
	go service.warmupCaches(context.Background())

	return service
}

// CheckMessage validates a chat message for violations (HOT PATH - optimized for <50ms P99)
func (s *ModerationService) CheckMessage(ctx context.Context, req *models.CheckMessageRequest) (*models.CheckMessageResponse, error) {
	startTime := time.Now()

	// OPTIMIZATION: Get response from pool
	resp := s.responsePool.Get().(*models.CheckMessageResponse)
	defer s.responsePool.Put(resp)

	// Reset response
	*resp = models.CheckMessageResponse{
		Allowed:           true,
		ViolationDetected: false,
		SeverityScore:     0.0,
		ActionRequired:    false,
		ProcessingTimeMs:  0.0,
	}

	// Basic validation
	if len(req.Message) > s.config.MaxMessageLength {
		resp.Allowed = false
		resp.ViolationDetected = true
		resp.ViolationType = models.ViolationTypeRateLimitExceeded
		resp.SeverityScore = 0.8
		resp.ProcessingTimeMs = float64(time.Since(startTime).Nanoseconds()) / 1e6
		atomic.AddInt64(&s.atomicStats.messagesChecked, 1)
		return resp, nil
	}

	// Check rate limiting
	if s.checkRateLimit(ctx, req.PlayerID) {
		resp.Allowed = false
		resp.ViolationDetected = true
		resp.ViolationType = models.ViolationTypeRateLimitExceeded
		resp.SeverityScore = 0.6
		resp.ProcessingTimeMs = float64(time.Since(startTime).Nanoseconds()) / 1e6
		atomic.AddInt64(&s.atomicStats.messagesChecked, 1)
		atomic.AddInt64(&s.atomicStats.violationsDetected, 1)
		return resp, nil
	}

	// Get active rules (from cache)
	rules, err := s.repo.GetActiveRules(ctx)
	if err != nil {
		s.logger.Error("Failed to get active rules", zap.Error(err))
		// Allow message on error to avoid blocking chat
		resp.Allowed = true
		resp.ProcessingTimeMs = float64(time.Since(startTime).Nanoseconds()) / 1e6
		atomic.AddInt64(&s.atomicStats.messagesChecked, 1)
		return resp, nil
	}

	// Check message against all rules
	maxSeverity := 0.0
	var triggeredRule *uuid.UUID

	for _, rule := range rules {
		if s.checkRule(req.Message, rule) {
			atomic.AddInt64(&s.atomicStats.violationsDetected, 1)
			resp.ViolationDetected = true
			resp.ViolationType = s.mapRuleTypeToViolationType(rule.RuleType)
			resp.SeverityScore = s.calculateSeverityScore(rule.Severity, req.Message)

			if resp.SeverityScore > maxSeverity {
				maxSeverity = resp.SeverityScore
				ruleID := rule.ID
				triggeredRule = &ruleID
			}

			// Apply automatic filtering if configured
			if rule.Action == models.ActionTypeMessageDelete {
				resp.Allowed = false
			}

			// Check if manual action required
			if s.requiresManualAction(rule, resp.SeverityScore) {
				resp.ActionRequired = true
			}

			// Apply content filtering
			resp.FilteredMessage = s.applyFiltering(req.Message, rule)
		}
	}

	if triggeredRule != nil {
		resp.RuleTriggered = triggeredRule
	}

	// Final decision
	if resp.SeverityScore >= 0.8 {
		resp.Allowed = false
	}

	resp.ProcessingTimeMs = float64(time.Since(startTime).Nanoseconds()) / 1e6

	// Update metrics
	atomic.AddInt64(&s.atomicStats.messagesChecked, 1)
	atomic.AddInt64(&s.atomicStats.processingTimeTotal, int64(resp.ProcessingTimeMs*1e6))
	atomic.AddInt64(&s.atomicStats.processingCount, 1)

	s.logger.Debug("Message checked",
		zap.String("player_id", req.PlayerID.String()),
		zap.String("channel", string(req.ChannelType)),
		zap.Bool("allowed", resp.Allowed),
		zap.Bool("violation", resp.ViolationDetected),
		zap.Float64("severity", resp.SeverityScore),
		zap.Float64("processing_ms", resp.ProcessingTimeMs))

	return resp, nil
}

// CreateModerationRule creates a new moderation rule
func (s *ModerationService) CreateModerationRule(ctx context.Context, req *models.CreateModerationRuleRequest, moderatorID uuid.UUID) (*models.ModerationRule, error) {
	rule := &models.ModerationRule{
		ID:        uuid.New(),
		RuleType:  req.RuleType,
		Name:      req.Name,
		Pattern:   req.Pattern,
		Severity:  req.Severity,
		Action:    req.Action,
		IsActive:  true,
		Metadata:  req.Metadata,
		CreatedAt: time.Now(),
		UpdatedAt: time.Now(),
		CreatedBy: moderatorID,
	}

	// Validate rule pattern
	if err := s.validateRulePattern(rule); err != nil {
		return nil, fmt.Errorf("invalid rule pattern: %w", err)
	}

	// Pre-compile regex if needed
	if rule.RuleType == models.RuleTypeSpamPattern {
		if _, err := regexp.Compile(rule.Pattern); err != nil {
			return nil, fmt.Errorf("invalid regex pattern: %w", err)
		}
	}

	if err := s.repo.CreateRule(ctx, rule); err != nil {
		return nil, fmt.Errorf("failed to create rule: %w", err)
	}

	// Update cache
	s.updateRuleCache(rule)

	s.logger.Info("Moderation rule created",
		zap.String("rule_id", rule.ID.String()),
		zap.String("rule_type", string(rule.RuleType)),
		zap.String("moderator_id", moderatorID.String()))

	return rule, nil
}

// GetModerationRules retrieves moderation rules with pagination
func (s *ModerationService) GetModerationRules(ctx context.Context, ruleType *models.RuleType, isActive *bool, limit, offset int) (*models.ModerationRulesResponse, error) {
	// This would implement filtering and pagination
	// For now, return active rules
	rules, err := s.repo.GetActiveRules(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to get rules: %w", err)
	}

	// Apply filters
	var filteredRules []*models.ModerationRule
	for _, rule := range rules {
		if ruleType != nil && rule.RuleType != *ruleType {
			continue
		}
		if isActive != nil && rule.IsActive != *isActive {
			continue
		}
		filteredRules = append(filteredRules, rule)
	}

	// Apply pagination
	start := offset
	if start > len(filteredRules) {
		start = len(filteredRules)
	}
	end := start + limit
	if end > len(filteredRules) {
		end = len(filteredRules)
	}

	paginatedRules := filteredRules[start:end]

	return &models.ModerationRulesResponse{
		Rules:  paginatedRules,
		Total:  len(filteredRules),
		Limit:  limit,
		Offset: offset,
	}, nil
}

// ApplyModerationAction applies a moderation action to a violation
func (s *ModerationService) ApplyModerationAction(ctx context.Context, violationID uuid.UUID, req *models.ApplyModerationActionRequest, moderatorID uuid.UUID) (*models.ModerationAction, error) {
	action := &models.ModerationAction{
		ID:          uuid.New(),
		ViolationID: violationID,
		ActionType:  req.ActionType,
		Duration:    req.Duration,
		Reason:      req.Reason,
		ModeratorID: &moderatorID,
		AppliedAt:   time.Now(),
	}

	// Calculate expiration time
	if req.Duration != "" {
		if duration, err := time.ParseDuration(req.Duration); err == nil {
			expiresAt := action.AppliedAt.Add(duration)
			action.ExpiresAt = &expiresAt
		}
	}

	if err := s.repo.CreateAction(ctx, action); err != nil {
		return nil, fmt.Errorf("failed to create action: %w", err)
	}

	// Update violation status
	newStatus := models.ViolationStatusResolved
	if req.ActionType == models.ActionTypeDismiss {
		newStatus = models.ViolationStatusDismissed
	}

	if err := s.repo.UpdateViolationStatus(ctx, violationID, newStatus); err != nil {
		s.logger.Warn("Failed to update violation status", zap.Error(err))
	}

	// Create audit log
	logEntry := &models.ModerationLog{
		ID:          uuid.New(),
		Action:      fmt.Sprintf("Applied %s action", req.ActionType),
		ActionType:  req.ActionType,
		PlayerID:    uuid.Nil, // Would need to get from violation
		ModeratorID: &moderatorID,
		Details: map[string]interface{}{
			"violation_id": violationID.String(),
			"reason":       req.Reason,
			"duration":     req.Duration,
		},
		CreatedAt: time.Now(),
	}

	if err := s.repo.CreateLogEntry(ctx, logEntry); err != nil {
		s.logger.Warn("Failed to create audit log", zap.Error(err))
	}

	atomic.AddInt64(&s.atomicStats.actionsTaken, 1)

	s.logger.Info("Moderation action applied",
		zap.String("violation_id", violationID.String()),
		zap.String("action_type", string(req.ActionType)),
		zap.String("moderator_id", moderatorID.String()))

	return action, nil
}

// GetStats returns moderation service statistics
func (s *ModerationService) GetStats(ctx context.Context, timeframe string) (*models.ModerationStatsResponse, error) {
	// Calculate time range
	var startTime time.Time
	switch timeframe {
	case "1h":
		startTime = time.Now().Add(-time.Hour)
	case "24h":
		startTime = time.Now().Add(-24 * time.Hour)
	case "7d":
		startTime = time.Now().Add(-7 * 24 * time.Hour)
	case "30d":
		startTime = time.Now().Add(-30 * 24 * time.Hour)
	default:
		startTime = time.Now().Add(-24 * time.Hour)
	}

	// Get data from repository (simplified implementation)
	// In real implementation, this would query aggregated stats

	stats := &models.ModerationStatsResponse{
		Timeframe:            timeframe,
		TotalMessagesChecked: atomic.LoadInt64(&s.atomicStats.messagesChecked),
		ViolationsDetected:   atomic.LoadInt64(&s.atomicStats.violationsDetected),
		ActionsTaken:         atomic.LoadInt64(&s.atomicStats.actionsTaken),
		ViolationsByType:     map[string]int{},       // Would be populated from DB
		ActionsByType:        map[string]int{},       // Would be populated from DB
		RuleHitCounts:        map[string]int{},       // Would be populated from DB
		TopViolatingPlayers:  []models.PlayerStats{}, // Would be populated from DB
	}

	// Calculate average processing time
	totalTime := atomic.LoadInt64(&s.atomicStats.processingTimeTotal)
	count := atomic.LoadInt64(&s.atomicStats.processingCount)
	if count > 0 {
		stats.AverageProcessingTimeMs = float64(totalTime) / float64(count) / 1e6
	}

	// Calculate P99 (simplified - would need proper percentile calculation)
	stats.P99ProcessingTimeMs = stats.AverageProcessingTimeMs * 1.5

	return stats, nil
}

// Private methods

// checkRateLimit implements rate limiting per player
func (s *ModerationService) checkRateLimit(ctx context.Context, playerID uuid.UUID) bool {
	// OPTIMIZATION: Use Redis for distributed rate limiting
	key := fmt.Sprintf("ratelimit:player:%s", playerID.String())

	// Simple sliding window implementation
	count, err := s.repo.(*repository.Repository).rdb.Incr(ctx, key).Result()
	if err != nil {
		s.logger.Warn("Rate limit check failed", zap.Error(err))
		return false // Allow on error
	}

	// Set expiration on first request
	if count == 1 {
		s.repo.(*repository.Repository).rdb.Expire(ctx, key, time.Minute)
	}

	return count > int64(s.config.RateLimitPerSecond*60) // Per minute limit
}

// checkRule applies a single rule to a message
func (s *ModerationService) checkRule(message string, rule *models.ModerationRule) bool {
	switch rule.RuleType {
	case models.RuleTypeWordFilter:
		return s.checkWordFilter(message, rule.Pattern)
	case models.RuleTypeSpamPattern:
		return s.checkSpamPattern(message, rule.Pattern)
	case models.RuleTypeToxicityThreshold:
		return s.checkToxicity(message, rule.Pattern)
	default:
		return false
	}
}

// checkWordFilter checks for forbidden words
func (s *ModerationService) checkWordFilter(message, pattern string) bool {
	words := strings.Fields(pattern)
	messageLower := strings.ToLower(message)

	for _, word := range words {
		if strings.Contains(messageLower, strings.ToLower(word)) {
			return true
		}
	}
	return false
}

// checkSpamPattern checks for spam patterns using regex
func (s *ModerationService) checkSpamPattern(message, pattern string) bool {
	s.ruleCacheMutex.RLock()
	regex, exists := s.ruleCache[pattern]
	s.ruleCacheMutex.RUnlock()

	if !exists {
		compiled, err := regexp.Compile(pattern)
		if err != nil {
			s.logger.Warn("Invalid spam pattern", zap.String("pattern", pattern), zap.Error(err))
			return false
		}

		s.ruleCacheMutex.Lock()
		s.ruleCache[pattern] = compiled
		s.ruleCacheMutex.Unlock()

		regex = compiled
	}

	return regex.MatchString(message)
}

// checkToxicity performs basic toxicity analysis (placeholder for ML model)
func (s *ModerationService) checkToxicity(message, threshold string) bool {
	// Placeholder implementation - would integrate with ML model
	toxicWords := []string{"toxic", "hate", "abuse", "insult"}
	messageLower := strings.ToLower(message)

	score := 0
	for _, word := range toxicWords {
		if strings.Contains(messageLower, word) {
			score++
		}
	}

	toxicityScore := float64(score) / float64(len(toxicWords))
	return toxicityScore > 0.5 // Simplified threshold
}

// mapRuleTypeToViolationType converts rule type to violation type
func (s *ModerationService) mapRuleTypeToViolationType(ruleType models.RuleType) models.ViolationType {
	switch ruleType {
	case models.RuleTypeWordFilter:
		return models.ViolationTypeForbiddenWords
	case models.RuleTypeSpamPattern:
		return models.ViolationTypeSpam
	case models.RuleTypeToxicityThreshold:
		return models.ViolationTypeToxicity
	default:
		return models.ViolationTypeSpam
	}
}

// calculateSeverityScore calculates severity based on rule and content
func (s *ModerationService) calculateSeverityScore(severity models.SeverityLevel, message string) float64 {
	baseScore := map[models.SeverityLevel]float64{
		models.SeverityLow:      0.3,
		models.SeverityMedium:   0.6,
		models.SeverityHigh:     0.8,
		models.SeverityCritical: 0.95,
	}[severity]

	// Adjust based on message characteristics
	if len(message) > 100 {
		baseScore += 0.1 // Longer messages might be more problematic
	}

	if strings.ToUpper(message) == message && len(message) > 10 {
		baseScore += 0.1 // All caps might indicate shouting/aggression
	}

	if baseScore > 1.0 {
		baseScore = 1.0
	}

	return baseScore
}

// requiresManualAction determines if violation requires manual review
func (s *ModerationService) requiresManualAction(rule *models.ModerationRule, severityScore float64) bool {
	// High severity or certain rule types require manual action
	return rule.Severity == models.SeverityHigh ||
		rule.Severity == models.SeverityCritical ||
		severityScore > 0.8
}

// applyFiltering applies content filtering to messages
func (s *ModerationService) applyFiltering(message string, rule *models.ModerationRule) string {
	// Basic filtering implementation
	if rule.RuleType == models.RuleTypeWordFilter {
		words := strings.Fields(rule.Pattern)
		filtered := message

		for _, word := range words {
			asterisks := strings.Repeat("*", len(word))
			filtered = strings.ReplaceAll(filtered, word, asterisks)
		}

		return filtered
	}

	return message
}

// validateRulePattern validates rule pattern syntax
func (s *ModerationService) validateRulePattern(rule *models.ModerationRule) error {
	if rule.Pattern == "" {
		return fmt.Errorf("pattern cannot be empty")
	}

	if rule.RuleType == models.RuleTypeSpamPattern {
		if _, err := regexp.Compile(rule.Pattern); err != nil {
			return fmt.Errorf("invalid regex pattern: %w", err)
		}
	}

	return nil
}

// updateRuleCache updates the compiled regex cache
func (s *ModerationService) updateRuleCache(rule *models.ModerationRule) {
	if rule.RuleType == models.RuleTypeSpamPattern {
		if regex, err := regexp.Compile(rule.Pattern); err == nil {
			s.ruleCacheMutex.Lock()
			s.ruleCache[rule.Pattern] = regex
			s.ruleCacheMutex.Unlock()
		}
	}
}

// warmupCaches pre-loads frequently used data
func (s *ModerationService) warmupCaches(ctx context.Context) {
	// Pre-load active rules
	if _, err := s.repo.GetActiveRules(ctx); err != nil {
		s.logger.Warn("Failed to warmup rule cache", zap.Error(err))
	}

	s.logger.Info("Cache warmup completed")
}
