package com.necpgame.adminservice.service;

import com.necpgame.adminservice.model.AddResponseRequest;
import com.necpgame.adminservice.model.AssignTicketRequest;
import com.necpgame.adminservice.model.AttachmentError;
import com.necpgame.adminservice.model.AttachmentMetadata;
import com.necpgame.adminservice.model.CreateTicketRequest;
import com.necpgame.adminservice.model.CreateTicketResponse;
import org.springframework.format.annotation.DateTimeFormat;
import com.necpgame.adminservice.model.Error;
import com.necpgame.adminservice.model.EscalateTicketRequest;
import com.necpgame.adminservice.model.FeedbackRequest;
import com.necpgame.adminservice.model.IncidentReport;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import com.necpgame.adminservice.model.ReopenTicketRequest;
import com.necpgame.adminservice.model.ResolveTicketRequest;
import com.necpgame.adminservice.model.SlaMetricsResponse;
import com.necpgame.adminservice.model.SupportTicket;
import com.necpgame.adminservice.model.SupportTicketError;
import com.necpgame.adminservice.model.TicketListResponse;
import com.necpgame.adminservice.model.TicketResponse;
import com.necpgame.adminservice.model.TimelineEntry;
import com.necpgame.adminservice.model.UpdateTicketRequest;
import com.necpgame.adminservice.model.WorkloadMetricsResponse;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for AdminService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface AdminService {

    /**
     * POST /admin/support/incidents : Зарегистрировать инцидент поддержки
     * Сообщает incident-service о массовых тикетах или критических проблемах.
     *
     * @param incidentReport  (required)
     * @return Void
     */
    Void adminSupportIncidentsPost(IncidentReport incidentReport);

    /**
     * GET /admin/support/metrics/sla : SLA отчёт
     *
     * @param range  (optional, default to 7d)
     * @return SlaMetricsResponse
     */
    SlaMetricsResponse adminSupportMetricsSlaGet(String range);

    /**
     * GET /admin/support/metrics/workload : Нагрузка агентов
     *
     * @return WorkloadMetricsResponse
     */
    WorkloadMetricsResponse adminSupportMetricsWorkloadGet();

    /**
     * GET /admin/support/tickets : Получить список тикетов
     *
     * @param status  (optional)
     * @param priority  (optional)
     * @param category  (optional)
     * @param agentId  (optional)
     * @param createdFrom  (optional)
     * @param createdTo  (optional)
     * @param search  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return TicketListResponse
     */
    TicketListResponse adminSupportTicketsGet(String status, String priority, String category, String agentId, OffsetDateTime createdFrom, OffsetDateTime createdTo, String search, Integer page, Integer pageSize);

    /**
     * POST /admin/support/tickets : Создать тикет
     * Создаёт тикет от игрока или агента, генерируя номер формата &#x60;NECPG-YYYYMMDD-XXXX&#x60;.
     *
     * @param createTicketRequest  (required)
     * @return CreateTicketResponse
     */
    CreateTicketResponse adminSupportTicketsPost(CreateTicketRequest createTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/assign : Назначить агента
     *
     * @param ticketId UUID тикета (required)
     * @param assignTicketRequest  (required)
     * @return Void
     */
    Void adminSupportTicketsTicketIdAssignPost(String ticketId, AssignTicketRequest assignTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/attachments : Загрузить вложение
     *
     * @param ticketId UUID тикета (required)
     * @param file  (optional)
     * @param description  (optional)
     * @return AttachmentMetadata
     */
    AttachmentMetadata adminSupportTicketsTicketIdAttachmentsPost(String ticketId, org.springframework.core.io.Resource file, String description);

    /**
     * POST /admin/support/tickets/{ticketId}/escalate : Эскалация тикета
     *
     * @param ticketId UUID тикета (required)
     * @param escalateTicketRequest  (required)
     * @return Void
     */
    Void adminSupportTicketsTicketIdEscalatePost(String ticketId, EscalateTicketRequest escalateTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/feedback : Сохранить оценку поддержки
     *
     * @param ticketId UUID тикета (required)
     * @param feedbackRequest  (required)
     * @return Void
     */
    Void adminSupportTicketsTicketIdFeedbackPost(String ticketId, FeedbackRequest feedbackRequest);

    /**
     * GET /admin/support/tickets/{ticketId} : Получить детали тикета
     *
     * @param ticketId UUID тикета (required)
     * @return SupportTicket
     */
    SupportTicket adminSupportTicketsTicketIdGet(String ticketId);

    /**
     * PATCH /admin/support/tickets/{ticketId} : Обновить тикет
     *
     * @param ticketId UUID тикета (required)
     * @param updateTicketRequest  (required)
     * @return SupportTicket
     */
    SupportTicket adminSupportTicketsTicketIdPatch(String ticketId, UpdateTicketRequest updateTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/reopen : Повторно открыть тикет
     *
     * @param ticketId UUID тикета (required)
     * @param reopenTicketRequest  (required)
     * @return Void
     */
    Void adminSupportTicketsTicketIdReopenPost(String ticketId, ReopenTicketRequest reopenTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/resolve : Разрешить тикет
     *
     * @param ticketId UUID тикета (required)
     * @param resolveTicketRequest  (required)
     * @return SupportTicket
     */
    SupportTicket adminSupportTicketsTicketIdResolvePost(String ticketId, ResolveTicketRequest resolveTicketRequest);

    /**
     * POST /admin/support/tickets/{ticketId}/responses : Добавить ответ
     * Добавляет ответ от игрока или агента. Внутренние заметки помечаются &#x60;isInternal&#x60;.
     *
     * @param ticketId UUID тикета (required)
     * @param addResponseRequest  (required)
     * @return TicketResponse
     */
    TicketResponse adminSupportTicketsTicketIdResponsesPost(String ticketId, AddResponseRequest addResponseRequest);

    /**
     * GET /admin/support/tickets/{ticketId}/timeline : Получить таймлайн тикета
     *
     * @param ticketId UUID тикета (required)
     * @return TimelineEntry
     */
    TimelineEntry adminSupportTicketsTicketIdTimelineGet(String ticketId);
}

