package com.necpgame.socialservice.service;

import com.necpgame.socialservice.model.CreateVoiceLobbyRequest;
import com.necpgame.socialservice.model.Error;
import com.necpgame.socialservice.model.JoinLobbyRequest;
import com.necpgame.socialservice.model.JoinLobbyResponse;
import com.necpgame.socialservice.model.LeaveLobbyResponse;
import com.necpgame.socialservice.model.LockLobbyRequest;
import com.necpgame.socialservice.model.MatchmakingRequest;
import com.necpgame.socialservice.model.MatchmakingStatus;
import com.necpgame.socialservice.model.ModerationActionRequest;
import com.necpgame.socialservice.model.ModerationResult;
import org.springframework.lang.Nullable;
import com.necpgame.socialservice.model.ReadyCheckState;
import com.necpgame.socialservice.model.SubchannelList;
import com.necpgame.socialservice.model.SubchannelMutationRequest;
import com.necpgame.socialservice.model.VoiceLobbyAnalytics;
import com.necpgame.socialservice.model.VoiceLobbyDetails;
import com.necpgame.socialservice.model.VoiceLobbyListResponse;
import com.necpgame.socialservice.model.VoiceSettings;
import com.necpgame.socialservice.model.VoiceSettingsPatch;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for SocialService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface SocialService {

    /**
     * GET /social/voice-lobbies/analytics/overview : Аналитика заполненности и качества
     *
     * @param activityCode  (optional)
     * @param region  (optional)
     * @param range  (optional, default to 1h)
     * @return VoiceLobbyAnalytics
     */
    VoiceLobbyAnalytics socialVoiceLobbiesAnalyticsOverviewGet(String activityCode, String region, String range);

    /**
     * GET /social/voice-lobbies : Список голосовых лобби
     *
     * @param activityCode  (optional)
     * @param lobbyType  (optional)
     * @param role  (optional)
     * @param region  (optional)
     * @param language  (optional)
     * @param status  (optional)
     * @param minRating  (optional)
     * @param maxRating  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return VoiceLobbyListResponse
     */
    VoiceLobbyListResponse socialVoiceLobbiesGet(String activityCode, String lobbyType, String role, String region, String language, String status, Integer minRating, Integer maxRating, Integer page, Integer pageSize);

    /**
     * GET /social/voice-lobbies/{lobbyId} : Получить детали лобби
     *
     * @param lobbyId  (required)
     * @return VoiceLobbyDetails
     */
    VoiceLobbyDetails socialVoiceLobbiesLobbyIdGet(String lobbyId);

    /**
     * POST /social/voice-lobbies/{lobbyId}/join : Присоединиться к лобби
     *
     * @param lobbyId  (required)
     * @param joinLobbyRequest  (required)
     * @return JoinLobbyResponse
     */
    JoinLobbyResponse socialVoiceLobbiesLobbyIdJoinPost(String lobbyId, JoinLobbyRequest joinLobbyRequest);

    /**
     * POST /social/voice-lobbies/{lobbyId}/leave : Покинуть лобби
     *
     * @param lobbyId  (required)
     * @return LeaveLobbyResponse
     */
    LeaveLobbyResponse socialVoiceLobbiesLobbyIdLeavePost(String lobbyId);

    /**
     * PATCH /social/voice-lobbies/{lobbyId}/lock : Заблокировать или открыть лобби
     *
     * @param lobbyId  (required)
     * @param lockLobbyRequest  (required)
     * @return VoiceLobbyDetails
     */
    VoiceLobbyDetails socialVoiceLobbiesLobbyIdLockPatch(String lobbyId, LockLobbyRequest lockLobbyRequest);

    /**
     * POST /social/voice-lobbies/{lobbyId}/moderation/actions : Выполнить модерационное действие
     *
     * @param lobbyId  (required)
     * @param moderationActionRequest  (required)
     * @return ModerationResult
     */
    ModerationResult socialVoiceLobbiesLobbyIdModerationActionsPost(String lobbyId, ModerationActionRequest moderationActionRequest);

    /**
     * POST /social/voice-lobbies/{lobbyId}/ready-check : Запустить ready-check
     *
     * @param lobbyId  (required)
     * @return ReadyCheckState
     */
    ReadyCheckState socialVoiceLobbiesLobbyIdReadyCheckPost(String lobbyId);

    /**
     * POST /social/voice-lobbies/{lobbyId}/subchannels : Управлять подканалами
     *
     * @param lobbyId  (required)
     * @param subchannelMutationRequest  (required)
     * @return SubchannelList
     */
    SubchannelList socialVoiceLobbiesLobbyIdSubchannelsPost(String lobbyId, SubchannelMutationRequest subchannelMutationRequest);

    /**
     * PATCH /social/voice-lobbies/{lobbyId}/voice-settings : Изменить параметры связи
     *
     * @param lobbyId  (required)
     * @param voiceSettingsPatch  (required)
     * @return VoiceSettings
     */
    VoiceSettings socialVoiceLobbiesLobbyIdVoiceSettingsPatch(String lobbyId, VoiceSettingsPatch voiceSettingsPatch);

    /**
     * POST /social/voice-lobbies/matchmaking/search : Запросить подбор лобби
     *
     * @param matchmakingRequest  (required)
     * @return MatchmakingStatus
     */
    MatchmakingStatus socialVoiceLobbiesMatchmakingSearchPost(MatchmakingRequest matchmakingRequest);

    /**
     * GET /social/voice-lobbies/matchmaking/status/{requestId} : Проверить статус подбора
     *
     * @param requestId  (required)
     * @return MatchmakingStatus
     */
    MatchmakingStatus socialVoiceLobbiesMatchmakingStatusRequestIdGet(String requestId);

    /**
     * POST /social/voice-lobbies : Создать голосовое лобби
     *
     * @param createVoiceLobbyRequest  (required)
     * @return VoiceLobbyDetails
     */
    VoiceLobbyDetails socialVoiceLobbiesPost(CreateVoiceLobbyRequest createVoiceLobbyRequest);
}

