package server

import (
	"encoding/json"
	"net/http"
	"time"

	"github.com/necpgame/hacking-core-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/google/uuid"
)

type HackingHandlers struct{}

func NewHackingHandlers() *HackingHandlers {
	return &HackingHandlers{}
}

func (h *HackingHandlers) InitiateHack(w http.ResponseWriter, r *http.Request) {
	var req api.InitiateHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	hackId := openapi_types.UUID(uuid.New())
	now := time.Now()
	difficulty := 5
	if req.Difficulty != nil {
		difficulty = *req.Difficulty
	}
	progress := 0
	targetType := api.HackSessionTargetType(req.TargetType)
	status := api.HackSessionStatusInitiated

	response := api.HackSession{
		Id:          &hackId,
		CharacterId: &req.CharacterId,
		TargetId:    &req.TargetId,
		TargetType:  &targetType,
		Difficulty:  &difficulty,
		Progress:    &progress,
		Status:      &status,
		CreatedAt:   &now,
		UpdatedAt:   &now,
	}

	respondJSON(w, http.StatusCreated, response)
}

func (h *HackingHandlers) CancelHack(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	status := api.HackSessionStatusCancelled
	now := time.Now()
	response := api.HackSession{
		Id:        &hackId,
		Status:    &status,
		UpdatedAt: &now,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ExecuteHack(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	var req api.ExecuteHackRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	now := time.Now()
	status := api.HackSessionStatusInProgress
	if req.Action == api.Complete {
		status = api.HackSessionStatusCompleted
	}

	response := api.HackSession{
		Id:        &hackId,
		Status:    &status,
		UpdatedAt: &now,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) RetryHackStep(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	success := true
	message := "Step retried successfully"
	stepType := "retry"
	progress := 50

	response := api.HackStepResult{
		Success:  &success,
		Message:  &message,
		StepType: &stepType,
		Progress: &progress,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetHackProcessStatus(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	status := api.HackProcessStatusStatusInProgress
	currentStep := "scanning"
	progress := 50
	stepsCompleted := 2
	totalSteps := 4

	response := api.HackProcessStatus{
		HackId:        &hackId,
		Status:         &status,
		CurrentStep:    &currentStep,
		Progress:       &progress,
		StepsCompleted: &stepsCompleted,
		TotalSteps:     &totalSteps,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ExecuteHackStep(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	var req api.HackStepRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		respondError(w, http.StatusBadRequest, err, "Invalid request body")
		return
	}

	success := true
	message := "Step executed successfully"
	stepType := string(req.StepType)
	progress := 75

	response := api.HackStepResult{
		Success:  &success,
		Message:  &message,
		StepType: &stepType,
		Progress: &progress,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetHackResult(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	success := true
	resultType := api.HackResultResultTypeAccess
	data := map[string]interface{}{
		"access_level": "read",
		"target_id":    hackId.String(),
	}

	response := api.HackResult{
		HackId:     &hackId,
		Success:    &success,
		ResultType: &resultType,
		Data:       &data,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) ApplyHackResult(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	status := "applied"
	response := api.SuccessResponse{
		Status: &status,
	}

	respondJSON(w, http.StatusOK, response)
}

func (h *HackingHandlers) GetHackStatus(w http.ResponseWriter, r *http.Request, hackId openapi_types.UUID) {
	now := time.Now()
	status := api.HackSessionStatusInProgress
	progress := 60
	difficulty := 5

	response := api.HackSession{
		Id:        &hackId,
		Status:    &status,
		Progress:  &progress,
		Difficulty: &difficulty,
		UpdatedAt: &now,
	}

	respondJSON(w, http.StatusOK, response)
}

