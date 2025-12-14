// Issue: #136
package server

import (
	"context"
	"crypto/rand"
	"encoding/hex"
	"encoding/json"
	"fmt"
	"net/http"
	"strings"

	"go.uber.org/zap"
	"golang.org/x/oauth2"
	"golang.org/x/oauth2/github"
	"golang.org/x/oauth2/google"
)

// OAuthProvider представляет OAuth провайдера
type OAuthProvider string

const (
	GoogleProvider  OAuthProvider = "google"
	GitHubProvider  OAuthProvider = "github"
	DiscordProvider OAuthProvider = "discord"
)

// OAuthUserInfo представляет информацию о пользователе от OAuth провайдера
type OAuthUserInfo struct {
	ID            string
	Email         string
	Username      string
	Name          string
	EmailVerified bool
	AvatarURL     string
}

// OAuthClient управляет OAuth аутентификацией
type OAuthClient struct {
	configs map[OAuthProvider]*oauth2.Config
	logger  *zap.Logger
}

// NewOAuthClient создает новый OAuth клиент
func NewOAuthClient(config *OAuthConfig, logger *zap.Logger) *OAuthClient {
	configs := make(map[OAuthProvider]*oauth2.Config)

	// Google OAuth
	if config.GoogleClientID != "" && config.GoogleClientSecret != "" {
		configs[GoogleProvider] = &oauth2.Config{
			ClientID:     config.GoogleClientID,
			ClientSecret: config.GoogleClientSecret,
			RedirectURL:  "http://localhost:8081/api/v1/auth/oauth/google/callback",
			Scopes:       []string{"openid", "profile", "email"},
			Endpoint:     google.Endpoint,
		}
	}

	// GitHub OAuth
	if config.GitHubClientID != "" && config.GitHubClientSecret != "" {
		configs[GitHubProvider] = &oauth2.Config{
			ClientID:     config.GitHubClientID,
			ClientSecret: config.GitHubClientSecret,
			RedirectURL:  "http://localhost:8081/api/v1/auth/oauth/github/callback",
			Scopes:       []string{"user:email", "read:user"},
			Endpoint:     github.Endpoint,
		}
	}

	// Discord OAuth (custom endpoint)
	if config.DiscordClientID != "" && config.DiscordClientSecret != "" {
		configs[DiscordProvider] = &oauth2.Config{
			ClientID:     config.DiscordClientID,
			ClientSecret: config.DiscordClientSecret,
			RedirectURL:  "http://localhost:8081/api/v1/auth/oauth/discord/callback",
			Scopes:       []string{"identify", "email"},
			Endpoint: oauth2.Endpoint{
				AuthURL:  "https://discord.com/api/oauth2/authorize",
				TokenURL: "https://discord.com/api/oauth2/token",
			},
		}
	}

	return &OAuthClient{
		configs: configs,
		logger:  logger,
	}
}

// GetAuthURL возвращает URL для перенаправления пользователя к OAuth провайдеру
func (c *OAuthClient) GetAuthURL(provider OAuthProvider, state string) (string, error) {
	config, exists := c.configs[provider]
	if !exists {
		return "", fmt.Errorf("OAuth provider %s not configured", provider)
	}

	return config.AuthCodeURL(state, oauth2.AccessTypeOffline), nil
}

// ExchangeCode обменивает authorization code на access token
func (c *OAuthClient) ExchangeCode(ctx context.Context, provider OAuthProvider, code, state string) (*oauth2.Token, error) {
	config, exists := c.configs[provider]
	if !exists {
		return nil, fmt.Errorf("OAuth provider %s not configured", provider)
	}

	token, err := config.Exchange(ctx, code)
	if err != nil {
		return nil, fmt.Errorf("failed to exchange code: %w", err)
	}

	return token, nil
}

// GetUserInfo получает информацию о пользователе от OAuth провайдера
func (c *OAuthClient) GetUserInfo(ctx context.Context, provider OAuthProvider, token *oauth2.Token) (*OAuthUserInfo, error) {
	switch provider {
	case GoogleProvider:
		return c.getGoogleUserInfo(ctx, token)
	case GitHubProvider:
		return c.getGitHubUserInfo(ctx, token)
	case DiscordProvider:
		return c.getDiscordUserInfo(ctx, token)
	default:
		return nil, fmt.Errorf("unsupported OAuth provider: %s", provider)
	}
}

