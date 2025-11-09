package com.necpgame.mailservice.service;

import com.necpgame.mailservice.model.AttachmentClaimRequest;
import com.necpgame.mailservice.model.AttachmentClaimResponse;
import com.necpgame.mailservice.model.CODPaymentRequest;
import com.necpgame.mailservice.model.CODPaymentResponse;
import com.necpgame.mailservice.model.Error;
import com.necpgame.mailservice.model.MailDetail;
import com.necpgame.mailservice.model.MailError;
import com.necpgame.mailservice.model.MailFlagRequest;
import com.necpgame.mailservice.model.MailHistoryResponse;
import com.necpgame.mailservice.model.MailListResponse;
import com.necpgame.mailservice.model.MailReturnRequest;
import com.necpgame.mailservice.model.MailSendRequest;
import com.necpgame.mailservice.model.MailSettings;
import com.necpgame.mailservice.model.MailSettingsUpdateRequest;
import com.necpgame.mailservice.model.MailStats;
import org.springframework.lang.Nullable;
import com.necpgame.mailservice.model.SystemMailBatchRequest;
import com.necpgame.mailservice.model.SystemMailRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for MailService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface MailService {

    /**
     * GET /mail/history : Журнал операций почты
     *
     * @param type  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return MailHistoryResponse
     */
    MailHistoryResponse mailHistoryGet(String type, Integer page, Integer pageSize);

    /**
     * GET /mail/inbox : Список входящих писем
     *
     * @param unread  (optional)
     * @param attachments  (optional)
     * @param from  (optional)
     * @param system  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return MailListResponse
     */
    MailListResponse mailInboxGet(Boolean unread, Boolean attachments, String from, Boolean system, Integer page, Integer pageSize);

    /**
     * POST /mail/{mailId}/attachments/claim : Забрать вложения письма
     *
     * @param mailId Идентификатор письма (required)
     * @param attachmentClaimRequest  (required)
     * @return AttachmentClaimResponse
     */
    AttachmentClaimResponse mailMailIdAttachmentsClaimPost(String mailId, AttachmentClaimRequest attachmentClaimRequest);

    /**
     * POST /mail/{mailId}/cod/pay : Оплатить COD и получить вложения
     *
     * @param mailId Идентификатор письма (required)
     * @param coDPaymentRequest  (required)
     * @return CODPaymentResponse
     */
    CODPaymentResponse mailMailIdCodPayPost(String mailId, CODPaymentRequest coDPaymentRequest);

    /**
     * DELETE /mail/{mailId} : Удалить письмо из почтового ящика
     *
     * @param mailId Идентификатор письма (required)
     * @return Void
     */
    Void mailMailIdDelete(String mailId);

    /**
     * POST /mail/{mailId}/flag : Пожаловаться на письмо
     *
     * @param mailId Идентификатор письма (required)
     * @param mailFlagRequest  (required)
     * @return Void
     */
    Void mailMailIdFlagPost(String mailId, MailFlagRequest mailFlagRequest);

    /**
     * GET /mail/{mailId} : Получить детали письма
     *
     * @param mailId Идентификатор письма (required)
     * @return MailDetail
     */
    MailDetail mailMailIdGet(String mailId);

    /**
     * POST /mail/{mailId}/read : Пометить письмо как прочитанное
     *
     * @param mailId Идентификатор письма (required)
     * @return Void
     */
    Void mailMailIdReadPost(String mailId);

    /**
     * POST /mail/{mailId}/resend : Повторно отправить письмо
     *
     * @param mailId Идентификатор письма (required)
     * @return Void
     */
    Void mailMailIdResendPost(String mailId);

    /**
     * POST /mail/{mailId}/return : Вернуть письмо отправителю
     *
     * @param mailId Идентификатор письма (required)
     * @param mailReturnRequest  (required)
     * @return Void
     */
    Void mailMailIdReturnPost(String mailId, MailReturnRequest mailReturnRequest);

    /**
     * GET /mail/outbox : Список отправленных писем
     *
     * @param status  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return MailListResponse
     */
    MailListResponse mailOutboxGet(String status, Integer page, Integer pageSize);

    /**
     * POST /mail : Отправить письмо
     *
     * @param mailSendRequest  (required)
     * @return MailDetail
     */
    MailDetail mailPost(MailSendRequest mailSendRequest);

    /**
     * GET /mail/settings : Получить почтовые настройки
     *
     * @return MailSettings
     */
    MailSettings mailSettingsGet();

    /**
     * PUT /mail/settings : Обновить почтовые настройки
     *
     * @param mailSettingsUpdateRequest  (required)
     * @return Void
     */
    Void mailSettingsPut(MailSettingsUpdateRequest mailSettingsUpdateRequest);

    /**
     * GET /mail/stats : Статистика почтовой активности
     *
     * @param range  (optional)
     * @return MailStats
     */
    MailStats mailStatsGet(String range);

    /**
     * POST /mail/system/batch : Массовая отправка писем
     *
     * @param systemMailBatchRequest  (required)
     * @return Void
     */
    Void mailSystemBatchPost(SystemMailBatchRequest systemMailBatchRequest);

    /**
     * POST /mail/system : Отправить системное письмо или рассылку
     *
     * @param systemMailRequest  (required)
     * @return Void
     */
    Void mailSystemPost(SystemMailRequest systemMailRequest);
}

