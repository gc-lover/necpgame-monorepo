package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) createReport(w http.ResponseWriter, r *http.Request) {
	var req models.CreateReportRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Reason == "" {
		s.respondError(w, http.StatusBadRequest, "reason is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	reporterID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	report, err := s.socialService.CreateReport(r.Context(), reporterID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create report")
		s.respondError(w, http.StatusInternalServerError, "failed to create report")
		return
	}

	s.respondJSON(w, http.StatusCreated, report)
}

func (s *HTTPServer) createBan(w http.ResponseWriter, r *http.Request) {
	var req models.CreateBanRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Reason == "" {
		s.respondError(w, http.StatusBadRequest, "reason is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	adminID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	ban, err := s.socialService.CreateBan(r.Context(), adminID, &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create ban")
		s.respondError(w, http.StatusInternalServerError, "failed to create ban")
		return
	}

	s.respondJSON(w, http.StatusCreated, ban)
}

func (s *HTTPServer) getBans(w http.ResponseWriter, r *http.Request) {
	characterIDStr := r.URL.Query().Get("character_id")
	var characterID *uuid.UUID
	if characterIDStr != "" {
		id, err := uuid.Parse(characterIDStr)
		if err != nil {
			s.respondError(w, http.StatusBadRequest, "invalid character id")
			return
		}
		characterID = &id
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	response, err := s.socialService.GetBans(r.Context(), characterID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get bans")
		s.respondError(w, http.StatusInternalServerError, "failed to get bans")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) removeBan(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	banID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid ban id")
		return
	}

	err = s.socialService.RemoveBan(r.Context(), banID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove ban")
		s.respondError(w, http.StatusInternalServerError, "failed to remove ban")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getReports(w http.ResponseWriter, r *http.Request) {
	statusStr := r.URL.Query().Get("status")
	var status *string
	if statusStr != "" {
		status = &statusStr
	}

	limit := 50
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if parsedLimit, err := strconv.Atoi(limitStr); err == nil && parsedLimit > 0 && parsedLimit <= 100 {
			limit = parsedLimit
		}
	}

	offset := 0
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if parsedOffset, err := strconv.Atoi(offsetStr); err == nil && parsedOffset >= 0 {
			offset = parsedOffset
		}
	}

	reports, total, err := s.socialService.GetReports(r.Context(), status, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get reports")
		s.respondError(w, http.StatusInternalServerError, "failed to get reports")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]interface{}{
		"reports": reports,
		"total":   total,
	})
}

func (s *HTTPServer) resolveReport(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	idStr := vars["id"]

	reportID, err := uuid.Parse(idStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid report id")
		return
	}

	var req struct {
		Status string `json:"status"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Status == "" {
		s.respondError(w, http.StatusBadRequest, "status is required")
		return
	}

	userID := r.Context().Value("user_id")
	if userID == nil {
		s.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	adminID, err := uuid.Parse(userID.(string))
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	err = s.socialService.ResolveReport(r.Context(), reportID, adminID, req.Status)
	if err != nil {
		s.logger.WithError(err).Error("Failed to resolve report")
		s.respondError(w, http.StatusInternalServerError, "failed to resolve report")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

