package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) createNotification(w http.ResponseWriter, r *http.Request) {
	var req models.CreateNotificationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.AccountID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "account_id is required")
		return
	}

	if req.Title == "" {
		s.respondError(w, http.StatusBadRequest, "title is required")
		return
	}

	notification, err := s.socialService.CreateNotification(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to create notification")
		s.respondError(w, http.StatusInternalServerError, "failed to create notification")
		return
	}

	s.respondJSON(w, http.StatusCreated, notification)
}

func (s *HTTPServer) getNotifications(w http.ResponseWriter, r *http.Request) {
	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "account_id query parameter is required")
		return
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account id")
		return
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

	response, err := s.socialService.GetNotifications(r.Context(), accountID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notifications")
		s.respondError(w, http.StatusInternalServerError, "failed to get notifications")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getNotification(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	notification, err := s.socialService.GetNotification(r.Context(), notificationID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notification")
		s.respondError(w, http.StatusInternalServerError, "failed to get notification")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
}

func (s *HTTPServer) updateNotificationStatus(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	notificationIDStr := vars["id"]

	notificationID, err := uuid.Parse(notificationIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid notification id")
		return
	}

	var req models.UpdateNotificationStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	notification, err := s.socialService.UpdateNotificationStatus(r.Context(), notificationID, req.Status)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update notification status")
		s.respondError(w, http.StatusInternalServerError, "failed to update notification status")
		return
	}

	if notification == nil {
		s.respondError(w, http.StatusNotFound, "notification not found")
		return
	}

	s.respondJSON(w, http.StatusOK, notification)
}

func (s *HTTPServer) getNotificationPreferences(w http.ResponseWriter, r *http.Request) {
	accountIDStr := r.URL.Query().Get("account_id")
	if accountIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "account_id is required")
		return
	}

	accountID, err := uuid.Parse(accountIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid account_id")
		return
	}

	prefs, err := s.socialService.GetNotificationPreferences(r.Context(), accountID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get notification preferences")
		s.respondError(w, http.StatusInternalServerError, "failed to get notification preferences")
		return
	}

	s.respondJSON(w, http.StatusOK, prefs)
}

func (s *HTTPServer) updateNotificationPreferences(w http.ResponseWriter, r *http.Request) {
	var prefs models.NotificationPreferences
	if err := json.NewDecoder(r.Body).Decode(&prefs); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if prefs.AccountID == uuid.Nil {
		s.respondError(w, http.StatusBadRequest, "account_id is required")
		return
	}

	err := s.socialService.UpdateNotificationPreferences(r.Context(), &prefs)
	if err != nil {
		s.logger.WithError(err).Error("Failed to update notification preferences")
		s.respondError(w, http.StatusInternalServerError, "failed to update notification preferences")
		return
	}

	updatedPrefs, err := s.socialService.GetNotificationPreferences(r.Context(), prefs.AccountID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get updated notification preferences")
		s.respondError(w, http.StatusInternalServerError, "failed to get updated notification preferences")
		return
	}

	s.respondJSON(w, http.StatusOK, updatedPrefs)
}

