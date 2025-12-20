package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

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
