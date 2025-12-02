// Issue: #138
package server

import (
	"context"
	"database/sql"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/achievement-service-go/pkg/api"
	"github.com/google/uuid"
)

type Repository struct {
	db *sql.DB
}

func NewRepository(db *sql.DB) *Repository {
	return &Repository{db: db}
}

func (r *Repository) GetAchievements(ctx context.Context, params api.GetAchievementsParams) ([]api.Achievement, error) {
	query := `
		SELECT id, name, description, category, type, icon_url, required_count, 
		       reward_currency, reward_amount, hidden
		FROM achievement_definitions
		WHERE ($1::text IS NULL OR category = $1)
		  AND ($2::text IS NULL OR type = $2)
		  AND (hidden = false OR $3 = true)
		ORDER BY category, name
	`

	var category, achType *string
	includeHidden := false

	if params.Category != nil {
		cat := string(*params.Category)
		category = &cat
	}
	if params.Type != nil {
		t := string(*params.Type)
		achType = &t
	}
	if params.IncludeHidden != nil {
		includeHidden = *params.IncludeHidden
	}

	rows, err := r.db.QueryContext(ctx, query, category, achType, includeHidden)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var achievements []api.Achievement
	for rows.Next() {
		var ach api.Achievement
		err := rows.Scan(
			&ach.Id, &ach.Name, &ach.Description, &ach.Category, &ach.Type,
			&ach.IconUrl, &ach.RequiredCount, &ach.RewardCurrency, &ach.RewardAmount, &ach.Hidden,
		)
		if err != nil {
			return nil, err
		}
		achievements = append(achievements, ach)
	}

	return achievements, nil
}

func (r *Repository) GetAchievementDetails(ctx context.Context, achievementId string) (*api.AchievementDetails, error) {
	query := `
		SELECT id, name, description, category, type, icon_url, required_count,
		       reward_currency, reward_amount, reward_items, title_id, hidden,
		       (SELECT COUNT(*) FROM player_achievement_progress WHERE achievement_id = $1 AND unlocked_at IS NOT NULL) as unlocked_by_count
		FROM achievement_definitions
		WHERE id = $1
	`

	var details api.AchievementDetails
	err := r.db.QueryRowContext(ctx, query, achievementId).Scan(
		&details.Id, &details.Name, &details.Description, &details.Category, &details.Type,
		&details.IconUrl, &details.RequiredCount, &details.RewardCurrency, &details.RewardAmount,
		&details.RewardItems, &details.TitleId, &details.Hidden, &details.UnlockedByCount,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &details, nil
}

func (r *Repository) GetPlayerProgress(ctx context.Context, playerId string, params api.GetPlayerProgressParams) ([]api.PlayerAchievementProgress, error) {
	query := `
		SELECT 
			p.achievement_id,
			p.current_count,
			p.unlocked_at,
			p.claimed_at,
			a.required_count,
			a.id, a.name, a.description, a.category, a.type, a.icon_url, a.required_count,
			a.reward_currency, a.reward_amount, a.hidden
		FROM player_achievement_progress p
		JOIN achievement_definitions a ON p.achievement_id = a.id
		WHERE p.player_id = $1
		  AND ($2 = false OR p.unlocked_at IS NOT NULL)
		  AND ($3 = false OR p.claimed_at IS NOT NULL)
		ORDER BY a.category, a.name
	`

	unlockedOnly := false
	claimedOnly := false
	if params.UnlockedOnly != nil {
		unlockedOnly = *params.UnlockedOnly
	}
	if params.ClaimedOnly != nil {
		claimedOnly = *params.ClaimedOnly
	}

	rows, err := r.db.QueryContext(ctx, query, playerId, unlockedOnly, claimedOnly)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var progress []api.PlayerAchievementProgress
	for rows.Next() {
		var p api.PlayerAchievementProgress
		var ach api.Achievement
		var reqCount int

		err := rows.Scan(
			&p.AchievementId, &p.CurrentCount, &p.UnlockedAt, &p.ClaimedAt, &reqCount,
			&ach.Id, &ach.Name, &ach.Description, &ach.Category, &ach.Type,
			&ach.IconUrl, &ach.RequiredCount, &ach.RewardCurrency, &ach.RewardAmount, &ach.Hidden,
		)
		if err != nil {
			return nil, err
		}

		p.Achievement = &ach
		p.RequiredCount = &reqCount

		// Calculate progress percent
		if reqCount > 0 {
			percent := float32(p.CurrentCount) / float32(reqCount) * 100
			p.ProgressPercent = &percent
		}

		progress = append(progress, p)
	}

	return progress, nil
}

func (r *Repository) GetPlayerAchievementProgress(ctx context.Context, playerId, achievementId string) (*api.PlayerAchievementProgress, error) {
	query := `
		SELECT achievement_id, current_count, unlocked_at, claimed_at
		FROM player_achievement_progress
		WHERE player_id = $1 AND achievement_id = $2
	`

	var p api.PlayerAchievementProgress
	err := r.db.QueryRowContext(ctx, query, playerId, achievementId).Scan(
		&p.AchievementId, &p.CurrentCount, &p.UnlockedAt, &p.ClaimedAt,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &p, nil
}

func (r *Repository) MarkAsClaimed(ctx context.Context, playerId, achievementId string) error {
	query := `
		UPDATE player_achievement_progress
		SET claimed_at = $1, updated_at = $1
		WHERE player_id = $2 AND achievement_id = $3
	`

	_, err := r.db.ExecContext(ctx, query, time.Now(), playerId, achievementId)
	return err
}

func (r *Repository) GetPlayerTitles(ctx context.Context, playerId string) ([]api.PlayerTitle, error) {
	query := `
		SELECT id, title, description, achievement_id, is_active, unlocked_at
		FROM player_titles
		WHERE player_id = $1
		ORDER BY unlocked_at DESC
	`

	rows, err := r.db.QueryContext(ctx, query, playerId)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var titles []api.PlayerTitle
	for rows.Next() {
		var t api.PlayerTitle
		err := rows.Scan(&t.Id, &t.Title, &t.Description, &t.AchievementId, &t.IsActive, &t.UnlockedAt)
		if err != nil {
			return nil, err
		}
		titles = append(titles, t)
	}

	return titles, nil
}

func (r *Repository) DeactivateAllTitles(ctx context.Context, playerId string) error {
	query := `
		UPDATE player_titles
		SET is_active = false, updated_at = $1
		WHERE player_id = $2
	`

	_, err := r.db.ExecContext(ctx, query, time.Now(), playerId)
	return err
}

func (r *Repository) ActivateTitle(ctx context.Context, playerId, titleId string) (*api.PlayerTitle, error) {
	query := `
		UPDATE player_titles
		SET is_active = true, updated_at = $1
		WHERE player_id = $2 AND id = $3
		RETURNING id, title, description, achievement_id, is_active, unlocked_at
	`

	var t api.PlayerTitle
	err := r.db.QueryRowContext(ctx, query, time.Now(), playerId, titleId).Scan(
		&t.Id, &t.Title, &t.Description, &t.AchievementId, &t.IsActive, &t.UnlockedAt,
	)
	if err == sql.ErrNoRows {
		return nil, ErrNotFound
	}
	if err != nil {
		return nil, err
	}

	return &t, nil
}
