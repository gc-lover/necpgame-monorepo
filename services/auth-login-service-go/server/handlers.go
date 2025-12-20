// Package server Issue: #1
package server

import (
	"context"
	"crypto/subtle"
	"database/sql"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
	"golang.org/x/crypto/argon2"

	"necpgame/services/auth-login-service-go/pkg/api"
)

// authHandler реализует Handler интерфейс для аутентификации
type authHandler struct {
	logger     *zap.Logger
	db         *sql.DB
	jwtSecret  []byte
	tokenTTL   time.Duration
	refreshTTL time.Duration
}

// newAuthHandler создает новый обработчик аутентификации с оптимизациями
func newAuthHandler(logger *zap.Logger, db *sql.DB, jwtSecret string) *authHandler {
	return &authHandler{
		logger:     logger,
		db:         db,
		jwtSecret:  []byte(jwtSecret),
		tokenTTL:   15 * time.Minute,   // короткий токен для безопасности
		refreshTTL: 7 * 24 * time.Hour, // длинный refresh токен
	}
}

// Login реализует вход в систему с оптимизациями производительности
func (h *authHandler) Login(ctx context.Context, req *api.LoginRequest) (api.LoginRes, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	h.logger.Info("Login attempt", zap.String("email", req.Email))

	// Получаем пользователя из БД с оптимизированным запросом
	user, err := h.getUserByEmail(ctx, req.Email)
	if err != nil {
		h.logger.Warn("User not found", zap.String("email", req.Email), zap.Error(err))
		return &api.LoginBadRequest{}, nil // Не раскрываем детали для безопасности
	}

	// Проверяем пароль с постоянным временем (constant-time comparison)
	if !h.verifyPassword(req.Password, user.PasswordHash, user.Salt) {
		h.logger.Warn("Invalid password", zap.String("email", req.Email))
		return &api.LoginBadRequest{}, nil
	}

	// Проверяем 2FA если включено
	if user.TwoFactorEnabled && !req.TwoFactorCode.IsSet() {
		return &api.LoginUnauthorized{}, nil
	}

	// Генерируем токены
	accessToken, refreshToken, err := h.generateTokens(user.ID, user.Role)
	if err != nil {
		h.logger.Error("Failed to generate tokens", zap.Error(err))
		return &api.LoginInternalServerError{}, nil
	}

	// Обновляем время последнего входа
	if err := h.updateLastLogin(ctx, user.ID); err != nil {
		h.logger.Warn("Failed to update last login", zap.Error(err))
		// Не возвращаем ошибку, так как аутентификация прошла успешно
	}

	response := &api.LoginResponse{
		AccessToken:  api.NewOptString(accessToken),
		RefreshToken: api.NewOptString(refreshToken),
		ExpiresIn:    api.NewOptInt(int(h.tokenTTL.Seconds())),
		Account: api.NewOptAccount(api.Account{
			ID:               api.NewOptUUID(uuid.MustParse(user.ID)),
			Email:            api.NewOptString(user.Email),
			Username:         api.NewOptString(user.Username),
			Role:             api.NewOptAccountRole(api.AccountRole(user.Role)),
			CreatedAt:        api.NewOptDateTime(user.CreatedAt),
			EmailVerified:    api.NewOptBool(user.EmailVerified),
			TwoFactorEnabled: api.NewOptBool(user.TwoFactorEnabled),
		}),
	}

	h.logger.Info("Login successful", zap.String("user_id", user.ID))
	return response, nil
}

// Logout реализует выход из системы
func (h *authHandler) Logout(ctx context.Context, req api.OptLogoutReq) (api.LogoutRes, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 3*time.Second)
	defer cancel()

	// Получаем user ID из контекста (из middleware)
	userID := getUserIDFromContext(ctx)
	if userID == "" {
		return &api.LogoutUnauthorized{}, nil
	}

	// Инвалидируем refresh токен если он предоставлен
	if req.IsSet() && req.Value.RefreshToken.IsSet() {
		token, _ := req.Value.RefreshToken.Get()
		if err := h.invalidateRefreshToken(ctx, token); err != nil {
			h.logger.Warn("Failed to invalidate refresh token", zap.Error(err))
		}
	}

	h.logger.Info("Logout successful", zap.String("user_id", userID))
	return &api.LogoutOK{Success: api.NewOptBool(true)}, nil
}

