// Issue: #136
package server

import (
	"context"
	"crypto/rand"
	"database/sql"
	"encoding/hex"
	"fmt"
	"net"
	"net/http"
	"strings"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"github.com/google/uuid"
	"go.uber.org/zap"
	"golang.org/x/crypto/bcrypt"

	"github.com/NECPGAME/auth-service-go/pkg/api"
	_ "github.com/jackc/pgx/v5/stdlib"
)

// RegisterResponse представляет ответ на регистрацию
type RegisterResponse struct {
	UserId               uuid.UUID
	Email                string
	Username             string
	VerificationRequired bool
	Message              string
}

// LoginResponse представляет ответ на вход
type LoginResponse struct {
	AccessToken  string
	RefreshToken string
	TokenType    string
	ExpiresIn    int
	User         *api.UserInfo
}

// RefreshTokenResponse представляет ответ на обновление токена
type RefreshTokenResponse struct {
	AccessToken  string
	RefreshToken string
	TokenType    string
	ExpiresIn    int
}

// UserRolesResponse представляет ответ со списком ролей
type UserRolesResponse struct {
	Roles []api.Role
}

// Service содержит бизнес-логику auth-service
type Service struct {
	repo        *Repository
	config      *Config
	logger      *zap.Logger
	jwtSecret   []byte
	eventBus    EventBus
	oauthClient *OAuthClient
}

// NewService создает новый сервис
func NewService(repo *Repository, config *Config, logger *zap.Logger, eventBus EventBus) *Service {
	oauthClient := NewOAuthClient(&config.OAuthConfig, logger)

	return &Service{
		repo:        repo,
		config:      config,
		logger:      logger,
		jwtSecret:   []byte(config.JWTSecret),
		eventBus:    eventBus,
		oauthClient: oauthClient,
	}
}

// Register регистрирует нового пользователя
func (s *Service) Register(ctx context.Context, req *api.RegisterRequest) (*RegisterResponse, error) {
	s.logger.Info("Registering new user", zap.String("email", req.Email), zap.String("username", req.Username))

	// Валидация входных данных
	if err := s.validateRegisterRequest(*req); err != nil {
		s.logger.Warn("Invalid register request", zap.Error(err))
		return nil, fmt.Errorf("validation failed: %w", err)
	}

	// Создаем пользователя
	user, err := s.repo.CreateUser(ctx, *req)
	if err != nil {
		s.logger.Error("Failed to create user", zap.Error(err))
		return nil, fmt.Errorf("failed to create user: %w", err)
	}

	// Генерируем токен верификации email
	token := s.generateSecureToken()
	if err := s.repo.CreateEmailVerificationToken(ctx, user.ID, token); err != nil {
		s.logger.Error("Failed to create email verification token", zap.Error(err))
		// Не возвращаем ошибку, пользователь создан, но верификация не сработает
	}

	response := &RegisterResponse{
		UserId:               user.ID,
		Email:                user.Email,
		Username:             user.Username,
		VerificationRequired: true,
		Message:              "User registered successfully. Please verify your email.",
	}

	// Публикуем событие создания аккаунта
	if err := s.eventBus.PublishEvent(ctx, string(EventAccountCreated), map[string]interface{}{
		"userId":    user.ID.String(),
		"email":     user.Email,
		"username":  user.Username,
		"createdAt": user.CreatedAt,
	}); err != nil {
		s.logger.Error("Failed to publish account created event", zap.Error(err))
		// Не возвращаем ошибку, регистрация успешна
	}

	s.logger.Info("User registered successfully", zap.String("user_id", user.ID.String()))
	return response, nil
}

