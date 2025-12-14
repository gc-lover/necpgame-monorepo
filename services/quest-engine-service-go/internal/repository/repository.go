package repository

import (
	"context"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/jackc/pgx/v4/pgxpool"
)

type Repository struct {
	db  *pgxpool.Pool
	rdb *redis.Client
}

type Quest struct {
	ID          string     `json:"id"`
	Type        string     `json:"type"`
	PlayerID    string     `json:"player_id"`
	TemplateID  string     `json:"template_id"`
	Status      string     `json:"status"`
	Progress    float64    `json:"progress"`
	CreatedAt   time.Time  `json:"created_at"`
	CompletedAt *time.Time `json:"completed_at,omitempty"`
}

func NewRepository(db *pgxpool.Pool, rdb *redis.Client) *Repository {
	return &Repository{db: db, rdb: rdb}
}

func (r *Repository) SaveQuest(ctx context.Context, quest *Quest) error {
	query := `
		INSERT INTO quests (id, type, player_id, template_id, status, progress, created_at, completed_at)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
		ON CONFLICT (id) DO UPDATE SET
			status = EXCLUDED.status,
			progress = EXCLUDED.progress,
			completed_at = EXCLUDED.completed_at
	`

	_, err := r.db.Exec(ctx, query,
		quest.ID, quest.Type, quest.PlayerID, quest.TemplateID,
		quest.Status, quest.Progress, quest.CreatedAt, quest.CompletedAt)

	return err
}

func (r *Repository) GetQuest(ctx context.Context, questID string) (*Quest, error) {
	query := `SELECT id, type, player_id, template_id, status, progress, created_at, completed_at FROM quests WHERE id = $1`

	var quest Quest
	err := r.db.QueryRow(ctx, query, questID).Scan(
		&quest.ID, &quest.Type, &quest.PlayerID, &quest.TemplateID,
		&quest.Status, &quest.Progress, &quest.CreatedAt, &quest.CompletedAt)

	return &quest, err
}

func (r *Repository) GetActiveQuests(ctx context.Context, playerID string) ([]*Quest, error) {
	query := `SELECT id, type, player_id, template_id, status, progress, created_at, completed_at FROM quests WHERE player_id = $1 AND status = 'active'`

	rows, err := r.db.Query(ctx, query, playerID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var quests []*Quest
	for rows.Next() {
		var quest Quest
		err := rows.Scan(&quest.ID, &quest.Type, &quest.PlayerID, &quest.TemplateID,
			&quest.Status, &quest.Progress, &quest.CreatedAt, &quest.CompletedAt)
		if err != nil {
			return nil, err
		}
		quests = append(quests, &quest)
	}

	return quests, rows.Err()
}
