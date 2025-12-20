// Package server Issue: #1597, #1607
// ogen handlers - TYPED responses (no interface{} boxing!)
package server

import (
	"context"
	"time"

	"github.com/gc-lover/necpgame-monorepo/services/quest-state-dialogue-service-go/pkg/api"
	"github.com/go-faster/jx"
)

const (
	DBTimeout = 50 * time.Millisecond
)

// Handlers implements api.Handler interface (ogen typed handlers!)
type Handlers struct{}

// GetQuestState - TYPED response!
func (h *Handlers) GetQuestState(ctx context.Context, _ api.GetQuestStateParams) (api.GetQuestStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	// Progress data as jx.Raw map
	progressJSON := jx.Raw(`50`)
	progressData := api.QuestStateProgressData{
		"progress": progressJSON,
	}

	response := &api.QuestState{
		State:            api.QuestStateStateINPROGRESS,
		CurrentObjective: 1,
		ProgressData:     api.NewOptQuestStateProgressData(progressData),
	}

	return response, nil
}

// UpdateQuestState - TYPED response!
func (h *Handlers) UpdateQuestState(ctx context.Context, req *api.UpdateStateRequest, _ api.UpdateQuestStateParams) (api.UpdateQuestStateRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	// Convert UpdateStateRequestProgressData to QuestStateProgressData
	var progressData api.QuestStateProgressData
	if req.ProgressData.Set {
		progressData = api.QuestStateProgressData(req.ProgressData.Value)
	} else {
		progressData = api.QuestStateProgressData{}
	}

	// Convert UpdateStateRequestState to QuestStateState
	var state api.QuestStateState
	switch req.State {
	case api.UpdateStateRequestStateINPROGRESS:
		state = api.QuestStateStateINPROGRESS
	case api.UpdateStateRequestStateCOMPLETED:
		state = api.QuestStateStateCOMPLETED
	case api.UpdateStateRequestStateFAILED:
		state = api.QuestStateStateFAILED
	case api.UpdateStateRequestStateCANCELLED:
		state = api.QuestStateStateCANCELLED
	default:
		state = api.QuestStateStateINPROGRESS
	}

	response := &api.QuestState{
		State:            state,
		CurrentObjective: 1,
		ProgressData:     api.NewOptQuestStateProgressData(progressData),
	}

	return response, nil
}

// GetQuestDialogue - TYPED response!
func (h *Handlers) GetQuestDialogue(ctx context.Context, _ api.GetQuestDialogueParams) (api.GetQuestDialogueRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	speaker := "NPC"
	nextNodeId1 := "node_2"
	nextNodeId2 := "node_3"

	response := &api.DialogueNode{
		NodeID:  "start",
		Text:    "Welcome to the quest!",
		Speaker: api.NewOptString(speaker),
		Choices: []api.DialogueChoice{
			{
				ChoiceID:   "choice_1",
				Text:       "Accept the quest",
				NextNodeID: api.NewOptString(nextNodeId1),
			},
			{
				ChoiceID:   "choice_2",
				Text:       "Decline",
				NextNodeID: api.NewOptString(nextNodeId2),
			},
		},
		SkillChecks: []api.SkillCheck{
			{
				CheckTarget:   "intelligence",
				CheckType:     api.SkillCheckCheckTypeAttribute,
				RequiredValue: 10,
			},
		},
	}

	return response, nil
}

// MakeDialogueChoice - TYPED response!
func (h *Handlers) MakeDialogueChoice(ctx context.Context, _ *api.DialogueChoiceRequest, _ api.MakeDialogueChoiceParams) (api.MakeDialogueChoiceRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	speaker := "NPC"
	nextNodeId := "node_4"

	response := &api.DialogueNode{
		NodeID:  "node_2",
		Text:    "You made a choice!",
		Speaker: api.NewOptString(speaker),
		Choices: []api.DialogueChoice{
			{
				ChoiceID:   "choice_3",
				Text:       "Continue",
				NextNodeID: api.NewOptString(nextNodeId),
			},
		},
	}

	return response, nil
}

// GetDialogueHistory - TYPED response!
func (h *Handlers) GetDialogueHistory(ctx context.Context, params api.GetDialogueHistoryParams) (api.GetDialogueHistoryRes, error) {
	ctx, cancel := context.WithTimeout(ctx, DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	now := time.Now()
	nodeId1 := "start"
	speaker1 := "NPC"
	text1 := "Welcome to the quest!"
	choiceMade1 := "choice_1"
	timestamp2 := now.Add(1 * time.Minute)

	response := &api.DialogueHistory{
		QuestInstanceID: params.QuestID,
		History: []api.DialogueHistoryHistoryItem{
			{
				NodeID:    api.NewOptString(nodeId1),
				Speaker:   api.NewOptString(speaker1),
				Text:      api.NewOptString(text1),
				Timestamp: api.NewOptDateTime(now),
			},
			{
				ChoiceMade: api.NewOptNilString(choiceMade1),
				Timestamp:  api.NewOptDateTime(timestamp2),
			},
		},
	}

	return response, nil
}
