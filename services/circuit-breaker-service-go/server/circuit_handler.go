package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/sirupsen/logrus"
)

func parseAlertThresholds(m map[string]interface{}) AlertThresholds {
	thresholds := AlertThresholds{}
	if errorRate, ok := m["error_rate"].(float64); ok {
		thresholds.ErrorRate = errorRate
	}
	if responseTime, ok := m["response_time"].(float64); ok {
		thresholds.ResponseTime = int64(responseTime)
	}
	if consecutiveFailures, ok := m["consecutive_failures"].(float64); ok {
		thresholds.ConsecutiveFailures = int(consecutiveFailures)
	}
	if slowCallRate, ok := m["slow_call_rate"].(float64); ok {
		thresholds.SlowCallRate = slowCallRate
	}
	return thresholds
}

// CreateCircuit OPTIMIZATION: Issue #2202 - Circuit breaker management operations with context timeouts
func (s *CircuitBreakerService) CreateCircuit(w http.ResponseWriter, r *http.Request) {
	var req CreateCircuitRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create circuit request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	circuit := &CircuitBreaker{
		CircuitID:           req.CircuitID,
		ServiceName:         req.ServiceName,
		Endpoint:            req.Endpoint,
		State:               "closed",
		StateChangedAt:      time.Now(),
		FailureCount:        0,
		SuccessCount:        0,
		ConsecutiveFailures: 0,
		Config: CircuitBreakerConfig{
			FailureThreshold:      req.FailureThreshold,
			SuccessThreshold:      req.SuccessThreshold,
			Timeout:               time.Duration(req.Timeout) * time.Millisecond,
			RetryDelay:            time.Duration(req.RetryDelay) * time.Millisecond,
			MaxRetryDelay:         time.Duration(req.MaxRetryDelay) * time.Millisecond,
			MonitoringWindow:      time.Duration(req.MonitoringWindow) * time.Millisecond,
			SlowCallThreshold:     time.Duration(req.SlowCallThreshold) * time.Millisecond,
			SlowCallRateThreshold: req.SlowCallRateThreshold,
			FallbackEnabled:       req.FallbackEnabled,
			FallbackResponse:      req.FallbackResponse,
			MetricsEnabled:        req.MetricsEnabled,
			AlertThresholds:       parseAlertThresholds(req.AlertThresholds),
		},
		StateHistory: []StateChange{},
		Metrics:      CircuitBreakerMetricsData{},
		CreatedAt:    time.Now(),
	}

	s.circuits.Store(circuit.CircuitID, circuit)
	s.metrics.ActiveCircuits.Inc()

	// Persist to Redis
	if err := s.persistCircuitState(circuit); err != nil {
		s.logger.WithError(err).WithField("circuit_id", circuit.CircuitID).Error("failed to persist circuit state")
	}

	resp := &CreateCircuitResponse{
		CircuitID:   circuit.CircuitID,
		ServiceName: circuit.ServiceName,
		Endpoint:    circuit.Endpoint,
		State:       circuit.State,
		CreatedAt:   circuit.CreatedAt.Unix(),
		Config:      &circuit.Config,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("circuit_id", circuit.CircuitID).Info("circuit breaker created successfully")
}

func (s *CircuitBreakerService) ListCircuits(w http.ResponseWriter, r *http.Request) {
	serviceFilter := r.URL.Query().Get("service")
	stateFilter := r.URL.Query().Get("state")

	var circuits []*CircuitBreakerSummary
	s.circuits.Range(func(key, value interface{}) bool {
		circuit := value.(*CircuitBreaker)

		if serviceFilter != "" && circuit.ServiceName != serviceFilter {
			return true
		}
		if stateFilter != "" && circuit.State != stateFilter {
			return true
		}

		summary := &CircuitBreakerSummary{
			CircuitID:       circuit.CircuitID,
			ServiceName:     circuit.ServiceName,
			Endpoint:        circuit.Endpoint,
			State:           circuit.State,
			FailureCount:    circuit.FailureCount,
			SuccessCount:    circuit.SuccessCount,
			ErrorRate:       s.calculateErrorRate(circuit),
			LastFailureTime: circuit.LastFailureTime.Unix(),
			CreatedAt:       circuit.CreatedAt.Unix(),
		}
		circuits = append(circuits, summary)
		return true
	})

	resp := &ListCircuitsResponse{
		Circuits:   circuits,
		TotalCount: len(circuits),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) GetCircuit(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	circuitValue, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	circuit := circuitValue.(*CircuitBreaker)

	details := &CircuitBreakerDetails{
		CircuitID:     circuit.CircuitID,
		ServiceName:   circuit.ServiceName,
		Endpoint:      circuit.Endpoint,
		State:         circuit.State,
		Config:        &circuit.Config,
		Metrics:       &circuit.Metrics,
		StateHistory:  circuit.StateHistory,
		CreatedAt:     circuit.CreatedAt.Unix(),
		LastUpdatedAt: circuit.StateChangedAt.Unix(),
	}

	resp := &GetCircuitResponse{
		Circuit: details,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) UpdateCircuit(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	var req UpdateCircuitRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode update circuit request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	circuitValue, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	circuit := circuitValue.(*CircuitBreaker)

	// Update fields
	if req.FailureThreshold != nil && *req.FailureThreshold > 0 {
		circuit.Config.FailureThreshold = *req.FailureThreshold
	}
	if req.SuccessThreshold != nil && *req.SuccessThreshold > 0 {
		circuit.Config.SuccessThreshold = *req.SuccessThreshold
	}
	if req.Timeout != nil && *req.Timeout > 0 {
		circuit.Config.Timeout = time.Duration(*req.Timeout) * time.Millisecond
	}
	if req.RetryDelay != nil && *req.RetryDelay > 0 {
		circuit.Config.RetryDelay = time.Duration(*req.RetryDelay) * time.Millisecond
	}
	if req.MaxRetryDelay != nil && *req.MaxRetryDelay > 0 {
		circuit.Config.MaxRetryDelay = time.Duration(*req.MaxRetryDelay) * time.Millisecond
	}
	if req.MonitoringWindow != nil && *req.MonitoringWindow > 0 {
		circuit.Config.MonitoringWindow = time.Duration(*req.MonitoringWindow) * time.Millisecond
	}
	if req.SlowCallThreshold != nil && *req.SlowCallThreshold > 0 {
		circuit.Config.SlowCallThreshold = time.Duration(*req.SlowCallThreshold) * time.Millisecond
	}
	if req.SlowCallRateThreshold != nil && *req.SlowCallRateThreshold > 0 {
		circuit.Config.SlowCallRateThreshold = *req.SlowCallRateThreshold
	}
	if req.FallbackEnabled != nil {
		circuit.Config.FallbackEnabled = *req.FallbackEnabled
	}
	if req.FallbackResponse != nil {
		circuit.Config.FallbackResponse = *req.FallbackResponse
	}
	if req.MetricsEnabled != nil {
		circuit.Config.MetricsEnabled = *req.MetricsEnabled
	}
	if req.AlertThresholds != nil {
		if errorRate, ok := (*req.AlertThresholds)["error_rate"].(float64); ok && errorRate > 0 {
			circuit.Config.AlertThresholds.ErrorRate = errorRate
		}
		if slowCallRate, ok := (*req.AlertThresholds)["slow_call_rate"].(float64); ok && slowCallRate > 0 {
			circuit.Config.AlertThresholds.SlowCallRate = slowCallRate
		}
		if consecutiveFailures, ok := (*req.AlertThresholds)["consecutive_failures"].(float64); ok && consecutiveFailures > 0 {
			circuit.Config.AlertThresholds.ConsecutiveFailures = int(consecutiveFailures)
		}
	}

	resp := &UpdateCircuitResponse{
		CircuitID:     circuit.CircuitID,
		UpdatedFields: []string{"config"},
		UpdatedAt:     time.Now().Unix(),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("circuit_id", circuitID).Info("circuit breaker updated successfully")
}

func (s *CircuitBreakerService) DeleteCircuit(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	_, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	s.circuits.Delete(circuitID)
	s.metrics.ActiveCircuits.Dec()

	w.WriteHeader(http.StatusNoContent)

	s.logger.WithField("circuit_id", circuitID).Info("circuit breaker deleted successfully")
}

func (s *CircuitBreakerService) GetCircuitState(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	circuitValue, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	circuit := circuitValue.(*CircuitBreaker)

	resp := &GetCircuitStateResponse{
		CircuitID:      circuit.CircuitID,
		State:          circuit.State,
		StateChangedAt: circuit.StateChangedAt.Unix(),
		Reason:         "current state",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) SetCircuitState(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	var req SetCircuitStateRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode set circuit state request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	circuitValue, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	circuit := circuitValue.(*CircuitBreaker)
	oldState := circuit.State

	circuit.State = req.State
	circuit.StateChangedAt = time.Now()

	// Add to state history
	stateChange := StateChange{
		FromState:   oldState,
		ToState:     req.State,
		ChangedAt:   circuit.StateChangedAt,
		Reason:      req.Reason,
		TriggeredBy: "manual",
	}
	circuit.StateHistory = append(circuit.StateHistory, stateChange)

	s.metrics.CircuitStateChanges.Inc()

	// Persist state
	if err := s.persistCircuitState(circuit); err != nil {
		s.logger.WithError(err).WithField("circuit_id", circuit.CircuitID).Error("failed to persist circuit state")
	}

	resp := &SetCircuitStateResponse{
		CircuitID:     circuit.CircuitID,
		PreviousState: oldState,
		NewState:      req.State,
		ChangedAt:     circuit.StateChangedAt.Unix(),
		ChangedBy:     r.Header.Get("X-User-ID"),
		Reason:        req.Reason,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"circuit_id": circuit.CircuitID,
		"old_state":  oldState,
		"new_state":  req.State,
	}).Info("circuit breaker state changed manually")
}

func (s *CircuitBreakerService) ResetCircuit(w http.ResponseWriter, r *http.Request) {
	circuitID := chi.URLParam(r, "circuitId")

	circuitValue, exists := s.circuits.Load(circuitID)
	if !exists {
		http.Error(w, "Circuit breaker not found", http.StatusNotFound)
		return
	}

	circuit := circuitValue.(*CircuitBreaker)
	oldState := circuit.State

	circuit.State = "closed"
	circuit.StateChangedAt = time.Now()
	circuit.FailureCount = 0
	circuit.SuccessCount = 0
	circuit.ConsecutiveFailures = 0

	// Add to state history
	stateChange := StateChange{
		FromState:   oldState,
		ToState:     "closed",
		ChangedAt:   circuit.StateChangedAt,
		Reason:      "manual reset",
		TriggeredBy: "manual",
	}
	circuit.StateHistory = append(circuit.StateHistory, stateChange)

	s.metrics.CircuitStateChanges.Inc()

	// Persist state
	if err := s.persistCircuitState(circuit); err != nil {
		s.logger.WithError(err).WithField("circuit_id", circuit.CircuitID).Error("failed to persist circuit state")
	}

	resp := &ResetCircuitResponse{
		CircuitID:     circuit.CircuitID,
		ResetAt:       circuit.StateChangedAt.Unix(),
		PreviousState: oldState,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("circuit_id", circuit.CircuitID).Info("circuit breaker reset manually")
}

// ListBulkheads Bulkhead methods (TODO: implement)
func (s *CircuitBreakerService) ListBulkheads(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

func (s *CircuitBreakerService) CreateBulkhead(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

func (s *CircuitBreakerService) GetBulkhead(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

func (s *CircuitBreakerService) DeleteBulkhead(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

// ListTimeouts Timeout methods (TODO: implement)
func (s *CircuitBreakerService) ListTimeouts(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

func (s *CircuitBreakerService) CreateTimeout(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

// CreateDegradationPolicy Degradation policy methods (TODO: implement)
func (s *CircuitBreakerService) CreateDegradationPolicy(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}

// GetMetrics Metrics method (TODO: implement)
func (s *CircuitBreakerService) GetMetrics(w http.ResponseWriter, request *http.Request) {
	http.Error(w, "Not implemented", http.StatusNotImplemented)
}
