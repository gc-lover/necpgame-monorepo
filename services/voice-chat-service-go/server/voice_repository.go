// Issue: #141888700
package server

import (
	"context"
	"encoding/json"
	"fmt"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/gc-lover/necpgame-monorepo/services/voice-chat-service-go/models"
	"github.com/sirupsen/logrus"
)

type VoiceRepository struct {
	db     *pgxpool.Pool
	logger *logrus.Logger
}

func NewVoiceRepository(db *pgxpool.Pool) *VoiceRepository {
	return &VoiceRepository{
		db:     db,
		logger: GetLogger(),
	}
}

func (r *VoiceRepository) CreateChannel(ctx context.Context, channel *models.VoiceChannel) error {
	settingsJSON, err := json.Marshal(channel.Settings)
	if err != nil {
		return fmt.Errorf("failed to marshal settings JSON: %w", err)
	}

	query := `
		INSERT INTO social.voice_channels (
			id, type, owner_id, owner_type, name, max_members,
			quality_preset, settings, created_at, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, $7, NOW(), NOW()
		) RETURNING id, created_at, updated_at`

	err := r.db.QueryRow(ctx, query,
		channel.Type, channel.OwnerID, channel.OwnerType, channel.Name,
		channel.MaxMembers, channel.QualityPreset, settingsJSON,
	).Scan(&channel.ID, &channel.CreatedAt, &channel.UpdatedAt)

	return err
}

func (r *VoiceRepository) GetChannel(ctx context.Context, channelID uuid.UUID) (*models.VoiceChannel, error) {
	var channel models.VoiceChannel
	var settingsJSON []byte

	query := `
		SELECT id, type, owner_id, owner_type, name, max_members,
		       quality_preset, settings, created_at, updated_at
		FROM social.voice_channels
		WHERE id = $1`

	err := r.db.QueryRow(ctx, query, channelID).Scan(
		&channel.ID, &channel.Type, &channel.OwnerID, &channel.OwnerType,
		&channel.Name, &channel.MaxMembers, &channel.QualityPreset,
		&settingsJSON, &channel.CreatedAt, &channel.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(settingsJSON) > 0 {
		if err := json.Unmarshal(settingsJSON, &channel.Settings); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal settings JSON")
			channel.Settings = make(map[string]interface{})
		}
	} else {
		channel.Settings = make(map[string]interface{})
	}

	return &channel, nil
}

func (r *VoiceRepository) ListChannels(ctx context.Context, channelType *models.VoiceChannelType, ownerID *uuid.UUID, limit, offset int) ([]models.VoiceChannel, error) {
	var args []interface{}
	baseQuery := `
		SELECT id, type, owner_id, owner_type, name, max_members,
		       quality_preset, settings, created_at, updated_at
		FROM social.voice_channels
		WHERE 1=1`

	if channelType != nil {
		baseQuery += fmt.Sprintf(" AND type = $%d", len(args)+1)
		args = append(args, *channelType)
	}

	if ownerID != nil {
		baseQuery += fmt.Sprintf(" AND owner_id = $%d", len(args)+1)
		args = append(args, *ownerID)
	}

	baseQuery += fmt.Sprintf(" ORDER BY created_at DESC LIMIT $%d OFFSET $%d", len(args)+1, len(args)+2)
	args = append(args, limit, offset)

	rows, err := r.db.Query(ctx, baseQuery, args...)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var channels []models.VoiceChannel
	for rows.Next() {
		var channel models.VoiceChannel
		var settingsJSON []byte

		err := rows.Scan(
			&channel.ID, &channel.Type, &channel.OwnerID, &channel.OwnerType,
			&channel.Name, &channel.MaxMembers, &channel.QualityPreset,
			&settingsJSON, &channel.CreatedAt, &channel.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(settingsJSON) > 0 {
			if err := json.Unmarshal(settingsJSON, &channel.Settings); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal settings JSON")
				return nil, fmt.Errorf("failed to unmarshal settings JSON: %w", err)
			}
		} else {
			channel.Settings = make(map[string]interface{})
		}

		channels = append(channels, channel)
	}

	return channels, nil
}

func (r *VoiceRepository) AddParticipant(ctx context.Context, participant *models.VoiceParticipant) error {
	positionJSON, err := json.Marshal(participant.Position)
	if err != nil {
		return fmt.Errorf("failed to marshal position JSON: %w", err)
	}
	statsJSON, err := json.Marshal(participant.Stats)
	if err != nil {
		return fmt.Errorf("failed to marshal stats JSON: %w", err)
	}

	query := `
		INSERT INTO social.voice_participants (
			id, channel_id, character_id, status, webrtc_token,
			position, stats, joined_at, updated_at
		) VALUES (
			gen_random_uuid(), $1, $2, $3, $4, $5, $6, NOW(), NOW()
		) RETURNING id, joined_at, updated_at`

	err := r.db.QueryRow(ctx, query,
		participant.ChannelID, participant.CharacterID, participant.Status,
		participant.WebRTCToken, positionJSON, statsJSON,
	).Scan(&participant.ID, &participant.JoinedAt, &participant.UpdatedAt)

	return err
}

