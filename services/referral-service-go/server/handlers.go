package server

import (
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/referral-service-go/models"
	"github.com/necpgame/referral-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type ReferralHandlers struct {
	service ReferralService
	logger  *logrus.Logger
}

func NewReferralHandlers(service ReferralService) *ReferralHandlers {
	return &ReferralHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

func (h *ReferralHandlers) GetReferralCode(w http.ResponseWriter, r *http.Request, params api.GetReferralCodeParams) {
	ctx := r.Context()
	playerID := uuid.UUID(params.PlayerId)

	code, err := h.service.GetReferralCode(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get referral code")
		h.respondError(w, http.StatusInternalServerError, "failed to get referral code")
		return
	}

	if code == nil {
		h.respondError(w, http.StatusNotFound, "referral code not found")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferralCode(code))
}

func (h *ReferralHandlers) GenerateReferralCode(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.GenerateReferralCodeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID := uuid.UUID(req.PlayerId)
	code, err := h.service.GenerateReferralCode(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to generate referral code")
		h.respondError(w, http.StatusInternalServerError, "failed to generate referral code")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferralCode(code))
}

func (h *ReferralHandlers) ValidateReferralCode(w http.ResponseWriter, r *http.Request, code string) {
	ctx := r.Context()

	referralCode, err := h.service.ValidateReferralCode(ctx, code)
	if err != nil {
		h.logger.WithError(err).Error("Failed to validate referral code")
		h.respondError(w, http.StatusNotFound, "referral code not found")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]interface{}{
		"valid": referralCode != nil && referralCode.IsActive,
		"code":  toAPIReferralCode(referralCode),
	})
}

func (h *ReferralHandlers) RegisterWithCode(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.RegisterWithCodeRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID := uuid.UUID(req.PlayerId)
	referral, err := h.service.RegisterWithCode(ctx, playerID, req.ReferralCode)
	if err != nil {
		h.logger.WithError(err).Error("Failed to register with code")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferral(referral))
}

