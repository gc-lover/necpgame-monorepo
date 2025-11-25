package server

import (
	"github.com/necpgame/referral-service-go/models"
	"github.com/necpgame/referral-service-go/pkg/api"
	openapi_types "github.com/oapi-codegen/runtime/types"
)

func toAPIReferralCode(code *models.ReferralCode) api.ReferralCode {
	if code == nil {
		return api.ReferralCode{}
	}

	apiID := openapi_types.UUID(code.ID)
	apiPlayerID := openapi_types.UUID(code.PlayerID)

	return api.ReferralCode{
		Id:        &apiID,
		PlayerId:  &apiPlayerID,
		Code:      &code.Code,
		IsActive:  &code.IsActive,
		UsedCount: &code.UsedCount,
		CreatedAt: &code.CreatedAt,
	}
}

func toAPIReferral(ref *models.Referral) api.Referral {
	if ref == nil {
		return api.Referral{}
	}

	apiID := openapi_types.UUID(ref.ID)
	apiReferrerID := openapi_types.UUID(ref.ReferrerID)
	apiReferredID := openapi_types.UUID(ref.ReferredID)
	apiStatus := api.ReferralStatus(ref.Status)

	return api.Referral{
		Id:           &apiID,
		ReferrerId:   &apiReferrerID,
		ReferredId:   &apiReferredID,
		Status:       &apiStatus,
		ReferralCode: &ref.ReferralCode,
		CreatedAt:    &ref.CreatedAt,
		CompletedAt:  ref.CompletedAt,
	}
}

func toAPIReferralMilestone(ms *models.ReferralMilestone) api.ReferralMilestone {
	if ms == nil {
		return api.ReferralMilestone{}
	}

	apiID := openapi_types.UUID(ms.ID)
	apiPlayerID := openapi_types.UUID(ms.PlayerID)
	apiMilestoneType := api.ReferralMilestoneType(ms.MilestoneType)

	return api.ReferralMilestone{
		Id:            &apiID,
		PlayerId:      &apiPlayerID,
		MilestoneType: &apiMilestoneType,
		IsCompleted:   &ms.IsCompleted,
		IsClaimed:     &ms.IsClaimed,
		CompletedAt:   ms.CompletedAt,
		ClaimedAt:     ms.ClaimedAt,
		RewardData:    ms.RewardData,
	}
}

func toAPIReferralReward(reward *models.ReferralReward) api.ReferralReward {
	if reward == nil {
		return api.ReferralReward{}
	}

	apiID := openapi_types.UUID(reward.ID)
	apiReferralID := openapi_types.UUID(reward.ReferralID)
	apiPlayerID := openapi_types.UUID(reward.PlayerID)
	apiRewardType := api.ReferralRewardType(reward.RewardType)

	return api.ReferralReward{
		Id:         &apiID,
		ReferralId: &apiReferralID,
		PlayerId:   &apiPlayerID,
		RewardType: &apiRewardType,
		IsClaimed:  &reward.IsClaimed,
		RewardData: reward.RewardData,
		CreatedAt:  &reward.CreatedAt,
		ClaimedAt:  reward.ClaimedAt,
	}
}

func toAPIReferralStats(stats *models.ReferralStats) api.ReferralStats {
	if stats == nil {
		return api.ReferralStats{}
	}

	apiPlayerID := openapi_types.UUID(stats.PlayerID)

	return api.ReferralStats{
		PlayerId:              &apiPlayerID,
		TotalReferrals:        &stats.TotalReferrals,
		ActiveReferrals:       &stats.ActiveReferrals,
		CompletedReferrals:    &stats.CompletedReferrals,
		TotalRewardsEarned:    &stats.TotalRewardsEarned,
		CurrentMilestone:      stats.CurrentMilestone,
		NextMilestoneProgress: stats.NextMilestoneProgress,
	}
}

func toAPILeaderboardEntry(entry *models.ReferralLeaderboardEntry) api.ReferralLeaderboardEntry {
	if entry == nil {
		return api.ReferralLeaderboardEntry{}
	}

	apiPlayerID := openapi_types.UUID(entry.PlayerID)

	return api.ReferralLeaderboardEntry{
		PlayerId:        &apiPlayerID,
		PlayerName:      entry.PlayerName,
		Rank:            &entry.Rank,
		TotalReferrals:  &entry.TotalReferrals,
		ActiveReferrals: &entry.ActiveReferrals,
		Score:           &entry.Score,
	}
}

func toAPIReferralEvent(event *models.ReferralEvent) api.ReferralEvent {
	if event == nil {
		return api.ReferralEvent{}
	}

	apiID := openapi_types.UUID(event.ID)
	apiPlayerID := openapi_types.UUID(event.PlayerID)
	apiEventType := api.ReferralEventType(event.EventType)

	var apiReferralID *openapi_types.UUID
	if event.ReferralID != nil {
		id := openapi_types.UUID(*event.ReferralID)
		apiReferralID = &id
	}

	return api.ReferralEvent{
		Id:         &apiID,
		PlayerId:   &apiPlayerID,
		ReferralId: apiReferralID,
		EventType:  &apiEventType,
		EventData:  event.EventData,
		CreatedAt:  &event.CreatedAt,
	}
}
