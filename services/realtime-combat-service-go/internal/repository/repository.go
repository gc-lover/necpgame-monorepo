// Issue: #2232
package repository

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"time"

	"github.com/go-redis/redis/v8"
	"github.com/lib/pq"
	"go.uber.org/zap"
)

// CombatSession represents a combat session in the database
type CombatSession struct {
	ID          string    `json:"id" db:"id"`
	Name        string    `json:"name" db:"name"`
	Type        string    `json:"type" db:"type"`
	Status      string    `json:"status" db:"status"`
	MaxPlayers  int       `json:"max_players" db:"max_players"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	StartedAt   *time.Time `json:"started_at" db:"started_at"`
	EndedAt     *time.Time `json:"ended_at" db:"ended_at"`
	MapID       string    `json:"map_id" db:"map_id"`
	GameMode    string    `json:"game_mode" db:"game_mode"`
}

// CombatEvent represents a combat event
type CombatEvent struct {
	ID        string          `json:"id"`
	SessionID string          `json:"session_id"`
	Type      string          `json:"type"`
	PlayerID  string          `json:"player_id"`
	Data      json.RawMessage `json:"data"`
	Timestamp time.Time       `json:"timestamp"`
}

// CombatRepository handles database operations
type CombatRepository struct {
	db     *sql.DB
	redis  *redis.Client
	logger *zap.SugaredLogger
}

// NewConnection creates a new database connection
func NewConnection(databaseURL string) (*sql.DB, error) {
	db, err := sql.Open("postgres", databaseURL)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	if err := db.Ping(); err != nil {
		return nil, fmt.Errorf("failed to ping database: %w", err)
	}

	// Configure connection pool
	db.SetMaxOpenConns(50)
	db.SetMaxIdleConns(10)
	db.SetConnMaxLifetime(time.Hour)

	return db, nil
}

// NewRedisClient creates a new Redis client
func NewRedisClient(redisURL string) (*redis.Client, error) {
	opt, err := redis.ParseURL(redisURL)
	if err != nil {
		return nil, err
	}

	client := redis.NewClient(opt)

	// Test connection
	ctx := context.Background()
	if err := client.Ping(ctx).Err(); err != nil {
		return nil, fmt.Errorf("failed to ping Redis: %w", err)
	}

	return client, nil
}

// NewCombatRepository creates a new combat repository
func NewCombatRepository(db *sql.DB, redis *redis.Client, logger *zap.SugaredLogger) *CombatRepository {
	return &CombatRepository{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// CreateCombatSession creates a new combat session
func (r *CombatRepository) CreateCombatSession(ctx context.Context, session *CombatSession) error {
	query := `
		INSERT INTO gameplay.combat_sessions (id, name, type, status, max_players, created_at, map_id, game_mode)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8)
	`

	_, err := r.db.ExecContext(ctx, query,
		session.ID, session.Name, session.Type, session.Status,
		session.MaxPlayers, session.CreatedAt, session.MapID, session.GameMode)

	if err != nil {
		r.logger.Errorf("Failed to create combat session: %v", err)
		return fmt.Errorf("failed to create combat session: %w", err)
	}

	// Cache session in Redis
	cacheKey := fmt.Sprintf("combat:session:%s", session.ID)
	sessionJSON, _ := json.Marshal(session)
	r.redis.Set(ctx, cacheKey, sessionJSON, 30*time.Minute)

	return nil
}

// GetCombatSession retrieves a combat session by ID
func (r *CombatRepository) GetCombatSession(ctx context.Context, sessionID string) (*CombatSession, error) {
	// Try cache first
	cacheKey := fmt.Sprintf("combat:session:%s", sessionID)
	cached, err := r.redis.Get(ctx, cacheKey).Result()
	if err == nil {
		var session CombatSession
		if err := json.Unmarshal([]byte(cached), &session); err == nil {
			return &session, nil
		}
	}

	// Fallback to database
	query := `
		SELECT id, name, type, status, max_players, created_at, started_at, ended_at, map_id, game_mode
		FROM gameplay.combat_sessions
		WHERE id = $1
	`

	var session CombatSession
	var startedAt, endedAt pq.NullTime

	err = r.db.QueryRowContext(ctx, query, sessionID).Scan(
		&session.ID, &session.Name, &session.Type, &session.Status,
		&session.MaxPlayers, &session.CreatedAt, &startedAt, &endedAt,
		&session.MapID, &session.GameMode,
	)

	if err != nil {
		if err == sql.ErrNoRows {
			return nil, fmt.Errorf("combat session not found")
		}
		r.logger.Errorf("Failed to get combat session: %v", err)
		return nil, fmt.Errorf("failed to get combat session: %w", err)
	}

	if startedAt.Valid {
		session.StartedAt = &startedAt.Time
	}
	if endedAt.Valid {
		session.EndedAt = &endedAt.Time
	}

	// Cache result
	sessionJSON, _ := json.Marshal(session)
	r.redis.Set(ctx, cacheKey, sessionJSON, 30*time.Minute)

	return &session, nil
}

// UpdateCombatSession updates a combat session
func (r *CombatRepository) UpdateCombatSession(ctx context.Context, session *CombatSession) error {
	query := `
		UPDATE gameplay.combat_sessions
		SET name = $2, type = $3, status = $4, max_players = $5,
		    started_at = $6, ended_at = $7, map_id = $8, game_mode = $9
		WHERE id = $1
	`

	_, err := r.db.ExecContext(ctx, query,
		session.ID, session.Name, session.Type, session.Status,
		session.MaxPlayers, session.StartedAt, session.EndedAt,
		session.MapID, session.GameMode)

	if err != nil {
		r.logger.Errorf("Failed to update combat session: %v", err)
		return fmt.Errorf("failed to update combat session: %w", err)
	}

	// Update cache
	cacheKey := fmt.Sprintf("combat:session:%s", session.ID)
	sessionJSON, _ := json.Marshal(session)
	r.redis.Set(ctx, cacheKey, sessionJSON, 30*time.Minute)

	return nil
}

// StoreCombatEvent stores a combat event
func (r *CombatRepository) StoreCombatEvent(ctx context.Context, event *CombatEvent) error {
	query := `
		INSERT INTO gameplay.combat_events (id, session_id, type, player_id, data, timestamp)
		VALUES ($1, $2, $3, $4, $5, $6)
	`

	_, err := r.db.ExecContext(ctx, query,
		event.ID, event.SessionID, event.Type, event.PlayerID, event.Data, event.Timestamp)

	if err != nil {
		r.logger.Errorf("Failed to store combat event: %v", err)
		return fmt.Errorf("failed to store combat event: %w", err)
	}

	return nil
}

// GetCombatEvents retrieves combat events for a session
func (r *CombatRepository) GetCombatEvents(ctx context.Context, sessionID string, limit int) ([]*CombatEvent, error) {
	query := `
		SELECT id, session_id, type, player_id, data, timestamp
		FROM gameplay.combat_events
		WHERE session_id = $1
		ORDER BY timestamp DESC
		LIMIT $2
	`

	rows, err := r.db.QueryContext(ctx, query, sessionID, limit)
	if err != nil {
		r.logger.Errorf("Failed to get combat events: %v", err)
		return nil, fmt.Errorf("failed to get combat events: %w", err)
	}
	defer rows.Close()

	var events []*CombatEvent
	for rows.Next() {
		var event CombatEvent
		err := rows.Scan(&event.ID, &event.SessionID, &event.Type,
			&event.PlayerID, &event.Data, &event.Timestamp)
		if err != nil {
			r.logger.Errorf("Failed to scan combat event: %v", err)
			continue
		}
		events = append(events, &event)
	}

	return events, nil
}
