package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.BoostActivationRequest;
import com.necpgame.backjava.model.BoostActivationResponse;
import com.necpgame.backjava.model.BoostStatusResponse;
import com.necpgame.backjava.model.Challenge;
import com.necpgame.backjava.model.ChallengeCompleteRequest;
import com.necpgame.backjava.model.ChallengeListResponse;
import com.necpgame.backjava.model.ChallengeProgressUpdateRequest;
import com.necpgame.backjava.model.ChallengeRerollRequest;
import com.necpgame.backjava.model.LeaderboardResponse;
import com.necpgame.backjava.model.RewardAnalyticsResponse;
import com.necpgame.backjava.model.RewardClaimRequest;
import com.necpgame.backjava.model.RewardClaimResponse;
import com.necpgame.backjava.model.RewardDefinition;
import com.necpgame.backjava.model.RewardDefinitionList;
import com.necpgame.backjava.model.RewardHistoryResponse;
import com.necpgame.backjava.model.RewardRerollRequest;
import com.necpgame.backjava.service.GameplayService;
import org.springframework.stereotype.Service;

@Service
public class GameplayServiceImpl implements GameplayService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Gameplay service is not implemented yet");
    }

    @Override
    public RewardAnalyticsResponse gameplayBattlePassAnalyticsRewardsGet(String seasonId, String range) {
        throw error();
    }

    @Override
    public BoostActivationResponse gameplayBattlePassBoostsActivatePost(BoostActivationRequest boostActivationRequest) {
        throw error();
    }

    @Override
    public BoostStatusResponse gameplayBattlePassBoostsGet() {
        throw error();
    }

    @Override
    public RewardClaimResponse gameplayBattlePassChallengesChallengeIdCompletePost(String challengeId, ChallengeCompleteRequest challengeCompleteRequest) {
        throw error();
    }

    @Override
    public Void gameplayBattlePassChallengesChallengeIdProgressPost(String challengeId, ChallengeProgressUpdateRequest challengeProgressUpdateRequest) {
        throw error();
    }

    @Override
    public Challenge gameplayBattlePassChallengesChallengeIdRerollPost(String challengeId, ChallengeRerollRequest challengeRerollRequest) {
        throw error();
    }

    @Override
    public ChallengeListResponse gameplayBattlePassChallengesDailyGet() {
        throw error();
    }

    @Override
    public ChallengeListResponse gameplayBattlePassChallengesWeeklyGet() {
        throw error();
    }

    @Override
    public LeaderboardResponse gameplayBattlePassLeaderboardGet(String metric, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public RewardClaimResponse gameplayBattlePassRewardsClaimPost(RewardClaimRequest rewardClaimRequest) {
        throw error();
    }

    @Override
    public RewardDefinitionList gameplayBattlePassRewardsGet(String track, String rarity) {
        throw error();
    }

    @Override
    public RewardHistoryResponse gameplayBattlePassRewardsHistoryGet(Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public RewardDefinition gameplayBattlePassRewardsRerollPost(RewardRerollRequest rewardRerollRequest) {
        throw error();
    }
}


