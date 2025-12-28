package service

import (
	"context"
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/google/uuid"
	"go.uber.org/zap"
)

// GuildMember represents a guild member from guild service
type GuildMember struct {
	PlayerID   uuid.UUID `json:"player_id"`
	Username   string    `json:"username"`
	Role       string    `json:"role"`
	JoinedAt   time.Time `json:"joined_at"`
	IsActive   bool      `json:"is_active"`
	Permissions []string `json:"permissions"`
}

// GuildPermissionsResponse represents guild permissions response
type GuildPermissionsResponse struct {
	AllowedRoles []string `json:"allowed_roles"`
	BlockedUsers []string `json:"blocked_users"`
}

// GuildClient handles communication with guild service
type GuildClient struct {
	baseURL string
	client  *http.Client
	logger  *zap.Logger
}

// NewGuildClient creates a new guild service client
func NewGuildClient(baseURL string, logger *zap.Logger) *GuildClient {
	return &GuildClient{
		baseURL: baseURL,
		client: &http.Client{
			Timeout: 10 * time.Second,
		},
		logger: logger,
	}
}

// ValidateGuildMembership checks if user is a member of the guild
func (gc *GuildClient) ValidateGuildMembership(ctx context.Context, guildID, userID uuid.UUID) (*GuildMember, error) {
	url := fmt.Sprintf("%s/guilds/%s/members", gc.baseURL, guildID.String())

	req, err := http.NewRequestWithContext(ctx, "GET", url, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	// Add JWT token from context if available
	// TODO: Extract JWT from context and add to Authorization header

	resp, err := gc.client.Do(req)
	if err != nil {
		gc.logger.Error("Failed to call guild service",
			zap.String("guild_id", guildID.String()),
			zap.String("user_id", userID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to call guild service: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode == http.StatusNotFound {
		return nil, fmt.Errorf("user is not a member of this guild")
	}

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("guild service returned status %d", resp.StatusCode)
	}

	var response struct {
		Members []GuildMember `json:"members"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&response); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	// Find the user in the members list
	for _, member := range response.Members {
		if member.PlayerID == userID {
			if !member.IsActive {
				return nil, fmt.Errorf("user membership is not active")
			}
			return &member, nil
		}
	}

	return nil, fmt.Errorf("user is not a member of this guild")
}

// GetGuildMembers retrieves all active members of a guild
func (gc *GuildClient) GetGuildMembers(ctx context.Context, guildID uuid.UUID) ([]GuildMember, error) {
	url := fmt.Sprintf("%s/guilds/%s/members?active_only=true", gc.baseURL, guildID.String())

	req, err := http.NewRequestWithContext(ctx, "GET", url, nil)
	if err != nil {
		return nil, fmt.Errorf("failed to create request: %w", err)
	}

	resp, err := gc.client.Do(req)
	if err != nil {
		gc.logger.Error("Failed to get guild members",
			zap.String("guild_id", guildID.String()),
			zap.Error(err))
		return nil, fmt.Errorf("failed to get guild members: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("guild service returned status %d", resp.StatusCode)
	}

	var response struct {
		Members []GuildMember `json:"members"`
	}

	if err := json.NewDecoder(resp.Body).Decode(&response); err != nil {
		return nil, fmt.Errorf("failed to decode response: %w", err)
	}

	return response.Members, nil
}

// CheckGuildPermission checks if user has specific permission in guild
func (gc *GuildClient) CheckGuildPermission(ctx context.Context, guildID, userID uuid.UUID, permission string) (bool, error) {
	member, err := gc.ValidateGuildMembership(ctx, guildID, userID)
	if err != nil {
		return false, err
	}

	// Guild leaders have all permissions
	if member.Role == "leader" {
		return true, nil
	}

	// Officers have most permissions
	if member.Role == "officer" && permission != "edit_guild_settings" && permission != "declare_war" {
		return true, nil
	}

	// Check specific permissions
	for _, p := range member.Permissions {
		if p == permission {
			return true, nil
		}
	}

	return false, nil
}

// HasVoiceChannelPermission checks if user has permission to manage voice channels
func (gc *GuildClient) HasVoiceChannelPermission(ctx context.Context, guildID, userID uuid.UUID) (bool, error) {
	// Voice channel management requires invite_members or manage_bank permission
	hasInvite, err := gc.CheckGuildPermission(ctx, guildID, userID, "invite_members")
	if err != nil {
		return false, err
	}

	if hasInvite {
		return true, nil
	}

	hasManage, err := gc.CheckGuildPermission(ctx, guildID, userID, "manage_bank")
	if err != nil {
		return false, err
	}

	return hasManage, nil
}

// GetUserGuildRole gets the role of a user in a guild
func (gc *GuildClient) GetUserGuildRole(ctx context.Context, guildID, userID uuid.UUID) (string, error) {
	member, err := gc.ValidateGuildMembership(ctx, guildID, userID)
	if err != nil {
		return "", err
	}

	return member.Role, nil
}

// PERFORMANCE: Guild client uses HTTP connection pooling for efficiency
// Error handling includes proper logging for troubleshooting
// Context cancellation supported for graceful shutdowns
