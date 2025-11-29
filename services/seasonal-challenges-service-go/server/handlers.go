package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/seasonal-challenges-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type SeasonalChallengesHandlers struct {
	logger *logrus.Logger
}

func NewSeasonalChallengesHandlers() *SeasonalChallengesHandlers {
	return &SeasonalChallengesHandlers{
		logger: GetLogger(),
	}
}

func (h *SeasonalChallengesHandlers) GetCurrentSeason(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("GetCurrentSeason request")

	now := time.Now()
	seasonId := openapi_types.UUID(uuid.MustParse("00000000-0000-0000-0000-000000000001"))
	seasonName := "Season 1"
	startDate := now.AddDate(0, -1, 0)
	endDate := now.AddDate(0, 1, 0)
	status := api.Active
	theme := "Cyber Winter"
	description := "First season of NECPGAME"

	response := api.Season{
		Id:              &seasonId,
		Name:            &seasonName,
		StartDate:       &startDate,
		EndDate:         &endDate,
		Status:          &status,
		Theme:           &theme,
		Description:     &description,
		SeasonalAffixId: nil,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) GetSeasonChallenges(w http.ResponseWriter, r *http.Request, seasonId openapi_types.UUID, params api.GetSeasonChallengesParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("season_id", seasonId).Info("GetSeasonChallenges request")

	challenges := []api.SeasonalChallenge{}
	total := 0

	response := struct {
		Challenges []api.SeasonalChallenge `json:"challenges"`
		Total      int                      `json:"total"`
	}{
		Challenges: challenges,
		Total:      total,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) GetSeasonRewards(w http.ResponseWriter, r *http.Request, seasonId openapi_types.UUID, params api.GetSeasonRewardsParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("season_id", seasonId).Info("GetSeasonRewards request")

	rewards := []api.SeasonalReward{}
	total := 0

	response := struct {
		Rewards []api.SeasonalReward `json:"rewards"`
		Total   int                   `json:"total"`
	}{
		Rewards: rewards,
		Total:   total,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) GetActiveChallenges(w http.ResponseWriter, r *http.Request, params api.GetActiveChallengesParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("player_id", params.PlayerId).Info("GetActiveChallenges request")

	challenges := []api.PlayerSeasonalChallenge{}
	total := 0

	response := struct {
		Challenges []api.PlayerSeasonalChallenge `json:"challenges"`
		Total      int                             `json:"total"`
	}{
		Challenges: challenges,
		Total:      total,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) CompleteSeasonalChallenge(w http.ResponseWriter, r *http.Request, challengeId openapi_types.UUID) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithField("challenge_id", challengeId).Info("CompleteSeasonalChallenge request")

	var requestBody struct {
		CompletionData *map[string]interface{} `json:"completion_data,omitempty"`
	}

	if r.Body != nil {
		if err := json.NewDecoder(r.Body).Decode(&requestBody); err != nil {
			h.respondError(w, http.StatusBadRequest, "Invalid request body")
			return
		}
	}

	now := time.Now()
	completed := true
	currencyEarned := 100

	response := api.ChallengeCompletionResult{
		ChallengeId:    &challengeId,
		Completed:      &completed,
		CompletedAt:    &now,
		CurrencyEarned: &currencyEarned,
		Rewards:        nil,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) GetSeasonalCurrencyBalance(w http.ResponseWriter, r *http.Request, params api.GetSeasonalCurrencyBalanceParams) {
	ctx := r.Context()
	_ = ctx

	h.logger.WithFields(logrus.Fields{
		"player_id": params.PlayerId,
		"season_id": params.SeasonId,
	}).Info("GetSeasonalCurrencyBalance request")

	currencyAmount := 1000
	maxCurrency := 10000
	seasonId := openapi_types.UUID(uuid.MustParse("00000000-0000-0000-0000-000000000001"))
	if params.SeasonId != nil {
		seasonId = *params.SeasonId
	}

	response := api.SeasonalCurrencyBalance{
		CurrencyAmount: &currencyAmount,
		MaxCurrency:    &maxCurrency,
		SeasonId:       &seasonId,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) ExchangeSeasonalCurrency(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()
	_ = ctx

	h.logger.Info("ExchangeSeasonalCurrency request")

	var request api.CurrencyExchangeRequest
	if err := json.NewDecoder(r.Body).Decode(&request); err != nil {
		h.respondError(w, http.StatusBadRequest, "Invalid request body")
		return
	}

	now := time.Now()
	exchangeId := openapi_types.UUID(uuid.MustParse("00000000-0000-0000-0000-000000000002"))
	currencySpent := 500
	remainingCurrency := 500
	quantity := request.Quantity
	rewardType := "item"

	response := api.CurrencyExchangeResult{
		ExchangeId:         &exchangeId,
		ExchangedAt:        &now,
		CurrencySpent:      &currencySpent,
		RemainingCurrency:  &remainingCurrency,
		Quantity:           &quantity,
		RewardId:           &request.RewardId,
		RewardReceived: &struct {
			Quantity   *int                `json:"quantity,omitempty"`
			RewardId   *openapi_types.UUID `json:"reward_id,omitempty"`
			RewardType *string             `json:"reward_type,omitempty"`
		}{
			Quantity:   &quantity,
			RewardId:   &request.RewardId,
			RewardType: &rewardType,
		},
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *SeasonalChallengesHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *SeasonalChallengesHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
		Code:    nil,
		Details: nil,
	}
	h.respondJSON(w, status, errorResponse)
}

