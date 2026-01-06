package session

import (
	"context"
	"sync"

	"github.com/go-faster/errors"
	"github.com/gorilla/websocket"
	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"go.opentelemetry.io/otel/metric"
	"go.uber.org/zap"

	"github.com/gc-lover/necp-game/services/realtime-gateway-service-go/internal/service/protobuf"
)

// Config holds session manager configuration
type Config struct {
	DB              *pgxpool.Pool
	Redis           *redis.Client
	ProtobufHandler *protobuf.Handler
	Logger          *zap.Logger
	Meter           metric.Meter
}

// Manager manages WebSocket sessions
type Manager struct {
	config      Config
	logger      *zap.Logger
	db          *pgxpool.Pool
	redis       *redis.Client
	protobufHandler *protobuf.Handler
	meter       metric.Meter

	// Session storage
	sessions    map[string]*Session
	sessionsMu  sync.RWMutex

	// Channel subscriptions
	channels    map[string]map[string]*Session // channel -> sessionID -> session
	channelsMu  sync.RWMutex

	// Metrics
	activeSessions metric.Int64Gauge
	sessionCreated metric.Int64Counter
	sessionClosed  metric.Int64Counter
}

// NewManager creates a new session manager
func NewManager(config Config) *Manager {
	return &Manager{
		config:      config,
		logger:      config.Logger,
		db:          config.DB,
		redis:       config.Redis,
		protobufHandler: config.ProtobufHandler,
		meter:       config.Meter,
		sessions:    make(map[string]*Session),
		channels:    make(map[string]map[string]*Session),
	}
}

// Start starts the session manager
func (m *Manager) Start(ctx context.Context) error {
	m.logger.Info("starting session manager")

	// Initialize metrics
	var err error
	m.activeSessions, err = m.meter.Int64Gauge(
		"realtime_gateway_active_sessions",
		metric.WithDescription("Number of currently active WebSocket sessions"),
	)
	if err != nil {
		return err
	}

	m.sessionCreated, err = m.meter.Int64Counter(
		"realtime_gateway_sessions_created_total",
		metric.WithDescription("Total number of sessions created"),
	)
	if err != nil {
		return err
	}

	m.sessionClosed, err = m.meter.Int64Counter(
		"realtime_gateway_sessions_closed_total",
		metric.WithDescription("Total number of sessions closed"),
	)
	if err != nil {
		return err
	}

	m.logger.Info("session manager started")
	return nil
}

// Stop stops the session manager
func (m *Manager) Stop(ctx context.Context) error {
	m.logger.Info("stopping session manager")

	// Close all active sessions
	m.sessionsMu.Lock()
	for id, session := range m.sessions {
		if err := session.Close(); err != nil {
			m.logger.Error("failed to close session", zap.String("session_id", id), zap.Error(err))
		}
		delete(m.sessions, id)
	}
	m.sessionsMu.Unlock()

	m.logger.Info("session manager stopped")
	return nil
}

// CreateSession creates a new WebSocket session
func (m *Manager) CreateSession(ctx context.Context, conn *websocket.Conn) (*Session, error) {
	session := NewSession(SessionConfig{
		ID:              generateSessionID(),
		Connection:      conn,
		ProtobufHandler: m.protobufHandler,
		Logger:          m.logger,
		Manager:         m,
		OnClose: func(id string) {
			m.removeSession(id)
		},
	})

	// Add to active sessions
	m.sessionsMu.Lock()
	m.sessions[session.ID()] = session
	m.sessionsMu.Unlock()

	// Update metrics
	m.activeSessions.Set(int64(len(m.sessions)))
	m.sessionCreated.Add(ctx, 1)

	m.logger.Info("session created", zap.String("session_id", session.ID()))
	return session, nil
}

// GetSession retrieves a session by ID
func (m *Manager) GetSession(id string) (*Session, bool) {
	m.sessionsMu.RLock()
	session, exists := m.sessions[id]
	m.sessionsMu.RUnlock()
	return session, exists
}

