package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type RelationshipRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewRelationshipRepository(db *pgxpool.Pool) *RelationshipRepository {
	return &RelationshipRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *RelationshipRepository) GetRelationships(ctx context.Context, playerID uuid.UUID, relationshipType *models.RelationshipType, limit, offset int) ([]models.Relationship, int, error) {
	var args []interface{}
	baseQuery := `
		SELECT id, player_id, target_id, type, created_at, updated_at
		FROM social.relationships
		WHERE player_id = $1`

	args = append(args, playerID)
	argIndex := 2

	if relationshipType != nil {
		baseQuery += fmt.Sprintf(" AND type = $%d", argIndex)
		args = append(args, *relationshipType)
		argIndex++
	}

	countQuery := "SELECT COUNT(*) FROM (" + baseQuery + ") AS count_query"
	var total int
	err := r.db.QueryRow(ctx, countQuery, args...).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count relationships: %w", err)
	}

	baseQuery += " ORDER BY updated_at DESC"
	baseQuery += fmt.Sprintf(" LIMIT $%d OFFSET $%d", argIndex, argIndex+1)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, baseQuery, args...)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get relationships: %w", err)
	}
	defer rows.Close()

	var relationships []models.Relationship
	for rows.Next() {
		var rel models.Relationship
		err := rows.Scan(
			&rel.ID, &rel.PlayerID, &rel.TargetID, &rel.Type,
			&rel.CreatedAt, &rel.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan relationship: %w", err)
		}
		relationships = append(relationships, rel)
	}

	return relationships, total, nil
}

func (r *RelationshipRepository) SetRelationship(ctx context.Context, playerID uuid.UUID, req *models.SetRelationshipRequest) (*models.Relationship, error) {
	query := `
		INSERT INTO social.relationships (id, player_id, target_id, type, created_at, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, NOW(), NOW())
		ON CONFLICT (player_id, target_id) 
		DO UPDATE SET type = $3, updated_at = NOW()
		RETURNING id, created_at, updated_at`

	var relationship models.Relationship
	err := r.db.QueryRow(ctx, query, playerID, req.TargetID, req.Type).Scan(
		&relationship.ID, &relationship.CreatedAt, &relationship.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to set relationship: %w", err)
	}

	relationship.PlayerID = playerID
	relationship.TargetID = req.TargetID
	relationship.Type = req.Type

	return &relationship, nil
}

func (r *RelationshipRepository) GetRelationshipBetween(ctx context.Context, playerID1, playerID2 uuid.UUID) (*models.Relationship, error) {
	query := `
		SELECT id, player_id, target_id, type, created_at, updated_at
		FROM social.relationships
		WHERE player_id = $1 AND target_id = $2`

	var relationship models.Relationship
	err := r.db.QueryRow(ctx, query, playerID1, playerID2).Scan(
		&relationship.ID, &relationship.PlayerID, &relationship.TargetID,
		&relationship.Type, &relationship.CreatedAt, &relationship.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get relationship: %w", err)
	}

	return &relationship, nil
}

func (r *RelationshipRepository) GetTrustLevel(ctx context.Context, playerID, targetID uuid.UUID) (*models.TrustLevel, error) {
	query := `
		SELECT player_id, target_id, level, experience, updated_at
		FROM social.trust_levels
		WHERE player_id = $1 AND target_id = $2`

	var trust models.TrustLevel
	err := r.db.QueryRow(ctx, query, playerID, targetID).Scan(
		&trust.PlayerID, &trust.TargetID, &trust.Level, &trust.Experience, &trust.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get trust level: %w", err)
	}

	return &trust, nil
}

func (r *RelationshipRepository) UpdateTrust(ctx context.Context, playerID uuid.UUID, req *models.UpdateTrustRequest) (*models.TrustLevel, error) {
	query := `
		INSERT INTO social.trust_levels (player_id, target_id, level, experience, updated_at)
		VALUES ($1, $2, GREATEST(0, LEAST(100, COALESCE((SELECT level FROM social.trust_levels WHERE player_id = $1 AND target_id = $2), 50) + $3)), 
		        COALESCE((SELECT experience FROM social.trust_levels WHERE player_id = $1 AND target_id = $2), 0) + ABS($3), NOW())
		ON CONFLICT (player_id, target_id)
		DO UPDATE SET 
			level = GREATEST(0, LEAST(100, social.trust_levels.level + $3)),
			experience = social.trust_levels.experience + ABS($3),
			updated_at = NOW()
		RETURNING player_id, target_id, level, experience, updated_at`

	var trust models.TrustLevel
	err := r.db.QueryRow(ctx, query, playerID, req.TargetID, req.Delta).Scan(
		&trust.PlayerID, &trust.TargetID, &trust.Level, &trust.Experience, &trust.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to update trust: %w", err)
	}

	return &trust, nil
}

func (r *RelationshipRepository) CreateTrustContract(ctx context.Context, playerID uuid.UUID, req *models.CreateTrustContractRequest) (*models.TrustContract, error) {
	termsJSON, err := json.Marshal(req.Terms)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal terms JSON")
		return nil, fmt.Errorf("failed to marshal terms: %w", err)
	}

	query := `
		INSERT INTO social.trust_contracts (id, player_id, target_id, terms, status, created_at, expires_at)
		VALUES (gen_random_uuid(), $1, $2, $3, 'active', NOW(), $4)
		RETURNING id, created_at, expires_at`

	var contract models.TrustContract
	err = r.db.QueryRow(ctx, query, playerID, req.TargetID, termsJSON, req.ExpiresAt).Scan(
		&contract.ID, &contract.CreatedAt, &contract.ExpiresAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to create trust contract: %w", err)
	}

	contract.PlayerID = playerID
	contract.TargetID = req.TargetID
	contract.Terms = req.Terms
	contract.Status = "active"

	return &contract, nil
}

func (r *RelationshipRepository) GetTrustContract(ctx context.Context, contractID uuid.UUID) (*models.TrustContract, error) {
	query := `
		SELECT id, player_id, target_id, terms, status, created_at, expires_at, terminated_at
		FROM social.trust_contracts
		WHERE id = $1`

	var contract models.TrustContract
	var termsJSON []byte
	err := r.db.QueryRow(ctx, query, contractID).Scan(
		&contract.ID, &contract.PlayerID, &contract.TargetID, &termsJSON,
		&contract.Status, &contract.CreatedAt, &contract.ExpiresAt, &contract.TerminatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get trust contract: %w", err)
	}

	if err := json.Unmarshal(termsJSON, &contract.Terms); err != nil {
		r.logger.WithError(err).WithField("contract_id", contractID).Error("Failed to unmarshal terms JSON")
		return nil, fmt.Errorf("failed to unmarshal terms: %w", err)
	}

	return &contract, nil
}

func (r *RelationshipRepository) TerminateTrustContract(ctx context.Context, contractID uuid.UUID) error {
	query := `
		UPDATE social.trust_contracts
		SET status = 'terminated', terminated_at = NOW()
		WHERE id = $1 AND status = 'active'`

	result, err := r.db.Exec(ctx, query, contractID)
	if err != nil {
		return fmt.Errorf("failed to terminate trust contract: %w", err)
	}

	if result.RowsAffected() == 0 {
		return pgx.ErrNoRows
	}

	return nil
}

