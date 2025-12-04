package server

import (
	"context"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/sirupsen/logrus"
)

// NOTE: This repository requires database migration for tables:
// - social.lobby_subchannels (id, lobby_id, name, description, type, max_participants, role_restrictions, created_at, updated_at)
// - social.lobby_participants (id, lobby_id, character_id, role, subchannel_id, is_muted, is_deafened, joined_at, left_at)
// See: knowledge/implementation/architecture/voice-lobby-system-architecture.yaml

type SubchannelRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewSubchannelRepository(db *pgxpool.Pool) *SubchannelRepository {
	return &SubchannelRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *SubchannelRepository) CreateSubchannel(ctx context.Context, lobbyID uuid.UUID, req *models.CreateSubchannelRequest) (*models.Subchannel, error) {
	query := `
		INSERT INTO social.lobby_subchannels (
			id, lobby_id, name, description, type, max_participants,
			role_restrictions, created_at, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, NOW(), NOW()
		) RETURNING id, created_at, updated_at`

	var subchannel models.Subchannel
	subchannel.LobbyID = lobbyID
	subchannel.Name = req.Name
	subchannelType := models.SubchannelTypeCustom
	subchannel.SubchannelType = subchannelType
	subchannel.MaxParticipants = req.MaxParticipants
	subchannel.IsLocked = false
	subchannel.CurrentParticipants = 0

	var roleRestrictions interface{}
	// roleRestrictions can be added to Settings if needed

	err := r.db.QueryRow(ctx, query,
		lobbyID, req.Name, "", string(subchannelType),
		req.MaxParticipants, roleRestrictions,
	).Scan(&subchannel.ID, &subchannel.CreatedAt, &subchannel.UpdatedAt)

	if err != nil {
		return nil, fmt.Errorf("failed to create subchannel: %w", err)
	}

	return &subchannel, nil
}

func (r *SubchannelRepository) GetSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) (*models.Subchannel, error) {
	var subchannel models.Subchannel
	var roleRestrictions interface{}

	query := `
		SELECT id, lobby_id, name, description, type, max_participants,
		       role_restrictions, created_at, updated_at
		FROM social.lobby_subchannels
		WHERE id = $1 AND lobby_id = $2`

	var description string
	var subchannelTypeStr string
	err := r.db.QueryRow(ctx, query, subchannelID, lobbyID).Scan(
		&subchannel.ID, &subchannel.LobbyID, &subchannel.Name,
		&description, &subchannelTypeStr,
		&subchannel.MaxParticipants, &roleRestrictions,
		&subchannel.CreatedAt, &subchannel.UpdatedAt,
	)
	
	subchannel.SubchannelType = models.SubchannelType(subchannelTypeStr)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to get subchannel: %w", err)
	}

	// roleRestrictions can be stored in Settings if needed
	if roleRestrictions != nil {
		if subchannel.Settings == nil {
			subchannel.Settings = make(map[string]interface{})
		}
		subchannel.Settings["role_restrictions"] = roleRestrictions
	}

	count, err := r.CountParticipants(ctx, subchannelID)
	if err != nil {
		r.logger.WithError(err).Warn("Failed to count participants")
	} else {
		subchannel.CurrentParticipants = count
	}

	return &subchannel, nil
}

func (r *SubchannelRepository) ListSubchannels(ctx context.Context, lobbyID uuid.UUID) ([]models.Subchannel, error) {
	query := `
		SELECT id, lobby_id, name, description, type, max_participants,
		       role_restrictions, created_at, updated_at
		FROM social.lobby_subchannels
		WHERE lobby_id = $1
		ORDER BY created_at ASC`

	rows, err := r.db.Query(ctx, query, lobbyID)
	if err != nil {
		return nil, fmt.Errorf("failed to list subchannels: %w", err)
	}
	defer rows.Close()

	var subchannels []models.Subchannel
	for rows.Next() {
		var subchannel models.Subchannel
		var roleRestrictions interface{}

		var description string
		var subchannelTypeStr string
		err := rows.Scan(
			&subchannel.ID, &subchannel.LobbyID, &subchannel.Name,
			&description, &subchannelTypeStr,
			&subchannel.MaxParticipants, &roleRestrictions,
			&subchannel.CreatedAt, &subchannel.UpdatedAt,
		)
		if err != nil {
			return nil, fmt.Errorf("failed to scan subchannel: %w", err)
		}

		subchannel.SubchannelType = models.SubchannelType(subchannelTypeStr)
		
		// roleRestrictions can be stored in Settings if needed
		if roleRestrictions != nil {
			if subchannel.Settings == nil {
				subchannel.Settings = make(map[string]interface{})
			}
			subchannel.Settings["role_restrictions"] = roleRestrictions
		}

		count, err := r.CountParticipants(ctx, subchannel.ID)
		if err != nil {
			r.logger.WithError(err).Warn("Failed to count participants")
		} else {
			subchannel.CurrentParticipants = count
		}

		subchannels = append(subchannels, subchannel)
	}

	return subchannels, nil
}

