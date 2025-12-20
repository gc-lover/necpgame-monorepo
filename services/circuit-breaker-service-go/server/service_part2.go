
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
		CircuitID:   circuit.CircuitID,
		PreviousState: oldState,
		NewState:    req.State,
		ChangedAt:   circuit.StateChangedAt.Unix(),
		ChangedBy:   r.Header.Get("X-User-ID"),
		Reason:      req.Reason,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"circuit_id":   circuit.CircuitID,
		"old_state":    oldState,
		"new_state":    req.State,
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
		NewState:      "closed",
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("circuit_id", circuit.CircuitID).Info("circuit breaker reset manually")
}

// Bulkhead management methods
func (s *CircuitBreakerService) CreateBulkhead(w http.ResponseWriter, r *http.Request) {
	var req CreateBulkheadRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create bulkhead request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	bulkhead := &Bulkhead{
		BulkheadID:           req.BulkheadID,
		ServiceName:          req.ServiceName,
		Config: BulkheadConfig{
			MaxConcurrentCalls: req.MaxConcurrentCalls,
			MaxWaitDuration:    time.Duration(req.MaxWaitDuration) * time.Millisecond,
			IsolationStrategy:  req.IsolationStrategy,
			ThreadPoolSize:     req.ThreadPoolSize,
			QueueSize:          req.QueueSize,
			Fairness:           req.Fairness,
			MetricsEnabled:     req.MetricsEnabled,
		},
		ActiveThreads:       0,
		QueuedRequests:      0,
		RejectedRequests:    0,
		CompletedRequests:   0,
		AverageExecutionTime: 0,
		MaxExecutionTime:    0,
		Metrics:             BulkheadMetricsData{},
		CreatedAt:          time.Now(),
	}

	s.bulkheads.Store(bulkhead.BulkheadID, bulkhead)
	s.metrics.ActiveBulkheads.Inc()

	resp := &CreateBulkheadResponse{
		BulkheadID:  bulkhead.BulkheadID,
		ServiceName: bulkhead.ServiceName,
		CreatedAt:   bulkhead.CreatedAt.Unix(),
		Config:      &bulkhead.Config,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("bulkhead_id", bulkhead.BulkheadID).Info("bulkhead created successfully")
}

func (s *CircuitBreakerService) ListBulkheads(w http.ResponseWriter, r *http.Request) {
	serviceFilter := r.URL.Query().Get("service")

	var bulkheads []*BulkheadSummary
	s.bulkheads.Range(func(key, value interface{}) bool {
		bulkhead := value.(*Bulkhead)

		if serviceFilter != "" && bulkhead.ServiceName != serviceFilter {
			return true
		}

		summary := &BulkheadSummary{
			BulkheadID:       bulkhead.BulkheadID,
			ServiceName:      bulkhead.ServiceName,
			ActiveThreads:    bulkhead.ActiveThreads,
			QueuedRequests:   bulkhead.QueuedRequests,
			RejectedRequests: bulkhead.RejectedRequests,
			CreatedAt:        bulkhead.CreatedAt.Unix(),
		}
		bulkheads = append(bulkheads, summary)
		return true
	})

	resp := &ListBulkheadsResponse{
		Bulkheads:   bulkheads,
		TotalCount:  len(bulkheads),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) GetBulkhead(w http.ResponseWriter, r *http.Request) {
	bulkheadID := chi.URLParam(r, "bulkheadId")

	bulkheadValue, exists := s.bulkheads.Load(bulkheadID)
	if !exists {
		http.Error(w, "Bulkhead not found", http.StatusNotFound)
		return
	}

	bulkhead := bulkheadValue.(*Bulkhead)

	details := &BulkheadDetails{
		BulkheadID: bulkhead.BulkheadID,
		ServiceName: bulkhead.ServiceName,
		Config:      &bulkhead.Config,
		Metrics:     &bulkhead.Metrics,
		CreatedAt:   bulkhead.CreatedAt.Unix(),
	}

	resp := &GetBulkheadResponse{
		Bulkhead: details,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

func (s *CircuitBreakerService) DeleteBulkhead(w http.ResponseWriter, r *http.Request) {
	bulkheadID := chi.URLParam(r, "bulkheadId")

	_, exists := s.bulkheads.Load(bulkheadID)
	if !exists {
		http.Error(w, "Bulkhead not found", http.StatusNotFound)
		return
	}

	s.bulkheads.Delete(bulkheadID)
	s.metrics.ActiveBulkheads.Dec()

	w.WriteHeader(http.StatusNoContent)

	s.logger.WithField("bulkhead_id", bulkheadID).Info("bulkhead deleted successfully")
}

// Timeout management methods
func (s *CircuitBreakerService) CreateTimeout(w http.ResponseWriter, r *http.Request) {
	var req CreateTimeoutRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create timeout request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	timeout := &TimeoutConfig{
		TimeoutID:          req.TimeoutID,
		ServiceName:        req.ServiceName,
		Endpoint:           req.Endpoint,
		TimeoutDuration:    time.Duration(req.TimeoutDuration) * time.Millisecond,
		CancelRunningCall:  req.CancelRunningCall,
		TimeoutStrategy:    req.TimeoutStrategy,
		AdaptiveConfig: AdaptiveTimeoutConfig{
			MinTimeout:     time.Duration(req.AdaptiveTimeoutConfig.MinTimeout) * time.Millisecond,
			MaxTimeout:     time.Duration(req.AdaptiveTimeoutConfig.MaxTimeout) * time.Millisecond,
			IncreaseFactor: req.AdaptiveTimeoutConfig.IncreaseFactor,
			DecreaseFactor: req.AdaptiveTimeoutConfig.DecreaseFactor,
		},
		TimeoutsTriggered:   0,
		AverageResponseTime: 0,
		MetricsEnabled:      req.MetricsEnabled,
		CreatedAt:          time.Now(),
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
			TimeoutID:       timeout.TimeoutID,
			ServiceName:     timeout.ServiceName,
			Endpoint:        timeout.Endpoint,
			TimeoutDuration: int(timeout.TimeoutDuration.Milliseconds()),
			TimeoutStrategy: timeout.TimeoutStrategy,
			TimeoutsTriggered: timeout.TimeoutsTriggered,
			CreatedAt:       timeout.CreatedAt.Unix(),
		}
		timeouts = append(timeouts, summary)
		return true
	})

	resp := &ListTimeoutsResponse{
		Timeouts:    timeouts,
		TotalCount:  len(timeouts),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}

// Degradation policy methods
func (s *CircuitBreakerService) CreateDegradationPolicy(w http.ResponseWriter, r *http.Request) {
	var req CreateDegradationPolicyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.logger.WithError(err).Error("failed to decode create degradation policy request")
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	policy := &DegradationPolicy{
		PolicyID:     req.PolicyID,
		ServiceName:  req.ServiceName,
		Status:       "active",
		TriggerConditions: make([]DegradationCondition, len(req.TriggerConditions)),
		DegradationActions: make([]DegradationAction, len(req.DegradationActions)),
		RecoveryConditions: make([]RecoveryCondition, len(req.RecoveryConditions)),
		TriggerCount:       0,
		RecoveryCount:      0,
		Enabled:            req.Enabled,
		MonitoringEnabled:  req.MonitoringEnabled,
		CreatedAt:          time.Now(),
	}

	// Convert conditions
	for i, cond := range req.TriggerConditions {
		policy.TriggerConditions[i] = DegradationCondition{
			Metric:    cond.Metric,
			Operator:  cond.Operator,
			Threshold: cond.Threshold,
			Duration:  time.Duration(cond.Duration) * time.Millisecond,
		}
	}

	for i, action := range req.DegradationActions {
		policy.DegradationActions[i] = DegradationAction{
			ActionType: action.ActionType,
			Priority:   action.Priority,
			Parameters: action.Parameters,
		}
	}

	for i, cond := range req.RecoveryConditions {
		policy.RecoveryConditions[i] = RecoveryCondition{
			Metric:    cond.Metric,
			Operator:  cond.Operator,
			Threshold: cond.Threshold,
			Duration:  time.Duration(cond.Duration) * time.Millisecond,
		}
	}

	s.degradationPolicies.Store(policy.PolicyID, policy)

	resp := &CreateDegradationPolicyResponse{
		PolicyID:    policy.PolicyID,
		ServiceName: policy.ServiceName,
		CreatedAt:   policy.CreatedAt.Unix(),
		Status:      policy.Status,
		Config: &DegradationPolicyConfig{
			TriggerConditions: req.TriggerConditions,
			DegradationActions: req.DegradationActions,
			RecoveryConditions: req.RecoveryConditions,
			Enabled:            policy.Enabled,
			MonitoringEnabled:  policy.MonitoringEnabled,
		},
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(resp)

	s.logger.WithField("policy_id", policy.PolicyID).Info("degradation policy created successfully")
}

func (s *CircuitBreakerService) ListDegradationPolicies(w http.ResponseWriter, r *http.Request) {
	var policies []*DegradationPolicySummary
	s.degradationPolicies.Range(func(key, value interface{}) bool {
		policy := value.(*DegradationPolicy)