// removeSession removes a session from active sessions
func (m *Manager) removeSession(id string) {
	m.sessionsMu.Lock()
	delete(m.sessions, id)
	m.sessionsMu.Unlock()

	// Update metrics
	m.activeSessions.Set(int64(len(m.sessions)))
	m.sessionClosed.Add(context.Background(), 1)

	m.logger.Info("session removed", zap.String("session_id", id))
}

// Broadcast sends a message to all active sessions
func (m *Manager) Broadcast(ctx context.Context, message []byte) error {
	m.sessionsMu.RLock()
	defer m.sessionsMu.RUnlock()

	for id, session := range m.sessions {
		if err := session.Send(message); err != nil {
			m.logger.Error("failed to send message to session",
				zap.String("session_id", id), zap.Error(err))
		}
	}

	return nil
}

// GetActiveSessionCount returns the number of active sessions
func (m *Manager) GetActiveSessionCount() int {
	m.sessionsMu.RLock()
	count := len(m.sessions)
	m.sessionsMu.RUnlock()
	return count
}

// SubscribeToChannel subscribes a session to a channel
func (m *Manager) SubscribeToChannel(sessionID, channel string) error {
	m.channelsMu.Lock()
	defer m.channelsMu.Unlock()

	if m.channels[channel] == nil {
		m.channels[channel] = make(map[string]*Session)
	}

	if session, exists := m.sessions[sessionID]; exists {
		m.channels[channel][sessionID] = session
		m.logger.Info("session subscribed to channel",
			zap.String("session_id", sessionID),
			zap.String("channel", channel))
		return nil
	}

	return errors.New("session not found")
}

// UnsubscribeFromChannel unsubscribes a session from a channel
func (m *Manager) UnsubscribeFromChannel(sessionID, channel string) error {
	m.channelsMu.Lock()
	defer m.channelsMu.Unlock()

	if channelSubs, exists := m.channels[channel]; exists {
		delete(channelSubs, sessionID)
		if len(channelSubs) == 0 {
			delete(m.channels, channel)
		}
		m.logger.Info("session unsubscribed from channel",
			zap.String("session_id", sessionID),
			zap.String("channel", channel))
		return nil
	}

	return errors.New("channel not found or session not subscribed")
}

// BroadcastToChannel broadcasts a message to all subscribers of a channel
func (m *Manager) BroadcastToChannel(ctx context.Context, channel string, message []byte) error {
	m.channelsMu.RLock()
	channelSubs, exists := m.channels[channel]
	m.channelsMu.RUnlock()

	if !exists {
		return errors.New("channel not found")
	}

	var wg sync.WaitGroup
	errorChan := make(chan error, len(channelSubs))

	for sessionID, session := range channelSubs {
		wg.Add(1)
		go func(sid string, sess *Session) {
			defer wg.Done()
			if err := sess.SendMessage(message); err != nil {
				m.logger.Error("failed to broadcast to session",
					zap.String("session_id", sid),
					zap.String("channel", channel),
					zap.Error(err))
				errorChan <- err
			}
		}(sessionID, session)
	}

	wg.Wait()
	close(errorChan)

	// Return first error if any
	select {
	case err := <-errorChan:
		return err
	default:
		return nil
	}
}

// SendPrivateMessage sends a message to a specific session
func (m *Manager) SendPrivateMessage(sessionID string, message []byte) error {
	m.sessionsMu.RLock()
	session, exists := m.sessions[sessionID]
	m.sessionsMu.RUnlock()

	if !exists {
		return errors.New("session not found")
	}

	return session.SendMessage(message)
}

// GetChannelSubscriberCount returns the number of subscribers for a channel
func (m *Manager) GetChannelSubscriberCount(channel string) int {
	m.channelsMu.RLock()
	defer m.channelsMu.RUnlock()

	if channelSubs, exists := m.channels[channel]; exists {
		return len(channelSubs)
	}
	return 0
}