func (r *SubchannelRepository) UpdateSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID, req *models.UpdateSubchannelRequest) (*models.Subchannel, error) {
	var updates []string
	var args []interface{}
	argIndex := 1

	if req.Name != nil {
		updates = append(updates, fmt.Sprintf("name = $%d", argIndex))
		args = append(args, *req.Name)
		argIndex++
	}
	if req.MaxParticipants != nil {
		updates = append(updates, fmt.Sprintf("max_participants = $%d", argIndex))
		args = append(args, *req.MaxParticipants)
		argIndex++
	}
	if req.IsLocked != nil {
		updates = append(updates, fmt.Sprintf("is_locked = $%d", argIndex))
		args = append(args, *req.IsLocked)
		argIndex++
	}

	if len(updates) == 0 {
		return r.GetSubchannel(ctx, lobbyID, subchannelID)
	}

	updates = append(updates, "updated_at = NOW()")
	args = append(args, subchannelID, lobbyID)

	setClause := ""
	for i, update := range updates {
		if i > 0 {
			setClause += ", "
		}
		setClause += update
	}

	query := fmt.Sprintf(`
		UPDATE social.lobby_subchannels
		SET %s
		WHERE id = $%d AND lobby_id = $%d
		RETURNING id, lobby_id, name, description, type, max_participants,
		          role_restrictions, created_at, updated_at`,
		setClause, argIndex, argIndex+1)

	var subchannel models.Subchannel
	var roleRestrictions interface{}

	var description string
	var subchannelTypeStr string
	err := r.db.QueryRow(ctx, query, args...).Scan(
		&subchannel.ID, &subchannel.LobbyID, &subchannel.Name,
		&description, &subchannelTypeStr,
		&subchannel.MaxParticipants, &roleRestrictions,
		&subchannel.CreatedAt, &subchannel.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("failed to update subchannel: %w", err)
	}

	subchannel.SubchannelType = models.SubchannelType(subchannelTypeStr)
	
	// roleRestrictions can be stored in Settings if needed
	if roleRestrictions != nil {
		if subchannel.Settings == nil {
			subchannel.Settings = make(map[string]interface{})
		}
		subchannel.Settings["role_restrictions"] = roleRestrictions
	}

	count, err := r.CountParticipants(ctx, subchannel.ID)
	if err != nil {
		r.logger.WithError(err).Warn("Failed to count participants")
	} else {
		subchannel.CurrentParticipants = count
	}

	return &subchannel, nil
}

func (r *SubchannelRepository) DeleteSubchannel(ctx context.Context, lobbyID, subchannelID uuid.UUID) error {
	query := `DELETE FROM social.lobby_subchannels WHERE id = $1 AND lobby_id = $2`
	result, err := r.db.Exec(ctx, query, subchannelID, lobbyID)
	if err != nil {
		return fmt.Errorf("failed to delete subchannel: %w", err)
	}

	if result.RowsAffected() == 0 {
		return pgx.ErrNoRows
	}

	return nil
}

func (r *SubchannelRepository) CountParticipants(ctx context.Context, subchannelID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM social.lobby_participants WHERE subchannel_id = $1`,
		subchannelID,
	).Scan(&count)
	return count, err
}

func (r *SubchannelRepository) MoveParticipant(ctx context.Context, subchannelID, characterID uuid.UUID) error {
	query := `
		UPDATE social.lobby_participants
		SET subchannel_id = $1, updated_at = NOW()
		WHERE character_id = $2`
	_, err := r.db.Exec(ctx, query, subchannelID, characterID)
	return err
}

func (r *SubchannelRepository) GetParticipants(ctx context.Context, subchannelID uuid.UUID) ([]models.SubchannelParticipant, error) {
	query := `
		SELECT lp.character_id, c.name, lp.role, lp.joined_at
		FROM social.lobby_participants lp
		JOIN mvp_core.character c ON c.id = lp.character_id
		WHERE lp.subchannel_id = $1
		ORDER BY lp.joined_at ASC`

	rows, err := r.db.Query(ctx, query, subchannelID)
	if err != nil {
		return nil, fmt.Errorf("failed to get participants: %w", err)
	}
	defer rows.Close()

	var participants []models.SubchannelParticipant
	for rows.Next() {
		var participant models.SubchannelParticipant

		err := rows.Scan(&participant.CharacterID, &participant.JoinedAt)
		if err != nil {
			return nil, fmt.Errorf("failed to scan participant: %w", err)
		}

		participants = append(participants, participant)
	}

	return participants, nil
}

