// Issue: #1604
package server

import (
	"context"
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/quest-state-dialogue-service-go/pkg/api"
)

// Context timeout constants (Issue #1604)
const (
	DBTimeout    = 50 * time.Millisecond
	CacheTimeout = 10 * time.Millisecond
)

type Handlers struct{}

func (h *Handlers) GetQuestState(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	response := api.QuestState{
		State:            api.QuestStateStateINPROGRESS,
		CurrentObjective: 1,
		ProgressData: &map[string]interface{}{
			"progress": 50,
		},
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) UpdateQuestState(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	var req api.UpdateStateRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	response := api.QuestState{
		State:            api.QuestStateState(req.State),
		CurrentObjective: 1,
		ProgressData:    req.ProgressData,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetQuestDialogue(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	response := api.DialogueNode{
		NodeId: "start",
		Text:   "Welcome to the quest!",
		Speaker: func() *string { s := "NPC"; return &s }(),
		Choices: &[]api.DialogueChoice{
			{
				ChoiceId:   "choice_1",
				Text:       "Accept the quest",
				NextNodeId: func() *string { s := "node_2"; return &s }(),
			},
			{
				ChoiceId:   "choice_2",
				Text:       "Decline",
				NextNodeId: func() *string { s := "node_3"; return &s }(),
			},
		},
		SkillChecks: &[]api.SkillCheck{
			{
				CheckTarget:    "intelligence",
				CheckType:      api.Attribute,
				RequiredValue:  10,
			},
		},
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) MakeDialogueChoice(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	var req api.DialogueChoiceRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	nextNodeId := "node_2"
	response := api.DialogueNode{
		NodeId: nextNodeId,
		Text:   "You made a choice!",
		Speaker: func() *string { s := "NPC"; return &s }(),
		Choices: &[]api.DialogueChoice{
			{
				ChoiceId:   "choice_3",
				Text:       "Continue",
				NextNodeId: func() *string { s := "node_4"; return &s }(),
			},
		},
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetDialogueHistory(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID, params api.GetDialogueHistoryParams) {
	ctx, cancel := context.WithTimeout(r.Context(), DBTimeout)
	defer cancel()
	_ = ctx // Will be used when DB operations are implemented

	now := time.Now()
	response := api.DialogueHistory{
		QuestInstanceId: questId,
		History: []struct {
			ChoiceMade *string    `json:"choice_made"`
			NodeId     *string    `json:"node_id,omitempty"`
			Speaker    *string    `json:"speaker,omitempty"`
			Text       *string    `json:"text,omitempty"`
			Timestamp  *time.Time `json:"timestamp,omitempty"`
		}{
			{
				NodeId:    func() *string { s := "start"; return &s }(),
				Speaker:   func() *string { s := "NPC"; return &s }(),
				Text:      func() *string { s := "Welcome to the quest!"; return &s }(),
				Timestamp: &now,
			},
			{
				ChoiceMade: func() *string { s := "choice_1"; return &s }(),
				Timestamp: func() *time.Time { t := now.Add(1 * time.Minute); return &t }(),
			},
		},
	}
	respondJSON(w, http.StatusOK, response)
}

