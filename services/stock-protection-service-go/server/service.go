package server

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"
)

// StockProtectionService implements surveillance and protection for stock exchange
type StockProtectionService struct {
	logger *zap.Logger
	db     *pgxpool.Pool
}

// NewStockProtectionService creates a new stock protection service instance
func NewStockProtectionService(logger *zap.Logger, db *pgxpool.Pool) *StockProtectionService {
	return &StockProtectionService{
		logger: logger,
		db:     db,
	}
}

// CircuitBreakerStatus represents the status of a circuit breaker
type CircuitBreakerStatus struct {
	StockID         string    `json:"stock_id"`
	IsActive        bool      `json:"is_active"`
	TriggeredAt     *time.Time `json:"triggered_at,omitempty"`
	TriggerReason   string    `json:"trigger_reason,omitempty"`
	ResumeAt        *time.Time `json:"resume_at,omitempty"`
	PriceThreshold  float64   `json:"price_threshold"`
	VolumeThreshold int64     `json:"volume_threshold"`
}

// SurveillanceAlert represents a surveillance alert
type SurveillanceAlert struct {
	ID           string    `json:"id"`
	StockID      string    `json:"stock_id"`
	AlertType    string    `json:"alert_type"`
	Severity     string    `json:"severity"`
	Status       string    `json:"status"`
	Description  string    `json:"description"`
	DetectedAt   time.Time `json:"detected_at"`
	ResolvedAt   *time.Time `json:"resolved_at,omitempty"`
	PlayerID     *string   `json:"player_id,omitempty"`
	Triggers     []AlertTrigger `json:"triggers"`
}

// AlertTrigger represents what triggered the alert
type AlertTrigger struct {
	Type      string      `json:"type"`
	Value     interface{} `json:"value"`
	Threshold interface{} `json:"threshold"`
}

// EnforcementAction represents an enforcement action
type EnforcementAction struct {
	ID          string     `json:"id"`
	PlayerID    string     `json:"player_id"`
	ActionType  string     `json:"action_type"`
	Reason      string     `json:"reason"`
	Status      string     `json:"status"`
	CreatedAt   time.Time  `json:"created_at"`
	ExecutedAt  *time.Time `json:"executed_at,omitempty"`
	ExpiresAt   *time.Time `json:"expires_at,omitempty"`
	Metadata    map[string]interface{} `json:"metadata"`
}

