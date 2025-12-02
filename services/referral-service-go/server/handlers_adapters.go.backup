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
	apiStatus := api.ReferralStatus(ref.Status)
	apiReferralCodeID := openapi_types.UUID(ref.ReferralCodeID)

	return api.Referral{
		Id:               &apiID,
		ReferrerId:       &apiReferrerID,
		RefereeId:       &apiRefereeID,
		Status:           &apiStatus,
		ReferralCodeId:   &apiReferralCodeID,
		RegisteredAt:     &ref.RegisteredAt,
		Level10Reached:   &ref.Level10Reached,
		Level10ReachedAt: ref.Level10ReachedAt,
		WelcomeBonusGiven: &ref.WelcomeBonusGiven,
		ReferrerBonusGiven: &ref.ReferrerBonusGiven,
		UpdatedAt:        &ref.UpdatedAt,
	}
}

func toAPIReferralMilestone(ms *models.ReferralMilestone) api.ReferralMilestone {
	if ms == nil {
		return api.ReferralMilestone{}
	}

	apiID := openapi_types.UUID(ms.ID)
	apiPlayerID := openapi_types.UUID(ms.PlayerID)
	apiMilestoneType := api.ReferralMilestoneMilestoneType(ms.MilestoneType)
	rewardClaimed := ms.RewardClaimed

	return api.ReferralMilestone{
		Id:             &apiID,
		PlayerId:       &apiPlayerID,
		MilestoneType: &apiMilestoneType,
		MilestoneValue: &ms.MilestoneValue,
		AchievedAt:     &ms.AchievedAt,
		RewardClaimed:  &rewardClaimed,
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
		Id:           &apiID,
		ReferralId:   apiReferralID,
		PlayerId:     &apiPlayerID,
		RewardType:   &apiRewardType,
		RewardAmount: &reward.RewardAmount,
		CurrencyType: &reward.CurrencyType,
		DistributedAt: &reward.DistributedAt,
	}
}

func toAPIReferralStats(stats *models.ReferralStats) api.ReferralStats {
	if stats == nil {
		return api.ReferralStats{}
	}

	apiPlayerID := openapi_types.UUID(stats.PlayerID)
	
	var currentMilestone *api.ReferralStatsCurrentMilestone
	if stats.CurrentMilestone != nil {
		cm := api.ReferralStatsCurrentMilestone(*stats.CurrentMilestone)
		currentMilestone = &cm
	}

	return api.ReferralStats{
		PlayerId:        &apiPlayerID,
		TotalReferrals:  &stats.TotalReferrals,
		ActiveReferrals: &stats.ActiveReferrals,
		Level10Referrals: &stats.Level10Referrals,
		CurrentMilestone: currentMilestone,
		TotalRewards:    &stats.TotalRewards,
		LastUpdated:      &stats.LastUpdated,
	}
}

func toAPILeaderboardEntry(entry *models.ReferralLeaderboardEntry) api.ReferralLeaderboardEntry {
	if entry == nil {
		return api.ReferralLeaderboardEntry{}
	}

	apiPlayerID := openapi_types.UUID(entry.PlayerID)
	playerName := entry.PlayerName
	
	var currentMilestone *api.ReferralLeaderboardEntryCurrentMilestone
	if entry.CurrentMilestone != nil {
		cm := api.ReferralLeaderboardEntryCurrentMilestone(*entry.CurrentMilestone)
		currentMilestone = &cm
	}

	return api.ReferralLeaderboardEntry{
		PlayerId:        &apiPlayerID,
		PlayerName:      &playerName,
		Rank:            &entry.Rank,
		TotalReferrals:  &entry.TotalReferrals,
		ActiveReferrals: &entry.ActiveReferrals,
		Level10Referrals: &entry.Level10Referrals,
		CurrentMilestone: currentMilestone,
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
	eventData := event.EventData

	return api.ReferralEvent{
		Id:        &apiID,
		PlayerId:  &apiPlayerID,
		EventType: &apiEventType,
		EventData: &eventData,
		CreatedAt: &event.CreatedAt,
	}
}