// getGoogleUserInfo получает информацию о пользователе от Google
func (c *OAuthClient) getGoogleUserInfo(ctx context.Context, token *oauth2.Token) (*OAuthUserInfo, error) {
	client := oauth2.NewClient(ctx, oauth2.StaticTokenSource(token))

	resp, err := client.Get("https://www.googleapis.com/oauth2/v2/userinfo")
	if err != nil {
		return nil, fmt.Errorf("failed to get Google user info: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("Google API returned status %d", resp.StatusCode)
	}

	var googleUser struct {
		ID            string `json:"id"`
		Email         string `json:"email"`
		VerifiedEmail bool   `json:"verified_email"`
		Name          string `json:"name"`
		GivenName     string `json:"given_name"`
		FamilyName    string `json:"family_name"`
		Picture       string `json:"picture"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&googleUser); err != nil {
		return nil, fmt.Errorf("failed to decode Google user info: %w", err)
	}

	// Создаем username из email или имени
	username := strings.Split(googleUser.Email, "@")[0]
	if googleUser.GivenName != "" && googleUser.FamilyName != "" {
		username = strings.ToLower(googleUser.GivenName + googleUser.FamilyName)
		username = strings.ReplaceAll(username, " ", "")
	}

	return &OAuthUserInfo{
		ID:            googleUser.ID,
		Email:         googleUser.Email,
		Username:      username,
		Name:          googleUser.Name,
		EmailVerified: googleUser.VerifiedEmail,
		AvatarURL:     googleUser.Picture,
	}, nil
}

// getGitHubUserInfo получает информацию о пользователе от GitHub
func (c *OAuthClient) getGitHubUserInfo(ctx context.Context, token *oauth2.Token) (*OAuthUserInfo, error) {
	client := oauth2.NewClient(ctx, oauth2.StaticTokenSource(token))

	// Получаем основную информацию о пользователе
	resp, err := client.Get("https://api.github.com/user")
	if err != nil {
		return nil, fmt.Errorf("failed to get GitHub user info: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("GitHub API returned status %d", resp.StatusCode)
	}

	var githubUser struct {
		ID        int    `json:"id"`
		Login     string `json:"login"`
		Name      string `json:"name"`
		Email     string `json:"email"`
		AvatarURL string `json:"avatar_url"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&githubUser); err != nil {
		return nil, fmt.Errorf("failed to decode GitHub user info: %w", err)
	}

	// Если email не получен, пытаемся получить emails
	if githubUser.Email == "" {
		emailsResp, err := client.Get("https://api.github.com/user/emails")
		if err == nil {
			defer emailsResp.Body.Close()
			if emailsResp.StatusCode == http.StatusOK {
				var emails []struct {
					Email    string `json:"email"`
					Primary  bool   `json:"primary"`
					Verified bool   `json:"verified"`
				}
				if err := json.NewDecoder(emailsResp.Body).Decode(&emails); err == nil {
					for _, email := range emails {
						if email.Primary && email.Verified {
							githubUser.Email = email.Email
							break
						}
					}
				}
			}
		}
	}

	return &OAuthUserInfo{
		ID:            fmt.Sprintf("%d", githubUser.ID),
		Email:         githubUser.Email,
		Username:      githubUser.Login,
		Name:          githubUser.Name,
		EmailVerified: githubUser.Email != "", // GitHub email считается верифицированным если получен
		AvatarURL:     githubUser.AvatarURL,
	}, nil
}

// getDiscordUserInfo получает информацию о пользователе от Discord
func (c *OAuthClient) getDiscordUserInfo(ctx context.Context, token *oauth2.Token) (*OAuthUserInfo, error) {
	client := oauth2.NewClient(ctx, oauth2.StaticTokenSource(token))

	resp, err := client.Get("https://discord.com/api/users/@me")
	if err != nil {
		return nil, fmt.Errorf("failed to get Discord user info: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("Discord API returned status %d", resp.StatusCode)
	}

	var discordUser struct {
		ID            string `json:"id"`
		Username      string `json:"username"`
		Discriminator string `json:"discriminator"`
		GlobalName    string `json:"global_name"`
		Email         string `json:"email"`
		Verified      bool   `json:"verified"`
		Avatar        string `json:"avatar"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&discordUser); err != nil {
		return nil, fmt.Errorf("failed to decode Discord user info: %w", err)
	}

	// Создаем display name
	displayName := discordUser.Username
	if discordUser.Discriminator != "0" {
		displayName = fmt.Sprintf("%s#%s", discordUser.Username, discordUser.Discriminator)
	} else if discordUser.GlobalName != "" {
		displayName = discordUser.GlobalName
	}

	// Создаем username для нашей системы
	username := discordUser.Username
	if discordUser.GlobalName != "" {
		username = strings.ToLower(strings.ReplaceAll(discordUser.GlobalName, " ", ""))
	}

	// Avatar URL
	avatarURL := ""
	if discordUser.Avatar != "" {
		avatarURL = fmt.Sprintf("https://cdn.discordapp.com/avatars/%s/%s.png", discordUser.ID, discordUser.Avatar)
	}

	return &OAuthUserInfo{
		ID:            discordUser.ID,
		Email:         discordUser.Email,
		Username:      username,
		Name:          displayName,
		EmailVerified: discordUser.Verified,
		AvatarURL:     avatarURL,
	}, nil
}

// GenerateState генерирует случайное состояние для защиты от CSRF
func (c *OAuthClient) GenerateState() (string, error) {
	bytes := make([]byte, 32)
	if _, err := rand.Read(bytes); err != nil {
		return "", fmt.Errorf("failed to generate state: %w", err)
	}
	return hex.EncodeToString(bytes), nil
}

// ValidateProvider проверяет что провайдер поддерживается
func (c *OAuthClient) ValidateProvider(provider string) (OAuthProvider, error) {
	p := OAuthProvider(provider)
	switch p {
	case GoogleProvider, GitHubProvider, DiscordProvider:
		if _, exists := c.configs[p]; !exists {
			return "", fmt.Errorf("OAuth provider %s not configured", provider)
		}
		return p, nil
	default:
		return "", fmt.Errorf("unsupported OAuth provider: %s", provider)
	}
}
