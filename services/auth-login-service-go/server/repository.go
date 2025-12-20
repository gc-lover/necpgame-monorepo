// Package server Issue: #1
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"go.uber.org/zap"

	_ "github.com/jackc/pgx/v5/stdlib" // PostgreSQL driver
)

// Repository представляет слой доступа к данным с оптимизациями
type Repository struct {
	db     *sql.DB
	logger *zap.Logger
}

// NewRepository создает новый репозиторий

// User представляет модель пользователя в БД
type User struct {
	ID               string
	Email            string
	Username         string
	PasswordHash     string
	Salt             string
	Role             string
	CreatedAt        time.Time
	LastLoginAt      *time.Time
	EmailVerified    bool
	TwoFactorEnabled bool
	DeletedAt        *time.Time
}

// CreateUser создает нового пользователя
func (r *Repository) CreateUser(ctx context.Context, user *User) error {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.accounts (
			id, email, username, password_hash, salt, role,
			created_at, email_verified, two_factor_enabled
		) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
	`

	now := time.Now()
	_, err := r.db.ExecContext(ctx, query,
		user.ID, user.Email, user.Username, user.PasswordHash, user.Salt, user.Role,
		now, user.EmailVerified, user.TwoFactorEnabled,
	)

	if err != nil {
		r.logger.Error("Failed to create user",
			zap.String("user_id", user.ID),
			zap.String("email", user.Email),
			zap.Error(err))
		return fmt.Errorf("failed to create user: %w", err)
	}

	r.logger.Info("User created", zap.String("user_id", user.ID))
	return nil
}

// GetUserByEmail получает пользователя по email
func (r *Repository) GetUserByEmail(ctx context.Context, email string) (*User, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		SELECT id, email, username, password_hash, salt, role,
		       created_at, last_login_at, email_verified, two_factor_enabled, deleted_at
		FROM auth.accounts
		WHERE email = $1 AND deleted_at IS NULL
	`

	var user User
	err := r.db.QueryRowContext(ctx, query, email).Scan(
		&user.ID, &user.Email, &user.Username, &user.PasswordHash, &user.Salt, &user.Role,
		&user.CreatedAt, &user.LastLoginAt, &user.EmailVerified, &user.TwoFactorEnabled, &user.DeletedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("user not found")
		}
		r.logger.Error("Failed to get user by email",
			zap.String("email", email),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	return &user, nil
}

// GetUserByID получает пользователя по ID
func (r *Repository) GetUserByID(ctx context.Context, userID string) (*User, error) {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		SELECT id, email, username, password_hash, salt, role,
		       created_at, last_login_at, email_verified, two_factor_enabled, deleted_at
		FROM auth.accounts
		WHERE id = $1 AND deleted_at IS NULL
	`

	var user User
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&user.ID, &user.Email, &user.Username, &user.PasswordHash, &user.Salt, &user.Role,
		&user.CreatedAt, &user.LastLoginAt, &user.EmailVerified, &user.TwoFactorEnabled, &user.DeletedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("user not found")
		}
		r.logger.Error("Failed to get user by ID",
			zap.String("user_id", userID),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	return &user, nil
}

// UpdateUserLastLogin обновляет время последнего входа
func (r *Repository) UpdateUserLastLogin(ctx context.Context, userID string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `UPDATE auth.accounts SET last_login_at = $1 WHERE id = $2`
	result, err := r.db.ExecContext(ctx, query, time.Now(), userID)

	if err != nil {
		r.logger.Error("Failed to update last login",
			zap.String("user_id", userID),
			zap.Error(err))
		return fmt.Errorf("failed to update last login: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		return fmt.Errorf("user not found")
	}

	return nil
}

// RevokeUserTokens отзывает все активные токены пользователя
func (r *Repository) RevokeUserTokens(ctx context.Context, userID string) error {
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.revoked_tokens (token, user_id, expires_at)
		SELECT rt.token, rt.user_id, rt.expires_at
		FROM auth.refresh_tokens rt
		WHERE rt.user_id = $1 AND rt.expires_at > $2
		ON CONFLICT (token) DO NOTHING
	`

	result, err := r.db.ExecContext(ctx, query, userID, time.Now())
	if err != nil {
		r.logger.Error("Failed to revoke user tokens",
			zap.String("user_id", userID),
			zap.Error(err))
		return fmt.Errorf("failed to revoke user tokens: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	r.logger.Info("User tokens revoked",
		zap.String("user_id", userID),
		zap.Int64("tokens_revoked", rowsAffected))

	return nil
}

// RefreshToken представляет модель refresh токена
type RefreshToken struct {
	Token     string
	UserID    string
	ExpiresAt time.Time
	CreatedAt time.Time
	UpdatedAt *time.Time
}

// StoreRefreshToken сохраняет refresh токен
func (r *Repository) StoreRefreshToken(ctx context.Context, token *RefreshToken) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.refresh_tokens (token, user_id, expires_at, created_at)
		VALUES ($1, $2, $3, $4)
		ON CONFLICT (token) DO UPDATE SET
			expires_at = EXCLUDED.expires_at,
			updated_at = $4
	`

	_, err := r.db.ExecContext(ctx, query,
		token.Token, token.UserID, token.ExpiresAt, token.CreatedAt)

	if err != nil {
		r.logger.Error("Failed to store refresh token",
			zap.String("user_id", token.UserID),
			zap.Error(err))
		return fmt.Errorf("failed to store refresh token: %w", err)
	}

	return nil
}

// ValidateRefreshToken проверяет валидность refresh токена
func (r *Repository) ValidateRefreshToken(ctx context.Context, token string) (*RefreshToken, error) {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		SELECT rt.token, rt.user_id, rt.expires_at, rt.created_at, rt.updated_at
		FROM auth.refresh_tokens rt
		LEFT JOIN auth.revoked_tokens revoked ON rt.token = revoked.token
		WHERE rt.token = $1 AND rt.expires_at > $2 AND revoked.token IS NULL
	`

	var rt RefreshToken
	err := r.db.QueryRowContext(ctx, query, token, time.Now()).Scan(
		&rt.Token, &rt.UserID, &rt.ExpiresAt, &rt.CreatedAt, &rt.UpdatedAt,
	)

	if err != nil {
		if errors.Is(err, sql.ErrNoRows) {
			return nil, fmt.Errorf("token not found or expired")
		}
		r.logger.Error("Failed to validate refresh token",
			zap.String("token", token[:16]+"..."),
			zap.Error(err))
		return nil, fmt.Errorf("failed to validate token: %w", err)
	}

	return &rt, nil
}

// RevokeRefreshToken отзывает конкретный refresh токен
func (r *Repository) RevokeRefreshToken(ctx context.Context, token string) error {
	ctx, cancel := context.WithTimeout(ctx, 2*time.Second)
	defer cancel()

	query := `
		INSERT INTO auth.revoked_tokens (token, user_id, expires_at)
		SELECT token, user_id, expires_at FROM auth.refresh_tokens
		WHERE token = $1
		ON CONFLICT (token) DO NOTHING
	`

	result, err := r.db.ExecContext(ctx, query, token)
	if err != nil {
		r.logger.Error("Failed to revoke refresh token",
			zap.String("token", token[:16]+"..."),
			zap.Error(err))
		return fmt.Errorf("failed to revoke token: %w", err)
	}

	rowsAffected, err := result.RowsAffected()
	if err != nil {
		return fmt.Errorf("failed to get rows affected: %w", err)
	}

	if rowsAffected == 0 {
		r.logger.Warn("Token not found for revocation",
			zap.String("token", token[:16]+"..."))
	}

	return nil
}

// CleanExpiredTokens очищает истекшие токены (для maintenance)
func (r *Repository) CleanExpiredTokens(ctx context.Context) (int64, error) {
	ctx, cancel := context.WithTimeout(ctx, 30*time.Second)
	defer cancel()

	// Очищаем истекшие refresh токены
	query1 := `DELETE FROM auth.refresh_tokens WHERE expires_at < $1`
	result1, err := r.db.ExecContext(ctx, query1, time.Now())
	if err != nil {
		return 0, fmt.Errorf("failed to clean expired refresh tokens: %w", err)
	}

	// Очищаем истекшие revoked токены
	query2 := `DELETE FROM auth.revoked_tokens WHERE expires_at < $1`
	result2, err := r.db.ExecContext(ctx, query2, time.Now())
	if err != nil {
		return 0, fmt.Errorf("failed to clean expired revoked tokens: %w", err)
	}

	rows1, _ := result1.RowsAffected()
	rows2, _ := result2.RowsAffected()
	totalCleaned := rows1 + rows2
	r.logger.Info("Cleaned expired tokens", zap.Int64("tokens_cleaned", totalCleaned))

	return totalCleaned, nil
}

// HealthCheck проверяет здоровье БД
func (r *Repository) HealthCheck(ctx context.Context) error {
	ctx, cancel := context.WithTimeout(ctx, 1*time.Second)
	defer cancel()

	if err := r.db.PingContext(ctx); err != nil {
		r.logger.Error("Database health check failed", zap.Error(err))
		return fmt.Errorf("database health check failed: %w", err)
	}

	return nil
}
