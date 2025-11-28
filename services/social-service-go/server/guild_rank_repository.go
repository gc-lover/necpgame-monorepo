package server

import (
	"context"
	"encoding/json"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/social-service-go/models"
)

func (r *GuildRepository) GetRanks(ctx context.Context, guildID uuid.UUID) ([]models.GuildRankEntity, error) {
	query := `
		SELECT id, guild_id, name, permissions, "order", created_at
		FROM social.guild_ranks
		WHERE guild_id = $1
		ORDER BY "order" ASC`

	rows, err := r.db.Query(ctx, query, guildID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var ranks []models.GuildRankEntity
	for rows.Next() {
		var rank models.GuildRankEntity
		var permissionsJSON []byte

		err := rows.Scan(
			&rank.ID, &rank.GuildID, &rank.Name, &permissionsJSON, &rank.Order, &rank.CreatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(permissionsJSON) > 0 {
			if err := json.Unmarshal(permissionsJSON, &rank.Permissions); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal rank permissions JSON")
			}
		}

		ranks = append(ranks, rank)
	}

	return ranks, rows.Err()
}

func (r *GuildRepository) GetRankByID(ctx context.Context, rankID uuid.UUID) (*models.GuildRankEntity, error) {
	var rank models.GuildRankEntity
	var permissionsJSON []byte

	query := `
		SELECT id, guild_id, name, permissions, "order", created_at
		FROM social.guild_ranks
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, rankID).Scan(
		&rank.ID, &rank.GuildID, &rank.Name, &permissionsJSON, &rank.Order, &rank.CreatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(permissionsJSON) > 0 {
		if err := json.Unmarshal(permissionsJSON, &rank.Permissions); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal rank permissions JSON")
		}
	}

	return &rank, nil
}

func (r *GuildRepository) CreateRank(ctx context.Context, rank *models.GuildRankEntity) error {
	permissionsJSON, err := json.Marshal(rank.Permissions)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal rank permissions JSON")
		return err
	}

	query := `
		INSERT INTO social.guild_ranks (
			id, guild_id, name, permissions, "order", created_at
		) VALUES (
			$1, $2, $3, $4, $5, $6
		)`

	_, err = r.db.Exec(ctx, query,
		rank.ID, rank.GuildID, rank.Name, permissionsJSON, rank.Order, rank.CreatedAt,
	)

	return err
}

func (r *GuildRepository) UpdateRank(ctx context.Context, rank *models.GuildRankEntity) error {
	permissionsJSON, err := json.Marshal(rank.Permissions)
	if err != nil {
		r.logger.WithError(err).Error("Failed to marshal rank permissions JSON")
		return err
	}

	query := `
		UPDATE social.guild_ranks
		SET name = $1, permissions = $2, "order" = $3
		WHERE id = $4 AND guild_id = $5`

	_, err = r.db.Exec(ctx, query,
		rank.Name, permissionsJSON, rank.Order, rank.ID, rank.GuildID,
	)

	return err
}

func (r *GuildRepository) DeleteRank(ctx context.Context, guildID, rankID uuid.UUID) error {
	query := `
		DELETE FROM social.guild_ranks
		WHERE id = $1 AND guild_id = $2`

	_, err := r.db.Exec(ctx, query, rankID, guildID)
	return err
}

