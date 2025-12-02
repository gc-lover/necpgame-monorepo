package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type RelationshipAllianceRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewRelationshipAllianceRepository(db *pgxpool.Pool) *RelationshipAllianceRepository {
	return &RelationshipAllianceRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *RelationshipAllianceRepository) CreateAlliance(ctx context.Context, leaderID uuid.UUID, req *models.CreateAllianceRequest) (*models.Alliance, error) {
	query := `
		INSERT INTO social.alliances (id, name, leader_id, description, status, created_at, updated_at)
		VALUES (gen_random_uuid(), $1, $2, $3, 'active', NOW(), NOW())
		RETURNING id, created_at, updated_at`

	var alliance models.Alliance
	err := r.db.QueryRow(ctx, query, req.Name, leaderID, req.Description).Scan(
		&alliance.ID, &alliance.CreatedAt, &alliance.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to create alliance: %w", err)
	}

	alliance.Name = req.Name
	alliance.LeaderID = leaderID
	alliance.Description = req.Description
	alliance.Status = "active"

	return &alliance, nil
}

func (r *RelationshipAllianceRepository) GetAlliances(ctx context.Context, limit, offset int) ([]models.Alliance, int, error) {
	countQuery := `SELECT COUNT(*) FROM social.alliances WHERE status = 'active'`
	var total int
	err := r.db.QueryRow(ctx, countQuery).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count alliances: %w", err)
	}

	query := `
		SELECT id, name, leader_id, description, status, created_at, updated_at, terminated_at
		FROM social.alliances
		WHERE status = 'active'
		ORDER BY created_at DESC
		LIMIT $1 OFFSET $2`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get alliances: %w", err)
	}
	defer rows.Close()

	var alliances []models.Alliance
	for rows.Next() {
		var alliance models.Alliance
		err := rows.Scan(
			&alliance.ID, &alliance.Name, &alliance.LeaderID, &alliance.Description,
			&alliance.Status, &alliance.CreatedAt, &alliance.UpdatedAt, &alliance.TerminatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan alliance: %w", err)
		}
		alliances = append(alliances, alliance)
	}

	return alliances, total, nil
}

func (r *RelationshipAllianceRepository) GetAlliance(ctx context.Context, allianceID uuid.UUID) (*models.Alliance, error) {
	query := `
		SELECT id, name, leader_id, description, status, created_at, updated_at, terminated_at
		FROM social.alliances
		WHERE id = $1`

	var alliance models.Alliance
	err := r.db.QueryRow(ctx, query, allianceID).Scan(
		&alliance.ID, &alliance.Name, &alliance.LeaderID, &alliance.Description,
		&alliance.Status, &alliance.CreatedAt, &alliance.UpdatedAt, &alliance.TerminatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get alliance: %w", err)
	}

	return &alliance, nil
}

func (r *RelationshipAllianceRepository) TerminateAlliance(ctx context.Context, allianceID uuid.UUID) error {
	query := `
		UPDATE social.alliances
		SET status = 'terminated', terminated_at = NOW(), updated_at = NOW()
		WHERE id = $1 AND status = 'active'`

	result, err := r.db.Exec(ctx, query, allianceID)
	if err != nil {
		return fmt.Errorf("failed to terminate alliance: %w", err)
	}

	if result.RowsAffected() == 0 {
		return pgx.ErrNoRows
	}

	return nil
}

func (r *RelationshipAllianceRepository) InviteToAlliance(ctx context.Context, allianceID, inviterID uuid.UUID, req *models.AllianceInviteRequest) error {
	query := `
		INSERT INTO social.alliance_invitations (id, alliance_id, inviter_id, player_id, status, created_at)
		VALUES (gen_random_uuid(), $1, $2, $3, 'pending', NOW())`

	_, err := r.db.Exec(ctx, query, allianceID, inviterID, req.PlayerID)
	if err != nil {
		return fmt.Errorf("failed to invite to alliance: %w", err)
	}

	return nil
}

func (r *RelationshipAllianceRepository) JoinAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error {
	query := `
		INSERT INTO social.alliance_members (id, alliance_id, player_id, role, joined_at)
		VALUES (gen_random_uuid(), $1, $2, 'member', NOW())
		ON CONFLICT (alliance_id, player_id) DO NOTHING`

	_, err := r.db.Exec(ctx, query, allianceID, playerID)
	if err != nil {
		return fmt.Errorf("failed to join alliance: %w", err)
	}

	return nil
}

func (r *RelationshipAllianceRepository) LeaveAlliance(ctx context.Context, allianceID, playerID uuid.UUID) error {
	query := `DELETE FROM social.alliance_members WHERE alliance_id = $1 AND player_id = $2`

	result, err := r.db.Exec(ctx, query, allianceID, playerID)
	if err != nil {
		return fmt.Errorf("failed to leave alliance: %w", err)
	}

	if result.RowsAffected() == 0 {
		return pgx.ErrNoRows
	}

	return nil
}

