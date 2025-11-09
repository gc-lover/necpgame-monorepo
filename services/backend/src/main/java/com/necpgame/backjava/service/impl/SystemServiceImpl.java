package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.MaintenanceAuditEntryEntity;
import com.necpgame.backjava.entity.MaintenanceStatusPayloadEntity;
import com.necpgame.backjava.entity.MaintenanceWindowEntity;
import com.necpgame.backjava.exception.MaintenanceException;
import com.necpgame.backjava.model.IntegrationHooks;
import com.necpgame.backjava.model.MaintenanceActionResponse;
import com.necpgame.backjava.model.MaintenanceAuditEntry;
import com.necpgame.backjava.model.MaintenanceAuditResponse;
import com.necpgame.backjava.model.MaintenanceError;
import com.necpgame.backjava.model.MaintenanceHookTriggerRequest;
import com.necpgame.backjava.model.MaintenanceStatus;
import com.necpgame.backjava.model.MaintenanceStatusPayload;
import com.necpgame.backjava.model.MaintenanceStatusSessionDrain;
import com.necpgame.backjava.model.MaintenanceStatusTimelineInner;
import com.necpgame.backjava.model.MaintenanceStatusUpdateRequest;
import com.necpgame.backjava.model.MaintenanceWindow;
import com.necpgame.backjava.model.MaintenanceWindowCreateRequest;
import com.necpgame.backjava.model.MaintenanceWindowList;
import com.necpgame.backjava.model.MaintenanceWindowUpdateRequest;
import com.necpgame.backjava.model.MaintenanceAuditEntryAttachmentsInner;
import com.necpgame.backjava.model.NotificationPlan;
import com.necpgame.backjava.model.Page;
import com.necpgame.backjava.model.ShutdownPlan;
import com.necpgame.backjava.model.SystemMaintenanceActiveEscalatePostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdCancelPostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdCompletePostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdNotificationsPostRequest;
import com.necpgame.backjava.model.SystemMaintenanceWindowsWindowIdRollbackPostRequest;
import com.necpgame.backjava.repository.MaintenanceAuditEntryRepository;
import com.necpgame.backjava.repository.MaintenanceStatusPayloadRepository;
import com.necpgame.backjava.repository.MaintenanceWindowRepository;
import com.necpgame.backjava.service.SystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SystemServiceImpl implements SystemService {

    private static final List<String> ACTIVE_STATUSES = List.of("IN_PROGRESS", "PAUSED");
    private static final List<String> BOOKED_STATUSES = List.of("PLANNED", "APPROVED", "IN_PROGRESS", "PAUSED");
    private static final List<String> UPCOMING_STATUSES = List.of("PLANNED", "APPROVED");
    private static final String ACTOR_SYSTEM = "system";
    private static final String ROLE_SYSTEM = "SYSTEM";

    private final MaintenanceWindowRepository maintenanceWindowRepository;
    private final MaintenanceAuditEntryRepository maintenanceAuditEntryRepository;
    private final MaintenanceStatusPayloadRepository maintenanceStatusPayloadRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public MaintenanceWindowList systemMaintenanceWindowsGet(String status, String type, String environment, String service, Integer page, Integer pageSize) {
        int pageNumber = Math.max(1, page == null ? 1 : page);
        int size = Math.max(1, Math.min(pageSize == null ? 20 : pageSize, 100));
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        String serviceFilter = StringUtils.hasText(service) ? service.trim() : null;
        var resultPage = maintenanceWindowRepository.findAllFiltered(normalize(status), normalize(type), normalize(environment), serviceFilter, pageable);
        List<MaintenanceWindow> windows = resultPage.getContent().stream()
            .map(this::toWindowDto)
            .toList();
        Page meta = new Page(pageNumber, size, safeInt(resultPage.getTotalElements()), resultPage.getTotalPages())
            .hasNext(resultPage.hasNext())
            .hasPrev(resultPage.hasPrevious());
        return new MaintenanceWindowList(windows).page(meta);
    }

    @Override
    public MaintenanceWindow systemMaintenanceWindowsPost(MaintenanceWindowCreateRequest request) {
        validateCreateRequest(request);
        OffsetDateTime start = request.getStartAt();
        OffsetDateTime end = resolveEndAt(request.getEndAt(), request.getExpectedDurationMinutes(), start);
        ensureNoConflicts(null, request.getEnvironment().getValue(), start, end);
        MaintenanceWindowEntity entity = new MaintenanceWindowEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setType(request.getType().getValue());
        entity.setEnvironment(request.getEnvironment().getValue());
        entity.setZonesJson(writeJson(request.getZones()));
        entity.setServicesJson(writeJson(request.getServices()));
        entity.setStartAt(start);
        entity.setEndAt(end);
        entity.setExpectedDurationMinutes(request.getExpectedDurationMinutes());
        entity.setStatus(MaintenanceWindow.StatusEnum.PLANNED.getValue());
        entity.setCreatedBy(ACTOR_SYSTEM);
        entity.setShutdownPlanJson(writeJson(request.getShutdownPlan()));
        entity.setNotificationPlanJson(writeJson(request.getNotificationPlan()));
        entity.setHooksJson(writeJson(request.getHooks()));
        entity.setAffectedServicesJson(writeJson(request.getServices()));
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        logTimeline(saved, "Окно создано", ACTOR_SYSTEM);
        logAudit(saved, "WINDOW_CREATED", Map.of("title", saved.getTitle()));
        return toWindowDto(saved);
    }

    @Override
    public MaintenanceWindow systemMaintenanceWindowsWindowIdPatch(UUID windowId, MaintenanceWindowUpdateRequest request) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getStartAt() != null) {
            entity.setStartAt(request.getStartAt());
        }
        if (request.getExpectedDurationMinutes() != null) {
            entity.setExpectedDurationMinutes(request.getExpectedDurationMinutes());
        }
        if (request.getEndAt() != null || request.getExpectedDurationMinutes() != null) {
            OffsetDateTime end = resolveEndAt(request.getEndAt(), request.getExpectedDurationMinutes(), entity.getStartAt());
            entity.setEndAt(end);
        }
        if (request.getNotificationPlan() != null) {
            entity.setNotificationPlanJson(writeJson(request.getNotificationPlan()));
        }
        if (request.getShutdownPlan() != null) {
            entity.setShutdownPlanJson(writeJson(request.getShutdownPlan()));
        }
        if (request.getHooks() != null) {
            entity.setHooksJson(writeJson(request.getHooks()));
        }
        if (request.getNotes() != null) {
            entity.setNotes(request.getNotes());
        }
        ensureNoConflicts(entity.getId(), entity.getEnvironment(), entity.getStartAt(), resolveEndAt(entity.getEndAt(), entity.getExpectedDurationMinutes(), entity.getStartAt()));
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        logTimeline(saved, "Окно обновлено", ACTOR_SYSTEM);
        logAudit(saved, "WINDOW_UPDATED", Map.of("windowId", saved.getId()));
        return toWindowDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceWindow systemMaintenanceWindowsWindowIdGet(UUID windowId) {
        return toWindowDto(getWindowOrThrow(windowId));
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceWindowsWindowIdActivatePost(UUID windowId) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        if (!List.of("PLANNED", "APPROVED", "PAUSED").contains(entity.getStatus())) {
            throw error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Окно не может быть активировано", Map.of("status", entity.getStatus()));
        }
        entity.setStatus("IN_PROGRESS");
        entity.setEmergency(false);
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_ACTIVATED", Map.of("windowId", saved.getId()));
        logTimeline(saved, "Обслуживание запущено", ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Обслуживание запущено");
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceWindowsWindowIdCancelPost(UUID windowId, SystemMaintenanceWindowsWindowIdCancelPostRequest request) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        if (List.of("COMPLETED", "ROLLED_BACK", "CANCELLED").contains(entity.getStatus())) {
            throw error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Окно уже завершено", Map.of("status", entity.getStatus()));
        }
        entity.setStatus("CANCELLED");
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        Map<String, Object> details = new HashMap<>();
        details.put("reason", request.getReason());
        details.put("notifyPlayers", request.getNotifyPlayers());
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_CANCELLED", details);
        logTimeline(saved, "Обслуживание отменено: " + request.getReason(), ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Обслуживание отменено");
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceWindowsWindowIdCompletePost(UUID windowId, @Nullable SystemMaintenanceWindowsWindowIdCompletePostRequest request) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        if (!"IN_PROGRESS".equals(entity.getStatus()) && !"PAUSED".equals(entity.getStatus())) {
            throw error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Окно не активно", Map.of("status", entity.getStatus()));
        }
        entity.setStatus("COMPLETED");
        if (entity.getEndAt() == null) {
            entity.setEndAt(OffsetDateTime.now());
        }
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        if (request != null && request.getSummary() != null) {
            entity.setNotes(request.getSummary());
        }
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        Map<String, Object> details = new HashMap<>();
        if (request != null) {
            details.put("postmortemUrl", request.getPostmortemUrl());
            details.put("summary", request.getSummary());
            details.put("attachAudit", request.getAttachAudit());
        }
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_COMPLETED", details);
        logTimeline(saved, "Обслуживание завершено", ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Обслуживание завершено");
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceWindowsWindowIdRollbackPost(UUID windowId, SystemMaintenanceWindowsWindowIdRollbackPostRequest request) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        if (!"IN_PROGRESS".equals(entity.getStatus()) && !"COMPLETED".equals(entity.getStatus()) && !"PAUSED".equals(entity.getStatus())) {
            throw error(MaintenanceError.CodeEnum.ROLLBACK_FAILED, HttpStatus.CONFLICT, "Откат невозможен", Map.of("status", entity.getStatus()));
        }
        entity.setStatus("ROLLED_BACK");
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        entity.setEmergency(false);
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        Map<String, Object> details = new HashMap<>();
        details.put("reason", request.getReason());
        details.put("actions", request.getActions());
        details.put("notify", request.getNotify());
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_ROLLBACK", details);
        logTimeline(saved, "Выполнен откат: " + request.getReason(), ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Откат инициирован");
    }

    @Override
    public Void systemMaintenanceWindowsWindowIdNotificationsPost(UUID windowId, SystemMaintenanceWindowsWindowIdNotificationsPostRequest request) {
        MaintenanceWindowEntity entity = getWindowOrThrow(windowId);
        Map<String, Object> details = new HashMap<>();
        details.put("channels", request.getChannels());
        details.put("templateOverride", request.getTemplateOverride());
        details.put("message", request.getMessage());
        logAudit(entity, "WINDOW_NOTIFICATIONS_TRIGGERED", details);
        logTimeline(entity, "Отправлены уведомления", ACTOR_SYSTEM);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceStatus systemMaintenanceActiveGet() {
        Optional<MaintenanceWindowEntity> active = maintenanceWindowRepository.findFirstByStatusInOrderByUpdatedAtDesc(ACTIVE_STATUSES);
        if (active.isPresent()) {
            return toStatus(active.get());
        }
        Optional<MaintenanceWindowEntity> upcoming = maintenanceWindowRepository.findFirstByStatusInOrderByStartAtAsc(UPCOMING_STATUSES);
        if (upcoming.isPresent()) {
            MaintenanceWindowEntity window = upcoming.get();
            MaintenanceStatus status = new MaintenanceStatus(MaintenanceStatus.StatusEnum.UPCOMING, OffsetDateTime.now());
            status.setProgressPercent(null);
            status.setAffectedServices(readStringList(window.getServicesJson()));
            status.setPlayerCount(null);
            status.setSessionDrain(null);
            status.setTimeline(new ArrayList<>());
            return status;
        }
        return new MaintenanceStatus(MaintenanceStatus.StatusEnum.NONE, OffsetDateTime.now());
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceActivePausePost() {
        MaintenanceWindowEntity entity = maintenanceWindowRepository.findFirstByStatusInOrderByUpdatedAtDesc(List.of("IN_PROGRESS"))
            .orElseThrow(() -> error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Нет активного окна", Map.of()));
        entity.setStatus("PAUSED");
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_PAUSED", Map.of("windowId", saved.getId()));
        logTimeline(saved, "Обслуживание приостановлено", ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Обслуживание приостановлено");
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceActiveResumePost() {
        MaintenanceWindowEntity entity = maintenanceWindowRepository.findFirstByStatusInOrderByUpdatedAtDesc(List.of("PAUSED"))
            .orElseThrow(() -> error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Нет приостановленного окна", Map.of()));
        entity.setStatus("IN_PROGRESS");
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_RESUMED", Map.of("windowId", saved.getId()));
        logTimeline(saved, "Обслуживание возобновлено", ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Обслуживание возобновлено");
    }

    @Override
    public MaintenanceActionResponse systemMaintenanceActiveEscalatePost(SystemMaintenanceActiveEscalatePostRequest request) {
        MaintenanceWindowEntity entity = maintenanceWindowRepository.findFirstByStatusInOrderByUpdatedAtDesc(ACTIVE_STATUSES)
            .orElseThrow(() -> error(MaintenanceError.CodeEnum.NOT_ACTIVE, HttpStatus.CONFLICT, "Нет активного окна", Map.of()));
        entity.setEmergency(true);
        entity.setStatusUpdatedAt(OffsetDateTime.now());
        MaintenanceWindowEntity saved = maintenanceWindowRepository.save(entity);
        Map<String, Object> details = new HashMap<>();
        details.put("reason", request.getReason());
        details.put("level", request.getEscalationLevel());
        details.put("actions", request.getActions());
        details.put("notify", request.getNotify());
        MaintenanceAuditEntryEntity audit = logAudit(saved, "WINDOW_ESCALATED", details);
        logTimeline(saved, "Эскалация обслуживания: " + request.getReason(), ACTOR_SYSTEM);
        return buildActionResponse(saved, audit, "Эскалация выполнена");
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceAuditResponse systemMaintenanceAuditGet(UUID windowId, String actor, String action, Integer page, Integer pageSize) {
        int pageNumber = Math.max(1, page == null ? 1 : page);
        int size = Math.max(1, Math.min(pageSize == null ? 20 : pageSize, 100));
        Pageable pageable = PageRequest.of(pageNumber - 1, size);
        var result = maintenanceAuditEntryRepository.findAllFiltered(windowId, normalize(actor), normalize(action), pageable);
        List<MaintenanceAuditEntry> entries = result.getContent().stream()
            .map(this::toAuditDto)
            .toList();
        Page meta = new Page(pageNumber, size, safeInt(result.getTotalElements()), result.getTotalPages())
            .hasNext(result.hasNext())
            .hasPrev(result.hasPrevious());
        return new MaintenanceAuditResponse(entries).page(meta);
    }

    @Override
    public MaintenanceAuditEntry systemMaintenanceAuditPost(MaintenanceAuditEntry maintenanceAuditEntry) {
        MaintenanceWindowEntity window = getWindowOrThrow(maintenanceAuditEntry.getWindowId());
        MaintenanceAuditEntryEntity entity = new MaintenanceAuditEntryEntity();
        entity.setWindow(window);
        entity.setActor(maintenanceAuditEntry.getActor());
        entity.setRole(maintenanceAuditEntry.getRole());
        entity.setAction(maintenanceAuditEntry.getAction());
        entity.setDetails(maintenanceAuditEntry.getDetails());
        entity.setAttachmentsJson(writeJson(maintenanceAuditEntry.getAttachments()));
        MaintenanceAuditEntryEntity saved = maintenanceAuditEntryRepository.save(entity);
        return toAuditDto(saved);
    }

    @Override
    public Void systemMaintenanceHooksDeploymentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest) {
        MaintenanceWindowEntity entity = getWindowOrThrow(maintenanceHookTriggerRequest.getWindowId());
        logAudit(entity, "HOOK_DEPLOYMENT_TRIGGERED", Map.of("payload", maintenanceHookTriggerRequest.getPayload(), "hookType", maintenanceHookTriggerRequest.getHookType()));
        logTimeline(entity, "Вызван deployment hook", ACTOR_SYSTEM);
        return null;
    }

    @Override
    public Void systemMaintenanceHooksIncidentPost(MaintenanceHookTriggerRequest maintenanceHookTriggerRequest) {
        MaintenanceWindowEntity entity = getWindowOrThrow(maintenanceHookTriggerRequest.getWindowId());
        logAudit(entity, "HOOK_INCIDENT_TRIGGERED", Map.of("payload", maintenanceHookTriggerRequest.getPayload(), "hookType", maintenanceHookTriggerRequest.getHookType()));
        logTimeline(entity, "Отправлено сообщение об инциденте", ACTOR_SYSTEM);
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceStatusPayload systemMaintenanceStatusGet() {
        Optional<MaintenanceStatusPayloadEntity> payload = maintenanceStatusPayloadRepository.findFirstByOrderByCreatedAtDesc();
        if (payload.isEmpty()) {
            return new MaintenanceStatusPayload("NONE", OffsetDateTime.now())._public(Boolean.FALSE).progressPercent(null).message(null);
        }
        MaintenanceStatusPayloadEntity entity = payload.get();
        return new MaintenanceStatusPayload(entity.getStatus(), entity.getCreatedAt())
            .progressPercent(entity.getProgressPercent() == null ? null : entity.getProgressPercent().floatValue())
            .message(entity.getMessage())
            ._public(entity.isPublicVisible());
    }

    @Override
    public Void systemMaintenanceStatusPost(MaintenanceStatusUpdateRequest maintenanceStatusUpdateRequest) {
        MaintenanceStatusPayloadEntity entity = new MaintenanceStatusPayloadEntity();
        entity.setStatus(maintenanceStatusUpdateRequest.getStatus());
        entity.setProgressPercent(null);
        entity.setMessage(maintenanceStatusUpdateRequest.getMessage());
        entity.setPublicVisible(Boolean.TRUE.equals(maintenanceStatusUpdateRequest.getPublic()));
        maintenanceStatusPayloadRepository.save(entity);
        return null;
    }

    private MaintenanceWindowEntity getWindowOrThrow(UUID id) {
        return maintenanceWindowRepository.findById(id)
            .orElseThrow(() -> error(MaintenanceError.CodeEnum.WINDOW_NOT_FOUND, HttpStatus.NOT_FOUND, "Окно не найдено", Map.of("windowId", id)));
    }

    private void validateCreateRequest(MaintenanceWindowCreateRequest request) {
        if (request.getTitle() == null || request.getType() == null || request.getEnvironment() == null || request.getStartAt() == null) {
            throw error(MaintenanceError.CodeEnum.INVALID_SCHEDULE, HttpStatus.BAD_REQUEST, "Отсутствуют обязательные поля", Map.of());
        }
    }

    private void ensureNoConflicts(UUID currentId, String environment, OffsetDateTime start, OffsetDateTime end) {
        List<MaintenanceWindowEntity> conflicts = maintenanceWindowRepository.findConflictingWindows(environment, BOOKED_STATUSES, start, end);
        if (currentId != null) {
            conflicts = conflicts.stream().filter(window -> !window.getId().equals(currentId)).toList();
        }
        if (!conflicts.isEmpty()) {
            throw error(MaintenanceError.CodeEnum.CONFLICTING_WINDOW, HttpStatus.CONFLICT, "Конфликтующее окно обслуживания", Map.of("windowId", conflicts.getFirst().getId()));
        }
    }

    private MaintenanceStatus toStatus(MaintenanceWindowEntity entity) {
        MaintenanceStatus.StatusEnum statusEnum;
        if ("PAUSED".equals(entity.getStatus())) {
            statusEnum = MaintenanceStatus.StatusEnum.PAUSED;
        } else if (entity.isEmergency()) {
            statusEnum = MaintenanceStatus.StatusEnum.EMERGENCY;
        } else {
            statusEnum = MaintenanceStatus.StatusEnum.IN_PROGRESS;
        }
        MaintenanceStatus status = new MaintenanceStatus(statusEnum, entity.getStatusUpdatedAt() != null ? entity.getStatusUpdatedAt() : OffsetDateTime.now());
        status.setProgressPercent(entity.getProgressPercent() == null ? null : entity.getProgressPercent().floatValue());
        status.setAffectedServices(readStringList(entity.getAffectedServicesJson()));
        status.setPlayerCount(entity.getPlayerCount());
        status.setSessionDrain(buildSessionDrain(entity));
        status.setTimeline(readTimeline(entity.getTimelineJson()));
        return status;
    }

    private MaintenanceWindow toWindowDto(MaintenanceWindowEntity entity) {
        MaintenanceWindow window = new MaintenanceWindow(entity.getId(), entity.getTitle(), MaintenanceWindow.TypeEnum.fromValue(entity.getType()), MaintenanceWindow.EnvironmentEnum.fromValue(entity.getEnvironment()), entity.getStartAt(), MaintenanceWindow.StatusEnum.fromValue(entity.getStatus()), entity.getCreatedBy());
        window.setDescription(entity.getDescription());
        window.setEndAt(entity.getEndAt());
        window.setExpectedDurationMinutes(entity.getExpectedDurationMinutes());
        window.setApprovedBy(entity.getApprovedBy());
        window.setCreatedAt(entity.getCreatedAt());
        window.setUpdatedAt(entity.getUpdatedAt());
        window.setZones(readStringList(entity.getZonesJson()));
        window.setServices(readStringList(entity.getServicesJson()));
        window.setShutdownPlan(readObject(entity.getShutdownPlanJson(), ShutdownPlan.class));
        window.setNotificationPlan(readObject(entity.getNotificationPlanJson(), NotificationPlan.class));
        window.setHooks(readObject(entity.getHooksJson(), IntegrationHooks.class));
        return window;
    }

    private MaintenanceAuditEntry toAuditDto(MaintenanceAuditEntryEntity entity) {
        MaintenanceAuditEntry dto = new MaintenanceAuditEntry(entity.getId(), entity.getWindow().getId(), entity.getAction(), entity.getCreatedAt());
        dto.setActor(entity.getActor());
        dto.setRole(entity.getRole());
        dto.setDetails(entity.getDetails());
        dto.setAttachments(readAttachments(entity.getAttachmentsJson()));
        return dto;
    }

    private MaintenanceStatusSessionDrain buildSessionDrain(MaintenanceWindowEntity entity) {
        if (entity.getSessionActiveSessions() == null && entity.getSessionDrainedSessions() == null && entity.getSessionEstimatedCompletion() == null) {
            return null;
        }
        return new MaintenanceStatusSessionDrain()
            .activeSessions(entity.getSessionActiveSessions())
            .drainedSessions(entity.getSessionDrainedSessions())
            .estimatedCompletion(entity.getSessionEstimatedCompletion());
    }

    private MaintenanceAuditEntryEntity logAudit(MaintenanceWindowEntity window, String action, Map<String, Object> details) {
        MaintenanceAuditEntryEntity audit = new MaintenanceAuditEntryEntity();
        audit.setWindow(window);
        audit.setAction(action);
        audit.setActor(ACTOR_SYSTEM);
        audit.setRole(ROLE_SYSTEM);
        audit.setDetails(writeJson(details));
        return maintenanceAuditEntryRepository.save(audit);
    }

    private MaintenanceActionResponse buildActionResponse(MaintenanceWindowEntity window, MaintenanceAuditEntryEntity audit, String message) {
        return new MaintenanceActionResponse(window.getId(), window.getStatus())
            .message(message)
            .auditEntryId(audit.getId());
    }

    private void logTimeline(MaintenanceWindowEntity window, String message, String actor) {
        List<MaintenanceStatusTimelineInner> timeline = readTimeline(window.getTimelineJson());
        MaintenanceStatusTimelineInner entry = new MaintenanceStatusTimelineInner()
            .timestamp(OffsetDateTime.now())
            .message(message)
            .actor(actor);
        timeline.add(entry);
        window.setTimelineJson(writeJson(timeline));
    }

    private List<MaintenanceStatusTimelineInner> readTimeline(String json) {
        return readList(json, MaintenanceStatusTimelineInner.class);
    }

    private List<String> readStringList(String json) {
        return readList(json, String.class);
    }

    private Map<String, Object> readMap(String json) {
        if (!StringUtils.hasText(json)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse map: {}", ex.getMessage());
            return new HashMap<>();
        }
    }

    private <T> T readObject(String json, Class<T> type) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse object: {}", ex.getMessage());
            return null;
        }
    }

    private <T> List<T> readList(String json, Class<T> itemType) {
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, itemType));
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse list: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private String writeJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw error(MaintenanceError.CodeEnum.HOOK_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось сериализовать данные", Map.of("reason", ex.getMessage()));
        }
    }

    private List<MaintenanceAuditEntryAttachmentsInner> readAttachments(String json) {
        return readList(json, MaintenanceAuditEntryAttachmentsInner.class);
    }

    private static OffsetDateTime resolveEndAt(OffsetDateTime endAt, Integer durationMinutes, OffsetDateTime startAt) {
        if (endAt != null) {
            return endAt;
        }
        if (durationMinutes != null) {
            return startAt.plusMinutes(Math.max(durationMinutes, 1));
        }
        return startAt.plusHours(1);
    }

    private static String normalize(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private static int safeInt(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    private static Double optionalFloat(Float value) {
        return value == null ? null : value.doubleValue();
    }

    private MaintenanceException error(MaintenanceError.CodeEnum code, HttpStatus status, String message, Map<String, Object> details) {
        return new MaintenanceException(code, status, message, details);
    }
}


