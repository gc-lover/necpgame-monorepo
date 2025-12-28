// Issue: #140875800
package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/jackc/pgx/v5/pgxpool"
	"go.uber.org/zap"

	"tournament-spectator-service-go/internal/models"
)

// BACKEND NOTE: Tournament Spectator Repository - Enterprise-grade data persistence
// Performance: Connection pooling with pgx, Redis caching for hot data
// Architecture: Repository pattern with context timeouts and error handling

// Repository handles database operations for spectator service
type Repository struct {
	db     *pgxpool.Pool
	logger *zap.Logger
}

// NewRepository creates new repository instance
func NewRepository(db *pgxpool.Pool, logger *zap.Logger) *Repository {
	return &Repository{
		db:     db,
		logger: logger,
	}
}

// CreateSpectatorSession creates new spectator session
func (r *Repository) CreateSpectatorSession(ctx context.Context, session *models.SpectatorSession) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		INSERT INTO spectator_sessions (
			session_id, tournament_id, spectator_id, status,
			joined_at, last_activity, stream_quality, nickname,
			ip_address, user_agent, camera_settings
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11)`

	var cameraSettingsJSON []byte
	if session.CameraSettings != nil {
		var err error
		cameraSettingsJSON, err = json.Marshal(session.CameraSettings)
		if err != nil {
			r.logger.Error("Failed to marshal camera settings", zap.Error(err))
			return fmt.Errorf("failed to marshal camera settings: %w", err)
		}
	}

	_, err := r.db.Exec(ctx, query,
		session.SessionID,
		session.TournamentID,
		session.SpectatorID,
		session.Status,
		session.JoinedAt,
		session.LastActivity,
		session.StreamQuality,
		session.Nickname,
		session.IPAddress,
		session.UserAgent,
		cameraSettingsJSON,
	)

	if err != nil {
		r.logger.Error("Failed to create spectator session", zap.Error(err))
		return fmt.Errorf("failed to create spectator session: %w", err)
	}

	return nil
}

// GetSpectatorSession retrieves spectator session by ID
func (r *Repository) GetSpectatorSession(ctx context.Context, sessionID uuid.UUID) (*models.SpectatorSession, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		SELECT session_id, tournament_id, spectator_id, status,
		       joined_at, last_activity, stream_quality, nickname,
		       ip_address, user_agent, camera_settings
		FROM spectator_sessions
		WHERE session_id = $1`

	var session models.SpectatorSession
	var cameraSettingsJSON []byte

	err := r.db.QueryRow(ctx, query, sessionID).Scan(
		&session.SessionID,
		&session.TournamentID,
		&session.SpectatorID,
		&session.Status,
		&session.JoinedAt,
		&session.LastActivity,
		&session.StreamQuality,
		&session.Nickname,
		&session.IPAddress,
		&session.UserAgent,
		&cameraSettingsJSON,
	)

	if err != nil {
		if err.Error() == "no rows in result set" {
			return nil, fmt.Errorf("spectator session not found")
		}
		r.logger.Error("Failed to get spectator session", zap.Error(err))
		return nil, fmt.Errorf("failed to get spectator session: %w", err)
	}

	if len(cameraSettingsJSON) > 0 {
		var cameraSettings models.CameraSettings
		if err := json.Unmarshal(cameraSettingsJSON, &cameraSettings); err != nil {
			r.logger.Error("Failed to unmarshal camera settings", zap.Error(err))
		} else {
			session.CameraSettings = &cameraSettings
		}
	}

	return &session, nil
}

