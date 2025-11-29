package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/google/uuid"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/necpgame/quest-skill-checks-conditions-service-go/pkg/api"
)

type Handlers struct{}

type CheckQuestConditionsResponse struct {
	AllConditionsMet bool                          `json:"all_conditions_met"`
	Conditions       []QuestConditionCheckResult `json:"conditions"`
}

type QuestConditionCheckResult struct {
	ConditionType  string `json:"condition_type"`
	ConditionTarget string `json:"condition_target"`
	RequiredValue  int    `json:"required_value"`
	ActualValue    int    `json:"actual_value"`
	Met            bool   `json:"met"`
}

func (h *Handlers) CheckQuestConditions(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	response := CheckQuestConditionsResponse{
		AllConditionsMet: true,
		Conditions: []QuestConditionCheckResult{
			{
				ConditionType:  "level",
				ConditionTarget: "character",
				RequiredValue:  10,
				ActualValue:    15,
				Met:            true,
			},
		},
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetQuestRequirements(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	response := api.QuestRequirements{
		Level: func() *int { v := 10; return &v }(),
		Items: &[]struct {
			ItemId   *openapi_types.UUID `json:"item_id,omitempty"`
			Quantity *int                `json:"quantity,omitempty"`
		}{
			{
				ItemId:   func() *openapi_types.UUID { id := openapi_types.UUID(uuid.New()); return &id }(),
				Quantity: func() *int { v := 5; return &v }(),
			},
		},
		QuestPrerequisites: &[]openapi_types.UUID{},
		Reputation:         &map[string]interface{}{},
		Location:           &map[string]interface{}{},
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) PerformSkillCheck(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID) {
	var req api.SkillCheckRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	now := time.Now()
	resultId := openapi_types.UUID(uuid.New())
	questInstanceId := questId

	response := api.SkillCheckResult{
		Id:              &resultId,
		QuestInstanceId:  &questInstanceId,
		CheckTarget:      req.CheckTarget,
		CheckType:        api.SkillCheckResultCheckType(req.CheckType),
		RequiredValue:    15,
		ActualValue:      20,
		Passed:           true,
		CheckedAt:        &now,
	}
	respondJSON(w, http.StatusOK, response)
}

func (h *Handlers) GetSkillCheckHistory(w http.ResponseWriter, r *http.Request, questId openapi_types.UUID, params api.GetSkillCheckHistoryParams) {
	now := time.Now()
	resultId := openapi_types.UUID(uuid.New())
	questInstanceId := questId

	skillChecks := []api.SkillCheckResult{
		{
			Id:             &resultId,
			QuestInstanceId: &questInstanceId,
			CheckTarget:     "hacking",
			CheckType:       api.SkillCheckResultCheckTypeSkill,
			RequiredValue:   15,
			ActualValue:     20,
			Passed:          true,
			CheckedAt:       &now,
		},
	}

	response := api.SkillChecksResponse{
		SkillChecks: skillChecks,
		Total:       len(skillChecks),
	}
	respondJSON(w, http.StatusOK, response)
}

