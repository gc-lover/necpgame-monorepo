// Issue: #1856
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/guild-core-service-go/pkg/api"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// ListGuilds returns list of guilds - TYPED response!
func (h *Handlers) ListGuilds(ctx context.Context, params api.ListGuildsParams) (api.ListGuildsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	guilds, err := h.service.GetGuilds(ctx, params)
	if err != nil {
		return &api.ListGuildsInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return guilds, nil
}

// GetGuild returns guild details - TYPED response!
func (h *Handlers) GetGuild(ctx context.Context, params api.GetGuildParams) (api.GetGuildRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	guild, err := h.service.GetGuild(ctx, params)
	if err != nil {
		// Check for specific error types
		if err.Error() == "guild not found" {
			return &api.GetGuildNotFound{}, nil
		}
		return &api.GetGuildInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return guild, nil
}

// CreateGuild creates a new guild - TYPED response!
func (h *Handlers) CreateGuild(ctx context.Context, req api.CreateGuildRequest) (api.CreateGuildRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CreateGuild(ctx, &req)
	if err != nil {
		// Check for validation errors
		if err.Error() == "guild name must be 3-50 characters" ||
		   err.Error() == "guild tag must be 2-5 characters" {
			return &api.CreateGuildBadRequest{}, nil
		}
		if err.Error() == "guild name already taken" ||
		   err.Error() == "guild tag already taken" {
			return &api.CreateGuildConflict{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.CreateGuildUnauthorized{}, nil
		}
		return &api.CreateGuildInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// UpdateGuild updates guild information - TYPED response!
func (h *Handlers) UpdateGuild(ctx context.Context, params api.UpdateGuildParams, req api.CreateGuildRequest) (api.UpdateGuildRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UpdateGuild(ctx, params, &req)
	if err != nil {
		// Check for authorization errors
		if err == ErrNotGuildLeader {
			return &api.UpdateGuildForbidden{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.UpdateGuildUnauthorized{}, nil
		}
		if err.Error() == "guild not found" {
			return &api.UpdateGuildNotFound{}, nil
		}
		return &api.UpdateGuildInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}

// DeleteGuild deletes a guild - TYPED response!
func (h *Handlers) DeleteGuild(ctx context.Context, params api.DeleteGuildParams) (api.DeleteGuildRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteGuild(ctx, params)
	if err != nil {
		// Check for authorization errors
		if err == ErrNotGuildLeader {
			return &api.DeleteGuildForbidden{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.DeleteGuildUnauthorized{}, nil
		}
		if err.Error() == "guild not found" {
			return &api.DeleteGuildNotFound{}, nil
		}
		return &api.DeleteGuildInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return &api.DeleteGuildNoContent{}, nil
}

// GuildWebSocket handles WebSocket connections - TYPED response!
func (h *Handlers) GuildWebSocket(ctx context.Context, params api.GetGuildParams) (api.GuildWebSocketRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GuildWebSocket(ctx, params)
	if err != nil {
		// Check for authorization errors
		if err == ErrNotGuildMember {
			return &api.GuildWebSocketForbidden{}, nil
		}
		if err.Error() == "user not authenticated" {
			return &api.GuildWebSocketUnauthorized{}, nil
		}
		if err.Error() == "guild not found" {
			return &api.GuildWebSocketNotFound{}, nil
		}
		return &api.GuildWebSocketInternalServerError{}, err
	}

	// Return TYPED response (ogen will marshal directly!)
	return result, nil
}
