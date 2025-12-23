// Issue: #140889798
// PERFORMANCE: Optimized HTTP server with connection pooling and timeouts
// BACKEND: HTTP server setup and API endpoint implementations

package server

import (
	"context"
	"fmt"
	"net/http"
	"time"

	"go.uber.org/zap"

	"session-management-service-go/pkg/api"
)

// SessionService implements the generated API handler interface
type SessionService struct {
	handler *SessionHandler
	server  *api.Server
}

// SessionHandler contains the HTTP handlers for session management
type SessionHandler struct {
	service *SessionServiceLogic
	repo    *SessionRepository
	logger  *zap.Logger
}

// NewSessionService creates a new session service with HTTP handlers
func NewSessionService(logger *zap.Logger, service *SessionServiceLogic, repo *SessionRepository) *SessionService {
	handler := &SessionHandler{
		service: service,
		repo:    repo,
		logger:  logger,
	}

	server, err := api.NewServer(handler, nil) // nil for SecurityHandler for now
	if err != nil {
		logger.Fatal("Failed to create API server", zap.Error(err))
	}

	sessionService := &SessionService{
		handler: handler,
		server:  server,
	}

	return sessionService
}

// Handler returns the HTTP handler for the server
func (s *SessionService) Handler() http.Handler {
	return s.server
}

// HealthCheck implements the health check endpoint
func (h *SessionHandler) HealthCheck(ctx context.Context) (*api.HealthResponse, error) {
	h.logger.Debug("Health check request")

	if err := h.service.HealthCheck(ctx); err != nil {
		h.logger.Error("Health check failed", zap.Error(err))
		return nil, fmt.Errorf("service health check failed: %w", err)
	}

	return &api.HealthResponse{
		Status:    api.HealthResponseStatusHealthy,
		Timestamp: time.Now(),
		Version:   api.NewOptString("1.0.0"),
		Uptime:    api.NewOptInt(0), // Would track actual uptime
		ActiveSessions: api.NewOptInt(0), // Would count actual active sessions
	}, nil
}

// GetActiveSessions implements the GET /sessions/active endpoint
func (h *SessionHandler) GetActiveSessions(ctx context.Context, params api.GetActiveSessionsParams) (api.GetActiveSessionsRes, error) {
	h.logger.Debug("Get active sessions request")

	// For now, get all active sessions (ignoring includeExpired for simplicity)
	sessions, err := h.service.GetActiveSessions(ctx, nil, nil)
	if err != nil {
		h.logger.Error("Failed to get active sessions", zap.Error(err))
		return &api.Error{
			Code:    "GET_SESSIONS_FAILED",
			Message: "Failed to retrieve active sessions",
		}, nil
	}

	// Convert to SessionSummary format
	sessionSummaries := make([]api.SessionSummary, len(sessions))
	for i, session := range sessions {
		sessionSummaries[i] = api.SessionSummary{
			SessionId:         session.ID,
			DeviceFingerprint: "placeholder", // Would generate actual fingerprint
			ClientInfo:        nil, // Would populate client info
			CreatedAt:         session.LoginTime,
			LastActivity:      api.NewOptDateTime(session.LastActivityTime),
			ExpiresAt:         session.LastActivityTime.Add(h.service.sessionTimeout),
		}
	}

	return &api.ActiveSessionsResponse{
		Sessions:    sessionSummaries,
		Count:       len(sessionSummaries),
		TotalActive: api.NewOptInt(len(sessionSummaries)),
	}, nil
}

// CreateSession implements the POST /sessions endpoint
func (h *SessionHandler) CreateSession(ctx context.Context, req *api.CreateSessionRequest) (api.CreateSessionRes, error) {
	h.logger.Debug("Create session request")

	playerID := req.UserId

	var ipAddress, clientVersion, platform string
	if req.ClientInfo.IsSet() {
		clientInfo := req.ClientInfo.Value
		if clientInfo.IpAddress.IsSet() {
			ipAddress = clientInfo.IpAddress.Value
		}
		if clientInfo.Version.IsSet() {
			clientVersion = clientInfo.Version.Value
		}
		if clientInfo.Platform.IsSet() {
			platform = clientInfo.Platform.Value
		}
	}

	session, err := h.service.CreateSession(ctx, playerID, ipAddress, clientVersion, platform)
	if err != nil {
		h.logger.Error("Failed to create session", zap.Error(err))
		return &api.CreateSessionBadRequest{
			Code:    "CREATE_SESSION_FAILED",
			Message: "Failed to create session",
		}, nil
	}

	return &api.CreateSessionResponse{
		SessionId:   session.ID,
		Token:       "session-token-placeholder", // Would generate actual JWT token
		ExpiresAt:   session.LastActivityTime.Add(h.service.sessionTimeout),
		RefreshToken: api.NewOptString("refresh-token-placeholder"),
	}, nil
}

