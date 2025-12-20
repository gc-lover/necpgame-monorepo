// Package server Issue: #1597, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"strconv"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-rewards-events-service-go/pkg/api"
	"github.com/go-faster/jx"
	"github.com/google/uuid"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// QuestRewardsEventsHandlers implements api.Handler interface (ogen typed handlers!)
type QuestRewardsEventsHandlers struct{}

func NewQuestRewardsEventsHandlers() *QuestRewardsEventsHandlers {
	return &QuestRewardsEventsHandlers{}
}

// GetQuestRewards - TYPED response!
func (h *QuestRewardsEventsHandlers) GetQuestRewards(ctx context.Context, _ api.GetQuestRewardsParams) (api.GetQuestRewardsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	experience := 1000
	currency := 5000
	itemId1 := uuid.New()
	itemId2 := uuid.New()
	quantity1 := 1
	quantity2 := 2

	items := []api.QuestRewardsItemsItem{
		{
			ItemID:   api.NewOptUUID(itemId1),
			Quantity: api.NewOptInt(quantity1),
		},
		{
			ItemID:   api.NewOptUUID(itemId2),
			Quantity: api.NewOptInt(quantity2),
		},
	}

	rewards := &api.QuestRewards{
		Experience: api.NewOptInt(experience),
		Currency:   api.NewOptInt(currency),
		Items:      items,
		Reputation: api.OptQuestRewardsReputation{},
		Titles:     []string{},
	}

	return rewards, nil
}

// DistributeQuestRewards - TYPED response!
func (h *QuestRewardsEventsHandlers) DistributeQuestRewards(ctx context.Context, _ api.DistributeQuestRewardsParams) (api.DistributeQuestRewardsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	experience := 1000
	currency := 5000
	success := true

	// Distribution details as jx.Raw map
	detailsJSON := jx.Raw(`{"experience_sent":true,"currency_sent":true,"items_sent":true}`)
	distributionDetails := api.DistributeQuestRewardsOKDistributionDetails{
		"experience_sent": detailsJSON,
		"currency_sent":   detailsJSON,
		"items_sent":      detailsJSON,
	}

	itemId1 := uuid.New()
	itemId2 := uuid.New()
	quantity1 := 1
	quantity2 := 2

	items := []api.QuestRewardsItemsItem{
		{
			ItemID:   api.NewOptUUID(itemId1),
			Quantity: api.NewOptInt(quantity1),
		},
		{
			ItemID:   api.NewOptUUID(itemId2),
			Quantity: api.NewOptInt(quantity2),
		},
	}

	rewards := api.QuestRewards{
		Experience: api.NewOptInt(experience),
		Currency:   api.NewOptInt(currency),
		Items:      items,
		Reputation: api.OptQuestRewardsReputation{},
		Titles:     []string{},
	}

	response := &api.DistributeQuestRewardsOK{
		Success:             true,
		Rewards:             rewards,
		DistributionDetails: api.NewOptDistributeQuestRewardsOKDistributionDetails(distributionDetails),
	}

	return response, nil
}

// GetQuestEvents - TYPED response!
func (h *QuestRewardsEventsHandlers) GetQuestEvents(ctx context.Context, _ api.GetQuestEventsParams) (api.GetQuestEventsRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	eventId1 := uuid.New()
	eventId2 := uuid.New()
	eventId3 := uuid.New()
	questInstanceId := uuid.New()
	now := time.Now()

	// Event data as jx.Raw
	eventData1JSON := jx.Raw(`{"objective_index":0}`)
	eventData2JSON := jx.Raw(`{"objective_index":1}`)
	eventData3JSON := jx.Raw(`{"completion_time":` + strconv.FormatInt(now.Unix(), 10) + `}`)

	eventData1 := api.QuestEventEventData{
		"objective_index": eventData1JSON,
	}
	eventData2 := api.QuestEventEventData{
		"objective_index": eventData2JSON,
	}
	eventData3 := api.QuestEventEventData{
		"completion_time": eventData3JSON,
	}

	events := []api.QuestEvent{
		{
			ID:              api.NewOptUUID(eventId1),
			QuestInstanceID: api.NewOptUUID(questInstanceId),
			EventType:       api.QuestEventEventTypeStarted,
			Timestamp:       now.Add(-48 * time.Hour),
			EventData:       api.NewOptQuestEventEventData(eventData1),
		},
		{
			ID:              api.NewOptUUID(eventId2),
			QuestInstanceID: api.NewOptUUID(questInstanceId),
			EventType:       api.QuestEventEventTypeObjectiveCompleted,
			Timestamp:       now.Add(-24 * time.Hour),
			EventData:       api.NewOptQuestEventEventData(eventData2),
		},
		{
			ID:              api.NewOptUUID(eventId3),
			QuestInstanceID: api.NewOptUUID(questInstanceId),
			EventType:       api.QuestEventEventTypeCompleted,
			Timestamp:       now,
			EventData:       api.NewOptQuestEventEventData(eventData3),
		},
	}

	total := len(events)

	response := &api.QuestEventsResponse{
		Events: events,
		Total:  total,
	}

	return response, nil
}
