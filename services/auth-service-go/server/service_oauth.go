// Issue: #136 - OAuth authentication operations
package server

import (
	"context"
	"encoding/json"
	"io"
	"net/http"
	"net/url"
	"time"

	"go.uber.org/zap"

	"necpgame/services/auth-service-go/pkg/api"
)

// InitiateOAuth начинает OAuth процесс
func (s *Service) InitiateOAuth(ctx context.Context, req *api.InitiateOAuthRequest) (*api.OAuthURLResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	var authURL string
	var state string

	switch req.Provider {
	case "google":
		authURL, state = s.buildGoogleOAuthURL(req.RedirectUri)
	case "github":
		authURL, state = s.buildGitHubOAuthURL(req.RedirectUri)
	case "discord":
		authURL, state = s.buildDiscordOAuthURL(req.RedirectUri)
	default:
		return nil, &ValidationError{Field: "provider", Message: "unsupported OAuth provider"}
	}

	// Store state for verification
	if err := s.storeOAuthState(ctx, state, req.Provider, req.RedirectUri); err != nil {
		s.logger.Error("Failed to store OAuth state", zap.Error(err))
		return nil, err
	}

	return &api.OAuthURLResponse{
		AuthUrl: authURL,
		State:   state,
	}, nil
}

// CompleteOAuth завершает OAuth процесс
func (s *Service) CompleteOAuth(ctx context.Context, req *api.CompleteOAuthRequest) (*LoginResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, 15*time.Second)
	defer cancel()

	// Verify state
	stateData, err := s.getOAuthState(ctx, req.State)
	if err != nil {
		s.logger.Error("Failed to get OAuth state", zap.Error(err))
		return nil, &AuthenticationError{Message: "invalid OAuth state"}
	}

	if stateData == nil || stateData.ExpiresAt.Before(time.Now()) {
		return nil, &AuthenticationError{Message: "OAuth state expired"}
	}

	// Exchange code for token
	var userInfo *OAuthUserInfo
	switch stateData.Provider {
	case "google":
		userInfo, err = s.exchangeGoogleCode(req.Code, stateData.RedirectUri)
	case "github":
		userInfo, err = s.exchangeGitHubCode(req.Code, stateData.RedirectUri)
	case "discord":
		userInfo, err = s.exchangeDiscordCode(req.Code, stateData.RedirectUri)
	default:
		return nil, &ValidationError{Field: "provider", Message: "unsupported OAuth provider"}
	}

	if err != nil {
		s.logger.Error("Failed to exchange OAuth code", zap.Error(err))
		return nil, &AuthenticationError{Message: "OAuth authentication failed"}
	}

	// Find or create user
	user, err := s.findOrCreateOAuthUser(ctx, userInfo, stateData.Provider)
	if err != nil {
		s.logger.Error("Failed to find/create OAuth user", zap.Error(err))
		return nil, err
	}

	// Generate tokens
	accessToken, refreshToken, err := s.generateTokens(ctx, user.ID)
	if err != nil {
		s.logger.Error("Failed to generate tokens", zap.Error(err))
		return nil, err
	}

	// Get user info
	userAPIInfo, err := s.getUserInfo(ctx, user.ID)
	if err != nil {
		s.logger.Error("Failed to get user info", zap.Error(err))
		return nil, err
	}

	// Clean up state
	if err := s.deleteOAuthState(ctx, req.State); err != nil {
		s.logger.Error("Failed to clean up OAuth state", zap.Error(err))
		// Don't fail the auth for this
	}

	return &LoginResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    3600,
		User:         userAPIInfo,
	}, nil
}

// buildGoogleOAuthURL строит URL для Google OAuth
func (s *Service) buildGoogleOAuthURL(redirectURI string) (string, string) {
	state := generateSecureToken()

	baseURL := "https://accounts.google.com/o/oauth2/v2/auth"
	params := url.Values{
		"client_id":     {s.oauthConfig.GoogleClientID},
		"redirect_uri":  {redirectURI},
		"response_type": {"code"},
		"scope":         {"openid email profile"},
		"state":         {state},
	}

	return baseURL + "?" + params.Encode(), state
}

// exchangeGoogleCode обменивает Google authorization code на access token
func (s *Service) exchangeGoogleCode(code, redirectURI string) (*OAuthUserInfo, error) {
	tokenURL := "https://oauth2.googleapis.com/token"

	data := url.Values{
		"client_id":     {s.oauthConfig.GoogleClientID},
		"client_secret": {s.oauthConfig.GoogleClientSecret},
		"code":          {code},
		"grant_type":    {"authorization_code"},
		"redirect_uri":  {redirectURI},
	}

	resp, err := http.PostForm(tokenURL, data)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	var tokenResp struct {
		AccessToken string `json:"access_token"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&tokenResp); err != nil {
		return nil, err
	}

	// Get user info from Google
	userInfoURL := "https://www.googleapis.com/oauth2/v2/userinfo"
	req, _ := http.NewRequest("GET", userInfoURL, nil)
	req.Header.Set("Authorization", "Bearer "+tokenResp.AccessToken)

	client := &http.Client{Timeout: 10 * time.Second}
	resp2, err := client.Do(req)
	if err != nil {
		return nil, err
	}
	defer resp2.Body.Close()

	var googleUser struct {
		ID    string `json:"id"`
		Email string `json:"email"`
		Name  string `json:"name"`
	}

	body, err := io.ReadAll(resp2.Body)
	if err != nil {
		return nil, err
	}

	if err := json.Unmarshal(body, &googleUser); err != nil {
		return nil, err
	}

	return &OAuthUserInfo{
		ProviderID: googleUser.ID,
		Email:      googleUser.Email,
		Name:       googleUser.Name,
		Provider:   "google",
	}, nil
}
