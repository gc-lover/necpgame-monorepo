// Issue: Social Service ogen Migration - Friends Repository
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

type FriendRepositoryInterface interface {
	GetFriends(ctx context.Context, characterID uuid.UUID, onlineOnly bool, limit, offset int) ([]models.Friendship, error)
	GetFriend(ctx context.Context, characterID, friendID uuid.UUID) (*models.Friendship, error)
	GetFriendsCount(ctx context.Context, characterID uuid.UUID) (int, error)
	GetOnlineFriends(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.Friendship, error)
	RemoveFriend(ctx context.Context, characterID, friendID uuid.UUID) error
}

type FriendRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewFriendRepository(db *pgxpool.Pool) *FriendRepository {
	logger := GetLogger()
	if logger == nil {
		// Fallback if logger not initialized
		logger = logrus.New()
	}
	return &FriendRepository{
		db:     db,
		logger: logger,
	}
}

// GetFriends returns list of friends for a character
// Uses covering index: (character_a_id, status) and (character_b_id, status)
func (r *FriendRepository) GetFriends(ctx context.Context, characterID uuid.UUID, onlineOnly bool, limit, offset int) ([]models.Friendship, error) {
	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM mvp_core.friendships
		WHERE (character_a_id = $1 OR character_b_id = $1)
			AND status = 'accepted'
			AND deleted_at IS NULL
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, characterID, limit, offset)
	if err != nil {
		r.logger.WithError(err).WithField("character_id", characterID).Error("Failed to get friends")
		return nil, fmt.Errorf("failed to get friends: %w", err)
	}
	defer rows.Close()

	var friendships []models.Friendship
	for rows.Next() {
		var f models.Friendship
		if err := rows.Scan(
			&f.ID, &f.CharacterAID, &f.CharacterBID, &f.Status,
			&f.InitiatorID, &f.CreatedAt, &f.UpdatedAt,
		); err != nil {
			r.logger.WithError(err).Error("Failed to scan friendship")
			return nil, fmt.Errorf("failed to scan friendship: %w", err)
		}
		friendships = append(friendships, f)
	}

	if err := rows.Err(); err != nil {
		r.logger.WithError(err).Error("Failed to iterate friends")
		return nil, fmt.Errorf("failed to iterate friends: %w", err)
	}

	// Filter online friends if requested
	if onlineOnly {
		// TODO: Join with online status from Redis/cache
		// For now, return all (will be filtered in service layer with cache)
	}

	return friendships, nil
}

// GetFriend returns specific friendship between two characters
func (r *FriendRepository) GetFriend(ctx context.Context, characterID, friendID uuid.UUID) (*models.Friendship, error) {
	// Ensure consistent ordering: character_a_id < character_b_id
	charA, charB := characterID, friendID
	if characterID.String() > friendID.String() {
		charA, charB = friendID, characterID
	}

	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM mvp_core.friendships
		WHERE character_a_id = $1 AND character_b_id = $2
			AND deleted_at IS NULL
		LIMIT 1`

	f := &models.Friendship{}
	err := r.db.QueryRow(ctx, query, charA, charB).Scan(
		&f.ID, &f.CharacterAID, &f.CharacterBID, &f.Status,
		&f.InitiatorID, &f.CreatedAt, &f.UpdatedAt,
	)
	if err != nil {
		if err == pgx.ErrNoRows {
			return nil, nil
		}
		r.logger.WithError(err).WithFields(logrus.Fields{
			"character_id": characterID,
			"friend_id":    friendID,
		}).Error("Failed to get friend")
		return nil, fmt.Errorf("failed to get friend: %w", err)
	}

	return f, nil
}

// GetFriendsCount returns total count of accepted friends
func (r *FriendRepository) GetFriendsCount(ctx context.Context, characterID uuid.UUID) (int, error) {
	query := `
		SELECT COUNT(*)
		FROM mvp_core.friendships
		WHERE (character_a_id = $1 OR character_b_id = $1)
			AND status = 'accepted'
			AND deleted_at IS NULL`

	var count int
	err := r.db.QueryRow(ctx, query, characterID).Scan(&count)
	if err != nil {
		r.logger.WithError(err).WithField("character_id", characterID).Error("Failed to get friends count")
		return 0, fmt.Errorf("failed to get friends count: %w", err)
	}

	return count, nil
}

// GetOnlineFriends returns online friends (requires join with online status)
// TODO: Optimize with Redis ZSET for online users
func (r *FriendRepository) GetOnlineFriends(ctx context.Context, characterID uuid.UUID, limit, offset int) ([]models.Friendship, error) {
	// For now, return all friends (online filtering in service layer with cache)
	// Future: JOIN with online status table or Redis lookup
	query := `
		SELECT id, character_a_id, character_b_id, status, initiator_id, created_at, updated_at
		FROM mvp_core.friendships
		WHERE (character_a_id = $1 OR character_b_id = $1)
			AND status = 'accepted'
			AND deleted_at IS NULL
		ORDER BY updated_at DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, characterID, limit, offset)
	if err != nil {
		r.logger.WithError(err).WithField("character_id", characterID).Error("Failed to get online friends")
		return nil, fmt.Errorf("failed to get online friends: %w", err)
	}
	defer rows.Close()

	var friendships []models.Friendship
	for rows.Next() {
		var f models.Friendship
		if err := rows.Scan(
			&f.ID, &f.CharacterAID, &f.CharacterBID, &f.Status,
			&f.InitiatorID, &f.CreatedAt, &f.UpdatedAt,
		); err != nil {
			r.logger.WithError(err).Error("Failed to scan friendship")
			return nil, fmt.Errorf("failed to scan friendship: %w", err)
		}
		friendships = append(friendships, f)
	}

	if err := rows.Err(); err != nil {
		r.logger.WithError(err).Error("Failed to iterate online friends")
		return nil, fmt.Errorf("failed to iterate online friends: %w", err)
	}

	return friendships, nil
}

// RemoveFriend soft-deletes friendship (sets deleted_at)
func (r *FriendRepository) RemoveFriend(ctx context.Context, characterID, friendID uuid.UUID) error {
	// Ensure consistent ordering
	charA, charB := characterID, friendID
	if characterID.String() > friendID.String() {
		charA, charB = friendID, characterID
	}

	query := `
		UPDATE mvp_core.friendships
		SET deleted_at = $1, updated_at = $1
		WHERE character_a_id = $2 AND character_b_id = $3
			AND deleted_at IS NULL`

	result, err := r.db.Exec(ctx, query, time.Now(), charA, charB)
	if err != nil {
		r.logger.WithError(err).WithFields(logrus.Fields{
			"character_id": characterID,
			"friend_id":    friendID,
		}).Error("Failed to remove friend")
		return fmt.Errorf("failed to remove friend: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("friendship not found")
	}

	return nil
}