// Login выполняет вход пользователя
func (s *Service) Login(ctx context.Context, req api.LoginRequest, ipAddress, userAgent string) (*LoginResponse, error) {
	s.logger.Info("User login attempt", zap.String("login", req.Login))

	// Получаем пользователя по email или username
	user, err := s.repo.GetUserByEmail(ctx, req.Login)
	if err != nil {
		if err == ErrUserNotFound {
			// Пробуем найти по username
			user, err = s.findUserByUsername(ctx, req.Login)
			if err != nil {
				s.logger.Warn("Login failed: user not found", zap.String("login", req.Login))
				return nil, fmt.Errorf("invalid credentials")
			}
		} else {
			s.logger.Error("Failed to get user", zap.Error(err))
			return nil, fmt.Errorf("authentication failed")
		}
	}

	// Проверяем пароль
	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(req.Password)); err != nil {
		s.logger.Warn("Login failed: invalid password", zap.String("user_id", user.ID.String()))
		return nil, fmt.Errorf("invalid credentials")
	}

	// Проверяем верификацию email
	if !user.EmailVerified {
		s.logger.Warn("Login failed: email not verified", zap.String("user_id", user.ID.String()))
		return nil, fmt.Errorf("email not verified")
	}

	// Генерируем JWT токены
	accessToken, refreshToken, err := s.generateTokens(user.ID)
	if err != nil {
		s.logger.Error("Failed to generate tokens", zap.Error(err))
		return nil, fmt.Errorf("failed to generate tokens")
	}

	// Обновляем время последнего входа
	if err := s.repo.UpdateUserLastLogin(ctx, user.ID); err != nil {
		s.logger.Error("Failed to update last login", zap.Error(err))
		// Не критичная ошибка, продолжаем
	}

	// Записываем историю входа
	if err := s.repo.CreateLoginHistory(ctx, user.ID, ipAddress, userAgent); err != nil {
		s.logger.Error("Failed to create login history", zap.Error(err))
		// Не критичная ошибка, продолжаем
	}

	response := &LoginResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600, // 1 час
		User: &api.UserInfo{
			ID:            api.NewOptUUID(user.ID),
			Email:         api.NewOptString(user.Email),
			Username:      api.NewOptString(user.Username),
			EmailVerified: api.NewOptBool(user.EmailVerified),
			CreatedAt:     api.NewOptDateTime(user.CreatedAt),
			LastLoginAt:   api.NewOptDateTime(*user.LastLoginAt),
		},
	}

	// Публикуем событие успешного входа
	if err := s.eventBus.PublishEvent(ctx, string(EventLoginSuccess), map[string]interface{}{
		"userId":    user.ID.String(),
		"email":     user.Email,
		"username":  user.Username,
		"ipAddress": ipAddress,
		"userAgent": userAgent,
		"loginAt":   time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish login success event", zap.Error(err))
		// Не возвращаем ошибку, вход успешен
	}

	s.logger.Info("User logged in successfully", zap.String("user_id", user.ID.String()))
	return response, nil
}

// Logout выполняет выход пользователя
func (s *Service) Logout(ctx context.Context, sessionID string) error {
	s.logger.Info("User logout", zap.String("session_id", sessionID))

	if err := s.repo.DeleteSession(ctx, sessionID); err != nil {
		s.logger.Error("Failed to delete session", zap.Error(err))
		return fmt.Errorf("failed to logout")
	}

	// Публикуем событие выхода (если есть userID, можно получить из сессии)
	// Для простоты публикуем событие с sessionID
	if err := s.eventBus.PublishEvent(ctx, string(EventLogout), map[string]interface{}{
		"sessionId": sessionID,
		"logoutAt":  time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish logout event", zap.Error(err))
		// Не возвращаем ошибку, выход успешен
	}

	s.logger.Info("User logged out successfully", zap.String("session_id", sessionID))
	return nil
}

// RefreshToken обновляет токен доступа
func (s *Service) RefreshToken(ctx context.Context, req api.RefreshTokenRequest) (*RefreshTokenResponse, error) {
	s.logger.Info("Token refresh attempt")

	// Парсим refresh токен
	token, err := jwt.Parse(req.RefreshToken, func(token *jwt.Token) (interface{}, error) {
		if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", token.Header["alg"])
		}
		return s.jwtSecret, nil
	})

	if err != nil || !token.Valid {
		s.logger.Warn("Invalid refresh token")
		return nil, fmt.Errorf("invalid refresh token")
	}

	// Получаем userID из claims
	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		s.logger.Warn("Invalid token claims")
		return nil, fmt.Errorf("invalid token claims")
	}

	userIDStr, ok := claims["user_id"].(string)
	if !ok {
		s.logger.Warn("Missing user_id in token")
		return nil, fmt.Errorf("invalid token")
	}

	userID, err := uuid.Parse(userIDStr)
	if err != nil {
		s.logger.Warn("Invalid user_id format in token")
		return nil, fmt.Errorf("invalid token")
	}

	// Генерируем новые токены
	accessToken, refreshToken, err := s.generateTokens(userID)
	if err != nil {
		s.logger.Error("Failed to generate new tokens", zap.Error(err))
		return nil, fmt.Errorf("failed to generate tokens")
	}

	response := &RefreshTokenResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600,
	}

	s.logger.Info("Tokens refreshed successfully", zap.String("user_id", userID.String()))
	return response, nil
}

