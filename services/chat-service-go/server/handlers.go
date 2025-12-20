// Package server Issue: #1595
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/chat-service-go/pkg/api"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(service *Service) *Handlers {
	return &Handlers{service: service}
}

// SendMessage implements sendMessage operation
func (h *Handlers) SendMessage(ctx context.Context, req *api.SendMessageRequest) (api.SendMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.SendMessage(ctx, req)
	if err != nil {
		return &api.SendMessageBadRequest{
			Error:   err.Error(),
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// GetChannelMessages implements getChannelMessages operation
func (h *Handlers) GetChannelMessages(ctx context.Context, params api.GetChannelMessagesParams) (api.GetChannelMessagesRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetChannelMessages(ctx, params)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetChannelMessagesNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.GetChannelMessagesForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// GetChannels implements getChannels operation
func (h *Handlers) GetChannels(ctx context.Context) (*api.ChannelsListResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetChannels(ctx)
	if err != nil {
		return nil, err
	}

	return result, nil
}

// CreateChannel implements createChannel operation
func (h *Handlers) CreateChannel(ctx context.Context, req *api.CreateChannelRequest) (api.CreateChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CreateChannel(ctx, req)
	if err != nil {
		return &api.Error{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// GetChannel implements getChannel operation
func (h *Handlers) GetChannel(ctx context.Context, params api.GetChannelParams) (api.GetChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetChannel(ctx, params)
	if err != nil {
		return &api.Error{
			Error:   "NOT_FOUND",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// UpdateChannel implements updateChannel operation
func (h *Handlers) UpdateChannel(ctx context.Context, req *api.UpdateChannelRequest, params api.UpdateChannelParams) (api.UpdateChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.UpdateChannel(ctx, params, req)
	if err != nil {
		if err == ErrNotFound {
			return &api.UpdateChannelNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.UpdateChannelForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// DeleteChannel implements deleteChannel operation
func (h *Handlers) DeleteChannel(ctx context.Context, params api.DeleteChannelParams) (api.DeleteChannelRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteChannel(ctx, params)
	if err != nil {
		if err == ErrNotFound {
			return &api.DeleteChannelNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.DeleteChannelForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// AddChannelMember implements addChannelMember operation
func (h *Handlers) AddChannelMember(ctx context.Context, req *api.AddMemberRequest, params api.AddChannelMemberParams) (api.AddChannelMemberRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	_, err := h.service.AddChannelMember(ctx, params, req)
	if err != nil {
		if err == ErrNotFound {
			return &api.AddChannelMemberNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.AddChannelMemberForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// RemoveChannelMember implements removeChannelMember operation
func (h *Handlers) RemoveChannelMember(ctx context.Context, params api.RemoveChannelMemberParams) (api.RemoveChannelMemberRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.RemoveChannelMember(ctx, params)
	if err != nil {
		if err == ErrNotFound {
			return &api.RemoveChannelMemberNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.RemoveChannelMemberForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// BanPlayer implements banPlayer operation
func (h *Handlers) BanPlayer(ctx context.Context, req *api.BanPlayerRequest) (api.BanPlayerRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.BanPlayer(ctx, req)
	if err != nil {
		return &api.BanPlayerBadRequest{
			Error:   "BAD_REQUEST",
			Message: err.Error(),
		}, nil
	}

	return result, nil
}

// UnbanPlayer implements unbanPlayer operation
func (h *Handlers) UnbanPlayer(ctx context.Context, params api.UnbanPlayerParams) (api.UnbanPlayerRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.UnbanPlayer(ctx, params)
	if err != nil {
		if err == ErrNotFound {
			return &api.UnbanPlayerNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.UnbanPlayerForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}

// DeleteMessage implements deleteMessage operation
func (h *Handlers) DeleteMessage(ctx context.Context, params api.DeleteMessageParams) (api.DeleteMessageRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	err := h.service.DeleteMessage(ctx, params)
	if err != nil {
		if err == ErrNotFound {
			return &api.DeleteMessageNotFound{
				Error:   "NOT_FOUND",
				Message: err.Error(),
			}, nil
		}
		return &api.DeleteMessageForbidden{
			Error:   "FORBIDDEN",
			Message: err.Error(),
		}, nil
	}

	return &api.SuccessResponse{
		Status: api.NewOptString("success"),
	}, nil
}
