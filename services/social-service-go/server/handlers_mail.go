package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/social-service-go/pkg/api/mail"
	"github.com/sirupsen/logrus"
)

type MailServiceInterface interface {
	GetMail(ctx context.Context, characterID, mailID uuid.UUID) (*mail.Mail, error)
	ListMail(ctx context.Context, characterID uuid.UUID, params mail.ListMailParams) ([]mail.Mail, error)
	SendMail(ctx context.Context, req *mail.SendMailRequest) (*mail.Mail, error)
	DeleteMail(ctx context.Context, characterID, mailID uuid.UUID) error
	MarkMailAsRead(ctx context.Context, characterID, mailID uuid.UUID) error
	ClaimMailAttachment(ctx context.Context, characterID, mailID, attachmentID uuid.UUID) error
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

func (h *MailHandlers) GetMail(w http.ResponseWriter, r *http.Request, characterId mail.CharacterId, mailId mail.MailId) {
	ctx := r.Context()
	
	mailItem, err := h.service.GetMail(ctx, characterId, mailId)
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

func (h *MailHandlers) ListMail(w http.ResponseWriter, r *http.Request, characterId mail.CharacterId, params mail.ListMailParams) {
	ctx := r.Context()
	
	mailList, err := h.service.ListMail(ctx, characterId, params)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list mail")
		h.respondError(w, http.StatusInternalServerError, "failed to list mail")
		return
	}
	
	response := mail.MailListResponse{
		Mail:  &mailList,
		Total: new(int),
	}
	*response.Total = len(mailList)
	h.respondJSON(w, http.StatusOK, response)
}

func (h *MailHandlers) SendMail(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	
	var req mail.SendMailRequest
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

func (h *MailHandlers) DeleteMail(w http.ResponseWriter, r *http.Request, characterId mail.CharacterId, mailId mail.MailId) {
	ctx := r.Context()
	
	if err := h.service.DeleteMail(ctx, characterId, mailId); err != nil {
		h.logger.WithError(err).Error("Failed to delete mail")
		h.respondError(w, http.StatusInternalServerError, "failed to delete mail")
		return
	}
	
	w.WriteHeader(http.StatusNoContent)
}

func (h *MailHandlers) MarkMailAsRead(w http.ResponseWriter, r *http.Request, characterId mail.CharacterId, mailId mail.MailId) {
	ctx := r.Context()
	
	if err := h.service.MarkMailAsRead(ctx, characterId, mailId); err != nil {
		h.logger.WithError(err).Error("Failed to mark mail as read")
		h.respondError(w, http.StatusInternalServerError, "failed to mark mail as read")
		return
	}
	
	h.respondJSON(w, http.StatusOK, mail.StatusResponse{Status: "success"})
}

func (h *MailHandlers) ClaimMailAttachment(w http.ResponseWriter, r *http.Request, characterId mail.CharacterId, mailId mail.MailId, attachmentId mail.AttachmentId) {
	ctx := r.Context()
	
	if err := h.service.ClaimMailAttachment(ctx, characterId, mailId, attachmentId); err != nil {
		h.logger.WithError(err).Error("Failed to claim mail attachment")
		h.respondError(w, http.StatusInternalServerError, "failed to claim mail attachment")
		return
	}
	
	h.respondJSON(w, http.StatusOK, mail.StatusResponse{Status: "success"})
}

func (h *MailHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *MailHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := mail.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

