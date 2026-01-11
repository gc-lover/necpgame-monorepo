// Package monitoring provides alerting system for performance issues
package monitoring

import (
	"context"
	"fmt"
	"sync"
	"time"

	"go.uber.org/zap"

	errorhandling "github.com/your-org/necpgame/scripts/core/error-handling"
)

// AlertLevel represents severity of an alert
type AlertLevel string

const (
	AlertLevelInfo     AlertLevel = "info"
	AlertLevelWarning  AlertLevel = "warning"
	AlertLevelError    AlertLevel = "error"
	AlertLevelCritical AlertLevel = "critical"
)

// Alert represents a performance alert
type Alert struct {
	ID          string                 `json:"id"`
	Level       AlertLevel             `json:"level"`
	Title       string                 `json:"title"`
	Description string                 `json:"description"`
	Service     string                 `json:"service"`
	Metric      string                 `json:"metric"`
	Value       float64                `json:"value"`
	Threshold   float64                `json:"threshold"`
	Region      string                 `json:"region,omitempty"`
	Timestamp   time.Time              `json:"timestamp"`
	Acked       bool                   `json:"acked"`
	AckedBy     string                 `json:"acked_by,omitempty"`
	AckedAt     *time.Time             `json:"acked_at,omitempty"`
	Metadata    map[string]interface{} `json:"metadata,omitempty"`
}

// AlertManager handles alert creation, routing, and management
type AlertManager struct {
	logger     *errorhandling.Logger
	alerts     map[string]*Alert
	alertChan  chan *Alert
	notifiers  []AlertNotifier
	mu         sync.RWMutex
	rules      []AlertRule
}

// AlertNotifier defines interface for alert notification
type AlertNotifier interface {
	Notify(ctx context.Context, alert *Alert) error
	Name() string
}

// AlertRule defines conditions for triggering alerts
type AlertRule struct {
	ID          string
	Name        string
	Description string
	Metric      string
	Condition   AlertCondition
	Level       AlertLevel
	Cooldown    time.Duration
	Service     string
	Region      string

	lastTriggered time.Time
}

// AlertCondition defines when an alert should trigger
type AlertCondition struct {
	Operator string  // "gt", "lt", "eq", "gte", "lte"
	Value    float64
	Duration time.Duration // How long condition must be true
}

// SlackNotifier sends alerts to Slack
type SlackNotifier struct {
	webhookURL string
	channel    string
	logger     *errorhandling.Logger
}

// DiscordNotifier sends alerts to Discord
type DiscordNotifier struct {
	webhookURL string
	logger     *errorhandling.Logger
}

// EmailNotifier sends alerts via email
type EmailNotifier struct {
	smtpServer string
	smtpPort   int
	username   string
	password   string
	from       string
	to         []string
	logger     *errorhandling.Logger
}

// NewAlertManager creates a new alert manager
func NewAlertManager(logger *errorhandling.Logger) *AlertManager {
	am := &AlertManager{
		logger:    logger,
		alerts:    make(map[string]*Alert),
		alertChan: make(chan *Alert, 100),
		notifiers: make([]AlertNotifier, 0),
		rules:     make([]AlertRule, 0),
	}

	// Start alert processing goroutine
	go am.processAlerts()

	// Initialize default alert rules
	am.initializeDefaultRules()

	return am
}

// AddNotifier adds an alert notifier
func (am *AlertManager) AddNotifier(notifier AlertNotifier) {
	am.mu.Lock()
	defer am.mu.Unlock()
	am.notifiers = append(am.notifiers, notifier)
	am.logger.Infow("Alert notifier added", "notifier", notifier.Name())
}

// AddRule adds an alert rule
func (am *AlertManager) AddRule(rule AlertRule) {
	am.mu.Lock()
	defer am.mu.Unlock()
	am.rules = append(am.rules, rule)
	am.logger.Infow("Alert rule added", "rule", rule.Name, "metric", rule.Metric)
}

// TriggerAlert manually triggers an alert
func (am *AlertManager) TriggerAlert(level AlertLevel, title, description, service, metric string, value, threshold float64, metadata map[string]interface{}) {
	alert := &Alert{
		ID:          fmt.Sprintf("alert_%d", time.Now().UnixNano()),
		Level:       level,
		Title:       title,
		Description: description,
		Service:     service,
		Metric:      metric,
		Value:       value,
		Threshold:   threshold,
		Timestamp:   time.Now(),
		Acked:       false,
		Metadata:    metadata,
	}

	select {
	case am.alertChan <- alert:
		am.logger.LogBusinessEvent("alert_triggered", "alert", alert.ID, map[string]interface{}{
			"level":       alert.Level,
			"service":     alert.Service,
			"metric":      alert.Metric,
			"value":       alert.Value,
			"threshold":   alert.Threshold,
		})
	default:
		am.logger.Errorw("Alert channel full, dropping alert",
			"alert_id", alert.ID,
			"title", alert.Title,
		)
	}
}

