package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/necpgame/social-service-go/models"
	"github.com/sirupsen/logrus"
)

type GuildRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewGuildRepository(db *pgxpool.Pool) *GuildRepository {
	return &GuildRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *GuildRepository) Create(ctx context.Context, guild *models.Guild) error {
	query := `
		INSERT INTO social.guilds (
			id, name, tag, leader_id, level, experience, max_members,
			description, status, created_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11
		)`

	_, err := r.db.Exec(ctx, query,
		guild.ID, guild.Name, guild.Tag, guild.LeaderID, guild.Level,
		guild.Experience, guild.MaxMembers, guild.Description, guild.Status,
		guild.CreatedAt, guild.UpdatedAt,
	)

	return err
}

func (r *GuildRepository) GetByID(ctx context.Context, id uuid.UUID) (*models.Guild, error) {
	var guild models.Guild

	query := `
		SELECT id, name, tag, leader_id, level, experience, max_members,
			description, status, created_at, updated_at
		FROM social.guilds
		WHERE id = $1 AND status != 'disbanded'`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&guild.ID, &guild.Name, &guild.Tag, &guild.LeaderID, &guild.Level,
		&guild.Experience, &guild.MaxMembers, &guild.Description, &guild.Status,
		&guild.CreatedAt, &guild.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &guild, nil
}

func (r *GuildRepository) GetByName(ctx context.Context, name string) (*models.Guild, error) {
	var guild models.Guild

	query := `
		SELECT id, name, tag, leader_id, level, experience, max_members,
			description, status, created_at, updated_at
		FROM social.guilds
		WHERE name = $1 AND status != 'disbanded'`

	err := r.db.QueryRow(ctx, query, name).Scan(
		&guild.ID, &guild.Name, &guild.Tag, &guild.LeaderID, &guild.Level,
		&guild.Experience, &guild.MaxMembers, &guild.Description, &guild.Status,
		&guild.CreatedAt, &guild.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &guild, nil
}

func (r *GuildRepository) GetByTag(ctx context.Context, tag string) (*models.Guild, error) {
	var guild models.Guild

	query := `
		SELECT id, name, tag, leader_id, level, experience, max_members,
			description, status, created_at, updated_at
		FROM social.guilds
		WHERE tag = $1 AND status != 'disbanded'`

	err := r.db.QueryRow(ctx, query, tag).Scan(
		&guild.ID, &guild.Name, &guild.Tag, &guild.LeaderID, &guild.Level,
		&guild.Experience, &guild.MaxMembers, &guild.Description, &guild.Status,
		&guild.CreatedAt, &guild.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &guild, nil
}

func (r *GuildRepository) List(ctx context.Context, limit, offset int) ([]models.Guild, error) {
	query := `
		SELECT id, name, tag, leader_id, level, experience, max_members,
			description, status, created_at, updated_at
		FROM social.guilds
		WHERE status = 'active'
		ORDER BY level DESC, experience DESC
		LIMIT $1 OFFSET $2`

	rows, err := r.db.Query(ctx, query, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var guilds []models.Guild
	for rows.Next() {
		var guild models.Guild
		err := rows.Scan(
			&guild.ID, &guild.Name, &guild.Tag, &guild.LeaderID, &guild.Level,
			&guild.Experience, &guild.MaxMembers, &guild.Description, &guild.Status,
			&guild.CreatedAt, &guild.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		guilds = append(guilds, guild)
	}

	return guilds, nil
}

func (r *GuildRepository) Count(ctx context.Context) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM social.guilds WHERE status = 'active'`
	err := r.db.QueryRow(ctx, query).Scan(&count)
	return count, err
}

func (r *GuildRepository) Update(ctx context.Context, guild *models.Guild) error {
	query := `
		UPDATE social.guilds
		SET name = $1, description = $2, updated_at = $3
		WHERE id = $4`

	_, err := r.db.Exec(ctx, query, guild.Name, guild.Description, guild.UpdatedAt, guild.ID)
	return err
}

func (r *GuildRepository) UpdateLevel(ctx context.Context, guildID uuid.UUID, level, experience int) error {
	query := `
		UPDATE social.guilds
		SET level = $1, experience = $2, updated_at = $3
		WHERE id = $4`

	_, err := r.db.Exec(ctx, query, level, experience, time.Now(), guildID)
	return err
}

func (r *GuildRepository) Disband(ctx context.Context, guildID uuid.UUID) error {
	query := `
		UPDATE social.guilds
		SET status = 'disbanded', updated_at = $1
		WHERE id = $2`

	_, err := r.db.Exec(ctx, query, time.Now(), guildID)
	return err
}
