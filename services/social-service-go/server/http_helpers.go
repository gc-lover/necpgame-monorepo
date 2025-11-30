package server

import (
	"encoding/json"
	"errors"
	"net/http"

	"github.com/google/uuid"
)

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

func (s *HTTPServer) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (s *HTTPServer) respondError(w http.ResponseWriter, status int, message string) {
	s.respondJSON(w, status, map[string]string{"error": message})
}

func (s *HTTPServer) getCharacterIDFromRequest(r *http.Request) (uuid.UUID, error) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		return uuid.Nil, errors.New("user not authenticated")
	}

	characterID, err := uuid.Parse(userID.(string))
	if err != nil {
		return uuid.Nil, errors.New("invalid user id")
	}

	return characterID, nil
}

