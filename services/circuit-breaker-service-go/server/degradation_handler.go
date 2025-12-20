package server

import (
	"encoding/json"
	"net/http"
	"time"
)

// OPTIMIZATION: Issue #2156 - Graceful degradation policy management
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

		summary := &DegradationPolicySummary{
			PolicyID:         policy.PolicyID,
			ServiceName:      policy.ServiceName,
			Status:           policy.Status,
			TriggerCount:     policy.TriggerCount,
			RecoveryCount:    policy.RecoveryCount,
			LastTriggeredAt:  policy.LastTriggeredAt.Unix(),
			CreatedAt:        policy.CreatedAt.Unix(),
		}
		policies = append(policies, summary)
		return true
	})

	resp := &ListDegradationPoliciesResponse{
		Policies:    policies,
		TotalCount:  len(policies),
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)
}
