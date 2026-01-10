package service

import (
	"context"
	"crypto/rand"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"net/url"
	"strings"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"

	"necpgame/services/auth-service-go/config"
	"necpgame/services/auth-service-go/internal/repository"
)

// OAuthService handles OAuth authentication flows
type OAuthService struct {
	config     *config.Config
	logger     *zap.Logger
	repo       *repository.Repository
	jwtService *JWTService
	stateStore map[string]string // Simple in-memory state store (use Redis in production)
}

// OAuthProvider represents an OAuth provider
type OAuthProvider interface {
	GetAuthURL(state string) string
	ExchangeCode(ctx context.Context, code string) (*OAuthUserInfo, error)
}

// OAuthUserInfo represents user information from OAuth provider
type OAuthUserInfo struct {
	ID            string
	Email         string
	Name          string
	AvatarURL     string
	Provider      string
	RawProfile    map[string]interface{}
}

// NewOAuthService creates a new OAuth service
func NewOAuthService(cfg *config.Config, logger *zap.Logger, repo *repository.Repository, jwtSvc *JWTService) *OAuthService {
	return &OAuthService{
		config:     cfg,
		logger:     logger,
		repo:       repo,
		jwtService: jwtSvc,
		stateStore: make(map[string]string),
	}
}

// GenerateState generates a secure random state parameter for CSRF protection
func (s *OAuthService) GenerateState() (string, error) {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		return "", fmt.Errorf("failed to generate random state: %w", err)
	}
	state := base64.URLEncoding.EncodeToString(bytes)

	// Store state for validation (in production, use Redis with TTL)
	s.stateStore[state] = state

	return state, nil
}

// ValidateState validates the state parameter
func (s *OAuthService) ValidateState(state string) bool {
	storedState, exists := s.stateStore[state]
	if !exists {
		return false
	}

	// Remove state after use (one-time use)
	delete(s.stateStore, state)

	return storedState == state
}

// GetOAuthRedirectURL generates OAuth redirect URL for the specified provider
func (s *OAuthService) GetOAuthRedirectURL(providerName, customRedirectURI, state string) (string, error) {
	provider, exists := s.config.OAuth.Providers[providerName]
	if !exists {
		return "", fmt.Errorf("unsupported OAuth provider: %s", providerName)
	}

	// Use custom redirect URI if provided, otherwise use configured one
	redirectURI := provider.RedirectURL
	if customRedirectURI != "" {
		// Validate that custom redirect URI is allowed (basic validation)
		if !strings.HasPrefix(customRedirectURI, s.config.OAuth.BaseURL) {
			return "", fmt.Errorf("invalid redirect URI: %s", customRedirectURI)
		}
		redirectURI = customRedirectURI
	}

	// Build authorization URL
	params := url.Values{}
	params.Add("client_id", provider.ClientID)
	params.Add("redirect_uri", redirectURI)
	params.Add("response_type", "code")
	params.Add("scope", strings.Join(provider.Scopes, " "))
	params.Add("state", state)
	params.Add("access_type", "offline") // Request refresh token for Google

	authURL := provider.AuthURL + "?" + params.Encode()

	s.logger.Info("Generated OAuth redirect URL",
		zap.String("provider", providerName),
		zap.String("redirect_uri", redirectURI),
		zap.String("state", state))

	return authURL, nil
}

// HandleOAuthCallback processes OAuth callback and completes authentication
func (s *OAuthService) HandleOAuthCallback(ctx context.Context, providerName, code, state string) (*repository.User, *repository.Session, bool, error) {
	// Validate state parameter
	if !s.ValidateState(state) {
		return nil, nil, fmt.Errorf("invalid or expired state parameter")
	}

	provider, exists := s.config.OAuth.Providers[providerName]
	if !exists {
		return nil, nil, fmt.Errorf("unsupported OAuth provider: %s", providerName)
	}

	// Exchange authorization code for access token
	userInfo, err := s.exchangeCodeForUserInfo(ctx, provider, code)
	if err != nil {
		return nil, nil, fmt.Errorf("failed to exchange code: %w", err)
	}

	// Find or create user
	user, isNewUser, err := s.findOrCreateUser(ctx, userInfo)
	if err != nil {
		return nil, nil, false, fmt.Errorf("failed to find/create user: %w", err)
	}

	// Create session
	session, err := s.createUserSession(ctx, user.ID)
	if err != nil {
		return nil, nil, false, fmt.Errorf("failed to create session: %w", err)
	}

	s.logger.Info("OAuth authentication completed",
		zap.String("provider", providerName),
		zap.String("user_id", user.ID.String()),
		zap.String("email", user.Email))

	return user, session, nil
}

