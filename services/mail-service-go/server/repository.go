// Issue: #151
package server

import (
	"context"
	"database/sql"
)

type Repository interface {
	GetPlayerMails(ctx context.Context, playerID string, filter string) ([]interface{}, error)
	GetMail(ctx context.Context, mailID string) (interface{}, error)
	CreateMail(ctx context.Context, senderID, recipientID, subject, body string) (string, error)
	DeleteMail(ctx context.Context, mailID string) error
	UpdateMailStatus(ctx context.Context, mailID, status string) error
	ClaimAttachments(ctx context.Context, mailID string) (interface{}, error)
}

type PostgresRepository struct {
	db *sql.DB
}

func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

func (r *PostgresRepository) GetPlayerMails(ctx context.Context, playerID string, filter string) ([]interface{}, error) {
	// TODO: SELECT из mail_messages
	return []interface{}{}, nil
}

func (r *PostgresRepository) GetMail(ctx context.Context, mailID string) (interface{}, error) {
	// TODO: SELECT
	return nil, nil
}

func (r *PostgresRepository) CreateMail(ctx context.Context, senderID, recipientID, subject, body string) (string, error) {
	// TODO: INSERT в mail_messages
	return "mail-123", nil
}

func (r *PostgresRepository) DeleteMail(ctx context.Context, mailID string) error {
	// TODO: DELETE
	return nil
}

func (r *PostgresRepository) UpdateMailStatus(ctx context.Context, mailID, status string) error {
	// TODO: UPDATE
	return nil
}

func (r *PostgresRepository) ClaimAttachments(ctx context.Context, mailID string) (interface{}, error) {
	// TODO: Получить и удалить вложения
	return nil, nil
}








