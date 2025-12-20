// Issue: #1911
package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"necpgame/services/chat-moderation-service-go/models"
)

// Repository handles data persistence for chat moderation
type Repository struct {
	db     *pgxpool.Pool
	rdb    *redis.Client
	logger *zap.Logger
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool, rdb *redis.Client, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		rdb:    rdb,
		logger: logger,
	}
}

// GetRedisClient returns the Redis client for rate limiting operations
func (r *Repository) GetRedisClient() *redis.Client {
	return r.rdb
}

// ModerationRuleRepository interface for rule operations
type ModerationRuleRepository interface {
	CreateRule(ctx context.Context, rule *models.ModerationRule) error
	GetRule(ctx context.Context, ruleID uuid.UUID) (*models.ModerationRule, error)
	GetActiveRules(ctx context.Context) ([]*models.ModerationRule, error)
	UpdateRule(ctx context.Context, ruleID uuid.UUID, updates map[string]interface{}) error
	DeleteRule(ctx context.Context, ruleID uuid.UUID) error
}

// ModerationViolationRepository interface for violation operations
type ModerationViolationRepository interface {
	CreateViolation(ctx context.Context, violation *models.ModerationViolation) error
	GetViolation(ctx context.Context, violationID uuid.UUID) (*models.ModerationViolation, error)
	GetViolations(ctx context.Context, filter map[string]interface{}, limit, offset int) ([]*models.ModerationViolation, error)
	UpdateViolationStatus(ctx context.Context, violationID uuid.UUID, status models.ViolationStatus) error
	GetViolationsCount(ctx context.Context, filter map[string]interface{}) (int, error)
}

// ModerationActionRepository interface for action operations
type ModerationActionRepository interface {
	CreateAction(ctx context.Context, action *models.ModerationAction) error
	GetActionsByViolation(ctx context.Context, violationID uuid.UUID) ([]*models.ModerationAction, error)
	GetAction(ctx context.Context, actionID uuid.UUID) (*models.ModerationAction, error)
}

// ModerationLogRepository interface for audit log operations
type ModerationLogRepository interface {
	CreateLogEntry(ctx context.Context, logEntry *models.ModerationLog) error
	GetLogs(ctx context.Context, filter map[string]interface{}, limit, offset int) ([]*models.ModerationLog, error)
	GetLogsCount(ctx context.Context, filter map[string]interface{}) (int, error)
}

// Cache operations for performance optimization

// cacheRule stores rule in Redis cache
func (r *Repository) cacheRule(ctx context.Context, rule *models.ModerationRule) error {
	data, err := json.Marshal(rule)
	if err != nil {
		return fmt.Errorf("failed to marshal rule for cache: %w", err)
	}

	key := fmt.Sprintf("moderation:rule:%s", rule.ID.String())
	return r.rdb.Set(ctx, key, data, 5*time.Minute).Err()
}

// getCachedRule retrieves rule from Redis cache
func (r *Repository) getCachedRule(ctx context.Context, ruleID uuid.UUID) (*models.ModerationRule, error) {
	key := fmt.Sprintf("moderation:rule:%s", ruleID.String())
	data, err := r.rdb.Get(ctx, key).Result()
	if err != nil {
		return nil, err // Cache miss
	}

	var rule models.ModerationRule
	if err := json.Unmarshal([]byte(data), &rule); err != nil {
		r.logger.Warn("Failed to unmarshal cached rule", zap.Error(err), zap.String("rule_id", ruleID.String()))
		return nil, err
	}

	return &rule, nil
}

// invalidateRuleCache removes rule from cache
func (r *Repository) invalidateRuleCache(ctx context.Context, ruleID uuid.UUID) error {
	key := fmt.Sprintf("moderation:rule:%s", ruleID.String())
	return r.rdb.Del(ctx, key).Err()
}

// cacheViolation stores violation in Redis cache
func (r *Repository) cacheViolation(ctx context.Context, violation *models.ModerationViolation) error {
	data, err := json.Marshal(violation)
	if err != nil {
		return fmt.Errorf("failed to marshal violation for cache: %w", err)
	}

	key := fmt.Sprintf("moderation:violation:%s", violation.ID.String())
	return r.rdb.Set(ctx, key, data, 10*time.Minute).Err()
}