// VerifyEmail подтверждает email
func (s *Service) VerifyEmail(ctx context.Context, req api.VerifyEmailRequest) (*api.SuccessResponse, error) {
	s.logger.Info("Email verification attempt", zap.String("token", req.Token[:8]+"..."))

	userID, err := s.repo.VerifyEmailToken(ctx, req.Token)
	if err != nil {
		s.logger.Warn("Email verification failed", zap.Error(err))
		return nil, fmt.Errorf("invalid verification token")
	}

	if err := s.repo.UpdateEmailVerified(ctx, *userID); err != nil {
		s.logger.Error("Failed to update email verified", zap.Error(err))
		return nil, fmt.Errorf("failed to verify email")
	}

	// Публикуем событие верификации email
	if err := s.eventBus.PublishEvent(ctx, string(EventEmailVerified), map[string]interface{}{
		"userId":     userID.String(),
		"verifiedAt": time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish email verified event", zap.Error(err))
		// Не возвращаем ошибку, email верифицирован
	}

	s.logger.Info("Email verified successfully", zap.String("user_id", userID.String()))
	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ResendVerification отправляет повторное письмо верификации
func (s *Service) ResendVerification(ctx context.Context, req api.ResendVerificationRequest) (*api.SuccessResponse, error) {
	s.logger.Info("Resend verification email", zap.String("email", req.Email))

	user, err := s.repo.GetUserByEmail(ctx, req.Email)
	if err != nil {
		if err == ErrUserNotFound {
			s.logger.Warn("User not found for verification resend", zap.String("email", req.Email))
			return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil // Не раскрываем существование пользователя
		}
		s.logger.Error("Failed to get user for verification", zap.Error(err))
		return nil, fmt.Errorf("failed to resend verification")
	}

	if user.EmailVerified {
		s.logger.Info("Email already verified", zap.String("user_id", user.ID.String()))
		return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
	}

	// Генерируем новый токен
	token := s.generateSecureToken()
	if err := s.repo.CreateEmailVerificationToken(ctx, user.ID, token); err != nil {
		s.logger.Error("Failed to create new verification token", zap.Error(err))
		return nil, fmt.Errorf("failed to resend verification")
	}

	// TODO: Отправить email с токеном

	s.logger.Info("Verification email resent", zap.String("user_id", user.ID.String()))
	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ForgotPassword инициирует сброс пароля
func (s *Service) ForgotPassword(ctx context.Context, req api.ForgotPasswordRequest) (*api.SuccessResponse, error) {
	s.logger.Info("Password reset requested", zap.String("email", req.Email))

	user, err := s.repo.GetUserByEmail(ctx, req.Email)
	if err != nil {
		if err == ErrUserNotFound {
			s.logger.Warn("User not found for password reset", zap.String("email", req.Email))
			return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil // Не раскрываем существование пользователя
		}
		s.logger.Error("Failed to get user for password reset", zap.Error(err))
		return nil, fmt.Errorf("failed to process password reset")
	}

	// Генерируем токен сброса
	token := s.generateSecureToken()
	if err := s.repo.CreatePasswordResetToken(ctx, user.ID, token); err != nil {
		s.logger.Error("Failed to create password reset token", zap.Error(err))
		return nil, fmt.Errorf("failed to process password reset")
	}

	// TODO: Отправить email с токеном

	s.logger.Info("Password reset token created", zap.String("user_id", user.ID.String()))
	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ResetPassword сбрасывает пароль
func (s *Service) ResetPassword(ctx context.Context, req api.ResetPasswordRequest) (*api.SuccessResponse, error) {
	s.logger.Info("Password reset attempt")

	userID, err := s.repo.VerifyPasswordResetToken(ctx, req.Token)
	if err != nil {
		s.logger.Warn("Password reset failed", zap.Error(err))
		return nil, fmt.Errorf("invalid reset token")
	}

	// Валидируем новый пароль
	if err := s.validatePassword(req.NewPassword); err != nil {
		s.logger.Warn("Invalid new password", zap.Error(err))
		return nil, fmt.Errorf("invalid password: %w", err)
	}

	// Хэшируем пароль
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.NewPassword), bcrypt.DefaultCost)
	if err != nil {
		s.logger.Error("Failed to hash password", zap.Error(err))
		return nil, fmt.Errorf("failed to process password reset")
	}

	// Обновляем пароль
	if err := s.repo.UpdatePassword(ctx, *userID, string(hashedPassword)); err != nil {
		s.logger.Error("Failed to update password", zap.Error(err))
		return nil, fmt.Errorf("failed to reset password")
	}

	// Публикуем событие сброса пароля
	if err := s.eventBus.PublishEvent(ctx, string(EventPasswordReset), map[string]interface{}{
		"userId":  userID.String(),
		"resetAt": time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish password reset event", zap.Error(err))
		// Не возвращаем ошибку, пароль сброшен
	}

	s.logger.Info("Password reset successfully", zap.String("user_id", userID.String()))
	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ChangePassword изменяет пароль
func (s *Service) ChangePassword(ctx context.Context, userID uuid.UUID, req api.ChangePasswordRequest) (*api.SuccessResponse, error) {
	s.logger.Info("Password change attempt", zap.String("user_id", userID.String()))

	user, err := s.repo.GetUserByID(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get user for password change", zap.Error(err))
		return nil, fmt.Errorf("failed to change password")
	}

	// Проверяем текущий пароль
	if err := bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(req.CurrentPassword)); err != nil {
		s.logger.Warn("Password change failed: invalid current password", zap.String("user_id", userID.String()))
		return nil, fmt.Errorf("invalid current password")
	}

	// Валидируем новый пароль
	if err := s.validatePassword(req.NewPassword); err != nil {
		s.logger.Warn("Invalid new password", zap.Error(err))
		return nil, fmt.Errorf("invalid password: %w", err)
	}

	// Хэшируем новый пароль
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.NewPassword), bcrypt.DefaultCost)
	if err != nil {
		s.logger.Error("Failed to hash new password", zap.Error(err))
		return nil, fmt.Errorf("failed to change password")
	}

	// Обновляем пароль
	if err := s.repo.UpdatePassword(ctx, userID, string(hashedPassword)); err != nil {
		s.logger.Error("Failed to update password", zap.Error(err))
		return nil, fmt.Errorf("failed to change password")
	}

	// Публикуем событие изменения пароля
	if err := s.eventBus.PublishEvent(ctx, string(EventPasswordChanged), map[string]interface{}{
		"userId":    userID.String(),
		"changedAt": time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish password changed event", zap.Error(err))
		// Не возвращаем ошибку, пароль изменен
	}

	s.logger.Info("Password changed successfully", zap.String("user_id", userID.String()))
	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// GetCurrentUser получает информацию о текущем пользователе
func (s *Service) GetCurrentUser(ctx context.Context, userID uuid.UUID) (*api.UserInfo, error) {
	s.logger.Info("Getting current user info", zap.String("user_id", userID.String()))

	user, err := s.repo.GetUserByID(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get current user", zap.Error(err))
		return nil, fmt.Errorf("failed to get user info")
	}

	userInfo := &api.UserInfo{
		ID:            api.NewOptUUID(user.ID),
		Email:         api.NewOptString(user.Email),
		Username:      api.NewOptString(user.Username),
		EmailVerified: api.NewOptBool(user.EmailVerified),
		CreatedAt:     api.NewOptDateTime(user.CreatedAt),
	}

	if user.LastLoginAt != nil {
		userInfo.LastLoginAt = api.NewOptDateTime(*user.LastLoginAt)
	}

	return userInfo, nil
}

// GetUserRoles получает роли пользователя
func (s *Service) GetUserRoles(ctx context.Context, userID uuid.UUID) (*UserRolesResponse, error) {
	s.logger.Info("Getting user roles", zap.String("user_id", userID.String()))

	roles, err := s.repo.GetUserRoles(ctx, userID)
	if err != nil {
		s.logger.Error("Failed to get user roles", zap.Error(err))
		return nil, fmt.Errorf("failed to get user roles")
	}

	var apiRoles []api.Role
	for _, role := range roles {
		apiRoles = append(apiRoles, api.Role{
			ID:          api.NewOptUUID(role.ID),
			Name:        api.NewOptString(role.Name),
			Description: api.NewOptString(role.Description),
		})
	}

	return &UserRolesResponse{Roles: apiRoles}, nil
}

// OAuthLogin начинает OAuth аутентификацию
func (s *Service) OAuthLogin(ctx context.Context, provider OAuthProvider) (string, string, error) {
	s.logger.Info("Starting OAuth login", zap.String("provider", string(provider)))

	// Генерируем state для защиты от CSRF
	state, err := s.oauthClient.GenerateState()
	if err != nil {
		s.logger.Error("Failed to generate OAuth state", zap.Error(err))
		return "", "", fmt.Errorf("failed to generate state")
	}

	// Получаем URL для перенаправления
	authURL, err := s.oauthClient.GetAuthURL(provider, state)
	if err != nil {
		s.logger.Error("Failed to get OAuth auth URL", zap.Error(err))
		return "", "", fmt.Errorf("failed to get auth URL")
	}

	s.logger.Info("OAuth login URL generated", zap.String("provider", string(provider)))
	return authURL, state, nil
}

// OAuthCallback обрабатывает OAuth callback
func (s *Service) OAuthCallback(ctx context.Context, provider OAuthProvider, code, state string) (*LoginResponse, error) {
	s.logger.Info("Processing OAuth callback", zap.String("provider", string(provider)))

	// Обмениваем код на токен
	token, err := s.oauthClient.ExchangeCode(ctx, provider, code, state)
	if err != nil {
		s.logger.Error("Failed to exchange OAuth code", zap.Error(err))
		return nil, fmt.Errorf("failed to exchange code")
	}

	// Получаем информацию о пользователе
	userInfo, err := s.oauthClient.GetUserInfo(ctx, provider, token)
	if err != nil {
		s.logger.Error("Failed to get OAuth user info", zap.Error(err))
		return nil, fmt.Errorf("failed to get user info")
	}

	// Ищем существующего пользователя по OAuth ID
	user, err := s.repo.GetUserByOAuthID(ctx, string(provider), userInfo.ID)
	if err != nil && err != ErrUserNotFound {
		s.logger.Error("Failed to get user by OAuth ID", zap.Error(err))
		return nil, fmt.Errorf("failed to get user")
	}

	isNewUser := false
	if err == ErrUserNotFound {
		// Проверяем существует ли пользователь с таким email
		existingUser, err := s.repo.GetUserByEmail(ctx, userInfo.Email)
		if err != nil && err != ErrUserNotFound {
			s.logger.Error("Failed to check existing user by email", zap.Error(err))
			return nil, fmt.Errorf("failed to check existing user")
		}

		if err == ErrUserNotFound {
			// Создаем нового пользователя
			user, err = s.createOAuthUser(ctx, provider, userInfo)
			if err != nil {
				s.logger.Error("Failed to create OAuth user", zap.Error(err))
				return nil, fmt.Errorf("failed to create user")
			}
			isNewUser = true
			s.logger.Info("Created new OAuth user", zap.String("user_id", user.ID.String()))
		} else {
			// Связываем OAuth аккаунт с существующим пользователем
			err = s.repo.LinkOAuthAccount(ctx, existingUser.ID, string(provider), userInfo.ID)
			if err != nil {
				s.logger.Error("Failed to link OAuth account", zap.Error(err))
				return nil, fmt.Errorf("failed to link OAuth account")
			}
			user = existingUser
			s.logger.Info("Linked OAuth account to existing user", zap.String("user_id", user.ID.String()))
		}
	}

	// Проверяем верификацию email
	if !user.EmailVerified && userInfo.EmailVerified {
		if err := s.repo.UpdateEmailVerified(ctx, user.ID); err != nil {
			s.logger.Error("Failed to update email verified", zap.Error(err))
			// Не критичная ошибка, продолжаем
		}
	}

	// Генерируем JWT токены
	accessToken, refreshToken, err := s.generateTokens(user.ID)
	if err != nil {
		s.logger.Error("Failed to generate tokens", zap.Error(err))
		return nil, fmt.Errorf("failed to generate tokens")
	}

	// Обновляем время последнего входа
	if err := s.repo.UpdateUserLastLogin(ctx, user.ID); err != nil {
		s.logger.Error("Failed to update last login", zap.Error(err))
		// Не критичная ошибка, продолжаем
	}

	apiUserInfo := &api.UserInfo{
		ID:            api.NewOptUUID(user.ID),
		Email:         api.NewOptString(user.Email),
		Username:      api.NewOptString(user.Username),
		EmailVerified: api.NewOptBool(user.EmailVerified),
		CreatedAt:     api.NewOptDateTime(user.CreatedAt),
	}

	if user.LastLoginAt != nil {
		apiUserInfo.LastLoginAt = api.NewOptDateTime(*user.LastLoginAt)
	}

	response := &LoginResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600,
		User:         apiUserInfo,
	}

	// Публикуем событие OAuth логина
	if err := s.eventBus.PublishEvent(ctx, string(EventOAuthLogin), map[string]interface{}{
		"userId":    user.ID.String(),
		"provider":  string(provider),
		"isNewUser": isNewUser,
		"loginAt":   time.Now(),
	}); err != nil {
		s.logger.Error("Failed to publish OAuth login event", zap.Error(err))
		// Не возвращаем ошибку, вход успешен
	}

	s.logger.Info("OAuth login successful", zap.String("user_id", user.ID.String()), zap.String("provider", string(provider)))
	return response, nil
}

// createOAuthUser создает нового пользователя через OAuth
func (s *Service) createOAuthUser(ctx context.Context, provider OAuthProvider, userInfo *OAuthUserInfo) (*User, error) {
	// Проверяем доступность username
	username := userInfo.Username
	if err := s.ensureUniqueUsername(ctx, &username); err != nil {
		return nil, fmt.Errorf("failed to ensure unique username: %w", err)
	}

	// Создаем пользователя
	user := &User{
		ID:            uuid.New(),
		Email:         userInfo.Email,
		Username:      username,
		EmailVerified: userInfo.EmailVerified,
		CreatedAt:     time.Now(),
	}

	// Создаем пользователя в БД
	createdUser, err := s.repo.CreateOAuthUser(ctx, user, string(provider), userInfo.ID)
	if err != nil {
		return nil, fmt.Errorf("failed to create OAuth user: %w", err)
	}

	return createdUser, nil
}

// ensureUniqueUsername гарантирует уникальность username
func (s *Service) ensureUniqueUsername(ctx context.Context, username *string) error {
	originalUsername := *username
	counter := 1

	for {
		// Проверяем существует ли пользователь с таким username
		_, err := s.findUserByUsername(ctx, *username)
		if err == sql.ErrNoRows {
			// Username свободен
			return nil
		}
		if err != nil {
			return fmt.Errorf("failed to check username uniqueness: %w", err)
		}

		// Username занят, добавляем суффикс
		*username = fmt.Sprintf("%s%d", originalUsername, counter)
		counter++

		// Защита от бесконечного цикла
		if counter > 100 {
			return fmt.Errorf("failed to generate unique username")
		}
	}
}

// Вспомогательные методы

func (s *Service) validateRegisterRequest(req api.RegisterRequest) error {
	if strings.TrimSpace(req.Email) == "" {
		return fmt.Errorf("email is required")
	}
	if len(req.Password) < 8 {
		return fmt.Errorf("password must be at least 8 characters")
	}
	if strings.TrimSpace(req.Username) == "" {
		return fmt.Errorf("username is required")
	}
	if len(req.Username) < 3 || len(req.Username) > 50 {
		return fmt.Errorf("username must be between 3 and 50 characters")
	}
	return nil
}

func (s *Service) validatePassword(password string) error {
	if len(password) < 8 {
		return fmt.Errorf("password must be at least 8 characters")
	}
	return nil
}

func (s *Service) findUserByUsername(ctx context.Context, username string) (*User, error) {
	// Реализация поиска по username
	query := `
		SELECT id, email, username, password_hash, email_verified, created_at, last_login_at
		FROM accounts
		WHERE username = $1
	`

	var user User
	var passwordHash string
	err := s.repo.db.QueryRowContext(ctx, query, username).Scan(
		&user.ID, &user.Email, &user.Username, &passwordHash, &user.EmailVerified, &user.CreatedAt, &user.LastLoginAt,
	)
	if err != nil {
		if err == sql.ErrNoRows {
			return nil, ErrUserNotFound
		}
		return nil, fmt.Errorf("failed to get user by username: %w", err)
	}

	user.PasswordHash = passwordHash
	return &user, nil
}

func (s *Service) generateTokens(userID uuid.UUID) (string, string, error) {
	// Генерируем access token (1 час)
	accessClaims := jwt.MapClaims{
		"user_id": userID.String(),
		"type":    "access",
		"exp":     time.Now().Add(time.Hour).Unix(),
		"iat":     time.Now().Unix(),
	}

	accessToken := jwt.NewWithClaims(jwt.SigningMethodHS256, accessClaims)
	accessTokenString, err := accessToken.SignedString(s.jwtSecret)
	if err != nil {
		return "", "", fmt.Errorf("failed to sign access token: %w", err)
	}

	// Генерируем refresh token (7 дней)
	refreshClaims := jwt.MapClaims{
		"user_id": userID.String(),
		"type":    "refresh",
		"exp":     time.Now().Add(7 * 24 * time.Hour).Unix(),
		"iat":     time.Now().Unix(),
	}

	refreshToken := jwt.NewWithClaims(jwt.SigningMethodHS256, refreshClaims)
	refreshTokenString, err := refreshToken.SignedString(s.jwtSecret)
	if err != nil {
		return "", "", fmt.Errorf("failed to sign refresh token: %w", err)
	}

	return accessTokenString, refreshTokenString, nil
}

func (s *Service) generateSecureToken() string {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		s.logger.Error("Failed to generate secure token", zap.Error(err))
		// Fallback to less secure method
		return fmt.Sprintf("%d", time.Now().UnixNano())
	}
	return hex.EncodeToString(bytes)
}

func (s *Service) extractIPAddress(r *http.Request) string {
	ip := r.Header.Get("X-Forwarded-For")
	if ip != "" {
		// Берем первый IP из списка
		ips := strings.Split(ip, ",")
		ip = strings.TrimSpace(ips[0])
	} else {
		ip = r.RemoteAddr
	}

	// Убираем порт
	host, _, err := net.SplitHostPort(ip)
	if err != nil {
		return ip
	}
	return host
}
