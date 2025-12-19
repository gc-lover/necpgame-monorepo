// SQL queries use prepared statements with placeholders ($1, $2, ?) for safety
package server

import (
	"context"
	"encoding/json"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"github.com/sirupsen/logrus"
)

type SessionStatus string

const (
	SessionStatusCreated      SessionStatus = "created"
	SessionStatusActive       SessionStatus = "active"
	SessionStatusIdle         SessionStatus = "idle"
	SessionStatusAFK          SessionStatus = "afk"
	SessionStatusDisconnected SessionStatus = "disconnected"
	SessionStatusExpired      SessionStatus = "expired"
	SessionStatusClosed       SessionStatus = "closed"
)

type PlayerSession struct {
	ID              uuid.UUID     `json:"id"`
	PlayerID        string        `json:"player_id"`
	Token           string        `json:"token"`
	ReconnectToken  string        `json:"reconnect_token"`
	Status          SessionStatus `json:"status"`
	ServerID        string        `json:"server_id"`
	IPAddress       string        `json:"ip_address"`
	UserAgent       string        `json:"user_agent"`
	CharacterID     *uuid.UUID    `json:"character_id,omitempty"`
	LastHeartbeat   time.Time     `json:"last_heartbeat"`
	CreatedAt       time.Time     `json:"created_at"`
	DisconnectCount int           `json:"disconnect_count"`
}

type SessionManagerInterface interface {
	CreateSession(ctx context.Context, playerID, ipAddress, userAgent string, characterID *uuid.UUID) (*PlayerSession, error)
	GetSessionByToken(ctx context.Context, token string) (*PlayerSession, error)
	GetSessionByPlayerID(ctx context.Context, playerID string) (*PlayerSession, error)
	UpdateHeartbeat(ctx context.Context, token string) error
	ReconnectSession(ctx context.Context, reconnectToken, ipAddress, userAgent string) (*PlayerSession, error)
	CloseSession(ctx context.Context, token string) error
	DisconnectSession(ctx context.Context, token string) error
	GetActiveSessionsCount(ctx context.Context) (int, error)
	CleanupExpiredSessions(ctx context.Context) error
	SaveSession(ctx context.Context, session *PlayerSession) error
}

type SessionManager struct {
	redis             *redis.Client
	logger            *logrus.Logger
	serverID          string
	heartbeatInterval time.Duration
	idleTimeout       time.Duration
	afkTimeout        time.Duration
}

func NewSessionManager(redisURL, serverID string) (*SessionManager, error) {
	redisOpts, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	redisClient := redis.NewClient(redisOpts)

	return &SessionManager{
		redis:             redisClient,
		logger:            GetLogger(),
		serverID:          serverID,
		heartbeatInterval: 30 * time.Second,
		idleTimeout:       2 * time.Minute,
		afkTimeout:        5 * time.Minute,
	}, nil
}

func (sm *SessionManager) CreateSession(ctx context.Context, playerID, ipAddress, userAgent string, characterID *uuid.UUID) (*PlayerSession, error) {
	session := &PlayerSession{
		ID:              uuid.New(),
		PlayerID:        playerID,
		Token:           uuid.New().String(),
		ReconnectToken:  uuid.New().String(),
		Status:          SessionStatusCreated,
		ServerID:        sm.serverID,
		IPAddress:       ipAddress,
		UserAgent:       userAgent,
		CharacterID:     characterID,
		LastHeartbeat:   time.Now(),
		CreatedAt:       time.Now(),
		DisconnectCount: 0,
	}

	session.Status = SessionStatusActive

	err := sm.SaveSession(ctx, session)
	if err != nil {
		return nil, err
	}

	sm.logger.WithFields(logrus.Fields{
		"session_id": session.ID,
		"player_id":  playerID,
		"server_id":  sm.serverID,
	}).Info("Session created")

	RecordSessionCreated()
	count, _ := sm.GetActiveSessionsCount(ctx)
	SetActiveSessions(float64(count))

	return session, nil
}

func (sm *SessionManager) SaveSession(ctx context.Context, session *PlayerSession) error {
	sessionJSON, err := json.Marshal(session)
	if err != nil {
		return err
	}

	pipe := sm.redis.Pipeline()
	pipe.Set(ctx, "session:"+session.Token, sessionJSON, 24*time.Hour)
	pipe.Set(ctx, "player_session:"+session.PlayerID, sessionJSON, 24*time.Hour)
	pipe.SAdd(ctx, "active_players:"+sm.serverID, session.PlayerID)
	pipe.Expire(ctx, "active_players:"+sm.serverID, 24*time.Hour)

	_, err = pipe.Exec(ctx)
	return err
}

