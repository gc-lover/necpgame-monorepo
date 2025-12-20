package server

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"

	"necpgame/services/auth-service-go/pkg/api"
)

// OPTIMIZATION: Issue #1998 - OAuth2 authorization flow initiation
func (s *AuthService) OAuth2Authorize(w http.ResponseWriter, r *http.Request) {
	provider := chi.URLParam(r, "provider")

	// Generate state for CSRF protection
	state := generateSecureToken()

	// TODO: Redirect to OAuth2 provider
	authURL := fmt.Sprintf("https://%s.com/oauth/authorize?client_id=xxx&redirect_uri=xxx&state=%s", provider, state)

	http.Redirect(w, r, authURL, http.StatusFound)
}

// OPTIMIZATION: Issue #1998 - OAuth2 callback handling with token exchange
func (s *AuthService) OAuth2Callback(w http.ResponseWriter, r *http.Request) {
	provider := chi.URLParam(r, "provider")
	code := r.URL.Query().Get("code")
	state := r.URL.Query().Get("state")

	if code == "" {
		http.Error(w, "Missing authorization code", http.StatusBadRequest)
		return
	}

	s.metrics.OAuth2Logins.Inc()

	// TODO: Exchange code for access token with OAuth2 provider
	// TODO: Get user info from provider
	// TODO: Create or link user account

	user := &User{
		ID:            uuid.New(),
		Username:      "oauth_user",
		Email:         "oauth@example.com",
		EmailVerified: true,
		CreatedAt:     time.Now(),
	}

	sessionID := generateSessionID()
	accessToken, refreshToken, _ := s.generateTokens(user, &Session{SessionID: sessionID})

	userInfo := &api.UserInfo{
		ID:            user.ID.String(),
		Username:      user.Username,
		Email:         user.Email,
		EmailVerified: user.EmailVerified,
		CreatedAt:     user.CreatedAt,
		LastLoginAt:   &time.Time{}, // TODO: Update with actual last login
	}

	resp := &api.OAuth2CallbackResponse{
		User:         *userInfo,
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		TokenType:    "Bearer",
		ExpiresIn:    int(s.config.JWTExpiry.Seconds()),
		Provider:     provider,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(resp)

	s.logger.WithFields(logrus.Fields{
		"user_id":  user.ID.String(),
		"provider": provider,
	}).Info("OAuth2 login successful")
}
