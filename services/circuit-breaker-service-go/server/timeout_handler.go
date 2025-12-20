package server

import (
	"encoding/json"
	"net/http"
	"time"
)

// OPTIMIZATION: Issue #2156 - Timeout management operations
func (s *CircuitBreakerService) CreateTimeout(w http.ResponseWriter, r *http.Request) {
	var req CreateTimeoutRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create timeout request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	timeout := &TimeoutConfig{
		TimeoutID:         req.TimeoutID,
		ServiceName:       req.ServiceName,
		Endpoint:          req.Endpoint,
		TimeoutDuration:   time.Duration(req.TimeoutDuration) * time.Millisecond,
		CancelRunningCall: req.CancelRunningCall,
		TimeoutStrategy:   req.TimeoutStrategy,
		AdaptiveConfig: AdaptiveTimeoutConfig{
			MinTimeout:     time.Duration(req.AdaptiveTimeoutConfig.MinTimeout) * time.Millisecond,
			MaxTimeout:     time.Duration(req.AdaptiveTimeoutConfig.MaxTimeout) * time.Millisecond,
			IncreaseFactor: req.AdaptiveTimeoutConfig.IncreaseFactor,
			DecreaseFactor: req.AdaptiveTimeoutConfig.DecreaseFactor,
		},
		TimeoutsTriggered:   0,
		AverageResponseTime: 0,
		MetricsEnabled:      req.MetricsEnabled,
		CreatedAt:           time.Now(),
	}

	s.timeouts.Store(timeout.TimeoutID, timeout)

	resp := &CreateTimeoutResponse{
		TimeoutID:   timeout.TimeoutID,
		ServiceName: timeout.ServiceName,
		Endpoint:    timeout.Endpoint,
		CreatedAt:   timeout.CreatedAt.Unix(),
		Config:      timeout,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("timeout_id", timeout.TimeoutID).Info("timeout configuration created successfully")
}

func (s *CircuitBreakerService) ListTimeouts(w http.ResponseWriter, r *http.Request) {
	serviceFilter := r.URL.Query().Get("service")

	var timeouts []*TimeoutSummary
	s.timeouts.Range(func(key, value interface{}) bool {
		timeout := value.(*TimeoutConfig)

		if serviceFilter != "" && timeout.ServiceName != serviceFilter {
			return true
		}

		summary := &TimeoutSummary{
			TimeoutID:         timeout.TimeoutID,
			ServiceName:       timeout.ServiceName,
			Endpoint:          timeout.Endpoint,
			TimeoutDuration:   int(timeout.TimeoutDuration.Milliseconds()),
			TimeoutStrategy:   timeout.TimeoutStrategy,
			TimeoutsTriggered: timeout.TimeoutsTriggered,
			CreatedAt:         timeout.CreatedAt.Unix(),
		}
		timeouts = append(timeouts, summary)
		return true
	})

	resp := &ListTimeoutsResponse{
		Timeouts:   timeouts,
		TotalCount: len(timeouts),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}
