package com.necpgame.backjava.service;

import com.necpgame.backjava.model.CreateVoiceChannelRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.EventAck;
import com.necpgame.backjava.model.JoinVoiceRequest;
import com.necpgame.backjava.model.JoinVoiceResponse;
import com.necpgame.backjava.model.LeaveVoiceResponse;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.ProviderEvent;
import com.necpgame.backjava.model.ProximityUpdateAccepted;
import com.necpgame.backjava.model.ProximityUpdateRequest;
import com.necpgame.backjava.model.UpdateVoiceChannelRequest;
import com.necpgame.backjava.model.VoiceChannel;
import com.necpgame.backjava.model.VoiceChannelList;
import com.necpgame.backjava.model.VoiceMetrics;
import com.necpgame.backjava.model.VoiceMuteRequest;
import com.necpgame.backjava.model.VoiceParticipant;
import com.necpgame.backjava.model.VoiceParticipantList;
import com.necpgame.backjava.model.VoiceQualityConfig;
import com.necpgame.backjava.model.VoiceQualityRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for SocialVoiceService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface SocialVoiceService {

    /**
     * DELETE /social/voice/channels/{channelId} : Закрыть канал
     *
     * @param channelId  (required)
     * @return Void
     */
    Void socialVoiceChannelsChannelIdDelete(String channelId);

    /**
     * POST /social/voice/channels/{channelId}/events : Вебхук голосового провайдера
     *
     * @param channelId  (required)
     * @param providerEvent  (required)
     * @return EventAck
     */
    EventAck socialVoiceChannelsChannelIdEventsPost(String channelId, ProviderEvent providerEvent);

    /**
     * GET /social/voice/channels/{channelId} : Детали канала
     *
     * @param channelId  (required)
     * @return VoiceChannel
     */
    VoiceChannel socialVoiceChannelsChannelIdGet(String channelId);

    /**
     * POST /social/voice/channels/{channelId}/join : Подключить игрока
     *
     * @param channelId  (required)
     * @param joinVoiceRequest  (required)
     * @return JoinVoiceResponse
     */
    JoinVoiceResponse socialVoiceChannelsChannelIdJoinPost(String channelId, JoinVoiceRequest joinVoiceRequest);

    /**
     * POST /social/voice/channels/{channelId}/leave : Отключить игрока
     *
     * @param channelId  (required)
     * @return LeaveVoiceResponse
     */
    LeaveVoiceResponse socialVoiceChannelsChannelIdLeavePost(String channelId);

    /**
     * GET /social/voice/channels/{channelId}/participants : Список участников
     *
     * @param channelId  (required)
     * @return VoiceParticipantList
     */
    VoiceParticipantList socialVoiceChannelsChannelIdParticipantsGet(String channelId);

    /**
     * POST /social/voice/channels/{channelId}/participants/{playerId}/deafen : Переключить deafen
     *
     * @param channelId  (required)
     * @param playerId  (required)
     * @return VoiceParticipant
     */
    VoiceParticipant socialVoiceChannelsChannelIdParticipantsPlayerIdDeafenPost(String channelId, String playerId);

    /**
     * POST /social/voice/channels/{channelId}/participants/{playerId}/mute : Переключить mute
     *
     * @param channelId  (required)
     * @param playerId  (required)
     * @param voiceMuteRequest  (required)
     * @return VoiceParticipant
     */
    VoiceParticipant socialVoiceChannelsChannelIdParticipantsPlayerIdMutePost(String channelId, String playerId, VoiceMuteRequest voiceMuteRequest);

    /**
     * PATCH /social/voice/channels/{channelId} : Обновить настройки
     *
     * @param channelId  (required)
     * @param updateVoiceChannelRequest  (required)
     * @return VoiceChannel
     */
    VoiceChannel socialVoiceChannelsChannelIdPatch(String channelId, UpdateVoiceChannelRequest updateVoiceChannelRequest);

    /**
     * POST /social/voice/channels/{channelId}/quality : Обновить параметры качества
     *
     * @param channelId  (required)
     * @param voiceQualityRequest  (required)
     * @return VoiceQualityConfig
     */
    VoiceQualityConfig socialVoiceChannelsChannelIdQualityPost(String channelId, VoiceQualityRequest voiceQualityRequest);

    /**
     * GET /social/voice/channels : Список каналов
     *
     * @param ownerType  (optional)
     * @param ownerId  (optional)
     * @param channelType  (optional)
     * @param isActive  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return VoiceChannelList
     */
    VoiceChannelList socialVoiceChannelsGet(String ownerType, String ownerId, String channelType, Boolean isActive, Integer page, Integer pageSize);

    /**
     * POST /social/voice/channels : Создать голосовой канал
     * Создаёт party/guild/raid/proximity канал, проверяя права через party/guild сервисы.
     *
     * @param createVoiceChannelRequest  (required)
     * @return VoiceChannel
     */
    VoiceChannel socialVoiceChannelsPost(CreateVoiceChannelRequest createVoiceChannelRequest);

    /**
     * GET /social/voice/metrics : Метрики качества связи
     *
     * @param channelId  (optional)
     * @param ownerType  (optional)
     * @param range  (optional, default to 1h)
     * @return VoiceMetrics
     */
    VoiceMetrics socialVoiceMetricsGet(String channelId, String ownerType, String range);

    /**
     * POST /social/voice/proximity/update : Обновить координаты proximity
     *
     * @param proximityUpdateRequest  (required)
     * @return ProximityUpdateAccepted
     */
    ProximityUpdateAccepted socialVoiceProximityUpdatePost(ProximityUpdateRequest proximityUpdateRequest);
}

