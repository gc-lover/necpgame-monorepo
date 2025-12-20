// Package server Issue: #131
package server

import (
	"context"
	"database/sql"
)

type Repository interface {
	CreateTradeSession(ctx context.Context, initiatorID, targetID string) (string, error)
	GetTradeSession(ctx context.Context, sessionID string) (interface{}, error)
	CancelSession(ctx context.Context, sessionID string) error
	AddItems(ctx context.Context, sessionID, playerID string, items interface{}) error
	AddCurrency(ctx context.Context, sessionID, playerID string, currency interface{}) error
	SetReady(ctx context.Context, sessionID, playerID string, ready bool) error
	CompleteTrade(ctx context.Context, sessionID string) error
	SaveTradeHistory(ctx context.Context, sessionID string) error
}

type PostgresRepository struct {
	db *sql.DB
}

func (r *PostgresRepository) CreateTradeSession(_ context.Context, _, _ string) (string, error) {
	return "session-123", nil
}

func (r *PostgresRepository) GetTradeSession(_ context.Context, _ string) (interface{}, error) {
	return nil, nil
}

func (r *PostgresRepository) CancelSession(_ context.Context, _ string) error {
	return nil
}

func (r *PostgresRepository) AddItems(_ context.Context, _, _ string, _ interface{}) error {
	return nil
}

func (r *PostgresRepository) AddCurrency(_ context.Context, _, _ string, _ interface{}) error {
	return nil
}

func (r *PostgresRepository) SetReady(_ context.Context, _, _ string, _ bool) error {
	return nil
}

func (r *PostgresRepository) CompleteTrade(_ context.Context, _ string) error {
	return nil
}

func (r *PostgresRepository) SaveTradeHistory(_ context.Context, _ string) error {
	return nil
}
