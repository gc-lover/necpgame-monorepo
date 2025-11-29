package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	feedbackapi "github.com/necpgame/feedback-service-go/pkg/api"
	"github.com/necpgame/feedback-service-go/models"
	"github.com/sirupsen/logrus"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

type FeedbackHandlers struct {
	feedbackService FeedbackServiceInterface
	logger          *logrus.Logger
}

func NewFeedbackHandlers(feedbackService FeedbackServiceInterface) *FeedbackHandlers {
	return &FeedbackHandlers{
		feedbackService: feedbackService,
		logger:          GetLogger(),
	}
}

func (h *FeedbackHandlers) SubmitFeedback(w http.ResponseWriter, r *http.Request) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	var req feedbackapi.SubmitFeedbackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertSubmitFeedbackRequestFromAPI(&req)
	response, err := h.feedbackService.SubmitFeedback(r.Context(), playerID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to submit feedback")
		h.respondError(w, http.StatusInternalServerError, "failed to submit feedback")
		return
	}

	apiResponse := convertSubmitFeedbackResponseToAPI(response)
	h.respondJSON(w, http.StatusCreated, apiResponse)
}

func (h *FeedbackHandlers) GetFeedback(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	feedbackID := uuid.UUID(id)
	feedback, err := h.feedbackService.GetFeedback(r.Context(), feedbackID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get feedback")
		h.respondError(w, http.StatusInternalServerError, "failed to get feedback")
		return
	}

	if feedback == nil {
		h.respondError(w, http.StatusNotFound, "feedback not found")
		return
	}

	apiFeedback := convertFeedbackToAPI(feedback)
	h.respondJSON(w, http.StatusOK, apiFeedback)
}

func (h *FeedbackHandlers) GetPlayerFeedback(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params feedbackapi.GetPlayerFeedbackParams) {
	playerID := uuid.UUID(playerId)
	
	var status *models.FeedbackStatus
	var feedbackType *models.FeedbackType
	
	if params.Status != nil {
		s := convertFeedbackStatusFromAPI(*params.Status)
		status = &s
	}
	
	if params.Type != nil {
		t := convertGetPlayerFeedbackParamsTypeFromAPI(*params.Type)
		feedbackType = &t
	}
	
	limit := 20
	offset := 0
	if params.Limit != nil {
		limit = *params.Limit
	}
	if params.Offset != nil {
		offset = *params.Offset
	}

	response, err := h.feedbackService.GetPlayerFeedback(r.Context(), playerID, status, feedbackType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get player feedback")
		h.respondError(w, http.StatusInternalServerError, "failed to get player feedback")
		return
	}

	apiResponse := convertFeedbackListToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *FeedbackHandlers) UpdateFeedbackStatus(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	feedbackID := uuid.UUID(id)

	var req feedbackapi.UpdateStatusRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	internalReq := convertUpdateStatusRequestFromAPI(&req)
	feedback, err := h.feedbackService.UpdateStatus(r.Context(), feedbackID, internalReq)
	if err != nil {
		h.logger.WithError(err).Error("Failed to update status")
		h.respondError(w, http.StatusInternalServerError, "failed to update status")
		return
	}

	apiFeedback := convertFeedbackToAPI(feedback)
	h.respondJSON(w, http.StatusOK, apiFeedback)
}

func (h *FeedbackHandlers) GetFeedbackBoard(w http.ResponseWriter, r *http.Request, params feedbackapi.GetFeedbackBoardParams) {
	var category *models.FeedbackCategory
	var status *models.FeedbackStatus
	var search *string

	if params.Category != nil {
		c := convertGetFeedbackBoardParamsCategoryFromAPI(*params.Category)
		category = &c
	}

	if params.Status != nil {
		s := convertGetFeedbackBoardParamsStatusFromAPI(*params.Status)
		status = &s
	}

	if params.Search != nil {
		search = params.Search
	}

	sort := "created"
	if params.Sort != nil {
		sort = convertSortFromAPI(*params.Sort)
	}

	limit := 20
	offset := 0
	if params.Limit != nil {
		limit = *params.Limit
	}
	if params.Offset != nil {
		offset = *params.Offset
	}

	response, err := h.feedbackService.GetBoard(r.Context(), category, status, search, sort, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get board")
		h.respondError(w, http.StatusInternalServerError, "failed to get board")
		return
	}

	apiResponse := convertFeedbackBoardListToAPI(response)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *FeedbackHandlers) VoteFeedback(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	feedbackID := uuid.UUID(id)
	response, err := h.feedbackService.Vote(r.Context(), feedbackID, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to vote")
		h.respondError(w, http.StatusInternalServerError, "failed to vote")
		return
	}

	apiResponse := convertVoteResponseToAPI(response, feedbackID)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *FeedbackHandlers) UnvoteFeedback(w http.ResponseWriter, r *http.Request, id openapi_types.UUID) {
	userID := r.Context().Value("user_id")
	if userID == nil {
		h.respondError(w, http.StatusUnauthorized, "user not authenticated")
		return
	}

	playerID, err := uuid.Parse(userID.(string))
	if err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid user id")
		return
	}

	feedbackID := uuid.UUID(id)
	response, err := h.feedbackService.Unvote(r.Context(), feedbackID, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to unvote")
		h.respondError(w, http.StatusInternalServerError, "failed to unvote")
		return
	}

	apiResponse := convertVoteResponseToAPI(response, feedbackID)
	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *FeedbackHandlers) GetFeedbackStats(w http.ResponseWriter, r *http.Request) {
	stats, err := h.feedbackService.GetStats(r.Context())
	if err != nil {
		h.logger.WithError(err).Error("Failed to get stats")
		h.respondError(w, http.StatusInternalServerError, "failed to get stats")
		return
	}

	apiStats := convertFeedbackStatsToAPI(stats)
	h.respondJSON(w, http.StatusOK, apiStats)
}

func (h *FeedbackHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *FeedbackHandlers) respondError(w http.ResponseWriter, status int, message string) {
	h.respondJSON(w, status, feedbackapi.Error{
		Error:   &message,
		Message: &message,
	})
}

