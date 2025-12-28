package handlers

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"

	"services/mail-system-service-go/internal/service"
	"services/mail-system-service-go/pkg/models"
)

// Handler handles HTTP requests for mail system
type Handler struct {
	service *service.Service
	logger  *zap.Logger
}

// NewHandler creates a new handler instance
func NewHandler(svc *service.Service, logger *zap.Logger) *Handler {
	return &Handler{
		service: svc,
		logger:  logger,
	}
}

// HealthCheck handles health check requests
func (h *Handler) HealthCheck(w http.ResponseWriter, r *http.Request) {
	response := models.HealthResponse{
		Status:    "healthy",
		Timestamp: time.Now(),
		Version:   "1.0.0",
		Uptime:    0, // Would be calculated from service start time
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetMailbox handles mailbox retrieval requests
func (h *Handler) GetMailbox(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	// Parse query parameters
	folder := r.URL.Query().Get("folder")
	if folder == "" {
		folder = "inbox"
	}

	status := r.URL.Query().Get("status")
	category := r.URL.Query().Get("category")

	limitStr := r.URL.Query().Get("limit")
	limit := 50
	if limitStr != "" {
		if l, err := strconv.Atoi(limitStr); err == nil && l > 0 && l <= 100 {
			limit = l
		}
	}

	offsetStr := r.URL.Query().Get("offset")
	offset := 0
	if offsetStr != "" {
		if o, err := strconv.Atoi(offsetStr); err == nil && o >= 0 {
			offset = o
		}
	}

	mailbox, err := h.service.GetMailbox(r.Context(), userID, folder, status, category, limit, offset)
	if err != nil {
		h.logger.Error("Failed to get mailbox", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get mailbox")
		return
	}

	h.respondJSON(w, http.StatusOK, mailbox)
}

// GetMail handles individual mail retrieval requests
func (h *Handler) GetMail(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	mailIDStr := chi.URLParam(r, "mail_id")
	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mail ID")
		return
	}

	mail, err := h.service.GetMail(r.Context(), mailID, userID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Mail not found")
			return
		}
		h.logger.Error("Failed to get mail", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get mail")
		return
	}

	h.respondJSON(w, http.StatusOK, mail)
}

// SendMail handles mail sending requests
func (h *Handler) SendMail(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	var request models.SendMailRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	// Validate required fields
	if request.Subject == "" || request.Content.Text == "" {
		h.respondError(w, http.StatusBadRequest, "Subject and content are required")
		return
	}

	// Set defaults
	if request.Category == "" {
		request.Category = "personal"
	}
	if request.Priority == "" {
		request.Priority = "normal"
	}
	if request.ExpiresInHours == 0 {
		request.ExpiresInHours = 168 // 7 days
	}

	response, err := h.service.SendMail(r.Context(), &request, userID)
	if err != nil {
		h.logger.Error("Failed to send mail", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusCreated, response)
}

// MarkAsRead handles mark as read requests
func (h *Handler) MarkAsRead(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	mailIDStr := chi.URLParam(r, "mail_id")
	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mail ID")
		return
	}

	if err := h.service.MarkAsRead(r.Context(), mailID, userID); err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Mail not found")
			return
		}
		h.logger.Error("Failed to mark mail as read", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to mark mail as read")
		return
	}

	response := models.MarkReadResponse{
		MailID:    mailID,
		ReadAt:    time.Now(),
		PreviousStatus: "unread",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// DeleteMail handles mail deletion requests
func (h *Handler) DeleteMail(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	mailIDStr := chi.URLParam(r, "mail_id")
	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mail ID")
		return
	}

	if err := h.service.DeleteMail(r.Context(), mailID, userID); err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Mail not found")
			return
		}
		h.logger.Error("Failed to delete mail", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to delete mail")
		return
	}

	w.WriteHeader(http.StatusNoContent)
}

// ArchiveMail handles mail archiving requests
func (h *Handler) ArchiveMail(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	mailIDStr := chi.URLParam(r, "mail_id")
	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mail ID")
		return
	}

	if err := h.service.ArchiveMail(r.Context(), mailID, userID); err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Mail not found")
			return
		}
		h.logger.Error("Failed to archive mail", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to archive mail")
		return
	}

	response := models.ArchiveResponse{
		MailID:    mailID,
		ArchivedAt: time.Now(),
		Folder:    "archived",
	}

	h.respondJSON(w, http.StatusOK, response)
}