// Implementation of ModerationRuleRepository

// CreateRule creates a new moderation rule
func (r *Repository) CreateRule(ctx context.Context, rule *models.ModerationRule) error {
	query := `
		INSERT INTO moderation_rules (
			id, rule_type, name, pattern, severity, action, is_active,
			metadata, created_at, updated_at, created_by
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)
	`

	metadataJSON, err := json.Marshal(rule.Metadata)
	if err != nil {
		return fmt.Errorf("failed to marshal metadata: %w", err)
	}

	now := time.Now()
	_, err = r.db.Exec(ctx, query,
		rule.ID, rule.RuleType, rule.Name, rule.Pattern,
		rule.Severity, rule.Action, rule.IsActive,
		metadataJSON, now, now, rule.CreatedBy,
	)

	if err != nil {
		r.logger.Error("Failed to create moderation rule",
			zap.Error(err),
			zap.String("rule_id", rule.ID.String()),
			zap.String("rule_type", string(rule.RuleType)))
		return fmt.Errorf("failed to create rule: %w", err)
	}

	r.logger.Info("Moderation rule created",
		zap.String("rule_id", rule.ID.String()),
		zap.String("rule_type", string(rule.RuleType)))

	return nil
}

// GetRule retrieves a moderation rule by ID
func (r *Repository) GetRule(ctx context.Context, ruleID uuid.UUID) (*models.ModerationRule, error) {
	// Try cache first
	if rule, err := r.getCachedRule(ctx, ruleID); err == nil && rule != nil {
		return rule, nil
	}

	query := `
		SELECT id, rule_type, name, pattern, severity, action, is_active,
			   metadata, created_at, updated_at, created_by
		FROM moderation_rules
		WHERE id = $1
	`

	var rule models.ModerationRule
	var metadataJSON []byte

	err := r.db.QueryRow(ctx, query, ruleID).Scan(
		&rule.ID, &rule.RuleType, &rule.Name, &rule.Pattern,
		&rule.Severity, &rule.Action, &rule.IsActive,
		&metadataJSON, &rule.CreatedAt, &rule.UpdatedAt, &rule.CreatedBy,
	)

	if err != nil {
		r.logger.Warn("Failed to get moderation rule",
			zap.Error(err),
			zap.String("rule_id", ruleID.String()))
		return nil, fmt.Errorf("failed to get rule: %w", err)
	}

	// Unmarshal metadata
	if err := json.Unmarshal(metadataJSON, &rule.Metadata); err != nil {
		r.logger.Warn("Failed to unmarshal rule metadata",
			zap.Error(err),
			zap.String("rule_id", ruleID.String()))
		rule.Metadata = make(map[string]interface{})
	}

	// Cache the result
	if err := r.cacheRule(ctx, &rule); err != nil {
		r.logger.Warn("Failed to cache rule", zap.Error(err))
	}

	return &rule, nil
}