func (sm *SessionManager) GetSessionByToken(ctx context.Context, token string) (*PlayerSession, error) {
	data, err := sm.redis.Get(ctx, "session:"+token).Result()
	if err == redis.Nil {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	var session PlayerSession
	err = json.Unmarshal([]byte(data), &session)
	if err != nil {
		return nil, err
	}

	return &session, nil
}

func (sm *SessionManager) GetSessionByPlayerID(ctx context.Context, playerID string) (*PlayerSession, error) {
	data, err := sm.redis.Get(ctx, "player_session:"+playerID).Result()
	if err == redis.Nil {
		return nil, nil
	}
	if err != nil {
		return nil, err
	}

	var session PlayerSession
	err = json.Unmarshal([]byte(data), &session)
	if err != nil {
		return nil, err
	}

	return &session, nil
}

func (sm *SessionManager) UpdateHeartbeat(ctx context.Context, token string) error {
	session, err := sm.GetSessionByToken(ctx, token)
	if err != nil {
		return err
	}
	if session == nil {
		return nil
	}

	now := time.Now()
	timeSinceLastHeartbeat := now.Sub(session.LastHeartbeat)
	session.LastHeartbeat = now

	if timeSinceLastHeartbeat > sm.afkTimeout {
		session.Status = SessionStatusAFK
	} else if timeSinceLastHeartbeat > sm.idleTimeout {
		session.Status = SessionStatusIdle
	} else {
		session.Status = SessionStatusActive
	}

	RecordHeartbeat()
	return sm.SaveSession(ctx, session)
}

func (sm *SessionManager) ReconnectSession(ctx context.Context, reconnectToken string, ipAddress, userAgent string) (*PlayerSession, error) {
	sessions, err := sm.redis.Keys(ctx, "session:*").Result()
	if err != nil {
		return nil, err
	}

	for _, key := range sessions {
		data, err := sm.redis.Get(ctx, key).Result()
		if err != nil {
			continue
		}

		var session PlayerSession
		if err := json.Unmarshal([]byte(data), &session); err != nil {
			continue
		}

		if session.ReconnectToken == reconnectToken {
			if session.Status == SessionStatusDisconnected || session.Status == SessionStatusExpired {
				reconnectWindow := 5 * time.Minute
				if time.Since(session.LastHeartbeat) > reconnectWindow {
					return nil, nil
				}

				session.Status = SessionStatusActive
				session.LastHeartbeat = time.Now()
				session.DisconnectCount++
				session.IPAddress = ipAddress
				session.UserAgent = userAgent

				err = sm.SaveSession(ctx, &session)
				if err != nil {
					return nil, err
				}

				sm.logger.WithFields(logrus.Fields{
					"session_id": session.ID,
					"player_id":  session.PlayerID,
				}).Info("Session reconnected")

				RecordSessionReconnected()
				return &session, nil
			}
		}
	}

	return nil, nil
}

func (sm *SessionManager) CloseSession(ctx context.Context, token string) error {
	session, err := sm.GetSessionByToken(ctx, token)
	if err != nil {
		return err
	}
	if session == nil {
		return nil
	}

	session.Status = SessionStatusClosed

	pipe := sm.redis.Pipeline()
	pipe.Del(ctx, "session:"+token)
	pipe.Del(ctx, "player_session:"+session.PlayerID)
	pipe.SRem(ctx, "active_players:"+sm.serverID, session.PlayerID)

	_, err = pipe.Exec(ctx)
	return err
}

func (sm *SessionManager) DisconnectSession(ctx context.Context, token string) error {
	session, err := sm.GetSessionByToken(ctx, token)
	if err != nil {
		return err
	}
	if session == nil {
		return nil
	}

	session.Status = SessionStatusDisconnected
	return sm.SaveSession(ctx, session)
}

func (sm *SessionManager) GetActiveSessionsCount(ctx context.Context) (int, error) {
	count, err := sm.redis.SCard(ctx, "active_players:"+sm.serverID).Result()
	return int(count), err
}

func (sm *SessionManager) CleanupExpiredSessions(ctx context.Context) error {
	sessions, err := sm.redis.Keys(ctx, "session:*").Result()
	if err != nil {
		return err
	}

	now := time.Now()
	expiredCount := 0

	for _, key := range sessions {
		data, err := sm.redis.Get(ctx, key).Result()
		if err != nil {
			continue
		}

		var session PlayerSession
		if err := json.Unmarshal([]byte(data), &session); err != nil {
			continue
		}

		if session.Status == SessionStatusDisconnected || session.Status == SessionStatusExpired {
			if now.Sub(session.LastHeartbeat) > 10*time.Minute {
				sm.CloseSession(ctx, session.Token)
				expiredCount++
				RecordSessionExpired()
			}
		}
	}

	if expiredCount > 0 {
		sm.logger.WithField("count", expiredCount).Info("Cleaned up expired sessions")
	}

	return nil
}

func (sm *SessionManager) GetRedisClient() *redis.Client {
	return sm.redis
}
