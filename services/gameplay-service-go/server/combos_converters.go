package server

import (
	"github.com/google/uuid"
	"github.com/necpgame/gameplay-service-go/pkg/combosapi"
	"github.com/necpgame/gameplay-service-go/models"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func convertLoadoutToAPI(loadout *models.ComboLoadout) combosapi.ComboLoadout {
	id := openapi_types.UUID(loadout.ID)
	characterID := openapi_types.UUID(loadout.CharacterID)
	createdAt := loadout.CreatedAt
	updatedAt := loadout.UpdatedAt

	activeCombos := make([]openapi_types.UUID, len(loadout.ActiveCombos))
	for i, comboID := range loadout.ActiveCombos {
		activeCombos[i] = openapi_types.UUID(comboID)
	}

	var preferences *struct {
		AutoActivate  *bool                 `json:"auto_activate,omitempty"`
		PriorityOrder *[]openapi_types.UUID `json:"priority_order,omitempty"`
	}
	if loadout.Preferences != nil {
		priorityOrder := make([]openapi_types.UUID, len(loadout.Preferences.PriorityOrder))
		for i, id := range loadout.Preferences.PriorityOrder {
			priorityOrder[i] = openapi_types.UUID(id)
		}

		preferences = &struct {
			AutoActivate  *bool                 `json:"auto_activate,omitempty"`
			PriorityOrder *[]openapi_types.UUID `json:"priority_order,omitempty"`
		}{
			AutoActivate:  &loadout.Preferences.AutoActivate,
			PriorityOrder: &priorityOrder,
		}
	}

	return combosapi.ComboLoadout{
		Id:           id,
		CharacterId:  characterID,
		ActiveCombos: &activeCombos,
		Preferences:  preferences,
		CreatedAt:    &createdAt,
		UpdatedAt:    &updatedAt,
	}
}

func convertUpdateLoadoutRequestFromAPI(req *combosapi.UpdateLoadoutRequest) *models.UpdateLoadoutRequest {
	result := &models.UpdateLoadoutRequest{
		CharacterID: uuid.UUID(req.CharacterId),
	}

	if req.ActiveCombos != nil {
		result.ActiveCombos = make([]uuid.UUID, len(*req.ActiveCombos))
		for i, id := range *req.ActiveCombos {
			result.ActiveCombos[i] = uuid.UUID(id)
		}
	}

	if req.Preferences != nil {
		result.Preferences = &models.LoadoutPreferences{
			AutoActivate: false,
		}
		if req.Preferences.AutoActivate != nil {
			result.Preferences.AutoActivate = *req.Preferences.AutoActivate
		}
		if req.Preferences.PriorityOrder != nil {
			result.Preferences.PriorityOrder = make([]uuid.UUID, len(*req.Preferences.PriorityOrder))
			for i, id := range *req.Preferences.PriorityOrder {
				result.Preferences.PriorityOrder[i] = uuid.UUID(id)
			}
		}
	}

	return result
}

func convertSubmitScoreRequestFromAPI(req *combosapi.SubmitScoreRequest) *models.SubmitScoreRequest {
	result := &models.SubmitScoreRequest{
		ActivationID:        uuid.UUID(req.ActivationId),
		ExecutionDifficulty: req.ExecutionDifficulty,
		DamageOutput:        req.DamageOutput,
		VisualImpact:        req.VisualImpact,
	}

	if req.TeamCoordination != nil {
		result.TeamCoordination = req.TeamCoordination
	}

	return result
}

func convertScoreSubmissionResponseToAPI(response *models.ScoreSubmissionResponse) combosapi.ScoreSubmissionResponse {
	score := convertComboScoreToAPI(&response.Score)

	var rewards *struct {
		Currency   *int                 `json:"currency,omitempty"`
		Experience *int                 `json:"experience,omitempty"`
		Items      *[]openapi_types.UUID `json:"items,omitempty"`
	}
	if response.Rewards != nil {
		items := make([]openapi_types.UUID, len(response.Rewards.Items))
		for i, id := range response.Rewards.Items {
			items[i] = openapi_types.UUID(id)
		}

		rewards = &struct {
			Currency   *int                 `json:"currency,omitempty"`
			Experience *int                 `json:"experience,omitempty"`
			Items      *[]openapi_types.UUID `json:"items,omitempty"`
		}{
			Currency:   &response.Rewards.Currency,
			Experience: &response.Rewards.Experience,
			Items:      &items,
		}
	}

	return combosapi.ScoreSubmissionResponse{
		Success: response.Success,
		Score:   score,
		Rewards: rewards,
	}
}

func convertComboScoreToAPI(score *models.ComboScore) combosapi.ComboScore {
	activationID := openapi_types.UUID(score.ActivationID)
	timestamp := score.Timestamp
	category := combosapi.ComboComplexity(score.Category)

	result := combosapi.ComboScore{
		ActivationId:        activationID,
		ExecutionDifficulty: &score.ExecutionDifficulty,
		DamageOutput:        &score.DamageOutput,
		VisualImpact:        &score.VisualImpact,
		TotalScore:          score.TotalScore,
		Category:            category,
		Timestamp:           &timestamp,
	}

	if score.TeamCoordination != nil {
		result.TeamCoordination = score.TeamCoordination
	}

	return result
}

func convertAnalyticsResponseToAPI(response *models.AnalyticsResponse) combosapi.AnalyticsResponse {
	analytics := make([]combosapi.ComboAnalytics, len(response.Analytics))
	for i, a := range response.Analytics {
		analytics[i] = convertComboAnalyticsToAPI(&a)
	}

	periodStart := response.PeriodStart
	periodEnd := response.PeriodEnd

	return combosapi.AnalyticsResponse{
		Analytics:   &analytics,
		PeriodStart: &periodStart,
		PeriodEnd:   &periodEnd,
	}
}

func convertComboAnalyticsToAPI(analytics *models.ComboAnalytics) combosapi.ComboAnalytics {
	comboID := openapi_types.UUID(analytics.ComboID)
	category := combosapi.ComboComplexity(analytics.AverageCategory)

	mostUsedSynergies := make([]struct {
		SynergyId   *openapi_types.UUID `json:"synergy_id,omitempty"`
		SynergyType *combosapi.SynergyType `json:"synergy_type,omitempty"`
		UsageCount  *int                `json:"usage_count,omitempty"`
	}, len(analytics.MostUsedSynergies))

	for i, s := range analytics.MostUsedSynergies {
		synergyID := openapi_types.UUID(s.SynergyID)
		synergyType := combosapi.SynergyType(s.SynergyType)
		mostUsedSynergies[i] = struct {
			SynergyId   *openapi_types.UUID `json:"synergy_id,omitempty"`
			SynergyType *combosapi.SynergyType `json:"synergy_type,omitempty"`
			UsageCount  *int                `json:"usage_count,omitempty"`
		}{
			SynergyId:   &synergyID,
			SynergyType: &synergyType,
			UsageCount:  &s.UsageCount,
		}
	}

	return combosapi.ComboAnalytics{
		ComboId:          &comboID,
		TotalActivations: &analytics.TotalActivations,
		SuccessRate:      &analytics.SuccessRate,
		AverageScore:     &analytics.AverageScore,
		AverageCategory:  &category,
		MostUsedSynergies: &mostUsedSynergies,
		ChainComboCount:  &analytics.ChainComboCount,
	}
}

