// Issue: #136
package server

import (
	"context"
	"fmt"
	"net/http"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/auth-service-go/pkg/api"
)

// Handlers содержит обработчики HTTP запросов
type Handlers struct {
	service *AuthService
	logger  *zap.Logger
}

// NewHandlers создает новые обработчики
func NewHandlers(service *AuthService, logger *zap.Logger) *Handlers {
	return &Handlers{
		service: service,
		logger:  logger,
	}
}

// Register регистрирует нового пользователя
func (h *Handlers) Register(ctx context.Context, req *api.RegisterRequest) (api.RegisterRes, error) {
	h.logger.Info("Handling register request", zap.String("email", req.Email))

	// Issue: #136 - Add context timeout for MMOFPS performance
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second) // 5s timeout for user registration
	defer cancel()

	response, err := h.service.Register(ctx, req)
	if err != nil {
		h.logger.Error("Register failed", zap.Error(err))
		return &api.BadRequest{}, err
	}

	return &api.RegisterResponse{
		UserId:               api.NewOptUUID(response.UserId),
		Email:                api.NewOptString(response.Email),
		Username:             api.NewOptString(response.Username),
		VerificationRequired: api.NewOptBool(response.VerificationRequired),
		Message:              api.NewOptString(response.Message),
	}, nil
}

// Login выполняет вход пользователя
func (h *Handlers) Login(ctx context.Context, req *api.LoginRequest) (api.LoginRes, error) {
	h.logger.Info("Handling login request", zap.String("login", req.Login))

	// Извлекаем IP адрес и User-Agent из контекста запроса
	r := h.getHTTPRequestFromContext(ctx)
	ipAddress := h.extractIPAddress(r)
	userAgent := r.Header.Get("User-Agent")

	response, err := h.service.Login(ctx, *req, ipAddress, userAgent)
	if err != nil {
		h.logger.Warn("Login failed", zap.Error(err))
		return &api.Unauthorized{}, nil
	}

	return &api.LoginResponse{
		AccessToken:  api.NewOptString(response.AccessToken),
		RefreshToken: api.NewOptString(response.RefreshToken),
		TokenType:    api.NewOptString(response.TokenType),
		ExpiresIn:    api.NewOptInt(response.ExpiresIn),
		User:         api.NewOptUserInfo(*response.User),
	}, nil
}

