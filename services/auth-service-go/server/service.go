// Issue: #136 - Auth service interface and constructor
// Implementation split across multiple files for better maintainability:
// - service_auth.go: Authentication operations (login, register, logout)
// - service_tokens.go: Token management operations
// - service_users.go: User management operations
// - service_roles.go: Role and permission operations
// - service_oauth.go: OAuth authentication operations
package server

import (
	"context"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"github.com/NECPGAME/auth-service-go/pkg/api"
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
	UserId uuid.UUID `json:",omitempty"`
	Roles  []api.Role
}

// ServiceInterface определяет интерфейс сервиса аутентификации
type ServiceInterface interface {
	// Authentication operations
	Register(ctx context.Context, req *api.RegisterRequest) (*RegisterResponse, error)
	Login(ctx context.Context, req api.LoginRequest, ipAddress, userAgent string) (*LoginResponse, error)
	Logout(ctx context.Context, sessionID string) error

	// Token operations
	RefreshToken(ctx context.Context, req api.RefreshTokenRequest) (*RefreshTokenResponse, error)
	VerifyEmail(ctx context.Context, req api.VerifyEmailRequest) (*api.SuccessResponse, error)
	ResendVerification(ctx context.Context, req api.ResendVerificationRequest) (*api.SuccessResponse, error)

	// Password operations
	ForgotPassword(ctx context.Context, req api.ForgotPasswordRequest) (*api.SuccessResponse, error)
	ResetPassword(ctx context.Context, req api.ResetPasswordRequest) (*api.SuccessResponse, error)
	ChangePassword(ctx context.Context, userID uuid.UUID, req api.ChangePasswordRequest) (*api.SuccessResponse, error)

	// User operations
	GetCurrentUser(ctx context.Context, userID uuid.UUID) (*api.UserInfo, error)
	GetUserRoles(ctx context.Context, userID uuid.UUID) (*UserRolesResponse, error)

	// OAuth operations
	OAuthLogin(ctx context.Context, provider OAuthProvider) (string, string, error)
	OAuthCallback(ctx context.Context, provider OAuthProvider, code, state string) (*LoginResponse, error)
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