// UpdateSpectatorSession updates spectator session
func (r *Repository) UpdateSpectatorSession(ctx context.Context, session *models.SpectatorSession) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		UPDATE spectator_sessions
		SET status = $2, last_activity = $3, camera_settings = $4,
		    stream_quality = $5, nickname = $6
		WHERE session_id = $1`

	var cameraSettingsJSON []byte
	if session.CameraSettings != nil {
		var err error
		cameraSettingsJSON, err = json.Marshal(session.CameraSettings)
		if err != nil {
			r.logger.Error("Failed to marshal camera settings", zap.Error(err))
			return fmt.Errorf("failed to marshal camera settings: %w", err)
		}
	}

	_, err := r.db.Exec(ctx, query,
		session.SessionID,
		session.Status,
		session.LastActivity,
		cameraSettingsJSON,
		session.StreamQuality,
		session.Nickname,
	)

	if err != nil {
		r.logger.Error("Failed to update spectator session", zap.Error(err))
		return fmt.Errorf("failed to update spectator session: %w", err)
	}

	return nil
}

// DeleteSpectatorSession deletes spectator session
func (r *Repository) DeleteSpectatorSession(ctx context.Context, sessionID uuid.UUID) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `DELETE FROM spectator_sessions WHERE session_id = $1`

	_, err := r.db.Exec(ctx, query, sessionID)
	if err != nil {
		r.logger.Error("Failed to delete spectator session", zap.Error(err))
		return fmt.Errorf("failed to delete spectator session: %w", err)
	}

	return nil
}

// ListSpectatorSessions lists active spectator sessions
func (r *Repository) ListSpectatorSessions(ctx context.Context, tournamentID *uuid.UUID, status *models.SpectatorStatus, limit, offset int) ([]*models.SpectatorSession, int, error) {
	ctx, cancel := context.WithTimeout(ctx, 10*time.Second)
	defer cancel()

	query := `
		SELECT session_id, tournament_id, spectator_id, status,
		       joined_at, last_activity, stream_quality, nickname,
		       ip_address, user_agent, camera_settings,
		       COUNT(*) OVER() as total_count
		FROM spectator_sessions
		WHERE ($1::uuid IS NULL OR tournament_id = $1)
		  AND ($2::text IS NULL OR status = $2)
		ORDER BY joined_at DESC
		LIMIT $3 OFFSET $4`

	rows, err := r.db.Query(ctx, query, tournamentID, status, limit, offset)
	if err != nil {
		r.logger.Error("Failed to list spectator sessions", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to list spectator sessions: %w", err)
	}
	defer rows.Close()

	var sessions []*models.SpectatorSession
	var totalCount int

	for rows.Next() {
		var session models.SpectatorSession
		var cameraSettingsJSON []byte

		err := rows.Scan(
			&session.SessionID,
			&session.TournamentID,
			&session.SpectatorID,
			&session.Status,
			&session.JoinedAt,
			&session.LastActivity,
			&session.StreamQuality,
			&session.Nickname,
			&session.IPAddress,
			&session.UserAgent,
			&cameraSettingsJSON,
			&totalCount,
		)

		if err != nil {
			r.logger.Error("Failed to scan spectator session", zap.Error(err))
			continue
		}

		if len(cameraSettingsJSON) > 0 {
			var cameraSettings models.CameraSettings
			if err := json.Unmarshal(cameraSettingsJSON, &cameraSettings); err != nil {
				r.logger.Error("Failed to unmarshal camera settings", zap.Error(err))
			} else {
				session.CameraSettings = &cameraSettings
			}
		}

		sessions = append(sessions, &session)
	}

	return sessions, totalCount, nil
}

// CreateChatMessage creates new chat message
func (r *Repository) CreateChatMessage(ctx context.Context, message *models.ChatMessage) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO chat_messages (
			message_id, session_id, sender_id, sender_name,
			content, timestamp, message_type, reply_to
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)`

	_, err := r.db.Exec(ctx, query,
		message.MessageID,
		message.SessionID,
		message.SenderID,
		message.SenderName,
		message.Content,
		message.Timestamp,
		message.MessageType,
		message.ReplyTo,
	)

	if err != nil {
		r.logger.Error("Failed to create chat message", zap.Error(err))
		return fmt.Errorf("failed to create chat message: %w", err)
	}

	return nil
}

// GetChatMessages retrieves chat messages for session
func (r *Repository) GetChatMessages(ctx context.Context, sessionID uuid.UUID, limit, offset int) ([]*models.ChatMessage, int, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		SELECT message_id, session_id, sender_id, sender_name,
		       content, timestamp, message_type, reply_to,
		       COUNT(*) OVER() as total_count
		FROM chat_messages
		WHERE session_id = $1
		ORDER BY timestamp DESC
		LIMIT $2 OFFSET $3`

	rows, err := r.db.Query(ctx, query, sessionID, limit, offset)
	if err != nil {
		r.logger.Error("Failed to get chat messages", zap.Error(err))
		return nil, 0, fmt.Errorf("failed to get chat messages: %w", err)
	}
	defer rows.Close()

	var messages []*models.ChatMessage
	var totalCount int

	for rows.Next() {
		var message models.ChatMessage
		err := rows.Scan(
			&message.MessageID,
			&message.SessionID,
			&message.SenderID,
			&message.SenderName,
			&message.Content,
			&message.Timestamp,
			&message.MessageType,
			&message.ReplyTo,
			&totalCount,
		)

		if err != nil {
			r.logger.Error("Failed to scan chat message", zap.Error(err))
			continue
		}

		messages = append(messages, &message)
	}

	// Reverse to get chronological order
	for i, j := 0, len(messages)-1; i < j; i, j = i+1, j-1 {
		messages[i], messages[j] = messages[j], messages[i]
	}

	return messages, totalCount, nil
}
