package com.necpgame.socialservice.service;

import com.necpgame.socialservice.model.ApiError;
import com.necpgame.socialservice.model.AutoBanTrigger;
import com.necpgame.socialservice.model.ChatBan;
import com.necpgame.socialservice.model.ChatBanPage;
import com.necpgame.socialservice.model.ChatBanRequest;
import com.necpgame.socialservice.model.ChatReportRequest;
import com.necpgame.socialservice.model.ModerationCheckRequest;
import com.necpgame.socialservice.model.ModerationCheckResponse;
import com.necpgame.socialservice.model.ModerationRuleSet;
import org.springframework.lang.Nullable;
import com.necpgame.socialservice.model.ReportDetail;
import com.necpgame.socialservice.model.ReportPage;
import com.necpgame.socialservice.model.ReportResolutionRequest;
import com.necpgame.socialservice.model.ReportTicket;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for ChatService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface ChatService {

    /**
     * POST /chat/moderation/filters/check : Проверить сообщение на нарушения
     *
     * @param moderationCheckRequest  (required)
     * @return ModerationCheckResponse
     */
    ModerationCheckResponse checkMessageModeration(ModerationCheckRequest moderationCheckRequest);

    /**
     * GET /chat/moderation/rules : Получить текущие правила фильтрации
     *
     * @return ModerationRuleSet
     */
    ModerationRuleSet getModerationRules();

    /**
     * POST /chat/ban : Выдать бан игроку
     *
     * @param chatBanRequest  (required)
     * @param xAuditReason  (optional)
     * @param xModeratorId  (optional)
     * @return ChatBan
     */
    ChatBan issueChatBan(ChatBanRequest chatBanRequest, String xAuditReason, UUID xModeratorId);

    /**
     * GET /chat/bans : Получить список банов
     *
     * @param playerId  (optional)
     * @param channelType  (optional)
     * @param includeExpired  (optional, default to false)
     * @param page  (optional, default to 1)
     * @param pageSize  (optional, default to 25)
     * @return ChatBanPage
     */
    ChatBanPage listChatBans(UUID playerId, String channelType, Boolean includeExpired, Integer page, Integer pageSize);

    /**
     * GET /chat/reports : Получить список жалоб
     *
     * @param status  (optional)
     * @param channelType  (optional)
     * @param page  (optional, default to 1)
     * @param pageSize  (optional, default to 25)
     * @return ReportPage
     */
    ReportPage listChatReports(String status, String channelType, Integer page, Integer pageSize);

    /**
     * POST /chat/reports/{reportId}/resolve : Принять решение по жалобе
     *
     * @param reportId Идентификатор жалобы. (required)
     * @param reportResolutionRequest  (required)
     * @param xAuditReason  (optional)
     * @param xModeratorId  (optional)
     * @return ReportDetail
     */
    ReportDetail resolveChatReport(UUID reportId, ReportResolutionRequest reportResolutionRequest, String xAuditReason, UUID xModeratorId);

    /**
     * DELETE /chat/bans/{banId} : Снять бан досрочно
     *
     * @param banId Идентификатор бана. (required)
     * @param xAuditReason  (optional)
     * @return Void
     */
    Void revokeChatBan(UUID banId, String xAuditReason);

    /**
     * POST /chat/report : Подать жалобу на сообщение или игрока
     *
     * @param chatReportRequest  (required)
     * @return ReportTicket
     */
    ReportTicket submitChatReport(ChatReportRequest chatReportRequest);

    /**
     * POST /chat/moderation/auto-ban : Триггер автоматического бана
     *
     * @param autoBanTrigger  (required)
     * @return Void
     */
    Void triggerAutoBan(AutoBanTrigger autoBanTrigger);

    /**
     * PUT /chat/moderation/rules : Обновить правила фильтрации
     *
     * @param moderationRuleSet  (required)
     * @return Void
     */
    Void updateModerationRules(ModerationRuleSet moderationRuleSet);
}