func (h *ReferralHandlers) GetReferralStatus(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetReferralStatusParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	var status *models.ReferralStatus
	if params.Status != nil {
		s := models.ReferralStatus(*params.Status)
		status = &s
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	referrals, total, err := h.service.GetReferralStatus(ctx, playerID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get referral status")
		h.respondError(w, http.StatusInternalServerError, "failed to get referral status")
		return
	}

	apiReferrals := make([]api.Referral, len(referrals))
	for i, ref := range referrals {
		apiReferrals[i] = toAPIReferral(&ref)
	}

	response := api.ReferralStatusResponse{
		Referrals: &apiReferrals,
		Total:     &total,
		Limit:     &limit,
		Offset:    &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) GetReferralMilestones(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	milestones, currentType, err := h.service.GetMilestones(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get milestones")
		h.respondError(w, http.StatusInternalServerError, "failed to get milestones")
		return
	}

	apiMilestones := make([]api.ReferralMilestone, len(milestones))
	for i, ms := range milestones {
		apiMilestones[i] = toAPIReferralMilestone(&ms)
	}

	var currentMilestone *api.ReferralMilestonesResponseCurrentMilestone
	if currentType != nil {
		mt := api.ReferralMilestonesResponseCurrentMilestone(*currentType)
		currentMilestone = &mt
	}

	response := api.ReferralMilestonesResponse{
		Milestones:       &apiMilestones,
		CurrentMilestone: currentMilestone,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) ClaimMilestoneReward(w http.ResponseWriter, r *http.Request, milestoneId openapi_types.UUID) {
	ctx := r.Context()

	var req api.ClaimMilestoneRewardRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	playerID := uuid.UUID(req.PlayerId)
	milestoneID := uuid.UUID(milestoneId)

	milestone, err := h.service.ClaimMilestoneReward(ctx, playerID, milestoneID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to claim milestone reward")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferralMilestone(milestone))
}

func (h *ReferralHandlers) DistributeReferralRewards(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.DistributeRewardsRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	referralID := uuid.UUID(req.ReferralId)
	rewardType := models.ReferralRewardType(req.RewardType)

	err := h.service.DistributeRewards(ctx, referralID, rewardType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to distribute rewards")
		h.respondError(w, http.StatusInternalServerError, "failed to distribute rewards")
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *ReferralHandlers) GetRewardHistory(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetRewardHistoryParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	var rewardType *models.ReferralRewardType
	if params.RewardType != nil {
		rt := models.ReferralRewardType(*params.RewardType)
		rewardType = &rt
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	rewards, total, err := h.service.GetRewardHistory(ctx, playerID, rewardType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get reward history")
		h.respondError(w, http.StatusInternalServerError, "failed to get reward history")
		return
	}

	apiRewards := make([]api.ReferralReward, len(rewards))
	for i, reward := range rewards {
		apiRewards[i] = toAPIReferralReward(&reward)
	}

	response := api.RewardHistoryResponse{
		Rewards: &apiRewards,
		Total:   &total,
		Limit:   &limit,
		Offset:  &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) GetReferralStats(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	stats, err := h.service.GetReferralStats(ctx, playerID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get referral stats")
		h.respondError(w, http.StatusInternalServerError, "failed to get referral stats")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferralStats(stats))
}

func (h *ReferralHandlers) GetPublicReferralStats(w http.ResponseWriter, r *http.Request, code string) {
	ctx := r.Context()

	stats, err := h.service.GetPublicReferralStats(ctx, code)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get public referral stats")
		h.respondError(w, http.StatusNotFound, "referral code not found")
		return
	}

	h.respondJSON(w, http.StatusOK, toAPIReferralStats(stats))
}

func (h *ReferralHandlers) GetReferralLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetReferralLeaderboardParams) {
	ctx := r.Context()

	leaderboardType := models.LeaderboardTypeTopReferrers
	if params.LeaderboardType != nil {
		leaderboardType = models.ReferralLeaderboardType(*params.LeaderboardType)
	}

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 500 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, total, err := h.service.GetLeaderboard(ctx, leaderboardType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get leaderboard")
		return
	}

	apiEntries := make([]api.ReferralLeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPILeaderboardEntry(&entry)
	}

	response := api.ReferralLeaderboardResponse{
		Entries: &apiEntries,
		Total:   &total,
		Limit:   &limit,
		Offset:  &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) GetLeaderboardPosition(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetLeaderboardPositionParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	leaderboardType := models.ReferralLeaderboardType("total")
	if params.LeaderboardType != nil {
		leaderboardType = models.ReferralLeaderboardType(*params.LeaderboardType)
	}

	entry, position, err := h.service.GetLeaderboardPosition(ctx, playerID, leaderboardType)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get leaderboard position")
		h.respondError(w, http.StatusNotFound, "player not found in leaderboard")
		return
	}

	entryValue := toAPILeaderboardEntry(entry)
	response := api.LeaderboardPositionResponse{
		Entry:    &entryValue,
		Position: &position,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) GetReferralEvents(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetReferralEventsParams) {
	ctx := r.Context()
	playerID := uuid.UUID(playerId)

	var eventType *models.ReferralEventType
	if params.EventType != nil {
		et := models.ReferralEventType(*params.EventType)
		eventType = &et
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 100 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	events, total, err := h.service.GetEvents(ctx, playerID, eventType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get referral events")
		h.respondError(w, http.StatusInternalServerError, "failed to get referral events")
		return
	}

	apiEvents := make([]api.ReferralEvent, len(events))
	for i, event := range events {
		apiEvents[i] = toAPIReferralEvent(&event)
	}

	response := api.ReferralEventsResponse{
		Events: &apiEvents,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *ReferralHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	json.NewEncoder(w).Encode(data)
}

func (h *ReferralHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorText := http.StatusText(status)
	errorResponse := api.Error{
		Error:   &errorText,
		Message: &message,
	}
	h.respondJSON(w, status, errorResponse)
}
