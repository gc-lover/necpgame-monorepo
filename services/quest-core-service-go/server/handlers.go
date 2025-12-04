// Issue: #1597
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"errors"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-core-service-go/pkg/api"
	"github.com/redis/go-redis/v9"
)

// Context timeout constants
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

var (
	ErrNotFound = errors.New("not found")
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct {
	service *Service
}

// NewHandlers creates new handlers
func NewHandlers(redisClient *redis.Client) *Handlers {
	repo := NewRepository()
	service := NewService(repo, redisClient) // Issue: #1609 - pass Redis client
	return &Handlers{service: service}
}

// StartQuest - TYPED response!
func (h *Handlers) StartQuest(ctx context.Context, req *api.StartQuestRequest) (api.StartQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.StartQuest(ctx, req)
	if err != nil {
		return &api.StartQuestInternalServerError{}, err
	}

	return result, nil
}

// GetQuest - TYPED response!
func (h *Handlers) GetQuest(ctx context.Context, params api.GetQuestParams) (api.GetQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetQuest(ctx, params.QuestID)
	if err != nil {
		if err == ErrNotFound {
			return &api.GetQuestNotFound{}, nil
		}
		return &api.GetQuestInternalServerError{}, err
	}

	return result, nil
}

// GetPlayerQuests - TYPED response!
func (h *Handlers) GetPlayerQuests(ctx context.Context, params api.GetPlayerQuestsParams) (api.GetPlayerQuestsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.GetPlayerQuests(ctx, params)
	if err != nil {
		return &api.GetPlayerQuestsInternalServerError{}, err
	}

	return result, nil
}

// CancelQuest - TYPED response!
func (h *Handlers) CancelQuest(ctx context.Context, params api.CancelQuestParams) (api.CancelQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	result, err := h.service.CancelQuest(ctx, params.QuestID)
	if err != nil {
		if err == ErrNotFound {
			return &api.CancelQuestNotFound{}, nil
		}
		return &api.CancelQuestInternalServerError{}, err
	}

	return result, nil
}

// CompleteQuest - TYPED response!
func (h *Handlers) CompleteQuest(ctx context.Context, req api.OptCompleteQuestRequest, params api.CompleteQuestParams) (api.CompleteQuestRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var reqPtr *api.CompleteQuestRequest
	if req.IsSet() {
		reqPtr = &req.Value
	}

	result, err := h.service.CompleteQuest(ctx, params.QuestID, reqPtr)
	if err != nil {
		if err == ErrNotFound {
			return &api.CompleteQuestNotFound{}, nil
		}
		return &api.CompleteQuestInternalServerError{}, err
	}

	return result, nil
}