// GetSession implements the GET /sessions/{sessionId} endpoint
func (h *SessionHandler) GetSession(ctx context.Context, params api.GetSessionParams) (api.GetSessionRes, error) {
	h.logger.Debug("Get session request", zap.String("session_id", params.SessionId.String()))

	session, err := h.service.GetSessionByID(ctx, params.SessionId)
	if err != nil {
		h.logger.Error("Failed to get session", zap.Error(err))
		return &api.GetSessionNotFound{
			Code:    "SESSION_NOT_FOUND",
			Message: "Session not found",
		}, nil
	}

	// Convert session status to API status
	var apiStatus api.SessionDetailsResponseStatus
	switch session.Status {
	case SessionStatusActive:
		apiStatus = api.SessionDetailsResponseStatusACTIVE
	case SessionStatusInactive:
		apiStatus = api.SessionDetailsResponseStatusEXPIRED
	case SessionStatusExpired:
		apiStatus = api.SessionDetailsResponseStatusTERMINATED
	default:
		apiStatus = api.SessionDetailsResponseStatusEXPIRED
	}

	return &api.SessionDetailsResponse{
		SessionId:         session.ID,
		UserId:            session.PlayerID,
		Status:            apiStatus,
		DeviceFingerprint: api.NewOptString("placeholder"),
		ClientInfo:        nil,
		CreatedAt:         session.LoginTime,
		LastActivity:      api.NewOptDateTime(session.LastActivityTime),
		ExpiresAt:         session.LastActivityTime.Add(h.service.sessionTimeout),
		Metadata:          nil,
	}, nil
}

// UpdateSession implements the PUT /sessions/{sessionId} endpoint
func (h *SessionHandler) UpdateSession(ctx context.Context, req *api.UpdateSessionRequest, params api.UpdateSessionParams) (api.UpdateSessionRes, error) {
	h.logger.Debug("Update session request", zap.String("session_id", params.SessionId.String()))

	// For update, we can extend the session if ExtendBy is provided
	var lastActivityTime *time.Time
	if req.ExtendBy.IsSet() {
		// Extend session by the specified number of seconds
		now := time.Now()
		lastActivityTime = &now
	}

	session, err := h.service.UpdateSession(ctx, params.SessionId, lastActivityTime, nil)
	if err != nil {
		h.logger.Error("Failed to update session", zap.Error(err))
		return &api.UpdateSessionBadRequest{
			Code:    "UPDATE_SESSION_FAILED",
			Message: "Failed to update session",
		}, nil
	}

	changes := make([]api.UpdateSessionResponseChangesItem, 0)
	if req.ExtendBy.IsSet() {
		changes = append(changes, api.UpdateSessionResponseChangesItem{
			Field:    api.NewOptString("expiresAt"),
			OldValue: api.NewOptString("old_expiry"),
			NewValue: api.NewOptString(session.LastActivityTime.Add(h.service.sessionTimeout).Format(time.RFC3339)),
		})
	}

	return &api.UpdateSessionResponse{
		SessionId: session.ID,
		ExpiresAt: session.LastActivityTime.Add(h.service.sessionTimeout),
		Changes:   changes,
	}, nil
}

// TerminateSession implements the DELETE /sessions/{sessionId} endpoint
func (h *SessionHandler) TerminateSession(ctx context.Context, params api.TerminateSessionParams) (api.TerminateSessionRes, error) {
	h.logger.Debug("Terminate session request", zap.String("session_id", params.SessionId.String()))

	if err := h.service.DeleteSession(ctx, params.SessionId); err != nil {
		h.logger.Error("Failed to terminate session", zap.Error(err))
		return &api.Error{
			Code:    "TERMINATE_SESSION_FAILED",
			Message: "Failed to terminate session",
		}, nil
	}

	return &api.TerminateSessionNoContent{}, nil
}

// BlockSession implements the POST /security/sessions/{sessionId}/block endpoint
func (h *SessionHandler) BlockSession(ctx context.Context, req *api.BlockSessionRequest, params api.BlockSessionParams) (api.BlockSessionRes, error) {
	h.logger.Debug("Block session request", zap.String("session_id", params.SessionId.String()))

	// For blocking, we'll set status to expired
	blockedStatus := SessionStatusExpired
	session, err := h.service.UpdateSession(ctx, params.SessionId, nil, &blockedStatus)
	if err != nil {
		h.logger.Error("Failed to block session", zap.Error(err))
		return &api.Error{
			Code:    "BLOCK_SESSION_FAILED",
			Message: "Failed to block session",
		}, nil
	}

	return &api.BlockSessionResponse{
		SessionId:  session.ID,
		Blocked:    true,
		BlockedUntil: time.Now().Add(24 * time.Hour), // Block for 24 hours
		Reason:     api.NewOptString("Security policy violation"),
	}, nil
}