// GetActiveRules retrieves all active moderation rules
func (r *Repository) GetActiveRules(ctx context.Context) ([]*models.ModerationRule, error) {
	query := `
		SELECT id, rule_type, name, pattern, severity, action, is_active,
			   metadata, created_at, updated_at, created_by
		FROM moderation_rules
		WHERE is_active = true
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to query active rules: %w", err)
	}
	defer rows.Close()

	var rules []*models.ModerationRule
	for rows.Next() {
		var rule models.ModerationRule
		var metadataJSON []byte

		err := rows.Scan(
			&rule.ID, &rule.RuleType, &rule.Name, &rule.Pattern,
			&rule.Severity, &rule.Action, &rule.IsActive,
			&metadataJSON, &rule.CreatedAt, &rule.UpdatedAt, &rule.CreatedBy,
		)
		if err != nil {
			r.logger.Warn("Failed to scan rule", zap.Error(err))
			continue
		}

		// Unmarshal metadata
		if err := json.Unmarshal(metadataJSON, &rule.Metadata); err != nil {
			rule.Metadata = make(map[string]interface{})
		}

		rules = append(rules, &rule)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("error iterating rules: %w", err)
	}

	r.logger.Info("Retrieved active moderation rules", zap.Int("count", len(rules)))
	return rules, nil
}

// UpdateRule updates a moderation rule
func (r *Repository) UpdateRule(ctx context.Context, ruleID uuid.UUID, updates map[string]interface{}) error {
	// Build dynamic update query
	setParts := []string{}
	args := []interface{}{}
	argIndex := 1

	for field, value := range updates {
		setParts = append(setParts, fmt.Sprintf("%s = $%d", field, argIndex))
		args = append(args, value)
		argIndex++
	}

	if len(setParts) == 0 {
		return fmt.Errorf("no fields to update")
	}

	setParts = append(setParts, "updated_at = $"+fmt.Sprintf("%d", argIndex))
	args = append(args, time.Now())
	argIndex++

	query := fmt.Sprintf(`
		UPDATE moderation_rules
		SET %s
		WHERE id = $%d
	`, fmt.Sprintf("%s", setParts), argIndex)

	args = append(args, ruleID)

	_, err := r.db.Exec(ctx, query, args...)
	if err != nil {
		r.logger.Error("Failed to update moderation rule",
			zap.Error(err),
			zap.String("rule_id", ruleID.String()))
		return fmt.Errorf("failed to update rule: %w", err)
	}

	// Invalidate cache
	if err := r.invalidateRuleCache(ctx, ruleID); err != nil {
		r.logger.Warn("Failed to invalidate rule cache", zap.Error(err))
	}

	r.logger.Info("Moderation rule updated", zap.String("rule_id", ruleID.String()))
	return nil
}

// DeleteRule deletes a moderation rule
func (r *Repository) DeleteRule(ctx context.Context, ruleID uuid.UUID) error {
	query := `DELETE FROM moderation_rules WHERE id = $1`

	_, err := r.db.Exec(ctx, query, ruleID)
	if err != nil {
		r.logger.Error("Failed to delete moderation rule",
			zap.Error(err),
			zap.String("rule_id", ruleID.String()))
		return fmt.Errorf("failed to delete rule: %w", err)
	}

	// Invalidate cache
	if err := r.invalidateRuleCache(ctx, ruleID); err != nil {
		r.logger.Warn("Failed to invalidate rule cache", zap.Error(err))
	}

	r.logger.Info("Moderation rule deleted", zap.String("rule_id", ruleID.String()))
	return nil
}

// Placeholder implementations for other interfaces (to be implemented)
func (r *Repository) CreateViolation(ctx context.Context, violation *models.ModerationViolation) error {
	return fmt.Errorf("not implemented")
}

func (r *Repository) GetViolation(ctx context.Context, violationID uuid.UUID) (*models.ModerationViolation, error) {
	return nil, fmt.Errorf("not implemented")
}

func (r *Repository) GetViolations(ctx context.Context, filter map[string]interface{}, limit, offset int) ([]*models.ModerationViolation, error) {
	return nil, fmt.Errorf("not implemented")
}

func (r *Repository) UpdateViolationStatus(ctx context.Context, violationID uuid.UUID, status models.ViolationStatus) error {
	return fmt.Errorf("not implemented")
}

func (r *Repository) GetViolationsCount(ctx context.Context, filter map[string]interface{}) (int, error) {
	return 0, fmt.Errorf("not implemented")
}

func (r *Repository) CreateAction(ctx context.Context, action *models.ModerationAction) error {
	return fmt.Errorf("not implemented")
}

func (r *Repository) GetActionsByViolation(ctx context.Context, violationID uuid.UUID) ([]*models.ModerationAction, error) {
	return nil, fmt.Errorf("not implemented")
}

func (r *Repository) GetAction(ctx context.Context, actionID uuid.UUID) (*models.ModerationAction, error) {
	return nil, fmt.Errorf("not implemented")
}

func (r *Repository) CreateLogEntry(ctx context.Context, logEntry *models.ModerationLog) error {
	return fmt.Errorf("not implemented")
}

func (r *Repository) GetLogs(ctx context.Context, filter map[string]interface{}, limit, offset int) ([]*models.ModerationLog, error) {
	return nil, fmt.Errorf("not implemented")
}

func (r *Repository) GetLogsCount(ctx context.Context, filter map[string]interface{}) (int, error) {
	return 0, fmt.Errorf("not implemented")
}
