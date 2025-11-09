package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.SystemApi;
import com.necpgame.backjava.model.MaintenanceActionResponse;
import com.necpgame.backjava.model.MaintenanceAuditEntry;
import com.necpgame.backjava.model.MaintenanceAuditResponse;
import com.necpgame.backjava.model.MaintenanceHookTriggerRequest;
import com.necpgame.backjava.model.MaintenanceStatus;
import com.necpgame.backjava.model.MaintenanceStatusPayload;
import com.necpgame.backjava.model.MaintenanceStatusUpdateRequest;
import com.necpgame.backjava.model.MaintenanceWindow;
import com.necpgame.backjava.model.MaintenanceWindowCreateRequest;
import com.necpgame.backjava.model.MaintenanceWindowList;
import com.necpgame.backjava.model.MaintenanceWindowUpdateRequest;
import com.necpgame.backjava.model.SystemMaintenanceActiveEscalatePostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdCancelPostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdCompletePostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdNotificationsPostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdRollbackPostRequest;
import com.necpgame.backjava.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SystemController implements SystemApi {

    private final SystemService systemService;

    @Override
    public ResponseEntity<MaintenanceWindowList> systemMaintenanceWindowsGet(String status, String type, String environment, String service, Integer page, Integer pageSize) {
        return ResponseEntity.ok(systemService.systemMaintenanceWindowsGet(status, type, environment, service, page, pageSize));
    }

    @Override
    public ResponseEntity<MaintenanceWindow> systemMaintenanceWindowsPost(MaintenanceWindowCreateRequest maintenanceWindowCreateRequest) {
        MaintenanceWindow body = systemService.systemMaintenanceWindowsPost(maintenanceWindowCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Override
    public ResponseEntity<MaintenanceWindow> systemMaintenanceWindowsWindowIdPatch(UUID windowId, MaintenanceWindowUpdateRequest maintenanceWindowUpdateRequest) {
        return ResponseEntity.ok(systemService.systemMaintenanceWindowsWindowIdPatch(windowId, maintenanceWindowUpdateRequest));
    }

    @Override
    public ResponseEntity<MaintenanceWindow> systemMaintenanceWindowsWindowIdGet(UUID windowId) {
        return ResponseEntity.ok(systemService.systemMaintenanceWindowsWindowIdGet(windowId));
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceWindowsWindowIdActivatePost(UUID windowId) {
        MaintenanceActionResponse body = systemService.systemMaintenanceWindowsWindowIdActivatePost(windowId);
        return ResponseEntity.accepted().body(body);
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceWindowsWindowIdCancelPost(UUID windowId, SystemMaintenanceWindowsWindowIdCancelPostRequest systemMaintenanceWindowsWindowIdCancelPostRequest) {
        MaintenanceActionResponse body = systemService.systemMaintenanceWindowsWindowIdCancelPost(windowId, systemMaintenanceWindowsWindowIdCancelPostRequest);
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceWindowsWindowIdCompletePost(UUID windowId, SystemMaintenanceWindowsWindowIdCompletePostRequest systemMaintenanceWindowsWindowIdCompletePostRequest) {
        MaintenanceActionResponse body = systemService.systemMaintenanceWindowsWindowIdCompletePost(windowId, systemMaintenanceWindowsWindowIdCompletePostRequest);
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceWindowsWindowIdRollbackPost(UUID windowId, SystemMaintenanceWindowsWindowIdRollbackPostRequest systemMaintenanceWindowsWindowIdRollbackPostRequest) {
        MaintenanceActionResponse body = systemService.systemMaintenanceWindowsWindowIdRollbackPost(windowId, systemMaintenanceWindowsWindowIdRollbackPostRequest);
        return ResponseEntity.accepted().body(body);
    }

    @Override
    public ResponseEntity<Void> systemMaintenanceWindowsWindowIdNotificationsPost(UUID windowId, SystemMaintenanceWindowsWindowIdNotificationsPostRequest systemMaintenanceWindowsWindowIdNotificationsPostRequest) {
        systemService.systemMaintenanceWindowsWindowIdNotificationsPost(windowId, systemMaintenanceWindowsWindowIdNotificationsPostRequest);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<MaintenanceStatus> systemMaintenanceActiveGet() {
        return ResponseEntity.ok(systemService.systemMaintenanceActiveGet());
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceActivePausePost() {
        return ResponseEntity.ok(systemService.systemMaintenanceActivePausePost());
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceActiveResumePost() {
        return ResponseEntity.ok(systemService.systemMaintenanceActiveResumePost());
    }

    @Override
    public ResponseEntity<MaintenanceActionResponse> systemMaintenanceActiveEscalatePost(SystemMaintenanceActiveEscalatePostRequest systemMaintenanceActiveEscalatePostRequest) {
        MaintenanceActionResponse body = systemService.systemMaintenanceActiveEscalatePost(systemMaintenanceActiveEscalatePostRequest);
        return ResponseEntity.ok(body);
    }

    @Override
    public ResponseEntity<MaintenanceAuditResponse> systemMaintenanceAuditGet(UUID windowId, String actor, String action, Integer page, Integer pageSize) {
        return ResponseEntity.ok(systemService.systemMaintenanceAuditGet(windowId, actor, action, page, pageSize));
    }

    @Override
    public ResponseEntity<MaintenanceAuditEntry> systemMaintenanceAuditPost(MaintenanceAuditEntry maintenanceAuditEntry) {
        MaintenanceAuditEntry body = systemService.systemMaintenanceAuditPost(maintenanceAuditEntry);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @Override
    public ResponseEntity<Void> systemMaintenanceHooksDeploymentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest) {
        systemService.systemMaintenanceHooksDeploymentPost(maintenanceHookTriggerRequest);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<Void> systemMaintenanceHooksIncidentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest) {
        systemService.systemMaintenanceHooksIncidentPost(maintenanceHookTriggerRequest);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<MaintenanceStatusPayload> systemMaintenanceStatusGet() {
        return ResponseEntity.ok(systemService.systemMaintenanceStatusGet());
    }

    @Override
    public ResponseEntity<Void> systemMaintenanceStatusPost(MaintenanceStatusUpdateRequest maintenanceStatusUpdateRequest) {
        systemService.systemMaintenanceStatusPost(maintenanceStatusUpdateRequest);
        return ResponseEntity.ok().build();
    }
}





