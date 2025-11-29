package server

import (
	"bytes"
	"context"
	"encoding/json"
	"net/http"

	"github.com/necpgame/social-service-go/pkg/api/mail"
	"github.com/sirupsen/logrus"
)

type MailServiceInterface interface {
	GetInbox(ctx context.Context, params mail.GetInboxParams) (*mail.MailListResponse, error)
	SendMail(ctx context.Context, req *mail.CreateMailRequest) (*mail.MailMessage, error)
	GetUnreadMailCount(ctx context.Context) (*mail.UnreadMailCountResponse, error)
	GetMail(ctx context.Context, mailID mail.MailId) (*mail.MailMessage, error)
	MarkMailAsRead(ctx context.Context, mailID mail.MailId) error
}

type MailHandlers struct {
	service MailServiceInterface
	logger  *logrus.Logger
}

func NewMailHandlers(service MailServiceInterface) *MailHandlers {
	return &MailHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *MailHandlers) GetInbox(w http.ResponseWriter, r *http.Request, params mail.GetInboxParams) {
	ctx := r.Context()
	
	response, err := h.service.GetInbox(ctx, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get inbox")
		h.respondError(w, http.StatusInternalServerError, "failed to get inbox")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *MailHandlers) SendMail(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req mail.CreateMailRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}
	
	mailItem, err := h.service.SendMail(ctx, &req)
	if err != nil {
		h.logger.WithError(err).Error("Failed to send mail")
		h.respondError(w, http.StatusInternalServerError, "failed to send mail")
		return
	}
	
	h.respondJSON(w, http.StatusCreated, mailItem)
}

func (h *MailHandlers) GetUnreadMailCount(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	response, err := h.service.GetUnreadMailCount(ctx)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get unread mail count")
		h.respondError(w, http.StatusInternalServerError, "failed to get unread mail count")
		return
	}
	
	h.respondJSON(w, http.StatusOK, response)
}

func (h *MailHandlers) GetMail(w http.ResponseWriter, r *http.Request, mailId mail.MailId) {
	ctx := r.Context()
	
	mailItem, err := h.service.GetMail(ctx, mailId)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get mail")
		h.respondError(w, http.StatusInternalServerError, "failed to get mail")
		return
	}
	
	if mailItem == nil {
		h.respondError(w, http.StatusNotFound, "mail not found")
		return
	}
	
	h.respondJSON(w, http.StatusOK, mailItem)
}

func (h *MailHandlers) MarkMailAsRead(w http.ResponseWriter, r *http.Request, mailId mail.MailId) {
	ctx := r.Context()
	
	if err := h.service.MarkMailAsRead(ctx, mailId); err != nil {
		h.logger.WithError(err).Error("Failed to mark mail as read")
		h.respondError(w, http.StatusInternalServerError, "failed to mark mail as read")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *MailHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")

	buf := &bytes.Buffer{}
	if err := json.NewEncoder(buf).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
		h.respondError(w, http.StatusInternalServerError, "Failed to encode JSON response")
		return
	}

	w.WriteHeader(status)
	if _, err := w.Write(buf.Bytes()); err != nil {
		h.logger.WithError(err).Error("Failed to write JSON response")
	}
}

func (h *MailHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := mail.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}
