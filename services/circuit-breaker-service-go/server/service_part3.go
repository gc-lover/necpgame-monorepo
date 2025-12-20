package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

func (s *CircuitBreakerService) ListDegradationPolicies(w http.ResponseWriter, r *http.Request) {
	policies := []*DegradationPolicySummary{}

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
