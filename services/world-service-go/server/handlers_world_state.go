// Issue: #140876058
package server

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
)

func (s *HTTPServer) getStateByKey(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	if key == "" {
		s.respondError(w, http.StatusBadRequest, "key is required")
		return
	}

	state, err := s.worldStateService.GetStateByKey(r.Context(), key)
	if err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to get state by key")
		s.respondError(w, http.StatusInternalServerError, "failed to get state by key")
		return
	}

	if state == nil {
		s.respondError(w, http.StatusNotFound, "state not found")
		return
	}

	s.respondJSON(w, http.StatusOK, state)
}

func (s *HTTPServer) getStateByCategory(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	category := vars["category"]

	if category == "" {
		s.respondError(w, http.StatusBadRequest, "category is required")
		return
	}

	states, err := s.worldStateService.GetStateByCategory(r.Context(), category)
	if err != nil {
		s.logger.WithError(err).WithField("category", category).Error("Failed to get state by category")
		s.respondError(w, http.StatusInternalServerError, "failed to get state by category")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"states": states,
	})
}

func (s *HTTPServer) updateState(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	if key == "" {
		s.respondError(w, http.StatusBadRequest, "key is required")
		return
	}

	var req struct {
		Value    map[string]interface{} `json:"value"`
		Version  *int                   `json:"version,omitempty"`
		SyncType *string                `json:"sync_type,omitempty"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Value == nil {
		s.respondError(w, http.StatusBadRequest, "value is required")
		return
	}

	state, err := s.worldStateService.UpdateState(r.Context(), key, req.Value, req.Version, req.SyncType)
	if err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to update state")
		if err.Error() == "sql: no rows in result set" {
			s.respondError(w, http.StatusNotFound, "state not found")
			return
		}
		s.respondError(w, http.StatusInternalServerError, "failed to update state")
		return
	}

	s.respondJSON(w, http.StatusOK, state)
}

func (s *HTTPServer) deleteState(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	key := vars["key"]

	if key == "" {
		s.respondError(w, http.StatusBadRequest, "key is required")
		return
	}

	err := s.worldStateService.DeleteState(r.Context(), key)
	if err != nil {
		s.logger.WithError(err).WithField("key", key).Error("Failed to delete state")
		s.respondError(w, http.StatusInternalServerError, "failed to delete state")
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

func (s *HTTPServer) batchUpdateState(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Updates []struct {
			Key      string                 `json:"key"`
			Value    map[string]interface{} `json:"value"`
			Version  *int                   `json:"version,omitempty"`
			SyncType *string                `json:"sync_type,omitempty"`
		} `json:"updates"`
	}

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if len(req.Updates) == 0 {
		s.respondError(w, http.StatusBadRequest, "updates are required")
		return
	}

	updates := make([]StateUpdate, len(req.Updates))
	for i, update := range req.Updates {
		updates[i] = StateUpdate{
			Key:      update.Key,
			Value:    update.Value,
			Version:  update.Version,
			SyncType: update.SyncType,
		}
	}

	states, err := s.worldStateService.BatchUpdateState(r.Context(), updates)
	if err != nil {
		s.logger.WithError(err).Error("Failed to batch update state")
		s.respondError(w, http.StatusInternalServerError, "failed to batch update state")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"states": states,
	})
}

