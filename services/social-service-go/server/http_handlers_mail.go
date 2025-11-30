package server

import (
	"encoding/json"
	"net/http"
	"strconv"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/necpgame/social-service-go/models"
)

func (s *HTTPServer) sendMail(w http.ResponseWriter, r *http.Request) {
	var req models.CreateMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Subject == "" {
		s.respondError(w, http.StatusBadRequest, "subject is required")
		return
	}

	var senderID *uuid.UUID
	senderName := "System"
	if userIDStr := r.Context().Value("user_id"); userIDStr != nil {
		if userID, err := uuid.Parse(userIDStr.(string)); err == nil {
			senderID = &userID
		}
	}
	if username := r.Context().Value("username"); username != nil {
		senderName = username.(string)
	}

	mail, err := s.socialService.SendMail(r.Context(), &req, senderID, senderName)
	if err != nil {
		s.logger.WithError(err).Error("Failed to send mail")
		s.respondError(w, http.StatusInternalServerError, "failed to send mail")
		return
	}

	s.respondJSON(w, http.StatusCreated, mail)
}

func (s *HTTPServer) getMails(w http.ResponseWriter, r *http.Request) {
	var recipientID uuid.UUID
	var err error

	recipientIDStr := r.URL.Query().Get("player_id")
	if recipientIDStr == "" {
		if accountID := r.Context().Value("account_id"); accountID != nil {
			recipientIDStr = accountID.(string)
		}
	}
	if recipientIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "player_id query parameter is required")
		return
	}

	recipientID, err = uuid.Parse(recipientIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
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

	response, err := s.socialService.GetMails(r.Context(), recipientID, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get mails")
		s.respondError(w, http.StatusInternalServerError, "failed to get mails")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getMail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	mail, err := s.socialService.GetMail(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get mail")
		s.respondError(w, http.StatusInternalServerError, "failed to get mail")
		return
	}

	if mail == nil {
		s.respondError(w, http.StatusNotFound, "mail not found")
		return
	}

	s.respondJSON(w, http.StatusOK, mail)
}

func (s *HTTPServer) markMailAsRead(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	err = s.socialService.MarkMailAsRead(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to mark mail as read")
		s.respondError(w, http.StatusInternalServerError, "failed to mark mail as read")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) claimAttachment(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	response, err := s.socialService.ClaimAttachment(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to claim attachment")
		s.respondError(w, http.StatusInternalServerError, "failed to claim attachment")
		return
	}

	if !response.Success {
		s.respondError(w, http.StatusBadRequest, "cannot claim attachment")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) deleteMail(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	err = s.socialService.DeleteMail(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to delete mail")
		s.respondError(w, http.StatusInternalServerError, "failed to delete mail")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (s *HTTPServer) getUnreadMailCount(w http.ResponseWriter, r *http.Request) {
	var recipientID uuid.UUID
	var err error

	recipientIDStr := r.URL.Query().Get("player_id")
	if recipientIDStr == "" {
		if accountID := r.Context().Value("account_id"); accountID != nil {
			recipientIDStr = accountID.(string)
		}
	}
	if recipientIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "player_id query parameter is required")
		return
	}

	recipientID, err = uuid.Parse(recipientIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	response, err := s.socialService.GetUnreadMailCount(r.Context(), recipientID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get unread mail count")
		s.respondError(w, http.StatusInternalServerError, "failed to get unread mail count")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) getMailAttachments(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	response, err := s.socialService.GetMailAttachments(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get mail attachments")
		s.respondError(w, http.StatusInternalServerError, "failed to get mail attachments")
		return
	}

	if response == nil {
		s.respondError(w, http.StatusNotFound, "mail not found")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) payMailCOD(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	response, err := s.socialService.PayMailCOD(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to pay mail COD")
		s.respondError(w, http.StatusInternalServerError, "failed to pay mail COD")
		return
	}

	if !response.Success {
		s.respondError(w, http.StatusBadRequest, "cannot pay COD")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) declineMailCOD(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	err = s.socialService.DeclineMailCOD(r.Context(), mailID)
	if err != nil {
		s.logger.WithError(err).Error("Failed to decline mail COD")
		s.respondError(w, http.StatusInternalServerError, "failed to decline mail COD")
		return
	}

	s.respondJSON(w, http.StatusOK, map[string]string{"status": "declined"})
}

func (s *HTTPServer) getExpiringMails(w http.ResponseWriter, r *http.Request) {
	var recipientID uuid.UUID
	var err error

	recipientIDStr := r.URL.Query().Get("player_id")
	if recipientIDStr == "" {
		if accountID := r.Context().Value("account_id"); accountID != nil {
			recipientIDStr = accountID.(string)
		}
	}
	if recipientIDStr == "" {
		s.respondError(w, http.StatusBadRequest, "player_id query parameter is required")
		return
	}

	recipientID, err = uuid.Parse(recipientIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid player_id")
		return
	}

	days := 3
	if daysStr := r.URL.Query().Get("days"); daysStr != "" {
		if d, err := strconv.Atoi(daysStr); err == nil && d >= 1 && d <= 30 {
			days = d
		}
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

	response, err := s.socialService.GetExpiringMails(r.Context(), recipientID, days, limit, offset)
	if err != nil {
		s.logger.WithError(err).Error("Failed to get expiring mails")
		s.respondError(w, http.StatusInternalServerError, "failed to get expiring mails")
		return
	}

	s.respondJSON(w, http.StatusOK, response)
}

func (s *HTTPServer) extendMailExpiration(w http.ResponseWriter, r *http.Request) {
	vars := mux.Vars(r)
	mailIDStr := vars["mail_id"]

	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid mail id")
		return
	}

	var req models.ExtendMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	if req.Days < 1 || req.Days > 30 {
		s.respondError(w, http.StatusBadRequest, "days must be between 1 and 30")
		return
	}

	mail, err := s.socialService.ExtendMailExpiration(r.Context(), mailID, req.Days)
	if err != nil {
		s.logger.WithError(err).Error("Failed to extend mail expiration")
		s.respondError(w, http.StatusInternalServerError, "failed to extend mail expiration")
		return
	}

	if mail == nil {
		s.respondError(w, http.StatusNotFound, "mail not found")
		return
	}

	s.respondJSON(w, http.StatusOK, mail)
}

func (s *HTTPServer) sendSystemMail(w http.ResponseWriter, r *http.Request) {
	var req models.SendSystemMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	mail, err := s.socialService.SendSystemMail(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to send system mail")
		s.respondError(w, http.StatusInternalServerError, "failed to send system mail")
		return
	}

	s.respondJSON(w, http.StatusOK, mail)
}

func (s *HTTPServer) broadcastSystemMail(w http.ResponseWriter, r *http.Request) {
	var req models.BroadcastSystemMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		s.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	result, err := s.socialService.BroadcastSystemMail(r.Context(), &req)
	if err != nil {
		s.logger.WithError(err).Error("Failed to broadcast system mail")
		s.respondError(w, http.StatusInternalServerError, "failed to broadcast system mail")
		return
	}

	s.respondJSON(w, http.StatusOK, result)
}

