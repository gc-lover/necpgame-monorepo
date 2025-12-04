package server

import (
	"net/http"
	"time"

	"github.com/necpgame/quest-rewards-events-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type QuestRewardsEventsHandlers struct{}

func NewQuestRewardsEventsHandlers() *QuestRewardsEventsHandlers {
	return &QuestRewardsEventsHandlers{}
}

func (h *QuestRewardsEventsHandlers) GetQuestRewards(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	experience := 1000
	currency := 5000
	itemId1 := openapi_types.UUID(uuid.New())
	itemId2 := openapi_types.UUID(uuid.New())
	quantity1 := 1
	quantity2 := 2
	items := []struct {
		ItemId   *openapi_types.UUID `json:"item_id,omitempty"`
		Quantity *int                 `json:"quantity,omitempty"`
	}{
		{
			ItemId:   &itemId1,
			Quantity: &quantity1,
		},
		{
			ItemId:   &itemId2,
			Quantity: &quantity2,
		},
	}

	rewards := api.QuestRewards{
		Experience: &experience,
		Currency:   &currency,
		Items:      &items,
		Reputation: nil,
		Titles:     nil,
	}

	respondJSON(w, http.StatusOK, rewards)
}

func (h *QuestRewardsEventsHandlers) DistributeQuestRewards(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	experience := 1000
	currency := 5000
	success := true
	distributionDetails := map[string]interface{}{
		"experience_sent": true,
		"currency_sent":   true,
		"items_sent":      true,
	}

	itemId1 := openapi_types.UUID(uuid.New())
	itemId2 := openapi_types.UUID(uuid.New())
	quantity1 := 1
	quantity2 := 2
	items := []struct {
		ItemId   *openapi_types.UUID `json:"item_id,omitempty"`
		Quantity *int                 `json:"quantity,omitempty"`
	}{
		{
			ItemId:   &itemId1,
			Quantity: &quantity1,
		},
		{
			ItemId:   &itemId2,
			Quantity: &quantity2,
		},
	}

	rewards := api.QuestRewards{
		Experience: &experience,
		Currency:   &currency,
		Items:      &items,
		Reputation: nil,
		Titles:     nil,
	}

	response := map[string]interface{}{
		"success":             success,
		"rewards":             rewards,
		"distribution_details": distributionDetails,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *QuestRewardsEventsHandlers) GetQuestEvents(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID, params api.GetQuestEventsParams) {
	eventId1 := openapi_types.UUID(uuid.New())
	eventId2 := openapi_types.UUID(uuid.New())
	eventId3 := openapi_types.UUID(uuid.New())
	questInstanceId := openapi_types.UUID(uuid.New())
	now := time.Now()
	eventType1 := api.QuestEventEventTypeStarted
	eventType2 := api.QuestEventEventTypeObjectiveCompleted
	eventType3 := api.QuestEventEventTypeCompleted
	eventData1 := map[string]interface{}{
		"objective_index": 0,
	}
	eventData2 := map[string]interface{}{
		"objective_index": 1,
	}
	eventData3 := map[string]interface{}{
		"completion_time": now.Unix(),
	}

	events := []api.QuestEvent{
		{
			Id:              &eventId1,
			QuestInstanceId: &questInstanceId,
			EventType:       eventType1,
			Timestamp:       now.Add(-48 * time.Hour),
			EventData:       &eventData1,
		},
		{
			Id:              &eventId2,
			QuestInstanceId: &questInstanceId,
			EventType:       eventType2,
			Timestamp:       now.Add(-24 * time.Hour),
			EventData:       &eventData2,
		},
		{
			Id:              &eventId3,
			QuestInstanceId: &questInstanceId,
			EventType:       eventType3,
			Timestamp:       now,
			EventData:       &eventData3,
		},
	}

	total := len(events)

	response := api.QuestEventsResponse{
		Events: events,
		Total:  total,
	}

	respondJSON(w, http.StatusOK, response)
}

















