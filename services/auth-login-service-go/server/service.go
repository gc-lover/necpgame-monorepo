// Package server Issue: #1
package server

import (
	"context"
	"crypto/rand"
	"crypto/subtle"
	"database/sql"
	"encoding/base64"
	"fmt"
	"sync"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
	"golang.org/x/crypto/argon2"

	"necpgame/services/auth-login-service-go/pkg/api"
)

// Service содержит бизнес-логику аутентификации с оптимизациями для MMOFPS
type Service struct {
	handler *authHandler
	logger  *zap.Logger
	db      *sql.DB

	// Memory pool для снижения аллокаций
	requestPool sync.Pool
}

// NewService создает новый сервис с оптимизациями
func NewService(logger *zap.Logger, db *sql.DB, jwtSecret string) *Service {
	return &Service{
		handler: newAuthHandler(logger, db, jwtSecret),
		logger:  logger,
		db:      db,
		requestPool: sync.Pool{
			New: func() interface{} {
				return &loginRequest{}
			},
		},
	}
}

// GetHandler возвращает обработчик для ogen сервера
func (s *Service) GetHandler() api.Handler {
	return s.handler
}

// loginRequest представляет собой объект запроса для пула
type loginRequest struct {
	email    string
	password string
}

// ValidateUserCredentials валидирует учетные данные пользователя
func (s *Service) ValidateUserCredentials(ctx context.Context, email, password string) (*UserCredentials, error) {
	// Используем context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	s.logger.Info("Validating user credentials", zap.String("email", email))

	// Получаем пользователя из БД
	user, err := s.getUserByEmail(ctx, email)
	if err != nil {
		return nil, fmt.Errorf("user not found: %w", err)
	}

	// Проверяем пароль с constant-time comparison
	if !s.verifyPassword(password, user.PasswordHash, user.Salt) {
		return nil, fmt.Errorf("invalid password")
	}

	return &UserCredentials{
		UserID: user.ID,
		Role:   user.Role,
		Email:  user.Email,
	}, nil
}

// UserCredentials представляет валидированные учетные данные пользователя
type UserCredentials struct {
	UserID string
	Role   string
	Email  string
}

// CreateUser создает нового пользователя (для регистрации)
func (s *Service) CreateUser(ctx context.Context, email, username, password string) (*UserCredentials, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	s.logger.Info("Creating new user", zap.String("email", email), zap.String("username", username))

	// Проверяем что пользователь не существует
	if _, err := s.getUserByEmail(ctx, email); err == nil {
		return nil, fmt.Errorf("user already exists")
	}

	// Генерируем соль и хэш пароля
	salt := make([]byte, 32)
	if _, err := rand.Read(salt); err != nil {
		return nil, fmt.Errorf("failed to generate salt: %w", err)
	}

	passwordHash := argon2.IDKey([]byte(password), salt, 1, 64*1024, 4, 32)

	userID := uuid.New().String()
	now := time.Now()

	// Создаем пользователя в БД
	query := `
		INSERT INTO auth.accounts (id, email, username, password_hash, salt, role, created_at, email_verified, two_factor_enabled)
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	_, err := s.db.ExecContext(ctx, query,
		userID, email, username,
		base64.StdEncoding.EncodeToString(passwordHash),
		base64.StdEncoding.EncodeToString(salt),
		"PLAYER", now, false, false,
	)

	if err != nil {
		return nil, fmt.Errorf("failed to create user: %w", err)
	}

	s.logger.Info("User created successfully", zap.String("user_id", userID))

	return &UserCredentials{
		UserID: userID,
		Role:   "PLAYER",
		Email:  email,
	}, nil
}

// UpdateUserLastLogin обновляет время последнего входа пользователя
func (s *Service) UpdateUserLastLogin(ctx context.Context, userID string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `UPDATE auth.accounts SET last_login_at = $1 WHERE id = $2`
	_, err := s.db.ExecContext(ctx, query, time.Now(), userID)

	if err != nil {
		s.logger.Error("Failed to update last login",
			zap.String("user_id", userID),
			zap.Error(err))
		return err
	}

	return nil
}

// RevokeUserTokens отзывает все токены пользователя (для logout)
func (s *Service) RevokeUserTokens(ctx context.Context, userID string) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.revoked_tokens (token, user_id, expires_at)
		SELECT rt.token, rt.user_id, rt.expires_at
		FROM auth.refresh_tokens rt
		WHERE rt.user_id = $1 AND rt.expires_at > $2
		ON CONFLICT (token) DO NOTHING
	`

	_, err := s.db.ExecContext(ctx, query, userID, time.Now())
	if err != nil {
		s.logger.Error("Failed to revoke user tokens",
			zap.String("user_id", userID),
			zap.Error(err))
		return err
	}

	return nil
}

