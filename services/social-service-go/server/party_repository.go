package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type PartyRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewPartyRepository(db *pgxpool.Pool) *PartyRepository {
	return &PartyRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *PartyRepository) Create(ctx context.Context, leaderID uuid.UUID, req *models.CreatePartyRequest) (*models.Party, error) {
	partyID := uuid.New()
	maxSize := 5
	if req.MaxSize != nil {
		maxSize = *req.MaxSize
	}
	lootMode := models.LootModeFreeForAll
	if req.LootMode != nil {
		lootMode = *req.LootMode
	}

	now := time.Now()
	query := `
		INSERT INTO social.parties (
			id, leader_id, max_size, loot_mode, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6
		) RETURNING id, leader_id, max_size, loot_mode, created_at, updated_at`

	party := &models.Party{}
	err := r.db.QueryRow(ctx, query,
		partyID, leaderID, maxSize, lootMode, now, now,
	).Scan(
		&party.ID, &party.LeaderID, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to create party")
		return nil, fmt.Errorf("failed to create party: %w", err)
	}

	memberQuery := `
		INSERT INTO social.party_members (
			party_id, character_id, role, joined_at
		) VALUES (
			$1, $2, $3, $4
		)`

	_, err = r.db.Exec(ctx, memberQuery,
		partyID, leaderID, models.PartyRoleLeader, now,
	)
	if err != nil {
		r.logger.WithError(err).Error("Failed to add leader to party")
		return nil, fmt.Errorf("failed to add leader to party: %w", err)
	}

	members, err := r.GetMembers(ctx, partyID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get party members")
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	party.Members = members

	return party, nil
}

func (r *PartyRepository) GetByID(ctx context.Context, partyID uuid.UUID) (*models.Party, error) {
	query := `
		SELECT id, leader_id, max_size, loot_mode, created_at, updated_at
		FROM social.parties
		WHERE id = $1`

	party := &models.Party{}
	err := r.db.QueryRow(ctx, query, partyID).Scan(
		&party.ID, &party.LeaderID, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt,
	)
	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).WithField("party_id", partyID).Error("Failed to get party")
		return nil, fmt.Errorf("failed to get party: %w", err)
	}

	members, err := r.GetMembers(ctx, partyID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get party members")
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	party.Members = members

	return party, nil
}

func (r *PartyRepository) GetByPlayerID(ctx context.Context, accountID uuid.UUID) (*models.Party, error) {
	query := `
		SELECT p.id, p.leader_id, p.max_size, p.loot_mode, p.created_at, p.updated_at
		FROM social.parties p
		INNER JOIN social.party_members pm ON p.id = pm.party_id
		WHERE pm.character_id = $1
		LIMIT 1`

	party := &models.Party{}
	err := r.db.QueryRow(ctx, query, accountID).Scan(
		&party.ID, &party.LeaderID, &party.MaxSize, &party.LootMode,
		&party.CreatedAt, &party.UpdatedAt,
	)
	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).WithField("account_id", accountID).Error("Failed to get party by player")
		return nil, fmt.Errorf("failed to get party by player: %w", err)
	}

	members, err := r.GetMembers(ctx, party.ID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to get party members")
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	party.Members = members

	return party, nil
}

func (r *PartyRepository) GetMembers(ctx context.Context, partyID uuid.UUID) ([]models.PartyMember, error) {
	query := `
		SELECT character_id, role, joined_at
		FROM social.party_members
		WHERE party_id = $1
		ORDER BY joined_at ASC`

	rows, err := r.db.Query(ctx, query, partyID)
	if err != nil {
		r.logger.WithError(err).WithField("party_id", partyID).Error("Failed to get party members")
		return nil, fmt.Errorf("failed to get party members: %w", err)
	}
	defer rows.Close()

	var members []models.PartyMember
	for rows.Next() {
		var member models.PartyMember
		if err := rows.Scan(&member.CharacterID, &member.Role, &member.JoinedAt); err != nil {
			r.logger.WithError(err).Error("Failed to scan party member")
			return nil, fmt.Errorf("failed to scan party member: %w", err)
		}
		members = append(members, member)
	}

	if err := rows.Err(); err != nil {
		r.logger.WithError(err).Error("Failed to iterate party members")
		return nil, fmt.Errorf("failed to iterate party members: %w", err)
	}

	return members, nil
}

func (r *PartyRepository) GetLeader(ctx context.Context, partyID uuid.UUID) (*models.PartyMember, error) {
	query := `
		SELECT character_id, role, joined_at
		FROM social.party_members
		WHERE party_id = $1 AND role = $2
		LIMIT 1`

	member := &models.PartyMember{}
	err := r.db.QueryRow(ctx, query, partyID, models.PartyRoleLeader).Scan(
		&member.CharacterID, &member.Role, &member.JoinedAt,
	)
	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).WithField("party_id", partyID).Error("Failed to get party leader")
		return nil, fmt.Errorf("failed to get party leader: %w", err)
	}

	return member, nil
}

func (r *PartyRepository) TransferLeadership(ctx context.Context, partyID, newLeaderID uuid.UUID) error {
	tx, err := r.db.Begin(ctx)
	if err != nil {
		r.logger.WithError(err).Error("Failed to begin transaction")
		return fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback(ctx)

	updateLeaderQuery := `
		UPDATE social.party_members
		SET role = $1
		WHERE party_id = $2 AND role = $3`

	_, err = tx.Exec(ctx, updateLeaderQuery, models.PartyRoleMember, partyID, models.PartyRoleLeader)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update old leader")
		return fmt.Errorf("failed to update old leader: %w", err)
	}

	updateNewLeaderQuery := `
		UPDATE social.party_members
		SET role = $1
		WHERE party_id = $2 AND character_id = $3`

	result, err := tx.Exec(ctx, updateNewLeaderQuery, models.PartyRoleLeader, partyID, newLeaderID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update new leader")
		return fmt.Errorf("failed to update new leader: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("new leader not found in party")
	}

	updatePartyQuery := `
		UPDATE social.parties
		SET leader_id = $1, updated_at = $2
		WHERE id = $3`

	_, err = tx.Exec(ctx, updatePartyQuery, newLeaderID, time.Now(), partyID)
	if err != nil {
		r.logger.WithError(err).Error("Failed to update party leader_id")
		return fmt.Errorf("failed to update party leader_id: %w", err)
	}

	if err := tx.Commit(ctx); err != nil {
		r.logger.WithError(err).Error("Failed to commit transaction")
		return fmt.Errorf("failed to commit transaction: %w", err)
	}

	return nil
}

