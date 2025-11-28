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
		CreatedAt: &code.CreatedAt,
	}
}

func toAPIReferral(ref *models.Referral) api.Referral {
	if ref == nil {
		return api.Referral{}
	}

	apiID := openapi_types.UUID(ref.ID)
	apiReferrerID := openapi_types.UUID(ref.ReferrerID)
	apiRefereeID := openapi_types.UUID(ref.RefereeID)
	apiReferralCodeID := openapi_types.UUID(ref.ReferralCodeID)
	apiStatus := api.ReferralStatus(ref.Status)

	return api.Referral{
		Id:                 &apiID,
		ReferrerId:         &apiReferrerID,
		RefereeId:          &apiRefereeID,
		ReferralCodeId:     &apiReferralCodeID,
		Status:             &apiStatus,
		RegisteredAt:       &ref.RegisteredAt,
		Level10Reached:     &ref.Level10Reached,
		Level10ReachedAt:   ref.Level10ReachedAt,
		ReferrerBonusGiven: &ref.ReferrerBonusGiven,
		CreatedAt:          &ref.RegisteredAt,
	}
}

func toAPIReferralMilestone(ms *models.ReferralMilestone) api.ReferralMilestone {
	if ms == nil {
		return api.ReferralMilestone{}
	}

	apiID := openapi_types.UUID(ms.ID)
	apiPlayerID := openapi_types.UUID(ms.PlayerID)
	apiMilestoneType := api.ReferralMilestoneMilestoneType(ms.MilestoneType)

	return api.ReferralMilestone{
		Id:              &apiID,
		PlayerId:        &apiPlayerID,
		MilestoneType:   &apiMilestoneType,
		MilestoneValue:  &ms.MilestoneValue,
		AchievedAt:      &ms.AchievedAt,
		RewardClaimed:   &ms.RewardClaimed,
		RewardClaimedAt: ms.RewardClaimedAt,
	}
}

func toAPIReferralReward(reward *models.ReferralReward) api.ReferralReward {
	if reward == nil {
		return api.ReferralReward{}
	}

	apiID := openapi_types.UUID(reward.ID)
	apiPlayerID := openapi_types.UUID(reward.PlayerID)
	apiRewardType := api.ReferralRewardRewardType(reward.RewardType)

	var apiReferralID *openapi_types.UUID
	if reward.ReferralID != nil {
		id := openapi_types.UUID(*reward.ReferralID)
		apiReferralID = &id
	}

	return api.ReferralReward{
		Id:            &apiID,
		ReferralId:    apiReferralID,
		PlayerId:      &apiPlayerID,
		RewardType:    &apiRewardType,
		RewardAmount:  &reward.RewardAmount,
		CurrencyType:  &reward.CurrencyType,
		DistributedAt: &reward.DistributedAt,
	}
}

func toAPIReferralStats(stats *models.ReferralStats) api.ReferralStats {
	if stats == nil {
		return api.ReferralStats{}
	}

	apiPlayerID := openapi_types.UUID(stats.PlayerID)

	var apiCurrentMilestone *api.ReferralStatsCurrentMilestone
	if stats.CurrentMilestone != nil {
		ms := api.ReferralStatsCurrentMilestone(string(*stats.CurrentMilestone))
		apiCurrentMilestone = &ms
	}

	return api.ReferralStats{
		PlayerId:         &apiPlayerID,
		TotalReferrals:   &stats.TotalReferrals,
		ActiveReferrals:  &stats.ActiveReferrals,
		Level10Referrals: &stats.Level10Referrals,
		CurrentMilestone: apiCurrentMilestone,
		TotalRewards:     &stats.TotalRewards,
		LastUpdated:      &stats.LastUpdated,
	}
}

func toAPILeaderboardEntry(entry *models.ReferralLeaderboardEntry) api.ReferralLeaderboardEntry {
	if entry == nil {
		return api.ReferralLeaderboardEntry{}
	}

	apiPlayerID := openapi_types.UUID(entry.PlayerID)

	var apiCurrentMilestone *api.ReferralLeaderboardEntryCurrentMilestone
	if entry.CurrentMilestone != nil {
		ms := api.ReferralLeaderboardEntryCurrentMilestone(string(*entry.CurrentMilestone))
		apiCurrentMilestone = &ms
	}

	return api.ReferralLeaderboardEntry{
		PlayerId:         &apiPlayerID,
		PlayerName:       &entry.PlayerName,
		Rank:             &entry.Rank,
		TotalReferrals:   &entry.TotalReferrals,
		ActiveReferrals:  &entry.ActiveReferrals,
		Level10Referrals: &entry.Level10Referrals,
		CurrentMilestone: apiCurrentMilestone,
		TotalRewards:     &entry.TotalRewards,
	}
}

func toAPIReferralEvent(event *models.ReferralEvent) api.ReferralEvent {
	if event == nil {
		return api.ReferralEvent{}
	}

	apiID := openapi_types.UUID(event.ID)
	apiPlayerID := openapi_types.UUID(event.PlayerID)
	apiEventType := api.ReferralEventEventType(event.EventType)

	return api.ReferralEvent{
		Id:        &apiID,
		PlayerId:  &apiPlayerID,
		EventType: &apiEventType,
		EventData: &event.EventData,
		CreatedAt: &event.CreatedAt,
	}
}
