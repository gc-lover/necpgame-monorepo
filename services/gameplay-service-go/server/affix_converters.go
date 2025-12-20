// Package server Issue: #1515
package server

import (
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/models"
	"github.com/gc-lover/necpgame-monorepo/services/gameplay-service-go/pkg/api"
)

func convertActiveAffixesResponseToAPI(resp *models.ActiveAffixesResponse) *api.ActiveAffixesResponse {
	result := &api.ActiveAffixesResponse{}
	result.WeekStart = api.NewOptDateTime(resp.WeekStart)
	result.WeekEnd = api.NewOptDateTime(resp.WeekEnd)

	for _, affix := range resp.ActiveAffixes {
		result.ActiveAffixes = append(result.ActiveAffixes, convertAffixSummaryToAPI(affix))
	}

	if resp.SeasonalAffix != nil {
		seasonal := convertAffixSummaryToAPI(*resp.SeasonalAffix)
		result.SeasonalAffix = api.NewOptNilAffixSummary(seasonal)
	}

	return result
}

func convertAffixToAPI(affix *models.Affix) *api.Affix {
	result := &api.Affix{}
	result.ID = api.NewOptUUID(affix.ID)
	result.Name = api.NewOptString(affix.Name)
	result.Category = api.NewOptAffixCategory(api.AffixCategory(affix.Category))
	result.Description = api.NewOptString(affix.Description)
	result.RewardModifier = api.NewOptFloat32(float32(affix.RewardModifier))
	result.DifficultyModifier = api.NewOptFloat32(float32(affix.DifficultyModifier))
	result.CreatedAt = api.NewOptDateTime(affix.CreatedAt)

	if affix.Mechanics != nil {
		mechanics := api.AffixMechanics{}
		if trigger, ok := affix.Mechanics["trigger"].(string); ok {
			mechanics.Trigger = api.NewOptString(trigger)
		}
		if effectType, ok := affix.Mechanics["effect_type"].(string); ok {
			mechanics.EffectType = api.NewOptString(effectType)
		}
		if radius, ok := affix.Mechanics["radius"].(float64); ok {
			mechanics.Radius = api.NewOptFloat32(float32(radius))
		}
		if damagePercent, ok := affix.Mechanics["damage_percent"].(float64); ok {
			mechanics.DamagePercent = api.NewOptInt(int(damagePercent))
		}
		if damageType, ok := affix.Mechanics["damage_type"].(string); ok {
			mechanics.DamageType = api.NewOptString(damageType)
		}
		result.Mechanics = api.NewOptAffixMechanics(mechanics)
	}

	if affix.VisualEffects != nil {
		visual := api.AffixVisualEffects{}
		if explosion, ok := affix.VisualEffects["explosion_particle"].(string); ok {
			visual.ExplosionParticle = api.NewOptString(explosion)
		}
		if sound, ok := affix.VisualEffects["sound_effect"].(string); ok {
			visual.SoundEffect = api.NewOptString(sound)
		}
		if screenShake, ok := affix.VisualEffects["screen_shake"].(bool); ok {
			visual.ScreenShake = api.NewOptBool(screenShake)
		}
		result.VisualEffects = api.NewOptAffixVisualEffects(visual)
	}

	return result
}

func convertAffixSummaryToAPI(summary models.AffixSummary) api.AffixSummary {
	result := api.AffixSummary{}
	result.ID = api.NewOptUUID(summary.ID)
	result.Name = api.NewOptString(summary.Name)
	result.Category = api.NewOptAffixSummaryCategory(api.AffixSummaryCategory(summary.Category))
	result.Description = api.NewOptString(summary.Description)
	result.RewardModifier = api.NewOptFloat32(float32(summary.RewardModifier))
	result.DifficultyModifier = api.NewOptFloat32(float32(summary.DifficultyModifier))
	return result
}

func convertInstanceAffixesResponseToAPI(resp *models.InstanceAffixesResponse) *api.InstanceAffixesResponse {
	result := &api.InstanceAffixesResponse{}
	result.InstanceID = api.NewOptUUID(resp.InstanceID)
	result.AppliedAt = api.NewOptDateTime(resp.AppliedAt)
	result.TotalRewardModifier = api.NewOptFloat32(float32(resp.TotalRewardModifier))
	result.TotalDifficultyModifier = api.NewOptFloat32(float32(resp.TotalDifficultyModifier))

	for _, affix := range resp.Affixes {
		result.Affixes = append(result.Affixes, convertAffixSummaryToAPI(affix))
	}

	return result
}

func convertRotationHistoryResponseToAPI(resp *models.AffixRotationHistoryResponse) *api.AffixRotationHistoryResponse {
	result := &api.AffixRotationHistoryResponse{
		Total: resp.Total,
	}
	result.Limit = api.NewOptInt(resp.Limit)
	result.Offset = api.NewOptInt(resp.Offset)

	for _, rotation := range resp.Items {
		result.Items = append(result.Items, convertRotationToAPI(&rotation))
	}

	return result
}

func convertRotationToAPI(rotation *models.AffixRotation) api.AffixRotation {
	result := api.AffixRotation{}
	result.ID = api.NewOptUUID(rotation.ID)
	result.WeekStart = api.NewOptDateTime(rotation.WeekStart)
	result.WeekEnd = api.NewOptDateTime(rotation.WeekEnd)
	result.CreatedAt = api.NewOptDateTime(rotation.CreatedAt)

	for _, affix := range rotation.ActiveAffixes {
		result.ActiveAffixes = append(result.ActiveAffixes, convertAffixSummaryToAPI(affix))
	}

	if rotation.SeasonalAffix != nil {
		seasonal := convertAffixSummaryToAPI(*rotation.SeasonalAffix)
		result.SeasonalAffix = api.NewOptNilAffixSummary(seasonal)
	}

	return result
}
