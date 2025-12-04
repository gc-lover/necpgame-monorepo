// Issue: #1599, #1604 - ogen handlers (TYPED responses)
package server

import (
	"context"
	"time"

	api "github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service Service
}

func NewHandlers(service Service) *Handlers {
	return &Handlers{service: service}
}

// GetInbox implements GET /social/mail/inbox - TYPED response!
func (h *Handlers) GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.GetInbox(ctx, params)
	if err != nil {
		return nil, err
	}
	return response, nil
}

// GetMail implements GET /social/mail/{mail_id} - TYPED response!
func (h *Handlers) GetMail(ctx context.Context, params api.GetMailParams) (api.GetMailRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.GetMail(ctx, params.MailID.String())
	if err != nil {
		return &api.GetMailNotFound{Error: "NotFound", Message: "Mail not found"}, nil
	}
	return response, nil
}

// DeleteMail implements DELETE /social/mail/{mail_id} - TYPED response!
func (h *Handlers) DeleteMail(ctx context.Context, params api.DeleteMailParams) (*api.SuccessResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteMail(ctx, params.MailID.String())
	if err != nil {
		return nil, err
	}
	return &api.SuccessResponse{Status: api.NewOptString("deleted")}, nil
}

// SendMail implements POST /social/mail - TYPED response!
func (h *Handlers) SendMail(ctx context.Context, req *api.SendMailRequest) (api.SendMailRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.SendMail(ctx, req)
	if err != nil {
		return &api.SendMailBadRequest{Error: "BadRequest", Message: err.Error()}, nil
	}
	return response, nil
}

// ClaimAttachments implements POST /social/mail/{mail_id}/claim-attachments - TYPED response!
func (h *Handlers) ClaimAttachments(ctx context.Context, params api.ClaimAttachmentsParams) (api.ClaimAttachmentsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	response, err := h.service.ClaimAttachments(ctx, params.MailID.String())
	if err != nil {
		return &api.ClaimAttachmentsNotFound{Error: "NotFound", Message: err.Error()}, nil
	}
	return response, nil
}