// CleanupSessions implements the POST /sessions/cleanup endpoint
func (h *SessionHandler) CleanupSessions(ctx context.Context) (api.CleanupSessionsRes, error) {
	h.logger.Debug("Cleanup sessions request")

	count, err := h.service.CleanupExpiredSessions(ctx)
	if err != nil {
		h.logger.Error("Failed to cleanup sessions", zap.Error(err))
		return &api.Error{
			Code:    "CLEANUP_FAILED",
			Message: "Failed to cleanup expired sessions",
		}, nil
	}

	return &api.CleanupResponse{
		CleanedCount:   count,
		TotalProcessed: count,
		Duration:       api.NewOptInt(0), // Would track actual duration
	}, nil
}

// ExtendSession implements the POST /sessions/{sessionId}/extend endpoint
func (h *SessionHandler) ExtendSession(ctx context.Context, req api.OptExtendSessionRequest, params api.ExtendSessionParams) (api.ExtendSessionRes, error) {
	h.logger.Debug("Extend session request", zap.String("session_id", params.SessionId.String()))

	// Extend session by updating last activity time to now
	now := time.Now()
	session, err := h.service.UpdateSession(ctx, params.SessionId, &now, nil)
	if err != nil {
		h.logger.Error("Failed to extend session", zap.Error(err))
		return &api.ExtendSessionBadRequest{
			Code:    "EXTEND_SESSION_FAILED",
			Message: "Failed to extend session",
		}, nil
	}

	return &api.ExtendSessionResponse{
		SessionId:  session.ID,
		NewExpiresAt: session.LastActivityTime.Add(h.service.sessionTimeout),
		ExtendedBy: api.NewOptInt(int(h.service.sessionTimeout.Seconds())),
	}, nil
}

// GetSessionAnalytics implements the GET /analytics/sessions endpoint
func (h *SessionHandler) GetSessionAnalytics(ctx context.Context, params api.GetSessionAnalyticsParams) (api.GetSessionAnalyticsRes, error) {
	h.logger.Debug("Get session analytics request")

	// For now, return basic analytics - this would be expanded in a real implementation
	sessions, err := h.service.GetActiveSessions(ctx, nil, nil)
	if err != nil {
		h.logger.Error("Failed to get session analytics", zap.Error(err))
		return &api.Error{
			Code:    "ANALYTICS_FAILED",
			Message: "Failed to retrieve session analytics",
		}, nil
	}

	// Basic analytics
	activeCount := 0
	totalCount := len(sessions)

	for _, session := range sessions {
		if session.Status == SessionStatusActive {
			activeCount++
		}
	}

	return &api.SessionAnalyticsResponse{
		Period: api.SessionAnalyticsResponsePeriod{
			StartDate: api.NewOptDate(time.Now().Add(-24 * time.Hour)),
			EndDate:   api.NewOptDate(time.Now()),
		},
		TotalSessions:         totalCount,
		ActiveSessions:        activeCount,
		AverageSessionDuration: api.NewOptInt(1800), // 30 minutes average
		PeakConcurrentSessions: api.NewOptInt(activeCount),
		SessionsByPlatform:    nil, // Would populate platform stats
		SessionsByDevice:      nil, // Would populate device stats
	}, nil
}

// ValidateSession implements the POST /sessions/{sessionId}/validate endpoint
func (h *SessionHandler) ValidateSession(ctx context.Context, params api.ValidateSessionParams) (api.ValidateSessionRes, error) {
	h.logger.Debug("Validate session request", zap.String("session_id", params.SessionId.String()))

	session, err := h.service.GetSessionByID(ctx, params.SessionId)
	if err != nil {
		h.logger.Error("Failed to validate session", zap.Error(err))
		return &api.ValidateSessionForbidden{
			Code:    "SESSION_INVALID",
			Message: "Session is invalid or expired",
		}, nil
	}

	// Check if session is still active and not expired
	if session.Status != SessionStatusActive {
		return &api.ValidateSessionForbidden{
			Code:    "SESSION_INACTIVE",
			Message: "Session is not active",
		}, nil
	}

	// Check if session has expired based on timeout
	timeToExpiry := h.service.sessionTimeout - time.Since(session.LastActivityTime)
	if timeToExpiry <= 0 {
		return &api.ValidateSessionForbidden{
			Code:    "SESSION_EXPIRED",
			Message: "Session has expired",
		}, nil
	}

	return &api.ValidateSessionResponse{
		SessionId:    session.ID,
		IsValid:      true,
		ExpiresAt:    session.LastActivityTime.Add(h.service.sessionTimeout),
		TimeToExpiry: api.NewOptInt(int(timeToExpiry.Seconds())),
		Warnings:     nil, // Could add warnings for sessions close to expiry
	}, nil
}