// ValidateRefreshToken проверяет валидность refresh токена
func (s *Service) ValidateRefreshToken(ctx context.Context, token string) (*TokenClaims, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	// Проверяем что токен существует и не отозван
	query := `
		SELECT rt.user_id, rt.expires_at, a.role, a.email
		FROM auth.refresh_tokens rt
		JOIN auth.accounts a ON rt.user_id = a.id
		WHERE rt.token = $1 AND rt.expires_at > $2
	`

	var userID, role, email string
	var expiresAt time.Time

	err := s.db.QueryRowContext(ctx, query, token, time.Now()).Scan(
		&userID, &expiresAt, &role, &email)

	if err != nil {
		return nil, fmt.Errorf("invalid or expired token: %w", err)
	}

	return &TokenClaims{
		UserID:    userID,
		Role:      role,
		Email:     email,
		ExpiresAt: expiresAt,
	}, nil
}

// TokenClaims представляет claims токена
type TokenClaims struct {
	UserID    string
	Role      string
	Email     string
	ExpiresAt time.Time
}

// StoreRefreshToken сохраняет refresh токен в БД
func (s *Service) StoreRefreshToken(ctx context.Context, userID, token string, expiresAt time.Time) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.refresh_tokens (token, user_id, expires_at, created_at)
		VALUES ($1, $2, $3, $4)
		ON CONFLICT (token) DO UPDATE SET
			expires_at = EXCLUDED.expires_at,
			updated_at = $4
	`

	_, err := s.db.ExecContext(ctx, query, token, userID, expiresAt, time.Now())
	if err != nil {
		s.logger.Error("Failed to store refresh token",
			zap.String("user_id", userID),
			zap.Error(err))
		return err
	}

	return nil
}

// HealthCheck проверяет здоровье сервиса
func (s *Service) HealthCheck(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	// Проверяем подключение к БД
	if err := s.db.PingContext(ctx); err != nil {
		return fmt.Errorf("database health check failed: %w", err)
	}

	return nil
}

// Private methods

func (s *Service) getUserByEmail(ctx context.Context, email string) (*user, error) {
	query := `
		SELECT id, email, username, password_hash, salt, role, created_at, email_verified, two_factor_enabled
		FROM auth.accounts
		WHERE email = $1 AND deleted_at IS NULL
	`

	var u user
	err := s.db.QueryRowContext(ctx, query, email).Scan(
		&u.ID, &u.Email, &u.Username, &u.PasswordHash, &u.Salt, &u.Role,
		&u.CreatedAt, &u.EmailVerified, &u.TwoFactorEnabled,
	)

	if err != nil {
		return nil, err
	}

	return &u, nil
}

func (s *Service) verifyPassword(password, hash, salt string) bool {
	saltBytes, err := base64.StdEncoding.DecodeString(salt)
	if err != nil {
		return false
	}

	hashBytes, err := base64.StdEncoding.DecodeString(hash)
	if err != nil {
		return false
	}

	computedHash := argon2.IDKey([]byte(password), saltBytes, 1, 64*1024, 4, 32)
	return subtle.ConstantTimeCompare(hashBytes, computedHash) == 1
}
