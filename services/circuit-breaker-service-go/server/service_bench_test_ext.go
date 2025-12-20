
	// Test state transitions: closed -> open -> half_open -> closed
	states := []string{"open", "half_open", "closed"}

	for _, targetState := range states {
		stateReq := SetCircuitStateRequest{
			State:   targetState,
			Reason:  fmt.Sprintf("testing transition to %s", targetState),
		}
		stateBody, _ := json.Marshal(stateReq)
		stateHttpReq := httptest.NewRequest("POST", "/cb/circuits/"+circuitID+"/state", bytes.NewReader(stateBody))
		stateHttpReq.Header.Set("X-User-ID", "user_123")
		stateW := httptest.NewRecorder()

		service.SetCircuitState(stateW, stateHttpReq)

		if stateW.Code != http.StatusOK {
			t.Errorf("Failed to transition to %s: %d", targetState, stateW.Code)
		}

		// Verify state
		getHttpReq := httptest.NewRequest("GET", "/cb/circuits/"+circuitID+"/state", nil)
		getHttpReq.Header.Set("X-User-ID", "user_123")
		getW := httptest.NewRecorder()

		service.GetCircuitState(getW, getHttpReq)

		if getW.Code != http.StatusOK {
			t.Errorf("Failed to get circuit state: %d", getW.Code)
		}
	}

	// Test reset
	resetHttpReq := httptest.NewRequest("POST", "/cb/circuits/"+circuitID+"/reset", nil)
	resetHttpReq.Header.Set("X-User-ID", "user_123")
	resetW := httptest.NewRecorder()

	service.ResetCircuit(resetW, resetHttpReq)

	if resetW.Code != http.StatusOK {
		t.Errorf("Failed to reset circuit: %d", resetW.Code)
	}

	t.Log("Circuit breaker state transitions test passed")
}

// Test bulkhead isolation
func TestCircuitBreakerService_BulkheadIsolation(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	bulkheadID := "isolation_test_bulkhead"

	// Create bulkhead
	createReq := CreateBulkheadRequest{
		BulkheadID:         bulkheadID,
		ServiceName:        "isolation_test_service",
		MaxConcurrentCalls: 5,
		MaxWaitDuration:    1000,
		IsolationStrategy:  "semaphore",
		QueueSize:          10,
		Fairness:           true,
		MetricsEnabled:     true,
	}
	createBody, _ := json.Marshal(createReq)
	createHttpReq := httptest.NewRequest("POST", "/cb/bulkheads", bytes.NewReader(createBody))
	createHttpReq.Header.Set("X-User-ID", "user_123")
	createW := httptest.NewRecorder()

	service.CreateBulkhead(createW, createHttpReq)

	if createW.Code != http.StatusCreated {
		t.Fatalf("Failed to create bulkhead: %d", createW.Code)
	}

	// Get bulkhead
	getHttpReq := httptest.NewRequest("GET", "/cb/bulkheads/"+bulkheadID, nil)
	getHttpReq.Header.Set("X-User-ID", "user_123")
	getW := httptest.NewRecorder()

	service.GetBulkhead(getW, getHttpReq)

	if getW.Code != http.StatusOK {
		t.Errorf("Failed to get bulkhead: %d", getW.Code)
	}

	// Delete bulkhead
	deleteHttpReq := httptest.NewRequest("DELETE", "/cb/bulkheads/"+bulkheadID, nil)
	deleteHttpReq.Header.Set("X-User-ID", "user_123")
	deleteW := httptest.NewRecorder()

	service.DeleteBulkhead(deleteW, deleteHttpReq)

	if deleteW.Code != http.StatusNoContent {
		t.Errorf("Failed to delete bulkhead: %d", deleteW.Code)
	}

	t.Log("Bulkhead isolation test passed")
}

