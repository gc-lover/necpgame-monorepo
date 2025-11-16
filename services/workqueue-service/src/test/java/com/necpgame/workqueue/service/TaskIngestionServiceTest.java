package com.necpgame.workqueue.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.config.WorkqueueIngestionProperties;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.TaskIngestionRequest;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.HandoffCondition;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.HandoffPlan;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.TemplateReference;
import com.necpgame.workqueue.service.model.TaskIngestionRequest.Templates;
import com.necpgame.workqueue.service.model.TaskIngestionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskIngestionServiceTest {
    @Mock
    private QueueItemRepository queueItemRepository;
    @Mock
    private QueueRepository queueRepository;
    @Mock
    private QueueItemStateRepository queueItemStateRepository;
    @Mock
    private QueueItemTemplateRepository queueItemTemplateRepository;
    @Mock
    private EnumCatalogService enumCatalogService;
    @Mock
    private ActivityLogService activityLogService;
    @Mock
    private AgentDirectoryService agentDirectoryService;

    private TaskIngestionService taskIngestionService;

    @BeforeEach
    void setUp() {
        WorkqueueIngestionProperties props = new WorkqueueIngestionProperties();
        taskIngestionService = new TaskIngestionService(
                queueItemRepository,
                queueRepository,
                queueItemStateRepository,
                queueItemTemplateRepository,
                enumCatalogService,
                activityLogService,
                agentDirectoryService,
                new ObjectMapper(),
                props,
                "../.."
        );
    }

    @Test
    void ingestCreatesQueueItemAndTemplates() {
        AgentEntity actor = AgentEntity.builder()
                .id(UUID.randomUUID())
                .roleKey("concept-director")
                .displayName("Concept Director")
                .active(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        QueueEntity queue = QueueEntity.builder()
                .id(UUID.randomUUID())
                .segment("concept")
                .statusCode("queued")
                .title("concept queued")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        when(queueItemRepository.findByExternalRef("SRC-1")).thenReturn(Optional.empty());
        when(queueRepository.findBySegmentAndStatusCode("concept", "queued")).thenReturn(Optional.of(queue));
        UUID statusId = UUID.randomUUID();
        when(enumCatalogService.requireTaskStatus(any())).thenReturn(statusId);
        when(agentDirectoryService.requireActiveByRole("concept-director")).thenReturn(actor);
        when(queueItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueItemTemplateRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(queueItemStateRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(activityLogService).recordItemEvent(any(), any(), any(), any());

        TaskIngestionRequest request = new TaskIngestionRequest(
                "SRC-1",
                "concept",
                "queued",
                3,
                "New task",
                "Summary",
                    List.of("/api/reference/templates/knowledge-entry-template"),
                new Templates(
                        List.of("concept-director-checklist"),
                        List.of("knowledge-entry"),
                        List.of(new TemplateReference("concept-canon", "2025.11", "pipeline/templates/concept-canon.yaml"))
                ),
                Map.of("foo", "bar"),
                new HandoffPlan("vision", List.of(new HandoffCondition("completed", "api")), "notes")
        );

        TaskIngestionResult result = taskIngestionService.ingest(request);

        assertThat(result.itemId()).isNotNull();
        verify(queueItemTemplateRepository).saveAll(argThat(iterable -> {
            if (iterable == null) {
                return false;
            }
            int count = 0;
            for (Object ignored : iterable) {
                count++;
            }
            return count == 3;
        }));
    }

    @Test
    void ingestFailsOnDuplicateSourceId() {
        when(queueItemRepository.findByExternalRef("SRC-1")).thenReturn(Optional.of(new com.necpgame.workqueue.domain.QueueItemEntity()));

        TaskIngestionRequest request = new TaskIngestionRequest(
                "SRC-1",
                "concept",
                "queued",
                3,
                "New task",
                "Summary",
                    List.of("/api/reference/templates/knowledge-entry-template"),
                Templates.empty(),
                Map.of(),
                new HandoffPlan("vision", List.of(new HandoffCondition("completed", "api")), null)
        );

        assertThatThrownBy(() -> taskIngestionService.ingest(request))
                .isInstanceOf(ApiErrorException.class)
                .hasMessageContaining("sourceId");
    }
}