// DownloadAttachment handles attachment download requests
func (h *Handler) DownloadAttachment(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	attachmentIDStr := chi.URLParam(r, "attachment_id")
	attachmentID, err := uuid.Parse(attachmentIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid attachment ID")
		return
	}

	attachment, err := h.service.DownloadAttachment(r.Context(), attachmentID, userID)
	if err != nil {
		if strings.Contains(err.Error(), "not found") {
			h.respondError(w, http.StatusNotFound, "Attachment not found")
			return
		}
		h.logger.Error("Failed to download attachment", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to download attachment")
		return
	}

	w.Header().Set("Content-Type", attachment.ContentType)
	w.Header().Set("Content-Disposition", fmt.Sprintf("attachment; filename=\"%s\"", attachment.Filename))
	w.Header().Set("Content-Length", fmt.Sprintf("%d", len(attachment.Data)))
	w.WriteHeader(http.StatusOK)
	w.Write(attachment.Data)
}

// ReportMail handles mail reporting requests
func (h *Handler) ReportMail(w http.ResponseWriter, r *http.Request) {
	userID, err := h.getUserIDFromContext(r)
	if err != nil {
		h.respondError(w, http.StatusUnauthorized, "Unauthorized")
		return
	}

	mailIDStr := chi.URLParam(r, "mail_id")
	mailID, err := uuid.Parse(mailIDStr)
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid mail ID")
		return
	}

	var request models.ReportRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if request.Reason == "" {
		h.respondError(w, http.StatusBadRequest, "Report reason is required")
		return
	}

	if request.Severity == "" {
		request.Severity = "medium"
	}

	report, err := h.service.ReportMail(r.Context(), mailID, userID, &request)
	if err != nil {
		h.logger.Error("Failed to report mail", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	response := models.ReportResponse{
		ReportID:    report.ID,
		MailID:      report.MailID,
		SubmittedAt: report.SubmittedAt,
		Status:      report.Status,
	}

	h.respondJSON(w, http.StatusOK, response)
}

// SendBulkMail handles bulk mail sending requests
func (h *Handler) SendBulkMail(w http.ResponseWriter, r *http.Request) {
	var request models.BulkMailRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	// Validate required fields
	if request.Subject == "" || request.Content.Text == "" {
		h.respondError(w, http.StatusBadRequest, "Subject and content are required")
		return
	}

	if len(request.RecipientCriteria.SpecificPlayerIDs) == 0 &&
		request.RecipientCriteria.VIPStatus == nil &&
		request.RecipientCriteria.GuildMembers == nil &&
		request.RecipientCriteria.PlayerLevelMin == nil &&
		len(request.RecipientCriteria.Regions) == 0 {
		h.respondError(w, http.StatusBadRequest, "Recipient criteria are required")
		return
	}

	response, err := h.service.SendBulkMail(r.Context(), &request)
	if err != nil {
		h.logger.Error("Failed to send bulk mail", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetMailAnalytics handles analytics requests
func (h *Handler) GetMailAnalytics(w http.ResponseWriter, r *http.Request) {
	timeframe := r.URL.Query().Get("timeframe")
	if timeframe == "" {
		timeframe = "7d"
	}

	// Validate timeframe
	validTimeframes := map[string]bool{
		"1h": true, "24h": true, "7d": true, "30d": true,
	}
	if !validTimeframes[timeframe] {
		h.respondError(w, http.StatusBadRequest, "Invalid timeframe")
		return
	}

	analytics, err := h.service.GetMailAnalytics(r.Context(), timeframe)
	if err != nil {
		h.logger.Error("Failed to get mail analytics", zap.Error(err))
		h.respondError(w, http.StatusInternalServerError, "Failed to get analytics")
		return
	}

	h.respondJSON(w, http.StatusOK, analytics)
}

// SendSystemAnnouncement handles system announcement requests
func (h *Handler) SendSystemAnnouncement(w http.ResponseWriter, r *http.Request) {
	var request models.SystemAnnouncementRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid JSON")
		return
	}

	if request.Subject == "" || request.Content.Text == "" {
		h.respondError(w, http.StatusBadRequest, "Subject and content are required")
		return
	}

	if request.Priority == "" {
		request.Priority = "normal"
	}
	if request.ExpiresInHours == 0 {
		request.ExpiresInHours = 24
	}

	response, err := h.service.SendSystemAnnouncement(r.Context(), &request)
	if err != nil {
		h.logger.Error("Failed to send system announcement", zap.Error(err))
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, response)
}

// AuthMiddleware authenticates requests
func (h *Handler) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Extract user ID from JWT token
		// This is a placeholder - actual JWT validation would go here
		userIDStr := r.Header.Get("X-User-ID")
		if userIDStr == "" {
			h.respondError(w, http.StatusUnauthorized, "Missing user ID")
			return
		}

		userID, err := uuid.Parse(userIDStr)
		if err != nil {
			h.respondError(w, http.StatusUnauthorized, "Invalid user ID")
			return
		}

		// Store user ID in context
		ctx := context.WithValue(r.Context(), "user_id", userID)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

// Helper methods

func (h *Handler) getUserIDFromContext(r *http.Request) (uuid.UUID, error) {
	userID, ok := r.Context().Value("user_id").(uuid.UUID)
	if !ok {
		return uuid.Nil, fmt.Errorf("user ID not found in context")
	}
	return userID, nil
}

func (h *Handler) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.Error("Failed to encode JSON response", zap.Error(err))
	}
}

func (h *Handler) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := models.Error{
		Error:     "request_error",
		Code:      http.StatusText(status),
		Message:   message,
		Timestamp: time.Now(),
	}

	h.respondJSON(w, status, errorResponse)
}
