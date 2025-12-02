package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/quest-core-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type QuestHandlers struct{}

func NewQuestHandlers() *QuestHandlers {
	return &QuestHandlers{}
}

func (h *QuestHandlers) StartQuest(w http.ResponseWriter, r *http.Request) {
	var req api.StartQuestRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	questInstanceId := openapi_types.UUID(uuid.New())
	playerId := openapi_types.UUID(uuid.New())
	now := time.Now()
	state := api.QuestInstanceStateINPROGRESS
	currentObjective := 0

	questInstance := api.QuestInstance{
		Id:               questInstanceId,
		QuestId:          req.QuestId,
		PlayerId:         playerId,
		State:            state,
		StartedAt:        now,
		CurrentObjective: &currentObjective,
		ProgressData:     nil,
		UpdatedAt:        &now,
		CompletedAt:      nil,
	}

	response := api.StartQuestResponse{
		QuestInstance: questInstance,
		Dialogue:      nil,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *QuestHandlers) GetQuest(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	playerId := openapi_types.UUID(uuid.New())
	questDefinitionId := openapi_types.UUID(uuid.New())
	now := time.Now()
	state := api.QuestInstanceStateINPROGRESS
	currentObjective := 1

	questInstance := api.QuestInstance{
		Id:               questId,
		QuestId:          questDefinitionId,
		PlayerId:         playerId,
		State:            state,
		StartedAt:        now.Add(-24 * time.Hour),
		CurrentObjective: &currentObjective,
		ProgressData:     nil,
		UpdatedAt:        &now,
		CompletedAt:      nil,
	}

	respondJSON(w, http.StatusOK, questInstance)
}

func (h *QuestHandlers) GetPlayerQuests(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetPlayerQuestsParams) {
	questId1 := openapi_types.UUID(uuid.New())
	questId2 := openapi_types.UUID(uuid.New())
	questDefinitionId1 := openapi_types.UUID(uuid.New())
	questDefinitionId2 := openapi_types.UUID(uuid.New())
	now := time.Now()
	state1 := api.QuestInstanceStateINPROGRESS
	state2 := api.QuestInstanceStateCOMPLETED
	currentObjective1 := 1
	currentObjective2 := 2
	completedAt2 := now.Add(-1 * time.Hour)

	quests := []api.QuestInstance{
		{
			Id:               questId1,
			QuestId:          questDefinitionId1,
			PlayerId:         playerId,
			State:            state1,
			StartedAt:        now.Add(-48 * time.Hour),
			CurrentObjective: &currentObjective1,
			ProgressData:     nil,
			UpdatedAt:        &now,
			CompletedAt:      nil,
		},
		{
			Id:               questId2,
			QuestId:          questDefinitionId2,
			PlayerId:         playerId,
			State:            state2,
			StartedAt:        now.Add(-72 * time.Hour),
			CurrentObjective: &currentObjective2,
			ProgressData:     nil,
			UpdatedAt:        &completedAt2,
			CompletedAt:      &completedAt2,
		},
	}

	total := len(quests)

	response := api.QuestListResponse{
		Quests: quests,
		Total:  total,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *QuestHandlers) CancelQuest(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	playerId := openapi_types.UUID(uuid.New())
	questDefinitionId := openapi_types.UUID(uuid.New())
	now := time.Now()
	state := api.QuestInstanceStateCANCELLED
	currentObjective := 0

	questInstance := api.QuestInstance{
		Id:               questId,
		QuestId:          questDefinitionId,
		PlayerId:         playerId,
		State:            state,
		StartedAt:        now.Add(-24 * time.Hour),
		CurrentObjective: &currentObjective,
		ProgressData:     nil,
		UpdatedAt:        &now,
		CompletedAt:      nil,
	}

	respondJSON(w, http.StatusOK, questInstance)
}

func (h *QuestHandlers) CompleteQuest(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	var req api.CompleteQuestRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	playerId := openapi_types.UUID(uuid.New())
	questDefinitionId := openapi_types.UUID(uuid.New())
	now := time.Now()
	state := api.QuestInstanceStateCOMPLETED
	currentObjective := 3
	experience := 1000
	currency := 5000

	questInstance := api.QuestInstance{
		Id:               questId,
		QuestId:          questDefinitionId,
		PlayerId:         playerId,
		State:            state,
		StartedAt:        now.Add(-48 * time.Hour),
		CurrentObjective: &currentObjective,
		ProgressData:     nil,
		UpdatedAt:        &now,
		CompletedAt:      &now,
	}

	rewards := api.QuestRewards{
		Experience: &experience,
		Currency:   &currency,
		Items:      nil,
		Reputation: nil,
		Titles:     nil,
	}

	response := api.CompleteQuestResponse{
		QuestInstance: questInstance,
		Rewards:       rewards,
	}

	respondJSON(w, http.StatusOK, response)
}