// exchangeCodeForUserInfo exchanges OAuth code for user information
func (s *OAuthService) exchangeCodeForUserInfo(ctx context.Context, provider config.OAuthProviderConfig, code string) (*OAuthUserInfo, error) {
	// Exchange code for access token
	tokenData := url.Values{}
	tokenData.Set("client_id", provider.ClientID)
	tokenData.Set("client_secret", provider.ClientSecret)
	tokenData.Set("code", code)
	tokenData.Set("grant_type", "authorization_code")
	tokenData.Set("redirect_uri", provider.RedirectURL)

	req, err := http.NewRequestWithContext(ctx, "POST", provider.TokenURL, strings.NewReader(tokenData.Encode()))
	if err != nil {
		return nil, fmt.Errorf("failed to create token request: %w", err)
	}
	req.Header.Set("Content-Type", "application/x-www-form-urlencoded")

	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to exchange code: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		body, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("token exchange failed: %s", string(body))
	}

	var tokenResp struct {
		AccessToken string `json:"access_token"`
		TokenType   string `json:"token_type"`
	}
	if err := json.NewDecoder(resp.Body).Decode(&tokenResp); err != nil {
		return nil, fmt.Errorf("failed to decode token response: %w", err)
	}

	// Get user info using access token
	return s.getUserInfo(ctx, provider, tokenResp.AccessToken)
}

// getUserInfo retrieves user information from OAuth provider
func (s *OAuthService) getUserInfo(ctx context.Context, provider config.OAuthProviderConfig, accessToken string) (*OAuthUserInfo, error) {
	req, err := http.NewRequestWithContext(ctx, "GET", provider.UserInfoURL, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create user info request: %w", err)
	}
	req.Header.Set("Authorization", "Bearer "+accessToken)

	client := &http.Client{Timeout: 10 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		return nil, fmt.Errorf("failed to get user info: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		body, _ := io.ReadAll(resp.Body)
		return nil, fmt.Errorf("user info request failed: %s", string(body))
	}

	var profile map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&profile); err != nil {
		return nil, fmt.Errorf("failed to decode user info: %w", err)
	}

	// Extract user information based on provider
	userInfo := &OAuthUserInfo{
		Provider:   provider.ClientID, // We'll use provider name instead
		RawProfile: profile,
	}

	// Handle different provider response formats
	switch {
	case strings.Contains(provider.UserInfoURL, "google"):
		if id, ok := profile["id"].(string); ok {
			userInfo.ID = id
		}
		if email, ok := profile["email"].(string); ok {
			userInfo.Email = email
		}
		if name, ok := profile["name"].(string); ok {
			userInfo.Name = name
		}
		if picture, ok := profile["picture"].(string); ok {
			userInfo.AvatarURL = picture
		}
	case strings.Contains(provider.UserInfoURL, "github"):
		if id, ok := profile["id"].(float64); ok {
			userInfo.ID = fmt.Sprintf("%.0f", id)
		}
		if email, ok := profile["email"].(string); ok {
			userInfo.Email = email
		}
		if name, ok := profile["name"].(string); ok {
			userInfo.Name = name
		}
		if avatar, ok := profile["avatar_url"].(string); ok {
			userInfo.AvatarURL = avatar
		}
	}

	return userInfo, nil
}

