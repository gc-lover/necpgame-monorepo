package server

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"log/slog"
	"time"

	"github.com/google/uuid"
)

// Repository handles database operations for homomorphic encryption
type Repository interface {
	// Encrypted data operations
	StoreEncryptedData(ctx context.Context, data *EncryptedData) error
	GetEncryptedData(ctx context.Context, dataID string) (*EncryptedData, error)
	UpdateEncryptedData(ctx context.Context, data *EncryptedData) error
	DeleteEncryptedData(ctx context.Context, dataID string) error

	// Key management
	StorePlayerKeyMapping(ctx context.Context, playerID, keyID string) error
	GetPlayerKeyID(ctx context.Context, playerID string) (string, error)

	// Player data mappings
	GetPlayerInventoryID(ctx context.Context, playerID string) (string, error)
	StorePlayerInventoryID(ctx context.Context, playerID, inventoryID string) error

	// Statistics and analytics
	CountEncryptedData(ctx context.Context) (int64, error)
	CountActiveKeys(ctx context.Context) (int64, error)
	GetRecentOperations(ctx context.Context, timeWindow time.Duration) ([]OperationLog, error)
	GetPlayerStat(ctx context.Context, playerID, statName string) (*EncryptedData, error)
}

// PostgresRepository implements Repository for PostgreSQL
type PostgresRepository struct {
	db *sql.DB
}

// NewPostgresRepository creates a new PostgreSQL repository
func NewPostgresRepository(db *sql.DB) Repository {
	return &PostgresRepository{db: db}
}

// StoreEncryptedData stores encrypted data in database
func (r *PostgresRepository) StoreEncryptedData(ctx context.Context, data *EncryptedData) error {
	if data.CreatedAt == 0 {
		data.CreatedAt = time.Now().Unix()
	}

	operationLogJSON, err := json.Marshal(data.OperationLog)
	if err != nil {
		return fmt.Errorf("failed to marshal operation log: %w", err)
	}

	query := `
		INSERT INTO homomorphic_encryption.encrypted_data (
			id, key_id, ciphertext, data_type, schema, created_at, operation_log
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (id) DO UPDATE SET
			ciphertext = EXCLUDED.ciphertext,
			schema = EXCLUDED.schema,
			operation_log = EXCLUDED.operation_log
	`

	_, err = r.db.ExecContext(ctx, query,
		data.ID, data.KeyID, data.Ciphertext, data.DataType,
		string(data.Schema), time.Unix(data.CreatedAt, 0), string(operationLogJSON),
	)

	if err != nil {
		slog.Error("Failed to store encrypted data", "data_id", data.ID, "error", err)
		return fmt.Errorf("failed to store encrypted data: %w", err)
	}

	slog.Debug("Encrypted data stored", "data_id", data.ID, "data_type", data.DataType)
	return nil
}

// GetEncryptedData retrieves encrypted data from database
func (r *PostgresRepository) GetEncryptedData(ctx context.Context, dataID string) (*EncryptedData, error) {
	query := `
		SELECT id, key_id, ciphertext, data_type, schema, created_at, operation_log
		FROM homomorphic_encryption.encrypted_data
		WHERE id = $1
	`

	var data EncryptedData
	var schemaStr, operationLogStr string
	var createdAt time.Time

	err := r.db.QueryRowContext(ctx, query, dataID).Scan(
		&data.ID, &data.KeyID, &data.Ciphertext, &data.DataType,
		&schemaStr, &createdAt, &operationLogStr,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("encrypted data not found: %s", dataID)
		}
		slog.Error("Failed to get encrypted data", "data_id", dataID, "error", err)
		return nil, fmt.Errorf("failed to get encrypted data: %w", err)
	}

	data.Schema = json.RawMessage(schemaStr)
	data.CreatedAt = createdAt.Unix()

	if operationLogStr != "" {
		if err := json.Unmarshal([]byte(operationLogStr), &data.OperationLog); err != nil {
			slog.Warn("Failed to unmarshal operation log", "data_id", dataID, "error", err)
			data.OperationLog = []OperationLog{}
		}
	}

	return &data, nil
}

