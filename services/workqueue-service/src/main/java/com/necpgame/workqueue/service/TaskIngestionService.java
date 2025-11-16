package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.config.WorkqueueIngestionProperties;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.TaskIngestionRequest;
import com.necpgame.workqueue.service.model.TaskIngestionResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskIngestionService {
    private static final String FIELD_SOURCE_ID = "sourceId";
    private static final String FIELD_KNOWLEDGE_REFS = "knowledgeRefs";
    private static final Logger log = LoggerFactory.getLogger(TaskIngestionService.class);
    private static final String TASK_INGESTION_DOC = "ingest-contract";
    private final QueueItemRepository queueItemRepository;
    private final QueueRepository queueRepository;
    private final QueueItemStateRepository queueItemStateRepository;
    private final QueueItemTemplateRepository queueItemTemplateRepository;
    private final EnumCatalogService enumCatalogService;
    private final ActivityLogService activityLogService;
    private final AgentDirectoryService agentDirectoryService;
    private final ObjectMapper objectMapper;
    private final WorkqueueIngestionProperties ingestionProperties;
    private final Set<String> allowedSegments;
    private final List<Path> knowledgeRoots;
    private final String creationSegment;

    public TaskIngestionService(QueueItemRepository queueItemRepository,
                                QueueRepository queueRepository,
                                QueueItemStateRepository queueItemStateRepository,
                                QueueItemTemplateRepository queueItemTemplateRepository,
                                EnumCatalogService enumCatalogService,
                                ActivityLogService activityLogService,
                                AgentDirectoryService agentDirectoryService,
                                ObjectMapper objectMapper,
                                WorkqueueIngestionProperties ingestionProperties,
                                @Value("${workqueue.repo-root:..}") String repoRootSetting) {
        this.queueItemRepository = queueItemRepository;
        this.queueRepository = queueRepository;
        this.queueItemStateRepository = queueItemStateRepository;
        this.queueItemTemplateRepository = queueItemTemplateRepository;
        this.enumCatalogService = enumCatalogService;
        this.activityLogService = activityLogService;
        this.agentDirectoryService = agentDirectoryService;
        this.objectMapper = objectMapper;
        this.ingestionProperties = ingestionProperties;
        this.allowedSegments = ingestionProperties.getAllowedSegments().stream()
                .filter(Objects::nonNull)
                .map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
        String configuredCreationSegment = normalize(ingestionProperties.getCreationSegment());
        if (configuredCreationSegment == null || configuredCreationSegment.isBlank()) {
            configuredCreationSegment = "concept";
        }
        this.creationSegment = configuredCreationSegment;
        this.knowledgeRoots = buildKnowledgeRoots(repoRootSetting);
    }

    @Transactional
    public TaskIngestionResult ingest(TaskIngestionRequest request) {
        Objects.requireNonNull(request, "TaskIngestionRequest required");
        String sourceId = trimToNull(request.sourceId());
        if (sourceId == null) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "ingest.validation.missing_field",
                    "sourceId обязателен",
                    List.of(TASK_INGESTION_DOC),
                    List.of(new ApiErrorDetail(FIELD_SOURCE_ID, "Значение не может быть пустым"))
            );
        }
        String segment = normalize(request.segment());
        String status = normalize(request.initialStatus());

        ensureSourceIdUnique(sourceId);
        ensureSegmentAllowed(segment, "segment");
        ensureCreationSegment(segment);
        ensureSegmentAllowed(normalize(request.handoffPlan().nextSegment()), "handoffPlan.nextSegment");
        for (TaskIngestionRequest.HandoffCondition condition : request.handoffPlan().conditions()) {
            ensureSegmentAllowed(normalize(condition.targetSegment()), "handoffPlan.conditions[].targetSegment");
            ensureStatusKnown(normalize(condition.status()), "handoffPlan.conditions[].status");
        }
        ensureStatusKnown(status, "initialStatus");
        validateKnowledgeRefs(request.knowledgeRefs());

        AgentEntity actor = agentDirectoryService.requireActiveByRole(ingestionProperties.getSystemRole());
        OffsetDateTime now = OffsetDateTime.now();
        QueueEntity queue = resolveQueue(segment, status, now, actor);
        UUID statusValueId = enumCatalogService.requireTaskStatus(status);

        QueueItemEntity item = buildQueueItem(queue, actor, request, sourceId, statusValueId, now);
        QueueItemEntity savedItem = persistQueueItem(item);
        String metadata = serializeMetadata(request);
        String summary = trimToNull(request.summary());
        QueueItemStateEntity state = QueueItemStateEntity.create(savedItem, actor, status, statusValueId, summary != null ? summary : request.summary(), metadata, now);
        QueueItemStateEntity persistedState = queueItemStateRepository.save(state);
        savedItem.applyState(persistedState);
        queueItemRepository.save(savedItem);

        persistTemplateLinks(savedItem, request.templates(), now);
        activityLogService.recordItemEvent(actor, savedItem, status, metadata);
        return new TaskIngestionResult(savedItem.getId(), queue.getId(), queue.getSegment(), status, now);
    }

    private void ensureSourceIdUnique(String sourceId) {
        if (queueItemRepository.findByExternalRef(sourceId).isPresent()) {
            throw new ApiErrorException(
                    HttpStatus.CONFLICT,
                    "ingest.conflict.source_id",
                    "Задача с таким sourceId уже существует",
                    List.of(TASK_INGESTION_DOC),
                    List.of(new ApiErrorDetail(FIELD_SOURCE_ID, "Укажите уникальное значение"))
            );
        }
    }

    private void ensureSegmentAllowed(String segment, String path) {
        if (segment == null || segment.isBlank() || !allowedSegments.contains(segment)) {
            String reference = segment == null ? "segment-registry" : "agent-brief:" + segment;
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "ingest.validation.invalid_segment",
                    "Указан недопустимый сегмент",
                    List.of(reference),
                    List.of(new ApiErrorDetail(path, "Сегмент должен соответствовать зарегистрированным очередям"))
            );
        }
    }

    private void ensureCreationSegment(String segment) {
        if (!Objects.equals(creationSegment, segment)) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "ingest.forbidden.segment",
                    "Создавать задачи через ingest может только сегмент " + creationSegment,
                    List.of("agent-brief:" + creationSegment),
                    List.of(new ApiErrorDetail("segment", "Используйте сегмент " + creationSegment))
            );
        }
    }

    private void ensureStatusKnown(String status, String path) {
        try {
            enumCatalogService.requireTaskStatus(status);
        } catch (IllegalStateException ex) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "ingest.validation.invalid_status",
                    "Неизвестный статус задачи",
                    List.of("enum:task_status", TASK_INGESTION_DOC),
                    List.of(new ApiErrorDetail(path, "Используйте статус из enum task_status"))
            );
        }
    }

    private void validateKnowledgeRefs(List<String> refs) {
        if (refs == null || refs.isEmpty()) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "ingest.validation.missing_field",
                    "knowledgeRefs не может быть пустым",
                    List.of(TASK_INGESTION_DOC),
                    List.of(new ApiErrorDetail(FIELD_KNOWLEDGE_REFS, "Добавьте ссылки на знания"))
            );
        }
        List<ApiErrorDetail> missing = new ArrayList<>();
        for (String ref : refs) {
            if (ref == null || ref.isBlank()) {
                missing.add(new ApiErrorDetail(FIELD_KNOWLEDGE_REFS, "Пустая ссылка запрещена"));
                continue;
            }
            String trimmed = ref.trim();
            boolean external = trimmed.startsWith("http://") || trimmed.startsWith("https://");
            boolean rest = isRestKnowledgeRef(trimmed);
            if (external || rest) {
                continue;
            }
            if (!isKnowledgeFileRef(trimmed)) {
                missing.add(new ApiErrorDetail(trimmed, "Разрешены только ссылки http(s), /api/* или knowledge/*"));
                continue;
            }
            if (!knowledgePathExists(trimmed)) {
                missing.add(new ApiErrorDetail(trimmed, "Файл не найден в репозитории знаний"));
            }
        }
        if (!missing.isEmpty()) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "ingest.validation.knowledge_ref_missing",
                    "Некоторые ссылки на знания недоступны",
                    List.of("policy:workqueue"),
                    missing
            );
        }
    }

    private QueueEntity resolveQueue(String segment, String status, OffsetDateTime now, AgentEntity owner) {
        return queueRepository.findBySegmentAndStatusCode(segment, status)
                .orElseGet(() -> createQueue(segment, status, now, owner));
    }

    private QueueEntity createQueue(String segment, String status, OffsetDateTime now, AgentEntity owner) {
        QueueEntity entity = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment(segment)
                .statusCode(status)
                .title(segment.toUpperCase(Locale.ROOT) + " :: " + status)
                .description("Автоматически создано ingestion эндпойнтом")
                .owner(owner)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return queueRepository.save(entity);
    }

    private QueueItemEntity buildQueueItem(QueueEntity queue,
                                           AgentEntity actor,
                                           TaskIngestionRequest request,
                                           String sourceId,
                                           UUID statusValueId,
                                           OffsetDateTime now) {
        String title = trimToNull(request.title());
        return QueueItemEntity.builder()
                .id(UUID.randomUUID())
                .queue(queue)
                .externalRef(sourceId)
                .title(title != null ? title : request.title())
                .priority(request.priority())
                .payload(serializePayload(request))
                .createdBy(actor)
                .assignedTo(null)
                .dueAt(null)
                .lockedUntil(null)
                .currentState(null)
                .statusValueId(statusValueId)
                .createdAt(now)
                .updatedAt(now)
                .version(0L)
                .build();
    }

    private QueueItemEntity persistQueueItem(QueueItemEntity item) {
        try {
            return queueItemRepository.save(item);
        } catch (DataIntegrityViolationException ex) {
            throw new ApiErrorException(
                    HttpStatus.CONFLICT,
                    "ingest.conflict.source_id",
                    "sourceId уже зарегистрирован",
                    List.of(TASK_INGESTION_DOC),
                    List.of(new ApiErrorDetail(FIELD_SOURCE_ID, "Проверьте уникальность идентификатора"))
            );
        }
    }

    private void persistTemplateLinks(QueueItemEntity item, TaskIngestionRequest.Templates templates, OffsetDateTime now) {
        if (templates == null) {
            return;
        }
        List<QueueItemTemplateEntity> entities = new ArrayList<>();
        for (String code : templates.primary()) {
            entities.add(buildTemplateLink(item, code, QueueItemTemplateEntity.TemplateType.PRIMARY, null, null, now));
        }
        for (String code : templates.checklists()) {
            entities.add(buildTemplateLink(item, code, QueueItemTemplateEntity.TemplateType.CHECKLIST, null, null, now));
        }
        for (TaskIngestionRequest.TemplateReference reference : templates.references()) {
            entities.add(buildTemplateLink(item, reference.code(), QueueItemTemplateEntity.TemplateType.REFERENCE, reference.version(), reference.path(), now));
        }
        if (!entities.isEmpty()) {
            queueItemTemplateRepository.saveAll(entities);
        }
    }

    private QueueItemTemplateEntity buildTemplateLink(QueueItemEntity item,
                                                      String code,
                                                      QueueItemTemplateEntity.TemplateType type,
                                                      String version,
                                                      String path,
                                                      OffsetDateTime now) {
        return QueueItemTemplateEntity.builder()
                .id(UUID.randomUUID())
                .item(item)
                .templateCode(code)
                .templateType(type)
                .templateVersion(version)
                .sourcePath(path)
                .createdAt(now)
                .build();
    }

    private String serializePayload(TaskIngestionRequest request) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(FIELD_SOURCE_ID, request.sourceId());
        payload.put("segment", request.segment());
        payload.put("initialStatus", request.initialStatus());
        payload.put("priority", request.priority());
        payload.put("title", request.title());
        payload.put("summary", request.summary());
        payload.put(FIELD_KNOWLEDGE_REFS, request.knowledgeRefs());
        payload.put("templates", request.templates());
        payload.put("payload", request.payload());
        payload.put("handoffPlan", request.handoffPlan());
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось сериализовать payload", e);
        }
    }

    private String serializeMetadata(TaskIngestionRequest request) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("handoffPlan", request.handoffPlan());
        metadata.put("templates", request.templates());
        try {
            return objectMapper.writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    private boolean knowledgePathExists(String relative) {
        List<String> candidates = buildKnowledgeCandidates(relative);
        for (Path base : knowledgeRoots) {
            if (base == null) {
                continue;
            }
            for (String candidate : candidates) {
                Path resolved = resolveUnderBase(base, candidate);
                if (resolved != null && Files.exists(resolved)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Path resolveUnderBase(Path base, String relative) {
        try {
            Path relPath = Paths.get(relative);
            Path candidate = base.resolve(relPath).normalize();
            if (!candidate.startsWith(base)) {
                return null;
            }
            return candidate;
        } catch (InvalidPathException ex) {
            log.warn("Invalid path for knowledge reference {}", relative, ex);
            return null;
        }
    }

    private boolean isKnowledgeFileRef(String value) {
        return value.startsWith("shared/docs/knowledge") || value.startsWith("knowledge");
    }

    private boolean isRestKnowledgeRef(String value) {
        return value.startsWith("/api/");
    }

    private List<String> buildKnowledgeCandidates(String original) {
        if (original.startsWith("shared/docs/knowledge")) {
            return List.of(original, original.replaceFirst("shared/docs/knowledge", "knowledge"));
        }
        if (original.startsWith("knowledge")) {
            return List.of(original, original.replaceFirst("knowledge", "shared/docs/knowledge"));
        }
        return List.of(original);
    }

    private List<Path> buildKnowledgeRoots(String repoRootSetting) {
        Set<Path> roots = new LinkedHashSet<>();
        Path configured = toAbsolute(repoRootSetting);
        if (configured != null) {
            roots.add(configured);
        }
        Path cwd = Paths.get("").toAbsolutePath().normalize();
        roots.add(cwd);
        return List.copyOf(roots);
    }

    private Path toAbsolute(String path) {
        try {
            return Paths.get(path == null ? "." : path).toAbsolutePath().normalize();
        } catch (InvalidPathException ex) {
            log.warn("Invalid repo root path: {}", path, ex);
            return Paths.get("").toAbsolutePath().normalize();
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