func (r *RelationshipAllianceRepository) GetPlayerRatings(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.PlayerRating, int, error) {
	countQuery := `SELECT COUNT(*) FROM social.player_ratings WHERE player_id = $1`
	var total int
	err := r.db.QueryRow(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count ratings: %w", err)
	}

	query := `
		SELECT player_id, rater_id, rating, comment, updated_at
		FROM social.player_ratings
		WHERE player_id = $1
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get player ratings: %w", err)
	}
	defer rows.Close()

	var ratings []models.PlayerRating
	for rows.Next() {
		var rating models.PlayerRating
		err := rows.Scan(
			&rating.PlayerID, &rating.RaterID, &rating.Rating,
			&rating.Comment, &rating.UpdatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan rating: %w", err)
		}
		ratings = append(ratings, rating)
	}

	return ratings, total, nil
}

func (r *RelationshipAllianceRepository) UpdateRating(ctx context.Context, playerID uuid.UUID, req *models.UpdateRatingRequest) (*models.PlayerRating, error) {
	query := `
		INSERT INTO social.player_ratings (player_id, rater_id, rating, comment, updated_at)
		VALUES ($1, $2, $3, $4, NOW())
		ON CONFLICT (player_id, rater_id)
		DO UPDATE SET rating = $3, comment = $4, updated_at = NOW()
		RETURNING updated_at`

	var rating models.PlayerRating
	err := r.db.QueryRow(ctx, query, playerID, req.RaterID, req.Rating, req.Comment).Scan(
		&rating.UpdatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to update rating: %w", err)
	}

	rating.PlayerID = playerID
	rating.RaterID = req.RaterID
	rating.Rating = req.Rating
	rating.Comment = req.Comment

	return &rating, nil
}

func (r *RelationshipAllianceRepository) GetSocialCapital(ctx context.Context, playerID uuid.UUID) (*models.SocialCapital, error) {
	query := `
		SELECT player_id, capital, positive_actions, negative_actions, updated_at
		FROM social.social_capital
		WHERE player_id = $1`

	var capital models.SocialCapital
	err := r.db.QueryRow(ctx, query, playerID).Scan(
		&capital.PlayerID, &capital.Capital, &capital.PositiveActions,
		&capital.NegativeActions, &capital.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get social capital: %w", err)
	}

	return &capital, nil
}

func (r *RelationshipAllianceRepository) GetInteractionHistory(ctx context.Context, playerID uuid.UUID, limit, offset int) ([]models.InteractionHistory, int, error) {
	countQuery := `SELECT COUNT(*) FROM social.interaction_history WHERE player_id = $1`
	var total int
	err := r.db.QueryRow(ctx, countQuery, playerID).Scan(&total)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to count interactions: %w", err)
	}

	query := `
		SELECT id, player_id, target_id, interaction_type, description, created_at
		FROM social.interaction_history
		WHERE player_id = $1
		ORDER BY created_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, playerID, limit, offset)
	if err != nil {
		return nil, 0, fmt.Errorf("failed to get interaction history: %w", err)
	}
	defer rows.Close()

	var interactions []models.InteractionHistory
	for rows.Next() {
		var interaction models.InteractionHistory
		err := rows.Scan(
			&interaction.ID, &interaction.PlayerID, &interaction.TargetID,
			&interaction.InteractionType, &interaction.Description, &interaction.CreatedAt,
		)
		if err != nil {
			return nil, 0, fmt.Errorf("failed to scan interaction: %w", err)
		}
		interactions = append(interactions, interaction)
	}

	return interactions, total, nil
}

func (r *RelationshipAllianceRepository) RequestArbitration(ctx context.Context, requesterID uuid.UUID, req *models.RequestArbitrationRequest) (*models.ArbitrationCase, error) {
	query := `
		INSERT INTO social.arbitration_cases (id, requester_id, target_id, issue, status, created_at)
		VALUES (gen_random_uuid(), $1, $2, $3, 'pending', NOW())
		RETURNING id, created_at`

	var case_ models.ArbitrationCase
	err := r.db.QueryRow(ctx, query, requesterID, req.TargetID, req.Issue).Scan(
		&case_.ID, &case_.CreatedAt,
	)
	if err != nil {
		return nil, fmt.Errorf("failed to request arbitration: %w", err)
	}

	case_.RequesterID = requesterID
	case_.TargetID = req.TargetID
	case_.Issue = req.Issue
	case_.Status = "pending"

	return &case_, nil
}

func (r *RelationshipAllianceRepository) GetArbitrationCase(ctx context.Context, caseID uuid.UUID) (*models.ArbitrationCase, error) {
	query := `
		SELECT id, requester_id, target_id, issue, status, created_at, resolved_at
		FROM social.arbitration_cases
		WHERE id = $1`

	var case_ models.ArbitrationCase
	err := r.db.QueryRow(ctx, query, caseID).Scan(
		&case_.ID, &case_.RequesterID, &case_.TargetID, &case_.Issue,
		&case_.Status, &case_.CreatedAt, &case_.ResolvedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get arbitration case: %w", err)
	}

	return &case_, nil
}

