package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/social-chat-moderation-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type ChatModerationHandlers struct{}

func NewChatModerationHandlers() *ChatModerationHandlers {
	return &ChatModerationHandlers{}
}

func (h *ChatModerationHandlers) ReportChatMessage(w http.ResponseWriter, r *http.Request) {
	var req api.ReportMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	reportId := openapi_types.UUID(uuid.New())
	success := true

	response := api.ReportResponse{
		ReportId: &reportId,
		Success:  &success,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatModerationHandlers) BanChatUser(w http.ResponseWriter, r *http.Request) {
	var req api.BanUserRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	banId := openapi_types.UUID(uuid.New())
	success := true
	var expiresAt *time.Time
	if req.DurationHours != nil {
		exp := time.Now().Add(time.Duration(*req.DurationHours) * time.Hour)
		expiresAt = &exp
	}

	response := api.BanResponse{
		BanId:     &banId,
		Success:   &success,
		ExpiresAt: expiresAt,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatModerationHandlers) GetChatBans(w http.ResponseWriter, r *http.Request, params api.GetChatBansParams) {
	banId1 := openapi_types.UUID(uuid.New())
	banId2 := openapi_types.UUID(uuid.New())
	playerId1 := openapi_types.UUID(uuid.New())
	playerId2 := openapi_types.UUID(uuid.New())
	bannedBy1 := openapi_types.UUID(uuid.New())
	bannedBy2 := openapi_types.UUID(uuid.New())
	now := time.Now()
	durationHours1 := 24
	durationHours2 := 48
	reason1 := "Spam"
	reason2 := "Toxic behavior"

	bans := []api.ChatBan{
		{
			Id:           &banId1,
			PlayerId:     &playerId1,
			BannedBy:     &bannedBy1,
			Reason:       &reason1,
			DurationHours: &durationHours1,
			CreatedAt:    &now,
			ExpiresAt:    nil,
		},
		{
			Id:           &banId2,
			PlayerId:     &playerId2,
			BannedBy:     &bannedBy2,
			Reason:       &reason2,
			DurationHours: &durationHours2,
			CreatedAt:    &now,
			ExpiresAt:    nil,
		},
	}

	total := len(bans)
	limit := 50
	offset := 0

	if params.Limit != nil {
		limit = int(*params.Limit)
	}
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response := api.BanListResponse{
		Bans:   &bans,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatModerationHandlers) RevokeChatBan(w http.ResponseWriter, r *http.Request, banId api.BanId) {
	success := true

	response := api.RevokeBanResponse{
		BanId:   &banId,
		Success: &success,
	}

	respondJSON(w, http.StatusOK, response)
}

