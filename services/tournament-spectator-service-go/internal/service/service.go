// Issue: #140875800
package service

import (
	"context"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"

	"tournament-spectator-service-go/internal/models"
	"tournament-spectator-service-go/internal/repository"
)

// BACKEND NOTE: Tournament Spectator Service - Enterprise-grade spectator management
// Performance: Redis caching for hot data, WebSocket connections, event streaming
// Architecture: Clean architecture with service layer abstraction

// Service handles business logic for spectator operations
type Service struct {
	repo   *repository.Repository
	cache  *redis.Client
	logger *zap.Logger
}

// NewService creates new service instance
func NewService(repo *repository.Repository, cache *redis.Client, logger *zap.Logger) *Service {
	return &Service{
		repo:   repo,
		cache:  cache,
		logger: logger,
	}
}

// JoinSpectatorSession joins or creates spectator session
func (s *Service) JoinSpectatorSession(ctx context.Context, req *models.JoinSpectatorRequest, spectatorID uuid.UUID, ipAddress, userAgent string) (*models.SpectatorSession, error) {
	// Check if session already exists
	existingSessions, _, err := s.repo.ListSpectatorSessions(ctx, &req.TournamentID, nil, 1, 0)
	if err != nil {
		s.logger.Error("Failed to check existing sessions", zap.Error(err))
		return nil, fmt.Errorf("failed to check existing sessions: %w", err)
	}

	// Check if spectator already has active session
	for _, session := range existingSessions {
		if session.SpectatorID == spectatorID && session.Status == models.StatusActive {
			return session, nil
		}
	}

	// Create new session
	session := &models.SpectatorSession{
		SessionID:    uuid.New(),
		TournamentID: req.TournamentID,
		SpectatorID:  spectatorID,
		Status:       models.StatusConnecting,
		CameraSettings: &models.CameraSettings{
			Mode:       req.PreferredCameraMode,
			Smoothness: 0.5,
		},
		JoinedAt:     time.Now(),
		LastActivity: time.Now(),
		StreamQuality: req.StreamQuality,
		Nickname:     req.Nickname,
		IPAddress:    ipAddress,
		UserAgent:    userAgent,
	}

	if err := s.repo.CreateSpectatorSession(ctx, session); err != nil {
		s.logger.Error("Failed to create spectator session", zap.Error(err))
		return nil, fmt.Errorf("failed to create spectator session: %w", err)
	}

	// Update status to active
	session.Status = models.StatusActive
	if err := s.repo.UpdateSpectatorSession(ctx, session); err != nil {
		s.logger.Error("Failed to update session status", zap.Error(err))
	}

	return session, nil
}

// GetSpectatorSession retrieves spectator session
func (s *Service) GetSpectatorSession(ctx context.Context, sessionID uuid.UUID) (*models.SpectatorSession, error) {
	session, err := s.repo.GetSpectatorSession(ctx, sessionID)
	if err != nil {
		return nil, err
	}

	return session, nil
}

// LeaveSpectatorSession leaves spectator session
func (s *Service) LeaveSpectatorSession(ctx context.Context, sessionID uuid.UUID) error {
	session, err := s.repo.GetSpectatorSession(ctx, sessionID)
	if err != nil {
		return err
	}

	session.Status = models.StatusDisconnected
	session.LastActivity = time.Now()

	return s.repo.UpdateSpectatorSession(ctx, session)
}

// ListSpectatorSessions lists active sessions
func (s *Service) ListSpectatorSessions(ctx context.Context, tournamentID *uuid.UUID, status *models.SpectatorStatus, limit, offset int) (*models.SpectatorSessionList, error) {
	sessions, totalCount, err := s.repo.ListSpectatorSessions(ctx, tournamentID, status, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.SpectatorSessionList{
		Sessions:   sessions,
		TotalCount: totalCount,
		HasMore:    len(sessions) == limit,
	}, nil
}

// UpdateCameraSettings updates camera settings for session
func (s *Service) UpdateCameraSettings(ctx context.Context, sessionID uuid.UUID, settings *models.CameraSettings) error {
	session, err := s.repo.GetSpectatorSession(ctx, sessionID)
	if err != nil {
		return err
	}

	session.CameraSettings = settings
	session.LastActivity = time.Now()

	return s.repo.UpdateSpectatorSession(ctx, session)
}

// SendChatMessage sends chat message
func (s *Service) SendChatMessage(ctx context.Context, sessionID, senderID uuid.UUID, senderName, content string, messageType models.MessageType, replyTo *uuid.UUID) (*models.ChatMessage, error) {
	message := &models.ChatMessage{
		MessageID:   uuid.New(),
		SessionID:   sessionID,
		SenderID:    senderID,
		SenderName:  senderName,
		Content:     content,
		Timestamp:   time.Now(),
		MessageType: messageType,
		ReplyTo:     replyTo,
	}

	if err := s.repo.CreateChatMessage(ctx, message); err != nil {
		s.logger.Error("Failed to create chat message", zap.Error(err))
		return nil, fmt.Errorf("failed to create chat message: %w", err)
	}

	return message, nil
}

// GetChatMessages retrieves chat messages
func (s *Service) GetChatMessages(ctx context.Context, sessionID uuid.UUID, limit, offset int) (*models.ChatMessageList, error) {
	messages, totalCount, err := s.repo.GetChatMessages(ctx, sessionID, limit, offset)
	if err != nil {
		return nil, err
	}

	return &models.ChatMessageList{
		Messages:   messages,
		TotalCount: totalCount,
		HasMore:    len(messages) == limit,
	}, nil
}

// UpdateSessionActivity updates last activity timestamp
func (s *Service) UpdateSessionActivity(ctx context.Context, sessionID uuid.UUID) error {
	session, err := s.repo.GetSpectatorSession(ctx, sessionID)
	if err != nil {
		return err
	}

	session.LastActivity = time.Now()
	return s.repo.UpdateSpectatorSession(ctx, session)
}

// GetTournamentStats gets live tournament statistics
func (s *Service) GetTournamentStats(ctx context.Context, tournamentID uuid.UUID) (*models.TournamentStats, error) {
	// This would typically aggregate data from multiple sources
	// For now, return basic stats
	sessions, _, err := s.repo.ListSpectatorSessions(ctx, &tournamentID, nil, 1000, 0)
	if err != nil {
		return nil, err
	}

	stats := &models.TournamentStats{
		TournamentID:    tournamentID,
		TotalSpectators: len(sessions),
		ActiveSessions:  0,
		LastUpdated:     time.Now(),
	}

	for _, session := range sessions {
		if session.Status == models.StatusActive {
			stats.ActiveSessions++
		}
	}

	// Calculate peak spectators (simplified)
	stats.PeakSpectators = stats.TotalSpectators

	return stats, nil
}

// HealthCheck performs health check
func (s *Service) HealthCheck(ctx context.Context) *models.HealthResponse {
	return &models.HealthResponse{
		Status:    "healthy",
		Domain:    "tournament-spectator",
		Timestamp: time.Now(),
		Version:   "1.0.0",
	}
}
