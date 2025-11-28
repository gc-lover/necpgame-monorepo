package server

import (
	"context"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/social-service-go/models"
)

func (r *GuildRepository) AddMember(ctx context.Context, member *models.GuildMember) error {
	query := `
		INSERT INTO social.guild_members (
			id, guild_id, character_id, rank, status, contribution,
			joined_at, updated_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8
		)`

	_, err := r.db.Exec(ctx, query,
		member.ID, member.GuildID, member.CharacterID, member.Rank,
		member.Status, member.Contribution, member.JoinedAt, member.UpdatedAt,
	)

	return err
}

func (r *GuildRepository) GetMember(ctx context.Context, guildID, characterID uuid.UUID) (*models.GuildMember, error) {
	var member models.GuildMember

	query := `
		SELECT id, guild_id, character_id, rank, status, contribution,
			joined_at, updated_at
		FROM social.guild_members
		WHERE guild_id = $1 AND character_id = $2 AND status = 'active'`

	err := r.db.QueryRow(ctx, query, guildID, characterID).Scan(
		&member.ID, &member.GuildID, &member.CharacterID, &member.Rank,
		&member.Status, &member.Contribution, &member.JoinedAt, &member.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &member, nil
}

func (r *GuildRepository) GetMembers(ctx context.Context, guildID uuid.UUID, limit, offset int) ([]models.GuildMember, error) {
	query := `
		SELECT id, guild_id, character_id, rank, status, contribution,
			joined_at, updated_at
		FROM social.guild_members
		WHERE guild_id = $1 AND status = 'active'
		ORDER BY 
			CASE rank
				WHEN 'leader' THEN 1
				WHEN 'officer' THEN 2
				WHEN 'member' THEN 3
				WHEN 'recruit' THEN 4
			END,
			joined_at ASC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, guildID, limit, offset)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var members []models.GuildMember
	for rows.Next() {
		var member models.GuildMember
		err := rows.Scan(
			&member.ID, &member.GuildID, &member.CharacterID, &member.Rank,
			&member.Status, &member.Contribution, &member.JoinedAt, &member.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}
		members = append(members, member)
	}

	return members, nil
}

func (r *GuildRepository) CountMembers(ctx context.Context, guildID uuid.UUID) (int, error) {
	var count int
	query := `SELECT COUNT(*) FROM social.guild_members WHERE guild_id = $1 AND status = 'active'`
	err := r.db.QueryRow(ctx, query, guildID).Scan(&count)
	return count, err
}

func (r *GuildRepository) UpdateMemberRank(ctx context.Context, guildID, characterID uuid.UUID, rank models.GuildRank) error {
	query := `
		UPDATE social.guild_members
		SET rank = $1, updated_at = $2
		WHERE guild_id = $3 AND character_id = $4 AND status = 'active'`

	_, err := r.db.Exec(ctx, query, rank, time.Now(), guildID, characterID)
	return err
}

func (r *GuildRepository) RemoveMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	query := `
		UPDATE social.guild_members
		SET status = 'left', updated_at = $1
		WHERE guild_id = $2 AND character_id = $3`

	_, err := r.db.Exec(ctx, query, time.Now(), guildID, characterID)
	return err
}

func (r *GuildRepository) KickMember(ctx context.Context, guildID, characterID uuid.UUID) error {
	query := `
		UPDATE social.guild_members
		SET status = 'kicked', updated_at = $1
		WHERE guild_id = $2 AND character_id = $3`

	_, err := r.db.Exec(ctx, query, time.Now(), guildID, characterID)
	return err
}

func (r *GuildRepository) UpdateMemberContribution(ctx context.Context, guildID, characterID uuid.UUID, contribution int) error {
	query := `
		UPDATE social.guild_members
		SET contribution = contribution + $1, updated_at = $2
		WHERE guild_id = $3 AND character_id = $4 AND status = 'active'`

	_, err := r.db.Exec(ctx, query, contribution, time.Now(), guildID, characterID)
	return err
}

