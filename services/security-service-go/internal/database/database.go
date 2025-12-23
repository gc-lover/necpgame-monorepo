package database

import (
	"context"
	"time"

	"github.com/jackc/pgx/v5/pgxpool"
	"github.com/redis/go-redis/v9"
	"github.com/rs/zerolog"
)

// Service provides database operations for the security service
type Service struct {
	db     *pgxpool.Pool
	redis  *redis.Client
	logger zerolog.Logger
}

// New creates a new database service
func New(db *pgxpool.Pool, redis *redis.Client, logger zerolog.Logger) *Service {
	return &Service{
		db:     db,
		redis:  redis,
		logger: logger,
	}
}

// Health checks database connectivity
func (s *Service) Health(ctx context.Context) error {
	return s.db.Ping(ctx)
}

// User represents a user in the database
type User struct {
	ID             string    `json:"id" db:"id"`
	Username       string    `json:"username" db:"username"`
	Email          string    `json:"email" db:"email"`
	EmailVerified  bool      `json:"email_verified" db:"email_verified"`
	Phone          *string   `json:"phone,omitempty" db:"phone"`
	PhoneVerified  bool      `json:"phone_verified" db:"phone_verified"`
	PasswordHash   string    `json:"-" db:"password_hash"`
	Roles          []string  `json:"roles" db:"roles"`
	Permissions    []string  `json:"permissions" db:"permissions"`
	LastLogin      *time.Time `json:"last_login,omitempty" db:"last_login"`
	LoginCount     int       `json:"login_count" db:"login_count"`
	AccountStatus  string    `json:"account_status" db:"account_status"`
	CreatedAt      time.Time `json:"created_at" db:"created_at"`
	UpdatedAt      time.Time `json:"updated_at" db:"updated_at"`
}

// Role represents a user role
type Role struct {
	ID            string    `json:"id" db:"id"`
	Name          string    `json:"name" db:"name"`
	Description   string    `json:"description" db:"description"`
	Permissions   []string  `json:"permissions" db:"permissions"`
	IsSystemRole  bool      `json:"is_system_role" db:"is_system_role"`
	CreatedAt     time.Time `json:"created_at" db:"created_at"`
}

// SecurityThreat represents a security threat
type SecurityThreat struct {
	ID             string     `json:"id" db:"id"`
	Type           string     `json:"type" db:"type"`
	Severity       string     `json:"severity" db:"severity"`
	Status         string     `json:"status" db:"status"`
	Description    string     `json:"description" db:"description"`
	UserID         *string    `json:"user_id,omitempty" db:"user_id"`
	IPAddress      *string    `json:"ip_address,omitempty" db:"ip_address"`
	UserAgent      *string    `json:"user_agent,omitempty" db:"user_agent"`
	Location       *string    `json:"location,omitempty" db:"location"`
	ConfidenceScore *float64   `json:"confidence_score,omitempty" db:"confidence_score"`
	DetectedAt     time.Time  `json:"detected_at" db:"detected_at"`
	ResolvedAt     *time.Time `json:"resolved_at,omitempty" db:"resolved_at"`
	ActionsTaken   []string   `json:"actions_taken" db:"actions_taken"`
}

// User operations
func (s *Service) GetUserByID(ctx context.Context, userID string) (*User, error) {
	query := `
		SELECT id, username, email, email_verified, phone, phone_verified,
			   password_hash, roles, permissions, last_login, login_count,
			   account_status, created_at, updated_at
		FROM users WHERE id = $1
	`

	var user User
	err := s.db.QueryRow(ctx, query, userID).Scan(
		&user.ID, &user.Username, &user.Email, &user.EmailVerified,
		&user.Phone, &user.PhoneVerified, &user.PasswordHash, &user.Roles,
		&user.Permissions, &user.LastLogin, &user.LoginCount,
		&user.AccountStatus, &user.CreatedAt, &user.UpdatedAt,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user by ID")
		return nil, err
	}

	return &user, nil
}

func (s *Service) GetUserByUsername(ctx context.Context, username string) (*User, error) {
	query := `
		SELECT id, username, email, email_verified, phone, phone_verified,
			   password_hash, roles, permissions, last_login, login_count,
			   account_status, created_at, updated_at
		FROM users WHERE username = $1
	`

	var user User
	err := s.db.QueryRow(ctx, query, username).Scan(
		&user.ID, &user.Username, &user.Email, &user.EmailVerified,
		&user.Phone, &user.PhoneVerified, &user.PasswordHash, &user.Roles,
		&user.Permissions, &user.LastLogin, &user.LoginCount,
		&user.AccountStatus, &user.CreatedAt, &user.UpdatedAt,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("username", username).Msg("Failed to get user by username")
		return nil, err
	}

	return &user, nil
}

