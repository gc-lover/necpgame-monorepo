// Package server Issue: #1607, ogen migration
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"sync"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/feedback-service-go/pkg/api"
	"github.com/google/uuid"
	"github.com/sirupsen/logrus"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
// Issue: #1607 - Memory pooling for hot path structs (Level 2 optimization)
type Handlers struct {
	feedbackService FeedbackServiceInterface
	logger          *logrus.Logger

	// Memory pooling for hot path structs (zero allocations target!)
	feedbackPool               sync.Pool
	feedbackListResponsePool   sync.Pool
	submitFeedbackResponsePool sync.Pool
}

// NewHandlers creates new handlers with memory pooling
func NewHandlers(feedbackService FeedbackServiceInterface) *Handlers {
	h := &Handlers{
		feedbackService: feedbackService,
		logger:          GetLogger(),
	}

	// Initialize memory pools (zero allocations target!)
	h.feedbackPool = sync.Pool{
		New: func() interface{} {
			return &api.Feedback{}
		},
	}
	h.feedbackListResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.FeedbackListResponse{}
		},
	}
	h.submitFeedbackResponsePool = sync.Pool{
		New: func() interface{} {
			return &api.SubmitFeedbackResponse{}
		},
	}

	return h
}

// GetFeedback - TYPED response!
func (h *Handlers) GetFeedback(ctx context.Context, params api.GetFeedbackParams) (api.GetFeedbackRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	feedback, err := h.feedbackService.GetFeedback(ctx, params.ID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get feedback")
		return &api.GetFeedbackInternalServerError{
			Error:   "internal_server_error",
			Message: "failed to get feedback",
		}, nil
	}

	if feedback == nil {
		return &api.GetFeedbackNotFound{
			Error:   "not_found",
			Message: "feedback not found",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiFeedback := h.feedbackPool.Get().(*api.Feedback)
	// Note: Not returning to pool - struct is returned to caller
	*apiFeedback = convertFeedbackToAPI(feedback)
	return apiFeedback, nil
}

// GetPlayerFeedback - TYPED response!
func (h *Handlers) GetPlayerFeedback(ctx context.Context, params api.GetPlayerFeedbackParams) (api.GetPlayerFeedbackRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	var status *models.FeedbackStatus
	if params.Status.Set {
		s := convertFeedbackStatusFromAPI(params.Status.Value)
		status = &s
	}

	limit := 20
	offset := 0
	if params.Limit.Set {
		limit = params.Limit.Value
	}
	if params.Offset.Set {
		offset = params.Offset.Value
	}

	response, err := h.feedbackService.GetPlayerFeedback(ctx, params.PlayerID, status, nil, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player feedback")
		return &api.GetPlayerFeedbackInternalServerError{
			Error:   "internal_server_error",
			Message: "failed to get player feedback",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiResponse := h.feedbackListResponsePool.Get().(*api.FeedbackListResponse)
	// Note: Not returning to pool - struct is returned to caller
	*apiResponse = convertFeedbackListToAPI(response)
	return apiResponse, nil
}

// SubmitFeedback - TYPED response!
func (h *Handlers) SubmitFeedback(ctx context.Context, req *api.SubmitFeedbackRequest) (api.SubmitFeedbackRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	userID := ctx.Value("user_id")
	if userID == nil {
		return &api.SubmitFeedbackUnauthorized{
			Error:   "unauthorized",
			Message: "user not authenticated",
		}, nil
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		return &api.SubmitFeedbackBadRequest{
			Error:   "bad_request",
			Message: "invalid user id",
		}, nil
	}

	internalReq := convertSubmitFeedbackRequestFromAPI(req)
	response, err := h.feedbackService.SubmitFeedback(ctx, playerID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to submit feedback")
		return &api.SubmitFeedbackInternalServerError{
			Error:   "internal_server_error",
			Message: "failed to submit feedback",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiResponse := h.submitFeedbackResponsePool.Get().(*api.SubmitFeedbackResponse)
	// Note: Not returning to pool - struct is returned to caller
	*apiResponse = convertSubmitFeedbackResponseToAPI(response)
	return apiResponse, nil
}

// UpdateFeedbackStatus - TYPED response!
func (h *Handlers) UpdateFeedbackStatus(ctx context.Context, req *api.UpdateStatusRequest, params api.UpdateFeedbackStatusParams) (api.UpdateFeedbackStatusRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()

	internalReq := convertUpdateStatusRequestFromAPI(req)
	feedback, err := h.feedbackService.UpdateStatus(ctx, params.ID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update status")
		return &api.UpdateFeedbackStatusInternalServerError{
			Error:   "internal_server_error",
			Message: "failed to update status",
		}, nil
	}

	// Issue: #1607 - Use memory pooling
	apiFeedback := h.feedbackPool.Get().(*api.Feedback)
	// Note: Not returning to pool - struct is returned to caller
	*apiFeedback = convertFeedbackToAPI(feedback)
	return apiFeedback, nil
}
