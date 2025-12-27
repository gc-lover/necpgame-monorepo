// Admin API handlers for stock exchange protection
// Issue: #140893702

package stockprotection

import (
	"encoding/json"
	"net/http"
	"strconv"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
)

// AdminAPI provides REST endpoints for stock exchange protection management
type AdminAPI struct {
	circuitBreaker      *CircuitBreaker
	manipulationDetector *ManipulationDetector
	alerts               []SurveillanceAlert
	enforcementActions   []EnforcementAction
}

// NewAdminAPI creates a new admin API instance
func NewAdminAPI(cb *CircuitBreaker, md *ManipulationDetector) *AdminAPI {
	return &AdminAPI{
		circuitBreaker:      cb,
		manipulationDetector: md,
		alerts:               make([]SurveillanceAlert, 0),
		enforcementActions:   make([]EnforcementAction, 0),
	}
}

// EnforcementAction represents an enforcement action against a user
type EnforcementAction struct {
	ID          string
	UserID      string
	ActionType  string // "warning", "suspension", "ban", "confiscation"
	Reason      string
	Symbol      string
	Amount      float64 // for confiscation
	Duration    time.Duration
	Status      string
	CreatedAt   time.Time
	ResolvedAt  time.Time
}

// RegisterRoutes registers API routes
func (api *AdminAPI) RegisterRoutes(r chi.Router) {
	r.Get("/stocks/protection/alerts", api.GetAlerts)
	r.Get("/stocks/protection/alerts/{id}", api.GetAlert)
	r.Put("/stocks/protection/alerts/{id}", api.UpdateAlert)
	r.Get("/stocks/protection/enforcement", api.GetEnforcementActions)
	r.Post("/stocks/protection/enforcement", api.CreateEnforcementAction)
	r.Get("/stocks/protection/enforcement/{id}", api.GetEnforcementAction)
	r.Put("/stocks/protection/enforcement/{id}", api.UpdateEnforcementAction)
	r.Get("/stocks/protection/circuit-breakers", api.GetCircuitBreakerStates)
	r.Get("/stocks/protection/circuit-breakers/{symbol}", api.GetCircuitBreakerState)
}

// GetAlerts returns all surveillance alerts
func (api *AdminAPI) GetAlerts(w http.ResponseWriter, r *http.Request) {
	// Parse query parameters
	status := r.URL.Query().Get("status")
	severity := r.URL.Query().Get("severity")
	limitStr := r.URL.Query().Get("limit")

	limit := 50 // default
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	filtered := make([]SurveillanceAlert, 0)
	for _, alert := range api.alerts {
		if status != "" && alert.Status != status {
			continue
		}
		if severity != "" && string(alert.Severity) != severity {
			continue
		}
		filtered = append(filtered, alert)
		if len(filtered) >= limit {
			break
		}
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"alerts": filtered,
		"total":  len(filtered),
	})
}

// GetAlert returns a specific alert
func (api *AdminAPI) GetAlert(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")

	for _, alert := range api.alerts {
		if alert.ID == id {
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(alert)
			return
		}
	}

	http.Error(w, "Alert not found", http.StatusNotFound)
}

// UpdateAlert updates an alert status
func (api *AdminAPI) UpdateAlert(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")

	var update struct {
		Status string `json:"status"`
	}

	if err := json.NewDecoder(r.Body).Decode(&update); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	for i, alert := range api.alerts {
		if alert.ID == id {
			api.alerts[i].Status = update.Status
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(api.alerts[i])
			return
		}
	}

	http.Error(w, "Alert not found", http.StatusNotFound)
}

// GetEnforcementActions returns all enforcement actions
func (api *AdminAPI) GetEnforcementActions(w http.ResponseWriter, r *http.Request) {
	userID := r.URL.Query().Get("user_id")
	actionType := r.URL.Query().Get("action_type")
	limitStr := r.URL.Query().Get("limit")

	limit := 50
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 {
			limit = l
		}
	}

	filtered := make([]EnforcementAction, 0)
	for _, action := range api.enforcementActions {
		if userID != "" && action.UserID != userID {
			continue
		}
		if actionType != "" && action.ActionType != actionType {
			continue
		}
		filtered = append(filtered, action)
		if len(filtered) >= limit {
			break
		}
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"actions": filtered,
		"total":   len(filtered),
	})
}

// CreateEnforcementAction creates a new enforcement action
func (api *AdminAPI) CreateEnforcementAction(w http.ResponseWriter, r *http.Request) {
	var action EnforcementAction
	if err := json.NewDecoder(r.Body).Decode(&action); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	action.ID = uuid.New().String()
	action.CreatedAt = time.Now()
	action.Status = "active"

	api.enforcementActions = append(api.enforcementActions, action)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(action)
}

// GetEnforcementAction returns a specific enforcement action
func (api *AdminAPI) GetEnforcementAction(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")

	for _, action := range api.enforcementActions {
		if action.ID == id {
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(action)
			return
		}
	}

	http.Error(w, "Enforcement action not found", http.StatusNotFound)
}

// UpdateEnforcementAction updates an enforcement action
func (api *AdminAPI) UpdateEnforcementAction(w http.ResponseWriter, r *http.Request) {
	id := chi.URLParam(r, "id")

	var update struct {
		Status     string `json:"status"`
		ResolvedAt string `json:"resolved_at,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&update); err != nil {
		http.Error(w, "Invalid JSON", http.StatusBadRequest)
		return
	}

	for i, action := range api.enforcementActions {
		if action.ID == id {
			api.enforcementActions[i].Status = update.Status
			if update.Status == "resolved" && update.ResolvedAt == "" {
				api.enforcementActions[i].ResolvedAt = time.Now()
			}
			w.Header().Set("Content-Type", "application/json")
			json.NewEncoder(w).Encode(api.enforcementActions[i])
			return
		}
	}

	http.Error(w, "Enforcement action not found", http.StatusNotFound)
}

// GetCircuitBreakerStates returns all circuit breaker states
func (api *AdminAPI) GetCircuitBreakerStates(w http.ResponseWriter, r *http.Request) {
	// This would need access to all circuit breaker states
	// For now, return empty array
	states := make([]CircuitBreakerStockState, 0)

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]interface{}{
		"states": states,
		"total":  len(states),
	})
}

// GetCircuitBreakerState returns circuit breaker state for a specific symbol
func (api *AdminAPI) GetCircuitBreakerState(w http.ResponseWriter, r *http.Request) {
	symbol := chi.URLParam(r, "symbol")

	state := api.circuitBreaker.GetStockState(symbol)
	if state == nil {
		http.Error(w, "Stock not found", http.StatusNotFound)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(state)
}


