// Issue: #151
package server

import (
	"context"
	"errors"

	"github.com/gc-lover/necpgame-monorepo/services/mail-service-go/pkg/api"
)

type Service interface {
	GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error)
	GetMail(ctx context.Context, mailID string) (*api.MailDetailResponse, error)
	DeleteMail(ctx context.Context, mailID string) error
	SendMail(ctx context.Context, req *api.SendMailRequest) (*api.SendMailResponse, error)
	ClaimAttachments(ctx context.Context, mailID string) (*api.ClaimAttachmentsResponse, error)
}

type MailService struct {
	repository Repository
}

func NewMailService(repository Repository) Service {
	return &MailService{repository: repository}
}

func (s *MailService) GetInbox(ctx context.Context, params api.GetInboxParams) (*api.InboxResponse, error) {
	// TODO: Реализовать получение списка писем
	return &api.InboxResponse{
		Mails: []api.MailSummary{},
	}, nil
}

func (s *MailService) GetMail(ctx context.Context, mailID string) (*api.MailDetailResponse, error) {
	// TODO: Реализовать получение письма
	return nil, errors.New("not implemented")
}

func (s *MailService) DeleteMail(ctx context.Context, mailID string) error {
	// TODO: Реализовать удаление письма
	return nil
}

func (s *MailService) SendMail(ctx context.Context, req *api.SendMailRequest) (*api.SendMailResponse, error) {
	// TODO: Реализовать отправку письма
	return nil, errors.New("not implemented")
}

func (s *MailService) ClaimAttachments(ctx context.Context, mailID string) (*api.ClaimAttachmentsResponse, error) {
	// TODO: Реализовать получение вложений
	return nil, errors.New("not implemented")
}

