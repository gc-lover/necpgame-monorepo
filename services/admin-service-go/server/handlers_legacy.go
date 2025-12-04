// Issue: #141888646
package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/necpgame/admin-service-go/models"
)

func (s *HTTPServer) banPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.BanPlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.BanPlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to ban player")
		s.respondError(w, http.StatusInternalServerError, "failed to ban player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) kickPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.KickPlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.KickPlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to kick player")
		s.respondError(w, http.StatusInternalServerError, "failed to kick player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) mutePlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.MutePlayerRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.MutePlayer(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to mute player")
		s.respondError(w, http.StatusInternalServerError, "failed to mute player")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) unbanPlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		Reason      string    `json:"reason"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
	}

	err = s.adminService.LogAction(r.Context(), adminID, models.AdminActionTypeUnban, &req.CharacterID, "character", details, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to log unban action")
		s.respondError(w, http.StatusInternalServerError, "failed to unban player")
		return
	}

	RecordAdminAction(string(models.AdminActionTypeUnban))

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) unmutePlayer(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req struct {
		CharacterID uuid.UUID `json:"character_id"`
		Reason      string    `json:"reason"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	details := map[string]interface{}{
		"character_id": req.CharacterID.String(),
		"reason":       req.Reason,
	}

	err = s.adminService.LogAction(r.Context(), adminID, models.AdminActionTypeUnmute, &req.CharacterID, "character", details, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to log unmute action")
		s.respondError(w, http.StatusInternalServerError, "failed to unmute player")
		return
	}

	RecordAdminAction(string(models.AdminActionTypeUnmute))

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) searchPlayers(w http.ResponseWriter, r *http.Request) {
	var req models.SearchPlayersRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SearchPlayers(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to search players")
		s.respondError(w, http.StatusInternalServerError, "failed to search players")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) giveItem(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.GiveItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.GiveItem(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to give item")
		s.respondError(w, http.StatusInternalServerError, "failed to give item")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) removeItem(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.RemoveItemRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.RemoveItem(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to remove item")
		s.respondError(w, http.StatusInternalServerError, "failed to remove item")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) setCurrency(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.SetCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SetCurrency(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to set currency")
		s.respondError(w, http.StatusInternalServerError, "failed to set currency")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) addCurrency(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.AddCurrencyRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.AddCurrency(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to add currency")
		s.respondError(w, http.StatusInternalServerError, "failed to add currency")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) setWorldFlag(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.SetWorldFlagRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.SetWorldFlag(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to set world flag")
		s.respondError(w, http.StatusInternalServerError, "failed to set world flag")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) createEvent(w http.ResponseWriter, r *http.Request) {
	adminID, err := s.getAdminID(r)
	if err != nil {
		s.respondError(w, http.StatusUnauthorized, "invalid admin credentials")
		return
	}

	var req models.CreateEventRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response, err := s.adminService.CreateEvent(r.Context(), adminID, &req, s.getIPAddress(r), r.UserAgent())
	if err != nil {
		s.logger.WithError(err).Error("Failed to create event")
		s.respondError(w, http.StatusInternalServerError, "failed to create event")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAnalytics(w http.ResponseWriter, r *http.Request) {
	response, err := s.adminService.GetAnalytics(r.Context())
	if err != nil {
		s.logger.WithError(err).Error("Failed to get analytics")
		s.respondError(w, http.StatusInternalServerError, "failed to get analytics")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAuditLogs(w http.ResponseWriter, r *http.Request) {
	var adminID *uuid.UUID
	if adminIDStr := r.URL.Query().Get("admin_id"); adminIDStr != "" {
		if id, err := uuid.Parse(adminIDStr); err == nil {
			adminID = &id
		}
	}

	var actionType *models.AdminActionType
	if actionTypeStr := r.URL.Query().Get("action_type"); actionTypeStr != "" {
		at := models.AdminActionType(actionTypeStr)
		actionType = &at
	}

	limit := 50
	offset := 0
	if limitStr := r.URL.Query().Get("limit"); limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}
	if offsetStr := r.URL.Query().Get("offset"); offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	response, err := s.adminService.GetAuditLogs(r.Context(), adminID, actionType, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get audit logs")
		s.respondError(w, http.StatusInternalServerError, "failed to get audit logs")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getAuditLog(w http.ResponseWriter, r *http.Request) {
	logIDStr := chi.URLParam(r, "log_id")
	logID, err := uuid.Parse(logIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid log_id")
		return
	}

	log, err := s.adminService.GetAuditLog(r.Context(), logID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get audit log")
		s.respondError(w, http.StatusInternalServerError, "failed to get audit log")
		return
	}

	if log == nil {
		s.respondError(w, http.StatusNotFound, "audit log not found")
		return
	}

	s.respondJSON(w, http.StatusOK, log)
}

func (s *HTTPServer) healthCheck(w http.ResponseWriter, r *http.Request) {
	s.respondJSON(w, http.StatusOK, map[string]string{"status": "healthy"})
}