// UpdateEncryptedData updates encrypted data in database
func (r *PostgresRepository) UpdateEncryptedData(ctx context.Context, data *EncryptedData) error {
	operationLogJSON, err := json.Marshal(data.OperationLog)
	if err != nil {
		return fmt.Errorf("failed to marshal operation log: %w", err)
	}

	query := `
		UPDATE homomorphic_encryption.encrypted_data
		SET ciphertext = $2, schema = $3, operation_log = $4
		WHERE id = $1
	`

	result, err := r.db.ExecContext(ctx, query,
		data.ID, data.Ciphertext, string(data.Schema), string(operationLogJSON),
	)

	if err != nil {
		slog.Error("Failed to update encrypted data", "data_id", data.ID, "error", err)
		return fmt.Errorf("failed to update encrypted data: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("encrypted data not found: %s", data.ID)
	}

	return nil
}

// DeleteEncryptedData removes encrypted data from database
func (r *PostgresRepository) DeleteEncryptedData(ctx context.Context, dataID string) error {
	query := `DELETE FROM homomorphic_encryption.encrypted_data WHERE id = $1`

	result, err := r.db.ExecContext(ctx, query, dataID)
	if err != nil {
		slog.Error("Failed to delete encrypted data", "data_id", dataID, "error", err)
		return fmt.Errorf("failed to delete encrypted data: %w", err)
	}

	rowsAffected, _ := result.RowsAffected()
	if rowsAffected == 0 {
		return fmt.Errorf("encrypted data not found: %s", dataID)
	}

	slog.Info("Encrypted data deleted", "data_id", dataID)
	return nil
}

// StorePlayerKeyMapping stores mapping between player and their encryption key
func (r *PostgresRepository) StorePlayerKeyMapping(ctx context.Context, playerID, keyID string) error {
	query := `
		INSERT INTO homomorphic_encryption.player_keys (player_id, key_id, created_at)
		VALUES ($1, $2, $3)
		ON CONFLICT (player_id) DO UPDATE SET key_id = EXCLUDED.key_id
	`

	_, err := r.db.ExecContext(ctx, query, playerID, keyID, time.Now())
	if err != nil {
		slog.Error("Failed to store player key mapping", "player_id", playerID, "key_id", keyID, "error", err)
		return fmt.Errorf("failed to store player key mapping: %w", err)
	}

	return nil
}

// GetPlayerKeyID retrieves encryption key ID for a player
func (r *PostgresRepository) GetPlayerKeyID(ctx context.Context, playerID string) (string, error) {
	query := `SELECT key_id FROM homomorphic_encryption.player_keys WHERE player_id = $1`

	var keyID string
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(&keyID)
	if err != nil {
		if err == sql.ErrNoRows {
			return "", fmt.Errorf("player key mapping not found: %s", playerID)
		}
		slog.Error("Failed to get player key ID", "player_id", playerID, "error", err)
		return "", fmt.Errorf("failed to get player key ID: %w", err)
	}

	return keyID, nil
}

// GetPlayerInventoryID gets the inventory ID for a player
func (r *PostgresRepository) GetPlayerInventoryID(ctx context.Context, playerID string) (string, error) {
	query := `
		SELECT ed.id
		FROM homomorphic_encryption.encrypted_data ed
		JOIN homomorphic_encryption.player_keys pk ON ed.key_id = pk.key_id
		WHERE pk.player_id = $1 AND ed.data_type = 'inventory'
		ORDER BY ed.created_at DESC
		LIMIT 1
	`

	var inventoryID string
	err := r.db.QueryRowContext(ctx, query, playerID).Scan(&inventoryID)
	if err != nil {
		if err == sql.ErrNoRows {
			return "", fmt.Errorf("player inventory not found: %s", playerID)
		}
		slog.Error("Failed to get player inventory ID", "player_id", playerID, "error", err)
		return "", fmt.Errorf("failed to get player inventory ID: %w", err)
	}

	return inventoryID, nil
}

// StorePlayerInventoryID stores the inventory ID for a player
func (r *PostgresRepository) StorePlayerInventoryID(ctx context.Context, playerID, inventoryID string) error {
	query := `
		INSERT INTO homomorphic_encryption.player_inventories (player_id, inventory_id, created_at)
		VALUES ($1, $2, $3)
		ON CONFLICT (player_id) DO UPDATE SET inventory_id = EXCLUDED.inventory_id
	`

	_, err := r.db.ExecContext(ctx, query, playerID, inventoryID, time.Now())
	if err != nil {
		slog.Error("Failed to store player inventory ID", "player_id", playerID, "inventory_id", inventoryID, "error", err)
		return fmt.Errorf("failed to store player inventory ID: %w", err)
	}

	return nil
}

// CountEncryptedData returns total count of encrypted data entries
func (r *PostgresRepository) CountEncryptedData(ctx context.Context) (int64, error) {
	query := `SELECT COUNT(*) FROM homomorphic_encryption.encrypted_data`

	var count int64
	err := r.db.QueryRowContext(ctx, query).Scan(&count)
	if err != nil {
		slog.Error("Failed to count encrypted data", "error", err)
		return 0, fmt.Errorf("failed to count encrypted data: %w", err)
	}

	return count, nil
}

// CountActiveKeys returns count of active encryption keys
func (r *PostgresRepository) CountActiveKeys(ctx context.Context) (int64, error) {
	query := `SELECT COUNT(DISTINCT key_id) FROM homomorphic_encryption.player_keys`

	var count int64
	err := r.db.QueryRowContext(ctx, query).Scan(&count)
	if err != nil {
		slog.Error("Failed to count active keys", "error", err)
		return 0, fmt.Errorf("failed to count active keys: %w", err)
	}

	return count, nil
}

// GetRecentOperations returns recent homomorphic operations
func (r *PostgresRepository) GetRecentOperations(ctx context.Context, timeWindow time.Duration) ([]OperationLog, error) {
	cutoffTime := time.Now().Add(-timeWindow)

	query := `
		SELECT operation_log
		FROM homomorphic_encryption.encrypted_data
		WHERE created_at >= $1
		LIMIT 100
	`

	rows, err := r.db.QueryContext(ctx, query, cutoffTime)
	if err != nil {
		slog.Error("Failed to get recent operations", "error", err)
		return nil, fmt.Errorf("failed to get recent operations: %w", err)
	}
	defer rows.Close()

	var operations []OperationLog
	for rows.Next() {
		var operationLogStr string
		if err := rows.Scan(&operationLogStr); err != nil {
			continue
		}

		var logs []OperationLog
		if err := json.Unmarshal([]byte(operationLogStr), &logs); err != nil {
			continue
		}

		operations = append(operations, logs...)
	}

	// Limit to last 50 operations
	if len(operations) > 50 {
		operations = operations[len(operations)-50:]
	}

	return operations, nil
}

// GetPlayerStat retrieves a specific encrypted stat for a player
func (r *PostgresRepository) GetPlayerStat(ctx context.Context, playerID, statName string) (*EncryptedData, error) {
	query := `
		SELECT ed.id, ed.key_id, ed.ciphertext, ed.data_type, ed.schema, ed.created_at, ed.operation_log
		FROM homomorphic_encryption.encrypted_data ed
		JOIN homomorphic_encryption.player_keys pk ON ed.key_id = pk.key_id
		WHERE pk.player_id = $1 AND ed.data_type = $2
		ORDER BY ed.created_at DESC
		LIMIT 1
	`

	var data EncryptedData
	var schemaStr, operationLogStr string
	var createdAt time.Time

	err := r.db.QueryRowContext(ctx, query, playerID, statName).Scan(
		&data.ID, &data.KeyID, &data.Ciphertext, &data.DataType,
		&schemaStr, &createdAt, &operationLogStr,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("player stat not found: %s/%s", playerID, statName)
		}
		slog.Error("Failed to get player stat", "player_id", playerID, "stat_name", statName, "error", err)
		return nil, fmt.Errorf("failed to get player stat: %w", err)
	}

	data.Schema = json.RawMessage(schemaStr)
	data.CreatedAt = createdAt.Unix()

	if operationLogStr != "" {
		if err := json.Unmarshal([]byte(operationLogStr), &data.OperationLog); err != nil {
			slog.Warn("Failed to unmarshal operation log", "data_id", data.ID, "error", err)
			data.OperationLog = []OperationLog{}
		}
	}

	return &data, nil
}