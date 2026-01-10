package repository

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
)

// Repository handles database operations for combat implants stats
type Repository struct {
	db *pgxpool.Pool
}

// NewRepository creates a new repository instance
func NewRepository(db *pgxpool.Pool) *Repository {
	return &Repository{db: db}
}

// ImplantStats represents implant usage statistics
type ImplantStats struct {
	ImplantID    string    `json:"implant_id"`
	PlayerID     string    `json:"player_id"`
	UsageCount   int64     `json:"usage_count"`
	SuccessRate  float64   `json:"success_rate"`
	AvgDuration  float64   `json:"avg_duration"`
	LastUsed     time.Time `json:"last_used"`
	CreatedAt    time.Time `json:"created_at"`
	UpdatedAt    time.Time `json:"updated_at"`
}

// GetImplantStats retrieves statistics for a specific implant
func (r *Repository) GetImplantStats(ctx context.Context, implantID string) (*ImplantStats, error) {
	query := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		WHERE implant_id = $1
	`

	var stats ImplantStats
	err := r.db.QueryRow(ctx, query, implantID).Scan(
		&stats.ImplantID, &stats.PlayerID, &stats.UsageCount,
		&stats.SuccessRate, &stats.AvgDuration, &stats.LastUsed,
		&stats.CreatedAt, &stats.UpdatedAt,
	)
	if err != nil {
		return nil, err
	}

	return &stats, nil
}

// UpdateImplantStats updates implant usage statistics
func (r *Repository) UpdateImplantStats(ctx context.Context, stats *ImplantStats) error {
	query := `
		INSERT INTO combat.implant_stats (
			implant_id, player_id, usage_count, success_rate, avg_duration, last_used, updated_at
		) VALUES ($1, $2, $3, $4, $5, $6, $7)
		ON CONFLICT (implant_id, player_id)
		DO UPDATE SET
			usage_count = EXCLUDED.usage_count,
			success_rate = EXCLUDED.success_rate,
			avg_duration = EXCLUDED.avg_duration,
			last_used = EXCLUDED.last_used,
			updated_at = EXCLUDED.updated_at
	`

	_, err := r.db.Exec(ctx, query,
		stats.ImplantID, stats.PlayerID, stats.UsageCount,
		stats.SuccessRate, stats.AvgDuration, stats.LastUsed, time.Now(),
	)
	return err
}

// GetPlayerImplantAnalytics retrieves analytics for player's implant usage
func (r *Repository) GetPlayerImplantAnalytics(ctx context.Context, playerID string) ([]*ImplantStats, error) {
	query := `
		SELECT implant_id, player_id, usage_count, success_rate, avg_duration, last_used, created_at, updated_at
		FROM combat.implant_stats
		WHERE player_id = $1
		ORDER BY last_used DESC
	`

	rows, err := r.db.Query(ctx, query, playerID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var stats []*ImplantStats
	for rows.Next() {
		var stat ImplantStats
		err := rows.Scan(
			&stat.ImplantID, &stat.PlayerID, &stat.UsageCount,
			&stat.SuccessRate, &stat.AvgDuration, &stat.LastUsed,
			&stat.CreatedAt, &stat.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		stats = append(stats, &stat)
	}

	return stats, nil
}