package com.necpgame.gameplayservice.service;

import com.necpgame.gameplayservice.model.ActivityType;
import com.necpgame.gameplayservice.model.Error;
import com.necpgame.gameplayservice.model.LeaderboardPage;
import org.springframework.lang.Nullable;
import com.necpgame.gameplayservice.model.PlacementRequest;
import com.necpgame.gameplayservice.model.PlacementStatus;
import com.necpgame.gameplayservice.model.RatingDeltaRequest;
import com.necpgame.gameplayservice.model.RatingDeltaResult;
import com.necpgame.gameplayservice.model.RatingError;
import com.necpgame.gameplayservice.model.RatingHistoryPage;
import com.necpgame.gameplayservice.model.RatingProfile;
import com.necpgame.gameplayservice.model.SeasonResetRequest;
import com.necpgame.gameplayservice.model.SeasonSummary;
import com.necpgame.gameplayservice.model.SmurfFlagList;
import com.necpgame.gameplayservice.model.SmurfReviewRequest;
import com.necpgame.gameplayservice.model.Tier;
import com.necpgame.gameplayservice.model.TierConfig;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for MatchmakingService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface MatchmakingService {

    /**
     * POST /matchmaking/ratings/{activityType}/delta : Применить изменение рейтинга после матча
     * Обработчик от матчинга. Идемпотентен по &#x60;matchId&#x60; + &#x60;playerId&#x60;. Ограничение: максимум 5 запросов на матч.
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param ratingDeltaRequest  (required)
     * @return RatingDeltaResult
     */
    RatingDeltaResult applyRatingDelta(ActivityType activityType, RatingDeltaRequest ratingDeltaRequest);

    /**
     * GET /matchmaking/leaderboard/{activityType} : Получить лидерборд активности
     * Возвращает страницу лидерборда. Кэшируется 60 секунд, поддерживает ETag.
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param leagueId Идентификатор сезона/лиги. По умолчанию текущий активный сезон. (optional)
     * @param region Регион лидерборда/аналитики. (optional)
     * @param tier  (optional)
     * @param page Номер страницы (начиная с 1). (optional, default to 1)
     * @param pageSize Размер страницы лидерборда (максимум 100). (optional, default to 50)
     * @return LeaderboardPage
     */
    LeaderboardPage getLeaderboard(ActivityType activityType, String leagueId, String region, Tier tier, Integer page, Integer pageSize);

    /**
     * GET /matchmaking/ratings/{activityType}/history : Получить историю изменений рейтинга
     * Возвращает историю рейтинга с пагинацией по курсору.
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param leagueId Идентификатор сезона/лиги. По умолчанию текущий активный сезон. (optional)
     * @param cursor Курсор пагинации истории рейтинга. (optional)
     * @param limit Количество записей (максимум 200). (optional, default to 100)
     * @return RatingHistoryPage
     */
    RatingHistoryPage getRatingHistory(ActivityType activityType, String leagueId, String cursor, Integer limit);

    /**
     * GET /matchmaking/ratings/{activityType} : Получить профиль рейтинга игрока для активности
     * Возвращает актуальный рейтинг игрока в выбранном режиме. Требует scope &#x60;matchmaking.ratings.read&#x60;.
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param leagueId Идентификатор сезона/лиги. По умолчанию текущий активный сезон. (optional)
     * @return RatingProfile
     */
    RatingProfile getRatingProfile(ActivityType activityType, String leagueId);

    /**
     * GET /matchmaking/ratings/{activityType}/summary : Получить агрегированную сезонную статистику
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param leagueId Идентификатор сезона/лиги. По умолчанию текущий активный сезон. (optional)
     * @param region Регион лидерборда/аналитики. (optional)
     * @return SeasonSummary
     */
    SeasonSummary getSeasonSummary(ActivityType activityType, String leagueId, String region);

    /**
     * GET /matchmaking/ratings/{activityType}/tiers : Получить конфигурацию рангов
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param leagueId Идентификатор сезона/лиги. По умолчанию текущий активный сезон. (optional)
     * @return TierConfig
     */
    TierConfig getTierConfig(ActivityType activityType, String leagueId);

    /**
     * GET /matchmaking/ratings/{activityType}/smurf-flags : Получить список подозрительных аккаунтов (smurf)
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param threshold Минимальный smurf score (0-1) для фильтрации. (optional, default to 0.75)
     * @param limit Количество записей (максимум 200). (optional, default to 100)
     * @return SmurfFlagList
     */
    SmurfFlagList listSmurfFlags(ActivityType activityType, Float threshold, Integer limit);

    /**
     * POST /matchmaking/ratings/{activityType}/seasons/reset : Инициировать сезонный сброс рейтингов
     * Админ операция со scope &#x60;matchmaking.ratings.manage&#x60;. Запускает асинхронный процесс сброса.
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param seasonResetRequest  (required)
     * @return Void
     */
    Void resetSeasonRatings(ActivityType activityType, SeasonResetRequest seasonResetRequest);

    /**
     * POST /matchmaking/ratings/{activityType}/smurf-review : Подтвердить или отклонить smurf-флаг
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param smurfReviewRequest  (required)
     * @return Void
     */
    Void reviewSmurfFlag(ActivityType activityType, SmurfReviewRequest smurfReviewRequest);

    /**
     * POST /matchmaking/ratings/{activityType}/placement : Обновить статус placement-серии
     *
     * @param activityType Тип активности (PvP/PvE режим). (required)
     * @param placementRequest  (required)
     * @return PlacementStatus
     */
    PlacementStatus updatePlacementStatus(ActivityType activityType, PlacementRequest placementRequest);
}