// Test degradation policies
func TestCircuitBreakerService_DegradationPolicies(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	policyID := "degradation_test_policy"

	// Create degradation policy
	createReq := CreateDegradationPolicyRequest{
		PolicyID:   policyID,
		ServiceName: "degradation_test_service",
		TriggerConditions: []DegradationCondition{
			{
				Metric:    "error_rate",
				Operator:  "gt",
				Threshold: 0.5,
				Duration:  time.Duration(60000) * time.Millisecond,
			},
		},
		DegradationActions: []DegradationAction{
			{
				ActionType: "reduce_features",
				Priority:   1,
				Parameters: map[string]interface{}{
					"disable_feature": "advanced_search",
				},
			},
		},
		RecoveryConditions: []RecoveryCondition{
			{
				Metric:    "error_rate",
				Operator:  "lt",
				Threshold: 0.1,
				Duration:  time.Duration(300000) * time.Millisecond,
			},
		},
		Enabled:            true,
		MonitoringEnabled:  true,
	}
	createBody, _ := json.Marshal(createReq)
	createHttpReq := httptest.NewRequest("POST", "/cb/degradation", bytes.NewReader(createBody))
	createHttpReq.Header.Set("X-User-ID", "user_123")
	createW := httptest.NewRecorder()

	service.CreateDegradationPolicy(createW, createHttpReq)

	if createW.Code != http.StatusCreated {
		t.Fatalf("Failed to create degradation policy: %d", createW.Code)
	}

	// List degradation policies
	listHttpReq := httptest.NewRequest("GET", "/cb/degradation", nil)
	listHttpReq.Header.Set("X-User-ID", "user_123")
	listW := httptest.NewRecorder()

	service.ListDegradationPolicies(listW, listHttpReq)

	if listW.Code != http.StatusOK {
		t.Errorf("Failed to list degradation policies: %d", listW.Code)
	}

	t.Log("Degradation policies test passed")
}

// Test timeout configurations
func TestCircuitBreakerService_TimeoutConfigurations(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	timeoutID := "timeout_test_config"

	// Create timeout configuration
	createReq := CreateTimeoutRequest{
		TimeoutID:       timeoutID,
		ServiceName:     "timeout_test_service",
		Endpoint:        "/api/timeout_test",
		TimeoutDuration: 3000,
		CancelRunningCall: true,
		TimeoutStrategy: "fixed",
		MetricsEnabled:  true,
	}
	createBody, _ := json.Marshal(createReq)
	createHttpReq := httptest.NewRequest("POST", "/cb/timeouts", bytes.NewReader(createBody))
	createHttpReq.Header.Set("X-User-ID", "user_123")
	createW := httptest.NewRecorder()

	service.CreateTimeout(createW, createHttpReq)

	if createW.Code != http.StatusCreated {
		t.Fatalf("Failed to create timeout configuration: %d", createW.Code)
	}

	// List timeout configurations
	listHttpReq := httptest.NewRequest("GET", "/cb/timeouts", nil)
	listHttpReq.Header.Set("X-User-ID", "user_123")
	listW := httptest.NewRecorder()

	service.ListTimeouts(listW, listHttpReq)

	if listW.Code != http.StatusOK {
		t.Errorf("Failed to list timeout configurations: %d", listW.Code)
	}

	t.Log("Timeout configurations test passed")
}

// Test error handling
func TestCircuitBreakerService_ErrorHandling(t *testing.T) {
	logger := logrus.New()
	logger.SetLevel(logrus.ErrorLevel)

	metrics := &CircuitBreakerMetrics{}
	config := &CircuitBreakerServiceConfig{}
	service := NewCircuitBreakerService(logger, metrics, config)

	// Test invalid circuit creation
	invalidReq := CreateCircuitRequest{
		CircuitID:   "",
		ServiceName: "test_service",
		Endpoint:    "/api/test",
	}
	invalidBody, _ := json.Marshal(invalidReq)
	invalidHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(invalidBody))
	invalidHttpReq.Header.Set("X-User-ID", "user_123")
	invalidW := httptest.NewRecorder()

	service.CreateCircuit(invalidW, invalidHttpReq)

	if invalidW.Code != http.StatusBadRequest {
		t.Errorf("Expected BadRequest for invalid circuit ID, got %d", invalidW.Code)
	}

	// Test unauthorized access
	noAuthReq := CreateCircuitRequest{
		CircuitID:   "test_circuit",
		ServiceName: "test_service",
		Endpoint:    "/api/test",
	}
	noAuthBody, _ := json.Marshal(noAuthReq)
	noAuthHttpReq := httptest.NewRequest("POST", "/cb/circuits", bytes.NewReader(noAuthBody))
	// No X-User-ID header
	noAuthW := httptest.NewRecorder()

	service.CreateCircuit(noAuthW, noAuthHttpReq)

	if noAuthW.Code != http.StatusUnauthorized {
		t.Errorf("Expected Unauthorized for missing auth, got %d", noAuthW.Code)
	}

	// Test not found circuit
	notFoundHttpReq := httptest.NewRequest("GET", "/cb/circuits/nonexistent_circuit", nil)
	notFoundHttpReq.Header.Set("X-User-ID", "user_123")
	notFoundW := httptest.NewRecorder()

	service.GetCircuit(notFoundW, notFoundHttpReq)

	if notFoundW.Code != http.StatusNotFound {
		t.Errorf("Expected NotFound for nonexistent circuit, got %d", notFoundW.Code)
	}

	t.Log("Error handling test passed")
}
