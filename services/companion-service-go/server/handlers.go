// Issue: #141886633, #141886669
package server

import (
	"context"
	"encoding/json"
	"net/http"

	"github.com/google/uuid"
	"github.com/necpgame/companion-service-go/models"
	"github.com/necpgame/companion-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
	"github.com/sirupsen/logrus"
)

type CompanionServiceInterface interface {
	GetCompanionType(ctx context.Context, companionTypeID string) (*models.CompanionType, error)
	ListCompanionTypes(ctx context.Context, category *models.CompanionCategory, limit, offset int) (*models.CompanionTypeListResponse, error)
	PurchaseCompanion(ctx context.Context, characterID uuid.UUID, companionTypeID string) (*models.PlayerCompanion, error)
	ListPlayerCompanions(ctx context.Context, characterID uuid.UUID, status *models.CompanionStatus, limit, offset int) (*models.PlayerCompanionListResponse, error)
	GetCompanionDetail(ctx context.Context, companionID uuid.UUID) (*models.CompanionDetailResponse, error)
	SummonCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error
	DismissCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID) error
	RenameCompanion(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, customName string) error
	AddExperience(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, amount int64, source string) error
	UseAbility(ctx context.Context, characterID uuid.UUID, companionID uuid.UUID, abilityID string) error
}

type CompanionHandlers struct {
	service CompanionServiceInterface
	repo    CompanionRepositoryInterface
	logger  *logrus.Logger
}

func NewCompanionHandlers(service CompanionServiceInterface, repo CompanionRepositoryInterface) *CompanionHandlers {
	return &CompanionHandlers{
		service: service,
		repo:    repo,
		logger:  GetLogger(),
	}
}

func (h *CompanionHandlers) GetActiveCompanion(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID) {
	ctx := r.Context()
	characterID := uuid.UUID(playerId)

	companion, err := h.repo.GetActiveCompanion(ctx, characterID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get active companion")
		h.respondError(w, http.StatusInternalServerError, "failed to get active companion")
		return
	}

	if companion == nil {
		h.respondError(w, http.StatusNotFound, "no active companion")
		return
	}

	apiCompanion := toAPIPlayerCompanion(companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}

func (h *CompanionHandlers) GetAvailableCompanions(w http.ResponseWriter, r *http.Request, params api.GetAvailableCompanionsParams) {
	ctx := r.Context()

	var category *models.CompanionCategory
	if params.Category != nil {
		cat := models.CompanionCategory(*params.Category)
		category = &cat
	}

	limit := 50
	if params.Limit != nil && *params.Limit > 0 {
		if *params.Limit > 100 {
			limit = 100
		} else {
			limit = *params.Limit
		}
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response, err := h.service.ListCompanionTypes(ctx, category, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list companion types")
		h.respondError(w, http.StatusInternalServerError, "failed to list companion types")
		return
	}

	apiTypes := make([]api.CompanionType, len(response.Types))
	for i, t := range response.Types {
		apiTypes[i] = toAPICompanionType(&t)
	}

	apiResponse := api.AvailableCompanionsResponse{
		Companions: &apiTypes,
		Total:      intPtr(response.Total),
		Limit:      intPtr(limit),
		Offset:     intPtr(offset),
	}

	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *CompanionHandlers) GetOwnedCompanions(w http.ResponseWriter, r *http.Request, playerId openapi_types.UUID, params api.GetOwnedCompanionsParams) {
	ctx := r.Context()
	characterID := uuid.UUID(playerId)

	var status *models.CompanionStatus

	limit := 50
	if params.Limit != nil && *params.Limit > 0 {
		if *params.Limit > 100 {
			limit = 100
		} else {
			limit = *params.Limit
		}
	}

	offset := 0
	if params.Offset != nil && *params.Offset >= 0 {
		offset = *params.Offset
	}

	response, err := h.service.ListPlayerCompanions(ctx, characterID, status, limit, offset)
	if err != nil {
		h.logger.WithError(err).Error("Failed to list player companions")
		h.respondError(w, http.StatusInternalServerError, "failed to list player companions")
		return
	}

	apiCompanions := make([]api.PlayerCompanion, len(response.Companions))
	for i, c := range response.Companions {
		apiCompanions[i] = toAPIPlayerCompanion(&c)
	}

	apiResponse := api.OwnedCompanionsResponse{
		Companions: &apiCompanions,
		Total:      intPtr(response.Total),
		Limit:      intPtr(limit),
		Offset:     intPtr(offset),
		PlayerId:   &playerId,
	}

	h.respondJSON(w, http.StatusOK, apiResponse)
}

func (h *CompanionHandlers) PurchaseCompanion(w http.ResponseWriter, r *http.Request) {
	ctx := r.Context()

	var req api.PurchaseCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)
	companionTypeID := uuid.UUID(req.CompanionTypeId).String()

	companion, err := h.service.PurchaseCompanion(ctx, characterID, companionTypeID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to purchase companion")
		if err.Error() == "companion type not found" {
			h.respondError(w, http.StatusNotFound, err.Error())
		} else if err.Error() == "companion already owned" {
			h.respondError(w, http.StatusBadRequest, err.Error())
		} else {
			h.respondError(w, http.StatusInternalServerError, "failed to purchase companion")
		}
		return
	}

	apiCompanion := toAPIPlayerCompanion(companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}

func (h *CompanionHandlers) GetCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	detail, err := h.service.GetCompanionDetail(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion detail")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion detail")
		return
	}

	if detail == nil {
		h.respondError(w, http.StatusNotFound, "companion not found")
		return
	}

	apiCompanion := toAPIPlayerCompanion(detail.Companion)
	h.respondJSON(w, http.StatusOK, apiCompanion)
}

