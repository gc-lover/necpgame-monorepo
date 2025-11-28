package server

import (
	"context"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/necpgame/social-service-go/models"
)

func (r *GuildRepository) CreateInvitation(ctx context.Context, invitation *models.GuildInvitation) error {
	query := `
		INSERT INTO social.guild_invitations (
			id, guild_id, character_id, invited_by, message, status,
			created_at, expires_at
		) VALUES (
			$1, $2, $3, $4, $5, $6, $7, $8
		)`

	_, err := r.db.Exec(ctx, query,
		invitation.ID, invitation.GuildID, invitation.CharacterID,
		invitation.InvitedBy, invitation.Message, invitation.Status,
		invitation.CreatedAt, invitation.ExpiresAt,
	)

	return err
}

func (r *GuildRepository) GetInvitation(ctx context.Context, id uuid.UUID) (*models.GuildInvitation, error) {
	var invitation models.GuildInvitation

	query := `
		SELECT id, guild_id, character_id, invited_by, message, status,
			created_at, expires_at
		FROM social.guild_invitations
		WHERE id = $1 AND status = 'pending' AND expires_at > NOW()`

	err := r.db.QueryRow(ctx, query, id).Scan(
		&invitation.ID, &invitation.GuildID, &invitation.CharacterID,
		&invitation.InvitedBy, &invitation.Message, &invitation.Status,
		&invitation.CreatedAt, &invitation.ExpiresAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	return &invitation, nil
}

func (r *GuildRepository) GetInvitationsByCharacter(ctx context.Context, characterID uuid.UUID) ([]models.GuildInvitation, error) {
	query := `
		SELECT id, guild_id, character_id, invited_by, message, status,
			created_at, expires_at
		FROM social.guild_invitations
		WHERE character_id = $1 AND status = 'pending' AND expires_at > NOW()
		ORDER BY created_at DESC`

	rows, err := r.db.Query(ctx, query, characterID)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var invitations []models.GuildInvitation
	for rows.Next() {
		var invitation models.GuildInvitation
		err := rows.Scan(
			&invitation.ID, &invitation.GuildID, &invitation.CharacterID,
			&invitation.InvitedBy, &invitation.Message, &invitation.Status,
			&invitation.CreatedAt, &invitation.ExpiresAt,
		)
		if err != nil {
			return nil, err
		}
		invitations = append(invitations, invitation)
	}

	return invitations, nil
}

func (r *GuildRepository) AcceptInvitation(ctx context.Context, invitationID uuid.UUID) error {
	query := `
		UPDATE social.guild_invitations
		SET status = 'accepted'
		WHERE id = $1`

	_, err := r.db.Exec(ctx, query, invitationID)
	return err
}

func (r *GuildRepository) RejectInvitation(ctx context.Context, invitationID uuid.UUID) error {
	query := `
		UPDATE social.guild_invitations
		SET status = 'rejected'
		WHERE id = $1`

	_, err := r.db.Exec(ctx, query, invitationID)
	return err
}

