package com.necpgame.workqueue.service;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.repository.QueueItemArtifactRepository;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.repository.QueueLockRepository;
import com.necpgame.workqueue.service.command.QueueItemUpdateCommand;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.StoredArtifactFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.service.validation.OpenApiSubmissionValidator;
import com.necpgame.workqueue.service.validation.SubmissionValidatorRegistry;
import com.necpgame.workqueue.web.dto.submission.TaskSubmissionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskSubmissionServiceTest {
    @Mock
    private QueueItemRepository queueItemRepository;
    @Mock
    private QueueItemStateRepository queueItemStateRepository;
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private QueueItemTemplateRepository queueItemTemplateRepository;
    @Mock
    private QueueItemArtifactRepository queueItemArtifactRepository;
    @Mock
    private QueueLockRepository queueLockRepository;
    @Mock
    private AgentDirectoryService agentDirectoryService;
    @Mock
    private QueueCommandService queueCommandService;
    @Mock
    private EnumCatalogService enumCatalogService;
    @Mock
    private ActivityLogService activityLogService;
    @Mock
    private ArtifactStorageService artifactStorageService;
    @Mock
    private HandoffRuleService handoffRuleService;

    private TaskSubmissionService service;
    private SubmissionValidatorRegistry submissionValidatorRegistry;

    private final UUID agentId = UUID.randomUUID();
    private final UUID itemId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        submissionValidatorRegistry = new SubmissionValidatorRegistry(List.of());
        service = new TaskSubmissionService(
                queueItemRepository,
                queueItemStateRepository,
                queueRepository,
                queueItemTemplateRepository,
                queueItemArtifactRepository,
                queueLockRepository,
                agentDirectoryService,
                queueCommandService,
                enumCatalogService,
                activityLogService,
                artifactStorageService,
                handoffRuleService,
                submissionValidatorRegistry
        );
    }

    @Test
    void submitWithoutArtifactsThrows() {
        AgentEntity agent = AgentEntity.builder().id(agentId).roleKey("backend-implementer").build();
        QueueEntity queue = QueueEntity.builder().id(UUID.randomUUID()).segment("backend").statusCode("in_progress").build();
        QueueItemEntity item = QueueItemEntity.builder()
                .id(itemId)
                .queue(queue)
                .title("Backend Task")
                .externalRef("BE-1")
                .priority(3)
                .payload("""
                        {"handoffPlan":{"nextSegment":"frontend"}}
                        """)
                .assignedTo(agent)
                .statusValueId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .version(0L)
                .build();
        when(agentDirectoryService.requireAgent(agentId)).thenReturn(agent);
        when(queueItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(queueItemTemplateRepository.findByItem(item)).thenReturn(List.of(QueueItemTemplateEntity.builder()
                .id(UUID.randomUUID())
                .item(item)
                .templateCode("backend-brief")
                .templateType(QueueItemTemplateEntity.TemplateType.PRIMARY)
                .sourcePath("pipeline/templates/backend-brief.yaml")
                .createdAt(OffsetDateTime.now())
                .build()));
        lenient().when(handoffRuleService.findRules("backend", "completed")).thenReturn(List.of(
                new HandoffRuleEntity(UUID.randomUUID(), "backend", null, "frontend", "frontend-brief", OffsetDateTime.now())
        ));

        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto("notes", List.of(), null);

        assertThatThrownBy(() -> service.submit(agentId, itemId, request, List.of()))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("артефакты");
    }

    @Test
    void submitCreatesNextQueueItem() {
        AgentEntity agent = AgentEntity.builder().id(agentId).roleKey("backend-implementer").build();
        QueueEntity queue = QueueEntity.builder().id(UUID.randomUUID()).segment("backend").statusCode("in_progress").title("Backend").build();
        QueueItemEntity item = QueueItemEntity.builder()
                .id(itemId)
                .queue(queue)
                .title("Backend Task")
                .externalRef("BE-1")
                .priority(4)
                .payload("""
                        {"handoffPlan":{"nextSegment":"frontend"}}
                        """)
                .assignedTo(agent)
                .statusValueId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .version(1L)
                .build();
        when(agentDirectoryService.requireAgent(agentId)).thenReturn(agent);
        when(queueItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(queueItemTemplateRepository.findByItem(item)).thenReturn(List.of(QueueItemTemplateEntity.builder()
                .id(UUID.randomUUID())
                .item(item)
                .templateCode("backend-brief")
                .templateType(QueueItemTemplateEntity.TemplateType.PRIMARY)
                .sourcePath("pipeline/templates/backend-brief.yaml")
                .createdAt(OffsetDateTime.now())
                .build()));

        QueueItemEntity updatedItem = QueueItemEntity.builder()
                .id(itemId)
                .queue(queue)
                .title("Backend Task")
                .externalRef("BE-1")
                .priority(4)
                .payload(item.getPayload())
                .assignedTo(agent)
                .statusValueId(UUID.randomUUID())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .version(2L)
                .build();
        when(queueCommandService.updateItem(any(QueueItemUpdateCommand.class))).thenReturn(updatedItem);
        when(queueRepository.findBySegmentAndStatusCode(eq("frontend"), eq("queued"))).thenReturn(Optional.empty());
        QueueEntity nextQueue = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment("frontend")
                .statusCode("queued")
                .title("FRONTEND :: queued")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .owner(agent)
                .build();
        when(queueRepository.save(any())).thenReturn(nextQueue);
        when(enumCatalogService.requireTaskStatus("queued")).thenReturn(UUID.randomUUID());
        when(artifactStorageService.store(eq(itemId), any(MultipartFile.class)))
                .thenReturn(new StoredArtifactFile("result.txt", "artifacts/result.txt", "text/plain", 10));
        when(queueItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueItemStateRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(handoffRuleService.findRules("backend", "completed")).thenReturn(List.of(
                new HandoffRuleEntity(UUID.randomUUID(), "backend", null, "frontend", "frontend-brief", OffsetDateTime.now())
        ));

        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto(
                "done",
                List.of(new TaskSubmissionRequestDto.SubmissionArtifactDto("link", "https://example.com/doc")),
                "{}"
        );
        MultipartFile file = org.mockito.Mockito.mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        var response = service.submit(agentId, itemId, request, List.of(file));

        assertThat(response.itemId()).isEqualTo(itemId);
        assertThat(response.nextSegment()).isEqualTo("frontend");
        verify(queueCommandService).updateItem(any());
        ArgumentCaptor<QueueItemEntity> newItemCaptor = ArgumentCaptor.forClass(QueueItemEntity.class);
        verify(queueItemRepository, atLeastOnce()).save(newItemCaptor.capture());
        QueueItemEntity lastSaved = newItemCaptor.getAllValues().get(newItemCaptor.getAllValues().size() - 1);
        assertThat(lastSaved.getQueue().getSegment()).isEqualTo("frontend");
    }

    @Test
    void submitFailsWhenNoHandoffRule() {
        AgentEntity agent = AgentEntity.builder().id(agentId).roleKey("backend-implementer").build();
        QueueEntity queue = QueueEntity.builder().id(UUID.randomUUID()).segment("backend").statusCode("in_progress").build();
        QueueItemEntity item = QueueItemEntity.builder()
                .id(itemId)
                .queue(queue)
                .title("Backend Task")
                .externalRef("BE-1")
                .priority(3)
                .assignedTo(agent)
                .statusValueId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .version(1L)
                .build();
        when(agentDirectoryService.requireAgent(agentId)).thenReturn(agent);
        when(queueItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(queueItemTemplateRepository.findByItem(item)).thenReturn(List.of());
        when(handoffRuleService.findRules("backend", "completed")).thenReturn(List.of());

        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto(
                "notes",
                List.of(new TaskSubmissionRequestDto.SubmissionArtifactDto("proof", "https://example.com/proof")),
                "{}"
        );

        assertThatThrownBy(() -> service.submit(agentId, itemId, request, List.of()))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("handoff");
    }

    @Test
    void submitForApiSegmentRequiresMetadata() {
        submissionValidatorRegistry = new SubmissionValidatorRegistry(List.of(new OpenApiSubmissionValidator(new ObjectMapper())));
        service = new TaskSubmissionService(
                queueItemRepository,
                queueItemStateRepository,
                queueRepository,
                queueItemTemplateRepository,
                queueItemArtifactRepository,
                queueLockRepository,
                agentDirectoryService,
                queueCommandService,
                enumCatalogService,
                activityLogService,
                artifactStorageService,
                handoffRuleService,
                submissionValidatorRegistry
        );

        AgentEntity agent = AgentEntity.builder().id(agentId).roleKey("api-task-architect").build();
        QueueEntity queue = QueueEntity.builder().id(UUID.randomUUID()).segment("api").statusCode("in_progress").build();
        QueueItemEntity item = QueueItemEntity.builder()
                .id(itemId)
                .queue(queue)
                .title("API Task")
                .externalRef("API-1")
                .priority(2)
                .assignedTo(agent)
                .statusValueId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .version(1L)
                .build();
        when(agentDirectoryService.requireAgent(agentId)).thenReturn(agent);
        when(queueItemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(queueItemTemplateRepository.findByItem(item)).thenReturn(List.of());

        TaskSubmissionRequestDto request = new TaskSubmissionRequestDto(
                "готово",
                List.of(new TaskSubmissionRequestDto.SubmissionArtifactDto("spec", "https://example.com/spec.yaml")),
                null
        );

        assertThatThrownBy(() -> service.submit(agentId, itemId, request, List.of()))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("metadata");
    }
}