func (h *CompanionHandlers) SummonCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.SummonCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	err := h.service.SummonCompanion(ctx, characterID, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to summon companion")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *CompanionHandlers) DismissCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.DismissCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	err := h.service.DismissCompanion(ctx, characterID, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to dismiss companion")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *CompanionHandlers) RenameCompanion(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.RenameCompanionRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	err := h.service.RenameCompanion(ctx, characterID, companionID, req.Name)
	if err != nil {
		h.logger.WithError(err).Error("Failed to rename companion")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *CompanionHandlers) AddCompanionXP(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	var req api.AddXPRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	err := h.service.AddExperience(ctx, characterID, companionID, int64(req.Amount), string(req.Source))
	if err != nil {
		h.logger.WithError(err).Error("Failed to add experience")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	h.respondJSON(w, http.StatusOK, map[string]string{"status": "success"})
}

func (h *CompanionHandlers) UseCompanionAbility(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID, abilityId openapi_types.UUID) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)
	abilityID := uuid.UUID(abilityId).String()

	var req api.UseAbilityRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		h.respondError(w, http.StatusBadRequest, "invalid request body")
		return
	}

	characterID := uuid.UUID(req.PlayerId)

	err := h.service.UseAbility(ctx, characterID, companionID, abilityID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to use ability")
		h.respondError(w, http.StatusBadRequest, err.Error())
		return
	}

	response := api.AbilityUsageResponse{
		Success:   boolPtr(true),
		AbilityId: &abilityId,
	}
	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) GetCompanionAbilities(w http.ResponseWriter, r *http.Request, companionId openapi_types.UUID, params api.GetCompanionAbilitiesParams) {
	ctx := r.Context()
	companionID := uuid.UUID(companionId)

	detail, err := h.service.GetCompanionDetail(ctx, companionID)
	if err != nil {
		h.logger.WithError(err).Error("Failed to get companion detail")
		h.respondError(w, http.StatusInternalServerError, "failed to get companion detail")
		return
	}

	abilities := make([]api.CompanionAbility, len(detail.Abilities))
	for i, a := range detail.Abilities {
		abilities[i] = toAPICompanionAbility(&a)
	}

	response := api.CompanionAbilitiesResponse{
		CompanionId: &companionId,
		Abilities:   &abilities,
	}

	h.respondJSON(w, http.StatusOK, response)
}

func (h *CompanionHandlers) respondJSON(w http.ResponseWriter, status int, data interface{}) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	if err := json.NewEncoder(w).Encode(data); err != nil {
		h.logger.WithError(err).Error("Failed to encode JSON response")
	}
}

func (h *CompanionHandlers) respondError(w http.ResponseWriter, status int, message string) {
	errorResponse := api.Error{
		Error: stringPtr(message),
	}
	h.respondJSON(w, status, errorResponse)
}

func stringPtr(s string) *string {
	return &s
}

func boolPtr(b bool) *bool {
	return &b
}

func intPtr(i int) *int {
	return &i
}