// GetCircuitBreakerStatus returns the circuit breaker status for a stock
func (s *StockProtectionService) GetCircuitBreakerStatus(w http.ResponseWriter, r *http.Request) {
	stockID := chi.URLParam(r, "stock_id")

	// Mock data for demonstration - in real implementation would query database
	status := CircuitBreakerStatus{
		StockID:         stockID,
		IsActive:        false,
		PriceThreshold:  15.0,  // 15% price change threshold
		VolumeThreshold: 1000000, // 1M volume threshold
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(status)
}

// GetSurveillanceAlerts returns list of surveillance alerts
func (s *StockProtectionService) GetSurveillanceAlerts(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	alertType := r.URL.Query().Get("alert_type")
	severity := r.URL.Query().Get("severity")
	status := r.URL.Query().Get("status")

	// Mock alerts for demonstration
	alerts := []SurveillanceAlert{
		{
			ID:          "alert-001",
			StockID:     "AAPL",
			AlertType:   "insider_trading",
			Severity:    "high",
			Status:      "active",
			Description: "Suspicious trading pattern detected",
			DetectedAt:  time.Now().Add(-1 * time.Hour),
			Triggers: []AlertTrigger{
				{Type: "volume_spike", Value: 500000, Threshold: 100000},
				{Type: "price_manipulation", Value: 12.5, Threshold: 10.0},
			},
		},
		{
			ID:          "alert-002",
			StockID:     "TSLA",
			AlertType:   "spoofing",
			Severity:    "medium",
			Status:      "investigating",
			Description: "Potential spoofing activity",
			DetectedAt:  time.Now().Add(-30 * time.Minute),
			Triggers: []AlertTrigger{
				{Type: "order_cancellation_rate", Value: 95.0, Threshold: 80.0},
			},
		},
	}

	// Filter alerts based on query parameters
	if alertType != "" {
		filtered := []SurveillanceAlert{}
		for _, alert := range alerts {
			if alert.AlertType == alertType {
				filtered = append(filtered, alert)
			}
		}
		alerts = filtered
	}

	if severity != "" {
		filtered := []SurveillanceAlert{}
		for _, alert := range alerts {
			if alert.Severity == severity {
				filtered = append(filtered, alert)
			}
		}
		alerts = filtered
	}

	if status != "" {
		filtered := []SurveillanceAlert{}
		for _, alert := range alerts {
			if alert.Status == status {
				filtered = append(filtered, alert)
			}
		}
		alerts = filtered
	}

	response := map[string]interface{}{
		"alerts":      alerts,
		"total_count": len(alerts),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetSurveillanceAlertDetails returns details of a specific alert
func (s *StockProtectionService) GetSurveillanceAlertDetails(w http.ResponseWriter, r *http.Request) {
	alertID := chi.URLParam(r, "alert_id")

	// Mock detailed alert
	alert := SurveillanceAlert{
		ID:          alertID,
		StockID:     "AAPL",
		AlertType:   "insider_trading",
		Severity:    "high",
		Status:      "active",
		Description: "Detailed analysis of suspicious trading pattern",
		DetectedAt:  time.Now().Add(-1 * time.Hour),
		Triggers: []AlertTrigger{
			{Type: "volume_spike", Value: 500000, Threshold: 100000},
			{Type: "price_manipulation", Value: 12.5, Threshold: 10.0},
			{Type: "timing_anomaly", Value: "pre-market", Threshold: "market_hours"},
		},
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{"alert": alert})
}

// UpdateSurveillanceAlertStatus updates the status of an alert
func (s *StockProtectionService) UpdateSurveillanceAlertStatus(w http.ResponseWriter, r *http.Request) {
	alertID := chi.URLParam(r, "alert_id")

	var request struct {
		Status     string `json:"status"`
		Resolution string `json:"resolution,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate status
	validStatuses := []string{"active", "investigating", "resolved", "dismissed"}
	isValid := false
	for _, status := range validStatuses {
		if request.Status == status {
			isValid = true
			break
		}
	}

	if !isValid {
		http.Error(w, "Invalid status", http.StatusBadRequest)
		return
	}

	// Mock response
	response := map[string]interface{}{
		"alert_id":   alertID,
		"status":     request.Status,
		"updated_at": time.Now(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// GetEnforcementActions returns list of enforcement actions
func (s *StockProtectionService) GetEnforcementActions(w http.ResponseWriter, r *http.Request) {
	// Parse pagination parameters
	page, _ := strconv.Atoi(r.URL.Query().Get("page"))
	if page < 1 {
		page = 1
	}
	perPage, _ := strconv.Atoi(r.URL.Query().Get("per_page"))
	if perPage < 1 || perPage > 100 {
		perPage = 20
	}

	// Mock enforcement actions
	actions := []EnforcementAction{
		{
			ID:         "enf-001",
			PlayerID:   "player-123",
			ActionType: "warning",
			Reason:     "First offense: suspicious trading pattern",
			Status:     "executed",
			CreatedAt:  time.Now().Add(-2 * time.Hour),
			ExecutedAt: &[]time.Time{time.Now().Add(-1 * time.Hour)}[0],
			Metadata: map[string]interface{}{
				"alert_id":    "alert-001",
				"severity":    "low",
				"auto_generated": true,
			},
		},
		{
			ID:         "enf-002",
			PlayerID:   "player-456",
			ActionType: "suspension",
			Reason:     "Market manipulation detected",
			Status:     "pending",
			CreatedAt:  time.Now().Add(-30 * time.Minute),
			ExpiresAt:  &[]time.Time{time.Now().Add(24 * time.Hour)}[0],
			Metadata: map[string]interface{}{
				"alert_id":    "alert-002",
				"severity":    "high",
				"duration_hours": 24,
			},
		},
	}

	response := map[string]interface{}{
		"actions":     actions,
		"total_count": len(actions),
		"page":        page,
		"per_page":    perPage,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(response)
}

// CreateEnforcementAction creates a new enforcement action
func (s *StockProtectionService) CreateEnforcementAction(w http.ResponseWriter, r *http.Request) {
	var request struct {
		PlayerID   string                 `json:"player_id"`
		ActionType string                 `json:"action_type"`
		Reason     string                 `json:"reason"`
		Metadata   map[string]interface{} `json:"metadata,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Validate required fields
	if request.PlayerID == "" || request.ActionType == "" || request.Reason == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	// Validate action type
	validActions := []string{"warning", "suspension", "ban", "confiscation", "fine"}
	isValid := false
	for _, action := range validActions {
		if request.ActionType == action {
			isValid = true
			break
		}
	}

	if !isValid {
		http.Error(w, "Invalid action type", http.StatusBadRequest)
		return
	}

	// Mock created action
	action := EnforcementAction{
		ID:         "enf-" + strconv.FormatInt(time.Now().Unix(), 10),
		PlayerID:   request.PlayerID,
		ActionType: request.ActionType,
		Reason:     request.Reason,
		Status:     "pending",
		CreatedAt:  time.Now(),
		Metadata:   request.Metadata,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(map[string]interface{}{"action": action})
}

// TriggerCircuitBreaker triggers a circuit breaker for a stock
func (s *StockProtectionService) TriggerCircuitBreaker(w http.ResponseWriter, r *http.Request) {
	var request struct {
		StockID       string `json:"stock_id"`
		Reason        string `json:"reason"`
		DurationHours int    `json:"duration_hours"`
	}

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if request.StockID == "" || request.Reason == "" {
		http.Error(w, "Missing required fields", http.StatusBadRequest)
		return
	}

	// Mock circuit breaker trigger
	triggeredAt := time.Now()
	resumeAt := triggeredAt.Add(time.Duration(request.DurationHours) * time.Hour)

	status := CircuitBreakerStatus{
		StockID:       request.StockID,
		IsActive:      true,
		TriggeredAt:   &triggeredAt,
		TriggerReason: request.Reason,
		ResumeAt:      &resumeAt,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "triggered",
		"circuit_breaker": status,
	})
}

// ResumeTrading resumes trading for a stock after circuit breaker
func (s *StockProtectionService) ResumeTrading(w http.ResponseWriter, r *http.Request) {
	var request struct {
		StockID string `json:"stock_id"`
		Reason  string `json:"reason,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	if request.StockID == "" {
		http.Error(w, "Missing stock_id", http.StatusBadRequest)
		return
	}

	// Mock trading resume
	resumeTime := time.Now()

	status := CircuitBreakerStatus{
		StockID:  request.StockID,
		IsActive: false,
		ResumeAt: &resumeTime,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "resumed",
		"circuit_breaker": status,
	})
}