func (r *VoiceRepository) RemoveParticipant(ctx context.Context, channelID, characterID uuid.UUID) error {
	_, err := r.db.Exec(ctx,
		`DELETE FROM social.voice_participants
		 WHERE channel_id = $1 AND character_id = $2`,
		channelID, characterID,
	)
	return err
}

func (r *VoiceRepository) GetParticipant(ctx context.Context, channelID, characterID uuid.UUID) (*models.VoiceParticipant, error) {
	var participant models.VoiceParticipant
	var positionJSON, statsJSON []byte

	query := `
		SELECT id, channel_id, character_id, status, webrtc_token,
		       position, stats, joined_at, updated_at
		FROM social.voice_participants
		WHERE channel_id = $1 AND character_id = $2`

	err := r.db.QueryRow(ctx, query, channelID, characterID).Scan(
		&participant.ID, &participant.ChannelID, &participant.CharacterID,
		&participant.Status, &participant.WebRTCToken,
		&positionJSON, &statsJSON, &participant.JoinedAt, &participant.UpdatedAt,
	)

	if err == pgx.ErrNoRows {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	if len(positionJSON) > 0 {
		if err := json.Unmarshal(positionJSON, &participant.Position); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal position JSON")
			participant.Position = make(map[string]interface{})
		}
	} else {
		participant.Position = make(map[string]interface{})
	}

	if len(statsJSON) > 0 {
		if err := json.Unmarshal(statsJSON, &participant.Stats); err != nil {
			r.logger.WithError(err).Error("Failed to unmarshal stats JSON")
			participant.Stats = make(map[string]interface{})
		}
	} else {
		participant.Stats = make(map[string]interface{})
	}

	return &participant, nil
}

func (r *VoiceRepository) ListParticipants(ctx context.Context, channelID uuid.UUID) ([]models.VoiceParticipant, error) {
	rows, err := r.db.Query(ctx,
		`SELECT id, channel_id, character_id, status, webrtc_token,
		        position, stats, joined_at, updated_at
		 FROM social.voice_participants
		 WHERE channel_id = $1
		 ORDER BY joined_at ASC`,
		channelID,
	)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	var participants []models.VoiceParticipant
	for rows.Next() {
		var participant models.VoiceParticipant
		var positionJSON, statsJSON []byte

		err := rows.Scan(
			&participant.ID, &participant.ChannelID, &participant.CharacterID,
			&participant.Status, &participant.WebRTCToken,
			&positionJSON, &statsJSON, &participant.JoinedAt, &participant.UpdatedAt,
		)
		if err != nil {
			return nil, err
		}

		if len(positionJSON) > 0 {
			if err := json.Unmarshal(positionJSON, &participant.Position); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal position JSON")
				return nil, fmt.Errorf("failed to unmarshal position JSON: %w", err)
			}
		} else {
			participant.Position = make(map[string]interface{})
		}

		if len(statsJSON) > 0 {
			if err := json.Unmarshal(statsJSON, &participant.Stats); err != nil {
				r.logger.WithError(err).Error("Failed to unmarshal stats JSON")
				return nil, fmt.Errorf("failed to unmarshal stats JSON: %w", err)
			}
		} else {
			participant.Stats = make(map[string]interface{})
		}

		participants = append(participants, participant)
	}

	return participants, nil
}

func (r *VoiceRepository) UpdateParticipantStatus(ctx context.Context, channelID, characterID uuid.UUID, status models.ParticipantStatus) error {
	_, err := r.db.Exec(ctx,
		`UPDATE social.voice_participants
		 SET status = $1, updated_at = NOW()
		 WHERE channel_id = $2 AND character_id = $3`,
		status, channelID, characterID,
	)
	return err
}

func (r *VoiceRepository) UpdateParticipantPosition(ctx context.Context, channelID, characterID uuid.UUID, position map[string]interface{}) error {
	positionJSON, err := json.Marshal(position)
	if err != nil {
		return fmt.Errorf("failed to marshal position JSON: %w", err)
	}

	_, err := r.db.Exec(ctx,
		`UPDATE social.voice_participants
		 SET position = $1, updated_at = NOW()
		 WHERE channel_id = $2 AND character_id = $3`,
		positionJSON, channelID, characterID,
	)
	return err
}

func (r *VoiceRepository) CountParticipants(ctx context.Context, channelID uuid.UUID) (int, error) {
	var count int
	err := r.db.QueryRow(ctx,
		`SELECT COUNT(*) FROM social.voice_participants WHERE channel_id = $1`,
		channelID,
	).Scan(&count)
	return count, err
}