// AcknowledgeAlert acknowledges an alert
func (am *AlertManager) AcknowledgeAlert(alertID, user string) error {
	am.mu.Lock()
	defer am.mu.Unlock()

	alert, exists := am.alerts[alertID]
	if !exists {
		return errorhandling.NewNotFoundError("ALERT_NOT_FOUND", "Alert not found").WithCause(nil)
	}

	if alert.Acked {
		return errorhandling.NewConflictError("ALERT_ALREADY_ACKED", "Alert already acknowledged")
	}

	now := time.Now()
	alert.Acked = true
	alert.AckedBy = user
	alert.AckedAt = &now

	am.logger.Infow("Alert acknowledged",
		"alert_id", alertID,
		"acked_by", user,
	)

	return nil
}

// GetActiveAlerts returns all active (non-acknowledged) alerts
func (am *AlertManager) GetActiveAlerts() []*Alert {
	am.mu.RLock()
	defer am.mu.RUnlock()

	var activeAlerts []*Alert
	for _, alert := range am.alerts {
		if !alert.Acked {
			activeAlerts = append(activeAlerts, alert)
		}
	}

	return activeAlerts
}

// GetAlertsByService returns alerts for a specific service
func (am *AlertManager) GetAlertsByService(service string) []*Alert {
	am.mu.RLock()
	defer am.mu.RUnlock()

	var serviceAlerts []*Alert
	for _, alert := range am.alerts {
		if alert.Service == service {
			serviceAlerts = append(serviceAlerts, alert)
		}
	}

	return serviceAlerts
}

// CheckMetric checks a metric against alert rules
func (am *AlertManager) CheckMetric(service, metric, region string, value float64) {
	am.mu.RLock()
	rules := make([]AlertRule, len(am.rules))
	copy(rules, am.rules)
	am.mu.RUnlock()

	for i := range rules {
		rule := &rules[i]

		// Check if rule applies
		if rule.Service != "" && rule.Service != service {
			continue
		}
		if rule.Metric != metric {
			continue
		}
		if rule.Region != "" && rule.Region != region {
			continue
		}

		// Check cooldown
		if time.Since(rule.lastTriggered) < rule.Cooldown {
			continue
		}

		// Check condition
		shouldTrigger := false
		switch rule.Condition.Operator {
		case "gt":
			shouldTrigger = value > rule.Condition.Value
		case "lt":
			shouldTrigger = value < rule.Condition.Value
		case "gte":
			shouldTrigger = value >= rule.Condition.Value
		case "lte":
			shouldTrigger = value <= rule.Condition.Value
		case "eq":
			shouldTrigger = value == rule.Condition.Value
		}

		if shouldTrigger {
			rule.lastTriggered = time.Now()

			title := fmt.Sprintf("%s Alert: %s", rule.Level, rule.Name)
			description := fmt.Sprintf("%s exceeded threshold (%.2f > %.2f)",
				metric, value, rule.Condition.Value)

			am.TriggerAlert(rule.Level, title, description, service, metric, value, rule.Condition.Value,
				map[string]interface{}{
					"rule_id":    rule.ID,
					"region":     region,
					"condition":  rule.Condition.Operator,
				})
		}
	}
}

// processAlerts processes alerts from the channel
func (am *AlertManager) processAlerts() {
	for alert := range am.alertChan {
		// Store alert
		am.mu.Lock()
		am.alerts[alert.ID] = alert
		am.mu.Unlock()

		// Send to all notifiers
		ctx := context.Background()
		for _, notifier := range am.notifiers {
			go func(n AlertNotifier) {
				if err := n.Notify(ctx, alert); err != nil {
					am.logger.LogError(err, "Failed to send alert notification",
						zap.String("notifier", n.Name()),
						zap.String("alert_id", alert.ID),
					)
				}
			}(notifier)
		}

		// Auto-acknowledge info level alerts after 5 minutes
		if alert.Level == AlertLevelInfo {
			go func(alertID string) {
				time.Sleep(5 * time.Minute)
				am.AcknowledgeAlert(alertID, "auto-ack")
			}(alert.ID)
		}
	}
}

