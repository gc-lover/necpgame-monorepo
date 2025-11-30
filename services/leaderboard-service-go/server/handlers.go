package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/necpgame/leaderboard-service-go/models"
	"github.com/necpgame/leaderboard-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type LeaderboardServiceInterface interface {
	GetGlobalLeaderboard(ctx context.Context, category models.LeaderboardCategory, limit, offset int) ([]models.LeaderboardEntry, error)
	GetSeasonalLeaderboard(ctx context.Context, category models.LeaderboardCategory, seasonID uuid.UUID, limit, offset int) ([]models.LeaderboardEntry, error)
	GetGuildLeaderboard(ctx context.Context, category models.LeaderboardCategory, limit, offset int) ([]models.GuildLeaderboardEntry, error)
	GetClassLeaderboard(ctx context.Context, category models.LeaderboardCategory, classType string, limit, offset int) ([]models.LeaderboardEntry, error)
	GetFriendsLeaderboard(ctx context.Context, category models.LeaderboardCategory, playerID uuid.UUID, limit, offset int) ([]models.LeaderboardEntry, error)
}

type LeaderboardHandlers struct {
	service LeaderboardServiceInterface
	logger  *logrus.Logger
}

func NewLeaderboardHandlers(service LeaderboardServiceInterface) *LeaderboardHandlers {
	return &LeaderboardHandlers{
		service: service,
		logger:  GetLogger(),
	}
}

// GetGlobalLeaderboard implements api.ServerInterface.
func (h *LeaderboardHandlers) GetGlobalLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetGlobalLeaderboardParams) {
	ctx := r.Context()

	category := models.LeaderboardCategory(params.Metric)

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, err := h.service.GetGlobalLeaderboard(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get global leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get global leaderboard")
		return
	}

	apiEntries := make([]api.LeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPILeaderboardEntry(&entry)
	}

	metric := string(params.Metric)
	response := api.LeaderboardResponse{
		Entries: &apiEntries,
		Metric:  &metric,
		Total:   intPtr(len(apiEntries)),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetSeasonalLeaderboard implements api.ServerInterface.
func (h *LeaderboardHandlers) GetSeasonalLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetSeasonalLeaderboardParams) {
	ctx := r.Context()

	category := models.LeaderboardCategory(params.Metric)
	
	var seasonID uuid.UUID
	if params.SeasonId != nil {
		seasonID = uuid.UUID(*params.SeasonId)
	}

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, err := h.service.GetSeasonalLeaderboard(ctx, category, seasonID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get seasonal leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get seasonal leaderboard")
		return
	}

	apiEntries := make([]api.LeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPILeaderboardEntry(&entry)
	}

	metric := string(params.Metric)
	response := api.LeaderboardResponse{
		Entries:  &apiEntries,
		Metric:   &metric,
		SeasonId: params.SeasonId,
		Total:    intPtr(len(apiEntries)),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetGuildLeaderboard implements api.ServerInterface.
func (h *LeaderboardHandlers) GetGuildLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetGuildLeaderboardParams) {
	ctx := r.Context()

	category := models.LeaderboardCategory(params.Metric)

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, err := h.service.GetGuildLeaderboard(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get guild leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get guild leaderboard")
		return
	}

	apiEntries := make([]api.GuildLeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPIGuildLeaderboardEntry(&entry)
	}

	metric := string(params.Metric)
	response := api.GuildLeaderboardResponse{
		Entries: &apiEntries,
		Metric:  &metric,
		Total:   intPtr(len(apiEntries)),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetClassLeaderboard implements api.ServerInterface.
func (h *LeaderboardHandlers) GetClassLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetClassLeaderboardParams) {
	ctx := r.Context()

	category := models.LeaderboardCategory(params.Metric)
	classType := params.ClassName

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, err := h.service.GetClassLeaderboard(ctx, category, classType, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get class leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get class leaderboard")
		return
	}

	apiEntries := make([]api.LeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPILeaderboardEntry(&entry)
	}

	metric := string(params.Metric)
	response := api.LeaderboardResponse{
		Entries: &apiEntries,
		Metric:  &metric,
		Total:   intPtr(len(apiEntries)),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// GetFriendsLeaderboard implements api.ServerInterface.
func (h *LeaderboardHandlers) GetFriendsLeaderboard(w http.ResponseWriter, r *http.Request, params api.GetFriendsLeaderboardParams) {
	ctx := r.Context()

	category := models.LeaderboardCategory(params.Metric)
	playerID := uuid.UUID(params.PlayerId)

	limit := 100
	if params.Limit != nil && *params.Limit > 0 && *params.Limit <= 1000 {
		limit = *params.Limit
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	entries, err := h.service.GetFriendsLeaderboard(ctx, category, playerID, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get friends leaderboard")
		h.respondError(w, http.StatusInternalServerError, "failed to get friends leaderboard")
		return
	}

	apiEntries := make([]api.LeaderboardEntry, len(entries))
	for i, entry := range entries {
		apiEntries[i] = toAPILeaderboardEntry(&entry)
	}

	metric := string(params.Metric)
	response := api.LeaderboardResponse{
		Entries: &apiEntries,
		Metric:  &metric,
		Total:   intPtr(len(apiEntries)),
	}

	h.respondJSON(w, http.StatusOK, response)
}

// Issue: #141886468
func (h *LeaderboardHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *LeaderboardHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error:   http.StatusText(status),
		Message: message,
	}
	h.respondJSON(w, status, errorResponse)
}

func stringPtr(s string) *string {
	return &s
}

func intPtr(i int) *int {
	return &i
}

func timePtr(t time.Time) *time.Time {
	return &t
}

func toAPILeaderboardEntry(entry *models.LeaderboardEntry) api.LeaderboardEntry {
	if entry == nil {
		return api.LeaderboardEntry{}
	}

	apiCharacterID := openapi_types.UUID(entry.CharacterID)
	score := float32(entry.Score)

	return api.LeaderboardEntry{
		CharacterId:   &apiCharacterID,
		CharacterName: &entry.CharacterName,
		Rank:          &entry.Rank,
		Score:         &score,
	}
}

func toAPIGuildLeaderboardEntry(entry *models.GuildLeaderboardEntry) api.GuildLeaderboardEntry {
	if entry == nil {
		return api.GuildLeaderboardEntry{}
	}

	apiGuildID := openapi_types.UUID(entry.GuildID)
	score := float32(entry.Score)

	return api.GuildLeaderboardEntry{
		GuildId:     &apiGuildID,
		GuildName:   &entry.GuildName,
		Rank:        &entry.Rank,
		Score:       &score,
		MemberCount: &entry.MemberCount,
	}
}

