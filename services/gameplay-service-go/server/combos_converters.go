// Package server SQL queries use prepared statements with placeholders (, , ?) for safety
// Issue: #1525
package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
)

func convertLoadoutToAPI(loadout *models.ComboLoadout) *api.ComboLoadout {
	result := &api.ComboLoadout{
		ID:           loadout.ID,
		CharacterID:  loadout.CharacterID,
		ActiveCombos: loadout.ActiveCombos,
	}

	result.CreatedAt.SetTo(loadout.CreatedAt)
	result.UpdatedAt.SetTo(loadout.UpdatedAt)

	if loadout.Preferences != nil {
		prefs := api.ComboLoadoutPreferences{}
		prefs.AutoActivate.SetTo(loadout.Preferences.AutoActivate)
		prefs.PriorityOrder = loadout.Preferences.PriorityOrder
		result.Preferences.SetTo(prefs)
	}

	return result
}

func convertUpdateLoadoutRequestFromAPI(req *api.UpdateLoadoutRequest) *models.UpdateLoadoutRequest {
	result := &models.UpdateLoadoutRequest{
		CharacterID:  req.CharacterID,
		ActiveCombos: req.ActiveCombos,
	}

	if req.Preferences.IsSet() {
		prefs := req.Preferences.Value
		result.Preferences = &models.LoadoutPreferences{
			PriorityOrder: prefs.PriorityOrder,
		}
		if autoActivate, ok := prefs.AutoActivate.Get(); ok {
			result.Preferences.AutoActivate = autoActivate
		}
	}

	return result
}

func convertSubmitScoreRequestFromAPI(req *api.SubmitScoreRequest) *models.SubmitScoreRequest {
	result := &models.SubmitScoreRequest{
		ActivationID:        req.ActivationID,
		ExecutionDifficulty: req.ExecutionDifficulty,
		DamageOutput:        req.DamageOutput,
		VisualImpact:        req.VisualImpact,
	}

	if teamCoord, ok := req.TeamCoordination.Get(); ok {
		result.TeamCoordination = &teamCoord
	}

	return result
}

func convertScoreSubmissionResponseToAPI(response *models.ScoreSubmissionResponse) *api.ScoreSubmissionResponse {
	result := &api.ScoreSubmissionResponse{
		Success: response.Success,
		Score:   convertComboScoreToAPI(&response.Score),
	}

	if response.Rewards != nil {
		rewards := api.ScoreSubmissionResponseRewards{
			Items: response.Rewards.Items,
		}
		rewards.Experience.SetTo(response.Rewards.Experience)
		rewards.Currency.SetTo(response.Rewards.Currency)
		result.Rewards.SetTo(rewards)
	}

	return result
}

func convertComboScoreToAPI(score *models.ComboScore) api.ComboScore {
	result := api.ComboScore{
		ActivationID: score.ActivationID,
		TotalScore:   score.TotalScore,
		Category:     api.ComboComplexity(score.Category),
	}

	result.Timestamp.SetTo(score.Timestamp)
	result.ExecutionDifficulty.SetTo(score.ExecutionDifficulty)
	result.DamageOutput.SetTo(score.DamageOutput)
	result.VisualImpact.SetTo(score.VisualImpact)

	if score.TeamCoordination != nil {
		result.TeamCoordination.SetTo(*score.TeamCoordination)
	}

	return result
}

func convertAnalyticsResponseToAPI(response *models.AnalyticsResponse) *api.AnalyticsResponse {
	result := &api.AnalyticsResponse{
		Analytics: make([]api.ComboAnalytics, len(response.Analytics)),
	}

	result.PeriodStart.SetTo(response.PeriodStart)
	result.PeriodEnd.SetTo(response.PeriodEnd)

	for i, a := range response.Analytics {
		result.Analytics[i] = convertComboAnalyticsToAPI(&a)
	}

	return result
}

func convertComboAnalyticsToAPI(analytics *models.ComboAnalytics) api.ComboAnalytics {
	result := api.ComboAnalytics{}

	result.ComboID.SetTo(analytics.ComboID)
	result.TotalActivations.SetTo(analytics.TotalActivations)
	result.SuccessRate.SetTo(analytics.SuccessRate)
	result.AverageScore.SetTo(analytics.AverageScore)
	result.ChainComboCount.SetTo(analytics.ChainComboCount)
	result.AverageCategory.SetTo(api.ComboComplexity(analytics.AverageCategory))

	result.MostUsedSynergies = make([]api.ComboAnalyticsMostUsedSynergiesItem, len(analytics.MostUsedSynergies))
	for i, s := range analytics.MostUsedSynergies {
		item := api.ComboAnalyticsMostUsedSynergiesItem{}
		item.SynergyID.SetTo(s.SynergyID)
		item.SynergyType.SetTo(api.SynergyType(s.SynergyType))
		item.UsageCount.SetTo(s.UsageCount)
		result.MostUsedSynergies[i] = item
	}

	return result
}
