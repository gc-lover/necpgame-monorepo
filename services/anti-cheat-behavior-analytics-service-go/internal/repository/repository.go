// Anti-Cheat Behavior Analytics Repository
// Issue: #2212
// PostgreSQL + Redis repository for anti-cheat data persistence

package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
)

// Repository handles data persistence for anti-cheat analytics
type Repository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewRepository creates a new repository instance
func NewRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *Repository {
	return &Repository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// PlayerBehavior represents player behavior data
type PlayerBehavior struct {
	PlayerID      string                 `json:"player_id"`
	SessionID     string                 `json:"session_id"`
	BehaviorType  string                 `json:"behavior_type"`
	Data          map[string]interface{} `json:"data"`
	Timestamp     time.Time              `json:"timestamp"`
	Confidence    float64                `json:"confidence"`
	RiskScore     float64                `json:"risk_score"`
}

// DetectionRule represents a detection rule
type DetectionRule struct {
	ID          string                 `json:"id"`
	Name        string                 `json:"name"`
	Type        string                 `json:"type"`
	Config      map[string]interface{} `json:"config"`
	Enabled     bool                   `json:"enabled"`
	CreatedAt   time.Time              `json:"created_at"`
	UpdatedAt   time.Time              `json:"updated_at"`
}

// Alert represents a security alert
type Alert struct {
	ID          string    `json:"id"`
	PlayerID    string    `json:"player_id"`
	RuleID      string    `json:"rule_id"`
	Type        string    `json:"type"`
	Severity    string    `json:"severity"`
	Message     string    `json:"message"`
	Data        string    `json:"data"`
	Status      string    `json:"status"`
	CreatedAt   time.Time `json:"created_at"`
	AcknowledgedAt *time.Time `json:"acknowledged_at,omitempty"`
}

// Statistics holds aggregated statistics
type Statistics struct {
	TotalPlayers       int64     `json:"total_players"`
	HighRiskPlayers    int64     `json:"high_risk_players"`
	TotalAlerts        int64     `json:"total_alerts"`
	ActiveAlerts       int64     `json:"active_alerts"`
	AvgRiskScore       float64   `json:"avg_risk_score"`
	LastUpdated        time.Time `json:"last_updated"`
}

// SavePlayerBehavior saves player behavior data
func (r *Repository) SavePlayerBehavior(ctx context.Context, behavior *PlayerBehavior) error {
	query := `
		INSERT INTO anticheat.player_behaviors (
			player_id, session_id, behavior_type, data, timestamp, confidence, risk_score
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (player_id, session_id, behavior_type, timestamp)
		DO UPDATE SET
			data = EXCLUDED.data,
			confidence = EXCLUDED.confidence,
			risk_score = EXCLUDED.risk_score
	`

	dataJSON, err := json.Marshal(behavior.Data)
	if err != nil {
		return fmt.Errorf("failed to marshal behavior data: %w", err)
	}

	_, err = r.db.ExecContext(ctx, query,
		behavior.PlayerID,
		behavior.SessionID,
		behavior.BehaviorType,
		string(dataJSON),
		behavior.Timestamp,
		behavior.Confidence,
		behavior.RiskScore,
	)

	if err != nil {
		r.logger.Errorf("Failed to save player behavior: %v", err)
		return err
	}

	// Cache player risk score
	cacheKey := fmt.Sprintf("anticheat:risk:%s", behavior.PlayerID)
	r.redis.Set(ctx, cacheKey, behavior.RiskScore, 10*time.Minute)

	r.logger.Debugf("Saved player behavior for player %s", behavior.PlayerID)
	return nil
}

// GetPlayerBehavior retrieves player behavior data
func (r *Repository) GetPlayerBehavior(ctx context.Context, playerID string, limit int) ([]*PlayerBehavior, error) {
	query := `
		SELECT player_id, session_id, behavior_type, data, timestamp, confidence, risk_score
		FROM anticheat.player_behaviors
		WHERE player_id = $1
		ORDER BY timestamp DESC
		LIMIT $2
	`

	rows, err := r.db.QueryContext(ctx, query, playerID, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query player behavior: %w", err)
	}
	defer rows.Close()

	var behaviors []*PlayerBehavior
	for rows.Next() {
		var behavior PlayerBehavior
		var dataJSON string

		err := rows.Scan(
			&behavior.PlayerID,
			&behavior.SessionID,
			&behavior.BehaviorType,
			&dataJSON,
			&behavior.Timestamp,
			&behavior.Confidence,
			&behavior.RiskScore,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan player behavior: %v", err)
			continue
		}

		if err := json.Unmarshal([]byte(dataJSON), &behavior.Data); err != nil {
			r.logger.Errorf("Failed to unmarshal behavior data: %v", err)
			continue
		}

		behaviors = append(behaviors, &behavior)
	}

	return behaviors, rows.Err()
}

// GetPlayerRiskScore retrieves cached player risk score
func (r *Repository) GetPlayerRiskScore(ctx context.Context, playerID string) (float64, error) {
	cacheKey := fmt.Sprintf("anticheat:risk:%s", playerID)

	val, err := r.redis.Get(ctx, cacheKey).Float64()
	if err == redis.Nil {
		// Calculate from database
		return r.calculatePlayerRiskScore(ctx, playerID)
	} else if err != nil {
		r.logger.Errorf("Failed to get cached risk score: %v", err)
		return r.calculatePlayerRiskScore(ctx, playerID)
	}

	return val, nil
}

// calculatePlayerRiskScore calculates risk score from database
func (r *Repository) calculatePlayerRiskScore(ctx context.Context, playerID string) (float64, error) {
	query := `
		SELECT AVG(risk_score) as avg_risk, MAX(risk_score) as max_risk
		FROM anticheat.player_behaviors
		WHERE player_id = $1 AND timestamp > $2
	`

	cutoff := time.Now().Add(-24 * time.Hour) // Last 24 hours
	row := r.db.QueryRowContext(ctx, query, playerID, cutoff)

	var avgRisk, maxRisk sql.NullFloat64
	err := row.Scan(&avgRisk, &maxRisk)
	if err != nil {
		return 0.0, err
	}

	// Weighted average: 70% recent average, 30% peak risk
	riskScore := 0.0
	if avgRisk.Valid && maxRisk.Valid {
		riskScore = (avgRisk.Float64 * 0.7) + (maxRisk.Float64 * 0.3)
	} else if avgRisk.Valid {
		riskScore = avgRisk.Float64
	} else if maxRisk.Valid {
		riskScore = maxRisk.Float64
	}

	// Cache the result
	cacheKey := fmt.Sprintf("anticheat:risk:%s", playerID)
	r.redis.Set(ctx, cacheKey, riskScore, 10*time.Minute)

	return riskScore, nil
}

// SaveDetectionRule saves a detection rule
func (r *Repository) SaveDetectionRule(ctx context.Context, rule *DetectionRule) error {
	configJSON, err := json.Marshal(rule.Config)
	if err != nil {
		return fmt.Errorf("failed to marshal rule config: %w", err)
	}

	query := `
		INSERT INTO anticheat.detection_rules (
			id, name, type, config, enabled, created_at, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (id)
		DO UPDATE SET
			name = EXCLUDED.name,
			type = EXCLUDED.type,
			config = EXCLUDED.config,
			enabled = EXCLUDED.enabled,
			updated_at = EXCLUDED.updated_at
	`

	now := time.Now()
	_, err = r.db.ExecContext(ctx, query,
		rule.ID,
		rule.Name,
		rule.Type,
		string(configJSON),
		rule.Enabled,
		now,
		now,
	)

	if err != nil {
		r.logger.Errorf("Failed to save detection rule: %v", err)
		return err
	}

	r.logger.Infof("Saved detection rule: %s", rule.Name)
	return nil
}

// GetDetectionRules retrieves all detection rules
func (r *Repository) GetDetectionRules(ctx context.Context) ([]*DetectionRule, error) {
	query := `
		SELECT id, name, type, config, enabled, created_at, updated_at
		FROM anticheat.detection_rules
		ORDER BY created_at DESC
	`

	rows, err := r.db.QueryContext(ctx, query)
	if err != nil {
		return nil, fmt.Errorf("failed to query detection rules: %w", err)
	}
	defer rows.Close()

	var rules []*DetectionRule
	for rows.Next() {
		var rule DetectionRule
		var configJSON string

		err := rows.Scan(
			&rule.ID,
			&rule.Name,
			&rule.Type,
			&configJSON,
			&rule.Enabled,
			&rule.CreatedAt,
			&rule.UpdatedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan detection rule: %v", err)
			continue
		}

		if err := json.Unmarshal([]byte(configJSON), &rule.Config); err != nil {
			r.logger.Errorf("Failed to unmarshal rule config: %v", err)
			continue
		}

		rules = append(rules, &rule)
	}

	return rules, rows.Err()
}

// SaveAlert saves a security alert
func (r *Repository) SaveAlert(ctx context.Context, alert *Alert) error {
	query := `
		INSERT INTO anticheat.alerts (
			id, player_id, rule_id, type, severity, message, data, status, created_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := r.db.ExecContext(ctx, query,
		alert.ID,
		alert.PlayerID,
		alert.RuleID,
		alert.Type,
		alert.Severity,
		alert.Message,
		alert.Data,
		alert.Status,
		alert.CreatedAt,
	)

	if err != nil {
		r.logger.Errorf("Failed to save alert: %v", err)
		return err
	}

	r.logger.Infof("Saved alert for player %s: %s", alert.PlayerID, alert.Message)
	return nil
}

// GetAlerts retrieves alerts with filtering
func (r *Repository) GetAlerts(ctx context.Context, status string, limit int) ([]*Alert, error) {
	query := `
		SELECT id, player_id, rule_id, type, severity, message, data, status, created_at, acknowledged_at
		FROM anticheat.alerts
		WHERE ($1 = '' OR status = $1)
		ORDER BY created_at DESC
		LIMIT $2
	`

	rows, err := r.db.QueryContext(ctx, query, status, limit)
	if err != nil {
		return nil, fmt.Errorf("failed to query alerts: %w", err)
	}
	defer rows.Close()

	var alerts []*Alert
	for rows.Next() {
		var alert Alert

		err := rows.Scan(
			&alert.ID,
			&alert.PlayerID,
			&alert.RuleID,
			&alert.Type,
			&alert.Severity,
			&alert.Message,
			&alert.Data,
			&alert.Status,
			&alert.CreatedAt,
			&alert.AcknowledgedAt,
		)
		if err != nil {
			r.logger.Errorf("Failed to scan alert: %v", err)
			continue
		}

		alerts = append(alerts, &alert)
	}

	return alerts, rows.Err()
}

// UpdateAlertStatus updates alert status
func (r *Repository) UpdateAlertStatus(ctx context.Context, alertID, status string) error {
	var query string
	var args []interface{}

	if status == "acknowledged" {
		query = `UPDATE anticheat.alerts SET status = $1, acknowledged_at = $2 WHERE id = $3`
		args = []interface{}{status, time.Now(), alertID}
	} else {
		query = `UPDATE anticheat.alerts SET status = $1 WHERE id = $2`
		args = []interface{}{status, alertID}
	}

	_, err := r.db.ExecContext(ctx, query, args...)
	if err != nil {
		r.logger.Errorf("Failed to update alert status: %v", err)
		return err
	}

	r.logger.Infof("Updated alert %s status to %s", alertID, status)
	return nil
}

// GetStatistics retrieves aggregated statistics
func (r *Repository) GetStatistics(ctx context.Context) (*Statistics, error) {
	query := `
		SELECT
			COUNT(DISTINCT pb.player_id) as total_players,
			COUNT(DISTINCT CASE WHEN pb.risk_score > 0.7 THEN pb.player_id END) as high_risk_players,
			COUNT(a.id) as total_alerts,
			COUNT(CASE WHEN a.status = 'active' THEN 1 END) as active_alerts,
			AVG(pb.risk_score) as avg_risk_score
		FROM anticheat.player_behaviors pb
		LEFT JOIN anticheat.alerts a ON pb.player_id = a.player_id
		WHERE pb.timestamp > $1
	`

	cutoff := time.Now().Add(-24 * time.Hour)
	row := r.db.QueryRowContext(ctx, query, cutoff)

	stats := &Statistics{LastUpdated: time.Now()}
	err := row.Scan(
		&stats.TotalPlayers,
		&stats.HighRiskPlayers,
		&stats.TotalAlerts,
		&stats.ActiveAlerts,
		&stats.AvgRiskScore,
	)

	if err != nil {
		r.logger.Errorf("Failed to get statistics: %v", err)
		return nil, err
	}

	return stats, nil
}

// CleanupOldData removes old behavior data
func (r *Repository) CleanupOldData(ctx context.Context, retentionPeriod time.Duration) error {
	cutoff := time.Now().Add(-retentionPeriod)

	// Delete old behavior data
	behaviorQuery := `DELETE FROM anticheat.player_behaviors WHERE timestamp < $1`
	result, err := r.db.ExecContext(ctx, behaviorQuery, cutoff)
	if err != nil {
		return fmt.Errorf("failed to cleanup old behaviors: %w", err)
	}

	behaviorsDeleted := getRowsAffected(result)

	// Delete old alerts (keep for longer)
	alertCutoff := time.Now().Add(-retentionPeriod * 2)
	alertQuery := `DELETE FROM anticheat.alerts WHERE created_at < $1 AND status != 'active'`
	result, err = r.db.ExecContext(ctx, alertQuery, alertCutoff)
	if err != nil {
		return fmt.Errorf("failed to cleanup old alerts: %w", err)
	}

	alertsDeleted := getRowsAffected(result)

	r.logger.Infof("Cleaned up %d old behavior records and %d old alerts", behaviorsDeleted, alertsDeleted)
	return nil
}

// getRowsAffected returns the number of rows affected by a query
func getRowsAffected(result sql.Result) int64 {
	affected, err := result.RowsAffected()
	if err != nil {
		return 0
	}
	return affected
}
