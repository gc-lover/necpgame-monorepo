package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemArtifactEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.repository.QueueItemArtifactRepository;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.service.command.QueueItemUpdateCommand;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.model.StoredArtifactFile;
import com.necpgame.workqueue.service.validation.SubmissionValidatorRegistry;
import com.necpgame.workqueue.service.validation.TaskSubmissionContext;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskSubmissionService {
    private static final String REQUIREMENT_GLOBAL = "policy:workqueue";
    private static final String REQUIREMENT_AGENT_PREFIX = "agent-brief:";
    private static final String REQUIREMENT_TEMPLATE_PREFIX = "template:";
    private static final String REQUIREMENT_HANDOFF_PREFIX = "handoff-rules:";
    private final QueueItemRepository queueItemRepository;
    private final QueueItemStateRepository queueItemStateRepository;
    private final QueueRepository queueRepository;
    private final QueueItemTemplateRepository queueItemTemplateRepository;
    private final QueueItemArtifactRepository queueItemArtifactRepository;
    private final QueueLockRepository queueLockRepository;
    private final AgentDirectoryService agentDirectoryService;
    private final QueueCommandService queueCommandService;
    private final EnumCatalogService enumCatalogService;
    private final ActivityLogService activityLogService;
    private final ArtifactStorageService artifactStorageService;
    private final HandoffRuleService handoffRuleService;
    private final SubmissionValidatorRegistry submissionValidatorRegistry;

    @Transactional
    public TaskSubmissionResponseDto submit(UUID agentId, UUID itemId, TaskSubmissionRequestDto request, List<MultipartFile> files) {
        AgentEntity agent = agentDirectoryService.requireAgent(agentId);
        QueueItemEntity item = queueItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
        validateOwnership(agentId, item);

        List<QueueItemTemplateEntity> templates = queueItemTemplateRepository.findByItem(item);
        List<String> requirements = new ArrayList<>();
        requirements.add(REQUIREMENT_GLOBAL);
        String segment = item.getQueue() != null ? item.getQueue().getSegment() : null;
        if (StringUtils.hasText(segment)) {
            requirements.add(REQUIREMENT_AGENT_PREFIX + segment);
        }
        templates.stream()
                .map(QueueItemTemplateEntity::getTemplateCode)
                .filter(StringUtils::hasText)
                .map(code -> REQUIREMENT_TEMPLATE_PREFIX + code)
                .forEach(requirements::add);

        List<TaskSubmissionRequestDto.SubmissionArtifactDto> linkArtifacts = sanitizeLinkArtifacts(request.artifacts());
        List<MultipartFile> nonEmptyFiles = sanitizeFiles(files);
        if (linkArtifacts.isEmpty() && nonEmptyFiles.isEmpty()) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "validation.missing_artifact",
                    "Не приложены артефакты для проверки",
                    requirements,
                    List.of(new ApiErrorDetail("artifacts", "Добавьте файл или ссылку на результат"))
            );
        }
        submissionValidatorRegistry.validate(new TaskSubmissionContext(
                item,
                request,
                nonEmptyFiles,
                linkArtifacts,
                List.copyOf(requirements)
        ));

        List<QueueItemArtifactEntity> storedArtifacts = new ArrayList<>();
        linkArtifacts.forEach(artifact ->
                storedArtifacts.add(queueItemArtifactRepository.save(QueueItemArtifactEntity.builder()
                        .id(UUID.randomUUID())
                        .item(item)
                        .artifactType(QueueItemArtifactEntity.ArtifactType.LINK)
                        .title(artifact.title())
                        .url(artifact.url())
                        .createdAt(OffsetDateTime.now())
                        .build()))
        );
        for (MultipartFile file : nonEmptyFiles) {
            StoredArtifactFile stored = artifactStorageService.store(itemId, file);
            storedArtifacts.add(queueItemArtifactRepository.save(QueueItemArtifactEntity.builder()
                    .id(UUID.randomUUID())
                    .item(item)
                    .artifactType(QueueItemArtifactEntity.ArtifactType.FILE)
                    .title(stored.originalFilename())
                    .storagePath(stored.storagePath())
                    .mediaType(stored.mediaType())
                    .sizeBytes(stored.sizeBytes())
                    .createdAt(OffsetDateTime.now())
                    .build()));
        }

        OffsetDateTime now = OffsetDateTime.now();
        QueueItemUpdateCommand updateCommand = new QueueItemUpdateCommand(
                item.getId(),
                agent,
                "completed",
                item.getVersion(),
                request.notes(),
                request.metadata(),
                agent,
                request.metadata(),
                now,
                false
        );
        QueueItemEntity updatedItem = queueCommandService.updateItem(updateCommand);
        activityLogService.recordItemEvent(agent, updatedItem, "completed", request.metadata());

        // Идемпотентно снимаем лок с текущего item, если он принадлежит агенту
        queueLockRepository.lockByItem(itemId).ifPresent(lock -> {
            if (lock.getOwner() != null && lock.getOwner().getId().equals(agentId)) {
                queueLockRepository.delete(lock);
                QueueItemEntity unlocked = queueItemRepository.findById(itemId)
                        .orElseThrow(() -> new EntityNotFoundException("Queue item not found"));
                unlocked.setLockedUntil(null);
                queueItemRepository.save(unlocked);
            }
        });

        if (!StringUtils.hasText(segment)) {
            throw new ApiErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "handoff.segment_missing",
                    "У очереди не задан сегмент",
                    List.of(REQUIREMENT_GLOBAL),
                    List.of(new ApiErrorDetail("segment", "empty"))
            );
        }

        List<HandoffRuleEntity> rules = handoffRuleService.findRules(segment, "completed");
        if (rules.isEmpty()) {
            throw new ApiErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "handoff.rule_missing",
                    "Не найдены правила handoff для сегмента",
                    List.of(REQUIREMENT_HANDOFF_PREFIX + segment),
                    List.of(new ApiErrorDetail("segment", segment))
            );
        }

        QueueItemEntity lastCreated = null;
        for (HandoffRuleEntity rule : rules) {
            try {
                lastCreated = createNextQueueItem(agent, updatedItem, rule, now);
            } catch (DataIntegrityViolationException ex) {
                // Идемпотентность handoff: если next уже существует по external_ref — считаем handoff выполненным
                String nextSegment = rule.getNextSegment().toLowerCase(Locale.ROOT);
                String externalRef = updatedItem.getExternalRef() + "::" + nextSegment;
                lastCreated = queueItemRepository.findByExternalRef(externalRef).orElse(null);
            }
        }
        return new TaskSubmissionResponseDto(
                updatedItem.getId(),
                "completed",
                lastCreated != null ? lastCreated.getId() : null,
                lastCreated != null ? lastCreated.getQueue().getSegment() : null
        );
    }

    private void validateOwnership(UUID agentId, QueueItemEntity item) {
        if (item.getAssignedTo() == null || item.getAssignedTo().getId() == null || !item.getAssignedTo().getId().equals(agentId)) {
            throw new ApiErrorException(
                    HttpStatus.FORBIDDEN,
                    "submission.not_owner",
                    "Задача назначена другому агенту",
                    List.of(REQUIREMENT_GLOBAL),
                    List.of(new ApiErrorDetail("assignedTo", item.getAssignedTo() == null ? "unassigned" : item.getAssignedTo().getId().toString()))
            );
        }
    }

    private QueueItemEntity createNextQueueItem(AgentEntity actor, QueueItemEntity source, HandoffRuleEntity rule, OffsetDateTime now) {
        String nextSegment = rule.getNextSegment().toLowerCase(Locale.ROOT);
        QueueEntity queue = queueRepository.findBySegmentAndStatusCode(nextSegment, "queued")
                .orElseGet(() -> queueRepository.save(QueueEntity.builder()
                        .id(UUID.randomUUID())
                        .segment(nextSegment)
                        .statusCode("queued")
                        .title(nextSegment.toUpperCase(Locale.ROOT) + " :: queued")
                        .description("Создано автоматически после submit")
                        .owner(actor)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()));

        UUID statusValueId = enumCatalogService.requireTaskStatus("queued");
        String externalRef = source.getExternalRef() + "::" + nextSegment;
        // Идемпотентность: если такой next уже существует — вернуть его
        var existingNext = queueItemRepository.findByExternalRef(externalRef);
        if (existingNext.isPresent()) {
            return existingNext.get();
        }
        QueueItemEntity nextItem = QueueItemEntity.builder()
                .id(UUID.randomUUID())
                .queue(queue)
                .externalRef(externalRef)
                .title(source.getTitle())
                .priority(source.getPriority())
                .payload(source.getPayload())
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
        queueItemRepository.save(nextItem);
        QueueItemStateEntity initialState = QueueItemStateEntity.create(nextItem, actor, "queued", statusValueId, "Handoff from " + source.getQueue().getSegment(), null, now);
        queueItemStateRepository.save(initialState);
        nextItem.applyState(initialState);
        queueItemRepository.save(nextItem);
        attachTemplatesFromRule(nextItem, rule, now);
        activityLogService.recordItemEvent(actor, nextItem, "queued", null);
        return nextItem;
    }

    private void attachTemplatesFromRule(QueueItemEntity item, HandoffRuleEntity rule, OffsetDateTime now) {
        if (rule.getTemplateCodes() == null || rule.getTemplateCodes().isBlank()) {
            return;
        }
        String[] codes = rule.getTemplateCodes().split(",");
        List<QueueItemTemplateEntity> templates = new ArrayList<>();
        for (String raw : codes) {
            String code = raw.trim();
            if (code.isEmpty()) {
                continue;
            }
            templates.add(QueueItemTemplateEntity.builder()
                    .id(UUID.randomUUID())
                    .item(item)
                    .templateCode(code)
                    .templateType(QueueItemTemplateEntity.TemplateType.REFERENCE)
                    .createdAt(now)
                    .build());
        }
        if (!templates.isEmpty()) {
            queueItemTemplateRepository.saveAll(templates);
        }
    }

    private List<TaskSubmissionRequestDto.SubmissionArtifactDto> sanitizeLinkArtifacts(List<TaskSubmissionRequestDto.SubmissionArtifactDto> artifacts) {
        if (artifacts == null) {
            return List.of();
        }
        return artifacts.stream()
                .filter(artifact -> artifact != null && StringUtils.hasText(artifact.title()) && StringUtils.hasText(artifact.url()))
                .toList();
    }

    private List<MultipartFile> sanitizeFiles(List<MultipartFile> files) {
        if (files == null) {
            return List.of();
        }
        return files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .toList();
    }
}

