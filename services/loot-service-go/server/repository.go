// Issue: #142
package server

import (
	"context"
	"database/sql"
)

type Repository interface {
	CreateLootTable(ctx context.Context, sourceType, sourceID, itemID string, dropChance float64) error
	GenerateLootFromTable(ctx context.Context, sourceType, sourceID string) ([]interface{}, error)
	CreateWorldDrop(ctx context.Context, locationID string, items, currency interface{}) (string, error)
	GetWorldDrops(ctx context.Context, locationID string) ([]interface{}, error)
	PickupDrop(ctx context.Context, dropID, playerID string) error
	CreateLootRoll(ctx context.Context, partyID, itemID string) (string, error)
	UpdateRollResult(ctx context.Context, rollID, playerID string, rollValue int) error
	SaveLootHistory(ctx context.Context, playerID, itemID string, quantity int) error
}

type PostgresRepository struct {
	db *sql.DB
}

func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

func (r *PostgresRepository) CreateLootTable(ctx context.Context, sourceType, sourceID, itemID string, dropChance float64) error {
	return nil
}

func (r *PostgresRepository) GenerateLootFromTable(ctx context.Context, sourceType, sourceID string) ([]interface{}, error) {
	return []interface{}{}, nil
}

func (r *PostgresRepository) CreateWorldDrop(ctx context.Context, locationID string, items, currency interface{}) (string, error) {
	return "drop-123", nil
}

func (r *PostgresRepository) GetWorldDrops(ctx context.Context, locationID string) ([]interface{}, error) {
	return []interface{}{}, nil
}

func (r *PostgresRepository) PickupDrop(ctx context.Context, dropID, playerID string) error {
	return nil
}

func (r *PostgresRepository) CreateLootRoll(ctx context.Context, partyID, itemID string) (string, error) {
	return "roll-123", nil
}

func (r *PostgresRepository) UpdateRollResult(ctx context.Context, rollID, playerID string, rollValue int) error {
	return nil
}

func (r *PostgresRepository) SaveLootHistory(ctx context.Context, playerID, itemID string, quantity int) error {
	return nil
}

