package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/social-chat-format-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type ChatFormatHandlers struct{}

func NewChatFormatHandlers() *ChatFormatHandlers {
	return &ChatFormatHandlers{}
}

func (h *ChatFormatHandlers) FormatChatMessage(w http.ResponseWriter, r *http.Request) {
	var req api.FormatMessageRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	original := req.Content
	formatted := req.Content
	mentions := []openapi_types.UUID{}

	response := api.FormattedMessageResponse{
		Original:  &original,
		Formatted: &formatted,
		Mentions:  &mentions,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *ChatFormatHandlers) GetChatEvents(w http.ResponseWriter, r *http.Request, params api.GetChatEventsParams) {
	eventId1 := openapi_types.UUID(uuid.New())
	eventId2 := openapi_types.UUID(uuid.New())
	channelId := openapi_types.UUID(uuid.New())
	eventType1 := api.ChatEventEventTypeChatMessageSent
	eventType2 := api.ChatEventEventTypeChatMessageReceived
	now := time.Now()
	eventData1 := map[string]interface{}{
		"message_id": uuid.New().String(),
		"player_id":  uuid.New().String(),
	}
	eventData2 := map[string]interface{}{
		"message_id": uuid.New().String(),
		"player_id":  uuid.New().String(),
	}

	events := []api.ChatEvent{
		{
			Id:        &eventId1,
			EventType: &eventType1,
			ChannelId: &channelId,
			EventData: &eventData1,
			CreatedAt: &now,
		},
		{
			Id:        &eventId2,
			EventType: &eventType2,
			ChannelId: &channelId,
			EventData: &eventData2,
			CreatedAt: &now,
		},
	}

	total := len(events)
	limit := 50
	offset := 0

	if params.Limit != nil {
		limit = int(*params.Limit)
	}
	if params.Offset != nil {
		offset = int(*params.Offset)
	}

	response := api.ChatEventsResponse{
		Events: &events,
		Total:  &total,
		Limit:  &limit,
		Offset: &offset,
	}

	respondJSON(w, http.StatusOK, response)
}