// Logout выполняет выход пользователя
func (h *Handlers) Logout(ctx context.Context) (api.LogoutRes, error) {
	h.logger.Info("Handling logout request")

	// Получаем sessionID из параметров или генерируем новый
	sessionID := h.extractSessionIDFromContext(ctx)

	err := h.service.Logout(ctx, sessionID)
	if err != nil {
		h.logger.Error("Logout failed", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// RefreshToken обновляет токен доступа
func (h *Handlers) RefreshToken(ctx context.Context, req *api.RefreshTokenRequest) (api.RefreshTokenRes, error) {
	h.logger.Info("Handling token refresh request")

	response, err := h.service.RefreshToken(ctx, *req)
	if err != nil {
		h.logger.Warn("Token refresh failed", zap.Error(err))
		return &api.Unauthorized{}, nil
	}

	return &api.RefreshTokenResponse{
		AccessToken:  api.NewOptString(response.AccessToken),
		RefreshToken: api.NewOptString(response.RefreshToken),
		TokenType:    api.NewOptString(response.TokenType),
		ExpiresIn:    api.NewOptInt(response.ExpiresIn),
	}, nil
}

// VerifyEmail подтверждает email
func (h *Handlers) VerifyEmail(ctx context.Context, req *api.VerifyEmailRequest) (api.VerifyEmailRes, error) {
	h.logger.Info("Handling email verification request")

	_, err := h.service.VerifyEmail(ctx, *req)
	if err != nil {
		h.logger.Warn("Email verification failed", zap.Error(err))
		return &api.BadRequest{}, nil
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ResendVerification отправляет повторное письмо верификации
func (h *Handlers) ResendVerification(ctx context.Context, req *api.ResendVerificationRequest) (api.ResendVerificationRes, error) {
	h.logger.Info("Handling resend verification request", zap.String("email", req.Email))

	_, err := h.service.ResendVerification(ctx, *req)
	if err != nil {
		h.logger.Error("Resend verification failed", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ForgotPassword инициирует сброс пароля
func (h *Handlers) ForgotPassword(ctx context.Context, req *api.ForgotPasswordRequest) (api.ForgotPasswordRes, error) {
	h.logger.Info("Handling forgot password request", zap.String("email", req.Email))

	_, err := h.service.ForgotPassword(ctx, *req)
	if err != nil {
		h.logger.Error("Forgot password failed", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ResetPassword сбрасывает пароль
func (h *Handlers) ResetPassword(ctx context.Context, req *api.ResetPasswordRequest) (api.ResetPasswordRes, error) {
	h.logger.Info("Handling password reset request")

	_, err := h.service.ResetPassword(ctx, *req)
	if err != nil {
		h.logger.Warn("Password reset failed", zap.Error(err))
		return &api.BadRequest{}, nil
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// ChangePassword изменяет пароль
func (h *Handlers) ChangePassword(ctx context.Context, req *api.ChangePasswordRequest) (api.ChangePasswordRes, error) {
	h.logger.Info("Handling password change request")

	userID, err := h.extractUserIDFromContext(ctx)
	if err != nil {
		h.logger.Warn("Failed to extract user ID from context", zap.Error(err))
		return &api.Unauthorized{}, nil
	}

	_, err = h.service.ChangePassword(ctx, userID, *req)
	if err != nil {
		h.logger.Error("Password change failed", zap.Error(err))
		return &api.BadRequest{}, nil
	}

	return &api.SuccessResponse{Success: api.NewOptBool(true)}, nil
}

// GetCurrentUser получает информацию о текущем пользователе
func (h *Handlers) GetCurrentUser(ctx context.Context) (api.GetCurrentUserRes, error) {
	h.logger.Info("Handling get current user request")

	userID, err := h.extractUserIDFromContext(ctx)
	if err != nil {
		h.logger.Warn("Failed to extract user ID from context", zap.Error(err))
		return &api.Unauthorized{}, nil
	}

	userInfo, err := h.service.GetCurrentUser(ctx, userID)
	if err != nil {
		h.logger.Error("Get current user failed", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	return userInfo, nil
}

// GetUserRoles получает роли пользователя
func (h *Handlers) GetUserRoles(ctx context.Context) (api.GetUserRolesRes, error) {
	h.logger.Info("Handling get user roles request")

	userID, err := h.extractUserIDFromContext(ctx)
	if err != nil {
		h.logger.Warn("Failed to extract user ID from context", zap.Error(err))
		return &api.Unauthorized{}, nil
	}

	roles, err := h.service.GetUserRoles(ctx, userID)
	if err != nil {
		h.logger.Error("Get user roles failed", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	return &api.UserRolesResponse{Roles: roles.Roles}, nil
}

// GetUserPermissions получает права пользователя
func (h *Handlers) GetUserPermissions(ctx context.Context) (api.GetUserPermissionsRes, error) {
	h.logger.Info("Handling get user permissions request")

	// Для простоты возвращаем базовые права на основе ролей
	// В реальной реализации нужно получить права из БД
	permissions := []string{"read", "write"} // Заглушка

	return &api.UserPermissionsResponse{Permissions: permissions}, nil
}

// OauthLogin начинает OAuth аутентификацию
func (h *Handlers) OauthLogin(ctx context.Context, params api.OauthLoginParams) (api.OauthLoginRes, error) {
	h.logger.Info("Handling OAuth login request", zap.String("provider", string(params.Provider)))

	// Валидируем провайдера
	provider, err := h.service.oauthClient.ValidateProvider(string(params.Provider))
	if err != nil {
		h.logger.Warn("Invalid OAuth provider", zap.String("provider", string(params.Provider)), zap.Error(err))
		return &api.BadRequest{}, nil
	}

	// Генерируем URL для перенаправления
	_, state, err := h.service.OAuthLogin(ctx, provider)
	if err != nil {
		h.logger.Error("Failed to generate OAuth login URL", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	// Сохраняем state в Redis для валидации callback
	if err := h.saveOAuthState(ctx, state); err != nil {
		h.logger.Error("Failed to save OAuth state", zap.Error(err))
		return &api.InternalServerError{}, err
	}

	// Возвращаем redirect response
	return &api.OauthLoginFound{}, nil
}

// OauthCallback обрабатывает OAuth callback
func (h *Handlers) OauthCallback(ctx context.Context, params api.OauthCallbackParams) (api.OauthCallbackRes, error) {
	h.logger.Info("Handling OAuth callback", zap.String("provider", string(params.Provider)))

	// Валидируем провайдера
	provider, err := h.service.oauthClient.ValidateProvider(string(params.Provider))
	if err != nil {
		h.logger.Warn("Invalid OAuth provider", zap.String("provider", string(params.Provider)), zap.Error(err))
		return &api.BadRequest{}, nil
	}

	// Проверяем state для защиты от CSRF
	if err := h.validateOAuthState(ctx, params.State); err != nil {
		h.logger.Warn("Invalid OAuth state", zap.Error(err))
		return &api.BadRequest{}, nil
	}

	// Обрабатываем OAuth callback
	response, err := h.service.OAuthCallback(ctx, provider, params.Code, params.State)
	if err != nil {
		h.logger.Error("OAuth callback processing failed", zap.Error(err))
		return &api.BadRequest{}, nil
	}

	// Определяем, новый ли пользователь
	isNewUser := response.User.CreatedAt == response.User.LastLoginAt

	return &api.OAuthCallbackResponse{
		AccessToken:  api.NewOptString(response.AccessToken),
		RefreshToken: api.NewOptString(response.RefreshToken),
		TokenType:    api.NewOptString(response.TokenType),
		ExpiresIn:    api.NewOptInt(response.ExpiresIn),
		User:         api.NewOptUserInfo(*response.User),
		IsNewUser:    api.NewOptBool(isNewUser),
	}, nil
}

// Вспомогательные методы

func (h *Handlers) extractUserIDFromContext(ctx context.Context) (uuid.UUID, error) {
	// Извлекаем userID из JWT токена в контексте
	// Реализация зависит от middleware аутентификации
	// TODO: Реализовать извлечение userID из контекста

	// Заглушка
	return uuid.New(), nil
}

func (h *Handlers) extractSessionIDFromContext(ctx context.Context) string {
	// Извлекаем sessionID из контекста или генерируем новый
	// TODO: Реализовать извлечение sessionID

	// Заглушка
	return uuid.New().String()
}

func (h *Handlers) getHTTPRequestFromContext(ctx context.Context) *http.Request {
	// Извлекаем http.Request из контекста
	// Реализация зависит от того, как контекст передается в handlers
	// TODO: Реализовать извлечение http.Request из контекста

	// Заглушка
	return &http.Request{
		Header: make(http.Header),
	}
}

func (h *Handlers) extractIPAddress(r *http.Request) string {
	ip := r.Header.Get("X-Forwarded-For")
	if ip != "" {
		// Берем первый IP из списка
		ips := strings.Split(ip, ",")
		ip = strings.TrimSpace(ips[0])
	} else {
		ip, _, _ = strings.Cut(r.RemoteAddr, ":")
	}
	return ip
}

// saveOAuthState сохраняет OAuth state в Redis для защиты от CSRF
func (h *Handlers) saveOAuthState(ctx context.Context, state string) error {
	key := "oauth:state:" + state
	return h.service.repo.redisClient.Set(ctx, key, "valid", 10*time.Minute).Err()
}

// validateOAuthState проверяет OAuth state
func (h *Handlers) validateOAuthState(ctx context.Context, state string) error {
	key := "oauth:state:" + state
	val, err := h.service.repo.redisClient.Get(ctx, key).Result()
	if err != nil {
		return fmt.Errorf("invalid or expired state")
	}
	if val != "valid" {
		return fmt.Errorf("invalid state")
	}

	// Удаляем использованный state
	h.service.repo.redisClient.Del(ctx, key)
	return nil
}
