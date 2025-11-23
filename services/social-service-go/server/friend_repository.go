package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
)

type FriendRepository struct {
	db *pgxpool.Pool
}

func NewFriendRepository(db *pgxpool.Pool) *FriendRepository {
	return &FriendRepository{db: db}
}

func (r *FriendRepository) CreateRequest(ctx context.Context, fromCharacterID, toCharacterID uuid.UUID) (*models.Friendship, error) {
	characterAID := fromCharacterID
	characterBID := toCharacterID
	if fromCharacterID.String() > toCharacterID.String() {
		characterAID = toCharacterID
		characterBID = fromCharacterID
	}

	friendship := &models.Friendship{
		ID:          uuid.New(),
		CharacterAID: characterAID,
		CharacterBID: characterBID,
		Status:      models.FriendshipStatusPending,
		InitiatorID: fromCharacterID,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	query := `
		INSERT INTO friendships (id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7)
		RETURNING id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
	`

	err := r.db.QueryRow(ctx, query,
		friendship.ID,
		friendship.CharacterAID,
		friendship.CharacterBID,
		friendship.Status,
		friendship.InitiatorID,
		friendship.CreatedAt,
		friendship.UpdatedAt,
	).Scan(
		&friendship.ID,
		&friendship.CharacterAID,
		&friendship.CharacterBID,
		&friendship.Status,
		&friendship.InitiatorID,
		&friendship.CreatedAt,
		&friendship.UpdatedAt,
	)

	if err != nil {
		return nil, err
	}

	return friendship, nil
}

func (r *FriendRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM friendships
		WHERE id = $1
	`

	var friendship models.Friendship
	err := r.db.QueryRow(ctx, query, id).Scan(
		&friendship.ID,
		&friendship.CharacterAID,
		&friendship.CharacterBID,
		&friendship.Status,
		&friendship.InitiatorID,
		&friendship.CreatedAt,
		&friendship.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &friendship, nil
}

func (r *FriendRepository) GetByCharacterID(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM friendships
		WHERE (character_a_id = $1 OR character_b_id = $1) AND status = $2
		ORDER BY updated_at DESC
	`

	rows, err := r.db.Query(ctx, query, characterID, models.FriendshipStatusAccepted)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var friendships []models.Friendship
	for rows.Next() {
		var friendship models.Friendship
		err := rows.Scan(
			&friendship.ID,
			&friendship.CharacterAID,
			&friendship.CharacterBID,
			&friendship.Status,
			&friendship.InitiatorID,
			&friendship.CreatedAt,
			&friendship.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		friendships = append(friendships, friendship)
	}

	return friendships, rows.Err()
}

func (r *FriendRepository) GetPendingRequests(ctx context.Context, characterID uuid.UUID) ([]models.Friendship, error) {
	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM friendships
		WHERE (character_a_id = $1 OR character_b_id = $1) 
		  AND status = $2 
		  AND initiator_id != $1
		ORDER BY created_at DESC
	`

	rows, err := r.db.Query(ctx, query, characterID, models.FriendshipStatusPending)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var friendships []models.Friendship
	for rows.Next() {
		var friendship models.Friendship
		err := rows.Scan(
			&friendship.ID,
			&friendship.CharacterAID,
			&friendship.CharacterBID,
			&friendship.Status,
			&friendship.InitiatorID,
			&friendship.CreatedAt,
			&friendship.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		friendships = append(friendships, friendship)
	}

	return friendships, rows.Err()
}

func (r *FriendRepository) GetFriendship(ctx context.Context, characterAID, characterBID uuid.UUID) (*models.Friendship, error) {
	characterA := characterAID
	characterB := characterBID
	if characterAID.String() > characterBID.String() {
		characterA = characterBID
		characterB = characterAID
	}

	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM friendships
		WHERE character_a_id = $1 AND character_b_id = $2
		LIMIT 1
	`

	var friendship models.Friendship
	err := r.db.QueryRow(ctx, query, characterA, characterB).Scan(
		&friendship.ID,
		&friendship.CharacterAID,
		&friendship.CharacterBID,
		&friendship.Status,
		&friendship.InitiatorID,
		&friendship.CreatedAt,
		&friendship.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &friendship, nil
}

func (r *FriendRepository) AcceptRequest(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	query := `
		UPDATE friendships
		SET status = $1, updated_at = NOW()
		WHERE id = $2 AND status = $3
		RETURNING id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
	`

	var friendship models.Friendship
	err := r.db.QueryRow(ctx, query, models.FriendshipStatusAccepted, id, models.FriendshipStatusPending).Scan(
		&friendship.ID,
		&friendship.CharacterAID,
		&friendship.CharacterBID,
		&friendship.Status,
		&friendship.InitiatorID,
		&friendship.CreatedAt,
		&friendship.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &friendship, nil
}

func (r *FriendRepository) Block(ctx context.Context, id uuid.UUID) (*models.Friendship, error) {
	query := `
		UPDATE friendships
		SET status = $1, updated_at = NOW()
		WHERE id = $2
		RETURNING id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
	`

	var friendship models.Friendship
	err := r.db.QueryRow(ctx, query, models.FriendshipStatusBlocked, id).Scan(
		&friendship.ID,
		&friendship.CharacterAID,
		&friendship.CharacterBID,
		&friendship.Status,
		&friendship.InitiatorID,
		&friendship.CreatedAt,
		&friendship.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &friendship, nil
}

func (r *FriendRepository) Delete(ctx context.Context, id uuid.UUID) error {
	query := `DELETE FROM friendships WHERE id = $1`
	_, err := r.db.Exec(ctx, query, id)
	return err
}

