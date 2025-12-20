// Package server Issue: #136
package server

import (
	"context"
	"database/sql"
	"fmt"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
	"go.uber.org/zap"
	"golang.org/x/crypto/bcrypt"

	"necpgame/services/auth-service-go/pkg/api"
)

// Repository предоставляет доступ к данным
type Repository struct {
	db          *sql.DB
	redisClient *redis.Client
	logger      *zap.Logger
}

// NewRepository создает новый репозиторий

// CreateUser создает нового пользователя
func (r *Repository) CreateUser(ctx context.Context, req api.RegisterRequest) (*User, error) {
	// Хэшируем пароль
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		r.logger.Error("Failed to hash password", zap.Error(err))
		return nil, fmt.Errorf("failed to hash password: %w", err)
	}

	userID := uuid.New()
	now := time.Now()

	// Создаем пользователя в БД
	query := `
		INSERT INTO accounts (id, email, username, password_hash, email_verified, created_at, updated_at)
		VALUES ($1, $2, $3, $4, false, $5, $5)
		RETURNING id, email, username, email_verified, created_at
	`

	var user User
	err = r.db.QueryRowContext(ctx, query, userID, req.Email, req.Username, string(hashedPassword), now).Scan(
		&user.ID, &user.Email, &user.Username, &user.EmailVerified, &user.CreatedAt,
	)
	if err != nil {
		r.logger.Error("Failed to create user", zap.Error(err), zap.String("email", req.Email))
		return nil, fmt.Errorf("failed to create user: %w", err)
	}

	r.logger.Info("User created", zap.String("user_id", userID.String()), zap.String("email", req.Email))
	return &user, nil
}

// GetUserByEmail получает пользователя по email
func (r *Repository) GetUserByEmail(ctx context.Context, email string) (*User, error) {
	query := `
		SELECT id, email, username, password_hash, email_verified, created_at, last_login_at
		FROM accounts
		WHERE email = $1
	`

	var user User
	var passwordHash string
	err := r.db.QueryRowContext(ctx, query, email).Scan(
		&user.ID, &user.Email, &user.Username, &passwordHash, &user.EmailVerified, &user.CreatedAt, &user.LastLoginAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrUserNotFound
		}
		r.logger.Error("Failed to get user by email", zap.Error(err), zap.String("email", email))
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	user.PasswordHash = passwordHash
	return &user, nil
}

// GetUserByID получает пользователя по ID
func (r *Repository) GetUserByID(ctx context.Context, userID uuid.UUID) (*User, error) {
	query := `
		SELECT id, email, username, email_verified, created_at, last_login_at
		FROM accounts
		WHERE id = $1
	`

	var user User
	err := r.db.QueryRowContext(ctx, query, userID).Scan(
		&user.ID, &user.Email, &user.Username, &user.EmailVerified, &user.CreatedAt, &user.LastLoginAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrUserNotFound
		}
		r.logger.Error("Failed to get user by ID", zap.Error(err), zap.String("user_id", userID.String()))
		return nil, fmt.Errorf("failed to get user: %w", err)
	}

	return &user, nil
}