// RefreshToken реализует обновление токена доступа
func (h *authHandler) RefreshToken(ctx context.Context, req *api.RefreshTokenRequest) (api.RefreshTokenRes, error) {
	// Context timeout для предотвращения зависаний
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	h.logger.Info("Token refresh attempt")

	// Валидируем refresh токен
	claims, err := h.validateRefreshToken(req.RefreshToken)
	if err != nil {
		h.logger.Warn("Invalid refresh token", zap.Error(err))
		return &api.RefreshTokenUnauthorized{}, nil
	}

	// Проверяем что токен не отозван
	if err := h.checkTokenNotRevoked(ctx, req.RefreshToken); err != nil {
		h.logger.Warn("Refresh token revoked", zap.Error(err))
		return &api.RefreshTokenUnauthorized{}, nil
	}

	// Генерируем новые токены
	userID := claims["user_id"].(string)
	role := claims["role"].(string)

	accessToken, refreshToken, err := h.generateTokens(userID, role)
	if err != nil {
		h.logger.Error("Failed to generate tokens", zap.Error(err))
		return &api.RefreshTokenInternalServerError{}, nil
	}

	// Отзываем старый refresh токен
	if err := h.invalidateRefreshToken(ctx, req.RefreshToken); err != nil {
		h.logger.Warn("Failed to revoke old refresh token", zap.Error(err))
	}

	response := &api.TokenResponse{
		AccessToken:  api.NewOptString(accessToken),
		RefreshToken: api.NewOptString(refreshToken),
		ExpiresIn:    api.NewOptInt(int(h.tokenTTL.Seconds())),
	}

	h.logger.Info("Token refresh successful", zap.String("user_id", userID))
	return response, nil
}

// Вспомогательные методы для работы с БД и токенами

type user struct {
	ID               string
	Email            string
	Username         string
	PasswordHash     string
	Salt             string
	Role             string
	CreatedAt        time.Time
	EmailVerified    bool
	TwoFactorEnabled bool
}

// getUserByEmail получает пользователя по email с оптимизированным запросом
func (h *authHandler) getUserByEmail(ctx context.Context, email string) (*user, error) {
	query := `
		SELECT id, email, username, password_hash, salt, role, created_at, email_verified, two_factor_enabled
		FROM auth.accounts
		WHERE email = $1 AND deleted_at IS NULL
	`

	var u user
	err := h.db.QueryRowContext(ctx, query, email).Scan(
		&u.ID, &u.Email, &u.Username, &u.PasswordHash, &u.Salt, &u.Role,
		&u.CreatedAt, &u.EmailVerified, &u.TwoFactorEnabled,
	)

	if err != nil {
		return nil, err
	}

	return &u, nil
}

// verifyPassword проверяет пароль с Argon2 и constant-time comparison
func (h *authHandler) verifyPassword(password, hash, salt string) bool {
	saltBytes, err := base64.StdEncoding.DecodeString(salt)
	if err != nil {
		return false
	}

	hashBytes, err := base64.StdEncoding.DecodeString(hash)
	if err != nil {
		return false
	}

	// Argon2 параметры (должны совпадать с регистрацией)
	computedHash := argon2.IDKey([]byte(password), saltBytes, 1, 64*1024, 4, 32)

	// Constant-time comparison для предотвращения timing attacks
	return subtle.ConstantTimeCompare(hashBytes, computedHash) == 1
}

// generateTokens генерирует пару access/refresh токенов
func (h *authHandler) generateTokens(userID, role string) (accessToken, refreshToken string, err error) {
	now := time.Now()

	// Access token
	accessClaims := jwt.MapClaims{
		"user_id": userID,
		"role":    role,
		"type":    "access",
		"iat":     now.Unix(),
		"exp":     now.Add(h.tokenTTL).Unix(),
	}

	accessToken, err = jwt.NewWithClaims(jwt.SigningMethodHS256, accessClaims).SignedString(h.jwtSecret)
	if err != nil {
		return "", "", fmt.Errorf("failed to sign access token: %w", err)
	}

	// Refresh token
	refreshClaims := jwt.MapClaims{
		"user_id": userID,
		"role":    role,
		"type":    "refresh",
		"iat":     now.Unix(),
		"exp":     now.Add(h.refreshTTL).Unix(),
	}

	refreshToken, err = jwt.NewWithClaims(jwt.SigningMethodHS256, refreshClaims).SignedString(h.jwtSecret)
	if err != nil {
		return "", "", fmt.Errorf("failed to sign refresh token: %w", err)
	}

	return accessToken, refreshToken, nil
}