// findOrCreateUser finds existing user or creates new one from OAuth data
func (s *OAuthService) findOrCreateUser(ctx context.Context, userInfo *OAuthUserInfo) (*repository.User, bool, error) {
	// Try to find existing user by OAuth provider ID
	user, err := s.repo.GetUserByOAuthID(ctx, userInfo.Provider, userInfo.ID)
	if err == nil && user != nil {
		// Update user info if needed
		if user.Name != userInfo.Name || user.AvatarURL != userInfo.AvatarURL {
			user.Name = userInfo.Name
			user.AvatarURL = userInfo.AvatarURL
			if err := s.repo.UpdateUser(ctx, user); err != nil {
				s.logger.Warn("Failed to update user info", zap.Error(err))
			}
		}
		return user, false, nil // Existing user
	}

	// Try to find by email
	user, err = s.repo.GetUserByEmail(ctx, userInfo.Email)
	if err == nil && user != nil {
		// Link OAuth account to existing user
		if err := s.repo.LinkOAuthAccount(ctx, user.ID, userInfo.Provider, userInfo.ID, userInfo.RawProfile); err != nil {
			s.logger.Warn("Failed to link OAuth account", zap.Error(err))
		}
		return user, false, nil // Existing user
	}

	// Create new user
	userID := uuid.New()
	username := s.generateUsername(userInfo.Name, userInfo.Email)

	newUser := &repository.User{
		ID:        userID.String(),
		Email:     userInfo.Email,
		Username:  username,
		Status:    "active",
		CreatedAt: time.Now().Format(time.RFC3339),
		UpdatedAt: time.Now().Format(time.RFC3339),
	}

	if err := s.repo.CreateUser(ctx, newUser); err != nil {
		return nil, false, fmt.Errorf("failed to create user: %w", err)
	}

	// Link OAuth account
	if err := s.repo.LinkOAuthAccount(ctx, userID, userInfo.Provider, userInfo.ID, userInfo.RawProfile); err != nil {
		s.logger.Warn("Failed to link OAuth account for new user", zap.Error(err))
	}

	s.logger.Info("Created new user from OAuth",
		zap.String("user_id", userID.String()),
		zap.String("provider", userInfo.Provider),
		zap.String("email", userInfo.Email))

	return newUser, true, nil // New user created
}

// createUserSession creates a new session for authenticated user
func (s *OAuthService) createUserSession(ctx context.Context, userID uuid.UUID) (*repository.Session, error) {
	sessionID := uuid.New()
	token, err := s.jwtService.GenerateToken(userID)
	if err != nil {
		return nil, fmt.Errorf("failed to generate JWT token: %w", err)
	}

	// Generate refresh token
	refreshToken := s.generateRefreshToken()

	session := &repository.Session{
		ID:           sessionID.String(),
		UserID:       userID.String(),
		Token:        token,
		RefreshToken: refreshToken,
		ExpiresAt:    time.Now().Add(s.config.JWT.Expiration).Format(time.RFC3339),
		CreatedAt:    time.Now().Format(time.RFC3339),
	}

	if err := s.repo.CreateSession(ctx, session); err != nil {
		return nil, fmt.Errorf("failed to create session: %w", err)
	}

	// Create refresh token record for tracking and revocation
	refreshTokenRecord := &repository.RefreshToken{
		UserID:    userID.String(),
		Token:     refreshToken,
		ExpiresAt: time.Now().Add(s.config.JWT.RefreshExpiration).Format(time.RFC3339), // 30 days default
	}

	if _, err := s.repo.CreateRefreshToken(ctx, refreshTokenRecord); err != nil {
		s.logger.Warn("Failed to create refresh token record, but session created successfully",
			zap.String("user_id", userID.String()),
			zap.Error(err))
		// Don't fail the whole operation if refresh token creation fails
	}

	return session, nil
}

// generateUsername generates a unique username from name and email
func (s *OAuthService) generateUsername(name, email string) string {
	// Simple username generation - in production, ensure uniqueness
	if name != "" {
		return strings.ReplaceAll(strings.ToLower(name), " ", "_")
	}
	if email != "" {
		return strings.Split(email, "@")[0]
	}
	return fmt.Sprintf("user_%d", time.Now().Unix())
}

// generateRefreshToken generates a secure refresh token
func (s *OAuthService) generateRefreshToken() string {
	bytes := make([]byte, 32)
	rand.Read(bytes)
	return base64.URLEncoding.EncodeToString(bytes)
}