// UpdateUserLastLogin обновляет время последнего входа
func (r *Repository) UpdateUserLastLogin(ctx context.Context, userID uuid.UUID) error {
	query := `UPDATE accounts SET last_login_at = $1, updated_at = $1 WHERE id = $2`

	_, err := r.db.ExecContext(ctx, query, time.Now(), userID)
	if err != nil {
		r.logger.Error("Failed to update last login", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to update last login: %w", err)
	}

	return nil
}

// CreateEmailVerificationToken создает токен верификации email
func (r *Repository) CreateEmailVerificationToken(ctx context.Context, userID uuid.UUID, token string) error {
	expiresAt := time.Now().Add(24 * time.Hour) // 24 часа

	query := `
		INSERT INTO email_verification_tokens (user_id, token, expires_at, created_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.ExecContext(ctx, query, userID, token, expiresAt, time.Now())
	if err != nil {
		r.logger.Error("Failed to create email verification token", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to create email verification token: %w", err)
	}

	return nil
}

// VerifyEmailToken проверяет токен верификации email
func (r *Repository) VerifyEmailToken(ctx context.Context, token string) (*uuid.UUID, error) {
	query := `
		UPDATE email_verification_tokens
		SET used_at = $1
		WHERE token = $2 AND expires_at > $1 AND used_at IS NULL
		RETURNING user_id
	`

	var userID uuid.UUID
	err := r.db.QueryRowContext(ctx, query, time.Now(), token).Scan(&userID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrInvalidToken
		}
		r.logger.Error("Failed to verify email token", zap.Error(err))
		return nil, fmt.Errorf("failed to verify email token: %w", err)
	}

	return &userID, nil
}

// UpdateEmailVerified обновляет статус верификации email
func (r *Repository) UpdateEmailVerified(ctx context.Context, userID uuid.UUID) error {
	query := `UPDATE accounts SET email_verified = true, updated_at = $1 WHERE id = $2`

	_, err := r.db.ExecContext(ctx, query, time.Now(), userID)
	if err != nil {
		r.logger.Error("Failed to update email verified", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to update email verified: %w", err)
	}

	return nil
}

// CreatePasswordResetToken создает токен сброса пароля
func (r *Repository) CreatePasswordResetToken(ctx context.Context, userID uuid.UUID, token string) error {
	expiresAt := time.Now().Add(1 * time.Hour) // 1 час

	query := `
		INSERT INTO password_reset_tokens (user_id, token, expires_at, created_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.ExecContext(ctx, query, userID, token, expiresAt, time.Now())
	if err != nil {
		r.logger.Error("Failed to create password reset token", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to create password reset token: %w", err)
	}

	return nil
}

// VerifyPasswordResetToken проверяет токен сброса пароля
func (r *Repository) VerifyPasswordResetToken(ctx context.Context, token string) (*uuid.UUID, error) {
	query := `
		UPDATE password_reset_tokens
		SET used_at = $1
		WHERE token = $2 AND expires_at > $1 AND used_at IS NULL
		RETURNING user_id
	`

	var userID uuid.UUID
	err := r.db.QueryRowContext(ctx, query, time.Now(), token).Scan(&userID)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrInvalidToken
		}
		r.logger.Error("Failed to verify password reset token", zap.Error(err))
		return nil, fmt.Errorf("failed to verify password reset token: %w", err)
	}

	return &userID, nil
}

// UpdatePassword обновляет пароль пользователя
func (r *Repository) UpdatePassword(ctx context.Context, userID uuid.UUID, hashedPassword string) error {
	query := `UPDATE accounts SET password_hash = $1, updated_at = $2 WHERE id = $3`

	_, err := r.db.ExecContext(ctx, query, hashedPassword, time.Now(), userID)
	if err != nil {
		r.logger.Error("Failed to update password", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to update password: %w", err)
	}

	return nil
}

// GetUserRoles получает роли пользователя
func (r *Repository) GetUserRoles(ctx context.Context, userID uuid.UUID) ([]Role, error) {
	query := `
		SELECT r.id, r.name, r.description
		FROM roles r
		JOIN account_roles ar ON r.id = ar.role_id
		WHERE ar.account_id = $1
	`

	rows, err := r.db.QueryContext(ctx, query, userID)
	if err != nil {
		r.logger.Error("Failed to get user roles", zap.Error(err), zap.String("user_id", userID.String()))
		return nil, fmt.Errorf("failed to get user roles: %w", err)
	}
	defer rows.Close()

	var roles []Role
	for rows.Next() {
		var role Role
		err := rows.Scan(&role.ID, &role.Name, &role.Description)
		if err != nil {
			r.logger.Error("Failed to scan role", zap.Error(err))
			return nil, fmt.Errorf("failed to scan role: %w", err)
		}
		roles = append(roles, role)
	}

	return roles, nil
}

// CreateLoginHistory записывает историю входа
func (r *Repository) CreateLoginHistory(ctx context.Context, userID uuid.UUID, ipAddress, userAgent string) error {
	query := `
		INSERT INTO login_history (user_id, ip_address, user_agent, login_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err := r.db.ExecContext(ctx, query, userID, ipAddress, userAgent, time.Now())
	if err != nil {
		r.logger.Error("Failed to create login history", zap.Error(err), zap.String("user_id", userID.String()))
		return fmt.Errorf("failed to create login history: %w", err)
	}

	return nil
}

// StoreSession сохраняет сессию в Redis
func (r *Repository) StoreSession(ctx context.Context, sessionID string, userID uuid.UUID, expiresAt time.Duration) error {
	key := fmt.Sprintf("session:%s", sessionID)
	value := userID.String()

	err := r.redisClient.Set(ctx, key, value, expiresAt).Err()
	if err != nil {
		r.logger.Error("Failed to store session", zap.Error(err), zap.String("session_id", sessionID))
		return fmt.Errorf("failed to store session: %w", err)
	}

	return nil
}

// GetSession получает сессию из Redis
func (r *Repository) GetSession(ctx context.Context, sessionID string) (*uuid.UUID, error) {
	key := fmt.Sprintf("session:%s", sessionID)

	value, err := r.redisClient.Get(ctx, key).Result()
	if err != nil {
		if err == redis.Nil {
			return nil, ErrSessionNotFound
		}
		r.logger.Error("Failed to get session", zap.Error(err), zap.String("session_id", sessionID))
		return nil, fmt.Errorf("failed to get session: %w", err)
	}

	userID, err := uuid.Parse(value)
	if err != nil {
		r.logger.Error("Invalid session value", zap.Error(err), zap.String("value", value))
		return nil, fmt.Errorf("invalid session value: %w", err)
	}

	return &userID, nil
}

// DeleteSession удаляет сессию из Redis
func (r *Repository) DeleteSession(ctx context.Context, sessionID string) error {
	key := fmt.Sprintf("session:%s", sessionID)

	err := r.redisClient.Del(ctx, key).Err()
	if err != nil {
		r.logger.Error("Failed to delete session", zap.Error(err), zap.String("session_id", sessionID))
		return fmt.Errorf("failed to delete session: %w", err)
	}

	return nil
}

// GetUserByOAuthID получает пользователя по OAuth ID провайдера
func (r *Repository) GetUserByOAuthID(ctx context.Context, provider, oauthID string) (*User, error) {
	query := `
		SELECT a.id, a.email, a.username, a.password_hash, a.email_verified, a.created_at, a.last_login_at
		FROM accounts a
		JOIN oauth_accounts oa ON a.id = oa.user_id
		WHERE oa.provider = $1 AND oa.oauth_id = $2
	`

	var user User
	var passwordHash string
	err := r.db.QueryRowContext(ctx, query, provider, oauthID).Scan(
		&user.ID, &user.Email, &user.Username, &passwordHash, &user.EmailVerified, &user.CreatedAt, &user.LastLoginAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrUserNotFound
		}
		r.logger.Error("Failed to get user by OAuth ID", zap.Error(err))
		return nil, fmt.Errorf("failed to get user by OAuth ID: %w", err)
	}

	user.PasswordHash = passwordHash
	return &user, nil
}

// CreateOAuthUser создает нового пользователя через OAuth
func (r *Repository) CreateOAuthUser(ctx context.Context, user *User, provider, oauthID string) (*User, error) {
	// Начинаем транзакцию
	tx, err := r.db.BeginTx(ctx, nil)
	if err != nil {
		r.logger.Error("Failed to begin transaction", zap.Error(err))
		return nil, fmt.Errorf("failed to begin transaction: %w", err)
	}
	defer tx.Rollback()

	// Создаем пользователя
	userQuery := `
		INSERT INTO accounts (id, email, username, email_verified, created_at, updated_at)
		VALUES ($1, $2, $3, $4, $5, $5)
		RETURNING id, email, username, email_verified, created_at
	`

	var createdUser User
	err = tx.QueryRowContext(ctx, userQuery, user.ID, user.Email, user.Username, user.EmailVerified, user.CreatedAt).Scan(
		&createdUser.ID, &createdUser.Email, &createdUser.Username, &createdUser.EmailVerified, &createdUser.CreatedAt,
	)
	if err != nil {
		r.logger.Error("Failed to create OAuth user", zap.Error(err))
		return nil, fmt.Errorf("failed to create OAuth user: %w", err)
	}

	// Создаем OAuth аккаунт
	oauthQuery := `
		INSERT INTO oauth_accounts (user_id, provider, oauth_id, created_at)
		VALUES ($1, $2, $3, $4)
	`

	_, err = tx.ExecContext(ctx, oauthQuery, createdUser.ID, provider, oauthID, user.CreatedAt)
	if err != nil {
		r.logger.Error("Failed to create OAuth account", zap.Error(err))
		return nil, fmt.Errorf("failed to create OAuth account: %w", err)
	}

	// Коммитим транзакцию
	if err = tx.Commit(); err != nil {
		r.logger.Error("Failed to commit transaction", zap.Error(err))
		return nil, fmt.Errorf("failed to commit transaction: %w", err)
	}

	r.logger.Info("Created OAuth user", zap.String("user_id", createdUser.ID.String()))
	return &createdUser, nil
}

// LinkOAuthAccount связывает OAuth аккаунт с существующим пользователем
func (r *Repository) LinkOAuthAccount(ctx context.Context, userID uuid.UUID, provider, oauthID string) error {
	query := `
		INSERT INTO oauth_accounts (user_id, provider, oauth_id, created_at)
		VALUES ($1, $2, $3, $4)
		ON CONFLICT (provider, oauth_id) DO NOTHING
	`

	_, err := r.db.ExecContext(ctx, query, userID, provider, oauthID, time.Now())
	if err != nil {
		r.logger.Error("Failed to link OAuth account", zap.Error(err))
		return fmt.Errorf("failed to link OAuth account: %w", err)
	}

	r.logger.Info("Linked OAuth account", zap.String("user_id", userID.String()), zap.String("provider", provider))
	return nil
}

// User представляет пользователя в системе
type User struct {
	ID            uuid.UUID
	Email         string
	Username      string
	PasswordHash  string
	EmailVerified bool
	CreatedAt     time.Time
	LastLoginAt   *time.Time
}

// Role представляет роль пользователя
type Role struct {
	ID          uuid.UUID
	Name        string
	Description string
}

// Стандартные ошибки
var (
	ErrUserNotFound    = fmt.Errorf("user not found")
	ErrInvalidToken    = fmt.Errorf("invalid or expired token")
	ErrSessionNotFound = fmt.Errorf("session not found")
)