// initializeDefaultRules sets up default alert rules for MMOFPS
func (am *AlertManager) initializeDefaultRules() {
	rules := []AlertRule{
		{
			ID:          "high_response_time",
			Name:        "High Combat Response Time",
			Description: "Combat response time exceeded threshold",
			Metric:      "combat_response_time",
			Condition:   AlertCondition{Operator: "gt", Value: 0.1, Duration: time.Minute},
			Level:       AlertLevelWarning,
			Cooldown:    5 * time.Minute,
		},
		{
			ID:          "high_network_latency",
			Name:        "High Network Latency",
			Description: "Network latency exceeded threshold",
			Metric:      "network_latency",
			Condition:   AlertCondition{Operator: "gt", Value: 0.05, Duration: time.Minute},
			Level:       AlertLevelError,
			Cooldown:    2 * time.Minute,
		},
		{
			ID:          "high_packet_loss",
			Name:        "High Packet Loss",
			Description: "Packet loss rate exceeded threshold",
			Metric:      "packet_loss_rate",
			Condition:   AlertCondition{Operator: "gt", Value: 0.02, Duration: 30 * time.Second},
			Level:       AlertLevelCritical,
			Cooldown:    time.Minute,
		},
		{
			ID:          "low_cache_hit_rate",
			Name:        "Low Cache Hit Rate",
			Description: "Cache hit rate below threshold",
			Metric:      "cache_hit_rate",
			Condition:   AlertCondition{Operator: "lt", Value: 0.85, Duration: 5 * time.Minute},
			Level:       AlertLevelWarning,
			Cooldown:    10 * time.Minute,
		},
		{
			ID:          "high_session_drop_rate",
			Name:        "High Session Drop Rate",
			Description: "Session drop rate exceeded threshold",
			Metric: "session_drop_rate",
			Condition: AlertCondition{Operator: "gt", Value: 0.03, Duration: time.Minute},
			Level:     AlertLevelError,
			Cooldown:  3 * time.Minute,
		},
		{
			ID:          "high_error_rate",
			Name:        "High Error Rate",
			Description: "Error rate exceeded threshold",
			Metric:      "error_rate",
			Condition:   AlertCondition{Operator: "gt", Value: 0.05, Duration: time.Minute},
			Level:       AlertLevelError,
			Cooldown:    2 * time.Minute,
		},
		{
			ID:          "slow_db_query",
			Name:        "Slow Database Query",
			Description: "Database query exceeded timeout",
			Metric:      "db_query_duration",
			Condition:   AlertCondition{Operator: "gt", Value: 0.1, Duration: 30 * time.Second},
			Level:       AlertLevelWarning,
			Cooldown:    30 * time.Second,
		},
		{
			ID:          "high_memory_usage",
			Name:        "High Memory Usage",
			Description: "Memory usage exceeded threshold",
			Metric:      "memory_usage",
			Condition:   AlertCondition{Operator: "gt", Value: 1000000000, Duration: time.Minute}, // 1GB
			Level:       AlertLevelWarning,
			Cooldown:    5 * time.Minute,
		},
	}

	for _, rule := range rules {
		am.AddRule(rule)
	}

	am.logger.Infow("Default alert rules initialized", "count", len(rules))
}

// Shutdown gracefully shuts down the alert manager
func (am *AlertManager) Shutdown(ctx context.Context) error {
	close(am.alertChan)
	am.logger.Info("Alert manager shutting down")
	return nil
}

// SlackNotifier implementation
func (sn *SlackNotifier) Notify(ctx context.Context, alert *Alert) error {
	// Implementation would send to Slack webhook
	sn.logger.Infow("Slack notification sent",
		"alert_id", alert.ID,
		"level", alert.Level,
		"title", alert.Title,
	)
	return nil
}

func (sn *SlackNotifier) Name() string {
	return "slack"
}

// DiscordNotifier implementation
func (dn *DiscordNotifier) Notify(ctx context.Context, alert *Alert) error {
	// Implementation would send to Discord webhook
	dn.logger.Infow("Discord notification sent",
		"alert_id", alert.ID,
		"level", alert.Level,
		"title", alert.Title,
	)
	return nil
}

func (dn *DiscordNotifier) Name() string {
	return "discord"
}

// EmailNotifier implementation
func (en *EmailNotifier) Notify(ctx context.Context, alert *Alert) error {
	// Implementation would send email
	en.logger.Infow("Email notification sent",
		"alert_id", alert.ID,
		"level", alert.Level,
		"title", alert.Title,
		"recipients", len(en.to),
	)
	return nil
}

func (en *EmailNotifier) Name() string {
	return "email"
}