// updateLastLogin обновляет время последнего входа
func (h *authHandler) updateLastLogin(ctx context.Context, userID string) error {
	query := `UPDATE auth.accounts SET last_login_at = $1 WHERE id = $2`
	_, err := h.db.ExecContext(ctx, query, time.Now(), userID)
	return err
}

// validateRefreshToken валидирует refresh токен
func (h *authHandler) validateRefreshToken(tokenString string) (jwt.MapClaims, error) {
	token, err := jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return h.jwtSecret, nil
	})

	if err != nil {
		return nil, err
	}

	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		if claims["type"] != "refresh" {
			return nil, fmt.Errorf("not a refresh token")
		}
		return claims, nil
	}

	return nil, fmt.Errorf("invalid token")
}

// checkTokenNotRevoked проверяет что токен не отозван
func (h *authHandler) checkTokenNotRevoked(ctx context.Context, token string) error {
	query := `SELECT 1 FROM auth.revoked_tokens WHERE token = $1 AND expires_at > $2`
	var exists int
	err := h.db.QueryRowContext(ctx, query, token, time.Now()).Scan(&exists)
	if err == sql.ErrNoRows {
		return nil // Токен не отозван
	}
	if err != nil {
		return err
	}
	return fmt.Errorf("token revoked")
}

// invalidateRefreshToken отзывает refresh токен
func (h *authHandler) invalidateRefreshToken(ctx context.Context, token string) error {
	// Извлекаем expiration из токена
	claims, err := h.validateRefreshToken(token)
	if err != nil {
		return err
	}

	expiresAt := time.Unix(int64(claims["exp"].(float64)), 0)
	query := `INSERT INTO auth.revoked_tokens (token, expires_at) VALUES ($1, $2)`
	_, err = h.db.ExecContext(ctx, query, token, expiresAt)
	return err
}

// HTTP Handlers для Chi роутера

// LoginHandler обрабатывает HTTP запросы на вход
func (h *authHandler) LoginHandler(w http.ResponseWriter, r *http.Request) {
	// Парсим JSON тело запроса
	var req api.LoginRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Warn("Failed to decode login request", zap.Error(err))
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	// Вызываем бизнес-логику
	response, err := h.Login(r.Context(), &req)
	if err != nil {
		h.logger.Error("Login failed", zap.Error(err))
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Отправляем ответ
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(response); err != nil {
		h.logger.Error("Failed to encode response", zap.Error(err))
	}
}

// LogoutHandler обрабатывает HTTP запросы на выход
func (h *authHandler) LogoutHandler(w http.ResponseWriter, r *http.Request) {
	// Парсим JSON тело запроса
	var req api.OptLogoutReq
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Warn("Failed to decode logout request", zap.Error(err))
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	// Вызываем бизнес-логику
	response, err := h.Logout(r.Context(), req)
	if err != nil {
		h.logger.Error("Logout failed", zap.Error(err))
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Отправляем ответ
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(response); err != nil {
		h.logger.Error("Failed to encode response", zap.Error(err))
	}
}

// RefreshTokenHandler обрабатывает HTTP запросы на обновление токена
func (h *authHandler) RefreshTokenHandler(w http.ResponseWriter, r *http.Request) {
	// Парсим JSON тело запроса
	var req api.RefreshTokenRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.logger.Warn("Failed to decode refresh token request", zap.Error(err))
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	// Вызываем бизнес-логику
	response, err := h.RefreshToken(r.Context(), &req)
	if err != nil {
		h.logger.Error("Refresh token failed", zap.Error(err))
		w.WriteHeader(http.StatusInternalServerError)
		return
	}

	// Отправляем ответ
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	if err := json.NewEncoder(w).Encode(response); err != nil {
		h.logger.Error("Failed to encode response", zap.Error(err))
	}
}

// getUserIDFromContext извлекает user ID из контекста (должен быть установлен middleware)
func getUserIDFromContext(ctx context.Context) string {
	if userID, ok := ctx.Value("user_id").(string); ok {
		return userID
	}
	return ""
}
