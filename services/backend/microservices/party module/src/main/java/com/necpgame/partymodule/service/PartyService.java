package com.necpgame.partymodule.service;

import com.necpgame.partymodule.model.Error;
import com.necpgame.partymodule.model.LootDistributeRequest;
import com.necpgame.partymodule.model.LootSettings;
import com.necpgame.partymodule.model.LootSettingsUpdateRequest;
import org.springframework.lang.Nullable;
import com.necpgame.partymodule.model.PartyCreateRequest;
import com.necpgame.partymodule.model.PartyDetail;
import com.necpgame.partymodule.model.PartyDisbandRequest;
import com.necpgame.partymodule.model.PartyError;
import com.necpgame.partymodule.model.PartyInviteRequest;
import com.necpgame.partymodule.model.PartyInvitesResponse;
import com.necpgame.partymodule.model.PartyMemberAddRequest;
import com.necpgame.partymodule.model.PartyMemberUpdateRequest;
import com.necpgame.partymodule.model.PartyMembersResponse;
import com.necpgame.partymodule.model.PartyQuestsResponse;
import com.necpgame.partymodule.model.PartyQueueRequest;
import com.necpgame.partymodule.model.PartySearchResponse;
import com.necpgame.partymodule.model.PartyStatus;
import com.necpgame.partymodule.model.PartySummary;
import com.necpgame.partymodule.model.PartyUpdateRequest;
import com.necpgame.partymodule.model.QuestSyncRequest;
import com.necpgame.partymodule.model.ReadyCheck;
import com.necpgame.partymodule.model.ReadyCheckRequest;
import com.necpgame.partymodule.model.ReadyCheckResponseRequest;
import com.necpgame.partymodule.model.VoteKickRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for PartyService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface PartyService {

    /**
     * POST /party/invites/{inviteId}/accept : Принять приглашение
     *
     * @param inviteId Идентификатор приглашения (required)
     * @return Void
     */
    Void partyInvitesInviteIdAcceptPost(String inviteId);

    /**
     * POST /party/invites/{inviteId}/decline : Отклонить приглашение
     *
     * @param inviteId Идентификатор приглашения (required)
     * @return Void
     */
    Void partyInvitesInviteIdDeclinePost(String inviteId);

    /**
     * DELETE /party/{partyId} : Распустить группу
     *
     * @param partyId Идентификатор party (required)
     * @param partyDisbandRequest  (required)
     * @return Void
     */
    Void partyPartyIdDelete(String partyId, PartyDisbandRequest partyDisbandRequest);

    /**
     * GET /party/{partyId} : Получить состояние party
     *
     * @param partyId Идентификатор party (required)
     * @return PartyDetail
     */
    PartyDetail partyPartyIdGet(String partyId);

    /**
     * GET /party/{partyId}/invites : Получить активные приглашения
     *
     * @param partyId Идентификатор party (required)
     * @return PartyInvitesResponse
     */
    PartyInvitesResponse partyPartyIdInvitesGet(String partyId);

    /**
     * POST /party/{partyId}/invites : Отправить приглашение
     *
     * @param partyId Идентификатор party (required)
     * @param partyInviteRequest  (required)
     * @return Void
     */
    Void partyPartyIdInvitesPost(String partyId, PartyInviteRequest partyInviteRequest);

    /**
     * POST /party/{partyId}/loot/distribute : Распределить предмет лута
     *
     * @param partyId Идентификатор party (required)
     * @param lootDistributeRequest  (required)
     * @return Void
     */
    Void partyPartyIdLootDistributePost(String partyId, LootDistributeRequest lootDistributeRequest);

    /**
     * GET /party/{partyId}/loot : Получить текущие настройки лута
     *
     * @param partyId Идентификатор party (required)
     * @return LootSettings
     */
    LootSettings partyPartyIdLootGet(String partyId);

    /**
     * POST /party/{partyId}/loot : Обновить настройки лута
     *
     * @param partyId Идентификатор party (required)
     * @param lootSettingsUpdateRequest  (required)
     * @return Void
     */
    Void partyPartyIdLootPost(String partyId, LootSettingsUpdateRequest lootSettingsUpdateRequest);

    /**
     * GET /party/{partyId}/members : Список участников party
     *
     * @param partyId Идентификатор party (required)
     * @return PartyMembersResponse
     */
    PartyMembersResponse partyPartyIdMembersGet(String partyId);

    /**
     * DELETE /party/{partyId}/members/{memberId} : Исключить участника или выйти
     *
     * @param partyId Идентификатор party (required)
     * @param memberId Идентификатор участника party (required)
     * @return Void
     */
    Void partyPartyIdMembersMemberIdDelete(String partyId, String memberId);

    /**
     * PATCH /party/{partyId}/members/{memberId} : Обновить роль или лидера участника
     *
     * @param partyId Идентификатор party (required)
     * @param memberId Идентификатор участника party (required)
     * @param partyMemberUpdateRequest  (required)
     * @return Void
     */
    Void partyPartyIdMembersMemberIdPatch(String partyId, String memberId, PartyMemberUpdateRequest partyMemberUpdateRequest);

    /**
     * POST /party/{partyId}/members : Добавить участника (по коду или автоматический join)
     *
     * @param partyId Идентификатор party (required)
     * @param partyMemberAddRequest  (required)
     * @return Void
     */
    Void partyPartyIdMembersPost(String partyId, PartyMemberAddRequest partyMemberAddRequest);

    /**
     * PATCH /party/{partyId} : Обновить настройки party
     *
     * @param partyId Идентификатор party (required)
     * @param partyUpdateRequest  (required)
     * @return Void
     */
    Void partyPartyIdPatch(String partyId, PartyUpdateRequest partyUpdateRequest);

    /**
     * GET /party/{partyId}/quests : Совместный прогресс квестов
     *
     * @param partyId Идентификатор party (required)
     * @return PartyQuestsResponse
     */
    PartyQuestsResponse partyPartyIdQuestsGet(String partyId);

    /**
     * POST /party/{partyId}/quests/{questId}/sync : Синхронизировать шаг квеста
     *
     * @param partyId Идентификатор party (required)
     * @param questId Идентификатор квеста (required)
     * @param questSyncRequest  (required)
     * @return Void
     */
    Void partyPartyIdQuestsQuestIdSyncPost(String partyId, String questId, QuestSyncRequest questSyncRequest);

    /**
     * DELETE /party/{partyId}/queue : Отменить очередь
     *
     * @param partyId Идентификатор party (required)
     * @return Void
     */
    Void partyPartyIdQueueDelete(String partyId);

    /**
     * POST /party/{partyId}/queue : Поставить party в очередь матчмейкинга
     *
     * @param partyId Идентификатор party (required)
     * @param partyQueueRequest  (required)
     * @return Void
     */
    Void partyPartyIdQueuePost(String partyId, PartyQueueRequest partyQueueRequest);

    /**
     * POST /party/{partyId}/ready-check : Запустить ready-check
     *
     * @param partyId Идентификатор party (required)
     * @param readyCheckRequest  (required)
     * @return ReadyCheck
     */
    ReadyCheck partyPartyIdReadyCheckPost(String partyId, ReadyCheckRequest readyCheckRequest);

    /**
     * POST /party/{partyId}/ready-check/respond : Ответить на ready-check
     *
     * @param partyId Идентификатор party (required)
     * @param readyCheckResponseRequest  (required)
     * @return Void
     */
    Void partyPartyIdReadyCheckRespondPost(String partyId, ReadyCheckResponseRequest readyCheckResponseRequest);

    /**
     * GET /party/{partyId}/status : Получить статус party
     *
     * @param partyId Идентификатор party (required)
     * @return PartyStatus
     */
    PartyStatus partyPartyIdStatusGet(String partyId);

    /**
     * POST /party/{partyId}/vote-kick : Инициировать vote-kick
     *
     * @param partyId Идентификатор party (required)
     * @param voteKickRequest  (required)
     * @return Void
     */
    Void partyPartyIdVoteKickPost(String partyId, VoteKickRequest voteKickRequest);

    /**
     * POST /party : Создать новую party
     *
     * @param partyCreateRequest  (required)
     * @return PartySummary
     */
    PartySummary partyPost(PartyCreateRequest partyCreateRequest);

    /**
     * GET /party/search : Поиск открытых групп
     *
     * @param visibility  (optional)
     * @param contentType  (optional)
     * @param requiredRole  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return PartySearchResponse
     */
    PartySearchResponse partySearchGet(String visibility, String contentType, String requiredRole, Integer page, Integer pageSize);
}

