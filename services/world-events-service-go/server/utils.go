// Package server Issue: #2224 - Utility functions for world events service
package server

import (
	"encoding/json"
	"net/http"

	"go.uber.org/zap"
)

// respondJSON sends a JSON response
func (s *WorldEventsService) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)

	if err := json.NewEncoder(w).Encode(data); err != nil {
		s.logger.Error("Failed to encode JSON response", zap.Error(err))
		http.Error(w, "Internal server error", http.StatusInternalServerError)
	}
}

// respondError sends an error response
func (s *WorldEventsService) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}