func (s *Service) UpdateUserLastLogin(ctx context.Context, userID string) error {
	query := `
		UPDATE users
		SET last_login = NOW(), login_count = login_count + 1, updated_at = NOW()
		WHERE id = $1
	`

	_, err := s.db.Exec(ctx, query, userID)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to update user last login")
		return err
	}

	return nil
}

// Session management with Redis
func (s *Service) StoreSession(ctx context.Context, sessionID, userID string, ttl time.Duration) error {
	key := "session:" + sessionID
	return s.redis.Set(ctx, key, userID, ttl).Err()
}

func (s *Service) GetSession(ctx context.Context, sessionID string) (string, error) {
	key := "session:" + sessionID
	return s.redis.Get(ctx, key).Result()
}

func (s *Service) DeleteSession(ctx context.Context, sessionID string) error {
	key := "session:" + sessionID
	return s.redis.Del(ctx, key).Err()
}

// JWT blacklist
func (s *Service) BlacklistToken(ctx context.Context, token string, ttl time.Duration) error {
	key := "blacklist:" + token
	return s.redis.Set(ctx, key, "blacklisted", ttl).Err()
}

func (s *Service) IsTokenBlacklisted(ctx context.Context, token string) (bool, error) {
	key := "blacklist:" + token
	exists, err := s.redis.Exists(ctx, key).Result()
	return exists > 0, err
}

// Role operations
func (s *Service) GetUserRoles(ctx context.Context, userID string) ([]Role, error) {
	query := `
		SELECT r.id, r.name, r.description, r.permissions, r.is_system_role, r.created_at
		FROM roles r
		JOIN user_roles ur ON r.id = ur.role_id
		WHERE ur.user_id = $1
	`

	rows, err := s.db.Query(ctx, query, userID)
	if err != nil {
		s.logger.Error().Err(err).Str("user_id", userID).Msg("Failed to get user roles")
		return nil, err
	}
	defer rows.Close()

	var roles []Role
	for rows.Next() {
		var role Role
		err := rows.Scan(&role.ID, &role.Name, &role.Description, &role.Permissions,
			&role.IsSystemRole, &role.CreatedAt)
		if err != nil {
			s.logger.Error().Err(err).Msg("Failed to scan role")
			continue
		}
		roles = append(roles, role)
	}

	return roles, rows.Err()
}

// Threat detection
func (s *Service) CreateSecurityThreat(ctx context.Context, threat *SecurityThreat) error {
	query := `
		INSERT INTO security_threats (
			id, type, severity, status, description, user_id, ip_address,
			user_agent, location, confidence_score, detected_at, actions_taken
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)
	`

	_, err := s.db.Exec(ctx, query,
		threat.ID, threat.Type, threat.Severity, threat.Status, threat.Description,
		threat.UserID, threat.IPAddress, threat.UserAgent, threat.Location,
		threat.ConfidenceScore, threat.DetectedAt, threat.ActionsTaken,
	)
	if err != nil {
		s.logger.Error().Err(err).Str("threat_id", threat.ID).Msg("Failed to create security threat")
		return err
	}

	return nil
}

func (s *Service) GetSecurityThreats(ctx context.Context, limit, offset int) ([]SecurityThreat, error) {
	query := `
		SELECT id, type, severity, status, description, user_id, ip_address,
			   user_agent, location, confidence_score, detected_at, resolved_at, actions_taken
		FROM security_threats
		ORDER BY detected_at DESC
		LIMIT $1 OFFSET $2
	`

	rows, err := s.db.Query(ctx, query, limit, offset)
	if err != nil {
		s.logger.Error().Err(err).Msg("Failed to get security threats")
		return nil, err
	}
	defer rows.Close()

	var threats []SecurityThreat
	for rows.Next() {
		var threat SecurityThreat
		err := rows.Scan(&threat.ID, &threat.Type, &threat.Severity, &threat.Status,
			&threat.Description, &threat.UserID, &threat.IPAddress, &threat.UserAgent,
			&threat.Location, &threat.ConfidenceScore, &threat.DetectedAt,
			&threat.ResolvedAt, &threat.ActionsTaken)
		if err != nil {
			s.logger.Error().Err(err).Msg("Failed to scan security threat")
			continue
		}
		threats = append(threats, threat)
	}

	return threats, rows.Err()
}
