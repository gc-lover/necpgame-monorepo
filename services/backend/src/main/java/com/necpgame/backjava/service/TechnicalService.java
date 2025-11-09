package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetNotifications200Response;
import com.necpgame.backjava.model.GetResetHistory200Response;
import com.necpgame.backjava.model.MarkAllNotificationsReadRequest;
import com.necpgame.backjava.model.PlayerResetStatus;
import com.necpgame.backjava.model.ResetExecutionResult;
import com.necpgame.backjava.model.ResetSchedule;
import com.necpgame.backjava.model.ResetStatusResponse;
import com.necpgame.backjava.model.ResetTypeStatus;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.SendNotification200Response;
import com.necpgame.backjava.model.SendNotificationRequest;
import com.necpgame.backjava.model.TriggerResetRequest;
import java.util.UUID;
import com.necpgame.backjava.model.UpdateScheduleRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TechnicalService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TechnicalService {

    /**
     * GET /technical/notifications : Получить уведомления
     * Возвращает список уведомлений. Поддерживает фильтрацию и пагинацию. 
     *
     * @param playerId  (required)
     * @param unreadOnly  (optional, default to false)
     * @param type  (optional)
     * @param page  (optional, default to 1)
     * @param limit  (optional, default to 50)
     * @return GetNotifications200Response
     */
    GetNotifications200Response getNotifications(String playerId, @Nullable Boolean unreadOnly, @Nullable String type, @Nullable Integer page, @Nullable Integer limit);

    /**
     * POST /technical/notifications/mark-all-read : Отметить все как прочитанные
     * Отмечает все уведомления как прочитанные
     *
     * @param markAllNotificationsReadRequest  (required)
     * @return Object
     */
    Object markAllNotificationsRead(MarkAllNotificationsReadRequest markAllNotificationsReadRequest);

    /**
     * POST /technical/notifications/{notification_id}/mark-read : Отметить как прочитанное
     * Отмечает уведомление как прочитанное
     *
     * @param notificationId  (required)
     * @return Object
     */
    Object markNotificationRead(String notificationId);

    /**
     * POST /technical/notifications/send : Отправить уведомление
     * Отправляет уведомление игроку. Используется системой для различных событий. 
     *
     * @param sendNotificationRequest  (required)
     * @return SendNotification200Response
     */
    SendNotification200Response sendNotification(SendNotificationRequest sendNotificationRequest);

    /**
     * GET /technical/resets/player/{player_id} : Получить статус сбросов для игрока
     * Что было сброшено, что доступно для сброса
     *
     * @param playerId  (required)
     * @return PlayerResetStatus
     */
    PlayerResetStatus getPlayerResetStatus(UUID playerId);

    /**
     * GET /technical/resets/history : Получить историю сбросов
     *
     * @param resetType  (optional)
     * @param days За сколько дней показать историю (optional, default to 7)
     * @return GetResetHistory200Response
     */
    GetResetHistory200Response getResetHistory(@Nullable String resetType, @Nullable Integer days);

    /**
     * GET /technical/resets/schedule : Получить расписание всех сбросов
     *
     * @return ResetSchedule
     */
    ResetSchedule getResetSchedule();

    /**
     * GET /technical/resets/status : Получить статус всех сбросов
     * Когда был последний сброс и когда будет следующий
     *
     * @return ResetStatusResponse
     */
    ResetStatusResponse getResetStatus();

    /**
     * GET /technical/resets/status/{reset_type} : Получить статус конкретного типа сброса
     *
     * @param resetType  (required)
     * @return ResetTypeStatus
     */
    ResetTypeStatus getResetTypeStatus(String resetType);

    /**
     * POST /technical/resets/admin/trigger : Принудительно запустить сброс
     * Только для администраторов. Запускает сброс вручную.
     *
     * @param triggerResetRequest  (required)
     * @return ResetExecutionResult
     */
    ResetExecutionResult triggerReset(TriggerResetRequest triggerResetRequest);

    /**
     * PUT /technical/resets/admin/schedule : Обновить расписание сброса
     * Только для администраторов
     *
     * @param updateScheduleRequest  (required)
     * @return Void
     */
    Void updateResetSchedule(UpdateScheduleRequest updateScheduleRequest);
}

