// Issue: #1636
package server

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
)

// BattlePassRepository handles database operations for battle pass
type BattlePassRepository struct {
	db *pgxpool.Pool
}

// NewBattlePassRepository creates new repository
func NewBattlePassRepository(db *pgxpool.Pool) *BattlePassRepository {
	return &BattlePassRepository{db: db}
}

// BattlePassSeason represents a battle pass season
type BattlePassSeason struct {
	ID          uuid.UUID `db:"id"`
	SeasonNumber int      `db:"season_number"`
	Name        string    `db:"name"`
	Description string    `db:"description"`
	StartDate   time.Time `db:"start_date"`
	EndDate     time.Time `db:"end_date"`
	MaxLevel    int       `db:"max_level"`
	IsActive    bool      `db:"is_active"`
	CreatedAt   time.Time `db:"created_at"`
	UpdatedAt   time.Time `db:"updated_at"`
}

// PlayerProgress represents player's battle pass progress
type PlayerProgress struct {
	ID           uuid.UUID `db:"id"`
	CharacterID  uuid.UUID `db:"character_id"`
	SeasonID     uuid.UUID `db:"season_id"`
	CurrentLevel int       `db:"current_level"`
	CurrentXP    int       `db:"current_xp"`
	TotalXP      int       `db:"total_xp"`
	IsPremium    bool      `db:"is_premium"`
	ClaimedRewards []int   `db:"claimed_rewards"`
	CreatedAt    time.Time `db:"created_at"`
	UpdatedAt    time.Time `db:"updated_at"`
}

// GetCurrentSeason returns the active battle pass season
func (r *BattlePassRepository) GetCurrentSeason(ctx context.Context) (*BattlePassSeason, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		SELECT id, season_number, name, description, start_date, end_date, 
		       max_level, is_active, created_at, updated_at
		FROM battle_pass.seasons
		WHERE is_active = true
		AND start_date <= NOW()
		AND end_date >= NOW()
		LIMIT 1
	`

	var season BattlePassSeason
	row := r.db.QueryRow(ctx, query)
	err := row.Scan(
		&season.ID, &season.SeasonNumber, &season.Name, &season.Description,
		&season.StartDate, &season.EndDate, &season.MaxLevel, &season.IsActive,
		&season.CreatedAt, &season.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, fmt.Errorf("no active season found")
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get current season: %w", err)
	}

	return &season, nil
}

// GetPlayerProgress returns player's progress for a specific season
func (r *BattlePassRepository) GetPlayerProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*PlayerProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		SELECT id, character_id, season_id, current_level, current_xp, 
		       total_xp, is_premium, claimed_rewards, created_at, updated_at
		FROM battle_pass.player_progress
		WHERE character_id = $1 AND season_id = $2
	`

	var progress PlayerProgress
	row := r.db.QueryRow(ctx, query, characterID, seasonID)
	err := row.Scan(
		&progress.ID, &progress.CharacterID, &progress.SeasonID,
		&progress.CurrentLevel, &progress.CurrentXP, &progress.TotalXP,
		&progress.IsPremium, &progress.ClaimedRewards,
		&progress.CreatedAt, &progress.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		// Create new progress entry
		return r.CreatePlayerProgress(ctx, characterID, seasonID)
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get player progress: %w", err)
	}

	return &progress, nil
}

// CreatePlayerProgress creates new progress entry for player
func (r *BattlePassRepository) CreatePlayerProgress(ctx context.Context, characterID, seasonID uuid.UUID) (*PlayerProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		INSERT INTO battle_pass.player_progress 
		(id, character_id, season_id, current_level, current_xp, total_xp, is_premium, claimed_rewards)
		VALUES ($1, $2, $3, 1, 0, 0, false, '{}')
		RETURNING id, character_id, season_id, current_level, current_xp, 
		          total_xp, is_premium, claimed_rewards, created_at, updated_at
	`

	var progress PlayerProgress
	row := r.db.QueryRow(ctx, query, uuid.New(), characterID, seasonID)
	err := row.Scan(
		&progress.ID, &progress.CharacterID, &progress.SeasonID,
		&progress.CurrentLevel, &progress.CurrentXP, &progress.TotalXP,
		&progress.IsPremium, &progress.ClaimedRewards,
		&progress.CreatedAt, &progress.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to create player progress: %w", err)
	}

	return &progress, nil
}

// AddXP adds experience points to player's progress
func (r *BattlePassRepository) AddXP(ctx context.Context, characterID uuid.UUID, xp int) (*PlayerProgress, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Get current season
	season, err := r.GetCurrentSeason(ctx)
	if err != nil {
		return nil, err
	}

	// Start transaction
	tx, err := r.db.Begin(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to start transaction: %w", err)
	}
	defer tx.Rollback(ctx)

	// Update XP and calculate new level
	query := `
		UPDATE battle_pass.player_progress
		SET current_xp = current_xp + $1,
		    total_xp = total_xp + $1,
		    current_level = LEAST(
		        1 + (total_xp + $1) / 1000, -- 1000 XP per level
		        $2 -- max level
		    ),
		    updated_at = NOW()
		WHERE character_id = $3 AND season_id = $4
		RETURNING id, character_id, season_id, current_level, current_xp, 
		          total_xp, is_premium, claimed_rewards, created_at, updated_at
	`

	var progress PlayerProgress
	row := tx.QueryRow(ctx, query, xp, season.MaxLevel, characterID, season.ID)
	err = row.Scan(
		&progress.ID, &progress.CharacterID, &progress.SeasonID,
		&progress.CurrentLevel, &progress.CurrentXP, &progress.TotalXP,
		&progress.IsPremium, &progress.ClaimedRewards,
		&progress.CreatedAt, &progress.UpdatedAt,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to add XP: %w", err)
	}

	// Commit transaction
	if err := tx.Commit(ctx); err != nil {
		return nil, fmt.Errorf("failed to commit transaction: %w", err)
	}

	return &progress, nil
}

// ClaimReward marks a reward as claimed
func (r *BattlePassRepository) ClaimReward(ctx context.Context, characterID uuid.UUID, level int) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Get current season
	season, err := r.GetCurrentSeason(ctx)
	if err != nil {
		return err
	}

	query := `
		UPDATE battle_pass.player_progress
		SET claimed_rewards = array_append(claimed_rewards, $1),
		    updated_at = NOW()
		WHERE character_id = $2 AND season_id = $3
		AND current_level >= $1
		AND NOT ($1 = ANY(claimed_rewards))
	`

	result, err := r.db.Exec(ctx, query, level, characterID, season.ID)
	if err != nil {
		return fmt.Errorf("failed to claim reward: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("reward already claimed or level not reached")
	}

	return nil
}

// UpgradeToPremium upgrades player's battle pass to premium
func (r *BattlePassRepository) UpgradeToPremium(ctx context.Context, characterID uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	// Get current season
	season, err := r.GetCurrentSeason(ctx)
	if err != nil {
		return err
	}

	query := `
		UPDATE battle_pass.player_progress
		SET is_premium = true,
		    updated_at = NOW()
		WHERE character_id = $1 AND season_id = $2
		AND is_premium = false
	`

	result, err := r.db.Exec(ctx, query, characterID, season.ID)
	if err != nil {
		return fmt.Errorf("failed to upgrade to premium: %w", err)
	}

	if result.RowsAffected() == 0 {
		return fmt.Errorf("already premium or player not found")
	}

	return nil
}









