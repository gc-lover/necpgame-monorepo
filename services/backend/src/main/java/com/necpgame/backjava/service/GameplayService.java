package com.necpgame.backjava.service;

import com.necpgame.backjava.model.BattlePassRewardError;
import com.necpgame.backjava.model.BoostActivationRequest;
import com.necpgame.backjava.model.BoostActivationResponse;
import com.necpgame.backjava.model.BoostStatusResponse;
import com.necpgame.backjava.model.Challenge;
import com.necpgame.backjava.model.ChallengeCompleteRequest;
import com.necpgame.backjava.model.ChallengeListResponse;
import com.necpgame.backjava.model.ChallengeProgressUpdateRequest;
import com.necpgame.backjava.model.ChallengeRerollRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.LeaderboardResponse;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.RewardAnalyticsResponse;
import com.necpgame.backjava.model.RewardClaimRequest;
import com.necpgame.backjava.model.RewardClaimResponse;
import com.necpgame.backjava.model.RewardDefinition;
import com.necpgame.backjava.model.RewardDefinitionList;
import com.necpgame.backjava.model.RewardHistoryResponse;
import com.necpgame.backjava.model.RewardRerollRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayService {

    /**
     * GET /gameplay/battle-pass/analytics/rewards : Аналитика наград и челленджей
     *
     * @param seasonId  (optional)
     * @param range  (optional, default to 7d)
     * @return RewardAnalyticsResponse
     */
    RewardAnalyticsResponse gameplayBattlePassAnalyticsRewardsGet(String seasonId, String range);

    /**
     * POST /gameplay/battle-pass/boosts/activate : Активировать XP boost
     *
     * @param boostActivationRequest  (required)
     * @return BoostActivationResponse
     */
    BoostActivationResponse gameplayBattlePassBoostsActivatePost(BoostActivationRequest boostActivationRequest);

    /**
     * GET /gameplay/battle-pass/boosts : Активные и доступные бусты XP
     *
     * @return BoostStatusResponse
     */
    BoostStatusResponse gameplayBattlePassBoostsGet();

    /**
     * POST /gameplay/battle-pass/challenges/{challengeId}/complete : Завершить челлендж и выдать награду
     *
     * @param challengeId Идентификатор челленджа Battle Pass (required)
     * @param challengeCompleteRequest  (required)
     * @return RewardClaimResponse
     */
    RewardClaimResponse gameplayBattlePassChallengesChallengeIdCompletePost(String challengeId, ChallengeCompleteRequest challengeCompleteRequest);

    /**
     * POST /gameplay/battle-pass/challenges/{challengeId}/progress : Обновить прогресс челленджа (service token)
     *
     * @param challengeId Идентификатор челленджа Battle Pass (required)
     * @param challengeProgressUpdateRequest  (required)
     * @return Void
     */
    Void gameplayBattlePassChallengesChallengeIdProgressPost(String challengeId, ChallengeProgressUpdateRequest challengeProgressUpdateRequest);

    /**
     * POST /gameplay/battle-pass/challenges/{challengeId}/reroll : Реролл челленджа
     *
     * @param challengeId Идентификатор челленджа Battle Pass (required)
     * @param challengeRerollRequest  (required)
     * @return Challenge
     */
    Challenge gameplayBattlePassChallengesChallengeIdRerollPost(String challengeId, ChallengeRerollRequest challengeRerollRequest);

    /**
     * GET /gameplay/battle-pass/challenges/daily : Активные дневные челленджи игрока
     *
     * @return ChallengeListResponse
     */
    ChallengeListResponse gameplayBattlePassChallengesDailyGet();

    /**
     * GET /gameplay/battle-pass/challenges/weekly : Активные недельные челленджи
     *
     * @return ChallengeListResponse
     */
    ChallengeListResponse gameplayBattlePassChallengesWeeklyGet();

    /**
     * GET /gameplay/battle-pass/leaderboard : Лидерборд по челленджам/наградам
     *
     * @param metric  (optional, default to challenge_points)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return LeaderboardResponse
     */
    LeaderboardResponse gameplayBattlePassLeaderboardGet(String metric, Integer page, Integer pageSize);

    /**
     * POST /gameplay/battle-pass/rewards/claim : Получить награду уровня
     *
     * @param rewardClaimRequest  (required)
     * @return RewardClaimResponse
     */
    RewardClaimResponse gameplayBattlePassRewardsClaimPost(RewardClaimRequest rewardClaimRequest);

    /**
     * GET /gameplay/battle-pass/rewards : Список наград сезона
     *
     * @param track  (optional)
     * @param rarity  (optional)
     * @return RewardDefinitionList
     */
    RewardDefinitionList gameplayBattlePassRewardsGet(String track, String rarity);

    /**
     * GET /gameplay/battle-pass/rewards/history : История полученных наград
     *
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return RewardHistoryResponse
     */
    RewardHistoryResponse gameplayBattlePassRewardsHistoryGet(Integer page, Integer pageSize);

    /**
     * POST /gameplay/battle-pass/rewards/reroll : Реролл награды уровня
     *
     * @param rewardRerollRequest  (required)
     * @return RewardDefinition
     */
    RewardDefinition gameplayBattlePassRewardsRerollPost(RewardRerollRequest rewardRerollRequest);
}

