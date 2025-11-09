package com.necpgame.systemservice.service;

import com.necpgame.systemservice.model.Error;
import com.necpgame.systemservice.model.MaintenanceActionResponse;
import com.necpgame.systemservice.model.MaintenanceAuditEntry;
import com.necpgame.systemservice.model.MaintenanceAuditResponse;
import com.necpgame.systemservice.model.MaintenanceError;
import com.necpgame.systemservice.model.MaintenanceHookTriggerRequest;
import com.necpgame.systemservice.model.MaintenanceStatus;
import com.necpgame.systemservice.model.MaintenanceStatusPayload;
import com.necpgame.systemservice.model.MaintenanceStatusUpdateRequest;
import com.necpgame.systemservice.model.MaintenanceWindow;
import com.necpgame.systemservice.model.MaintenanceWindowCreateRequest;
import com.necpgame.systemservice.model.MaintenanceWindowList;
import com.necpgame.systemservice.model.MaintenanceWindowUpdateRequest;
import org.springframework.lang.Nullable;
import com.necpgame.systemservice.model.SystemMaintenanceActiveEscalatePostRequest;
import com.necpgame.systemservice.model.SystemMaintenanceWindowsWindowIdCancelPostRequest;
import com.necpgame.systemservice.model.SystemMaintenanceWindowsWindowIdCompletePostRequest;
import com.necpgame.systemservice.model.SystemMaintenanceWindowsWindowIdNotificationsPostRequest;
import com.necpgame.systemservice.model.SystemMaintenanceWindowsWindowIdRollbackPostRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for SystemService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface SystemService {

    /**
     * POST /system/maintenance/active/escalate : Перевести обслуживание в emergency режим
     *
     * @param systemMaintenanceActiveEscalatePostRequest  (required)
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceActiveEscalatePost(SystemMaintenanceActiveEscalatePostRequest systemMaintenanceActiveEscalatePostRequest);

    /**
     * GET /system/maintenance/active : Получить текущее активное обслуживание
     *
     * @return MaintenanceStatus
     */
    MaintenanceStatus systemMaintenanceActiveGet();

    /**
     * POST /system/maintenance/active/pause : Приостановить активное обслуживание
     *
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceActivePausePost();

    /**
     * POST /system/maintenance/active/resume : Возобновить обслуживание
     *
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceActiveResumePost();

    /**
     * GET /system/maintenance/audit : Получить аудит операций
     *
     * @param windowId  (optional)
     * @param actor  (optional)
     * @param action  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return MaintenanceAuditResponse
     */
    MaintenanceAuditResponse systemMaintenanceAuditGet(UUID windowId, String actor, String action, Integer page, Integer pageSize);

    /**
     * POST /system/maintenance/audit : Добавить запись аудита / пост-мортем
     *
     * @param maintenanceAuditEntry  (required)
     * @return MaintenanceAuditEntry
     */
    MaintenanceAuditEntry systemMaintenanceAuditPost(MaintenanceAuditEntry maintenanceAuditEntry);

    /**
     * POST /system/maintenance/hooks/deployment : Триггер интеграции с деплойментом
     *
     * @param maintenanceHookTriggerRequest  (required)
     * @return Void
     */
    Void systemMaintenanceHooksDeploymentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest);

    /**
     * POST /system/maintenance/hooks/incident : Сообщить об инциденте
     *
     * @param maintenanceHookTriggerRequest  (required)
     * @return Void
     */
    Void systemMaintenanceHooksIncidentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest);

    /**
     * GET /system/maintenance/status : Публичный статус для status-page
     *
     * @return MaintenanceStatusPayload
     */
    MaintenanceStatusPayload systemMaintenanceStatusGet();

    /**
     * POST /system/maintenance/status : Обновить статус вручную
     *
     * @param maintenanceStatusUpdateRequest  (required)
     * @return Void
     */
    Void systemMaintenanceStatusPost(MaintenanceStatusUpdateRequest maintenanceStatusUpdateRequest);

    /**
     * GET /system/maintenance/windows : Получить список окон обслуживания
     *
     * @param status  (optional)
     * @param type  (optional)
     * @param environment  (optional)
     * @param service  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return MaintenanceWindowList
     */
    MaintenanceWindowList systemMaintenanceWindowsGet(String status, String type, String environment, String service, Integer page, Integer pageSize);

    /**
     * POST /system/maintenance/windows : Создать новое окно обслуживания
     *
     * @param maintenanceWindowCreateRequest  (required)
     * @return MaintenanceWindow
     */
    MaintenanceWindow systemMaintenanceWindowsPost(MaintenanceWindowCreateRequest maintenanceWindowCreateRequest);

    /**
     * POST /system/maintenance/windows/{windowId}/activate : Запустить обслуживание
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceWindowsWindowIdActivatePost(UUID windowId);

    /**
     * POST /system/maintenance/windows/{windowId}/cancel : Отменить запланированное обслуживание
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @param systemMaintenanceWindowsWindowIdCancelPostRequest  (required)
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceWindowsWindowIdCancelPost(UUID windowId, SystemMaintenanceWindowsWindowIdCancelPostRequest systemMaintenanceWindowsWindowIdCancelPostRequest);

    /**
     * POST /system/maintenance/windows/{windowId}/complete : Завершить обслуживание и опубликовать отчёт
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @param systemMaintenanceWindowsWindowIdCompletePostRequest  (optional)
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceWindowsWindowIdCompletePost(UUID windowId, SystemMaintenanceWindowsWindowIdCompletePostRequest systemMaintenanceWindowsWindowIdCompletePostRequest);

    /**
     * GET /system/maintenance/windows/{windowId} : Получить детали окна обслуживания
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @return MaintenanceWindow
     */
    MaintenanceWindow systemMaintenanceWindowsWindowIdGet(UUID windowId);

    /**
     * POST /system/maintenance/windows/{windowId}/notifications : Запустить уведомления по окну обслуживания вручную
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @param systemMaintenanceWindowsWindowIdNotificationsPostRequest  (required)
     * @return Void
     */
    Void systemMaintenanceWindowsWindowIdNotificationsPost(UUID windowId, SystemMaintenanceWindowsWindowIdNotificationsPostRequest systemMaintenanceWindowsWindowIdNotificationsPostRequest);

    /**
     * PATCH /system/maintenance/windows/{windowId} : Обновить информацию об окне
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @param maintenanceWindowUpdateRequest  (required)
     * @return MaintenanceWindow
     */
    MaintenanceWindow systemMaintenanceWindowsWindowIdPatch(UUID windowId, MaintenanceWindowUpdateRequest maintenanceWindowUpdateRequest);

    /**
     * POST /system/maintenance/windows/{windowId}/rollback : Выполнить откат обслуживания
     *
     * @param windowId Идентификатор окна обслуживания. (required)
     * @param systemMaintenanceWindowsWindowIdRollbackPostRequest  (required)
     * @return MaintenanceActionResponse
     */
    MaintenanceActionResponse systemMaintenanceWindowsWindowIdRollbackPost(UUID windowId, SystemMaintenanceWindowsWindowIdRollbackPostRequest systemMaintenanceWindowsWindowIdRollbackPostRequest);
}

