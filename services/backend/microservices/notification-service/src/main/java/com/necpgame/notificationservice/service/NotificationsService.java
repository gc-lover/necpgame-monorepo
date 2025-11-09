package com.necpgame.notificationservice.service;

import org.springframework.format.annotation.DateTimeFormat;
import com.necpgame.notificationservice.model.Error;
import com.necpgame.notificationservice.model.NotificationAckRequest;
import com.necpgame.notificationservice.model.NotificationBatchRequest;
import com.necpgame.notificationservice.model.NotificationBatchResponse;
import com.necpgame.notificationservice.model.NotificationDeliveryResponse;
import com.necpgame.notificationservice.model.NotificationDeviceRegisterRequest;
import com.necpgame.notificationservice.model.NotificationDevicesResponse;
import com.necpgame.notificationservice.model.NotificationError;
import com.necpgame.notificationservice.model.NotificationHistoryResponse;
import com.necpgame.notificationservice.model.NotificationInboxResponse;
import com.necpgame.notificationservice.model.NotificationPreferencesResponse;
import com.necpgame.notificationservice.model.NotificationPreferencesUpdateRequest;
import com.necpgame.notificationservice.model.NotificationRetryRequest;
import com.necpgame.notificationservice.model.NotificationSendRequest;
import com.necpgame.notificationservice.model.NotificationSendResponse;
import com.necpgame.notificationservice.model.NotificationTemplateRenderRequest;
import com.necpgame.notificationservice.model.NotificationTemplateRenderResponse;
import com.necpgame.notificationservice.model.NotificationTemplatesResponse;
import com.necpgame.notificationservice.model.NotificationTestRequest;
import com.necpgame.notificationservice.model.NotificationWebhookRequest;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import com.necpgame.notificationservice.model.QuietHours;
import com.necpgame.notificationservice.model.QuietHoursUpdateRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for NotificationsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface NotificationsService {

    /**
     * POST /notifications/ack : Подтвердить получение уведомления
     *
     * @param notificationAckRequest  (required)
     * @return Void
     */
    Void notificationsAckPost(NotificationAckRequest notificationAckRequest);

    /**
     * GET /notifications/delivery : Метрики доставляемости
     *
     * @param notificationId  (optional)
     * @param channel  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return NotificationDeliveryResponse
     */
    NotificationDeliveryResponse notificationsDeliveryGet(String notificationId, String channel, Integer page, Integer pageSize);

    /**
     * DELETE /notifications/devices/{deviceId} : Удалить устройство
     *
     * @param deviceId Идентификатор зарегистрированного устройства (required)
     * @return Void
     */
    Void notificationsDevicesDeviceIdDelete(String deviceId);

    /**
     * GET /notifications/devices : Список устройств игрока
     *
     * @return NotificationDevicesResponse
     */
    NotificationDevicesResponse notificationsDevicesGet();

    /**
     * POST /notifications/devices : Зарегистрировать устройство
     *
     * @param notificationDeviceRegisterRequest  (required)
     * @return Void
     */
    Void notificationsDevicesPost(NotificationDeviceRegisterRequest notificationDeviceRegisterRequest);

    /**
     * GET /notifications/history : История уведомлений
     *
     * @param from  (optional)
     * @param to  (optional)
     * @param search  (optional)
     * @param category  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return NotificationHistoryResponse
     */
    NotificationHistoryResponse notificationsHistoryGet(OffsetDateTime from, OffsetDateTime to, String search, String category, Integer page, Integer pageSize);

    /**
     * GET /notifications/inbox : Получить текущие уведомления игрока
     *
     * @param channel  (optional)
     * @param status  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return NotificationInboxResponse
     */
    NotificationInboxResponse notificationsInboxGet(String channel, String status, Integer page, Integer pageSize);

    /**
     * DELETE /notifications/{notificationId} : Скрыть уведомление
     *
     * @param notificationId Идентификатор уведомления (required)
     * @return Void
     */
    Void notificationsNotificationIdDelete(String notificationId);

    /**
     * GET /notifications/preferences : Получить предпочтения игрока
     *
     * @return NotificationPreferencesResponse
     */
    NotificationPreferencesResponse notificationsPreferencesGet();

    /**
     * PUT /notifications/preferences : Обновить предпочтения
     *
     * @param notificationPreferencesUpdateRequest  (required)
     * @return Void
     */
    Void notificationsPreferencesPut(NotificationPreferencesUpdateRequest notificationPreferencesUpdateRequest);

    /**
     * GET /notifications/quiet-hours : Получить тихие часы игрока
     *
     * @return QuietHours
     */
    QuietHours notificationsQuietHoursGet();

    /**
     * PUT /notifications/quiet-hours : Настроить тихие часы
     *
     * @param quietHoursUpdateRequest  (required)
     * @return Void
     */
    Void notificationsQuietHoursPut(QuietHoursUpdateRequest quietHoursUpdateRequest);

    /**
     * POST /notifications/retry/{notificationId} : Повторить отправку уведомления
     *
     * @param notificationId Идентификатор уведомления (required)
     * @param notificationRetryRequest  (required)
     * @return Void
     */
    Void notificationsRetryNotificationIdPost(String notificationId, NotificationRetryRequest notificationRetryRequest);

    /**
     * POST /notifications/send/batch : Массовая отправка уведомлений
     *
     * @param notificationBatchRequest  (required)
     * @return NotificationBatchResponse
     */
    NotificationBatchResponse notificationsSendBatchPost(NotificationBatchRequest notificationBatchRequest);

    /**
     * POST /notifications/send : Отправить индивидуальное уведомление
     *
     * @param notificationSendRequest  (required)
     * @return NotificationSendResponse
     */
    NotificationSendResponse notificationsSendPost(NotificationSendRequest notificationSendRequest);

    /**
     * GET /notifications/templates : Получить список шаблонов
     *
     * @return NotificationTemplatesResponse
     */
    NotificationTemplatesResponse notificationsTemplatesGet();

    /**
     * POST /notifications/templates/render : Предпросмотр шаблона
     *
     * @param notificationTemplateRenderRequest  (required)
     * @return NotificationTemplateRenderResponse
     */
    NotificationTemplateRenderResponse notificationsTemplatesRenderPost(NotificationTemplateRenderRequest notificationTemplateRenderRequest);

    /**
     * POST /notifications/test : Тестовая отправка шаблона
     *
     * @param notificationTestRequest  (required)
     * @return Void
     */
    Void notificationsTestPost(NotificationTestRequest notificationTestRequest);

    /**
     * POST /notifications/webhooks : Настроить webhook для уведомлений
     *
     * @param notificationWebhookRequest  (required)
     * @return Void
     */
    Void notificationsWebhooksPost(NotificationWebhookRequest notificationWebhookRequest);
}

