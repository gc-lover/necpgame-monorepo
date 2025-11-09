package com.necpgame.backjava.service;

import com.necpgame.backjava.model.AllianceRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GuildApplicationDecisionRequest;
import com.necpgame.backjava.model.GuildApplicationRequest;
import com.necpgame.backjava.model.GuildApplicationsResponse;
import com.necpgame.backjava.model.GuildAttendanceRequest;
import com.necpgame.backjava.model.GuildAuditResponse;
import com.necpgame.backjava.model.GuildBankResponse;
import com.necpgame.backjava.model.GuildBankTransactionRequest;
import com.necpgame.backjava.model.GuildCreateRequest;
import com.necpgame.backjava.model.GuildDetail;
import com.necpgame.backjava.model.GuildDisbandRequest;
import com.necpgame.backjava.model.GuildError;
import com.necpgame.backjava.model.GuildEventCreateRequest;
import com.necpgame.backjava.model.GuildEventsResponse;
import com.necpgame.backjava.model.GuildMemberAddRequest;
import com.necpgame.backjava.model.GuildMemberUpdateRequest;
import com.necpgame.backjava.model.GuildMembersResponse;
import com.necpgame.backjava.model.GuildPerkUnlockRequest;
import com.necpgame.backjava.model.GuildProgression;
import com.necpgame.backjava.model.GuildRankUpdateRequest;
import com.necpgame.backjava.model.GuildRanksResponse;
import com.necpgame.backjava.model.GuildSearchResponse;
import com.necpgame.backjava.model.GuildSummary;
import com.necpgame.backjava.model.GuildUpdateRequest;
import com.necpgame.backjava.model.GuildWarStatusResponse;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GuildsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GuildsService {

    /**
     * GET /guilds : Поиск гильдий
     *
     * @param query  (optional)
     * @param language  (optional)
     * @param playstyle  (optional)
     * @param shard  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return GuildSearchResponse
     */
    GuildSearchResponse guildsGet(String query, String language, String playstyle, String shard, Integer page, Integer pageSize);

    /**
     * POST /guilds/{guildId}/alliances : Подать заявку на союз
     *
     * @param guildId Идентификатор гильдии (required)
     * @param allianceRequest  (required)
     * @return Void
     */
    Void guildsGuildIdAlliancesPost(String guildId, AllianceRequest allianceRequest);

    /**
     * POST /guilds/{guildId}/applications/{applicationId}/approve : Принять или отклонить заявку
     *
     * @param guildId Идентификатор гильдии (required)
     * @param applicationId Идентификатор заявки (required)
     * @param guildApplicationDecisionRequest  (required)
     * @return Void
     */
    Void guildsGuildIdApplicationsApplicationIdApprovePost(String guildId, String applicationId, GuildApplicationDecisionRequest guildApplicationDecisionRequest);

    /**
     * GET /guilds/{guildId}/applications : Получить заявки
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildApplicationsResponse
     */
    GuildApplicationsResponse guildsGuildIdApplicationsGet(String guildId);

    /**
     * POST /guilds/{guildId}/applications : Подать заявку в гильдию
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildApplicationRequest  (required)
     * @return Void
     */
    Void guildsGuildIdApplicationsPost(String guildId, GuildApplicationRequest guildApplicationRequest);

    /**
     * GET /guilds/{guildId}/audit : Журнал действий гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildAuditResponse
     */
    GuildAuditResponse guildsGuildIdAuditGet(String guildId);

    /**
     * GET /guilds/{guildId}/bank : Статус гильдейского банка
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildBankResponse
     */
    GuildBankResponse guildsGuildIdBankGet(String guildId);

    /**
     * POST /guilds/{guildId}/bank/transactions : Внести или вывести средства
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildBankTransactionRequest  (required)
     * @return Void
     */
    Void guildsGuildIdBankTransactionsPost(String guildId, GuildBankTransactionRequest guildBankTransactionRequest);

    /**
     * DELETE /guilds/{guildId} : Распустить гильдию
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildDisbandRequest  (required)
     * @return Void
     */
    Void guildsGuildIdDelete(String guildId, GuildDisbandRequest guildDisbandRequest);

    /**
     * POST /guilds/{guildId}/events/{eventId}/attendance : Подтвердить участие
     *
     * @param guildId Идентификатор гильдии (required)
     * @param eventId Идентификатор гильдейского события (required)
     * @param guildAttendanceRequest  (required)
     * @return Void
     */
    Void guildsGuildIdEventsEventIdAttendancePost(String guildId, String eventId, GuildAttendanceRequest guildAttendanceRequest);

    /**
     * GET /guilds/{guildId}/events : Получить расписание событий
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildEventsResponse
     */
    GuildEventsResponse guildsGuildIdEventsGet(String guildId);

    /**
     * POST /guilds/{guildId}/events : Создать событие
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildEventCreateRequest  (required)
     * @return Void
     */
    Void guildsGuildIdEventsPost(String guildId, GuildEventCreateRequest guildEventCreateRequest);

    /**
     * GET /guilds/{guildId} : Получить информацию о гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildDetail
     */
    GuildDetail guildsGuildIdGet(String guildId);

    /**
     * GET /guilds/{guildId}/members : Список участников
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildMembersResponse
     */
    GuildMembersResponse guildsGuildIdMembersGet(String guildId);

    /**
     * DELETE /guilds/{guildId}/members/{memberId} : Исключить участника / добровольный выход
     *
     * @param guildId Идентификатор гильдии (required)
     * @param memberId Участник гильдии (required)
     * @return Void
     */
    Void guildsGuildIdMembersMemberIdDelete(String guildId, String memberId);

    /**
     * PATCH /guilds/{guildId}/members/{memberId} : Обновить роль участника
     *
     * @param guildId Идентификатор гильдии (required)
     * @param memberId Участник гильдии (required)
     * @param guildMemberUpdateRequest  (required)
     * @return Void
     */
    Void guildsGuildIdMembersMemberIdPatch(String guildId, String memberId, GuildMemberUpdateRequest guildMemberUpdateRequest);

    /**
     * POST /guilds/{guildId}/members : Пригласить или добавить участника
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildMemberAddRequest  (required)
     * @return Void
     */
    Void guildsGuildIdMembersPost(String guildId, GuildMemberAddRequest guildMemberAddRequest);

    /**
     * PATCH /guilds/{guildId} : Обновить настройки гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildUpdateRequest  (required)
     * @return Void
     */
    Void guildsGuildIdPatch(String guildId, GuildUpdateRequest guildUpdateRequest);

    /**
     * GET /guilds/{guildId}/progression : Получить прогрессию гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildProgression
     */
    GuildProgression guildsGuildIdProgressionGet(String guildId);

    /**
     * POST /guilds/{guildId}/progression/upgrade : Разблокировать перк или исследование
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildPerkUnlockRequest  (required)
     * @return Void
     */
    Void guildsGuildIdProgressionUpgradePost(String guildId, GuildPerkUnlockRequest guildPerkUnlockRequest);

    /**
     * GET /guilds/{guildId}/ranks : Получить ранги гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildRanksResponse
     */
    GuildRanksResponse guildsGuildIdRanksGet(String guildId);

    /**
     * PUT /guilds/{guildId}/ranks : Обновить ранги гильдии
     *
     * @param guildId Идентификатор гильдии (required)
     * @param guildRankUpdateRequest  (required)
     * @return Void
     */
    Void guildsGuildIdRanksPut(String guildId, GuildRankUpdateRequest guildRankUpdateRequest);

    /**
     * GET /guilds/{guildId}/wars : Статус войн и союзов
     *
     * @param guildId Идентификатор гильдии (required)
     * @return GuildWarStatusResponse
     */
    GuildWarStatusResponse guildsGuildIdWarsGet(String guildId);

    /**
     * POST /guilds : Создать гильдию
     *
     * @param guildCreateRequest  (required)
     * @return GuildSummary
     */
    GuildSummary guildsPost(GuildCreateRequest guildCreateRequest);
}

